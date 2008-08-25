package org.javasimon.jdbc;

import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import org.testng.Assert;

/**
 * Trieda SqlNormalizerTest.
 *
 * @author Radovan Sninsky
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @version $Revision$ $Date$
 * @created 25.8.2008 10:51:52
 * @since 1.0
 */
public class SqlNormalizerTest {

	@DataProvider(name = "dp1")
	public Object[][] createTestData() {
		return new Object[][] {
			{null, null, null},
			{"select * from trn, subtrn where amount>=45.8 and date!=to_date('5.6.2008', 'dd.mm.yyyy') and type='Mark''s'",
				"select", "select * from trn, subtrn where amount >= ? and date != ? and type = ?"},
			{"update trn set amount=50.6,type='bubu' where id=4",
				"update", "update trn set amount = ?, type = ? where id = ?"},
			{"update 'trn' set amount=  50.6,type='' where id in (4,5,6) or date in (to_date('6.6.2006','dd.mm.yyyy'), to_date('7.6.2006','dd.mm.yyyy'))",
				"update", "update 'trn' set amount = ?, type = ? where id in (?) or date in (?)"},
			{"   delete from trn where id in   (select    id from subtrn where trndesc not like '%SX')",
				"delete", "delete from trn where id in (select id from subtrn where trndesc not like ?)"},
			{"delete from trn where date between to_date('6.6.2006','dd.mm.yyyy') and to_date('7.6.2006','dd.mm.yyyy') and id<=10000",
				"delete", "delete from trn where date between ? and ? and id <= ?"},
			{"  create table	foo(a1 varchar2(30) not null, a2   numeric(12,4))", "create", "create table foo"},
			{"insert into foo ('bufo', 4.47)", "insert", "insert into foo (?, ?)"},
			{"insert into foo (a1) values ('bubu')", "insert", "insert into foo (a1) values (?)"},
		};
	}

	@Test(dataProvider = "dp1")
	public void sqlNormalizerTest(String sql, String type, String normSql) {
		SqlNormalizer sn = new SqlNormalizer(sql);

		Assert.assertEquals(sn.getType(), type);
		Assert.assertEquals(sn.getNormalizedSql(), normSql);
	}
}
