package com.vance.google;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Collection;

public class GoogleOAuthUtil {

    private String clientId;

    private String clientSecret;

    private String redirectUri;

    private Collection<String> scope;

    private GoogleAuthorizationCodeFlow flow;

    public GoogleOAuthUtil(String clientId, String clientSecret, String redirectUri, Collection<String> scope) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.scope = scope;

        HttpTransport httpTransport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();
        flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory, this.clientId, this.clientSecret, this.scope).build();
    }

    public String buildLoginUrl() {
        String stateToken = "google;" + new SecureRandom().nextInt();
        GoogleAuthorizationCodeRequestUrl url = flow.newAuthorizationUrl();
        return url.setRedirectUri(this.redirectUri).setState(stateToken).build();
    }

    public Credential getCredential(String authCode) throws IOException {
        final GoogleTokenResponse response = flow.newTokenRequest(authCode).setRedirectUri(this.redirectUri).execute();
        final Credential credential = flow.createAndStoreCredential(response, null);
        return credential;
    }
}
