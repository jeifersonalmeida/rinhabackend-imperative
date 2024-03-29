package br.com.jeiferson.rinhabackendimperative.repository;

import br.com.jeiferson.rinhabackendimperative.model.entity.Customer;
import br.com.jeiferson.rinhabackendimperative.util.DataSource;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class CustomerRepository {

  private final String QUERY = "SELECT * FROM customer WHERE id = ?";
  private final String QUERY_FOR_UPDATE = "SELECT * FROM customer WHERE id = ? FOR UPDATE";

  public Optional<Customer> findById(Connection conn, Integer customerId) {
    return findById(conn, customerId, QUERY);
  }

  public Optional<Customer> findByIdForUpdate(Connection conn, Integer customerId) {
    return findById(conn, customerId, QUERY_FOR_UPDATE);
  }

  private Optional<Customer> findById(Connection conn, Integer customerId, String query) {
    try (PreparedStatement ps = conn.prepareStatement(query)) {
      ps.setInt(1, customerId);
      ResultSet rs = ps.executeQuery();

      Customer customer = null;
      while (rs.next()) {
        customer = new Customer();
        customer.setId(rs.getInt("id"));
        customer.setBalance(rs.getInt("balance"));
        customer.setAccountLimit(rs.getInt("account_limit"));
      }
      rs.close();
      return customer != null ? Optional.of(customer) : Optional.empty();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void update(Connection conn, Customer customer) {
    String query = "UPDATE customer SET balance = ?, account_limit = ? WHERE id = ?";

    try (PreparedStatement ps = conn.prepareStatement(query)) {
      ps.setInt(1, customer.getBalance());
      ps.setInt(2, customer.getAccountLimit());
      ps.setInt(3, customer.getId());
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

}
