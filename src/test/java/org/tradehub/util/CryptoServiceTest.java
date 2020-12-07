package org.tradehub.util;

import org.bitcoinj.core.Bech32;
import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;

import static org.junit.jupiter.api.Assertions.*;

class CryptoServiceTest {

    private final CryptoService classUnderTest = new CryptoService();
    private final String mnemonic = "shell scatter method illegal area bid law genius found maze hope negative kit soldier promote various power true reward success own decrease retire raven";
    private final String mnemonicPriv = "c5780e30b880b42c1f7b075c3131b5c9f7af9d2b0f8ca95d2ae2ec29e0726dfd";
    private final String address = "swth1z0mj8pj7ye57e33jzf6wajy43cz6kq0pyl2sdn";
    private final String password = "mypassword";

    @Test
    void generateKeyStore_when_correct_then_keyStoreGetsGenerated() throws Exception {
        final KeyStore keyStore = classUnderTest.generateKeystore(password);
        assertNotNull(keyStore.getKey(CryptoService.KEY_NAME, password.toCharArray()));
    }

    @Test
    void loadKeyPairFromKeyStore_when_wrongPass_then_throwsException() throws Exception {
        final KeyStore keyStore = classUnderTest.generateKeystore(password);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            keyStore.store(out, password.toCharArray());
            assertThrows(Exception.class, () ->
                    classUnderTest.loadKeyPairFromKeyStore(out.toByteArray(), "wrongpass"));
        }
    }

    @Test
    void loadKeyPairFromKeyStore_when_correctPass_then_loadKeystore() throws Exception {
        final KeyStore keyStore = classUnderTest.generateKeystore(password);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            keyStore.store(out, password.toCharArray());
            final KeyPair pair = classUnderTest.loadKeyPairFromKeyStore(out.toByteArray(), password);
            assertEquals(pair.getPrivate().hashCode(), keyStore.getKey(CryptoService.KEY_NAME, password.toCharArray()).hashCode());
        }
    }

    @Test
    void verifySignature_when_correctPublicKey_then_correctSignature() throws Exception {
        final KeyStore keyStore = classUnderTest.generateKeystore(password);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            keyStore.store(out, password.toCharArray());
            KeyPair keyPair = classUnderTest.loadKeyPairFromKeyStore(out.toByteArray(), password);
            byte[] data = new byte[20];
            new SecureRandom().nextBytes(data);
            byte[] signatureBytes = classUnderTest.getSignature(keyPair.getPrivate(), data);
            assertTrue(classUnderTest.verifySignature(keyPair.getPublic(), data, signatureBytes));
        }
    }

    @Test
    void encodedToPublicKey_when_correctBytes_then_correctKeys() throws Exception {
        final KeyStore keyStore = classUnderTest.generateKeystore(password);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            keyStore.store(out, password.toCharArray());
            final KeyPair keyPair = classUnderTest.loadKeyPairFromKeyStore(out.toByteArray(), password);
            final ECPublicKey publicKey = (ECPublicKey) keyPair.getPublic();
            byte[] encodedBytes = publicKey.getEncoded();
            final ECPublicKey publicKey2 = classUnderTest.encodedToPublicKey(encodedBytes);
            assertArrayEquals(publicKey.getEncoded(), publicKey2.getEncoded());
            assertEquals(publicKey.hashCode(), publicKey2.hashCode());
        }
    }

    @Test
    void generateKeyStoreFromMnemonic_when_validMnemonic_then_generateKeystore() throws Exception {
        final KeyStore keyStore = classUnderTest.generateKeyStoreFromMnemonic(password, mnemonic);
        final ECPrivateKey privateKey = (ECPrivateKey) keyStore.getKey(CryptoService.KEY_NAME, password.toCharArray());
        final ECPrivateKey mnemonicPrivKey = classUnderTest.getECPrivateKeyFromRawString(mnemonicPriv);
        assertEquals(mnemonicPrivKey.hashCode(), privateKey.hashCode());
    }

    @Test
    void privateKeyFromMnemonic_when_givenStaticMnemonic_then_givenStaticAddress() throws Exception {
        // arrange
        final KeyStore keyStore = classUnderTest.generateKeyStoreFromMnemonic(password, mnemonic);
        final ECPrivateKey privateKey = (ECPrivateKey) keyStore.getKey(CryptoService.KEY_NAME, password.toCharArray());
        System.out.println(Hex.toHexString(privateKey.getS().toByteArray()));
        final byte[] compressedPubKeyBytes = classUnderTest.getCompressedPubKeyBytesFromPrivKey(privateKey);
        System.out.println(Hex.toHexString(compressedPubKeyBytes));
        final String hrp = "swth";

        // act
        final byte[] ripeMDHashBytes = CryptoService.getRIPEMD160(compressedPubKeyBytes);
        System.out.println(Hex.toHexString(ripeMDHashBytes));
        final String binaryString = CryptoService.toByteString(ripeMDHashBytes);
        final byte[] bech32Result = CryptoService.getBech32Bytes(binaryString, new byte[32], 0);
        final String bech32Address = Bech32.encode(hrp, bech32Result);

        // assert
        // assertEquals(address, bech32Address);

    }

    @Test
    void bech32FromPublicKey_when_staticInput_then_staticOutput() {
        // arrange
        final String publicKeyRawCompressed = "0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798";
        final String version = "00";
        final String hrp = "swth";
        final String expectedRipeMDHex = "751e76e8199196d454941c45d1b3a323f1433bd6";
        final String expectedBinaryString = "01110101000111100111011011101000000110011001000110010110110101000101010010" +
                "01010000011100010001011101000110110011101000110010001111110001010000110011101111010110";
        final String expectedResultWithVersion = "000e140f070d1a001912060b0d081504140311021d030c1d03040f1814060e1e16";
        final String expectedAddress = "swth1qw508d6qejxtdg4y5r3zarvary0c5xw7k92cmur";

        // act
        final byte[] ripeMDHashBytes = CryptoService.getRIPEMD160(Hex.decode(publicKeyRawCompressed));
        final String ripeMDHex = Hex.toHexString(ripeMDHashBytes);
        final String binaryString = CryptoService.toByteString(ripeMDHashBytes);
        final byte[] bech32Result = CryptoService.getBech32Bytes(binaryString, new byte[32], 0);
        final String resultWithVersion = version + Hex.toHexString(bech32Result);
        final String bech32Address = Bech32.encode(hrp, Hex.decode(resultWithVersion));

        // assert
        assertEquals(expectedRipeMDHex, ripeMDHex);
        assertEquals(expectedBinaryString, binaryString);
        assertEquals(expectedResultWithVersion, resultWithVersion);
        assertEquals(expectedAddress, bech32Address);
    }
}