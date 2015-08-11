package com.transinfo.tplus.util;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.jpos.iso.ISOUtil;

import com.transinfo.tplus.messaging.OnlineException;

/**
 * BatchConfig is the configuration class for Batch process.
 * It loads the properties from the config file to the static variables.
 */

public class Config extends Object{
	//constants
	public static final  String poolName                     =   "com.tivn.contactless.online.properties.gpac_db";
	public static String LAST_UPDATED_BY                     = "ONLINE";

	// public static String ISSUER_ID                         =  "OCEANBANK"; //FOR OJB
	//public static String ISSUER_ID                           =  "BCEL"; //FOR BIDV
	public static String ISSUER_ID                           =  "Issuer1"; //FOR BIDV

	public static final String ORIGINAL                      = "0";
	public static final String ORIGINAL_VOID                 = "1";
	public static final String ORIGINAL_REVERSE              = "2";
	public static final String REVERSE                       = "3";

	public static final String PREAUTH_COMPL                 = "4";

	/* public static final String ORIGINAL_PRE_TOPUP            = "4";
    public static final String ORIGINAL_PRE_TOPUP_REVERSE    = "5";
    public static final String ORIGINAL_PRE_TOPUP_PROCESSED  = "6";*/



	public static final String VOID                          = "VOID";
	public static final String REVERSAL                      = "REVERSAL";



	/* public String TRANSFER_CASH(String tranxType, String debitAccount, BigDecimal amount, String creditAccount, String tranxDes, boolean isDebit) throws Exception {

        String result = null;

        try{
            SmsBankingStub ws = new SmsBankingStub();

            if(isDebit){
                result  = ws.TRANSFERCASH("TIGIFTPAC", "GIFTPAC", tranxType, debitAccount, amount, creditAccount, tranxDes, "PREPAID", debitAccount.substring(0,2));
            }else{
                result  = ws.TRANSFERCASH("TIGIFTPAC", "GIFTPAC", tranxType, debitAccount, amount, creditAccount, tranxDes, "PREPAID", creditAccount.substring(0,2));
            }

        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }

        return result;
    }*/


	// returns next value of the sequence from the SEQUENCES table.
	public synchronized static String getSeqNumber(String issuerId, String sequenceName) throws Exception {
		String nextSeq = "";
		try {
			DBManager objDBManager = new DBManager();
			//StringBuffer sbfQuery = new StringBuffer();
			String sSQL = "SELECT GET_SEQUENCE_NUMBER ('"+ issuerId +"','"+ sequenceName +"') AS SEQVAL FROM DUAL ";

			objDBManager.executeSQL(sSQL);
			TPlusResultSet ResultSet = objDBManager.getResultSet() ;
			if(ResultSet.next()) {
				nextSeq = ResultSet.getString("SEQVAL");
			}
		}catch(Exception e){
			throw e;
		}
		return nextSeq;
	}

	// returns next value of the sequence from the SEQUENCES table.
	public synchronized static String getAWK(String cardNumber, String serialNo) throws Exception {
		String AWK = "";
		try
		{
			DBUtil util = new DBUtil();

			String query = "SELECT CONTACTLESS_CARD FROM CARD WHERE CARD_NO = '" + cardNumber + "'";
			String newCard = util.getFieldValue(query,"CONTACTLESS_CARD");


			String sql = "";

			if(newCard != null && newCard.equalsIgnoreCase("Y")){
				sql = "SELECT AMK_INDEX FROM CARD C, CARD_TYPE CT WHERE C.ISSUER_ID = CT.ISSUER_ID AND C.CARD_TYPE = CT.CARD_TYPE AND C.CARD_NO = '" + cardNumber + "'";
			}else{
				sql = "SELECT AMK_INDEX FROM OLD_CARDTYPE_INDEX";
			}

			String AMK = util.getFieldValue(sql,"AMK_INDEX");
			if(AMK==null || AMK.equals("")){
				throw new OnlineException("17", "022821", "Invalid AMK Index for card number: " + cardNumber);
			}
			String data = serialNo + cardNumber;
			byte[] encryptedData = new byte[32];
			byte[] icv = "00000000".getBytes();
			byte[] ocv = "00000000".getBytes();

			//EracomPHW.encryptData(AMK, true, icv, data.getBytes(), ocv, encryptedData);

			AWK = ISOUtil.hexString(encryptedData).substring(32);

		}catch (Exception e)
		{
			e.printStackTrace();
			throw new OnlineException("18", "022822", "Unable to generate AWK for card number:  "+cardNumber);
		}
		return AWK;
	}


	public synchronized static String getSettlementGLAccount() throws Exception {
		String glAccount = "";
		try
		{
			String sql = "SELECT PARAM_VALUE FROM CONFIG_MASTER WHERE ISSUER_ID = 'ASP' AND PARAM_NAME = 'GL_ACCOUNT' ";
			DBUtil util = new DBUtil();
			glAccount = util.getFieldValue(sql,"PARAM_VALUE");
			if(glAccount==null || glAccount.equals("")){
				throw new OnlineException("19", "022821", "Invalid Settlement GL Account");
			}

		}catch (Exception e)
		{
			e.printStackTrace();
			throw new OnlineException("19", "022822", "Unable to get Settlement GL Account");
		}
		return glAccount;
	}


	public synchronized static String getRedeemGLAccount() throws Exception {
		String glAccount = "";
		try
		{
			String sql = "SELECT PARAM_VALUE FROM CONFIG_MASTER WHERE ISSUER_ID = 'ASP' AND PARAM_NAME = 'REDEEM_GL_ACCOUNT' ";
			DBUtil util = new DBUtil();
			glAccount = util.getFieldValue(sql,"PARAM_VALUE");
			if(glAccount==null || glAccount.equals("")){
				throw new OnlineException("19", "022821", "Invalid Redeem GL Account");
			}

		}catch (Exception e)
		{
			e.printStackTrace();
			throw new OnlineException("19", "022822", "Unable to get Redeem GL Account");
		}
		return glAccount;
	}


	public synchronized static String getMerchantBranchId(String issuerId, String merchantNo) throws Exception {
		String branchId = "";
		try
		{
			String sql = "SELECT BRANCH_ID FROM MERCHANT WHERE MERCHANT_NO = '" + merchantNo + "' AND ISSUER_ID = '" + issuerId + "' ";
			DBUtil util = new DBUtil();
			branchId = util.getFieldValue(sql,"BRANCH_ID");
			if(branchId==null || branchId.equals("")){
				throw new OnlineException("21", "022821", "Could not get branch for merchant: " + merchantNo);
			}
		}catch (Exception e)
		{
			e.printStackTrace();
			throw new OnlineException("21", "022822", "Could not get branch for merchant: " + merchantNo);
		}
		return branchId;
	}


	public synchronized static String getMerchantGroupId(String issuerId, String merchantNo) throws Exception {
		String merchantGroupId = "";
		try
		{
			String sql = "SELECT MERCHANT_GROUP_ID FROM MERCHANT WHERE MERCHANT_NO = '" + merchantNo + "' AND ISSUER_ID = '" + issuerId + "' ";
			DBUtil util = new DBUtil();
			merchantGroupId = util.getFieldValue(sql,"MERCHANT_GROUP_ID");
			if(merchantGroupId==null || merchantGroupId.equals("")){
				throw new OnlineException("21", "022821", "Could not get group for merchant: " + merchantNo);
			}
		}catch (Exception e)
		{
			e.printStackTrace();
			throw new OnlineException("21", "022822", "Could not get group for merchant: " + merchantNo);
		}
		return merchantGroupId;
	}


	public synchronized static String getCardProductGLAccount(String cardNumber, String issuerId) throws Exception {
		String glAccount = "";
		try
		{
			String sql = "SELECT CP.GL_ACCOUNT FROM CARD_PRODUCT CP, CARD C WHERE  CP.ISSUER_ID = C.ISSUER_ID AND  CP.CARD_PRODUCT_ID = C.CARD_PRODUCT_ID AND  C.CARD_NO = '" + cardNumber +"' AND  C.ISSUER_ID = '" + issuerId + "'";
			DBUtil util = new DBUtil();
			glAccount = util.getFieldValue(sql,"GL_ACCOUNT");
			if(glAccount==null || glAccount.equals("")){
				throw new OnlineException("19", "022821", "Invalid Card Product GL Account");
			}

		}catch (Exception e)
		{
			e.printStackTrace();
			throw new OnlineException("19", "022822", "Unable to get Card Product GL Account");
		}
		return glAccount;
	}


	// returns next value of the sequence from the SEQUENCES table.
	public synchronized static String getCoreUser() throws Exception {
		String coreUser = "";
		try
		{
			String sql = "SELECT PARAM_VALUE FROM CONFIG_MASTER WHERE ISSUER_ID = 'ASP' AND PARAM_NAME = 'CORE_USER' ";
			DBUtil util = new DBUtil();
			coreUser = util.getFieldValue(sql,"PARAM_VALUE");
			if(coreUser==null || coreUser.equals("")){
				throw new OnlineException("20", "022821", "Invalid Core User");
			}

		}catch (Exception e)
		{
			e.printStackTrace();
			throw new OnlineException("20", "022822", "Unable to get Core User");
		}
		return coreUser;
	}


	public static String toString(double dblVal, int multVal) throws Exception {
		BigInteger bd = new BigDecimal(dblVal * multVal).toBigInteger();
		return bd.toString();
	}


}
