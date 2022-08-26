package pin.safenet;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;


import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

public class ApacheHttpClientTest {

    private HttpClient httpClient;

   
    public void initClient() throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        httpClient = HttpClients
                .custom()
                .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build())
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .build();
    }

   
    public void apacheHttpClient455Test() throws IOException {
        executeRequestAndVerifyStatusIsOk("https://expired.badssl.com");
        executeRequestAndVerifyStatusIsOk("https://wrong.host.badssl.com");
        executeRequestAndVerifyStatusIsOk("https://self-signed.badssl.com");
        executeRequestAndVerifyStatusIsOk("https://untrusted-root.badssl.com");
        executeRequestAndVerifyStatusIsOk("https://revoked.badssl.com");
        executeRequestAndVerifyStatusIsOk("https://pinning-test.badssl.com");
        executeRequestAndVerifyStatusIsOk("https://sha1-intermediate.badssl.com");
    }

    private void executeRequestAndVerifyStatusIsOk(String url) throws IOException {
        HttpUriRequest request = new HttpGet("https://api.sierrahive.com/v1/messages/sms?clientid=ef21abfe83d3&clientsecret=a83b6ed00f9f42b8ac665b6cf2992eec&token=0a2f6b1b60104851855ea99b7e3a4cbb&from=RCBank&to=23278195738&reference=1234&content=testing");

        HttpResponse response = httpClient.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println(statusCode);
        assert statusCode == 200;
    }
    public static void main(String[] args) throws IOException {
		ApacheHttpClientTest ss = new ApacheHttpClientTest();
		
		ss.executeRequestAndVerifyStatusIsOk("https://api.sierrahive.com/v1/messages/sms?clientid=ef21abfe83d3&clientsecret=a83b6ed00f9f42b8ac665b6cf2992eec&token=0a2f6b1b60104851855ea99b7e3a4cbb&from=RCBank&to=23278195738&reference=1234&content=testing");
	}
}
