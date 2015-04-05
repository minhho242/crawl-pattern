package model;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;

/**
 * Description
 *
 * @author minhho242 on 3/23/15.
 */
public class Webbot {
    
    //------------- Contansts --------------//
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    
    private String _webURL;
    private String _data;
    
    public Webbot(String URL) throws IOException {
        _webURL = URL;
        
    }
    
    public String crawl(String method) throws IOException {
        HttpRequestBase httpRequest;
        switch (method) {
            default:
            case METHOD_GET:
                httpRequest = new HttpGet(_webURL);
                break;
            case METHOD_POST:
                httpRequest = new HttpPost(_webURL);
                break;
        }
        HttpClient httpClient = HttpClients.createDefault();

        HttpResponse httpResponse = httpClient.execute(httpRequest);
        HttpEntity httpEntity = httpResponse.getEntity();
        InputStream inputStream = httpEntity.getContent();
        _data = IOUtils.toString(inputStream);
        
        inputStream.close();
        
        return _data;
    }
    
    public String getData() {
        return _data;
        
    }
}
