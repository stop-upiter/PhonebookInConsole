package backend;

import java.util.Objects;
import java.util.regex.Pattern;

public class FormatDataChecker {
    public static String makeNotBlank(String str){
        if (Objects.isNull(str)||str.isBlank()){
            return "";
        }
        else{
            return str.trim();
        }
    }

    public static boolean isPhoneNumber(String number){
        return Pattern.matches("^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{3,15}$", number);
    }

    public static boolean isEmail(String email){
        return Pattern.matches(
                "([a-zA-Z0-9]+(?:[._+-][a-zA-Z0-9]+)*)@([a-zA-Z0-9]+(?:[.-][a-zA-Z0-9]+)*[.][a-zA-Z]{2,})",
                email);

    }
}
