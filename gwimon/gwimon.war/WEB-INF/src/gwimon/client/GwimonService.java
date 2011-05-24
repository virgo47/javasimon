package gwimon.client;

import com.google.gwt.user.client.rpc.RemoteService;

import java.util.ArrayList;

/**
 * Gwimon service.
 *
 * @author Richard "Virgo" Richter (virgo47@gmail.com)
 */
public interface GwimonService extends RemoteService {
	ArrayList<SimonValue> listSimons(String mask);
}
