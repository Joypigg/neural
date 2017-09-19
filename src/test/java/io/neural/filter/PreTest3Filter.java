package io.neural.filter;

import io.neural.Message;
import io.neural.extension.Extension;
import io.neural.filter.Chain;
import io.neural.filter.Filter;
import io.neural.filter.FilterChain;

@Extension(category = FilterChain.PRE, order = 2)
public class PreTest3Filter extends Filter<Message> {
	
	@Override
	public void doFilter(Chain<Message> chain, Message m) throws Exception {
		System.out.println(this.getClass().getName());
//		throw new RuntimeException();
		chain.doFilter(chain, m);
	}

}
