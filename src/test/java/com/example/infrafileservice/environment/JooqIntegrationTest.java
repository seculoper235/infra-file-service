package com.example.infrafileservice.environment;

import org.springframework.boot.test.autoconfigure.jooq.JooqTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@JooqTest
public class JooqIntegrationTest {
}
