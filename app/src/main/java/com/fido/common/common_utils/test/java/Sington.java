package com.fido.common.common_utils.test.java;

/**
 * @author: FiDo
 * @date: 2024/2/19
 * @des:
 */
public class Sington {

    private String AA = "AA";
    private final String BB = "BB";

    public static final int DD = 44;

    public static final boolean EE = false;
    public static int CC = 66;

    public static String STR = "";

    public static Boolean BOl = true;

    public static int hInt;

    public static long mLong;
    public static Long hLong = 0L;
    public static char mChar;
    public static byte mByte = new Byte("");
    private static int pInt =9;
    private static String pSTR = "strrr";
    private static Long pLong = 0L;
    protected static Long pro= 9L;

    private static final int KK = 0;
    public String getAA() {
        return AA;
    }

    public static void main(String[] args) {
//        KK = 666;
        System.out.println("AA = " + Sington.getInstance().getAA());
    }

    public static Sington getInstance(){
        try {
            Class.forName("",false,null);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return Instance.SINGTON;
    }

    private static class Instance{
        private static final Sington SINGTON = new Sington();
    }


    /*private static OkHttpClient createTwoWayAuthClient() throws IOException, CertificateException, KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {
        // 服务器证书
        InputStream serverCertStream = TwoWayAuthHttpClient.class.getResourceAsStream("/server_certificate.crt");
        X509Certificate serverCertificate = readCertificate(serverCertStream);
        if (serverCertStream != null) {
            serverCertStream.close();
        }

        // 客户端证书和私钥
        InputStream clientCertStream = TwoWayAuthHttpClient.class.getResourceAsStream("/client_centificate.p12");
        KeyStore clientKeyStore = KeyStore.getInstance("PKCS12");
        clientKeyStore.load(clientCertStream, "client_password".toCharArray());
        if (clientCertStream != null) {
            clientCertStream.close();
        }

        // 创建 KeyManagerFactory 和 TrustManagerFactory
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(clientKeyStore, "client_password".toCharArray());

        // 创建信任管理器，信任服务器证书
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null, null);
        trustStore.setCertificateEntry("server", serverCertificate);
        trustManagerFactory.init(trustStore);

        // 初始化 SSL 上下文
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());

        // 创建 OkHttpClient
        return new OkHttpClient.Builder()
                .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustManagerFactory.getTrustManagers()[0])
                .build();
    }

    private static X509Certificate readCertificate(InputStream inputStream) throws CertificateException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        return (X509Certificate) certificateFactory.generateCertificate(inputStream);
    }*/

}
