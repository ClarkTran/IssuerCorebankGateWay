package com.transinfo.tplus.javabean;

import java.io.Serializable;
import java.util.ArrayList;

import com.transinfo.tplus.util.Config;
import com.transinfo.tplus.util.DBManager;

public class CardholderDataBean implements Serializable
{

  private String cardNo="";
  private String cardholderStatus="";
  private String cardholderSeq="";
  private String custType="";
  private String salutation="";
  private String name="";
  private String nameOnCard="";
  private String nricPassportNo="";
  private String dob="";
  private String mpPhoneNo="";
  private String emailAddress="";
  private String maritalStatus="";
  private String maritalStatusOthers="";
  private String nationality="";
  private String gender="";
  private String ethnicGroup="";
  private String ethnicGroupOthers="";
  private String citizen="";
  private String mailingAddress="";
  private AddressDataBean homeAddress = null;
  private AddressDataBean officeAddress = null;
  private ArrayList cardList = new ArrayList();
  private String applicationNo="";
  private String debitCardNumber="";
  private String issuerId="";
  private String branchId="";

  public DBManager objDBManager = null;

  public CardholderDataBean()
  {
    objDBManager = new DBManager();
  }


  public String generateCardHolderSeq() throws Exception {
        String cardholderSeq = "";
        try{
            cardholderSeq = Config.getSeqNumber(this.getIssuerId(),"CUSTOMER_SEQ");
        }catch(Exception vep) {
            System.out.println("Exception while generating the Cardholder Sequency : "+vep.toString());
            throw vep;
        }
        return cardholderSeq;
    }

    public DBManager getInsertSql(String cardholderSeq)throws Exception{
        try{
            this.setCardholderSeq(cardholderSeq);
            return this.getInsertSql();
        }catch(Exception vep) {
            System.out.println("Exception while building the cardholder table query : "+vep.toString());
            throw vep;
        }
    }

    public DBManager getInsertSql() throws Exception {
        StringBuffer strSql = new StringBuffer();
        try{
            strSql.append("INSERT INTO CARDHOLDER (ISSUER_ID, BRANCH_ID, CARDHOLDER_SEQ, SALUTATION, NAME, NAME_ON_CARD, ");
            strSql.append("NRIC_PASSPORT_NO, DOB,  MAILING_ADDRESS, EMAIL_ADDRESS, MOBILE_NO, NATIONALITY, GENDER, ETHNIC_GROUP, ");
            strSql.append("ETHNIC_GROUP_OTHERS, CITIZENSHIP, STATUS, OPEN_DATE, LAST_UPDATED_DATE, LAST_UPDATED_BY ");
            strSql.append(") VALUES (");
            strSql.append("'"+this.getIssuerId()+"',");
            strSql.append("'"+this.getBranchId()+"',");
            strSql.append("'"+this.getCardholderSeq()+"',");
            strSql.append("'2',");
            strSql.append("'"+this.getName()+"',");
            strSql.append("'"+this.getNameOnCard().toUpperCase()+"',");
            strSql.append("'"+this.getNricPassportNo()+"',");
            strSql.append("TO_DATE('"+this.getDob()+"','DDMMYYYY'),");
            strSql.append("'"+this.getMailingAddress()+"',");
            strSql.append("'"+this.getEmailAddress()+"',");
            strSql.append("'"+this.getMpPhoneNo()+"',");
            strSql.append("'VN',");
            strSql.append("'" + this.getGender() + "',");
            strSql.append("'01',");
            strSql.append("'"+this.getEthnicGroupOthers()+"',");
            strSql.append("'01',");
            strSql.append("'G', sysdate, sysdate,'"+Config.LAST_UPDATED_BY+"')");
            System.out.println(strSql.toString());
            objDBManager.addSQL(strSql.toString());

            homeAddress = new AddressDataBean();
            strSql = new StringBuffer();
            strSql.append("INSERT INTO CARDHOLDER_ADDRESS ( ISSUER_ID, CARDHOLDER_SEQ, ADDRESS_TYPE, BLOCK_HOUSE_NO, ");
            strSql.append("UNIT_NO, STREET_ROAD_NAME, ESTATE_NAME, CITY, COUNTRY, POSTAL_CODE, PHONE_NO ");
            strSql.append(") VALUES (");
            strSql.append("'"+this.getIssuerId()+"',");
            strSql.append("'"+this.getCardholderSeq()+"',");
            strSql.append("'HOME',");
            strSql.append("'"+homeAddress.getBlockHouseNo()+"',");
            strSql.append("'"+homeAddress.getUnitNo()+"',");
            strSql.append("'"+homeAddress.getStreetRoadName()+"',");
            strSql.append("'"+homeAddress.getEstateName()+"',");
            strSql.append("'"+homeAddress.getCity()+"',");
            strSql.append("'"+homeAddress.getCountry()+"',");
            strSql.append("'"+homeAddress.getPinCode()+"',");
            strSql.append("'"+homeAddress.getPhoneNumber()+"')");
            System.out.println(strSql.toString());
            objDBManager.addSQL(strSql.toString());

            officeAddress = new AddressDataBean();
            strSql = new StringBuffer();
            strSql.append("INSERT INTO CARDHOLDER_ADDRESS ( ISSUER_ID, CARDHOLDER_SEQ, ADDRESS_TYPE, BLOCK_HOUSE_NO, ");
            strSql.append("UNIT_NO, STREET_ROAD_NAME, ESTATE_NAME, CITY, COUNTRY, POSTAL_CODE, PHONE_NO ");
            strSql.append(") VALUES (");
            strSql.append("'"+this.getIssuerId()+"',");
            strSql.append("'"+this.getCardholderSeq()+"',");
            strSql.append("'OFFICE',");
            strSql.append("'"+officeAddress.getBlockHouseNo()+"',");
            strSql.append("'"+officeAddress.getUnitNo()+"',");
            strSql.append("'"+officeAddress.getStreetRoadName()+"',");
            strSql.append("'"+officeAddress.getEstateName()+"',");
            strSql.append("'"+officeAddress.getCity()+"',");
            strSql.append("'"+officeAddress.getCountry()+"',");
            strSql.append("'"+officeAddress.getPinCode()+"',");
            strSql.append("'"+officeAddress.getPhoneNumber()+"')");
            System.out.println(strSql.toString());
            objDBManager.addSQL(strSql.toString());

            strSql = new StringBuffer();
            strSql.append("UPDATE CARD_CARDHOLDER SET ");
            strSql.append("CARDHOLDER_SEQ= '"+this.getCardholderSeq()+"' ");
            strSql.append("WHERE CARD_NO = '"+this.getCardNo()+"' ");
            strSql.append("AND ISSUER_ID = '"+this.getIssuerId()+"' ");
            System.out.println(strSql.toString());
            objDBManager.addSQL(strSql.toString());

            strSql = new StringBuffer();
            strSql.append("UPDATE CARD SET ");
            strSql.append("NAME_ON_CARD= '"+this.getNameOnCard()+"' ");
            strSql.append("WHERE CARD_NO = '"+this.getCardNo()+"' ");
            strSql.append("AND ISSUER_ID = '"+this.getIssuerId()+"' ");
            System.out.println(strSql.toString());
            objDBManager.addSQL(strSql.toString());

            strSql = new StringBuffer();
            strSql.append("UPDATE LOG_NEW_CARD SET ");
            strSql.append("NAME_ON_CARD= '"+this.getNameOnCard()+"' ");
            strSql.append("WHERE CARD_NO = '"+this.getCardNo()+"' ");
            strSql.append("AND ISSUER_ID = '"+this.getIssuerId()+"' ");
            System.out.println(strSql.toString());
            objDBManager.addSQL(strSql.toString());

        }catch(Exception vep) {
            System.out.println("Exception while building the cardholders table query : "+vep.toString());
            throw vep;
        }
        return objDBManager;
    }


  public void setCardNo(String cardNo)
  {
    this.cardNo = cardNo;
  }


  public String getCardNo()
  {
    return cardNo;
  }


  public void setCardholderStatus(String cardholderStatus)
  {
    this.cardholderStatus = cardholderStatus;
  }


  public String getCardholderStatus()
  {
    return cardholderStatus;
  }


  public void setCardholderSeq(String cardholderSeq)
  {
    this.cardholderSeq = cardholderSeq;
  }


  public String getCardholderSeq()
  {
    return cardholderSeq;
  }


  public void setCustType(String custType)
  {
    this.custType = custType;
  }


  public String getCustType()
  {
    return custType;
  }


  public void setSalutation(String salutation)
  {
    this.salutation = salutation;
  }


  public String getSalutation()
  {
    return salutation;
  }


  public void setName(String name)
  {
    this.name = name;
  }


  public String getName()
  {
    return name;
  }


  public void setNameOnCard(String nameOnCard)
  {
    this.nameOnCard = nameOnCard;
  }


  public String getNameOnCard()
  {
    return nameOnCard;
  }


  public void setNricPassportNo(String nricPassportNo)
  {
    this.nricPassportNo = nricPassportNo;
  }


  public String getNricPassportNo()
  {
    return nricPassportNo;
  }


  public void setDob(String dob)
  {
    this.dob = dob;
  }


  public String getDob()
  {
    return dob;
  }


  public void setMpPhoneNo(String mpPhoneNo)
  {
    this.mpPhoneNo = mpPhoneNo;
  }


  public String getMpPhoneNo()
  {
    return mpPhoneNo;
  }


  public void setEmailAddress(String emailAddress)
  {
    this.emailAddress = emailAddress;
  }


  public String getEmailAddress()
  {
    return emailAddress;
  }


  public void setMaritalStatus(String maritalStatus)
  {
    this.maritalStatus = maritalStatus;
  }


  public String getMaritalStatus()
  {
    return maritalStatus;
  }


  public void setMaritalStatusOthers(String maritalStatusOthers)
  {
    this.maritalStatusOthers = maritalStatusOthers;
  }


  public String getMaritalStatusOthers()
  {
    return maritalStatusOthers;
  }


  public void setNationality(String nationality)
  {
    this.nationality = nationality;
  }


  public String getNationality()
  {
    return nationality;
  }


  public void setGender(String gender)
  {
    this.gender = gender;
  }


  public String getGender()
  {
    return gender;
  }


  public void setEthnicGroup(String ethnicGroup)
  {
    this.ethnicGroup = ethnicGroup;
  }


  public String getEthnicGroup()
  {
    return ethnicGroup;
  }


  public void setEthnicGroupOthers(String ethnicGroupOthers)
  {
    this.ethnicGroupOthers = ethnicGroupOthers;
  }


  public String getEthnicGroupOthers()
  {
    return ethnicGroupOthers;
  }


  public void setCitizen(String citizen)
  {
    this.citizen = citizen;
  }


  public String getCitizen()
  {
    return citizen;
  }


  public void setMailingAddress(String mailingAddress)
  {
    this.mailingAddress = mailingAddress;
  }


  public String getMailingAddress()
  {
    return mailingAddress;
  }


  public void setObjDBManager(DBManager objDBManager)
  {
    this.objDBManager = objDBManager;
  }


  public DBManager getObjDBManager()
  {
    return objDBManager;
  }


  public void setIssuerId(String issuerId)
  {
    this.issuerId = issuerId;
  }


  public String getIssuerId()
  {
    return issuerId;
  }


  public void setApplicationNo(String applicationNo)
  {
    this.applicationNo = applicationNo;
  }

  public String getApplicationNo()
  {
    return applicationNo;
  }


  public void setBranchId(String branchId)
  {
    this.branchId = branchId;
  }


  public String getBranchId()
  {
    return branchId;
  }

}