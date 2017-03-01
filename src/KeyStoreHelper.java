import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import javax.crypto.Cipher;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
public class KeyStoreHelper
{
    public static void main(String[] args) throws Exception
    {
        String privatePath = "/opt/key/HeartyPri.key"; // 准备导出的私钥
        String publicPath = "/opt/key/HeartyPub.key"; // 准备导出的公钥
        //PrivateKey privateKey = getPrivateKeyFromStore();
        //createKeyFile(privateKey, privatePath);
        //PublicKey publicKey = getPublicKeyFromCrt();
        //createKeyFile(publicKey, publicPath);
        
        
        //System.err.println(privateKey);
        //System.err.println(publicKey);
        //PublicKey publicKey2 = getPublicKeyFromCrt();
        //System.err.println(publicKey2.equals(publicKey));
        
        PrivateKey privateKey=(PrivateKey)getKey(privatePath);
        PublicKey publicKey=(PublicKey)getKey(publicPath);
        
        String data="d28c7e2b-825e-4d40-89c5-83e8e23ec771";
        System.out.println("明文 ~ " + data);
        byte[] enb=encryptByKey(data,privateKey);
        System.out.println("加密结果 ~ " + new String(enb));
        byte[] result= decryptByPublicKey(new String(enb),publicKey);
        System.out.println("解密结果 ~ " + new String(result));
        //test(privateKey, publicKey);
        //test(publicKey, privateKey);
        //test(privateKey, privateKey);
        //test(publicKey, publicKey);
    }
    
    //通过读文件获得密钥
    public static Object getKey(String filePath) throws Exception{
    	FileInputStream fis=new FileInputStream(filePath);
        ObjectInputStream ois=new ObjectInputStream(fis);
        Object o=ois.readObject();
        return o;
    }
    
    //加密 
    public static byte[] encryptByKey(String data, Key encryptKey) throws Exception{
			//String data = "d28c7e2b-825e-4d40-89c5-83e8e23ec771";
			
			byte[] enb = Base64.encode(RSAencrypt(encryptKey, data.getBytes()));
			//String en = new String(enb);
			
			return enb;
    }
    //解密
    public static byte[] decryptByPublicKey(String data, Key decryptKey) throws Exception {
    	
        byte[] deb = Base64.decode(data);
        byte[] result = RSAdecrypt(decryptKey, deb);
        
        return result;
    }
    
    private static PrivateKey getPrivateKeyFromStore() throws Exception
    {
        String alias = "Hearty"; // KeyTool中生成KeyStore时设置的alias
        String storeType = "JCEKS"; // KeyTool中生成KeyStore时设置的storetype
        char[] pw = "5201314".toCharArray(); // KeyTool中生成KeyStore时设置的storepass
        String storePath = "/opt/key/Hearty.store"; // KeyTool中已生成的KeyStore文件
        storeType = null == storeType ? KeyStore.getDefaultType() : storeType;
        KeyStore keyStore = KeyStore.getInstance(storeType);
        InputStream is = new FileInputStream(storePath);
        keyStore.load(is, pw);
        // 由密钥库获取密钥的两种方式
        // KeyStore.PrivateKeyEntry pkEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, new KeyStore.PasswordProtection(pw));
        // return pkEntry.getPrivateKey();
        return (PrivateKey) keyStore.getKey(alias, pw);
    }
    private static PublicKey getPublicKeyFromCrt() throws CertificateException, FileNotFoundException
    {
        String crtPath = "/opt/key/Hearty.crt"; // KeyTool中已生成的证书文件
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        FileInputStream in = new FileInputStream(crtPath);
        Certificate crt = cf.generateCertificate(in);
        PublicKey publicKey = crt.getPublicKey();
        return publicKey;
    }
    private static void test(Key encryptKey, Key decryptKey) throws Exception
    {
        System.out.println();
        String data = "d28c7e2b-825e-4d40-89c5-83e8e23ec771";
        System.out.println("明文 ~ " + data);
        byte[] enb = Base64.encode(RSAencrypt(encryptKey, data.getBytes()));
        String en = new String(enb);
        System.out.println("加密结果 ~ " + new String(enb));
        byte[] deb = Base64.decode(en);
        byte[] result = RSAdecrypt(decryptKey, deb);
        System.out.println("解密结果 ~ " + new String(result));
    }
    
    private static byte[] RSAencrypt(Key pk, byte[] data) throws Exception
    {
        Cipher cipher = Cipher.getInstance("RSA", new BouncyCastleProvider());
        cipher.init(Cipher.ENCRYPT_MODE, pk);
        int blockSize = cipher.getBlockSize();
        int outputSize = cipher.getOutputSize(data.length);
        int leavedSize = data.length % blockSize;
        int blocksSize = leavedSize != 0 ? data.length / blockSize + 1 : data.length / blockSize;
        byte[] raw = new byte[outputSize * blocksSize];
        int i = 0;
        while (data.length - i * blockSize > 0)
        {
            if (data.length - i * blockSize > blockSize)
            {
                cipher.doFinal(data, i * blockSize, blockSize, raw, i * outputSize);
            }
            else
            {
                cipher.doFinal(data, i * blockSize, data.length - i * blockSize, raw, i * outputSize);
            }
            i++;
        }
        return raw;
    }
    private static byte[] RSAdecrypt(Key pk, byte[] raw) throws Exception
    {
        Cipher cipher = Cipher.getInstance("RSA", new BouncyCastleProvider());
        cipher.init(Cipher.DECRYPT_MODE, pk);
        ByteArrayOutputStream bout = null;
        try
        {
            bout = new ByteArrayOutputStream(64);
            int j = 0;
            int blockSize = cipher.getBlockSize();
            while (raw.length - j * blockSize > 0)
            {
                bout.write(cipher.doFinal(raw, j * blockSize, blockSize));
                j++;
            }
            return bout.toByteArray();
        }
        catch (Exception e)
        {
            throw e;
        }
        finally
        {
           if (bout != null)
           {
               try
                {
                    bout.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    
    
    private static void createKeyFile(Object key, String filePath) throws Exception
    {
        FileOutputStream fos = new FileOutputStream(filePath);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(key);
        oos.flush();
        oos.close();
    }
}