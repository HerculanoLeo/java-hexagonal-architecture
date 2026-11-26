package com.lodh8.starter.cadastros.infra.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.mail.autoconfigure.MailProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.nio.charset.Charset;
import java.util.Properties;

/**
 * Reaplica {@code spring.mail.*} no refresh (Modelo C) sem reiniciar a JVM.
 */
@Configuration
@ConditionalOnClass(JavaMailSender.class)
@EnableConfigurationProperties(MailProperties.class)
public class RefreshableMailConfiguration {

    @Bean
    @Primary
    @RefreshScope
    JavaMailSender javaMailSender(MailProperties properties) {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        applyProperties(sender, properties);
        return sender;
    }

    private static void applyProperties(JavaMailSenderImpl sender, MailProperties properties) {
        sender.setHost(properties.getHost());
        if (properties.getPort() != null) {
            sender.setPort(properties.getPort());
        }
        sender.setUsername(properties.getUsername());
        sender.setPassword(properties.getPassword());
        sender.setProtocol(properties.getProtocol());
        Charset encoding = properties.getDefaultEncoding();
        sender.setDefaultEncoding(encoding.name());
        if (!properties.getProperties().isEmpty()) {
            Properties javaMailProps = new Properties();
            javaMailProps.putAll(properties.getProperties());
            sender.setJavaMailProperties(javaMailProps);
        }
    }
}
