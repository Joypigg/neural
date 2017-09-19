package io.neural.jwt.algorithms.support;

import io.neural.jwt.DecodedJWT;
import io.neural.jwt.algorithms.Algorithm;
import io.neural.jwt.exceptions.SignatureGenerationException;
import io.neural.jwt.exceptions.SignatureVerificationException;
import io.neural.util.Base64;

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
