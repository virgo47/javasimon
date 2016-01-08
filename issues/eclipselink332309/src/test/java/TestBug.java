import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.eclipse.persistence.bugs.model.Dog;
import org.eclipse.persistence.bugs.model.Owner;
import org.junit.After;
import org.junit.Assert;
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
		// data preparation, 2 dogs and owner with both of them
		em.getTransaction().begin();
		Dog lassie = new Dog("Lassie"); // we will try to remove this one from collection later
		em.persist(lassie);
		Dog rex = new Dog("Rex");
		em.persist(rex);

		Owner owner = new Owner();
		owner.setUniqId(47);
		HashSet<Dog> dogs = new HashSet<Dog>();
		dogs.add(lassie);
		dogs.add(rex);
		owner.setDogs(dogs);
		em.persist(owner);
		em.getTransaction().commit();

		checkOwnerDogCount(2);

		// now removal of one dog
		em.getTransaction().begin();
		owner.getDogs().remove(lassie);
		Assert.assertEquals(owner.getDogs().size(), 1);
		Assert.assertNotNull(owner.getUniqId());
		em.getTransaction().commit();

		checkOwnerDogCount(1);
	}

	private void checkOwnerDogCount(int expectedCount) {
		Long count = em.createQuery("select count(d) from Dog d", Long.class).getSingleResult();
		Assert.assertEquals(count.longValue(), 2); // always two, no dogs were killed during the test

		List results = em.createQuery("select o.dogs from Owner o").getResultList();
		Assert.assertEquals(results.size(), expectedCount);
	}
}
