<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="library/master.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>限流统计 - 分布式限流</title>
    <meta name="keywords" content="">
    <meta name="description" content="">

    <link rel="shortcut icon" href="favicon.ico">
    <link href="${ctx}/res/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
    <link href="${ctx}/res/css/font-awesome.css?v=4.4.0" rel="stylesheet">
    <link href="${ctx}/res/css/plugins/footable/footable.core.css" rel="stylesheet">

 	<!-- Sweet Alert -->
    <link href="${ctx}/res/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
    
    <link href="${ctx}/res/css/animate.css" rel="stylesheet">
    <link href="${ctx}/res/css/style.css?v=4.1.0" rel="stylesheet">

</head>

<body class="gray-bg">
    <div class="wrapper wrapper-content animated fadeInRight">
    	<div class="row">
    		<div class="col-sm-12">
    			<div class="ibox float-e-margins">
					<div class="ibox-title">
	                    <h5><font color="gray">功能模块 > 分布式限流 > </font>限流规则</h5>
	                    <div class="ibox-tools">
	                        <a class="dropdown-toggle" data-toggle="dropdown" href="#"><i class="fa fa-wrench"></i></a>
	                        <ul class="dropdown-menu dropdown-user">
	                            <li><a href="#">选项 01</a></li>
	                            <li><a href="#">选项 02</a></li>
	                        </ul>
	                    </div>
	                </div>
                </div>
			</div>
    		<c:forEach items="${limiterRules}" var="limiterRule">
				<div class="col-sm-4">
	                <div class="ibox float-e-margins">
	                    <div class="ibox-title">
	                        <h5><i class="fa fa-paper-plane"></i> ${limiterRule.keys}</h5>
	                        <div class="ibox-tools">
	                        	<span class="badge badge-blue"><i class="fa fa-heartbeat"></i> 正常</span>
	                        </div>
	                    </div>
	                    <div class="ibox-content">
	                        <h5>限流资源：${limiterRule.keys}</h5>
	                        <table class="table table-stripped small m-t-md">
	                        	<thead>
	                        		<tr>
	                                	<th>时间粒度</th>
	                                    <th>资源上线</th>
	                                    <th>当前资源</th>
	                               	</tr>
	                        	</thead>
	                            <tbody>
	                            	<c:forEach items="${limiterRule.limiterRes}" var="lres">
		                            	<tr>
	                                    	<td><i class="fa fa-circle text-navy"> ${lres.category}</i></td>
	                                    	<td><i class="fa text-navy"> ${lres.maxAmount} 次</i></td>
	                                    	<td><i class="fa text-navy"> ${lres.nowAmount} 次</i></td>
	                                	</tr>
	                            	</c:forEach>
	                            </tbody>
	                        </table> 
	                        <small><i class="fa fa-spinner"></i> 
							<jsp:useBean id="dateValue" class="java.util.Date"/>
							<jsp:setProperty name="dateValue" property="time" value="${limiterRule.time}"/>
							查询时间：<fmt:formatDate value="${dateValue}" pattern="yyyy-MM-dd HH:mm:ss"/>
	                        </small>
	                    </div>
	                </div>
	            </div>
			</c:forEach>
        </div>
    </div>
    <!-- 全局js -->
    <script src="${ctx}/res/js/jquery.min.js?v=2.1.4"></script>
    <script src="${ctx}/res/js/bootstrap.min.js?v=3.3.6"></script>
    <script src="${ctx}/res/js/plugins/footable/footable.all.min.js"></script>

    <!-- 自定义js -->
    <script src="${ctx}/res/js/content.js?v=1.0.0"></script>
    <!-- Sweet alert -->
    <script src="${ctx}/res/js/plugins/sweetalert/sweetalert.min.js"></script>
    <script>
        $(document).ready(function() {
            $('.footable').footable();
	        $('.demo3').click(function () {
	            swal({
	                title: "您确定要删除该配置数据项吗",
	                text: "删除后将无法恢复，请谨慎操作！",
	                type: "warning",
	                showCancelButton: true,
	                confirmButtonColor: "#DD6B55",
	                confirmButtonText: "删除",
	                closeOnConfirm: false
	            }, function () {
	            	swal("删除成功！", "您已经永久删除了该配置数据项。", "success");
	            });
	        });
        });
    </script>

</body>

</html>