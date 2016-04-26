import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.eclipse.persistence.bugs.model.Dog;
import org.eclipse.persistence.bugs.model.DogFalse;
import org.eclipse.persistence.bugs.model.DogTrue;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestSharedCacheMode {

	@Test
	public void testSharedCacheModeAll() throws SQLException {
		dropAll();
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("testcase-all");
		try {
			prepareData();

			System.out.println("\nChecks BEFORE change");
			selectDogName(emf, Dog.class, "Lessie");
			selectDogName(emf, DogTrue.class, "Lessie");
			selectDogName(emf, DogFalse.class, "Lessie");

			assertTrue(emf.getCache().contains(Dog.class, 1));
			assertTrue(emf.getCache().contains(DogTrue.class, 1));
			assertTrue(emf.getCache().contains(DogFalse.class, 1));

			directSqlChange();

			System.out.println("\nChecks AFTER change");
			// all entities read cached data
			selectDogName(emf, Dog.class, "Lessie");
			selectDogName(emf, DogTrue.class, "Lessie");
			selectDogName(emf, DogFalse.class, "Lessie");
		} finally {
			emf.close();
		}
	}

	@Test
	public void testSharedCacheModeNone() throws SQLException {
		dropAll();
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("testcase-none");
		try {
			prepareData();

			System.out.println("\nChecks BEFORE change");
			selectDogName(emf, Dog.class, "Lessie");
			selectDogName(emf, DogTrue.class, "Lessie");
			selectDogName(emf, DogFalse.class, "Lessie");

			assertFalse(emf.getCache().contains(Dog.class, 1));
			assertFalse(emf.getCache().contains(DogTrue.class, 1));
			assertFalse(emf.getCache().contains(DogFalse.class, 1));

			directSqlChange();

			System.out.println("\nChecks AFTER change");
			// all entities read data from DB
			selectDogName(emf, Dog.class, "Rex");
			selectDogName(emf, DogTrue.class, "Rex");
			selectDogName(emf, DogFalse.class, "Rex");
		} finally {
			emf.close();
		}
	}

	@Test
	public void testSharedCacheModeEnableSelected() throws SQLException {
		dropAll();
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("testcase-ensel");
		try {
			prepareData();

			System.out.println("\nChecks BEFORE change");
			selectDogName(emf, Dog.class, "Lessie");
			selectDogName(emf, DogTrue.class, "Lessie");
			selectDogName(emf, DogFalse.class, "Lessie");

			assertFalse(emf.getCache().contains(Dog.class, 1));
			assertTrue(emf.getCache().contains(DogTrue.class, 1));
			assertFalse(emf.getCache().contains(DogFalse.class, 1));

			directSqlChange();

			System.out.println("\nChecks AFTER change");
			// only selected are cached (dog true)
			selectDogName(emf, Dog.class, "Rex");
			selectDogName(emf, DogTrue.class, "Lessie");
			selectDogName(emf, DogFalse.class, "Rex");
		} finally {
			emf.close();
		}
	}

	private <T> void selectDogName(EntityManagerFactory emf, Class<T> entityClass, String name) {
		EntityManager em = emf.createEntityManager();
		try {
			T dog = em.find(entityClass, 1);
//			T dog = em.createQuery("select d from " + entityClass.getSimpleName() +
//				" d where d.id = 1", entityClass)
			// this would cause load of fresh DB data all the time regardless of cache settings
//				.setHint("javax.persistence.cache.storeMode", CacheStoreMode.REFRESH)
//				.getSingleResult();
			Assert.assertEquals(dog.toString(), name);
		} finally {
			em.close();
		}
	}

	private void dropAll() throws SQLException {
		Connection conn = DriverManager.getConnection(
			"jdbc:h2:mem:test;TRACE_LEVEL_SYSTEM_OUT=2", "sa", "");

		Statement stmt = conn.createStatement();
		stmt.executeUpdate("drop all objects");
		conn.commit();

		conn.close();
	}

	private void prepareData() throws SQLException {
		Connection conn = DriverManager.getConnection(
			"jdbc:h2:mem:test;TRACE_LEVEL_SYSTEM_OUT=2", "sa", "");

		Statement stmt = conn.createStatement();
		stmt.executeUpdate("insert into Dog (name) values ('Lessie')");
		stmt.executeUpdate("insert into DogTrue (name) values ('Lessie')");
		stmt.executeUpdate("insert into DogFalse (name) values ('Lessie')");
		conn.commit();

		conn.close();
	}

	private void directSqlChange() throws SQLException {
		System.out.println("\nDIRECT SQL CHANGE");
		Connection conn = DriverManager.getConnection(
			"jdbc:h2:mem:test;TRACE_LEVEL_SYSTEM_OUT=2", "sa", "");

		Statement stmt = conn.createStatement();
		stmt.executeUpdate("update Dog set name='Rex'");
		stmt.executeUpdate("update DogTrue set name='Rex'");
		stmt.executeUpdate("update DogFalse set name='Rex'");
		conn.commit();

		conn.close();
	}
}
