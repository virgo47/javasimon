"use strict";
var javasimon = window.javasimon || {};
window.javasimon = javasimon;
/**
 * Auto refresh controller
 */
javasimon.AutoRefreshController = function (oRefreshTimeSelect, fnOnRefresh) {

	var SECOND = 1000;
	var NEVER = "never";

	this.timeoutMap = {
		"1 sec": SECOND,
		"10 sec": 10 * SECOND,
		"30 sec": 30 * SECOND,
		"60 sec": 60 * SECOND
	};

	this.oRefreshTimeSelect = oRefreshTimeSelect;
	this.fnOnRefresh = fnOnRefresh;
	this.timeoutHandle = null;

	var that = this;
	// Disable by default because it may be a burden for server side
	this.oRefreshTimeSelect.val("never");
	this.oRefreshTimeSelect.change(function () {

		if (that.timeoutHandle) {
			clearInterval(that.timeoutHandle);
		}

		that.restartTimer();
	});
	this.restartTimer();
};

javasimon.AutoRefreshController.prototype = {
	restartTimer: function () {
		var sRefreshVal = this.oRefreshTimeSelect.val();
		var iTimeoutVal = this.getTimeoutVal(sRefreshVal);

		if (iTimeoutVal) {
			var that = this;

			this.timeoutHandle = setTimeout(function () {
				that.fnOnRefresh();
				that.restartTimer();
			}, iTimeoutVal);

		}
	},

	getTimeoutVal: function (sRefreshVal) {
		return this.timeoutMap[sRefreshVal];
	}
};