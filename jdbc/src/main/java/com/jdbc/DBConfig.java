package com.jdbc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;

import javax.sql.DataSource;

//@Configuration
public class DBConfig {

    DBConfig() {

    }
//    @Bean
//    PlatformTransactionManager getDataSource() {
//        return new PlatformTransactionManager(new TransactionDefinition());
//    }
}
