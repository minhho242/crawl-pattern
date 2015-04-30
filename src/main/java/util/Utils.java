package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description
 *
 * @author minhho242 on 3/27/15.
 */
public class Utils {

    public static String toTitleCase(String text) {
        if ( text == null ) {
            return null;
        }
        text = text.toLowerCase();
        text = _upperAbbreviation(text);
        
        Pattern pattern  = Pattern.compile("\\b(\\s*[\\w])", Pattern.UNICODE_CHARACTER_CLASS);
        Matcher matcher = pattern.matcher(text);

        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            String character = matcher.group().toUpperCase();
            matcher.appendReplacement(result, character);
        }
        matcher.appendTail(result);
        return result.toString();
    }

    public static String _upperAbbreviation(String text) {
        Pattern pattern = Pattern.compile("\\b([^aáàảãạâấầẩẫậăắằẳẵặeéèẻẽẹêếềểễệuúùủũụưứừửữựiíìỉĩịoóòỏõọôốồổỗộơớờởỡợyýỳỷỹỵ]+)\\b", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CHARACTER_CLASS);
        Matcher matcher = pattern.matcher(text);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static String buildURL(String url, Object... params) {
        StringBuilder sb = new StringBuilder(url);
        if ( params.length > 0 ) {
            sb.append("/");
            for (int i = 0; i < params.length; i += 2) {
                sb.append(params[i]).append("/").append(params[i+1]).append("/");
            }
            sb.deleteCharAt(sb.length() - 1); //remove last & symbol
        }
        return sb.toString();
    }

    public static String shortString(String text, int beginIndex, int length) {
        int endIndex = beginIndex + length;
        while ( (text.charAt(endIndex--) != ' ') && endIndex > 0);
        return text.substring(beginIndex, endIndex + 1);
    }

    public static char getFirstLetter(String text) {
        String firstLetter = text.substring(0, 2).toUpperCase();
        Pattern pattern = Pattern.compile("[A-Z]");
        Matcher matcher = pattern.matcher(firstLetter);
        return matcher.find() ? firstLetter.charAt(0) : '#';
    }

}
