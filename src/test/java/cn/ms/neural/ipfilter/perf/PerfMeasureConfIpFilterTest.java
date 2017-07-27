package cn.ms.neural.ipfilter.perf;

import java.io.IOException;

import org.junit.Test;

import cn.ms.neural.ipfilter.ConfIpFilter;

public class PerfMeasureConfIpFilterTest {

    @Test
    public void runPerformanceMeasurement() throws IOException {
        ConfIpFilter filter = IpListUtil.createConfigIpFilterUsingIpList();
        MeasurementData data = new IpFilterPerfExecutor(filter).execute();
        data.printReport();
    }

}
