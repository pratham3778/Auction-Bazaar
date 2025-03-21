package com.ceeras.auctionBazar.entity;

//     import lombok.AllArgsConstructor;
// import lombok.Data;
// import lombok.NoArgsConstructor;

// Class
public class EmailDetails {

    // Class data members
    private String recipient;
    private String msgBody;
    private String subject;
    private String attachment;

    // Getters and Setters

    public String getRecipient() {
        return recipient;
    }
    public String getMsgBody() {
        return msgBody;
    }
    public String getSubject() {
        return subject;
    }
    public String getAttachment() {
        return attachment;
    }
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

}
