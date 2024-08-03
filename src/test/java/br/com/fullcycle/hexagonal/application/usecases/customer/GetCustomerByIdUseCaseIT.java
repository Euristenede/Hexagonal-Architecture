package br.com.fullcycle.hexagonal.application.usecases.customer;

import br.com.fullcycle.hexagonal.IntegrationTest;
import br.com.fullcycle.hexagonal.application.domain.customer.Customer;
import br.com.fullcycle.hexagonal.application.repositories.CustomerRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

class GetCustomerByIdUseCaseIT extends IntegrationTest {

    @Autowired
    private GetCustomerByIdUseCase useCase;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve obter um cliente por id")
    public void testGetById() {
        //given
        final var expectedCpf = "123.456.789-01";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";

        final var customer = createCustomer(expectedCpf, expectedEmail, expectedName);

        final var input = new GetCustomerByIdUseCase.Input(customer.getCustomerId().value());

        //when
        final var output = useCase.execute(input).get();

        //then
        Assertions.assertEquals(customer.getCustomerId().value(), output.id());
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
        final var output = useCase.execute(input);

        //then
        Assertions.assertTrue(output.isEmpty());
    }

    private Customer createCustomer(final String cpf, final String email, final String name) {
        return customerRepository.create(Customer.create(name, cpf, email));
    }

}