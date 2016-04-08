package DKcrack;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DkMD5 {
	private static final char[] HEX_DIGITS = { 48, 49, 50, 51, 52, 53, 54, 55,
			56, 57, 65, 66, 67, 68, 69, 70 };

	public static String MD5_16(String paramString) {
		return MD5_32(paramString).subSequence(8, 24).toString();
	}

	public static String MD5_32(String paramString) {
		StringBuffer localStringBuffer;
		try {
			MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
			localStringBuffer = new StringBuffer();
			localMessageDigest.update(paramString.getBytes(), 0,
					paramString.length());
			byte[] arrayOfByte = localMessageDigest.digest();
			//for (int i = 0; i < arrayOfByte.length; i++)
				localStringBuffer.append(byte2Hex(arrayOfByte));
		} catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
			return null;
		}
		return localStringBuffer.toString();
	}
	
	public static byte[] getMD5(byte[] paramBytes) {
		try {
			MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
			localMessageDigest.update(paramBytes, 0,paramBytes.length);
			return localMessageDigest.digest();

		} catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
			return null;
		}
	}

	private static String byte2Hex(byte paramByte) {
		byte[] tmp = { paramByte };
		return byte2Hex(tmp);
	}

	public static String byte2Hex(byte[] b) {
		StringBuffer hs = new StringBuffer(b.length);
		String stmp = "";
		int len = b.length;
		for (int n = 0; n < len; n++) {
			stmp = Integer.toHexString(b[n] & 0xFF);
			if (stmp.length() == 1)
				hs = hs.append("0").append(stmp);
			else {
				hs = hs.append(stmp);
			}
		}
		return String.valueOf(hs);
	}

	public static String md5sum(String paramString) throws Exception {
		byte[] arrayOfByte = new byte[1024];
		FileInputStream localFileInputStream;
		MessageDigest localMessageDigest;
		try {
			localFileInputStream = new FileInputStream(paramString);
			localMessageDigest = MessageDigest.getInstance("MD5");
			while (true) {
				int i = localFileInputStream.read(arrayOfByte);
				if (i <= 0)
					break;
				localMessageDigest.update(arrayOfByte, 0, i);
			}
		} catch (Exception localException) {
			System.out.println("error");
			return null;
		}
		localFileInputStream.close();
		String str = toHexString(localMessageDigest.digest());
		return str;
	}

	public static String toHexString(byte[] paramArrayOfByte) {
		StringBuilder localStringBuilder = new StringBuilder(
				2 * paramArrayOfByte.length);
		for (int i = 0; i < paramArrayOfByte.length; i++) {
			localStringBuilder
					.append(HEX_DIGITS[((0xF0 & paramArrayOfByte[i]) >>> 4)]);
			localStringBuilder.append(HEX_DIGITS[(0xF & paramArrayOfByte[i])]);
		}
		return localStringBuilder.toString();
	}

}
