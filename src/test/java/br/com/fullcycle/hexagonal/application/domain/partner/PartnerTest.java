package br.com.fullcycle.hexagonal.application.domain.partner;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PartnerTest {

    @Test
    @DisplayName("Deve instanciar um parceiro")
    public void testCreatePartner(){

        //given
        final var expectedCnpj = "41.536.538/0001-00";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";

        //when
        final var actualPartner = Partner.create(expectedName, expectedCnpj, expectedEmail);

        //then
        Assertions.assertNotNull(actualPartner.getPartnerId());
        Assertions.assertEquals(expectedCnpj, actualPartner.getCnpj().value());
        Assertions.assertEquals(expectedEmail, actualPartner.getEmail().value());
        Assertions.assertEquals(expectedName, actualPartner.getName().value());

    }

    @Test
    @DisplayName("Não deve instanciar um parceiro com cnpj inválido")
    public void testCreatePartnerWithInvalidCPF(){
        //given
        final var expectedError = "Invalid value for Cnpj";

        //when
        final var actualError = Assertions.assertThrows(
                ValidationException.class,
                () -> Partner.create("John Doe", "41536538000100", "john.doe@gmail.com")
        );

        //then
        Assertions.assertEquals(expectedError, actualError.getMessage());
    }

    @Test
    @DisplayName("Não deve instanciar um parceiro com email inválido")
    public void testCreatePartnerWithInvalidEmail(){
        //given
        final var expectedError = "Invalid value for Email";

        //when
        final var actualError = Assertions.assertThrows(
                ValidationException.class,
                () -> Partner.create("John Doe", "41.536.538/0001-00", "john.doe.gmail.com")
        );

        //then
        Assertions.assertEquals(expectedError, actualError.getMessage());
    }

    @Test
    @DisplayName("Não deve instanciar um parceiro com nome inválido")
    public void testCreatePartnerWithInvalidName(){
        //given
        final var expectedError = "Invalid value for Name";

        //when
        final var actualError = Assertions.assertThrows(
                ValidationException.class,
                () -> Partner.create(null, "41.536.538/0001-00", "john.doe@gmail.com")
        );

        //then
        Assertions.assertEquals(expectedError, actualError.getMessage());
    }

}
