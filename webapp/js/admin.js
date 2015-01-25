//JavaScript Document

window.onload = function() {
	$('#tree').treeview({
		data : getTree()
	});
};

function getTree() {
	/* The default value */
	var tree = [ {
		text : "根节点"
	} ];

	$.ajax({
		url : "ajax/organization",
		data : {
			action : 'r',
		},
		async : false,
		type : "post",
		dataType : "json",
		success : function(data, status, xHr) {
			if ("OK" == data.Result) {
				if (data.Data.length > 0) {
					tree = data.Data;
				}
			} else {
				alert(data.Message);
			}
		},
		error : function(xHr, status, error) {
			alert("与服务器通信出错，请检查网络！");
		}
	});

	/*
	 * var tree = [ { text: "复旦大学", nodes: [ { text: "计算机科学计算机学院", id:"122",
	 * nodes: [ { text: "2014级研究生班级体的辅导员", nodes: [ { text: "计算机", nodes: [ {
	 * text: "团委" }, { text: "Grandchild 2" } ] }, { text: "Grandchild 2" } ] }, {
	 * text: "Grandchild 2" } ] }, { text: "Child 2" } ] }, { text: "Parent 2" }, {
	 * text: "Parent 3" }, { text: "Parent 4" }, { text: "Parent 5" } ];
	 */
	return tree;
}

function btnClick(e) {
	e = e || event;
	var t = e.target || e.srcElement

	if (t.id == "btn1") {
		$("#li1").removeClass("active");
		$("#li2").removeClass("active");
		$("#li3").removeClass("active");
		$("#li4").removeClass("active");

		$("#li1").addClass("active");

		$("#content-msg").addClass("hideTheDiv");
		$("#content-mail").addClass("hideTheDiv");
		$("#content-settings").addClass("hideTheDiv");

		$("#content-list").removeClass("hideTheDiv");
	} else if (t.id == "btn2") {
		$("#li1").removeClass("active");
		$("#li2").removeClass("active");
		$("#li3").removeClass("active");
		$("#li4").removeClass("active");

		$("#li2").addClass("active");

		$("#content-list").addClass("hideTheDiv");
		$("#content-mail").addClass("hideTheDiv");
		$("#content-settings").addClass("hideTheDiv");

		$("#content-msg").removeClass("hideTheDiv");
	} else if (t.id == "btn3") {
		$("#li1").removeClass("active");
		$("#li2").removeClass("active");
		$("#li3").removeClass("active");
		$("#li4").removeClass("active");

		$("#li3").addClass("active");

		$("#content-list").addClass("hideTheDiv");
		$("#content-msg").addClass("hideTheDiv");
		$("#content-settings").addClass("hideTheDiv");

		$("#content-mail").removeClass("hideTheDiv");
	} else if (t.id == "btn4") {
		$("#li1").removeClass("active");
		$("#li2").removeClass("active");
		$("#li3").removeClass("active");
		$("#li4").removeClass("active");

		$("#li4").addClass("active");

		$("#content-list").addClass("hideTheDiv");
		$("#content-msg").addClass("hideTheDiv");
		$("#content-mail").addClass("hideTheDiv");

		$("#content-settings").removeClass("hideTheDiv");
	}
}

function logout() {
	$.post("user?action=o", function(data) {
		if ("OK" == data.Result) {
			document.location = "index.jsp";
		}
	});
}

function editStu(id) {
	var no = document.getElementById("no" + id).innerHTML;
	var name = document.getElementById("name" + id).innerHTML;
	var phone = document.getElementById("phone" + id).innerHTML;
	var email = document.getElementById("email" + id).innerHTML;
	var remark = document.getElementById("remark" + id).innerHTML;

	document.getElementById("editNo").value = no;
	document.getElementById("editName").value = name;
	document.getElementById("editPhone").value = phone;
	document.getElementById("editMail").value = email;
	document.getElementById("editComment").value = remark;
	document.getElementById("editId").value = id;
}

function saveEdit() {
	var id = document.getElementById("editId").value;
	var no = document.getElementById("editNo").value;
	var name = document.getElementById("editName").value;
	var phone = document.getElementById("editPhone").value;
	var email = document.getElementById("editMail").value;
	var remark = document.getElementById("editComment").value;
	console.log(remark);
	$("#editStudent").modal("hide");

	$.ajax({
		url : "ajax/student",
		data : {
			action : 'u',
			id : id,
			studentNo : no,
			name : name,
			phone : phone,
			email : email,
			remark : remark,
		},
		async : false,
		type : "post",
		dataType : "json",
		success : function(data, status, xHr) {
			if ("OK" == data.Result) {
				document.getElementById("no" + id).innerHTML = no;
				document.getElementById("name" + id).innerHTML = name;
				document.getElementById("phone" + id).innerHTML = phone;
				document.getElementById("email" + id).innerHTML = email;
				document.getElementById("remark" + id).innerHTML = remark;
				new Toast({
					context : $('body'),
					message : "保存成功！"
				}).show();
			} else {
				// alert(data.Message);
				new Toast({
					context : $('body'),
					message : data.Message
				}).show();
			}
		},
		error : function(xHr, status, error) {
			// alert("与服务器通信出错，请检查网络！");
			new Toast({
				context : $('body'),
				message : "与服务器通信出错，请检查网络！"
			}).show();
		}
	});
}
var stuId;
function commentStu(id) {
	// id = 5;
	stuId = id;
	// console.log(stuId);
	$.ajax({
		url : "ajax/evaluate",
		data : {
			action : 't',
		},
		async : false,
		type : "post",
		dataType : "json",
		success : function(data, status, xHr) {
			if ("OK" == data.Result) {
				var objs = eval(data.Data);
				document.getElementById("commentTag").innerHTML = "";
				$.each(objs, function displayStudent(id, item) {
					// document.getElementById("commentTag").options.add(new
					// Option(item.tagName,item.id));
					var newNode = document.createElement("label");
					newNode.className = "checkbox-inline";
					newNode.innerHTML = "<input type=\"checkbox\" value=\"" + item.id + "\" name=\"tagCheckbox\"> " + item.tagName;
					document.getElementById("commentTag").appendChild(newNode);
				});
				var check = document.getElementsByName("tagCheckbox");
				var len = check.length;
				for (var i = 0; i < len; i++) {
					check[i].checked = false;
				}
			} else {
				// alert(data.Message);
				new Toast({
					context : $('body'),
					message : data.Message
				}).show();
				$("#commentStudent").modal("hide");
				return;
			}
		},
		error : function(xHr, status, error) {
			// alert("与服务器通信出错，请检查网络！");
			new Toast({
				context : $('body'),
				message : "与服务器通信出错，请检查网络！"
			}).show();
			$("#commentStudent").modal("hide");
			return;
		}
	});

	$.ajax({
		url : "ajax/evaluate",
		data : {
			action : 'r',
			sid : id,
		},
		async : false,
		type : "post",
		dataType : "json",
		success : function(data, status, xHr) {
			if ("OK" == data.Result) {
				// new Toast({context:$('body'),message:"保存成功！"}).show();
				// console.log(data.Data);

				var objs = eval(data.Data);
				listCommnet(objs);

			} else {
				// alert(data.Message);
				new Toast({
					context : $('body'),
					message : data.Message
				}).show();
			}
		},
		error : function(xHr, status, error) {
			// alert("与服务器通信出错，请检查网络！");
			new Toast({
				context : $('body'),
				message : "与服务器通信出错，请检查网络！"
			}).show();
		}
	});
}

function listCommnet(objs) {
	document.getElementById("commentTable").innerHTML = "";
	// $.each(objs, function displayStudent(id,item){
	// console.log(item.comment);
	/* the topic */
	/*
	 * var newNode1 = document.createElement ("tr");
	 * 
	 * var cell1=document.createElement ("td"); cell1.innerHTML = "主题：";
	 * cell1.className = "colomn"; //cell1.setAttribute("width","3em");
	 * newNode1.appendChild (cell1);
	 * 
	 * var cell2=document.createElement ("td"); cell2.innerHTML =
	 * item.evaluation.topic; newNode1.appendChild (cell2);
	 * 
	 * document.getElementById("commentTable").appendChild (newNode1);
	 * 
	 * /*the comment
	 */
	/*
	 * var newNode2 = document.createElement ("tr");
	 * 
	 * var cell3=document.createElement ("td"); cell3.innerHTML = "评价：";
	 * //cell3.setAttribute("width","3em"); newNode2.appendChild (cell3);
	 * 
	 * var cell4=document.createElement ("td"); cell4.innerHTML = "<div
	 * style=\"word-break:break-all\">"+item.evaluation.comment+"</div>";
	 * newNode2.appendChild (cell4);
	 * 
	 * document.getElementById("commentTable").appendChild (newNode2);
	 * 
	 * 
	 * var newNode3 = document.createElement ("tr");
	 * 
	 * var cell5=document.createElement ("td"); cell5.innerHTML = "时间：";
	 * //cell3.setAttribute("width","3em"); newNode3.appendChild (cell5);
	 * 
	 * var cell6=document.createElement ("td"); cell6.innerHTML =
	 * item.evaluation.createdTime; newNode3.appendChild (cell6);
	 * 
	 * document.getElementById("commentTable").appendChild (newNode3);
	 * 
	 * 
	 * var newNode4 = document.createElement ("tr");
	 * 
	 * var cell7=document.createElement ("td"); cell7.innerHTML = "时长：";
	 * //cell3.setAttribute("width","3em"); newNode4.appendChild (cell7);
	 * 
	 * var cell8=document.createElement ("td"); cell8.innerHTML =
	 * item.evaluation.period; newNode4.appendChild (cell8);
	 * 
	 * document.getElementById("commentTable").appendChild (newNode4);
	 * 
	 * 
	 * var newNode5 = document.createElement ("tr");
	 * 
	 * var cell9=document.createElement ("td"); cell9.innerHTML = "标签：";
	 * //cell3.setAttribute("width","3em"); newNode5.appendChild (cell9);
	 * 
	 * var cell10=document.createElement ("td");
	 * 
	 * var tempHtml = ""; $.each(item.tags, function displayStudent(id,item){
	 * tempHtml += item.tagName + "、"; }); if(tempHtml.length != 0)
	 * cell10.innerHTML = "<div
	 * style=\"word-break:break-all\">"+tempHtml.substring(0,tempHtml.length -
	 * 1)+"</div>"; else cell10.innerHTML = tempHtml;
	 * 
	 * newNode5.appendChild (cell10);
	 * 
	 * document.getElementById("commentTable").appendChild (newNode5);
	 * 
	 * 
	 * var newNode3 = document.createElement ("tr");
	 * 
	 * var cell5=document.createElement ("td"); newNode3.appendChild (cell5);
	 * 
	 * var cell6=document.createElement ("td"); cell6.innerHTML = "<button
	 * type=\"button\" class=\"btn btn-default\"
	 * onClick=\"deleteComment("+item.evaluation.id+");\">删除</button>";
	 * newNode3.appendChild (cell6);
	 * 
	 * document.getElementById("commentTable").appendChild (newNode3);
	 * 
	 * var newNode4 = document.createElement ("tr");
	 */

	// document.getElementById("commentTable").appendChild (newNode4);
	$.each(objs, function displayStudent(id, item) {
		// console.log(id + " : " + item.id);
		var newNode = document.createElement("tr");

		var cell2 = document.createElement("td");
		cell2.className = "text-positon";
		cell2.innerHTML = item.evaluation.topic;
		newNode.appendChild(cell2);

		var cell6 = document.createElement("td");

		// cell6.className = "text-positon";
		// cell6.id = "remark" + item.student.id;

		var commentHtml = "";
		$.each(item.tags, function displayStudent(id, item) {
			// commentHtml += "<p style=\"word-break:break-all; height:1em;
			// margin:0;\">"+item.tagName + "<p>";
			commentHtml += item.tagName + "、";
		});

		if (commentHtml.length != 0)
			commentHtml = "<div style=\"word-break:break-all\">" + commentHtml.substring(0, commentHtml.length - 1) + "</div>";

		cell6.innerHTML = commentHtml;
		newNode.appendChild(cell6);

		var cell12 = document.createElement("td");
		cell12.innerHTML = item.evaluation.period;
		newNode.appendChild(cell12);

		var opeHtml = "";
		var vis = item.evaluation.visibility;
		// console.log(vis);
		// if(vis == 0)
		opeHtml = "<table><tr><td><input type=\"button\" class=\"btn btn-default btn-comment\" value=\"查看\" onClick=\"viewComment(" + item.evaluation.id + ");\" id=\"see" + item.evaluation.id + "\"></td>" + "<td><input type=\"button\" class=\"btn btn-default btn-comment\" value=\"删除\" onClick=\"deleteComment(" + item.evaluation.id + ");\"></td></tr></table>";

		var cell7 = document.createElement("td");
		cell7.className = "text-positon";
		cell7.innerHTML = opeHtml;
		cell7.setAttribute("width", "10%");
		newNode.appendChild(cell7);

		document.getElementById("commentTable").appendChild(newNode);

		var newNode1 = document.createElement("tr");
		newNode1.className = "hideTheDiv";
		newNode1.id = "commentTr" + item.evaluation.id;

		var cell1 = document.createElement("td");
		cell1.setAttribute("colspan", "4");
		// cell1.className = "text-positon";
		cell1.innerHTML = "<div style=\"word-break:break-all\">" + item.evaluation.comment + "</div>";
		newNode1.appendChild(cell1);

		document.getElementById("commentTable").appendChild(newNode1);
	});
	// });
}

function deleteComment(id) {
	$.ajax({
		url : "ajax/evaluate",
		data : {
			action : 'd',
			cid : id,
		},
		async : false,
		type : "post",
		dataType : "json",
		success : function(data, status, xHr) {
			if ("OK" == data.Result) {
				new Toast({
					context : $('body'),
					message : "删除成功！"
				}).show();
				commentStu(stuId);
			} else {
				// alert(data.Message);
				new Toast({
					context : $('body'),
					message : data.Message
				}).show();
			}
		},
		error : function(xHr, status, error) {
			// alert("与服务器通信出错，请检查网络！");
			new Toast({
				context : $('body'),
				message : "与服务器通信出错，请检查网络！"
			}).show();
		}
	});
}

function cancleSend() {
	document.getElementById("msg-content").value = "";
	$("#msg-content").focus();
	document.getElementById("counter").innerHTML = "0";
}

function viewComment(id) {
	var name = document.getElementById("commentTr" + id).className;
	var button = document.getElementById("see" + id);
	// console.log(button.value);
	if (name == "") {
		document.getElementById("commentTr" + id).className = "hideTheDiv";
		button.value = "展开";
	} else {
		document.getElementById("commentTr" + id).className = "";
		button.value = "收起";
	}
	// console.log(name);
}