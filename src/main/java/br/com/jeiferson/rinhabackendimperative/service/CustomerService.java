package br.com.jeiferson.rinhabackendimperative.service;

import br.com.jeiferson.rinhabackendimperative.exception.NotFoundException;
import br.com.jeiferson.rinhabackendimperative.model.entity.Customer;
import br.com.jeiferson.rinhabackendimperative.model.entity.Transaction;
import br.com.jeiferson.rinhabackendimperative.model.response.StatementResponse;
import br.com.jeiferson.rinhabackendimperative.model.response.StatementInfoResponse;
import br.com.jeiferson.rinhabackendimperative.model.response.StatementTransactionResponse;
import br.com.jeiferson.rinhabackendimperative.repository.CustomerRepository;
import br.com.jeiferson.rinhabackendimperative.repository.TransactionRepository;
import br.com.jeiferson.rinhabackendimperative.util.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private TransactionRepository transactionRepository;

  public StatementResponse getStatement(Integer customerId) throws SQLException {
    Connection conn = DataSource.getConnection();
    Optional<Customer> optionalCustomer = customerRepository.findById(conn, customerId);
    if (optionalCustomer.isEmpty()) {
      throw new NotFoundException("CustomerNotFoundException");
    }

    List<Transaction> transactionList = transactionRepository.findLastestTransactions(conn, customerId);
    conn.commit();
    conn.close();
    return buildResponse(optionalCustomer.get(), transactionList);
  }

  private StatementResponse buildResponse(Customer customer, List<Transaction> transactionList) {
    List<StatementTransactionResponse> transactionResponses = transactionList.stream()
        .map(tran -> new StatementTransactionResponse(
            tran.getValue(),
            tran.getType(),
            tran.getDescription(),
            tran.getDate()
        )).toList();
    return new StatementResponse(
        new StatementInfoResponse(
            customer.getBalance(),
            LocalDateTime.now().toString(),
            customer.getAccountLimit()
        ),
        transactionResponses
    );
  }
}
