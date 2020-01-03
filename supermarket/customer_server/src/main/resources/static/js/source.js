function deleteGoods(goods_id) {
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
    request.open("GET","http://localhost:8081/business-server/deleteGoods?goods_id="+goods_id,true);
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


function toSourceOrder() {
    alert("跳转");
    window.location.href = "http://localhost:8081/customer-server/addSourceOrder.html";
}

function toAddGoods() {
    window.location.href = "http://localhost:8081/customer-server/addGoods.html";
}

function toAddStaff() {
    window.location.href = "http://localhost:8081/customer-server/addStaff.html";
}

function toAddStaffOrder() {
    window.location.href = "http://localhost:8081/customer-server/addStaffOrder.html";
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

function addGoods() {

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
    var formDta = new FormData();
    formDta.append("goods_name",document.getElementById("goods_name").value);
    formDta.append("goods_number",document.getElementById("goods_number").value);
    formDta.append("goods_original_price",document.getElementById("goods_original_price").value);
    formDta.append("goods_price",document.getElementById("goods_price").value);
    formDta.append("goods_manufacture_data",document.getElementById("goods_manufacture_data").value);
    formDta.append("goods_quality_time",document.getElementById("goods_quality_time").value);
    request.open("POST", "http://localhost:8081/business-server/addGoods?token="+token, true);
    request.withCredentials = true;
    if (token!=""){
        request.setRequestHeader("Authorization","Bearer "+token);
    }
    request.send(formDta);
    request.onreadystatechange = function (ev1) {
        if (request.readyState === 4) {
            if (request.status === 200) {
                alert("添加成功");
            }
        }
    }
}

function addSource() {
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
    for (var i=0;i<sourceOrders.length;i++){
        var objList = sourceOrders[i];
        var obj = {"goods_name":objList[0].text,"goods_number":objList[1].text,"goods_original_price":objList[2].text,"goods_price":objList[3].text,
            "goods_manufacture_data":objList[4].text,"goods_quality_time":objList[5].text}
        goodsList.push(obj);
    }
    var request = new XMLHttpRequest();
    request.open("POST", "http://localhost:8081/business-server/addSource", true);
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