package com.transinfo.tplus.javabean;

import java.io.Serializable;

import com.transinfo.tplus.util.DBManager;
import com.transinfo.tplus.util.ProcessCode;
import com.transinfo.tplus.util.TPlusResultSet;

public class MarketingMessageDataBean implements Serializable {

    private String issuerId;
    private String cardType = "";
    private String cardProduct = "";
    private String messageType = "";
    private String message = "";
    private String displayType = ""; //hardcoded for now to print on the receipt. REC: print on Receipt, TER: show on terminal

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
     * Getter for property messageType.
     * @return Value of property messageType.
     */
    public java.lang.String getMessageType() {
        return messageType;
    }

    /**
     * Setter for property messageType.
     * @param messageType New value of property messageType.
     */
    public void setMessageType(java.lang.String messageType) {
        this.messageType = messageType;
    }

    /**
     * Getter for property message.
     * @return Value of property message.
     */
    public java.lang.String getMessage() {
        return message;
    }

    /**
     * Setter for property message.
     * @param message New value of property message.
     */
    public void setMessage(java.lang.String message) {
        this.message = message;
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

    private DBManager objDBManager = null;
    private boolean terminalExist = false;
    public MarketingMessageDataBean() {
        objDBManager = new DBManager();
    }

    public MarketingMessageDataBean(CardDataBean objCardDataBean, String processingCode) {
        objDBManager = new DBManager();
        boolean fetch = false;
        try{
            this.setIssuerId(objCardDataBean.getIssuerId());
            this.setCardProduct(objCardDataBean.getCardProduct());

            if(processingCode.trim().equals(ProcessCode.CONTACTLESS_CARD_ACTIVATION)){
                this.setMessageType("SIGNON");
                fetch = true;
            }

            if(processingCode.trim().equals(ProcessCode.CONTACTLESS_SALE_OFFLINE)){
                this.setMessageType("SALE");
                fetch = true;
            }

            if(processingCode.trim().equals(ProcessCode.CONTACTLESS_TOPUP_BY_CASH) || processingCode.trim().equals(ProcessCode.CONTACTLESS_GET_PRE_TOPUP)){
                this.setMessageType("TOPUP");
                fetch = true;
            }

            if(processingCode.trim().equals(ProcessCode.CONTACTLESS_POINT_REDEEM)){
                this.setMessageType("REDEEM");
                fetch = true;
            }

            if(processingCode.trim().equals(ProcessCode.CONTACTLESS_POINT_INQUIRY)
            || processingCode.trim().equals(ProcessCode.CONTACTLESS_CASH_TRANSFER)
            || processingCode.trim().equals(ProcessCode.CONTACTLESS_POINT_TRANSFER)){
                this.setMessageType("DEFAULT");
                fetch = true;
            }

            if(fetch) this.execute();

            if(!this.getMessageType().trim().equals("DEFAULT") && fetch && this.getMessage().trim().equals("")){
                this.setMessageType("DEFAULT");
                this.execute();
            }
        }catch(Exception ex){
            this.setMessage("");
            System.out.println(ex.toString());
        }
    }

    public void execute() throws Exception {
        try{
            StringBuffer query = new StringBuffer();
            query.append("SELECT MESSAGE FROM MARKETING_MESSAGE ");
            query.append("WHERE (CARD_PRODUCT_ID = '"+this.getCardProduct()+"' OR CARD_PRODUCT_ID = 'ALL') ");;
            query.append("AND (MESSAGE_TYPE = '"+this.getMessageType()+"' OR MESSAGE_TYPE = 'ALL') ");
            query.append("AND ISSUER_ID='"+this.getIssuerId()+"'");
            query.append("AND TO_CHAR(START_DATE, 'YYYYMMDD') <= TO_CHAR(SYSDATE, 'YYYYMMDD') ");
            query.append("AND TO_CHAR(END_DATE, 'YYYYMMDD') >= TO_CHAR(SYSDATE, 'YYYYMMDD')   ");
            System.out.println(query.toString());
            objDBManager.executeSQL(query.toString());
            TPlusResultSet rs = objDBManager.getResultSet();
            if(rs.next()) {
                this.setMessage(rs.getString("MESSAGE"));
                System.out.println(this.getMessage());
                this.setMessage(this.getDisplayType() + this.getMessage());
            }
        }catch(Exception vep) {
            System.out.println("Exception while getting marketing message information : "+vep.toString());
            throw vep;
        }
    }

    /**
     * Getter for property displayType.
     * @return Value of property displayType.
     */
    public java.lang.String getDisplayType() {
        return displayType;
    }

    /**
     * Setter for property displayType.
     * @param displayType New value of property displayType.
     */
    public void setDisplayType(java.lang.String displayType) {
        this.displayType = displayType;
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
