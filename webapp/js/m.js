$(function(){
	initMDialog();
});

function initMDialog(){
	$("#m-dialog").dialog({
		autoOpen: false,
		width: 500,
		height: 400,
		resizable: false,
		position: {at: "right bottom"},
		buttons: [{
			text: "取消",
			click: function(){
				$(this).dialog("close");
			} 
		},{
			text: "发送",
			click: function(){
				if("mail" === $("#m-dialog").attr("type")){ sendMail(); }
				else { sendMessage(); }
			}
		}]
	});
}

function sendMessage(){
	
}
function sendMail(){
	var cid = $("#mt-class").attr("cid"),
		title = $.trim($("#mt-title").val()),
		content = $.trim($("m-content textarea").val());
	if(!(title && content)){
		showMsg("主题或者内容不能为空！", "error");
		return;
	}
	$.ajax({
		url: '',
		data: {cid: cid, title: title, content: content },
		dataType: 'json',
		success: function(data){
			
		},
		error: function(){}
	});
}
