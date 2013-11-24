package org.javasimon.examples.testapp;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.examples.testapp.model.Tuple;
import org.javasimon.examples.testapp.model.TupleDAO;
import org.javasimon.examples.testapp.model.Tuples;
import org.javasimon.examples.testapp.test.Action;

/**
 * Class InsertAction.
 *
 * @author Radovan Sninsky
 * @since 2.0
 */
public class InsertAction implements Action {
	private RandomNumberDataProvider provider;

	private Connection conn;

	/**
	 * Insert action constructor.
	 *
	 * @param provider random number data provider
	 * @param conn SQL connection
	 */
	public InsertAction(RandomNumberDataProvider provider, Connection conn) {
		this.provider = provider;
		this.conn = conn;
	}

	/**
	 * Inserts random record.
	 *
	 * @param runno run number
	 */
	public void perform(int runno) {
		Split split = SimonManager.getStopwatch("org.javasimon.examples.testapp.action.insert").start();

		int cnt = provider.no();
		System.out.println("Run: " + runno + ", InsertAction [count: " + cnt + "]");

		try {
			Iterator<Tuple> t = new Tuples(cnt).iterator();
			TupleDAO dao = new TupleDAO(conn, "tuple");
			for (int i = 0; i < cnt / 200; i++) {
				List<Tuple> list = new ArrayList<Tuple>(200);
				int j = 0;
				while (j++ < 200) {
					list.add(t.next());
				}
				dao.save(list);
			}

			for (int i = 0; i < (cnt - (cnt / 200) * 200) / 20; i++) {
				List<Tuple> list = new ArrayList<Tuple>(20);
				int j = 0;
				while (j++ < 20) {
					list.add(t.next());
				}
				dao.save(list);
			}

			while (t.hasNext()) {
				dao.save(t.next());
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}

		split.stop();
	}
}
