function gettable(){
	ret = "<table>";
	ret += "<tr class='ui-widget-header'>";

	if (service.usetime){
		ret += "<th class='time'>";
		ret += i18n.start;
		ret += "</th>";
		ret += "<th class='duration'>";
		ret += i18n.duration;
		ret += "</th>";
	}
	ret += "<th class='servtbpad'>";
	ret += i18n.title;
	ret += "</th>";
	ret += "<th class='servtbpad'>";
	ret += i18n.notes;
	ret += "</th>";
	ret += "</tr>";
	for (i=0; i < service.items.length; i++){

		ret += "<tr>";

		if (service.usetime){
			ret += "<td class='time'>";
			ret += service.items[i].start;
			ret += "</td>";
			ret += "<td class='duration'>";
			ret += service.items[i].duration;
			ret += "</td>";
		}
		ret += "<td>";
		ret += "<a href='#' onclick='showcont("+i+");'>";
		ret += service.items[i].title;
		ret += "</a>";
		ret += "</td>";
		ret += "<td>";
		ret += service.items[i].notes;
		ret += "</td>";
		ret += "</tr>";
	}
	ret += "</table>";
	return ret;
}

function hascontent(i){

	return  (typeof(service.items[i].text) !== "undefined") ||
		(typeof(service.items[i].chordcompl) !== "undefined") ||
		(typeof(service.items[i].chordsimpl) !== "undefined");
}

function getcontent(){
	ret = "";

	for (i=0; i < service.items.length; i++){
		ret += "<div id='cont_"+i+"'>";
		ret += "<div id='tabs_"+i+"'>";

		ret += "<ul>";
		if (typeof(service.items[i].text) !== "undefined"){
			ret += "<li><a href='#tabs_"+i+"_text'>";
			ret += i18n.text;
			ret += "</a></li>";
		}
		if (typeof(service.items[i].chordcompl) !== "undefined"){
			ret += "<li><a href='#tabs_"+i+"_chordcompl'>";
			ret += i18n.chordcompl;
			ret += "</a></li>";
		}
		if (typeof(service.items[i].chordsimpl) !== "undefined"){
			ret += "<li><a href='#tabs_"+i+"_chordsimpl'>";
			ret += i18n.chordsimpl;
			ret += "</a></li>";
		}
		ret += "</ul>";

		if (typeof(service.items[i].text) !== "undefined"){
			ret += "<div id='tabs_"+i+"_text' class='textitem'>";
			ret += service.items[i].text;
			ret += "</div>";
		}

		if (typeof(service.items[i].chordcompl) !== "undefined"){
			ret += "<div id='tabs_"+i+"_chordcompl' class='chorditem'>";
			ret += service.items[i].chordcompl;
			ret += "</div>";
		}

		if (typeof(service.items[i].chordsimpl) !== "undefined"){
			ret += "<div id='tabs_"+i+"_chordsimpl' class='chorditem'>";
			ret += service.items[i].chordsimpl;
			ret += "</div>";
		}

	
		ret += "</div>";
		ret += "</div>";
	}

	return ret;
}

function showcont(x){

	if (! hascontent(x)){
		return false;
	}

	$("#tabs_"+x).tabs();
	$("#cont_" + x).dialog({ 
		modal: true,
		width: '80%',
		title: service.items[x].title
	});
}

function getfootnote(){
	ret = "";
	ret += "<a href='http://code.google.com/p/datasoul/'>Datasoul</a> ";
	ret += "is an open source presentation software to display lyrics and text in churches services";
	ret += "<br>";
	ret += "This page will be available for 30 days or until our storage quota permit";
	return ret;
}

$(document).ready(function(){

	$("#servnotes").html( service.notes );
	$("#servtable").html( gettable() );
	$("#content").html( getcontent() );
	$("#content").hide();

	if (service.notes == ''){
		$('#servnotes').hide();
	}

	$("#footnote").html( getfootnote() );

	$("#loading").hide();
	$("#bodycont").show();
});
