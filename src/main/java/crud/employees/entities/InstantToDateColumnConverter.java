package crud.employees.entities;

import org.joda.time.Instant;

import javax.persistence.AttributeConverter;
import java.util.Date;

public class InstantToDateColumnConverter implements AttributeConverter<Instant, Date> {
    @Override
    public Date convertToDatabaseColumn(Instant attribute) {
        return attribute == null ? null : attribute.toDate();
    }

    @Override
    public Instant convertToEntityAttribute(Date dbData) {
        return dbData == null ? null : new Instant(dbData);
    }
}
