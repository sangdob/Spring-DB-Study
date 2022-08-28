package com.jdbc;

import com.jdbc.connect.DBConnectionUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;

@SpringBootApplication
public class JdbcApplication {

	public static void main(String[] args) {
		SpringApplication.run(JdbcApplication.class, args);
		Connection connection = DBConnectionUtil.getConnection();
		System.out.println(connection);
	}

}
