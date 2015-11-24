package de.neuland.hybris.http.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CookieParser {

    private static CookieParser cookieParser = new CookieParser();

    public static CookieParser getInstance() {
        return cookieParser;
    }

    private CookieParser() {}

    public String getSpecialCookie(String cookieHTTPHeaderString, String cookieName) {
        String regEx = ".*(((" + cookieName + "=[^;]*)|(" + cookieName + "=\"[\";]*))|(" + cookieName + "=.*$)).*";
        Pattern searchPattern = Pattern.compile(regEx);
        Matcher matcher = searchPattern.matcher(cookieHTTPHeaderString);
        return matcher.matches() ? matcher.group(1) : null;
    }

}
