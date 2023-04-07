package common;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.SearchTerm;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;


public class JavaxMail {

    public static void main(String[] args) throws IOException, MessagingException {

        FileInputStream fileInputStream = new FileInputStream("src/main/resources/mail.properties");
        Properties properties = new Properties();
        properties.load(fileInputStream);

        String user = properties.getProperty("test.yandex.login.mail");
        String password = properties.getProperty("test.yandex.login.password");
        String host = properties.getProperty("test.yandex.mail.host");
        String welcomeHeading = "Добро пожаловать в ";

        Properties prop = new Properties();

        prop.put("mail.store.protocol","imaps");

        Store store = Session.getDefaultInstance(prop, null).getStore();

        store.connect(host,user,password);
        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);

        Message[] messages = inbox.getMessages();

        for (Message msg:messages) {

            Object content = msg.getContent();

            if (content instanceof String messageText) {

                if (messageText.contains("Tapper")) {

                    System.out.println(messageText);

                }

            } else if (content instanceof Multipart multipart) {

                for (int i = 0; i < multipart.getCount(); i++) {

                    BodyPart bodyPart = multipart.getBodyPart(i);
                    String partContent = bodyPart.getContent().toString();

                    if (partContent.contains("Tapper")) {

                        System.out.println(partContent);

                        break;
                    }
                }
            }

        }

        inbox.close(false);
        store.close();

    }

}
