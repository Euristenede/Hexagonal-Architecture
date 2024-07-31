package br.com.fullcycle.hexagonal.application.usecases;

import br.com.fullcycle.hexagonal.application.InMemoryCustomerRepository;
import br.com.fullcycle.hexagonal.application.InMemoryEventRepository;
import br.com.fullcycle.hexagonal.application.InMemoryTicketRepository;
import br.com.fullcycle.hexagonal.application.domain.*;
import br.com.fullcycle.hexagonal.infrastructure.jpa.entities.TicketStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SubscribeCustomerToEventUseCaseTest {

    @Test
    @DisplayName("Deve comprar um ticket de um evento")
    public void testReserveTicket() throws Exception {

        // given
        final var expectedTicketsSize = 1;
        final var aPartner = Partner.create("John Doe", "41.536.538/0001-00", "john.doe@gmail.com");
        final var anEvent = Event.create("Disney on Ice", "2021-01-01", 10, aPartner);
        final var aCustomer = Customer.create("Gabriel Doe", "123.456.789-01", "gabriel.doe@gmail.com");
        final var customerId = aCustomer.getCustomerId().value();
        final var eventId = anEvent.getEventId().value();

        final var subscribeInput =
                new SubscribeCustomerToEventUseCase.Input(eventId, customerId);

        final var customerRepository = new InMemoryCustomerRepository();
        final var eventRepository = new InMemoryEventRepository();
        final var ticketRepository = new InMemoryTicketRepository();

        customerRepository.create(aCustomer);
        eventRepository.create(anEvent);

        // when
        final var useCase = new SubscribeCustomerToEventUseCase(customerRepository, eventRepository, ticketRepository);
        final var output = useCase.execute(subscribeInput);

        // then

        Assertions.assertEquals(eventId, output.eventId());
        Assertions.assertNotNull(output.ticketId());
        Assertions.assertNotNull(output.reservationDate());
        Assertions.assertEquals(TicketStatus.PENDING.name(), output.ticketStatus());

        final var actualEvent = eventRepository.eventOfId(anEvent.getEventId());
        Assertions.assertEquals(expectedTicketsSize, actualEvent.get().allTickets().size());
    }

    @Test
    @DisplayName("Não deve comprar um ticket de um evento que não existe")
    public void testReserveTicketWithoutEvent() throws Exception {

        // given
        final var expectedError = "Event not found";
        final var aPartner = Partner.create("John Doe", "41.536.538/0001-00", "john.doe@gmail.com");
        final var anEvent = Event.create("Disney on Ice", "2021-01-01", 10, aPartner);

        final var customerId = CustomerId.unique().value();
        final var eventId = anEvent.getEventId().value();

        final var subscribeInput =
                new SubscribeCustomerToEventUseCase.Input(eventId, customerId);

        final var customerRepository = new InMemoryCustomerRepository();
        final var eventRepository = new InMemoryEventRepository();
        final var ticketRepository = new InMemoryTicketRepository();

        eventRepository.create(anEvent);

        // when
        final var useCase = new SubscribeCustomerToEventUseCase(customerRepository, eventRepository, ticketRepository);
        final var actualException = Assertions.assertThrows(Exception.class, () -> useCase.execute(subscribeInput));

        // then
        Assertions.assertEquals(expectedError, actualException.getMessage());

    }

    @Test
    @DisplayName("Não deve comprar um ticket com um cliente não existente")
    public void testReserveTicketWithoutCustomer() throws Exception {

        // given
        final var expectedError = "Event not found";
        final var aCustomer = Customer.create("Gabriel Doe", "123.456.789-01", "gabriel.doe@gmail.com");
        final var customerId = aCustomer.getCustomerId().value();
        final var eventId = EventId.unique().value();

        final var subscribeInput =
                new SubscribeCustomerToEventUseCase.Input(eventId, customerId);

        final var customerRepository = new InMemoryCustomerRepository();
        final var eventRepository = new InMemoryEventRepository();
        final var ticketRepository = new InMemoryTicketRepository();

        customerRepository.create(aCustomer);

        // when
        final var useCase = new SubscribeCustomerToEventUseCase(customerRepository, eventRepository, ticketRepository);
        final var actualException = Assertions.assertThrows(Exception.class, () -> useCase.execute(subscribeInput));

        // then
        Assertions.assertEquals(expectedError, actualException.getMessage());

    }

    @Test
    @DisplayName("Um mesmo cliente não pode comprar mais de um ticket por evento")
    public void testReserveTicketMoreThanOnce() throws Exception {

        // given
        final var expectedError = "Email already registered";
        final var aPartner = Partner.create("John Doe", "41.536.538/0001-00", "john.doe@gmail.com");
        final var anEvent = Event.create("Disney on Ice", "2021-01-01", 10, aPartner);
        final var aCustomer = Customer.create("Gabriel Doe", "123.456.789-01", "gabriel.doe@gmail.com");
        final var customerId = aCustomer.getCustomerId().value();
        final var eventId = anEvent.getEventId().value();

        final var subscribeInput =
                new SubscribeCustomerToEventUseCase.Input(eventId, customerId);

        final var customerRepository = new InMemoryCustomerRepository();
        final var eventRepository = new InMemoryEventRepository();
        final var ticketRepository = new InMemoryTicketRepository();

        final var ticket = anEvent.reserveTicket(aCustomer.getCustomerId());

        customerRepository.create(aCustomer);
        eventRepository.create(anEvent);
        ticketRepository.create(ticket);

        // when
        final var useCase = new SubscribeCustomerToEventUseCase(customerRepository, eventRepository, ticketRepository);
        final var actualException = Assertions.assertThrows(Exception.class, () -> useCase.execute(subscribeInput));

        // then
        Assertions.assertEquals(expectedError, actualException.getMessage());

    }

    @Test
    @DisplayName("Um cliente não pode comprar de um evento que não há mais cadeiras")
    public void testReserveTicketWithoutSlots() throws Exception {

        // given
        final var expectedError = "Event sold out";
        final var aPartner = Partner.create("John Doe", "41.536.538/0001-00", "john.doe@gmail.com");
        final var anEvent = Event.create("Disney on Ice", "2021-01-01", 1, aPartner);
        final var aCustomer = Customer.create("Gabriel Doe", "123.456.789-01", "gabriel.doe@gmail.com");
        final var aCustomer2 = Customer.create("Pedro Doe", "123.111.789-01", "pedro.doe@gmail.com");
        final var customerId = aCustomer.getCustomerId().value();
        final var eventId = anEvent.getEventId().value();

        final var subscribeInput =
                new SubscribeCustomerToEventUseCase.Input(eventId, customerId);

        final var customerRepository = new InMemoryCustomerRepository();
        final var eventRepository = new InMemoryEventRepository();
        final var ticketRepository = new InMemoryTicketRepository();

        final var ticket = anEvent.reserveTicket(aCustomer2.getCustomerId());

        customerRepository.create(aCustomer2);
        customerRepository.create(aCustomer);
        eventRepository.create(anEvent);
        ticketRepository.create(ticket);

        // when
        final var useCase = new SubscribeCustomerToEventUseCase(customerRepository, eventRepository, ticketRepository);
        final var actualException = Assertions.assertThrows(Exception.class, () -> useCase.execute(subscribeInput));

        // then
        Assertions.assertEquals(expectedError, actualException.getMessage());

    }

}