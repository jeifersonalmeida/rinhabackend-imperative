package br.com.jeiferson.rinhabackendimperative.model.response;

public record StatementTransactionResponse(
    Integer valor,
    Character tipo,
    String descricao,
    String realizada_em
) { }
