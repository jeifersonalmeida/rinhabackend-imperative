package br.com.jeiferson.rinhabackendimperative.controller;

import br.com.jeiferson.rinhabackendimperative.model.dto.CreateTransactionDTO;
import br.com.jeiferson.rinhabackendimperative.model.response.StatementResponse;
import br.com.jeiferson.rinhabackendimperative.model.response.TransactionResponse;
import br.com.jeiferson.rinhabackendimperative.service.CustomerService;
import br.com.jeiferson.rinhabackendimperative.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/clientes")
public class CustomerController {

  @Autowired
  private CustomerService customerService;

  @Autowired
  private TransactionService transactionService;

  @PostMapping("/{id}/transacoes")
  public ResponseEntity<TransactionResponse> create(
      @PathVariable String id,
      @RequestBody CreateTransactionDTO createTransactionDTO
  ) throws SQLException {
    return ResponseEntity.ok().body(transactionService.create(Integer.parseInt(id), createTransactionDTO));
  }

  @GetMapping("/{id}/extrato")
  public ResponseEntity<StatementResponse> statement(
      @PathVariable String id
  ) throws SQLException {
    return ResponseEntity.ok().body(customerService.getStatement(Integer.parseInt(id)));
  }
}
