if(!sessionStorage.getItem("logged")){
    window.location.href = "login.html"
}

$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();   
  });

sessionStorage.removeItem("load")

switchsIDs = ['EU', 'NA', 'SA', 'AS', 'AF']
switchsNames = ['Europe', 'North America', 'South America', 'Asia', 'Africa']

url = 'http://localhost:8080/loadP'

fetch(url).then(res => {
    return res.json()
})
    .then(dataBack => {
        main = document.getElementById("MainList")
        //Si databack.length == 0 poner mensaje de empty list
        for (i = 0; i < dataBack.length; i++) {
            console.log(dataBack[i])
            projectI = document.createElement("button")
            projectI.setAttribute("type", "button");
            projectI.classList.add(dataBack[i].nombre)
            projectI.classList.add("list-group-item")
            projectI.classList.add("list-group-item-action")
            projectI.classList.add("flex-column")
            projectI.classList.add("align-items-start")
            projectI.setAttribute('onclick', 'optionSelected(event)')
            

            divInputs = document.createElement("div")
            divInputs.classList.add(dataBack[i].nombre)
            divInputs.classList.add("d-flex")
            divInputs.classList.add("w-100")

            addInputs(divInputs, dataBack[i])

            epiP = document.createElement("p")
            epiP.innerHTML = 'Epidemiological constraint:'
            epiP.style.paddingTop = '1rem'
            epiP.classList.add(dataBack[i].nombre)

            divEpi = document.createElement("div")
            divEpi.classList.add(dataBack[i].nombre)
            divEpi.classList.add('progress')
            divEpi.classList.add('mb-3')
            divEpi.style.height = '25px'

            addEpi(divEpi, dataBack[i])

            polP = document.createElement("p")
            polP.innerHTML = 'Political constraint:'
            polP.classList.add(dataBack[i].nombre)

            divSwitchs = document.createElement("div")
            divSwitchs.style.display = 'flex'
            divSwitchs.style.justifyContent = 'space-around'
            divSwitchs.classList.add(dataBack[i].nombre)

            addSwitchs(divSwitchs, dataBack[i])

            decP = document.createElement("p")
            decP.innerHTML = 'Decision maker\'s preferences:'
            decP.classList.add(dataBack[i].nombre)

            divDecMain = document.createElement('div')
            divDecMain.classList.add(dataBack[i].nombre)
            divDecMain.classList.add('parent')
            divDecMain.classList.add('mb-3')

            addPrefImgs(divDecMain, dataBack[i])

            projectI.appendChild(divInputs)
            projectI.appendChild(epiP)
            projectI.appendChild(divEpi)
            projectI.appendChild(polP)
            projectI.appendChild(divSwitchs)
            projectI.appendChild(decP)
            projectI.appendChild(divDecMain)

            main.appendChild(projectI)
        }
    }
    )

function addInputs(divInputs, dataBack) {
    divTagInput1 = document.createElement("div")
    divTagInput1.classList.add("input-group-prepend")

    divTagInput1Span = document.createElement("span")
    divTagInput1Span.classList.add(dataBack.nombre)
    divTagInput1Span.classList.add("input-group-text")
    divTagInput1Span.innerHTML = "Project name"

    divInput1 = document.createElement("input")
    divInput1.setAttribute("type", "text");
    divInput1.classList.add("form-control")
    divInput1.setAttribute('value', dataBack.nombre)
    divInput1.setAttribute('disabled', 'true')
    divInput1.style.width = '15%'

    divTagInput2 = document.createElement("div")
    divTagInput2.classList.add("input-group-prepend")
    divTagInput2.style.marginLeft = '10%'

    divTagInput2Span = document.createElement("span")
    divTagInput2Span.classList.add(dataBack.nombre)
    divTagInput2Span.classList.add("input-group-text")
    divTagInput2Span.innerHTML = "Start date"

    divInput2 = document.createElement("input")
    divInput2.setAttribute("type", "date");
    divInput2.classList.add("form-control")
    divInput2.setAttribute('value', dataBack.fechas.fechaI)
    divInput2.setAttribute('disabled', 'true')
    divInput2.style.width = '15%'

    divTagInput3 = document.createElement("div")
    divTagInput3.classList.add("input-group-prepend")
    divTagInput3.style.marginLeft = '10%'

    divTagInput3Span = document.createElement("span")
    divTagInput3Span.classList.add(dataBack.nombre)
    divTagInput3Span.classList.add("input-group-text")
    divTagInput3Span.innerHTML = "End date"

    divInput3 = document.createElement("input")
    divInput3.setAttribute("type", "date");
    divInput3.classList.add("form-control")
    divInput3.setAttribute('value', dataBack.fechas.fechaF)
    divInput3.setAttribute('disabled', 'true')
    divInput3.style.width = '15%'

    divTagInput1.appendChild(divTagInput1Span)
    divInputs.appendChild(divTagInput1)
    divInputs.appendChild(divInput1)

    divTagInput2.appendChild(divTagInput2Span)
    divInputs.appendChild(divTagInput2)
    divInputs.appendChild(divInput2)

    divTagInput3.appendChild(divTagInput3Span)
    divInputs.appendChild(divTagInput3)
    divInputs.appendChild(divInput3)
}

function addEpi(divEpi, dataBack) {
    divProgress = document.createElement('div')
    divProgress.classList.add(dataBack.nombre)
    divProgress.classList.add('progress-bar')
    divProgress.setAttribute('role', 'progressbar')
    divProgress.style.width = String(dataBack.res.epi) + '%'
    divProgress.innerHTML = String(dataBack.res.epi) + '%'
    divEpi.appendChild(divProgress)
}

function addSwitchs(divSwitchs, dataBack) {
    for (j = 0; j < switchsIDs.length; j++) {
        divSwitchI = document.createElement('div')
        divSwitchI.classList.add(dataBack.nombre)
        divSwitchI.classList.add('form-check')
        divSwitchI.classList.add('form-switch')

        inputSwitchI = document.createElement('input')
        inputSwitchI.classList.add(dataBack.nombre)
        inputSwitchI.classList.add('form-check-input')
        inputSwitchI.setAttribute('type', 'checkbox')
        inputSwitchI.setAttribute('disabled', 'true')
        inputSwitchI.setAttribute('id', switchsIDs[j] + dataBack.nombre)
        inputSwitchI.checked = dataBack.res.pol.includes(switchsIDs[j])

        labelSwitchI = document.createElement('label')
        labelSwitchI.classList.add(dataBack.nombre)
        labelSwitchI.classList.add('form-check-label')
        labelSwitchI.setAttribute('for', switchsIDs[j] + dataBack.nombre)
        labelSwitchI.innerHTML = switchsNames[j]

        divSwitchI.appendChild(inputSwitchI)
        divSwitchI.appendChild(labelSwitchI)
        divSwitchs.appendChild(divSwitchI)
    }

}

function addPrefImgs(divDecMain, dataBack) {

    divChildPlusIMG = document.createElement('div')
    divChildPlusIMG.classList.add(dataBack.nombre)
    divChildPlusIMG.classList.add('child')
    divPlusImg = document.createElement('div')
    divPlusImg.classList.add(dataBack.nombre)
    divPlusImg.style.display = 'flex'
    divPlusImg.style.justifyContent = 'center'
    divPlusImg.innerHTML = '<svg  width="40%" fill="currentColor"class="'+ dataBack.nombre +' bi bi-file-plus" viewBox="0 0 16 16"><path class="'+ dataBack.nombre +'" d="M8.5 6a.5.5 0 0 0-1 0v1.5H6a.5.5 0 0 0 0 1h1.5V10a.5.5 0 0 0 1 0V8.5H10a.5.5 0 0 0 0-1H8.5z"></path><path class="'+ dataBack.nombre +'" d="M2 2a2 2 0 0 1 2-2h8a2 2 0 0 1 2 2v12a2 2 0 0 1-2 2H4a2 2 0 0 1-2-2zm10-1H4a1 1 0 0 0-1 1v12a1 1 0 0 0 1 1h8a1 1 0 0 0 1-1V2a1 1 0 0 0-1-1" /></svg>'

    divChildMinusIMG = document.createElement('div')
    divChildMinusIMG.classList.add(dataBack.nombre)
    divChildMinusIMG.classList.add('child')
    divMinusImg = document.createElement('div')
    divMinusImg.classList.add(dataBack.nombre)
    divMinusImg.style.display = 'flex'
    divMinusImg.style.justifyContent = 'center'
    divMinusImg.innerHTML = '<svg xmlns="http://www.w3.org/2000/svg" width="40%" fill="currentColor"class="'+ dataBack.nombre +' bi bi-file-minus" viewBox="0 0 16 16"><path class="'+ dataBack.nombre +'" d="M5.5 8a.5.5 0 0 1 .5-.5h4a.5.5 0 0 1 0 1H6a.5.5 0 0 1-.5-.5" /><path class="'+ dataBack.nombre +'" d="M4 0a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h8a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2zm0 1h8a1 1 0 0 1 1 1v12a1 1 0 0 1-1 1H4a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1" /></svg>'

    divChildPlusIMG.appendChild(divPlusImg)
    divChildMinusIMG.appendChild(divMinusImg)
    divDecMain.appendChild(divChildPlusIMG)

    for (k = 0; k < dataBack.order.order.length; k++) {
        divChildIMG = document.createElement('div')
        divChildIMG.classList.add(dataBack.nombre)
        divChildIMG.classList.add('child')
        img = document.createElement('img');
        img.src = '../Utils/Objective' + dataBack.order.order[k] + '.png';
        img.setAttribute('width', '98%')
        img.setAttribute('height', '100%')
        img.classList.add(dataBack.nombre)
        divChildIMG.appendChild(img)
        divDecMain.appendChild(divChildIMG)
    }

    divDecMain.appendChild(divChildMinusIMG)

}

function optionSelected(event){
    //Redirigir a lista de soluciones guardando el nombre del proyecto en sessionStorage
    console.log(event.target.classList[0])
    sessionStorage.setItem("projectName",event.target.classList[0]);
    window.location.href = "SList.html"
}

function logOut(){
    sessionStorage.clear()
    window.location.href = "../index.html"
}