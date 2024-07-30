package br.com.fullcycle.hexagonal.application.usecases;

import br.com.fullcycle.hexagonal.application.InMemoryPartnerRepository;
import br.com.fullcycle.hexagonal.application.entities.Partner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;


public class GetPartnerByIdUseCaseTest {

    @Test
    @DisplayName("Deve obter um parceiro por id")
    public void testGetById() {
        //given
        final String expectedCnpj = "41.536.538/0001-00";
        final String expectedEmail = "john.doe@gmail.com";
        final String expectedName = "John Doe";

        final var partnerRepository = new InMemoryPartnerRepository();

        final var aPartner = Partner.create(expectedName, expectedCnpj, expectedEmail);
        partnerRepository.create(aPartner);

        final var expectedId = aPartner.getPartnerId().value().toString();
        final var input = new GetPartnerByIdUseCase.Input(expectedId);

        //when
        final var useCase = new GetPartnerByIdUseCase(partnerRepository);
        final var output = useCase.execute(input).get();

        //then
        Assertions.assertEquals(expectedId, output.id());
        Assertions.assertEquals(expectedCnpj, output.cnpj());
        Assertions.assertEquals(expectedEmail, output.email());
        Assertions.assertEquals(expectedName, output.name());
    }

    @Test
    @DisplayName("Deve obter vazio ao tentar recuperar um parceiro não existente por id")
    public void testGetByIdWithInvalid() {
        //given
        final var expectedId = UUID.randomUUID().toString();

        final var input = new GetPartnerByIdUseCase.Input(expectedId);

        //when
        final var partnerRepository = new InMemoryPartnerRepository();

        final var useCase = new GetPartnerByIdUseCase(partnerRepository);
        final var output = useCase.execute(input);

        //then
        Assertions.assertTrue(output.isEmpty());
    }

}
