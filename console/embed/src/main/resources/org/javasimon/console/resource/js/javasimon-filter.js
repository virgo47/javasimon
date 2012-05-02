"use strict";
var javasimon=window.javasimon||{};
window.javasimon=javasimon;
/**
 * Filter controller
 */
javasimon.FilterController=function(
		oPatternText, oPatternHelp, 
		oStopwatchTypeCheck,oCounterTypeCheck,oUnknownTypeCheck,
		oTimeFormatSelect) {
	this.oPatternText=oPatternText;
	this.oPatternHelp=oPatternHelp;
	this.oStopwatchTypeCheck=oStopwatchTypeCheck;
	this.oCounterTypeCheck=oCounterTypeCheck;
	this.oUnknownTypeCheck=oUnknownTypeCheck;
	this.oTypeChecks=[oStopwatchTypeCheck,oCounterTypeCheck,oUnknownTypeCheck];
	this.oTimeFormatSelect=oTimeFormatSelect;
};
javasimon.FilterController.prototype={
	fnSetValFromUrlParams:function() {
		var type=$(document).getUrlParam('type');
		var types;
		if (!type) {
			types=javasimon.SettingsService.get("asTypes");
		} else if (typeof type=="string") {
			types=[type];
		} else {
			types=type;
		}
		var timeFormat=$(document).getUrlParam('timeFormat');
		if (!timeFormat) {
			timeFormat=javasimon.SettingsService.get("sTimeFormat");
		}
		this.fnSetVal({
			sPattern:$(document).getUrlParam('pattern'),
			asTypes:types,  
			sTimeFormat:  timeFormat
		});
	},
	fnSetVal:function(oVal) {
		this.oPatternText.val(oVal.sPattern||"");
		if (oVal.asTypes) {
			this.fnSetTypeChecks(oVal.asTypes);
		} else {
			this.fnResetTypeChecks();
		}
		this.oTimeFormatSelect.val(oVal.sTimeFormat||"");
	},
	fnSetTypeChecks:function(asTypes) {
		for(var i=0;i<this.oTypeChecks.length;i++) {
			this.oTypeChecks[i].attr("checked",$.inArray(this.oTypeChecks[i].val(), asTypes)>=0);
		}
	},
	fnResetTypeChecks:function() {
		this.fnSetTypeChecks(["STOPWATCH","COUNTER"]);
	},
	fnGetVal:function() {
		var sPattern=this.oPatternText.val();
		var asTypes=[];
		for(var i=0;i<this.oTypeChecks.length;i++) {
			if (this.oTypeChecks[i].attr("checked")) {
				asTypes.push(this.oTypeChecks[i].val());
			}
		}
		var sTimeFormat=this.oTimeFormatSelect.val();
		return {
			sPattern:sPattern||"",
			asTypes:asTypes,
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
		var bTypeValid=false;
		if (bPatternValid) {
			// Valid pattern
			$(this.oPatternText).removeClass("error");
			$(this.oPatternHelp).addClass("hidden");
		} else {
			// Invalid pattern
			$(this.oPatternText).addClass("error");
			$(this.oPatternHelp).removeClass("hidden");
		}
		// At least one type
		for(var i=0; i<this.oTypeChecks.length && !bTypeValid; i++) {
			bTypeValid=this.oTypeChecks[i].attr("checked");
		}		
		if (!bTypeValid) {
			this.fnResetTypeChecks();
		}
		return bPatternValid;
	},
	fnWithVal:function(oCallbackContext, fnCallback) {
		var oVal;
		if (this.fnValidate()) {
			oVal=this.fnGetVal();
			fnCallback.call(oCallbackContext, oVal);
			javasimon.SettingsService.set(oVal);
			javasimon.SettingsService.save(document);
		}
	},
	fnExportCsv:function() {
		this.fnWithVal(
			this, function(oVal){
				javasimon.TableService.fnGetDataAsCsv(oVal)
			});
	},
	fnExportHtml:function() {
		this.fnWithVal(
			this, function(oVal){
				javasimon.TableService.fnGetDataAsHtml(oVal)
			});
	},
	fnResetAll:function(fnAjaxCallback) {
		this.fnWithVal(
			this, function(oVal){
				var oData={
					pattern:oVal.sPattern,
					type:oVal.sType
				};
				javasimon.ResetService.fnResetAll(oData, fnAjaxCallback);
			});
	}
};