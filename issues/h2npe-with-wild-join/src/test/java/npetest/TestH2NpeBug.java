package npetest;

import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;

import org.h2.tools.RunScript;
import org.junit.Test;

public class TestH2NpeBug {

	@Test
	public void runFailingScript() throws Exception {
		Connection conn = DriverManager.getConnection(
			"jdbc:h2:mem:test;TRACE_LEVEL_SYSTEM_OUT=2", "sa", "");
		InputStreamReader scriptReader = new InputStreamReader(
			getClass().getResourceAsStream("/failing-script.sql"), "UTF-8");

		// this throws org.h2.jdbc.JdbcSQLException: General error: "java.lang.NullPointerException" [50000-191]
		RunScript.execute(conn, scriptReader);
	}
}
