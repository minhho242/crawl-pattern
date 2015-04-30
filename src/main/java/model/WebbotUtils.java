package model;

import java.util.Map;
import java.util.Set;

/**
 * Util class providing functions for manipulating with Http request calling
 *
 * @author minhho242 on 4/25/15.
 */
public class WebbotUtils {

    public enum ParamType {
        SLASH_TYPE, QUESTION_AND_TYPE
    }

    public static String buildParams(String webUrl, ParamType paramType, Object... keyValuePairs) {
        if ( paramType == ParamType.QUESTION_AND_TYPE ) {
            return _buildParams(webUrl, '?', '&', '=', keyValuePairs);
        } else {
            return _buildParams(webUrl, '/', '/','/', keyValuePairs);
        }
    }

    public static String buildParams(String webUrl, Object... keyValuePairs) {
        return _buildParams(webUrl, '?', '&', '=', keyValuePairs);
    }

    public static String buildParams(String webUrl, Map<String, Object> keyValuePairs) {
        return buildParams(webUrl, ParamType.QUESTION_AND_TYPE, keyValuePairs);
    }

    public static String buildParams(String webUrl, ParamType paramType, Map<String, Object> keyValuePairs) {
        String[] arrayKeyValuePairs = new String[keyValuePairs.size() * 2];
        Set<String> keys = keyValuePairs.keySet();

        int index = 0;
        for (String key : keys) {
            arrayKeyValuePairs[index++] = key;
            arrayKeyValuePairs[index++] = String.valueOf(keyValuePairs.get(key));
        }

        return buildParams(webUrl, paramType, arrayKeyValuePairs);
    }

    private static String _buildParams(String webUrl,
                                      char startParamSegmentChar,
                                      char paramSeparator,
                                      char keyValueSeparator,
                                      Object... keyValuePairs) {

        StringBuffer sb = new StringBuffer(webUrl);

        if ( webUrl.indexOf(startParamSegmentChar) < 0 ) {
            sb.append(startParamSegmentChar);
        } else {
            sb.append(paramSeparator);
        }

        for (int i = 0; i < keyValuePairs.length; i += 2) {
            sb.append(keyValuePairs[i]).append(keyValueSeparator).append(keyValuePairs[i + 1]).append(paramSeparator);
        }

        sb.deleteCharAt(sb.length() - 1); //remove last symbol

        return sb.toString();
    }

}
