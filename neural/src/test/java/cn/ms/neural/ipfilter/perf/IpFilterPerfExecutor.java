package cn.ms.neural.ipfilter.perf;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import cn.ms.neural.ipfilter.IpFilter;

class IpFilterPerfExecutor {
    private IpFilter filter;

    public IpFilterPerfExecutor(IpFilter filter) {
        this.filter = filter;
    }

    public MeasurementData executeInMultithread(int threadCounts) {
        ExecutorService executorService = Executors.newFixedThreadPool(threadCounts);
        CompletionService<MeasurementData> completionService =
                new ExecutorCompletionService<MeasurementData>(executorService);

        for (int i = 0 ; i < threadCounts; i++) {
            completionService.submit(new Callable<MeasurementData>() {
                @Override
                public MeasurementData call() throws Exception {
                    return new IpFilterPerfExecutor(filter).execute();
                }
            });
        }
        MeasurementData sumData = new MeasurementData();
        for (int i = 0 ; i < threadCounts; i++) {
            try {
                Future<MeasurementData> future = completionService.take();
                MeasurementData data = future.get();
                data.printReport();;
                sumData.add(data);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            } catch (ExecutionException ex) {
                ex.printStackTrace();
            }
        }
        executorService.shutdown();
        return sumData;
    }

    public MeasurementData execute() throws IOException {
        MeasurementData sm = new MeasurementData();

        List<IpIterator> ipIterList = IpListUtil.randomIpIteratorList(5);
        sm.start();
        outer:
        for (IpIterator ipIter : ipIterList) {
            while (ipIter.hasNext()) {
                String ip = ipIter.next();
                filter.accept(ip);
                sm.increaseCount();
                if (sm.getCount() == 100000)
                    break outer;
            }
        }
        sm.stop();
        return sm;
    }
}
