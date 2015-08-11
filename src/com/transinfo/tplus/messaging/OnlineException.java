package com.transinfo.tplus.messaging;

public class OnlineException extends RuntimeException {
  String responseCode;
  String reasonCode;
  String description;

  public OnlineException(String responseCode, String reasonCode, String description) {
    this.responseCode = responseCode;
    this.reasonCode = reasonCode;
    this.description = description;
  }

  public OnlineException(OnlineException e)
  {
    this.responseCode = e.getResponseCode();
    this.reasonCode = e.getReasonCode();
    this.description = e.getDescription();
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getReasonCode() {
    return reasonCode;
  }

  public void setReasonCode(String reasonCode) {
    this.reasonCode = reasonCode;
  }

  public String getResponseCode() {
    return responseCode;
  }

  public void setResponseCode(String responseCode) {
    this.responseCode = responseCode;
  }
}
