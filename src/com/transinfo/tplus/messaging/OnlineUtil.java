package com.transinfo.tplus.messaging;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.jpos.iso.ISOUtil;

public class OnlineUtil
{
  public OnlineUtil()
  {
  }
  public static boolean checkLuhn (String cardNumber)
  {
    if (getCheckDigit(cardNumber.substring(0,15))==(cardNumber.charAt(15)-'0'))
      return true;
    return false;
  }

  public static int getCheckDigit(String number) { //Luhn check modulus 10
		// follow VISA standard
		char[] digits = number.toCharArray();
		int sum = 0;
		int multiplier = 1;
		for (int i=digits.length-1; i>=0; i--) {
      	int digit;
	   	try {
   	   	digit = Integer.parseInt(String.valueOf(digits[i]));
      	} catch (Exception e) {
				return -1;
	      }
			if (multiplier == 1) multiplier = 2;
			else if (multiplier == 2) multiplier = 1;
			int multiple = digit * multiplier;
			sum = sum + (multiple % 10) + (multiple / 10);
		}
      int chk = 10 - (sum % 10);
      if (chk == 10) chk = 0;
 		return chk;

	}

  public static byte[] encrypt(byte[] keyData, byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException{
    SecretKey key = new SecretKeySpec(keyData, "DES");
    Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
    cipher.init(Cipher.ENCRYPT_MODE, key);
    byte[] cipherText = cipher.doFinal(data);

    return cipherText;
  }

  public static byte[] decrypt(byte[] keyData, byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException{
    SecretKey key = new SecretKeySpec(keyData, "DES");
    Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
    cipher.init(Cipher.DECRYPT_MODE, key);
    byte[] cipherText = cipher.doFinal(data);

    return cipherText;
  }

  //Hiep
  public static byte[] encrypt3DES(byte[] keyData, byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException{

    SecretKey key = new SecretKeySpec(keyData, "DESede");
    Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");
    cipher.init(Cipher.ENCRYPT_MODE, key);
    byte[] cipherText = cipher.doFinal(data);

    return cipherText;
  }

  //Hiep
  public static byte[] decrypt3DES(byte[] keyData, byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException{
    SecretKey key = new SecretKeySpec(keyData, "DESede");
    Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");
    cipher.init(Cipher.DECRYPT_MODE, key);
    byte[] cipherText = cipher.doFinal(data);

    return cipherText;
  }

  public static byte[] buildPINBlock(String cardNumber, String clearPIN){
    byte[] s1 = new byte[16];
    byte[] s2 = new byte[16];
    byte[] pinBlock = new byte[8];

    // construct s1
    s1[0] = (byte)0x0;    // 0: filled
    s1[1] = (byte)clearPIN.length();    // PIN Len
    for (int i=0; i<clearPIN.length(); i++){
      s1[2+i] = (byte)(clearPIN.charAt(i) - '0');
    }
    for (int i=2+clearPIN.length(); i<16; i++){
      s1[i] = 0xF;    // filled
    }

    // construct s2
    s2[0] = (byte)0x0;    // filled
    s2[1] = (byte)0x0;    // filled
    s2[2] = (byte)0x0;    // filled
    s2[3] = (byte)0x0;    // filled
    for (int i=0; i<12; i++){
      s2[4+i] = (byte)(cardNumber.charAt(3+i) - '0');
    }

    // construct pinBlock
    for (int i=0 ;i<8; i++){
      pinBlock[i] = (byte)(((s1[2*i] ^ s2[2*i]) << 4) | (s1[2*i+1] ^ s2[2*i+1]));
    }

    return pinBlock;
  }

  public static String getClearPIN(String cardNumber, byte[] pinBlock){
    byte[] s1 = new byte[16];
    byte[] s2 = new byte[16];

    // construct s2
    s2[0] = (byte)0x0;    // filled
    s2[1] = (byte)0x0;    // filled
    s2[2] = (byte)0x0;    // filled
    s2[3] = (byte)0x0;    // filled
    for (int i=0; i<12; i++){
      s2[4+i] = (byte)(cardNumber.charAt(3+i) - '0');
    }

    for (int i=0 ;i<8; i++){
      byte leftNipple, rightNipple;
      leftNipple = (byte)((pinBlock[i] & 0xF0) >> 4);
      rightNipple = (byte)(pinBlock[i] & 0x0F);
      s1[2*i] = (byte)(leftNipple ^ s2[2*i]);
      s1[2*i+1] = (byte)(rightNipple ^ s2[2*i+1]);
    }
    if ((s1[0] != 0) || (s1[1] <= 0) || (s1[1] > 15)){
      throw new RuntimeException("Invalid PIN format");
    }
    for (int i=2; i<2+s1[1]; i++){
      s1[i] = (byte)('0' + s1[i]);
    }
    return new String(s1, 2, s1[1]);
  }

  public static byte[] encryptClearPIN(byte[] keyData, String cardNumber, String clearPIN) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException
  {
    byte[] clearPINBlock = buildPINBlock(cardNumber, clearPIN);
    return encrypt3DES(keyData, clearPINBlock);
  }

  public static void test1() throws Exception
  {

    String cardNumber = "4368050000003098";
    String clearPIN = "5196";

    byte[] clearPINBlock = buildPINBlock(cardNumber, clearPIN);
    System.out.println(ISOUtil.hexString(clearPINBlock));
    // 045116affffffcf6

    System.out.println(getClearPIN(cardNumber, clearPINBlock));
  }

  public static String test2(String cardNumber,String clearPIN) throws Exception
  {

    String sKey = "111111111111111111111111111111111111111111111111";
    //String cardNumber = "6229749000053060";
    //String clearPIN = "325143";

    byte[] clearPINBlock = buildPINBlock(cardNumber, clearPIN);
    System.out.println(ISOUtil.hexString(clearPINBlock));
    byte[] keyData = ISOUtil.hex2byte(sKey);
    byte[] encyptedPINBlock = encryptClearPIN(keyData, cardNumber, clearPIN);
    System.out.println(ISOUtil.hexString(encyptedPINBlock));
    return ISOUtil.hexString(encyptedPINBlock);
  }

  private static void testTerminal() throws Exception{
    byte[] masterKey = ISOUtil.hex2byte("555555555555555555555555555555555555555555555555");
    byte[] encryptedKey = ISOUtil.hex2byte("56006D140360EDF655DE1FFDBB49EAD0");

    byte[] clearKey = decrypt3DES(masterKey, encryptedKey);
    System.out.println("Encrypt key "+ISOUtil.hexString(clearKey)+"   "+Base64Coder.encode(clearKey));

    /*byte[] encryptedPINBlock = ISOUtil.hex2byte(ISOUtil.hexString(clearKey));
    byte[] clearPINBlock = decrypt(masterKey, encryptedPINBlock);
    System.out.println(ISOUtil.hexString(clearPINBlock)+"   "+Base64Coder.encode(clearPINBlock));*/

   /* String cardNumber = "4839123456789019";
    String clearPIN = getClearPIN(cardNumber, clearPINBlock);

    System.out.println(clearPIN);*/
  }

  public static void main(String[] args) throws Exception
  {
	  
  }
}
