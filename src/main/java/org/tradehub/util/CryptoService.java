package org.tradehub.util;

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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECPrivateKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Calendar;
import java.util.Date;

public class CryptoService {

    public static final String EC_CURVE = "secp256k1";
    public static final String KEYSTORE_FORMAT = "UBER";
    public static final String ROOTNAME = "CN=tradehub.org";
    public static final String ALGORITHM = "ECDSA";
    public static final String PROVIDER = "BC";
    public static final String SIGNER_ALGORITHM = "SHA256withECDSA";
    public static final String KEY_NAME = "swth";
    static final MessageDigest ripeMd160 = new RIPEMD160.Digest();
    static final MessageDigest sha256 = new SHA256.Digest();
    static final ECNamedCurveParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec(EC_CURVE);
    static final ECNamedCurveSpec params = new ECNamedCurveSpec(EC_CURVE, ecSpec.getCurve(), ecSpec.getG(), ecSpec.getN());

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public KeyStore generateKeystore(final String password) throws Exception {
        final ECNamedCurveParameterSpec ecGenSpec = ECNamedCurveTable.getParameterSpec(EC_CURVE);
        final KeyPairGenerator g = KeyPairGenerator.getInstance(ALGORITHM, PROVIDER);
        g.initialize(ecGenSpec, new SecureRandom());
        final KeyPair keyPair = g.generateKeyPair();
        return keyPairToKeyStore(password, keyPair);
    }

    public KeyStore keyPairToKeyStore(final String password, final KeyPair keyPair) throws Exception {
        final KeyStore keyStore = KeyStore.getInstance(KEYSTORE_FORMAT);
        keyStore.load(null, password.toCharArray());
        final X509Certificate[] certificateChain = new X509Certificate[1];
        certificateChain[0] = generateCertificate(keyPair);
        keyStore.setKeyEntry(KEY_NAME, keyPair.getPrivate(), password.toCharArray(), certificateChain);
        return keyStore;
    }

    public KeyStore generateKeyStoreFromRawString(final String password, final String raw) throws Exception {
        final ECPrivateKey privateKey = getECPrivateKeyFromRawString(raw);
        final BCECPrivateKey bcec = (BCECPrivateKey) privateKey;
        final KeyFactory kf = KeyFactory.getInstance(ALGORITHM, PROVIDER);
        final ECPoint Q = ecSpec.getG().multiply(bcec.getD());
        final byte[] publicBytes = Q.getEncoded(false);
        final ECPoint point = ecSpec.getCurve().decodePoint(publicBytes);
        final ECPublicKeySpec pubSpec = new ECPublicKeySpec(point, ecSpec);
        final ECPublicKey publicKey = (ECPublicKey) kf.generatePublic(pubSpec);
        final KeyPair keyPair = new KeyPair(publicKey, privateKey);
        return keyPairToKeyStore(password, keyPair);
    }

    public KeyStore generateKeyStoreFromMnemonic(final String password, final String mnemonic) throws Exception {
        final String rawKey = Hex.toHexString(MnemonicService.generateEntropy(mnemonic));
        return generateKeyStoreFromRawString(password, rawKey);
    }

    public ECPrivateKey getECPrivateKeyFromRawString(final String raw) throws Exception {
        final ECPrivateKeySpec ecPrivateKeySpec = new ECPrivateKeySpec(new BigInteger(raw, 16), params);
        final KeyFactory kf = KeyFactory.getInstance(ALGORITHM, PROVIDER);
        return (ECPrivateKey) kf.generatePrivate(ecPrivateKeySpec);
    }

    public KeyPair loadKeyPairFromKeyStore(final byte[] keystoreBytes, final String password) throws Exception {
        final KeyStore keyStore = KeyStore.getInstance(KEYSTORE_FORMAT);
        try (InputStream in = new ByteArrayInputStream(keystoreBytes)) {
            keyStore.load(in, password.toCharArray());
        }
        final PrivateKey privateKey = (PrivateKey) keyStore.getKey(KEY_NAME, password.toCharArray());
        final X509Certificate certificate = (X509Certificate) keyStore.getCertificate(KEY_NAME);
        final PublicKey publicKey = certificate.getPublicKey();
        return new KeyPair(publicKey, privateKey);
    }

    public byte[] getSignature(final PrivateKey privateKey, final byte[] data) throws Exception {
        final Signature signature = Signature.getInstance(SIGNER_ALGORITHM, PROVIDER);
        signature.initSign(privateKey);
        signature.update(data);
        return signature.sign();
    }

    public boolean verifySignature(final PublicKey publicKey, final byte[] data, final byte[] signatureBytes) throws Exception {
        final Signature signature = Signature.getInstance(SIGNER_ALGORITHM, PROVIDER);
        signature.initVerify(publicKey);
        signature.update(data);
        return signature.verify(signatureBytes);
    }

    public byte[] signBytes(final PrivateKey privateKey, final byte[] objToSign) throws Exception {
        final Signature signature = Signature.getInstance(SIGNER_ALGORITHM, PROVIDER);
        signature.initSign(privateKey);
        signature.update(objToSign);
        final byte[] signatureBytes = signature.sign();
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            try (DataOutputStream obj = new DataOutputStream(out)) {
                obj.write(objToSign);
                obj.write(signatureBytes.length);
                obj.write(signatureBytes);
                return out.toByteArray();
            }
        }
    }

    public ECPublicKey encodedToPublicKey(final byte[] bytes) throws Exception {
        KeyFactory factory = KeyFactory.getInstance(ALGORITHM, PROVIDER);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(bytes);
        return (ECPublicKey) factory.generatePublic(x509EncodedKeySpec);
    }

    public byte[] getCompressedPubKeyBytesFromPrivKey(final ECPrivateKey privateKey) {
        final X9ECParameters params = SECNamedCurves.getByName(CryptoService.EC_CURVE);
        final ECPoint point = params.getG().multiply(privateKey.getS());
        return point.getEncoded(true);
    }

    private X509Certificate generateCertificate(final KeyPair keyPair) throws Exception {
        Calendar calendar = Calendar.getInstance();
        Date validFrom = calendar.getTime();
        calendar.add(Calendar.YEAR, 1000);
        Date validUntil = calendar.getTime();
        X509v3CertificateBuilder builder = new JcaX509v3CertificateBuilder(
                new X500Name(ROOTNAME),
                new BigInteger(64, new SecureRandom()),
                validFrom, validUntil,
                new X500Name(ROOTNAME),
                keyPair.getPublic());
        ContentSigner signer = new JcaContentSignerBuilder(SIGNER_ALGORITHM).build(keyPair.getPrivate());
        X509CertificateHolder certHolder = builder.build(signer);
        X509Certificate cert = new JcaX509CertificateConverter().setProvider(PROVIDER).getCertificate(certHolder);
        cert.verify(keyPair.getPublic());
        return cert;
    }

    public static byte[] getRIPEMD160(final byte[] bytes) {
        return ripeMd160.digest(sha256.digest(bytes));
    }

    public static byte[] getBech32Bytes(final String binaryString, final byte[] result, final int pos) {
        if (binaryString.length() == 0) return result;
        result[pos] = Byte.valueOf(binaryString.substring(0, 5), 2);
        return getBech32Bytes(binaryString.substring(5), result, pos + 1);
    }

    public static String toByteString(final byte[] bytes) {
        final StringBuilder sb = new StringBuilder();
        for (byte b1 : bytes) {
            sb.append(String.format("%8s", Integer.toBinaryString(b1 & 0xFF)).replace(' ', '0'));
        }
        return sb.toString();
    }

}
