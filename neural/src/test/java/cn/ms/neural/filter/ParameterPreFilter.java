package cn.ms.neural.filter;

import cn.ms.neural.extension.Activation;
import cn.ms.neural.extension.SpiMeta;

@SpiMeta(name = "parameter")
@Activation(key = FilterChain.PRE, seq = 1)
public class ParameterPreFilter extends Filter<Message> {

	@Override
	public boolean doPre(Message message, Object... args) throws Throwable {
		return true;
	}

}
