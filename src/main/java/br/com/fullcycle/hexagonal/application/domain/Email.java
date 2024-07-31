package br.com.fullcycle.hexagonal.application.domain;

public record Email(String value) {

    public Email{
        if(value == null || value.isBlank() || !value.matches("^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$")){
            throw new IllegalArgumentException("Invalid email for Partner");
        }
    }

}
