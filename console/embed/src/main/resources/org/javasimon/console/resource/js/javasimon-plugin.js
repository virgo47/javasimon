(function(tns,domUtil) {
	"use strict";
	/**
	 * View plugin class
	 */
	tns.ViewPlugin=function(oPlugin) {
		this.resourceCount=0;
		this.state="NEW";
		this.id=oPlugin.id;
		this.resources=oPlugin.resources;
	};
	tns.ViewPlugin.prototype={
		/**
		 * Add JS and CSS resources to page header
		 */
		loadResources:function(){
			var oResource, sResourcePath, eResource, self=this, 
				resourceNb=this.resources.length, i,
				fnOnLoad=function(data, textStatus, jqxhr) {
					self.resourceCount--;
					self.onLoaded(); 
				};
			this.state="LOADING";
			if (resourceNb>0) {
				for(i=0;i<resourceNb;i++) {
					oResource=this.resources[i];
					if (/^https?:\/\/.*/.test(oResource.path)) {
						sResourcePath=oResource.path;
					} else {
						sResourcePath="resource/"+oResource.path;
					}                               
					switch(oResource.type) {
						case "JS":
                            this.resourceCount++;
                            $.getScript(sResourcePath, fnOnLoad);
							break;
						case "CSS":
                            $("<link/>", {
                                rel: "stylesheet",
                                type: "text/css",
                                href: sResourcePath
                            }).appendTo("head");
							break;
					}
				}
			} else {					
				this.onLoaded(); // No resource to load
			}
		},
		/**
		 * Inject plugin data
		 */
		setData:function(oView, eTableBody, oData) {
			this.data=oData;
			this.view=oView;
			this.tableBody=eTableBody;
			this.onLoaded();
		},
		/**
		 * When all resources and data is loaded try to render
		 */
		onLoaded:function() {
			if (this.resourceCount<=0 && this.data && this.state==="LOADING") {
				this.state="LOADED";
				this.onRender();
			}
		},
		/**
		 * Inject rendering function (call by plugin itself once JS is loaded),
		 * and then try to render
		 */
		setRenderer:function(fnRenderer) {
			this.fnRenderer=fnRenderer;
			this.onRender();
		},
		/**
		 * When JS is loaded, data and rendering function where injected
		 * then run rendering
		 */
		onRender:function() {
			if (this.fnRenderer && this.state==="LOADED") {
				this.fnRenderer.call(this.view, this.tableBody, this.data);
				this.state="RENDERED";
			}
		}
	};
	/**
	 * Detail view plugin manager
	 */
	tns.ViewPluginManager={
		/**
		 * Plugins indexed by id
		 */
		plugins:{},
		/**
		 * URL of the PluginsJsonAction
		 */
		sUrl:"data/plugins.json",
		/**
		 * Load via an Ajax call the list of plugins
		 */
		fnLoadPlugins:function(sType, fnCallback) {
			var self=this, 
				fnAjaxCallback=function(aoPlugins){
					for(var i=0;i<aoPlugins.length;i++) {
						self.fnAddPlugin(aoPlugins[i]);
					}
					if (fnCallback) {
						fnCallback(aoPlugins);
					}
				};
			$.ajax( {
				url: this.sUrl,
				data: {type:sType},
				success: fnAjaxCallback,
				error: tns.fnDefaultAjaxErrorCallback,
				dataType: "json"
			});
		},
		/**
		 * Add a plugin and started loading its resources
		 */
		fnAddPlugin:function(oPlugin) {
			this.plugins[oPlugin.id]=new tns.ViewPlugin(oPlugin);
			this.plugins[oPlugin.id].loadResources();
		},
		/**
		 * Inject rendering function into a plugin 
		 */
		fnAddPluginRenderer:function(sName, fnPluginRenderer) {
			this.plugins[sName].setRenderer(fnPluginRenderer);
		},
		/**
		 * Inject data into a plugin 
		 */
		fnAddPluginData:function(oPlugin, oView, eTableBody) {
			this.plugins[oPlugin.id].setData(oView, eTableBody, oPlugin.data);
		}
	};
})(javasimon, javasimon.DOMUtil);