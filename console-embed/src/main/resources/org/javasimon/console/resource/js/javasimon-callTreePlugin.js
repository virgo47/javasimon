"use strict";
var javasimon=window.javasimon;
if (javasimon) {
	(function(domUtil, viewPluginMgr) {
		viewPluginMgr.fnAddPluginRenderer("callTree",function(eTableBody, oCallTree) {
			var row, eTreeTable,oDataTreeTable;
			if (oCallTree.message) {
				row=this.fnAppendRow(eTableBody);
				this.fnAppendLabelValueCell(row,"Message", oCallTree.message, 3);
			}
			if (oCallTree.logThreshold) {
				row=this.fnAppendRow(eTableBody);
				this.fnAppendLabelValueCell(row, "Threshold", oCallTree.logThreshold);
			}
			if (oCallTree.rootNode) {
				row=this.fnAppendRow(eTableBody);
				this.fnAppendLabelCell(row,"Tree");
				eTreeTable=domUtil.fnAppendChildElement(
					this.fnAppendValueCell(row, null, 1), 
					"table", 
					{id:"callDataTreeTable", style:"width:auto","class":"dataTreeTable"}
				);
				oDataTreeTable=new javasimon.DataTreeTable(
					eTreeTable,{
						aoColumns:[
							{sTitle:"Name",		sField:"name", 		sClass:'string headCell'},
							{sTitle:"Percent",	sField:"percent",	sClass:"number"},
							{sTitle:"Total",	sField:"total",		sClass:"number"},
							{sTitle:"Count",	sField:"splitCount",sClass:"number"}
						]
					}
				);
				oDataTreeTable.fnSetRootData(oCallTree.rootNode);
				oDataTreeTable.fnDrawHeader();
				oDataTreeTable.fnDraw();
			}
		});
	}(javasimon.DOMUtil, javasimon.ViewPluginManager));
}

