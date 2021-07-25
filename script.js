//Turns encoded list of examples into HTML hyperlinks. Examples contained in brackets, with the filesystem
//location stored ahead of a hashmark ['#'] and a description stored after
//
//  Input: Encoded list of examples
//  Output: String encoded as list of HTML hyperlinks
function linkAttach(links){
	var linkList = links.split("]");
	linkList.forEach(function(link, index){
			    this[index] = link.substring(1).split("#")}, linkList);
	var full = "";
	var linklen = 0;
	linkList.forEach(function(link){
			    if(link.length == 3){
				full += "<p>"+link[1]+"</p> -<a href=/"+link[0]+" download>Download</a> -<a href=/"+link[2]+">View</a><br>, ";
				linklen+=1;
			    }
			    if(link.length == 2){
				full += "<p>"+link[1]+"</p> -<a href=/"+link[0]+" download>Download</a><br>, ";
				linklen+=1;
			    }
			});
	if (full.length == 0) return "No relevant examples."
	else if (linklen == 1) return "Example:"+ toolList(full);
	return "Examples:"+ toolList(full);
}


//Turns encoded list into HTML unordered list. Commas [','] represent distinct categories, and dashes ['-']
//represent items that need to be nested.
//
//  Input: Encoded list
//  Output: String encoded as HTML unordered list
function toolList(tools){
	var toolList = tools.split(", ");
	var full = "<ul>";
	toolList.forEach(function(tool){ 
		if(tool.length > 0){
			if(tool.includes(" -")){
				var send = tool.split(" -");
				full+= "<li>"+send[0] + "<ul>";
				for(var i = 1; i<send.length; i++){
					full+="<li>" + send[i] + "</li>";
				}
				full+="</ul></li>";
			} else full+="<li>"+tool+"</li>";
		}
	});
	return full+"</ul>";
}


//Writes new object for Academic work list, using data from the particular class it's creating the panel for.
//
// Input: Object
// Output: String encoded as HTML panel
function panelWriter(classData){
	return '<button class="subAccordion">'+classData.name+' ⮞</button>'+
		'<div class="panel">'+
			'<div style="float:left; width:65%">'+
				'<p style="border-bottom: 1px solid black; border-right: 1px solid black; width: 100%; padding-bottom: 1.5%; padding-right: 2.5%">'+
					classData.description+
				'</p>'+
				'<p style="width: 65%; ">'+
					linkAttach(classData.examples)+
				'</p>'+
			'</div>'+
			'<div style="padding-left: 3.5%; float: right; width: 30%">'+
				'<p>'+
					'Instructor: '+ classData.professor+
				'</p>'+
				'<p>'+
					'Tools: '+ toolList(classData.tools)+
				'</p>'+
			'</div>'+
		'</div>';
}


//Takes local CSV file, named 'classData.csv', and breaks into component rows, which are then sent to panelWriter
//and written to HTML document in 'Academic' element.
//
//  Input: CSV file, callback for function that must be run afterwards
//  Output: -> to HTML
function readCSVtoAcademic(callback){
	var academic = document.getElementById("Academic");
	
	d3.tsv('classData.csv').then(_data => {
	    for(var i = 0; i<_data.length; i++) academic.innerHTML += panelWriter(_data[i]);

	    //accordionHandle, performed as callback to accomodate 
	    //	panelwriter, which can be slow.

	    callback("subAccordion");
	});
}


//shamelessly ripped from https://www.w3schools.com/howto/howto_js_accordion.asp,
//	with some modifications
function accordionHandle(className){
	var acc = document.getElementsByClassName(className);
	var i;

	for (i = 0; i < acc.length; i++) {
	  acc[i].addEventListener("click", function() {
	    this.classList.toggle("active");

	    local = this.textContent;
	    var panel = this.nextElementSibling;
	    if (panel.style.display === "block") {
	      this.textContent = local.slice(0, -1) + "⮞";
	      panel.style.display = "none";
	    } else {
	      this.textContent = local.slice(0, -1) + "⮟";
	      panel.style.display = "block";
	    }
	  });
	} 
}

readCSVtoAcademic(accordionHandle);
accordionHandle("accordion");
