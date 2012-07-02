"use strict";
var javasimon=window.javasimon;
if (javasimon) {
	(function(domUtil, detailViewService) {
		detailViewService.fnAddPluginRenderer("callTree",function(eTableBody, oCallTree) {
			var row=this.fnAppendRow(eTableBody);
			if (oCallTree.message) {
				this.fnAppendLabelValueCell(row,"Message", oCallTree.message, 3);
			}

			row=this.fnAppendRow(eTableBody);
			this.fnAppendLabelValueCell(row, "Threshold", oCallTree.logThreshold);

			row=this.fnAppendRow(eTableBody);
			this.fnAppendLabelCell(row,"Tree");
			this.fnAppendValueCell(row,"TODO");

		});
	}(javasimon.DOMUtil, javasimon.DetailViewService));
}

