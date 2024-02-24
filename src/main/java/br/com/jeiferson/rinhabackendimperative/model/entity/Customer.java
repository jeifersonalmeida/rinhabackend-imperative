package br.com.jeiferson.rinhabackendimperative.model.entity;

public class Customer {

  Integer id;
  Integer balance;
  Integer accountLimit;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public int getBalance() {
    return balance;
  }

  public void setBalance(Integer balance) {
    this.balance = balance;
  }

  public int getAccountLimit() {
    return accountLimit;
  }

  public void setAccountLimit(Integer accountLimit) {
    this.accountLimit = accountLimit;
  }

}
