package org.javasimon.examples.testapp;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.examples.testapp.model.Tuple;
import org.javasimon.examples.testapp.model.TupleDAO;
import org.javasimon.examples.testapp.model.Tuples;
import org.javasimon.examples.testapp.test.Action;

/**
 * Class InsertBatchAction.
 *
 * @author Radovan Sninsky
 * @since 2.0
 */
public class InsertBatchAction implements Action {
	private static final int COUNT = 28;

	private Connection conn;

	/**
	 * Insert batch action constructor.
	 *
	 * @param conn SQL connection
	 */
	public InsertBatchAction(Connection conn) {
		this.conn = conn;
	}

	/**
	 * Inserts batch.
	 *
	 * @param runno run number
	 */
	public void perform(int runno) {
		Split split = SimonManager.getStopwatch("org.javasimon.examples.testapp.action.insertbatch").start();

		System.out.println("Run: " + runno + ", InsertBatchAction [count: " + COUNT + "]");

		List<Tuple> list = new ArrayList<Tuple>(COUNT);
		for (Tuple t : new Tuples(COUNT)) {
			list.add(t);
		}
		try {
			new TupleDAO(conn, "tuple").save(list);
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}

		split.stop();
	}
}
