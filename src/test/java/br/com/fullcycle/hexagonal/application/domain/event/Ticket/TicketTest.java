package br.com.fullcycle.hexagonal.application.domain.event.Ticket;

import br.com.fullcycle.hexagonal.application.domain.customer.Customer;
import br.com.fullcycle.hexagonal.application.domain.event.Event;
import br.com.fullcycle.hexagonal.application.domain.event.ticket.Ticket;
import br.com.fullcycle.hexagonal.application.domain.partner.Partner;
import br.com.fullcycle.hexagonal.infrastructure.jpa.entities.TicketStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeFormatter;

public class TicketTest {

    @Test
    @DisplayName("Deve criar um ticket")
    public void testCreateTicket() throws Exception {

        // given
        final var aPartner = Partner.create("John Doe", "41.536.538/0001-00", "john.doe@gmail.com");
        final var aCustomer = Customer.create("John Doe", "123.456.789-01", "john.doe@gmail.com");
        final var anEvent = Event.create("Disney on Ice", "2021-01-01", 10, aPartner);
        final var expectedTicketStatus = TicketStatus.PENDING;
        final var expectedEventId = anEvent.getEventId();
        final var expectedCustomerId = aCustomer.getCustomerId();

        // when
        final var actualTicket = Ticket.create(aCustomer.getCustomerId(), anEvent.getEventId());

        // then
        Assertions.assertNotNull(actualTicket.getTicketId());
        Assertions.assertNotNull(actualTicket.getReservedAt());
        Assertions.assertNull(actualTicket.getPaidAt());
        Assertions.assertEquals(expectedEventId, actualTicket.getEventId());
        Assertions.assertEquals(expectedCustomerId, actualTicket.getCustomerId());
        Assertions.assertEquals(expectedTicketStatus, actualTicket.getStatus());

    }
}
