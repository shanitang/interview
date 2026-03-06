package com.apple.meridian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration;

/**
 * Entry point for the Meridian API server.
 *
 * <p>Cassandra auto-configuration is excluded because this server uses an in-memory
 * store ({@code ConcurrentHashMap} inside each manager) — no real Cassandra cluster is
 * required. The Spring Data Cassandra annotations on the model classes exist purely as
 * documentation of the production table/column schema; they have no runtime effect here.</p>
 */
@SpringBootApplication(exclude = {
    CassandraAutoConfiguration.class,
    CassandraDataAutoConfiguration.class
})
public class MeridianApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeridianApplication.class, args);
    }
}
