package br.com.jeiferson.rinhabackendimperative.service;

import br.com.jeiferson.rinhabackendimperative.exception.NotFoundException;
import br.com.jeiferson.rinhabackendimperative.model.entity.Customer;
import br.com.jeiferson.rinhabackendimperative.model.entity.Transaction;
import br.com.jeiferson.rinhabackendimperative.model.response.StatementResponse;
import br.com.jeiferson.rinhabackendimperative.model.response.StatementInfoResponse;
import br.com.jeiferson.rinhabackendimperative.model.response.StatementTransactionResponse;
import br.com.jeiferson.rinhabackendimperative.repository.CustomerRepository;
import br.com.jeiferson.rinhabackendimperative.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private TransactionRepository transactionRepository;

  public StatementResponse getStatement(Integer customerId) {
    Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
    if (optionalCustomer.isEmpty()) {
      throw new NotFoundException("CustomerNotFoundException");
    }

    List<Transaction> transactionList = transactionRepository.findLastestTransactions(customerId);
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
