package util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import sun.misc.BASE64Decoder;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description
 *
 * @author minhho242 on 3/24/15.
 */
public class HoSoCongTyUtils {
    
    private static final Map<String, String> decodes = new HashMap<>();
    
    static {
        decodes.put("y", "0");
        decodes.put("z", "1");
        decodes.put("e", "2");
        decodes.put("g", "3");
        decodes.put("b", "4");
        decodes.put("c", "5");
        decodes.put("a", "6");
        decodes.put("d", "7");
        decodes.put("o", "8");
        decodes.put("p", "9");
        decodes.put("f", "/");
    }
    
    public static String extractByCSSQuery(Document document, String cssQuery) {
        Elements elements = document.select(cssQuery);
        if ( elements.size() > 0 ) {
            return elements.get(0).ownText();
        }
        return null;
        
    }

    public static String extractValueMatchingText(Document document, String text) {
        Elements elements = document.getElementsMatchingOwnText(text);
        if ( elements.size() > 0 ) {
            return elements.get(0).child(0).text();
        }
        return null;
    }


    public static String extractValueFromImageAndMatchingText(Document document, String text) {
        Elements elements = document.getElementsMatchingOwnText(text);
        if ( elements.size() > 0 ) {
            Node tempNode = elements.get(0).childNode(1);
            String imageValue;
            if ( tempNode.nodeName().equals("img") ) {
                String imageSource = tempNode.attr("src");
                imageValue = decodeImageValue(imageSource);
            } else {
                String imageSource = tempNode.childNode(0).attr("src");
                imageValue = decodeImageValue(imageSource);
            }
            return imageValue;
        }
        return null;
    }
    
    public static String extractBusinessLicense(Document document) {
        Elements elements = document.getElementsMatchingOwnText("Giấy phép kinh doanh");
        if ( elements.size() > 0 ) {
            String businessLicenseImageSource = elements.get(0).childNode(1).attr("src");
            String businessLicense = decodeImageValue(businessLicenseImageSource);
            return businessLicense;
        }
        return null;
    }

    public static String extractBusinessLicenseIssueDate(Document document) {
        Elements elements = document.getElementsMatchingOwnText("Giấy phép kinh doanh");
        if ( elements.size() > 0 ) {
            String businessLicenseIssuedDateImageSource = elements.get(0).childNode(3).attr("src");
            String businessLicenseIssuedDate = decodeImageValue(businessLicenseIssuedDateImageSource);
            return businessLicenseIssuedDate;
        }
        return null;
    }
    
    public static String extractBusinesses(Document document) {
        Elements elements = document.select("table tr");
        if ( elements.size() == 0 ) {
            return null;
        }
        List<List<String>> businesses = new ArrayList<>();
        for (int i = 1; i < elements.size(); i++) {
            Element trElement = elements.get(i);
            Element tdBusinessNameElement = trElement.child(1); //business name;
            
            String mainOperation = "N";
            if ( tdBusinessNameElement.childNodes().size() > 1 ) {
                mainOperation = "Y";
            }
            
            List<String> business = new ArrayList<>();
            business.add(tdBusinessNameElement.text());
            business.add(trElement.child(2).text());
            business.add(mainOperation);

            businesses.add(business);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonString = objectMapper.writeValueAsString(businesses);
            return jsonString;
        } catch (JsonProcessingException e) {
            return null;
        }
    }
    
    public static String decodeImageValue(String imageSource) {
        try {
            imageSource = URLDecoder.decode(imageSource, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }

        Pattern pattern = Pattern.compile(".*char=(.*)&", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(imageSource);

        String code;

        if ( matcher.find() ) {
            code = matcher.group(1);
        } else {
            return null;
        }
        
        code = _base64Decode(code);
        
        StringBuilder resultBuilder = new StringBuilder();
        for (int i = 0; i < code.length(); i++) {
            String decodedChar = decodes.get(String.valueOf(code.charAt(i)));
            resultBuilder.append(decodedChar);
        }
        
        return resultBuilder.toString();
    }
    
    private static String _base64Decode(String code) {
        byte[] byteBuffer = Base64.decode(code);
        return new String(byteBuffer);
        
    }
}
