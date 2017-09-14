package cn.ms.neural.jwt.support;

import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;

/**
 * Elliptic Curve (EC) Public/Private Key provider.
 */
public interface ECDSAKeyProvider extends KeyProvider<ECPublicKey, ECPrivateKey> {
}
