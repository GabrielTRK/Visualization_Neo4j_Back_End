if(!sessionStorage.getItem("logged")){
    window.location.href = "login.html"
}

if (sessionStorage.getItem("projectName") && sessionStorage.getItem("solutionID")) {
    $(document).ready(function(){
        $('[data-toggle="tooltip"]').tooltip();   
      });
    var margin = { top: 50, left: 50, right: 50, bottom: 50 };
    var margin2 = { top: 100, left: 100, right: 400, bottom: 100 };
    height = 400 //- margin.top - margin.bottom;
    width = 800 //- margin.left - margin.right;
    var params = { numDias: 0, diaActual: 0 };

    var html_p_Open = '<p>'

    var html_p_Close = '</p>'

    projectName = sessionStorage.getItem("projectName")
    solutionID = sessionStorage.getItem("solutionID")

    currentAnimation = false;

    mainURL = 'http://localhost:8080/' + projectName + '/' + solutionID

    var projection = d3.geoMercator()
        .translate([width / 2, height / 2])
        .scale(100);

    var path = d3.geoPath()
        .projection(projection);

    const zoom = d3.zoom()
        //.translateExtent([[0, 0], [width, height]])
        .scaleExtent([1, 10])
        .on('zoom', handlezoom);

        var svg = d3.select('#map')
        .append('svg')
        .attr("viewBox", [0, 0, width, height])
        .attr('height', height + margin2.top + margin2.bottom)
        .attr('width', width + margin2.left + margin2.right)
        .append('g')
        .attr('transform', 'translate(' + margin.left + ',' + margin.top + ')');
    
    var Tooltip = d3.select('#map')
        .append("div")
        .style("opacity", 0)
        .attr("class", "tooltip")
        .style("background-color", "white")
        .style("border", "solid")
        .style("border-width", "2px")
        .style("border-radius", "5px")
        .style("padding", "5px")
    
    var mouseover = function (d) {
        Tooltip
            .style("opacity", 1)
        d3.select(this)
            .attr('class', 'connectionSelected')
    }
    var mousemove = function (d) {
        Tooltip
            .html(html_p_Open + 'Departures: ' + d.iataOrigen + html_p_Close + html_p_Open + 'Destination: ' + d.iataDestino + html_p_Close
                + html_p_Open + 'Number of passengers: ' + d.pasajeros + html_p_Close)
            .style("left", (d3.mouse(this)[0]) + 0 + "px")
            .style("top", (d3.mouse(this)[1]) + 0 + "px")
    }
    var mouseleave = function (d) {
        Tooltip
            .style("opacity", 0)
        d3.select(this)
            .attr('class', 'connection')
    }

    function handlezoom() {
        const currentTransform = d3.event.transform;
    
        svg.attr('transform', currentTransform);
    }
    
    function initzoom() {
        svg.call(zoom)
    }
    
    
    d3.queue()
        .defer(d3.json, 'C:/Users/penad/Desktop/Doctorado/NSGA-III/Visualization_Neo4j_Back_End/src/main/resources/static/world.topojson')
        .await(drawWorld);
    
    init();
    initzoom();
}else{
    window.location.href = "list.html"
}






function drawWorld(error, world) {
    var countries = topojson.feature(world, world.objects.countries).features;
    svg.selectAll('.country')
        .data(countries)
        .enter().append('path')
        .attr('class', 'country')
        .attr('d', path)
}

function init() {
    getNumDias();
    circulosYConexiones();

}

function getNumDias() {

    fetch(mainURL + '/numDias').then(res => {
        return res.json()
    })
        .then(dataBack => {

            params.numDias = dataBack
            actualizarBotones();
        }
        )
}
//Devolver desde el Back una lista de aeropuertos en donde cada aeropuerto contiene las coordenadas y el iata. 
//También se debe indicar si cada aeroipuerto es Origen o destino para cambiarso color en el mapa

function circulosYConexiones() {

    dia = params.diaActual

    fetch(mainURL + '/' + dia)
        .then(res => {
            return res.json()
        })
        .then(dataBack => {
            drawServerResult(dataBack)
        })
}

function drawServerResult(dataBack) {
    document.getElementById('startDate').value = dataBack.fechas.Fecha_Inicial
    document.getElementById('endDate').value = dataBack.fechas.Fecha_Final
    document.getElementById('curDate').value = dataBack.fechas.Fecha_Actual


    d3.selectAll('.airport').remove()
    d3.selectAll('.connection').remove()
    svg.selectAll('.airport')
        .data(dataBack.listaAeropuertos)
        .enter().append('circle')
        .attr('class', 'airport')
        .attr('r', 0.5)
        .attr('cx', function (d) {
            var coords = projection([d.longitud, d.latitud])
            return coords[0]
        })
        .attr('cy', function (d) {
            var coords = projection([d.longitud, d.latitud])
            return coords[1]
        })
        .attr('fill', function (d) {
            if (d.origen == true) {
                return 'green'
            }
            else
                return 'blue'
        })

    svg.selectAll('.connection')
        .data(dataBack.coordenadasConexiones)
        .enter().append('path')
        .attr('class', 'connection')
        .attr('d', function (d) {
            var xO = projection([d.longitudeO, d.latitudeO])[0]
            var yO = projection([d.longitudeO, d.latitudeO])[1]
            var xD = projection([d.longitudeD, d.latitudeD])[0]
            var yD = projection([d.longitudeD, d.latitudeD])[1]
            return 'M' + xO + ' ' + yO + ' L' + xD + ' ' + yD
        })
        .attr('stroke', function (d) {
            if (d.abierto_cerrado) {
                return 'black'
            } else {
                return 'red'
            }
        })
        .on("mouseover", mouseover)
        .on("mousemove", mousemove)
        .on("mouseleave", mouseleave)

}

function solucionDiaAnterior() {

    if (currentAnimation) {
        document.getElementById('curDate').style.animationName = 'changedDate1st'
        document.getElementById('curDate').style.animationDuration = '5s'
        document.getElementById('curDateSpan').style.animationName = 'changedDate1st'
        document.getElementById('curDateSpan').style.animationDuration = '5s'
    } else {
        document.getElementById('curDate').style.animationName = 'changedDate2nd'
        document.getElementById('curDate').style.animationDuration = '5s'
        document.getElementById('curDateSpan').style.animationName = 'changedDate2nd'
        document.getElementById('curDateSpan').style.animationDuration = '5s'
    }
    currentAnimation = !currentAnimation



    params.diaActual -= 1

    dia = params.diaActual

    return fetch(mainURL + '/' + dia).then(res => {
        return res.json()
    })
        .then(dataBack => {
            drawServerResult(dataBack)
            actualizarBotones()

        }
        )
}

function solucionDiaPosterior() {

    if (currentAnimation) {
        document.getElementById('curDate').style.animationName = 'changedDate1st'
        document.getElementById('curDate').style.animationDuration = '5s'
        document.getElementById('curDateSpan').style.animationName = 'changedDate1st'
        document.getElementById('curDateSpan').style.animationDuration = '5s'
    } else {
        document.getElementById('curDate').style.animationName = 'changedDate2nd'
        document.getElementById('curDate').style.animationDuration = '5s'
        document.getElementById('curDateSpan').style.animationName = 'changedDate2nd'
        document.getElementById('curDateSpan').style.animationDuration = '5s'
    }
    currentAnimation = !currentAnimation

    params.diaActual += 1

    dia = params.diaActual

    return fetch(mainURL + '/' + dia).then(res => {
        return res.json()
    })
        .then(dataBack => {
            drawServerResult(dataBack)
            actualizarBotones()

        }
        )
}

function actualizarBotones() {
    if (params.diaActual == 0) {
        document.getElementById("anterior").disabled = true;
    } else {
        document.getElementById("anterior").disabled = false;
    }
    if (params.diaActual == params.numDias - 1) {
        document.getElementById("posterior").disabled = true;
    } else {
        document.getElementById("posterior").disabled = false;
    }

}

function goDescription() {
    sessionStorage.setItem("load", 'load')
    window.location.href = "/html/description.html"
}

function logOut(){
    sessionStorage.clear()
    window.location.href = "../index.html"
}