"use strict";
var javasimon=window.javasimon;
if (javasimon) {
	(function(domUtil, detailViewService) {
		detailViewService.fnAddPluginRenderer("timeline",function(eTableBody, oTimeline) {
			var row=this.fnAppendRow(eTableBody),
				subTable, oDataTable;
			if (oTimeline.message) {
				this.fnAppendLabelValueCell(row,"Message", oTimeline.message, 3);
			}

			row=this.fnAppendRow(eTableBody);
			this.fnAppendLabelValueCell(row, "Capacity", oTimeline.capacity);
			this.fnAppendLabelValueCell(row, "Width", oTimeline.width);

			row=this.fnAppendRow(eTableBody);
			this.fnAppendLabelCell(row,"Evolution");
			subTable=domUtil.fnAppendChildElement(this.fnAppendValueCell(row, " ", 3),"table");
			oDataTable=$(subTable).dataTable( {
				bJQueryUI: true,
				bProcessing: true,
				bPaginate:false,bLengthChange:false,
				bFilter:false,bInfo:false,
				aoColumns: [
					{sTitle:"Start", mDataProp: "startTimestamp",bSearchable: false,sWidth: "10%",sClass:"numeric" },
					{sTitle:"End", mDataProp: "endTimestamp",bSearchable: false,sWidth: "10%",sClass:"numeric",asSorting: ["desc","asc" ] },
					{sTitle:"Count", mDataProp: "counter",bSearchable: false,sType:"numeric",sWidth: "5%",sClass:"numeric",asSorting: ["desc","asc" ] },
					{sTitle:"Total", mDataProp: "total",bSearchable: false,sType:"numeric",sWidth: "5%",sClass:"numeric",asSorting: ["desc","asc" ] },
					{sTitle:"Min", mDataProp: "min",bSearchable: false,sType:"numeric",sWidth: "5%",sClass:"numeric",asSorting: ["desc","asc" ] },
					{sTitle:"Mean", mDataProp: "mean",bSearchable: false,sType:"numeric",sWidth: "5%",sClass:"numeric",asSorting: ["desc","asc" ] },
					{sTitle:"Last", mDataProp: "last",bSearchable: false,sType:"numeric",sWidth: "5%",sClass:"numeric",asSorting: ["desc","asc" ] },
					{sTitle:"Max", mDataProp: "max",bSearchable: false,sType:"numeric",sWidth: "5%",sClass:"numeric",asSorting: ["desc","asc" ] },
					{sTitle:"StdDev", mDataProp: "standardDeviation",bSearchable: false,sType:"numeric",sWidth: "5%",sClass:"numeric",asSorting: ["desc","asc" ] }
				],
				aaSorting: [[ 0, "asc" ]]
			});
			oDataTable.fnAddData(oTimeline.timeRanges);
		});
	}(javasimon.DOMUtil, javasimon.DetailViewService));
}

