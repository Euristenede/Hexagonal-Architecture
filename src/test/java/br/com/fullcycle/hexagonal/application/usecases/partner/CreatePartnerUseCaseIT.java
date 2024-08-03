package br.com.fullcycle.hexagonal.application.usecases.partner;

import br.com.fullcycle.hexagonal.IntegrationTest;
import br.com.fullcycle.hexagonal.application.domain.partner.Partner;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.repositories.PartnerRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;


public class CreatePartnerUseCaseIT extends IntegrationTest {

    @Autowired
    private CreatePartnerUseCase useCase;

    @Autowired
    private PartnerRepository partnerRepository;

    @BeforeEach
    void setUp() {
        partnerRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve criar um parceiro")
    public void testCreatePartner() {
        // given
        final String expectedCnpj = "41.536.538/0001-00";
        final String expectedEmail = "john.doe@gmail.com";
        final String expectedName = "John Doe";

        final var createInput = new CreatePartnerUseCase.Input(expectedCnpj, expectedEmail, expectedName);

        // when
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

        createPartner(expectedCnpj, expectedEmail, expectedName);

        final var createInput = new CreatePartnerUseCase.Input(expectedCnpj, expectedEmail, expectedName);

        // when
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

        createPartner( expectedCnpj, expectedEmail, expectedName);

        final var createInput = new CreatePartnerUseCase.Input(expectedCnpj, expectedEmail, expectedName);

        // when
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(createInput));

        //then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }

    private Partner createPartner(String cnpj, String email, String name) {
        return partnerRepository.create(Partner.create(name, cnpj, email));
    }

}
