package cn.ms.neural.ipfilter.perf;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class PerfMeasureListIpFilterTest {

    @Test
    public void runPerformanceMeasurement() throws IOException {
        ListIpFilter filter = IpListUtil.createListIpFilterUsingIpList();
        MeasurementData data = new IpFilterPerfExecutor(filter).execute();
        data.printReport();
    }

}
