"use strict";
var javasimon=window.javasimon||{};
window.javasimon=javasimon;
/**
 * Service to reset Simons through Ajax HTTP requests
 */
javasimon.ResetService={
	sUrl:"data/reset",
	/**
	 * Reset one or more Simons. A confirmation dialog appears before.
	 * @param oData {object} Filter
	 * @param fnAjaxCallback {function} Callback
	 */
	fnResetAll: function(oData, fnAjaxCallback) {
		if (window.confirm("Are you sure you want to reset all monitors?")) {
			this.fnReset(oData, fnAjaxCallback);
		}		
	},
	/**
	 * Reset one Simons. 
	 * @param sName {string} Simon name
	 * @param fnAjaxCallback {function} Callback
	 */
	fnResetOne: function(sName, fnAjaxCallback) {
		this.fnReset({name:sName}, fnAjaxCallback);
	},
	/**
	 * Reset one or more Simons. 
	 * @param oData {object} Filter
	 * @param fnAjaxCallback {function} Callback
	 */
	fnReset: function(oData, fnAjaxCallback) {
		$.post(this.sUrl, oData, fnAjaxCallback);
	}
};
/**
 * Service to get Simon data as flat list through Ajax HTTP requests
 */
javasimon.TableService={
	sUrl:"data/table",
	fnGetDataAsXxx:function(sExt, oParam) {
		var lsUrl=this.sUrl+"."+sExt+"?";
		if (oParam.sPattern) {
			lsUrl = lsUrl + "pattern=" + oParam.sPattern;
		}
		if (oParam.sPattern) {
			lsUrl = lsUrl + "type=" + oParam.sType;
		}
		window.location.href=lsUrl;
	},
	fnGetDataAsCsv:function(oParam) {
		this.fnGetDataAsXxx("csv", oParam);
	},
	fnGetDataAsHtml:function(oParam) {
		this.fnGetDataAsXxx("html", oParam);
	},
	fnGetDataAsJson:function(oParam, fnAjaxCallback) {
		$.ajax( {
			url: this.sUrl+".json",
			data: oParam,
			success: fnAjaxCallback,
			dataType: "json"
		});
	}
};
/**
 * Service to get Simon hierchical, tree-style model through Ajax HTTP requests
 */
javasimon.TreeService={
	sUrl:"data/tree",
	fnGetDataAsXxx:function(sExt, oParam) {
		var lsUrl=this.sUrl+"."+sExt+"?";
		window.location.href=lsUrl;
	},
	fnGetDataAsXml:function(oParam) {
		this.fnGetDataAsXxx("xml", oParam);
	},
	fnGetDataAsJson:function(oParam, fnAjaxCallback) {
		$.ajax( {
			url: this.sUrl+".json",
			data: oParam,
			success: fnAjaxCallback,
			dataType: "json"
		});
	}
};
