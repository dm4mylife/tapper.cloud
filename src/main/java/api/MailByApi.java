package api;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Assertions;

import javax.mail.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static data.Constants.*;
import static data.Constants.RegexPattern.Mail.*;


public class MailByApi {

      static Store setConnection(String userProperty, String passwordProperty) throws IOException, MessagingException {

        FileInputStream fileInputStream = new FileInputStream(data.Constants.JAVAX_PROPERTIES_PATH);
        Properties properties = new Properties();
        properties.load(fileInputStream);

        String user = properties.getProperty(userProperty);
        String password = properties.getProperty(passwordProperty);
        String host = properties.getProperty("yandex.mail.host");

        Properties prop = new Properties();
        prop.put("mail.store.protocol","imaps");

        Store store = Session.getDefaultInstance(prop, null).getStore();
        store.connect(host,user,password);

        Awaitility.await().pollDelay(Duration.ofSeconds(4)).untilAsserted(store::isConnected);

        return store;

    }

     static Message[] getMessages(Store store) throws MessagingException {

        Folder folder = store.getFolder("INBOX");
        folder.open(Folder.READ_ONLY);

        return folder.getMessages();

    }

     static String findCertainMailByText(Message[] messages, String text) throws MessagingException, IOException {

        int mailCount = 0;

        LinkedHashMap<Integer,String> filteredMessages = new LinkedHashMap<>();

        for (Message msg:messages) {

            Object content = msg.getContent();

            if (content instanceof String messageText) {

                if (messageText.contains(text)) {

                    filteredMessages.put(mailCount,messageText);
                    mailCount++;

                }

            } else if (content instanceof Multipart multipart) {

                BodyPart bodyPart = multipart.getBodyPart(1);
                String textMail = bodyPart.getContent().toString();

                if (textMail.contains(text)) {

                    filteredMessages.put(mailCount,textMail);
                    mailCount++;

                }
            }
        }

        return filteredMessages.get(filteredMessages.size()-1);

    }

    static boolean findCertainMailsAndDelete(Folder folder, String typeMail)
            throws MessagingException, IOException {

        boolean isDeleted = false;
        folder.open(Folder.READ_WRITE);

        Message[] messages = folder.getMessages();

            for (Message msg:messages) {

                Object content = msg.getContent();

                if (content instanceof String messageText) {

                    if (messageText.contains(typeMail)) {

                        msg.setFlag(Flags.Flag.DELETED,true);
                        isDeleted = true;

                    }

                } else if (content instanceof Multipart multipart) {

                    if (multipart.getBodyPart(1).getContent().toString().contains(typeMail)) {

                        msg.setFlag(Flags.Flag.DELETED,true);
                        isDeleted = true;

                    }
                }
            }

            folder.close();
            return isDeleted;

    }

     static HashMap<String,String> parseAdministratorEmail(String textMail) {

         HashMap<String,String> waiterData = new HashMap<>();

         regexData(textMail,ADMINISTRATOR_LOGIN_MAIL_REGEX,waiterData,"login");
         regexData(textMail, ADMINISTRATOR_PASSWORD_MAIL_REGEX,waiterData,"password");
         regexData(textMail,AUTH_URL_MAIL_REGEX,waiterData,"url");

         return waiterData;

    }
    public static HashMap<String,String> parseWaiterEmail(String textMail) {

        HashMap<String,String> waiterData = new HashMap<>();

        regexData(textMail,WAITER_LOGIN_MAIL_REGEX,waiterData,"login");
        regexData(textMail, WAITER_PASSWORD_MAIL_REGEX,waiterData,"password");
        regexData(textMail,AUTH_URL_MAIL_REGEX,waiterData,"url");

        return waiterData;

    }

    static HashMap<String,String> parseRestorePasswordEmail(String textMail) {

        HashMap<String,String> waiterData = new HashMap<>();

        regexData(textMail,RESTORE_PASSWORD_URL_MAIL_REGEX,waiterData,"url");

        return waiterData;

    }

    public static HashMap<String,String> regexData
            (String textMail, String regex,HashMap<String,String> waiterData, String key ) {

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(textMail);

        if (matcher.find()) {

            String extractedString = matcher.group(1).trim();
            waiterData.put(key,extractedString);

        }

        return waiterData;

    }

    public static HashMap<String, String> getMailData(String mail, String password, String typeMail)
            throws MessagingException, IOException {

        Store store = setConnection(mail, password);
        Message[] messages = getMessages(store);

        HashMap<String, String> mailData = new HashMap<>();
        String mailText = findCertainMailByText(messages,typeMail);

        switch (typeMail) {

            case WAITER_REGISTRATION_EMAIL -> mailData = parseWaiterEmail(mailText);
            case ADMIN_REGISTRATION_EMAIL -> mailData = parseAdministratorEmail(mailText);
            case RESTORE_PASSWORD_REGISTRATION_EMAIL -> mailData = parseRestorePasswordEmail(mailText);

        }

        store.close();

        return mailData;

    }

    public static boolean deleteMails(String mail, String password, String typeMail)
            throws MessagingException, IOException {

        Store store = setConnection(mail, password);
        Folder folder = store.getFolder("INBOX");

        boolean isDeleted = findCertainMailsAndDelete(folder,typeMail);

        store.close();

        return isDeleted;

    }

    public static void deleteMailsByApi(String mail, String password, String typeMail) {

        Awaitility.await().pollInterval(1, TimeUnit.SECONDS)
                .atMost(20, TimeUnit.MINUTES).timeout(Duration.ofSeconds(20)).untilAsserted(
                        () -> Assertions.assertTrue(deleteMails(mail,password,typeMail)));

    }

}
