package com.ppn.ppn.service.constract;

import com.ppn.ppn.payload.VerifyMailRequest;
import jakarta.mail.MessagingException;

public interface IEmailSenderService {
    void sendMail(VerifyMailRequest verifyMailRequest) throws MessagingException;
}
