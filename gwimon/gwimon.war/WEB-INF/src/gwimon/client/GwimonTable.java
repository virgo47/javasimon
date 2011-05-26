package gwimon.client;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.view.client.ListDataProvider;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * GwimonTable.
 *
 * @author virgo47@gmail.com
 */
public class GwimonTable extends CellTable<SimonValue> {
	private ListDataProvider<SimonValue> dataProvider;
	private List<SimonColumn> hiddenColumns = new ArrayList<SimonColumn>();

	public GwimonTable(List<SimonValue> simonValues) {
		dataProvider = new ListDataProvider<SimonValue>(simonValues);
		dataProvider.addDataDisplay(this);

		addColumn("Simon", true, 400, new StringColumnCallback() {
			@Override
			public String getText(SimonValue simon) {
				return simon.name;
			}
		});
		addColumn("Cnt", true, 70, new ColumnCallback() {
			@Override
			public int compare(SimonValue o1, SimonValue o2) {
				return (o1.counter < o2.counter) ? -1 : (o1.counter == o2.counter ? 0 : 1);
			}

			@Override
			public String getText(SimonValue simon) {
				return String.valueOf(simon.counter);
			}
		});
		addColumn("Total", true, 90, new NsColumnCallback() {
			@Override
			protected long getValue(SimonValue simon) {
				return simon.total;
			}
		});
		addColumn("Max", true, 90, new NsColumnCallback() {
			@Override
			protected long getValue(SimonValue simon) {
				return simon.max;
			}
		});
		addColumn("Min", true, 90, new NsColumnCallback() {
			@Override
			protected long getValue(SimonValue simon) {
				return simon.min;
			}
		});
		addColumn("Mean", true, 90, new NsColumnCallback() {
			@Override
			protected long getValue(SimonValue simon) {
				return (long) simon.mean;
			}
		});
		addColumn("Note", false, 600, new StringColumnCallback() {
			@Override
			public String getText(SimonValue simon) {
				return simon.note;
			}
		});

//		getColumnSortList().push(nameColumn);
	}

	private abstract class SimonColumn extends TextColumn<SimonValue> {
		private String header;

		public SimonColumn(String header) {
			this.header = header;
		}

		public String getHeader() {
			return header;
		}
	}

	private SimonColumn addColumn(String header, boolean primary, int pxWidth, final ColumnCallback columnCallback) {
		SimonColumn column = new SimonColumn(header) {
			@Override
			public String getValue(SimonValue simon) {
				return columnCallback.getText(simon);
			}
		};
		column.setSortable(true);
		ColumnSortEvent.ListHandler<SimonValue> nameSortHandler = new ColumnSortEvent.ListHandler<SimonValue>(dataProvider.getList());
		nameSortHandler.setComparator(column, new Comparator<SimonValue>() {
			public int compare(SimonValue o1, SimonValue o2) {
				if (o1 == o2) {
					return 0;
				}
				if (o1 != null) {
					return (o2 != null) ? columnCallback.compare(o1, o2) : 1;
				}
				return -1;
			}
		});
		addColumnSortHandler(nameSortHandler);
		if (primary) {
			addColumn(column, header);
		} else {
			hiddenColumns.add(column);
		}
		setColumnWidth(column, pxWidth, com.google.gwt.dom.client.Style.Unit.PX);
		return column;
	}

	public void toggleColumns() {
		List<SimonColumn> justHidden = new ArrayList<SimonColumn>();
		for (int i = 1; i < getColumnCount(); i++) {
			justHidden.add((SimonColumn) getColumn(i));
		}
		for (SimonColumn column : justHidden) {
			removeColumn(column);
		}
		for (SimonColumn hiddenColumn : hiddenColumns) {
			addColumn(hiddenColumn, hiddenColumn.getHeader());
		}
		hiddenColumns = justHidden;
		redraw();
		redrawHeaders();
	}

	private interface ColumnCallback {
		// Will not be null, just compare the two
		int compare(SimonValue o1, SimonValue o2);

		// Give me String value.
		String getText(SimonValue simon);
	}

	private abstract class NsColumnCallback implements ColumnCallback {
		@Override
		public int compare(SimonValue o1, SimonValue o2) {
			return (getValue(o1) < getValue(o2)) ? -1 : (getValue(o1) == getValue(o2) ? 0 : 1);
		}

		@Override
		public String getText(SimonValue simon) {
			return Utils.presentNanoTime(getValue(simon));
		}

		protected abstract long getValue(SimonValue simon);
	}

	private abstract class StringColumnCallback implements ColumnCallback {
		@Override
		public int compare(SimonValue o1, SimonValue o2) {
			return getText(o1).compareTo(getText(o2));
		}
	}
}
