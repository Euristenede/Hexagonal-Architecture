package br.com.fullcycle.hexagonal.application.entities;

public record Cnpj(String value) {

        public Cnpj {
            if(value == null || value.isBlank() || !value.matches("^\\d{2}\\.\\d{3}\\.\\d{3}\\/\\d{4}\\-\\d{2}$")){
                throw new IllegalArgumentException("Invalid cnpj for Partner");
            }
        }
}
