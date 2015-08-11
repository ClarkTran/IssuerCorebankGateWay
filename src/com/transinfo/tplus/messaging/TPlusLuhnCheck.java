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

package com.transinfo.tplus.messaging;

public class TPlusLuhnCheck {


/**
 * This method is used to check the cardnumber is validated against the LunhCheck Alogrithm .
 *
 * @param CardNumber
 * @return boolean. TRUE card is valid or FALSE card is not valid
 */

  public static boolean isValid (String cardNumber) {
    String digitsOnly = getDigitsOnly (cardNumber);
    int sum = 0;
    int digit = 0;
    int addend = 0;
    boolean timesTwo = false;

    for (int i = digitsOnly.length () - 1; i >= 0; i--) {
      digit = Integer.parseInt (digitsOnly.substring (i, i + 1));
      if (timesTwo) {
        addend = digit * 2;
        if (addend > 9) {
          addend -= 9;
        }
      }
      else {
        addend = digit;
      }
      sum += addend;
      timesTwo = !timesTwo;
    }

    int modulus = sum % 10;
    return modulus == 0;

  }


/**
 * This method is used to Filter out non-digit characters.
 *
 * @param String
 * @return String.
 */

  private static String getDigitsOnly (String s) {
    StringBuffer digitsOnly = new StringBuffer ();
    char c;
    for (int i = 0; i < s.length (); i++) {
      c = s.charAt (i);
      if (Character.isDigit (c)) {
        digitsOnly.append (c);
      }
    }
    return digitsOnly.toString ();
  }


  //-----
  // Test
  //-----

  public static void main (String[] args) {
    String cardNumber = "4408 0412 3456 7890";
    boolean valid = TPlusLuhnCheck.isValid (cardNumber);
    //System.out.println (cardNumber + ": " + valid);
    cardNumber = "4408 0412 3456 7893";
    valid = TPlusLuhnCheck.isValid (cardNumber);
    //System.out.println (cardNumber + ": " + valid);
    cardNumber = "4417 1234 5678 9112";
    valid = TPlusLuhnCheck.isValid (cardNumber);
    //System.out.println (cardNumber + ": " + valid);
    cardNumber = "4417 1234 5678 9113";
    valid = TPlusLuhnCheck.isValid (cardNumber);
    //System.out.println (cardNumber + ": " + valid);


  }

}
