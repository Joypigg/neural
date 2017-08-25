package cn.ms.neural.ipfilter.perf;

import java.io.IOException;

import org.junit.Test;

import cn.ms.neural.ipfilter.ConfIpFilter;

public class PerfMeasureConfIpFilterMultiThreadTest {

    @Test
    public void runPerformanceMeasurement() throws IOException {
        ConfIpFilter filter = IpListUtil.createConfigIpFilterUsingIpList();
        MeasurementData data = new IpFilterPerfExecutor(filter).executeInMultithread(20);
        data.printReport();
    }

}
