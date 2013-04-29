package org.javasimon.examples.testapp;

import org.javasimon.examples.testapp.test.Action;
import org.javasimon.examples.testapp.model.TupleDAO;
import org.javasimon.Split;
import org.javasimon.SimonManager;

import java.sql.SQLException;
import java.sql.Connection;

/**
 * Class UpdateAction.
 *
 * @author Radovan Sninsky
 * @since 2.0
 */
public class UpdateAction implements Action {

	private RandomNumberDataProvider provider;
	private Connection conn;

	public UpdateAction(RandomNumberDataProvider provider, Connection conn) {
		this.provider = provider;
		this.conn = conn;
	}

	public void perform(int runno) {
		Split split = SimonManager.getStopwatch("org.javasimon.examples.testapp.action.update").start();

		int unique1 = provider.no();
		try {
			int updated = new TupleDAO(conn, "tuple").update(unique1);
			System.out.println("Run: "+runno+", UpdateAction [unique1: "+unique1+", updated: "+updated+"]");
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		split.stop();
	}
}
