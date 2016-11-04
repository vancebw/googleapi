
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.gdata.client.contacts.ContactsService;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.contacts.ContactFeed;
import com.google.gdata.data.extensions.Email;
import com.google.gdata.data.extensions.Name;
import com.google.gdata.util.ServiceException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;

public class GoogleUserInfo {

    //client Id
    private static final String CLIENT_ID = "XXXXXXX";

    //client secret
    private static final String CLIENT_SECRET = "XXXXXX";

    //loopback ip address
    private static final String REDIRECT_URI = "http://localhost:8000";

    //userInfo scope
    private static final Collection<String> SCOPE = Collections.singleton("openid profile");

    //contacts url
    private static final String USER_INFO_URL = "https://www.googleapis.com/oauth2/v3/userinfo";

    public static String getUserInfoJson(Credential credential) throws IOException, ServiceException {
        final HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory(credential);
        // Make an authenticated request
        final GenericUrl url = new GenericUrl(USER_INFO_URL);
        final HttpRequest request = requestFactory.buildGetRequest(url);
        request.getHeaders().setContentType("application/json");
        final String jsonIdentity = request.execute().parseAsString();
        return jsonIdentity;
    }

    public static void main(String[] args) throws Exception {
        GoogleOAuthUtil googleOAuthUtil = new GoogleOAuthUtil(CLIENT_ID, CLIENT_SECRET, REDIRECT_URI, SCOPE);
        String loginUrl = googleOAuthUtil.buildLoginUrl();
        System.out.println("Paste this url in your browser:");
        System.out.println(loginUrl);
        System.out.println("Type the code you received here: ");
        String authCode = new BufferedReader(new InputStreamReader(System.in)).readLine();
        Credential credential = googleOAuthUtil.getCredential(authCode);
        System.out.println(getUserInfoJson(credential));
    }
}
