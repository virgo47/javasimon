package org.javasimon.jdbc;

import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import org.testng.Assert;

import java.util.Arrays;

/**
 * Unit tests for SqlNormalizer class.
 *
 * @author Radovan Sninsky
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @version $Revision: 192 $ $Date: 2009-03-22 01:03:48 +0100 (Sun, 22 Mar 2009) $
 * @created 25.8.2008 10:51:52
 * @since 1.0
 */
public final class SqlNormalizerTestNG {
	@DataProvider(name = "dp1")
	public Object[][] createTestData() {
		return new Object[][] {
			{null, null, null},
			{"select * from trn, subtrn where amount>=45.8 and date!=to_date('5.6.2008', 'dd.mm.yyyy') and type='Mark''s'",
				"select", "select * from trn, subtrn where amount >= ? and date != ? and type = ?"},
			{"select name as 'Customer Name', sum(item_price) as sum from foo where dept = ? group by name",
				"select", "select name as 'customer name', sum(item_price) as sum from foo where dept = ? group by name"},
			{"select name as employee, sum(dur)/8 hrs from foo where dept='sys' group by name order by name desc",
				"select", "select name as employee, sum(dur) / ? hrs from foo where dept = ? group by name order by name desc"},
			{"select sysdate(), sysdate from sys.dual",
				"select", "select sysdate(), sysdate from sys.dual"},
			{"SELECT * FROM a JOIN (b JOIN c ON (b.ref = c.id)) ON (a.id = b.id);",
				"select", "select * from a join (b join c on (b.ref = c.id)) on (a.id = b.id);"},
			{"select * from wherever where x=function1(function2('xxx'), function2(function3(3 + 5)))",
				"select", "select * from wherever where x = ?"},

			{"update trn set amount=50.6,type='bubu' where id=4",
				"update", "update trn set amount = ?, type = ? where id = ?"},
			{"update 'trn' set amount=  50.6,type='' where id in (4,5,6) or date in (to_date('6.6.2006','dd.mm.yyyy'), to_date('7.6.2006','dd.mm.yyyy'))",
				"update", "update 'trn' set amount = ?, type = ? where id in (?) or date in (?)"},
			{"update nonsense set amount=null where substring(date,3,17) like '%'.to_date('7.6.2006','dd.mm.yyyy')",
				"update", "update nonsense set amount = null where substring(date, ?, ?) like ?.to_date(?, ?)"},

			{"   delete from trn where id in   (select    id from subtrn where trndesc not like '%SX')",
				"delete", "delete from trn where id in (select id from subtrn where trndesc not like ?)"},
			{"delete from trn where date between to_date('6.6.2006','dd.mm.yyyy') and to_date('7.6.2006','dd.mm.yyyy') and id<=10000",
				"delete", "delete from trn where date between ? and ? and id <= ?"},
			{"delete from trn where id in (select id from subtrn where trndesc in (select allowed from descs))",
				"delete", "delete from trn where id in (select id from subtrn where trndesc in (select allowed from descs))"},

			{"insert into foo ('bufo', 4.47)", "insert", "insert into foo (?, ?)"},
			{"insert into foo (a1) values ('bubu')", "insert", "insert into foo (a1) values (?)"},

			{"{call foo_ins_proc(99999, 'This text is inserted from stored procedure')}", "call", "call foo_ins_proc(?, ?)"},
			{"{?= call foo_ins_proc_with_ret(99999, 'Text', sysdate())}", "call", "? = call foo_ins_proc_with_ret(?, ?, ?)"},
			{"begin foo_ins_proc_with_ret(99999, 'Text', sysdate); end;", "call", "call foo_ins_proc_with_ret(?, ?, sysdate)"},

			// DDL
			{"  create table	foo(a1 varchar2(30) not null, a2   numeric(12,4))", "create", "create table foo"},
		};
	}

	@Test(dataProvider = "dp1")
	public void sqlNormalizerTest(String sql, String type, String normSql) {
		SqlNormalizer sn = new SqlNormalizer(sql);

		Assert.assertEquals(sn.getType(), type);
		Assert.assertEquals(sn.getNormalizedSql(), normSql);
	}

	@Test
	public void batchNormalizationTest() {
		SqlNormalizer sn = new SqlNormalizer(Arrays.asList("update trn set amount=50.6,type='bubu' where id=4", "insert into foo ('bufo', 4.47)"));

		Assert.assertEquals(sn.getType(), "batch");
		Assert.assertEquals(sn.getSql(), "batch");
		Assert.assertEquals(sn.getNormalizedSql(), "update trn set amount = ?, type = ? where id = ?; insert into foo (?, ?)");

		sn = new SqlNormalizer(Arrays.asList(
			"insert into fuu values (?, ?, ?, ?, ?)",
			"insert into fuu values (?, ?, ?, ?, ?)",
			"insert into fuu values (?, ?, ?, ?, ?)",
			"insert into fuu values (?, ?, ?, ?, ?)",
			"delete from fuu where id =?",
			"delete from fuu where id =?",
			"insert into fuu2 values (?, 'aaa')"
		));

		Assert.assertEquals(sn.getType(), "batch");
		Assert.assertEquals(sn.getSql(), "batch");
		Assert.assertEquals(sn.getNormalizedSql(), "4x insert into fuu values (?, ?, ?, ?, ?); 2x delete from fuu where id = ?; insert into fuu2 values (?, ?)");
	}
}
