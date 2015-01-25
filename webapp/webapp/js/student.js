var cacheSelectedRows = {};
var cacheCID = -1;
function initStudentTable(){
	$("#student-box").jtable({
		title: "学生表<span id='t-name'></span>",
		selecting: true,
		multiselect: true,
		columnResizable: false,
		selectOnRowClick: false,
		selectingCheckboxes: true,
		paging: true,
		pageList: 'minimal',
		pageSizeChangeArea: false,
		saveUserPreferences: false,
		tableId: "studentTable",
		actions: {
            listAction: 'ajax/student?action=r',
            createAction: 'ajax/student?action=c',
            updateAction: 'ajax/student?action=u',
            deleteAction: 'ajax/student?action=d'
        },
        fields: {
            id: {
                key: true,
                list: false
            },
            studentNo: {
                title: '学号',
                width: '30%'
            },
            name: {
                title: '姓名',
                width: '15%'
            },
            phone: {
                title: '电话',
                width: '20%'
            },
            email: {
            	title: '邮箱',
            	width: '35%'
            }
        },
        toolbar: {
            items: [{
                icon: 'images/load.png',
                text: '加载',
                click: function () {
                	loadStudents();
                }
            },{
                icon: 'images/refresh.png',
                text: '刷新',
                click: function () {
                }
            }]
        },
        selectionChanged: function(event, data){},
        formSubmitting: function(e, data){
        	var form = data.form, type = data.formType, row = data.row;
        	var ts = $("input[type='text']",form);
        	var valid = true;
			ts.each(function(){
				var self = $(this), val = $.trim(self.val());
				if(val.length <= 0){ self.addClass("required"); valid = false; }
			});
        	return valid;
        },
        recordsLoaded: function(e, data){
        	if($("#student-box").prop("collapse")){
        		hideCommand();
        		if($(".nform").is(":visible")){
        			showSelect();
        		}else {
        			hideSelect();
        		}
        		if(cacheCID == $("#student-box").attr("curId")){
        			$("#student-box tbody tr").each(function(){
            			var sid = $(this).attr("data-record-key");
            			if(cacheSelectedRows[sid]){
            				$("#student-box").jtable("selectRows", $(this));
            			}
            		});
        		}
        	}else{
        		hideSelect();
        		showCommand();
        	}
        }
	});
}

function showCommand(){
	$("#student-box th.jtable-command-column-header:not(.jtable-column-header-selecting)").show();
	$("#student-box td.jtable-command-column").show();
}

function hideCommand(){
	$("#student-box th.jtable-command-column-header").hide();
	$("#student-box td.jtable-command-column").hide();
}

function showSelect(){
	$("#student-box th.jtable-column-header-selecting").show();
	$("#student-box td.jtable-selecting-column").show();
}

function hideSelect(){
	$("#student-box th.jtable-column-header-selecting").hide();
	$("#student-box td.jtable-selecting-column").hide();
	deselectRows($("#student-box").jtable("selectedRows"));
}

function collapseTable(){
	if($("#student-box").prop("collapse")){ return; }
	$($("#student-box div.jtable-title-text").contents()[0]).remove();
	var title = $("#t-name").text();
	title = title.substring(1, title.length - 1);
	$("#t-name").text(title);
	$("#student-box").width(250).css("float", "left");
	$("#student-box").jtable('changeColumnVisibility', 'phone', 'hidden');
	$("#student-box").jtable('changeColumnVisibility', 'email', 'hidden');
	$("#student-box  span.jtable-toolbar-item:gt(0)").hide();
	$("#student-box  th.jtable-column-header:eq(0)").css("width", "54%");
	$("#student-box  th.jtable-column-header:eq(1)").css("width", "45%");
	$("#student-box  div.jtable-right-area").hide();
	$("div#tool-bar").hide();
	$("#student-box").prop("collapse", true);
	$("#admin").show();
	$("#student-box").jtable("reload");
	hideCommand();
}

function restoreTable(){
	if(!$("#student-box").prop("collapse")){ return; }	
	$("#student-box  div.jtable-title-text").prepend("学生表");
	var title = $("#t-name").text();
	title = "（" + title + "）";
	$("#t-name").text(title);
	$("#student-box").width(700).css("float", "none");
	$("#student-box").jtable('changeColumnVisibility', 'phone', 'visible');
	$("#student-box").jtable('changeColumnVisibility', 'email', 'visible');
	$("#student-box  span.jtable-toolbar-item:gt(0)").show();
	$("#student-box  th.jtable-column-header:eq(0)").css("width", "30%");
	$("#student-box  th.jtable-column-header:eq(1)").css("width", "15%");
	$("#student-box  div.jtable-right-area").show();
	$("div#tool-bar").show();
	$("#student-box").prop("collapse", false);
	$("#admin").hide();
	$("#student-box").jtable("reload");
	showCommand();
}

function loadStudents() {
	var nodes = ztreeObj.getSelectedNodes();
	if(null == nodes || nodes.length <= 0){ showMsg("先选中左边的类别。", "warn"); return; }
	var node = nodes[0], nodeId = node.id, nodeName = node.name, tId = node.tId;
	$("#student-box").jtable('load', {classId: nodeId}, function(){
		if($("#student-box").prop("collapse")){ $("span#t-name").text(nodeName); }
		else{ $("span#t-name").text("（" + nodeName + "）"); }
		$("#student-box").attr("tId", tId);
		$("#student-box").attr("curId", nodeId);
	});
	if(cacheCID !== $("#student-box").attr("curId")){
		$(".nform").hide();
	}
}


function addToClass(){
	var pId = $("#student-box").attr("curId"),
		tId = $("#student-box").attr("tId"),
		pNode = ztreeObj.getNodeByTId(tId),
		name = $.trim($("#c-name").val());
	
	if(!name){ showMsg("分类名称不能为空！", "error"); return;}
	var ids = [];
	$(".c-list .sn").each(function(){
		ids.push($(this).attr("index"));
	});
	if(ids.length <= 0){ showMsg("没有任何添加任何学生！", "error"); return;}
	var d = {
		pId: pId,
		name: name,
		ids: ids
	};
	$.ajax({
		url: "ajax/organization?action=c",
		data: d,
		type: "post",
		dataType: "json",
		success: function(data){
			if("OK" == data.Result){
				showMsg("add success.", "ok");
				var node = data.Data;
				ztreeObj.addNodes(pNode, node, true);
			}else{
				showMsg(data.Message, "error");
			}
		},
		error: function(xHr, status, err){
			console.log(err);
		}
	});
}

function addRelation(){
	var srs = $('#student-box').jtable('selectedRows');
	var nodes = ztreeObj.getSelectedNodes();
	if(null == nodes || nodes.length <= 0){ showMsg("No element selected.", "error"); return; }
	var nodeId = nodes[0].id;
	
	if(nodeId == $("#student-box").attr("curId")){ showMsg("Already in.", "error"); return; }
	
	var ids = [];
	srs.each(function () {
	    var record = $(this).data('record');
	    ids.push(record.id);
	});
	
	addToClass(nodeId, ids);
}

function initConfirm(){
	$("#confirm").dialog({
		resizable: false,
		modal: true,
		autoOpen: false,
		minWidth: 300,
		minHeight: 150,
		buttons: [{
			text: "取消",
			click: function(){
				showMsg("您取消了该操作！", "warn");
				$("#confirm").dialog("close");
			}
			},{
			text: "确定",
			click: function(){
				var cId = $("#up-cn").attr("cid");
				if(cId){ getProcessMsg(cId); }
				$("#confirm").dialog("close");
				showMsg("正在处理中...", "loading");
			}
		}]
	});
}
/** 文件上传 */
function initUploader(){
	var reg = /xlsx|xls/i;
	new AjaxUpload('uploader', {
        action: 'upload',
		data : {},
		onSubmit : function(file , ext){
			$("#uploader").button({disabled: true});
			this.disable();
			if(!reg.test(ext)){
				showMsg("上传的文件必须是Excel文档！", "warn");
				$("#uploader").button({disabled: false});
				this.enable();
				return false;
			}
			showMsg("文件上传中...", "loading");
			return true;
		},
		onComplete: function(file, response){
			response = $.parseJSON(response);
			if("OK" === response.Result){
				showMsg("请在左边选择好要上传的分类！", "warn");
				setTimeout(function(){
					var nodes = ztreeObj.getSelectedNodes();
                	var nodeId = nodes[0].id, nodeName = nodes[0].name;
                	$("#up-cn").attr("cid", nodeId).text(nodeName);
                	$("#confirm").dialog('open');
				}, 2000);
			}else{
				showMsg("上传失败，请重试！", "error");
			}
			$("#uploader").button({disabled: false});
			this.enable();
		}
	});
}

function getProcessMsg(nodeId){
	$.ajax({
		url: "ajax/student?action=p",
		data: {
			cid: nodeId
		},
		type: "get",
		dataType: "json",
		success: function(data){
			if("OK" == data.Result){ showMsg("添加成功！","ok");}
			else{ showMsg("添加失败，请重试！", "error"); }
		},
		error: function(xHr, status, err){
			console.log(err);
		}
	});
}

function addTag(tr){
	var sid = $(tr).attr("data-record-key"),
		name = $("td:eq(2)", tr).text();
	
	var html = "<div class='sn' index=" + sid + "><div class='del-tag'></div><span>" + name + "</span></div>";
	$(".c-list").append(html);
	cacheSelectedRows[sid] = true;
}

function removeTag(tr){
	var sid = $(tr).attr("data-record-key");
	$(".c-list > .sn[index='" + sid + "']").remove();
	delete cacheSelectedRows[sid];
}

function addAll(){
	$("#student-box tbody tr").each(function(){
		var sid = $(this).attr("data-record-key");
		if($(".c-list > .sn[index='" + sid + "']").length <= 0){
			addTag(this);
		}
	});
	
}

function removeAll(){
	$("#student-box tbody tr").each(function(){
		var sid = $(this).attr("data-record-key");
		if($(".c-list > .sn[index='" + sid + "']").length > 0){
			removeTag(this);
		}
	});
}

function deselectRows(trs){
	trs.each(function(){
		$(this).removeClass("jtable-row-selected");
		$("input[type=checkbox]", this).prop("checked", false);
	});
	if($(".c-list .sn").length <= 0){ 
		$("th.jtable-column-header-selecting input").prop("checked", false);	
		$("th.jtable-column-header-selecting input").prop("indeterminate", false);
	}
}

function newClass(){
	$(".nform").show();
	$("#c-name").val("");
	$(".c-list").empty();
	cacheCID = $("#sudent-box").attr("curId");
	cacheSelectedRows = {};
	showSelect();
	deselectRows($("#student-box").jtable("selectedRows"));
}

$(function(){
	initStudentTable();
	$("#downloader").button();
	$("#uploader").button({disabled: false});
	initUploader();
	initConfirm();
	$("button[index='student']").click(function(){ restoreTable(); });
	$("button[index='admin']").click(function(){ collapseTable(); });
	$("#addNew").button().click(function(){ newClass(); });
	$("#cbtn").button().click(function(){ 
		$(".nform").hide();
		cacheSelectedRows = {};
		hideSelect();
	});
	$("#sbtn").button().click(function(){ addToClass(); });
	
	$(".sn").live({'mouseenter': function(){
		$("span", this).css("background", "#aaa");
		$(".del-tag", this).show();
	},mouseleave: function(){
		$("span", this).css("background", "#ccc");
		$(".del-tag", this).hide();
	}});
	$("#student-box td.jtable-selecting-column > input").live("change", function(){
		var tr = $(this).parent().parent();
		if($(this).prop("checked")){ addTag(tr); }
		else { removeTag(tr); }
	});
	$("#student-box th.jtable-column-header-selecting input").live("change", function(){
		if($(this).prop("checked")){ addAll(); }
		else { removeAll(); }
	});
	$(".sn .del-tag").live("click", function(){
		var sid = $(this).parent().attr("index");
		var tr = $("#student-box tbody tr[data-record-key=" + sid + "]");
		removeTag(tr);
		deselectRows(tr);
	});
});
