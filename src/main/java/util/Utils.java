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
}
