"use strict";
var javasimon=window.javasimon||{};
window.javasimon=javasimon;
(function(domUtil,objUtil,tns) {
	/**
	* Data Tree Table component main class
	*/
	tns.DataTreeTable=function(eTable, oSettings) {
		this.eTable=eTable;
		this.oSettings={
			oImages: {
				'spacer':		'resource/images/TreeTableSpacer.gif',
				'lastSpacer':		'resource/images/TreeTableLastSpacer.gif',
				'nodeExpanded':		'resource/images/TreeTableNodeExpanded.gif',
				'lastNodeExpanded':	'resource/images/TreeTableLastNodeExpanded.gif',
				'nodeCollapsed':	'resource/images/TreeTableNodeCollapsed.gif',
				'lastNodeCollapsed':	'resource/images/TreeTableLastNodeCollapsed.gif',
				'leaf':			'resource/images/TreeTableLeaf.gif',
				'lastLeaf':		'resource/images/TreeTableLastLeaf.gif'
			},
			aoColumns: []
		};
		if (oSettings) {
			this.oSettings=objUtil.fnMerge(this.oSettings, oSettings, true);	
		}
		// Default cell rendering function
		var fnRenderDefault =function(oNode,eCell,oDataTreeTable) {
			var sFieldValue=oNode.oData[this.sField];
			if (sFieldValue) {
				domUtil.fnAppendChildText(eCell, sFieldValue);
			}
			if (this.sClass) {
				domUtil.fnAppendClass(eCell, this.sClass);
			}
		},
			aoColumns=this.oSettings.aoColumns;
		for(var i=0;i<aoColumns.length;i++) {
			aoColumns[i].nIndex=i;
			if (aoColumns[i].fnRender===undefined) {
				aoColumns[i].fnRender=fnRenderDefault;
			}
		}		
	};
	tns.DataTreeTable.prototype={
		fnSetData:function(oParentNode,oParentData) {
			var aoChildDatas=oParentData.children;
			oParentNode.bHasChildren=false;
			if (aoChildDatas!== undefined) {			
				var nLastChildIndex=aoChildDatas.length-1;
				if (nLastChildIndex>=0) {
					oParentNode.aoChildren=[];
					oParentNode.bHasChildren=true;
					oParentNode.bExpanded=true;
				}
				for(var nChildIndex=0;nChildIndex<=nLastChildIndex;nChildIndex++) {
					var oChildData=aoChildDatas[nChildIndex];
					var nChildDepth=oParentNode.nDepth+1;
					var oChildNode={
						oParent:oParentNode,
						sHtmlId:oParentNode.sHtmlId+'_'+nChildIndex,
						nIndex:nChildIndex,
						bIsLast: nChildIndex===nLastChildIndex,
						nDepth:nChildDepth,
						oData:oChildData					
					};
					oParentNode.aoChildren.push(oChildNode);
					this.fnSetData(oChildNode,oChildData);
				}
			}

		},
		fnSetRootData:function(oRootData) {
			this.oRootNode={
				sHtmlId:this.eTable.getAttribute('id')+'_Node',
				nDepth:0,
				oData:oRootData,
				bIsLast:true,
				oParent:null
			};
			this.fnSetData(this.oRootNode, oRootData);
		},
		fnVisitNode:function(oNode, fnVisitor, oContext) {
			var aoResult=[];
			var oResult=fnVisitor.call(this, oNode, oContext);
			if (oResult !== undefined) {
				aoResult.push(oResult);
			}
			if (oNode.bHasChildren) {
				for(var childIndex=0;childIndex<oNode.aoChildren.length;childIndex++) {
					aoResult.concat(this.fnVisitNode(oNode.aoChildren[childIndex], fnVisitor, oContext));
				}
			}
			return aoResult;
		},
		fnVisitRootNode:function(fnVisitor, oContext) {
			if (this.oRootNode) {
				this.fnVisitNode(this.oRootNode, fnVisitor, oContext);			
			}
		},
		fnAppendHeader:function() {
			// New Row
			var eHead=domUtil.fnAppendChildElement(this.eTable,'thead');
			var eRow=domUtil.fnAppendChildElement(eHead,'tr');
			var eCell,i;
			// Cells
			var aoColumns=this.oSettings.aoColumns;
			for(i=0;i<aoColumns.length;i++) {
				eCell=domUtil.fnAppendChildElement(eRow,'td');	
				domUtil.fnAppendChildText(eCell,aoColumns[i].sTitle);
			}
		},
		fnAppendImage:function(eParent,sType) {
			var eImg=domUtil.fnAppendChildImage(eParent,this.oSettings.oImages[sType]);
			domUtil.fnSetClass(eImg, "icon");
			return eImg;
		},
		fnGetNodePath:function(oNode) {
			var oCurrentNode=oNode;
			var aoNodes=[];
			while(oCurrentNode!==null) {
				aoNodes.push(oCurrentNode);
				oCurrentNode=oCurrentNode.oParent;
			}
			return aoNodes.reverse();
		},
		fnGetImageType:function(oNode) {
			var sImgType;
			if (oNode.bHasChildren) {
				sImgType =  oNode.bIsLast?'lastNode':'node';
				sImgType += oNode.bExpanded?'Expanded':'Collapsed';
			} else {
				sImgType =  oNode.bIsLast?'lastLeaf':'leaf';
			}
			return sImgType;
		},
		fnAppendNode:function(oNode, oContext) {
			// New Row
			var eRow=domUtil.fnAppendChildElement(
				this.eTableBody,'tr',
				{id: oNode.sHtmlId}
			);
			oNode.eRow=eRow;
			// New Header Cell
			var eCell=domUtil.fnAppendChildElement(eRow,'td');	
			domUtil.fnAppendClass(eCell, 'headCell');
			// Prepare tree node
			var aoPathNodes=this.fnGetNodePath(oNode.oParent);
			var oDataTreeTable=this;
			var bVisible=true;
			var eImg,sImgType;
			var i;
			var aoColumns=this.oSettings.aoColumns;
			for(i=0;i<aoPathNodes.length;i++) {
				var oPathNode=aoPathNodes[i];
				if (oPathNode.bIsLast) {
					this.fnAppendImage(eCell,'lastSpacer');
				} else {
					this.fnAppendImage(eCell,'spacer');
				}
				if (oPathNode.bExpanded===false) {
					bVisible=false;
				}
			}
			domUtil.fnAppendClass(eRow, (bVisible?'visible':'hidden'));
			// Tree node images
			sImgType =  this.fnGetImageType(oNode);
			eImg=this.fnAppendImage(eCell,sImgType);
			if (oNode.bHasChildren) {
				eImg.onclick=function(){ 
					oDataTreeTable.fnToggleNodeExpanded(oNode);
					return false;
				};
				domUtil.fnAppendClass(eImg, 'clickable');
				oNode.eToggleImage=eImg;
			} 
			// Tree node label
			aoColumns[0].fnRender(oNode, eCell, this);
			// Other columns
			for(i=1;i<aoColumns.length;i++) {
				eCell=domUtil.fnAppendChildElement(eRow,'td');	
				aoColumns[i].fnRender(oNode, eCell, this);
			}
		},
		fnDrawHeader:function() {
			this.fnAppendHeader();
		},
		fnDraw:function() {
			if (this.eTableBody) {
				domUtil.fnRemoveChildren(this.eTableBody);			
			} else {
				this.eTableBody=domUtil.fnAppendChildElement(this.eTable,'tbody');
			}
			this.fnVisitRootNode(this.fnAppendNode, null);			
		},
		fnToggleNodeExpanded:function(oNode) {
			var oContext;
			if (oNode.bHasChildren) {
				oNode.bExpanded = !oNode.bExpanded;
				oNode.eToggleImage.setAttribute('src',this.oSettings.oImages[this.fnGetImageType(oNode)]);
				oContext=oNode.bExpanded?
					{sOldClass:'hidden',	sNewClass:'visible'}:
					{sOldClass:'visible',	sNewClass:'hidden'};
				oContext.bFirst=true;
				this.fnVisitNode(oNode, function(poNode,poContext) {
					if (poContext.bFirst) {
						poContext.bFirst=false;
					} else {
						domUtil.fnReplaceClass(poNode.eRow,poContext.sOldClass,poContext.sNewClass);
					}
				}, oContext);
				return false;
			} else {
				return true;
			}
		},
		fnFindNodeByHtmlId:function(sHtmlId) {
			var fnTestNodeByHtmlId=function(oNode, oContext) {
				if (oNode.sHtmlId===oContext.sHtmlId) {
					return oNode;
				} else {
					return undefined;
				}
			}
			return this.fnVisitRootNode(fnTestNodeByHtmlId, {sHtmlId: sHtmlId});
		}
	};
}(javasimon.DOMUtil, javasimon.ObjectUtil, javasimon));