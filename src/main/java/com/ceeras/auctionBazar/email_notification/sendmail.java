
package com.ceeras.auctionBazar.email_notification;

import org.springframework.beans.factory.annotation.Value;
//zimport org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class sendmail {
    @Value("${brevo.api.key}")
    private String apiKey;
    String url = "https://api.brevo.com/v3/smtp/email";
    public void sendEmail(String toEmail,String name ,String templateId) {
         //String templateId = "1";
       
        HttpHeaders headers = new HttpHeaders();
        headers.set("api-key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body =  "{"
        + "\"sender\": {\"email\": \"anushkadutta102@gmail.com\"},"
        + "\"to\": [{\"email\": \"" + toEmail + "\"}],"
        + "\"templateId\": " + templateId + ","
        + "\"params\": {"
        + "\"name\": \"" + name + "\""
        + "}"
        + "}";

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        System.out.println(response.getBody());
    }
    public void sendEmail(String toEmail,String name ,String templateId,String prurl) {
       HttpHeaders headers = new HttpHeaders();
       headers.set("api-key", apiKey);
       headers.setContentType(MediaType.APPLICATION_JSON);

       String body =  "{"
       + "\"sender\": {\"email\": \"anushkadutta102@gmail.com\"},"
       + "\"to\": [{\"email\": \"" + toEmail + "\"}],"
       + "\"templateId\": " + templateId + ","
       + "\"params\": {"
       + "\"name\": \"" + name + "\","
       + "\"url\": \"" + prurl + "\""
       + "}"
       + "}";


       HttpEntity<String> entity = new HttpEntity<>(body, headers);

       RestTemplate restTemplate = new RestTemplate();
       ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

       System.out.println(response.getBody());
   }
}


