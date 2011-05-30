package gwimon.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.*;

/**
 * Gwimon console main class with entry point.
 *
 * @author Richard "Virgo" Richter (virgo47@gmail.com)
 */
public class Gwimon implements EntryPoint {
	private GwimonServiceAsync service;
	private VerticalPanel contentPanel;
	private GwimonTable simonTable;
	private FilterForm filterForm;

	@Override
	public void onModuleLoad() {
		service = (GwimonServiceAsync) GWT.create(GwimonService.class);
		ServiceDefTarget endpoint = (ServiceDefTarget) service;
		endpoint.setServiceEntryPoint(GWT.getModuleBaseURL() + "gwimon");

		contentPanel = new VerticalPanel();
		contentPanel.addStyleName("content-screen");
		Panel uberPanel = new VerticalPanel();
		uberPanel.addStyleName("width-100-percent");

		filterForm = new FilterForm() {
			@Override
			public void callToggleColumns() {
				if (simonTable != null) {
					simonTable.toggleColumns();
				}
			}

			@Override
			public void callRefresh() {
				listAllSimons();
			}
		};

		uberPanel.add(filterForm);
		uberPanel.add(contentPanel);
		RootPanel.get().add(uberPanel);

		listAllSimons();
	}

	private void listAllSimons() {
		SimonFilter simonFilter = new SimonFilter();
		simonFilter.setMask(filterForm.getFilterMask());
		service.listSimons(simonFilter, new AsyncCallback<SimonAggregation>() {
			@Override
			public void onSuccess(SimonAggregation result) {
				showSimonResults(result);
			}

			@Override
			public void onFailure(Throwable throwable) {
				Window.alert("ERROR listSimons(): " + throwable.getMessage());
			}
		});
	}

	private void showSimonResults(SimonAggregation aggregation) {
		contentPanel.clear();

		simonTable = new GwimonTable(aggregation.getSimonList());
		simonTable.setPageSize(10);
		contentPanel.add(simonTable);
	}

	public GwimonServiceAsync getService() {
		return service;
	}
}