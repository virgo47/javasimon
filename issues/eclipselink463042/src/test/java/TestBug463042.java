import java.util.List;

import javax.persistence.CacheStoreMode;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/** https://bugs.eclipse.org/bugs/show_bug.cgi?id=463042 */
public class TestBug463042 {

	private EntityManagerFactory emf;

	@BeforeClass
	public void setup() {
		emf = Persistence.createEntityManagerFactory("testcase");
	}

	@AfterClass
	public void teardown() {
		emf.close();
	}

	@Test(threadPoolSize = 10, invocationCount = 100)
	public void selectWithCaseAndHintShouldWorkInParallel() {
		EntityManager em = emf.createEntityManager();
		try {
			List result = em
				.createQuery("select case when d.id = 0 then 1 else 2 end from Dog d")
				.setHint("javax.persistence.cache.storeMode", CacheStoreMode.REFRESH)
				.getResultList();
		} finally {
			em.close();
		}
	}

	@Test(threadPoolSize = 10, invocationCount = 100)
	public void selectWithHintAndNoCaseShouldWorkInParallel() {
		EntityManager em = emf.createEntityManager();
		try {
			List result = em
				.createQuery("select d from Dog d")
				.setHint("javax.persistence.cache.storeMode", CacheStoreMode.REFRESH)
				.getResultList();
		} finally {
			em.close();
		}
	}

	@Test(threadPoolSize = 10, invocationCount = 100)
	public void selectWithCaseShouldWorkInParallel() {
		EntityManager em = emf.createEntityManager();
		try {
			List result = em
				.createQuery("select case when d.id = 0 then 1 else 2 end from Dog d")
				.getResultList();
		} finally {
			em.close();
		}
	}
}
