var dataArray = [20, 40, 50, 60]
var width = 500
var height = 500

var color = d3.scaleLinear()
            .domain([d3.min(dataArray), d3.max(dataArray)])
            .range(["red", "black"])

var canvas = d3.select("body")
            .append("svg")
            .attr("width", width)
            .attr("height", height)

var bars = canvas.selectAll("rect")
            .data(dataArray)
            .enter()
                .append("rect")
                .attr("width", 1000)
                .attr("height", 50)
                .attr("fill", function(d) {return color(d)})
                .attr("y", function(d, i) {return i * 100})
