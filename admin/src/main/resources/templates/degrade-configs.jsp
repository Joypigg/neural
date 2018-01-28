<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="library/master.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>服务降级</title>
    <meta name="keywords" content="">
    <meta name="description" content="">

    <link rel="shortcut icon" href="favicon.ico">
    <link href="${ctx}/res/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
    <link href="${ctx}/res/css/font-awesome.css?v=4.4.0" rel="stylesheet">

    <!-- Data Tables -->
    <link href="${ctx}/res/css/plugins/dataTables/dataTables.bootstrap.css" rel="stylesheet">
    <link href="${ctx}/res/css/animate.css" rel="stylesheet">
    <link href="${ctx}/res/css/style.css?v=4.1.0" rel="stylesheet">
    <link href="${ctx}/res/css/plugins/switchery/switchery.css" rel="stylesheet">
    <link href="${ctx}/res/css/plugins/bootstrap-table/bootstrap-table.min.css" rel="stylesheet">
</head>

<body class="gray-bg">
<div class="wrapper wrapper-content animated fadeInRight">
    <div class="row">
        <div class="col-sm-12">
            <div class="ibox float-e-margins">
                <div class="ibox-title">
                    <h5>服务降级
                        <small>微治理 > 服务降级</small>
                    </h5>
                </div>
                <form action="${ctx}/degrade/update-global-config" method="post">
                    <div class="ibox-content">
                        <table border="0" class="col-sm-12">
                            <tr>
                                <td>
                                    <h4 style="display:inline">全局配置
                                        <small>（集群降级系统参数值配置）</small>
                                    </h4>
                                </td>
                                <td style="text-align: right">
                                    <button class="btn btn-danger btn-xs" type="submit">
                                        <i class="fa fa-check"></i> 修改配置
                                    </button>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="ibox-content">
                        <div class="table-responsive">
                            <table class="table table-bordered table-striped">
                                <thead>
                                <tr>
                                    <th>降级总开关</th>
                                    <th>推送降级事件</th>
                                    <th>全局降级模式</th>
                                    <th>配置拉取周期</th>
                                    <th>监控上报周期</th>
                                    <th>数据过期周期</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <th>
                                        <input type="text" name="enable"
                                               value="${globalConfig.enable}" style="display: none"/>
                                        <input type="checkbox" id="enable"
                                               class="js-switch_1" ${globalConfig.enable=='ON'?'checked':''}/>
                                    </th>
                                    <th>
                                        <input type="text" name="broadcastEvent"
                                               value="${globalConfig.broadcastEvent}" style="display: none"/>
                                        <input type="checkbox" id="broadcastEvent"
                                               class="js-switch_2" ${globalConfig.broadcastEvent=='ON'?'checked':''} />
                                    </th>
                                    <th>
                                        <select class="form-control" name="level" value="${globalConfig.level}">
                                            <option value="NON" ${globalConfig.level=='NON'?'selected':''}>关闭</option>
                                            <option value="HINT" ${globalConfig.level=='HINT'?'selected':''}>提示</option>
                                            <option value="RECOMMEND" ${globalConfig.level=='RECOMMEND'?'selected':''}>推荐</option>
                                            <option value="NEED" ${globalConfig.level=='NEED'?'selected':''}>需要</option>
                                            <option value="WARN" ${globalConfig.level=='WARN'?'selected':''}>警告</option>
                                            <option value="SERIOUS" ${globalConfig.level=='SERIOUS'?'selected':''}>必须</option>
                                        </select>
                                    </th>
                                    <th>
                                        <input type="text" placeholder="请输入参数……" name="pullConfigCycle"
                                               class="form-control"
                                               onblur="onblus()" value="${globalConfig.pullConfigCycle}"
                                               style="width:75px;display:inline"/> ms
                                    </th>
                                    <th>
                                        <input type="text" placeholder="请输入参数……" name="reportStatisticCycle"
                                               class="form-control" value="${globalConfig.reportStatisticCycle}"
                                               style="width:90px;display:inline"/>
                                        ms
                                    </th>
                                    <th>
                                        <input type="text" placeholder="请输入参数……" name="statisticDataExpire"
                                               class="form-control" value="${globalConfig.statisticDataExpire}"
                                               style="width:90px;display:inline"/>
                                        ms
                                    </th>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </form>
                <div class="ibox-content" style="border-top: none;padding-top: 0px;padding-bottom: 5px">
                    <h4 style="display:inline">降级规则
                        <small>（降级资源集群参数配置）</small>
                    </h4>
                </div>
                <div class="ibox-content">
                    <table class="table table-striped table-bordered table-hover dataTables-example">
                        <thead>
                        <tr>
                            <th>名称</th>
                            <th>降级ID</th>
                            <th>所属组</th>
                            <th>所属应用</th>
                            <th>开关</th>
                            <th>降级模式</th>
                            <th>返回类型</th>
                            <th>MOCK_CLASS</th>
                            <th>MOCK数据</th>
                            <th>降级策略</th>
                            <th>备注</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${degradeConfigs}" var="degradeConfig">
                            <tr class="gradeX">
                                <td>${degradeConfig.config.name}</td>
                                <td>${degradeConfig.identity.resource}</td>
                                <td>${degradeConfig.identity.group}</td>
                                <td>${degradeConfig.identity.application}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${degradeConfig.config.enable=='ON'}">
                                            <button class="btn btn-info btn-circle" type="button">
                                                <i class="fa fa-check"></i></button>
                                        </c:when>
                                        <c:when test="${degradeConfig.config.enable=='OFF'}">
                                            <button class="btn btn-danger btn-circle" type="button">
                                                <i class="fa fa-times"></i>
                                            </button>
                                        </c:when>
                                        <c:otherwise><font color="red">非法状态</font></c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${'NON'==degradeConfig.config.level}">
                                            <a class="btn btn-success btn-xs btn-rounded">未设置</a>
                                        </c:when>
                                        <c:when test="${'HINT'==degradeConfig.config.level}">
                                            <a class="btn btn-success btn-xs btn-rounded">提示</a>
                                        </c:when>
                                        <c:when test="${'RECOMMEND'==degradeConfig.config.level}">
                                            <a class="btn btn-info btn-xs btn-rounded">推荐</a>
                                        </c:when>
                                        <c:when test="${'NEED'==degradeConfig.config.level}">
                                            <a class="btn btn-primary btn-xs btn-rounded">需要</a>
                                        </c:when>
                                        <c:when test="${'WARN'==degradeConfig.config.level}">
                                            <a class="btn btn-warning btn-xs btn-rounded">警告</a>
                                        </c:when>
                                        <c:when test="${'SERIOUS'==degradeConfig.config.level}">
                                            <a class="btn btn-danger btn-xs btn-rounded">严重</a>
                                        </c:when>
                                        <c:otherwise><a class="btn btn-danger btn-xs">未知等级</a></c:otherwise>
                                    </c:choose>
                                </td>
                                <td>${degradeConfig.config.mock}</td>
                                <td>${degradeConfig.config.clazz}</td>
                                <td>${degradeConfig.config.data}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${'NON'==degradeConfig.config.strategy}">
                                            <a class="btn btn-danger btn-xs btn-rounded">不处理</a>
                                        </c:when>
                                        <c:when test="${'MOCK'==degradeConfig.config.strategy}">
                                            <a class="btn btn-info btn-xs btn-rounded">MOCK</a>
                                        </c:when>
                                        <c:when test="${'FALLBACK'==degradeConfig.config.strategy}">
                                            <a class="btn btn-info btn-xs btn-rounded">自定义</a></c:when>
                                        <c:otherwise><a class="btn btn-danger btn-xs btn-rounded">未知策略</a></c:otherwise>
                                    </c:choose>
                                </td>
                                <td>${degradeConfig.config.remarks}</td>
                                <td>
                                    <a href="${ctx}/degrade/degrade-config/${degradeConfig.identity.application}/${degradeConfig.identity.group}/${degradeConfig.identity.resource}">
                                        <button class="btn btn-success btn-xs" type="button"
                                                style="background-color: #1AB394">
                                            <i class="fa fa-paste"></i> 编辑
                                        </button>
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <div class="ibox-content" style="border-top: none;padding-top: 0px;padding-left: 0px">
                        <h4 style="display:inline;color: red">注意：
                            <small>①降级模式：每条降级资源都有自己所属的降级模式，可以通过全局的降级模式配置来实现全局的不同模式降级</small>
                            <small>②MOCK降级：MOCK降级可以支持十多种返回数据类型的适配，包括List&lt;T&gt;、MapList&lt;String,String&gt;等类型</small>
                        </h4>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</div>
</div>

<!-- 全局js -->
<script src="${ctx}/res/js/jquery.min.js?v=2.1.4"></script>
<script src="${ctx}/res/js/bootstrap.min.js?v=3.3.6"></script>

<script src="${ctx}/res/js/plugins/jeditable/jquery.jeditable.js"></script>

<!-- Data Tables -->
<script src="${ctx}/res/js/plugins/dataTables/jquery.dataTables.js"></script>
<script src="${ctx}/res/js/plugins/dataTables/dataTables.bootstrap.js"></script>

<script src="${ctx}/res/js/plugins/datapicker/bootstrap-datepicker.js"></script>
<script src="${ctx}/res/js/plugins/switchery/switchery.js"></script>
<script src="${ctx}/res/js/plugins/cropper/cropper.min.js"></script>
<!-- Page-Level Scripts -->
<script>
    $(document).ready(function () {
            var colorArray = ['#ED5565', '#1AB394']
            for (var i = 1; i <3; i++) {
                new Switchery(document.querySelector('.js-switch_' + i), {color: colorArray[i - 1]});
            }

            var nameArray = ['enable', 'printExceedLog', 'broadcastEvent', 'statisticReportEnable']
            $('span[id=enable]').click(function () {
                var obj = $('input[name=enable]');
                if (obj.val() == 'ON') {
                    obj.attr('value', 'OFF')
                } else {
                    obj.attr('value', 'ON')
                }
            });
            $('span[id=broadcastEvent]').click(function () {
                var obj = $('input[name=broadcastEvent]');
                if (obj.val() == 'ON') {
                    obj.attr('value', 'OFF')
                } else {
                    obj.attr('value', 'ON')
                }
            });


            $('.dataTables-example').dataTable();
            /* Init DataTables */
            var oTable = $('#editable').dataTable();
            /* Apply the jEditable handlers to the table */
            oTable.$('td').editable('../example_ajax1.php', {
                "callback": function (sValue, y) {
                    var aPos = oTable.fnGetPosition(this);
                    oTable.fnUpdate(sValue, aPos[0], aPos[1]);
                },
                "submitdata": function (value, settings) {
                    return {
                        "row_id": this.parentNode.getAttribute('id'),
                        "column": oTable.fnGetPosition(this)[2]
                    };
                },
                "width": "90%",
                "height": "100%"
            });
        }
    );

    function fnClickAddRow() {
        $('#editable').dataTable().fnAddData([
            "Custom row",
            "New row",
            "New row",
            "New row",
            "New row"]);
    }
</script>
<!-- Bootstrap table -->
<script src="${ctx}/res/js/plugins/bootstrap-table/bootstrap-table.min.js"></script>
<script src="${ctx}/res/js/plugins/bootstrap-table/bootstrap-table-mobile.min.js"></script>
<script src="${ctx}/res/js/plugins/bootstrap-table/locale/bootstrap-table-zh-CN.min.js"></script>
</body>
</html>