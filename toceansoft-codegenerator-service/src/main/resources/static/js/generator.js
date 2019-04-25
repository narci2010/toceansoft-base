$(function() {
	loadTables()
});

var vm = new Vue({
	el : '#rrapp',
	data : {
		q : {
			tableName : null,
			sysName : null,
			moduleName : null,
			jdbcUrl : null,
			port : null,
			username : null,
			password : null,
			database : null
		},
		canDo : false
	},
	methods : {
		query : function() {
			vm.isInitDB();
			vm.dbInfos();
			var canDo = vm.canDo;
			if (!canDo) {
				alert("查询之前请先初始化数据库。");
				return;
			}
			$("#jqGrid").jqGrid('setGridParam', {
				url : "/sys/generator/list",
				postData : {
					'tableName' : vm.q.tableName
				},
				page : 1
			}).trigger("reloadGrid");
		},
		isInitDB : function() {
			$.ajax({
				url : "/sys/generator/isInitDB",
				type : 'GET',
				async : false,
				success : function(response) {
					console.log(response)
					if (response.code == 0) {
						vm.canDo = response.init;
					}
				}
			});
		},
		dbInfos : function() {
			$.ajax({
				url : "/sys/generator/dbInfos",
				type : 'GET',
				async : false,
				success : function(response) {
					console.log(response)
					if (response.code == 0) {
						if (typeof response.infos != "undefined"
								&& response.infos != null
								&& response.infos != "") {
							vm.q.jdbcUrl = response.infos.jdbcUrl;
							vm.q.port = response.infos.port;
							vm.q.database = response.infos.database;
							vm.q.username = response.infos.username;
							vm.q.password = response.infos.password;
						}
					}
				}
			});
		},
		initDB : function() {
			var jdbcUrl = vm.q.jdbcUrl;
			var port = vm.q.port;
			var username = vm.q.username;
			var password = vm.q.password;
			var database = vm.q.database;
			if (typeof jdbcUrl == "undefined" || jdbcUrl == null
					|| jdbcUrl == "") {
				alert("必须填写数据库IP地址。");
				return;
			}
			if (typeof database == "undefined" || database == null
					|| database == "") {
				alert("必须填写数据库名称。");
				return;
			}
			if (typeof username == "undefined" || username == null
					|| username == "") {
				alert("必须填写数据库用户名。");
				return;
			}
			if (typeof password == "undefined" || password == null
					|| password == "") {
				alert("必须填写数据库密码。");
				return;
			}

			$.ajax({
				url : "/sys/generator/datasource?jdbcUrl=" + jdbcUrl + "&port="
						+ port + "&username=" + username + "&password="
						+ password + "&database=" + database,
				type : 'GET',
				async : false,
				success : function(response) {
					console.log(response)
					if (response.code == 0) {
						alert("数据库初始化成功。")
						vm.canDo = true
						vm.reload()
					} else {
						alert(response.msg)
					}
				}
			});

		},
		generator : function() {
			vm.isInitDB();
			var canDo = vm.canDo;
			if (!canDo) {
				alert("生成代码之前请先初始化数据库。");
				return;
			}
			var tableNames = getSelectedRows();
			var sysName = vm.q.sysName;
			var moduleName = vm.q.moduleName;
			if (typeof moduleName == "undefined" || sysName == null
					|| sysName == "") {
				alert("输入系统名（必须符合java包名命名规则2）");
				return;
			}
			if (typeof moduleName == "undefined" || moduleName == null
					|| moduleName == "") {
				alert("输入模块名（必须符合java包名命名规则）");
				return;
			}
			if (tableNames == null) {
				return;
			}
			location.href = "sys/generator/code?tables="
					+ encodeURIComponent(JSON.stringify(tableNames))
					+ "&sysName=" + sysName + "&moduleName=" + moduleName;
		},
		reload : function(event) {
			vm.dbInfos();
			var page = $("#jqGrid").jqGrid('getGridParam', 'page');
			$("#jqGrid").jqGrid('setGridParam', {
				postData : {
					'tableName' : vm.q.tableName
				},
				page : page
			}).trigger("reloadGrid");
		}
	},
	mounted : function() {
		var jdbcUrl = "";
		var port = "";
		var database = "";
		var username = "";
		var password = "";
		$.ajax({
			url : "/sys/generator/dbInfos",
			type : 'GET',
			async : false,
			success : function(response) {
				console.log(response)
				if (response.code == 0) {
					if (typeof response.infos != "undefined"
							&& response.infos != null && response.infos != "") {
						jdbcUrl = response.infos.jdbcUrl;
						port = response.infos.port;
						database = response.infos.database;
						username = response.infos.username;
						password = response.infos.password;
					}
				}
			}
		});
		this.q.jdbcUrl = jdbcUrl;
		this.q.port = port;
		this.q.database = database;
		this.q.username = username;
		this.q.password = password;
	}
});

function loadTables() {
	$("#jqGrid").jqGrid({
		url : 'sys/generator/list',
		datatype : "json",
		colModel : [ {
			label : '表名',
			name : 'tableName',
			width : 100,
			key : true
		}, {
			label : 'Engine',
			name : 'engine',
			width : 70
		}, {
			label : '表备注',
			name : 'tableComment',
			width : 100
		}, {
			label : '创建时间',
			name : 'createTime',
			width : 100
		} ],
		viewrecords : true,
		height : 385,
		rowNum : 10,
		rowList : [ 10, 30, 50, 100, 200 ],
		rownumbers : true,
		rownumWidth : 25,
		autowidth : true,
		multiselect : true,
		pager : "#jqGridPager",
		jsonReader : {
			root : "page.list",
			page : "page.currPage",
			total : "page.totalPage",
			records : "page.totalCount"
		},
		prmNames : {
			page : "page",
			rows : "limit",
			order : "order"
		},
		gridComplete : function() {
			// 隐藏grid底部滚动条
			$("#jqGrid").closest(".ui-jqgrid-bdiv").css({
				"overflow-x" : "hidden"
			});
		}
	}).trigger("reloadGrid");
}
// var
// data=[{"tableName":"dev_example","engine":"InnoDB","tableComment":"代码生成样例数据库表","createTime":"2019-02-22"}]
var data = []
function loadLocalTables() {
	$("#jqGrid").jqGrid({
		datatype : "local",
		data : data,
		colModel : [ {
			label : '表名',
			name : 'tableName',
			width : 100,
			key : true
		}, {
			label : 'Engine',
			name : 'engine',
			width : 70
		}, {
			label : '表备注',
			name : 'tableComment',
			width : 100
		}, {
			label : '创建时间',
			name : 'createTime',
			width : 100
		} ],
		viewrecords : true,
		height : 385,
		rowNum : 10,
		rowList : [ 10, 30, 50, 100, 200 ],
		rownumbers : true,
		rownumWidth : 25,
		autowidth : true,
		multiselect : true,
		gridComplete : function() {
			// 隐藏grid底部滚动条
			$("#jqGrid").closest(".ui-jqgrid-bdiv").css({
				"overflow-x" : "hidden"
			});
		}
	});
}
