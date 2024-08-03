package br.com.fullcycle.hexagonal.application.usecases.partner;

import br.com.fullcycle.hexagonal.IntegrationTest;
import br.com.fullcycle.hexagonal.application.domain.partner.Partner;
import br.com.fullcycle.hexagonal.application.repositories.PartnerRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;


public class GetPartnerByIdUseCaseIT extends IntegrationTest {

    @Autowired
    private GetPartnerByIdUseCase useCase;

    @Autowired
    private PartnerRepository partnerRepository;

    @BeforeEach
    void setUp() {
        partnerRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve obter um parceiro por id")
    public void testGetById() {
        //given
        final String expectedCnpj = "41.536.538/0001-00";
        final String expectedEmail = "john.doe@gmail.com";
        final String expectedName = "John Doe";

        final var partner = createPartner(expectedCnpj, expectedEmail, expectedName);

        final var input = new GetPartnerByIdUseCase.Input(partner.getPartnerId().value());

        //when
        final var output = useCase.execute(input).get();

        //then
        Assertions.assertEquals(partner.getPartnerId().value(), output.id());
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
        final var output = useCase.execute(input);

        //then
        Assertions.assertTrue(output.isEmpty());
    }

    private Partner createPartner(String cnpj, String email, String name) {
        return partnerRepository.create(Partner.create(name, cnpj, email));
    }

}
