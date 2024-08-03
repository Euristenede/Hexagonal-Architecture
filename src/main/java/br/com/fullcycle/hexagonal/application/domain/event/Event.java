package br.com.fullcycle.hexagonal.application.domain.event;

import br.com.fullcycle.hexagonal.application.domain.person.Name;
import br.com.fullcycle.hexagonal.application.domain.event.ticket.Ticket;
import br.com.fullcycle.hexagonal.application.domain.customer.CustomerId;
import br.com.fullcycle.hexagonal.application.domain.partner.Partner;
import br.com.fullcycle.hexagonal.application.domain.partner.PartnerId;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Event {

    private static final int ONE = 1;
    private final EventId eventId;
    private Name name;
    private LocalDate date;
    private Integer totalSpots;
    private PartnerId partnerId;
    private Set<EventTicket> tickets;

    public Event(
            final EventId eventId,
            final String name,
            final String date,
            final Integer totalSpots,
            final PartnerId partnerId,
            final Set<EventTicket> tickets) {
        this(eventId, tickets);
        this.setName(name);
        this.setDate(date);
        this.setTotalSpots(totalSpots);
        this.setPartnerId(partnerId);
    }

    private Event(final EventId eventId, final Set<EventTicket> tickets){
        if(eventId == null){
            throw new ValidationException("Invalid eventId for Event");
        }
        this.eventId = eventId;
        this.tickets = tickets != null ? tickets : new HashSet<>(0);
    }

    public static Event create(
            final String name,
            final String date,
            final Integer totalSpots,
            final Partner partner) {
        return new Event(EventId.unique(), name, date, totalSpots, partner.getPartnerId(), null);
    }

    public static Event restore(
            final String id,
            final String name,
            final String date,
            final int totalSpots,
            final String partnerId,
            final Set<EventTicket> collect
    ) {
        return new Event(EventId.with(id), name, date, totalSpots, PartnerId.with(partnerId), collect);
    }

    public Ticket reserveTicket(final CustomerId customerId){
        this.allTickets().stream()
                .filter(it -> Objects.equals(it.getCustomerId(), customerId))
                .findFirst()
                .ifPresent(it -> {
                    throw new ValidationException("Email already registered");
                });
        if (getTotalSpots() < allTickets().size() + ONE) {
            throw new ValidationException("Event sold out");
        }

        final var newTicket = Ticket.create(customerId, getEventId());
        this.tickets
                .add(new EventTicket(newTicket.getTicketId(), getEventId(), customerId, allTickets().size() + ONE));
        return newTicket;
    }

    public EventId getEventId() {
        return eventId;
    }

    public Name getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public Integer getTotalSpots() {
        return totalSpots;
    }

    public PartnerId getPartnerId() {
        return partnerId;
    }

    public Set<EventTicket> allTickets(){
        return Collections.unmodifiableSet(tickets);
    }

    private void setName(final String name) {
        this.name = new Name(name);
    }

    private void setDate(final String date) {
        if(date == null){
            throw new ValidationException("Invalid date for Event");
        }
        try {
            this.date = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
        }catch (RuntimeException ex){
            throw new ValidationException("Invalid date for Event");
        }
    }

    private void setTotalSpots(final Integer totalSpots) {
        if(totalSpots == null){
            throw new ValidationException("Invalid totalSpots for Event");
        }
        this.totalSpots = totalSpots;
    }

    private void setPartnerId(final PartnerId partnerId) {
        if(partnerId == null){
            throw new ValidationException("Invalid partnerId for Event");
        }
        this.partnerId = partnerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(eventId, event.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(eventId);
    }
}
