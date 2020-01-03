function deleteGoods(order_id) {
    var cookieList = decodeURIComponent(document.cookie);
    var tokens = cookieList.split(";");
    var token = "";
    for (var i =0;i<tokens.length;i++){
        var cookieName = tokens[i].substring(0,tokens[i].indexOf("=")).trim();
        if (cookieName == "access-token"){
            token = tokens[i].substring(tokens[i].indexOf("=")+1).trim();
        }
    }

    var request = new XMLHttpRequest();
    request.open("GET","http://localhost:8081/business-server/deleteOrder?order_id="+order_id,true);
    request.withCredentials = true;
    request.setRequestHeader("Content-Type","application/json");
    if (token!=""){
        request.setRequestHeader("Authorization","Bearer "+token);
    }
    request.send();
    request.onreadystatechange = function (ev1) {
        if (request.readyState === 4) {
            if (request.status === 200) {
                location.reload(true);
            }
        }
    }
}


function removeGoods(x){
    var index = x.parentNode.parentNode.rowIndex;
    var goodsTable = document.getElementById("goodsTable");
    goodsTable.deleteRow(index);
    delete sourceOrders[index-1];
    for (var i=index-1;i<sourceOrders.length-1;i++){
        sourceOrders[i] = sourceOrders[i+1];    }
    sourceOrders.length = sourceOrders.length-1;
}

function toAddOrder() {
    window.location.href = "http://localhost:8081/customer-server/addOrder.html";
}

function addOrder() {
    var cookieList = decodeURIComponent(document.cookie);
    var tokens = cookieList.split(";");
    var token = "";
    for (var i =0;i<tokens.length;i++){
        var cookieName = tokens[i].substring(0,tokens[i].indexOf("=")).trim();
        if (cookieName == "access-token"){
            token = tokens[i].substring(tokens[i].indexOf("=")+1).trim();
        }
    }

    var goodsList = [];
    var user = "";
    for (var i=0;i<sourceOrders.length;i++){
        var objList = sourceOrders[i];
        user = objList[3].text;
        var obj = {"order_goods_id":objList[0].text,"order_goods_name":objList[1].text,"order_subtotal":objList[4].text}
        goodsList.push(obj);
    }
    if (user != null && user != "") {
        var request = new XMLHttpRequest();
        request.open("POST", "http://localhost:8081/business-server/addOrder?order_purchaser="+user, true);
        request.withCredentials = true;
        request.setRequestHeader("Content-Type","application/json");
        if (token!=""){
            request.setRequestHeader("Authorization","Bearer "+token);
        }
        request.setRequestHeader("kbn-version", "5.3.0");
        request.send(JSON.stringify(goodsList));
        request.onreadystatechange = function (ev1) {
            if (request.readyState === 4) {
                if (request.status === 200) {
                    alert("添加成功");
                }
            }
        }
    }
}