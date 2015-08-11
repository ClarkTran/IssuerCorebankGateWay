package com.transinfo.tplus.util;

public class DBException extends Exception
{

    private String strErrorCode;
    private String strErrorDesc;

    /**
     * Constructor      BatchException
     *
     * @param           strError
     */
    public DBException(String strError)
    {
        super(strError);

        this.strErrorCode = strError;
    }
 public DBException(int intError, String strErrorDesc)
    {
        super(String.valueOf(intError) + strErrorDesc);
        this.strErrorCode = String.valueOf(intError);
        this.strErrorDesc = strErrorDesc;
    }

    /**
     * Constructor      BatchException
     *
     * @param           strError
     * @param           strErrorDesc
     */
    public DBException(String strError, String strErrorDesc)
    {
        super(strError + strErrorDesc);

        this.strErrorCode = strError;
        this.strErrorDesc = strErrorDesc;
    }

    /**
     * Method             getErrorCode
     * Description      : returns the error code
     * @param none
     * @return  String
     *  Description     : Error Code
     * @throws none
     */
    public String getErrorCode()
    {
        return strErrorCode;
    }

    /**
     * Method             getErrorDesc
     * Description      : returns the Error Description
     * @param none
     * @return  String
     *  Description     : Error Description
     * @throws none
     */
    public String getErrorDesc()
    {
        return strErrorDesc;
    }
}