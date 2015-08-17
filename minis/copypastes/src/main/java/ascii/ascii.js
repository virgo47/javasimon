// rough JavaScript version of table-based diacritics stripper

TAB_00C0="AAAAAAACEEEEIIII" +
	"DNOOOOO*OUUUUYIs" +
	"aaaaaaaceeeeiiii" +
	"?nooooo/ouuuuy?y" +
	"AaAaAaCcCcCcCcDd" +
	"DdEeEeEeEeEeGgGg" +
	"GgGgHhHhIiIiIiIi" +
	"IiJjJjKkkLlLlLlL" +
	"lLlNnNnNnnNnOoOo" +
	"OoOoRrRrRrSsSsSs" +
	"SsTtTtTtUuUuUuUu" +
	"UuUuWwYyYZzZzZzF";

function stripDiacritics(source) {
	var result = source.split('');
	for (var i = 0; i < result.length; i++) {
		var c = source.charCodeAt(i);
		if (c >= 0x00c0 && c <= 0x017f) {
			result[i] = String.fromCharCode(TAB_00C0.charCodeAt(c - 0x00c0));
		} else if (c > 127) {
			result[i] = '?';
		}
	}
	return result.join('');
}

stripDiacritics("Šupa, čo? ľšťčžýæøå");