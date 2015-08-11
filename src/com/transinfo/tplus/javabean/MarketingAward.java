package com.transinfo.tplus.javabean;

import java.util.ArrayList;

import com.transinfo.tplus.util.DBManager;
import com.transinfo.tplus.util.TPlusResultSet;

/**
 *
 * @author  Owner
 */
public class MarketingAward {

    public DBManager objDBManager = null;

    //input data
    private String issuerId = "";
    private String cardNo = "";
    private String cardProduct = "";
    private String awardType = "";
    private String userId = "";
    private String transactionAmount = "";

    //processing data
    private String points = "";
    private String cashAwardType = "";
    private String cashAward = "";
    private String cashAwardMinimum = "";

    //output data
    private String awardPoint   = "0";
    private String awardCash    = "0";
    private ArrayList awardCoupon = new ArrayList();
    private boolean awardExist = false;

    /** Creates a new instance of CardProduction */
    public MarketingAward() {
        objDBManager = new DBManager();
    }

    public StringBuffer getLoyaltyAwardSql(){
        StringBuffer sqlStr = new StringBuffer();
        sqlStr.append("UPDATE CARD_POINT_PURSE SET ");
        sqlStr.append("TOTAL_EARN= TOTAL_EARN + "+this.getAwardPoint()+", ");
        sqlStr.append("BALANCE= BALANCE + "+this.getAwardPoint()+", ");
        sqlStr.append("LAST_AWARD_DATE= SYSDATE, ");
        sqlStr.append("LAST_UPDATED_DATE= SYSDATE, ");
        sqlStr.append("LAST_UPDATED_BY= '"+this.getUserId()+"' ");
        sqlStr.append("WHERE ISSUER_ID= '"+this.getIssuerId()+"' ");
        sqlStr.append("AND CARD_NO = '"+this.getCardNo()+"' ");
        System.out.println(sqlStr.toString());
        return sqlStr;
    }

    public StringBuffer getCashAwardSql(){
        StringBuffer sqlStr = new StringBuffer();
        sqlStr.append("UPDATE CARD_CASH_PURSE SET ");
        sqlStr.append("TOTAL_RELOAD= TOTAL_RELOAD + "+this.getAwardCash()+", ");
        sqlStr.append("BALANCE= BALANCE + "+this.getAwardCash()+", ");
        sqlStr.append("LAST_RELOAD_DATE= SYSDATE, ");
        sqlStr.append("LAST_UPDATED_DATE= SYSDATE, ");
        sqlStr.append("LAST_UPDATED_BY= '"+this.getUserId()+"' ");
        sqlStr.append("WHERE ISSUER_ID= '"+this.getIssuerId()+"' ");
        sqlStr.append("AND CARD_NO = '"+this.getCardNo()+"' ");
        System.out.println(sqlStr.toString());
        return sqlStr;
    }

    public ArrayList getCouponAwardSql(){
        ArrayList arlSqlList = new ArrayList();
        StringBuffer sqlStr = new StringBuffer();
        ArrayList arlList = this.getAwardCoupon();
        if(arlList != null){
            for(int i=0;i<arlList.size(); i++){
                String couponId = (String) arlList.get(i);
                sqlStr = new StringBuffer();
                sqlStr.append("INSERT INTO CARD_COUPON_PURSE (ISSUER_ID, CARD_NO, COUPON_ID, COUPON_SERIAL_NO, COUPON_TYPE, COUPON_UNIT, COUPON_VALUE, ");
                sqlStr.append("COUPON_TEXT, COUPON_EXPIRY_TYPE, COUPON_EXPIRY_DATE, COUPON_STATUS, AWARD_MERCHANT, AWARD_DATE, MIN_AMT,  ");
                sqlStr.append("LAST_UPDATED_DATE, LAST_UPDATED_BY) SELECT '"+this.getIssuerId()+"', '"+this.getCardNo()+"', COUPON_ID,COUPON_NEXT_SERIAL_NO,  ");
                sqlStr.append("COUPON_TYPE, COUPON_UNIT, COUPON_VALUE, COUPON_TEXT, COUPON_EXPIRY_TYPE,   ");
                sqlStr.append("CASE WHEN COUPON_EXPIRY_TYPE = 'Birth' THEN NULL  ");
                sqlStr.append("WHEN COUPON_EXPIRY_TYPE = 'Day' THEN  SYSDATE + COUPON_EXPIRY_DAYS  ");
                sqlStr.append("ELSE TO_DATE(COUPON_EXPIRY_DATE) ");
                sqlStr.append("END AS EXPIRY_DATE, 'N', 'Activation Award', SYSDATE, MIN_AMT, SYSDATE, 'SYSTEM' ");
                sqlStr.append("FROM COUPON_MASTER WHERE COUPON_ID='"+couponId+"' AND ISSUER_ID='"+this.getIssuerId()+"' ");
                System.out.println(sqlStr.toString());
                arlSqlList.add(sqlStr.toString());

                sqlStr = new StringBuffer();
                sqlStr.append("UPDATE COUPON_MASTER ");
                sqlStr.append("SET COUPON_NEXT_SERIAL_NO = LPAD(TO_CHAR(TO_NUMBER(COUPON_NEXT_SERIAL_NO)+1), 8, 0) ");
                sqlStr.append("WHERE COUPON_ID='"+couponId+"' AND ISSUER_ID='"+this.getIssuerId()+"' ");
                System.out.println(sqlStr.toString());
                arlSqlList.add(sqlStr.toString());
            }
        }
        return arlSqlList;
    }

    public ArrayList getCouponAwardSql(String transactionId){
        ArrayList arlSqlList = new ArrayList();
        StringBuffer sqlStr = new StringBuffer();
        ArrayList arlList = this.getAwardCoupon();
        if(arlList != null){
            for(int i=0;i<arlList.size(); i++){
                String couponId = (String) arlList.get(i);
                sqlStr = new StringBuffer();
                sqlStr.append("INSERT INTO CARD_COUPON_PURSE (ISSUER_ID, CARD_NO, COUPON_ID, COUPON_SERIAL_NO, COUPON_TYPE, COUPON_UNIT, COUPON_VALUE, ");
                sqlStr.append("COUPON_TEXT, COUPON_EXPIRY_TYPE, COUPON_EXPIRY_DATE, COUPON_STATUS, AWARD_MERCHANT, AWARD_DATE, MIN_AMT,  ");
                sqlStr.append("LAST_UPDATED_DATE, LAST_UPDATED_BY, TRANSACTION_ID ) SELECT '"+this.getIssuerId()+"', '"+this.getCardNo()+"', COUPON_ID,COUPON_NEXT_SERIAL_NO,  ");
                sqlStr.append("COUPON_TYPE, COUPON_UNIT, COUPON_VALUE, COUPON_TEXT, COUPON_EXPIRY_TYPE,   ");
                sqlStr.append("CASE WHEN COUPON_EXPIRY_TYPE = 'Birth' THEN NULL  ");
                sqlStr.append("WHEN COUPON_EXPIRY_TYPE = 'Day' THEN  SYSDATE + COUPON_EXPIRY_DAYS  ");
                sqlStr.append("ELSE TO_DATE(COUPON_EXPIRY_DATE) ");
                sqlStr.append("END AS EXPIRY_DATE, 'N', 'Activation Award', SYSDATE, MIN_AMT, SYSDATE, 'SYSTEM','"+transactionId+"' ");
                sqlStr.append("FROM COUPON_MASTER WHERE COUPON_ID='"+couponId+"' AND ISSUER_ID='"+this.getIssuerId()+"' ");
                System.out.println(sqlStr.toString());
                arlSqlList.add(sqlStr.toString());
                sqlStr = new StringBuffer();
                sqlStr.append("UPDATE COUPON_MASTER ");
                sqlStr.append("SET COUPON_NEXT_SERIAL_NO = LPAD(TO_CHAR(TO_NUMBER(COUPON_NEXT_SERIAL_NO)+1), 8, 0) ");
                sqlStr.append("WHERE COUPON_ID='"+couponId+"' AND ISSUER_ID='"+this.getIssuerId()+"' ");
                System.out.println(sqlStr.toString());
                arlSqlList.add(sqlStr.toString());
            }
        }
        return arlSqlList;
    }

/*    public ArrayList getMarketingAwards(){
        ArrayList mktAwardList = new ArrayList();
        System.out.println("<<< MARKETING AWARD START >>>");
        try{
            SignonRenewalAwardDataBean objSignonRenewalAwardDataBean = getMarketingAwardData();

            if(objSignonRenewalAwardDataBean != null){
                this.print(objSignonRenewalAwardDataBean);
                StringBuffer sqlStr = new StringBuffer();
                //UPDATE POINT PURSE
                if(this.getCardTypeFeature().trim().equals("LOYALTY") || this.getCardTypeFeature().trim().equals("GIFTCARDLOYALTY")
                || this.getCardTypeFeature().trim().equals("CASHCARDLOYALTY")){
                    String awardPoints = objSignonRenewalAwardDataBean.getPoints();
                    if(Integer.parseInt(awardPoints) > 0){
                        sqlStr = new StringBuffer();
                        sqlStr.append("UPDATE CARD_POINT_PURSE SET ");
                        sqlStr.append("TOTAL_EARN= TOTAL_EARN + "+objSignonRenewalAwardDataBean.getPoints()+", ");
                        sqlStr.append("BALANCE= BALANCE + "+objSignonRenewalAwardDataBean.getPoints()+", ");
                        sqlStr.append("LAST_AWARD_DATE= SYSDATE, ");
                        sqlStr.append("LAST_UPDATED_DATE= SYSDATE, ");
                        sqlStr.append("LAST_UPDATED_BY= '"+this.getUserId()+"' ");
                        sqlStr.append("WHERE ISSUER_ID= '"+this.getIssuerId()+"' ");
                        sqlStr.append("AND CARD_NO = '"+this.getCardNo()+"' ");
                        System.out.println(sqlStr.toString());
                        mktAwardList.add(sqlStr.toString());
                    }
                }
                //UPDATE CASH PURSE
                if((this.getCardTypeFeature().trim().equals("GIFTCARD") || this.getCardTypeFeature().trim().equals("CASHCARD")
                || this.getCardTypeFeature().trim().equals("GIFTCARDLOYALTY") || this.getCardTypeFeature().trim().equals("CASHCARDLOYALTY"))
                && this.getCardPurseType().trim().equals("G")){
                    String minAmount = objSignonRenewalAwardDataBean.getCashAwardMinimum();
                    boolean award = true;
                    if(!minAmount.trim().equals("")){
                        if(!minAmount.trim().equals("0")){
                            int awardMinAmount = Integer.parseInt(minAmount);
                            int cashAmount = Integer.parseInt(this.getCashAmount());
                            if(cashAmount < awardMinAmount){
                                award = false;
                            }
                        }
                    }

                    String awardCash = "";
                    if(award){
                        if(objSignonRenewalAwardDataBean.getCashAwardType().trim().equals("F")){
                            awardCash = objSignonRenewalAwardDataBean.getCashAward();
                        }else{
                            int percentageAmount = (Integer.parseInt(this.getCashAmount()) * Integer.parseInt(objSignonRenewalAwardDataBean.getCashAward())) / 100;
                            awardCash = String.valueOf(percentageAmount);
                        }
                        sqlStr = new StringBuffer();
                        sqlStr.append("UPDATE CARD_CASH_PURSE SET ");
                        sqlStr.append("TOTAL_RELOAD= TOTAL_RELOAD + "+awardCash+", ");
                        sqlStr.append("BALANCE= BALANCE + "+awardCash+", ");
                        sqlStr.append("LAST_RELOAD_DATE= SYSDATE, ");
                        sqlStr.append("LAST_UPDATED_DATE= SYSDATE, ");
                        sqlStr.append("LAST_UPDATED_BY= '"+this.getUserId()+"' ");
                        sqlStr.append("WHERE ISSUER_ID= '"+this.getIssuerId()+"' ");
                        sqlStr.append("AND CARD_NO = '"+this.getCardNo()+"' ");
                        System.out.println(sqlStr.toString());
                        mktAwardList.add(sqlStr.toString());
                    }
                }
                //INSERT INTO COUPON PURSE
                ArrayList arlList = this.getAwardCouponList(objSignonRenewalAwardDataBean.getReferenceNo());
                if(arlList != null){
                    for(int i=0;i<arlList.size(); i++){
                        String couponId = (String) arlList.get(i);
                        sqlStr = new StringBuffer();
                        sqlStr.append("INSERT INTO CARD_COUPON_PURSE (ISSUER_ID, CARD_NO, COUPON_ID, COUPON_SERIAL_NO, COUPON_TYPE, COUPON_UNIT, COUPON_VALUE, ");
                        sqlStr.append("COUPON_TEXT, COUPON_EXPIRY_TYPE, COUPON_EXPIRY_DATE, COUPON_STATUS, AWARD_MERCHANT, AWARD_DATE, MIN_AMT,  ");
                        sqlStr.append("LAST_UPDATED_DATE, LAST_UPDATED_BY) SELECT '"+this.getIssuerId()+"', '"+this.getCardNo()+"', COUPON_ID,COUPON_NEXT_SERIAL_NO,  ");
                        sqlStr.append("COUPON_TYPE, COUPON_UNIT, COUPON_VALUE, COUPON_TEXT, COUPON_EXPIRY_TYPE,   ");
                        sqlStr.append("CASE WHEN COUPON_EXPIRY_TYPE = 'Birth' THEN NULL  ");
                        sqlStr.append("WHEN COUPON_EXPIRY_TYPE = 'Day' THEN  SYSDATE + COUPON_EXPIRY_DAYS  ");
                        sqlStr.append("ELSE TO_DATE(COUPON_EXPIRY_DATE) ");
                        sqlStr.append("END AS EXPIRY_DATE, 'N', 'Signon Award', SYSDATE, MIN_AMT, SYSDATE, 'SYSTEM' ");
                        sqlStr.append("FROM COUPON_MASTER WHERE COUPON_ID='"+couponId+"' AND ISSUER_ID='"+this.getIssuerId()+"' ");
                        System.out.println(sqlStr.toString());
                        mktAwardList.add(sqlStr.toString());

                        sqlStr = new StringBuffer();
                        sqlStr.append("UPDATE COUPON_MASTER ");
                        sqlStr.append("SET COUPON_NEXT_SERIAL_NO = LPAD(TO_CHAR(TO_NUMBER(COUPON_NEXT_SERIAL_NO)+1), 8, 0) ");
                        sqlStr.append("WHERE COUPON_ID='"+couponId+"' AND ISSUER_ID='"+this.getIssuerId()+"' ");
                        System.out.println(sqlStr.toString());
                        mktAwardList.add(sqlStr.toString());
                    }
                }

            }else{
                System.out.println("Nothing to award.");
            }
        }catch(Exception e){
            System.out.println("<<< EXCEPTION WHILE MARKETING AWARD >>> " + e.toString());
        }
        return mktAwardList;
    }

    private void print(SignonRenewalAwardDataBean objSignonRenewalAwardDataBean){
        System.out.println("Card Number         :   "+this.getCardNo());
        System.out.println("Award Details...");
        System.out.println("Reference Number    :   "+objSignonRenewalAwardDataBean.getReferenceNo());
        System.out.println("Card Type           :   "+objSignonRenewalAwardDataBean.getCardType());
        System.out.println("Points              :   "+objSignonRenewalAwardDataBean.getPoints());
        System.out.println("Cash Award Type     :   "+objSignonRenewalAwardDataBean.getCashAwardType());
        System.out.println("Cash Award          :   "+objSignonRenewalAwardDataBean.getCashAward());
        System.out.println("Min Cash            :   "+objSignonRenewalAwardDataBean.getCashAwardMinimum());
        System.out.println("Card Type Feature   :   "+this.getCardTypeFeature());
        System.out.println("Card Purse Type     :   "+this.getCardPurseType());
    }
 */

    public void execute() throws Exception {
        StringBuffer strSql = new StringBuffer();
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>> Awarding start.");
        try{
            strSql.append("SELECT SRA.REFERENCE_NO, SRA.CARD_PRODUCT_ID, NVL(SRA.POINTS,'0') AS POINTS, SRA.CASH_AWARD_TYPE, ");
            strSql.append("NVL(SRA.CASH_AWARD,'0') AS CASH_AWARD, NVL(SRA.CASH_MIN_AMT,'0') AS CASH_MIN_AMT, CA.CASH_PURSE_TYPE ");
            strSql.append("FROM SIGNON_RENEWAL_AWARD SRA, CARD_PRODUCT CP, CARD CA  ");
            strSql.append("WHERE CP.CARD_PRODUCT_ID = SRA.CARD_PRODUCT_ID ");
            strSql.append("AND CP.ISSUER_ID = SRA.ISSUER_ID  ");
            strSql.append("AND CA.CARD_PRODUCT_ID = CP.CARD_PRODUCT_ID ");
            strSql.append("AND CA.ISSUER_ID = CP.ISSUER_ID ");
            strSql.append("AND TO_CHAR(SRA.START_DATE,'YYYYMMDD') <= TO_CHAR(SYSDATE,'YYYYMMDD') ");
            strSql.append("AND TO_CHAR(SRA.END_DATE,'YYYYMMDD') >= TO_CHAR(SYSDATE,'YYYYMMDD') ");
            strSql.append("AND CA.CARD_NO='"+this.getCardNo()+"' AND SRA.AWARD_TYPE='"+this.getAwardType()+"' AND SRA.ISSUER_ID='"+this.getIssuerId()+"' AND SRA.CARD_PRODUCT_ID = '" + this.getCardProduct() + "' ");
            System.out.println(strSql.toString());
            objDBManager.executeSQL(strSql.toString());
            TPlusResultSet rs = objDBManager.getResultSet();
            if(rs.next()){
                this.setAwardPoint(rs.getString("POINTS"));
                this.setAwardCash(rs.getString("CASH_AWARD"));
                String cashAwardType = rs.getString("CASH_AWARD_TYPE");
                double transactionAmount = Double.parseDouble(this.getTransactionAmount()) / 100;

                System.out.println("Cash Award : "+this.getAwardCash());
                System.out.println("Points Award : "+this.getAwardPoint());
                System.out.println("cashAwardType : "+cashAwardType);
                System.out.println("Tranx amount : "+transactionAmount);

                if(cashAwardType.trim().equals("P")){
                    double percentageAmount = (Double.parseDouble(this.getAwardCash()) * transactionAmount) / 100;
                    this.setAwardCash(String.valueOf(percentageAmount));
                }
                String minAmount = rs.getString("CASH_MIN_AMT");
                boolean award = true;
                if(!minAmount.trim().equals("")){
                    if(!minAmount.trim().equals("0")){
                        double awardMinAmount = Double.parseDouble(minAmount);
                        System.out.println("Minimum amount : "+awardMinAmount);
                        if(transactionAmount < awardMinAmount){
                            award = false;
                        }
                    }
                }
                if(!award) this.setAwardCash("0");
                System.out.println("Award amount : "+this.getAwardCash());
                this.setAwardCoupon(this.getAwardCouponList(rs.getString("REFERENCE_NO")));
                this.setAwardExist(true);
            }
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>> Awarding End.");
        }catch(Exception vep) {
            System.out.println("Exception while getting card production information : "+vep.toString());
            throw vep;
        }
    }

    private ArrayList getAwardCouponList(String strReferenceNo)  throws Exception {
        ArrayList arlList = new ArrayList();
        StringBuffer strSql = new StringBuffer();
        try{
            strSql.append("SELECT COUPON_ID FROM SIGNON_RENEWAL_COUPONS ");
            strSql.append("WHERE REFERENCE_NO = '"+strReferenceNo+"' ");
            strSql.append("AND ISSUER_ID = '"+this.getIssuerId()+"' ");
            System.out.println(strSql.toString());
            objDBManager.executeSQL(strSql.toString());
            TPlusResultSet rs = objDBManager.getResultSet();
            System.out.println("Coupons To Awards....");
            while(rs.next()){
                arlList.add(rs.getString("COUPON_ID"));
                System.out.println(rs.getString("COUPON_ID"));
            }
        }catch(Exception e){
            System.out.println("Exception while getting card production information : "+e.toString());
            throw e;
        }
        return arlList;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try{
            MarketingAward objMarketingAward = new MarketingAward();
            objMarketingAward.setIssuerId("VSN");
            objMarketingAward.setCardNo("1111000009");
            objMarketingAward.setTransactionAmount("30000");
            objMarketingAward.setAwardType("ACTIVATION");
            objMarketingAward.setUserId("HIEP");
            objMarketingAward.execute();
            if(objMarketingAward.isAwardExist()){
                System.out.println("----- Loyalty -----");
                System.out.println(objMarketingAward.getLoyaltyAwardSql());
                System.out.println(objMarketingAward.getAwardPoint());
                System.out.println("----- Cash -----");
                System.out.println(objMarketingAward.getCashAwardSql());
                System.out.println(objMarketingAward.getAwardCash());
                System.out.println("----- Coupon -----");
                System.out.println(objMarketingAward.getCouponAwardSql());
                System.out.println(objMarketingAward.getAwardCoupon());
            }
        }catch(Exception bep) {
            System.out.println("Exception while loading the AdminConfig... "+bep.toString());
        }
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
     * Getter for property awardType.
     * @return Value of property awardType.
     */
    public java.lang.String getAwardType() {
        return awardType;
    }

    /**
     * Setter for property awardType.
     * @param awardType New value of property awardType.
     */
    public void setAwardType(java.lang.String awardType) {
        this.awardType = awardType;
    }

    /**
     * Getter for property userId.
     * @return Value of property userId.
     */
    public java.lang.String getUserId() {
        return userId;
    }

    /**
     * Setter for property userId.
     * @param userId New value of property userId.
     */
    public void setUserId(java.lang.String userId) {
        this.userId = userId;
    }

    /**
     * Getter for property transactionAmount.
     * @return Value of property transactionAmount.
     */
    public java.lang.String getTransactionAmount() {
        return transactionAmount;
    }

    /**
     * Setter for property transactionAmount.
     * @param transactionAmount New value of property transactionAmount.
     */
    public void setTransactionAmount(java.lang.String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    /**
     * Getter for property points.
     * @return Value of property points.
     */
    public java.lang.String getPoints() {
        return points;
    }

    /**
     * Setter for property points.
     * @param points New value of property points.
     */
    public void setPoints(java.lang.String points) {
        this.points = points;
    }

    /**
     * Getter for property cashAwardType.
     * @return Value of property cashAwardType.
     */
    public java.lang.String getCashAwardType() {
        return cashAwardType;
    }

    /**
     * Setter for property cashAwardType.
     * @param cashAwardType New value of property cashAwardType.
     */
    public void setCashAwardType(java.lang.String cashAwardType) {
        this.cashAwardType = cashAwardType;
    }

    /**
     * Getter for property cashAward.
     * @return Value of property cashAward.
     */
    public java.lang.String getCashAward() {
        return cashAward;
    }

    /**
     * Setter for property cashAward.
     * @param cashAward New value of property cashAward.
     */
    public void setCashAward(java.lang.String cashAward) {
        this.cashAward = cashAward;
    }

    /**
     * Getter for property cashAwardMinimum.
     * @return Value of property cashAwardMinimum.
     */
    public java.lang.String getCashAwardMinimum() {
        return cashAwardMinimum;
    }

    /**
     * Setter for property cashAwardMinimum.
     * @param cashAwardMinimum New value of property cashAwardMinimum.
     */
    public void setCashAwardMinimum(java.lang.String cashAwardMinimum) {
        this.cashAwardMinimum = cashAwardMinimum;
    }

    /**
     * Getter for property awardPoint.
     * @return Value of property awardPoint.
     */
    public java.lang.String getAwardPoint() {
        return awardPoint;
    }

    /**
     * Setter for property awardPoint.
     * @param awardPoint New value of property awardPoint.
     */
    public void setAwardPoint(java.lang.String awardPoint) {
        this.awardPoint = awardPoint;
    }

    /**
     * Getter for property awardCash.
     * @return Value of property awardCash.
     */
    public java.lang.String getAwardCash() {
        return awardCash;
    }

    /**
     * Setter for property awardCash.
     * @param awardCash New value of property awardCash.
     */
    public void setAwardCash(java.lang.String awardCash) {
        this.awardCash = awardCash;
    }

    /**
     * Getter for property awardCoupon.
     * @return Value of property awardCoupon.
     */
    public java.util.ArrayList getAwardCoupon() {
        return awardCoupon;
    }

    /**
     * Setter for property awardCoupon.
     * @param awardCoupon New value of property awardCoupon.
     */
    public void setAwardCoupon(java.util.ArrayList awardCoupon) {
        this.awardCoupon = awardCoupon;
    }

    /**
     * Getter for property awardExist.
     * @return Value of property awardExist.
     */
    public boolean isAwardExist() {
        return awardExist;
    }

    /**
     * Setter for property awardExist.
     * @param awardExist New value of property awardExist.
     */
    public void setAwardExist(boolean awardExist) {
        this.awardExist = awardExist;
    }


  public void setCardProduct(String cardProduct)
  {
    this.cardProduct = cardProduct;
  }


  public String getCardProduct()
  {
    return cardProduct;
  }

}