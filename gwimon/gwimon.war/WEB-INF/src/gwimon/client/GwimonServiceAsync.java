package gwimon.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;

import java.util.ArrayList;

/**
 * Gwimon service async version.
 *
 * @author Richard "Virgo" Richter (virgo47@gmail.com)
 */
public interface GwimonServiceAsync extends RemoteService {
	void listSimons(String mask, AsyncCallback<ArrayList<SimonValue>> async);
}
