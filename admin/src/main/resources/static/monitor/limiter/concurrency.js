// 基于准备好的dom，初始化实例
var CONCURRENCYChart = echarts.init(document.getElementById('concurrency'), 'essos');
var CONCURRENCYData = {
    time: ["2017-07-12", "2017-07-13", "2017-07-14", "2017-07-15", "2017-07-16", "2017-07-17", "2017-07-18"],
    maxCONCURRENCY: [16, 28, 60, 70, 56, 4, 25],
    avgCONCURRENCY: [48, 130, 192, 151, 72, 56, 191],
};
// 指定图表的配置项和数据
var CONCURRENCYOption = {
    title: {
        text: '',
        subtext: ''
    },
    tooltip: {
        trigger: 'axis'
    },
    legend: {
        data: ['最大并发', '平均并发']
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
        data: CONCURRENCYData.time
    },
    yAxis: {
        name: '并发量'
    },
    grid: [{
        left: 'left',
        top: 'top',
        right: '10',
        bottom: 30
    }],
    series: [
        {
            name: '最大并发',
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
            data: CONCURRENCYData.maxCONCURRENCY
        },
        {
            name: '平均并发',
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
            data: CONCURRENCYData.avgCONCURRENCY
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
CONCURRENCYChart.setOption(CONCURRENCYOption);

//动态添加数据，可以通过Ajax获取数据然后填充
setInterval(function () {
    CONCURRENCYData.time.push((new Date()).toLocaleTimeString().replace(/^\D*/, ''));
    CONCURRENCYData.maxCONCURRENCY.push(Math.floor(Math.random() * 200) + 20);
    CONCURRENCYData.avgCONCURRENCY.push(Math.floor(Math.random() * 100) + 20);

    //按自己需求可以取消这个限制
    if (CONCURRENCYData.time.length >= 20) {
        //移除最开始的一条数据
        CONCURRENCYData.time.shift();
        CONCURRENCYData.maxCONCURRENCY.shift();
        CONCURRENCYData.avgCONCURRENCY.shift();
    }
    CONCURRENCYChart.setOption({
        xAxis: {
            data: CONCURRENCYData.time
        },
        series: [
            {
                name: '最大并发',
                data: CONCURRENCYData.maxCONCURRENCY
            },
            {
                name: '平均并发',
                data: CONCURRENCYData.avgCONCURRENCY
            }
        ]
    });
}, 2000);
$(window).resize(function () {
    CONCURRENCYChart.resize();
});