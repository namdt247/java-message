import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;


public class KeyPairGenerate {
    public static byte[] pubKey;
    public static byte[] priKey;

    public static void main(String[] args) {
        try {
            SecureRandom sr = new SecureRandom();
            //Thuật toán phát sinh khóa - Rivest Shamir Adleman (RSA)
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048, sr);
            //Phát sinh cặp khóa
            KeyPair kp = kpg.genKeyPair();
            //PublicKey
            pubKey = kp.getPublic().getEncoded();
            //PrivateKey
            priKey = kp.getPrivate().getEncoded();
//            //Lưu Public Key
//            FileOutputStream fos = new FileOutputStream("D:/file/pubKey.bin");
//            fos.write(pubKey.getEncoded());
//            fos.close();
//            //Lưu Private Key
//            fos = new FileOutputStream("D:/file/priKey.bin");
//            fos.write(priKey.getEncoded());
//            fos.close();
            System.out.println("Generate key successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void genKey() {
        try {
            SecureRandom sr = new SecureRandom();
            //Thuật toán phát sinh khóa - Rivest Shamir Adleman (RSA)
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048, sr);
            //Phát sinh cặp khóa
            KeyPair kp = kpg.genKeyPair();
            //PublicKey
            pubKey = kp.getPublic().getEncoded();
            //PrivateKey
            priKey = kp.getPrivate().getEncoded();
//            //Lưu Public Key
//            FileOutputStream fos = new FileOutputStream("D:/file/pubKey.bin");
//            fos.write(pubKey.getEncoded());
//            fos.close();
//            //Lưu Private Key
//            fos = new FileOutputStream("D:/file/priKey.bin");
//            fos.write(priKey.getEncoded());
//            fos.close();
            System.out.println("Generate key successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}