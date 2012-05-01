"use strict";
var javasimon=window.javasimon||{};
window.javasimon=javasimon;
/**
 * Filter controller
 */
javasimon.FilterController=function(oPatternText, oPatternHelp, oTypeSelect, oTimeFormatSelect) {
	this.oPatternText=oPatternText;
	this.oPatternHelp=oPatternHelp;
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
	arePattern:[
		new RegExp("^[\\*]?[-_\\[\\]A-Za-z0-9.,@$%()<>]+[\\*]?$"),
		new RegExp("^[-_\\[\\]A-Za-z0-9.,@$%()<>]+[\\*]?[-_\\[\\]A-Za-z0-9.,@$%()<>]+$")
	],
	fnValidate:function() {
		var sPattern=this.oPatternText.val();
		var bPatternValid=!sPattern // Empty or undefined is OK
			||this.arePattern[0].test(sPattern) // Starting and/or ending with *
			||this.arePattern[1].test(sPattern); // Containing single *
		if (bPatternValid) {
			// Valid pattern
			$(this.oPatternText).removeClass("error");
			$(this.oPatternHelp).addClass("hidden");
		} else {
			// Invalid pattern
			$(this.oPatternText).addClass("error");
			$(this.oPatternHelp).removeClass("hidden");
		}
		return bPatternValid;
	},
	fnExportCsv:function() {
		if (this.fnValidate()) {
			javasimon.TableService.fnGetDataAsCsv(this.fnGetVal());
		}
	},
	fnExportHtml:function() {
		if (this.fnValidate()) {
			javasimon.TableService.fnGetDataAsHtml(this.fnGetVal());
		}
	},
	fnResetAll:function(fnAjaxCallback) {
		if (this.fnValidate()) {
			var oFilterVal=this.fnGetVal();
			var oData={
				pattern:oFilterVal.sPattern,
				type:oFilterVal.sType
			};
			javasimon.ResetService.fnResetAll(oData, fnAjaxCallback);
		}
	}
};