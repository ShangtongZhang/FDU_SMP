function register(){
	var username = $(".user").val(); 
	var password = $(".pwd").val();
	var password2 = $(".pwd2").val(); 
	var name = $(".name").val();
	var email = $(".email").val(); 
	var phone = $(".tel").val();
	
	if(username == "" || password == "" || password2 == "" || name == "" || email == "" || phone =="")
		return;
	
	if(password != password2){
		alert("密码输入错误，请重新输入！");
		document.getElementById("2").value="";
		document.getElementById("3").value="";
		return;
	}
	
	var myreg = /^[-_A-Za-z0-9]+@([_A-Za-z0-9]+\.)+[A-Za-z0-9]{2,3}$/;
    if(!myreg.test(email)){
    	alert("请输入正确的邮件地址！");
    	document.getElementById("5").value="";
        return;
    }
    
    var regu =/^[0-9]{11}$/; 
	var re = new RegExp(regu); 
	if (!re.test(phone)) { 	   
		alert("请输入正确的电话号码！");
		document.getElementById("6").value="";
	    return; 
	} 
	
	$.ajax({
		url : "user",
		data : {
			action : "g",
			username : username,
			password : password,
			name: name,
			email: email,
			phone: phone
		},
		type: "post",
		dataType: "json",
		success: function(data, status, xHr){			
			if("OK" == data.Result){
				document.location = "index.jsp";
			}else if("Already Exist." == data.Message){		
				document.getElementById("1").value="";
				alert("此用户名已存在");				
			}else
			    location.reload();
		},
		error: function(xHr, status, error){
			alert("与服务器通信出错，请检查网络！");
		}
	});
}