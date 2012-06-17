"use strict";
var javasimon=window.javasimon;
if (javasimon) {
	(function(domUtil, detailViewService) {
		detailViewService.fnAddPluginRenderer("quantiles",function(eTableBody, oQuantiles) {
			var row=this.fnAppendRow(eTableBody),
				i,subTable,subRow, subTableSection, oBucket;
			if (oQuantiles.message) {
				this.fnAppendLabelValueCell(row,"Message", oQuantiles.message, 3);
			}

			row=this.fnAppendRow(eTableBody);
			this.fnAppendLabelValueCell(row, "Median", oQuantiles.median);
			this.fnAppendLabelValueCell(row, "90%", oQuantiles.percentile90);

			row=this.fnAppendRow(eTableBody);
			this.fnAppendLabelCell(row,"Distribution");
			subTable=domUtil.fnAppendChildElement(this.fnAppendValueCell(row, " ", 3),"table");
			subTableSection=domUtil.fnAppendChildElement(subTable, "thead");
			subRow=this.fnAppendRow(subTable);
			this.fnAppendLabelCell(subRow, "Min");
			this.fnAppendLabelCell(subRow, "Max");
			this.fnAppendLabelCell(subRow, "Count");
			subTableSection=domUtil.fnAppendChildElement(subTable, "tbody");
			if (oQuantiles.buckets) {
				for(i=0; i<oQuantiles.buckets.length; i++) {
					oBucket=oQuantiles.buckets[i];
					subRow=this.fnAppendRow(subTableSection);
					this.fnAppendCell(subRow, null, oBucket.min);
					this.fnAppendCell(subRow, null, oBucket.max);
					this.fnAppendCell(subRow, null, oBucket.count);
				}
			}

		});
	}(javasimon.DOMUtil, javasimon.DetailViewService));
}

