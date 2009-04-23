package org.javasimon.testapp;

import org.javasimon.testapp.RandomNumberDataProvider;
import org.javasimon.testapp.test.Action;
import org.javasimon.testapp.model.TupleDAO;
import org.javasimon.Split;
import org.javasimon.SimonManager;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Class DeleteAction.
 *
 * @author Radovan Sninsky
 * @version $Revision$ $ Date: $
 * @created 19.3.2009 17:51:45
 * @since 2.0
 */
public class DeleteAction implements Action {

	private RandomNumberDataProvider provider;
	private Connection conn;

	public DeleteAction(RandomNumberDataProvider provider, Connection conn) {
		this.provider = provider;
		this.conn = conn;
	}

	public void perform(int runno) {
		Split split = SimonManager.getStopwatch("org.javasimon.testapp.action.delete").start();

		int no = provider.no();
		try {
			int deleted = new TupleDAO(conn, "tuple").deleteByUnique1(no);
			System.out.println("Run: "+runno+", DeleteAction [unique1: "+no+", deleted: "+deleted+"]");
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}

		split.stop();
	}
}
