package br.com.jeiferson.rinhabackendimperative.model.response;

public record StatementInfoResponse(
    Integer total,
    String data_extrato,
    Integer limite
) {
}
