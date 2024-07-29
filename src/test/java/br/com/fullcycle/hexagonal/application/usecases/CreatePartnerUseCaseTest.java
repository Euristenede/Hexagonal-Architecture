package br.com.fullcycle.hexagonal.application.usecases;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.infrastructure.models.Partner;
import br.com.fullcycle.hexagonal.infrastructure.services.PartnerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CreatePartnerUseCaseTest {

    @Test
    @DisplayName("Deve criar um parceiro")
    public void testCreatePartner() {
        // given
        final String expectedCnpj = "41536538000100";
        final String expectedEmail = "john.doe@gmail.com";
        final String expectedName = "John Doe";

        final var createInput = new CreatePartnerUseCase.Input(expectedCnpj, expectedEmail, expectedName);

        // when
        final var partnerService = Mockito.mock(PartnerService.class);

        when(partnerService.findByCnpj(expectedCnpj)).thenReturn(Optional.empty());
        when(partnerService.findByEmail(expectedEmail)).thenReturn(Optional.empty());
        when(partnerService.save(any())).then(a -> {
            var partner = a.getArgument(0, Partner.class);
            partner.setId(UUID.randomUUID().getMostSignificantBits());
            return partner;
        });

        final var useCase = new CreatePartnerUseCase(partnerService);
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
        final String expectedCnpj = "41536538000100";
        final String expectedEmail = "john.doe@gmail.com";
        final String expectedName = "John Doe";
        final var expectedError = "Partner already exists";

        final var createInput = new CreatePartnerUseCase.Input(expectedCnpj, expectedEmail, expectedName);

        final var partner = new Partner();
        partner.setId(UUID.randomUUID().getMostSignificantBits());
        partner.setCnpj(expectedCnpj);
        partner.setEmail(expectedEmail);
        partner.setName(expectedName);

        // when
        final var partnerService = Mockito.mock(PartnerService.class);

        when(partnerService.findByCnpj(expectedCnpj)).thenReturn(Optional.of(partner));

        final var useCase = new CreatePartnerUseCase(partnerService);
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(createInput));

        //then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }

    @Test
    @DisplayName("Não deve cadastrar um parceiro com e-mail duplicado")
    public void testCreateWithDuplicatedEmailShouldFail() throws Exception {
        // given
        final String expectedCnpj = "41536538000100";
        final String expectedEmail = "john.doe@gmail.com";
        final String expectedName = "John Doe";
        final var expectedError = "Partner already exists";

        final var createInput = new CreatePartnerUseCase.Input(expectedCnpj, expectedEmail, expectedName);

        final var partner = new Partner();
        partner.setId(UUID.randomUUID().getMostSignificantBits());
        partner.setCnpj(expectedCnpj);
        partner.setEmail(expectedEmail);
        partner.setName(expectedName);

        // when
        final var partnerService = Mockito.mock(PartnerService.class);

        when(partnerService.findByEmail(expectedEmail)).thenReturn(Optional.of(partner));

        final var useCase = new CreatePartnerUseCase(partnerService);
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(createInput));

        //then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }

}
