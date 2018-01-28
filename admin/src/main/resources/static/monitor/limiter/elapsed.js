// 基于准备好的dom，初始化实例
var ELAPSEDChart = echarts.init(document.getElementById('elapsed'), 'westeros');
var ELAPSEDData = {
    time: ["2017-07-12", "2017-07-13", "2017-07-14", "2017-07-15", "2017-07-16", "2017-07-17", "2017-07-18"],
    maxELAPSED: [16, 28, 60, 70, 56, 4, 25],
    avgELAPSED: [48, 130, 192, 151, 72, 56, 191],
};
// 指定图表的配置项和数据
var ELAPSEDOption = {
    title: {
        text: '',
        subtext: ''
    },
    tooltip: {
        trigger: 'axis'
    },
    legend: {
        data: ['最大耗时', '平均耗时']
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
        data: ELAPSEDData.time
    },
    yAxis: {
        name: '耗时/ms'
    },
    grid: [{
        left: 'left',
        top: 'top',
        right: '10',
        bottom: 30
    }],
    series: [
        {
            name: '最大耗时',
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
            data: ELAPSEDData.maxELAPSED
        },
        {
            name: '平均耗时',
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
            data: ELAPSEDData.avgELAPSED
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
ELAPSEDChart.setOption(ELAPSEDOption);

//动态添加数据，可以通过Ajax获取数据然后填充
setInterval(function () {
    ELAPSEDData.time.push((new Date()).toLocaleTimeString().replace(/^\D*/, ''));
    ELAPSEDData.maxELAPSED.push(Math.floor(Math.random() * 200) + 20);
    ELAPSEDData.avgELAPSED.push(Math.floor(Math.random() * 100) + 20);

    //按自己需求可以取消这个限制
    if (ELAPSEDData.time.length >= 20) {
        //移除最开始的一条数据
        ELAPSEDData.time.shift();
        ELAPSEDData.maxELAPSED.shift();
        ELAPSEDData.avgELAPSED.shift();
    }
    ELAPSEDChart.setOption({
        xAxis: {
            data: ELAPSEDData.time
        },
        series: [
            {
                name: '最大耗时',
                data: ELAPSEDData.maxELAPSED
            },
            {
                name: '平均耗时',
                data: ELAPSEDData.avgELAPSED
            }
        ]
    });
}, 2000);
$(window).resize(function () {
    ELAPSEDChart.resize();
});