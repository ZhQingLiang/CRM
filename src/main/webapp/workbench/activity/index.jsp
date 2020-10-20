<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">

	<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

	<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
	<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

	<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
	<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

<script type="text/javascript">

	$(function(){
		// 不能用 $("#activityPage").bs_pagination('getOption',rowsPerPage)
		pageList(1,5);
		$("#addBtn").click(function () {
			// alert("123")
			$("#createActivityModal").modal("show")

			<%--$("#create-marketActivityName").val("${user.name}")--%>


				$(".time").datetimepicker({
					minView: "month",
					language:  'zh-CN',
					format: 'yyyy-mm-dd',
					autoclose: true,
					todayBtn: true,
					pickerPosition: "bottom-left"
				});

			$.ajax({
				url:"workbench/activity/getUserList.do",
				data:{},
				type:"get",
				dataType:"json",
				success:function (result) {
					var list = ""
					$.each(result,function (i,n) {
						// 字符串拼接！！
						list += "<option value='"+n.id+"'>"+n.name+"</option>"
					})
					$("#createOwner").html(list)

					var id = "${user.id}";
					// alert(id)

					// 注意大小写
					// 设置Select的Value值为id的项选中
					$("#createOwner").val(id);


				}

			})

		})


		$("#saveBtn").click(function () {
			$.ajax({
				url: "workbench/activity/saveActivity.do",
				data: {
						"owner":$("#createOwner").val().trim(),
						"name":$.trim($("#createActivityName").val()),
						"startDate":$.trim($("#create-startTime").val()),
						"endDate":$.trim($("#create-endTime").val()),
						"cost":$.trim($("#create-cost").val()),
						"description":$.trim($("#create-describe").val())
						},
				type: "post",
				dataType: "json",
				success:function (result) {

					if(result.success){
						alert("添加成功")
						$(" #addActivitiForm")[0].reset();
						$("#createActivityModal").modal("hide")

						// pageList()放在这里，放在ajax之外，可能失败
						pageList(1,$("#activityPage").bs_pagination('getOption',rowsPerPage))
					}else{
						alert("添加失败")
					}
				}
			})

		})

		$("#editBtn").click(function () {
			var count = $("input[name=xuanze]:checked").length
			$(".time").datetimepicker({
				minView: "month",
				language:  'zh-CN',
				format: 'yyyy-mm-dd',
				autoclose: true,
				todayBtn: true,
				pickerPosition: "bottom-left"
			});
			if(count==1){

				$.ajax({
					url:"workbench/activity/getEditList.do",
					// $("input[name=xuanze]:checked").val()
					data:{"id": $("input[name=xuanze]:checked").val()},
					type:"get",
					dataType:"json",
					success:function (result) {
						var list = ""
						$.each(result.owners,function (i,n) {
							// 字符串拼接！！
							list += "<option value='"+n.id+"'>"+n.name+"</option>"
						})
						$("#edit-marketActivityOwner").html(list)

						$("#hidden-id").val(result.activity.id)
						$("#edit-marketActivityName").val(result.activity.name)
						$("#edit-startTime").val(result.activity.startDate)
						$("#edit-endTime").val(result.activity.endDate)
						$("#edit-describe").val(result.activity.description)
						$("#edit-cost").val(result.activity.cost)

						// activity中的owner就是user中的id！！注意外键关联
						$("#edit-marketActivityOwner").val(result.activity.owner);


					}
				})

				$("#editActivityModal").modal("show")


			}else if(count<1){
				alert("请选择需要修改的项")
			}else {
				alert("选择修改的项过多")
			}
		})

		$("#updateBtn").click(function () {
			$.ajax({
				url:"workbench/activity/update.do",
				data:{
					// 设置隐藏域id
					"id":$.trim($("#hidden-id").val()),
					"owner":$.trim($("#edit-marketActivityOwner").val()),
					"name":$.trim($("#edit-marketActivityName").val()),
					"startTime":$.trim($("#edit-startTime").val()),
					"endTime":$.trim($("#edit-endTime").val()),
					"describe":$.trim($("#edit-describe").val()),
					"cost":$.trim($("#edit-cost").val()),

				},
				type:"post",
				dataType:"json",
				success:function (result) {
					if(result.success){
						alert("修改成功")
						pageList($("#activityPage").bs_pagination('getOption',currentPage),
								$("#activityPage").bs_pagination('getOption',rowsPerPage))
					}else {
						alert("修改失败")
						alert($.trim($("#edit-id").val()))
					}
				}
			})

		})

		$("#deleteBtn").click(function () {
			var count = $("input[name=xuanze]:checked").length
			if(count==0){
				alert("请勾选需要删除的项")
			}else {
				// 删除多条
				var param = ""
				for(var i=0;i<count;i++){
					param += "id=" + $("input[name=xuanze]:checked")[i].value;
					if(i<count-1){
						param += "&";
					}
				}

				if(confirm("确定删除？")){
					$.ajax({
						url:"workbench/activity/delete.do",
						data:param,
						dataType:"json",
						type:"post",
						success:function (result) {
							if(result.success){
								alert("删除成功")

								pageList(1,$("#activityPage").bs_pagination('getOption',rowsPerPage))
							}else {
								alert("删除失败")
							}
						}
					})
				}

			}
			$("#sellectAll").prop("checked",false)

		})
		// $.ajax()

		/*

            点击查询按钮的时候，我们应该将搜索框中的信息保存起来,保存到隐藏域中


         */
		$("#searchBtn").click(function () {
			$("#hidden-name").val($.trim($("#search-name").val()));
			$("#hidden-owner").val($.trim($("#search-owner").val()));
			$("#hidden-startDate").val($.trim($("#search-startDate").val()));
			$("#hidden-endDate").val($.trim($("#search-endDate").val()));

			pageList(1,5);

		})
		$("#sellectAll").click(function () {
			$("input[name=xuanze]").prop("checked",this.checked)
		})
		//以下这种做法是不行的
		/*$("input[name=xz]").click(function () {

			alert(123);

		})*/

		//因为动态生成的元素，是不能够以普通绑定事件的形式来进行操作的
		/*

			动态生成的元素，我们要以on方法的形式来触发事件

			语法：
				$(需要绑定元素的有效的外层元素(非动态).on(绑定事件的方式,需要绑定的元素的jquery对象,回调函数)

		 */
		$("#activityBody").on("click",$("input[name=xuanze]"),function () {

			$("#sellectAll").prop("checked",$("input[name=xuanze]").length==$("input[name=xuanze]:checked").length);

		})

	})

	// 首先分析需要用到该函数的地方，共有6处
	function pageList(pageNo,pageSize) {

		//查询前，将隐藏域中保存的信息取出来，重新赋予到搜索框中
		$("#search-name").val($.trim($("#hidden-name").val()));
		$("#search-owner").val($.trim($("#hidden-owner").val()));
		$("#search-startDate").val($.trim($("#hidden-startDate").val()));
		$("#search-endDate").val($.trim($("#hidden-endDate").val()));

		$.ajax({
			url:"workbench/activity/pageList.do",
			data:{
				"pageNo":pageNo,
				"pageSize":pageSize,
				"name":$.trim($("#search-name").val()),
				"owner":$.trim($("#search-owner").val()),
				"startDate":$.trim($("#search-startDate").val()),
				"endDate":$.trim($("#search-endDate").val()),
			},
			dataType:"json",
			type:"get",
			success:function (result) {
				var html = "";

				// n为result.dataList的一条记录，不是result的一条记录

				// n.数据库的字段名，set/get方法名称要域xml输出名称对应
				$.each(result.dataList,function (i,n) {
					   html += '<tr class="active"> '
						// class主要用来描述样式，id不能重复，所以用name
                       html += '<td><input type="checkbox" name="xuanze" value="'+n.id+'"></td>'
                       html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.do?id='+n.id+'\';">'+n.name+'</a></td>'
					   html += '<td>'+n.owner+'</td>'
					   html += '<td>'+n.startDate+'</td>'
					   html += '<td>'+n.endDate+'</td>'
					   html += '</tr>'
				})

                $("#activityBody").html(html);
				// 注意取余
				var totalPages = result.total%pageSize==0?(result.total/pageSize):parseInt(result.total/pageSize)+1;

				// bs_pagination是分页插件写的
				$("#activityPage").bs_pagination({
					currentPage: pageNo, // 页码
					rowsPerPage: pageSize, // 每页显示的记录条数
					maxRowsPerPage: 20, // 每页最多显示的记录条数
					totalPages: totalPages, // 总页数
					totalRows: result.total, // 总记录条数

					visiblePageLinks: 3, // 显示几个卡片

					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,

					//该回调函数时在，点击分页组件的时候触发的
					onChangePage : function(event, data){
						pageList(data.currentPage , data.rowsPerPage);
					}
				});
			}


		})

	}

</script>
</head>
<body>
	<input type="hidden" id="hidden-name"/>
	<input type="hidden" id="hidden-owner"/>
	<input type="hidden" id="hidden-startDate"/>
	<input type="hidden" id="hidden-endDate"/>

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">

					<form id="addActivitiForm" class="form-horizontal" role="form">

						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="createOwner">

								</select>
							</div>
                            <label for="create-createActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="createActivityName">
                            </div>
						</div>

						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startTime" readonly>
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endTime" readonly>
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-describe"></textarea>
							</div>
						</div>

					</form>

				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn" data-dismiss="modal">保存</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">

					<form class="form-horizontal" role="form">
						<input type="hidden" id="hidden-id"/>
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-marketActivityOwner">
<%--								  <option>zhangsan</option>--%>
<%--								  <option>lisi</option>--%>
<%--								  <option>wangwu</option>--%>
								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-marketActivityName" value="发传单">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-startTime" value="2020-10-10">
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-endTime" value="2020-10-20">
							</div>
						</div>

						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" value="5,000">
							</div>
						</div>

						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-describe">市场活动Marketing，是指品牌主办或参与的展览会议与公关市场活动，包括自行主办的各类研讨会、客户交流会、演示会、新产品发布会、体验会、答谢会、年会和出席参加并布展或演讲的展览会、研讨会、行业交流会、颁奖典礼等</textarea>
							</div>
						</div>

					</form>

				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default"  data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateBtn" data-dismiss="modal">更新</button>
				</div>
			</div>
		</div>
	</div>




	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">

			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="startTime" id="search-startDate">
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="endTime" id="search-endDate">
				    </div>
				  </div>

				  <button type="button" id="searchBtn" class="btn btn-default">查询</button>

				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn" ><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>

			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="sellectAll" /></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityBody">
<%--						<tr class="active">--%>
<%--							<td><input type="checkbox" /></td>--%>
<%--							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>--%>
<%--                            <td>zhangsan</td>--%>
<%--							<td>2020-10-10</td>--%>
<%--							<td>2020-10-20</td>--%>
<%--						</tr>--%>
<%--                        <tr class="active">--%>
<%--                            <td><input type="checkbox" /></td>--%>
<%--                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>--%>
<%--                            <td>zhangsan</td>--%>
<%--                            <td>2020-10-10</td>--%>
<%--                            <td>2020-10-20</td>--%>
<%--                        </tr>--%>
					</tbody>
				</table>
			</div>

			<div style="height: 50px; position: relative;top: 30px;">


				<div id="activityPage"></div>
			</div>

		</div>

	</div>
</body>
</html>