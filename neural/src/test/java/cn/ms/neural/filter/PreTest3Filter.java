package cn.ms.neural.filter;

import cn.ms.neural.extension.Extension;

@Extension(category = FilterChain.PRE, order = 2)
public class PreTest3Filter extends Filter<Message> {
	
	@Override
	public void doFilter(Chain<Message> chain, Message m) throws Exception {
		System.out.println(this.getClass().getName());
//		throw new RuntimeException();
		chain.doFilter(chain, m);
	}

}
