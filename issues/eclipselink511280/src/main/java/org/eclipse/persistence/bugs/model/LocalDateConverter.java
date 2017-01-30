package org.eclipse.persistence.bugs.model;

import java.sql.Date;
import java.time.LocalDate;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LocalDateConverter implements AttributeConverter<LocalDate, Date> {

	@Override
	public Date convertToDatabaseColumn(LocalDate entityValue) {
		if (entityValue == null) return null;

		return Date.valueOf(entityValue);
	}

	@Override
	public LocalDate convertToEntityAttribute(Date databaseValue) {
		if (databaseValue == null) return null;

		return databaseValue.toLocalDate();
	}
}
