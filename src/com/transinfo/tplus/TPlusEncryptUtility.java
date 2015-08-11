/**
* Copyright (c) 2007-2008 Trans-Info Pte Ltd. Singapore. All Rights Reserved.
* This work contains trade secrets and confidential material of
* Trans-Info Pte Ltd. Singapore and its use of disclosure in whole
* or in part without express written permission of
* Trans-Info Pte Ltd. Singapore. is prohibited.
* Date of Creation   : Feb 25, 2008
* Version Number     : 1.0
*                   Modification History:
* Date          Version No.         Modified By           Modification Details.
*/

package com.transinfo.tplus;


import java.security.KeyException;
import java.security.Security;

import xjava.security.Cipher;
import cryptix.provider.key.RawSecretKey;
import cryptix.util.core.Hex;


/**
 *
 * This is a utility class is used for encryption. Cryptix package is
 * used for encryption, with Triple-DES algorithm for all encryptions.The Master Key
 * for encryption is stored in a key store file.
 * This master key will also be encrypted using a password. The algorithm
 * used for this encryption is RC4 with the password as the key.
 */
public class TPlusEncryptUtility extends Object {

    // The key for encryption of encryption password only
    // This is only a local key used to encrypt the password that is used to
    // encrypt the master key.
    // This encrypted password will be stored in the configuration file.
    // The encrypted master key will be stored in a keystore file
    private static String strLocalKey = new String("1A812A419C63AA771AD9F61345CC0CE633812AA19C630022");


    /** Adds the cryptix provider dynamically */
    static {
          Security.addProvider(new cryptix.provider.Cryptix());
    }


    /**
    * @param strClearText is the text which is to be encrypted
    * @return String, the 3DES encrypted string.
    * @exception TPlusException when the key is corrupted or not available & when data could
    *  not be encrypted
    */
    public static String encrypt(String strClearText, String strKey) throws TPlusException{

      try{
            RawSecretKey rskKey = new RawSecretKey("DES-EDE3",Hex.fromString(strKey));
            //Use Triple -DES , cryptix package
            Cipher ch3Des=Cipher.getInstance("DES-EDE3/ECB/PKCS#5","Cryptix");
            ch3Des.initEncrypt(rskKey);
            byte[] bytClear = strClearText.getBytes();
            byte[] bytCipherText = ch3Des.crypt(bytClear);

            return Hex.toString(bytCipherText);

       }catch(KeyException keyEx){
            throw new TPlusException(TPlusCodes.ENCR_DCR_FAIL,TPlusCodes.getErrorDesc(TPlusCodes.ENCR_DCR_FAIL) + " Error :" + keyEx.getMessage());
       }catch(Exception ex){
            throw new TPlusException(TPlusCodes.ENCR_DCR_FAIL,TPlusCodes.getErrorDesc(TPlusCodes.ENCR_DCR_FAIL) + " Error :" + ex.getMessage());
       }
    } //end encrypt


    /**
    * Encrypts the String sent with DES encryption
    * @param strClearText is the text which is to be encrypted
    * @param strKey is the key which is to be encrypted with.
    * @return String, the DES encrypted string.
    * @exception TPlusException when the key is corrupted or not available & when data could
    *  not be encrypted
    */
    public static String encryptDES(String strClearText, String strKey) throws TPlusException{

      try{
            RawSecretKey rskKey = new RawSecretKey("DES",Hex.fromString(strKey));
            //Use DES , cryptix package
            Cipher chDes=Cipher.getInstance("DES","Cryptix");
            chDes.initEncrypt(rskKey);
            byte[] bytClear = strClearText.getBytes();
            byte[] bytCipherText = chDes.crypt(bytClear);

            return Hex.toString(bytCipherText);

       }catch(KeyException keyEx){
            throw new TPlusException(TPlusCodes.ENCR_DCR_FAIL,TPlusCodes.getErrorDesc(TPlusCodes.ENCR_DCR_FAIL) + " Error :" + keyEx.getMessage());
       }catch(Exception ex){
            throw new TPlusException(TPlusCodes.ENCR_DCR_FAIL,TPlusCodes.getErrorDesc(TPlusCodes.ENCR_DCR_FAIL) + " Error :" + ex.getMessage());
       }
    } //end encrypt



    /**
    * @param strEncrText is the text which is to be decrypted
    * @return String, the decrypted string.
    * @exception TPlusException when the key is corrupted or not available & when data could
    *  not be decrypted
    */
    public static String decrypt(String strEncrText, String strKey) throws TPlusException{
      try{

            RawSecretKey rskKey = new RawSecretKey("DES-EDE3",Hex.fromString(strKey));
            Cipher ch3Des=Cipher.getInstance("DES-EDE3/ECB/PKCS#5","Cryptix");
            ch3Des.initDecrypt(rskKey);
            byte[] bytDecrypted = ch3Des.crypt(Hex.fromString(strEncrText));

            return new String(bytDecrypted);

       }catch(KeyException keyEx){
		   throw new TPlusException(TPlusCodes.ENCR_DCR_FAIL,TPlusCodes.getErrorDesc(TPlusCodes.ENCR_DCR_FAIL) + " Error :" + keyEx.getMessage());
       }catch(Exception ex){
		   throw new TPlusException(TPlusCodes.ENCR_DCR_FAIL,TPlusCodes.getErrorDesc(TPlusCodes.ENCR_DCR_FAIL) + " Error :" + ex.getMessage());
       }
    } //end decrypt


    /**
    * @param strClearPwd is the password that is to be decrypted
    * @return String, the encrypted password string.
    * @exception TPlusException when the key is corrupted or not available & when data could
    *  not be decrypted
    */
    public static String encrPassword (String strClearPwd) throws TPlusException{
        try{
            String strEncrPwd = encrypt(strClearPwd,strLocalKey);
            return strEncrPwd;
        }catch(TPlusException TPlusEx){
            throw TPlusEx;
        }
    }

    /**
    * @param strEncrPwd is the password which is to be decrypted
    * @return String, the decrypted password string.
    * @exception TPlusException when the key is corrupted or not available & when data could
    *  not be decrypted
    */
    public static String decrPassword (String strEncrPwd) throws TPlusException {
        try{
             String strDecrPwd = decrypt(strEncrPwd,strLocalKey);
             return strDecrPwd;
         }catch(TPlusException TPlusEx){
             throw TPlusEx;
         }
    }

    /**
    * @param strClearPwd is the password string which is used as a key for encryption
    * of the master key.The password is converted into a byte array with length of 96
    * bytes and used as a key.
    * @param strClearKey is the master key in clear, before encryption. This encrypted master
    * key will be stored in a keystore.
    * @return String, the encrypted master key string.
    * @exception TPlusException when the key is corrupted or not available & when data could
    *  not be decrypted
    */
    public static String getEncrKey (String strClearPwd, String strClearKey)
                                                          throws TPlusException{

        try{

            String strEncrKey = "";
            byte[] bytClear = strClearPwd.getBytes();
            String strkey = "";
            for(int i=0;i < bytClear.length; i++ ){
                strkey = strkey + bytClear[i];
            }
            int len = strkey.length();

            //Use 96 as the length of 32 chars (32 X 3 = 96) for the key as 96
            // is standard key length, each char will transalate to 3 bytes maximum
            if(strkey.length() < 96){
                for(int k=0; k < (96 - len); k++){
                    strkey = strkey + "0";
                }
            }

            Cipher chRC4 = Cipher.getInstance("RC4", "Cryptix");
            RawSecretKey rsKey = new RawSecretKey("RC4", Hex.fromString(strkey));
            chRC4.initEncrypt(rsKey);
            byte[] bytEncr = chRC4.crypt(Hex.fromString(strClearKey));
            strEncrKey = Hex.toString(bytEncr);
            return strEncrKey;

         }catch(KeyException keyEx){
              throw new TPlusException(TPlusCodes.ENCR_DCR_FAIL,TPlusCodes.getErrorDesc(TPlusCodes.ENCR_DCR_FAIL) + " Error :" + keyEx.getMessage());
         }catch(Exception ex){
              throw new TPlusException(TPlusCodes.ENCR_DCR_FAIL,TPlusCodes.getErrorDesc(TPlusCodes.ENCR_DCR_FAIL) + " Error :" + ex.getMessage());
         }
    }


    /**
    * @param strClearPwd is the password string which is used as a key for encryption
    * of the master key.The password is converted into a byte array with length of 96
    * bytes and used as a key.
    * @param strEncrKey is the master key in after encryption. This encrypted master
    * is read from a keystore.
    * @return String, the decrypted master key string.
    * @exception TPlusException when the key is corrupted or not available & when data could
    *  not be decrypted
    */
    public static String getDecrKey(String strClearPwd, String strEncrKey)
                                                          throws TPlusException{

        try{
            String strDecrKey = "";
            byte[] bytClear = strClearPwd.getBytes();
            String strkey = "";
            for(int i=0;i < bytClear.length; i++ ){
                strkey = strkey + bytClear[i];
            }
            int len = strkey.length();
            //Use 96 as the length of 32 chars (32 X 3 = 96) for the key as 96
            // is standard key length, each char will transalate to 3 bytes maximum
            if(strkey.length() < 96){
                for(int k=0; k < (96 - len); k++){
                    strkey = strkey + "0";
                }
            }

            Cipher chRC4 = Cipher.getInstance("RC4", "Cryptix");
            RawSecretKey rsKey = new RawSecretKey("RC4", Hex.fromString(strkey));
            chRC4.initDecrypt(rsKey);
            byte[] bytDecr = chRC4.crypt(Hex.fromString(strEncrKey));
            strDecrKey = Hex.toString(bytDecr);

            return strDecrKey;

         }catch(KeyException keyEx){
              throw new TPlusException(TPlusCodes.ENCR_DCR_FAIL,TPlusCodes.getErrorDesc(TPlusCodes.ENCR_DCR_FAIL) + " Error :" + keyEx.getMessage());
         }catch(Exception ex){
              throw new TPlusException(TPlusCodes.ENCR_DCR_FAIL,TPlusCodes.getErrorDesc(TPlusCodes.ENCR_DCR_FAIL) + " Error :" + ex.getMessage());
         }
    } //end


} //end class