package com.creditscoring.dossier;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@OpenAPIDefinition(info = @Info(title = "МС Досье", version = "1.0"))
@SpringBootApplication
@ConfigurationPropertiesScan
public class DossierApplication {

	public static void main(String[] args) {
		SpringApplication.run(DossierApplication.class, args);
	}

}
