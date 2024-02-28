package br.com.jeiferson.rinhabackendimperative.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSource {

  private static final HikariConfig config = new HikariConfig();
  private static final HikariDataSource ds;

  static {
    config.setJdbcUrl("jdbc:postgresql://database:5432/rinhabackend");
    config.setUsername("rinhabackend");
    config.setPassword("rinhabackend");
    config.setAutoCommit(false);
    ds = new HikariDataSource(config);
  }

  public static Connection getConnection() throws SQLException {
    return ds.getConnection();
  }
}
