package gwimon.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextBox;

/**
 * FilterForm.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public abstract class FilterForm extends Composite {
	private TextBox filterMask;

	public FilterForm() {
		FlexTable panel = new FlexTable();
		panel.addStyleName("top-form");

		panel.setText(0, 0, "Simon mask (regex):");

		filterMask = new TextBox();
		panel.setWidget(0, 1, filterMask);

		Button refreshButton = new Button("Refresh");
		refreshButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				callRefresh();
			}
		});
		panel.setWidget(0, 2, refreshButton);
		Button toggleColsButton = new Button("Toggle Columns");
		toggleColsButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				callToggleColumns();
			}
		});
		panel.setWidget(0, 3, toggleColsButton);
		initWidget(panel);
	}

	public String getFilterMask() {
		return filterMask.getValue();
	}

	public abstract void callToggleColumns();

	public abstract void callRefresh();
}
