package br.com.fullcycle.hexagonal.infrastructure.jpa.entities;

import br.com.fullcycle.hexagonal.application.domain.customer.CustomerId;
import br.com.fullcycle.hexagonal.application.domain.event.EventId;
import br.com.fullcycle.hexagonal.application.domain.event.EventTicket;
import br.com.fullcycle.hexagonal.application.domain.event.ticket.TicketId;
import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

@Entity(name = "EventTicket")
@Table(name = "events_tickets")
public class EventTicketEntity {

    @Id
    private UUID ticketId;

    private UUID customerId;

    private Integer ordering;

    @ManyToOne(fetch = FetchType.LAZY)
    private EventEntity event;

    public EventTicketEntity() {
    }

    public EventTicketEntity(
            final UUID ticketId,
            final UUID customerId,
            final Integer ordering,
            final EventEntity event) {
        this.ticketId = ticketId;
        this.customerId = customerId;
        this.ordering = ordering;
        this.event = event;
    }

    public static EventTicketEntity of(final EventEntity eventEntity, final EventTicket ev) {
        return new EventTicketEntity(
                UUID.fromString(ev.getTicketId().value()),
                UUID.fromString(ev.getCustomerId().value()),
                ev.getOrdering(),
                eventEntity
        );
    }

    public EventTicket toEventTicket() {
        return new EventTicket(
                TicketId.with(this.getTicketId().toString()),
                EventId.with(this.getEvent().getId().toString()),
                CustomerId.with(this.getCustomerId().toString()),
                this.ordering
        );
    }

    public UUID getTicketId() {
        return ticketId;
    }

    public void setTicketId(UUID ticketId) {
        this.ticketId = ticketId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public Integer getOrdering() {
        return ordering;
    }

    public void setOrdering(Integer ordering) {
        this.ordering = ordering;
    }

    public EventEntity getEvent() {
        return event;
    }

    public void setEvent(EventEntity event) {
        this.event = event;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventTicketEntity that = (EventTicketEntity) o;
        return Objects.equals(ticketId, that.ticketId)
                && Objects.equals(customerId, that.customerId)
                && Objects.equals(ordering, that.ordering)
                && Objects.equals(event, that.event);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticketId, customerId, ordering, event);
    }
}
