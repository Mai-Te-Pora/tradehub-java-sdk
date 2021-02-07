package org.tradehub.adapter.crypto;

import com.adelean.inject.resources.junit.jupiter.TestWithResources;
import org.bitcoinj.core.Bech32;
import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.Test;
import org.tradehub.domain.message.FeeBuilder;
import org.tradehub.domain.message.TradehubMessageSignature;
import org.tradehub.domain.message.TradehubMessageSignatureBuilder;
import org.tradehub.domain.message.payload.CreateOrderBuilder;
import org.tradehub.domain.message.payload.Side;
import org.tradehub.domain.util.JacksonSerializer;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestWithResources
class TradehubCryptoProviderTest {

    private final TradehubCryptoProvider classUnderTest = new TradehubCryptoProvider();
    private final JacksonSerializer jacksonSerializer = new JacksonSerializer();
    private final String mnemonic = "shell scatter method illegal area bid law genius found maze hope negative kit soldier promote various power true reward success own decrease retire raven";
    private final String mnemonicPriv = "c5780e30b880b42c1f7b075c3131b5c9f7af9d2b0f8ca95d2ae2ec29e0726dfd";
    private final String general_address = "swth1fshaavu32cmxuae34tsuskfqxr7ch5sr43crvj";
    private final String password = "mypassword";

    @Test
    void generateKeyStore_when_correct_then_keyStoreGetsGenerated() throws Exception {
        final Optional<KeyStore> keyStore = classUnderTest.generateNewKeystore(password);
        assertTrue(keyStore.isPresent());
        assertNotNull(keyStore.get().getKey(TradehubCryptoProvider.KEY_NAME, password.toCharArray()));
    }

    @Test
    void loadKeyPairFromKeyStore_when_wrongPass_then_OptionalIsEmpty() throws Exception {
        final Optional<KeyStore> keyStore = classUnderTest.generateNewKeystore(password);
        assertTrue(keyStore.isPresent());
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            keyStore.get().store(out, password.toCharArray());
            assertTrue(classUnderTest.getKeyPairFromKeyStore(out.toByteArray(), "wrongpass").isEmpty());
        }
    }

    @Test
    void loadKeyPairFromKeyStore_when_correctPass_then_loadKeystore() throws Exception {
        final Optional<KeyStore> keyStore = classUnderTest.generateNewKeystore(password);
        assertTrue(keyStore.isPresent());
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            keyStore.get().store(out, password.toCharArray());
            final Optional<KeyPair> pair = classUnderTest.getKeyPairFromKeyStore(out.toByteArray(), password);
            assertTrue(pair.isPresent());
            assertEquals(pair.get().getPrivate().hashCode(), keyStore.get().getKey(TradehubCryptoProvider.KEY_NAME, password.toCharArray()).hashCode());
        }
    }

    @Test
    void verifySignature_when_correctPublicKey_then_correctSignature() throws Exception {
        final Optional<KeyStore> keyStore = classUnderTest.generateNewKeystore(password);
        assertTrue(keyStore.isPresent());
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            keyStore.get().store(out, password.toCharArray());
            Optional<KeyPair> keyPair = classUnderTest.getKeyPairFromKeyStore(out.toByteArray(), password);
            assertTrue(keyPair.isPresent());
            byte[] data = new byte[20];
            new SecureRandom().nextBytes(data);
            Optional<byte[]> signatureBytes = classUnderTest.getSignature((ECPrivateKey) keyPair.get().getPrivate(), data);
            assertTrue(signatureBytes.isPresent());
            assertTrue(classUnderTest.verifySignature((ECPublicKey) keyPair.get().getPublic(), data, signatureBytes.get()));
        }
    }

    @Test
    void encodedToPublicKey_when_correctBytes_then_correctKeys() throws Exception {
        final Optional<KeyStore> keyStore = classUnderTest.generateNewKeystore(password);
        assertTrue(keyStore.isPresent());
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            keyStore.get().store(out, password.toCharArray());
            final Optional<KeyPair> keyPair = classUnderTest.getKeyPairFromKeyStore(out.toByteArray(), password);
            assertTrue(keyPair.isPresent());
            final ECPublicKey publicKey = (ECPublicKey) keyPair.get().getPublic();
            byte[] encodedBytes = publicKey.getEncoded();
            final Optional<ECPublicKey> publicKey2 = classUnderTest.generateNewPublicKeyFromEncodedBytes(encodedBytes);
            assertTrue(publicKey2.isPresent());
            assertArrayEquals(publicKey.getEncoded(), publicKey2.get().getEncoded());
            assertEquals(publicKey.hashCode(), publicKey2.hashCode());
        }
    }

    @Test
    void generateKeyStoreFromMnemonic_when_validMnemonic_then_generateKeystore() throws Exception {
        final Optional<KeyStore> keyStore = classUnderTest.generateNewKeyStoreFromMnemonic(password, mnemonic);
        assertTrue(keyStore.isPresent());
        final ECPrivateKey privateKey = (ECPrivateKey) keyStore.get().getKey(TradehubCryptoProvider.KEY_NAME, password.toCharArray());
        final Optional<ECPrivateKey> mnemonicPrivKey = classUnderTest.generateNewECPrivateKeyFromRawString(mnemonicPriv);
        assertTrue(mnemonicPrivKey.isPresent());
        assertEquals(mnemonicPrivKey.hashCode(), privateKey.hashCode());
    }

    @Test
    void privateKeyFromMnemonic_when_givenStaticMnemonic_then_givenStaticAddress() throws Exception {
        // arrange
        final Optional<KeyStore> keyStore = classUnderTest.generateNewKeyStoreFromMnemonic(password, mnemonic);
        assertTrue(keyStore.isPresent());
        final ECPrivateKey privateKey = (ECPrivateKey) keyStore.get().getKey(TradehubCryptoProvider.KEY_NAME, password.toCharArray());
        System.out.println(Hex.toHexString(privateKey.getS().toByteArray()));
        final byte[] compressedPubKeyBytes = classUnderTest.getCompressedPubKeyBytesFromPrivKey(privateKey);
        System.out.println(Hex.toHexString(compressedPubKeyBytes));
        final String hrp = "swth";

        // act
        final byte[] ripeMDHashBytes = classUnderTest.getSha256AndRipeMD160FromBytes(compressedPubKeyBytes);
        System.out.println(Hex.toHexString(ripeMDHashBytes));
        final String binaryString = classUnderTest.getBinaryStringFromBytes(ripeMDHashBytes);
        final byte[] bech32Result = classUnderTest.getBech32AddressFromBytes(binaryString, new byte[32], 0);
        final String bech32Address = Bech32.encode(hrp, bech32Result);

        // assert
        assertEquals(general_address, bech32Address);
    }

    @Test
    void bech32FromPublicKey_when_staticInput_then_staticOutput() {
        // arrange
        final String publicKeyRawCompressed = "0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798";
        final String hrp = "swth";
        final String expectedRipeMDHex = "751e76e8199196d454941c45d1b3a323f1433bd6";
        final String expectedBinaryString = "01110101000111100111011011101000000110011001000110010110110101000101010010" +
                "01010000011100010001011101000110110011101000110010001111110001010000110011101111010110";
        final String expectedResultHex = "0e140f070d1a001912060b0d081504140311021d030c1d03040f1814060e1e16";
        final String expectedAddress = "swth1w508d6qejxtdg4y5r3zarvary0c5xw7k9ravup";

        // act
        final byte[] ripeMDHashBytes = classUnderTest.getSha256AndRipeMD160FromBytes(Hex.decode(publicKeyRawCompressed));
        final String ripeMDHex = Hex.toHexString(ripeMDHashBytes);
        final String binaryString = classUnderTest.getBinaryStringFromBytes(ripeMDHashBytes);
        final byte[] bech32Result = classUnderTest.getBech32AddressFromBytes(binaryString, new byte[32], 0);
        final String resultHex = Hex.toHexString(bech32Result);
        final String bech32Address = Bech32.encode(hrp, Hex.decode(resultHex));

        // assert
        assertEquals(expectedRipeMDHex, ripeMDHex);
        assertEquals(expectedBinaryString, binaryString);
        assertEquals(expectedResultHex, resultHex);
        assertEquals(expectedAddress, bech32Address);
    }

    @Test
    void getBase64SignatureFromBytes_when_tradehubMessageSignature_then_base64Signature() throws Exception {
        // arrange
        final TradehubMessageSignature payload = new TradehubMessageSignatureBuilder()
                .withAccountNumber(5L)
                .withChainId("3")
                .withFee(new FeeBuilder()
                        .withAmount("swth", 100000000L)
                        .withGas(100000000000L)
                        .build())
                .withTradehubMessages(Collections.singletonList(new CreateOrderBuilder()
                        .withMarket("swth_eth")
                        .withSide(Side.SELL)
                        .withQuantity(new BigDecimal(200))
                        .withPrice(1.01)
                        .withOriginator("swth1z2jz4uhz8zgt4lq9mq5slz3ukyp3grhl7nsr4x")
                        .build()))
                .withSequence(1L)
                .build();

        final String payloadJson = jacksonSerializer.getStringFromRequestObject(payload);

        final Optional<KeyStore> keyStore = classUnderTest.generateNewKeystore(password);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            keyStore.get().store(out, password.toCharArray());
            Optional<KeyPair> keyPair = classUnderTest.getKeyPairFromKeyStore(out.toByteArray(), password);
            Optional<String> signature = classUnderTest.getBase64SignatureFromBytes((ECPrivateKey) keyPair.get().getPrivate(),
                    payloadJson.getBytes(StandardCharsets.UTF_8));
            assertTrue(signature.isPresent());
        }
    }
}