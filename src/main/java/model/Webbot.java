package model;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import sun.net.util.URLUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Handling Http request calling.
 *
 * @author minhho242 on 3/23/15.
 */
public class Webbot {
    
    //------------- Contansts --------------//
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";


    //---- Properties -----//
    private String _webURL;
    private String _data;
    private HttpRequestBase _httpRequest;
    private Map<String, Object> _headers;
    
    public Webbot(String URL) throws IOException {
        _webURL = URL;
        _headers = new HashMap<>();
        
    }
    
    public String crawl(String method) throws IOException {
        return crawl(method, null);
    }

    public String crawl(String method, Map<String, Object> params) throws IOException {
        switch (method) {
            default:
            case METHOD_GET:
                String url = params != null ? WebbotUtils.buildParams(_webURL, params) : _webURL;
                _httpRequest = new HttpGet(url);
                break;
            case METHOD_POST:
                HttpPost httpPost = new HttpPost(_webURL);

                if ( params != null ) {
                    List<NameValuePair> nameValuePairList = new ArrayList<>();
                    Iterator<String> keyIterator = params.keySet().iterator();
                    while (keyIterator.hasNext()) {
                        String name = keyIterator.next();
                        String value = String.valueOf(params.get(name));
                        nameValuePairList.add(new BasicNameValuePair(name, value));
                    }

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairList));
                    _httpRequest = httpPost;
                    break;
                }
        }

        _setHeaderToRequest();

        return _sendRequestAndGetData();
    }

    private String _sendRequestAndGetData() throws IOException {
        HttpClient httpClient = HttpClients.createDefault();

        HttpResponse httpResponse = httpClient.execute(_httpRequest);
        HttpEntity httpEntity = httpResponse.getEntity();
        InputStream inputStream = httpEntity.getContent();
        _data = IOUtils.toString(inputStream);

        inputStream.close();

        return _data;
    }
    
    public String getData() {
        return _data;
        
    }

    public void setHeader(String header, Object value) {
        _headers.put(header, value);
    }

    /**
     * Set headers To _httpRequest property
     */
    private void _setHeaderToRequest() {
        Set<String> headerKeys = _headers.keySet();
        for (String headerKey : headerKeys) {
            _httpRequest.setHeader(headerKey, String.valueOf(_headers.get(headerKey)));
        }
    }

}
