package org.javasimon.jdbcx;

import org.javasimon.jdbcx.SimonDataSource;

import javax.naming.spi.ObjectFactory;
import javax.naming.Name;
import javax.naming.Context;
import javax.naming.Reference;
import java.util.Hashtable;

/**
 * Trieda DataSourceFactory.
 *
 * @author Radovan Sninsky
 * @version $Revision$ $Date$
 * @created 15.9.2008 18:43:42
 * @since 1.0
 */
public final class SimonDataSourceFactory implements ObjectFactory {

	final static String ATT_REAL_DS = "realDataSource";
	final static String ATT_PREFIX = "prefix";

	public Object getObjectInstance(Object o, Name name, Context ctx, Hashtable<?, ?> env) throws Exception {
		if (o instanceof Reference) {
			Reference ref = (Reference) o;
			if (ref.getClassName().equals(SimonDataSource.class.getName())) {
				SimonDataSource ds = new SimonDataSource();
				ds.setRealDataSource((String) ref.get(ATT_REAL_DS).getContent());
				ds.setPrefix((String) ref.get(ATT_PREFIX).getContent());
				return ds;
			}
		}
		return null;
	}
}
