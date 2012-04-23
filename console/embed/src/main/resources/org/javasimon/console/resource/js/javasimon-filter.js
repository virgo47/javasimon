"use strict";
var javasimon=window.javasimon||{};
window.javasimon=javasimon;
/**
 * Filter controller
 */
javasimon.FilterController=function(oPatternText, oTypeSelect, oTimeFormatSelect) {
	this.oPatternText=oPatternText;
	this.oTypeSelect=oTypeSelect;
	this.oTimeFormatSelect=oTimeFormatSelect;
};
javasimon.FilterController.prototype={
	fnSetValFromUrlParams:function() {
		this.fnSetVal({
			sPattern:$(document).getUrlParam('pattern'),
			sType:$(document).getUrlParam('type'),  
			sTimeFormat:$(document).getUrlParam('timeFormat')  
		});
	},
	fnSetVal:function(oVal) {
		this.oPatternText.val(oVal.sPattern||"");
		this.oTypeSelect.val(oVal.sType||"");
		this.oTimeFormatSelect.val(oVal.sTimeFormat||"");
	},
	fnGetVal:function() {
		var sPattern=this.oPatternText.val();
		var sType=this.oTypeSelect.val();
		var sTimeFormat=this.oTimeFormatSelect.val();
		return {
			sPattern:sPattern||"",
			sType:sType||"",
			sTimeFormat:sTimeFormat||""
		};
	},
	fnExportCsv:function() {
		javasimon.TableService.fnGetDataAsCsv(this.fnGetVal());
	},
	fnExportHtml:function() {
		javasimon.TableService.fnGetDataAsHtml(this.fnGetVal());
	},
	fnResetAll:function(fnAjaxCallback) {
		var oFilterVal=this.fnGetVal();
		var oData={
			pattern:oFilterVal.sPattern,
			type:oFilterVal.sType
		};
		javasimon.ResetService.fnResetAll(oData, fnAjaxCallback);
	}
};