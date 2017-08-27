package cn.ms.neural.filter;

import cn.ms.neural.extension.Extension;

@Extension(value = "parameter", category = NeuralChain.POST, order = 1)
public class ParameterPreFilter extends Filter<Message> {

	@Override
	public void doFilter(FilterChain<Message> chain, Message m) {
		// TODO Auto-generated method stub
		
	}


}
