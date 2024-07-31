package br.com.fullcycle.hexagonal.application.domain;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.infrastructure.jpa.entities.TicketStatus;

import java.time.Instant;

public class Ticket {

    private final TicketId ticketId;
    private CustomerId customerId;
    private EventId eventId;
    private TicketStatus status;
    private Instant paidAt;
    private Instant reservedAt;

    public Ticket(
            final TicketId ticketId,
            final CustomerId customerId,
            final EventId eventId,
            final TicketStatus status,
            final Instant paidAt,
            final Instant reservedAt
    ) {
        this.ticketId = ticketId;
        this.setCustomerId(customerId);
        this.setEventId(eventId);
        this.setStatus(status);
        this.setPaidAt(paidAt);
        this.setReservedAt(reservedAt);
    }

    public static Ticket create(
            final CustomerId customerId,
            final EventId eventId
    ) {
        return new Ticket(TicketId.unique(), customerId, eventId, TicketStatus.PENDING, null, Instant.now());
    }

    public TicketId getTicketId() {
        return ticketId;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public EventId getEventId() {
        return eventId;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public Instant getPaidAt() {
        return paidAt;
    }

    public Instant getReservedAt() {
        return reservedAt;
    }

    private void setCustomerId(final CustomerId customerId) {
        if(customerId == null){
            throw new ValidationException("Invalid customerId for Ticket");
        }
        this.customerId = customerId;
    }

    private void setEventId(final EventId eventId) {
        if(customerId == null){
            throw new ValidationException("Invalid eventId for Ticket");
        }
        this.eventId = eventId;
    }

    private void setStatus(final TicketStatus status) {
        if(status == null){
            throw new ValidationException("Invalid status for Ticket");
        }
        this.status = status;
    }

    private void setPaidAt(final Instant paidAt) {
        this.paidAt = paidAt;
    }

    private void setReservedAt(final Instant reservedAt) {
        if(reservedAt == null){
            throw new ValidationException("Invalid reservedAt for Ticket");
        }
        this.reservedAt = reservedAt;
    }
}
