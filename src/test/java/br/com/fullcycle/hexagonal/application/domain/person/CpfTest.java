package br.com.fullcycle.hexagonal.application.domain.person;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CpfTest {

    @Test
    @DisplayName("Deve instanciar um cpf")
    public void testCreateCpf(){
        //given
        final var expectedCpf = "123.456.789-01";

        //when
        final var actualCpf = new Cpf(expectedCpf);

        //then
        Assertions.assertEquals(expectedCpf, actualCpf.value());
    }

    @Test
    @DisplayName("Não deve instanciar um cpf inválido")
    public void testCreateCpfWithInvalidValue(){
        //given
        final var expectedError = "Invalid value for Cpf";

        //when
        final var actualError = Assertions.assertThrows(
                ValidationException.class,
                () -> new Cpf("12345678954")
        );

        //then
        Assertions.assertEquals(expectedError, actualError.getMessage());
    }

    @Test
    @DisplayName("Não deve instanciar um cpf branco")
    public void testCreateCpfWithBlankValue(){
        //given
        final var expectedError = "Invalid value for Cpf";

        //when
        final var actualError = Assertions.assertThrows(
                ValidationException.class,
                () -> new Cpf("")
        );

        //then
        Assertions.assertEquals(expectedError, actualError.getMessage());
    }

    @Test
    @DisplayName("Não deve instanciar um cpf nulo")
    public void testCreateCpfWithNullValue(){
        //given
        final var expectedError = "Invalid value for Cpf";

        //when
        final var actualError = Assertions.assertThrows(
                ValidationException.class,
                () -> new Cpf(null)
        );

        //then
        Assertions.assertEquals(expectedError, actualError.getMessage());
    }
}