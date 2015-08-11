package com.transinfo.tplus.javabean;

import java.util.HashMap;

import com.transinfo.tplus.TPlusUtility;
import com.transinfo.tplus.db.TransactionDB;
import com.transinfo.tplus.messaging.OnlineException;
import com.transinfo.tplus.messaging.parser.IParser;
import com.transinfo.tplus.util.Config;
import com.transinfo.tplus.util.DBManager;
import com.transinfo.tplus.util.TPlusResultSet;

@SuppressWarnings("unchecked")
public class TransactionDataBean {

	private DBManager objDBManager = null;
	private boolean recordExist = false;

	private String issuerId = "";
	private String terminalNo = "";
	private String merchantNo = "";
	private String cardNo = "";
	private String STAN =  "";
	private String bachNo = "";
	private String processingCode = "";
	private String RRN = "";
	private String cardPresentFlag = "";
	private String transactionType = "";
	private String transactionId = "";
	private String transactionAmt = "";
	private String transactionDate = "";
	private String settlementDate = "";
	private String batchStatus = "";
	private String transactionStatus = "";
	private String invoiceNo = "";

	private String pointSchemeId = "";
	private String pointEarn = "";
	private String pointRedeem = "";
	private String pointBalance = "";

	private String cashReload = "";
	private String cashSpend = "";
	private String cashBalance = "";
	private String spendAmount = "";
	private String authAmount = "";
	private String reloadAmount = "";
	private String leftSaleAmount = "";
	private String oldLeftSaleAmount = "";
	private String totalLeftPreTopupAmount = "";
	private String cardClassId = "";
	private String oldCardClassId = "";
	private String convertRatio = "";
	private String convertAmount = "";

	private String rebateSchemeId = "";
	private String rebateEarn = "";
	private String rebateSpend = "";
	private String rebateBalance = "";

	private String couponSchemeId = "";
	private String couponId = "";
	private String couponSerialNo = "";
	private String couponRedeemAmt = "";

	private String transRecordType = "";
	private String mrktAwardPoints = "";
	private String mrktAwardCash = "";
	private String deactivationType = "";
	private String responseCode = "";
	private String destinationCardNo = "";
	private String approvalCode = "";
	private String transmissionDate = "";
	private String refundAmount = "";
	private String offlineBatchNumber = "";
	private String cardSerial = "";
	private String coreRefNo = "";
	private String refNo = "";
	private String glTranxType="";

	private String acqBIN = "";
	private String tranxSettledAmt = "";
	private String tranxCurr = "";
	private String tranxSettledCurr = "";
	private String tranxCHAmt = "";
	private String tranxFee = "";
	private String tranxDate = "";
	private String tranxTime = "";
	private String mcc= "" ;
	private String track2Data = "";
	private String deleted = "";
	private String settled = "";
	private String posEntryMode = "";
	private String isAuthComplete = "";
	private String recon = "";
	private String mti = "";
	private String cupCutOff = "";
	private String F90 = "";
	private String acqBinType = "";
	private String description = "";


	public TransactionDataBean() {
		objDBManager = new DBManager();
	}

	public void execute()throws Exception {
		try{
			String transRecordType = this.getTransRecordType();
			System.out.println("transRecordType :===: "+transRecordType.trim());

			StringBuffer query = new StringBuffer();
			query.append("SELECT TRANSACTION_ID, ISSUER_ID, MERCHANT_NO, TERMINAL_NO, CARD_NO, PROCESSING_CODE,");
			query.append("CARD_PRESENT_FLAG, STAN, RRN, TRANSACTION_AMT, TRANSACTION_DATE, TRANSACTION_TYPE, ");
			query.append("TRANSACTION_STATUS, BATCH_NO, BATCH_STATUS, SETTLEMENT_DATE, INVOICE_NO, POINT_SCHEME_ID,");
			query.append("POINT_EARN, POINT_REDEEM, POINT_BALANCE, REBATE_SCHEME_ID, REBATE_EARN, REBATE_REDEEM,");
			query.append("REBATE_BALANCE, COUPON_SCHEME_ID, COUPON_ID, COUPON_SERIAL_NO, COUPON_REDEEM_AMT, CASH_RELOAD,");
			query.append("CASH_SPEND, CASH_BALANCE, DEACTIVATION_TYPE, NVL(MRKT_POINT_AWARD,0) AS MRKT_POINT_AWARD, ");
			query.append("NVL(MRKT_CASH_AWARD ,0) AS MRKT_CASH_AWARD, RESPONSE_CODE, APPROVAL_CODE, TRANSMISSION_DATE, REFUND_AMOUNT, OFFLINE_BATCH_NUMBER, CARD_SERIAL, CORE_REF_NO, OLD_LEFT_SALE_AMOUNT, OLD_CARD_CLASS_ID, CONVERT_AMOUNT, DESTINATION_CARD_NO FROM TRANSACTION ");
			query.append(" WHERE TERMINAL_NO='"+this.getTerminalNo()+"' ");
			query.append(" AND MERCHANT_NO='"+this.getMerchantNo()+"' ");
			query.append(" AND CARD_NO='"+this.getCardNo()+"' AND TRANSACTION_STATUS ='0' AND TRANSACTION_TYPE NOT IN ('REVERSAL')");

			//  query.append(" AND TRANSACTION_STATUS ='"+this.getTransactionStatus()+"' ");
			//  query.append(" AND RESPONSE_CODE ='"+this.getResponseCode()+"' ");

			if(transRecordType.trim().equals("REVERSAL") || transRecordType.trim().equals("VOID"))   query.append(" AND STAN='" + this.getSTAN()+ "' AND TRANSMISSION_DATE='" + this.getTransmissionDate()+ "' ");
			// else if(transRecordType.trim().equals("VOID") )  query.append(" AND RRN='" + this.getRRN() + "' AND APPROVAL_CODE='" + this.getApprovalCode() + "' ");
			else if(transRecordType.trim().equals("PREAUTHCOMP") )  query.append(" AND TRANSACTION_TYPE= 'PREAUTH' AND APPROVAL_CODE='" + this.getApprovalCode() + "' ");
			System.out.println(query.toString());
			objDBManager.executeSQL(query.toString());
			TPlusResultSet rs = objDBManager.getResultSet();

			if(rs.next()) {
				this.setIssuerId(rs.getString("ISSUER_ID"));
				this.setTerminalNo(rs.getString("TERMINAL_NO"));
				this.setMerchantNo(rs.getString("MERCHANT_NO"));
				this.setCardNo(rs.getString("CARD_NO"));
				this.setPointSchemeId(rs.getString("POINT_SCHEME_ID"));
				this.setCouponSchemeId(rs.getString("COUPON_SCHEME_ID"));
				this.setSTAN(rs.getString("STAN"));
				this.setBachNo(rs.getString("BATCH_NO"));
				this.setProcessingCode(rs.getString("PROCESSING_CODE"));
				this.setRRN(rs.getString("RRN"));
				this.setCardPresentFlag(rs.getString("CARD_PRESENT_FLAG"));
				this.setTransactionType(rs.getString("TRANSACTION_TYPE"));
				this.setTransactionId(rs.getString("TRANSACTION_ID"));
				this.setTransactionAmt(rs.getString("TRANSACTION_AMT"));
				this.setTransactionStatus(rs.getString("TRANSACTION_STATUS"));
				this.setBatchStatus(rs.getString("BATCH_STATUS"));

				this.setPointEarn(rs.getString("POINT_EARN"));
				this.setPointRedeem(rs.getString("POINT_REDEEM"));
				this.setPointBalance(rs.getString("POINT_BALANCE"));

				this.setCashBalance(rs.getString("CASH_BALANCE"));
				this.setCashReload(rs.getString("CASH_RELOAD"));
				this.setCashSpend(rs.getString("CASH_SPEND"));

				this.setCouponId(rs.getString("COUPON_ID"));
				this.setCouponSerialNo(rs.getString("COUPON_SERIAL_NO"));
				this.setCouponRedeemAmt(rs.getString("COUPON_REDEEM_AMT"));

				this.setRebateEarn(rs.getString("REBATE_EARN"));
				this.setRebateSpend(rs.getString("REBATE_REDEEM"));
				this.setRebateBalance(rs.getString("REBATE_BALANCE"));

				this.setMrktAwardPoints(rs.getString("MRKT_POINT_AWARD"));
				this.setMrktAwardCash(rs.getString("MRKT_CASH_AWARD"));

				this.setApprovalCode(rs.getString("APPROVAL_CODE"));
				this.setResponseCode(rs.getString("RESPONSE_CODE"));
				this.setTransmissionDate(rs.getString("TRANSMISSION_DATE"));
				this.setRefundAmount(rs.getString("REFUND_AMOUNT"));

				this.setCardSerial(rs.getString("CARD_SERIAL"));
				this.setOfflineBatchNumber(rs.getString("OFFLINE_BATCH_NUMBER"));
				this.setDestinationCardNo(rs.getString("DESTINATION_CARD_NO"));
				this.setCoreRefNo(rs.getString("CORE_REF_NO"));
				this.setOldLeftSaleAmount(rs.getString("OLD_LEFT_SALE_AMOUNT"));
				this.setOldCardClassId(rs.getString("OLD_CARD_CLASS_ID"));
				this.setConvertAmount(rs.getString("CONVERT_AMOUNT"));

				this.setRecordExist(true);
			}
		}catch(Exception vep) {
			System.out.println("Exception while getting Transaction information : "+vep.toString());
		}
	}

	public String generateTransactionId() throws Exception {
		String tranxId = "";
		try{
			tranxId = Config.getSeqNumber(this.getIssuerId(),"TRANSACTION_ID");
		}catch(Exception vep) {
			System.out.println("Exception while generating the Transaction ID : "+vep.toString());
			throw vep;
		}
		return tranxId;
	}

	public String getInsertSql(String transactionId)throws Exception{
		try{
			this.setTransactionId(transactionId);
			return this.getInsertSql();
		}catch(Exception vep) {
			System.out.println("Exception while building the transaction table query : "+vep.toString());
			throw vep;
		}
	}

	public String getInsertSql() throws Exception {
		StringBuffer strQuery = new StringBuffer();
		try{
			strQuery.append("INSERT INTO TRANSACTION ( ");
			strQuery.append("TRANSACTION_ID, ISSUER_ID, MERCHANT_NO, TERMINAL_NO, CARD_NO, PROCESSING_CODE,  ");
			strQuery.append("STAN, RRN, TRANSACTION_AMT, TRANSACTION_DATE, TRANSACTION_TYPE,  ");
			strQuery.append("TRANSACTION_STATUS, BATCH_NO, SETTLEMENT_DATE, INVOICE_NO, POINT_SCHEME_ID,  ");
			strQuery.append("POINT_EARN, POINT_REDEEM, POINT_BALANCE, REBATE_SCHEME_ID, REBATE_EARN, REBATE_REDEEM,  ");
			strQuery.append("REBATE_BALANCE, COUPON_SCHEME_ID, COUPON_ID, COUPON_SERIAL_NO, COUPON_REDEEM_AMT, CASH_RELOAD,  ");
			strQuery.append("CASH_SPEND, CASH_BALANCE, MRKT_POINT_AWARD,MRKT_CASH_AWARD,DEACTIVATION_TYPE, RESPONSE_CODE,");
			strQuery.append("APPROVAL_CODE, TRANSMISSION_DATE, REFUND_AMOUNT, OFFLINE_BATCH_NUMBER, CARD_SERIAL, CORE_REF_NO,");
			strQuery.append("LEFT_SALE_AMOUNT, OLD_LEFT_SALE_AMOUNT,  TOTAL_LEFT_PRE_TOPUP, CARD_CLASS_ID, OLD_CARD_CLASS_ID, ");
			strQuery.append("CONVERT_RATIO, CONVERT_AMOUNT, DESTINATION_CARD_NO,ACQ_BIN,SETTLED_AMT,CARDHOLDER_AMT,TRANX_FEE,TRANSACTION_CURR,");
			strQuery.append("SETTLED_CURR,TRANXDATE,TRANXTIME,MCC,TRACK2DATE,DELETED,SETTLED,POSENTRYMODE,ISAUTHCOMPLETE,RECON,F90,MTI,DESCRIPTION)  ");
			strQuery.append("VALUES (");

			strQuery.append("'"+this.getTransactionId()+"',");
			strQuery.append("'"+this.getIssuerId()+"',");
			strQuery.append("'"+this.getMerchantNo()+"',");
			strQuery.append("'"+this.getTerminalNo()+"',");
			strQuery.append("'"+this.getCardNo()+"',");
			strQuery.append("'"+this.getProcessingCode()+"',");
			strQuery.append("'"+this.getSTAN()+"',");
			strQuery.append("'"+this.getRRN()+"',");
			strQuery.append("'"+this.getTransactionAmt()+"',");
			strQuery.append("SYSDATE,");
			strQuery.append("'"+this.getTransactionType()+"',");
			strQuery.append("'"+this.getTransactionStatus()+"',");

			if(this.getBachNo()!=null && !this.getBachNo().equals("")){
				strQuery.append("'"+this.getBachNo()+"',");
			}else{
				strQuery.append("'000000',");
			}

			if(this.settlementDate !=null && !this.settlementDate.equals("")){
				strQuery.append("TO_DATE('"+this.settlementDate+"','DD/MM/YYYY HH24:MI:SS'), ");
			}else{
				strQuery.append("NULL,");
			}

			if(this.getInvoiceNo()!=null){
				strQuery.append("'"+this.getInvoiceNo()+"',");
			}else{
				strQuery.append("'',");
			}

			strQuery.append("'"+this.getPointSchemeId()+"',");
			strQuery.append("'"+this.getPointEarn()+"',");
			strQuery.append("'"+this.getPointRedeem()+"',");
			strQuery.append("'"+this.getPointBalance()+"',");
			strQuery.append("'"+this.getRebateSchemeId()+"',");
			strQuery.append("'"+this.getRebateEarn()+"',");
			strQuery.append("'"+this.getRebateSpend()+"',");
			strQuery.append("'"+this.getRebateBalance()+"',");
			strQuery.append("'"+this.getCouponSchemeId()+"',");
			strQuery.append("'"+this.getCouponId()+"',");
			strQuery.append("'"+this.getCouponSerialNo()+"',");
			strQuery.append("'"+this.getCouponRedeemAmt()+"',");
			strQuery.append("'"+this.getCashReload()+"',");
			strQuery.append("'"+this.getCashSpend()+"',");
			strQuery.append("'"+this.getCashBalance()+"', ");
			strQuery.append("'"+this.getMrktAwardPoints()+"', ");
			strQuery.append("'"+this.getMrktAwardCash()+"', ");
			strQuery.append("'"+this.getDeactivationType()+"', ");
			strQuery.append("'"+this.getResponseCode()+"', ");

			if(this.getApprovalCode()!=null){
				strQuery.append("'"+this.getApprovalCode()+"', ");
			}else{
				strQuery.append("'',");
			}

			strQuery.append("'"+this.getTransmissionDate()+"', ");
			strQuery.append("'"+this.getRefundAmount()+"',");
			strQuery.append("'"+this.getOfflineBatchNumber()+"',");
			strQuery.append("'"+this.getCardSerial()+"',");
			strQuery.append("'"+this.getCoreRefNo()+"',");

			if(this.getLeftSaleAmount()!=null && !this.getLeftSaleAmount().equals("")){
				strQuery.append("'"+this.getLeftSaleAmount()+"', ");
			}else{
				strQuery.append("'0',");
			}

			if(this.getOldLeftSaleAmount()!=null && !this.getOldLeftSaleAmount().equals("")){
				strQuery.append("'"+this.getOldLeftSaleAmount()+"', ");
			}else{
				strQuery.append("'0',");
			}

			if(this.getTotalLeftPreTopupAmount()!=null && !this.getTotalLeftPreTopupAmount().equals("")){
				strQuery.append("'"+this.getTotalLeftPreTopupAmount()+"', ");
			}else{
				strQuery.append("'0',");
			}

			if(this.getCardClassId()!=null && !this.getCardClassId().equals("")){
				strQuery.append("'"+this.getCardClassId()+"', ");
			}else{
				strQuery.append("'',");
			}

			if(this.getOldCardClassId()!=null && !this.getOldCardClassId().equals("")){
				strQuery.append("'"+this.getOldCardClassId()+"', ");
			}else{
				strQuery.append("'',");
			}

			if(this.getConvertRatio()!=null && !this.getConvertRatio().equals("")){
				strQuery.append("'"+this.getConvertRatio()+"', ");
			}else{
				strQuery.append("'0',");
			}

			if(this.getConvertAmount()!=null && !this.getConvertAmount().equals("")){
				strQuery.append("'"+this.getConvertAmount()+"', ");
			}else{
				strQuery.append("'0',");
			}

			strQuery.append("'"+this.getDestinationCardNo()+"', ");

			// Added for BCEL


			strQuery.append("'"+this.getAcqBin()+"', ");
			strQuery.append("'"+this.getTranxCHAmt()+"',");
			strQuery.append("'"+this.getTranxSettledAmt()+"', ");
			strQuery.append("'"+this.getTranxFee()+"',");
			strQuery.append("'"+this.getTranxCurr()+"',");
			strQuery.append("'"+this.getTranxSettledCurr()+"',");
			strQuery.append("'"+this.getTranxDate()+"',");
			strQuery.append("'"+this.getTranxTime()+"',");
			strQuery.append("'"+this.getMcc()+"',");
			strQuery.append("'"+this.getTrack2Data()+"',");
			strQuery.append("'"+this.getDeleted()+"',");
			strQuery.append("'"+this.getSettled()+"',");
			strQuery.append("'"+this.getPosEntryMode()+"',");
			strQuery.append("'"+this.getIsAuthComplete()+"',");
			strQuery.append("'"+this.getRecon()+"', ");
			strQuery.append("'"+this.getF90()+"', ");
			strQuery.append("'"+this.getMTI()+"', ");
			strQuery.append("'"+this.getDescription()+"' ");

			strQuery.append(")");
			System.out.println(strQuery.toString());
		}catch(Exception vep) {
			System.out.println("Exception while building the transaction table query : "+vep.toString());
			throw vep;
		}
		return strQuery.toString();
	}


	public String getGLInsertSql() throws Exception {
		StringBuffer strQuery = new StringBuffer();
		try{
			strQuery.append("INSERT INTO CARD_GL ( ");
			strQuery.append(" TRANSACTION_ID,DATETIME,CARD_NO,ISSUER_ID,TRANX_TYPE,GL_TYPE,REFNO,AMOUNT,BALANCE )");
			strQuery.append(" VALUES (");
			strQuery.append("'"+ this.getTransactionId()+"', ");
			strQuery.append(" SYSDATE, ");
			strQuery.append("'"+ this.getCardNo()+"', ");
			strQuery.append("'"+ this.getIssuerId()+"', ");
			strQuery.append("'"+ this.getTransactionType()+"', ");
			strQuery.append("'"+ this.getGLType()+"', ");
			strQuery.append("'"+ this.getRefNo()+"', ");
			strQuery.append("'"+ this.getTranxSettledAmt()+"', ");
			strQuery.append("'"+ this.getCashBalance()+"') ");

			System.out.println(strQuery.toString());
		}catch(Exception vep) {
			System.out.println("Exception while building the transaction table query : "+vep.toString());
			throw vep;
		}
		return strQuery.toString();
	}


	public String getTranxPinActivityInsertSql() throws Exception {
		StringBuffer strQuery = new StringBuffer();
		try{
			strQuery.append("INSERT INTO PINACTIVITY_TRANXLOG ( ");
			strQuery.append(" TRANSACTION_ID,DATETIME,CARD_NO,ISSUER_ID,TRANX_TYPE,TRACE_NO,REF_NO,RESPONSE_CODE,TRANSIMISSION_TIME,STATUS )");
			strQuery.append(" VALUES (");
			strQuery.append("'"+ this.getTransactionId()+"', ");
			strQuery.append(" SYSDATE, ");
			strQuery.append("'"+ this.getCardNo()+"', ");
			strQuery.append("'"+ this.getIssuerId()+"', ");
			strQuery.append("'"+ this.getTransactionType()+"', ");
			strQuery.append("'"+ this.getSTAN()+"', ");
			strQuery.append("'"+ this.getRefNo()+"', ");
			strQuery.append("'"+ this.getResponseCode()+"', ");
			strQuery.append("'"+ this.getTransmissionDate()+"', ");
			strQuery.append("'0') ");

		}catch(Exception vep) {
			System.out.println("Exception while building the transaction table query : "+vep.toString());
			throw vep;
		}
		return strQuery.toString();
	}

	public String getCardActivityInsertSql(String strCardActivity) throws Exception
	{
		StringBuffer strQuery = new StringBuffer();
		try
		{
			strQuery.append("INSERT INTO CARD_ACTIVITY ( ");
			strQuery.append(" ACTIVITY_ID,ISSUER_ID,CARD_NO,ACTIVITY,UPDATED_BY,UPDATED_DATE)");
			strQuery.append(" VALUES (");
			strQuery.append("CARDACTIVITY_SEQ.NEXTVAL,");
			strQuery.append("'"+this.getIssuerId() +"', ");
			strQuery.append("'"+ this.getCardNo()+"', ");
			strQuery.append("'"+ strCardActivity+"', ");
			strQuery.append("'ONLINE',SYSDATE) ");
			System.out.println(strQuery.toString());
		}catch(Exception vep) {
			System.out.println("Exception while building the transaction table query : "+vep.toString());
			throw vep;
		}
		return strQuery.toString();
	}


	public String updateActivation(String strCardNo)
	{

		StringBuffer query = new StringBuffer();
		query.append("UPDATE CARD SET ");
		query.append("CARD_ACTIVE ='Y',PIN_CHANGE_FLAG='N' ");
		query.append("WHERE CARD_NO = '"+strCardNo+"' ");

		return query.toString();

	}



	//Updating CARD_CASH_PURSE table

	public String updateCashPurse()
	{
		StringBuffer query = new StringBuffer();
		query.append("UPDATE CARD_CASH_PURSE SET ");
		query.append("BALANCE ='"+this.getCashBalance()+"', ");
		query.append("TOTAL_SPEND ='"+this.getSpendAmount()+"', ");
		query.append("TOTAL_AUTH ='"+this.getAuthAmount()+"', ");
		query.append("TOTAL_RELOAD ='"+this.getReloadAmount()+"', ");
		// query.append("LEFT_SALE_AMOUNT ="+this.getLeftSaleAmount()+"', ");
		// query.append("LAST_SPEND_DATE = SYSDATE, ");
		System.out.println("this.getTransactionType()"+this.getTransactionType());
		if(this.getTransactionType().equals("TOPUP"))
		{
			query.append("LAST_RELOAD_DATE = SYSDATE, ");
		}
		else
		{
			query.append("LAST_SPEND_DATE = SYSDATE, ");
		}

		query.append("FLAG = '1', ");
		query.append("LAST_UPDATED_DATE  =SYSDATE, LAST_UPDATED_BY  ='"+Config.LAST_UPDATED_BY+"' ");
		query.append("WHERE ISSUER_ID = '"+issuerId+"' ");
		query.append("AND CARD_NO = '"+this.getCardNo()+"' ");

		// System.out.println(query.toString());

		return query.toString();
	}

	public String updateTransaction(String strTranxId, String status)
	{

		StringBuffer query = new StringBuffer();
		query.append("UPDATE TRANSACTION SET ");
		query.append("TRANSACTION_STATUS ='"+status+"' ");
		query.append("WHERE TRANSACTION_ID = '"+strTranxId+"' ");
		//query.append("AND PROCESSING_CODE = '"+this.getProcessingCode()+"' ");
		// query.append("AND TRANSACTION_STATUS = '"+Config.ORIGINAL+"' ");
		// query.append("AND ISSUER_ID='"+this.getIssuerId()+"' ");
		// System.out.println(query.toString());
		return query.toString();

	}

	public TransactionDataBean recordExists(String strTranxType,HashMap conditionMap)throws Exception
	{
		TransactionDataBean objTransactionDataBean = new TransactionDataBean();
		try
		{
			StringBuffer query = new StringBuffer();
			query.append("SELECT TRANSACTION_ID,CARD_NO,TRANSACTION_AMT,SETTLED_AMT,TRANSACTION_TYPE,F90 FROM TRANSACTION WHERE 1=1");
			if(strTranxType.equals("AUTHCOMPLETE"))
			{
				query.append(" AND TRANSACTION_TYPE='PREAUTH' AND CARD_NO='"+(String)conditionMap.get("CARDNO")+"' ");
				query.append(" AND TRANSACTION_STATUS='"+Config.ORIGINAL+"' AND APPROVAL_CODE='"+ (String)conditionMap.get("APPROVALCODE")+"'");
			}

			else if(strTranxType.equals("VOID") || strTranxType.equals("REVERSAL"))
			{
				query.append(" AND MERCHANT_NO='"+(String)conditionMap.get("MERCHANTID")+"' AND TERMINAL_NO='"+ (String)conditionMap.get("TERMINALID")+"'");
				query.append(" AND CARD_NO='"+(String)conditionMap.get("CARDNO")+"' AND STAN='"+ (String)conditionMap.get("STAN")+"'");
				query.append(" AND TRANSMISSION_DATE='"+(String)conditionMap.get("TRANSMISSIONDATE")+"'");
				if(strTranxType.equals("VOID"))
				{

					query.append(" AND TRANSACTION_STATUS='"+Config.ORIGINAL+"'");
				}
			}

			else if(strTranxType.equals("VOIDREVERSAL"))
			{

				query.append(" AND CARD_NO='"+(String)conditionMap.get("CARDNO")+"' AND STAN='"+ (String)conditionMap.get("STAN")+"'");
				query.append(" AND TRANSMISSION_DATE='"+(String)conditionMap.get("TRANSMISSIONDATE")+"'");
			}

			System.out.println(query.toString());

			objDBManager.executeSQL(query.toString());
			TPlusResultSet rs = objDBManager.getResultSet();
			if(rs.next())
			{
				objTransactionDataBean.setRecordExist(true);
				objTransactionDataBean.setTransactionId(rs.getString("TRANSACTION_ID"));
				objTransactionDataBean.setCardNo(rs.getString("CARD_NO"));
				objTransactionDataBean.setTranxSettledAmt(rs.getString("SETTLED_AMT"));
				objTransactionDataBean.setTransactionAmt(rs.getString("TRANSACTION_AMT"));
				objTransactionDataBean.setTransactionType(rs.getString("TRANSACTION_TYPE"));
				objTransactionDataBean.setF90(rs.getString("F90"));

			}

		}catch(Exception vep)
		{
			System.out.println("Exception while getting Total Txn for given card : "+vep.toString());
			throw vep;
		}

		return objTransactionDataBean;

	}


	public String getTotalVisits() throws Exception{
		String totalVisits = "0";
		try{
			StringBuffer query = new StringBuffer();
			query.append("SELECT LPAD(TXN_COUNT, 3 ,0) AS TXN_COUNT FROM TOTAL_CARD_TXN_COUNT ");
			query.append("WHERE CARD_NO='"+this.getCardNo()+"' ");
			System.out.println(query.toString());
			objDBManager.executeSQL(query.toString());
			TPlusResultSet rs = objDBManager.getResultSet();
			if(rs.next()) {
				totalVisits = rs.getString("TXN_COUNT");
			}
		}catch(Exception vep) {
			System.out.println("Exception while getting Total Txn for given card : "+vep.toString());
			throw vep;
		}
		return totalVisits;
	}

	public String getCurrentMonthVisits() throws Exception{
		String totalVisits = "0";
		try{
			StringBuffer query = new StringBuffer();
			query.append("SELECT LPAD(TXN_COUNT, 3 ,0) AS TXN_COUNT FROM MONTH_CARD_TXN_COUNT ");
			query.append("WHERE CARD_NO='"+this.getCardNo()+"' ");
			query.append("AND TO_CHAR(SYSDATE,'MMYYYY') = MONTH ");
			System.out.println(query.toString());
			objDBManager.executeSQL(query.toString());
			TPlusResultSet rs = objDBManager.getResultSet();
			if(rs.next()) {
				totalVisits = rs.getString("TXN_COUNT");
			}
		}catch(Exception vep) {
			System.out.println("Exception while getting Total current month Txn for given card : "+vep.toString());
			throw vep;
		}
		return totalVisits;
	}

	/**
	 * Getter for property issuerId.
	 * @return Value of property issuerId.
	 */
	public java.lang.String getIssuerId() {
		return issuerId;
	}

	/**
	 * Setter for property issuerId.
	 * @param issuerId New value of property issuerId.
	 */
	public void setIssuerId(java.lang.String issuerId) {
		this.issuerId = issuerId;
	}

	/**
	 * Getter for property terminalNo.
	 * @return Value of property terminalNo.
	 */
	public java.lang.String getTerminalNo() {
		return terminalNo;
	}

	/**
	 * Setter for property terminalNo.
	 * @param terminalNo New value of property terminalNo.
	 */
	public void setTerminalNo(java.lang.String terminalNo) {
		this.terminalNo = terminalNo;
	}

	/**
	 * Getter for property merchantNo.
	 * @return Value of property merchantNo.
	 */
	public java.lang.String getMerchantNo() {
		return merchantNo;
	}

	/**
	 * Setter for property merchantNo.
	 * @param merchantNo New value of property merchantNo.
	 */
	public void setMerchantNo(java.lang.String merchantNo) {
		this.merchantNo = merchantNo;
	}

	/**
	 * Getter for property cardNo.
	 * @return Value of property cardNo.
	 */
	public java.lang.String getCardNo() {
		return cardNo;
	}

	/**
	 * Setter for property cardNo.
	 * @param cardNo New value of property cardNo.
	 */
	public void setCardNo(java.lang.String cardNo) {
		this.cardNo = cardNo;
	}

	/**
	 * Getter for property STAN.
	 * @return Value of property STAN.
	 */
	public java.lang.String getSTAN() {
		return STAN;
	}

	/**
	 * Setter for property STAN.
	 * @param STAN New value of property STAN.
	 */
	public void setSTAN(java.lang.String STAN) {
		this.STAN = STAN;
	}

	/**
	 * Getter for property bachNo.
	 * @return Value of property bachNo.
	 */
	public java.lang.String getBachNo() {
		return bachNo;
	}

	/**
	 * Setter for property bachNo.
	 * @param bachNo New value of property bachNo.
	 */
	public void setBachNo(java.lang.String bachNo) {
		this.bachNo = bachNo;
	}

	/**
	 * Getter for property processingCode.
	 * @return Value of property processingCode.
	 */
	public java.lang.String getProcessingCode() {
		return processingCode;
	}

	/**
	 * Setter for property processingCode.
	 * @param processingCode New value of property processingCode.
	 */
	public void setProcessingCode(java.lang.String processingCode) {
		this.processingCode = processingCode;
	}

	/**
	 * Getter for property RRN.
	 * @return Value of property RRN.
	 */
	public java.lang.String getRRN() {
		return RRN;
	}

	/**
	 * Setter for property RRN.
	 * @param RRN New value of property RRN.
	 */
	public void setRRN(java.lang.String RRN) {
		this.RRN = RRN;
	}

	/**
	 * Getter for property cardPresentFlag.
	 * @return Value of property cardPresentFlag.
	 */
	public java.lang.String getCardPresentFlag() {
		return cardPresentFlag;
	}

	/**
	 * Setter for property cardPresentFlag.
	 * @param cardPresentFlag New value of property cardPresentFlag.
	 */
	public void setCardPresentFlag(java.lang.String cardPresentFlag) {
		this.cardPresentFlag = cardPresentFlag;
	}

	/**
	 * Getter for property transactionType.
	 * @return Value of property transactionType.
	 */
	public java.lang.String getTransactionType() {
		return transactionType;
	}

	/**
	 * Setter for property transactionType.
	 * @param transactionType New value of property transactionType.
	 */
	public void setTransactionType(java.lang.String transactionType) {
		this.transactionType = transactionType;
	}

	/**
	 * Getter for property transactionId.
	 * @return Value of property transactionId.
	 */
	public java.lang.String getTransactionId() {
		return transactionId;
	}

	/**
	 * Setter for property transactionId.
	 * @param transactionId New value of property transactionId.
	 */
	public void setTransactionId(java.lang.String transactionId) {
		this.transactionId = transactionId;
	}

	/**
	 * Getter for property transactionAmt.
	 * @return Value of property transactionAmt.
	 */
	public java.lang.String getTransactionAmt() {
		return transactionAmt;
	}

	/**
	 * Setter for property transactionAmt.
	 * @param transactionAmt New value of property transactionAmt.
	 */
	public void setTransactionAmt(java.lang.String transactionAmt) {
		this.transactionAmt = transactionAmt;
	}

	/**
	 * Getter for property transactionDate.
	 * @return Value of property transactionDate.
	 */
	public java.lang.String getTransactionDate() {
		return transactionDate;
	}

	/**
	 * Setter for property transactionDate.
	 * @param transactionDate New value of property transactionDate.
	 */
	public void setTransactionDate(java.lang.String transactionDate) {
		this.transactionDate = transactionDate;
	}

	/**
	 * Getter for property settlementDate.
	 * @return Value of property settlementDate.
	 */
	public java.lang.String getSettlementDate() {
		return settlementDate;
	}

	/**
	 * Setter for property settlementDate.
	 * @param settlementDate New value of property settlementDate.
	 */
	public void setSettlementDate(java.lang.String settlementDate) {
		this.settlementDate = settlementDate;
	}

	/**
	 * Getter for property batchStatus.
	 * @return Value of property batchStatus.
	 */
	public java.lang.String getBatchStatus() {
		return batchStatus;
	}

	/**
	 * Setter for property batchStatus.
	 * @param batchStatus New value of property batchStatus.
	 */
	public void setBatchStatus(java.lang.String batchStatus) {
		this.batchStatus = batchStatus;
	}

	/**
	 * Getter for property pointSchemeId.
	 * @return Value of property pointSchemeId.
	 */
	public java.lang.String getPointSchemeId() {
		return pointSchemeId;
	}

	/**
	 * Setter for property pointSchemeId.
	 * @param pointSchemeId New value of property pointSchemeId.
	 */
	public void setPointSchemeId(java.lang.String pointSchemeId) {
		this.pointSchemeId = pointSchemeId;
	}

	/**
	 * Getter for property pointEarn.
	 * @return Value of property pointEarn.
	 */
	public java.lang.String getPointEarn() {
		return pointEarn;
	}

	/**
	 * Setter for property pointEarn.
	 * @param pointEarn New value of property pointEarn.
	 */
	public void setPointEarn(java.lang.String pointEarn) {
		this.pointEarn = pointEarn;
	}

	/**
	 * Getter for property pointRedeem.
	 * @return Value of property pointRedeem.
	 */
	public java.lang.String getPointRedeem() {
		return pointRedeem;
	}

	/**
	 * Setter for property pointRedeem.
	 * @param pointRedeem New value of property pointRedeem.
	 */
	public void setPointRedeem(java.lang.String pointRedeem) {
		this.pointRedeem = pointRedeem;
	}

	/**
	 * Getter for property pointBalance.
	 * @return Value of property pointBalance.
	 */
	public java.lang.String getPointBalance() {
		return pointBalance;
	}

	/**
	 * Setter for property pointBalance.
	 * @param pointBalance New value of property pointBalance.
	 */
	public void setPointBalance(java.lang.String pointBalance) {
		this.pointBalance = pointBalance;
	}

	/**
	 * Getter for property cashReload.
	 * @return Value of property cashReload.
	 */
	public java.lang.String getCashReload() {
		return cashReload;
	}

	/**
	 * Setter for property cashReload.
	 * @param cashReload New value of property cashReload.
	 */
	public void setCashReload(java.lang.String cashReload) {
		this.cashReload = cashReload;
	}

	/**
	 * Getter for property cashSpend.
	 * @return Value of property cashSpend.
	 */
	public java.lang.String getCashSpend() {
		return cashSpend;
	}

	/**
	 * Setter for property cashSpend.
	 * @param cashSpend New value of property cashSpend.
	 */
	public void setCashSpend(java.lang.String cashSpend) {
		this.cashSpend = cashSpend;
	}

	/**
	 * Getter for property cashBalance.
	 * @return Value of property cashBalance.
	 */
	public java.lang.String getCashBalance() {
		return cashBalance;
	}

	/**
	 * Setter for property cashBalance.
	 * @param cashBalance New value of property cashBalance.
	 */
	public void setCashBalance(java.lang.String cashBalance) {
		this.cashBalance = cashBalance;
	}

	/**
	 * Getter for property cashBalance.
	 * @return Value of property cashBalance.
	 */
	public java.lang.String getAuthAmount() {
		return authAmount;
	}


	/**
	 * Setter for property cashBalance.
	 * @param cashBalance New value of property cashBalance.
	 */
	public void setAuthAmount(java.lang.String authAmount) {
		this.authAmount = authAmount;
	}


	/**
	 * Getter for property cashBalance.
	 * @return Value of property cashBalance.
	 */
	public java.lang.String getSpendAmount() {
		return spendAmount;
	}


	/**
	 * Setter for property cashBalance.
	 * @param cashBalance New value of property cashBalance.
	 */
	public void setSpendAmount(java.lang.String spendAmount) {
		this.spendAmount = spendAmount;
	}


	/**
	 * Setter for property cashBalance.
	 * @param cashBalance New value of property cashBalance.
	 */
	public void setReloadAmount(java.lang.String reloadAmount) {
		this.reloadAmount = reloadAmount;
	}

	/**
	 * Getter for property cashBalance.
	 * @return Value of property cashBalance.
	 */
	public java.lang.String getReloadAmount() {
		return reloadAmount;
	}




	/**
	 * Getter for property couponSchemeId.
	 * @return Value of property couponSchemeId.
	 */
	public java.lang.String getCouponSchemeId() {
		return couponSchemeId;
	}

	/**
	 * Setter for property couponSchemeId.
	 * @param couponSchemeId New value of property couponSchemeId.
	 */
	public void setCouponSchemeId(java.lang.String couponSchemeId) {
		this.couponSchemeId = couponSchemeId;
	}

	/**
	 * Getter for property couponId.
	 * @return Value of property couponId.
	 */
	public java.lang.String getCouponId() {
		return couponId;
	}

	/**
	 * Setter for property couponId.
	 * @param couponId New value of property couponId.
	 */
	public void setCouponId(java.lang.String couponId) {
		this.couponId = couponId;
	}

	/**
	 * Getter for property couponSerialNo.
	 * @return Value of property couponSerialNo.
	 */
	public java.lang.String getCouponSerialNo() {
		return couponSerialNo;
	}

	/**
	 * Setter for property couponSerialNo.
	 * @param couponSerialNo New value of property couponSerialNo.
	 */
	public void setCouponSerialNo(java.lang.String couponSerialNo) {
		this.couponSerialNo = couponSerialNo;
	}

	/**
	 * Getter for property couponRedeemAmt.
	 * @return Value of property couponRedeemAmt.
	 */
	public java.lang.String getCouponRedeemAmt() {
		return couponRedeemAmt;
	}

	/**
	 * Setter for property couponRedeemAmt.
	 * @param couponRedeemAmt New value of property couponRedeemAmt.
	 */
	public void setCouponRedeemAmt(java.lang.String couponRedeemAmt) {
		this.couponRedeemAmt = couponRedeemAmt;
	}

	/**
	 * Getter for property transRecordType.
	 * @return Value of property transRecordType.
	 */
	public java.lang.String getTransRecordType() {
		return transRecordType;
	}

	/**
	 * Setter for property transRecordType.
	 * @param transRecordType New value of property transRecordType.
	 */
	public void setTransRecordType(java.lang.String transRecordType) {
		this.transRecordType = transRecordType;
	}

	/**
	 * Getter for property rebateEarn.
	 * @return Value of property rebateEarn.
	 */
	public java.lang.String getRebateEarn() {
		return rebateEarn;
	}

	/**
	 * Setter for property rebateEarn.
	 * @param rebateEarn New value of property rebateEarn.
	 */
	public void setRebateEarn(java.lang.String rebateEarn) {
		this.rebateEarn = rebateEarn;
	}

	/**
	 * Getter for property rebateSpend.
	 * @return Value of property rebateSpend.
	 */
	public java.lang.String getRebateSpend() {
		return rebateSpend;
	}

	/**
	 * Setter for property rebateSpend.
	 * @param rebateSpend New value of property rebateSpend.
	 */
	public void setRebateSpend(java.lang.String rebateSpend) {
		this.rebateSpend = rebateSpend;
	}

	/**
	 * Getter for property rebateBalance.
	 * @return Value of property rebateBalance.
	 */
	public java.lang.String getRebateBalance() {
		return rebateBalance;
	}

	/**
	 * Setter for property rebateBalance.
	 * @param rebateBalance New value of property rebateBalance.
	 */
	public void setRebateBalance(java.lang.String rebateBalance) {
		this.rebateBalance = rebateBalance;
	}

	/**
	 * Getter for property recordExist.
	 * @return Value of property recordExist.
	 */
	public boolean isRecordExist() {
		return recordExist;
	}

	/**
	 * Setter for property recordExist.
	 * @param recordExist New value of property recordExist.
	 */
	public void setRecordExist(boolean recordExist) {
		this.recordExist = recordExist;
	}

	/**
	 * Getter for property transactionStatus.
	 * @return Value of property transactionStatus.
	 */
	public java.lang.String getTransactionStatus() {
		return transactionStatus;
	}

	/**
	 * Setter for property transactionStatus.
	 * @param transactionStatus New value of property transactionStatus.
	 */
	public void setTransactionStatus(java.lang.String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	/**
	 * Getter for property invoiceNo.
	 * @return Value of property invoiceNo.
	 */
	public java.lang.String getInvoiceNo() {
		return invoiceNo;
	}

	/**
	 * Setter for property invoiceNo.
	 * @param invoiceNo New value of property invoiceNo.
	 */
	public void setInvoiceNo(java.lang.String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	/**
	 * Getter for property rebateSchemeId.
	 * @return Value of property rebateSchemeId.
	 */
	public java.lang.String getRebateSchemeId() {
		return rebateSchemeId;
	}

	/**
	 * Setter for property rebateSchemeId.
	 * @param rebateSchemeId New value of property rebateSchemeId.
	 */
	public void setRebateSchemeId(java.lang.String rebateSchemeId) {
		this.rebateSchemeId = rebateSchemeId;
	}

	/**
	 * Getter for property mrktAwardPoints.
	 * @return Value of property mrktAwardPoints.
	 */
	public java.lang.String getMrktAwardPoints() {
		return mrktAwardPoints;
	}

	/**
	 * Setter for property mrktAwardPoints.
	 * @param mrktAwardPoints New value of property mrktAwardPoints.
	 */
	public void setMrktAwardPoints(java.lang.String mrktAwardPoints) {
		this.mrktAwardPoints = mrktAwardPoints;
	}

	/**
	 * Getter for property mrktAwardCash.
	 * @return Value of property mrktAwardCash.
	 */
	public java.lang.String getMrktAwardCash() {
		return mrktAwardCash;
	}

	/**
	 * Setter for property mrktAwardCash.
	 * @param mrktAwardCash New value of property mrktAwardCash.
	 */
	public void setMrktAwardCash(java.lang.String mrktAwardCash) {
		this.mrktAwardCash = mrktAwardCash;
	}

	/**
	 * Getter for property deactivationType.
	 * @return Value of property deactivationType.
	 */
	public java.lang.String getDeactivationType() {
		return deactivationType;
	}

	/**
	 * Setter for property deactivationType.
	 * @param deactivationType New value of property deactivationType.
	 */
	public void setDeactivationType(java.lang.String deactivationType) {
		this.deactivationType = deactivationType;
	}


	public void setDestinationCardNo(String destinationCardNo)
	{
		this.destinationCardNo = destinationCardNo;
	}


	public String getDestinationCardNo()
	{
		return destinationCardNo;
	}


	public void setResponseCode(String responseCode)
	{
		this.responseCode = responseCode;
	}


	public String getResponseCode()
	{
		return responseCode;
	}


	public void setApprovalCode(String approvalCode)
	{
		this.approvalCode = approvalCode;
	}


	public String getApprovalCode()
	{
		return approvalCode;
	}


	public void setTransmissionDate(String transmissionDate)
	{
		this.transmissionDate = transmissionDate;
	}


	public String getTransmissionDate()
	{
		return transmissionDate;
	}


	public void setRefundAmount(String refundAmount)
	{
		this.refundAmount = refundAmount;
	}


	public String getRefundAmount()
	{
		return refundAmount;
	}


	public void setOfflineBatchNumber(String offlineBatchNumber)
	{
		this.offlineBatchNumber = offlineBatchNumber;
	}


	public String getOfflineBatchNumber()
	{
		return offlineBatchNumber;
	}


	public void setCardSerial(String cardSerial)
	{
		this.cardSerial = cardSerial;
	}


	public String getCardSerial()
	{
		return cardSerial;
	}


	public void setCoreRefNo(String coreRefNo)
	{
		this.coreRefNo = coreRefNo;
	}


	public String getCoreRefNo()
	{
		return coreRefNo;
	}


	public void set_settlementDate(String settlementDate)
	{
		this.settlementDate = settlementDate;
	}


	public String get_settlementDate()
	{
		return settlementDate;
	}


	public void setLeftSaleAmount(String leftSaleAmount)
	{
		this.leftSaleAmount = leftSaleAmount;
	}


	public String getLeftSaleAmount()
	{
		return leftSaleAmount;
	}


	public void setCardClassId(String cardClassId)
	{
		this.cardClassId = cardClassId;
	}


	public String getCardClassId()
	{
		return cardClassId;
	}


	public void setConvertRatio(String convertRatio)
	{
		this.convertRatio = convertRatio;
	}


	public String getConvertRatio()
	{
		return convertRatio;
	}


	public void setConvertAmount(String convertAmount)
	{
		this.convertAmount = convertAmount;
	}


	public String getConvertAmount()
	{
		return convertAmount;
	}


	public void setTotalLeftPreTopupAmount(String totalLeftPreTopupAmount)
	{
		this.totalLeftPreTopupAmount = totalLeftPreTopupAmount;
	}


	public String getTotalLeftPreTopupAmount()
	{
		return totalLeftPreTopupAmount;
	}


	public void setOldLeftSaleAmount(String oldLeftSaleAmount)
	{
		this.oldLeftSaleAmount = oldLeftSaleAmount;
	}


	public String getOldLeftSaleAmount()
	{
		return oldLeftSaleAmount;
	}


	public void setOldCardClassId(String oldCardClassId)
	{
		this.oldCardClassId = oldCardClassId;
	}


	public String getOldCardClassId()
	{
		return oldCardClassId;
	}

	public void setRefNo(String refNo)
	{
		this.refNo = refNo;
	}


	public String getRefNo()
	{
		return refNo;
	}


	public void setGLType(String glTranxType)
	{
		this.glTranxType = glTranxType;
	}


	public String getGLType()
	{
		return glTranxType;
	}



	public String getMTI() {
		return mti;
	}
	public void setMTI(String mti) {
		this.mti = mti;
	}


	public String getCUPCutOff() {
		return cupCutOff;
	}
	public void setCUPCutOff(String cupCutOff) {
		this.cupCutOff= cupCutOff;
	}

	public String getF90() {
		return F90;
	}
	public void setF90(String F90) {
		this.F90= F90;
	}

	public String getAcqBin() {
		return acqBIN;
	}
	public void setAcqBin(String acqBIN) {
		this.acqBIN = acqBIN;
	}
	public String getTranxSettledAmt() {
		return tranxSettledAmt;
	}
	public void setTranxSettledAmt(String tranxSettledAmt) {
		this.tranxSettledAmt = tranxSettledAmt;
	}
	public String getTranxCurr() {
		return tranxCurr;
	}
	public void setTranxCurr(String tranxCurr) {
		this.tranxCurr = tranxCurr;
	}
	public String getTranxSettledCurr() {
		return tranxSettledCurr;
	}
	public void setTranxSettledCurr(String tranxSettledCurr) {
		this.tranxSettledCurr = tranxSettledCurr;
	}
	public String getTranxCHAmt() {
		return tranxCHAmt;
	}
	public void setTranxCHAmt(String tranxCHAmt) {
		this.tranxCHAmt = tranxCHAmt;
	}
	public String getTranxFee() {
		return tranxFee;
	}
	public void setTranxFee(String tranxFee) {
		this.tranxFee = tranxFee;
	}
	public String getTranxDate() {
		return tranxDate;
	}
	public void setTranxDate(String tranxDate) {
		this.tranxDate = tranxDate;
	}
	public String getTranxTime() {
		return tranxTime;
	}
	public void setTranxTime(String tranxTime) {
		this.tranxTime = tranxTime;
	}
	public String getMcc() {
		return mcc;
	}
	public void setMcc(String mcc) {
		this.mcc = mcc;
	}
	public String getTrack2Data() {
		return track2Data;
	}
	public void setTrack2Data(String track2Data) {
		this.track2Data = track2Data;
	}
	public String getDeleted() {
		return deleted;
	}
	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}
	public String getSettled() {
		return settled;
	}
	public void setSettled(String settled) {
		this.settled = settled;
	}
	public String getPosEntryMode() {
		return posEntryMode;
	}
	public void setPosEntryMode(String posEntryMode) {
		this.posEntryMode = posEntryMode;
	}
	public String getIsAuthComplete() {
		return isAuthComplete;
	}
	public void setIsAuthComplete(String isAuthComplete) {
		this.isAuthComplete = isAuthComplete;
	}
	public String getRecon() {
		return recon;
	}
	public void setRecon(String recon) {
		this.recon = recon;
	}

	public String getAcqBinType() {
		return acqBinType;
	}
	public void setAcqBinType(String acqBinType) {
		this.acqBinType = acqBinType;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public void populateData(IParser objISO)throws OnlineException
	{

		try
		{
			this.setIssuerId(Config.ISSUER_ID);
			this.setMTI(objISO.getMTI());
			this.setProcessingCode(objISO.getValue(3));
			this.setTransactionType(objISO.getTranxType());
			System.out.println(this.getTransactionType()+" 22  "+objISO.getTranxType());
			this.setTransmissionDate(objISO.getValue(7));
			System.out.println("11115");
			this.setSTAN(objISO.getValue(11));
			System.out.println("11110");
			this.setMcc(objISO.getValue(18));
			this.setPosEntryMode(objISO.getValue(22));
			//this.setAcqBin(objISO.getValue(32));
			System.out.println("1111");
			this.setTrack2Data(objISO.getValue(35));
			this.setRRN(objISO.getValue(37));
			this.setApprovalCode(objISO.getValue(38));
			System.out.println("1112");
			this.setTerminalNo(objISO.getValue(41));
			this.setMerchantNo(objISO.getValue(42));
			this.setRefNo(objISO.getValue(43));
			System.out.println("1113");
			this.setCardNo(objISO.getCardNumber());
			this.setRecon("N");
			this.setCUPCutOff("N");
			this.setF90(objISO.getValue(90));
			System.out.println("1114");
			//if(objISO.getValue(32).equals("437734")){
			if(objISO.getValue(32).equals("437733")){
				this.setAcqBinType("ONUS");
				System.out.println("+++++++++++++++++++++++++++++ON US+++++++++++++++++++++++++++");
			}
			else{
				System.out.println("+++++++++++++++++++++++++++++Off US+++++++++++++++++++++++++++");
				this.setAcqBinType("OFFUS");}
			//this.setAcqBinType("OFFUS");
			System.out.println("11115");
			this.setTransactionId(generateTransactionId());
			System.out.println("TransactionId : "+ transactionId);
		}
		catch(Exception exp)
		{
			throw new OnlineException("09","A05374","Problem Populating Data...");
		}

	}


	public void checkOffusTransaction(IParser objISO) throws OnlineException
	{
		try{
			System.out.println("1");
			TransactionDB objTranxDB = new TransactionDB();
			if(objISO.hasField(6))
			{
				System.out.println("2");
				if(!objISO.hasField(10) || !objISO.hasField(51))
				{
					System.out.println("4");
					throw new OnlineException("05","A05374","F10 or F51 are missing");
				}
				else
				{
					System.out.println("3");
					double rate =objTranxDB.getCurrRate();

					System.out.println(objISO.getValue(4)+"  "+TPlusUtility.currConversion(objISO.getValue(6),8550));

					this.setTransactionAmt(double2str(objISO.getValue(4)));
					this.setTranxSettledAmt(double2str(TPlusUtility.currConversion(objISO.getValue(6),rate)));
					this.setTranxFee(objISO.getValue(28));
					this.setTranxCurr(objISO.getValue(49));
					this.setTranxSettledCurr(objISO.getValue(51));
					this.setTranxCHAmt(double2str(objISO.getValue(6)));
				}
			}
			else
			{
				System.out.println("5");
				System.out.println(objISO.getValue(4));
				if(objTranxDB.validateCurr(objISO.getValue(49)))
				{
					System.out.println("valid Currecy");
					this.setTransactionAmt(double2str(objISO.getValue(4)));
					this.setTranxSettledAmt(double2str(objISO.getValue(4)));
					this.setTranxCHAmt(double2str(objISO.getValue(4)));
					this.setTranxFee("0.0");
					this.setTranxCurr(objISO.getValue(49));
					this.setTranxSettledCurr(objISO.getValue(49));
				}
				else
				{
					System.out.println("Invalid Currecy");
					throw new OnlineException("05","A05374","Invalid Currency..");
				}
			}

		}
		catch(Exception exp)
		{
			System.out.println("6");
			throw new OnlineException("05","A05374","Problem in process the Currency Conversion..");
		}

	}

	public String double2str(String strTranxAtm)
	{
		double dlbAmt = new Double(strTranxAtm).doubleValue()/100;
		return new Double(dlbAmt).toString();

	}
}
