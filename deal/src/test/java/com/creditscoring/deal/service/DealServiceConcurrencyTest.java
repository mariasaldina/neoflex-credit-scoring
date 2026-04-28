package com.creditscoring.deal.service;

import com.creditscoring.deal.dto.request.LoanOfferDto;
import com.creditscoring.deal.entity.Client;
import com.creditscoring.deal.entity.Statement;
import com.creditscoring.deal.exception.ApplicationStatusConflictException;
import com.creditscoring.deal.mapper.*;
import com.creditscoring.deal.repository.ClientRepository;
import com.creditscoring.deal.repository.StatementRepository;
import com.creditscoring.deal.service.hook.TestLockHookImpl;
import com.creditscoring.deal.utils.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Arrays;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles({"test", "concurrency"})
public class DealServiceConcurrencyTest {

    @Autowired
    private DealService dealService;
    @Autowired
    private TestLockHookImpl lockHook;

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private StatementRepository statementRepository;

    @MockitoBean
    private KafkaProducerService producer;

    @Test
    void selectOfferConcurrentTest() throws InterruptedException, ExecutionException {
        Client client = clientRepository.save(TestDataFactory.createClient().build());
        Statement statement = this.statementRepository.save(new Statement(client));
        LoanOfferDto appliedOffer = TestDataFactory.createLoanOfferDto()
                .statementId(statement.getStatementId())
                .build();

        lockHook.reset();

        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            Future<Void> firstCall = executor.submit(() -> {
                this.dealService.selectOffer(appliedOffer);
                return null;
            });

            lockHook.firstLocked.await();

            Future<Void> secondCall = executor.submit(() -> {
                this.dealService.selectOffer(appliedOffer);
                return null;
            });

            Thread.sleep(5000);

            assertFalse(secondCall.isDone());

            lockHook.allowFirstToFinish.countDown();

            firstCall.get();

            ExecutionException ex =
                    assertThrows(ExecutionException.class, secondCall::get);
            assertInstanceOf(
                    ApplicationStatusConflictException.class,
                    ex.getCause()
            );
        }
    }
}
