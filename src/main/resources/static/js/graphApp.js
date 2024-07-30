var params = {numDias: 0, diaActual: 0, numSoluciones: 0, solucionActual: 0};

var LineChart_margin = {top: 10, right: 30, bottom: 30, left: 30},
    LineChart_width = 300 - LineChart_margin.left - LineChart_margin.right,
    LineChart_height = 300 - LineChart_margin.top - LineChart_margin.bottom;

var BarChart_margin = {top: 10, right: 30, bottom: 30, left: 30},
    BarChart_width = 400 - BarChart_margin.left - BarChart_margin.right,
    BarChart_height = 300 - BarChart_margin.top - BarChart_margin.bottom;
    barPadding = 1

var PieChart_margin = 50,
    PieChart_width = 400 - PieChart_margin - PieChart_margin,
    PieChart_height = 400 - PieChart_margin - PieChart_margin;

var radius = Math.min(PieChart_width, PieChart_height) / 2 - PieChart_margin

var format2Decimals = d3.format(".2f")

var svgLine = d3.select("#lineChart")
    .append("svg")
      .attr("width", LineChart_width + LineChart_margin.left + LineChart_margin.right)
      .attr("height", LineChart_height + LineChart_margin.top + LineChart_margin.bottom)
    .append("g")
      .attr("transform", "translate(" + LineChart_margin.left + "," + LineChart_margin.top + ")");


var svgBar = d3.select("#barChart")
    .append("svg")
    .attr("width", BarChart_width + BarChart_margin.left + BarChart_margin.right)
    .attr("height", BarChart_height + BarChart_margin.top + BarChart_margin.bottom)
    .append("g")
    .attr("transform", "translate(" + BarChart_margin.left + "," + BarChart_margin.top + ")");

var svgPieP = d3.select("#pieChartPersonas")
    .append("svg")
    .attr("width", PieChart_width)
    .attr("height", PieChart_height)
    .append("g")
    .attr("transform", "translate(" + PieChart_width / 2 + "," + PieChart_height / 2 + ")");

var svgPieV = d3.select("#pieChartVuelos")
    .append("svg")
    .attr("width", PieChart_width)
    .attr("height", PieChart_height)
    .append("g")
    .attr("transform", "translate(" + PieChart_width / 2 + "," + PieChart_height / 2 + ")");



init();


function init(){
  getNumSoluciones()
}

function getNumSoluciones(){
  fetch('http://localhost:8080/numSoluciones').then(res => {
      return res.json()
  })
  .then(dataBack => {
      
      params.numSoluciones = dataBack
      params.solucionActual = dataBack-1
      getNumDias();
      actualizarListaSoluciones();
      drawLineChart()
      drawBarChart()
      drawPieChartPersonas()
      drawPieChartVuelos()

  }
  )
}

function getNumDias(){

  /*p_id_sol = 'id='

    id_sol = params.solucionActual
    
    url = 'http://localhost:8080/solucionI/numDias' + '?' + p_id_sol + id_sol*/

    id = params.solucionActual
    
    url = 'http://localhost:8080/' + id + '/numDias'


  fetch(url).then(res => {
      return res.json()
  })
  .then(dataBack => {
      
      params.numDias = dataBack
  }
  )
}

function actualizarListaSoluciones(){
    
  const select = document.getElementById("Soluciones");

  for(var i = 0; i < params.numSoluciones; i++){
      const newOption = document.createElement('option');
      const optionText = document.createTextNode(i);
      newOption.appendChild(optionText);
      newOption.setAttribute('value', i);
      select.appendChild(newOption);
      if(i == params.numSoluciones-1){
          newOption.selected = true;
      }
  }
}

function drawLineChart(){

  /*p_id_sol = 'id='

  id_sol = params.solucionActual + '&'
  
  url = 'http://localhost:8080/solucionI/hist' + '?' + p_id_sol + id_sol*/

  id = params.solucionActual
  
  url = 'http://localhost:8080/' + id + '/hist'

        fetch(url)
        .then(res => {
            return res.json()
        })
        .then(dataBack => {
            drawServerResultLine(dataBack)
        })
}

function drawBarChart(){

  /*p_id_sol = 'id='

  id_sol = params.solucionActual + '&'
  
  url = 'http://localhost:8080/solucionI/objetivos' + '?' + p_id_sol + id_sol*/

  id = params.solucionActual
  
  url = 'http://localhost:8080/' + id + '/objetivos'

    fetch(url)
    .then(res => {
        return res.json()
    })
    .then(dataBack => {
        drawServerResultBar(dataBack)
    })
}

function drawPieChartPersonas(){

  /*p_id_sol = 'id='

  id_sol = params.solucionActual + '&'
  
  url = 'http://localhost:8080/solucionI/personas' + '?' + p_id_sol + id_sol*/

  id = params.solucionActual
  
  url = 'http://localhost:8080/' + id + '/personas'

    fetch(url)
    .then(res => {
        return res.json()
    })
    .then(dataBack => {
        drawServerResultPiePersonas(dataBack)
    })

}

function drawPieChartVuelos(){

  /*p_id_sol = 'id='

  id_sol = params.solucionActual + '&'
  
  url = 'http://localhost:8080/solucionI/vuelos' + '?' + p_id_sol + id_sol*/

  id = params.solucionActual
  
  url = 'http://localhost:8080/' + id + '/vuelos'

    fetch(url)
    .then(res => {
        return res.json()
    })
    .then(dataBack => {
        drawServerResultPieVuelos(dataBack)
    })
  
}


function drawServerResultLine(dataPoints){
  d3.selectAll('.LineXAxis').remove()
  d3.selectAll('.LineYAxis').remove()
  d3.selectAll('.line').remove()
    var x = d3.scaleLinear()
        .domain(d3.extent(dataPoints, d =>  d.iteracion +1))
        .range([ 0, LineChart_width ]);
    svgLine.append("g")
        .attr("transform", "translate(0," + LineChart_height + ")")
        .attr('class', 'LineXAxis')
        .call(d3.axisBottom(x));

    var y = d3.scaleLinear()
        //.domain([d3.min(dataPoints, d =>  d.fitness), d3.max(dataPoints, d =>  d.fitness)])//Poner el 0 en min
        .domain([0, d3.max(dataPoints, d =>  d.fitness)])
        .range([ LineChart_height, 0 ]);
    svgLine.append("g")
        .attr('class', 'LineYAxis')
        .call(d3.axisLeft(y));

    svgLine.append("path")
        .datum(dataPoints)
        .attr('class', 'line')
        .attr("fill", "none")
        .attr("stroke", "steelblue")
        .attr("stroke-width", 2)
        .attr("d", d3.line()
          .x(function(d) { return x(d.iteracion+1) })
          .y(function(d) { return y(d.fitness) }));

    svgLine.append("text")
        .attr("x", (LineChart_width + LineChart_margin.right)/2)
        .attr("y", 15)
        .attr("class","title")				
        .attr("text-anchor", "middle")
        .text("Fitness por iteración");
        
}

function drawServerResultBar(dataPoints){
  d3.selectAll('.barYAxis').remove()
  d3.selectAll('.barXAxis').remove()
  d3.selectAll('.barRect').remove()

    var xScale = d3.scaleLinear()
			.domain([0, dataPoints.length])
			.range([0, BarChart_width]);

    var yScale = d3.scaleLinear()
            .domain([0, 1])
            .range([BarChart_height, 0]);

    svgBar.selectAll("rect")
		   .data(dataPoints)
		   .enter()
		   .append("rect")
       .attr('class', 'barRect')
			.attr("x", function(d, i) {
			    return xScale(i);
			})
		   .attr("width", BarChart_width / dataPoints.length - barPadding)   
			.attr("y", function(d) {
			    return yScale(d.valor);
			})  
			.attr("height", function(d) {
			    return BarChart_height-yScale(d.valor);
			})
			.attr("fill", "lightgrey");

    svgBar.selectAll(".barYAxis")
            .data(dataPoints)
            .enter()
            .append("text")
            .attr('class', 'barYAxis')
            .text(function(d) {
                    return format2Decimals(d.valor);
            })
            .attr("text-anchor", "middle")
            // Set x position to the left edge of each bar plus half the bar width
            .attr("x", function(d, i) {
                    return (i * (BarChart_width / dataPoints.length)) + ((BarChart_width / dataPoints.length - barPadding) / 2);
            })
            .attr("y", function(d) {
                    return yScale(d.valor) + 14;
            })

    var xLabels = svgBar
		.append("g")
		.attr("transform", "translate(" + 0 + "," + (BarChart_margin.top + BarChart_height)  + ")");
	
	xLabels.selectAll("text.barXAxis")
		  .data(dataPoints)
		  .enter()
		  .append("text")
		  .text(function(d) { return d.nombre;})
		  .attr("text-anchor", "middle")
			// Set x position to the left edge of each bar plus half the bar width
						   .attr("x", function(d, i) {
						   		return (i * (BarChart_width / dataPoints.length)) + ((BarChart_width / dataPoints.length - barPadding) / 2);
						   })
		  .attr("y", 15)
		  .attr("class", "barXAxis");			
	 
	// Title
	
	svgBar.append("text")
		.attr("x", (BarChart_width)/2)
		.attr("y", 15)
		.attr("class","title")				
		.attr("text-anchor", "middle")
		.text("Problem criteria");
        

}

function drawServerResultPiePersonas(data){
  d3.selectAll('.piePPath').remove()
  d3.selectAll('.piePText').remove()
  
  var color = d3.scaleOrdinal()
    .domain(data)
    .range(["steelblue", "#a05d56"])

  // Compute the position of each group on the pie:
  var pie = d3.pie()
    .value(function (d) { return d.value.valorPersonas; })
  var data_ready = pie(d3.entries(data))

  // Build the pie chart: Basically, each part of the pie is a path that we build using the arc function.
  svgPieP.selectAll('.piePPath')
    .data(data_ready)
    .enter()
    .append('path')
    .attr('class', 'piePPath')
    .attr('d', d3.arc()
      .innerRadius(0)
      .outerRadius(radius)
    )
    .attr('fill', function (d) { return (color(d.data.value.campoPersonas)) })
    .attr("stroke", "black")
    .style("stroke-width", "1px")
    .style("opacity", 0.7);

  svgPieP.selectAll('mySlices')
    .data(data_ready)
    .enter()
    .append('text')
    .attr('class', 'piePText')
    .text(function (d) { return d.data.value.valorPersonas })
    .attr("transform", function (d) {
      return "translate(" + d3.arc()
        .innerRadius(0)
        .outerRadius(radius).centroid(d) + ")";
    })
    .style("text-anchor", "middle")
    .style("font-size", 14)

  svgPieP.append("text")
    .attr("x", 0)
    .attr("y", -125)
    .attr("class", "title")
    .attr("text-anchor", "middle")
    .text("Afected people");
}

function drawServerResultPieVuelos(data){
  d3.selectAll('.pieVPath').remove()
  d3.selectAll('.pieVText').remove()

  var color = d3.scaleOrdinal()
    .domain(data)
    .range(["steelblue", "#a05d56"])

  // Compute the position of each group on the pie:
  var pie = d3.pie()
    .value(function (d) { return d.value.numVuelos; })
  var data_ready = pie(d3.entries(data))
  
  // Build the pie chart: Basically, each part of the pie is a path that we build using the arc function.
  svgPieV.selectAll('.pieVPath')
    .data(data_ready)
    .enter()
    .append('path')
    .attr('class', 'pieVPath')
    .attr('d', d3.arc()
      .innerRadius(0)
      .outerRadius(radius)
    )
    .attr('fill', function (d) { return (color(d.data.value.nombreVuelos)) })
    .attr("stroke", "black")
    .style("stroke-width", "1px")
    .style("opacity", 0.7);

  svgPieV.selectAll('mySlices')
    .data(data_ready)
    .enter()
    .append('text')
    .attr('class', 'pieVText')
    .text(function (d) { return d.data.value.numVuelos })
    .attr("transform", function (d) {
      return "translate(" + d3.arc()
        .innerRadius(0)
        .outerRadius(radius).centroid(d) + ")";
    })
    .style("text-anchor", "middle")
    .style("font-size", 14)

  svgPieV.append("text")
    .attr("x", 0)
    .attr("y", -125)
    .attr("class", "title")
    .attr("text-anchor", "middle")
    .text("Canceled flights");
}

function selectChange(sel){
  
  params.solucionActual = parseInt(sel.options[sel.selectedIndex].value)
  drawLineChart();
  drawBarChart();
  drawPieChartPersonas();
  drawPieChartVuelos();

}