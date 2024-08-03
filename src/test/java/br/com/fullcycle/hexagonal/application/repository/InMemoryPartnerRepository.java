package br.com.fullcycle.hexagonal.application.repository;

import br.com.fullcycle.hexagonal.application.domain.partner.Partner;
import br.com.fullcycle.hexagonal.application.domain.partner.PartnerId;
import br.com.fullcycle.hexagonal.application.domain.person.Cnpj;
import br.com.fullcycle.hexagonal.application.domain.person.Cpf;
import br.com.fullcycle.hexagonal.application.domain.person.Email;
import br.com.fullcycle.hexagonal.application.repositories.PartnerRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class InMemoryPartnerRepository implements PartnerRepository {

    private final Map<String, Partner> partners;
    private final Map<String, Partner> partnersByCnpj;
    private final Map<String, Partner> partnersByEmail;

    public InMemoryPartnerRepository() {
        this.partners = new HashMap<>();
        this.partnersByCnpj = new HashMap<>();
        this.partnersByEmail = new HashMap<>();
    }

    @Override
    public Optional<Partner> partnerOfId(PartnerId anId) {
        return Optional.ofNullable(this.partners.get(Objects.requireNonNull(anId).value()));
    }

    @Override
    public Optional<Partner> partnerOfCnpj(Cnpj cnpj) {
        return Optional.ofNullable(this.partnersByCnpj.get(cnpj.value()));
    }

    @Override
    public Optional<Partner> partnerOfEmail(Email email) {
        return Optional.ofNullable(this.partnersByEmail.get(email.value()));
    }

    @Override
    public Partner create(Partner partner) {
        this.partners.put(partner.getPartnerId().value(), partner);
        this.partnersByCnpj.put(partner.getCnpj().value(), partner);
        this.partnersByEmail.put(partner.getEmail().value(), partner);
        return partner;
    }

    @Override
    public Partner update(Partner partner) {
        this.partners.put(partner.getPartnerId().value(), partner);
        this.partnersByCnpj.put(partner.getCnpj().value(), partner);
        this.partnersByEmail.put(partner.getEmail().value(), partner);
        return partner;
    }

    @Override
    public void deleteAll() {
        this.partners.clear();
        this.partnersByCnpj.clear();
        this.partnersByEmail.clear();
    }
}
