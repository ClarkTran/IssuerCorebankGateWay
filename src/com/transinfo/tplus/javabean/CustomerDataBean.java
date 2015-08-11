package com.transinfo.tplus.javabean;



public class CustomerDataBean {
    public CustomerDataBean() {
    }

  // Customer data
  /** IssuerId */
   public String strIssuerId = "";
  /** CustSeq */
   public String strCustSeq = "";
  /** OpenDate */
   public String strOpenDate;
  /** Status */
   public String strStatus = "";
   /** LastUpdatedDate */
   public String strLastUpdatedDate;
   /** LastUpdatedBy */
   public String strLastUpdatedBy = "";
   /** DateOfBirth */
   public String dateOfBirth;
   /** Nric_Passport_No */
   public String strCustNRIC   = "";


/** Getters and Setters */

 public void setIssuerId(String issuerId)
 {
     this.strIssuerId = issuerId;
 }

 public String getIssuerId()
 {
     return this.strIssuerId;
 }

public void setCustSeq(String custSeq)
{
    this.strCustSeq = custSeq;
}

public String getCustSeq()
{
    return this.strCustSeq;
}

public void setOpenDate(String openDate)
{
    this.strOpenDate = openDate;
}

public String getOpenDate()
{
    return this.strOpenDate;
}

public void setStatus(String status)
{
    this.strStatus = status;
}

public String getStatus()
{
    return this.strStatus;
}

public void setLastUpdatedDate(String LastUpdatedDate)
{
    this.strLastUpdatedDate = LastUpdatedDate;
}

public String getLastUpdatedDate()
{
    return this.strLastUpdatedDate;
}

public void setLastUpdatedBy(String lastUpdatedBy)
{
    this.strLastUpdatedBy = lastUpdatedBy;
}

public String getLastUpdatedBy()
{
    return this.strLastUpdatedBy;
}

public void setCustNRIC(String custNRIC)
{
    this.strCustNRIC = custNRIC;
}

public String getCustNRIC()
{
    return this.strCustNRIC;
}

/**
 * Setter for property dateOfBirth.
 * @param dateOfBirth New value of property dateOfBirth.
 */
public void setDateOfBirth(java.lang.String dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
}

/**
 * Getter for property dateOfBirth.
 * @return Value of property dateOfBirth.
 */
public java.lang.String getDateOfBirth() {
    return dateOfBirth;
}

}
