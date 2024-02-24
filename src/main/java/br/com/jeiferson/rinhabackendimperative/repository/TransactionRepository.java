package br.com.jeiferson.rinhabackendimperative.repository;

import br.com.jeiferson.rinhabackendimperative.model.entity.Transaction;
import br.com.jeiferson.rinhabackendimperative.util.DataSource;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TransactionRepository {

  public List<Transaction> findLastestTransactions(Integer customerId) {
    String query = "SELECT * FROM transaction WHERE customer_id = ? ORDER BY date DESC LIMIT 10";

    List<Transaction> transactionList = new ArrayList<>();
    try (Connection conn = DataSource.getConnection();
         PreparedStatement ps = conn.prepareStatement(query)) {
      ps.setInt(1, customerId);
      ResultSet rs = ps.executeQuery();
      Transaction transaction;
      while (rs.next()) {
        transaction = new Transaction();
        transaction.setId(rs.getInt("id"));
        transaction.setValue(rs.getInt("value"));
        transaction.setType(rs.getString("type").charAt(0));
        transaction.setDescription(rs.getString("description"));
        transaction.setCustomerId(rs.getInt("customer_id"));
        transaction.setDate(rs.getString("date"));
        transactionList.add(transaction);
      }
      rs.close();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return transactionList;
  }

  public void save(Transaction transaction) {
    String query = "INSERT INTO transaction (value, type, description, date, customer_id) VALUES (?, ?, ?, ?, ?)";

    try (Connection conn = DataSource.getConnection();
         PreparedStatement ps = conn.prepareStatement(query)) {
      ps.setInt(1, transaction.getValue());
      ps.setString(2, transaction.getType().toString());
      ps.setString(3, transaction.getDescription());
      ps.setString(4, transaction.getDate());
      ps.setInt(5, transaction.getCustomerId());
      ps.execute();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
