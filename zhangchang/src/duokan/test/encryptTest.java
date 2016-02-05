package duokan.test;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.SecretKeySpec;






public class encryptTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	  public static String f(String paramString)
	  {
	    KeyGenerator localKeyGenerator = KeyGenerator.getInstance("AES");
	    localKeyGenerator.init(128, new SecureRandom());
	    byte[] arrayOfByte1 = localKeyGenerator.generateKey().getEncoded();
	    byte[] arrayOfByte2 = new byte[16];
	    new Random().nextBytes(arrayOfByte2);
	    IvParameterSpec localIvParameterSpec = new IvParameterSpec(arrayOfByte2);
	    //new byte[arrayOfByte1.length + arrayOfByte2.length];
	    byte[] arrayOfByte3 = Arrays.copyOf(arrayOfByte1, arrayOfByte1.length + arrayOfByte2.length);
	    System.arraycopy(arrayOfByte2, 0, arrayOfByte3, arrayOfByte1.length, arrayOfByte2.length);
	    byte[] arrayOfByte4 = a(arrayOfByte3, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAy1+Tb5ch4blt46afIvJW21WQuGeRITyPWKlrXUrPBJOkSaTRe9rzeN+kWXA/IFLkM9HSqrnfzjO5nU7t3rCZfHtw/xutrarj+sci3eXWnN9d55edIs91DwJh5iWQyU5hRJ5DxpB1iDi6r8qEncqU2gK1JH56G/tdCXcdrgNBo2ge83gl/bm0CjtXcrAwzZ+pFrXkKsP9OmYyTlVdOUP1V6SAzI2nlMArcjTe99g4xdnR0X/YqAFcTIqA8sX7mA2/LcWItDLauRH6x7Mbca2XvPqTLV1boBa4V7rlm8qzgUMoJE+2Pt+0GM8JIqmG3lpe8sd8qK0twhJvFWVu6ixSmwIDAQAB");
	    byte[] arrayOfByte5 = a(paramString, arrayOfByte1, localIvParameterSpec);
	    byte[] arrayOfByte6 = Arrays.copyOf(arrayOfByte4, arrayOfByte4.length + arrayOfByte5.length);
	    System.arraycopy(arrayOfByte5, 0, arrayOfByte6, arrayOfByte4.length, arrayOfByte5.length);
	    return Base64.encodeToString(arrayOfByte6, 0);
	  }
	  
	  public static byte[] a(byte[] paramArrayOfByte, String paramString) throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
	  {
	    OAEPParameterSpec localOAEPParameterSpec = OAEPParameterSpec.DEFAULT;
	    SecureRandom localSecureRandom = new SecureRandom();
	    X509EncodedKeySpec localX509EncodedKeySpec = new X509EncodedKeySpec(Base64.decode(paramString, 0));
	    PublicKey localPublicKey = KeyFactory.getInstance("RSA").generatePublic(localX509EncodedKeySpec);
	    Cipher localCipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA1ANDMGF1PADDING");
	    localCipher.init(1, localPublicKey, localOAEPParameterSpec, localSecureRandom);
	    return localCipher.doFinal(paramArrayOfByte);
	  }
	  
	  public static byte[] a(String paramString, byte[] paramArrayOfByte, IvParameterSpec paramIvParameterSpec) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
	  {
	    byte[] arrayOfByte1 = paramString.getBytes();
	    int i = arrayOfByte1.length;
	    if (i % 16 != 0)
	      i += 16 - i % 16;
	    byte[] arrayOfByte2 = Arrays.copyOf(arrayOfByte1, i);
	    SecretKeySpec localSecretKeySpec = new SecretKeySpec(paramArrayOfByte, "AES");
	    Cipher localCipher = Cipher.getInstance("AES/CBC/NoPadding");
	    localCipher.init(1, localSecretKeySpec, paramIvParameterSpec);
	    return localCipher.doFinal(arrayOfByte2);
	  }
}
