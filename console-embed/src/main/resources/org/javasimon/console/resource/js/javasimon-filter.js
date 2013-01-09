"use strict";
var javasimon=window.javasimon||{};
window.javasimon=javasimon;
/**
 * Filter controller
 */
javasimon.FilterController=function(
		oPatternText, oPatternHelp,
		oStopwatchTypeCheck,oCounterTypeCheck,oUnknownTypeCheck,oTypeChecks,
		oTimeFormatSelect) {
	this.oPatternText=oPatternText;
	var ctrl=this,
		fnFilterFunc=function() {
		ctrl.fnFilter();
	};
	$(this.oPatternText).blur(fnFilterFunc);
	this.oPatternHelp=oPatternHelp;
	this.oStopwatchTypeCheck=oStopwatchTypeCheck;
	this.oCounterTypeCheck=oCounterTypeCheck;
	this.oUnknownTypeCheck=oUnknownTypeCheck;
	this.oTypeChecks=oTypeChecks;
	this.aoTypeChecks=[oStopwatchTypeCheck,oCounterTypeCheck,oUnknownTypeCheck];
	for(var i=0;i<this.aoTypeChecks.length;i++) {
		$(this.aoTypeChecks[i]).change(fnFilterFunc);
	}
	this.oTimeFormatSelect=oTimeFormatSelect;
	$(this.oTimeFormatSelect).change(fnFilterFunc);
	this.bNoteColumnVis = false;
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
	fnIsTypeChecked:function(iTypeIndex) {
		return ($(this.aoTypeChecks[iTypeIndex]).attr("checked")==="checked");
	},
	fnSetTypeChecks:function(asTypes) {
		for(var i=0;i<this.aoTypeChecks.length;i++) {
			this.aoTypeChecks[i].attr("checked",($.inArray(this.aoTypeChecks[i].val(), asTypes)<0)?undefined:"checked");
		}
	},
	fnResetTypeChecks:function() {
		this.fnSetTypeChecks(["STOPWATCH","COUNTER"]);
	},
	fnGetVal:function() {
		var sPattern=this.oPatternText.val();
		var asTypes=[];
		for(var i=0;i<this.aoTypeChecks.length;i++) {
			if (this.fnIsTypeChecked(i)) {
				asTypes.push(this.aoTypeChecks[i].val());
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
		new RegExp("^\\*?[-_\\[\\]A-Za-z0-9.,@$%()<>]+\\*?$"),
		new RegExp("^[-_\\[\\]A-Za-z0-9.,@$%()<>]?\\*?[-_\\[\\]A-Za-z0-9.,@$%()<>]?$")
	],
	fnSetElementClass: function(oElement, sClass, bEnabled) {
		if (bEnabled) {
			$(oElement).addClass(sClass);
		} else {
			$(oElement).removeClass(sClass);
		}
	},
	fnValidate:function() {
		// Check pattern
		var sPattern=this.oPatternText.val();
		var bPatternValid=!sPattern // Empty or undefined is OK
			||this.arePattern[0].test(sPattern) // Starting and/or ending with *
			||this.arePattern[1].test(sPattern); // Containing single *
		this.fnSetElementClass(this.oPatternText, "error", !bPatternValid);
		this.fnSetElementClass(this.oPatternHelp, "hidden", bPatternValid);
		// Check type
		var bTypeValid=false;
		for(var i=0; i<this.aoTypeChecks.length; i++) {
			bTypeValid = bTypeValid || this.fnIsTypeChecked(i);
		}
		this.fnSetElementClass(this.oTypeChecks, "error", !bTypeValid);
		return bPatternValid&&bTypeValid;
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
	fnFilter:function() {
		this.fnWithVal(
			this, function(oVal){
				this.oDataTable.fnReloadAjax();
			});
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
	},
	fnToggleNoteColumnVis:function() {
		this.bNoteColumnVis = !this.bNoteColumnVis;
		this.oDataTable.fnSetColumnVis(0, !this.bNoteColumnVis, false);
		this.oDataTable.fnSetColumnVis(1,  this.bNoteColumnVis, true);
	}
};