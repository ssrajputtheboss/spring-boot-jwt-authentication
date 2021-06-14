package com.shash.jwtapp.utils;

import java.util.regex.Pattern;

import java.util.regex.Matcher;

public class Verifier {
    private static final String EMAIL_REGEX = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
    
    public static boolean email(String mail){
        if(mail == null)return false;
        Pattern re = Pattern.compile(EMAIL_REGEX , Pattern.CASE_INSENSITIVE);
        Matcher matcher = re.matcher(mail);
        return matcher.find();
    }

    public static boolean pass(String password){
        if(password == null)return false;
        return (password.length()>=8) && (password.length()<=25);
    }
}
