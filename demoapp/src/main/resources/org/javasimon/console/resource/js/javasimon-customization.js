"use strict";
var javasimon=window.javasimon||{};
window.javasimon=javasimon;

$.getScript("resource/js/javasimon-util.js", function(data, textStatus, jqxhr) {
    console.log("javasimon-utils were loaded");
});

javasimon.onTableData = function(json) {
    $.each(json, function(index, sample) {
        var $sampleRow = javasimon.DOMUtil.fnGetSampleRow(sample.name);
        if (sample.max > 20) {
            $sampleRow.css('background-color','#ff0000');
        }
    });
}

function isLeaf(treeElement) {
    return treeElement.bHasChildren === false;
}

javasimon.onTreeElementDrawn = function(treeElement) {
    if (isLeaf(treeElement)) {
        var $sampleRow = javasimon.DOMUtil.fnGetSampleRow(treeElement.oData.name);
        if (treeElement.oData.max > 20) {
            $sampleRow.css('background-color','#ff0000');
        }
    }
}
