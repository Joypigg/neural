package cn.ms.neural.jwt.algorithms.support;

import cn.ms.neural.jwt.DecodedJWT;
import cn.ms.neural.jwt.algorithms.Algorithm;
import cn.ms.neural.jwt.exceptions.SignatureGenerationException;
import cn.ms.neural.jwt.exceptions.SignatureVerificationException;
import cn.ms.neural.util.micro.Base64;

public class NoneAlgorithm extends Algorithm {

	public NoneAlgorithm() {
        super("none", "none");
    }

    @Override
    public void verify(DecodedJWT jwt) throws SignatureVerificationException {
        byte[] signatureBytes = Base64.getUrlDecoder().decode(jwt.getSignature());
        if (signatureBytes.length > 0) {
            throw new SignatureVerificationException(this);
        }
    }

    @Override
    public byte[] sign(byte[] contentBytes) throws SignatureGenerationException {
        return new byte[0];
    }
}
