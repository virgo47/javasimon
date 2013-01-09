"use strict";
var javasimon=window.javasimon;
if (javasimon) {
	(function(domUtil, viewPluginMgr) {
		viewPluginMgr.fnAddPluginRenderer("timeline",function(eTableBody, oTimeline) {
			var row=this.fnAppendRow(eTableBody), cell,
				subTable, oDataTable, googleChartDiv;
			if (oTimeline.message) {
				this.fnAppendLabelValueCell(row,"Message", oTimeline.message, 3);
			}

			row=this.fnAppendRow(eTableBody);
			this.fnAppendLabelValueCell(row, "Capacity", oTimeline.capacity);
			this.fnAppendLabelValueCell(row, "Width", oTimeline.width);

			row=this.fnAppendRow(eTableBody);
			this.fnAppendLabelCell(row,"Table");
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
			
			if (google) {
				row=this.fnAppendRow(eTableBody);
				this.fnAppendLabelCell(row,"Chart");
				if (oTimeline.timeRanges.length>0) {
				    cell=this.fnAppendValueCell(row, " ", 3)
                    googleChartDiv=domUtil.fnAppendChildElement(cell,"div",{style:"width: 800px; height: 400px;"});
                    google.load("visualization", "1.0", {packages:["corechart"],
                        callback:function() {
                        // Prepare data for Google Chart
                        var googleData=[], googleChart,
                            aTimeRanges=oTimeline.timeRanges;
                        googleData.push(["Timestamp","Min","Mean","Max"]);
                        for(var i=0;i<aTimeRanges.length;i++) {
                            googleData.push([
                                aTimeRanges[i].startTimestamp,
                                aTimeRanges[i].min,
                                aTimeRanges[i].mean,
                                aTimeRanges[i].max
                            ]);
                        }
                        // Configure Google Chart
                        googleChart=new google.visualization.LineChart(googleChartDiv);
                        googleChart.draw(
                            google.visualization.arrayToDataTable(googleData),
                            {colors: ['#80B646', '#49A4CB', '#C93B3B']});
                    }});
                } else {
                    cell=this.fnAppendValueCell(row, "No data available in chart", 3)
                }
			}
			
		});
	}(javasimon.DOMUtil, javasimon.ViewPluginManager));
}

