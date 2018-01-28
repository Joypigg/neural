// 基于准备好的dom，初始化实例
var TRADEChart = echarts.init(document.getElementById('trade'), 'walden');
var TRADEData = {
    time: ["2017-07-12", "2017-07-13", "2017-07-14", "2017-07-15", "2017-07-16", "2017-07-17", "2017-07-18"],
    SUCCESS: [16, 28, 60, 70, 56, 4, 25],
    FAILURE: [48, 130, 192, 151, 72, 56, 191],
    TIMEOUT: [23, 34, 67, 170, 256, 443, 525],
    REJECTION: [12, 100, 192, 201, 232, 356, 791]
};
// 指定图表的配置项和数据
var TRADEOption = {
    title: {
        text: '',
        subtext: ''
    },
    tooltip: {
        trigger: 'axis'
    },
    legend: {
        data: ['成功交易量', '失败交易量', '超时交易量', '拒绝交易量']
    },
    toolbox: {
        show: false,
        feature: {
            magicType: {show: true, type: ['stack', 'tiled']},
            saveAsImage: {show: true}
        }
    },
    xAxis: {
        type: 'category',
        boundaryGap: false,
        data: TRADEData.time
    },
    yAxis: {
        name: '交易量/笔'
    },
    series: [
        {
            name: '成功交易量',
            type: 'line',
            smooth: true,
            markPoint : {
                data : [ {
                    type : 'max'
                }, {
                    type : 'min'
                } ]
            },
            areaStyle: {
                normal: {}
            },
            lineStyle: {
                normal: {
                    width: 1.5
                }
            },
            data: TRADEData.SUCCESS
        },
        {
            name: '失败交易量',
            type: 'line',
            smooth: true,
            markPoint : {
                data : [ {
                    type : 'max'
                }, {
                    type : 'min'
                } ]
            },
            areaStyle: {
                normal: {}
            },
            lineStyle: {
                normal: {
                    width: 1.5
                }
            },
            data: TRADEData.FAILURE
        },
        {
            name: '超时交易量',
            type: 'line',
            smooth: true,
            markPoint : {
                data : [ {
                    type : 'max'
                }, {
                    type : 'min'
                } ]
            },
            areaStyle: {
                normal: {}
            },
            lineStyle: {
                normal: {
                    width: 1.5
                }
            },
            data: TRADEData.TIMEOUT
        },
        {
            name: '拒绝交易量',
            type: 'line',
            smooth: true,
            markPoint : {
                data : [ {
                    type : 'max'
                }, {
                    type : 'min'
                } ]
            },
            areaStyle: {
                normal: {}
            },
            lineStyle: {
                normal: {
                    width: 1.5
                }
            },
            data: TRADEData.REJECTION
        }
    ],
    grid: [{
        left: 30,
        top: 60,
        right: 30,
        bottom: 30
    }]
};

// 使用刚指定的配置项和数据显示图表。
TRADEChart.setOption(TRADEOption);

//动态添加数据，可以通过Ajax获取数据然后填充
setInterval(function () {
    TRADEData.time.push((new Date()).toLocaleTimeString().replace(/^\D*/, ''));
    TRADEData.SUCCESS.push(Math.floor(Math.random() * 100) + 20);
    TRADEData.FAILURE.push(Math.floor(Math.random() * 200) + 20);
    TRADEData.TIMEOUT.push(Math.floor(Math.random() * 300) + 20);
    TRADEData.REJECTION.push(Math.floor(Math.random() * 400) + 20);

    //按自己需求可以取消这个限制
    if (TRADEData.time.length >= 20) {
        //移除最开始的一条数据
        TRADEData.time.shift();
        TRADEData.SUCCESS.shift();
        TRADEData.FAILURE.shift();
        TRADEData.TIMEOUT.shift();
        TRADEData.REJECTION.shift();
    }
    TRADEChart.setOption({
        xAxis: {
            data: TRADEData.time
        },
        series: [
            {
                name: '成功交易量',
                data: TRADEData.SUCCESS
            },
            {
                name: '失败交易量',
                data: TRADEData.FAILURE
            },
            {
                name: '超时交易量',
                data: TRADEData.TIMEOUT
            },
            {
                name: '拒绝交易量',
                data: TRADEData.REJECTION
            }
        ]
    });
}, 2000);
$(window).resize(function () {
    TRADEChart.resize();
});