package org.launchcode.codingevents.models;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Objects;

// This annotation indicates that this class is a superclass for JPA entities.Your use of annotations such as @MappedSuperclass, @Id, and @GeneratedValue are commonly associated with JPA (Java Persistence API),
// which is often used in Java applications for object-relational mapping (ORM) to manage relational data in databases.
@MappedSuperclass
public abstract class AbstractEntity {
    @Id
    @GeneratedValue
    int id;

    public int getId() {
        return id;
    }

    // Override equals method to compare entities by their id
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventCategory entity = (EventCategory) o;
        return id == entity.id;
    }

    // Override hashCode method to generate hash based on the id
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
