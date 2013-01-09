"use strict";
/**
 * Plugin for example/demonstration purpose
 */
(function(domUtil, viewPluginMgr) {
	console.log("Loaded JavaSimon Dummy Plugin");
	// "dummy" is the plugin identifier (see DummyDetailPlugin Java class)
	viewPluginMgr.fnAddPluginRenderer("dummy",function(eTableBody, oDummy) {
		var row=this.fnAppendRow(eTableBody);
		// oDummy={message:"Hello World!"}
		this.fnAppendLabelValueCell(row,"Message", oDummy.message, 3);
	});
}(javasimon.DOMUtil, javasimon.ViewPluginManager));

