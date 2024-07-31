package br.com.fullcycle.hexagonal.application.domain;

public record Cpf(String value) {

    public Cpf {
        if(value == null || value.isBlank() || !value.matches("^\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}$")){
            throw new IllegalArgumentException("Invalid cpf for Customer");
        }
    }
}
