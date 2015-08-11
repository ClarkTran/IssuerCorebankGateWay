package com.transinfo.tplus.util;


//import com.tivn.contactless.online.databean.HSMDataBean;

import java.util.ArrayList;
import java.util.HashMap;

import com.transinfo.tplus.TPlusException;
import com.transinfo.tplus.debug.DebugWriter;


public class DBUtil {

    private DBManager objDBManager = null;
    private String poolName = "";

    /**
     * Dummy constructor
     */
    public DBUtil() {
    }

    /**
     * Constructor for DBUtil.
     * @param poolName  Database properties file name.
     */
    public DBUtil(String poolName){
        this.poolName = poolName;
    }

    /**
     * This method checks if record exists or not.
     * @param sqlStr Sql string.
     * @return Whether record exist or not (true or false).
     */

    public boolean checkExistrecord(String sqlStr){
        boolean flag = false;
        TPlusResultSet objrs=null;
        objDBManager = new DBManager();
        try {
            boolean blnexec = objDBManager.executeSQL(sqlStr.toString());
            objrs = objDBManager.getResultSet();
            if(objrs.next())
                flag = true;
            else
                flag = false;
        }catch(Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * This method returns the column value.
     * @param sqlStr Sql string.
     * @param fieldName  Column name.
     * @return Column value.
     */

    public String getFieldValue(String sqlStr,String fieldName){
        String rtnValue = "";
        TPlusResultSet objrs=null;
        objDBManager = new DBManager();
        try {
            boolean blnexec = objDBManager.executeSQL(sqlStr.toString());
            objrs = objDBManager.getResultSet();

            if(objrs.next())
                rtnValue = objrs.getString(fieldName);

        }catch(Exception e){
            e.printStackTrace();
        }
        return rtnValue;
    }


    /**
     * This method returns the ErorMessage for the given error code
     * @return HashMap
     * @throws Exception
     */
    public HashMap getErrorMessages() throws Exception {
        DBManager objDBManager = new DBManager();
        objDBManager.executeSQL("SELECT ERROR_DESCRIPTION, ERROR_CODE FROM ERROR_MASTER ");
        TPlusResultSet resultSet = objDBManager.getResultSet();
        HashMap hmErrorCodes = new HashMap();
        while (resultSet.next()) {
            hmErrorCodes.put(resultSet.getString("ERROR_CODE"),
            resultSet.getString("ERROR_DESCRIPTION"));
        }
        return hmErrorCodes;
    }

    public ArrayList getCntryList() throws Exception {
        System.out.println("In get cntry list");
        ArrayList arlList = new ArrayList();
        Object [] objArr = null;
        DBManager objDBManager = new DBManager();
        objDBManager.executeSQL(" SELECT CNTRY_CODE, CNTRY_NAME FROM CNTRY_MASTER ORDER BY CNTRY_NAME");
        TPlusResultSet resultSet = objDBManager.getResultSet();
        while (resultSet.next()) {
            objArr = new Object [2];
            objArr [0] = resultSet.getString("CNTRY_CODE");
            objArr [1] = resultSet.getString("CNTRY_NAME");
            arlList.add(objArr);
        }
        System.out.println("End of get cntry list");
        return arlList;
    }

    public ArrayList getCorporateList(String issuerID) throws Exception {
        System.out.println("In get corporate list");
        ArrayList arlList = new ArrayList();
        Object [] objArr = null;
        DBManager objDBManager = new DBManager();
        objDBManager.executeSQL(" SELECT CORPORATE_NO, CORPORATE_NAME FROM CORPORATE WHERE ISSUER_ID ='"+issuerID+"' ORDER BY CORPORATE_NAME");
        TPlusResultSet resultSet = objDBManager.getResultSet();
        while (resultSet.next()) {
            objArr = new Object [2];
            objArr [0] = resultSet.getString("CORPORATE_NO");
            objArr [1] = resultSet.getString("CORPORATE_NAME");
            arlList.add(objArr);
        }
        System.out.println("End of get corporate list");
        return arlList;
    }

    public ArrayList getCardList(String issuerID) throws Exception {
        System.out.println("In get card list");
        ArrayList arlList = new ArrayList();
        Object [] objArr = null;
        DBManager objDBManager = new DBManager();
        objDBManager.executeSQL(" SELECT CARD_TYPE, CARD_TYPE_DESC,CARD_TYPE_FEATURE FROM CARD_TYPE WHERE ISSUER_ID ='"+issuerID+"' ORDER BY CARD_TYPE_DESC");
        TPlusResultSet resultSet = objDBManager.getResultSet();
        while (resultSet.next()) {
            objArr = new Object [3];
            objArr [0] = resultSet.getString("CARD_TYPE");
            objArr [1] = resultSet.getString("CARD_TYPE_DESC");
            objArr [2] = resultSet.getString("CARD_TYPE_FEATURE");
            arlList.add(objArr);
        }
        System.out.println("End of get card list");
        return arlList;
    }

    public ArrayList getCardProductList(String issuerID) throws Exception{
        System.out.println("In get card product list");
        ArrayList arlList = new ArrayList();
        Object [] objArr = null;
        DBManager objDBManager = new DBManager();
        objDBManager.executeSQL(" SELECT CARD_PRODUCT_ID, CARD_TYPE_ID, CARD_PRODUCT_NAME FROM CARD_PRODUCT WHERE ISSUER_ID ='"+issuerID+"' ORDER BY CARD_PRODUCT_NAME");
        TPlusResultSet resultSet = objDBManager.getResultSet();
        while (resultSet.next()) {
            objArr = new Object [3];
            objArr [0] = resultSet.getString("CARD_PRODUCT_ID");
            objArr [1] = resultSet.getString("CARD_TYPE_ID");
            objArr [2] = resultSet.getString("CARD_PRODUCT_NAME");
            arlList.add(objArr);
        }
        System.out.println("End of get card product list");
        return arlList;
    }

    public ArrayList getCardBatchList(String issuerID, String category1, String category2, String category3, String status) throws Exception{
        System.out.println("In get card batch list");
        ArrayList arlList = new ArrayList();
        Object [] objArr = null;
        DBManager objDBManager = new DBManager();
        objDBManager.executeSQL(" SELECT CARD_PRODUCT_ID, REFERENCE_NO, NAME, CATEGORY FROM CARD_PRODUCTION WHERE FLAG = '" + status + "' AND ISSUER_ID ='"+issuerID+"' AND CATEGORY IN ('" + category1 + "','" + category2  + "','"  + category3 + "') ORDER BY REFERENCE_NO");
        TPlusResultSet resultSet = objDBManager.getResultSet();
        while (resultSet.next()) {
            objArr = new Object [4];
            objArr [0] = resultSet.getString("CARD_PRODUCT_ID");
            objArr [1] = resultSet.getString("REFERENCE_NO");
            objArr [2] = resultSet.getString("NAME");
            objArr [3] = resultSet.getString("CATEGORY");
            arlList.add(objArr);
        }
        System.out.println("End of get card batch list");
        return arlList;
    }

public void UpdatePinCnt(String strCardno, String pinStatus,int wrongPinCnt)throws Exception
{

 		StringBuffer query = new StringBuffer();
        query.append("UPDATE CARD SET ");
        query.append("WRONG_PIN_COUNT ='"+wrongPinCnt+"', ");
        query.append("PIN_CHANGE_FLAG ='"+pinStatus+"' ");
        query.append("WHERE CARD_NO = '"+strCardno+"' ");

        System.out.println(query.toString());
        DBManager objDBManager = new DBManager();
        objDBManager.executeSQL(query.toString());



}


/*    public HSMDataBean getHSM() throws Exception {

        HSMDataBean bean = new HSMDataBean();
        StringBuffer strSql = new StringBuffer();
        try{
            DBManager objDBManager = new DBManager();
            strSql = new StringBuffer();
            strSql.append("SELECT HSM_IP, HSM_PORT, MAX_CONN, WEIGHT FROM HSM");
            System.out.println(strSql.toString());
            objDBManager.executeSQL(strSql.toString());
            TPlusResultSet rs = objDBManager.getResultSet();
            if(rs.next()){
                System.out.println("Getting HSM Info ...... ");
                bean.setIp(rs.getString("HSM_IP"));
                bean.setPort(rs.getString("HSM_PORT"));
                bean.setMaxConn(rs.getString("MAX_CONN"));
                bean.setWeight(rs.getString("WEIGHT"));
            }else{
                throw new Exception("<<< NO VALID HSM >>>");
            }
        }catch(Exception vep) {
            System.out.println("Exception while getting HSM Information : "+vep.toString());
            throw vep;
        }
        return bean;
    }*/

    public ArrayList getCouponList(String issuerID, String couponSchemeType) throws Exception {
        System.out.println("In get coupon list");
        ArrayList arlList = new ArrayList();
        Object [] objArr = null;
        DBManager objDBManager = new DBManager();
        String strSql = "SELECT COUPON_ID, COUPON_TEXT FROM COUPON_MASTER WHERE ISSUER_ID ='"+issuerID+"' ";
        strSql += "AND (COUPON_SCHEME_TYPE = '"+couponSchemeType+"' OR COUPON_SCHEME_TYPE = 'ALL') ";
        strSql += "ORDER BY COUPON_TEXT";
        objDBManager.executeSQL(strSql);
        TPlusResultSet resultSet = objDBManager.getResultSet();
        while (resultSet.next()) {
            objArr = new Object [2];
            objArr [0] = resultSet.getString("COUPON_ID");
            objArr [1] = resultSet.getString("COUPON_TEXT");
            arlList.add(objArr);
        }
        System.out.println("End of get coupon list");
        return arlList;
    }

    public ArrayList getArlAvailableCouponList(String strCardNo, String strIssuerID) throws Exception {
        ArrayList arlList = new ArrayList();
        Object[] objArr = null;
        String strSql = "SELECT COUPON_ID, COUPON_TEXT, COUPON_SCHEME_TYPE FROM COUPON_MASTER WHERE ISSUER_ID ='"+strIssuerID+"' ";
        strSql += "AND COUPON_SCHEME_TYPE IN ( 'BSC' ,'MSC') ";
        /*(COUPON_SCHEME_TYPE = 'BSC' OR COUPON_SCHEME_TYPE = 'MSC')
         strSql += "MINUS (";
        strSql += "SELECT CCP.COUPON_ID, CM.COUPON_TEXT, CM.COUPON_SCHEME_TYPE ";
        strSql += "FROM CARD_COUPON_PURSE CCP, COUPON_MASTER CM ";
        strSql += "WHERE CCP.CARD_NO='" +strCardNo+ "' ";
        strSql += "AND CCP.ISSUER_ID ='"+strIssuerID+"' ";
        strSql += "AND CCP.ISSUER_ID = CM.ISSUER_ID ";
        strSql += "AND CCP.COUPON_ID = CM.COUPON_ID ";
        strSql += ")";*/
        strSql += "ORDER BY COUPON_TEXT";
        System.out.println(strSql);
        DBManager objDBManager = new DBManager();
        objDBManager.executeSQL(strSql);
        TPlusResultSet rs = objDBManager.getResultSet();
        while(rs.next()){
            objArr = new Object[3];
            objArr[0] = rs.getString("COUPON_ID");
            objArr[1] = rs.getString("COUPON_TEXT");
            objArr[2] = rs.getString("COUPON_SCHEME_TYPE");
            arlList.add(objArr);
        }
        return arlList;
    }

    public ArrayList getCustTypeList(String issuerID) throws Exception {
        System.out.println("In get customer list");
        ArrayList arlList = new ArrayList();
        Object [] objArr = null;
        DBManager objDBManager = new DBManager();
        objDBManager.executeSQL(" SELECT CUST_TYPE, CUST_TYPE_DESCRIPTION FROM CUSTOMER_TYPE WHERE ISSUER_ID ='"+issuerID+"' ORDER BY CUST_TYPE_DESCRIPTION");
        TPlusResultSet resultSet = objDBManager.getResultSet();
        while (resultSet.next()) {
            objArr = new Object [2];
            objArr [0] = resultSet.getString("CUST_TYPE");
            objArr [1] = resultSet.getString("CUST_TYPE_DESCRIPTION");
            arlList.add(objArr);
        }
        System.out.println("End of get customer list");
        return arlList;
    }

    public ArrayList getTerminalList(String issuerID, String merchantNo) throws Exception {
        System.out.println("In get terminal list");
        ArrayList arlList = new ArrayList();
        Object [] objArr = null;
        DBManager objDBManager = new DBManager();
        String executeSql = "SELECT TERMINAL_NO, TERMINAL_NAME FROM TERMINAL WHERE ISSUER_ID ='"+issuerID+"' ";
        executeSql += "AND MERCHANT_NO ='"+merchantNo+"' ORDER BY TERMINAL_NAME ";
        objDBManager.executeSQL(executeSql);
        TPlusResultSet resultSet = objDBManager.getResultSet();
        while (resultSet.next()) {
            objArr = new Object [2];
            objArr [0] = resultSet.getString("TERMINAL_NO");
            objArr [1] = resultSet.getString("TERMINAL_NAME");
            arlList.add(objArr);
        }
        System.out.println("End of get terminal list");
        return arlList;
    }


    public String getSaleMarketingMessage (String issuerId) throws Exception {

        String messages = "";
        String sSQL = "SELECT REFERENCE_NO, CARD_PRODUCT_ID, TO_CHAR(START_DATE, 'DD/MM/YYYY') AS START_DATE, TO_CHAR(END_DATE, 'DD/MM/YYYY') AS END_DATE, MESSAGE FROM MARKETING_MESSAGE WHERE MESSAGE_TYPE = 'SALE' AND ISSUER_ID = '" + issuerId + "' ";

        System.out.println("In getSaleMarketingMessage");

        DBManager objDBManager = new DBManager();
        objDBManager.executeSQL(sSQL);
        TPlusResultSet resultSet = objDBManager.getResultSet();
        while (resultSet.next()) {

            String refNo = resultSet.getString("REFERENCE_NO");
            String cardProductId = resultSet.getString("CARD_PRODUCT_ID");
            String startDate = resultSet.getString("START_DATE");
            String endDate = resultSet.getString("END_DATE");
            String message = resultSet.getString("MESSAGE");

            messages = messages + refNo + "-" + cardProductId + "-" + startDate + "-" + endDate + "-" + message + "#" ;

        }
        System.out.println("End getSaleMarketingMessage");
        return messages;
    }


    public ArrayList getMerchantList(String issuerID) throws Exception {
        System.out.println("In get merchant list");
        ArrayList arlList = new ArrayList();
        Object [] objArr = null;
        DBManager objDBManager = new DBManager();
        objDBManager.executeSQL(" SELECT MERCHANT_NO, MERCHANT_NAME FROM MERCHANT WHERE ISSUER_ID ='"+issuerID+"' ORDER BY MERCHANT_NAME ");
        TPlusResultSet resultSet = objDBManager.getResultSet();
        while (resultSet.next()) {
            objArr = new Object [2];
            objArr [0] = resultSet.getString("MERCHANT_NO");
            objArr [1] = resultSet.getString("MERCHANT_NAME");
            arlList.add(objArr);
        }
        System.out.println("End of get merchant list");
        return arlList;
    }

    public String getMerchantName(String issuerID, String merchantNo) throws Exception {
        System.out.println("In get merchant name");
        String rtnStrValue = "";
        DBManager objDBManager = new DBManager();
        String query = "SELECT MERCHANT_NAME FROM MERCHANT ";
        query += "WHERE ISSUER_ID ='"+issuerID+"' AND MERCHANT_NO ='"+merchantNo+"'";
        objDBManager.executeSQL(query);
        TPlusResultSet resultSet = objDBManager.getResultSet();
        while (resultSet.next()) {
            rtnStrValue = resultSet.getString("MERCHANT_NAME");
        }
        System.out.println("End of get merchant name");
        return rtnStrValue;
    }

    public ArrayList getClubList(String issuerID) throws Exception {
        System.out.println("In get club list");
        ArrayList arlList = new ArrayList();
        Object [] objArr = null;
        DBManager objDBManager = new DBManager();
        objDBManager.executeSQL(" SELECT CLUB_ID, CLUB_NAME FROM CLUB_MASTER WHERE ISSUER_ID ='"+issuerID+"' ORDER BY CLUB_ID");
        TPlusResultSet resultSet = objDBManager.getResultSet();
        while (resultSet.next()) {
            objArr = new Object [2];
            objArr [0] = resultSet.getString("CLUB_ID");
            objArr [1] = resultSet.getString("CLUB_NAME");
            arlList.add(objArr);
        }
        System.out.println("End of get club list");
        return arlList;
    }

    public ArrayList getCodeList(String codeType, String groupId) throws Exception {
        ArrayList arlList = new ArrayList();
        Object [] objArr = null;
        DBManager objDBManager = new DBManager();
        objDBManager.executeSQL("SELECT  CODE_ID, CODE_DESCRIPTION FROM CODE_MASTER WHERE GROUP_ID ='"+groupId+"' AND CODE_TYPE='"+codeType+"' ORDER BY CODE_ORDER");
        TPlusResultSet resultSet = objDBManager.getResultSet();
        while (resultSet.next()) {
            objArr = new Object [2];
            objArr [0] = resultSet.getString("CODE_ID");
            objArr [1] = resultSet.getString("CODE_DESCRIPTION");
            arlList.add(objArr);
        }
        return arlList;
    }

    public ArrayList getAllCardCustomers(String issuerId) throws Exception {
        ArrayList arlList = new ArrayList();
        Object [] objArr = null;
        DBManager objDBManager = new DBManager();
        StringBuffer strSql = new StringBuffer();
        strSql.append(" SELECT CA.CARD_NO, CA.CUST_SEQ, UPPER(CA.NAME_ON_CARD), CD.NRIC_PASSPORT_NO, UPPER(CD.NAME) ");
        strSql.append(" FROM CARD CA, CUSTOMER CU, CUSTOMER_DATA CD  ");
        strSql.append(" WHERE CA.ISSUER_ID='"+issuerId+"' ");
        strSql.append(" AND CA.CUST_SEQ = CU.CUST_SEQ AND CU.CUST_SEQ = CD.CUST_SEQ ");
        strSql.append(" ORDER BY CA.CARD_NO ");
        System.out.println(strSql.toString());
        objDBManager.executeSQL(strSql.toString());
        TPlusResultSet resultSet = objDBManager.getResultSet();
        while (resultSet.next()) {
            objArr = new Object [4];
            objArr [0] = resultSet.getString("CARD_NO");
            objArr [1] = resultSet.getString("NAME");
            objArr [2] = resultSet.getString("NRIC_PASSPORT_NO");
            objArr [3] = resultSet.getString("NAME_ON_CARD");
            arlList.add(objArr);
        }
        return arlList;
    }

    // returns next value of the sequence from the SEQUENCES table.
    public String getSeqNumber(String issuerId, String sequenceName) throws Exception {
        String nextSeq = "";
        try {
            DBManager objDBManager = new DBManager();
            StringBuffer sbfQuery = new StringBuffer();
            String sSQL = "SELECT GET_SEQUENCE_NUMBER ('"+ issuerId +"','"+ sequenceName +"') AS SEQVAL FROM DUAL ";

            objDBManager.executeSQL(sSQL);
            TPlusResultSet ResultSet = objDBManager.getResultSet() ;
            if(ResultSet.next()) {
                nextSeq = ResultSet.getString("SEQVAL");
            }
        /*}catch(SQLException dbe){
            throw dbe;*/
        }catch(Exception e){
            throw e;
        }
        return nextSeq;
    }

    public ArrayList getIssuerList() throws Exception {
        ArrayList arlList = new ArrayList();
        Object [] objArr = null;
        DBManager objDBManager = new DBManager();
        objDBManager.executeSQL("SELECT  ISSUER_ID, ISSUER_NAME FROM ISSUER_MASTER WHERE ISSUER_STATUS NOT IN ('ASP','I','S','C')");
        TPlusResultSet resultSet = objDBManager.getResultSet();
        while (resultSet.next()) {
            objArr = new Object [2];
            objArr [0] = resultSet.getString("ISSUER_ID");
            objArr [1] = resultSet.getString("ISSUER_NAME");
            arlList.add(objArr);
        }
        return arlList;
    }

    public ArrayList getAvailableFunctions(String strRoleId, String strUserType, String strIssuerId) throws Exception {
        ArrayList arlList = new ArrayList();
        Object [] objArr = null;
        StringBuffer sbfStr = new StringBuffer();
        DBManager objDBManager = new DBManager();
        sbfStr.append(" SELECT FUNCTION_ID, FUNCTION_DESCRIPTION FROM ( ");
        sbfStr.append(" SELECT FSM.FUNCTION_ID, FSM.FUNCTION_DESCRIPTION ");
        sbfStr.append(" FROM FUNCTION_SET_MASTER FSM, PERMISSION_MATRIX_FUNCTIONS PMF, USER_LEVEL UL, ISSUER_SCREEN_SET ISS ");
        sbfStr.append(" WHERE FSM.SCREEN_ID = PMF.SCREEN_ACCESSIBLE AND UL.USER_TYPE = PMF.USER_TYPE AND UL.USER_TYPE='"+strUserType+"' ");
        sbfStr.append(" AND FSM.SCREEN_ID = ISS.SCREEN_ID AND ISS.ISSUER_ID = '"+strIssuerId+"' ");
        sbfStr.append(" MINUS ");
        sbfStr.append(" SELECT FSM.FUNCTION_ID, FSM.FUNCTION_DESCRIPTION   ");
        sbfStr.append(" FROM ROLE_FUNCTION_SET RFS, ROLE_MASTER RM, FUNCTION_SET_MASTER FSM, ISSUER_SCREEN_SET ISS ");
        sbfStr.append(" WHERE RM.ROLE_ID = RFS.ROLE_ID AND  RFS.FUNCTION_ID = FSM.FUNCTION_ID AND FSM.SCREEN_ID = ISS.SCREEN_ID ");
        sbfStr.append(" AND RM.ROLE_ID = '"+strRoleId+"' AND RM.ISSUER_ID = '"+strIssuerId+"')         ");

        objDBManager.executeSQL(sbfStr.toString());
        System.out.println(sbfStr.toString());
        TPlusResultSet resultSet = objDBManager.getResultSet();
        while (resultSet.next()) {
            objArr = new Object [2];
            objArr [0] = resultSet.getString("FUNCTION_ID");
            objArr [1] = resultSet.getString("FUNCTION_DESCRIPTION");
            arlList.add(objArr);
        }
        return arlList;
    }

    public String getMaxTopupAmount(String issuerId) throws Exception {

        StringBuffer strSql = new StringBuffer();
        String maxTopupAmount = null;
        try{
            DBManager objDBManager = new DBManager();
            strSql = new StringBuffer();
            strSql.append("SELECT PARAM_VALUE FROM CONFIG_MASTER WHERE PARAM_NAME='MAX_TOPUP_AMOUNT' AND PARAM_TYPE='GENE_PARAM' AND ISSUER_ID='" + issuerId + "'");
            //System.out.println(strSql.toString());
            objDBManager.executeSQL(strSql.toString());
            TPlusResultSet rs = objDBManager.getResultSet();
            if(rs.next()){
                maxTopupAmount =  rs.getString("PARAM_VALUE");
            }else{
                throw new Exception("<<< NO VALID MAX TOPUP AMOUNT FOR UNNAMED CARD  >>>");
            }
        }catch(Exception vep) {
            System.out.println("Exception while getting Max Topup Amount Information : "+vep.toString());
            throw vep;
        }
        return maxTopupAmount;
    }

    public ArrayList getSeletedFunctions(String strRoleId, String strUserType, String strIssuerId) throws Exception {
        ArrayList arlList = new ArrayList();
        Object [] objArr = null;
        StringBuffer sbfStr = new StringBuffer();
        DBManager objDBManager = new DBManager();
        sbfStr.append(" SELECT FSM.FUNCTION_ID,FSM.FUNCTION_DESCRIPTION ");
        sbfStr.append(" FROM ROLE_MASTER RM, ROLE_FUNCTION_SET RFS, FUNCTION_SET_MASTER FSM ");
        sbfStr.append(" WHERE RM.ROLE_ID = RFS.ROLE_ID AND RFS.FUNCTION_ID = FSM.FUNCTION_ID ");
        sbfStr.append(" AND RM.ROLE_ID='"+strRoleId+"' AND RM.ISSUER_ID='"+strIssuerId+"' ");
        sbfStr.append(" AND RM.ISSUER_ID = RFS.ISSUER_ID ");
        objDBManager.executeSQL(sbfStr.toString());
        System.out.println(sbfStr.toString());
        TPlusResultSet resultSet = objDBManager.getResultSet();
        while (resultSet.next()) {
            objArr = new Object [2];
            objArr [0] = resultSet.getString("FUNCTION_ID");
            objArr [1] = resultSet.getString("FUNCTION_DESCRIPTION");
            arlList.add(objArr);
        }
        return arlList;
    }

    public ArrayList getAvailableScreens(String strIssuerId) throws Exception {
        ArrayList arlList = new ArrayList();
        Object [] objArr = null;
        StringBuffer sbfStr = new StringBuffer();
        DBManager objDBManager = new DBManager();
        sbfStr.append(" SELECT SCREEN_ID, SCREEN_NAME FROM SCREEN_MASTER WHERE SCREEN_ID IN ");
        sbfStr.append(" (SELECT SCREEN_ID FROM SCREEN_MASTER WHERE SCREEN_TYPE IN('ISSUER','BOTH') ");
        sbfStr.append(" MINUS  ");
        sbfStr.append(" SELECT SCREEN_ID FROM ISSUER_SCREEN_SET WHERE ISSUER_ID = '"+strIssuerId+"') ");
        sbfStr.append(" ORDER BY SCREEN_ORDER ");
        objDBManager.executeSQL(sbfStr.toString());
        System.out.println(sbfStr.toString());
        TPlusResultSet resultSet = objDBManager.getResultSet();
        while (resultSet.next()) {
            objArr = new Object [2];
            objArr [0] = resultSet.getString("SCREEN_ID");
            objArr [1] = resultSet.getString("SCREEN_NAME");
            arlList.add(objArr);
        }
        return arlList;
    }

    public ArrayList getSeletedScreens(String strIssuerId) throws Exception {
        ArrayList arlList = new ArrayList();
        Object [] objArr = null;
        StringBuffer sbfStr = new StringBuffer();
        DBManager objDBManager = new DBManager();
        sbfStr.append("SELECT ISS.SCREEN_ID, SM.SCREEN_NAME ");
        sbfStr.append("FROM ISSUER_SCREEN_SET ISS, SCREEN_MASTER SM ");
        sbfStr.append("WHERE ISS.ISSUER_ID='"+strIssuerId+"' ");
        sbfStr.append("AND SM.SCREEN_ID = ISS.SCREEN_ID ORDER BY SCREEN_ORDER");
        objDBManager.executeSQL(sbfStr.toString());
        System.out.println(sbfStr.toString());
        TPlusResultSet resultSet = objDBManager.getResultSet();
        while (resultSet.next()) {
            objArr = new Object [2];
            objArr [0] = resultSet.getString("SCREEN_ID");
            objArr [1] = resultSet.getString("SCREEN_NAME");
            arlList.add(objArr);
        }
        return arlList;
    }

    public ArrayList getRoles(String strUserType, String strIssuerId) throws Exception {
        ArrayList arlList = new ArrayList();
        Object [] objArr = null;
        StringBuffer sbfStr = new StringBuffer();
        DBManager objDBManager = new DBManager();
        sbfStr.append(" SELECT ROLE_ID, ROLE_DESCRIPTION FROM ROLE_MASTER ");
        sbfStr.append(" WHERE ISSUER_ID = '"+strIssuerId+"' AND USER_TYPE = '"+strUserType+"' ");
        objDBManager.executeSQL(sbfStr.toString());
        System.out.println(sbfStr.toString());
        TPlusResultSet resultSet = objDBManager.getResultSet();
        while (resultSet.next()) {
            objArr = new Object [2];
            objArr [0] = resultSet.getString("ROLE_ID");
            objArr [1] = resultSet.getString("ROLE_DESCRIPTION");
            arlList.add(objArr);
        }
        return arlList;
    }


    public String getIssuerName(String issuerID) throws Exception {
        System.out.println("In get issuer name");
        String rtnStrValue = "";
        DBManager objDBManager = new DBManager();
        String query = "SELECT ISSUER_NAME FROM ISSUER_MASTER ";
        query += "WHERE ISSUER_ID ='"+issuerID+"'";
        objDBManager.executeSQL(query);
        TPlusResultSet resultSet = objDBManager.getResultSet();
        while (resultSet.next()) {
            rtnStrValue = resultSet.getString("ISSUER_NAME");
        }
        System.out.println("End of get issuer name");
        return rtnStrValue;
    }

    public String getConfigParameter(String paramType,String paramName) throws Exception {
        String rtnValue = "";
        try {
            DBManager objDBManager = new DBManager();
            StringBuffer sbfQuery = new StringBuffer();
            sbfQuery.append(" SELECT PARAM_VALUE ") ;
            sbfQuery.append(" FROM  CONFIG_MASTER ") ;
            sbfQuery.append(" WHERE PARAM_NAME = '"+paramName+"' AND PARAM_TYPE = '"+paramType+"' ");
            System.out.println(sbfQuery.toString());
            objDBManager.executeSQL(sbfQuery.toString());
            TPlusResultSet admResultSet = objDBManager.getResultSet() ;
            if(admResultSet.next()) {
                rtnValue = admResultSet.getString("PARAM_VALUE");
            }
       /* }catch(SQLException dbe){
            throw dbe;*/
        }catch(Exception e){
            throw e;
        }
        return rtnValue;
    }

    public String getTodayDate() {
        String strtodayDate = getFieldValue("SELECT TO_CHAR(SYSDATE,'DD/MM/YYYY') AS TODAYDATE FROM DUAL","TODAYDATE");

        return strtodayDate;
    }

    public String getSeqNextValue(String sequenceName) throws Exception {	// returns next value of the sequence
        String nextSeq = "";
        try {
            DBManager objDBManager = new DBManager();
            StringBuffer sbfQuery = new StringBuffer();
            String sSQL = "SELECT " + sequenceName.trim().toUpperCase() + ".NEXTVAL AS SEQVAL FROM DUAL ";

            objDBManager.executeSQL(sSQL);
            TPlusResultSet ResultSet = objDBManager.getResultSet() ;
            if(ResultSet.next()) {
                nextSeq = ResultSet.getString("SEQVAL");
            }
        /*}catch(SQLException dbe){
            throw dbe;*/
        }catch(Exception e){
            throw e;
        }
        return nextSeq;
    }

    public ArrayList getCriteriaList(String issuer_id) throws Exception {
        ArrayList arlList = new ArrayList();
        Object [] objArr = null;
        DBManager objDBManager = new DBManager();
        objDBManager.executeSQL("SELECT CRI_REF_NO, CRI_NAME FROM EMAIL_CRITERIA WHERE ISSUER_ID='"+issuer_id+"' AND CRI_STATUS ='G' ORDER BY CRI_NAME");
        TPlusResultSet resultSet = objDBManager.getResultSet();
        while (resultSet.next()) {
            objArr = new Object [2];
            objArr [0] = resultSet.getString("CRI_REF_NO");
            objArr [1] = resultSet.getString("CRI_NAME");
            arlList.add(objArr);
        }
        return arlList;
    }

    public ArrayList getIssuerProgramList(String issuer_id) throws Exception {
        System.out.println("In get Issuer Program list");
        ArrayList arlList = new ArrayList();
        Object [] objArr = null;
        DBManager objDBManager = new DBManager();
        StringBuffer strSql = new StringBuffer();
        strSql.append("SELECT IP.PROGRAM_CODE, CM.CODE_DESCRIPTION FROM ISSUER_PROGRAM IP, CODE_MASTER CM ");
        strSql.append("WHERE CM.CODE_TYPE='COMMON' AND CM.GROUP_ID='ISSUER_PROGRAM'  ");
        strSql.append("AND CM.CODE_ID = IP.PROGRAM_CODE AND IP.ISSUER_ID='"+issuer_id+"' ORDER BY CM.CODE_ORDER ");
        objDBManager.executeSQL(strSql.toString());
        TPlusResultSet resultSet = objDBManager.getResultSet();
        while (resultSet.next()) {
            objArr = new Object [2];
            objArr [0] = resultSet.getString("PROGRAM_CODE");
            objArr [1] = resultSet.getString("CODE_DESCRIPTION");
            arlList.add(objArr);
        }
        System.out.println("End of get Issuer Program list");
        return arlList;
    }

    public boolean isIssuerProgram(String issuer_id, String programName){
        String rtnValue = "";
        TPlusResultSet objrs=null;
        objDBManager = new DBManager();
        try {
            String sqlStr = "SELECT PROGRAM_CODE FROM ISSUER_MASTER WHERE ISSUER_ID='"+issuer_id+"' ";
            sqlStr += "AND PROGRAM_CODE = '"+programName+"'";
            boolean blnexec = objDBManager.executeSQL(sqlStr);
            objrs = objDBManager.getResultSet();
            if(objrs.next()) return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public synchronized String getCardNumber(String issuerId, String cardType) throws Exception {
        String cardNumber = "";
        StringBuffer strSql = new StringBuffer();
        try{
            DBManager objDBManager = new DBManager();
            strSql = new StringBuffer();
            strSql.append("SELECT CARD_TYPE || LPAD(TO_CHAR(TO_NUMBER(CARD_NEXT_SERIAL_NO)), CARD_LENGTH-4, 0) AS CARD_NUMBER FROM CARD_TYPE ");
            strSql.append("WHERE CARD_TYPE='"+cardType+"' and ISSUER_ID='"+issuerId+"'");
            System.out.println(strSql.toString());
            objDBManager.executeSQL(strSql.toString());
            TPlusResultSet rs = objDBManager.getResultSet();
            if(rs.next()){
                System.out.println("Getting card number ...... ");
                cardNumber = rs.getString("CARD_NUMBER");
                System.out.print("------ "+cardNumber);
                strSql = new StringBuffer();
                strSql.append("UPDATE CARD_TYPE SET CARD_NEXT_SERIAL_NO = TO_NUMBER(CARD_NEXT_SERIAL_NO)+1 ");
                strSql.append("WHERE CARD_TYPE='"+cardType+"' AND ISSUER_ID='"+issuerId+"'");
                System.out.println(strSql.toString());
                int update = objDBManager.executeUpdate(strSql.toString());
            }else{
                throw new Exception("<<< NO VALID CARD TYPE RECORD TO GENERATE CARD NUMBER >>>");
            }
        }catch(Exception vep) {
            System.out.println("Exception while generating card number : "+vep.toString());
            throw vep;
        }
        System.out.println("Generated Card Number : "+cardNumber);
        return cardNumber;
    }



 /**
	* This method is used to check the transaction is available in TranxLog.
	* @param strTerminalId,strCardNumber,strRefNo,ApprovalCode
	* @returns boolean (True,Flase)
	* @throws TPlusException
 	*/


	public boolean getPinExistingTranx(String strTerminalID, String strCardNumber, String strTraceNo)throws Exception
	{

		if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:getExistingTranx");

		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		boolean recAvailable = false;


		try
		{

				StringBuffer sbfDTDVal = new StringBuffer();
				sbfDTDVal.append("SELECT * FROM PINACTIVITY_TRANXLOG WHERE CARD_NO='" + strCardNumber + "' ");
				/*if(strCardNumber != null && !strCardNumber.equals(""))
				{
					sbfDTDVal.append("AND CARD_NO='" + strCardNumber + "' ");

				}*/

				sbfDTDVal.append(" AND TRACE_NO='" + strTraceNo + "' AND RESPONSE_CODE='00'"+ " AND STATUS='0'");


				if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
				System.out.println(" SQL="+ sbfDTDVal.toString());
				bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
				objRs = objDBMan.getResultSet();

				if (objRs.next())
				{
					if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:Transaction Exists");

					recAvailable = true;

				}


		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
		} // End of the Main Try Block

		return recAvailable;
   }



public boolean updatePinActivityTranx(String strTerminalID, String strCardNumber, String strTraceNo)throws Exception
	{

		if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:updatePinActivityTranx");

		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		boolean recAvailable = false;


		try
		{

				StringBuffer sbfDTDVal = new StringBuffer();
				sbfDTDVal.append("UPDATE PINACTIVITY_TRANXLOG SET STATUS=1 WHERE CARD_NO='" + strCardNumber + "' ");
				/*if(strCardNumber != null && !strCardNumber.equals(""))
				{
					sbfDTDVal.append("AND CARD_NO='" + strCardNumber + "' ");

				}*/

				sbfDTDVal.append(" AND TRACE_NO='" + strTraceNo + "' AND RESPONSE_CODE='00'"+ " AND STATUS='0'");


				if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
				System.out.println(" SQL="+ sbfDTDVal.toString());
				int i = objDBMan.executeUpdate(sbfDTDVal.toString());

				if (i>0)
				{
					if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:PIN Activate Transaction Updated");

					recAvailable = true;

				}


		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
		} // End of the Main Try Block

		return recAvailable;
   }


public boolean updateCardPvv(String strCardNumber,String oldPvv,String strProcessingCode)throws Exception
	{

		if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:updateCardPvv");

		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		boolean recAvailable = false;

		try
		{

				StringBuffer sbfDTDVal = new StringBuffer();
				sbfDTDVal.append("UPDATE CARD SET PVV='"+oldPvv+"' ");

				if(strProcessingCode.startsWith("28"))
				{
					sbfDTDVal.append(",PIN_CHANGE_FLAG='N'");
				}

				sbfDTDVal.append(" WHERE CARD_NO='" + strCardNumber + "' ");

				if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
				System.out.println(" SQL="+ sbfDTDVal.toString());
				int i = objDBMan.executeUpdate(sbfDTDVal.toString());

				if (i>0)
				{
					if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:CARD PVV Updated");
					recAvailable = true;

				}


		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
		} // End of the Main Try Block

		return recAvailable;
   }



    /**
     * This method is check the customer temporary Limits
     * @param cardnumber cardproductid custtypeid mcc amount currency issuerid
     * @returns double
     * @throws TPlusException
     */

    public boolean withdrawRulesLimitCheck(String CardNumber, String CardProductID, String CustTypeID, String TranxCode, double Amount, String CURRCODE,String IssuerId)throws Exception {

        if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:withdrawRulesLimitCheck"+TranxCode);

        DBManager objDBMan = new DBManager();
        TPlusResultSet objRs = null;
        boolean bolExecute=false;
        int AccumCount;
        double AccumAmount;
        boolean bApplicable;

        try {
            StringBuffer sbfDTDVal = new StringBuffer();
            sbfDTDVal.append("SELECT AmountPerTranx, DAILYLIMITCOUNT, DAILYLIMITAMOUNT, MONTHLYLIMITCOUNT, MONTHLYLIMITAMOUNT, CURR_CODE, PRODUCT_ID,TYPE_ID, TRANX_CODE FROM WithdrawalLimitRules WHERE TRANX_CODE = '" + TranxCode + "'");
            if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
            System.out.println(" SQL="+ sbfDTDVal.toString());
            bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
            objRs = objDBMan.getResultSet();

            while (objRs.next())
            {

	             bApplicable = true;
                if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:WithdrawalLimits Exists");
                System.out.println("1" +bApplicable+"  "+Amount+"  "+CURRCODE+"   "+objRs.getDouble("AMOUNTPERTRANX")+"  "+IssuerId+"  "+objRs.getString("CURR_CODE"));
                if (bApplicable)
                {

						// Check Amount/Tranx

						if (objRs.getDouble("AMOUNTPERTRANX") > 0)
						{
							if (equalAmount(Amount, CURRCODE, objRs.getString("CURR_CODE"),IssuerId) > objRs.getDouble("AMOUNTPERTRANX"))
							{
								//throw new TPlusException(TPlusCodes.DO_NOT_HONOUR,"Temporary Limit Amount/Tranx","01");
								System.out.println(" >> CardHolder Amt/Tranx exceeded from WithdrawalLimits Rules");
								return false;
							}
						}

					sbfDTDVal.setLength(0);
					sbfDTDVal.append("select count(*) cnt, sum(settled_Amt) sumamt from transaction where extract (day from transaction_date)=extract (day from sysdate)");
					sbfDTDVal.append("and  response_code = '00'	AND transaction_status = '0'and transaction_type in('"+TranxCode+"')");

					if (DebugWriter.boolDebugEnabled) DebugWriter.write(" Daily Limit Check:"+sbfDTDVal.toString());
					 System.out.println(" SQL="+sbfDTDVal.toString());
					  bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
					  TPlusResultSet objRs1 = objDBMan.getResultSet();
					 if(objRs1.next() && objRs1.getInt("cnt")>0)
					 {
System.out.println("Inside the 1nd If");
							if (objRs.getInt("DAILYLIMITCOUNT") > 0 )
							{

								System.out.println("Accumcount="+objRs1.getInt("cnt")+" "+objRs.getInt("DAILYLIMITCOUNT"));
								if (objRs1.getInt("cnt") > objRs.getInt("DAILYLIMITCOUNT"))
								{
									System.out.println(" >> CardHolder  Daily Limit Count exceded from WithdrawalLimits Rules");
									return false;
								}
							 }

							if (objRs.getInt("DAILYLIMITAMOUNT") > 0 )
							{

								System.out.println("Accumcount="+objRs1.getInt("sumamt")+" "+objRs.getInt("DAILYLIMITAMOUNT"));
								if (objRs1.getInt("sumamt") > objRs.getInt("DAILYLIMITAMOUNT"))
								{
									System.out.println(" >> CardHolder  Daily Limit Amount exceded from WithdrawalLimits Rules");
									return false;
								}
							 }
				 	}

					sbfDTDVal.setLength(0);
					sbfDTDVal.append("select count(*) cnt, sum(settled_Amt) sumamt from transaction where sysdate-transaction_date < 30 ");
					sbfDTDVal.append("and  response_code = '00'	AND transaction_status = '0'and transaction_type in('"+TranxCode+"')");

					if (DebugWriter.boolDebugEnabled) DebugWriter.write(" Montly Limit Check:"+sbfDTDVal.toString());
					 System.out.println(" SQL="+sbfDTDVal.toString());
					  bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
					  objRs1 = objDBMan.getResultSet();

					 if(objRs1.next() && objRs1.getInt("cnt")>0)
					 {
System.out.println("Inside the 2nd If");
							if (objRs.getInt("MONTHLYLIMITCOUNT") > 0 )
							{

								System.out.println("Accumcount="+objRs1.getInt("cnt")+" "+objRs.getInt("MONTHLYLIMITCOUNT"));
								if (objRs1.getInt("cnt") > objRs.getInt("MONTHLYLIMITCOUNT"))
								{
									System.out.println(" >> CardHolder  Montly Limit Count exceded from WithdrawalLimits Rules");
									return false;
								}
							 }
							 System.out.println("objRs.getInt(MONTHLYLIMITAMOUNT)"+objRs.getInt("MONTHLYLIMITAMOUNT"));

							if (objRs.getInt("MONTHLYLIMITAMOUNT") > 0 )
							{
								System.out.println("22");
								System.out.println("Accumcount="+objRs1.getInt("sumamt")+" "+objRs.getInt("MONTHLYLIMITAMOUNT"));
								if (objRs1.getInt("sumamt") > objRs.getInt("MONTHLYLIMITAMOUNT"))
								{
									System.out.println(" >> CardHolder  Montly Limit Amount exceded from WithdrawalLimits Rules");
									return false;
								}
							 }

				 	}


                }
            }

        }
        catch (Exception e) {
           // throw new Exception(TPlusCodes.APPL_ERR,"Error :while Retrieving the WithdrawalLimit info"+e);
           System.out.println("Error Happen while processing the WithDrawal Limit Rules");
           return false;
        }
        finally {
        } // End of the Main Try Block

        return true;
    }


 public double equalAmount(double amount,String fromCurrency,String toCurrency,String issuerid  )throws Exception {

        if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:equalAmount");

        return amount;
    }




}
