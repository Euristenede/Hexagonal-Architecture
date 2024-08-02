package br.com.fullcycle.hexagonal.application.domain.customer;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CustomerTest {

    @Test
    @DisplayName("Deve instanciar um cliente")
    public void testCreateCustomer(){

        //given
        final var expectedCpf = "123.456.789-01";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";

        //when
        final var actualCustomer = Customer.create(expectedName, expectedCpf, expectedEmail);

        //then
        Assertions.assertNotNull(actualCustomer.getCustomerId());
        Assertions.assertEquals(expectedCpf, actualCustomer.getCpf().value());
        Assertions.assertEquals(expectedEmail, actualCustomer.getEmail().value());
        Assertions.assertEquals(expectedName, actualCustomer.getName().value());

    }

    @Test
    @DisplayName("Não deve instanciar um cliente com cpf inválido")
    public void testCreateCustomerWithInvalidCPF(){
        //given
        final var expectedError = "Invalid value for Cpf";

        //when
        final var actualError = Assertions.assertThrows(
                ValidationException.class,
                () -> Customer.create("John Doe", "12345678901", "john.doe@gmail.com")
        );

        //then
        Assertions.assertEquals(expectedError, actualError.getMessage());
    }

    @Test
    @DisplayName("Não deve instanciar um cliente com email inválido")
    public void testCreateCustomerWithInvalidEmail(){
        //given
        final var expectedError = "Invalid value for Email";

        //when
        final var actualError = Assertions.assertThrows(
                ValidationException.class,
                () -> Customer.create("John Doe", "123.456.789-01", "john.doe.gmail.com")
        );

        //then
        Assertions.assertEquals(expectedError, actualError.getMessage());
    }

    @Test
    @DisplayName("Não deve instanciar um cliente com nome inválido")
    public void testCreateCustomerWithInvalidName(){
        //given
        final var expectedError = "Invalid value for Name";

        //when
        final var actualError = Assertions.assertThrows(
                ValidationException.class,
                () -> Customer.create(null, "123.456.789-01", "john.doe@gmail.com")
        );

        //then
        Assertions.assertEquals(expectedError, actualError.getMessage());
    }

}
