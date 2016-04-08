package DKcrack;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

public class DKdrm {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String user_name = "zhangchang@163.com";
		String order_id = "TO451TZ80YWN905AW24XNJSFYXYU7WX0";
		String book_id = "44015918320111e28a9300163e0123ac";
		System.out.println(makeDrmPart2(user_name,order_id,book_id));
		
	}
	
	  public static String bytes2hex(byte[] paramArrayOfByte)
	  {
	    assert (paramArrayOfByte != null);
	    StringBuilder localStringBuilder = new StringBuilder(2 * paramArrayOfByte.length);
	    for (int i = 0; i < paramArrayOfByte.length; i++)
	    {
	      Object[] arrayOfObject = new Object[1];
	      arrayOfObject[0] = Byte.valueOf(paramArrayOfByte[i]);
	      localStringBuilder.append(String.format("%02x", arrayOfObject));
	    }
	    return localStringBuilder.toString();
	  }
	  
	  public static byte[] hex2bytes(String paramString)
	  {
	    assert (paramString.length() % 2 == 0);
	    byte[] arrayOfByte = new byte[paramString.length() / 2];
	    for (int i = 0; i < arrayOfByte.length; i++)
	      arrayOfByte[i] = (byte)Integer.valueOf(paramString.substring(i * 2, 2 + i * 2), 16).intValue();
	    return arrayOfByte;
	  }
	  
	  public static String makeDrmPart2(String loginName,String paramString1,String paramString2) throws Exception {
		  String str1 = UUID.randomUUID().toString();
		  System.out.println(str1);
		    byte[] arrayOfByte1 = DkMD5.getMD5(loginName.getBytes("UTF-8"));
		    byte[] arrayOfByte2 = DkMD5.getMD5(paramString1.getBytes("UTF-8"));
		    byte[] arrayOfByte3 = DkMD5.getMD5(paramString2.getBytes("UTF-8"));
		    byte[] arrayOfByte4 = DkMD5.getMD5(str1.getBytes("UTF-8"));
		    
//		    byte[] arrayOfByte1 = DkMD5.MD5_32("18622931346").getBytes("UTF-8");
//		    byte[] arrayOfByte2 = DkMD5.MD5_32(paramString1).getBytes("UTF-8");
//		    byte[] arrayOfByte3 = DkMD5.MD5_32(paramString2).getBytes("UTF-8");
//		    byte[] arrayOfByte4 = DkMD5.MD5_32(str1).getBytes("UTF-8");
		    
		    byte[] arrayOfByte5 = new byte[arrayOfByte1.length + arrayOfByte2.length + arrayOfByte3.length + arrayOfByte4.length];
		    System.arraycopy(arrayOfByte1, 0, arrayOfByte5, 0, arrayOfByte1.length);
		    System.arraycopy(arrayOfByte2, 0, arrayOfByte5, arrayOfByte1.length, arrayOfByte2.length);
		    System.arraycopy(arrayOfByte3, 0, arrayOfByte5, arrayOfByte1.length + arrayOfByte2.length, arrayOfByte3.length);
		    System.arraycopy(arrayOfByte4, 0, arrayOfByte5, arrayOfByte1.length + arrayOfByte2.length + arrayOfByte3.length, arrayOfByte4.length);

		    return DkMD5.byte2Hex(arrayOfByte5);
	  }
	  
	  public static String makeDrmPart2MD5(String loginName,String paramString1,String paramString2) throws Exception {
		  String str1 = UUID.randomUUID().toString();
		  System.out.println(str1);
		    byte[] arrayOfByte1 = DkMD5.getMD5(loginName.getBytes("UTF-8"));
		    byte[] arrayOfByte2 = DkMD5.getMD5(paramString1.getBytes("UTF-8"));
		    byte[] arrayOfByte3 = DkMD5.getMD5(paramString2.getBytes("UTF-8"));
		    byte[] arrayOfByte4 = DkMD5.getMD5(str1.getBytes("UTF-8"));
		    
//		    byte[] arrayOfByte1 = DkMD5.MD5_32("18622931346").getBytes("UTF-8");
//		    byte[] arrayOfByte2 = DkMD5.MD5_32(paramString1).getBytes("UTF-8");
//		    byte[] arrayOfByte3 = DkMD5.MD5_32(paramString2).getBytes("UTF-8");
//		    byte[] arrayOfByte4 = DkMD5.MD5_32(str1).getBytes("UTF-8");
		    
		    byte[] arrayOfByte5 = new byte[arrayOfByte1.length + arrayOfByte2.length + arrayOfByte3.length + arrayOfByte4.length];
		    System.arraycopy(arrayOfByte1, 0, arrayOfByte5, 0, arrayOfByte1.length);
		    System.arraycopy(arrayOfByte2, 0, arrayOfByte5, arrayOfByte1.length, arrayOfByte2.length);
		    System.arraycopy(arrayOfByte3, 0, arrayOfByte5, arrayOfByte1.length + arrayOfByte2.length, arrayOfByte3.length);
		    System.arraycopy(arrayOfByte4, 0, arrayOfByte5, arrayOfByte1.length + arrayOfByte2.length + arrayOfByte3.length, arrayOfByte4.length);

		    return DkMD5.byte2Hex(DkMD5.getMD5(arrayOfByte5));
	  }

}
