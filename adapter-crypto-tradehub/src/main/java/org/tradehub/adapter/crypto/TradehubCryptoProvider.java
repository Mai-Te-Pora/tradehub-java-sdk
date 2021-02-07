package org.tradehub.adapter.crypto;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.digest.RIPEMD160;
import org.bouncycastle.jcajce.provider.digest.SHA256;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.spec.ECNamedCurveSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.util.encoders.Hex;
import org.tradehub.domain.gateway.TradehubCrypto;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECPrivateKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import static java.lang.String.format;

public class TradehubCryptoProvider implements TradehubCrypto {

    public static final String EC_CURVE = "secp256k1";
    public static final String KEYSTORE_FORMAT = "UBER";
    public static final String ROOTNAME = "CN=tradehub.org";
    public static final String ALGORITHM = "ECDSA";
    public static final String PROVIDER = "BC";
    public static final String SIGNER_ALGORITHM = "SHA256withECDSA";
    public static final String KEY_NAME = "swth";
    public static final MessageDigest ripeMd160 = new RIPEMD160.Digest();
    static final MessageDigest sha256 = new SHA256.Digest();
    static final ECNamedCurveParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec(EC_CURVE);
    static final ECNamedCurveSpec params = new ECNamedCurveSpec(EC_CURVE, ecSpec.getCurve(), ecSpec.getG(), ecSpec.getN());
    private static final Logger log = LogManager.getLogger();

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Override
    public Optional<KeyStore> generateNewKeystore(final String password) {
        try {
            final ECNamedCurveParameterSpec ecGenSpec = ECNamedCurveTable.getParameterSpec(EC_CURVE);
            final KeyPairGenerator g = KeyPairGenerator.getInstance(ALGORITHM, PROVIDER);
            g.initialize(ecGenSpec, new SecureRandom());
            final KeyPair keyPair = g.generateKeyPair();
            return saveKeyPairToKeyStore(password, keyPair);
        } catch (Exception e) {
            log.error(format("Error in generateNewKeystore: %s", e.getMessage()));
            return Optional.empty();
        }
    }

    @Override
    public Optional<KeyStore> saveKeyPairToKeyStore(final String password, final KeyPair keyPair) {
        try {
            final KeyStore keyStore = KeyStore.getInstance(KEYSTORE_FORMAT);
            keyStore.load(null, password.toCharArray());
            final X509Certificate[] certificateChain = new X509Certificate[1];
            certificateChain[0] = generateCertificate(keyPair);
            keyStore.setKeyEntry(KEY_NAME, keyPair.getPrivate(), password.toCharArray(), certificateChain);
            return Optional.of(keyStore);
        } catch (Exception e) {
            log.error(format("Error in saveKeyPairToKeyStore: %s", e.getMessage()));
            return Optional.empty();
        }
    }

    @Override
    public Optional<KeyStore> generateNewKeyStoreFromRawString(final String password, final String raw) {
        try {
            final Optional<ECPrivateKey> privateKeyOpt = generateNewECPrivateKeyFromRawString(raw);
            if (privateKeyOpt.isPresent()) {
                final BCECPrivateKey bcec = (BCECPrivateKey) privateKeyOpt.get();
                final KeyFactory kf = KeyFactory.getInstance(ALGORITHM, PROVIDER);
                final ECPoint Q = ecSpec.getG().multiply(bcec.getD());
                final byte[] publicBytes = Q.getEncoded(false);
                final ECPoint point = ecSpec.getCurve().decodePoint(publicBytes);
                final ECPublicKeySpec pubSpec = new ECPublicKeySpec(point, ecSpec);
                final ECPublicKey publicKey = (ECPublicKey) kf.generatePublic(pubSpec);
                final KeyPair keyPair = new KeyPair(publicKey, privateKeyOpt.get());
                return saveKeyPairToKeyStore(password, keyPair);
            } else return Optional.empty();
        } catch (Exception e) {
            log.error(format("Error in generateNewKeyStoreFromRawString: %s", e.getMessage()));
            return Optional.empty();
        }
    }

    @Override
    public Optional<KeyStore> generateNewKeyStoreFromMnemonic(final String password, final String mnemonic) {
        final String rawKey = Hex.toHexString(MnemonicService.generateEntropy(mnemonic));
        return generateNewKeyStoreFromRawString(password, rawKey);
    }

    @Override
    public Optional<ECPrivateKey> generateNewECPrivateKeyFromRawString(final String raw) {
        try {
            final ECPrivateKeySpec ecPrivateKeySpec = new ECPrivateKeySpec(new BigInteger(raw, 16), params);
            final KeyFactory kf = KeyFactory.getInstance(ALGORITHM, PROVIDER);
            return Optional.of((ECPrivateKey) kf.generatePrivate(ecPrivateKeySpec));
        } catch (Exception e) {
            log.error(format("Error in generateNewECPrivateKeyFromRawString: %s", e.getMessage()));
            return Optional.empty();
        }
    }

    @Override
    public Optional<KeyPair> getKeyPairFromKeyStore(byte[] keystoreBytes, String password) {
        try {
            final KeyStore keyStore = KeyStore.getInstance(KEYSTORE_FORMAT);
            try (InputStream in = new ByteArrayInputStream(keystoreBytes)) {
                keyStore.load(in, password.toCharArray());
            }
            final PrivateKey privateKey = (PrivateKey) keyStore.getKey(KEY_NAME, password.toCharArray());
            final X509Certificate certificate = (X509Certificate) keyStore.getCertificate(KEY_NAME);
            final PublicKey publicKey = certificate.getPublicKey();
            return Optional.of(new KeyPair(publicKey, privateKey));
        } catch (Exception e) {
            log.error(format("Error in getKeyPairFromKeyStore: %s", e.getMessage()));
            return Optional.empty();
        }
    }

    @Override
    public Optional<byte[]> getSignature(ECPrivateKey privateKey, byte[] data) {
        try {
            final Signature signature = Signature.getInstance(SIGNER_ALGORITHM, PROVIDER);
            signature.initSign(privateKey);
            signature.update(data);
            return Optional.of(signature.sign());
        } catch (Exception e) {
            log.error(format("Error in getSignature: %s", e.getMessage()));
            return Optional.empty();
        }
    }

    @Override
    public Optional<ECPublicKey> generateNewPublicKeyFromEncodedBytes(byte[] bytes) {
        try {
            KeyFactory factory = KeyFactory.getInstance(ALGORITHM, PROVIDER);
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(bytes);
            return Optional.of((ECPublicKey) factory.generatePublic(x509EncodedKeySpec));
        } catch (Exception e) {
            log.error(format("Error in generateNewPublicKeyFromEncodedBytes: %s", e.getMessage()));
            return Optional.empty();
        }
    }

    @Override
    public boolean verifySignature(ECPublicKey publicKey, byte[] data, byte[] signatureBytes) {
        try {
            final Signature signature = Signature.getInstance(SIGNER_ALGORITHM, PROVIDER);
            signature.initVerify(publicKey);
            signature.update(data);
            return signature.verify(signatureBytes);
        } catch (Exception e) {
            log.error(format("Error in verifySignature: %s", e.getMessage()));
            return false;
        }
    }

    @Override
    public byte[] getCompressedPubKeyBytesFromPrivKey(ECPrivateKey privateKey) {
        final X9ECParameters params = SECNamedCurves.getByName(EC_CURVE);
        final ECPoint point = params.getG().multiply(privateKey.getS());
        return point.getEncoded(true);
    }

    @Override
    public byte[] getSha256AndRipeMD160FromBytes(byte[] bytes) {
        return ripeMd160.digest(sha256.digest(bytes));
    }

    @Override
    public byte[] getSha256FromBytes(byte[] bytes) {
        return sha256.digest(bytes);
    }

    @Override
    public byte[] getBech32AddressFromBytes(final String binaryString, final byte[] result, final int pos) {
        if (binaryString.length() == 0) return result;
        result[pos] = Byte.valueOf(binaryString.substring(0, 5), 2);
        return getBech32AddressFromBytes(binaryString.substring(5), result, pos + 1);
    }

    @Override
    public String getBinaryStringFromBytes(byte[] bytes) {
        final StringBuilder sb = new StringBuilder();
        for (byte b1 : bytes) {
            sb.append(String.format("%8s", Integer.toBinaryString(b1 & 0xFF)).replace(' ', '0'));
        }
        return sb.toString();
    }

    @Override
    public Optional<String> getBase64SignatureFromBytes(final ECPrivateKey privateKey, final byte[] bytes) {
        return getSignature(privateKey, bytes)
                .map(signature -> Base64.getEncoder().encodeToString(signature));
    }

    private X509Certificate generateCertificate(final KeyPair keyPair) throws Exception {
        final Calendar calendar = Calendar.getInstance();
        final Date validFrom = calendar.getTime();
        calendar.add(Calendar.YEAR, 1000);
        final Date validUntil = calendar.getTime();
        final X509v3CertificateBuilder builder = new JcaX509v3CertificateBuilder(
                new X500Name(ROOTNAME),
                new BigInteger(64, new SecureRandom()),
                validFrom, validUntil,
                new X500Name(ROOTNAME),
                keyPair.getPublic());
        final ContentSigner signer = new JcaContentSignerBuilder(SIGNER_ALGORITHM).build(keyPair.getPrivate());
        final X509CertificateHolder certHolder = builder.build(signer);
        final X509Certificate cert = new JcaX509CertificateConverter().setProvider(PROVIDER).getCertificate(certHolder);
        cert.verify(keyPair.getPublic());
        return cert;
    }
}
