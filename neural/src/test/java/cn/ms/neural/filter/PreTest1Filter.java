package cn.ms.neural.filter;

import cn.ms.neural.Message;
import cn.ms.neural.extension.Extension;

@Extension(category = FilterChain.PRE, order = 1)
public class PreTest1Filter extends Filter<Message> {
	
	@Override
	public void doFilter(Chain<Message> chain, Message m) throws Exception {
		System.out.println(this.getClass().getName());
		chain.doFilter(chain, m);
	}

}
