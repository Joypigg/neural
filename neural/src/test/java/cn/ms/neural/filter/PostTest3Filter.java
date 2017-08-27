package cn.ms.neural.filter;

import cn.ms.neural.extension.Extension;

@Extension(category = NeuralChain.POST, order = 3)
public class PostTest3Filter extends Filter<Message> {
	
	@Override
	public void doFilter(FilterChain<Message> chain, Message m) throws Exception {
		System.out.println(this.getClass().getName());
		chain.doFilter(chain, m);
	}

}
