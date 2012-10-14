"use strict";
var javasimon=window.javasimon;
if (javasimon) {
	(function(domUtil, viewPluginMgr) {
		viewPluginMgr.fnAddPluginRenderer("quantiles",function(eTableBody, oQuantiles) {
			var row=this.fnAppendRow(eTableBody),
				i,subTable,subRow, subTableSection, oBucket, 
				eBucketCell,nBucketWidth;
			if (oQuantiles.message) {
				this.fnAppendLabelValueCell(row,"Message", oQuantiles.message, 3);
			}

			row=this.fnAppendRow(eTableBody);
			this.fnAppendLabelValueCell(row, "Median", oQuantiles.median);
			this.fnAppendLabelValueCell(row, "90%", oQuantiles.percentile90);

			row=this.fnAppendRow(eTableBody);
			this.fnAppendLabelCell(row,"Distribution");
			subTable=domUtil.fnAppendChildElement(this.fnAppendValueCell(row, " ", 3),"table");
			if (oQuantiles.buckets && oQuantiles.buckets.length>0) {
				subTableSection=domUtil.fnAppendChildElement(subTable, "thead");
				subRow=this.fnAppendRow(subTable);
				this.fnAppendLabelCell(subRow, "Min");
				this.fnAppendLabelCell(subRow, "Max");
				this.fnAppendLabelCell(subRow, "Count");
				subTableSection=domUtil.fnAppendChildElement(subTable, "tbody");
				for(i=oQuantiles.buckets.length-1; i>=0; i--) {
					oBucket=oQuantiles.buckets[i];
					subRow=this.fnAppendRow(subTableSection);
					this.fnAppendCell(subRow, null, oBucket.min === 0 ?"0":oBucket.min);
					this.fnAppendCell(subRow, null, oBucket.max === 0 ?"0":oBucket.max);
					eBucketCell=this.fnAppendCell(subRow, null, null);
					if (oBucket.count) {
						if (oQuantiles.maxCount) {
							nBucketWidth = oBucket.count > 0 && oQuantiles.maxCount > 0 ? Math.round(oBucket.count * 200 / oQuantiles.maxCount) : 0;
							domUtil.fnAppendChildText(domUtil.fnAppendChildElement(eBucketCell,"div",{"class":"ui-widget-header ui-corner-right bar",style:"width:"+nBucketWidth+"px;"}),".");
						}
						domUtil.fnAppendChildText(eBucketCell,oBucket.count);
					}
				}
			}

		});
	}(javasimon.DOMUtil, javasimon.ViewPluginManager));
}

