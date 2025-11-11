package com.meucurriculo.testsupport;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public final class PostgresTC {
    private static final DockerImageName IMAGE = DockerImageName.parse("postgres");

    // Singleton container reused by all tests in the JVM
    public static final PostgreSQLContainer<?> INSTANCE =
            new PostgreSQLContainer<>(IMAGE)
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

    static {
        INSTANCE.start();
    }

    private PostgresTC() {}
}
