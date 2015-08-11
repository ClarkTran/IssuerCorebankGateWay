package com.transinfo.tplus.javabean;

import com.transinfo.tplus.util.DBManager;
import com.transinfo.tplus.util.DBUtil;
import com.transinfo.tplus.util.DateUtil;
import com.transinfo.tplus.util.TPlusResultSet;

public class CalculatePoints {

    private String LogName = "CALCPOINT";
    private DBManager objDBManager = null;
    private boolean recordExist = false;
    private String issuerId = "";
    private double transAmount = 0;
    private double pointsAwarded = 0;
    private double leftSaleAmount = 0;
    private double redeemPoints = 0;
    private double convertAmount = 0;
    private String merchantNo = "";
    private String cardType = "";
    private String cardProduct = "";
    private String schemeType = "";
    private String schemeId = "";
    private String birthDaySchemeId = "";
    private String birthDayAwardSchemeId = "";
    private String cardNumber = "";
    private String gender = "";
    private String cardClassId = "";
    private double convertRatio = 0;

    public CalculatePoints() {
        objDBManager = new DBManager();
    }

    public void execute()throws Exception {
        try{
          //  this.getValidScheme();
            if(!this.getSchemeId().trim().equals("")){
                this.setPointsAwarded(this.getBasicPoints() + this.getRangePoints() + this.getPromotionPoints());
            }else{
                System.out.println("No Valid Scheme Id Found.");
            }
        }catch(Exception vep) {
            System.out.println("Exception while calculating the points : "+vep.toString());
            throw vep;
        }
    }

     public void executeRedeem()throws Exception {
        try{
          //  this.getValidScheme();
            if(!this.getSchemeId().trim().equals("")){
                this.setConvertAmount(this.getRedeemAmount());
            }else{
                System.out.println("No Valid Scheme Id Found.");
            }
        }catch(Exception vep) {
            System.out.println("Exception while calculating the convertion amount : "+vep.toString());
            throw vep;
        }
    }

    /** ruleTwo Method */
    //ruleTwo Method will be defined later
    private double getRedeemAmount() throws Exception {
        System.out.println("RULE REDEEM : Calculating the convertion in Redeem Rule");
        double varAwardCash = 0;

        StringBuffer query = new StringBuffer();
        query.append("SELECT MIN_AMOUNT, POINT_AWARD ");
        query.append("FROM POINT_SCHEME_RULE ");
        query.append("WHERE RULE_ID = '02' AND SCHEME_ID = '"+ this.getSchemeId() +"' ");
        query.append("AND ISSUER_ID='"+this.getIssuerId()+"' ");
        System.out.println(query.toString());//HIEP
        objDBManager.executeSQL(query.toString());
        TPlusResultSet rs = objDBManager.getResultSet();

        if(rs.next()) {
            String minAmount =  rs.getString("MIN_AMOUNT");
            String  pointAward = rs.getString("POINT_AWARD");

            DBUtil dbUtil = new DBUtil();
            String sql = "SELECT EXCHANGE_RATIO FROM CARD_CLASS WHERE CLASS_ID = '" + this.getCardClassId() + "'";
            String convertRatio = dbUtil.getFieldValue(sql,"EXCHANGE_RATIO");
            double ratio = Double.parseDouble(convertRatio);
            this.setConvertRatio(ratio);

            varAwardCash += Math.floor((Double.parseDouble(minAmount) * this.getRedeemPoints() * ratio)/100);

        }else{
            System.out.println("There is no valid redeem rule.");
        }
        System.out.println("RULE REDEEM : Awarded Cash ==> "+varAwardCash);
        return varAwardCash;
    }

  /*  public void getValidScheme() throws Exception {

        StringBuffer query = new StringBuffer();
        query.append("SELECT SCHEME_ID FROM SCHEME_MASTER ");
        query.append("WHERE ISSUER_ID='"+this.getIssuerId()+"' ");
        query.append("AND STATUS='G' AND SCHEME_TYPE='"+this.getSchemeType()+"' ");
        query.append("AND CARD_PRODUCT_ID='"+this.getCardProduct()+"' ");
        query.append("AND TO_CHAR(SCHEME_START_DATE, 'YYYYMMDD') <= TO_CHAR(SYSDATE, 'YYYYMMDD') ");
        query.append("AND TO_CHAR(SCHEME_END_DATE, 'YYYYMMDD') >= TO_CHAR(SYSDATE, 'YYYYMMDD')");

        try{

            String merchantGroupId =  Config.getMerchantGroupId(this.getIssuerId(),this.getMerchantNo());
            if(merchantGroupId==null) merchantGroupId = "";

            StringBuffer query1 = new StringBuffer();
            query1.append(query);
            query1.append("AND INSTR(MERCHANT_NO,'"+ merchantGroupId + "') > 0 ");
            System.out.println(query1.toString());//HIEP
            objDBManager.executeSQL(query1.toString());
            TPlusResultSet rs = objDBManager.getResultSet();
            if(rs.next()){
                schemeId = rs.getString("SCHEME_ID");
            }else{
                query.append("AND MERCHANT_NO='ALL' ");
                System.out.println(query.toString());//HIEP
                objDBManager.executeSQL(query.toString());
                rs = objDBManager.getResultSet();
                if(rs.next()){
                    schemeId = rs.getString("SCHEME_ID");
                }
            }
            this.setSchemeId(schemeId);
            System.out.println("Scheme Id : " + schemeId);
        }catch(Exception vep) {
            System.out.println("Exception while getting the valid scheme : "+vep.toString());
            throw vep;
        }
    }



    public void getBirthDayScheme() throws Exception {

        StringBuffer query = new StringBuffer();
        query.append("SELECT SCHEME_ID FROM SCHEME_MASTER ");
        query.append("WHERE ISSUER_ID='"+this.getIssuerId()+"' ");
        query.append("AND STATUS='G' AND SCHEME_TYPE='BS' ");
        query.append("AND CARD_PRODUCT_ID='"+this.getCardProduct()+"' ");
        query.append("AND TO_CHAR(SCHEME_START_DATE, 'YYYYMMDD') <= TO_CHAR(SYSDATE, 'YYYYMMDD') ");
        query.append("AND TO_CHAR(SCHEME_END_DATE, 'YYYYMMDD') >= TO_CHAR(SYSDATE, 'YYYYMMDD')");

        try{

            String merchantGroupId =  Config.getMerchantGroupId(this.getIssuerId(),this.getMerchantNo());
            if(merchantGroupId==null) merchantGroupId = "";

            StringBuffer query1 = new StringBuffer();
            query1.append(query);
            query1.append("AND INSTR(MERCHANT_NO,'"+ merchantGroupId + "') > 0 ");
            System.out.println(query1.toString());//HIEP
            objDBManager.executeSQL(query1.toString());
            TPlusResultSet rs = objDBManager.getResultSet();
            if(rs.next()){
                birthDaySchemeId = rs.getString("SCHEME_ID");
            }else{
                query.append("AND MERCHANT_NO='ALL' ");
                System.out.println(query.toString());//HIEP
                objDBManager.executeSQL(query.toString());
                rs = objDBManager.getResultSet();
                if(rs.next()){
                    birthDaySchemeId = rs.getString("SCHEME_ID");
                }
            }
            this.setBirthDaySchemeId(birthDaySchemeId);
            System.out.println("BirthDay Scheme Id : " + birthDaySchemeId);
        }catch(Exception vep) {
            System.out.println("Exception while getting the BirthDay Scheme : "+vep.toString());
            throw vep;
        }
    }*/


    /**  ruleOne Method  */
    private double getRangePoints() throws Exception {
        System.out.println("RULE RANGES : Calculating the points in Ranges Rule");
        double varAwardPoints = 0;
        String rewardType = "";
        String percentageAward = "";
        String pointAward = "";

        StringBuffer query = new StringBuffer();
        query.append("SELECT REWARD_TYPE, PERCENTAGE_AWARD, POINT_AWARD ");
        query.append("FROM POINT_SCHEME_RULE ");
        query.append("WHERE ((MIN_AMOUNT <=" + this.getTransAmount() +" ) AND (MAX_AMOUNT >=" + this.getTransAmount() +")) ");
        query.append("AND RULE_ID = '01' AND SCHEME_ID = '"+ this.getSchemeId() +"' ");
        query.append("AND ISSUER_ID='"+this.getIssuerId()+"' ");
        System.out.println(query.toString());//HIEP
        objDBManager.executeSQL(query.toString());
        TPlusResultSet rs = objDBManager.getResultSet();

        if(rs.next()) {
            rewardType = rs.getString("REWARD_TYPE");
            percentageAward = rs.getString("PERCENTAGE_AWARD");
            pointAward = rs.getString("POINT_AWARD");
            if(rewardType.trim().equals("PT")) {
                System.out.println("RULE RANGES : Point Award = "+pointAward);
                varAwardPoints += Double.parseDouble(pointAward);
            }else {
                System.out.println("RULE RANGES : Percentage Award = "+percentageAward+"%");
                varAwardPoints += (this.getTransAmount() * Double.parseDouble(percentageAward)) / 100;
            }
        }else{
            System.out.println("There is no valid Range rule.");
        }
        System.out.println("RULE RANGES : Awarded points ==> "+varAwardPoints);
        return varAwardPoints;
    }


    /** ruleTwo Method */
    //ruleTwo Method will be defined later
    private double getBasicPoints() throws Exception {
        System.out.println("RULE BASIC : Calculating the points in Basic Rule");
        double varAwardPoints = 0;

        StringBuffer query = new StringBuffer();
        query.append("SELECT MIN_AMOUNT, POINT_AWARD ");
        query.append("FROM POINT_SCHEME_RULE ");
        query.append("WHERE RULE_ID = '02' AND SCHEME_ID = '"+ this.getSchemeId() +"' ");
        query.append("AND ISSUER_ID='"+this.getIssuerId()+"' ");
        System.out.println(query.toString());//HIEP
        objDBManager.executeSQL(query.toString());
        TPlusResultSet rs = objDBManager.getResultSet();

        if(rs.next()) {
            String minAmount =  rs.getString("MIN_AMOUNT");
            String  pointAward = rs.getString("POINT_AWARD");
            double leftAmount = this.getTransAmount()%Double.parseDouble(minAmount);
            double calculateAmount = this.getTransAmount() - leftAmount;
            System.out.println("RULE BASIC : For Every $ "+minAmount+", Award Points = "+pointAward);
            varAwardPoints += Math.floor(calculateAmount / Double.parseDouble(minAmount)) * Double.parseDouble(pointAward);
            this.setLeftSaleAmount(leftAmount);
        }else{
            System.out.println("There is no valid Basic rule.");
        }
        System.out.println("RULE BASIC : Awarded points ==> "+varAwardPoints);
        return varAwardPoints;
    }


    /**  ruleThree Method  */
    private double getPromotionPoints() throws Exception {
        System.out.println("RULE PROMO : Calculating the points in Promotion Rule");
        double varAwardPoints = 0;
        String rewardType = "";
        String percentageAward = "";
        String pointAward = "";
        String minAmount = "";
        String strMultiplier = "0";
        String strMultiplierMinAmt = "0";
        String gender = "";

        StringBuffer query = new StringBuffer();
        query.append("SELECT REWARD_TYPE, PERCENTAGE_AWARD, POINT_AWARD, MIN_AMOUNT, GENDER, ");
        query.append("MULTIPLIER, MULTIPLIER_MIN_AMT ");
        query.append("FROM POINT_SCHEME_RULE ");
        query.append("WHERE RULE_ID = '03' AND SCHEME_ID ='"+ this.getSchemeId() +"' ");
        query.append("AND TO_CHAR (START_DATE,'YYYYMMDDHH24MISS') <= TO_CHAR (SYSDATE,'YYYYMMDDHH24MISS') ");
        query.append("AND TO_CHAR (END_DATE,'YYYYMMDDHH24MISS') >= TO_CHAR (SYSDATE,'YYYYMMDDHH24MISS') ");
        query.append("AND ISSUER_ID='"+this.getIssuerId()+"' ");
        System.out.println(query.toString());//HIEP
        objDBManager.executeSQL(query.toString());
        TPlusResultSet rs = objDBManager.getResultSet();

        if(rs.next()) {
            rewardType = rs.getString("REWARD_TYPE");
            percentageAward = rs.getString("PERCENTAGE_AWARD");
            pointAward = rs.getString("POINT_AWARD");
            minAmount = rs.getString("MIN_AMOUNT");
            strMultiplier = rs.getString("MULTIPLIER");
            strMultiplierMinAmt = rs.getString("MULTIPLIER_MIN_AMT");
            gender = rs.getString("GENDER");

            if(gender.equals("A") || gender.equals(this.getGender())){
                if(rewardType.trim().equals("PT")) {
                double leftAmount = this.getTransAmount()%Double.parseDouble(minAmount);
                double calculateAmount = this.getTransAmount() - leftAmount;
                System.out.println("RULE PROMO : For Every $ "+minAmount+", Award Points = "+pointAward);
                varAwardPoints += Math.floor(calculateAmount / Double.parseDouble(minAmount)) * Double.parseDouble(pointAward);
                }else {
                    System.out.println("RULE PROMO : Percentage Award = "+percentageAward+"%");
                    varAwardPoints += (this.getTransAmount() * Double.parseDouble(percentageAward)) / 100;
                }
                System.out.println("RULE PROMO : strMultiplier="+strMultiplier+"  strMultiplierMinAmt="+strMultiplierMinAmt);
                if(!strMultiplier.trim().equals("") && !strMultiplierMinAmt.trim().equals("")){
                    double multiplier = Double.parseDouble(strMultiplier);
                    float multiplierMinAmt = Float.parseFloat(strMultiplierMinAmt);
                    if(multiplier > 0 && multiplierMinAmt <= this.getTransAmount()) {
                        varAwardPoints *= multiplier;
                    }
                }
            }

        }else{
            System.out.println("RULE PROMO : There is no valid rule.");
        }
        System.out.println("RULE PROMO : Awarded points ==> "+varAwardPoints);
        return varAwardPoints;
    }


    /**  ruleThree Method  */

    public void getBirthDayPoints(String dateOfBirth)throws Exception {
        System.out.println("RULE BIRTHDAY : Calculating the points in Birthday Rule");
        int varAwardPoints = 0;
        String rewardType = "";
        String percentageAward = "";
        String pointAward = "";
        String strMultiplier = "";
        String strMultiplierMinAmt = "";
        String strOnBirthDatePoints = "";
        String awardFreq = "";
        String minAmount = "";
        boolean awardPoints = false;

        StringBuffer query = new StringBuffer();
        query.append("SELECT BIRTHDAY_AWARD_FREQ, BIRTHDAY_AWARD_TYPE FROM SCHEME_MASTER ");
        query.append("WHERE SCHEME_ID ='"+ this.getBirthDaySchemeId() +"' ");
        query.append("AND ISSUER_ID='"+this.getIssuerId()+"' ");
        System.out.println(query.toString());//HIEP
        objDBManager.executeSQL(query.toString());
        TPlusResultSet rs = objDBManager.getResultSet();

        if(rs.next()){
            awardFreq = rs.getString("BIRTHDAY_AWARD_FREQ");
            String awardType = rs.getString("BIRTHDAY_AWARD_TYPE");
            System.out.println("awardFreq "+awardFreq);
            System.out.println("awardType "+awardType);
            //multiple awarding
            if(awardFreq.trim().equals("M")){
                //award points
                System.out.println("Inside Multi");
                if(dateOfBirth.substring(2,4).equals(DateUtil.getDateValue("MM"))){
                    awardPoints = true;
                }
            }else{
                System.out.println("Inside Only");
                //single awarding
                if(awardType.trim().equals("1")){
                   System.out.println("Inside 1");
                    //check the award log
                    if(dateOfBirth.substring(2,4).equals(DateUtil.getDateValue("MM"))){
                        if(!checkBirthdayAwardLog(this.getIssuerId(), this.getCardNumber())){
                            awardPoints = true;
                        }
                    }
                }else if(awardType.trim().equals("2")){
                    System.out.println("Inside 2");
                    if(dateOfBirth.startsWith(DateUtil.getDateValue("DDMM"))){
                        if(!checkBirthdayAwardLog(this.getIssuerId(), this.getCardNumber())){
                            awardPoints = true;
                        }
                    }
                }
              }
         }

        if(awardPoints){
            query = new StringBuffer();
            query.append("SELECT MIN_AMOUNT, REWARD_TYPE, PERCENTAGE_AWARD, POINT_AWARD, ");
            query.append("MULTIPLIER, MULTIPLIER_MIN_AMT ");
            query.append("FROM POINT_SCHEME_RULE ");
            query.append("WHERE RULE_ID = '04' AND SCHEME_ID ='"+ schemeId +"' ");
            query.append("AND ISSUER_ID='"+this.getIssuerId()+"' ");
            System.out.println(query.toString());//HIEP
            objDBManager.executeSQL(query.toString());
            TPlusResultSet rsb = objDBManager.getResultSet();

            if(rsb.next()) {
                rewardType = rsb.getString("REWARD_TYPE");
                percentageAward = rsb.getString("PERCENTAGE_AWARD");
                pointAward = rsb.getString("POINT_AWARD");
                minAmount = rsb.getString("MIN_AMOUNT");
                strMultiplier = rsb.getString("MULTIPLIER");
                strMultiplierMinAmt = rsb.getString("MULTIPLIER_MIN_AMT");

                if(rewardType.trim().equals("PT")) {
                    double leftAmount = this.getTransAmount()%Double.parseDouble(minAmount);
                    double calculateAmount = this.getTransAmount() - leftAmount;
                    System.out.println("RULE BIRTHDAY : For Every $ "+minAmount+", Award Points = "+pointAward);
                    varAwardPoints += Math.floor(calculateAmount / Double.parseDouble(minAmount)) * Double.parseDouble(pointAward);
                }else {
                    System.out.println("RULE BIRTHDAY : Percentage Award = "+percentageAward+"%");
                    varAwardPoints += (this.getTransAmount() * Double.parseDouble(percentageAward)) / 100;
                }

                System.out.println("RULE BIRTHDAY : strMultiplier="+strMultiplier+"  strMultiplier="+strMultiplierMinAmt);
                if(!strMultiplier.trim().equals("") && !strMultiplierMinAmt.trim().equals("")){
                    double multiplier = Double.parseDouble(strMultiplier);
                    float multiplierMinAmt = Float.parseFloat(strMultiplierMinAmt);
                    if(multiplier > 0 && multiplierMinAmt <= this.getTransAmount()) {
                        varAwardPoints *= multiplier;
                    }
                }
            }
        }
        System.out.println("RULE BIRTHDAY : Awarded points ==> "+varAwardPoints);

        //award frequency is one time, insert to birthday award log
        if(varAwardPoints>0 && awardFreq.trim().equals("O")){
            insertBirthdayAwardLog(varAwardPoints);
        }

        this.setPointsAwarded(this.getPointsAwarded() + varAwardPoints);
    }

    private boolean checkBirthdayAwardLog(String issuerId, String cardNo) throws Exception{
        System.out.println("Before checking the birthday log");
        StringBuffer query = new StringBuffer();
        query.append("SELECT AWARD_DATE FROM BIRTHDAY_POINT_AWARD_LOG ");
        query.append("WHERE ISSUER_ID='"+issuerId+"' ");
        query.append("AND CARD_NO ='"+cardNo+"' ");
        query.append("AND TO_CHAR(AWARD_DATE,'MMYYYY') = TO_CHAR(SYSDATE,'MMYYYY') ");
        System.out.println(query.toString());//HIEP
        objDBManager.executeSQL(query.toString());
        TPlusResultSet rsb = objDBManager.getResultSet();
        if(rsb.next()){
            return true;
        }
        System.out.println("After checking the birthday log");
        return false;
    }


    public boolean checkBirthdayAwardLogByYear (String issuerId, String cardNo) throws Exception{
        System.out.println("Before checking the birthday log by year");
        StringBuffer query = new StringBuffer();
        query.append("SELECT AWARD_DATE FROM BIRTHDAY_POINT_AWARD_LOG ");
        query.append("WHERE ISSUER_ID='"+issuerId+"' ");
        query.append("AND CARD_NO ='"+cardNo+"' ");
        query.append("AND TO_CHAR(AWARD_DATE,'YYYY') = TO_CHAR(SYSDATE,'YYYY') ");
        System.out.println(query.toString());//HIEP
        objDBManager.executeSQL(query.toString());
        TPlusResultSet rsb = objDBManager.getResultSet();
        if(rsb.next()){
            return true;
        }
        System.out.println("After checking the birthday log by year");
        return false;
    }


    private boolean insertBirthdayAwardLog(double awardedPoints) throws Exception{
        System.out.println("Before inserting to the birthday award log");
        StringBuffer strSql = new StringBuffer();
        strSql = new StringBuffer();
        strSql.append("INSERT INTO BIRTHDAY_POINT_AWARD_LOG (ISSUER_ID, CARD_NO, AWARD_DATE, AWARD_POINTS ");
        strSql.append(") VALUES (");
        strSql.append("'"+this.getIssuerId()+"',");
        strSql.append("'"+this.getCardNumber()+"',");
        strSql.append("sysdate,");
        strSql.append("'"+awardedPoints+"')");
        System.out.println(strSql.toString());//HIEP
        objDBManager.addSQL(strSql.toString());

        boolean insert = objDBManager.executeAllSQLs();
        System.out.println("End inserting to the birthday award log");

        if(insert){
            return true;
        }else{
            return false;
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
     * Getter for property schemeType.
     * @return Value of property schemeType.
     */
    public java.lang.String getSchemeType() {
        return schemeType;
    }

    /**
     * Setter for property schemeType.
     * @param schemeType New value of property schemeType.
     */
    public void setSchemeType(java.lang.String schemeType) {
        this.schemeType = schemeType;
    }

    /**
     * Getter for property cardType.
     * @return Value of property cardType.
     */
    public java.lang.String getCardType() {
        return cardType;
    }

    /**
     * Setter for property cardType.
     * @param cardType New value of property cardType.
     */
    public void setCardType(java.lang.String cardType) {
        this.cardType = cardType;
    }

    /**
     * Getter for property schemeId.
     * @return Value of property schemeId.
     */
    public java.lang.String getSchemeId() {
        return schemeId;
    }

    /**
     * Setter for property schemeId.
     * @param schemeId New value of property schemeId.
     */
    public void setSchemeId(java.lang.String schemeId) {
        this.schemeId = schemeId;
    }

    /**
     * Getter for property pointsAwarded.
     * @return Value of property pointsAwarded.
     */
    public double getPointsAwarded() {
        return pointsAwarded;
    }

    /**
     * Setter for property pointsAwarded.
     * @param pointsAwarded New value of property pointsAwarded.
     */
    public void setPointsAwarded(double pointsAwarded) {
        this.pointsAwarded = pointsAwarded;
    }

    /**
     * Getter for property transAmount.
     * @return Value of property transAmount.
     */
    public double getTransAmount() {
        return transAmount;
    }

    /**
     * Setter for property transAmount.
     * @param transAmount New value of property transAmount.
     */
    public void setTransAmount(double transAmount) {
        this.transAmount = transAmount;
    }


  public void setCardNumber(String cardNumber)
  {
    this.cardNumber = cardNumber;
  }


  public String getCardNumber()
  {
    return cardNumber;
  }


  public void setBirthDaySchemeId(String birthDaySchemeId)
  {
    this.birthDaySchemeId = birthDaySchemeId;
  }


  public String getBirthDaySchemeId()
  {
    return birthDaySchemeId;
  }


  public void setLeftSaleAmount(double leftSaleAmount)
  {
    this.leftSaleAmount = leftSaleAmount;
  }


  public double getLeftSaleAmount()
  {
    return leftSaleAmount;
  }


  public void setGender(String gender)
  {
    this.gender = gender;
  }


  public String getGender()
  {
    return gender;
  }


  public void set_issuerId(String issuerId)
  {
    this.issuerId = issuerId;
  }


  public String get_issuerId()
  {
    return issuerId;
  }


  public void setConvertAmount(double convertAmount)
  {
    this.convertAmount = convertAmount;
  }


  public double getConvertAmount()
  {
    return convertAmount;
  }


  public void setRedeemPoints(double redeemPoints)
  {
    this.redeemPoints = redeemPoints;
  }


  public double getRedeemPoints()
  {
    return redeemPoints;
  }




  public void setCardClassId(String cardClassId)
  {
    this.cardClassId = cardClassId;
  }


  public String getCardClassId()
  {
    return cardClassId;
  }


  public void setConvertRatio(double convertRatio)
  {
    this.convertRatio = convertRatio;
  }


  public double getConvertRatio()
  {
    return convertRatio;
  }


  public void setCardProduct(String cardProduct)
  {
    this.cardProduct = cardProduct;
  }


  public String getCardProduct()
  {
    return cardProduct;
  }


  public void setBirthDayAwardSchemeId(String birthDayAwardSchemeId)
  {
    this.birthDayAwardSchemeId = birthDayAwardSchemeId;
  }


  public String getBirthDayAwardSchemeId()
  {
    return birthDayAwardSchemeId;
  }

}
