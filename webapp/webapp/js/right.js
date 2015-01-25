var isOpe = false;
var selectedOrg = new Array();
(function($, window, document, undefined) {

	/*global jQuery, console*/

	'use strict';

	var pluginName = 'right';
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
			
			/*Handle the tree operation*/
			if(isOpe){				
				for(var i = 0;i < selectedOrg.length;i++){
					if(selectedOrg[i] == node.id){
						selectedOrg.splice(0,1);
						new Toast({context:$('body'),message:"移除权限"}).show();
						isOpe = false;
						return;
					}
				}
				new Toast({context:$('body'),message:"本来就没有"}).show();
				isOpe = false;
				return;
			}
			
			var exist = false;
			
			for(var i = 0;i < selectedOrg.length;i++){
				if(selectedOrg[i] == node.id){
					exist = true;
					return;
				}
			}
			
			if(!exist){
				selectedOrg.push(node.id);
				new Toast({context:$('body'),message:"已加入"}).show();
			}
			
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
			         
					 
					    
					    
                        
						'<a class="btn-tree-operation" onClick="treeOperation();"><span class="glyphicon glyphicon-remove"></span></a>'+
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
function treeOperation(){
	isOpe = true;
}

function showT(id){
	console.log(id);
	selectedOrg.splice(0,selectedOrg.length);
	$('#orgTree'+id).right({data: getT(id)});
	console.log(getT(id));
	//$('#moveTree').right({data: getTree()});
	$.ajax({
		url : "user",
		data : {
			action : 'e',	
			uid:id,
		},
		async: false, 
		type: "post",
		dataType: "json",
		success: function(data, status, xHr){			
			if("OK" == data.Result){	
				selectedOrg = data.authority;
				//console.log(selectedOrg);
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

function getT(id){
	var tree;
	$.ajax({
		url : "user",
		data : {
			action : 't',	
			uid:id,
		},
		async: false, 
		type: "post",
		dataType: "json",
		success: function(data, status, xHr){			
			if("OK" == data.Result){	
				tree = data.Data;
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
	return tree;
}

function saveSelectedOrg(id){
	$.ajax({
		url : "user",
		data : {
			action : 'a',
			oids:selectedOrg,
			uid:id,
		},
		async: false, 
		type: "post",
		dataType: "json",
		success: function(data, status, xHr){			
			if("OK" == data.Result){	
				//return data.Data;
				new Toast({context:$('body'),message:"保存成功"}).show();
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