package utils;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;

public class JavaDateTimeConversions {

	public static OffsetDateTime timestampToOffsetDateTime(Timestamp timestamp) {
		return OffsetDateTime.ofInstant(
			Instant.ofEpochMilli(timestamp.getTime()), ZoneId.systemDefault());
	}
}
