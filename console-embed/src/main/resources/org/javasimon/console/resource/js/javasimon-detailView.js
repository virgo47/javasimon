"use strict";
var javasimon=window.javasimon||{};
window.javasimon=javasimon;
(function(domUtil, pluginService, tns) {
	/**
	* Detail View
	*/
	tns.DetailView=function(eDiv, oSettings) {
		this.eDiv=eDiv;
		this.oSettings={
		};
		if (oSettings) {
			this.oSettings=javasimon.ObjectUtil.fnMerge(this.oSettings, oSettings, true);
		}
	};
	tns.DetailView.prototype={
		fnAppendSection:function(sId) {
			var eSectionDiv=domUtil.fnAppendChildElement(this.eDiv, "div", {id:sId, "class":"detailViewSection ui-widget-content ui-corner-all"}),
				eSectionTitle=domUtil.fnAppendChildElement(eSectionDiv, "div", {"class":"title ui-corner-top ui-widget-header"}),
				eSectionTable=domUtil.fnAppendChildElement(eSectionDiv, "table", {"class":"formTable"}),
				eSectionTableBody=domUtil.fnAppendChildElement(eSectionTable, "tbody");
			return {eTitle:eSectionTitle, eTableBody:eSectionTableBody};
		},
		fnAppendCell:function(eRow, sClass, sText, nColSpan) {
			var eCell=domUtil.fnAppendChildElement(eRow, "td", {"class":sClass});
			domUtil.fnAppendChildText(eCell, sText?sText:" ");
			if (nColSpan) {
				eCell.setAttribute("colspan",nColSpan);
			}
			return eCell;
		},
		fnAppendLabelCell:function(eRow, sLabel) {
			return this.fnAppendCell(eRow, "label", sLabel);
		},
		fnAppendValueCell:function(eRow, sValue, nColSpan) {
			return this.fnAppendCell(eRow, "value", sValue, nColSpan);
		},
		fnAppendLabelValueCell:function(eRow, sLabel, sValue, nColSpan) {
			this.fnAppendLabelCell(eRow, sLabel);
			this.fnAppendValueCell(eRow, sValue, nColSpan);
		},
		fnAppendSimonLabelValueCell:function(eRow, sLabel, sAttr, nColSpan) {
			this.fnAppendLabelValueCell(eRow, sLabel, this.oSimon[sAttr], nColSpan);
		},
		fnAppendRow:function(eTable) {
			return domUtil.fnAppendChildElement(eTable, "tr");
		},
		fnAppendSimonType:function(eParent) {
			var sType=this.oSimon.type,
				oType=domUtil.fnGetSimonType(sType);
			domUtil.fnAppendSimonTypeImage(eParent, sType);
//			domUtil.fnAppendChildText(eParent, oType.sLabel);
            domUtil.fnAppendChildText(
                domUtil.fnAppendChildElement(eParent,"span"),
                oType.sLabel
            );
		},
		fnRenderSimonDiv:function() {
			// Section Title
			var section=this.fnAppendSection("simonSection"),
				row,eTypeCell;
			domUtil.fnAppendChildText(section.eTitle, "Simon");
			// Table
			row=this.fnAppendRow(section.eTableBody);
			this.fnAppendSimonLabelValueCell(row, "Name", "name", 5);
			row=this.fnAppendRow(section.eTableBody);
			this.fnAppendLabelCell(row, "Type");
			eTypeCell=this.fnAppendValueCell(row);
			this.fnAppendSimonType(eTypeCell);
			this.fnAppendSimonLabelValueCell(row, "State", "state");
			this.fnAppendSimonLabelValueCell(row, "Enabled", "enabled");
			row=this.fnAppendRow(section.eTableBody);
			this.fnAppendSimonLabelValueCell(row, "Note", "note", 5);
			// Dates
			row=this.fnAppendRow(section.eTableBody);
			this.fnAppendSimonLabelValueCell(row, "First Use", "firstUsage");
			this.fnAppendSimonLabelValueCell(row, "Last Reset", "lastReset");
			this.fnAppendSimonLabelValueCell(row, "Last Use", "lastUsage");
		},
		fnRenderStopwatchDiv:function() {
			// Section Title
			var section=this.fnAppendSection("stopwatchSection"),
				row;
			this.fnAppendSimonType(section.eTitle);
			// Table
			row=this.fnAppendRow(section.eTableBody);
			this.fnAppendSimonLabelValueCell(row, "Counter", "counter");
			this.fnAppendSimonLabelValueCell(row, "Total", "total");
			row=this.fnAppendRow(section.eTableBody);
			this.fnAppendSimonLabelValueCell(row, "Min", "min");
			this.fnAppendSimonLabelValueCell(row, "Min Timestamp", "minTimeStamp");
			row=this.fnAppendRow(section.eTableBody);
			this.fnAppendSimonLabelValueCell(row, "Mean", "mean");
			this.fnAppendSimonLabelValueCell(row, "Standard Deviation", "standardDeviation");
			row=this.fnAppendRow(section.eTableBody);
			this.fnAppendSimonLabelValueCell(row, "Max", "max");
			this.fnAppendSimonLabelValueCell(row, "Max Timestamp", "maxTimeStamp");
			row=this.fnAppendRow(section.eTableBody);
			this.fnAppendSimonLabelValueCell(row, "Last", "last");
			this.fnAppendSimonLabelValueCell(row, "Last Timestamp", "lastUsage");
			row=this.fnAppendRow(section.eTableBody);
			this.fnAppendSimonLabelValueCell(row, "Max Active", "maxActive");
			this.fnAppendSimonLabelValueCell(row, "Max Active Timestamp", "maxActiveTimestamp");
		},
		fnRenderCounterDiv:function() {
			// Section Title
			var section=this.fnAppendSection("counterSection"),
				row;
			this.fnAppendSimonType(section.eTitle);
			// Table
			row=this.fnAppendRow(section.eTableBody);
			this.fnAppendSimonLabelValueCell(row, "Counter", "counter", 3);
			row=this.fnAppendRow(section.eTableBody);
			this.fnAppendSimonLabelValueCell(row, "Min", "min");
			this.fnAppendSimonLabelValueCell(row, "Min Timestamp", "minTimeStamp");
			row=this.fnAppendRow(section.eTableBody);
			this.fnAppendSimonLabelValueCell(row, "Max", "max");
			this.fnAppendSimonLabelValueCell(row, "Max Timestamp", "maxTimeStamp");
			row=this.fnAppendRow(section.eTableBody);
			this.fnAppendSimonLabelValueCell(row, "Increment Sum", "incrementSum");
			this.fnAppendSimonLabelValueCell(row, "Decrement Sum", "decrementSum");
		},
		fnRenderPluginDiv:function(oPlugin) {
			// Section Title
			var sPluginId=oPlugin.id,
				section=this.fnAppendSection(sPluginId + "Section");
			domUtil.fnAppendChildText(section.eTitle, oPlugin.label);
			// Table
			tns.ViewPluginManager.fnAddPluginData(
				oPlugin,
				this,
				section.eTableBody
			);
		},
		fnRender: function() {
			this.fnRenderSimonDiv();
			if (this.oSimon.type==="STOPWATCH") {
				this.fnRenderStopwatchDiv();
			} else if (this.oSimon.type==="COUNTER") {
				this.fnRenderCounterDiv();
			}
			if (this.oSimon.plugins) {
				for(var i=0;i<this.oSimon.plugins.length;i++) {
					this.fnRenderPluginDiv(this.oSimon.plugins[i]);
				}
			}
		},
		fnSetData:function(oSimon) {
			domUtil.fnRemoveChildren(this.eDiv);
			this.oSimon=oSimon;
			this.fnRender();
		}
	};
}(javasimon.DOMUtil, javasimon.PluginService, javasimon));
