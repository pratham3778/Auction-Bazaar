
package com.ceeras.auctionBazar.email_notification;

//zimport org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class sendmail {
    private String apiKey="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";
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


