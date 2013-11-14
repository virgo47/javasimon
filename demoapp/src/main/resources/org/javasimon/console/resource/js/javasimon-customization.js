"use strict";
var javasimon=window.javasimon||{};
window.javasimon=javasimon;

$.getScript("resource/js/javasimon-util.js", function(data, textStatus, jqxhr) {
    console.log("javasimon-utils were loaded");
});

javasimon.onJsonData = function(json) {
    $.each(json, function(index, sample) {
        var $sampleRow = javasimon.DOMUtil.fnGetSampleRow(sample.name);
        if (sample.max > 20) {
            $sampleRow.css('background-color','#ff0000');
        }
    });
}