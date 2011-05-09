package org.javasimon.examples.jdbc.h2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * StoredProcedures for H2.
 *
 * @author Radovan Sninsky
 * @version $ Revision $ $ Date $
 * @since 1.0
 */
public class StoredProcedures {

	public static void fooProc(Connection c, int id, String text)  throws SQLException {
		PreparedStatement stmt = null;
		try {
			stmt = c.prepareStatement("insert into foo values (?, ?)  ");
			stmt.setInt(1, id);
			stmt.setString(2, text);
			stmt.executeUpdate();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}
}
