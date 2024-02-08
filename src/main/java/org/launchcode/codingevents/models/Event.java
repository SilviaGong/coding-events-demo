package org.launchcode.codingevents.models;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

// Entity annotation indicates that this class is a JPA entity and will be mapped to a database table
@Entity
public class Event extends AbstractEntity {

    // Validation annotations ensure that these fields meet certain criteria when validating input data

    // Name of the event
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;


    @OneToOne(cascade = CascadeType.ALL)
    @Valid
    @NotNull
    private EventDetails eventDetails;

    // Many-to-One relationship with EventCategory
    @ManyToOne
    @NotNull(message = "Category is required")
    private EventCategory eventCategory;

    @ManyToMany
    private final List<Tag> tags = new ArrayList<>();

    // Constructors

    // Constructor with parameters


    public Event(String name, EventDetails eventDetails, EventCategory eventCategory) {
        this.name = name;
        this.eventDetails = eventDetails;
        this.eventCategory = eventCategory;
    }

    // Default constructor
    public Event() {
    }

    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public EventCategory getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(EventCategory eventCategory) {
        this.eventCategory = eventCategory;
    }

    public EventDetails getEventDetails() {
        return eventDetails;
    }

    public void setEventDetails(EventDetails eventDetails) {
        this.eventDetails = eventDetails;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void addTag(Tag tag){
        this.tags.add(tag);

    }

    // toString method for debugging and logging purposes

    @Override
    public String toString() {
        return "Event{" +
                "name='" + name + '\'' +
                ", eventCategory=" + eventCategory +
                '}';
    }
}
