package br.com.fullcycle.hexagonal.application.usecases.event;

import br.com.fullcycle.hexagonal.application.usecases.UseCase;
import br.com.fullcycle.hexagonal.application.domain.event.Event;
import br.com.fullcycle.hexagonal.application.domain.partner.PartnerId;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.repositories.EventRepository;
import br.com.fullcycle.hexagonal.application.repositories.PartnerRepository;

import java.util.Objects;

public class CreateEventUseCase extends UseCase<CreateEventUseCase.Input, CreateEventUseCase.Output>{

    private final EventRepository eventRepository;

    private final PartnerRepository partnerRepository;

    public CreateEventUseCase(final EventRepository eventRepository, final PartnerRepository partnerRepository) {
        this.eventRepository = Objects.requireNonNull(eventRepository);
        this.partnerRepository = Objects.requireNonNull(partnerRepository);
    }

    @Override
    public Output execute(final Input input) {

        final var aPartner = partnerRepository.partnerOfId(PartnerId.with(input.partnerId))
                .orElseThrow(() -> new ValidationException("Partner not found"));

        final var anEvent = eventRepository.create(Event.create(input.name, input.date, input.totalSpots, aPartner));

        return new Output(
                anEvent.getEventId().value(),
                input.date,
                anEvent.getName().value(),
                input.totalSpots,
                anEvent.getPartnerId().value()
        );
    }

    public record Input(String date, String name, String partnerId, Integer totalSpots) {}

    public record Output(String id, String date, String name, Integer totalSpots, String partnerId) {}
}
