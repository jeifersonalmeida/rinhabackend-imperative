package br.com.jeiferson.rinhabackendimperative.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSource {

  private static HikariConfig config = new HikariConfig();
  private static HikariDataSource ds;

  static {
//    config.setJdbcUrl("jdbc:postgresql://localhost:5432/rinhabackend");
    config.setJdbcUrl("jdbc:mysql://database:3306/rinhabackend");
    config.setUsername( "rinhabackend" );
    config.setPassword( "rinhabackend" );
    config.addDataSourceProperty( "cachePrepStmts" , "true" );
    config.addDataSourceProperty( "prepStmtCacheSize" , "250" );
    config.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );
    config.setMaximumPoolSize(20);
    ds = new HikariDataSource( config );
  }

  public static Connection getConnection() throws SQLException {
    return ds.getConnection();
  }
}
