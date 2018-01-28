// 基于准备好的dom，初始化实例
var RATEChart = echarts.init(document.getElementById('rate'), 'macarons');
var RATEData = {
    time: ["2017-07-12", "2017-07-13", "2017-07-14", "2017-07-15", "2017-07-16", "2017-07-17", "2017-07-18"],
    maxRATE: [16, 28, 60, 70, 56, 4, 25],
    avgRATE: [48, 130, 192, 151, 72, 56, 191],
};
// 指定图表的配置项和数据
var RATEOption = {
    title: {
        text: '',
        subtext: ''
    },
    tooltip: {
        trigger: 'axis'
    },
    legend: {
        data: ['最大访问速率', '平均访问速率']
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
        data: RATEData.time
    },
    yAxis: {
        name: '访问速率'
    },
    grid: [{
        left: 'left',
        top: 'top',
        right: '10',
        bottom: 30
    }],
    series: [
        {
            name: '最大访问速率',
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
            data: RATEData.maxRATE
        },
        {
            name: '平均访问速率',
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
            data: RATEData.avgRATE
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
RATEChart.setOption(RATEOption);

//动态添加数据，可以通过Ajax获取数据然后填充
setInterval(function () {
    RATEData.time.push((new Date()).toLocaleTimeString().replace(/^\D*/, ''));
    RATEData.maxRATE.push(Math.floor(Math.random() * 200) + 20);
    RATEData.avgRATE.push(Math.floor(Math.random() * 100) + 20);

    //按自己需求可以取消这个限制
    if (RATEData.time.length >= 20) {
        //移除最开始的一条数据
        RATEData.time.shift();
        RATEData.maxRATE.shift();
        RATEData.avgRATE.shift();
    }
    RATEChart.setOption({
        xAxis: {
            data: RATEData.time
        },
        series: [
            {
                name: '最大访问速率',
                data: RATEData.maxRATE
            },
            {
                name: '平均访问速率',
                data: RATEData.avgRATE
            }
        ]
    });
}, 2000);
$(window).resize(function () {
    RATEChart.resize();
});