function isTrue() {
    var tokens = decodeURIComponent(document.cookie);
    var token = tokens.split(";");
    var x = false;
    for (var i =0;i<token.length;i++){
        var cookieName = token[i].substring(0,token[i].indexOf("=")).trim();
        if (cookieName == "token"){
            document.getElementById("judge").innerHTML = token[i].substring(token[i].indexOf("=")+1).trim();
            x = true;
        }
    }
    if (!x){
        window.open("http://localhost:8082/oauth/authorize?client_id=client&response_type=code");
    }
}
