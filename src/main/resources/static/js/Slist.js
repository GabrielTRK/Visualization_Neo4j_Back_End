/*if (!sessionStorage.getItem("logged")) {
    window.location.href = "login.html"
}*/
datosGlobal = null
if (sessionStorage.getItem("projectName")) {
    $(document).ready(function () {
        $('[data-toggle="tooltip"]').tooltip();
    });
    projectName = sessionStorage.getItem("projectName")

    document.getElementById('ListTitle').innerHTML = 'Solutions list from project ' + projectName

    if (sessionStorage.getItem("ejecutando") == 1) {
        console.log(sessionStorage.getItem("ejecutando"))

        document.getElementById('StopSaved').style.display = 'block'
        document.getElementById('RunSaved').style.display = 'none'

        document.getElementById('DeleteSaved').style.display = 'none'
    }

    url = 'https://138.4.92.155:8081/' + projectName + '/loadS'

    fetch(url).then(res => {
        return res.json()
    })
        .then(dataBack => {
            main = document.getElementById("MainList")
            //Si databack.length == 0 poner mensaje de empty list
            if (dataBack.length == 0) {
                noSolutions = document.createElement("p")
                noSolutions.innerHTML = 'No solutions'
                noSolutions.classList.add('text-center')
                main.appendChild(noSolutions)
            }

            datosGlobal = dataBack
            for (i = 0; i < dataBack.length; i++) {

                solutionI = document.createElement("button")
                solutionI.setAttribute("type", "button");
                solutionI.classList.add("Solution-" + dataBack[i].id)
                solutionI.classList.add("list-group-item")
                solutionI.classList.add("list-group-item-action")
                solutionI.classList.add("flex-column")
                solutionI.classList.add("align-items-start")
                solutionI.setAttribute('onclick', 'optionSelected(event)')

                divIDInputs = document.createElement("div")
                divIDInputs.classList.add("Solution-" + dataBack[i].id)
                divIDInputs.classList.add("d-flex")
                divIDInputs.classList.add("w-100")

                addIDInputs(divIDInputs, dataBack[i])

                ObjP = document.createElement('p')
                ObjP.innerHTML = 'Objective values:'
                ObjP.classList.add("Solution-" + dataBack[i].id)

                divIMGS = document.createElement('div')
                divIMGS.classList.add("Solution-" + dataBack[i].id)
                divIMGS.classList.add('parent')
                divIMGS.classList.add('mb-3')

                addIMGS(divIMGS, dataBack[i])

                divObjs = document.createElement('div')
                divObjs.classList.add("Solution-" + dataBack[i].id)
                divObjs.classList.add('parent')
                divObjs.classList.add('mb-3')

                addObjs(divObjs, dataBack[i])

                divInputIR = document.createElement('div')
                divInputIR.classList.add("Solution-" + dataBack[i].id)
                divInputIR.classList.add('d-flex')

                addRisk(divInputIR, dataBack[i])

                solutionI.appendChild(divIDInputs)
                solutionI.appendChild(ObjP)
                solutionI.appendChild(divIMGS)
                solutionI.appendChild(divObjs)
                solutionI.appendChild(divInputIR)


                main.appendChild(solutionI)
            }
        }
        )
} else {
    window.location.href = "list.html"
}



function addIDInputs(divIDInputs, dataBack) {
    SolutionID = document.createElement('h4')
    idText = dataBack.id + 1
    SolutionID.innerHTML = 'Solution ' + idText
    SolutionID.classList.add("Solution-" + dataBack.id)

    divTagInput1 = document.createElement("div")
    divTagInput1.classList.add("input-group-prepend")
    divTagInput1.style.marginLeft = '5%'

    divTagInput1Span = document.createElement("span")
    divTagInput1Span.classList.add("Solution-" + dataBack.id)
    divTagInput1Span.classList.add("input-group-text")
    divTagInput1Span.innerHTML = "Iterations"

    divInput1 = document.createElement("input")
    divInput1.setAttribute("type", "text");
    divInput1.classList.add("form-control")
    divInput1.setAttribute('value', dataBack.iter)
    divInput1.setAttribute('disabled', 'true')
    divInput1.style.width = '10%'

    divTagInput2 = document.createElement("div")
    divTagInput2.classList.add("input-group-prepend")
    divTagInput2.style.marginLeft = '5%'

    divTagInput2Span = document.createElement("span")
    divTagInput2Span.classList.add("Solution-" + dataBack.id)
    divTagInput2Span.classList.add("input-group-text")
    divTagInput2Span.innerHTML = "Fitness"

    divInput2 = document.createElement("input")
    divInput2.setAttribute("type", "text");
    divInput2.classList.add("form-control")
    divInput2.setAttribute('value', dataBack.fitness.toFixed(3))
    divInput2.setAttribute('disabled', 'true')
    divInput2.style.width = '10%'

    divSVGIcon = document.createElement('div')
    divSVGIcon.style.marginLeft = '5%'
    if(dataBack.temporal){
        divSVGIcon.innerHTML = '<svg data-toggle="tooltip" title="This is a solution obtained after an optimization was stopped." data-bs-placement="right" xmlns="http://www.w3.org/2000/svg" width="35" height="35" fill="currentColor" class="'+ "Solution-" + dataBack.id +' bi bi-exclamation circle-fill" viewBox="0 0 16 16"> <path class="'+ "Solution-" + dataBack.id +'" d="M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0M8 4a.905.905 0 0 0-.9.995l.35 3.507a.552.552 0 0 0 1.1 0l.35-3.507A.905.905 0 0 0 8 4m.002 6a1 1 0 1 0 0 2 1 1 0 0 0 0-2"/></svg>'
        $(document).ready(function(){
            $('[data-toggle="tooltip"]').tooltip();   
          });
    }

    divIDInputs.appendChild(SolutionID)
    divTagInput1.appendChild(divTagInput1Span)
    divIDInputs.appendChild(divTagInput1)
    divIDInputs.appendChild(divInput1)

    divTagInput2.appendChild(divTagInput2Span)
    divIDInputs.appendChild(divTagInput2)
    divIDInputs.appendChild(divInput2)
    divIDInputs.appendChild(divSVGIcon)
}

function addIMGS(divIMGS, databack) {
    for (j = 1; j < databack.obj.length; j++) {
        divChild = document.createElement('div')
        divChild.classList.add('child')
        divChild.style.marginRight = '1%'
        divChild.innerHTML = '<img class="Solution-' + databack.id + '" src="../Utils/Objective' + databack.obj[j].nombre + '.png" width="100%" height="100%">'

        divIMGS.appendChild(divChild)
    }
}

function addObjs(divObjs, databack) {
    for (j = 1; j < databack.obj.length; j++) {
        divChild = document.createElement('div')
        divChild.classList.add('child')
        divChild.style.marginRight = '1%'

        inputObj = document.createElement('input');
        inputObj.setAttribute("type", "text");
        inputObj.classList.add("form-control")
        inputObj.setAttribute('value', databack.obj[j].valor.toFixed(3))
        inputObj.setAttribute('disabled', 'true')
        divChild.appendChild(inputObj)

        divObjs.appendChild(divChild)
    }
}

function addRisk(divInputIR, databack) {
    divLabelIR = document.createElement('div')
    divLabelIR.classList.add('input-group-prepend')

    labelIR = document.createElement('span')
    labelIR.classList.add("Solution-" + databack.id)
    labelIR.classList.add('input-group-text')
    labelIR.innerHTML = 'Imported Risk'

    inputIR = document.createElement('input');
    inputIR.setAttribute("type", "text");
    inputIR.classList.add("form-control")
    inputIR.setAttribute('value', databack.obj[0].valor.toFixed(3))
    inputIR.setAttribute('disabled', 'true')
    inputIR.style.width = "10%"

    divLabelIR.appendChild(labelIR)

    divInputIR.appendChild(divLabelIR)
    divInputIR.appendChild(inputIR)
}

function optionSelected(event) {
    // Get the modal
    var modal = document.getElementById("myModalSaved");
    //var modalR = document.getElementById("myModalRun");

    // Get the <span> element that closes the modal
    var span = document.getElementsByClassName("close")[0];

    span.onclick = function () {
        modal.style.display = "none";
        //modalR.style.display = "none";
    }

    window.onclick = function (event) {
        if (event.target == modal) {
            modal.style.display = "none";
            //modalR.style.display = "none";
        }
    }

    modal.style.display = "block";
    document.getElementById('CheckDetails').style.display = 'block'
    document.getElementById('DeleteSol').style.display = 'block'

    sessionStorage.setItem("solutionID", event.target.classList[0].split("-")[1]);

    //Comprobar con un bucle si la solucion elegida es temporal o no
    for (i = 0; i < datosGlobal.length; i++) {
        if(datosGlobal[i].id == event.target.classList[0].split("-")[1] && datosGlobal[i].temporal){
            document.getElementById('ContinueOpt').style.display = 'block'
        }
    }

}

function goToDescription() {
    sessionStorage.setItem("load", 'load')
    window.location.href = "description.html"
}

function algoritmoGuardado() {
    // Get the modal
    var modal = document.getElementById("myModalSaved");
    //var modalR = document.getElementById("myModalRun");

    // Get the <span> element that closes the modal
    var span = document.getElementsByClassName("close")[0];

    span.onclick = function () {
        modal.style.display = "none";
        //modalR.style.display = "none";
    }

    window.onclick = function (event) {
        if (event.target == modal) {
            modal.style.display = "none";
            //modalR.style.display = "none";
        }
    }


    modal.style.display = "block";
    document.getElementById('ModalText').innerHTML = "Running optimization..."
    document.getElementById('CheckDetails').style.display = 'none'
    document.getElementById('DeleteSol').style.display = 'none'

    url = 'https://138.4.92.155:8081/' + projectName + '/optimize'

    const options = {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' }
    };

    fetch(url, options)
        .then(response => response.json())
        .then(response => {
            if (response.ok_KO) {
                //modalR.style.display = "none";

                //Guardar id de solucion y proyecto en local storage

                //Redirigir a mapa



            }
            else {
                //Mostrar modal con error
                console.log(response)
                //modalR.style.display = "none";
                modal.style.display = "block";
                document.getElementById('ModalText').innerHTML = response.mensaje
                document.getElementById('CheckDetails').style.display = 'none'
                document.getElementById('DeleteSol').style.display = 'none'
                document.getElementById('ContinueOpt').style.display = 'none'
            }
        });


}

function stopOpt() {
    // Get the modal
    var modal = document.getElementById("myModalSaved");
    //var modalR = document.getElementById("myModalRun");

    // Get the <span> element that closes the modal
    var span = document.getElementsByClassName("close")[0];

    span.onclick = function () {
        modal.style.display = "none";
        //modalR.style.display = "none";
    }

    window.onclick = function (event) {
        if (event.target == modal) {
            modal.style.display = "none";
            //modalR.style.display = "none";
        }
    }

    url = 'https://138.4.92.155:8081/pauseOpt'

    fetch(url).then(res => {
        return res.json()
    })
        .then(response => {
            sessionStorage.setItem("ejecutando", 0);
            modal.style.display = "block";
            document.getElementById('ModalText').innerHTML = response.mensaje
            document.getElementById('CheckDetails').style.display = 'none'
            document.getElementById('ContinueOpt').style.display = 'none'
            document.getElementById('DeleteSol').style.display = 'none'
        }
        )

}

function temporalSol() {
    modal.style.display = "block";
    document.getElementById('CheckDetails').style.display = 'block'
    document.getElementById('ContinueOpt').style.display = 'block'
}

function deleteProject() {
    // Get the modal
    var modal = document.getElementById("myModalSaved");
    //var modalR = document.getElementById("myModalRun");

    // Get the <span> element that closes the modal
    var span = document.getElementsByClassName("close")[0];

    span.onclick = function () {
        modal.style.display = "none";
        //modalR.style.display = "none";
    }

    window.onclick = function (event) {
        if (event.target == modal) {
            modal.style.display = "none";
            //modalR.style.display = "none";
        }
    }



    url = 'https://138.4.92.155:8081/' + projectName + '/delete'

    const options = {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' }
    };

    fetch(url, options)
        .then(response => response.json())
        .then(response => {

            modal.style.display = "block";
            document.getElementById('ModalText').innerHTML = response.mensaje
            document.getElementById('CheckDetails').style.display = 'none'
            document.getElementById('ContinueOpt').style.display = 'none'
            document.getElementById('DeleteSol').style.display = 'none'
        });
}

function details(){
    window.location.href = "map.html"
}

function continueOpt(){
    // Get the modal
    var modal = document.getElementById("myModalSaved");
    //var modalR = document.getElementById("myModalRun");

    // Get the <span> element that closes the modal
    var span = document.getElementsByClassName("close")[0];

    span.onclick = function () {
        modal.style.display = "none";
        //modalR.style.display = "none";
    }

    window.onclick = function (event) {
        if (event.target == modal) {
            modal.style.display = "none";
            //modalR.style.display = "none";
        }
    }


    modal.style.display = "block";
    document.getElementById('ModalText').innerHTML = "Running optimization..."
    document.getElementById('CheckDetails').style.display = 'none'
    document.getElementById('DeleteSol').style.display = 'none'
    document.getElementById('ContinueOpt').style.display = 'none'

    url = 'https://138.4.92.155:8081/' + projectName + '/' + sessionStorage.getItem('solutionID') + '/continue'

    const options = {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' }
    };

    fetch(url, options)
        .then(response => response.json())
        .then(response => {
            if (response.ok_KO) {
                //modalR.style.display = "none";

                //Guardar id de solucion y proyecto en local storage

                //Redirigir a mapa



            }
            else {
                //Mostrar modal con error
                console.log(response)
                //modalR.style.display = "none";
                modal.style.display = "block";
                document.getElementById('ModalText').innerHTML = response.mensaje
                document.getElementById('CheckDetails').style.display = 'none'
                document.getElementById('DeleteSol').style.display = 'none'
                document.getElementById('ContinueOpt').style.display = 'none'
            }
        });
}

function deleteSol(){
    // Get the modal
    var modal = document.getElementById("myModalSaved");
    //var modalR = document.getElementById("myModalRun");

    // Get the <span> element that closes the modal
    var span = document.getElementsByClassName("close")[0];

    span.onclick = function () {
        modal.style.display = "none";
        //modalR.style.display = "none";
    }

    window.onclick = function (event) {
        if (event.target == modal) {
            modal.style.display = "none";
            //modalR.style.display = "none";
        }
    }


    url = 'https://138.4.92.155:8081/' + projectName + '/' + sessionStorage.getItem('solutionID') + '/delete'

    const options = {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' }
    };

    fetch(url, options)
        .then(response => response.json())
        .then(response => {
            modal.style.display = "block";
                document.getElementById('ModalText').innerHTML = response.mensaje
                document.getElementById('CheckDetails').style.display = 'none'
                document.getElementById('DeleteSol').style.display = 'none'
                document.getElementById('ContinueOpt').style.display = 'none'
        });
}

function logOut() {
    sessionStorage.clear()
    window.location.href = "../index.html"
}