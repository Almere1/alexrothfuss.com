	function linkAttach(links){
		var linkList = links.split("]");
		linkList.forEach(function(link, index){
				    this[index] = link.substring(1).split("#")}, linkList);
		var full = "";
		linkList.forEach(function(link){
				    if(link.length == 2){
					full += "<a href=/"+link[0]+">"+link[1]+"</a><br>"}});
		return full;
	}
	function panelWriter(classData){
		return '<button class="subAccordion">'+classData.name+
			' ⮞</button> <div class="panel"><p style="border-right: 1px solid black; float: left; width: 70%; padding-right: 2.5%">'+classData.description+
			'</p><div style="float: right;"> <p>'+classData.professor+"</p><p>"+classData.languages+
			"</p><p>"+linkAttach(classData.examples)+"</p></div></div>"
	}
	function readCSVtoAcademic(callback){
		var academic = document.getElementById("Academic");
		
		d3.tsv('classData.csv').then(_data => {
		    for(var i = 0; i<_data.length; i++) academic.innerHTML += panelWriter(_data[i]);
		    console.log('original data:', _data);

		    //accordionHandle, performed as callback to accomodate 
		    //	panelwriter, which can be slow.

		    callback("subAccordion");
		});
		console.log("AAA");
	}

	//shamelessly ripped from https://www.w3schools.com/howto/howto_js_accordion.asp,
	//	with some modifications
	function accordionHandle(className){
		console.log(className);
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
