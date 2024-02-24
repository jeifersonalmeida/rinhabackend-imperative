package br.com.jeiferson.rinhabackendimperative.model.dto;

public record CreateTransactionDTO(
    Double valor,
    Character tipo,
    String descricao
) { }
