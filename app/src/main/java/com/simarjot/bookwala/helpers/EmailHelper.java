package com.simarjot.bookwala.helpers;

import android.util.Patterns;

public class EmailHelper {

    public static boolean seemsNumber(CharSequence target){
        //finding if all the digits entered till now are integers
        boolean hasOtherChars = false;
        for(int i=0;i<target.length();i++){
            char c = target.charAt(i);
            //48 to 57 are nos
            int code = (int) c;
            if(code<48 || code>57){
                hasOtherChars = true;
            }
        }
        if(hasOtherChars){
            return false;
        }
        else return true;
    }

    public static boolean isValidEmail(CharSequence target) {
        return (Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static boolean hasTenDigits(CharSequence target){
       if(target.length()>0){
           if(target.charAt(0) == '0'){
               if(target.length()==11){
                   return true;
               }else return false;
           }else{
               if(target.length()==10){
                   return true;
               }else return false;
           }
       }else{
           //if the string is empty then always returning false
           return false;
       }
    }
}
