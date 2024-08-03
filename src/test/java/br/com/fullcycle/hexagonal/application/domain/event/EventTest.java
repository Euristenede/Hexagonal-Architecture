package br.com.fullcycle.hexagonal.application.domain.event;

import br.com.fullcycle.hexagonal.application.domain.customer.Customer;
import br.com.fullcycle.hexagonal.application.domain.partner.Partner;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.domain.event.ticket.TicketStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeFormatter;

public class EventTest {

    @Test
    @DisplayName("Deve criar um evento")
    public void testCreateEvent() throws Exception {

        // given
        final var aPartner = Partner.create("John Doe", "41.536.538/0001-00", "john.doe@gmail.com");
        final var expectedDate = "2021-01-01";
        final var expectedName = "Disney on Ice";
        final var expectedTotalSpots = 10;
        final var expectedPartnerId = aPartner.getPartnerId().value();
        final var expectedTickets = 0;

        // when
        final var actualEvent = Event.create(expectedName, expectedDate, expectedTotalSpots, aPartner);

        // then
        Assertions.assertNotNull(actualEvent.getEventId());
        Assertions.assertEquals(expectedDate, actualEvent.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
        Assertions.assertEquals(expectedName, actualEvent.getName().value());
        Assertions.assertEquals(expectedTotalSpots, actualEvent.getTotalSpots());
        Assertions.assertEquals(expectedPartnerId, actualEvent.getPartnerId().value());
        Assertions.assertEquals(expectedTickets, actualEvent.allTickets().size());
    }

    @Test
    @DisplayName("Não deve criar um evento com nome inválido")
    public void testCreateEventWithInvalidName() throws Exception {

        // given
        final var expectedError = "Invalid value for Name";
        final var aPartner = Partner.create("John Doe", "41.536.538/0001-00", "john.doe@gmail.com");

        //when
        final var actualError = Assertions.assertThrows(
                ValidationException.class,
                () -> Event.create(null, "2021-01-01", 10, aPartner)
        );

        //then
        Assertions.assertEquals(expectedError, actualError.getMessage());
    }

    @Test
    @DisplayName("Não deve criar um evento com data inválida")
    public void testCreateEventWithInvalidDate() throws Exception {

        // given
        final var expectedError = "Invalid date for Event";
        final var aPartner = Partner.create("John Doe", "41.536.538/0001-00", "john.doe@gmail.com");

        //when
        final var actualError = Assertions.assertThrows(
                ValidationException.class,
                () -> Event.create("John Doe", "20210101", 10, aPartner)
        );

        //then
        Assertions.assertEquals(expectedError, actualError.getMessage());
    }

    @Test
    @DisplayName("Não deve criar um evento com total de lugares inválido")
    public void testCreateEventWithInvalidTotalSpots() throws Exception {

        // given
        final var expectedError = "Invalid totalSpots for Event";
        final var aPartner = Partner.create("John Doe", "41.536.538/0001-00", "john.doe@gmail.com");

        //when
        final var actualError = Assertions.assertThrows(
                ValidationException.class,
                () -> Event.create("John Doe", "2021-01-01", null, aPartner)
        );

        //then
        Assertions.assertEquals(expectedError, actualError.getMessage());
    }

    @Test
    @DisplayName("Deve reservar um ticket quando é possível")
    public void testReserveTicket() throws Exception {

        // given
        final var aPartner = Partner.create("John Doe", "41.536.538/0001-00", "john.doe@gmail.com");
        final var aCustomer = Customer.create("John Doe", "123.456.789-01", "john.doe@gmail.com");
        final var expectedCustomerId = aCustomer.getCustomerId();
        final var expectedDate = "2021-01-01";
        final var expectedName = "Disney on Ice";
        final var expectedTotalSpots = 10;
        final var expectedPartnerId = aPartner.getPartnerId().value();
        final var expectedTickets = 1;
        final var expectedTicketOrder = 1;
        final var actualEvent = Event.create(expectedName, expectedDate, expectedTotalSpots, aPartner);
        final var expectedEventId = actualEvent.getEventId();
        final var expectedTicketStatus = TicketStatus.PENDING;

        // when
        final var actualTicket = actualEvent.reserveTicket(expectedCustomerId);

        // then
        Assertions.assertNotNull(actualTicket.getTicketId());
        Assertions.assertNotNull(actualTicket.getReservedAt());
        Assertions.assertNull(actualTicket.getPaidAt());
        Assertions.assertEquals(expectedEventId, actualTicket.getEventId());
        Assertions.assertEquals(expectedCustomerId, actualTicket.getCustomerId());
        Assertions.assertEquals(expectedTicketStatus, actualTicket.getStatus());

        Assertions.assertEquals(expectedDate, actualEvent.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
        Assertions.assertEquals(expectedName, actualEvent.getName().value());
        Assertions.assertEquals(expectedTotalSpots, actualEvent.getTotalSpots());
        Assertions.assertEquals(expectedPartnerId, actualEvent.getPartnerId().value());
        Assertions.assertEquals(expectedTickets, actualEvent.allTickets().size());

        final var actualEventTicket = actualEvent.allTickets().iterator().next();
        Assertions.assertEquals(expectedTicketOrder, actualEventTicket.getOrdering());
        Assertions.assertEquals(expectedEventId, actualEventTicket.getEventId());
        Assertions.assertEquals(expectedCustomerId, actualEventTicket.getCustomerId());
        Assertions.assertEquals(actualTicket.getTicketId(), actualEventTicket.getTicketId());
    }

    @Test
    @DisplayName("Não deve reservar um ticket quando o evento está esgotado")
    public void testReserveTicketWhenEventIsSoldOut() throws Exception {

        // given
        final var aPartner = Partner.create("John Doe", "41.536.538/0001-00", "john.doe@gmail.com");
        final var aCustomer = Customer.create("John Doe", "123.456.789-01", "john.doe@gmail.com");
        final var aCustomer2 = Customer.create("John2 Doe", "122.456.789-01", "john2.doe@gmail.com");
        final var expectedCustomerId = aCustomer.getCustomerId();
        final var expectedTotalSpots = 1;
        final var actualEvent = Event.create("Disney on Ice", "2021-01-01", expectedTotalSpots, aPartner);
        final var expectedError = "Event sold out";

        actualEvent.reserveTicket(expectedCustomerId);

        // when
        final var actualError = Assertions.assertThrows(
                ValidationException.class,
                () -> actualEvent.reserveTicket(aCustomer2.getCustomerId())
        );

        // then
        Assertions.assertEquals(expectedError, actualError.getMessage());
    }

    @Test
    @DisplayName("Não deve reservar dois tickets para um mesmo cliente")
    public void testReserveTwoTicketsForTheSameClient() throws Exception {

        // given
        final var aPartner = Partner.create("John Doe", "41.536.538/0001-00", "john.doe@gmail.com");
        final var aCustomer = Customer.create("John Doe", "123.456.789-01", "john.doe@gmail.com");
        final var expectedCustomerId = aCustomer.getCustomerId();
        final var expectedTotalSpots = 1;
        final var expectedError = "Email already registered";

        final var actualEvent = Event.create("Disney on Ice", "2021-01-01", expectedTotalSpots, aPartner);

        actualEvent.reserveTicket(expectedCustomerId);

        // when
        final var actualError = Assertions.assertThrows(
                ValidationException.class,
                () -> actualEvent.reserveTicket(aCustomer.getCustomerId())
        );

        // then
        Assertions.assertEquals(expectedError, actualError.getMessage());
    }
}
