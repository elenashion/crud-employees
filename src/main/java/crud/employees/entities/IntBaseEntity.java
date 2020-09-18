package crud.employees.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;

@MappedSuperclass
public class IntBaseEntity {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(value= AccessType.FIELD)
    private int id;

    @Override
    public String toString() {
        return Integer.toString(id);
    }

    @Override
    public int hashCode() {
        return this.getId();
    }


    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (other == null) {
            return false;
        }

        if (!Hibernate.getClass(other).equals(Hibernate.getClass(this))) {
            return false;
        }

        IntBaseEntity castedOther = (IntBaseEntity) other;

        if (!this.isSaved() || !castedOther.isSaved()) {
            return false;
        }

        return getId() == castedOther.getId();

    }

    // Is object saved into db
    private boolean isSaved() {
        return this.id != 0;
    }
}
