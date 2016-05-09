import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import junit.framework.TestCase;
import org.eclipse.persistence.bugs.model.Dog;
import org.eclipse.persistence.bugs.model.Owner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestBug422510 {

	private EntityManager em;
	private EntityManagerFactory emf;

	@Before
	public void setup() {
		emf = Persistence.createEntityManagerFactory("testcase");
		prepareData();
		em = emf.createEntityManager();
	}

	// owner A with a dog Lessie,
	// owner B with a dog Rex,
	// owner C with no dog
	// flushes to enforce ID order
	private void prepareData() {
		em = emf.createEntityManager();
		em.getTransaction().begin();

		Dog lessie = new Dog("Lessie");
		em.persist(lessie);
		em.flush();
		Dog rex = new Dog("Rex");
		em.persist(rex);
		em.flush();

		Owner ownerA = new Owner("A");
		ownerA.setDogs(Collections.singleton(lessie));
		em.persist(ownerA);
		em.flush();

		Owner ownerB = new Owner("B");
		ownerB.setDogs(Collections.singleton(rex));
		em.persist(ownerB);
		em.flush();

		em.persist(new Owner("C"));

		em.getTransaction().commit();
		em.close();
	}

	@After
	public void teardown() {
		em.close();
		emf.close();
	}

	@Test
	public void leftJoinWithoutOnReturnsThreeOwnersWithTwoDogs() {
		List<Object[]> resultList = em.createQuery(
			"select o.name, d.name from Owner o" +
				" left join o.dogs d" +
				" order by o.id", Object[].class)
			.getResultList();

		printResults(resultList);
		TestCase.assertEquals(resultList.size(), 3);
		TestCase.assertEquals(resultList.get(0)[0], "A");
		TestCase.assertEquals(resultList.get(0)[1], "Lessie");
		TestCase.assertEquals(resultList.get(1)[0], "B");
		TestCase.assertEquals(resultList.get(1)[1], "Rex");
		TestCase.assertEquals(resultList.get(2)[0], "C");
		TestCase.assertEquals(resultList.get(2)[1], null);
	}

	@Test
	public void leftJoinWithOnReturnsThreeOwnersWithOneDog() {
		List<Object[]> resultList = em.createQuery(
			"select o.name, d.name from Owner o" +
				" left join o.dogs d on d.name='Lessie'" +
				" order by o.id", Object[].class)
			.getResultList();

		printResults(resultList);
		TestCase.assertEquals(resultList.size(), 3);
		TestCase.assertEquals(resultList.get(0)[0], "A");
		TestCase.assertEquals(resultList.get(0)[1], "Lessie");
		TestCase.assertEquals(resultList.get(1)[0], "B");
		TestCase.assertEquals(resultList.get(1)[1], null);
		TestCase.assertEquals(resultList.get(2)[0], "C");
		TestCase.assertEquals(resultList.get(2)[1], null);
	}

	private void printResults(List<Object[]> resultList) {
		System.out.println("\nRESULT:");
		for (Object[] objects : resultList) {
			System.out.println(Arrays.toString(objects));
		}
	}
}
