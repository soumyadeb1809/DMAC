package in.teamconsultants.dmac.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtils {

    private static final String PAN_PATTERN = "[A-Z]{5}[0-9]{4}[A-Z]{1}";

    private static final String GSTIN_PATTERN = "^([0]{1}[1-9]{1}|[1-2]{1}[0-9]{1}|[3]{1}[0-7]{1})([a-zA-Z]{5}[0-9]{4}[a-zA-Z]{1}[1-9a-zA-Z]{1}[zZ]{1}[0-9a-zA-Z]{1})+$";

    public static  final String EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    public static boolean isValidPAN(String panNumber){
        Pattern pattern = Pattern.compile(PAN_PATTERN);

        if(panNumber == null)
            return false;

        Matcher matcher = pattern.matcher(panNumber);
        // Check if pattern matches
        return matcher.matches();
    }


    public static boolean isValidGSTIN(String gstinNumber){
        Pattern pattern = Pattern.compile(GSTIN_PATTERN);

        if(gstinNumber == null)
            return false;

        Matcher matcher = pattern.matcher(gstinNumber);
        // Check if pattern matches
        return matcher.matches();
    }


    public static boolean isValidEmail(String email)
    {
        Pattern pat = Pattern.compile(EMAIL_PATTERN);
        if (email == null)
            return false;

        return pat.matcher(email).matches();
    }

}
