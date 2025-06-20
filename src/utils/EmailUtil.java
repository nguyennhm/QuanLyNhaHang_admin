package utils;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailUtil {

    public static void sendBirthdayEmail(String toEmail, String tenNhanVien) {
        final String fromEmail = "nguyennhm0304@gmail.com"; // Email người gửi
        final String password = "mugf kqgt sfdr ytso";       // Mật khẩu ứng dụng (App password của Gmail)

        String subject = "🎉 Chúc mừng sinh nhật " + tenNhanVien + "!";
        String content = "Chúc bạn " + tenNhanVien + " có một ngày sinh nhật vui vẻ và hạnh phúc!\n"
                + "Bạn đã được cộng thêm 50 điểm thưởng vì là nhân viên tuyệt vời của chúng tôi!";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2"); // Đảm bảo tương thích TLS hiện đại

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(fromEmail, "Phòng Nhân Sự"));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            msg.setSubject(subject);
            msg.setText(content);

            Transport.send(msg);
            System.out.println("✅ Đã gửi email chúc mừng sinh nhật tới: " + toEmail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
