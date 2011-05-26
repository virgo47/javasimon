package gwimon.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;

/**
 * Gwimon service async version.
 *
 * @author Richard "Virgo" Richter (virgo47@gmail.com)
 */
public interface GwimonServiceAsync extends RemoteService {
	void listSimons(SimonFilter filter, AsyncCallback<SimonAggregation> async);
}
