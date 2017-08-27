package cn.ms.neural.filter;

import java.util.List;
import java.util.Map;

import org.junit.Test;

/**
 * PRO:1->3->2<br>
 * POST:2->1->3<br>
 * ERROR:3->2->1<br>
 * DEFAULT:3->2->1<br>
 * 
 * @author lry
 *
 */
public class NeuralChainTest {

	@SuppressWarnings("rawtypes")
	@Test
	public void testDefaultFilter() {
		try {
			NeuralChain<Message> neuralChain = new NeuralChain<Message>();
			for (Map.Entry<String, List<Filter>> entry : neuralChain.getFilterMap().entrySet()) {
				System.out.println(entry.getKey() + "--->" + entry.getValue());
			}
			System.out.println("===========");
			neuralChain.doChain(new Message());
			
			System.out.println("===========");
			neuralChain.doChain(new Message(), "PRE");
			
			System.out.println("===========");
			neuralChain.doChain(new Message(), "POST");
			
			System.out.println("===========");
			neuralChain.doChain(new Message(), "ERROR");
			
			System.out.println("===========");
			neuralChain.doChains(new Message());			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
