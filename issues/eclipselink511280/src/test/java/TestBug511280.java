import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.eclipse.persistence.bugs.model.Dog;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/** https://bugs.eclipse.org/bugs/show_bug.cgi?id=511280 */
public class TestBug511280 {

	private EntityManagerFactory emf;

	@BeforeClass
	public void setup() {
		emf = Persistence.createEntityManagerFactory("testcase");
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();
		em.createQuery("delete from Dog").executeUpdate();

		Dog lassie = new Dog();
		lassie.name = "Lassie";
		lassie.birthdate = LocalDate.of(1970, 1, 1);
		lassie.died = LocalDate.of(1980, 1, 1);
		em.persist(lassie);

		Dog rexo = new Dog();
		rexo.name = "Rex";
		rexo.birthdate = LocalDate.of(2010, 1, 1);
		// does not have a died date - causes coalesce to kick in and fail
		em.persist(rexo);

		em.getTransaction().commit();
		em.close();
	}

	@AfterClass
	public void teardown() {
		emf.close();
	}

	@Test
	public void coalesceQueryShouldUseConverter() {
		EntityManager em = emf.createEntityManager();
		try {
			List result = em.createQuery(
				"select d from Dog d where coalesce(d.died, :date) > d.birthdate", Dog.class)
				.setParameter("date", LocalDate.now())
				.getResultList();

			System.out.println("result = " + result);
		} finally {
			em.close();
		}
	}
}
