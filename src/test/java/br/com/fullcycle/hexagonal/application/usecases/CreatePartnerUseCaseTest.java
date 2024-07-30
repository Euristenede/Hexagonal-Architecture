package br.com.fullcycle.hexagonal.application.usecases;

import br.com.fullcycle.hexagonal.application.InMemoryPartnerRepository;
import br.com.fullcycle.hexagonal.application.entities.Partner;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CreatePartnerUseCaseTest {

    @Test
    @DisplayName("Deve criar um parceiro")
    public void testCreatePartner() {
        // given
        final String expectedCnpj = "41.536.538/0001-00";
        final String expectedEmail = "john.doe@gmail.com";
        final String expectedName = "John Doe";

        final var createInput = new CreatePartnerUseCase.Input(expectedCnpj, expectedEmail, expectedName);

        // when
        final var partnerRepository = new InMemoryPartnerRepository();
        final var useCase = new CreatePartnerUseCase(partnerRepository);
        final var output = useCase.execute(createInput);

        // then
        Assertions.assertNotNull(output.id());
        Assertions.assertEquals(expectedCnpj, output.cnpj());
        Assertions.assertEquals(expectedEmail, output.email());
        Assertions.assertEquals(expectedName, output.name());

    }

    @Test
    @DisplayName("Não deve cadastrar um parceiro com CNPJ duplicado")
    public void testCreateWithDuplicatedCNPJShouldFail() throws Exception {
        // given
        final String expectedCnpj = "41.536.538/0001-00";
        final String expectedEmail = "john.doe@gmail.com";
        final String expectedName = "John Doe";
        final var expectedError = "Partner already exists";

        final var partnerRepository = new InMemoryPartnerRepository();

        final var aPartner = Partner.create(expectedName, expectedCnpj, "john.doe2@gmail.com");
        partnerRepository.create(aPartner);

        final var createInput = new CreatePartnerUseCase.Input(expectedCnpj, expectedEmail, expectedName);

        // when
        final var useCase = new CreatePartnerUseCase(partnerRepository);
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(createInput));

        //then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }

    @Test
    @DisplayName("Não deve cadastrar um parceiro com e-mail duplicado")
    public void testCreateWithDuplicatedEmailShouldFail() throws Exception {
        // given
        final String expectedCnpj = "41.536.538/0001-00";
        final String expectedEmail = "john.doe@gmail.com";
        final String expectedName = "John Doe";
        final var expectedError = "Partner already exists";

        final var partnerRepository = new InMemoryPartnerRepository();

        final var aPartner = Partner.create(expectedName, "41.536.538/0002-00", expectedEmail);
        partnerRepository.create(aPartner);

        final var createInput = new CreatePartnerUseCase.Input(expectedCnpj, expectedEmail, expectedName);

        // when
        final var useCase = new CreatePartnerUseCase(partnerRepository);
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(createInput));

        //then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }

}
