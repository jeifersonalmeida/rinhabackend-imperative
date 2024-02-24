package br.com.jeiferson.rinhabackendimperative.service;

import br.com.jeiferson.rinhabackendimperative.exception.NotFoundException;
import br.com.jeiferson.rinhabackendimperative.exception.UnprocessableEntityException;
import br.com.jeiferson.rinhabackendimperative.model.dto.CreateTransactionDTO;
import br.com.jeiferson.rinhabackendimperative.model.entity.Customer;
import br.com.jeiferson.rinhabackendimperative.model.entity.Transaction;
import br.com.jeiferson.rinhabackendimperative.model.response.TransactionResponse;
import br.com.jeiferson.rinhabackendimperative.repository.CustomerRepository;
import br.com.jeiferson.rinhabackendimperative.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TransactionService {

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private TransactionRepository transactionRepository;

  public TransactionResponse create(Integer customerId, CreateTransactionDTO createTransactionDTO) {
    Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
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

    customerRepository.update(customer);

    createTransaction(createTransactionDTO, customerId);

    return buildResponse(customer);
  }

  private TransactionResponse buildResponse(Customer customer) {
    return new TransactionResponse(customer.getAccountLimit(), customer.getBalance());
  }

  private void createTransaction(CreateTransactionDTO createTransactionDTO, Integer customerID) {
    Transaction transaction = new Transaction();
    transaction.setValue(createTransactionDTO.valor().intValue());
    transaction.setType(createTransactionDTO.tipo());
    transaction.setDescription(createTransactionDTO.descricao());
    transaction.setDate(LocalDateTime.now().toString());
    transaction.setCustomerId(customerID);
    transactionRepository.save(transaction);
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
