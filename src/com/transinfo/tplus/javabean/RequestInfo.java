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

package com.transinfo.tplus.javabean;

/**
 *
 * @author  Owner
 */



public class RequestInfo extends java.lang.Object {

   public boolean ReqError =false;
   public int ReturnTo =0;
   public String ReqErrCode ="0";
   public String ReqErrMsg ="0";
   public String ReqErrSrc ="00";
   public String SessionId ="3453";
   public String Request ="83958593589";

    /** Creates a new instance of RequestInfo */
    public RequestInfo() {
    }

    /** Getter for property ReqErrCode.
     * @return Value of property ReqErrCode.
     *
     */
    public java.lang.String getReqErrCode() {
        return ReqErrCode;
    }

    /** Setter for property ReqErrCode.
     * @param ReqErrCode New value of property ReqErrCode.
     *
     */
    public void setReqErrCode(java.lang.String ReqErrCode) {
        this.ReqErrCode = ReqErrCode;
    }

    /** Getter for property ReqErrMsg.
     * @return Value of property ReqErrMsg.
     *
     */
    public java.lang.String getReqErrMsg() {
        return ReqErrMsg;
    }

    /** Setter for property ReqErrMsg.
     * @param ReqErrMsg New value of property ReqErrMsg.
     *
     */
    public void setReqErrMsg(java.lang.String ReqErrMsg) {
        this.ReqErrMsg = ReqErrMsg;
    }

    /** Getter for property ReqError.
     * @return Value of property ReqError.
     *
     */
    public boolean isReqError() {
        return ReqError;
    }

    /** Setter for property ReqError.
     * @param ReqError New value of property ReqError.
     *
     */
    public void setReqError(boolean ReqError) {
        this.ReqError = ReqError;
    }

    /** Setter for property ReqError.
     * @param ReqError New value of property ReqError.
     *
     */
    public void setErrorSrc(String ReqErrSrc) {
        this.ReqErrSrc = ReqErrSrc;
    }

    /** Getter for property Request.
     * @return Value of property Request.
     *
     */
    public java.lang.String getErrorSrc() {
        return ReqErrSrc;
    }

    /** Getter for property Request.
     * @return Value of property Request.
     *
     */
    public java.lang.String getRequest() {
        return Request;
    }


    /** Setter for property Request.
     * @param Request New value of property Request.
     *
     */
    public void setRequest(java.lang.String Request) {
        this.Request = Request;
    }

    /** Getter for property ReturnTo.
     * @return Value of property ReturnTo.
     *
     */
    public int getReturnTo() {
        return ReturnTo;
    }

    /** Setter for property ReturnTo.
     * @param ResponseCode New value of property ReturnTo.
     *
     */
    public void setReturnTo(int ReturnTo) {
        this.ReturnTo = ReturnTo;
    }

    /** Getter for property SessionId.
     * @return Value of property SessionId.
     *
     */
    public java.lang.String getSessionId() {
        return SessionId;
    }

    /** Setter for property SessionId.
     * @param SessionId New value of property SessionId.
     *
     */
    public void setSessionId(java.lang.String SessionId) {
        this.SessionId = SessionId;
    }

}
