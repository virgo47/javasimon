"use strict";
var javasimon=window.javasimon||{};
window.javasimon=javasimon;
(function($, tns) {
	tns.fnDefaultAjaxErrorCallback=function(content) {
		window.alert("Server request failed:" +content);
	};
	/**
	* Service to reset Simons through Ajax HTTP requests
	*/
	tns.ResetService={
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
			$.post(this.sUrl, oData, fnAjaxCallback, tns.fnDefaultAjaxErrorCallback);
		}
	};
	/**
	* Service to clear Simons through Ajax HTTP requests
	*/
	tns.ClearService={
		sUrl:"data/clear",
		/**
		* Clear all simons. A confirmation dialog appears before.
		* @param fnAjaxCallback {function} Callback
		*/
		fnClearAll: function(fnAjaxCallback) {
			if (window.confirm("Are you sure you want to clear the Manager? This will delete all the monitors.")) {
				this.fnClear(fnAjaxCallback);
			}
		},
		/**
		* Clear all Simons.
		* @param fnAjaxCallback {function} Callback
		*/
		fnClear: function(fnAjaxCallback) {
			$.post(this.sUrl, {}, fnAjaxCallback, tns.fnDefaultAjaxErrorCallback);
		}
	};
	/**
	* Service to get Simon data as flat list through Ajax HTTP requests
	*/
	tns.TableService={
		sUrl:"data/table",
		fnGetDataAsXxx:function(sExt, oParam) {
			var lsUrl=this.sUrl+"."+sExt+"?_";
			if (oParam.sPattern) {
				lsUrl = lsUrl + "&pattern=" + oParam.sPattern;
			}
			if (oParam.asTypes) {
				for(var i=0; i<oParam.asTypes.length; i++) {
					lsUrl = lsUrl + "&type=" + oParam.asTypes[i];
				}
			}
			if (oParam.sTimeFormat) {
				lsUrl = lsUrl + "&timeFormat=" + oParam.sTimeFormat;
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
				error: tns.fnDefaultAjaxErrorCallback,
				dataType: "json"
			});
		}
	};
	/**
	* Service to get Simon hierchical, tree-style model through Ajax HTTP requests
	*/
	tns.TreeService={
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
				error: tns.fnDefaultAjaxErrorCallback,
				dataType: "json"
			});
		}
	};
	/**
	* Service to get Simon detailed model through Ajax HTTP requests
	*/
	tns.DetailService={
		sUrl:"data/detail",
		fnGetDataAsXxx:function(sExt, oParam) {
			var lsUrl=this.sUrl+"."+sExt+"?";
			window.location.href=lsUrl;
		},
		fnGetDataAsJson:function(oParam, fnAjaxCallback) {
			$.ajax( {
				url: this.sUrl+".json",
				data: oParam,
				success: fnAjaxCallback,
				error: tns.fnDefaultAjaxErrorCallback,
				dataType: "json"
			});
		}
	};
	/**
	* Service to store/load settings as cookies
	*/
	tns.SettingsService={
		sVersion:"3.5",
		/**
		* Object representing settings
		*/
		oSettings: {
			asTypes: ["STOPWATCH","COUNTER"],
			sTimeFormat:"MILLISECOND",
			iDataTableLength:25
		},
		asConstants:[
			["STOPWATCH",	"S"],
			["COUNTER",	"C"],
			["UNKNOWN",	"U"],
			["NANOSECOND",	"NS"],
			["MICROSECOND", "US"],
			["MILLISECOND", "MS"],
			["SECOND",	" S"]
		],
		getConstant:function(sIn, sOut) {
			for(var i=0;i<this.asConstants.length;i++) {
				if        (sIn  && sIn ===this.asConstants[i][0]) {
					// Forward
					return this.asConstants[i][1];
				} else if (sOut && sOut===this.asConstants[i][1]) {
					// Backward
					return this.asConstants[i][0];
				}
			}
			return undefined;
		},
		/**
		* Load settings from a cookie
		*/
		load:function(oDocument) {
			// Read cookie
			oDocument = oDocument || document;
			if (!oDocument.cookie) {
				return;
			}
			var sCookieValue;
			var asCookieValueParts=oDocument.cookie.split(";");
			for(var i=0; i<asCookieValueParts.length && !sCookieValue; i++) {
				var iPrefixPos=asCookieValueParts[i].indexOf("JSimon=");
				if (iPrefixPos>=0) {
					sCookieValue=asCookieValueParts[i].substring(iPrefixPos+7, asCookieValueParts[i].length);
				}
			}
			if (!sCookieValue) {
				return;
			}
			// Parse cookie value
			var asTypes=[];
			var sType, sTimeFormat, iDataTableLength;
			if (sCookieValue) {
				var asCookieValue=sCookieValue.split(",");
				// Version
				if (asCookieValue.length>=4 && asCookieValue[0]===this.sVersion) {
					// Types
					for(var i=0;i<asCookieValue[1].length;i++) {
						sType=this.getConstant(undefined, asCookieValue[1].charAt(i));
						if (sType) {
							asTypes.push(sType);
						}
					}
					if (asTypes.length>0) {
						this.oSettings.asTypes=asTypes;
					}
					// Time format
					sTimeFormat=this.getConstant(undefined, asCookieValue[2]);
					if (sTimeFormat) {
						this.oSettings.sTimeFormat=sTimeFormat;
					}
					// Data table length
					iDataTableLength=asCookieValue[3];
					if (iDataTableLength) {
						this.oSettings.iDataTableLength=iDataTableLength;
					}
				}
			}
		},
		/**
		* Save settings into a cookie
		*/
		save:function(oDocument) {
			oDocument = oDocument || document;
			var sCookieValuePart;
			// Version
			var sCookieValue=this.sVersion;
			// Expiration date J+30
			var dCookieExpire = new Date();
			dCookieExpire.setTime(dCookieExpire.getTime()+(30*24*60*60*1000));
			// Type
			sCookieValue=sCookieValue+",";
			for(var i=0;i<this.oSettings.asTypes.length;i++) {
				sCookieValuePart=this.getConstant(this.oSettings.asTypes[i], undefined);
				if (sCookieValuePart) {
					sCookieValue=sCookieValue+sCookieValuePart;
				}
			}
			// Time format
			sCookieValue=sCookieValue+",";
			sCookieValuePart=this.getConstant(this.oSettings.sTimeFormat, undefined);
			if (sCookieValuePart) {
				sCookieValue=sCookieValue+sCookieValuePart;
			}
			// Data table length
			sCookieValue=sCookieValue+",";
			if (this.oSettings.iDataTableLength) {
				sCookieValue=sCookieValue+this.oSettings.iDataTableLength;
			}
			// Write cookie
			sCookieValue = "JSimon="+sCookieValue+"; expires="+dCookieExpire.toGMTString()+"; path=/";
			oDocument.cookie=sCookieValue;
			return sCookieValue;
		},
		/**
		* Set some settings
		*/
		set:function(oSettings) {
			for(var sProp in oSettings) {
				if (this.oSettings[sProp]) {
					this.oSettings[sProp]=oSettings[sProp];
				}
			}
		},
		/**
		* Get settings
		*/
		get:function(sName) {
			if (sName) {
				return this.oSettings[sName];
			} else {
				return this.oSettings;
			}
		}
	};
	tns.SettingsService.load(document);
})(jQuery, javasimon);
