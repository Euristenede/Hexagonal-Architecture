package br.com.fullcycle.hexagonal.infrastructure.jpa.entities;

import br.com.fullcycle.hexagonal.application.domain.customer.Customer;
import br.com.fullcycle.hexagonal.application.domain.customer.CustomerId;
import br.com.fullcycle.hexagonal.application.domain.event.EventId;
import br.com.fullcycle.hexagonal.application.domain.event.ticket.Ticket;
import br.com.fullcycle.hexagonal.application.domain.event.ticket.TicketId;
import br.com.fullcycle.hexagonal.application.domain.event.ticket.TicketStatus;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity(name = "Ticket")
@Table(name = "tickets")
public class TicketEntity {

    @Id
    private UUID id;

    private UUID customerId;

    private UUID eventId;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    private Instant paidAt;

    private Instant reservedAt;

    public TicketEntity() {
    }

    public TicketEntity(
            final UUID id,
            final UUID customerId,
            final UUID eventId,
            final TicketStatus status,
            final Instant paidAt,
            final Instant reservedAt) {
        this.id = id;
        this.customerId = customerId;
        this.eventId = eventId;
        this.status = status;
        this.paidAt = paidAt;
        this.reservedAt = reservedAt;
    }

    public Ticket toTicket() {
        return new Ticket(
                TicketId.with(this.getId().toString()),
                CustomerId.with(this.getCustomerId().toString()),
                EventId.with(this.getEventId().toString()),
                this.getStatus(),
                this.getPaidAt(),
                this.getReservedAt()
        );
    }

    public static TicketEntity of(final Ticket ticket) {
        return new TicketEntity(
                UUID.fromString(ticket.getTicketId().value()),
                UUID.fromString(ticket.getCustomerId().value()),
                UUID.fromString(ticket.getEventId().value()),
                ticket.getStatus(),
                ticket.getPaidAt(),
                ticket.getReservedAt()
        );
    }

    public UUID getId() {
        return id;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public Instant getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(Instant paidAt) {
        this.paidAt = paidAt;
    }

    public Instant getReservedAt() {
        return reservedAt;
    }

    public void setReservedAt(Instant reservedAt) {
        this.reservedAt = reservedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicketEntity that = (TicketEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
