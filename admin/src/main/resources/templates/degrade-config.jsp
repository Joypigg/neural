<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="library/master.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>服务降级 - 降级配置变更</title>
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

    <link href="${ctx}/res/css/plugins/ionRangeSlider/ion.rangeSlider.css" rel="stylesheet">
    <link href="${ctx}/res/css/plugins/ionRangeSlider/ion.rangeSlider.skinFlat.css" rel="stylesheet">
    <link rel="stylesheet" href="${ctx}/res/ionRangeSliderDemo/css/normalize.css"/>
    <link rel="stylesheet" href="${ctx}/res/ionRangeSliderDemo/css/ion.rangeSlider.css"/>
    <link rel="stylesheet" href="${ctx}/res/ionRangeSliderDemo/css/ion.rangeSlider.skinFlat.css"/>
</head>

<body class="gray-bg">
<div class="wrapper wrapper-content animated fadeInRight">
    <div class="col-sm-12">
        <div class="ibox float-e-margins">
            <div class="ibox-title">
                <h5>降级配置信息变更
                    <small>（
                        ${degradeConfig.identity.application}->${degradeConfig.identity.group}->${degradeConfig.identity.resource}）
                    </small>
                </h5>
            </div>
            <div class="ibox-content">
                <form method="post" action="${ctx}/degrade/update-config" class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">降级标题</label>
                        <div class="col-sm-10 has-success">
                            <input type="text" placeholder="请输入降级名称" class="form-control" name="name" required=""
                                   aria-required="true"
                                   value="${degradeConfig.config.name}">
                        </div>
                    </div>
                    <div class="hr-line-dashed"></div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">限流ID</label>
                        <div class="col-sm-10">
                            <input type="text" readonly name="resource" value="${degradeConfig.identity.resource}"
                                   class="form-control"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">所属组</label>
                        <div class="col-sm-10">
                            <input type="text" readonly name="group" value="${degradeConfig.identity.group}"
                                   class="form-control"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">所属应用</label>
                        <div class="col-sm-10">
                            <input type="text" readonly name="application" value="${degradeConfig.identity.application}"
                                   class="form-control">
                        </div>
                    </div>
                    <div class="hr-line-dashed"></div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">降级开关</label>
                        <div class="col-sm-2">
                            <select class="form-control m-b" name="enable" value="${degradeConfig.config.enable}">
                                <option value="ON" ${degradeConfig.config.enable=='ON'?'selected':''}>启用降级</option>
                                <option value="OFF" ${degradeConfig.config.enable=='OFF'?'selected':''}>关闭降级</option>
                            </select>
                        </div>
                    </div>
                    <div class="hr-line-dashed"></div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">自动降级</label>
                        <div class="col-sm-1 has-success">
                            <input type="text" placeholder="请输入监控流量大小" class="form-control" name="requestVolume"
                                   required="" aria-required="true"
                                   value="${degradeConfig.config.requestVolume}">
                        </div>
                        <div class="col-sm-1 has-success">笔</div>
                        <div class="col-sm-1 has-success">
                            <input type="text" placeholder="请输入最大平均耗时" class="form-control" name="elapsed" required=""
                                   aria-required="true"
                                   value="${degradeConfig.config.elapsed}">
                        </div>
                        <div class="col-sm-1 has-success">ms</div>
                        <div class="col-sm-1 has-success">
                            <input type="text" placeholder="请输入降级周期" class="form-control" name="cycle" required=""
                                   aria-required="true" value="${degradeConfig.config.cycle}">
                        </div>
                        <div class="col-sm-1 has-success">ms</div>
                    </div>
                    <div class="hr-line-dashed"></div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">降级策略</label>
                        <div class="col-sm-2">
                            <select class="form-control m-b" name="strategy" value="${degradeConfig.config.strategy}">
                                <option value="NON" ${degradeConfig.config.strategy=='NON'?'selected':''}>不处理</option>
                                <option value="MOCK" ${degradeConfig.config.strategy=='MOCK'?'selected':''}>MOCK</option>
                                <option value="FALLBACK" ${degradeConfig.config.strategy=='FALLBACK'?'selected':''}>自定义</option>
                            </select>
                        </div>
                    </div>
                    <div class="hr-line-dashed"></div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">备注信息</label>
                        <div class="col-sm-10 has-warning">
                            <textarea id="ccomment" name="remarks" class="form-control">${degradeConfig.config.remarks}</textarea>
                        </div>
                    </div>
                    <div class="hr-line-dashed"></div>
                    <div class="form-group">
                        <div class="col-sm-4 col-sm-offset-2">
                            <button class="btn btn-info" type="submit">确认更新</button>
                            <button class="btn btn-white reset-data" type="reset">重置</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- 全局js -->
<script src="${ctx}/res/js/jquery.min.js?v=2.1.4"></script>
<script src="${ctx}/res/js/bootstrap.min.js?v=3.3.6"></script>

<!-- All JS -->
<script src="${ctx}/res/ionRangeSliderDemo/js/jquery-1.12.3.min.js"></script>
<script src="${ctx}/res/ionRangeSliderDemo/js/ion.rangeSlider.js"></script>
</body>
</html>