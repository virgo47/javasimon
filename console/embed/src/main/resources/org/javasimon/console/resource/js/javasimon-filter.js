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
	fnGoToUrl:function(sUrl) {
		var oVal=this.fnGetVal();
		window.location.href=sUrl+"?pattern="+oVal.sPattern+"&type="+oVal.sType;
	},
	fnAjaxPost:function(sUrl, fnCallback) {
		var oVal=this.fnGetVal();
		$.post(sUrl, {pattern:oVal.sPattern, type:oVal.sType}, fnCallback);
	}
};