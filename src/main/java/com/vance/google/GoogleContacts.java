package com.vance.google;

import com.google.api.client.auth.oauth2.Credential;
import com.google.gdata.client.contacts.ContactsService;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.contacts.ContactFeed;
import com.google.gdata.data.extensions.Email;
import com.google.gdata.data.extensions.Name;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;

public class GoogleContacts {

    //client Id
    private static final String CLIENT_ID = "XXXXXXX.apps.googleusercontent.com";

    //client secret
    private static final String CLIENT_SECRET = "XXXXXXX";

    //loopback ip address
    private static final String REDIRECT_URI = "http://localhost:8000";

    //contacts scope
    private static final Collection<String> SCOPE = Collections.singleton("https://www.google.com/m8/feeds");

    //contacts url
    private static final String CONTACTS_URL = "https://www.google.com/m8/feeds/contacts/default/full";

    public static void printAllContacts(ContactsService myService) throws Exception {
        URL feedUrl = new URL(CONTACTS_URL);
        ContactFeed resultFeed = myService.getFeed(feedUrl, ContactFeed.class);
        System.out.println(resultFeed.getTitle().getPlainText());
        for (ContactEntry entry : resultFeed.getEntries()) {
            for (Email email : entry.getEmailAddresses()) {
                System.out.print(email.getAddress() + "\t");
            }
            if (entry.hasName()) {
                Name name = entry.getName();
                System.out.print("First Name: ");
                if (name.hasGivenName()) {
                    String givenNameToDisplay = name.getGivenName().getValue();
                    if (name.getGivenName().hasYomi()) {
                        givenNameToDisplay += " (" + name.getGivenName().getYomi() + ")";
                    }
                    System.out.print(givenNameToDisplay);
                }
                System.out.print("\tLast Name: ");
                if (name.hasFamilyName()) {
                    String familyNameToDisplay = name.getFamilyName().getValue();
                    if (name.getFamilyName().hasYomi()) {
                        familyNameToDisplay += " (" + name.getFamilyName().getYomi() + ")";
                    }
                    System.out.print(familyNameToDisplay);
                }
            }
            System.out.println();

        }
    }

    public static void main(String[] args) throws Exception {
        GoogleOAuthUtil googleOAuthUtil = new GoogleOAuthUtil(CLIENT_ID, CLIENT_SECRET, REDIRECT_URI, SCOPE);
        String loginUrl = googleOAuthUtil.buildLoginUrl();
        System.out.println("Paste this url in your browser:");
        System.out.println(loginUrl);
        System.out.println("Type the code you received here: ");
        String authCode = new BufferedReader(new InputStreamReader(System.in)).readLine();
        Credential credential = googleOAuthUtil.getCredential(authCode);
        ContactsService myService = new ContactsService("contacts");
        myService.setOAuth2Credentials(credential);
        printAllContacts(myService);
    }
}
