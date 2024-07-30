if(!sessionStorage.getItem("logged")){
    window.location.href = "login.html"
}

$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();   
  });

function logOut(){
    sessionStorage.clear()
    window.location.href = "../index.html"
}