package DKcrack;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class AesUtil {

	private static Charset PLAIN_TEXT_ENCODING = Charset.forName("UTF-8");
	private static String CIPHER_TRANSFORMATION = "AES/CTR/NoPadding";
	private static String KEY_TYPE = "AES";
	private static int KEY_SIZE_BITS = 128;

	private static SecretKey key;
	public static Cipher cipher ;
	private static byte[] ivBytes = new byte[KEY_SIZE_BITS / 8];

	public AesUtil() throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance(KEY_TYPE);
		kgen.init(KEY_SIZE_BITS);
		key = kgen.generateKey();
		cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		ivBytes = cipher.getParameters().getParameterSpec(IvParameterSpec.class).getIV();
	}

	public String getIVAsHex() {
		return byteArrayToHexString(ivBytes);
	}

	public String getKeyAsHex() {
		return byteArrayToHexString(key.getEncoded());
	}

	public void setCrtKey(String keyText) throws Exception {
		byte[] bText = keyText.getBytes();
		SecretKey secretKey = new SecretKeySpec(bText, "AES");
		Cipher c2 = Cipher.getInstance(CIPHER_TRANSFORMATION);
		c2.init(Cipher.ENCRYPT_MODE, secretKey);
		bText = c2.doFinal(bText);
		key = new SecretKeySpec(bText, "AES");
	}
	
	public void setCrtKeyFromBase64(String keyTextBase64) throws Exception {
		byte[] bText = new Base64().decode(keyTextBase64);
		SecretKey secretKey = new SecretKeySpec(bText, "AES");
		Cipher c2 = Cipher.getInstance(CIPHER_TRANSFORMATION);
		c2.init(Cipher.ENCRYPT_MODE, secretKey);
		bText = c2.doFinal(bText);
		key = new SecretKeySpec(bText, "AES");
	}
	
	

	public void setStringToKey(String keyText) throws Exception {
		setKey(keyText.getBytes());
	}

	public void setHexToKey(String hexKey) {
		setKey(hexStringToByteArray(hexKey));
	}

	private void setKey(byte[] bArray) {
		byte[] bText = new byte[KEY_SIZE_BITS / 8];
		int end = Math.min(KEY_SIZE_BITS / 8, bArray.length);
		System.arraycopy(bArray, 0, bText, 0, end);
		key = new SecretKeySpec(bText, KEY_TYPE);
	}

	public void setStringToIV(String ivText) {
		setIV(ivText.getBytes());
	}

	public void setHexToIV(String hexIV) {
		setIV(hexStringToByteArray(hexIV));
	}

	private void setIV(byte[] bArray) {
		byte[] bText = new byte[KEY_SIZE_BITS / 8];
		int end = Math.min(KEY_SIZE_BITS / 8, bArray.length);
		System.arraycopy(bArray, 0, bText, 0, end);
		ivBytes = bText;
	}

	public String encryptCRT(String message) throws Exception {
		String hexMessage = encrypt(message);
		return byteArrayToHexString(ivBytes).concat(hexMessage.substring(2));
	}
	
	public String encryptCRTBytes(byte[] messageByte) throws Exception {
		String hexMessage = encryptBytes(messageByte);
		return byteArrayToHexString(ivBytes).concat(hexMessage.substring(2));
	}

	public String encrypt(String message) throws Exception {
		cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(ivBytes));
		byte[] encrypted = cipher.doFinal(message.getBytes(PLAIN_TEXT_ENCODING));
		String result = byteArrayToHexString(encrypted);
		return result;
	}
	
	public String encryptBytes(byte[] messageByte) throws Exception {
		cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(ivBytes));
		byte[] encrypted = cipher.doFinal(messageByte);
		String result = byteArrayToHexString(encrypted);
		return result;
	}
	
	public static byte[] encryptBytes2(byte[] messageByte) throws Exception {
		cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(ivBytes));
		return cipher.doFinal(messageByte);
	}

	public String decryptCrt(String hexCipherText) throws Exception {
		byte[] ciphertextBytes = hexStringToByteArray(hexCipherText);
		ivBytes = Arrays.copyOf(Arrays.copyOf(ciphertextBytes, 8), 16);
		cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(ivBytes));
		byte[] recoveredCleartext = cipher.doFinal(ciphertextBytes, 8,ciphertextBytes.length - 8);
		return new String(recoveredCleartext);
	}
	
	public String decryptCrtBytes(byte[] cipherByte) throws Exception {
		//byte[] ciphertextBytes = hexStringToByteArray(hexCipherText);
		ivBytes = Arrays.copyOf(Arrays.copyOf(cipherByte, 8), 16);
		cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(ivBytes));
		byte[] recoveredCleartext = cipher.doFinal(cipherByte, 8,cipherByte.length - 8);
		return new String(recoveredCleartext);
	}

	public String decrypt(String hexCiphertext)	throws Exception {
		byte[] dec = hexStringToByteArray(hexCiphertext);
		cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(ivBytes));
		byte[] decrypted = cipher.doFinal(dec);
		return new String(decrypted, PLAIN_TEXT_ENCODING);
	}
	
	public String decryptBytes(byte[] cipherByte)	throws Exception {
		//byte[] dec = hexStringToByteArray(hexCiphertext);
		cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(ivBytes));
		byte[] decrypted = cipher.doFinal(cipherByte);
		return new String(decrypted, PLAIN_TEXT_ENCODING);
		//return new String(decrypted, Charset.forName("GB2312"));
	}
	
	public static byte[] decryptBytes2(byte[] cipherByte)	throws Exception {
		//byte[] dec = hexStringToByteArray(hexCiphertext);
		cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(ivBytes));
		return cipher.doFinal(cipherByte);

	}

	private static String byteArrayToHexString(byte[] raw) {
		StringBuilder sb = new StringBuilder(2 + raw.length * 2);
		sb.append("0x");
		for (int i = 0; i < raw.length; i++) {
			sb.append(String.format("%02X", Integer.valueOf(raw[i] & 0xFF)));
		}
		return sb.toString();
	}

	private static byte[] hexStringToByteArray(String hex) {
		Pattern replace = Pattern.compile("^0x");
		String s = replace.matcher(hex).replaceAll("");

		byte[] b = new byte[s.length() / 2];
		for (int i = 0; i < b.length; i++) {
			int index = i * 2;
			int v = Integer.parseInt(s.substring(index, index + 2), 16);
			b[i] = (byte) v;
		}
		return b;
	}
	
	private static byte[] readBytesFromFile(String filename) throws Exception {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(filename));
		
		byte[] result = new byte[in.available()];
		in.read(result);
		return result;
	}
	
	private void writeBytesToFile(byte[] context,String filename) throws Exception {
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(filename));
		
		out.write(context);
		out.flush();
		out.close();
	}

	public static void main(String[] args) throws Exception {

		AesUtil aes = new AesUtil();
		String file1 = "E:/DEV/DKcrack/input.txt";
		String file2 = "E:/DEV/DKcrack/output.txt";
		String file3 = "E:/DEV/DKcrack/input_decrypted.txt";
		byte[] bytes1 = readBytesFromFile(file1);
		
		
		aes.setCrtKeyFromBase64("NIUBe7VM3oQgIfw2OW1vtA==");
		aes.writeBytesToFile(encryptBytes2(bytes1),file2);
		
		//aes.setCrtKeyFromBase64("NIUBe7VM3oQgIfw2OW1vtA==");
		byte[] bytes2 = readBytesFromFile(file2);
		aes.writeBytesToFile(decryptBytes2(bytes2),file3);
		//String decrypted = aes.decryptBytes(encryptedBytes);
		//System.out.println(decrypted);
		//String data = "test AES Encryption !@#$%^&~";
		//String encrypted = aes.encrypt(data);
		//System.out.println(aes.encryptCRT(data));
		//System.out.println(encrypted);

	}

}
