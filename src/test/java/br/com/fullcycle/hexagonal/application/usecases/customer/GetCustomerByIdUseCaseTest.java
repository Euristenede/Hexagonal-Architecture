package br.com.fullcycle.hexagonal.application.usecases.customer;

import br.com.fullcycle.hexagonal.application.repository.InMemoryCustomerRepository;
import br.com.fullcycle.hexagonal.application.domain.customer.Customer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;


class GetCustomerByIdUseCaseTest {

    @Test
    @DisplayName("Deve obter um cliente por id")
    public void testGetById() {
        //given
        final var expectedCpf = "123.456.789-01";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";

        final var aCustomer = Customer.create(expectedName, expectedCpf, expectedEmail);

        final var customerRepository = new InMemoryCustomerRepository();

        customerRepository.create(aCustomer);
        final var expectedId = aCustomer.getCustomerId().value();
        final var input = new GetCustomerByIdUseCase.Input(expectedId);

        //when
        final var useCase = new GetCustomerByIdUseCase(customerRepository);
        final var output = useCase.execute(input).get();

        //then
        Assertions.assertEquals(expectedId, output.id());
        Assertions.assertEquals(expectedCpf, output.cpf());
        Assertions.assertEquals(expectedEmail, output.email());
        Assertions.assertEquals(expectedName, output.name());
    }

    @Test
    @DisplayName("Deve obter vazio ao tentar recuperar um cliente não existente por id")
    public void testGetByIdWithInvalid() {
        //given
        final var expectedId = UUID.randomUUID().toString();

        final var input = new GetCustomerByIdUseCase.Input(expectedId);

        //when
        final var customerRepository = new InMemoryCustomerRepository();

        final var useCase = new GetCustomerByIdUseCase(customerRepository);
        final var output = useCase.execute(input);

        //then
        Assertions.assertTrue(output.isEmpty());
    }

}