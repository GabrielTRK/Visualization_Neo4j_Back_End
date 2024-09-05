/*if (!sessionStorage.getItem("logged")) {
    window.location.href = "login.html"
}*/

if (sessionStorage.getItem("projectName") && sessionStorage.getItem("solutionID")) {
    var html_p_Open = '<p>'

    var html_p_Close = '</p>'

    data = null
    
    $(document).ready(function () {
        $('[data-toggle="tooltip"]').tooltip();
    });
    projectName = sessionStorage.getItem("projectName")
    solutionID = sessionStorage.getItem("solutionID")

    currentDay = 0
    currentAir = ''
    separator = '/'
    minDate = new Date("2020/01/01")
    maxDate = new Date("2020/01/01")
    minDateString = ''
    numDias = 0

    mainURL = 'https://138.4.92.155:8081/' + projectName + separator + solutionID + separator

    getConnections()
    getObj()
    getRanges()
    getFit()
} else {
    window.location.href = "list.html"
}
// Get the modal
var modal = document.getElementById("myModal");
// Get the <span> element that closes the modal
var span = document.getElementsByClassName("close")[0];

span.onclick = function () {
    modal.style.display = "none";
}

window.onclick = function (event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}
var connectionClicked = function (d){
    
    elemento = encontrarElemento(d.target.classList[0].split("-"), data.coordenadasConexiones)
    
    modal.style.display = "block";
        if (elemento.abierto_cerrado) {
            document.getElementById('ModalText').innerHTML =
                html_p_Open +
                'Departures: '+ elemento.aerOrigen + ' (' + elemento.iataOrigen + ')'+
                html_p_Close +

                html_p_Open +
                'Destination: '+ elemento.aerDestino + ' (' + elemento.iataDestino + ')'+
                html_p_Close

                + html_p_Open +
                'Number of passengers: ' + elemento.pasajeros +
                html_p_Close

                + html_p_Open +
                'Revenue gained in the catchment area: ' + elemento.ingresos + ' euros' +
                html_p_Close

                + html_p_Open +
                'Revenue gained in the destination airport: ' + elemento.tasas + ' euros' +
                html_p_Close

                + html_p_Open +
                'Imported risk: ' + elemento.riesgo +
                html_p_Close
        } else {
            document.getElementById('ModalText').innerHTML =
                html_p_Open +
                'Departures: '+ elemento.aerOrigen + ' (' + elemento.iataOrigen + ')'+
                html_p_Close +

                html_p_Open +
                'Destination: '+ elemento.aerDestino + ' (' + elemento.iataDestino + ')'+
                html_p_Close

                + html_p_Open +
                'Number of affected passengers: ' + elemento.pasajeros +
                html_p_Close

                + html_p_Open +
                'Revenue lost in the catchment area: ' + elemento.ingresos + ' euros' +
                html_p_Close

                + html_p_Open +
                'Revenue lost in the destination airport: ' + elemento.tasas + ' euros' +
                html_p_Close

                + html_p_Open +
                'Avoided risk: ' + elemento.riesgo +
                html_p_Close
        }
}
function getConnections() {
    fetch(mainURL + currentDay + separator + currentAir).then(res => {
        return res.json()
    })
        .then(dataBack => {
            //Si databack.length == 0 poner mensaje de empty list
            data = dataBack
            document.getElementById('Sdate').value = dataBack.fechas.Fecha_Actual
            document.getElementById('Sdate').min = dataBack.fechas.Fecha_Inicial
            document.getElementById('Sdate').max = dataBack.fechas.Fecha_Final
            minDate = new Date(dataBack.fechas.Fecha_Inicial)
            maxDate = new Date(dataBack.fechas.Fecha_Final)
            numDias = Math.round((maxDate.getTime() - minDate.getTime()) / (1000 * 3600 * 24))
            minDateString = dataBack.fechas.Fecha_Inicial

            row1 = document.getElementById('row1')
            row2 = document.getElementById('row2')

            th1 = document.createElement('th')
            th1.setAttribute("scope", "col");
            th1.classList.add('text-center')
            th1.innerHTML = 'Connections'

            th2 = document.createElement('th')
            th2.setAttribute("scope", "col");
            th2.classList.add('text-center')
            th2.innerHTML = 'Cancelled connections'

            row1.appendChild(th1)
            row2.appendChild(th2)
            for (i = 0; i < dataBack.coordenadasConexiones.length; i++) {
                row1TH = document.createElement('th')
                row1TH.setAttribute("scope", "col");
                row1TH.classList.add(dataBack.coordenadasConexiones[i].iataOrigen + '-' + dataBack.coordenadasConexiones[i].iataDestino)
                row1TH.classList.add('text-center')
                //row1TH.innerHTML = '<a class="' + dataBack.coordenadasConexiones[i].iataOrigen + '-' + dataBack.coordenadasConexiones[i].iataDestino + '" href="#" style="text-decoration:none; color: #000;">' + dataBack.coordenadasConexiones[i].iataOrigen + ' - ' + dataBack.coordenadasConexiones[i].iataDestino + '</a>'
                row1TH.innerHTML = dataBack.coordenadasConexiones[i].iataOrigen + ' - ' + dataBack.coordenadasConexiones[i].iataDestino
                row1TH.onclick = connectionClicked

                row2TD = document.createElement('td')
                row2TD.setAttribute("scope", "col");
                row2TD.classList.add(dataBack.coordenadasConexiones[i].iataOrigen + '-' + dataBack.coordenadasConexiones[i].iataDestino)
                row2TD.classList.add('text-center')
                if (dataBack.coordenadasConexiones[i].abierto_cerrado) {
                    //row2TD.innerHTML = '<a class="' + dataBack.coordenadasConexiones[i].iataOrigen + '-' + dataBack.coordenadasConexiones[i].iataDestino + '" href="#" style="text-decoration:none; color: #000;">' + '1' + '</a>'
                    row2TD.innerHTML = '1'
                    row2TD.style.color = 'black'
                } else {
                    //row2TD.innerHTML = '<a class="' + dataBack.coordenadasConexiones[i].iataOrigen + '-' + dataBack.coordenadasConexiones[i].iataDestino + '" href="#" style="text-decoration:none; color: red;">' + '0' + '</a>'
                    row2TD.innerHTML = '0'
                    row2TD.style.color = 'red'
                }
                row2TD.onclick = connectionClicked

                row1.appendChild(row1TH)
                row2.appendChild(row2TD)
            }
            document.getElementById('tableCaption').innerHTML = dataBack.coordenadasConexiones.length + ' connections'

        }
        )
}

function getObj() {
    fetch(mainURL + 'objetivos').then(res => {
        return res.json()
    })
        .then(dataBack => {
            //Si databack.length == 0 poner mensaje de empty list
            //console.log(dataBack)

            row3 = document.getElementById('row3')
            row4 = document.getElementById('row4')

            th3 = document.createElement('th')
            th3.setAttribute("scope", "col");
            th3.classList.add('text-center')
            th3.innerHTML = 'Objectives'

            th4 = document.createElement('th')
            th4.setAttribute("scope", "col");
            th4.classList.add('text-center')
            th4.innerHTML = 'Optimal values'

            row3.appendChild(th3)
            row4.appendChild(th4)

            for (id = 1; id < 6; id++) {
                for (obj = 1; obj < dataBack.length; obj++) {
                    if (dataBack[obj].nombre == id.toString()) {
                        row3TH = document.createElement('th')
                        row3TH.setAttribute("scope", "col");
                        row3TH.classList.add('text-center')
                        row3TH.innerHTML = '<img src="../Utils/Objective' + dataBack[obj].nombre + '.png" width="149" height="49">'

                        row4TD = document.createElement('td')
                        row4TD.setAttribute("scope", "col");
                        row4TD.classList.add('text-center')
                        if (id == 1 || id == 4) {
                            row4TD.innerHTML = dataBack[obj].valor + '%'
                        } else {
                            row4TD.innerHTML = dataBack[obj].valor
                        }



                        row3.appendChild(row3TH)
                        row4.appendChild(row4TD)
                    }
                }
            }

            /*for (j = 1; j < 6; j++) {

                row3TH = document.createElement('th')
                row3TH.setAttribute("scope", "col");
                row3TH.classList.add('text-center')
                row3TH.innerHTML = '<img src="../Utils/Objective' + dataBack[j].nombre + '.png" width="149" height="49">'

                row4TD = document.createElement('td')
                row4TD.setAttribute("scope", "col");
                row4TD.classList.add('text-center')
                if (j == 1 || j == 4) {
                    row4TD.innerHTML = dataBack[j].valor + '%'
                } else {
                    row4TD.innerHTML = dataBack[j].valor
                }



                row3.appendChild(row3TH)
                row4.appendChild(row4TD)
            }*/

            row5 = document.getElementById('row5')
            row6 = document.getElementById('row6')

            th5 = document.createElement('th')
            th5.setAttribute("scope", "col");
            th5.classList.add('text-center')
            th5.innerHTML = 'Objectives'

            th6 = document.createElement('th')
            th6.setAttribute("scope", "col");
            th6.classList.add('text-center')
            th6.innerHTML = 'Optimal values'

            row5.appendChild(th5)
            row6.appendChild(th6)

            for (id = 6; id < 8; id++) {
                for (obj = 1; obj < dataBack.length; obj++) {
                    if (dataBack[obj].nombre == id.toString()) {
                        row5TH = document.createElement('th')
                        row5TH.setAttribute("scope", "col");
                        row5TH.classList.add('text-center')
                        row5TH.innerHTML = '<img src="../Utils/Objective' + dataBack[obj].nombre + '.png" width="149" height="49">'

                        row6TD = document.createElement('td')
                        row6TD.setAttribute("scope", "col");
                        row6TD.classList.add('text-center')
                        row6TD.innerHTML = dataBack[obj].valor + '%'

                        row5.appendChild(row5TH)
                        row6.appendChild(row6TD)
                    }
                }
            }

            /*for (j = 6; j < dataBack.length; j++) {

                row5TH = document.createElement('th')
                row5TH.setAttribute("scope", "col");
                row5TH.classList.add('text-center')
                row5TH.innerHTML = '<img src="../Utils/Objective' + dataBack[j].nombre + '.png" width="149" height="49">'

                row6TD = document.createElement('td')
                row6TD.setAttribute("scope", "col");
                row6TD.classList.add('text-center')
                row6TD.innerHTML = dataBack[j].valor + '%'

                row5.appendChild(row5TH)
                row6.appendChild(row6TD)
            }*/

        }
        )
}

function getRanges() {
    fetch(mainURL + 'rangos').then(res => {
        return res.json()
    })
        .then(dataBack => {
            //Si databack.length == 0 poner mensaje de empty list
            //console.log(dataBack)

            row7 = document.getElementById('row7')
            row8 = document.getElementById('row8')

            row7TH = document.createElement('th')
            row7TH.setAttribute("scope", "col");
            row7TH.classList.add('text-center')
            row7TH.innerHTML = 'Influence areas'

            row7.appendChild(row7TH)

            row7TH = document.createElement('th')
            row7TH.setAttribute("scope", "col");
            row7TH.classList.add('text-center')
            row7TH.innerHTML = 'Airlines'

            row7.appendChild(row7TH)

            row7TH = document.createElement('th')
            row7TH.setAttribute("scope", "col");
            row7TH.classList.add('text-center')
            row7TH.innerHTML = 'Airports'

            row7.appendChild(row7TH)

            row8TD = document.createElement('td')
            row8TD.setAttribute("scope", "col");
            row8TD.classList.add('text-center')
            row8TD.innerHTML = '(' + dataBack.listaIngresoPerdidoPorAreaInf[0] + ', ' + dataBack.listaIngresoPerdidoPorAreaInf[1] + ')%'

            row8.appendChild(row8TD)

            row8TD = document.createElement('td')
            row8TD.setAttribute("scope", "col");
            row8TD.classList.add('text-center')
            row8TD.innerHTML = '(' + dataBack.listaPasajerosPerdidosPorCompanyia[0] + ', ' + dataBack.listaPasajerosPerdidosPorCompanyia[1] + ')%'

            row8.appendChild(row8TD)

            row8TD = document.createElement('td')
            row8TD.setAttribute("scope", "col");
            row8TD.classList.add('text-center')
            row8TD.innerHTML = '(' + dataBack.listaIngresoPerdidoPorAerDest[0] + ', ' + dataBack.listaIngresoPerdidoPorAerDest[1] + ')%'

            row8.appendChild(row8TD)
        }
        )
}

function getFit() {
    fetch(mainURL + 'hist').then(res => {
        return res.json()
    })
        .then(dataBack => {
            //Si databack.length == 0 poner mensaje de empty list
            //console.log(dataBack)

            row9 = document.getElementById('row9')
            row10 = document.getElementById('row10')

            th9 = document.createElement('th')
            th9.setAttribute("scope", "col");
            th9.classList.add('text-center')
            th9.innerHTML = 'Number of iteration'

            th10 = document.createElement('th')
            th10.setAttribute("scope", "col");
            th10.classList.add('text-center')
            th10.innerHTML = 'Fitness value'

            row9.appendChild(th9)
            row10.appendChild(th10)

            for (k = 0; k < dataBack.length; k++) {

                row9TH = document.createElement('th')
                row9TH.setAttribute("scope", "col");
                row9TH.classList.add('text-center')
                row9TH.innerHTML = dataBack[k].iteracion

                row10TD = document.createElement('td')
                row10TD.setAttribute("scope", "col");
                row10TD.classList.add('text-center')
                row10TD.innerHTML = dataBack[k].fitness

                row9.appendChild(row9TH)
                row10.appendChild(row10TD)
            }

        }
        )
}

function datesDifference(fecha) {
    let date1 = new Date(fecha);
    currentDay = Math.round((date1.getTime() - minDate.getTime()) / (1000 * 3600 * 24))
    if (currentDay < 0 || currentDay > numDias) {
        currentDay = 0
        document.getElementById('Sdate').value = minDateString
    }
}

function applyFilter() {
    currentAir = document.getElementById('airInput').value
    datesDifference(document.getElementById('Sdate').value)
    removeChildren(document.getElementById('row1'))
    removeChildren(document.getElementById('row2'))
    getConnections()
}

function resetFilter() {
    currentAir = ''
    currentDay = 0
    document.getElementById('Sdate').value = minDateString
    document.getElementById('airInput').value = ''
    removeChildren(document.getElementById('row1'))
    removeChildren(document.getElementById('row2'))
    getConnections()
}

function removeChildren(element) {
    let child = element.lastElementChild;
    while (child) {
        element.removeChild(child);
        child = element.lastElementChild;
    }
}

function exportTableToExcel(tableID, filename = '') {
    var downloadLink;
    var dataType = 'application/vnd.ms-excel';
    var tableSelect = document.getElementById(tableID);
    var tableHTML = tableSelect.outerHTML.replace(/ /g, '%20');

    // Specify file name
    filename = filename ? filename + '.xls' : 'excel_data.xls';

    // Create download link element
    downloadLink = document.createElement("a");

    document.body.appendChild(downloadLink);

    if (navigator.msSaveOrOpenBlob) {
        var blob = new Blob(['\ufeff', tableHTML], {
            type: dataType
        });
        navigator.msSaveOrOpenBlob(blob, filename);
    } else {
        // Create a link to the file
        downloadLink.href = 'data:' + dataType + ', ' + tableHTML;

        // Setting the file name
        downloadLink.download = filename;

        //triggering the function
        downloadLink.click();
    }
}

function logOut() {
    sessionStorage.clear()
    window.location.href = "../index.html"
}

function encontrarElemento(lista, conexiones){
    pos = 0
    encontrado = false
    elemento = null
    while(pos < conexiones.length && !encontrado){
        if(conexiones[pos].iataOrigen == lista[0] && conexiones[pos].iataDestino == lista[1]){
            encontrado = true
            elemento = conexiones[pos]
        }
        pos++
    }
    return elemento
}

function goToDescription() {
    sessionStorage.setItem("load", 'load')
    window.location.href = "description.html"
}