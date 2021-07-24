/* You do your p3 work in this file.
The first part of the file does not need to be modified (at least not for the basic
requirements); the second part (starting with dataProc) is where you do your work. */

// ========================================================
// BEGIN part that doesn't need changing

'use strict';
/* assuming that "d3" has been "imported" by whatever is importing this module */

export const transDur = 500; // duration of all transition()s
export const circRad = 6; // size of circle marks in scatterplot
export const scatSize = 360; // width and height of scatterplot

const colorDem = d3.rgb(40, 50, 255); // color showing pure democratic vote
const colorRep = d3.rgb(230, 30, 20); // color showing pure democratic vote

/* create the annotated balance bars for popular and electoral college votes */
export const balanceInit = function (did, sid) {
  // https://bl.ocks.org/curran/3a68b0c81991e2e94b19
  const div = document.getElementById(did);
  /* appending elements to the div likely changes clientWidth and clientHeight,
  hence the need to save these values representing the original grid */
  const ww = div.clientWidth;
  let hh = div.clientHeight;
  const svg = d3.select('#' + did).append('svg');
  // make svg fully occupy the (original) containing div
  svg.attr('id', sid).attr('width', ww).attr('height', hh);
  const wee = 8;
  const bal = svg.append('g').attr('transform', `translate(0,${2 * wee})`);
  hh -= 2 * wee;
  /*                L                                                        R
   +                ----------------------------|-----------------------------
       popular vote | #D-pv-bar,#D-pv-txt       |        #R-pv-bar,#R-pv-txt |
  H                 ----------------------------|-----------------------------
                      #D-name                   |                    #R-name
                    ----------------------------|-----------------------------
  electoral college | #D-ec-bar,#D-ec-txt       |        #R-ec-bar,#R-ec-txt |
                    ----------------------------|-----------------------------
  */
  // some convenience variables for defining geometry
  const L = ww / 7,
    R = (6.5 * ww) / 7,
    H = hh / 3;
  // mapping over an array of adhoc parameter objects to avoid copy-pasta
  [
    // create the left-side labels for the two bars
    { y: 0.5 * H, t: 'Popular Vote' },
    { y: 2.5 * H, t: 'Electoral College' },
  ].map((i) => {
    bal
      .append('text')
      .attr('transform', `translate(${L - wee},${i.y})`)
      .style('text-anchor', 'end')
      .html(i.t);
  });
  [
    /* the bars and text values for the four counts: {D,R}x{popular vote, electoral college},
    and, the two candidate names */
    { id: 'D-pv', p: -1, y: 0 },
    { id: 'D-name', p: -1, y: H },
    { id: 'D-ec', p: -1, y: 2 * H },
    { id: 'R-pv', p: 1, y: 0 },
    { id: 'R-name', p: 1, y: H },
    { id: 'R-ec', p: 1, y: 2 * H },
  ].map((i) => {
    if (!i.id.includes('name')) {
      bal
        .append('rect')
        .attr(
          /* NOTE how these bars are transformed: your code only needs to set width (even though
        the D bars grow rightward, and the R bars grown leftward), and, your code doesn't need
        to know the width in pixels.  Just set width to 0.5 to make the bar go to the middle */
          'transform',
          i.p < 0 ? `translate(${L},0) scale(${R - L},1)` : `translate(${R},0) scale(${L - R},1)`
        )
        .attr('x', 0)
        .attr('y', i.y)
        .attr('height', H)
        .attr('fill', i.p < 0 ? colorDem : colorRep)
        // NOTE: select the bars with #D-pv-bar, #D-ec-bar, #R-pv-bar, #R-ec-bar
        .attr('id', `${i.id}-bar`)
        .attr('width', 0.5); // totally random initial fractional value
    }
    const txt = bal
      .append('text')
      .attr('transform', `translate(${i.p < 0 ? L + wee : R - wee},${i.y + 0.5 * H})`)
      .style('text-anchor', i.p < 0 ? 'start' : 'end')
      // NOTE: select the text fields with #D-pv-text, #D-ec-text, #R-pv-text, #R-ec-text
      .attr('id', `${i.id}${i.id.includes('name') ? '' : '-txt'}`);
    txt.html('#' + txt.node().id);
  });
  bal
    .append('line')
    .attr('x1', (L + R) / 2)
    .attr('x2', (L + R) / 2)
    .attr('y1', 0)
    .attr('y2', hh)
    .attr('stroke-width', 1)
    .attr('stroke', '#fff');
};

/* canvasInit initializes the HTML canvas that we use to draw a picture of the bivariate
colormap underneath the scatterplot.
NOTE THAT AS A SIDE-EFFECT this sets scatContext and scatImage, which you must use again
later when changing the pixel values inside the canvas */
let scatContext = null;
let scatImage = null;
export const canvasInit = function (id) {
  const canvas = document.querySelector('#' + id);
  canvas.width = scatSize;
  canvas.height = scatSize;
  scatContext = canvas.getContext('2d');
  scatImage = scatContext.createImageData(scatSize, scatSize);
  /* set pixels of scatImage to checkerboard pattern with ramps; the only purpose of this is to
  show an example of traversing the scatImage pixel array, in a way that (with thought and scrutiny)
  identifies how i and j are varying over the image as it is seen on the screen.
  NOTE that nested for() loops like this are an idiomatic way of working with pixel data arrays,
  as opposed to functional idioms like .map() that we use for other kinds of data. */
  for (let k = 0, j = 0; j < scatSize; j++) {
    for (let i = 0; i < scatSize; i++) {
      scatImage.data[k++] =
        100 + // RED channel is a constant plus ...
        (120 * i) / scatSize + // ... ramp up along i,
        30 * (Math.floor(i / 40) % 2); // with wide bands
      scatImage.data[k++] =
        100 + // GREEN channel is a constant plus ...
        (120 * j) / scatSize + // ... ramp up along with j,
        30 * (Math.floor(j / 10) % 2); // with narrow bands
      scatImage.data[k++] = 30; // BLUE channel is constant
      scatImage.data[k++] = 255; // 255 = full OPACITY (don't change)
    }
  }
  /* display scatImage inside canvas.
  NOTE that you will need to call this again (exactly this way, with these variable names)
  anytime you change the scatImage.data canvas pixels */
  scatContext.putImageData(scatImage, 0, 0);
};

/* Place the scatterplot axis labels, and finalize the stacking of both the labels
and the scatterplot marks over the canvas.
That this assumes many specific element ids in the DOM is likely evidence of bad design */
export const scatLabelPos = function () {
  // place the scatterplot axis labels.
  const marg = 20; // around the scatterplot domain
  const wee = 7; // extra tweak to text position
  const sz = scatSize;
  /* since these two had style "position: absolute", we have to specify where they will be,
  and this is done relative to the previously placed element, the canvas */
  /* (other functions here in p3.js made an effort to not assume particular element ids, using
  instead ids passed to the function, but GLK gave up trying that with this one, alas) */
  ['#scat-axes', '#scat-marks-container'].map((pid) =>
    d3
      .select(pid)
      .style('left', -marg)
      .style('top', -marg)
      .attr('width', 2 * marg + sz)
      .attr('height', 2 * marg + sz)
  );
  d3.select('#y-axis').attr('transform', `translate(${marg - wee},${marg + sz / 2}) rotate(-90)`);
  d3.select('#x-axis').attr('transform', `translate(${marg + sz / 2},${marg + sz + wee})`);
  d3.select('#scat-marks')
    .attr('transform', `translate(${marg},${marg})`)
    .attr('width', sz)
    .attr('height', sz);
};

/* scatMarksInit() creates the per-state circles to be drawn over the scatterplot */
export const scatMarksInit = function (id, data) {
  /* maps interval [0,data.length-1] to [0,scatSize-1];
  this is NOT an especially informative thing to do; it just gives all the
  tickmarks some well-defined initial location */
  const tscl = d3
    .scaleLinear()
    .domain([0, data.length - 1])
    .range([0, scatSize]);

  /* create the circles */
  d3.select('#' + id)
    .selectAll('circle')
    .data(data)
    .join('circle')
    .attr('r', circRad)
    .attr('cx', (d, ii) => tscl(ii))
    .attr('cy', (d, ii) => scatSize - tscl(ii))
    .attr('stroke', 'white')
    .attr('stroke-opacity', 0.8)
    .attr('stroke-width', 1.3)
    .attr('fill', 'black')
    .attr('fill-opacity', 0)
    .on("mouseover", handleMouseOver)  // found via http://bl.ocks.org/WilliamQLiu/76ae20060e19bf42d774
    .on("mouseout", handleMouseOff)   // also this
    .on("click", handleClick);  


};


/*
var highlightList = [];
var highlightMap  = {};


       function handleMouseOver(d, ii){
	    d3.select('#scat-marks')
            .append('text')
            .text(ii.StateAbbr)
	    .attr("font-family", "sans-serif")
	    .attr("font-weight", "100")
	    .attr("x", 5)
	    .attr("y", 15);
	}


       function handleMouseOff(d, ii){
	    d3.select('#scat-marks')
            .selectAll('text')
            .remove();
	}

       function handleClick(d, ii) {  
	    var index = highlightList.indexOf(ii.StateAbbr);
	    if (index < 0){
		    highlightList.push(ii.StateAbbr);
		    highlightMap[ii.StateAbbr] = ii;
	    }
	    else{
		    highlightList.splice(index, 1);
		    delete highlightMap[ii.StateAbbr];
	    }

            d3.select('#scat-marks').selectAll('circle')
		       .attr('fill', "rgb(0,255,0)")
		       .attr('fill-opacity', (data) => (highlightList.includes(data.StateAbbr)) ? "255" : "0");
	    d3.select("#us-map").selectAll('.state')
		      .style("stroke", (data) => `rgba(0,255,0,${(highlightList.includes(data.StateAbbr)) ? "255" : "0"})`)
		      .style("stroke-width", "5");

	    
	    d3.select('#scat-marks').selectAll('line').remove();
	    highlightList.forEach(i => extraScatMarksInit(highlightMap[i]));
	}


*/













export const formsInit = function (tlid, yid, years, mdid) {
  // finish setting up timeline for choosing the year
  const tl = d3.select('#' + tlid);
  tl.attr('min', d3.min(years))
    .attr('max', d3.max(years))
    .attr('step', 4)
    // responding to both input and click facilitates being activated from code
    .on('input click', function () {
      /* This is one of the rare times that you CANNOT use an arrow function; you need a real
      "function" so that "this" is usefully set (here, "this" is this input element) */
      d3.select('#' + yid).html(this.value);
      yearSet(+this.value); // need the + so year is numeric
    });
  // create radio buttons for choosing colormap/scatterplot mode
  const radioModes = [
    /* id: how the mode selection is identified; str: how it is displayed
    NOTE: these identifiers 'RVD', 'PUR', 'LVA' will not change; feel free to use
    them as magic constant strings throughout your code */
    { id: 'RVD', str: 'red/blue' },
    { id: 'PUR', str: 'purple' },
    { id: 'LVA', str: 'lean-vs-amount' },
  ];
  // one span per choice
  const spans = d3
    .select('#' + mdid)
    .selectAll('span')
    .data(radioModes)
    .join('span');
  // inside each span, put a radio button
  spans
    .append('input')
    // add some spacing left of 2nd and 3rd radio button; the 'px' units are in fact needed
    .style('margin-left', (_, i) => `${20 * !!i}px`)
    .attr('type', 'radio')
    .attr('name', 'mode') // any string that all the radiobuttons share
    .attr('id', (d) => d.id) // so label can refer to this, and is thus clickable
    .attr('value', (d) => d.id) // so that form as a whole has a value
    // respond to being selected by calling the modeSet function (in this file).
    .on('input', function (d) {
      modeSet(this.value);
    });
  // also in each span put the choice description
  spans
    .append('label')
    .attr('for', (d) => d.id)
    .html((d) => d.str);
};

// END part that doesn't need changing
// ==============================================================================

// ==============================================================================
// BEGIN part where you'll change and add things

//Attaches index to particular state, and declares number of states
const indexState = new Object();

//Objects describing electoral and popular votes for democrats and republicans, by year, by state
const pop = new Object();
const ele = new Object();
var overMode = "RVD";
var overYear = 2020;
var yearen;
var highlightList = [];
var highlightMap  = {};


export const dataProc = function (_data, years) {
  yearen = years;
  for (var state in _data){
	indexState[state] = _data[state]["StateAbbr"];
	pop[state] = new Object();
	ele[state] = new Object();
	years.forEach(function(year){
		pop[state][year] = new Object();
		ele[state][year] = new Object();
		pop[state][year]['D'] = _data[state]["DN_"+year.toString()];
		pop[state][year]['R'] = _data[state]["RN_"+year.toString()];
		ele[state][year]['D'] = _data[state]["DE_"+year.toString()];
		ele[state][year]['R'] = _data[state]["RE_"+year.toString()];
	});
  }
  

  return _data; // HEY you should change this!
};

export const init = function () {
  d3.select('#us-map').selectAll('.state')
	.on("click", handleClick)
        .on("mouseover", mapMouseOver)  // found via http://bl.ocks.org/WilliamQLiu/76ae20060e19bf42d774
        .on("mouseout", mapMouseOff);
};

const modeSet = function (mode) {
	overMode = mode;
	if(overMode == 'RVD'){
		canvasRVD();
		document.getElementById("y-axis").innerHTML = "Democrat Votes";
		document.getElementById("x-axis").innerHTML = "Republican Votes";
	}else if(overMode == 'PUR'){ 
		canvasPUR();
		document.getElementById("y-axis").innerHTML = "Democrat Votes";
		document.getElementById("x-axis").innerHTML = "Republican Votes";
	}else if(overMode == "LVA"){
		canvasLVA();
		document.getElementById("y-axis").innerHTML = "Population";
		document.getElementById("x-axis").innerHTML = "Proportion of Votes: Democrat <-> Republican";
	}
	
	
	yearSet(overYear);
	d3.select('#scat-marks').selectAll('line').remove();
	highlightList.forEach(i => extraScatMarksInit(highlightMap[i]));
};

const yearSet = function (year) {
	overYear = year;
	topBar();
	if(overMode == 'RVD'){
		rvbColorState();
  		moveTicks();
	}
	else if(overMode == 'PUR'){
		purpleColorState();
  		moveTicks();
	}
	else if(overMode == "LVA"){
		lvaColorState();
		lvaMoveTicks();
	}
};


//#################################
//
//       Helper Functions
//
//################################

//Count total popular vote for a given party
function countPopular(party){
	var sum = 0;	
	for (var state in pop){
		if(!Number.isNaN(parseInt(pop[state][overYear][party]))) sum+=parseInt(pop[state][overYear][party]);
	}
	return sum;
}

//Count total electoral vote for a given party
function countElectoral(party){
	var sum = 0;	
	for (var state in pop){
		if(!Number.isNaN(parseInt(ele[state][overYear][party]))) sum+=parseInt(ele[state][overYear][party]);
	}
	return sum;
}

//Get maximum vote for a given party
function getMaxVal(){
	var maxVal = 0;
	for(var state in pop){
		if(state == 'columns') break;
		else if (maxVal<(parseInt(pop[state][2020]['D'])+parseInt(pop[state][2020]['R']))) maxVal = parseInt(pop[state][2020]['D'])+parseInt(pop[state][2020]['R']);
	}
	return maxVal;
}

//Adds text to map
function addText(toPut, width, height){
    d3.select('#us-map')
    .append('text')
    .attr("class", "info")
    .text(toPut)
    .attr("font-family", "sans-serif")
    .attr("font-weight", "100")
    .attr("x", width)
    .attr("y", height);
}

//#################################
//
//        Curve Functions
//
//################################

//Sets gradient curve, to better display more significant disparities between 
//democratic and republican states
function curveFunc(n){
	return 1/(1+Math.pow(Math.E, (-8*(n-.5))));
}

//Replicates the behavior of curveFunc between .5 and 1 between 0 and 1, and
//expands the range such that the function starts at 0 and ends (near) 1.
function curveFuncExpand(n){
	return 2/(1+Math.pow(Math.E, (-4*(n)))) - 1;
}

//Better displays ordinal differences in population between states
function curveFuncUnique(n){
	return Math.sqrt(2/(1+Math.pow(Math.E, (-4*(n)))) - 1);
}

//Inverts the above function, for graphical display
function invCurveFuncUnique(n){
	return Math.log((1/(Math.pow(n, 2) + 1)) -.5)/(-4) -.17;
}


//################################
//
//     Hex Coloring Functions
//
//################################

function purpleColorState(){
	var helperMyColor = d3.scaleLinear().domain([0,1]).range(["red", "blue"]);
	var myColor = function(n, m) {return helperMyColor(curveFunc(n/(n+m)));};
	d3.select('#us-map').selectAll('.state').transition().duration(transDur).style("fill", (d) => myColor(parseFloat(d['DN_' + overYear.toString()]), parseFloat(d['RN_' + overYear.toString()] )));
}

function lvaColorState(){
	var maxVal = getMaxVal();
	var makeColor = function(repCount, demCount){
		var count = repCount+demCount;
		var half = count/2;
		var lum = 255*(curveFuncExpand(count/parseFloat(maxVal)));
		var proportion = (repCount > demCount) ? curveFuncExpand((repCount - half)/half) : curveFuncExpand((demCount-half)/half);
		proportion = 1-proportion; 
		return (repCount > demCount) ? d3.rgb(lum,proportion*lum,proportion*lum) : d3.rgb(proportion*lum,proportion*lum, lum);
	};
	d3.select('#us-map').selectAll('.state').transition().duration(transDur).style("fill", (d) => makeColor(parseFloat(d['RN_'+overYear.toString()]),parseFloat(d['DN_'+overYear.toString()])));
	

}

function rvbColorState(){
	d3.select('#us-map').selectAll('.state').transition().duration(transDur)
		.style("fill", (d) => (parseInt(d['RN_' + overYear.toString()]) > parseInt(d['DN_' + overYear.toString()])) ? "red" : "blue");
}


//################################
//
//     Tick Moving Functions
//
//################################


function lvaMoveTicks(){
	var maxVal = getMaxVal();

	var xVal = function(dem, rep){
		var count = dem+rep;
		return scatSize*curveFunc(rep/count);
	}

	d3.select('#scat-marks-container').select('#scat-marks').selectAll("circle").transition().duration(transDur).attr("cy", (d) => scatSize-scatSize*curveFuncExpand((parseFloat(d['DN_'+overYear.toString()])+parseFloat(d['RN_'+overYear.toString()]))/maxVal)).attr("cx", (d) => xVal(parseFloat(d['DN_'+overYear.toString()]),parseFloat(d['RN_'+overYear.toString()])));
}



function moveTicks(){
	var maxVal = 0;
	for(var state in pop){
		if(state == 'columns') break;
		else if (maxVal<parseInt(pop[state][2020]['D'])) maxVal = parseInt(pop[state][2020]['D']);
		else if (maxVal<parseInt(pop[state][2020]['R'])) maxVal = parseInt(pop[state][2020]['R']);
	}
	maxVal *= 1.2;
	d3.select('#scat-marks-container').select('#scat-marks').selectAll("circle").transition().duration(transDur).attr("cx", (d) => scatSize*curveFuncUnique(parseFloat(d['RN_'+overYear.toString()])/maxVal)).attr("cy", (d) => scatSize-scatSize*curveFuncUnique(parseFloat(d['DN_'+overYear.toString()])/maxVal));
}

//################################
//
//     Top Bar Function
//
//################################

function topBar(){
	var dvotes = countPopular('D');
	var rvotes = countPopular('R');
	var delect = countElectoral('D');
	var relect = countElectoral('R');
	document.getElementById("D-pv-txt").innerHTML = dvotes; 
	document.getElementById("R-pv-txt").innerHTML = rvotes;
	document.getElementById("D-ec-txt").innerHTML = delect;
	document.getElementById("R-ec-txt").innerHTML = relect;
	
	d3.select("#D-ec-bar").transition().duration(transDur).attr("width", (delect/(relect+delect)).toString());	
	d3.select("#R-ec-bar").transition().duration(transDur).attr("width", (relect/(relect+delect)).toString());	
	d3.select("#D-pv-bar").transition().duration(transDur).attr("width", (dvotes/(dvotes+rvotes)).toString());	
	d3.select("#R-pv-bar").transition().duration(transDur).attr("width", (rvotes/(dvotes+rvotes)).toString());	
	document.getElementById("D-name").innerHTML = window.p3.name[overYear]['D'];
	document.getElementById("R-name").innerHTML = window.p3.name[overYear]['R'];
}

//################################
//
//     Canvas Functions
//
//################################

export const canvasRVD = function () {
  const canvas = document.querySelector('#scat-canvas');
  canvas.width = scatSize;
  canvas.height = scatSize;
  scatContext = canvas.getContext('2d');
  scatImage = scatContext.createImageData(scatSize, scatSize);
  for (let k = 0, j = 0; j < scatSize; j++) {
    for (let i = 0; i < scatSize; i++) {
	if((j+i) > scatSize){
		scatImage.data[k++] = 255;
		scatImage.data[k++] = 0;
		scatImage.data[k++] = 0;
		scatImage.data[k++] = 255;
	}
	else{
		scatImage.data[k++] = 0;
		scatImage.data[k++] = 0;
		scatImage.data[k++] = 255;
		scatImage.data[k++] = 255;
	}
    }
  }
  scatContext.putImageData(scatImage, 0, 0);
};


export const canvasPUR = function () {
  const canvas = document.querySelector('#scat-canvas');
  canvas.width = scatSize;
  canvas.height = scatSize;
  scatContext = canvas.getContext('2d');
  scatImage = scatContext.createImageData(scatSize, scatSize);
  for (let k = 0, j = 0; j < scatSize; j++) {
    for (let i = 0; i < scatSize; i++) {
	var ratio;
	
	if(i == 0){
		ratio = 0;
	}
	if(j == 0){
		ratio = 1;
	}
	var x = scatSize * (invCurveFuncUnique(i/scatSize));
	var y = scatSize * (invCurveFuncUnique((scatSize-j)/scatSize));
	ratio = curveFunc(x/(x+y));
	var invRatio = 1-ratio;
	scatImage.data[k++] = (ratio)*255;
	scatImage.data[k++] = 0;
	scatImage.data[k++] = (invRatio)*255;
	scatImage.data[k++] = 255;
    }
  }
  scatContext.putImageData(scatImage, 0, 0);
};

export const canvasLVA = function () {
  const canvas = document.querySelector('#scat-canvas');
  canvas.width = scatSize;
  canvas.height = scatSize;
  scatContext = canvas.getContext('2d');
  scatImage = scatContext.createImageData(scatSize, scatSize);

  for (let k = 0, j = 0; j < scatSize; j++) {
    for (let i = 0; i < scatSize; i++) {
	var lum = 255 * ((scatSize - j)/scatSize);
	var proportion;
	if(i < (scatSize/2)){
		proportion = 1-curveFuncExpand(((scatSize/2) - i)/(scatSize/2));
		scatImage.data[k++] = lum*proportion;
		scatImage.data[k++] = lum*proportion;
		scatImage.data[k++] = lum;
		scatImage.data[k++] = 255;
	} else {
		proportion = 1-curveFuncExpand((i - (scatSize/2))/(scatSize/2));
		scatImage.data[k++] = lum;
		scatImage.data[k++] = lum*proportion;
		scatImage.data[k++] = lum*proportion;
		scatImage.data[k++] = 255;
	}
    }
  }
  scatContext.putImageData(scatImage, 0, 0);
};

//################################
//
//    Gives Path of voting
//
//################################

export const extraScatMarksInit = function (data) {
	var x1, x2, y1, y2;
	
	if(overMode == 'LVA'){

		var maxVal = getMaxVal();

		var xVal = function(dem, rep){
			var count = dem+rep;
			return scatSize*curveFunc(rep/count);
		}

		for(var yearIndex = 0; yearIndex < yearen.length-1; yearIndex++){
			var currentYear = yearen[yearIndex];	
			var nextYear = yearen[yearIndex+1];	
			var local = d3.select('#scat-marks')
			    .append('line')
			    .attr('x1', xVal(parseFloat(data['DN_'+currentYear.toString()]),parseFloat(data['RN_'+currentYear.toString()])))
			    .attr('y1', scatSize-scatSize*curveFuncExpand((parseFloat(data['DN_'+currentYear.toString()])+parseFloat(data['RN_'+currentYear.toString()]))/maxVal))
			    .attr('x2', xVal(parseFloat(data['DN_'+nextYear.toString()]),parseFloat(data['RN_'+nextYear.toString()])))	
			    .attr('y2', scatSize-scatSize*curveFuncExpand((parseFloat(data['DN_'+nextYear.toString()])+parseFloat(data['RN_'+nextYear.toString()]))/maxVal))
			    .attr('stroke', `rgba(0,255,0,255)`) 
			    .attr('stroke-opacity', 0.8)
			    .attr('stroke-width', 3)
			    .attr('fill', 'black')
			    .attr('fill-opacity', 0);
		}
	} else {
		var maxVal = 0;
		for(var state in pop){
			if(state == 'columns') break;
			else if (maxVal<parseInt(pop[state][2020]['D'])) maxVal = parseInt(pop[state][2020]['D']);
			else if (maxVal<parseInt(pop[state][2020]['R'])) maxVal = parseInt(pop[state][2020]['R']);
		}
		maxVal *= 1.2;


		for(var yearIndex = 0; yearIndex < yearen.length-1; yearIndex++){
			var currentYear = yearen[yearIndex];	
			var nextYear = yearen[yearIndex+1];	
			  d3.select('#scat-marks')
			    .append('line')
			    .attr('x1', scatSize*curveFuncUnique(parseFloat(data['RN_'+currentYear.toString()])/maxVal))
			    .attr('y1', scatSize-scatSize*curveFuncUnique(parseFloat(data['DN_'+currentYear.toString()])/maxVal))
			    .attr('x2', scatSize*curveFuncUnique(parseFloat(data['RN_'+nextYear.toString()])/maxVal))
			    .attr('y2', scatSize-scatSize*curveFuncUnique(parseFloat(data['DN_'+nextYear.toString()])/maxVal))
			    .attr('stroke', `rgba(0,255,0,255)`) 
			    .attr('stroke-opacity', 0.8)
			    .attr('stroke-width', 3)
			    .attr('fill', 'black')
			    .attr('fill-opacity', 0);
		}
	}
	


};

//################################
//
//    For hovering over
//    hex on grid
//
//################################

function mapMouseOver(d, ii){
    var height = d3.select('#us-map').node().getBBox()['height'];
    var width = d3.select('#us-map').node().getBBox()['width'];
    addText("Popular Vote:", width*.2, height*.05);
    addText("Electoral Vote:", width*.6, height*.05);
    addText("Democrat: "+parseInt(ii["DN_"+overYear.toString()]).toLocaleString(), width*.2, height*.16);
    addText("Republican: "+parseInt(ii["RN_"+overYear.toString()]).toLocaleString(), width*.2, height*.207);
    addText("Democrat: "+ii["DE_"+overYear.toString()], width*.6, height*.16);
    addText("Republican: "+ii["RE_"+overYear.toString()], width*.6, height*.207);
    addText(ii.StateName, width*.8, height-20);
    lowerBar(width*.8, height, ii);
}


function mapMouseOff(d, ii){
    d3.select('#us-map')
    .selectAll('.info')
    .remove();
}

//Creates the various charts. poorly written, but still
//relatively little copying

function lowerBar(width, height, ii){


	var dN =  parseFloat(ii["DN_"+overYear.toString()])	
	var rN =  parseFloat(ii["RN_"+overYear.toString()])	
	var dE =  parseFloat(ii["DE_"+overYear.toString()])	
	var rE =  parseFloat(ii["RE_"+overYear.toString()])	
	
	//Makes the Popular vote visualization, a bar chart, as recommended by Cleveland and McGill
	d3.select('#us-map').append('rect').attr("class", "info").attr('x', width*.3).attr('y', height*.1).attr('width', Math.round(width*.4*(rN/(dN+rN)))).attr('height', 10).attr('fill', 'red');
	d3.select('#us-map').append('rect').attr("class", "info").attr('x', width*.3).attr('y', height*.07).attr('width', Math.round(width*.4*(dN/(dN+rN)))).attr('height', 10).attr('fill', 'blue');

	//Makes the Electoral College visualization, which is a single unit of a percent stacked bar chart (to imitate a pie chart with less hassle)
	d3.select('#us-map').append('rect').attr("class", "info").attr('x', width*.75).attr('y', height*.07).attr('width', Math.round(width*.2*(dE/(dE+rE)))).attr('height', 17).attr('fill', 'blue')
	d3.select('#us-map').append('rect').attr("class", "info").attr('x', width*.75+Math.round(width*.2*(dE/(dE+rE)))).attr('y', height*.07)
		.attr('width', Math.round(width*.2*(rE/(dE+rE)))).attr('height', 17).attr('fill', 'red');


	//Makes bars around the hover visualizations, for viewer convenience
	d3.select('#us-map').append('rect').attr("class", "info").attr('x', width*.75).attr('y', height*.07).attr('width', width*.2).attr('height', 17).attr('fill', 'none')
		.style("stroke", "white").style("stroke-width", 3)
	d3.select('#us-map').append('rect').attr("class", "info").attr('x', width*.3).attr('y', height*.065).attr('width', 3).attr('height', 25).attr('fill', 'white');
}




//################################
//
//    For hovering over
//    label on scatterplot 
//
//################################

function handleMouseOver(d, ii){
    d3.select('#scat-marks')
    .append('text')
    .text(ii.StateAbbr)
    .attr("font-family", "sans-serif")
    .attr("font-weight", "100")
    .attr("x", 5)
    .attr("y", 15);
}


function handleMouseOff(d, ii){
    d3.select('#scat-marks')
    .selectAll('text')
    .remove();
}

//################################
//
//    For clicking on
//    label on scatterplot 
//    or hex
//
//################################

function handleClick(d, ii) {
    var index = highlightList.indexOf(ii.StateAbbr);
    if (index < 0){
	    highlightList.push(ii.StateAbbr);
	    highlightMap[ii.StateAbbr] = ii;
    }
    else{
	    highlightList.splice(index, 1);
	    delete highlightMap[ii.StateAbbr];
    }

    d3.select('#scat-marks').selectAll('circle')
	       .attr('fill', "rgb(0,255,0)")
	       .attr('fill-opacity', (data) => (highlightList.includes(data.StateAbbr)) ? "255" : "0");
    d3.select("#us-map").selectAll('.state')
	      .style("stroke", (data) => `rgba(0,255,0,${(highlightList.includes(data.StateAbbr)) ? "255" : "0"})`)
	      .style("stroke-width", "5");


    d3.select('#scat-marks').selectAll('line').remove();
    highlightList.forEach(i => extraScatMarksInit(highlightMap[i]));
}

