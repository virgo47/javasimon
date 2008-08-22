package org.javasimon.jdbc;

/**
 * SqlNormalizer takes SQL statement and replaces parameters with question marks.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 17, 2008
 */
public class SqlNormalizer {
    private final String sql;
    private String normalizedSql;
    private String type;

    public SqlNormalizer(String sql) {
        this.sql = sql;
        normalize();
    }

    private void normalize() {
        normalizedSql = sql.toLowerCase()
                .replaceAll("''", "?") // replace empty strings and '' inside other strings
                .replaceAll(" *([=<>!,]+) *", "$1") // remove spaces around = and ,
                .replaceAll(" +", " ") // normalize spaces
                .replaceAll("([(=<>!,]+)(?:(?:'[^']+')|(?:[0-9.]+))", "$1?") // replace arguments after =, ( and , with ?
                .replaceAll("([(=<>!,]+)\\w+\\([^)]*\\)", "$1?") // replace whole functions with ?
                .replaceAll("like '[^']+'", "like ?") // replace like arguments
                .replaceAll("between \\S+ and \\S+", "between ? and ?") // replace between arguments
                .replaceAll(" *\\(", "(") // remove spaces in front of (
                .replaceAll("([=<>!]+)", " $1 ") // put spaces around =, >=, <=, !=...
                .replaceAll(",", ", ") // put spaces after ,
                ;
        type = normalizedSql.replaceAll("\\W*(\\w+)\\W.*", "$1");
    }

    public String getSql() {
        return sql;
    }

    public String getNormalizedSql() {
        return normalizedSql;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "SqlNormalizer{" +
                "\n  sql='" + sql + '\'' +
                ",\n  normalizedSql='" + normalizedSql + '\'' +
                ",\n  type='" + type + '\'' +
                '}';
    }

    public static void main(String[] args) {
        System.out.println(new SqlNormalizer("select * from trn, subtrn where amount>=45.8 and date!=to_date('5.6.2008', 'dd.mm.yyyy') and type='Mark''s'"));
        System.out.println(new SqlNormalizer("update trn set amount=50.6,type='bubu' where id=4"));
        System.out.println(new SqlNormalizer("update 'trn' set amount=50.6,type='' where id in (4,5,6) or date in (to_date('6.6.2006','dd.mm.yyyy'), to_date('7.6.2006','dd.mm.yyyy'))"));
        System.out.println(new SqlNormalizer("delete from trn where id in(select id from subtrn where trndesc not like '%SX')"));
        System.out.println(new SqlNormalizer("delete from trn where date between to_date('6.6.2006','dd.mm.yyyy') and to_date('7.6.2006','dd.mm.yyyy') and id<=10000"));
    }
}
