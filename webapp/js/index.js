function login(){
	var username = $(".user").val(); 
	var password = $(".pwd").val();
	
	if(username == ""){
		document.getElementById("1").focus();
		return;
	}
	
	if(password == ""){
		document.getElementById("2").focus();
		return;
	}
	
	$.ajax({
		url : "user",
		data : {
			action : 'i',
			username : username,
			password : password
		},
		type: "post",
		dataType: "json",
		success: function(data, status, xHr){			
			if("OK" == data.Result){
				document.location = "admin.jsp";
			}else{
				alert(data.Message);
			}
		},
		error: function(xHr, status, error){
			alert("与服务器通信出错，请检查网络！");
		}
	});
}