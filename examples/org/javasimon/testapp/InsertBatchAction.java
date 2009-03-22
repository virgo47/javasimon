package org.javasimon.testapp;

import org.javasimon.testapp.test.Action;
import org.javasimon.testapp.model.Tuple;
import org.javasimon.testapp.model.Tuples;
import org.javasimon.testapp.model.TupleDAO;
import org.javasimon.Split;
import org.javasimon.SimonManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

/**
 * Class InsertBatchAction.
 *
 * @author Radovan Sninsky
 * @version $Revision$ $ Date: $
 * @created 20.3.2009 11:21:48
 * @since 2.0
 */
public class InsertBatchAction implements Action {

	private static final int COUNT = 28;

	private Connection conn;

	public InsertBatchAction(Connection conn) {
		this.conn = conn;
	}

	public void perform(int runno) {
		Split split = SimonManager.getStopwatch("org.javasimon.testapp.action.insertbatch").start();

		System.out.println("Run: "+runno+", InsertBatchAction [count: "+COUNT+"]");

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
