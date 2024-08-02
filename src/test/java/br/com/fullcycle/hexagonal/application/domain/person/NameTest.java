package br.com.fullcycle.hexagonal.application.domain.person;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NameTest {

    @Test
    @DisplayName("Deve instanciar um name")
    public void testCreateName(){
        //given
        final var expectedName = "john doe";

        //when
        final var actualName = new Name(expectedName);

        //then
        Assertions.assertEquals(expectedName, actualName.value());
    }

    @Test
    @DisplayName("Não deve instanciar um name branco")
    public void testCreateNameWithBlankValue(){
        //given
        final var expectedError = "Invalid value for Name";

        //when
        final var actualError = Assertions.assertThrows(
                ValidationException.class,
                () -> new Name("")
        );

        //then
        Assertions.assertEquals(expectedError, actualError.getMessage());
    }

    @Test
    @DisplayName("Não deve instanciar um name nulo")
    public void testCreateNameWithNullValue(){
        //given
        final var expectedError = "Invalid value for Name";

        //when
        final var actualError = Assertions.assertThrows(
                ValidationException.class,
                () -> new Name(null)
        );

        //then
        Assertions.assertEquals(expectedError, actualError.getMessage());
    }

}