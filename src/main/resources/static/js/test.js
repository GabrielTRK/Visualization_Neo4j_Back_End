function callBack(){
    fetch('http://138.4.92.155:8081/getData').then(res => {
        return res.json()
    })
        .then(response => {
            console.log(response)
        }
        )
}

callBack()