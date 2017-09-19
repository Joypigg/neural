package cn.ms.neural.limiter;

import io.neural.limiter.injvm.TokenBucket;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class TokenBucketTest {
	
	public static void arrayTest() {
		ArrayBlockingQueue<Integer> tokenQueue = new ArrayBlockingQueue<Integer>(10);
		tokenQueue.offer(1);
		tokenQueue.offer(1);
		tokenQueue.offer(1);
		System.out.println(tokenQueue.size());
		System.out.println(tokenQueue.remainingCapacity());
	}

	public static void tokenTest() throws InterruptedException, IOException {
		TokenBucket tokenBucket = TokenBucket.newBuilder().avgFlowRate(512).maxFlowRate(1024).build();

		BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("/tmp/ds_test")));
		String data = "xxxx";// 四个字节
		for (int i = 1; i <= 1000; i++) {
			Random random = new Random();
			int i1 = random.nextInt(100);
			boolean tokens = tokenBucket.getTokens(stringCopy(data, i1).getBytes());
			TimeUnit.MILLISECONDS.sleep(100);
			if (tokens) {
				bufferedWriter.write("token pass --- index:" + i1);
				System.out.println("token pass --- index:" + i1);
			} else {
				bufferedWriter.write("token rejuect --- index" + i1);
				System.out.println("token rejuect --- index" + i1);
			}

			bufferedWriter.newLine();
			bufferedWriter.flush();
		}

		bufferedWriter.close();
	}

	private static String stringCopy(String data, int copyNum) {
		StringBuilder sbuilder = new StringBuilder(data.length() * copyNum);
		for (int i = 0; i < copyNum; i++) {
			sbuilder.append(data);
		}

		return sbuilder.toString();
	}

	public static void main(String[] args) throws Exception {
		tokenTest();
	}

}
