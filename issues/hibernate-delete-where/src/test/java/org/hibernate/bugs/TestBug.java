package org.hibernate.bugs;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestBug {

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
	public void deleteWithWhereAcrossRelationsShouldWork() {
		em.getTransaction().begin();
		em.createQuery("delete from Dog d where d.breed.name = 'collie'")
			.executeUpdate();
		// it doesn't get here anyway
		em.getTransaction().rollback();
	}
}
