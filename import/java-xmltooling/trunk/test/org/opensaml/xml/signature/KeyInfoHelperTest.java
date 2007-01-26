/*
 * Copyright [2006] [University Corporation for Advanced Internet Development, Inc.]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensaml.xml.signature;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.security.KeyException;
import java.security.PublicKey;
import java.security.cert.CRLException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.interfaces.DSAParams;
import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;

import javax.security.auth.x500.X500Principal;

import org.opensaml.xml.XMLObjectBaseTestCase;
import org.opensaml.xml.util.Base64;

/**
 * Test to exercise the KeyInfoHelper methods to convert
 * between XMLObject's contained within KeyInfo and 
 * Java security native types.
 */
public class KeyInfoHelperTest extends XMLObjectBaseTestCase {
    
    /* These test examples are from the NIST PKI path processing test suite:
     * http://csrc.nist.gov/pki/testing/x509paths.html
     * Data file:
     * http://csrc.nist.gov/pki/testing/PKITS_data.zip
     */
    
    /* certs/BasicSelfIssuedNewKeyCACert.crt */
    /** Test cert subject DN 1. */
    private final String cert1SubjectDN = "CN=Basic Self-Issued New Key CA,O=Test Certificates,C=US";
    /** Test cert 1. */
    private final String cert1 = 
        "MIICgjCCAeugAwIBAgIBEzANBgkqhkiG9w0BAQUFADBAMQswCQYDVQQGEwJVUzEa" +
        "MBgGA1UEChMRVGVzdCBDZXJ0aWZpY2F0ZXMxFTATBgNVBAMTDFRydXN0IEFuY2hv" +
        "cjAeFw0wMTA0MTkxNDU3MjBaFw0xMTA0MTkxNDU3MjBaMFAxCzAJBgNVBAYTAlVT" +
        "MRowGAYDVQQKExFUZXN0IENlcnRpZmljYXRlczElMCMGA1UEAxMcQmFzaWMgU2Vs" +
        "Zi1Jc3N1ZWQgTmV3IEtleSBDQTCBnzANBgkqhkiG9w0BAQEFAAOBjQAwgYkCgYEA" +
        "tCkygqcMEOy3i8p6ZV3685us1lOugSU4pUMRJNRH/lV2ykesk+JRcQy1s7WS12j9" +
        "GCnSJ919/TgeKLmV3ps1fC1B8HziC0mzBAr+7f5LkJqSf0kS0kfpyLOoO8VSJCip" +
        "/8uENkSkpvX+Lak96OKzhtyvi4KpUdQKfwpg6xUqakECAwEAAaN8MHowHwYDVR0j" +
        "BBgwFoAU+2zULYGeyid6ng2wPOqavIf/SeowHQYDVR0OBBYEFK+5+R3CRRjMuCHi" +
        "p0e8Sb0ZtXgoMA4GA1UdDwEB/wQEAwIBBjAXBgNVHSAEEDAOMAwGCmCGSAFlAwIB" +
        "MAEwDwYDVR0TAQH/BAUwAwEB/zANBgkqhkiG9w0BAQUFAAOBgQCuRBfDy2gSPp2k" +
        "ZR7OAvt+xDx4toJ9ImImUvJ94AOLd6Uxsi2dvQT5HLrIBrTYsSfQj1pA50XY2F7k" +
        "3eM/+JhYCcyZD9XtAslpOkjwACPJnODFAY8PWC00CcOxGb6q+S/VkrCwvlBeMjev" +
        "IH4bHvAymWsZndBZhcG8gBmDrZMwhQ==";
    
    /* certs/GoodCACert.crt */
    /** Test cert subject DN 2. */
    private final String cert2SubjectDN = "CN=Good CA,O=Test Certificates,C=US";
    /** Test cert 2. */
    private final String cert2 = 
        "MIICbTCCAdagAwIBAgIBAjANBgkqhkiG9w0BAQUFADBAMQswCQYDVQQGEwJVUzEa" +
        "MBgGA1UEChMRVGVzdCBDZXJ0aWZpY2F0ZXMxFTATBgNVBAMTDFRydXN0IEFuY2hv" +
        "cjAeFw0wMTA0MTkxNDU3MjBaFw0xMTA0MTkxNDU3MjBaMDsxCzAJBgNVBAYTAlVT" +
        "MRowGAYDVQQKExFUZXN0IENlcnRpZmljYXRlczEQMA4GA1UEAxMHR29vZCBDQTCB" +
        "nzANBgkqhkiG9w0BAQEFAAOBjQAwgYkCgYEArsI1lQuXKwOxSkOVRaPwlhMQtgp0" +
        "p7HT4rKLGqojfY0twvMDc4rC9uj97wlh98kkraMx3r0wlllYSQ+Cp9mCCNu/C/Y2" +
        "IbZCyG+io4A3Um3q/QGvbHlclmrJb0j0MQi3o88GhE8Q6Vy6SGwFXGpKDJMpLSFp" +
        "Pxz8lh7M6J56Ex8CAwEAAaN8MHowHwYDVR0jBBgwFoAU+2zULYGeyid6ng2wPOqa" +
        "vIf/SeowHQYDVR0OBBYEFLcupoLLwsi8qHsnRNc1M9+aFZTHMA4GA1UdDwEB/wQE" +
        "AwIBBjAXBgNVHSAEEDAOMAwGCmCGSAFlAwIBMAEwDwYDVR0TAQH/BAUwAwEB/zAN" +
        "BgkqhkiG9w0BAQUFAAOBgQCOls9+0kEUS71w+KoQhfkVLdAKANXUmGCVZHL1zsya" +
        "cPP/Q8IsCNvwjefZpgc0cuhtnHt2uDd0/zYLRmgcvJwfx5vwOfmDN13mMB8Za+cg" +
        "3sZ/NI8MqQseKvS3fWqXaK6FJoKLzxId0iUGntbF4c5+rPFArzqM6IE7f9cMD5Fq" +
        "rA==";
    
    /* crls/BasicSelfIssuedCRLSigningKeyCACRL.crl */
    /** Test cert issuer DN 1. */
    private final String crl1IssuerDN = "CN=Basic Self-Issued CRL Signing Key CA,O=Test Certificates,C=US";
    /** Test CRL 1. */
    private final String crl1 = 
        "MIIBdTCB3wIBATANBgkqhkiG9w0BAQUFADBYMQswCQYDVQQGEwJVUzEaMBgGA1UE" +
        "ChMRVGVzdCBDZXJ0aWZpY2F0ZXMxLTArBgNVBAMTJEJhc2ljIFNlbGYtSXNzdWVk" +
        "IENSTCBTaWduaW5nIEtleSBDQRcNMDEwNDE5MTQ1NzIwWhcNMTEwNDE5MTQ1NzIw" +
        "WjAiMCACAQMXDTAxMDQxOTE0NTcyMFowDDAKBgNVHRUEAwoBAaAvMC0wHwYDVR0j" +
        "BBgwFoAUD3LKM0OpxBFRq2PaRIcPYaT0vkcwCgYDVR0UBAMCAQEwDQYJKoZIhvcN" +
        "AQEFBQADgYEAXM2Poz2eZPdkc5wsOeLn1w64HD6bHRTcmMKOWh/lRzH9fqfVn1Ix" +
        "yBD30KKEP3fH8bp+JGKtBa4ce//w4s5V9SfTzCR/yB2muM5CBeEG7B+HTNVpjXhZ" +
        "0jOUHDsnaIA9bz2mx58rOZ/Xw4Prd73Mf5azrSRomdEavwUcjD4qAvg=";
    
    
    /* These are just randomly generated RSA and DSA public keys using OpenSSL. */
    
    /** Test RSA key 1. */
    private final String rsaPubKey1 = 
        "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAw/WnsbA8frhQ+8EoPgMr" +
        "QjpINjt20U/MvsvmoAgQnAgEF4OYt9Vj9/2YvMO4NvX1fNDFzoYRyOMrypF7skAP" +
        "cITUhdcPSEpI4nsf5yFZLziK/tQ26RsccE7WhpGB8eHu9tfseelgyioorvmt+JCo" +
        "P15c5rYUuIfVC+eEsYolw344q6N61OACHETuySL0a1+GFu3WoISXte1pQIst7HKv" +
        "BbHH41HEWAxT6e0hlD5PyKL4lBJadGHXg8Zz4r2jV2n6+Ox7raEWmtVCGFxsAoCR" +
        "alu6nvs2++5Nnb4C1SE640esfYhfeMd5JYfsTNMaQ8sZLpsWdglAGpa/Q87K19LI" +
        "wwIDAQAB";
    
    /** Test DSA key 1. */
    private final String dsaPubKey1 = 
        "MIIDOjCCAi0GByqGSM44BAEwggIgAoIBAQCWV7IK073aK2C3yggy69qXkxCw30j5" +
        "Ig0s1/GHgq5jEZf8FTGVpehX5qaYlRC3TBMSN4WAgkG+nFnsjHb6kIYkayV8ZVvI" +
        "IgEBCeaZg016f90G+Rre5C38G3OwsODKjPsVZCV5YQ9rm6lWMOfMRSUzJuFA0fdx" +
        "RLssAfKLI5JmzupliO2iH5FU3+dQr0UvcPwPjjRDA9JIi3ShKdmq9f/SzRM9AJPs" +
        "sjc0v4lRVMKWkTHLjbRH2XiOxsok/oL7NVTJ9hvd3xqi1/O3MM2pNhYaQoA0kLqq" +
        "sr006dNftgo8n/zrBFMC6iP7tmxhuRxgXXkNo5xiQCvAX7HsGno4y9ilAhUAjKlv" +
        "CQhbGeQo3fWbwVJMdokSK5ECggEAfERqa+S8UwjuvNxGlisuBGzR7IqqHSQ0cjFI" +
        "BD61CkYh0k0Y9am6ZL2jiAkRICdkW6f9lmGy0HidCwC56WeAYpLyfJslBAjC4r0t" +
        "6U8a822fECVcbsPNLDULoQG0KjVRtYfFH5GedNQ8LRkG8b+XIe4G74+vXOatVu8L" +
        "9QXQKYx9diOAHx8ghpt1pC0UAqPzAgVGNWIPQ+VO7WEYOYuVw+/uFoHiaU1OZOTF" +
        "C4VXk2+33AasT4i6It7DIESp+ye9lPnNU6nLEBNSnXdnBgaH27m8QnFRTfrjimiG" +
        "BwBTQvbjequRvM5dExfUqyCd2BUOK1lbaQmjZnCMH6k3ZFiAYgOCAQUAAoIBAGnD" +
        "wMuRoRGJHUhjjeePKwP9BgCc7dtKlB7QMnIHGPv03hdVPo9ezaQ5mFxdzQdXoLR2" +
        "BFucDtSj1je3e5L9KEnHZ5fHnislBnzSvYR5V8LwTa5mbNS4VHkAv8Eh3WG9tp1S" +
        "/f9ymefKHB7ISlskT7kODCIbr5HHU/n1zXtMRjoslY1A+nFlWiAaIvjnj/C8x0BW" +
        "BkhuSKX/2PbljnmIdGV7mJK9/XUHnyKgZBxXEul2mlvGkrgUvyv+qYsCFsKSSrkB" +
        "1Mj2Ql5xmTMaePMEmvOr6fDAP0OH8cvADEZjx0s/5vvoBFPGGmPrHJluEVS0Fu8I" +
        "9sROg9YjyuhRV0b8xHo=";
    

    private X509Certificate xmlCert1, xmlCert2;
    private X509CRL xmlCRL1;
    private X509Data xmlX509Data;
    private KeyInfo keyInfo;
    private KeyValue keyValue;
    private DSAKeyValue xmlDSAKeyValue1, xmlDSAKeyValue1NoParams;
    private RSAKeyValue xmlRSAKeyValue1;
    private int numExpectedCerts;
    private int numExpectedCRLs;
    
    private java.security.cert.X509Certificate javaCert1;
    private java.security.cert.X509Certificate javaCert2;
    private java.security.cert.X509CRL javaCRL1;
    private RSAPublicKey javaRSAPubKey1;
    private DSAPublicKey javaDSAPubKey1;
    private DSAParams javaDSAParams1;
    


    /**
     * Constructor.
     *
     */
    public KeyInfoHelperTest() {
        super();
    }

    /** {@inheritDoc} */
    protected void setUp() throws Exception {
        super.setUp();
        
        xmlCert1 = (X509Certificate) buildXMLObject(X509Certificate.DEFAULT_ELEMENT_NAME);
        xmlCert1.setValue(cert1);
        
        xmlCert2 = (X509Certificate) buildXMLObject(X509Certificate.DEFAULT_ELEMENT_NAME);
        xmlCert2.setValue(cert2);
        
        xmlCRL1 = (X509CRL) buildXMLObject(X509CRL.DEFAULT_ELEMENT_NAME);
        xmlCRL1.setValue(crl1);
        
        xmlX509Data = (X509Data) buildXMLObject(X509Data.DEFAULT_ELEMENT_NAME);
        xmlX509Data.getX509Certificates().add(xmlCert1);
        xmlX509Data.getX509Certificates().add(xmlCert2);
        xmlX509Data.getX509CRLs().add(xmlCRL1);
        
        keyValue = (KeyValue) buildXMLObject(KeyValue.DEFAULT_ELEMENT_NAME);
        
        keyInfo = (KeyInfo) buildXMLObject(KeyInfo.DEFAULT_ELEMENT_NAME);
        keyInfo.getX509Datas().add(xmlX509Data);
        
        numExpectedCerts = 2;
        numExpectedCRLs = 1;
        
        javaCert1 = buildJavaX509Cert(cert1);
        javaCert2 = buildJavaX509Cert(cert2);
        javaCRL1 = buildJavaX509CRL(crl1);
        
        javaDSAPubKey1 = buildJavaDSAPublicKey(dsaPubKey1);
        javaRSAPubKey1 = buildJavaRSAPublicKey(rsaPubKey1);
        
        xmlRSAKeyValue1 = (RSAKeyValue) buildXMLObject(RSAKeyValue.DEFAULT_ELEMENT_NAME);
        Modulus modulus = (Modulus) buildXMLObject(Modulus.DEFAULT_ELEMENT_NAME);
        Exponent exponent = (Exponent) buildXMLObject(Exponent.DEFAULT_ELEMENT_NAME);
        modulus.setValue(Base64.encodeBytes(javaRSAPubKey1.getModulus().toByteArray()));
        exponent.setValue(Base64.encodeBytes(javaRSAPubKey1.getPublicExponent().toByteArray()));
        xmlRSAKeyValue1.setModulus(modulus);
        xmlRSAKeyValue1.setExponent(exponent);
        
        xmlDSAKeyValue1 = (DSAKeyValue) buildXMLObject(DSAKeyValue.DEFAULT_ELEMENT_NAME);
        P p = (P) buildXMLObject(P.DEFAULT_ELEMENT_NAME);
        Q q = (Q) buildXMLObject(Q.DEFAULT_ELEMENT_NAME);
        G g = (G) buildXMLObject(G.DEFAULT_ELEMENT_NAME);
        Y y1 = (Y) buildXMLObject(Y.DEFAULT_ELEMENT_NAME);
        p.setValue(Base64.encodeBytes(javaDSAPubKey1.getParams().getP().toByteArray()));
        q.setValue(Base64.encodeBytes(javaDSAPubKey1.getParams().getQ().toByteArray()));
        g.setValue(Base64.encodeBytes(javaDSAPubKey1.getParams().getG().toByteArray()));
        y1.setValue(Base64.encodeBytes(javaDSAPubKey1.getY().toByteArray()));
        xmlDSAKeyValue1.setP(p);
        xmlDSAKeyValue1.setQ(q);
        xmlDSAKeyValue1.setG(g);
        xmlDSAKeyValue1.setY(y1);
        
        xmlDSAKeyValue1NoParams = (DSAKeyValue) buildXMLObject(DSAKeyValue.DEFAULT_ELEMENT_NAME);
        Y y2 = (Y) buildXMLObject(Y.DEFAULT_ELEMENT_NAME);
        y2.setValue(Base64.encodeBytes(javaDSAPubKey1.getY().toByteArray()));
        xmlDSAKeyValue1NoParams.setY(y2);
        javaDSAParams1 = javaDSAPubKey1.getParams();
    }


    
    /** Test converting XML X509Certificate to java.security.cert.X509Certificte. */
    public void testCertConversionXMLtoJava() {
        java.security.cert.X509Certificate javaCert = null;
        try {
            javaCert = KeyInfoHelper.getCertificate(xmlCert1);
        } catch (CertificateException e) {
            fail("Conversion from XML X509Certificate format to java.security.cert.X509Certificate failed: " + e);
        }
        assertNotNull("Cert1 was null, failed to convert from XML to Java representation", javaCert);
        assertEquals("Cert1 SubjectDN", cert1SubjectDN,
                javaCert.getSubjectX500Principal().getName(X500Principal.RFC2253));
        assertEquals("Java cert was not the expected value", buildJavaX509Cert(xmlCert1.getValue()), javaCert);
        
        List<java.security.cert.X509Certificate> javaCertList = null;
        
        try {
            javaCertList = KeyInfoHelper.getCertificates(xmlX509Data);
        } catch (CertificateException e) {
            fail("Obtaining certs from X509Data failed: " + e);
        }
        assertEquals("# of certs returned", numExpectedCerts, javaCertList.size());
        assertEquals("Cert1 SubjectDN", cert1SubjectDN,
                javaCertList.get(0).getSubjectX500Principal().getName(X500Principal.RFC2253));
        assertEquals("Cert2 SubjectDN", cert2SubjectDN,
                javaCertList.get(1).getSubjectX500Principal().getName(X500Principal.RFC2253));
        
        try {
            javaCertList = KeyInfoHelper.getCertificates(keyInfo);
        } catch (CertificateException e) {
            fail("Obtaining certs from KeyInfo failed: " + e);
        }
        assertEquals("# of certs returned", numExpectedCerts, javaCertList.size());
        assertEquals("Cert1 SubjectDN", cert1SubjectDN,
                javaCertList.get(0).getSubjectX500Principal().getName(X500Principal.RFC2253));
        assertEquals("Cert2 SubjectDN", cert2SubjectDN,
                javaCertList.get(1).getSubjectX500Principal().getName(X500Principal.RFC2253));
    }
    
    /** Test converting XML X509CRL to java.security.cert.X509CRL. */
    public void testCRLConversionXMLtoJava() {
        java.security.cert.X509CRL javaCRL = null;
        try {
            javaCRL = KeyInfoHelper.getCRL(xmlCRL1);
        } catch (CRLException e) {
            fail("Conversion from XML X509CRL format to java.security.cert.X509CRL failed: " + e);
        }
        assertNotNull("CRL was null, failed to convert from XML to Java representation", javaCRL);
        assertEquals("CRL IssuerDN", crl1IssuerDN, javaCRL.getIssuerX500Principal().getName(X500Principal.RFC2253));
        assertEquals("Java CRL was not the expected value", buildJavaX509CRL(xmlCRL1.getValue()), javaCRL);
        
        
        List<java.security.cert.X509CRL> javaCRLList = null;
        
        try {
            javaCRLList = KeyInfoHelper.getCRLs(xmlX509Data);
        } catch (CRLException e) {
            fail("Obtaining CRLs from X509Data failed: " + e);
        }
        assertEquals("# of CRLs returned", numExpectedCRLs, javaCRLList.size());
        assertEquals("CRL IssuerDN", crl1IssuerDN,
                javaCRLList.get(0).getIssuerX500Principal().getName(X500Principal.RFC2253));
        
        try {
            javaCRLList = KeyInfoHelper.getCRLs(keyInfo);
        } catch (CRLException e) {
            fail("Obtaining CRLs from KeInfo failed: " + e);
        }
        assertEquals("# of CRLs returned", numExpectedCRLs, javaCRLList.size());
        assertEquals("CRL IssuerDN", crl1IssuerDN,
                javaCRLList.get(0).getIssuerX500Principal().getName(X500Principal.RFC2253));
        
    }
    
    /** Test converting java.security.cert.X509Certificate to XML X509Certificate. */
    public void testCertConversionJavaToXML() {
        X509Certificate xmlCert = null;
        try {
            xmlCert = KeyInfoHelper.buildX509Certificate(javaCert1);
        } catch (CertificateEncodingException e) {
            fail("Conversion from Java X509Certificate to XMLObject failed: " + e);
        }
        
        assertEquals("Java X509Certificate encoding to XMLObject failed",
                javaCert1, buildJavaX509Cert(xmlCert.getValue()));
    }
    
    /** Test converting java.security.cert.X509CRL to XML X509CRL. */
    public void testCRLConversionJavaToXML() {
        X509CRL xmlCRL = null;
        try {
            xmlCRL = KeyInfoHelper.buildX509CRL(javaCRL1);
        } catch (CRLException e) {
            fail("Conversion from Java X509CRL to XMLObject failed: " + e);
        }
        
        assertEquals("Java X509CRL encoding to XMLObject failed", javaCRL1, buildJavaX509CRL(xmlCRL.getValue()));
    }
    
    /** Test conversion of DSA public keys from XML to Java security native type. */
    public void testDSAConversionXMLToJava() {
        PublicKey key = null;
        DSAPublicKey dsaKey = null;
        
        try {
            key = KeyInfoHelper.getDSAKey(xmlDSAKeyValue1);
        } catch (KeyException e) {
            fail("DSA key conversion XML to Java failed: " + e);
        }
        dsaKey = (DSAPublicKey) key;
        assertNotNull("Generated key was not an instance of DSAPublicKey", dsaKey);
        assertEquals("Generated key was not the expected value", javaDSAPubKey1, dsaKey);
        
        try {
            key = KeyInfoHelper.getDSAKey(xmlDSAKeyValue1NoParams, javaDSAParams1);
        } catch (KeyException e) {
            fail("DSA key conversion XML to Java failed: " + e);
        }
        dsaKey = (DSAPublicKey) key;
        assertNotNull("Generated key was not an instance of DSAPublicKey", dsaKey);
        assertEquals("Generated key was not the expected value", javaDSAPubKey1, dsaKey);
        
        try {
            key = KeyInfoHelper.getDSAKey(xmlDSAKeyValue1NoParams);
            fail("DSA key conversion XML to Java failed should have thrown an exception but didn't");
        } catch (KeyException e) {
            // do nothing, we expect to fail b/c not complete set of DSAParams
        }
    }
    
    /** Test conversion of RSA public keys from XML to Java security native type. */
    public void testRSAConversionXMLToJava() {
        PublicKey key = null;
        RSAPublicKey rsaKey = null;
        
        try {
            key = KeyInfoHelper.getRSAKey(xmlRSAKeyValue1);
        } catch (KeyException e) {
            fail("RSA key conversion XML to Java failed: " + e);
        }
        rsaKey = (RSAPublicKey) key;
        assertNotNull("Generated key was not an instance of RSAPublicKey", rsaKey);
        assertEquals("Generated key was not the expected value", javaRSAPubKey1, rsaKey);
    }
    
    /** Test conversion of DSA public keys from Java security native type to XML. */
    public void testDSAConversionJavaToXML() {
        DSAKeyValue dsaKeyValue = KeyInfoHelper.buildDSAKeyValue(javaDSAPubKey1);
        assertNotNull("Generated DSAKeyValue was null");
        assertEquals("Generated DSAKeyValue Y component was not the expected value",
                javaDSAPubKey1.getY(), getBigInt(dsaKeyValue.getY().getValue()));
        assertEquals("Generated DSAKeyValue P component was not the expected value",
                javaDSAPubKey1.getParams().getP(), getBigInt(dsaKeyValue.getP().getValue()));
        assertEquals("Generated DSAKeyValue Q component was not the expected value",
                javaDSAPubKey1.getParams().getQ(), getBigInt(dsaKeyValue.getQ().getValue()));
        assertEquals("Generated DSAKeyValue G component was not the expected value",
                javaDSAPubKey1.getParams().getG(), getBigInt(dsaKeyValue.getG().getValue()));
    }
    
    /** Test conversion of RSA public keys from Java security native type to XML. */
    public void testRSAConversionJavaToXML() {
        RSAKeyValue rsaKeyValue = KeyInfoHelper.buildRSAKeyValue(javaRSAPubKey1);
        assertNotNull("Generated RSAKeyValue was null");
        assertEquals("Generated RSAKeyValue modulus component was not the expected value",
                javaRSAPubKey1.getModulus(), getBigInt(rsaKeyValue.getModulus().getValue()));
        assertEquals("Generated RSAKeyValue exponent component was not the expected value",
                javaRSAPubKey1.getPublicExponent(), getBigInt(rsaKeyValue.getExponent().getValue()));
    }
    
    /** Tests extracting a DSA public key from a KeyValue. */
    public void testGetDSAKey() {
        keyValue.setRSAKeyValue(null);
        keyValue.setDSAKeyValue(xmlDSAKeyValue1);
        
        PublicKey pk = null;
        DSAPublicKey dsaKey = null;
        try {
            pk = KeyInfoHelper.getKey(keyValue);
        } catch (KeyException e) {
            fail("Extraction of key from KeyValue failed: " + e);
        }
        assertTrue("Generated key was not an instance of DSAPublicKey", pk instanceof DSAPublicKey);
        dsaKey = (DSAPublicKey) pk;
        assertEquals("Generated key was not the expected value", javaDSAPubKey1, dsaKey);        
        
        keyValue.setDSAKeyValue(null);
    }
        
    /** Tests extracting a RSA public key from a KeyValue. */
    public void testGetRSAKey() {
        keyValue.setDSAKeyValue(null);
        keyValue.setRSAKeyValue(xmlRSAKeyValue1);
        
        PublicKey pk = null;
        RSAPublicKey rsaKey = null;
        try {
            pk = KeyInfoHelper.getKey(keyValue);
        } catch (KeyException e) {
            fail("Extraction of key from KeyValue failed: " + e);
        }
        assertTrue("Generated key was not an instance of RSAPublicKey", pk instanceof RSAPublicKey);
        rsaKey = (RSAPublicKey) pk;
        assertEquals("Generated key was not the expected value", javaRSAPubKey1, rsaKey);        
        
        keyValue.setRSAKeyValue(null);
    }
    
    /** Tests adding a public key as a KeyValue to KeyInfo. */
    public void testAddDSAPublicKey() {
        keyInfo.getKeyValues().clear();
        
        KeyInfoHelper.addPublicKey(keyInfo, javaDSAPubKey1);
        KeyValue kv = keyInfo.getKeyValues().get(0);
        assertNotNull("KeyValue was null", kv);
        DSAKeyValue dsaKeyValue = kv.getDSAKeyValue();
        assertNotNull("DSAKeyValue was null", dsaKeyValue);
        
        DSAPublicKey javaKey = null;
        try {
            javaKey = (DSAPublicKey) KeyInfoHelper.getDSAKey(dsaKeyValue);
        } catch (KeyException e) {
            fail("Extraction of Java key failed: " + e);
        }
        
        assertEquals("Inserted DSA public key was not the expected value", javaDSAPubKey1, javaKey);
        
        keyInfo.getKeyValues().clear();
    }
    
    /** Tests adding a public key as a KeyValue to KeyInfo. */
    public void testAddRSAPublicKey() {
       keyInfo.getKeyValues().clear();
        
        KeyInfoHelper.addPublicKey(keyInfo, javaRSAPubKey1);
        KeyValue kv = keyInfo.getKeyValues().get(0);
        assertNotNull("KeyValue was null", kv);
        RSAKeyValue rsaKeyValue = kv.getRSAKeyValue();
        assertNotNull("RSAKeyValue was null", rsaKeyValue);
        
        RSAPublicKey javaKey = null;
        try {
            javaKey = (RSAPublicKey) KeyInfoHelper.getRSAKey(rsaKeyValue);
        } catch (KeyException e) {
            fail("Extraction of Java key failed: " + e);
        }
        
        assertEquals("Inserted RSA public key was not the expected value", javaRSAPubKey1, javaKey);
        
        keyInfo.getKeyValues().clear();
    }
    
    /**
     * Build Java certificate from base64 encoding.
     * 
     * @param base64Cert base64-encoded certificate
     * @return a native Java X509 certificate
     */
    protected java.security.cert.X509Certificate buildJavaX509Cert(String base64Cert) {
        CertificateFactory  cf = null;
        try {
            cf = CertificateFactory.getInstance("X.509");
        } catch (CertificateException e) {
            fail("Couldn't create X509 CertificateFactory: " + e);
        }
        
        ByteArrayInputStream input = new ByteArrayInputStream(Base64.decode(base64Cert));
        java.security.cert.X509Certificate newCert = null;
        try {
            newCert = (java.security.cert.X509Certificate) cf.generateCertificate(input);
        } catch (CertificateException e) {
            fail("Couldn't create Java X509Certificate: " + e);
        }
        return newCert;
    }
    
    /**
     * Build Java CRL from base64 encoding.
     * 
     * @param base64CRL base64-encoded CRL
     * @return a native Java X509 CRL
     */
    protected java.security.cert.X509CRL buildJavaX509CRL(String base64CRL) {
        CertificateFactory  cf = null;
        try {
            cf = CertificateFactory.getInstance("X.509");
        } catch (CertificateException e) {
            fail("Couldn't create X509 CertificateFactory: " + e);
        }
        
        ByteArrayInputStream input = new ByteArrayInputStream(Base64.decode(base64CRL));
        java.security.cert.X509CRL newCRL = null;
        try {
            newCRL = (java.security.cert.X509CRL) cf.generateCRL(input);
        } catch (CRLException e) {
            fail("Couldn't create Java X509CRL: " + e);
        }
        return newCRL;
    }
    
    /**
     * Build Java DSA public key from base64 encoding.
     * 
     * @param base64EncodedKey base64-encoded DSA public key
     * @return a native Java DSAPublicKey
     */
    protected DSAPublicKey buildJavaDSAPublicKey(String base64EncodedKey) {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decode(base64EncodedKey));
        DSAPublicKey key = null;
        try {
            key = (DSAPublicKey) KeyInfoHelper.buildKey(keySpec, "DSA");
        } catch (KeyException e) {
            fail("Couldn't build DSA key from base64 encoding: " + e);
        }
        return key;
    }
    
    /**
     * Build Java RSA public key from base64 encoding.
     * 
     * @param base64EncodedKey base64-encoded RSA public key
     * @return a native Java RSAPublicKey
     */
    protected RSAPublicKey buildJavaRSAPublicKey(String base64EncodedKey) {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decode(base64EncodedKey));
        RSAPublicKey key = null;
        try {
            key = (RSAPublicKey) KeyInfoHelper.buildKey(keySpec, "RSA");
        } catch (KeyException e) {
            fail("Couldn't build RSA key from base64 encoding: " + e);
        }
        return key;
    }
    
    
    /**
     * Build a Java BigInteger from the base64 encoded string.
     * 
     * @param base64Value a base64 encoded large integer
     * @return a BigInteger instance
     */
    protected BigInteger getBigInt(String base64Value) {
        return new BigInteger(Base64.decode(base64Value));  
    }
    
}
