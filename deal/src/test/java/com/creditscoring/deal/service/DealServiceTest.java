package com.creditscoring.deal.service;

import com.creditscoring.deal.dto.calculator.response.CreditDto;
import com.creditscoring.deal.dto.request.FinishRegistrationRequestDto;
import com.creditscoring.deal.dto.request.LoanOfferDto;
import com.creditscoring.deal.dto.request.LoanStatementRequestDto;
import com.creditscoring.deal.entity.Client;
import com.creditscoring.deal.entity.Credit;
import com.creditscoring.deal.entity.Statement;
import com.creditscoring.deal.enums.ApplicationStatus;
import com.creditscoring.deal.enums.ChangeType;
import com.creditscoring.deal.json.StatusHistory;
import com.creditscoring.deal.mapper.*;
import com.creditscoring.deal.repository.ClientRepository;
import com.creditscoring.deal.repository.StatementRepository;
import com.creditscoring.deal.utils.TestDataFactory;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class DealServiceTest {

    @Autowired
    private DealService dealService;

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private StatementRepository statementRepository;

    @Autowired
    private ClientMapper clientMapper;
    @Autowired
    private PassportMapper passportMapper;
    @Autowired
    private OfferMapper offerMapper;
    @Autowired
    private CreditMapper creditMapper;
    @Autowired
    private ScoringDataMapper scoringDataMapper;

    @MockitoBean
    private CalculatorRestClient calculatorRestClient;

    private void assertClientEquals(
            Client actual,
            Client expected
    ) {
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(
                        "clientId",
                        "passport.passportId",
                        "employment.employmentId"
                )
                .isEqualTo(expected);
    }

    private void assertStatementStatuses(
            Statement statement,
            ApplicationStatus... historyStatuses
    ) {
        assertThat(statement.getStatus())
                .isEqualTo(Arrays.stream(historyStatuses).toList().getLast());

        assertThat(statement.getStatusHistory())
                .extracting(StatusHistory::status)
                .containsExactly(historyStatuses);
    }

    private void assertCreditEquals(
            Credit actual,
            CreditDto expected
    ) {
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(
                        "creditId"
                )
                .isEqualTo(creditMapper.toCreditEntity(expected));
    }

    @Test
    void saveStatementTest() {
        LoanStatementRequestDto dto = TestDataFactory.createLoanStatementDto().build();
        when(calculatorRestClient.getOffers(dto)).thenReturn(List.of());

        this.dealService.saveStatement(dto);

        verify(calculatorRestClient).getOffers(dto);

        Statement statement = statementRepository.findAll().getFirst();

        assertClientEquals(statement.getClient(), TestDataFactory.createClient().build());
        assertStatementStatuses(
                statement,
                ApplicationStatus.PREAPPROVAL
        );
    }

    @Test
    void selectOfferTest() {
        Client client = clientRepository.save(TestDataFactory.createClient().build());
        Statement statement = this.statementRepository.save(new Statement(client));
        LoanOfferDto appliedOffer = TestDataFactory.createLoanOfferDto()
                .statementId(statement.getStatementId())
                .build();

        this.dealService.selectOffer(appliedOffer);

        Statement statementAfterUpdate = statementRepository.findAll().getFirst();

        assertThat(statementAfterUpdate.getAppliedOffer())
                .isEqualTo(offerMapper.toAppliedOfferJson(appliedOffer));

        assertStatementStatuses(
                statementAfterUpdate,
                ApplicationStatus.PREAPPROVAL,
                ApplicationStatus.APPROVED
        );
    }

    private Statement createApprovedStatement() {
        Client client = clientRepository.save(TestDataFactory.createClient().build());
        Statement statement = this.statementRepository.save(new Statement(client));
        LoanOfferDto appliedOffer = TestDataFactory.createLoanOfferDto()
                .statementId(statement.getStatementId())
                .build();

        statement.setStatus(ApplicationStatus.APPROVED);
        statement.setAppliedOffer(offerMapper.toAppliedOfferJson(appliedOffer));
        statement.getStatusHistory().add(new StatusHistory(
                ApplicationStatus.APPROVED,
                ChangeType.AUTOMATIC
        ));

        return statement;
    }

    @Test
    void calculateCreditTest_success() {
        Statement statement = createApprovedStatement();

        CreditDto creditDto = TestDataFactory.createCreditDto().build();
        when(calculatorRestClient.getCredit(any())).thenReturn(creditDto);

        FinishRegistrationRequestDto finishDto = TestDataFactory.createFinishRegistrationDto().build();

        this.dealService.calculateCredit(finishDto, statement.getStatementId());

        verify(calculatorRestClient).getCredit(any());

        assertCreditEquals(statement.getCredit(), creditDto);

        Client expectedClient = TestDataFactory.createClient().build();
        clientMapper.updateClientEntity(finishDto, expectedClient);
        passportMapper.updatePassportEntity(finishDto, expectedClient.getPassport());
        assertClientEquals(statement.getClient(), expectedClient);

        assertStatementStatuses(
                statement,
                ApplicationStatus.PREAPPROVAL,
                ApplicationStatus.APPROVED,
                ApplicationStatus.CC_APPROVED
        );
    }

    @Test
    void calculateCreditTest_failure() {
        Statement statement = createApprovedStatement();

        when(calculatorRestClient.getCredit(any()))
                .thenThrow(new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY));

        FinishRegistrationRequestDto finishDto = TestDataFactory.createFinishRegistrationDto().build();

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                dealService.calculateCredit(finishDto, statement.getStatementId())
        );

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, ex.getStatusCode());

        verify(calculatorRestClient).getCredit(any());

        Client expectedClient = TestDataFactory.createClient().build();
        clientMapper.updateClientEntity(finishDto, expectedClient);
        passportMapper.updatePassportEntity(finishDto, expectedClient.getPassport());
        assertClientEquals(statement.getClient(), expectedClient);

        assertStatementStatuses(
                statement,
                ApplicationStatus.PREAPPROVAL,
                ApplicationStatus.APPROVED,
                ApplicationStatus.CC_DENIED
        );
    }
}
