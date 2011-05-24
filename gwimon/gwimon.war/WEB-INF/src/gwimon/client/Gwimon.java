package gwimon.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.ListDataProvider;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Gwimon console main class with entry point.
 *
 * @author Richard "Virgo" Richter (virgo47@gmail.com)
 */
public class Gwimon implements EntryPoint {
	private GwimonServiceAsync service;
	private VerticalPanel contentPanel;

	@Override
	public void onModuleLoad() {
		service = (GwimonServiceAsync) GWT.create(GwimonService.class);
		ServiceDefTarget endpoint = (ServiceDefTarget) service;
		endpoint.setServiceEntryPoint(GWT.getModuleBaseURL() + "gwimon");

		contentPanel = new VerticalPanel();
		contentPanel.addStyleName("content-screen");
		Panel uberPanel = new VerticalPanel();
		uberPanel.addStyleName("width-100-percent");
		Button refreshButton = new Button("Refresh");
		refreshButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				listAllSimons();
			}
		});
		uberPanel.add(refreshButton);
		uberPanel.add(contentPanel);
		RootPanel.get().add(uberPanel);

		listAllSimons();
	}

	private void listAllSimons() {
		service.listSimons(null, new AsyncCallback<ArrayList<SimonValue>>() {
			@Override
			public void onSuccess(ArrayList<SimonValue> simonValues) {
				showSimons(simonValues);
			}

			@Override
			public void onFailure(Throwable throwable) {
				Window.alert("ERROR listSimons(): " + throwable.getMessage());
			}
		});
	}

	private void showSimons(ArrayList<SimonValue> simonValues) {
		contentPanel.clear();

		CellTable<SimonValue> table = new CellTable<SimonValue>();

		TextColumn<SimonValue> nameColumn = new TextColumn<SimonValue>() {
			@Override
			public String getValue(SimonValue simon) {
				return simon.name;
			}
		};

		TextColumn<SimonValue> meanColumn = new TextColumn<SimonValue>() {
			@Override
			public String getValue(SimonValue simon) {
				return Utils.presentNanoTime((long) simon.mean);
			}
		};

		table.addColumn(nameColumn, "Simon");
		table.addColumn(meanColumn, "Mean");

		ListDataProvider<SimonValue> dataProvider = new ListDataProvider<SimonValue>(simonValues);
		dataProvider.addDataDisplay(table);

		ColumnSortEvent.ListHandler<SimonValue> columnSortHandler = new ColumnSortEvent.ListHandler<SimonValue>(dataProvider.getList());
		columnSortHandler.setComparator(nameColumn, new Comparator<SimonValue>() {
				public int compare(SimonValue o1, SimonValue o2) {
					if (o1 == o2) {
						return 0;
					}
					if (o1 != null) {
						return (o2 != null) ? o1.name.compareTo(o2.name) : 1;
					}
					return -1;
				}
			});
		table.addColumnSortHandler(columnSortHandler);

		// We know that the data is sorted alphabetically by default.
		table.getColumnSortList().push(nameColumn);

		contentPanel.add(table);

//		for (SimonValue simonValue : simonValues) {
//			contentPanel.add(new Label(simonValue.name));
//		}
	}

	public GwimonServiceAsync getService() {
		return service;
	}
}