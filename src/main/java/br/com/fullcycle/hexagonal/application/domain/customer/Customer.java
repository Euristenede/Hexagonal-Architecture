package br.com.fullcycle.hexagonal.application.domain.customer;

import br.com.fullcycle.hexagonal.application.domain.person.Cpf;
import br.com.fullcycle.hexagonal.application.domain.person.Email;
import br.com.fullcycle.hexagonal.application.domain.person.Name;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;

public class Customer {
    private final CustomerId customerId;
    private Name name;
    private Cpf cpf;
    private Email email;

    public Customer(final CustomerId customerId, final String name, final String cpf, final String email) {
        if(customerId == null){
            throw new ValidationException("Invalid customerId for Customer");
        }

        this.customerId = customerId;
        this.setName(name);
        this.setCpf(cpf);
        this.setEmail(email);
    }

    public static Customer create(final String name, final String cpf, final String email){
        return new Customer(CustomerId.unique(), name, cpf, email);
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public Name getName() {
        return name;
    }

    public Cpf getCpf() {
        return cpf;
    }

    public Email getEmail() {
        return email;
    }

    private void setName(String name) {
        this.name = new Name(name);
    }

    private void setCpf(String cpf) {
        this.cpf = new Cpf(cpf);
    }

    private void setEmail(String email) {
        this.email = new Email(email);
    }


}
