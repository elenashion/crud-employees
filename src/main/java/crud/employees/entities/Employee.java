package crud.employees.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.Instant;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "employees", schema = "crud")
@NoArgsConstructor
@AllArgsConstructor
public class Employee extends IntBaseEntity {
    @Getter
    @Setter
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Getter
    @Setter
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Getter
    @Setter
    @Column(name = "middle_name", nullable = false)
    private String middleName;

    @Getter
    @Setter
    @Column(name = "date_of_birth", nullable = false)
    @Convert(converter = InstantToDateColumnConverter.class)
    private Instant dateOfBirth;

    @Getter
    @Setter
    @Column(name = "date_added", nullable = false)
    @Convert(converter = InstantToDateColumnConverter.class)
    private Instant dateAdded;

    @Getter
    @Setter
    @Column(name = "date_updated", nullable = false)
    @Convert(converter = InstantToDateColumnConverter.class)
    private Instant dateUpdated;

    @Getter
    @Setter
    @Column(name = "account_number")
    private int accountNumber;

    @Getter
    @Setter
    @Column(name = "is_deleted")
    private boolean deleted;

    public Employee(String firstName, String lastName, String middleName, Instant dateOfBirth, Instant dateAdded, Instant dateUpdated,
                    Integer accountNumber, boolean deleted) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.dateOfBirth = dateOfBirth;
        this.dateAdded = dateAdded;
        this.dateUpdated = dateUpdated;
        this.accountNumber = accountNumber;
        this.deleted = deleted;
    }

    public boolean update(String firstName, String lastName, String middleName, Instant dateOfBirth, Instant dateAdded,
                          Integer accountNumber, Boolean deleted) {
        boolean somethingChanged = false;
        if (firstName != null && !firstName.equals(this.firstName)) {
            this.firstName = firstName;
            somethingChanged = true;
        }
        if (lastName != null && !lastName.equals(this.lastName)) {
            this.lastName = lastName;
            somethingChanged = true;
        }
        if (middleName != null && !middleName.equals(this.middleName)) {
            this.middleName = middleName;
            somethingChanged = true;
        }
        if (dateOfBirth != null && !dateOfBirth.equals(this.dateOfBirth)) {
            this.dateOfBirth = dateOfBirth;
            somethingChanged = true;
        }
        if (dateAdded != null && !dateAdded.equals(this.dateAdded)) {
            this.dateAdded = dateAdded;
            somethingChanged = true;
        }
        if (accountNumber != null && !accountNumber.equals(this.accountNumber)) {
            this.accountNumber = accountNumber;
            somethingChanged = true;
        }
        if (deleted != null && !deleted.equals(this.deleted)) {
            this.deleted = deleted;
            somethingChanged = true;
        }
        if (somethingChanged) {
            this.dateUpdated = Instant.now();
        }
        return somethingChanged;
    }

}
