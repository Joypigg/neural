package cn.ms.neural.filter;

import cn.ms.neural.extension.Extension;

@Extension(value = "parameter", category = FilterChain.PRE, order = 1)
public class ParameterPreFilter extends Filter<Message> {

	@Override
	public boolean doPre(Message message, Object... args) throws Throwable {
		return true;
	}

}
