package org.example.Config;

import org.example.Models.Pokedex;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.Properties;

public class HibernateConfig {

    private static final SessionFactory SESSION_FACTORY = buildSessionFactory();

    private HibernateConfig() {
    }

    private static SessionFactory buildSessionFactory() {
        StandardServiceRegistry registry = null;
        try {
            Properties settings = new Properties();
            settings.put("hibernate.connection.driver_class", "org.postgresql.Driver");
            settings.put("hibernate.connection.url", envOrDefault(
                    "POKEMON_DB_URL", "jdbc:postgresql://localhost:5432/pokemon"));
            settings.put("hibernate.connection.username", envOrDefault("POKEMON_DB_USER", "postgres"));
            settings.put("hibernate.connection.password", envOrDefault("POKEMON_DB_PASSWORD", ""));
            settings.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL10Dialect");
            settings.put("hibernate.hbm2ddl.auto", "none");
            settings.put("hibernate.show_sql", "true");

            registry = new StandardServiceRegistryBuilder()
                    .applySettings(settings)
                    .build();
            return new org.hibernate.boot.MetadataSources(registry)
                    .addAnnotatedClass(Pokedex.class)
                    .buildMetadata()
                    .buildSessionFactory();
        } catch (Exception e) {
            if (registry != null) {
                StandardServiceRegistryBuilder.destroy(registry);
            }
            throw new ExceptionInInitializerError("No se pudo inicializar Hibernate: " + e.getMessage());
        }
    }

    private static String envOrDefault(String name, String defaultValue) {
        String value = System.getenv(name);
        return value == null || value.trim().isEmpty() ? defaultValue : value;
    }

    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }

    public static void shutdown() {
        SESSION_FACTORY.close();
    }

}