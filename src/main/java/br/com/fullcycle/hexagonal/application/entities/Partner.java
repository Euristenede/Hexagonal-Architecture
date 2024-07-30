package br.com.fullcycle.hexagonal.application.entities;

public class Partner {
    private PartnerId partnerId;
    private Name name;
    private Cnpj cnpj;
    private Email email;

    public Partner(final PartnerId partnerId, final String name, final String cnpj, final String email) {
        this.partnerId = partnerId;
        this.name = new Name(name);
        this.cnpj = new Cnpj(cnpj);
        this.email = new Email(email);
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
}
