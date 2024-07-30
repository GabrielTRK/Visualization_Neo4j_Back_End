// Get the modal
var modal = document.getElementById("myModal");

// Get the button that opens the modal
var btn = document.getElementById("myBtn");

// Get the <span> element that closes the modal
var span = document.getElementsByClassName("close")[0];

span.onclick = function() {
    modal.style.display = "none";
}

window.onclick = function(event) {
    if (event.target == modal) {
      modal.style.display = "none";
    }
}

function login(){
    
    const usuario = document.getElementById("user").value;
    const pass = document.getElementById("password").value;

    const params = {
        "usuario": usuario,
        "pass": pass
    };
    const options = {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify( params )
    };
    fetch( 'http://192.168.1.41:8080/login', options )
        .then( response => response.json() )
        .then( response => {
            if(response){
                //GUardar usuario en sesión
                sessionStorage.setItem("logged", true);
                location.replace("../html/home.html")
            }else{
                modal.style.display = "block";
            }
        } );
}