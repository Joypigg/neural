package cn.ms.neural.jwt.support;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * RSA Public/Private Key provider.
 */
public interface RSAKeyProvider extends KeyProvider<RSAPublicKey, RSAPrivateKey> {
}
