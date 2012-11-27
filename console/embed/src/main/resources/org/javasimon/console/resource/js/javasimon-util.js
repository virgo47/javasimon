"use strict";
var javasimon=window.javasimon||{};
window.javasimon=javasimon;
/**
 * DOM Helper to create and append nodes to DOM tree
 */
javasimon.DOMUtil={
	fnAppendChildElement:function(eParent, sName, oAttributes) {
		var eChild=document.createElement(sName);
		this.fnSetAttributes(eChild, oAttributes);
		if (eParent) {
			eParent.appendChild(eChild);
		}
		return eChild;
	},
	fnSetAttributes:function(eNode, oAttributes) {
		if (oAttributes) {
			for (var sAttributeName in oAttributes) {
				eNode.setAttribute(sAttributeName, oAttributes[sAttributeName]);
			}
		}
	},
	fnAppendChildText:function(eParent,sText) {
		var eChild=document.createTextNode(sText);
		eParent.appendChild(eChild);
		return eChild;
	},
	fnAppendChildImage:function(eParent,sSrc) {
		return this.fnAppendChildElement(eParent,'img', {src: sSrc});
	},
	fnAppendLink:function(eParent, oAttributes) {
		var eLink=this.fnAppendChildElement(eParent, "a", oAttributes);
		if (oAttributes) {
			var sLabel=oAttributes.label;
			if (sLabel) {
				this.fnAppendChildText(eLink, sLabel);
			}
		}
		return eLink;
	},
	fnAppendLinkButton:function(eParent, oAttributes){
		var eLink=this.fnAppendLink(eParent);
		return $(eLink).button(oAttributes);
	},
	fnRemoveChildren:function(eParent) {
		if (eParent.hasChildNodes()) {
			var nChildCount=eParent.childNodes.length;
			for(var i=0;i<nChildCount;i++) {
				eParent.removeChild(eParent.lastChild);
			}
		}
	},
	fnGetHead:function() {
		return document.getElementsByTagName("head")[0];
	},
	fnSetClass:function(eParent,sClass) {
		eParent.setAttribute('class',sClass);
	},
	fnGetClass:function(eParent) {
		return eParent.getAttribute('class');
	},
	fnAppendClass:function(eParent,sClass) {
		var sClasses=this.fnGetClass(eParent);
		if (sClasses) {
			sClasses=sClasses+" "+sClass;
		} else {
			sClasses=sClass;
		}
		this.fnSetClass(eParent, sClasses);
	},
	fnReplaceClass:function(eParent,sOldClass,sNewClass) {
		var sClasses=this.fnGetClass(eParent);
		var asClasses;
		if (sClasses) {
			asClasses=sClasses.split(/\s+/);
			var bFound=false;
			for(var i=0;i<asClasses.length;i++) {
				if (asClasses[i]===sOldClass) {
					asClasses[i]=sNewClass;
					bFound=true;
				}
			}
			if (!bFound) {
				asClasses.push(sNewClass);
			}
			sClasses=asClasses.join(' ');
		} else {
			sClasses=sNewClass;
		}
		this.fnSetClass(eParent, sClasses);
	},
	oSimonTypes: {
		"unknown":{sLabel:"Group",sImage:"resource/images/TypeUnknown.png"},
		"counter":{sLabel:"Counter", sImage:"resource/images/TypeCounter.png"},
		"stopwatch":{sLabel:"Stopwatch", sImage:"resource/images/TypeStopwatch.png"}
	},
	fnGetSimonType: function(sType) {
		var oType;
		if (sType) {
			oType=this.oSimonTypes[sType.toLowerCase()];
		}
		return oType;
	},
	fnAppendSimonTypeImage:function(eParent, sType) {
		var eImg, oType=this.fnGetSimonType(sType);
		if (oType) {
			eImg=this.fnAppendChildImage(eParent, oType.sImage);
			eImg.setAttribute("alt", oType.sLabel);
			this.fnSetClass(eImg, "icon");
		}
		return eImg;
	}
};
javasimon.ObjectUtil={
	fnMerge:function(oTarget, oSource, bRecurse) {
		var tSourceProp,tTargetProp;
		for (var sProp in oSource) {
			if (bRecurse && typeof oTarget[sProp]==='object' && typeof oSource[sProp]==='object') {
				tSourceProp=oSource[sProp].constructor;
				tTargetProp=oTarget[sProp].constructor;
				if (tSourceProp===tTargetProp) {
					if (tTargetProp===Object) {
						oTarget[sProp]=this.fnMerge(oTarget[sProp], oSource[sProp], true);
					} else if (tTargetProp===Array) {
						oTarget[sProp]=oTarget[sProp].concat(oSource[sProp]);
					}
				}
			} else {
				oTarget[sProp]=oSource[sProp];
			}
		}
		return oTarget;
	}
};
