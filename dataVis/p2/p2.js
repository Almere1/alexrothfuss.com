
'use strict';

const fullMonth = {
  Jan: 'JANUARY',
  Feb: 'FEBRUARY',
  Mar: 'MARCH',
  Apr: 'APRIL',
  May: 'MAY',
  Jun: 'JUNE',
  Jul: 'JULY',
  Aug: 'AUGUST',
  Sep: 'SEPTEMBER',
  Oct: 'OCTOBER',
  Nov: 'NOVEMBER',
  Dec: 'DECEMBER',
};

/* something to repackage data in a more convenient form */
export const dataProc = function (_data) {
  let data = _data.map((d, i) => ({
    month: fullMonth[d.month],
    year: +d.year,
    // would have been nice for csv to have column headers that were valid identifers
    size: +d['army-size'],
    zNum: +d['disease-death'],
    wNum: +d['wound-death'],
    oNum: +d['other-death'],
    zRate: +d['disease-rate'],
    wRate: +d['wound-rate'],
    oRate: +d['other-rate'],
  }));
  return data;
};


//This code is rushed, horribly ugly, and unreadable. I apologize.


export const part1 = function(id, data) {


  const svg = d3.select(`#${id}`);
  const marg = 100;
  const width = parseFloat(svg.style('width'));
  const height = parseFloat(svg.style('height'));
  const whichone = ["zRate", "oRate", "wRate"];

  ['zbar', 'obar', 'wbar'].map((which, idx) => {
    const outer = svg
      .append('g')
      .attr('transform', `translate(${marg},${(idx * height) / 3 + marg / 4})`);

    const circle = svg
      .append('g')
      .attr('transform', `translate(${marg},${(height) / 3 + marg / 4})`);

	var xcoord = 750;
	var flipflag = 1;
	for (var mon = 0; mon<24; mon++) {
		if (mon == 12){ xcoord = 150; }
		var month = mon.toString();
		var radius = Math.sqrt(data[month][whichone[idx]]/Math.PI)*17
		var arcGen = d3.arc()
		  .innerRadius(0)
		  .outerRadius(radius)
		  .startAngle(mon*Math.PI/6-Math.PI/2)
		  .endAngle((1+mon)*Math.PI/6-Math.PI/2)
		circle.append('g')
		  .attr('transform',`translate(${xcoord}, 0)`)
		  .append("path")
		  .attr("d", arcGen)
		  .attr("class", which)
		  .attr("stroke", "gray")
		  .attr("stroke-width", 1);
		if(idx == 0){
		  circle.append("text")
		      .attr('x', xcoord)
		      .attr('y', 5)
		      .attr('text-anchor', 'middle')
		      .text(data[month]["month"])    
		      .attr("font-size", "9px")
		      .attr("fill", "black")
		      .attr("transform",`translate(${Math.sin(-(Math.PI/2)-(Math.PI/12)-(Math.PI/6)*mon)*((radius>100) ? (radius+30) : 100)},${Math.cos(-(Math.PI/2)-(Math.PI/12)-(Math.PI/6)*mon)*((radius>100) ? (radius+30) : 100)})`);
		}

		if(idx == 2){
			if (mon == 12){ flipflag = 0; }
			const arcGen = d3.arc()
			  .innerRadius(flipflag * Math.sqrt(data[month][whichone[1]]/Math.PI)*17)
			  .outerRadius(Math.sqrt(data[month][whichone[1]]/Math.PI)*17)
			  .startAngle(mon*Math.PI/6-Math.PI/2)
			  .endAngle((1+mon)*Math.PI/6-Math.PI/2)
			circle.append('g')
			  .attr('transform',`translate(${xcoord}, 0)`)
			  .append("path")
			  .attr("d", arcGen)
			  .attr("class", "obar")
			  .attr("stroke", "black")
			  .attr("stroke-width", 1.5);

		
		}
	}

		circle
		      .append('text')
		      .attr('transform', `translate(450,-200)`)
		      .attr('text-anchor', 'middle')
		      .text(['DIAGRAM of the CAUSES of MORTALITY'])
		circle
		      .append('text')
		      .attr('transform', `translate(450,-180)`)
		      .attr('text-anchor', 'middle')
		      .text(['in the ARMY in the EAST'])
		      
		  circle.append('text')
		      .attr('transform', `translate(150, -210)`)
		      .attr('text-anchor', 'end')
		      .text(['2.']);
		circle
		      .append('text')
		      .attr('transform', `translate(750,-185)`)
		      .attr('text-anchor', 'end')
		      .text(['1.']);
		circle
		      .append('text')
		      .attr('transform', `translate(250,-195)`)
		      .attr('text-anchor', 'end')
		      .text(['APRIL 1855 to MARCH 1856']);
		circle
		      .append('text')
		      .attr('transform', `translate(850,-170)`)
		      .attr('text-anchor', 'end')
		      .text(['APRIL 1854 to MARCH 1855']);
	  var setOfText = ["The Areas of the blue, red, & black wedges are each measured from the","centre as the common vertex.","The blue wedges measured from the centre of the circle represent are a for","area the deaths from Preventible or Mitigable Zymotic diseases, the","red wedges measured from the centre the deaths from wounds, & the","black wedges measured from the centre the deaths from all other causes.","The black line across the red triangle in Nov. 1854 marks the boundary of","the deaths from all other causes during the month.","In October 1854, & April 1855, the black area coincides with the red,","in January & February 1856, the blue coincides with the black.","The entire areas may be compared by following the blue, the red, & the","black lines enclosing them."];
	  var ttt = 0;
		for (var line in setOfText){

			circle
		      .append('text')
		      .attr('transform', `translate(-100, ${ttt*15+ 150})`)
		      // https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/text-anchor
		      .attr('text-anchor', 'start')
		      .text(setOfText[ttt]);
			ttt+=1;
		}
  });
};




export const part2 = function (id, data) {



  const svg = d3.select(`#${id}`);


  const blue = getComputedStyle(document.body).getPropertyValue('--fn-blue');
  const marg = 100;
  const width = parseFloat(svg.style('width'));
  const height = parseFloat(svg.style('height'));
  const maxNum = d3.max(data.map((d) => Math.max(d.zNum, d.wNum, d.oNum)));
  const months = data.map((d) => `${d.month} ${d.year}`);
  const xScale = d3
    .scaleLinear()
    .domain([0, maxNum])
    .range([0, width - 2 * marg]);
  const yScale = d3
    .scaleBand()
    .domain(months)
    .range([0, (height - marg) / 3])
    .padding(0.1);
  const whichone = ["zNum", "wNum", "oNum"];
  ['zbar', 'wbar', 'obar'].map((which, idx) => {
    const outer = svg
      .append('g')
      .attr('transform', `translate(${marg},${(0 * height) / 3 + marg / 4})`);


	//Found out about line class from https://www.d3indepth.com/shapes/#arc-generator


	var points = []
	for(var i = 0; i<24; i++){
		points.push([i*40,(maxNum-data[i][whichone[idx]])/6]);

	}


	var lineGenerator = d3.line();
	var pathData = lineGenerator(points); 

    outer.append("path").attr('d', pathData).attr("stroke", "black").attr("class", which);

  });
  ['zbar', 'wbar', 'obar'].map((which, idx) => {
    const outer = svg
      .append('g')
      .attr('transform', `translate(${marg},${(0 * height) / 3 + marg / 4})`);

    const txter = svg
      .append('g')
      .attr('transform', `translate(${marg-100},${(0 * height) / 3 + marg / 4})`);

	//Found out about line class from https://www.d3indepth.com/shapes/#arc-generator


	var points = []
	for(var i = 0; i<24; i++){
		points.push([i*40,(maxNum-data[i][whichone[idx]])/6]);

		if(idx == 0){
		  txter.append("text")
		      .attr('x', -150)
		      .attr('y', 470)
		      .attr('text-anchor', 'middle')
		      .text(data[i]["month"])    
		      .attr("font-size", "9px")
		      .attr("fill", "black")
		      .attr("transform",`translate(${40*i}, 0) rotate(-30)`); //${Math.sin(-(Math.PI/2)-(Math.PI/12)-(Math.PI/6)*mon)*(radius+30)},${Math.cos(-(Math.PI/2)-(Math.PI/12)-(Math.PI/6)*mon)*(radius+30)})`)
//		      .attr("transform",`rotate(5)`); 
		}
	}


	var lineGenerator = d3.line();
	var pathData = lineGenerator(points); 

    outer.append("path").attr('d', pathData).attr("stroke", "black").attr("fill", "none");

		outer
		      .append('text')
		      .attr('transform', `translate(220,30)`)
		      .attr('text-anchor', 'middle')
		      .text(['DIAGRAM of the CAUSES of MORTALITY'])
		outer
		      .append('text')
		      .attr('transform', `translate(220,45)`)
		      .attr('text-anchor', 'middle')
		      .text(['in the ARMY in the EAST'])
	  var setOfText = ["The height of the blue, red, & black regions are each measured from the bottom.","The blue line measured from the bottom of the chart represents the","number of deaths from Preventible or Mitigable Zymotic diseases, the","red line measured from the bottom the deaths from wounds, & the","black line measured from the bottom the deaths from all other causes."];
	  var ttt = 0;
		for (var line in setOfText){

			outer
		      .append('text')
		      .attr("font-size", "12px")
		      .attr('transform', `translate(500, ${ttt*15+ 150})`)
		      // https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/text-anchor
		      .attr('text-anchor', 'start')
		      .text(setOfText[ttt]);
			ttt+=1;
		}
  });
};
