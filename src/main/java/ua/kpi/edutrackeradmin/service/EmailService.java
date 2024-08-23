package ua.kpi.edutrackeradmin.service;

public interface EmailService {
    void sendEmail(String subject, String text, String toEmail);
}