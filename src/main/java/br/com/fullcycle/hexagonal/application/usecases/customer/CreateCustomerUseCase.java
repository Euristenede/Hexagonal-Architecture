package br.com.fullcycle.hexagonal.application.usecases.customer;

import br.com.fullcycle.hexagonal.application.domain.person.Cpf;
import br.com.fullcycle.hexagonal.application.domain.person.Email;
import br.com.fullcycle.hexagonal.application.usecases.UseCase;
import br.com.fullcycle.hexagonal.application.domain.customer.Customer;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.repositories.CustomerRepository;

public class CreateCustomerUseCase extends UseCase<CreateCustomerUseCase.Input, CreateCustomerUseCase.Output> {

    private final CustomerRepository customerRepository;

    public CreateCustomerUseCase(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Output execute(final Input input) {
        if (customerRepository.customerOfCpf(new Cpf(input.cpf)).isPresent()) {
            throw new ValidationException("Customer already exists");
        }
        if (customerRepository.customerOfEmail(new Email(input.email)).isPresent()) {
            throw new ValidationException("Customer already exists");
        }

        var customer = customerRepository.create(Customer.create(input.name, input.cpf, input.email));

        return new Output(
                customer.getCustomerId().value(),
                customer.getCpf().value(),
                customer.getEmail().value(),
                customer.getName().value()
        );
    }

    public record Input(String cpf, String email, String name) {}
    public record Output(String id, String cpf, String email, String name) {}

}
