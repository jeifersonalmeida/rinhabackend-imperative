package br.com.jeiferson.rinhabackendimperative.service;

import br.com.jeiferson.rinhabackendimperative.exception.NotFoundException;
import br.com.jeiferson.rinhabackendimperative.exception.UnprocessableEntityException;
import br.com.jeiferson.rinhabackendimperative.model.dto.CreateTransactionDTO;
import br.com.jeiferson.rinhabackendimperative.model.entity.Customer;
import br.com.jeiferson.rinhabackendimperative.model.entity.Transaction;
import br.com.jeiferson.rinhabackendimperative.model.response.TransactionResponse;
import br.com.jeiferson.rinhabackendimperative.repository.CustomerRepository;
import br.com.jeiferson.rinhabackendimperative.repository.TransactionRepository;
import br.com.jeiferson.rinhabackendimperative.util.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TransactionService {

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private TransactionRepository transactionRepository;

  public TransactionResponse create(Integer customerId, CreateTransactionDTO createTransactionDTO) throws SQLException {
    try (Connection conn = DataSource.getConnection()) {
      conn.setAutoCommit(false);

      Optional<Customer> optionalCustomer = customerRepository.findByIdForUpdate(conn, customerId);
      if (optionalCustomer.isEmpty()) {
        throw new NotFoundException("CustomerNotFoundException");
      }

      Customer customer = optionalCustomer.get();
      if (!isValidTransaction(createTransactionDTO, customer)) {
        throw new UnprocessableEntityException("BalanceLessThanLimitException");
      }

      if (createTransactionDTO.tipo() == 'c') {
        customer.setBalance(customer.getBalance() + createTransactionDTO.valor().intValue());
      } else {
        customer.setBalance(customer.getBalance() - createTransactionDTO.valor().intValue());
      }

      customerRepository.update(conn, customer);

      createTransaction(conn, createTransactionDTO, customerId);
      conn.commit();

      return buildResponse(customer);
    }
  }

  private TransactionResponse buildResponse(Customer customer) {
    return new TransactionResponse(customer.getAccountLimit(), customer.getBalance());
  }

  private void createTransaction(Connection conn, CreateTransactionDTO createTransactionDTO, Integer customerID) {
    Transaction transaction = new Transaction();
    transaction.setValue(createTransactionDTO.valor().intValue());
    transaction.setType(createTransactionDTO.tipo());
    transaction.setDescription(createTransactionDTO.descricao());
    transaction.setDate(LocalDateTime.now().toString());
    transaction.setCustomerId(customerID);
    transactionRepository.save(conn, transaction);
  }

  private boolean isValidTransaction(CreateTransactionDTO createTransactionDTO, Customer customer) {
    if (createTransactionDTO.valor().intValue() != createTransactionDTO.valor()) {
      return false;
    } else if (!createTransactionDTO.tipo().equals('c') && !createTransactionDTO.tipo().equals('d')) {
      return false;
    } else if (createTransactionDTO.valor() < 0) {
      return false;
    } else if (createTransactionDTO.descricao() == null || createTransactionDTO.descricao().isEmpty() || createTransactionDTO.descricao().length() > 10) {
      return false;
    } else if (createTransactionDTO.tipo().equals('d') && ((customer.getBalance() - createTransactionDTO.valor()) < (customer.getAccountLimit() * -1))) {
      return false;
    }
    return true;
  }
}
