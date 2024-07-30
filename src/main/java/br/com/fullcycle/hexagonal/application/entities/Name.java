package br.com.fullcycle.hexagonal.application.entities;

public record Name(String value) {

    public Name {
        if(value == null || value.isBlank()){
            throw new IllegalArgumentException("Invalid name for Customer");
        }
    }
}
