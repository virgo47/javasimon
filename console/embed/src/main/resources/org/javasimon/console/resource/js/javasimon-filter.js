"use strict";
var javasimon=window.javasimon||{};
window.javasimon=javasimon;
/**
 * Filter controller
 */
javasimon.FilterController=function(oPatternText, oTypeSelect) {
	this.oPatternText=oPatternText;
	this.oTypeSelect=oTypeSelect;
};
javasimon.FilterController.prototype={
	fnSetValFromUrlParams:function() {
		this.fnSetVal({
			sPattern:$(document).getUrlParam('pattern'),
			sType:$(document).getUrlParam('type')  
		});
	},
	fnSetVal:function(oVal) {
		this.oPatternText.val(oVal.sPattern||"");
		this.oTypeSelect.val(oVal.sType||"");
	},
	fnGetVal:function() {
		var sPattern=this.oPatternText.val();
		var sType=this.oTypeSelect.val();
		return {
			sPattern:sPattern||"",
			sType:sType||""
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