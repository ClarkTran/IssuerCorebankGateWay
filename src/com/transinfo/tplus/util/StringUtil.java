package com.transinfo.tplus.util;

import com.transinfo.tplus.TPlusCodes;
import com.transinfo.tplus.TPlusException;

public class StringUtil extends Object{

    public static boolean checkPassword(String password){
        int status = 0;
        String str = password;
        if(str.length() > 7) {
            for(int i = 0; i < str.length() - 3; i++) {
                if(str.charAt(i) != str.charAt(i + 1) || str.charAt(i) != str.charAt(i + 2) || str.charAt(i) != str.charAt(i + 3) )
                    continue;
                status = 1;
                break;
            }

            return status != 1;
        } else {
            return false;
        }
    }

  /* This method is validate Length of password; ie Password must be between 8 - 16
   * and no 3 contenious letters should not be same
   */
    public static boolean checkLength3Seq(int minLength, int maxLength, String password){
        boolean flag= false;
        int status=0;
        String str=password;

        if (str.length() >= minLength && str.length() <= maxLength) {
            for(int i=0;i<str.length()-2;i++){
                if(str.charAt(i)==str.charAt(i+1) && str.charAt(i)==str.charAt(i+2)) {
                    status=1;
                    break;
                }
            }
            if(status==1){
                flag = false;
            }else{
                flag = true;
            }
        }else{
            flag=false;
        }
        return flag;
    }

  /* This method is validate the password; ie atleast 1 character and 1 number must be in password
   * and only password should contain only alphanumeric
   */
    public static boolean checkPasswordAN(String password){
        boolean flag=false;
        int c=0;
        int n=0;
        int nc=0;
        int r=0;
        for(int i=0;i<password.length();i++){
            char b = password.charAt(i);
            if(Character.isDigit(b)){
                c=1;
            }else{
                n=1;
            }
            if((b<'a' || b>'z') && (b<'A' || b>'Z') && (b<'0' || b>'9')) {
                nc = 1;
            }
        }
        if (c==1 && n==1 && nc==0) {
            flag = true;
        }
        return flag;
    }


    public static String rmQuote(String str){
        if(str==null || str.trim().equals("")) return "";
        int index = 0;
        StringBuffer sb=new StringBuffer(str);
        index = str.indexOf("'");
        while(index!=-1){
            if (index >0){
                str = new String(sb.insert(index,"'"));
            }else if (index==0){
                str = new String(sb.insert(0,"'"));
            }
            index = str.indexOf("'",index+2);
        }
        return str;
    }

    public static String checkNullint(String str){
        if(str == null || str.trim().equals("")){
            str = "0";
            return str;
        } else {
            return str.trim();
        }
    }

    public static String checkNullstr(String str){
        if(str == null || str.trim().equals("")){
            str = " ";
            return str;
        } else {
            return str.trim();
        }
    }

    public static String checkNull(String str){
        if(str == null || str.trim().equals("")){
            str = "";
            return str;
        } else {
            return str.trim();
        }
    }

    public static String checkBox(String str){
        if(str == null || str.trim().equals("")) {
            return "0";
        } else {
            return "1";
        }
    }

    public static String getCheckBoxValue(String str) {
        if(str == null || str.trim().equals("")) str = "";
        if (str.trim().equals("on")) {
            return "Y";
        }else{
            return "N";
        }
    }

    public static String changeTextBox(String str) {
        if(str == null || str.trim().equals("")) str = "";
        if (str.trim().equals("on")) {
            return "Y";
        }else{
            return "N";
        }
    }
    public static boolean checkNulldate(String str) {
        boolean flag = false;
        if(str == null || str.trim().equals("")) {
            flag = true;
        }
        return flag;
    }

    public static String convertValue(String str){
        String newStr="";
        if(str == null || str.equals("")){
            newStr = "";
            return str;
        }else{
            char tempChar;
            for (int i = 0; i < str.length(); i++) {
                tempChar = str.charAt(i);
                newStr = newStr + tempChar;
                if (tempChar == '\''){
                    newStr = newStr + tempChar;
                }
            }
            return newStr;
        }
    }

    /**
     * This method functions pad the given char to the left of the string.
     * @param  strValue Any string.
     * @param  totLength Total length of the string.
     * @param  padChar Padding character.
     * @return Formated string.
     */
    public static String LPAD(String strValue, int totLength, String padChar){
        String rtnValue = "";
        int strLen = (String.valueOf(strValue)).length();
        for(int i=1;i<=(totLength-strLen);i++){
            rtnValue +=padChar;
        }
        return rtnValue+strValue;
    }

    /**
     * This method functions pad the given char to the right of the string.
     * @param  strValue Any string.
     * @param  totLength Total length of the string.
     * @param  padChar Padding character.
     * @return Formated string.
     */
    public static String RPAD(String strValue, int totLength, String padChar){
        String rtnValue = "";
        int strLen = (String.valueOf(strValue)).length();
        for(int i=1;i<=(totLength-strLen);i++){
            rtnValue +=padChar;
        }
        return strValue+rtnValue;
    }

    /**
     * This method alters the given string.
     * @param           strOriginal  Original string.
     * @param           intlength Total length.
     * @param           padChar Padding character.
     * @param           justify Justification.
     * @return  Formated string.
     */
    public static String alterString(String strOriginal, int intlength, String padChar, String justify) {
        try {
            if (strOriginal.length() == intlength) {
                return strOriginal;
            }else if(justify.trim().equals("L")){
                return LPAD(strOriginal,intlength,padChar);
            }else if (justify.trim().equals("R")){
                return RPAD(strOriginal,intlength,padChar);
            }
        } catch (Exception exp) {
        }

        return strOriginal;
    }

    /**
    *  This method replaces all occurance of a substring with the given substring in a string
    *  @param strString The parent string
    *  @param strSrchString The substring which needs to be replaced
    *  @param strRplString The new substring which will replace all occurences of strsrchString
    *  @return String Parent string with all occurences of strsrchString replaced by strRplString
    */
    public static String replaceString(String strString, String strSrchString, String strRplString)
    {

        if( strString == null)
            return "";

        String  strOutString = "" ;
        int     intIndex = 0 ;
        int     intPrevIndex = 0 ;
        int     intSrcStrLength = strSrchString.length() ;

        do
        {
            intIndex = strString.indexOf(strSrchString, intPrevIndex) ;

            if (intIndex == -1)
            {
                strOutString += strString.substring(intPrevIndex) ;
                break ;
            }

            strOutString += strString.substring(intPrevIndex, intIndex) + strRplString ;
            intPrevIndex = intIndex + intSrcStrLength ;

        } while (true) ;

        return strOutString ;
    }


    public static String setAmountNoGroupSeparator(String value) {
      String temp = value ;
      if(temp!=null){
         temp = temp.replace('.','G');
         temp = temp.replaceAll("G","");
         temp = temp.replace(',','.');
      }
      return temp;
    }

    public static String commifyWithDecimalSeparator(String Num) {
      String newNum = "";
      String newNum2 = "";
      String end = "";
      String groupSeparator = ".";
      String decimalSeparator = ",";
      int count = 0;

      //check for decimal number
      if (Num.indexOf(decimalSeparator) != -1){  //number ends with a decimal point
          if (Num.indexOf(decimalSeparator) == Num.length()-1){
              Num += "00";
          }
          if (Num.indexOf(decimalSeparator) == Num.length()-2){ //number ends with a single digit
              Num += "0";
          }

          String[] a = Num.split(decimalSeparator);
          Num = a[0];   //the part we will commify
          end = a[1]; //the decimal place we will ignore and add back later
      }
      else {end = "00";}

      //this loop actually adds the commas
      for (int k = Num.length()-1; k >= 0; k--){
        char oneChar = Num.charAt(k);
        if (count == 3){
          newNum += groupSeparator;
          newNum += oneChar;
          count = 1;
          continue;
        }
        else {
          newNum += oneChar;
          count ++;
        }
     }  //but now the string is reversed!

    //re-reverse the string
    for (int k = newNum.length()-1; k >= 0; k--){
        char oneChar = newNum.charAt(k);
        newNum2 += oneChar;
    }

     // add dollar sign and decimal ending from above
     newNum2 = newNum2 + decimalSeparator + end;
     return newNum2;
  }


  public static String commifyWithGroupSeparator(String Num) {

      if(Num == null || Num.equals("")){
        return Num;
      }

      boolean negative = false;

      if(Num.startsWith("-")){
          negative = true;
          Num = Num.substring(1);
      }

      String newNum = "";
      String newNum2 = "";
      String end = "";
      char groupSeparator = '.';
      char decimalSeparator = ',';
      int count = 0;

      Num = Num.replace(groupSeparator,decimalSeparator);

      //check for decimal number
      if (Num.indexOf(decimalSeparator) != -1){  //number ends with a decimal point
          if (Num.indexOf(decimalSeparator) == Num.length()-1){
              Num += "00";
          }
          if (Num.indexOf(decimalSeparator) == Num.length()-2){ //number ends with a single digit
              Num += "0";
          }

          String[] a = Num.split(decimalSeparator+"");
          Num = a[0];   //the part we will commify
          end = a[1]; //the decimal place we will ignore and add back later
      }
      else {end = "";}

      //this loop actually adds the commas
      for (int k = Num.length()-1; k >= 0; k--){
        char oneChar = Num.charAt(k);
        if (count == 3){
          newNum += groupSeparator;
          newNum += oneChar;
          count = 1;
          continue;
        }
        else {
          newNum += oneChar;
          count ++;
        }
     }  //but now the string is reversed!

    //re-reverse the string
    for (int k = newNum.length()-1; k >= 0; k--){
        char oneChar = newNum.charAt(k);
        newNum2 += oneChar;
    }

     if(!end.equals("")){
        newNum2 = newNum2 + decimalSeparator + end;
     }

     if(negative){
        newNum2 = "-" + newNum2;
     }

     return newNum2;
  }

  public static String commifyWithDollarSign(String Num) {
      String newNum = "";
      String newNum2 = "";
      String end = "";
      int count = 0;

      //check for decimal number
      if (Num.indexOf('.') != -1){  //number ends with a decimal point
          if (Num.indexOf('.') == Num.length()-1){
              Num += "00";
          }
          if (Num.indexOf('.') == Num.length()-2){ //number ends with a single digit
              Num += "0";
          }

          String[] a = Num.split(".");
          Num = a[0];   //the part we will commify
          end = a[1]; //the decimal place we will ignore and add back later
      }
      else {end = "00";}

      //this loop actually adds the commas
      for (int k = Num.length()-1; k >= 0; k--){
        char oneChar = Num.charAt(k);
        if (count == 3){
          newNum += ",";
          newNum += oneChar;
          count = 1;
          continue;
        }
        else {
          newNum += oneChar;
          count ++;
        }
     }  //but now the string is reversed!

    //re-reverse the string
    for (int k = newNum.length()-1; k >= 0; k--){
        char oneChar = newNum.charAt(k);
        newNum2 += oneChar;
    }

     // add dollar sign and decimal ending from above
     newNum2 = "$" + newNum2 + "." + end;
     return newNum2;
  }

  public static String getISOAmt(String strAmt)
  {
	  if(strAmt !=null)
	  {
		 return  ("0000000000"+strAmt).substring(strAmt.length())+"00";
	  }
	  else
	  {
		  return "";
	  }
  }

  public static String getISOAmtNew(String strAmt)
  {
	 System.out.println(strAmt);
	  if(strAmt !=null)
	  {
		 Double amtDlb = (new Double(strAmt).doubleValue())*100;
		 System.out.println("Double Amt="+amtDlb);
		 long lAmt = new Double(amtDlb).longValue();
		 System.out.println("long Amt="+lAmt);
		 String strDestAmount = LPAD(new Long(lAmt).toString(),12,"0");
		 System.out.println("strDestAmount="+strDestAmount);
		 return strDestAmount;

		 //return  ("0000000000"+strAmt).substring(strAmt.length())+"00";
	  }
	  else
	  {
		  return "";
	  }
  }

	public static String escapeSingeQuote(String text) throws TPlusException {
		
		String res = "";
		
		try{
			
			if(text != null && !"".equals(text)){
				res = text.replace("'", "''");
			}
		
		}catch (Exception e) {
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error :escapeSingeQuote " + e);
		}
		
		return res;
		
	}

	public static String getZero(int noOfSpaces) {

		String res = "";
		for (int i = 0; i < noOfSpaces; i++) {
			res += "0";
		}

		return res;
	}


  public static void main(String[] args) {
    StringUtil u = new StringUtil();
    System.out.println(u.setAmountNoGroupSeparator("15.000,1"));
    System.out.println(u.commifyWithGroupSeparator(u.setAmountNoGroupSeparator("15.000,1")));

  }

}