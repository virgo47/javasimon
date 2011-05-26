package gwimon.client;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * Gwimon service.
 *
 * @author Richard "Virgo" Richter (virgo47@gmail.com)
 */
public interface GwimonService extends RemoteService {
	SimonAggregation listSimons(SimonFilter filter);
}
