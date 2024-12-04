package com.rtk.mdm.esia.service;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cms.*;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.DigestCalculatorProvider;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.util.Store;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.*;


//пример сформированной урлы:
//http://esia-portal1.test.gosuslugi.ru/aas/oauth2/ac?client_id=GOST_METROLOGY&client_secret=MIAGCSqGSIb3DQEHAqCAMIACAQExDzANBglghkgBZQMEAgEFADCABgkqhkiG9w0BBwGggCSABFpvcGVuaWQgZnVsbG5hbWUyMDE3LjAzLjA4IDEyOjE3OjM5ICswMzAwR09TVF9NRVRST0xPR1k3ZWE0ODIzYS0yMDUwLTRhYTMtOWJiNi04MzdmMTk0YWYwNjUAAAAAAACggDCCA4MwggJroAMCAQICBCKb6n4wDQYJKoZIhvcNAQELBQAwcjELMAkGA1UEBhMCUlUxDzANBgNVBAgTBlJ1c3NpYTEPMA0GA1UEBxMGTW9zY293MQ0wCwYDVQQKEwRHT1NUMRIwEAYDVQQLEwlNZXRyb2xvZ3kxHjAcBgNVBAMTFUdPU1QgTWV0cm9sb2d5IFBvcnRhbDAeFw0xNzAyMjcxMjM1MTlaFw00NDA3MTUxMjM1MTlaMHIxCzAJBgNVBAYTAlJVMQ8wDQYDVQQIEwZSdXNzaWExDzANBgNVBAcTBk1vc2NvdzENMAsGA1UEChMER09TVDESMBAGA1UECxMJTWV0cm9sb2d5MR4wHAYDVQQDExVHT1NUIE1ldHJvbG9neSBQb3J0YWwwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCGtPUPZz9aJZUQsa27AhBFhkb102HtkeKbFywQusM49dSEeqnfA5RuZdfNzhh691%2BAKTiNNSX59FeW8hZcnGEdmobnAZSTcdt%2Ba4pGSN%2BiWF04Z8HLpdYBBcmkHAWUhQttyeR4KgxAOY35L4j78C0NtT1HTcu6ZCH2KvbGbr6d6x35XeQbX6YV9W9%2BfPpHUtKc137qvCwiunuTI6BcPFNd9e1QEED56IT7of935lnFyCRrmQ%2BSJevYmDavzNp8rrMCH4i%2FmRLf%2FH8%2FQQJ7fojjJdf4ElPnJSex0ndDoJAVYa2zL1caRBCRDmVQno%2Fx7VAONkHWZNkWPEjuY5Cf%2BBi5AgMBAAGjITAfMB0GA1UdDgQWBBQ3blLclFC5XE8ViBz7jOVOeaAO%2FTANBgkqhkiG9w0BAQsFAAOCAQEAQ4zZQgoNbY73cXavh%2Fhg8GtPA9KDLQF2rJ8RKTN%2BkhCACOVETDIGzwGYanw0i3Oxd1XBgBl6KeyK%2FO0ipUxD9lMiWaBSLVxEpHIcC%2Fsmp0cd6eCfRva5lwfvSXq%2BnVlTQT8UGKB35Jr5gji%2B91C3RQkKSKhdsNTaOoCWYZXgzTUHkbtDnKjaF%2Fk%2B%2F%2BLJlztadmrZLdwVAfUtmhJmZTrkk3o3Gjfn4ctp%2B3iedioIjxIU7pMYmUrw7vDs%2BVDVjw01sFPMZgjf6t2PEAiDOKqOqD8Md%2BSFL1wGgOmBbHXHOI89i9%2Bp%2BxVDLNpGRKcu3qJjJD3l0c52zuc9lJcUBckonAAAMYICEDCCAgwCAQEwejByMQswCQYDVQQGEwJSVTEPMA0GA1UECBMGUnVzc2lhMQ8wDQYDVQQHEwZNb3Njb3cxDTALBgNVBAoTBEdPU1QxEjAQBgNVBAsTCU1ldHJvbG9neTEeMBwGA1UEAxMVR09TVCBNZXRyb2xvZ3kgUG9ydGFsAgQim%2Bp%2BMA0GCWCGSAFlAwQCAQUAoGkwGAYJKoZIhvcNAQkDMQsGCSqGSIb3DQEHATAcBgkqhkiG9w0BCQUxDxcNMTcwMzA4MDkxNzM5WjAvBgkqhkiG9w0BCQQxIgQgwf6FPJGg47R9%2BqU6kOms0mWLb119T5OeVISJPivUdA0wDQYJKoZIhvcNAQEBBQAEggEANlrd8iPKK1a%2BWT0XkdsMOqbjUGyY9I6D8mn0UYv0xep2QznZDS8MawE2Ej%2Bq%2Fazny35iXbPU%2FSykhy83%2FCRRh3WvlR3lBIbX33k8ahmF1AF0RiuxnfjJl9dH9cMCZNqyvx8kjCU5Eg5qZN68qoLY5TwWZ4TVGeReujczVncZPirspGN0A%2BEwx6KpxdQbBrMPtNn4fpu6H8tBatwaIIzpInF%2BEmxLaq94fKNeoLKK1nNdCP6S1zJlXCSSxEbJneEV0PJlRd8IwpJuwSYE5BPZxcVtHLFycUbIJOXHsRa7ja5naAALs3hsUzikHPbBBOkmOMqr3lZRpEHeEkxVIhsO3QAAAAAAAA%3D%3D&redirect_uri=http://localhost:8080/callback&scope=openid fullname&response_type=code&state=7ea4823a-2050-4aa3-9bb6-837f194af065&timestamp=2017.03.08+12%3A17%3A39+%2B0300&access_type=offline

//после авторизации у ЕСИА она светит вот таким урлом
//https://esia-portal1.test.gosuslugi.ru/aas/oauth2/?error=unauthorized_client&state=7ea4823a-2050-4aa3-9bb6-837f194af065&error_description=ESIA-007005%3A+The+client+is+not+authorized+to+request+an+access+token+using+this+method.

@Service
public class SignerService {

    @Value("${esia.keystore.path}")
    private String keyStorePath;

    @Value("${esia.keystore.password}")
    private String KEYSTORE_PASSWORD;

    @Value("${signature.algorithm}")
    private String SIGNATURE_ALGORITHM;

    @Value("${esia.keystore.key.alias}")
    private String KEY_ALIAS;

    private KeyStore loadKeyStore() throws Exception {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(keyStorePath);
        Security.addProvider(new BouncyCastleProvider());
        KeyStore keyStore = KeyStore.getInstance("PKCS12", "BC");
        keyStore.load(stream, KEYSTORE_PASSWORD.toCharArray());
        return keyStore;
    }

    private Key getKeyByKeyStore(KeyStore ks) throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException {
        String alias = ks.aliases().nextElement();
        return ks.getKey(alias, KEYSTORE_PASSWORD.toCharArray());
    }

    private CMSSignedDataGenerator getDataGenerator(KeyStore ks) throws Exception {
        String alias = ks.aliases().nextElement();
        java.security.cert.Certificate [] certs = ks.getCertificateChain(alias);
        List<java.security.cert.Certificate> certList = new ArrayList<>();
        for (int i = 0, length = certs == null ? 0 : certs.length; i < length; i++) {
            certList.add(certs[i]);
        }

        Store certstore = new JcaCertStore(certList);
        X509Certificate cert = (X509Certificate) ks.getCertificate(alias);

        PrivateKey privateKey = (PrivateKey) getKeyByKeyStore(ks);
        ContentSigner signer = new JcaContentSignerBuilder(SIGNATURE_ALGORITHM)
                .setProvider("BC")
                .build(privateKey);

        CMSSignedDataGenerator generator = new CMSSignedDataGenerator();
        DigestCalculatorProvider bc = new JcaDigestCalculatorProviderBuilder().setProvider("BC").build();
        generator.addSignerInfoGenerator(new JcaSignerInfoGeneratorBuilder(bc).build(signer, cert));
        generator.addCertificates(certstore);
        return generator;
    }

    public String check(byte[] data, byte[] signature){
        boolean checkResult = false;

        CMSProcessable signedContent = new CMSProcessableByteArray(data);
        CMSSignedData signedData;
        try {
            signedData = new CMSSignedData(signedContent, signature);
        } catch (CMSException e) {
            return "ERROR";
        }

        SignerInformation signer;
        try {
            Store<X509CertificateHolder> certStoreInSing = signedData.getCertificates();
            signer = signedData.getSignerInfos().getSigners().iterator().next();

            Collection certCollection = certStoreInSing.getMatches(signer.getSID());
            Iterator certIt = certCollection.iterator();

            X509CertificateHolder certHolder = (X509CertificateHolder) certIt.next();
            X509Certificate certificate = new JcaX509CertificateConverter().getCertificate(certHolder);

            checkResult = signer.verify(new JcaSimpleSignerInfoVerifierBuilder().build(certificate));

        } catch (Exception ex) {
            return "ERROR";
        }
        return String.valueOf(checkResult);
    }

    public byte[] signMessageGost(byte[] dataToSign) throws Exception {
        KeyStore keyStore = loadKeyStore();

        String alias = keyStore.aliases().nextElement();
        java.security.cert.Certificate [] certs = keyStore.getCertificateChain(alias);
        List<java.security.cert.Certificate> certList = new ArrayList<>();
        for (int i = 0, length = certs == null ? 0 : certs.length; i < length; i++) {
            certList.add(certs[i]);
        }

        Store certstore = new JcaCertStore(certList);

        PrivateKey privateKey = (PrivateKey) getKeyByKeyStore(keyStore);
        ContentSigner signer = new org.bouncycastle.operator.jcajce.JcaContentSignerBuilder("GOST3411WITHECGOST3410-2012-256").setProvider("BC").build(privateKey);

        CMSProcessableByteArray msg = new CMSProcessableByteArray(dataToSign);

        X509Certificate certificate = (X509Certificate) keyStore.getCertificate(KEY_ALIAS);
        CMSSignedDataGenerator gen = new CMSSignedDataGenerator();

        gen.addSignerInfoGenerator(new JcaSignerInfoGeneratorBuilder(new JcaDigestCalculatorProviderBuilder().setProvider("BC").build()).build(signer, certificate));
        gen.addCertificates(certstore);
        CMSSignedData sigData = gen.generate(msg, false);

        return sigData.getEncoded();
    }

    public byte[] signData(byte[] data) throws Exception {
        KeyStore keyStore = loadKeyStore();

        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM, "BC");

        PrivateKey key = (PrivateKey) keyStore.getKey(KEY_ALIAS, KEYSTORE_PASSWORD.toCharArray());

        signature.initSign(key);
        signature.update(data);

        return signature.sign();
    }
}