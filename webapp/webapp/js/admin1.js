var minH  = 620;  // 屏幕的最小高度
var ztreeObj;
var ztreeSetting = {
	view : {
		showIcon : false,
		selectedMulti : false,
		fontCss: {color: "white"}
	},
	edit : {
		drag: {
			isCopy: false,
			isMove: false
		},
		enable : true,
		showRemoveBtn : false,
		showRenameBtn : false
	},
	callback : {
		onNodeCreated : function(e, treeId, node) {
			bindContextMenu(node.tId + "_a");
			if(node.isNew){ ztreeObj.editName(node);}
		},
		onRename: function(e, treeId, newNode, isCancel){
			updateNode(newNode); 
		},
		onRemove: function(e, treeId, node){
			deleteNode(node);
		},
		onAsyncSuccess: function(e, tid, node, msg){
			if(!node){
				ztreeObj.selectNode(ztreeObj.getNodes()[0]);
				loadStudents();
			}
			var res = $.parseJSON(msg);
			if(res && "ERROR" === res.Result){
				showMsg(res.Message || "Request Data Error.");
				if(res.Session){ setTimeout(function(){
					document.location = "index.jsp";
				}, 3000);}
			}
		},
		onExpand: function(e, tid, node){
			ztreeObj.selectNode(node);
		},
		onCollapse: function(e, tid, node){
			ztreeObj.selectNode(node);
		}
	},
	async: {
		enable: true,
		url: "ajax/category?action=r",
		dataFilter: filterData,
		autoParam: ["id=pid"]
	}
};

function adaptScreen(){
	var sh = $(window).height();
	sh  = sh > minH ? sh : minH;
	var mh = sh - 120;
	$(".main").css("min-height", mh + "px");
}

function logout(){
	$.post("user?action=o", function(data){
		if("OK" == data.Result){ document.location = "index.jsp"; }
	});
}

function switcher(self) {
	$(".tabcur").removeClass("tabcur").attr("disabled", false);
	$(self).addClass("tabcur").attr("disabled", true);
	var id = $(self).attr("index");
	if("admin" === id){ id = "student"; }
	$(".page-cur").removeClass("page-cur").hide();
	$("#" + id).addClass("page-cur").show();
}


$(function() {
	adaptScreen();
	$(window).resize(function(){ adaptScreen(); });
	$("span#logout > a").click(function(e){ e.preventDefault(); logout(); });
	ztreeObj = $.fn.zTree.init($("#gray"), ztreeSetting, null);
	$(".tabbtn").click(function(e){ e.preventDefault(); switcher(this); });
	$("#s-in").focus(function(){
		if($(this).hasClass("tip")){ $(this).removeClass("tip").val(""); }
	}).keydown(function(e){
		if(13 == e.which){ search();}
	});
	
	$("#s-btn").click(function(){ search(); });
});

function search(){
	var key;
	if(!$("#s-in").hasClass('tip')){
		key = $.trim($("#s-in").val());
	}
	var type = $(".s-box select").val();
	if(key){ 
		$("#student-box").jtable('load', {
			classId: $("#student-box").attr("curid"),
			type: type,
			key: key
		});
	}else{ showMsg("搜索关键字不能为空！", 'error'); }
}


function filterData(treeId, pNode, response){
	if("OK" == response.Result){ return response.Data; }
	else{ showMsg(response.Message, "error"); }
	return [];
}

function bindContextMenu(id) {
	$("#" + id).contextMenu('menu', {
		bindings : {
			'edit' : function(d) {
				var node = ztreeObj.getNodeByTId($(d).parent().attr("id"));
				ztreeObj.editName(node);
			},
			'delete' : function(d) {
				var node = ztreeObj.getNodeByTId($(d).parent().attr("id"));
				ztreeObj.removeNode(node, true);
			},
			'msg': function(d){
				var node = ztreeObj.getNodeByTId($(d).parent().attr("id")), 
				name = node.name, cid = node.id;
				$("#mt-class").val(name).attr("cid", cid);
				$("#m-dialog").attr("type", "msg").dialog("option", "title", "新信息").dialog("open");
				$("#m-title").hide();
				$("#m-content textarea").val("").focus();
			},
			'mail': function(d){
				var node = ztreeObj.getNodeByTId($(d).parent().attr("id")), 
					name = node.name, cid = node.id;
				$("#mt-class").val(name).attr("cid", cid);
				$("#m-dialog").attr("type", "mail").dialog("option", "title", "新邮件").dialog("open");
				$("#m-content textarea").val("");
				$("#m-title").show();
				$("#mt-title").val("").focus();
			}
		}
	});
}

function updateNode(node){
	var d = {
		id: node.id,
		newName: node.name
	};
	$.ajax({
		url: "ajax/category?action=u",
		data: d,
		type: "post",
		dataType: "json",
		success: function(data){
			if("OK" == data.Result){
				console.log("success.");
			}else{
				console.log(data.Message);
			}
		},
		error: function(xHr, status, err){
			console.log(err);
		}
	});
}

function deleteNode(node){
	var d = {
		id: node.id,
	};
	$.ajax({
		url: "ajax/category?action=d",
		data: d,
		type: "post",
		dataType: "json",
		success: function(data){
			if("OK" == data.Result){
				console.log("success.");
			}else{
				console.log(data.Message);
			}
		},
		error: function(xHr, status, err){
			console.log(err);
		}
	});
}

function showMsg(msg, icon, delay){
	$(".msg span.tm").text(msg);
	switch(icon){
	case "ok": $(".msg").addClass("ok"); $(".msg > img").attr("src", "images/ok.png"); break;
	case "error": $(".msg").addClass("error"); $(".msg > img").attr("src", "images/error.png"); break;
	case "loading": $(".msg").addClass("warn"); $(".msg > img").attr("src", "images/loading.gif"); break;
	default: $(".msg").addClass("warn"); $(".msg > img").attr("src", "images/warn.png"); break;
	}
	$(".msg").show();
	delay = delay || 3000;
	$(".msg").fadeOut(delay);
}

function hideMsg(){
	$(".msg span.tm").text("");
	$(".msg span.im").attr("src", "");
	$(".msg").hide();
}