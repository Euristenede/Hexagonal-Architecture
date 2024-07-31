package br.com.fullcycle.hexagonal.application.domain;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;

public class Partner {
    private final PartnerId partnerId;
    private Name name;
    private Cnpj cnpj;
    private Email email;

    public Partner(final PartnerId partnerId, final String name, final String cnpj, final String email) {
        if(partnerId == null){
            throw new ValidationException("Invalid partnerId for Customer");
        }

        this.partnerId = partnerId;
        this.setName(name);
        this.setCnpj(cnpj);
        this.setEmail(email);
    }

    public static Partner create(final String name, final String cnpj, final String email){
        return new Partner(PartnerId.unique(), name, cnpj, email);
    }

    public PartnerId getPartnerId() {
        return partnerId;
    }

    public Name getName() {
        return name;
    }

    public Cnpj getCnpj() {
        return cnpj;
    }

    public Email getEmail() {
        return email;
    }

    private void setName(String name) {
        this.name = new Name(name);
    }

    private void setCnpj(String cnpj) {
        this.cnpj = new Cnpj(cnpj);
    }

    private void setEmail(String email) {
        this.email = new Email(email);
    }
}
