package crud.employees.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employees")
public class Employee extends IntBaseEntity {
    @Getter
    @Setter
    private String firstName;

    @Getter
    @Setter
    private String lastName;

    @Getter
    @Setter
    private String middleName;

    @Getter
    @Setter
    private Instant dateOfBirth;

    @Getter
    @Setter
    private Instant dateAdded;

    @Getter
    @Setter
    private Instant dateUpdated;

    @Getter
    @Setter
    private int accountNumber;

    @Getter
    @Setter
    private boolean deleted;

    public Employee(String firstName, String lastName, String middleName, Instant dateOfBirth, Instant dateAdded, Instant dateUpdated,
                    Integer accountNumber, Boolean deleted) {
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
