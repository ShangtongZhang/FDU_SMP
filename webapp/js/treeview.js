var isOpe = false;
var opeType = 0;
var currentPage = 1;
var totalPage = 1;
var oid = 1;
var studentArray = new Array();
var search = "";
var type = -1;
var tempType = 1;
var perPage = 20;
var selectedStuArray = new Array();
var ini = false;
var total = 0;
(function($, window, document, undefined) {

	/*global jQuery, console*/

	'use strict';

	var pluginName = 'treeview';
	//var dbl = false;
	//var pt = 0;
	//var nt = 1;
	var time = null;
	var Tree = function(element, options) {

		this.$element = $(element);
		this._element = element;
		this._elementId = this._element.id;
		this._styleId = this._elementId + '-style';

		this.tree = [];
		this.nodes = [];
		this.selectedNode = {};
		
		this._init(options);
	};

	Tree.defaults = {

		injectStyle: true,

		levels: 2,

		expandIcon: 'glyphicon glyphicon-plus',
		collapseIcon: 'glyphicon glyphicon-minus',
		emptyIcon: 'glyphicon',
		nodeIcon: 'glyphicon glyphicon-bookmark',

		color: undefined, // '#000000',
		backColor: undefined, // '#FFFFFF',
		borderColor: undefined, // '#dddddd',
		onhoverColor: '#F5F5F5',
		selectedColor: '#FFFFFF',
		selectedBackColor: '#428bca',

		enableLinks: false,
		highlightSelected: true,
		showBorder: true,
		showTags: false,

		// Event handler for when a node is selected
		onNodeSelected: undefined
	};

	Tree.prototype = {

		remove: function() {

			this._destroy();
			$.removeData(this, 'plugin_' + pluginName);
			$('#' + this._styleId).remove();
		},

		_destroy: function() {

			if (this.initialized) {
				this.$wrapper.remove();
				this.$wrapper = null;

				// Switch off events
				this._unsubscribeEvents();
			}

			// Reset initialized flag
			this.initialized = false;
		},

		_init: function(options) {
		
			if (options.data) {
				if (typeof options.data === 'string') {
					options.data = $.parseJSON(options.data);
				}
				this.tree = $.extend(true, [], options.data);
				delete options.data;
			}

			this.options = $.extend({}, Tree.defaults, options);

			this._setInitialLevels(this.tree, 0);

			this._destroy();
			this._subscribeEvents();
			this._render();
		},

		_unsubscribeEvents: function() {

			this.$element.off('click');
		},
		
		_subscribeEvents: function() {

			this._unsubscribeEvents();

			
			this.$element.on('click', $.proxy(this._set, this));
			this.$element.on('dblclick', $.proxy(this._clickHandler_2, this));        /*added for double click*/

			if (typeof (this.options.onNodeSelected) === 'function') {
				this.$element.on('nodeSelected', this.options.onNodeSelected);
			}
		},
		
		_set:function(event){
			clearTimeout(time);
			var obj = this;
			time = setTimeout(function(){
				if (!obj.options.enableLinks) { event.preventDefault(); }
				
				var target = $(event.target),
					classList = target.attr('class') ? target.attr('class').split(' ') : [],
					node = obj._findNode(target);

				if ((classList.indexOf('click-expand') != -1) ||
						(classList.indexOf('click-collapse') != -1)) {
					// Expand or collapse node by toggling child node visibility
					obj._toggleNodes(node);
					obj._render();
				}
				else if (node) {
					if (obj._isSelectable(node)) {
						obj._setSelectedNode(node);
					} else {
						obj._toggleNodes(node);
						obj._render();
					}
				}
			},300);
		},
		
		_clickHandler_2: function(event) {
			console.log("双击----");
			clearTimeout(time);
			if (!this.options.enableLinks) { event.preventDefault(); }
			
			var target = $(event.target),
				classList = target.attr('class') ? target.attr('class').split(' ') : [],
				node = this._findNode(target);

			if (classList.indexOf('list-group-item') != -1) {
				// Expand or collapse node by toggling child node visibility
				this._toggleNodes(node);
				this._render();
			}
			else if (node) {
				if (this._isSelectable(node)) {
					this._setSelectedNode(node);
				} else {
					this._toggleNodes(node);
					this._render();
				}
			}
			
		},
		
		_clickHandler: function(event) {
			console.log("单击---");
			if (!this.options.enableLinks) { event.preventDefault(); }
			
			var target = $(event.target),
				classList = target.attr('class') ? target.attr('class').split(' ') : [],
				node = this._findNode(target);

			if ((classList.indexOf('click-expand') != -1) ||
					(classList.indexOf('click-collapse') != -1)) {
				// Expand or collapse node by toggling child node visibility
				this._toggleNodes(node);
				this._render();
			}
			else if (node) {
				if (this._isSelectable(node)) {
					this._setSelectedNode(node);
				} else {
					this._toggleNodes(node);
					this._render();
				}
			}
		},

		// Looks up the DOM for the closest parent list item to retrieve the 
		// data attribute nodeid, which is used to lookup the node in the flattened structure.
		_findNode: function(target) {

			var nodeId = target.closest('li.list-group-item').attr('data-nodeid'),
				node = this.nodes[nodeId];

			if (!node) {
				console.log('Error: node does not exist');
			}
			return node;
		},

		// Actually triggers the nodeSelected event
		_triggerNodeSelectedEvent: function(node) {

			this.$element.trigger('nodeSelected', [$.extend(true, {}, node)]);
		},
		
		/*return the nodeId*/
		 _findTheParentNode: function(root,id){
			 var result;
			 var nodesArray = root.nodes ? root.nodes : root._nodes ? root._nodes : undefined;
			 if (nodesArray) {
				 for(var i = 0;i < nodesArray.length;i++){
					 if(nodesArray[i].nodeId == id){
						 return root.nodeId;
					 }
					 else{
					     result = this._findTheParentNode(nodesArray[i],id);
						 if(result != -1)
						     return result;
					 }
				 }				 
			 }else
			     return -1;
			 return -1;
		 },
		 
		// Handles selecting and unselecting of nodes, 
		// as well as determining whether or not to trigger the nodeSelected event
		_setSelectedNode: function(node) {
			//console.log(ope);
			
			/*Handle the tree operation*/
			if(isOpe){				
				/*0 for remove, 1 for modify, 2 for add.*/				
				if(opeType == 0){
					if(node.nodeId == 0)
					    return;
					
					var res = confirm("确定删除\"" + node.text + "\"?");
					if(res){						
						$.ajax({
							url: "ajax/organization",
							data : {
								action : 'd',
								id: node.id,
							},
							async: false,
							type: "post",
							dataType: "json",
							success: function(data){
								if("OK" == data.Result){
									
								}else{
									//console.log(data.Message);
									new Toast({context:$('body'),message:data.Message}).show();
									return;
								}
							},
							error: function(xHr, status, err){
								//console.log(err);
								new Toast({context:$('body'),message:err}).show();
								return;
							}
						});	
						/*展开以后_nodes为空，收起后nodes为空*/
                        var pnode = this.nodes[this._findTheParentNode(this.nodes[0],node.nodeId)];						
						
						var nodesArray = pnode.nodes ? pnode.nodes : pnode._nodes ? pnode._nodes : undefined;
						var len = nodesArray.length;
				        if (nodesArray) {
					        for(var i = 0;i < len;i++){								
								if(nodesArray[i].nodeId == node.nodeId){
									nodesArray.splice(i,1);
									
									if(len == 1){
										pnode.nodes = undefined;
										pnode._nodes = undefined;
									}
									break;
								}
							}
				        }else
						    console.log("undefined");
						
						
						this._render();
					}
					else
					    return;
				    
				}
				
				if(opeType == 1){
					var text = node.text;
					var str = prompt("更改名称",text);					
					if(str == null || str == text)
					    return;
								
					$.ajax({
						url: "ajax/organization",
						data : {
							action : 'u',
							id: node.id,
							newTitle: str,
							newDescription: "",
						},
						async: false,
						type: "post",
						dataType: "json",
						success: function(data){
							if("OK" == data.Result){
								node.text = str;
							}else{
								//console.log(data.Message);
								new Toast({context:$('body'),message:data.Message}).show();
								return;
							}
						},
						error: function(xHr, status, err){
							//console.log(err);
							new Toast({context:$('body'),message:err}).show();
							return;
						}
					});
					
																	
					this._render();
				}
				
				if(opeType == 2){
					var str = prompt("子机构名称");
					if(str == null)
					    return;
					var newId;
					$.ajax({
						url: "ajax/organization",
						data : {
							action : 'c',
							title: str,
							description: "",
							pId: node.id,
						},
						async: false,
						type: "post",
						dataType: "json",
						success: function(data){
							if("OK" == data.Result){
								newId = data.id;	
							}else{
								//alert(data.Message);
								new Toast({context:$('body'),message:data.Message}).show();
								return;
							}
						},
						error: function(xHr, status, err){
							//alert(err);
							new Toast({context:$('body'),message:err}).show();
							return;
						}
					});
					
					var nodesArray = node.nodes ? node.nodes : node._nodes ? node._nodes : undefined;	
					var newNode = {"text":str,"id":newId,"pid":node.id,"description":"","stucount":0};				
				    if (nodesArray) {
					    nodesArray.push(newNode);
					    if(node.nodes == undefined)
					    	this._toggleNodes(node);
				    }
					else{
						node._nodes = [newNode];
						this._toggleNodes(node);
					}					
				    
					this._render();
				}
				
				
				isOpe = false;
				return;
			}
			//console.log("_setSelectedNode : " + node.nodeId);
			//console.log(node.id);
			//console.log(currentPage);
			search = "";
			type = -1;
			document.getElementById("list-table").innerHTML = "";
			document.getElementById("searchText").value = "";
			currentPage = 1;
			totalPage = 1;
			document.getElementById("showPage").innerHTML = "1/1";
			oid = node.id;
			total = 0;
			//$("#optionSide").addClass("hideTheDiv");
			//console.log(oid);
			ini = true;
			document.getElementById("showTotal").innerHTML = "共0条";
			$.ajax({
				url : "ajax/student",
				data : {
					action : 'r',
					oid: node.id,
					page: currentPage,
					search:search,
					type:type,
					numPerPage:perPage,
				},
				async: false, 
				type: "post",
				dataType: "json",
				success: function(data, status, xHr){			
					if("OK" == data.Result){
						totalPage = data.TotalPageSize;
						var objs = eval(data.Students);
						document.getElementById("selectAllBox").checked = false;
						/*
						if(objs.length == 0)
							return;			
						*/		
						//oid = node.id;
						total = data.Sum;
						listStudent(objs);
						document.getElementById("showPage").innerHTML = "1/" + totalPage;
						document.getElementById("showTotal").innerHTML = "共" + total + "条";
					}else{
						//alert(data.Message);
						studentArray.splice(0,studentArray.length);
						new Toast({context:$('body'),message:data.Message}).show();
						document.getElementById("selectAllBox").checked = false;
					}
				},
				error: function(xHr, status, error){
					//alert("与服务器通信出错，请检查网络！");
					studentArray.splice(0,studentArray.length);
					new Toast({context:$('body'),message:"与服务器通信出错，请检查网络！"}).show();
					document.getElementById("selectAllBox").checked = false;
				}
			});
			
			
			
			this._buildTree(this.tree, 0);
			if (!node) { return; }
			
			if (node === this.selectedNode) {
				this.selectedNode = {};
			}
			else {
				this._triggerNodeSelectedEvent(this.selectedNode = node);
			}
			
			this._render();
		},

		// On initialization recurses the entire tree structure 
		// setting expanded / collapsed states based on initial levels
		_setInitialLevels: function(nodes, level) {

			if (!nodes) { return; }
			level += 1;

			var self = this;
			$.each(nodes, function addNodes(id, node) {
				
				if (level >= self.options.levels) {
					self._toggleNodes(node);
				}

				// Need to traverse both nodes and _nodes to ensure 
				// all levels collapsed beyond levels
				var nodes = node.nodes ? node.nodes : node._nodes ? node._nodes : undefined;
				if (nodes) {
					return self._setInitialLevels(nodes, level);
				}
			});
		},

		// Toggle renaming nodes -> _nodes, _nodes -> nodes
		// to simulate expanding or collapsing a node.
		_toggleNodes: function(node) {
			if (!node.nodes && !node._nodes) {
				return;
			}			
			if (node.nodes) {
				node._nodes = node.nodes;
				delete node.nodes;
				
			}
			else {
				node.nodes = node._nodes;
				delete node._nodes;				
			}
		},

		// Returns true if the node is selectable in the tree
		_isSelectable: function (node) {
			return node.selectable !== false;
		},

		_render: function() {

			var self = this;

			if (!self.initialized) {

				// Setup first time only components
				self.$element.addClass(pluginName);
				self.$wrapper = $(self._template.list);

				self._injectStyle();
				
				self.initialized = true;
			}

			self.$element.empty().append(self.$wrapper.empty());

			// Build tree
			self.nodes = [];
			self._buildTree(self.tree, 0);
		},

		// Starting from the root node, and recursing down the 
		// structure we build the tree one node at a time
		_buildTree: function(nodes, level) {
            //console.log("build_tree");
			if (!nodes) { return; }
			level += 1;

			var self = this;
			$.each(nodes, function addNodes(id, node) {

				node.nodeId = self.nodes.length;
				self.nodes.push(node);

				var treeItem = $(self._template.item)
					.addClass('node-' + self._elementId)
					.addClass((node === self.selectedNode) ? 'node-selected' : '')
					.attr('data-nodeid', node.nodeId)
					.attr('style', self._buildStyleOverride(node));

				// Add indent/spacer to mimic tree structure
				for (var i = 0; i < (level - 1); i++) {
					treeItem.append(self._template.indent);
				}

				// Add expand, collapse or empty spacer icons 
				// to facilitate tree structure navigation
				if (node._nodes) {
					treeItem
						.append($(self._template.expandCollapseIcon)
							.addClass('click-expand')
							.addClass(self.options.expandIcon)
						);
				}
				else if (node.nodes) {
					treeItem
						.append($(self._template.expandCollapseIcon)
							.addClass('click-collapse')
							.addClass(self.options.collapseIcon)
						);
				}
				else {
					treeItem
						.append($(self._template.expandCollapseIcon)
							.addClass(self.options.emptyIcon)
						);
				}

				// Add node icon
				treeItem
					.append($(self._template.icon)
						.addClass(node.icon ? node.icon : self.options.nodeIcon)
					);

				// Add text
				if (self.options.enableLinks) {
					// Add hyperlink
					treeItem
						.append($(self._template.link)
							.attr('href', node.href)
							.append(node.text)
						);
				}
				else {
					// otherwise just text
					treeItem
						.append(node.text);
				}

				// Add tags as badges
				if (self.options.showTags && node.tags) {
					$.each(node.tags, function addTag(id, tag) {
						treeItem
							.append($(self._template.badge)
								.append(tag)
							);
					});
				}

				// Add item to the tree
				self.$wrapper.append(treeItem);

				// Recursively add child ndoes
				if (node.nodes) {
					return self._buildTree(node.nodes, level);
				}
			});
		},

		// Define any node level style override for
		// 1. selectedNode
		// 2. node|data assigned color overrides
		_buildStyleOverride: function(node) {

			var style = '';
			if (this.options.highlightSelected && (node === this.selectedNode)) {
				style += 'color:' + this.options.selectedColor + ';';
			}
			else if (node.color) {
				style += 'color:' + node.color + ';';
			}

			if (this.options.highlightSelected && (node === this.selectedNode)) {
				style += 'background-color:' + this.options.selectedBackColor + ';';
			}
			else if (node.backColor) {
				style += 'background-color:' + node.backColor + ';';
			}

			return style;
		},

		// Add inline style into head 
		_injectStyle: function() {

			if (this.options.injectStyle && !document.getElementById(this._styleId)) {
				$('<style type="text/css" id="' + this._styleId + '"> ' + this._buildStyle() + ' </style>').appendTo('head');
			}
		},

		// Construct trees style based on user options
		_buildStyle: function() {

			var style = '.node-' + this._elementId + '{';
			if (this.options.color) {
				style += 'color:' + this.options.color + ';';
			}
			if (this.options.backColor) {
				style += 'background-color:' + this.options.backColor + ';';
			}
			if (!this.options.showBorder) {
				style += 'border:none;';
			}
			else if (this.options.borderColor) {
				style += 'border:1px solid ' + this.options.borderColor + ';';
			}
			style += '}';

			if (this.options.onhoverColor) {
				style += '.node-' + this._elementId + ':hover{' +
				'background-color:' + this.options.onhoverColor + ';' +
				'}';
			}

			return this._css + style;
		},

		_template: {
			list: '<ul class="list-group"></ul>',
			item: '<li class="list-group-item">'+
			         
					 
					    
					    
                        '<a class="btn-tree-operation" onClick="treeOperation(0);"><span class="glyphicon glyphicon-remove"></span></a>'+
						'<a class="btn-tree-operation" onClick="treeOperation(1);"><span class="glyphicon glyphicon-pencil"></span></a>'+
						'<a class="btn-tree-operation" onClick="treeOperation(2);"><span class="glyphicon glyphicon-plus"></span></a>'+
						  /*
                          '<ul class="dropdown-menu tree-operation-menu" role="menu">'+                         
                            '<li><a>增加</a></li>'+
							'<li><a>删除</a></li>'+
							'<li><a>修改</a></li>'+
                          '</ul>'+
                           */
			       '</li>',
			indent: '<span class="indent"></span>',
			expandCollapseIcon: '<span class="expand-collapse"></span>',
			icon: '<span class="icon"></span>',
			link: '<a href="#" style="color:inherit;"></a>',
			badge: '<span class="badge"></span>'
		},

		_css: '.list-group-item{cursor:pointer;}span.indent{margin-left:10px;margin-right:10px}span.expand-collapse{width:1rem;height:1rem}span.icon{margin-left:10px;margin-right:5px}'
		// _css: '.list-group-item{cursor:pointer;}.list-group-item:hover{background-color:#f5f5f5;}span.indent{margin-left:10px;margin-right:10px}span.icon{margin-right:5px}'

	};

	var logError = function(message) {
        if(window.console) {
            window.console.error(message);
        }
    };

	// Prevent against multiple instantiations,
	// handle updates and method calls
	$.fn[pluginName] = function(options, args) {
		return this.each(function() {
			var self = $.data(this, 'plugin_' + pluginName);
			if (typeof options === 'string') {
				if (!self) {
					logError('Not initialized, can not call method : ' + options);
				}
				else if (!$.isFunction(self[options]) || options.charAt(0) === '_') {
					logError('No such method : ' + options);
				}
				else {
					if (typeof args === 'string') {
						args = [args];
					}
					self[options].apply(self, args);
				}
			}
			else {
				if (!self) {
					$.data(this, 'plugin_' + pluginName, new Tree(this, $.extend(true, {}, options)));
				}
				else {
					self._init(options);
				}
			}
		});
	};

})(jQuery, window, document);

/*0 for remove, 1 for modify, 2 for add.*/
function treeOperation(n){
	isOpe = true;
	opeType = n;
}

function listStudent(objs){
	document.getElementById("list-table").innerHTML = "";
	document.getElementById("commentAndRemart").innerHTML = "辅导员";
	//$("#list-table").innerHTML = "";
	//console.log("reach here");
	/*
	for(var i=1;i<10;i++){
	var row=document.createElement ("tr"); 
	row.id=i;
	for(var j=1;j<6;j++){
	    var cell=document.createElement ("td"); 
	    cell.id =i+"/"+j;
	    cell.appendChild(document.createTextNode ("第"+cell.id+"列")); 
	    row.appendChild (cell); 
	}
    document.getElementById("list-table").appendChild (row); 
	}*/
	var k = 1;
	studentArray.splice(0,studentArray.length);
	//console.log(studentArray);
	$.each(objs, function displayStudent(id,item){
		//console.log(id + " : " + item.id);
		var newNode = document.createElement ("tr");
		newNode.id = "tr" + item.id;
		/*
		var html1 = ""+
                      +"<td style=\"width:4%;\"><input type=\"checkbox\" name=\"student-list\"></td>"+
                      +"<td style=\"width:14%;\">12302010054</td>"+
                      +"<td style=\"width:13%;\">李明琪</td>"+
                      +"<td style=\"width:14%;\">13788936686</td>"+
                      +"<td style=\"width:25%;\">12302010054@fudan.edu.cn</td>"
                      +"<td style=\"width:20%;\">看看能有多长</td>"
                      +"<td style=\"width:8%;\"><input type=\"button\" class=\"btn btn-default btn-comment\" value=\"评价\"></td>"+
                 +"";		
		*/
		
		var cell1=document.createElement ("td");	
		cell1.innerHTML = "<input type=\"checkbox\" name=\"student-list\" id=\"checkbox"+item.id+"\" onClick=\"showSide("+item.id+");\">";
		cell1.setAttribute("width","4%");
		newNode.appendChild (cell1);
		
		studentArray.push(item.id);
		
		
		
		var cell2=document.createElement ("td");
		cell2.className = "text-positon";
		cell2.id = "no" + item.id;
		cell2.innerHTML = item.studentNo;
		cell2.setAttribute("width","10%");
		newNode.appendChild (cell2);
		
		var cell3=document.createElement ("td");
		cell3.className = "text-positon";
		cell3.id = "name" + item.id;
		cell3.innerHTML = item.name;
		cell3.setAttribute("width","12%");		
		newNode.appendChild (cell3);
		
		var cell4=document.createElement ("td");	
		cell4.className = "text-positon";
		cell4.id = "phone" + item.id;
		cell4.innerHTML = item.phone;
		cell4.setAttribute("width","12%");
		newNode.appendChild (cell4);
		
		var cell5=document.createElement ("td");		
		cell5.className = "text-positon";
		cell5.id = "email" + item.id;
		cell5.innerHTML = item.email;
		cell5.setAttribute("width","24%");
		newNode.appendChild (cell5);
		
		var cell6=document.createElement ("td");		
		cell6.innerHTML = item.remark;	
		cell6.className = "text-positon";
		cell6.id = "remark" + item.id;
		cell6.setAttribute("width","24%");		
		newNode.appendChild (cell6);
		
		var opeHtml = "<table><tr><td><input type=\"button\" class=\"btn btn-default btn-comment\" value=\"评价\" data-toggle=\"modal\" data-target=\"#commentStudent\" onClick=\"commentStu("+item.id+");\"></td>"+
		                         "<td><input type=\"button\" class=\"btn btn-default btn-comment\" value=\"编辑\" data-toggle=\"modal\" data-target=\"#editStudent\" onClick=\"editStu("+item.id+");\"></td>"+
		                         "<td><input type=\"button\" class=\"btn btn-default btn-comment\" value=\"删除\" onClick=\"deleteStu("+item.id+");\"></td></tr></table>";
		var cell7=document.createElement ("td");
		cell7.className = "text-positon";
		cell7.innerHTML = opeHtml;
		cell7.setAttribute("width","10%");
		newNode.appendChild (cell7);
				
		
		document.getElementById("list-table").appendChild (newNode); 
		
		if(isExist(item.id) == 1)
			document.getElementById("checkbox"+item.id).checked = true;
		else 
			k = k * 0;
	});
	if(k == 1)
		document.getElementById("selectAllBox").checked = true;
}

function listStudentByTag(objs){
	document.getElementById("list-table").innerHTML = "";
	document.getElementById("commentAndRemart").innerHTML = "辅导员和评价";
    
	//studentArray.splice(0,studentArray.length);
	var k = 1;
	studentArray.splice(0,studentArray.length);
	$.each(objs, function displayStudent(id,item){
		//console.log(id + " : " + item.id);
		var newNode = document.createElement ("tr");
		newNode.id = "tr" + item.student.id;
		
		
		var cell1=document.createElement ("td");
		
		
		var exist = false;
		for(var i = 0;i < studentArray.length;i++){
			if(studentArray[i] == item.student.id){
				exist = true;
				break;
			}				
		}
		if(!exist){
			studentArray.push(item.student.id);
			cell1.innerHTML = "<input type=\"checkbox\" name=\"student-list\" id=\"checkbox"+item.student.id+"\" onClick=\"showSide("+item.student.id+");\">";
		}
		else
			cell1.innerHTML = "";
		cell1.setAttribute("width","4%");
		newNode.appendChild (cell1);
		
		
		
		var cell2=document.createElement ("td");
		cell2.className = "text-positon";
		cell2.id = "no" + item.student.id;
		cell2.innerHTML = item.student.studentNo;
		cell2.setAttribute("width","10%");
		newNode.appendChild (cell2);
		
		var cell3=document.createElement ("td");
		cell3.className = "text-positon";
		cell3.id = "name" + item.student.id;
		cell3.innerHTML = item.student.name;
		cell3.setAttribute("width","12%");		
		newNode.appendChild (cell3);
		
		var cell4=document.createElement ("td");	
		cell4.className = "text-positon";
		cell4.id = "phone" + item.student.id;
		cell4.innerHTML = item.student.phone;
		cell4.setAttribute("width","12%");
		newNode.appendChild (cell4);
		
		var cell5=document.createElement ("td");		
		cell5.className = "text-positon";
		cell5.id = "email" + item.student.id;
		cell5.innerHTML = item.student.email;
		cell5.setAttribute("width","24%");
		newNode.appendChild (cell5);
		
		var cell12=document.createElement ("td");				
		
		cell12.innerHTML = "<input type=\"button\" class=\"btn btn-default btn-comment\" value=\"点击查看\" onClick=\"seeComment("+item.evaluation.id+");\" id=\"csee"+item.evaluation.id+"\">";
		cell12.className = "text-positon";
		newNode.appendChild (cell12);
		
		
		/*
		var opeHtml = "<table><tr><td><input type=\"button\" class=\"btn btn-default btn-comment\" value=\"评价\" data-toggle=\"modal\" data-target=\"#commentStudent\" onClick=\"commentStu("+item.student.id+");\"></td>"+
		                         "<td><input type=\"button\" class=\"btn btn-default btn-comment\" value=\"编辑\" data-toggle=\"modal\" data-target=\"#editStudent\" onClick=\"editStu("+item.student.id+");\"></td>"+
		                         "<td><input type=\"button\" class=\"btn btn-default btn-comment\" value=\"删除\" onClick=\"deleteStu("+item.student.id+");\"></td></tr></table>";
		*/
		var opeHtml = "<table><tr><td><input type=\"button\" class=\"btn btn-default btn-comment\" value=\"评价\" data-toggle=\"modal\" data-target=\"#commentStudent\" onClick=\"commentStu("+item.student.id+");\"></td>"+
        				"</tr></table>";
		var cell7=document.createElement ("td");
		cell7.className = "text-positon";
		cell7.innerHTML = opeHtml;
		cell7.setAttribute("width","10%");
		newNode.appendChild (cell7);
				
		
		document.getElementById("list-table").appendChild (newNode); 
		
		
		
		var newNode1 = document.createElement ("tr");
		newNode1.id = "ctr" + item.evaluation.id;
		newNode1.className = "hideTheDiv";
		var cell6=document.createElement ("td");		
		
		//cell6.className = "text-positon";
		//cell6.id = "remark" + item.student.id;
		cell6.setAttribute("width","24%");	
		
		
		var commentHtml = "<p style=\"word-break:break-all\">辅导员："+item.student.remark+"</p>"+
		                  "<p style=\"word-break:break-all\">评价主题："+item.evaluation.topic+"</p>";
		
		var temp = "";
		$.each(item.tags, function displayStudent(id,item){
			temp += item.tagName + "、";
		});
		if(temp.length != 0)
			commentHtml += "<p style=\"word-break:break-all\">标签："+temp.substring(0,temp.length - 1)+"</p>";
		else
			commentHtml += "标签：";
		commentHtml += "<p style=\"word-break:break-all\">评价内容："+item.evaluation.comment+"</p>";
		cell6.innerHTML = commentHtml;
		cell6.setAttribute("colspan","7");
		newNode1.appendChild (cell6);
		document.getElementById("list-table").appendChild (newNode1);
		
		
		
		
		if(isExist(item.id) == 1)
			document.getElementById("checkbox"+item.id).checked = true;
		else 
			k = k * 0;
	});
	if(k == 1)
		document.getElementById("selectAllBox").checked = true;
}

function addStudent(n){	
	var no = document.getElementById("addNo").value;
	var name = document.getElementById("addName").value;
	var phone = document.getElementById("addPhone").value;
	var email = document.getElementById("addMail").value;
	var remark = document.getElementById("addComment").value;
	
	if(no == "" || name == "" || phone == "" || email == ""){
		alert("信息不完整！");
		return;
	}
		
	if(n == 2)
	    $("#addStudent").modal("hide");
	//console.log("here");
	$.ajax({
		url : "ajax/student",
		data : {
			action : 'c',	
			oid:oid,
			studentNo:no,
			name:name,
			phone:phone,
			email:email,
			remark:remark,
		},
		async: false, 
		type: "post",
		dataType: "json",
		success: function(data, status, xHr){			
			if("OK" == data.Result){
				//alert("添加成功！");	
				new Toast({context:$('body'),message:"添加成功！"}).show();
				document.getElementById("addNo").value = "";
				document.getElementById("addName").value = "";
				document.getElementById("addPhone").value = "";
				document.getElementById("addMail").value = "";
				document.getElementById("addComment").value = "";
				
				var id = data.Record;
				var newNode = document.createElement ("tr");
				newNode.id = "tr" + id;
				
				var cell1=document.createElement ("td");	
				cell1.innerHTML = "<input type=\"checkbox\" name=\"student-list\" id=\"checkbox"+id+"\" onClick=\"showSide("+id+");\">";
				cell1.setAttribute("width","4%");
				newNode.appendChild (cell1);
				
				studentArray.push(id);
				//console.log(id);
				
				var cell2=document.createElement ("td");
				cell2.className = "text-positon";
				cell2.id = "no" + id;
				cell2.innerHTML = no;
				cell2.setAttribute("width","10%");
				newNode.appendChild (cell2);
				
				var cell3=document.createElement ("td");
				cell3.className = "text-positon";
				cell3.id = "name" + id;
				cell3.innerHTML = name;
				cell3.setAttribute("width","12%");		
				newNode.appendChild (cell3);
				
				var cell4=document.createElement ("td");	
				cell4.className = "text-positon";
				cell4.id = "phone" + id;
				cell4.innerHTML = phone;
				cell4.setAttribute("width","12%");
				newNode.appendChild (cell4);
				
				var cell5=document.createElement ("td");		
				cell5.className = "text-positon";
				cell5.id = "email" + id;
				cell5.innerHTML = email;
				cell5.setAttribute("width","24%");
				newNode.appendChild (cell5);
				
				var cell6=document.createElement ("td");		
				cell6.innerHTML = remark;	
				cell6.className = "text-positon";
				cell6.id = "remark" + id;
				cell6.setAttribute("width","24%");		
				newNode.appendChild (cell6);
				
				var opeHtml = "<table><tr><td><input type=\"button\" class=\"btn btn-default btn-comment\" value=\"评价\" data-toggle=\"modal\" data-target=\"#commentStudent\" onClick=\"commentStu("+id+");\"></td>"+
				                         "<td><input type=\"button\" class=\"btn btn-default btn-comment\" value=\"编辑\" data-toggle=\"modal\" data-target=\"#editStudent\" onClick=\"editStu("+id+");\"></td>"+
				                         "<td><input type=\"button\" class=\"btn btn-default btn-comment\" value=\"删除\" onClick=\"deleteStu("+id+");\"></td></tr></table>";
				var cell7=document.createElement ("td");
				cell7.className = "text-positon";
				cell7.innerHTML = opeHtml;
				cell7.setAttribute("width","10%");
				newNode.appendChild (cell7);
						
				
				document.getElementById("list-table").appendChild (newNode); 
				
				total++;
				document.getElementById("showTotal").innerHTML = "共" + total + "条";
			}else{
				//alert(data.Message);
				new Toast({context:$('body'),message:data.Message}).show();
				return;
			}
		},
		error: function(xHr, status, error){
			//alert("与服务器通信出错，请检查网络！");
			new Toast({context:$('body'),message:"与服务器通信出错，请检查网络！"}).show();
			return;
		}
	});
	//console.log("success");
}

var apk = false;
function deleteAll(){
	//console.log(selectedStuArray);
	
	apk = true;
	
	for(var i = 0;i < selectedStuArray.length;i++){
		deleteStu(selectedStuArray[i]);
	}
	selectedStuArray.splice(0,selectedStuArray.length);
	apk = false;
	document.getElementById("selectAllBox").checked = false;
	showSide(-1);
}

function deleteStu(id){
	if(!apk){
	    var res = confirm("确定删除?");
	
	    if(!res)
		    return;
	}
	
	$.ajax({
		url : "ajax/student",
		data : {
			action : 'd',
			sid:id,
			oid:oid,
		},
		async: false, 
		type: "post",
		dataType: "json",
		success: function(data, status, xHr){			
			if("OK" == data.Result){
				$("#tr" + id).addClass("hideTheDiv");	
				total--;
				document.getElementById("showTotal").innerHTML = "共" + total + "条";
				new Toast({context:$('body'),message:"删除成功！"}).show();
				var checkbox = "checkbox" + id;
				//console.log(checkbox);
				for(var i = 0;i < studentArray.length;i++){
					//console.log(studentArray[i]);
					if(checkbox == "checkbox"+studentArray[i]){
						studentArray.splice(i,1);
						break;
				    }
				}
				
				for(var i = 0;i < selectedStuArray.length;i++){
				    if(selectedStuArray[i] == id){
				    	//console.log("delete : " + id2no[id]);
				    	delete id2no[id];
						delete no2name[document.getElementById("no"+id).innerHTML];
				    	selectedStuArray.splice(i,1);
				    	//console.log(i + " : selectedStuArray : " + selectedStuArray);
				    	//is = false;
				    	showSide(-1);
				    	break;
				    }
			    }
			}else{
				//alert(data.Message);
				new Toast({context:$('body'),message:data.Message}).show();
			}
		},
		error: function(xHr, status, error){
			//alert("与服务器通信出错，请检查网络！");
			new Toast({context:$('body'),message:"与服务器通信出错，请检查网络！"}).show();
		}
	});
}


function cancleAdd(){
	document.getElementById("addNo").value = "";
	document.getElementById("addName").value = "";
	document.getElementById("addPhone").value = "";
	document.getElementById("addMail").value = "";
	document.getElementById("addComment").value = "";
	
	$("#addStudent").modal("hide");
}

function refresh(){
	$.ajax({
		url : "ajax/student",
		data : {
			action : 'r',
			oid: oid,
			page: currentPage,
			search:search,
			type:type,
		},
		async: false, 
		type: "post",
		dataType: "json",
		success: function(data, status, xHr){			
			if("OK" == data.Result){
				new Toast({context:$('body'),message:"刷新成功！"}).show();
				totalPage = data.TotalPageSize;
				var objs = eval(data.Students);
				/*
				if(objs.length == 0)
					return;			
				*/		
				listStudent(objs);
				total = data.Sum;
				document.getElementById("showTotal").innerHTML = "共" + total + "条";
				document.getElementById("selectAllBox").checked = false;
				document.getElementById("showPage").innerHTML = currentPage + "/" + totalPage;
			}else{
				//alert(data.Message);
				new Toast({context:$('body'),message:data.Message}).show();
			}
		},
		error: function(xHr, status, error){
			//alert("与服务器通信出错，请检查网络！");
			new Toast({context:$('body'),message:"与服务器通信出错，请检查网络！"}).show();
		}
	});
}

function prePage(){
	if(currentPage == 1)
		return;
	$.ajax({
		url : "ajax/student",
		data : {
			action : 'r',
			oid: oid,
			page: currentPage - 1,
			search:search,
			type:type,
			numPerPage:perPage,
		},
		async: false, 
		type: "post",
		dataType: "json",
		success: function(data, status, xHr){			
			if("OK" == data.Result){
				totalPage = data.TotalPageSize;
				var objs = eval(data.Students);
				/*
				if(objs.length == 0)
					return;			
				*/		
				listStudent(objs);
				currentPage--;
				total = data.Sum;
				document.getElementById("showTotal").innerHTML = "共" + total + "条";
				document.getElementById("showPage").innerHTML = currentPage + "/" + totalPage;
				document.getElementById("selectAllBox").checked = false;
			}else{
				//alert(data.Message);
				new Toast({context:$('body'),message:data.Message}).show();
			}
		},
		error: function(xHr, status, error){
			//alert("与服务器通信出错，请检查网络！");
			new Toast({context:$('body'),message:"与服务器通信出错，请检查网络！"}).show();
		}
	});
}

function nextPage(){
	if(currentPage == totalPage)
		return;
	$.ajax({
		url : "ajax/student",
		data : {
			action : 'r',
			oid: oid,
			page: currentPage + 1,
			search:search,
			type:type,
			numPerPage:perPage,
		},
		async: false, 
		type: "post",
		dataType: "json",
		success: function(data, status, xHr){			
			if("OK" == data.Result){
				totalPage = data.TotalPageSize;
				var objs = eval(data.Students);
				/*
				if(objs.length == 0)
					return;			
				*/		
				listStudent(objs);
				currentPage++;
				total = data.Sum;
				document.getElementById("showTotal").innerHTML = "共" + total + "条";
				document.getElementById("showPage").innerHTML = currentPage + "/" + totalPage;
				document.getElementById("selectAllBox").checked = false;
			}else{
				//alert(data.Message);
				new Toast({context:$('body'),message:data.Message}).show();
			}
		},
		error: function(xHr, status, error){
			//alert("与服务器通信出错，请检查网络！");
			new Toast({context:$('body'),message:"与服务器通信出错，请检查网络！"}).show();
		}
	});
}

function searchButton(){
	clearSelected();
	if(tempType == 3){
		var s = document.getElementById("searchTag");
		search = s.options[s.selectedIndex].value;
	}
	else{
		search = document.getElementById("searchText").value;
	}
	document.getElementById("showTotal").innerHTML = "共0条";
	console.log(search);
	console.log(tempType);
	if(search == "")
		return;
	if(tempType == 3 || tempType == 4){
		$.ajax({
			url : "ajax/student",
			data : {
				action : 'f',
				oid: oid,
				page: 1,
				search:search,
				type:tempType,
				numPerPage:perPage,
			},
			async: false, 
			type: "post",
			dataType: "json",
			success: function(data, status, xHr){			
				if("OK" == data.Result){
					type = tempType;				
					
									
					totalPage = data.TotalPageSize;
					var objs = eval(data.Data);
					
					listStudentByTag(objs);
					/*
					if(objs.length == 0)
						return;			
					*/		
					//listStudent(objs);		
					total = data.Sum;
					document.getElementById("showTotal").innerHTML = "共" + total + "条";
					document.getElementById("showPage").innerHTML = "1/" + totalPage;
					document.getElementById("selectAllBox").checked = false;
				}else{
					//alert(data.Message);
					document.getElementById("list-table").innerHTML = "";
					new Toast({context:$('body'),message:data.Message}).show();
				}
			},
			error: function(xHr, status, error){
				//alert("与服务器通信出错，请检查网络！");
				document.getElementById("list-table").innerHTML = "";
				new Toast({context:$('body'),message:"与服务器通信出错，请检查网络！"}).show();
			}
		});
		return;
	}
	//console.log(tempType);
	//console.log(search);
	//console.log(oid);
	$.ajax({
		url : "ajax/student",
		data : {
			action : 'r',
			oid: oid,
			page: 1,
			search:search,
			type:tempType,
			numPerPage:perPage,
		},
		async: false, 
		type: "post",
		dataType: "json",
		success: function(data, status, xHr){			
			if("OK" == data.Result){
				type = tempType;				
				
								
				totalPage = data.TotalPageSize;
				var objs = eval(data.Students);
				/*
				if(objs.length == 0)
					return;			
				*/		
				listStudent(objs);		
				total = data.Sum;
				document.getElementById("showTotal").innerHTML = "共" + total + "条";
				document.getElementById("showPage").innerHTML = "1/" + totalPage;
				document.getElementById("selectAllBox").checked = false;
			}else{
				//alert(data.Message);
				document.getElementById("list-table").innerHTML = "";
				new Toast({context:$('body'),message:data.Message}).show();
			}
		},
		error: function(xHr, status, error){
			//alert("与服务器通信出错，请检查网络！");
			document.getElementById("list-table").innerHTML = "";
			new Toast({context:$('body'),message:"与服务器通信出错，请检查网络！"}).show();
		}
	});
}

function changeSearchCategory(e){
	e = e||event;
    var t = e.target||e.srcElement
	document.getElementById("searchText").value = "";
	if(t.id == "category1"){		
		document.getElementById("searchCategory").innerHTML = "按姓名<span class=\"caret\"></span>";
		tempType = 1;
		
		document.getElementById("searchTag").style.display = "none";
		document.getElementById("searchText").style.display = "inline";
	}
	else if(t.id == "category2"){
		document.getElementById("searchCategory").innerHTML = "按学号<span class=\"caret\"></span>";
		tempType = 2;
		
		document.getElementById("searchTag").style.display = "none";
		document.getElementById("searchText").style.display = "inline";
	}
	else if(t.id == "category3"){
		document.getElementById("searchCategory").innerHTML = "按标签<span class=\"caret\"></span>";
		tempType = 3;
		
		$.ajax({
			url : "ajax/evaluate",
			data : {
				action : 't',			
			},
			async: false, 
			type: "post",
			dataType: "json",
			success: function(data, status, xHr){			
				if("OK" == data.Result){	
					document.getElementById("searchTag").style.display = "inline";
					document.getElementById("searchText").style.display = "none";
					
										
					var objs = eval(data.Data);
					document.getElementById("searchTag").options.length = 0;
					$.each(objs, function displayStudent(id,item){	
						//console.log(item.tagName);
						document.getElementById("searchTag").options.add(new Option(item.tagName,item.id));						
					});						
				}else{
					//alert(data.Message);
					new Toast({context:$('body'),message:data.Message}).show();
					//$("#commentStudent").modal("hide");
					return;
				}
			},
			error: function(xHr, status, error){
				//alert("与服务器通信出错，请检查网络！");
				new Toast({context:$('body'),message:"与服务器通信出错，请检查网络！"}).show();
				//$("#commentStudent").modal("hide");
				return;
			}
		});
	}
	else if(t.id == "category4"){
		document.getElementById("searchCategory").innerHTML = "按评价<span class=\"caret\"></span>";
		tempType = 4;
		
		document.getElementById("searchTag").style.display = "none";
		document.getElementById("searchText").style.display = "inline";
	}
}

function sendMsg(){
	var content = document.getElementById("msg-content").value;
	if(content == ""){
		new Toast({context:$('body'),message:"内容为空！"}).show();
		return;
	}
	console.log(studentArray);
	$.ajax({
		url : "message",
		data : {
			action : 's',
			content:content,
			personList:selectedStuArray,
		},
		async: false, 
		type: "post",
		dataType: "json",
		success: function(data, status, xHr){			
			if("OK" == data.Result){
				new Toast({context:$('body'),message:"发送成功！"}).show();
			}else{
				//alert(data.Message);
				new Toast({context:$('body'),message:data.Message}).show();
			}
		},
		error: function(xHr, status, error){
			//alert("与服务器通信出错，请检查网络！");
			new Toast({context:$('body'),message:"与服务器通信出错，请检查网络！"}).show();
		}
	});
}

function submintParameter(){
	$.ajax({
		url : "upload",
		data : {
			action : 'r',
			oid:oid,
		},
		async: false, 
		type: "post",
		dataType: "json",
		success: function(data, status, xHr){			
			if("OK" == data.Result){
				//new Toast({context:$('body'),message:"发送成功！"}).show();
			}else{
				//alert(data.Message);
				new Toast({context:$('body'),message:data.Message}).show();
			}
		},
		error: function(xHr, status, error){
			//alert("与服务器通信出错，请检查网络！");
			new Toast({context:$('body'),message:"与服务器通信出错，请检查网络！"}).show();
		}
	});
}

$(function(){
	$("#uploader").button({disabled: false});
	initUploader();
});

function initUploader(){
	var reg = /xlsx|xls/i;
	//console.log(oid);
	new AjaxUpload('uploader', {
		action : "upload?action=p",
		data : {
			
		},
		async: false, 
		onSubmit : function(file , ext){
			$("#uploader").button({disabled: true});
			this.disable();
			if(!reg.test(ext)){
				//alert("上传的文件必须是Excel文档！");
				new Toast({context:$('body'),message:"上传的文件必须是Excel文档！"}).show();
				$("#uploader").button({disabled: false});
				this.enable();
				return false;
			}
			submintParameter();
			$("#showWait").modal("show");
			return true;
		},
		onComplete: function(file, response){
			response = $.parseJSON(response);
			if("OK" === response.Result){
				//alert("导入成功！");
				new Toast({context:$('body'),message:"导入成功！"}).show();
				$("#showWait").modal("hide");
				/*
				alert("请在左边选择好要上传的分类！", "warn");
				setTimeout(function(){
					var nodes = ztreeObj.getSelectedNodes();
                	var nodeId = nodes[0].id, nodeName = nodes[0].name;
                	$("#up-cn").attr("cid", nodeId).text(nodeName);
                	$("#confirm").dialog('open');
				}, 2000);
				*/
				$.ajax({
					url : "ajax/student",
					data : {
						action : 'r',
						oid: oid,
						page: 1,
						search:"",
						type:-1,
						numPerPage:perPage,
					},
					async: false, 
					type: "post",
					dataType: "json",
					success: function(data, status, xHr){			
						if("OK" == data.Result){
							totalPage = data.TotalPageSize;
							var objs = eval(data.Students);
							document.getElementById("selectAllBox").checked = false;
							/*
							if(objs.length == 0)
								return;			
							*/		
							//oid = node.id;
							listStudent(objs);
							document.getElementById("showPage").innerHTML = "1/" + totalPage;
						}else{
							//alert(data.Message);
							new Toast({context:$('body'),message:data.Message}).show();
						}
					},
					error: function(xHr, status, error){
						//alert("与服务器通信出错，请检查网络！");
						new Toast({context:$('body'),message:"与服务器通信出错，请检查网络！"}).show();
					}
				});
			}else{
				//alert("上传失败，请重试！", "error");
				new Toast({context:$('body'),message:"上传失败，请重试！"}).show();
				$("#showWait").modal("hide");
			}
			$("#uploader").button({disabled: false});
			this.enable();
		}
	});
}
var no2name = new Array();
var id2no = new Array();
function selectAll(){
	//console.log(studentArray.length);
	//console.log(studentArray);
	//console.log(selectedStuArray);
	if(document.getElementById("selectAllBox").checked == true){
	    for(var i = 0;i < studentArray.length;i++){
		    document.getElementById("checkbox"+studentArray[i]).checked = true;
		    if(isExist(studentArray[i]) == 1)
		    	continue;
		    selectedStuArray.push(studentArray[i]);
		    id2no[studentArray[i]] = document.getElementById("no"+studentArray[i]).innerHTML;
		    no2name[document.getElementById("no"+studentArray[i]).innerHTML] = document.getElementById("name"+studentArray[i]).innerHTML;
	    }
    }
	else{
		for(var i = 0;i < studentArray.length;i++){
			document.getElementById("checkbox"+studentArray[i]).checked = false;
			for(var j = 0;j < selectedStuArray.length;j++){
				if(selectedStuArray[j] == studentArray[i]){
					selectedStuArray.splice(j,1);
					delete id2no[studentArray[i]];
					delete no2name[document.getElementById("no"+studentArray[i]).innerHTML];
					break;
				}
			}
		}
	}
	//console.log(selectedStuArray);
	showSide(-1);
}

function isExist(id){
	for(var i = 0;i < selectedStuArray.length;i++){
		if(id == selectedStuArray[i])
			return 1;
	}
	return 0;
}

function showSide(aid){
	//selectedStuArray.splice(0,selectedStuArray.length);
	//console.log(selectedStuArray);
	var tree = document.getElementById("tree");
	//tree.setAttribute("max-height","50%");
	//tree.setAttribute("overflow","auto");
	
	var is = true;
	//if(aid != -1){
	    for(i = 0;i < selectedStuArray.length;i++){
		    if(selectedStuArray[i] == aid && document.getElementById("checkbox"+aid).checked == false){
		    	//console.log("delete : " + aid);
		    	delete id2no[aid];
				delete no2name[document.getElementById("no"+aid).innerHTML];
		    	selectedStuArray.splice(i,1);
		    	console.log(i + " : selectedStuArray : " + selectedStuArray);
		    	is = false;
		    	break;
		    }
	    }
	    
	    if(is && aid != -1){
	    	//console.log("push : " + aid);
		    selectedStuArray.push(aid);
		    id2no[aid] = document.getElementById("no"+aid).innerHTML;
		    no2name[document.getElementById("no"+aid).innerHTML] = document.getElementById("name"+aid).innerHTML;
	    }
	//}
	//console.log("selectedStuArray : "+selectedStuArray);
	//console.log("id2no : "+id2no);
	//console.log("no2name : "+no2name);
	document.getElementById("selectedStudent").innerHTML = "";
	for(var i = 0;i < selectedStuArray.length;i++){		
		//if(document.getElementById("checkbox"+studentArray[i]).checked == true){
			var ssid = selectedStuArray[i];
			//selectedStuArray.push(ssid);
			
			var newNode = document.createElement ("tr");
			newNode.id = "str" + ssid;
			newNode.className = "navediv"
			
			var cell2=document.createElement ("td");
			cell2.className = "text-positon";
			cell2.id = "sno" + ssid;
			//console.log(ssid);
			cell2.innerHTML = id2no[ssid];
			cell2.setAttribute("width","30%");
			newNode.appendChild (cell2);
			
			var cell3=document.createElement ("td");
			cell3.className = "text-positon";
			cell3.id = "sname" + ssid;
			cell3.innerHTML = no2name[id2no[ssid]];
			cell3.setAttribute("width","30%");		
			newNode.appendChild (cell3);						
			
			var opeHtml = "<input type=\"button\" class=\"btn btn-default btn-comment\" value=\"取消选择\" onClick=\"deleteStuFromSelected("+ssid+");\" class=\"aaaa\">";
			var cell7=document.createElement ("td");
			cell7.className = "text-positon";
			cell7.innerHTML = opeHtml;
			cell7.setAttribute("width","40%");
			newNode.appendChild (cell7);
					
			
			document.getElementById("selectedStudent").appendChild (newNode);
		//};
	}
	//console.log(selectedStuArray);	
	document.getElementById("totalSelected").innerHTML = selectedStuArray.length;
	if(selectedStuArray.length != 0){
		$("#optionSide").removeClass("hideTheDiv");	
		tree.style.cssText = "max-height:33%; overflow:auto";
	}
	else{
		$("#optionSide").addClass("hideTheDiv");
		tree.style.cssText = "";
	}
	//console.log(selectedStuArray);
}

function deleteStuFromSelected(selectedId){
	$("#str" + selectedId).addClass("hideTheDiv");
	for(var i = 0;i < selectedStuArray.length;i++){
		if(selectedStuArray[i] == selectedId)
			selectedStuArray.splice(i,1);
	}
	document.getElementById("checkbox"+selectedId).checked = false;
	//console.log(selectedStuArray);
	//showSide(-1);
	if(selectedStuArray.length == 0)
		$("#optionSide").addClass("hideTheDiv");
}

function changePer(){	
	var a = document.getElementById("perPage1");
	perPage = a.options[a.selectedIndex].value;
	//console.log(perPage);
	if(!ini)
		return;
	$.ajax({
		url : "ajax/student",
		data : {
			action : 'r',
			oid: oid,
			page: 1,
			search:search,
			type:type,
			numPerPage:perPage,
		},
		async: false, 
		type: "post",
		dataType: "json",
		success: function(data, status, xHr){			
			if("OK" == data.Result){
				totalPage = data.TotalPageSize;
				var objs = eval(data.Students);
				currentPage = 1;
				/*
				if(objs.length == 0)
					return;			
				*/		
				listStudent(objs);
				document.getElementById("showPage").innerHTML = currentPage + "/" + totalPage;
				document.getElementById("selectAllBox").checked = false;
			}else{
				//alert(data.Message);
				new Toast({context:$('body'),message:data.Message}).show();
			}
		},
		error: function(xHr, status, error){
			//alert("与服务器通信出错，请检查网络！");
			new Toast({context:$('body'),message:"与服务器通信出错，请检查网络！"}).show();
		}
	});
}

function sortByno(){
	selectedStuArray.sort(function(a,b){
		var k1 = id2no[a];
		var k2 = id2no[b];
		
		return k1 > k2?1:-1;
	});
	showSide(-1);
}

function sortByname(){
	selectedStuArray.sort(function(a,b){
		var k1 = no2name[id2no[a]];
		var k2 = no2name[id2no[b]];
		//console.log(k1 + " : " + k2);
		//console.log(k1.localeCompare(k2));
		return k1.localeCompare(k2);
	});
	showSide(-1);
}

function clearSelected(){
	selectedStuArray.splice(0,selectedStuArray.length);
	document.getElementById("selectAllBox").checked = false;
	for(var i = 0;i < studentArray.length;i++){
		document.getElementById("checkbox"+studentArray[i]).checked = false;
	}
	showSide(-1);
}

function saveCommentAll(){
	var topic = document.getElementById("commentTopicHis").value;
	var comment = document.getElementById("commentDetailHis").value;
		
	var objs = document.getElementById("s1h");
	var y = objs.options[objs.selectedIndex].value;
	
	var objs1 = document.getElementById("s2h");
	var m = objs1.options[objs1.selectedIndex].value;
	
	var objs2 = document.getElementById("s3h");
	var d = objs2.options[objs2.selectedIndex].value;
	
	period = y+"-"+m+"-"+d;
	//var tag = document.getElementById("commentTag");
	//var tagName = tag.options[tag.options.selectedIndex].value;
	var check = document.getElementsByName("tagCheckboxHis");
	var len = check.length;
	var array = new Array();
	for(var i = 0;i < len;i++){
		if(check[i].checked){
			array.push(check[i].value);
		}
	}
	//console.log(array);
	var vis = document.getElementById("commentVisibilityHis");
	var visibility = vis.options[vis.options.selectedIndex].value;
	for(var i = 0;i < selectedStuArray.length;i++){
		
		$.ajax({
			url : "ajax/evaluate",
			data : {
				action : 'c',
				sid:selectedStuArray[i],
				tags:array,
				period:period,
				topic:topic,
				comment:comment,
				visibility:visibility,
				oid:oid,
			},
			async: false, 
			type: "post",
			dataType: "json",
			success: function(data, status, xHr){			
				if("OK" == data.Result){
					new Toast({context:$('body'),message:"评价成功！"}).show();
					document.getElementById("commentTopicHis").value = "";
					document.getElementById("commentDetailHis").value = "";
					document.getElementById("commentPeriodHis").value = "";				
				}else{
					//alert(data.Message);
					new Toast({context:$('body'),message:data.Message}).show();
				}
			},
			error: function(xHr, status, error){
				//alert("与服务器通信出错，请检查网络！");
				new Toast({context:$('body'),message:"与服务器通信出错，请检查网络！"}).show();
			}
		});
	}
}

function saveComment(){
	var topic = document.getElementById("commentTopic").value;
	var comment = document.getElementById("commentDetail").value;
	//var period = document.getElementById("commentPeriod").value;
	
	var objs = document.getElementById("s1");
	var y = objs.options[objs.selectedIndex].value;
	
	var objs1 = document.getElementById("s2");
	var m = objs1.options[objs1.selectedIndex].value;
	
	var objs2 = document.getElementById("s3");
	var d = objs2.options[objs2.selectedIndex].value;
	
	period = y+"-"+m+"-"+d;
	//console.log(period);
	//var tag = document.getElementById("commentTag");
	//var tagName = tag.options[tag.options.selectedIndex].value;
	var check = document.getElementsByName("tagCheckbox");
	var len = check.length;
	var array = new Array();
	for(var i = 0;i < len;i++){
		if(check[i].checked){
			array.push(check[i].value);
		}
	}
	//console.log(array);
	var vis = document.getElementById("commentVisibility");
	var visibility = vis.options[vis.options.selectedIndex].value;
	$.ajax({
		url : "ajax/evaluate",
		data : {
			action : 'c',
			sid:stuId,
			tags:array,
			period:period,
			topic:topic,
			comment:comment,
			visibility:visibility,
			oid:oid,
		},
		async: false, 
		type: "post",
		dataType: "json",
		success: function(data, status, xHr){			
			if("OK" == data.Result){
				new Toast({context:$('body'),message:"评价成功！"}).show();
				document.getElementById("commentTopic").value = "";
				document.getElementById("commentDetail").value = "";
				//document.getElementById("commentPeriod").value = "";				
				commentStu(stuId);
			}else{
				//alert(data.Message);
				new Toast({context:$('body'),message:data.Message}).show();
			}
		},
		error: function(xHr, status, error){
			//alert("与服务器通信出错，请检查网络！");
			new Toast({context:$('body'),message:"与服务器通信出错，请检查网络！"}).show();
		}
	});
}

function commentStuAll(){
	$.ajax({
		url : "ajax/evaluate",
		data : {
			action : 't',			
		},
		async: false, 
		type: "post",
		dataType: "json",
		success: function(data, status, xHr){			
			if("OK" == data.Result){				
				var objs = eval(data.Data);
				document.getElementById("commentTagHis").innerHTML = "";
				$.each(objs, function displayStudent(id,item){					
					//document.getElementById("commentTag").options.add(new Option(item.tagName,item.id));
					var newNode = document.createElement ("label");
					newNode.className = "checkbox-inline";
					newNode.innerHTML = "<input type=\"checkbox\" value=\""+item.id+"\" name=\"tagCheckboxHis\"> "+item.tagName;										
					document.getElementById("commentTagHis").appendChild (newNode);
				});	
				var check = document.getElementsByName("commentTagHis");
				var len = check.length;
				for(var i = 0;i < len;i++){
					check[i].checked = false;
				}
			}else{
				//alert(data.Message);
				new Toast({context:$('body'),message:data.Message}).show();
				$("#commentStudentHis").modal("hide");
				return;
			}
		},
		error: function(xHr, status, error){
			//alert("与服务器通信出错，请检查网络！");
			new Toast({context:$('body'),message:"与服务器通信出错，请检查网络！"}).show();
			$("#commentStudentHis").modal("hide");
			return;
		}
	});
}

function moveOrg(){
	//console.log(noid);
	if(noid == -1){
		new Toast({context:$('body'),message:"未选择新组织！"}).show();
	}
	$("#move").modal("hide");
	$("#moveWait").modal("show");
	$.ajax({
		url : "ajax/student",
		data : {
			action : 'a',
			noid:noid,
			sida:selectedStuArray,
		},
		async: false, 
		type: "post",
		dataType: "json",
		success: function(data, status, xHr){			
			if("OK" == data.Result){				
				new Toast({context:$('body'),message:"移动成功！"}).show();
				$("#moveWait").modal("hide");
				$.ajax({
					url : "ajax/student",
					data : {
						action : 'r',
						oid: oid,
						page: currentPage,
						search:search,
						type:type,
						numPerPage:perPage,
					},
					async: false, 
					type: "post",
					dataType: "json",
					success: function(data, status, xHr){			
						if("OK" == data.Result){
							totalPage = data.TotalPageSize;
							var objs = eval(data.Students);
							total = data.Sum;
							document.getElementById("showTotal").innerHTML = "共" + total + "条";
							/*
							if(objs.length == 0)
								return;			
							*/		
							listStudent(objs);
							//currentPage--;
							document.getElementById("showPage").innerHTML = currentPage + "/" + totalPage;
							//document.getElementById("selectAllBox").checked = false;
						}else{
							//alert(data.Message);
							new Toast({context:$('body'),message:data.Message}).show();
						}
					},
					error: function(xHr, status, error){
						//alert("与服务器通信出错，请检查网络！");
						
						new Toast({context:$('body'),message:"与服务器通信出错，请检查网络！"}).show();
					}
				});
			}else{
				//alert(data.Message);
				$("#moveWait").modal("hide");
				new Toast({context:$('body'),message:data.Message}).show();
				//$("#commentStudentHis").modal("hide");
				return;
			}
		},
		error: function(xHr, status, error){
			//alert("与服务器通信出错，请检查网络！");
			$("#moveWait").modal("hide");
			new Toast({context:$('body'),message:"与服务器通信出错，请检查网络！"}).show();
			//$("#commentStudentHis").modal("hide");
			return;
		}
	});
}

function showTree(){
	noid = -1;
	$('#moveTree').moveT({data: getTree()});
	console.log(getTree());
}

function sendMail(){
	var html = editor.html();
	var title = document.getElementById("mailTopic").value;
	console.log(title);
	console.log(html);
	$.ajax({
		url: "mail",
		data : {
			action : 'c',
			title:title,
			content:html,
			ids:selectedStuArray,
		},
		async: false,
		type: "post",
		dataType: "json",
		success: function(data){
			if("OK" == data.Result){
				new Toast({context:$('body'),message:"发送成功！"}).show();
			}else{
				//alert(data.Message);
				new Toast({context:$('body'),message:data.Message}).show();
				return;
			}
		},
		error: function(xHr, status, err){
			//alert(err);
			new Toast({context:$('body'),message:err}).show();
			return;
		}
	});
}

function changeDate(){
	var myDate = new Date();
	var str = myDate.getFullYear() + "-" + (myDate.getMonth()+1) + "-" + myDate.getDate();
	
	//console.log(str);
	console.log(Calendar('time'));
}

function addTag(){
	var tagName = document.getElementById("addTagName").value;
	//console.log(tagName);
	
	
	$.ajax({
		url: "ajax/evaluate",
		data : {
			action : 'a',
			tagName:tagName,
		},
		async: false,
		type: "post",
		dataType: "json",
		success: function(data){
			if("OK" == data.Result){
				document.getElementById("addTagName").value = "";
				new Toast({context:$('body'),message:"添加成功！"}).show();
				$.ajax({
					url : "ajax/evaluate",
					data : {
						action : 't',			
					},
					async: false, 
					type: "post",
					dataType: "json",
					success: function(data, status, xHr){			
						if("OK" == data.Result){				
							var objs = eval(data.Data);
							document.getElementById("commentTag").innerHTML = "";
							$.each(objs, function displayStudent(id,item){					
								//document.getElementById("commentTag").options.add(new Option(item.tagName,item.id));
								var newNode = document.createElement ("label");
								newNode.className = "checkbox-inline";
								newNode.innerHTML = "<input type=\"checkbox\" value=\""+item.id+"\" name=\"tagCheckbox\"> "+item.tagName;										
								document.getElementById("commentTag").appendChild (newNode);
							});	
							
							var check = document.getElementsByName("tagCheckbox");
							var len = check.length;
							check[len - 1].checked = true;
							console.log(check[len - 1].checked);
							console.log(len);
							
							var check = document.getElementsByName("tagCheckbox");
							var len = check.length;
							for(var i = 0;i < len;i++){
								check[i].checked = false;
							}
						}else{
							//alert(data.Message);
							new Toast({context:$('body'),message:data.Message}).show();
							//$("#commentStudent").modal("hide");
							return;
						}
					},
					error: function(xHr, status, error){
						//alert("与服务器通信出错，请检查网络！");
						new Toast({context:$('body'),message:"与服务器通信出错，请检查网络！"}).show();
						//$("#commentStudent").modal("hide");
						return;
					}
				});	
			}else{
				//alert(data.Message);
				new Toast({context:$('body'),message:data.Message}).show();
				return;
			}
		},
		error: function(xHr, status, err){
			//alert(err);
			new Toast({context:$('body'),message:err}).show();
			return;
		}
	});
}

function addTagAll(){
	var tagName = document.getElementById("addTagNameHis").value;
	//console.log(tagName);
	
	
	$.ajax({
		url: "ajax/evaluate",
		data : {
			action : 'a',
			tagName:tagName,
		},
		async: false,
		type: "post",
		dataType: "json",
		success: function(data){
			if("OK" == data.Result){
				document.getElementById("addTagNameHis").value = "";
				new Toast({context:$('body'),message:"添加成功！"}).show();
				$.ajax({
					url : "ajax/evaluate",
					data : {
						action : 't',			
					},
					async: false, 
					type: "post",
					dataType: "json",
					success: function(data, status, xHr){			
						if("OK" == data.Result){				
							var objs = eval(data.Data);
							document.getElementById("commentTagHis").innerHTML = "";
							$.each(objs, function displayStudent(id,item){					
								//document.getElementById("commentTag").options.add(new Option(item.tagName,item.id));
								var newNode = document.createElement ("label");
								newNode.className = "checkbox-inline";
								newNode.innerHTML = "<input type=\"checkbox\" value=\""+item.id+"\" name=\"tagCheckboxHis\"> "+item.tagName;										
								document.getElementById("commentTagHis").appendChild (newNode);
							});	
							var check = document.getElementsByName("tagCheckboxHis");
							//var len = check.length;
							check[check.length - 1].checked = true;
							
							var check = document.getElementsByName("tagCheckboxHis");
							var len = check.length;
							for(var i = 0;i < len;i++){
								check[i].checked = false;
							}
						}else{
							//alert(data.Message);
							new Toast({context:$('body'),message:data.Message}).show();
							//$("#commentStudent").modal("hide");
							return;
						}
					},
					error: function(xHr, status, error){
						//alert("与服务器通信出错，请检查网络！");
						new Toast({context:$('body'),message:"与服务器通信出错，请检查网络！"}).show();
						//$("#commentStudent").modal("hide");
						return;
					}
				});	
			}else{
				//alert(data.Message);
				new Toast({context:$('body'),message:data.Message}).show();
				return;
			}
		},
		error: function(xHr, status, err){
			//alert(err);
			new Toast({context:$('body'),message:err}).show();
			return;
		}
	});
}

var inik = false;
var editor;
function iniEmail(){
	//console.log(inik);
	if(inik)
		return;
	//document.getElementById('editor_id').innerHTML="";
	//document.getElementById("editor_id").innerHTML = "";
	//console.log("here");
	editor = $('#editor_id').wangEditor();	
	//console.log("here");
	inik =true;
	
}

/*
var user = [{username:"a",name:"ak",phone:[{tel:"13788930000"},{tel:"13788930000"}],email:[{mail:"12302010000@fudan.edu.cn"},{mail:"12302010000@fudan.edu.cn"}],id:'1',status:"1"},
            {username:"a",name:"ak",phone:[{tel:"13788930000"},{tel:"13788930000"}],email:[{mail:"12302010000@fudan.edu.cn"},{mail:"12302010000@fudan.edu.cn"}],id:'2',status:"1"},
            {username:"a",name:"ak",phone:[{tel:"13788930000"},{tel:"13788930000"}],email:[{mail:"12302010000@fudan.edu.cn"},{mail:"12302010000@fudan.edu.cn"}],id:'3',status:"1"}]
*/

function manageUser(){
	$.ajax({
		url : "user",
		data : {
			action : 'r',			
		},
		async: false, 
		type: "post",
		dataType: "json",
		success: function(data, status, xHr){			
			if("OK" == data.Result){				
				var objs = eval(data.Users);
				listAllUser(objs);
			}else{
				//alert(data.Message);
				new Toast({context:$('body'),message:data.Message}).show();
				//$("#commentStudent").modal("hide");
				return;
			}
		},
		error: function(xHr, status, error){
			//alert("与服务器通信出错，请检查网络！");
			new Toast({context:$('body'),message:"与服务器通信出错，请检查网络！"}).show();
			//$("#commentStudent").modal("hide");
			return;
		}
	});	
	
}

function listAllUser(user){
	document.getElementById("userTable").innerHTML = "";
	$.each(user, function displayStudent(id,item){
		var newNode = document.createElement ("tr");		
		
		var cell2=document.createElement ("td");
		cell2.className = "text-positon";		
		cell2.innerHTML = item.username;		
		newNode.appendChild (cell2);
		
		var cell3=document.createElement ("td");
		cell3.className = "text-positon";		
		cell3.innerHTML = item.name;		
		newNode.appendChild (cell3);
		
		var cell4=document.createElement ("td");
		cell4.className = "text-positon";		
		//cell4.innerHTML = item.phone;		
		var tempH = "";
		//console.log(item.phone);
		//console.log(item.phone);
		$.each(item.phone, function displayStudent(id,item){
			tempH += "<p>"+item+"</p>";
		});
		
		cell4.innerHTML = tempH;
		newNode.appendChild (cell4);
		
		var cell5=document.createElement ("td");
		cell5.className = "text-positon";		
		var tempE = "";
		
		$.each(item.email, function displayStudent(id,item){
			tempE += "<p>"+item+"</p>";
		});
		
		cell5.innerHTML = tempE;		
		newNode.appendChild (cell5);
		
		/*0 means that the user is invalid*/
		var sta = item.isAuthenticated;
		var opeHtml = "";
		if(sta == 0){
			opeHtml = "<table><tr><td><input type=\"button\" class=\"btn btn-default userButton\" value=\"通过\" onClick=\"stopUser("+item.id+");\" id=\"aaa"+item.id+"\"></td><td>";
		}else
			opeHtml = "<table><tr><td><input type=\"button\" class=\"btn btn-default userButton\" value=\"停用\" onClick=\"stopUser("+item.id+");\" id=\"aaa"+item.id+"\"></td><td>";
		opeHtml += "<input type=\"button\" class=\"btn btn-default userButton\" value=\"权限\" onClick=\"editRights("+item.id+");\" id=\"eee"+item.id+"\"></td><tr></table>";
		var cell7=document.createElement ("td");
		cell7.className = "text-positon";
		cell7.innerHTML = opeHtml;		
		newNode.appendChild (cell7);
				
		
		document.getElementById("userTable").appendChild (newNode); 
		
		var newNode1 = document.createElement ("tr");				
		var cell12=document.createElement ("td");
		cell12.setAttribute("colspan","5");
		cell12.innerHTML = "<div id=\"orgTree"+item.id+"\"></div>";
		newNode1.appendChild (cell12);
		newNode1.id = "treeTr" + item.id;
		newNode1.className = "hideTheDiv";
		
		document.getElementById("userTable").appendChild (newNode1); 
	});
}

function stopUser(id){
	var t = document.getElementById("aaa"+id);
	if(t.value == "通过"){
		//console.log("pass");
		
		//t.removeClass("btn-default");
		//t.addClass("btn-success");
		$.ajax({
			url : "user",
			data : {
				action : 'm',	
				uid:id,
				type:1,
			},
			async: false, 
			type: "post",
			dataType: "json",
			success: function(data, status, xHr){			
				if("OK" == data.Result){				
					t.value = "停用";
				}else{
					//alert(data.Message);
					new Toast({context:$('body'),message:data.Message}).show();
					//$("#commentStudent").modal("hide");
					return;
				}
			},
			error: function(xHr, status, error){
				//alert("与服务器通信出错，请检查网络！");
				new Toast({context:$('body'),message:"与服务器通信出错，请检查网络！"}).show();
				//$("#commentStudent").modal("hide");
				return;
			}
		});	
	}
	else{
		//console.log("stop");
		$.ajax({
			url : "user",
			data : {
				action : 'm',	
				uid:id,
				type:0,
			},
			async: false, 
			type: "post",
			dataType: "json",
			success: function(data, status, xHr){			
				if("OK" == data.Result){				
					t.value = "通过";
				}else{
					//alert(data.Message);
					new Toast({context:$('body'),message:data.Message}).show();
					//$("#commentStudent").modal("hide");
					return;
				}
			},
			error: function(xHr, status, error){
				//alert("与服务器通信出错，请检查网络！");
				new Toast({context:$('body'),message:"与服务器通信出错，请检查网络！"}).show();
				//$("#commentStudent").modal("hide");
				return;
			}
		});	
	}
}

function seeComment(id){
	//console.log(id);
	var t = document.getElementById("csee"+id);
	if(t.value == "点击查看"){
		document.getElementById("ctr"+id).className = "";
		t.value = "点击收起";
	}
	else{
		document.getElementById("ctr"+id).className = "hideTheDiv";
		t.value = "点击查看";
	}
}

function userInfo(){
	$.ajax({
		url : "user",
		data : {
			action : 's',	
		},
		async: false, 
		type: "post",
		dataType: "json",
		success: function(data, status, xHr){			
			if("OK" == data.Result){	
				var objs = eval(data.User);
				listUserInfo(objs);
			}else{
				//alert(data.Message);
				new Toast({context:$('body'),message:data.Message}).show();
				//$("#commentStudent").modal("hide");
				return;
			}
		},
		error: function(xHr, status, error){
			//alert("与服务器通信出错，请检查网络！");
			new Toast({context:$('body'),message:"与服务器通信出错，请检查网络！"}).show();
			//$("#commentStudent").modal("hide");
			return;
		}
	});	
}
var phoneA = new Array();
var mailA = new Array();
var password = "";
function listUserInfo(objs){
	document.getElementById("infoTable").innerHTML = "";
	mailA.splice(0,mailA.length);
	phoneA.splice(0,phoneA.length);
	//console.log(objs);
	//$.each(objs, function displayStudent(id,item){
		var newNode = document.createElement ("tr");		
		
		var cell2=document.createElement ("td");
		cell2.className = "text-positon";		
		cell2.innerHTML = objs.username;		
		newNode.appendChild (cell2);
		
		var cell3=document.createElement ("td");
		cell3.className = "text-positon";		
		cell3.innerHTML = objs.name;		
		newNode.appendChild (cell3);
		
		var cell6=document.createElement ("td");
		cell6.className = "text-positon";		
		cell6.innerHTML = "password";	                                 /*need to be changed*/
		password = "pass";
		cell6.id = "std"+objs.id;
		newNode.appendChild (cell6);
		
		var cell4=document.createElement ("td");
		//cell4.className = "text-positon";		
		//cell4.innerHTML = item.phone;		
		var tempH = "";
		//console.log(item.phone);
		
		$.each(objs.phone, function displayStudent(id,item){
			if(id == 0)
			    tempH += "<p>"+item+"</p>";
			else{
				tempH += "<table><td>"+item+"</td><td><input type=\"button\" class=\"btn btn-default btn-comment\" value=\"删除\" onClick=\"deletePhone("+id+");\"></td><td><input type=\"button\" class=\"btn btn-default btn-comment\" value=\"设为默认\" onClick=\"phoneDefault("+id+");\"></td>";
			}
			phoneA.push(item);
		});
		
		cell4.innerHTML = tempH;
		newNode.appendChild (cell4);
		
		var cell5=document.createElement ("td");
		//cell5.className = "text-positon";		
		var tempE = "";
		
		$.each(objs.email, function displayStudent(id,item){
			//tempE += "<p>"+item+"</p>";
			if(id == 0)
				tempE += "<p>"+item+"</p>";
			else{
				tempE += "<table><td>"+item+"</td><td><input type=\"button\" class=\"btn btn-default btn-comment\" value=\"删除\" onClick=\"deleteMail("+id+");\"></td><td><input type=\"button\" class=\"btn btn-default btn-comment\" value=\"设为默认\" onClick=\"mailDefault("+id+");\"></td>";
			}
			mailA.push(item);
		});
		
		cell5.innerHTML = tempE;		
		newNode.appendChild (cell5);
		
		/*0 means that the user is invalid*/
		/*
		var sta = item.isAuthenticated;
		var opeHtml = "";
		if(sta == 0){
			opeHtml = "<input type=\"button\" class=\"btn btn-default userButton\" value=\"通过\" onClick=\"stopUser("+item.id+");\" id=\"aaa"+item.id+"\">";
		}else
			opeHtml = "<input type=\"button\" class=\"btn btn-default userButton\" value=\"停用\" onClick=\"stopUser("+item.id+");\" id=\"aaa"+item.id+"\">";
		
		var cell7=document.createElement ("td");
		cell7.className = "text-positon";
		cell7.innerHTML = opeHtml;		
		newNode.appendChild (cell7);
		*/		
		
		document.getElementById("infoTable").appendChild (newNode); 
	//});
}
var ph = false;
var ma = false;
function updateUser(){
	console.log(phoneA);
	console.log(mailA);
	$.ajax({
		url : "user",
		data : {
			action : 'u',
			emails:mailA,
			phones:phoneA,
			password:password,
		},
		async: false, 
		type: "post",
		dataType: "json",
		success: function(data, status, xHr){			
			if("OK" == data.Result){	
				new Toast({context:$('body'),message:"操作成功！"}).show();
				userInfo();
			}else{
				//alert(data.Message);
				new Toast({context:$('body'),message:data.Message}).show();
				//$("#commentStudent").modal("hide");
				if(ph)
					phoneA.pop();
				if(ma)
					mailA.pop();
				return;
			}
		},
		error: function(xHr, status, error){
			//alert("与服务器通信出错，请检查网络！");
			new Toast({context:$('body'),message:"与服务器通信出错，请检查网络！"}).show();
			//$("#commentStudent").modal("hide");
			if(ph)
				phoneA.pop();
			if(ma)
				mailA.pop();
			return;
		}
	});	
}

function changePsw(){
	var p1 = prompt("输入新密码");
	var p2 = prompt("再输入一次");
	
	if(p1 != p2){
		alert("输入的两次密码不一致！");
		return;
	}
	
}

function addPhone(){
	var phone = prompt("输入电话号码");
	if(phone == "" || phone == null)
		return;
	phoneA.push(phone);
	console.log(phone);
	ph = true;
	updateUser();
	ph = false;
}

function addMail(){
	var mail = prompt("输入邮箱");
	if(mail == "" || mail == null)
		return;
	mailA.push(mail);
	ma = true;
	updateUser();
	ma = false;
}

function deletePhone(id){
	phoneA.splice(id,1);
	updateUser();
}

function deleteMail(id){
	mailA.splice(id,1);
	updateUser();
}

function phoneDefault(id){
	var temp = phoneA[id];
	phoneA.splice(id,1);
	phoneA.unshift(temp);
	updateUser();
}

function mailDefault(id){
	var temp = mailA[id];
	mailA.splice(id,1);
	mailA.unshift(temp);
	updateUser();
}

function editRights(id){
	var obj = document.getElementById("eee"+id);
	console.log(obj.value == "权限");
	if(obj.value == "权限"){
		obj.value = "保存";
		document.getElementById("treeTr"+id).className = "";
		showT(id);
	}
	else{
		obj.value = "权限";
		document.getElementById("treeTr"+id).className = "hideTheDiv";
		saveSelectedOrg(id);
	}
}

function countChar(textareaNamezzjs,spanName){
	 document.getElementById(spanName).innerHTML=document.getElementById(textareaNamezzjs).value.length;
}

function mailHistory(){
	$.ajax({
		url : "mail",
		data : {
			action : 'r',			
		},
		async: false, 
		type: "post",
		dataType: "json",
		success: function(data, status, xHr){			
			if("OK" == data.Result){	
				var objs = eval(data.Data);
				listMail(objs);
			}else{
				//alert(data.Message);
				new Toast({context:$('body'),message:data.Message}).show();						
			}
		},
		error: function(xHr, status, error){
			//alert("与服务器通信出错，请检查网络！");
			new Toast({context:$('body'),message:"与服务器通信出错，请检查网络！"}).show();		
		}
	});	
}

function listMail(objs){
	document.getElementById("mailListTable").innerHTML = "";
	
		
				
		//document.getElementById("commentTable").appendChild (newNode4); 
		$.each(objs, function displayStudent(id,item){
			//console.log(id + " : " + item.id);
			var newNode = document.createElement ("tr");						
			
			var cell1=document.createElement ("td");
			cell1.className = "text-positon";			
			cell1.innerHTML = item.topic;			
			newNode.appendChild (cell1);
			
			
			
			
			
			//var opeHtml = "";
			//var vis = item.evaluation.visibility;
			//console.log(vis);
			//if(vis == 0)
			var opeHtml = "<table><tr><td><input type=\"button\" class=\"btn btn-default btn-comment\" value=\"查看\" onClick=\"viewMail("+item.id+");\" id=\"seeMail"+item.id+"\"></td>"+
                "</tr></table>";
			
			var cell2=document.createElement ("td");
			cell2.className = "text-positon";
			cell2.innerHTML = opeHtml;
			cell2.setAttribute("width","10%");
			newNode.appendChild (cell2);
	
			
			
			var cell3=document.createElement ("td");
			cell3.className = "text-positon";			
			cell3.innerHTML = item.sender;			
			newNode.appendChild (cell3);
			
			
			
			var opeHtml = "<table><tr><td><input type=\"button\" class=\"btn btn-default btn-comment\" value=\"查看\" onClick=\"viewMailR("+item.id+");\" id=\"seeMailR"+item.id+"\"></td>"+
            "</tr></table>";
		
			var cell4=document.createElement ("td");
			cell4.className = "text-positon";
			cell4.innerHTML = opeHtml;			
			newNode.appendChild (cell4);
			
			
			var cell5=document.createElement ("td");
			cell5.className = "text-positon";			
			cell5.innerHTML = item.sendTime;			
			newNode.appendChild (cell5);
			
					
			
			document.getElementById("mailListTable").appendChild (newNode); 		
			
			
			
			
			
			
            
			
			
			
			var newNode2 = document.createElement ("tr");
            newNode2.className = "hideTheDiv";
            newNode2.id = "mailRTr" + item.id;
            
			var cell7=document.createElement ("td");
			cell7.setAttribute("colspan","5");			
			//cell1.className = "text-positon";			
			//cell7.innerHTML = "<div style=\"word-break:break-all\">"+item.receivers+"</div>";
			var html7 = ""
			$.each(item.receivers, function displayStudent(id,item){
				html7 += "<p>"+item+"</p>";
			});
			cell7.innerHTML = html7;
			newNode2.appendChild (cell7);
			
			document.getElementById("mailListTable").appendChild (newNode2);
			
			
			
			
			
			
			
			
			var newNode1 = document.createElement ("tr");
            newNode1.className = "hideTheDiv";
            newNode1.id = "mailTr" + item.id;
            
			var cell6=document.createElement ("td");
			cell6.setAttribute("colspan","5");			
			//cell1.className = "text-positon";			
			cell6.innerHTML = "<div style=\"word-break:break-all\">"+item.content+"</div>";				
			newNode1.appendChild (cell6);
			
			document.getElementById("mailListTable").appendChild (newNode1);
		});
	//});
}

function viewMail(id) {
	var name = document.getElementById("mailTr" + id).className;
	var button = document.getElementById("seeMail" + id);
	// console.log(button.value);
	if (name == "") {
		document.getElementById("mailTr" + id).className = "hideTheDiv";
		button.value = "查看";
	} else {
		document.getElementById("mailTr" + id).className = "";
		button.value = "收起";
	}
	// console.log(name);
}

function viewMailR(id) {
	var name = document.getElementById("mailRTr" + id).className;
	var button = document.getElementById("seeMailR" + id);
	// console.log(button.value);
	if (name == "") {
		document.getElementById("mailRTr" + id).className = "hideTheDiv";
		button.value = "查看";
	} else {
		document.getElementById("mailRTr" + id).className = "";
		button.value = "收起";
	}
	// console.log(name);
}










function msgHistory(){
	$.ajax({
		url : "message",
		data : {
			action : 'r',			
		},
		async: false, 
		type: "post",
		dataType: "json",
		success: function(data, status, xHr){			
			if("OK" == data.Result){	
				//var objs = eval(data.Data);
				//listMail(objs);
			}else{
				//alert(data.Message);
				new Toast({context:$('body'),message:data.Message}).show();						
			}
		},
		error: function(xHr, status, error){
			//alert("与服务器通信出错，请检查网络！");
			new Toast({context:$('body'),message:"与服务器通信出错，请检查网络！"}).show();		
		}
	});	
}

function listMsg(objs){
	document.getElementById("mailListTable").innerHTML = "";
	
		
				
		//document.getElementById("commentTable").appendChild (newNode4); 
		$.each(objs, function displayStudent(id,item){
			//console.log(id + " : " + item.id);
			var newNode = document.createElement ("tr");						
			
			var cell1=document.createElement ("td");
			cell1.className = "text-positon";			
			cell1.innerHTML = item.topic;			
			newNode.appendChild (cell1);
			
			
			
			
			
			//var opeHtml = "";
			//var vis = item.evaluation.visibility;
			//console.log(vis);
			//if(vis == 0)
			var opeHtml = "<table><tr><td><input type=\"button\" class=\"btn btn-default btn-comment\" value=\"查看\" onClick=\"viewMail("+item.id+");\" id=\"seeMail"+item.id+"\"></td>"+
                "</tr></table>";
			
			var cell2=document.createElement ("td");
			cell2.className = "text-positon";
			cell2.innerHTML = opeHtml;
			cell2.setAttribute("width","10%");
			newNode.appendChild (cell2);
	
			
			
			var cell3=document.createElement ("td");
			cell3.className = "text-positon";			
			cell3.innerHTML = item.sender;			
			newNode.appendChild (cell3);
			
			
			
			var opeHtml = "<table><tr><td><input type=\"button\" class=\"btn btn-default btn-comment\" value=\"查看\" onClick=\"viewMailR("+item.id+");\" id=\"seeMailR"+item.id+"\"></td>"+
            "</tr></table>";
		
			var cell4=document.createElement ("td");
			cell4.className = "text-positon";
			cell4.innerHTML = opeHtml;			
			newNode.appendChild (cell4);
			
			
			var cell5=document.createElement ("td");
			cell5.className = "text-positon";			
			cell5.innerHTML = item.sendTime;			
			newNode.appendChild (cell5);
			
					
			
			document.getElementById("mailListTable").appendChild (newNode); 		
			
			
			
			
			
			
            var newNode1 = document.createElement ("tr");
            newNode1.className = "hideTheDiv";
            newNode1.id = "mailTr" + item.id;
            
			var cell6=document.createElement ("td");
			cell6.setAttribute("colspan","5");			
			//cell1.className = "text-positon";			
			cell6.innerHTML = "<div style=\"word-break:break-all\">"+item.content+"</div>";			
			newNode1.appendChild (cell6);
			
			document.getElementById("mailListTable").appendChild (newNode1);
			
			
			
			var newNode2 = document.createElement ("tr");
            newNode2.className = "hideTheDiv";
            newNode2.id = "mailRTr" + item.id;
            
			var cell7=document.createElement ("td");
			cell7.setAttribute("colspan","5");			
			//cell1.className = "text-positon";			
			cell7.innerHTML = "<div style=\"word-break:break-all\">"+item.receiver+"</div>";			
			newNode2.appendChild (cell7);
			
			document.getElementById("mailListTable").appendChild (newNode2);
		});
	//});
}

function viewMsg(id) {
	var name = document.getElementById("mailTr" + id).className;
	var button = document.getElementById("seeMail" + id);
	// console.log(button.value);
	if (name == "") {
		document.getElementById("mailTr" + id).className = "hideTheDiv";
		button.value = "查看";
	} else {
		document.getElementById("mailTr" + id).className = "";
		button.value = "收起";
	}
	// console.log(name);
}

function viewMsgR(id) {
	var name = document.getElementById("mailRTr" + id).className;
	var button = document.getElementById("seeMailR" + id);
	// console.log(button.value);
	if (name == "") {
		document.getElementById("mailRTr" + id).className = "hideTheDiv";
		button.value = "查看";
	} else {
		document.getElementById("mailRTr" + id).className = "";
		button.value = "收起";
	}
	// console.log(name);
}

function tagCloud(){
	$.ajax({
		url : "ajax/evaluate",
		data : {
			action : 's',			
		},
		async: false, 
		type: "post",
		dataType: "json",
		success: function(data, status, xHr){			
			if("OK" == data.Result){	
				var objs = eval(data.Data);
				listTag(objs);
			}else{
				//alert(data.Message);
				new Toast({context:$('body'),message:data.Message}).show();						
			}
		},
		error: function(xHr, status, error){
			//alert("与服务器通信出错，请检查网络！");
			new Toast({context:$('body'),message:"与服务器通信出错，请检查网络！"}).show();		
		}
	});	
}

function listTag(objs){
	document.getElementById("tagCloudTable").innerHTML = "";	
	var k = 1;
		//document.getElementById("commentTable").appendChild (newNode4); 
		$.each(objs, function displayStudent(id,item){
			//console.log(id + " : " + item.id);
			var newNode = document.createElement ("tr");						
			
			var cell1=document.createElement ("td");
			cell1.className = "text-positon";			
			cell1.innerHTML = k;			
			newNode.appendChild (cell1);
			k++;
					
			var opeHtml = "<table><tr><td><input type=\"button\" class=\"btn btn-default btn-comment\" value=\""+item.tagName+"\" onClick=\"viewTag("+item.id+");\" id=\"tag"+item.id+"\"></td>"+
                "</tr></table>";
			
			var cell2=document.createElement ("td");
			cell2.className = "text-positon";
			cell2.innerHTML = opeHtml;
			cell2.setAttribute("width","10%");
			newNode.appendChild (cell2);
	
			
			
			var cell3=document.createElement ("td");
			cell3.className = "text-positon";			
			cell3.innerHTML = item.sum;			
			newNode.appendChild (cell3);
												
			
			document.getElementById("tagCloudTable").appendChild (newNode); 						
		});
	//});
}

function viewTag(id){
	document.getElementById("searchCategory").innerHTML = "按标签<span class=\"caret\"></span>";
	tempType = 3;
	
	$.ajax({
		url : "ajax/evaluate",
		data : {
			action : 't',			
		},
		async: false, 
		type: "post",
		dataType: "json",
		success: function(data, status, xHr){			
			if("OK" == data.Result){	
				document.getElementById("searchTag").style.display = "inline";
				document.getElementById("searchText").style.display = "none";
				
									
				var objs = eval(data.Data);
				document.getElementById("searchTag").options.length = 0;
				$.each(objs, function displayStudent(id,item){	
					//console.log(item.tagName);
					document.getElementById("searchTag").options.add(new Option(item.tagName,item.id));						
				});						
			}else{
				//alert(data.Message);
				new Toast({context:$('body'),message:data.Message}).show();
				
				return;
			}
		},
		error: function(xHr, status, error){
			//alert("与服务器通信出错，请检查网络！");
			new Toast({context:$('body'),message:"与服务器通信出错，请检查网络！"}).show();
			
			return;
		}
	});
	var objItemText = document.getElementById("tag"+id).value;
	var s = document.getElementById("searchTag");
	for (var i = 0; i < s.options.length; i++) { 
		if (s.options[i].text == objItemText) { 
			s.options[i].selected = true;  
			break; 
		} 
	} 
	$("#tagCloud").modal("hide");
	searchButton();
}