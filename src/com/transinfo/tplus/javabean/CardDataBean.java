package com.transinfo.tplus.javabean;

import java.io.Serializable;

import com.transinfo.tplus.util.DBManager;
import com.transinfo.tplus.util.TPlusResultSet;


public class CardDataBean implements Serializable
{
    private String issuerId = "";
    private String cardNo = "";
    private String name = "";
    private String cardType = "";
    private String cardProduct = "";
    private String nameOnCard = "";
    private String issueDate = "";
    private String expiryDate = "";
    private String openDate = "";
    private String closeDate = "";
    private String renewalCount = "";
    private String blockCode = "";
    private String status = "";
    private String blockReason = "";
    private String cardActive = "";
    private String cardValidity = "";
    private String validityPeriod = "";
    private int expiryDays = 0;
    private boolean cardExist = false;
    private boolean cardExpired = false;
    private boolean cardInActive = false;
    private boolean cardBlocked = false;
    private boolean cardActivated = false;
    private boolean isUnnamed = false;
    private boolean isNewCard = false;
    private String fixedValue = "";
    private String cashPurseType = "";
    private String allowTopup = "";
    private String cardTypeFeature = "";
    private String merchantNo = "";
    private String terminalNo = "";
    private String oldCategory = "";
    private String newCategory = "";
    private String maxTopUpAmount = "";
    private String pinChangeFlag = "";
    private boolean pinBlocked= false;
    private String pinRequired="";
    private String wrongPINCnt = "";
    private String pvv="";
    private String oldPvv="";

    /** Getter for property cardNo.
     * @return Value of property cardNo.
     */
    public String getCardNo() {
        return cardNo;
    }

    /** Setter for property cardNo.
     * @param cardNo New value of property cardNo.
     */
    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    /** Getter for property cardType.
     * @return Value of property cardType.
     */
    public String getCardType() {
        return cardType;
    }

    /** Setter for property cardType.
     * @param cardType New value of property cardType.
     */
    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    /** Getter for property nameOnCard.
     * @return Value of property nameOnCard.
     */
    public String getNameOnCard() {
        return nameOnCard;
    }

    /** Setter for property nameOnCard.
     * @param nameOnCard New value of property nameOnCard.
     */
    public void setNameOnCard(String nameOnCard) {
        this.nameOnCard = nameOnCard;
    }

    /** Getter for property issueDate.
     * @return Value of property issueDate.
     */
    public String getIssueDate() {
        return issueDate;
    }

    /** Setter for property issueDate.
     * @param issueDate New value of property issueDate.
     */
    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    /** Getter for property expiryDate.
     * @return Value of property expiryDate.
     */
    public String getExpiryDate() {
        return expiryDate;
    }

    /** Setter for property expiryDate.
     * @param expiryDate New value of property expiryDate.
     */
    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    /** Getter for property openDate.
     * @return Value of property openDate.
     */
    public String getOpenDate() {
        return openDate;
    }

    /** Setter for property openDate.
     * @param openDate New value of property openDate.
     */
    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }

    /** Getter for property closeDate.
     * @return Value of property closeDate.
     */
    public String getCloseDate() {
        return closeDate;
    }

    /** Setter for property closeDate.
     * @param closeDate New value of property closeDate.
     */
    public void setCloseDate(String closeDate) {
        this.closeDate = closeDate;
    }

    /** Getter for property renewalCount.
     * @return Value of property renewalCount.
     */
    public String getRenewalCount() {
        return renewalCount;
    }

    /** Setter for property renewalCount.
     * @param renewalCount New value of property renewalCount.
     */
    public void setRenewalCount(String renewalCount) {
        this.renewalCount = renewalCount;
    }

    /** Getter for property blockCode.
     * @return Value of property blockCode.
     */
    public String getBlockCode() {
        return blockCode;
    }

    /** Setter for property blockCode.
     * @param blockCode New value of property blockCode.
     */
    public void setBlockCode(String blockCode) {
        this.blockCode = blockCode;
    }

    /** Getter for property status.
     * @return Value of property status.
     */
    public String getStatus() {
        return status;
    }

    /** Setter for property status.
     * @param status New value of property status.
     */
    public void setStatus(String status) {
        this.status = status;
    }
    /** Getter for property name.
     * @return Value of property name.
     */
    public String getName() {
        return name;
    }

    /** Setter for property name.
     * @param name New value of property name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /** Getter for property blockReason.
     * @return Value of property blockReason.
     */
    public String getBlockReason() {
        return blockReason;
    }

    /** Setter for property blockReason.
     * @param blockReason New value of property blockReason.
     */
    public void setBlockReason(String blockReason) {
        this.blockReason = blockReason;
    }

    /**
     * Getter for property cardActive.
     * @return Value of property cardActive.
     */
    public java.lang.String getCardActive() {
        return cardActive;
    }

    /**
     * Setter for property cardActive.
     * @param cardActive New value of property cardActive.
     */
    public void setCardActive(java.lang.String cardActive) {
        this.cardActive = cardActive;
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

    public java.lang.String getPinChangeFlag(){
		return pinChangeFlag;
	}

	public void setPinChangeFlag(String pinChangeFlag)
	{
		this.pinChangeFlag = pinChangeFlag;

	}

    public java.lang.String getPinRequired(){
		return pinRequired;
	}

	public void setPinRequired(String pinRequired)
	{
		this.pinRequired = pinRequired;

	}

    public java.lang.String getWrongPINCnt(){
		return wrongPINCnt;
	}

	public void setWrongPINCnt(String wrongPINCnt)
	{
		this.wrongPINCnt = wrongPINCnt;

	}

    public java.lang.String getPVV(){
		return pvv;
	}

	public void setPVV(String pvv)
	{
		this.pvv = pvv;

	}
  public java.lang.String getOldPVV(){
		return pvv;
	}

	public void setOldPVV(String oldPvv)
	{
		this.oldPvv = oldPvv;

	}


    /**
     * Getter for property expiryDays.
     * @return Value of property expiryDays.
     */
    public int getExpiryDays() {
        return expiryDays;
    }

    /**
     * Setter for property expiryDays.
     * @param expiryDays New value of property expiryDays.
     */
    public void setExpiryDays(int expiryDays) {
        this.expiryDays = expiryDays;
    }

    public DBManager objDBManager = null;

    public CardDataBean() {
        objDBManager = new DBManager();
    }

    /** Checking Merchant Card Details */
    public void checkMerchantCard() throws Exception {
        System.out.println("Validating the card record  & getting the values.");
        try{
            StringBuffer strSql = new StringBuffer();
            strSql.append("SELECT C.ISSUER_ID, C.CARD_NO, C.CARD_TYPE, C.NAME_ON_CARD, ");
            strSql.append("TO_CHAR(C.ISSUE_DATE,'DD/MM/YYYY')ISSUE_DATE, TO_CHAR(C.EXPIRY_DATE,'DDMMYYYY')EXPIRY_DATE, ");
            strSql.append("TO_CHAR(C.OPEN_DATE,'DD/MM/YYYY')OPEN_DATE, TO_CHAR(C.CLOSE_DATE,'DD/MM/YYYY')CLOSE_DATE, ");
            strSql.append("C.RENEWAL_COUNT, C.BLOCK_CODE, C.BLOCK_REASON, C.STATUS, C.CARD_ACTIVE, C.VALIDITY_PERIOD, C.CARD_VALIDITY, ");
            strSql.append("NVL(TRUNC(C.EXPIRY_DATE - SYSDATE), 0) AS EXPIRY_DAYS, C.CASH_FIXED_VALUE, C.CASH_PURSE_TYPE,");
            strSql.append("C.CASH_TOPUP, CT.CARD_TYPE_FEATURE,C.WRONG_PIN_COUNT,C.PIN_CHANGE_FLAG,C.PIN_REQUIRED,PVV,OLD_PVV ");
            strSql.append("FROM CARD C, CARD_TYPE CT, MERCHANT ME, CARDHOLDER_APPLICATION CHA, CARD_APPLICATION CA ");
            strSql.append("WHERE C.CARD_NO='"+this.getCardNo()+"' ");
            strSql.append("AND C.ISSUER_ID = CT.ISSUER_ID ");
            strSql.append("AND C.CARD_TYPE = CT.CARD_TYPE ");
            strSql.append("AND C.ISSUER_ID = '" + this.getIssuerId() + "' ");
            strSql.append("AND C.ISSUER_ID = CHA.ISSUER_ID ");
            strSql.append("AND C.CARD_NO = CHA.CARD_NO ");
            strSql.append("AND CHA.APPLICATION_NO = CA.APPLICATION_NO ");
            strSql.append("AND CHA.ISSUER_ID = CA.ISSUER_ID ");
            strSql.append("AND ME.MERCHANT_NO = CA.MERCHANT_NO ");
            strSql.append("AND ME.ISSUER_ID = CA.ISSUER_ID ");
            strSql.append("AND CA.MERCHANT_NO = '" + this.getMerchantNo() +"' ");

            System.out.println(strSql.toString());//HIEP
            objDBManager.executeSQL(strSql.toString());
            TPlusResultSet rs = objDBManager.getResultSet();
            if(rs.next()){
                this.setIssuerId(rs.getString("ISSUER_ID"));
                this.setCardNo(rs.getString("CARD_NO"));
                this.setCardType(rs.getString("CARD_TYPE"));
                this.setNameOnCard(rs.getString("NAME_ON_CARD"));
                this.setIssueDate(rs.getString("ISSUE_DATE"));
                this.setExpiryDate(rs.getString("EXPIRY_DATE"));
                this.setOpenDate(rs.getString("OPEN_DATE"));
                this.setCloseDate(rs.getString("CLOSE_DATE"));
                this.setRenewalCount(rs.getString("RENEWAL_COUNT"));
                this.setBlockCode(rs.getString("BLOCK_CODE"));
                this.setBlockReason(rs.getString("BLOCK_REASON"));
                this.setStatus(rs.getString("STATUS"));
                this.setCardActive(rs.getString("CARD_ACTIVE"));
                this.setExpiryDays(rs.getInt("EXPIRY_DAYS"));
                this.setValidityPeriod(rs.getString("VALIDITY_PERIOD"));
                this.setCardValidity(rs.getString("CARD_VALIDITY"));
                this.setFixedValue(rs.getString("CASH_FIXED_VALUE"));
                this.setCashPurseType(rs.getString("CASH_PURSE_TYPE"));
                this.setAllowTopup(rs.getString("CASH_TOPUP"));
                this.setCardTypeFeature(rs.getString("CARD_TYPE_FEATURE"));
                this.setPinRequired(rs.getString("PIN_REQUIRED"));
                this.setWrongPINCnt(rs.getString("WRONG_PIN_COUNT"));
                this.setPVV(rs.getString("PVV"));
                this.setOldPVV(rs.getString("OLD_PVV"));
                setCardExist(true);
                if(!this.getStatus().trim().equalsIgnoreCase("G")) this.setCardInActive(true);
                if(!this.getBlockCode().trim().equals("")) this.setCardBlocked(true);
                if(this.getCardActive().trim().equalsIgnoreCase("Y")){
                    this.setCardActivated(true);
                    if(this.getExpiryDays() < 0) this.setCardExpired(true);
                }
                if(this.getPinChangeFlag().equals("Y"))
                {
					this.setPINBlocked(true);
				}

                System.out.println("Merchant Card Exist   : "+this.isCardExist());
                System.out.println("Merchant Card Status  : "+this.isCardInActive());
                System.out.println("Merchant Card Expiry  : "+this.isCardExpired());
                System.out.println("Merchant Card Blocked : "+this.isCardBlocked());
                System.out.println("Merchant Card Activated : "+this.isCardActivated());
                System.out.println("Merchant Card Exist:"+this.isCardExist()+"/Merchant Card Status:"+this.isCardInActive()+"/Merchant Card Expiry:"+this.isCardExpired()+"/Merchant Card Blocked:"+this.isCardBlocked()+"/Merchant Card Activated : "+this.isCardActivated());
            }
        }catch(Exception vep) {
            System.out.println("Exception while getting merchant card information : "+vep.toString());
            throw vep;
        }
    }

    /** Checking Card Details */
    public void execute() throws Exception
    {
        System.out.println("Validating the card record  & getting the values.");
        try{
            StringBuffer strSql = new StringBuffer();
            strSql.append("SELECT CA.CONTACTLESS_CARD, CA.ISSUER_ID, CA.CARD_NO, CA.CARD_TYPE, CA.CARD_PRODUCT_ID, NAME_ON_CARD, ");
            strSql.append("TO_CHAR(CA.ISSUE_DATE,'DD/MM/YYYY')ISSUE_DATE, TO_CHAR(CA.EXPIRY_DATE,'DDMMYYYY')EXPIRY_DATE, ");
            strSql.append("TO_CHAR(CA.OPEN_DATE,'DD/MM/YYYY')OPEN_DATE, TO_CHAR(CA.CLOSE_DATE,'DD/MM/YYYY')CLOSE_DATE, ");
            strSql.append("RENEWAL_COUNT, BLOCK_CODE, BLOCK_REASON, STATUS,CARD_ACTIVE, CA.VALIDITY_PERIOD, CA.CARD_VALIDITY,CA.PIN_CHANGE_FLAG, ");
            strSql.append("NVL(TRUNC(EXPIRY_DATE - SYSDATE), 0) AS EXPIRY_DAYS, CASH_FIXED_VALUE, CASH_PURSE_TYPE, CASH_TOPUP, CT.CARD_TYPE_FEATURE, ");
            strSql.append(" CA.OLD_CATEGORY, CA.NEW_CATEGORY, CP.MAX_TOPUP_AMOUNT ,CA.WRONG_PIN_COUNT,CA.PIN_CHANGE_FLAG,CA.PIN_REQUIRED ");
            strSql.append("FROM CARD CA, CARD_TYPE CT, CARD_PRODUCT CP ");
            strSql.append("WHERE CARD_NO='"+this.getCardNo()+"' ");
            strSql.append("AND CA.ISSUER_ID = CT.ISSUER_ID ");
            strSql.append("AND CA.CARD_TYPE = CT.CARD_TYPE ");
            strSql.append("AND CA.ISSUER_ID = CP.ISSUER_ID ");
            strSql.append("AND CA.CARD_PRODUCT_ID = CP.CARD_PRODUCT_ID ");
            strSql.append("AND CA.ISSUER_ID = '" + this.getIssuerId() + "' ");

            System.out.println(strSql.toString());//HIEP
            objDBManager.executeSQL(strSql.toString());
            TPlusResultSet rs = objDBManager.getResultSet();
            if(rs.next())
            {
                this.setIssuerId(rs.getString("ISSUER_ID"));
                this.setCardNo(rs.getString("CARD_NO"));
                this.setCardProduct(rs.getString("CARD_PRODUCT_ID"));
                this.setCardType(rs.getString("CARD_TYPE"));
                this.setNameOnCard(rs.getString("NAME_ON_CARD"));
                this.setIssueDate(rs.getString("ISSUE_DATE"));
                this.setExpiryDate(rs.getString("EXPIRY_DATE"));
                this.setOpenDate(rs.getString("OPEN_DATE"));
                this.setCloseDate(rs.getString("CLOSE_DATE"));
                this.setRenewalCount(rs.getString("RENEWAL_COUNT"));
                this.setBlockCode(rs.getString("BLOCK_CODE"));
                this.setBlockReason(rs.getString("BLOCK_REASON"));
                this.setStatus(rs.getString("STATUS"));
                this.setCardActive(rs.getString("CARD_ACTIVE"));
                this.setExpiryDays(rs.getInt("EXPIRY_DAYS"));
                this.setValidityPeriod(rs.getString("VALIDITY_PERIOD"));
                this.setCardValidity(rs.getString("CARD_VALIDITY"));
                this.setFixedValue(rs.getString("CASH_FIXED_VALUE"));
                this.setCashPurseType(rs.getString("CASH_PURSE_TYPE"));
                this.setAllowTopup(rs.getString("CASH_TOPUP"));
                this.setCardTypeFeature(rs.getString("CARD_TYPE_FEATURE"));
                this.setOldCategory(rs.getString("OLD_CATEGORY"));
                this.setNewCategory(rs.getString("NEW_CATEGORY"));
                this.setMaxTopUpAmount(rs.getString("MAX_TOPUP_AMOUNT"));
                this.setPinChangeFlag(rs.getString("PIN_CHANGE_FLAG"));
                this.setPinRequired(rs.getString("PIN_REQUIRED"));
                this.setWrongPINCnt(rs.getString("WRONG_PIN_COUNT"));

                setCardExist(true);

                if(this.getNewCategory().trim().equalsIgnoreCase("Unnamed")) this.setIsUnnamed(true);
                if(!this.getStatus().trim().equalsIgnoreCase("G")) this.setCardInActive(true);
                if(!this.getBlockCode().trim().equals("")) this.setCardBlocked(true);
                if(this.getCardActive().trim().equalsIgnoreCase("Y"))
                {
                    this.setCardActivated(true);
                    if(this.getExpiryDays() < 0) this.setCardExpired(true);
                }

				if(this.getPinChangeFlag().equals("Y"))
                {
					this.setPINBlocked(true);
				}

                if(rs.getString("CONTACTLESS_CARD") != null && rs.getString("CONTACTLESS_CARD").equalsIgnoreCase("Y")){
                    this.isNewCard =  true;
                }

                System.out.println("Card Exist   : "+this.isCardExist());
                System.out.println("Card Status  : "+this.isCardInActive());
                System.out.println("Card Expiry  : "+this.isCardExpired());
                System.out.println("Card Blocked : "+this.isCardBlocked());
                System.out.println("Card Activated : "+this.isCardActivated());
                System.out.println( "Card Exist:"+this.isCardExist()+"/Card Status:"+this.isCardInActive()+"/Card Expiry:"+this.isCardExpired()+"/Card Blocked:"+this.isCardBlocked()+"/Card Activated : "+this.isCardActivated());
            }
        }catch(Exception vep) {
            System.out.println("Exception while getting card information : "+vep.toString());
            throw vep;
        }
    }


    /** Checking Card Contactless Details */
    public void getFreeContactlessCard() throws Exception {
        System.out.println("Validating the card record  & getting free contactless card.");
        try{
            StringBuffer strSql = new StringBuffer();
            strSql.append("SELECT CA.ISSUER_ID, CA.CARD_NO, CA.CARD_TYPE, NAME_ON_CARD, ");
            strSql.append("TO_CHAR(ISSUE_DATE,'DD/MM/YYYY')ISSUE_DATE, TO_CHAR(EXPIRY_DATE,'DDMMYYYY')EXPIRY_DATE, ");
            strSql.append("TO_CHAR(OPEN_DATE,'DD/MM/YYYY')OPEN_DATE, TO_CHAR(CLOSE_DATE,'DD/MM/YYYY')CLOSE_DATE, ");
            strSql.append("RENEWAL_COUNT, BLOCK_CODE, BLOCK_REASON, STATUS,CARD_ACTIVE, CA.VALIDITY_PERIOD, CA.CARD_VALIDITY, ");
            strSql.append("NVL(TRUNC(EXPIRY_DATE - SYSDATE), 0) AS EXPIRY_DAYS, CASH_FIXED_VALUE, CASH_PURSE_TYPE, CASH_TOPUP, CT.CARD_TYPE_FEATURE, CA.CONTACTLESS_CARD ");
            strSql.append("FROM CARD CA, CARD_CARDHOLDER CCH, CARD_TYPE CT ");
            strSql.append("WHERE CA.ISSUER_ID = CT.ISSUER_ID ");
            strSql.append("AND CA.CARD_TYPE = CT.CARD_TYPE ");
            strSql.append("AND CA.CARD_NO = CCH.CARD_NO ");
            strSql.append("AND CA.ISSUER_ID = CCH.ISSUER_ID ");
            strSql.append("AND (CCH.CARDHOLDER_SEQ = '' OR CCH.CARDHOLDER_SEQ IS NULL) ");
            strSql.append("AND CA.CONTACTLESS_CARD = 'Y' ");
            strSql.append("AND CA.CARD_ACTIVE = 'N' ");
            strSql.append("AND CA.ISSUER_ID ='"+this.getIssuerId()+"' ");
            strSql.append("AND CA.CARD_TYPE ='"+this.getCardType()+"' ");
            strSql.append("AND ROWNUM <=1 ");

            System.out.println(strSql.toString());//HIEP
            objDBManager.executeSQL(strSql.toString());
            TPlusResultSet rs = objDBManager.getResultSet();
            if(rs.next()){
                this.setIssuerId(rs.getString("ISSUER_ID"));
                this.setCardNo(rs.getString("CARD_NO"));
                this.setCardType(rs.getString("CARD_TYPE"));
                this.setNameOnCard(rs.getString("NAME_ON_CARD"));
                this.setIssueDate(rs.getString("ISSUE_DATE"));
                this.setExpiryDate(rs.getString("EXPIRY_DATE"));
                this.setOpenDate(rs.getString("OPEN_DATE"));
                this.setCloseDate(rs.getString("CLOSE_DATE"));
                this.setRenewalCount(rs.getString("RENEWAL_COUNT"));
                this.setBlockCode(rs.getString("BLOCK_CODE"));
                this.setBlockReason(rs.getString("BLOCK_REASON"));
                this.setStatus(rs.getString("STATUS"));
                this.setCardActive(rs.getString("CARD_ACTIVE"));
                this.setExpiryDays(rs.getInt("EXPIRY_DAYS"));
                this.setValidityPeriod(rs.getString("VALIDITY_PERIOD"));
                this.setCardValidity(rs.getString("CARD_VALIDITY"));
                this.setFixedValue(rs.getString("CASH_FIXED_VALUE"));
                this.setCashPurseType(rs.getString("CASH_PURSE_TYPE"));
                this.setAllowTopup(rs.getString("CASH_TOPUP"));
                this.setCardTypeFeature(rs.getString("CARD_TYPE_FEATURE"));
                setCardExist(true);
                if(!this.getStatus().trim().equalsIgnoreCase("G")) this.setCardInActive(true);
                if(!this.getBlockCode().trim().equals("")) this.setCardBlocked(true);
                if(this.getCardActive().trim().equalsIgnoreCase("Y")){
                    this.setCardActivated(true);
                    if(this.getExpiryDays() < 0) this.setCardExpired(true);
                }
                System.out.println("Card Exist   : "+this.isCardExist());
                System.out.println("Card Status  : "+this.isCardInActive());
                System.out.println("Card Expiry  : "+this.isCardExpired());
                System.out.println("Card Blocked : "+this.isCardBlocked());
                System.out.println("Card Activated : "+this.isCardActivated());
                System.out.println( "Card Exist:"+this.isCardExist()+"/Card Status:"+this.isCardInActive()+"/Card Expiry:"+this.isCardExpired()+"/Card Blocked:"+this.isCardBlocked()+"/Card Activated : "+this.isCardActivated());
            }
        }catch(Exception vep) {
            System.out.println("Exception while getting card information : "+vep.toString());
            throw vep;
        }
    }


    /** Getting Card Holder Details */
    public String getDateOfBirth(String birthDaySchemeId) throws Exception {
        System.out.println("Getting Card Holder Detail");
        String dateOfBirth = null ;
        try{

            StringBuffer strSql = new StringBuffer();
            strSql.append("SELECT TO_CHAR(DOB,'DDMMYYYY')DOB ");
            strSql.append("FROM CARD_CARDHOLDER CC, CARDHOLDER CH, SCHEME_MASTER SM ");
            strSql.append("WHERE CC.CARD_NO='"+this.getCardNo()+"' ");
            strSql.append("AND CC.CARDHOLDER_SEQ = CH.CARDHOLDER_SEQ ");
            strSql.append("AND CC.ISSUER_ID = CH.ISSUER_ID ");
            strSql.append("AND SM.SCHEME_ID='"+birthDaySchemeId+"' ");
            strSql.append("AND TO_CHAR(SCHEME_START_DATE, 'MMDD') <= TO_CHAR(DOB, 'MMDD') ");
            strSql.append("AND TO_CHAR(SCHEME_END_DATE, 'MMDD') >= TO_CHAR(DOB, 'MMDD') ");
            System.out.println(strSql.toString());//HIEP
            objDBManager.executeSQL(strSql.toString());
            TPlusResultSet rs = objDBManager.getResultSet();
            if(rs.next()){
                dateOfBirth = rs.getString("DOB");
                System.out.println("Data Of Birth : "+dateOfBirth);
            }
        }catch(Exception vep) {
            System.out.println("Exception while getting cardholder information : "+vep.toString());
            throw vep;
        }
        return dateOfBirth;
    }


    /** Getting Card Holder Details */
    public String getGender() throws Exception {
        System.out.println("Getting Card Holder Detail");
        String gender = null ;
        try{

            StringBuffer strSql = new StringBuffer();
            strSql.append("SELECT TO_CHAR(DOB,'DDMMYYYY')DOB, GENDER ");
            strSql.append("FROM CARD_CARDHOLDER CC, CARDHOLDER CH ");
            strSql.append("WHERE CC.CARD_NO='"+this.getCardNo()+"' ");
            strSql.append("AND CC.CARDHOLDER_SEQ = CH.CARDHOLDER_SEQ ");
            strSql.append("AND CC.ISSUER_ID = CH.ISSUER_ID ");
            System.out.println(strSql.toString());//HIEP
            objDBManager.executeSQL(strSql.toString());
            TPlusResultSet rs = objDBManager.getResultSet();
            if(rs.next()){
                gender = rs.getString("GENDER");
                System.out.println("Gender : "+gender);
            }
        }catch(Exception vep) {
            System.out.println("Exception while getting cardholder gender information : "+vep.toString());
            throw vep;
        }
        return gender;
    }


    /** Getting Track 1 & 2 of Selected Card */
    public String getCardData() throws Exception {
        System.out.println("Getting Card Track 1 & Track 2");
        String cardData = null ;
        try{

            StringBuffer strSql = new StringBuffer();
            strSql.append("SELECT TO_CHAR(ISSUE_DATE,'YYMM')ISSUE_DATE, TO_CHAR(EXPIRY_DATE,'YYMM')EXPIRY_DATE ");
            strSql.append("FROM CARD C ");
            strSql.append("WHERE C.ISSUER_ID ='"+ this.getIssuerId() +"' ");
            strSql.append("AND C.CARD_NO = '"+ this.getCardNo()+"' ");
            System.out.println(strSql.toString());//HIEP

            String cardNumber = this.getCardNo();
            String effDate = "";
            String expDate = "";
            String EmbossingName = (this.getNameOnCard() + "                              ").substring(0, 25);
            String svcCode = "000"; //3 digits
            String CVV = "000"; // 3-digits
            String CVV2 = "000"; // 3-digits
            String PVV = "0000"; // 4 digits
            String PVKI = "0"; // 1 digits

            objDBManager.executeSQL(strSql.toString());
            TPlusResultSet rs = objDBManager.getResultSet();
            if(rs.next()){
                effDate = rs.getString("ISSUE_DATE");
                expDate = rs.getString("EXPIRY_DATE");
                System.out.println("ISSUE_DATE : "+effDate);
                System.out.println("EXPIRY_DATE : "+expDate);
            }

            //track 1
            StringBuffer track1 = new StringBuffer();
            track1.append("B");
            track1.append(cardNumber);
            track1.append("^");
            track1.append(EmbossingName);
            track1.append(" |");

            //track 2
            StringBuffer track2 = new StringBuffer();
            track2.append(cardNumber);
            track2.append("=");
            track2.append(expDate);
            track2.append(svcCode);
            track2.append(PVKI);
            track2.append(PVV);
            track2.append(CVV);
            track2.append("||");

            cardData = track1.toString() + track2.toString();

        }catch(Exception vep) {
            System.out.println("Exception while getting cardholder information : "+vep.toString());
            throw vep;
        }
        return cardData;
    }


    public StringBuffer getCardActivationSql(){
        StringBuffer query = new StringBuffer();
        query.append("UPDATE CARD SET ");
        query.append("CARD_ACTIVE ='Y', ");
        query.append("LAST_UPDATED_DATE = SYSDATE, ");
        query.append("LAST_UPDATED_BY = 'SYSTEM' ");
        query.append("WHERE CARD_NO='"+this.getCardNo()+"' AND ISSUER_ID='"+this.getIssuerId()+"' ");
        return query;
    }

    public String getCardExpiryDate(String issuerId, String cardNo)throws Exception {
        String expiryDate = "";
        System.out.println("Validating the card record  & getting the values.");
        try{
            StringBuffer strSql = new StringBuffer();
            strSql.append("SELECT TO_CHAR(EXPIRY_DATE,'DDMMYYYY') EXPIRY_DATE ");
            strSql.append("FROM CARD ");
            strSql.append("WHERE CARD_NO='"+cardNo+"' ");
            strSql.append("AND ISSUER_ID = '"+issuerId+"' ");
            System.out.println(strSql.toString());//HIEP
            objDBManager.executeSQL(strSql.toString());
            TPlusResultSet rs = objDBManager.getResultSet();
            if(rs.next()){
                expiryDate = rs.getString("EXPIRY_DATE");
            }
        }catch(Exception vep) {
            System.out.println("Exception while getting CardExpiryDate : "+vep.toString());
            throw vep;
        }
        return expiryDate;
    }

    /**
     * Getter for property cardExist.
     * @return Value of property cardExist.
     */
    public boolean isCardExist() {
        return cardExist;
    }


    /**
     * Getter for property cardExist.
     * @return Value of property cardExist.
     */
    public boolean isPINBlocked() {
        return pinBlocked;
    }

    public void setPINBlocked(boolean pinBlocked)
    {
		this.pinBlocked = pinBlocked;
	}


    /**
     * Setter for property cardExist.
     * @param cardExist New value of property cardExist.
     */
    public void setCardExist(boolean cardExist) {
        this.cardExist = cardExist;
    }

    /**
     * Getter for property cardExpired.
     * @return Value of property cardExpired.
     */
    public boolean isCardExpired() {
        return cardExpired;
    }

    /**
     * Setter for property cardExpired.
     * @param cardExpired New value of property cardExpired.
     */
    public void setCardExpired(boolean cardExpired) {
        this.cardExpired = cardExpired;
    }

    /**
     * Getter for property cardInActive.
     * @return Value of property cardInActive.
     */
    public boolean isCardInActive() {
        return cardInActive;
    }

    /**
     * Setter for property cardInActive.
     * @param cardInActive New value of property cardInActive.
     */
    public void setCardInActive(boolean cardInActive) {
        this.cardInActive = cardInActive;
    }

    /**
     * Getter for property cardBlocked.
     * @return Value of property cardBlocked.
     */
    public boolean isCardBlocked() {
        return cardBlocked;
    }

    /**
     * Setter for property cardBlocked.
     * @param cardBlocked New value of property cardBlocked.
     */

    public void setCardBlocked(boolean cardBlocked) {
        this.cardBlocked = cardBlocked;
    }

    /**
     * Getter for property cardActivated.
     * @return Value of property cardActivated.
     */
    public boolean isCardActivated() {
        return cardActivated;
    }

    /**
     * Setter for property cardActivated.
     * @param cardActivated New value of property cardActivated.
     */
    public void setCardActivated(boolean cardActivated) {
        this.cardActivated = cardActivated;
    }

    /**
     * Getter for property cardValidity.
     * @return Value of property cardValidity.
     */
    public java.lang.String getCardValidity() {
        return cardValidity;
    }

    /**
     * Setter for property cardValidity.
     * @param cardValidity New value of property cardValidity.
     */
    public void setCardValidity(java.lang.String cardValidity) {
        this.cardValidity = cardValidity;
    }

    /**
     * Getter for property validityPeriod.
     * @return Value of property validityPeriod.
     */
    public java.lang.String getValidityPeriod() {
        return validityPeriod;
    }

    /**
     * Setter for property validityPeriod.
     * @param validityPeriod New value of property validityPeriod.
     */
    public void setValidityPeriod(java.lang.String validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    /**
     * Getter for property fixedValue.
     * @return Value of property fixedValue.
     */
    public java.lang.String getFixedValue() {
        return fixedValue;
    }

    /**
     * Setter for property fixedValue.
     * @param fixedValue New value of property fixedValue.
     */
    public void setFixedValue(java.lang.String fixedValue) {
        this.fixedValue = fixedValue;
    }

    /**
     * Getter for property cashPurseType.
     * @return Value of property cashPurseType.
     */
    public java.lang.String getCashPurseType() {
        return cashPurseType;
    }

    /**
     * Setter for property cashPurseType.
     * @param cashPurseType New value of property cashPurseType.
     */
    public void setCashPurseType(java.lang.String cashPurseType) {
        this.cashPurseType = cashPurseType;
    }

    /**
     * Getter for property allowTopup.
     * @return Value of property allowTopup.
     */
    public java.lang.String getAllowTopup() {
        return allowTopup;
    }

    /**
     * Setter for property allowTopup.
     * @param allowTopup New value of property allowTopup.
     */
    public void setAllowTopup(java.lang.String allowTopup) {
        this.allowTopup = allowTopup;
    }

    /**
     * Getter for property cardTypeFeature.
     * @return Value of property cardTypeFeature.
     */
    public java.lang.String getCardTypeFeature() {
        return cardTypeFeature;
    }

    /**
     * Setter for property cardTypeFeature.
     * @param cardTypeFeature New value of property cardTypeFeature.
     */
    public void setCardTypeFeature(java.lang.String cardTypeFeature) {
        this.cardTypeFeature = cardTypeFeature;
    }

    /**
     * Getter for property loyalty.
     * @return Value of property loyalty.
     */
    public boolean isLoyalty() {
        if (this.getCardTypeFeature().trim().equals("GIFTCARDLOYALTY") || this.getCardTypeFeature().trim().equals("CASHCARDLOYALTY"))
            return true;
        else
            return false;
    }


  public void setMerchantNo(String merchantNo)
  {
    this.merchantNo = merchantNo;
  }


  public String getMerchantNo()
  {
    return merchantNo;
  }


  public void setTerminalNo(String terminalNo)
  {
    this.terminalNo = terminalNo;
  }


  public String getTerminalNo()
  {
    return terminalNo;
  }


  public void setOldCategory(String oldCategory)
  {
    this.oldCategory = oldCategory;
  }


  public String getOldCategory()
  {
    return oldCategory;
  }


  public void setNewCategory(String newCategory)
  {
    this.newCategory = newCategory;
  }


  public String getNewCategory()
  {
    return newCategory;
  }


  public void setMaxTopUpAmount(String maxTopUpAmount)
  {
    this.maxTopUpAmount = maxTopUpAmount;
  }


  public String getMaxTopUpAmount()
  {
    return maxTopUpAmount;
  }


  public void setCardProduct(String cardProduct)
  {
    this.cardProduct = cardProduct;
  }


  public String getCardProduct()
  {
    return cardProduct;
  }


  public void setIsUnnamed(boolean isUnnamed)
  {
    this.isUnnamed = isUnnamed;
  }


  public boolean isIsUnnamed()
  {
    return isUnnamed;
  }


  public void setIsNewCard(boolean isNewCard)
  {
    this.isNewCard = isNewCard;
  }


  public boolean isIsNewCard()
  {
    return isNewCard;
  }

}