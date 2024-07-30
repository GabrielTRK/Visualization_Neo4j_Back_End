if (!sessionStorage.getItem("logged")) {
    window.location.href = "login.html"
}


$(document).ready(function () {
    $('[data-toggle="tooltip"]').tooltip();
});

projectName = ''

if (sessionStorage.getItem("load")) {
    projectName = sessionStorage.getItem("projectName")

    //Load project, desactivar inputs, y cambiar botones
    url = 'http://localhost:8080/loadP'

    fetch(url).then(res => {
        return res.json()
    })
        .then(dataBack => {
            //Si databack.length == 0 poner mensaje de empty list
            for (i = 0; i < dataBack.length; i++) {
                if (dataBack[i].nombre == projectName) {
                    fillForm(dataBack[i])
                }
            }
        }
        )

    deactivateFormChangeButtons()

}



// Get the modal
var modal = document.getElementById("myModalSaved");
var modalR = document.getElementById("myModalRun");

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

date_PDMAX = new Date(document.getElementById("end").max)
date_PDMIN = new Date(document.getElementById("end").min)
startDateValid = false
endDateValid = false

function algoritmo() {
    err = false

    projectName = document.getElementById("projectName").value;

    if (projectName == '') {
        err = true
    }

    fecha_I = String(document.getElementById("start").value) + '&'
    fecha_F = String(document.getElementById("end").value) + '&'

    fecha_ID = new Date(document.getElementById("start").value)
    fecha_FD = new Date(document.getElementById("end").value)

    fecha_PDMAX = new Date(document.getElementById("start").max)
    fecha_PDMIN = new Date(document.getElementById("start").min)

    if (document.getElementById("start").value == '' || document.getElementById("end").value == '') {
        err = true
    } else if (fecha_ID < fecha_PDMIN || fecha_ID > fecha_PDMAX || fecha_FD < fecha_PDMIN || fecha_FD > fecha_PDMAX || fecha_ID > fecha_FD) {
        err = true
    }

    contIMG = 0

    const parents = document.getElementsByClassName("DDContainer");
    for (i = 0; i < parents[2].children.length; i++) {
        if (parents[2].children[i].children.length != 0) {
            contIMG++
        }
    }

    if (contIMG != parents[2].children.length) {
        err = true
    }

    epiRes = String(document.getElementById("restriction").value) + '&'

    switchs = document.getElementsByClassName("form-check-input")

    polRes = []

    contSW = 0
    for (i = 0; i < switchs.length; i++) {
        if (switchs[i].checked) {
            polRes[contSW] = switchs[i].id
            contSW++
        }
    }

    idOrder = []

    if (contIMG == parents[2].children.length) {
        for (i = 0; i < parents[2].children.length; i++) {
            idOrder[i] = parseInt(parents[2].children[i].children[0].id)
        }
    }

    if (document.getElementById("numP").value != '') {
        numP = String(document.getElementById("numP").value) + '&'
    } else {
        numP = String(document.getElementById("numP").placeholder) + '&'
    }

    if (document.getElementById("iW").value != '') {
        iW = String(document.getElementById("iW").value) + '&'
    } else {
        iW = String(document.getElementById("iW").placeholder) + '&'
    }

    if (document.getElementById("c1").value != '') {
        c1 = String(document.getElementById("c1").value) + '&'
    } else {
        c1 = String(document.getElementById("c1").placeholder) + '&'
    }

    if (document.getElementById("c2").value != '') {
        c2 = String(document.getElementById("c2").value) + '&'
    } else {
        c2 = String(document.getElementById("c2").placeholder) + '&'
    }

    if (err) {
        //Mostrar modal con error
        modal.style.display = "block";
        document.getElementById('ModalText').innerHTML = "Invalid data"
    } else {
        modalR.style.display = "block";
        document.getElementById('ModalRText').innerHTML = "Saving project and Running optimization..."

        p_fecha_I = 'fecha_inicial='
        p_fecha_F = 'fecha_final='
        p_iteraciones = 'iteraciones='
        p_num_P = 'numIndividuos='
        p_iW = 'inertiaW='
        p_c1 = 'c1='
        p_c2 = 'c2='
        p_m = 'm='
        p_p = 'p='
        p_c1 = 'c1='
        p_res_epi = 'res_epi='

        p_nombre = 'nombre='

        url = 'http://localhost:8080/optimize' + '?' + p_fecha_I + fecha_I + p_fecha_F + fecha_F + p_iteraciones + String(0) + '&' + p_num_P + numP +
            p_iW + iW + p_c1 + c1 + p_c2 + c2 + p_m + String(0.0) + '&' + p_p + String(0.0) + '&' + p_res_epi + epiRes + p_nombre + projectName

        const params = {
            "pol": polRes,
            "order": idOrder
        };
        const options = {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(params)
        };
        fetch(url, options)
            .then(response => response.json())
            .then(response => {
                if (response) {
                    modalR.style.display = "none";

                    //Guardar id de solucion y proyecto en local storage

                    //Redirigir a mapa



                }
                else {
                    //Mostrar modal con error
                    modalR.style.display = "none";
                    modal.style.display = "block";
                    document.getElementById('ModalText').innerHTML = "The name of the project already exists"
                }
            });
    }
}

function algoritmoGuardado() {
    modalR.style.display = "block";
    document.getElementById('ModalRText').innerHTML = "Saving project and Running optimization..."

    url = 'http://localhost:8080/' + projectName + '/optimize'

    const options = {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' }
    };

    fetch(url, options)
        .then(response => response.json())
        .then(response => {
            if (response) {
                modalR.style.display = "none";

                //Guardar id de solucion y proyecto en local storage

                //Redirigir a mapa



            }
            else {
                //Mostrar modal con error
                modalR.style.display = "none";
            }
        });


}

function saveConf() {
    //Comprobar datos y enviar error en caso de no validez
    err = false

    projectName = document.getElementById("projectName").value;

    if (projectName == '') {
        err = true
    }

    fecha_I = String(document.getElementById("start").value) + '&'
    fecha_F = String(document.getElementById("end").value) + '&'

    fecha_ID = new Date(document.getElementById("start").value)
    fecha_FD = new Date(document.getElementById("end").value)

    fecha_PDMAX = new Date(document.getElementById("start").max)
    fecha_PDMIN = new Date(document.getElementById("start").min)

    if (document.getElementById("start").value == '' || document.getElementById("end").value == '') {
        err = true
    } else if (fecha_ID < fecha_PDMIN || fecha_ID > fecha_PDMAX || fecha_FD < fecha_PDMIN || fecha_FD > fecha_PDMAX || fecha_ID > fecha_FD) {
        err = true
    }

    contPref = 0

    const parents = document.getElementsByClassName("DDContainer");
    for (i = 0; i < parents[2].children.length; i++) {
        if (parents[2].children[i].children.length != 0) {
            contPref++
        }
    }
    if (contPref != parents[2].children.length) {
        err = true
    }

    epiRes = String(document.getElementById("restriction").value) + '&'

    switchs = document.getElementsByClassName("form-check-input")

    polRes = []

    contSwitch = 0
    for (i = 0; i < switchs.length; i++) {
        if (switchs[i].checked) {
            polRes[contSwitch] = switchs[i].id
            contSwitch++
        }
    }

    idOrder = []

    if (contPref == parents[2].children.length) {
        for (i = 0; i < parents[2].children.length; i++) {
            idOrder[i] = parseInt(parents[2].children[i].children[0].id)
        }
    }

    if (document.getElementById("numP").value != '') {
        numP = String(document.getElementById("numP").value) + '&'
    } else {
        numP = String(document.getElementById("numP").placeholder) + '&'
    }

    if (document.getElementById("iW").value != '') {
        iW = String(document.getElementById("iW").value) + '&'
    } else {
        iW = String(document.getElementById("iW").placeholder) + '&'
    }

    if (document.getElementById("c1").value != '') {
        c1 = String(document.getElementById("c1").value) + '&'
    } else {
        c1 = String(document.getElementById("c1").placeholder) + '&'
    }

    if (document.getElementById("c2").value != '') {
        c2 = String(document.getElementById("c2").value) + '&'
    } else {
        c2 = String(document.getElementById("c2").placeholder) + '&'
    }

    if (err) {
        //Mostrar modal con error
        modal.style.display = "block";
        document.getElementById('ModalText').innerHTML = "Invalid data"
    } else {
        p_fecha_I = 'fecha_inicial='
        p_fecha_F = 'fecha_final='
        p_iteraciones = 'iteraciones='
        p_num_P = 'numIndividuos='
        p_iW = 'inertiaW='
        p_c1 = 'c1='
        p_c2 = 'c2='
        p_m = 'm='
        p_p = 'p='
        p_c1 = 'c1='
        p_res_epi = 'res_epi='

        p_nombre = 'nombre='

        url = 'http://localhost:8080/saveP' + '?' + p_fecha_I + fecha_I + p_fecha_F + fecha_F + p_iteraciones + String(0) + '&' + p_num_P + numP +
            p_iW + iW + p_c1 + c1 + p_c2 + c2 + p_m + String(0.0) + '&' + p_p + String(0.0) + '&' + p_res_epi + epiRes + p_nombre + projectName

        const params = {
            "pol": polRes,
            "order": idOrder
        };
        const options = {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(params)
        };
        fetch(url, options)
            .then(response => response.json())
            .then(response => {
                if (response) {
                    modal.style.display = "block";
                    document.getElementById('ModalText').innerHTML = "Project saved"

                    deactivateFormChangeButtons()
                }
                else {
                    //Mostrar modal con error
                    modal.style.display = "block";
                    document.getElementById('ModalText').innerHTML = "The name of the project already exists"
                }
            });
    }

}

function refresh() {
    sessionStorage.removeItem("load")
    location.reload()
}

function allowDrop(ev) {
    ev.preventDefault();
}

function drag(ev) {
    if (ev.target.tagName == 'IMG') {
        ev.dataTransfer.setData("text", ev.target.id);
    }
}

function drop(ev) {
    if (!sessionStorage.getItem("load")) {
        cont = 0
        ev.preventDefault();
        if (ev.target.tagName == 'DIV') {
            var data = ev.dataTransfer.getData("text");
            if (data > 0) {
                ev.target.style.border = "1px solid rgb(90, 89, 89)"
                ev.target.appendChild(document.getElementById(data));
            }
        }
        //Comprobar con un for los que no tienen asociado un criterio y ponerlos en rojo
        const parents = document.getElementsByClassName("DDContainer");
        for (i = 0; i < parents[2].children.length; i++) {
            if (parents[2].children[i].children.length == 0) {
                parents[2].children[i].style.border = "1px solid #de4251"
            } else {
                cont++
            }
        }
        if (cont == parents[2].children.length) {
            document.getElementById('PrefValid').style.color = "#198754"
            document.getElementById('PrefValid').innerHTML = "Valid"
        } else {
            document.getElementById('PrefValid').style.color = "#de4251"
            document.getElementById('PrefValid').innerHTML = "All the criteria must be on the right column"
        }
    }

}

function NumPshowHelp() {
    document.getElementById("NumPTooltipText").style.visibility = "visible";

}

function NumPhideHelp() {
    document.getElementById("NumPTooltipText").style.visibility = "hidden";
}

function IneWshowHelp() {
    document.getElementById("IneWTooltipText").style.visibility = "visible";
}

function IneWhideHelp() {
    document.getElementById("IneWTooltipText").style.visibility = "hidden";
}

function c1showHelp() {
    document.getElementById("c1TooltipText").style.visibility = "visible";
}

function c1hideHelp() {
    document.getElementById("c1TooltipText").style.visibility = "hidden";
}

function c2showHelp() {
    document.getElementById("c2TooltipText").style.visibility = "visible";
}

function c2hideHelp() {
    document.getElementById("c2TooltipText").style.visibility = "hidden";
}

function startDateOnChange() {
    date = document.getElementById('start').value

    fecha_PDMAX = new Date(document.getElementById("start").max)
    fecha_PDMIN = new Date(document.getElementById("start").min)

    fecha_ID = new Date(date)

    if (date != '' && fecha_ID >= fecha_PDMIN && fecha_ID <= fecha_PDMAX) {
        document.getElementById('end').min = date
        startDateValid = true
    } else {
        document.getElementById('end').min = date_PDMIN
        startDateValid = false
    }
    checkDatesSendRequest()
}

function endDateOnChange() {
    date = document.getElementById('end').value

    fecha_PDMAX = new Date(document.getElementById("end").max)
    fecha_PDMIN = new Date(document.getElementById("end").min)

    fecha_FD = new Date(date)

    if (date != '' && fecha_FD >= fecha_PDMIN && fecha_FD <= fecha_PDMAX) {
        document.getElementById('start').max = date
        endDateValid = true
    } else {
        document.getElementById('start').max = date_PDMAX
        endDateValid = false
    }
    checkDatesSendRequest()
}

function checkDatesSendRequest() {
    if (startDateValid && endDateValid) {
        fecha_I = String(document.getElementById("start").value) + '&'
        fecha_F = String(document.getElementById("end").value)
        p_fecha_I = 'fecha_inicial='
        p_fecha_F = 'fecha_final='


        url = 'http://localhost:8080/tooltips' + '?' + p_fecha_I + fecha_I + p_fecha_F + fecha_F

        fetch(url).then(res => {
            return res.json()
        })
            .then(dataBack => {
                //Si databack.length == 0 poner mensaje de empty list
                document.getElementById('1').setAttribute('data-bs-original-title', dataBack.z1)
                document.getElementById('1').setAttribute('aria-label', dataBack.z1)

                document.getElementById('2').setAttribute('data-bs-original-title', dataBack.z2)
                document.getElementById('2').setAttribute('aria-label', dataBack.z2)

                document.getElementById('3').setAttribute('data-bs-original-title', dataBack.z3)
                document.getElementById('3').setAttribute('aria-label', dataBack.z3)

                document.getElementById('4').setAttribute('data-bs-original-title', dataBack.z4)
                document.getElementById('4').setAttribute('aria-label', dataBack.z4)

                document.getElementById('5').setAttribute('data-bs-original-title', dataBack.z5)
                document.getElementById('5').setAttribute('aria-label', dataBack.z5)


                document.getElementById('6').setAttribute('data-bs-original-title', dataBack.z6)
                document.getElementById('6').setAttribute('aria-label', dataBack.z6)

                document.getElementById('7').setAttribute('data-bs-original-title', dataBack.z7)
                document.getElementById('7').setAttribute('aria-label', dataBack.z7)
            }
            )
    }
}

function numPOnChange() {
    //console.log(document.getElementById('numP').value)
    /*if(document.getElementById('numP').value == ''){
        document.getElementById('numP').value = ''
    }else if(document.getElementById('numP').value > document.getElementById('numP').max){
        document.getElementById('numP').value = document.getElementById('numP').max
        console.log(document.getElementById('numP').value)
    }*/
    if (document.getElementById('numP').value > document.getElementById('numP').max) {
        document.getElementById('numP').value = document.getElementById('numP').max
    }


}

function deactivateFormChangeButtons() {

    document.getElementById('PrefValid').innerHTML = ''

    const parents = document.getElementsByClassName("DDContainer");

    switchs = document.getElementsByClassName("form-check-input")

    for (i = 0; i < switchs.length; i++) {
        switchs[i].setAttribute("disabled", true)
    }

    for (i = 0; i < parents.length; i++) {
        for (j = 0; j < parents[i].children.length; j++) {
            parents[i].children[j].style.border = "1px solid rgb(90, 89, 89)"
            if (parents[i].children[j].children.length > 0) {
                parents[i].children[j].children[0].setAttribute("draggable", false)
            }
        }
    }

    inputs = document.getElementsByTagName('input')
    for (i = 0; i < inputs.length; i++) {
        inputs[i].setAttribute("disabled", true)
    }

    //document.getElementById("EndDateDiv").style.marginTop = '-4.4%'

    document.getElementById("SaveButton").style.display = 'none'
    document.getElementById("NewPButton").style.display = 'block'

    document.getElementById("RunNew").style.display = 'none'
    document.getElementById("RunSaved").style.display = 'block'
}

function fillForm(dataBack) {

    document.getElementById('projectName').value = dataBack.nombre

    document.getElementById('start').value = dataBack.fechas.fechaI
    document.getElementById('end').value = dataBack.fechas.fechaF

    document.getElementById('restriction').value = dataBack.res.epi
    document.getElementById('restrictionOut').innerHTML = dataBack.res.epi + '%'

    for (i = 0; i < dataBack.res.pol.length; i++) {
        document.getElementById(dataBack.res.pol[i]).checked = true
    }

    for (i = 0; i < dataBack.order.order.length; i++) {
        document.getElementById(dataBack.order.order[i]).remove()
        document.getElementsByClassName("DDContainer")[2].children[i].innerHTML = '<img src="../Utils/Objective' + dataBack.order.order[i] + '.png" draggable="false" width="149" height="49">'
    }

    document.getElementById("numP").value = dataBack.params.numIndividuos
    document.getElementById("iW").value = dataBack.params.inertiaW
    document.getElementById("c1").value = dataBack.params.c1
    document.getElementById("c2").value = dataBack.params.c2

}

function emptyForm() {

    document.getElementById('projectName').value = ''

    document.getElementById('start').value = ''
    document.getElementById('end').value = ''

    document.getElementById('restriction').value = 50.0
    document.getElementById('restrictionOut').innerHTML = 50.0 + '%'

    switchs = document.getElementsByClassName("form-check-input")

    for (i = 0; i < switchs.length; i++) {
        switchs[i].checked = false
    }

    document.getElementById("numP").value = ''
    document.getElementById("iW").value = ''
    document.getElementById("c1").value = ''
    document.getElementById("c2").value = ''
}

function goHome() {
    emptyForm()
    window.location.href = "/html/home.html"
}

function goLoad() {
    emptyForm()
    window.location.href = "/html/list.html"
}

function logOut() {
    sessionStorage.clear()
    window.location.href = "../index.html"
}