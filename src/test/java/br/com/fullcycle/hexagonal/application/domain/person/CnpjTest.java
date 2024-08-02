package br.com.fullcycle.hexagonal.application.domain.person;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CnpjTest {

    @Test
    @DisplayName("Deve instanciar um cnpj")
    public void testCreateCnpj(){
        //given
        final String expectedCnpj = "41.536.538/0001-00";

        //when
        final var actualCnpj = new Cnpj(expectedCnpj);

        //then
        Assertions.assertEquals(expectedCnpj, actualCnpj.value());
    }

    @Test
    @DisplayName("Não deve instanciar um cnpj inválido")
    public void testCreateCnpjWithInvalidValue(){
        //given
        final var expectedError = "Invalid value for Cnpj";

        //when
        final var actualError = Assertions.assertThrows(
                ValidationException.class,
                () -> new Cnpj("41536538000100")
        );

        //then
        Assertions.assertEquals(expectedError, actualError.getMessage());
    }

    @Test
    @DisplayName("Não deve instanciar um cnpj branco")
    public void testCreateCnpjWithBlankValue(){
        //given
        final var expectedError = "Invalid value for Cnpj";

        //when
        final var actualError = Assertions.assertThrows(
                ValidationException.class,
                () -> new Cnpj("")
        );

        //then
        Assertions.assertEquals(expectedError, actualError.getMessage());
    }

    @Test
    @DisplayName("Não deve instanciar um cnpj nulo")
    public void testCreateCnpjWithNullValue(){
        //given
        final var expectedError = "Invalid value for Cnpj";

        //when
        final var actualError = Assertions.assertThrows(
                ValidationException.class,
                () -> new Cnpj(null)
        );

        //then
        Assertions.assertEquals(expectedError, actualError.getMessage());
    }

}