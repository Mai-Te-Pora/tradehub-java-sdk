package org.tradehub.application.gateway;

import java.security.KeyPair;
import java.security.KeyStore;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.util.Optional;

public interface TradehubCrypto {

    Optional<KeyStore> generateNewKeystore(String password);

    Optional<KeyStore> saveKeyPairToKeyStore(String password, KeyPair keyPair);

    Optional<KeyStore> generateNewKeyStoreFromRawString(String password, String raw);

    Optional<KeyStore> generateNewKeyStoreFromMnemonic(String password, String mnemonic);

    Optional<ECPrivateKey> generateNewECPrivateKeyFromRawString(String raw);

    Optional<KeyPair> getKeyPairFromKeyStore(byte[] keystoreBytes, String password);

    Optional<byte[]> getSignature(ECPrivateKey privateKey, byte[] data);

    Optional<ECPublicKey> generateNewPublicKeyFromEncodedBytes(byte[] bytes);

    boolean verifySignature(ECPublicKey publicKey, byte[] data, byte[] signatureBytes);

    byte[] getCompressedPubKeyBytesFromPrivKey(ECPrivateKey privateKey);

    byte[] getSha256AndRipeMD160FromBytes(byte[] bytes);

    byte[] getSha256FromBytes(byte[] bytes);

    byte[] getBech32AddressFromBytes(String binaryString, byte[] result, int pos);

    String getBinaryStringFromBytes(byte[] bytes);
}
