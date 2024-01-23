package com.musalasoft.drones.Utils;

import com.musalasoft.drones.repositories.DroneRepository;
import com.musalasoft.drones.repositories.EventLogRepository;
import com.musalasoft.drones.repositories.MedicationRepository;
import com.musalasoft.drones.services.DroneService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@AutoConfigureWebTestClient
@Testcontainers
@DirtiesContext
@ActiveProfiles("test")
@ContextConfiguration(initializers = AbstractTest.Initializer.class)
public abstract class AbstractTest {

    @Autowired
    protected WebTestClient webTestClient;
    @Autowired
    protected DroneRepository droneRepository;
    @Autowired
    protected MedicationRepository medicationRepository;
    @Autowired
    protected EventLogRepository eventLogRepository;
    @Autowired
    protected DroneService droneService;

    @Container
    public static MongoDBContainer DATABASE = new MongoDBContainer("mongo:latest")
            .withExposedPorts(27017);

    static {
        DATABASE.start();
    }

    @BeforeEach
    void clearDatabase() {
        droneRepository.deleteAll().block();
        medicationRepository.deleteAll().block();
        eventLogRepository.deleteAll().block();
    }

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of("MONGO_PORT=" + DATABASE.getMappedPort(27017))
                    .applyTo(applicationContext);
        }
    }

}
