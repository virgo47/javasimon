package org.hibernate.bugs;

import static org.junit.Assert.assertNotNull;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JpaFunctionTest {

	private EntityManager em;

	@Before
	public void setup() {
		em = Persistence.createEntityManagerFactory("testcase").createEntityManager();
	}

	@After
	public void teardown() {
		em.close();
	}

	@Test
	public void noArgFunctionTest() {
		Object result = em
			.createQuery("select FUNCTION('random_uuid') from Dual dual")
			.getSingleResult();
		assertNotNull(result);
		System.out.println("result = " + result);
		// it doesn't get here anyway
	}

	@Test
	public void oneArgFunctionTest() {
		Object result = em
			.createQuery("select FUNCTION('random', 3) from Dual dual")
			.getSingleResult();
		assertNotNull(result);
		System.out.println("result = " + result);
		// it doesn't get here anyway
	}
}
