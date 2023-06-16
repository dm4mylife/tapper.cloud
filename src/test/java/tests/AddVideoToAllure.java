package tests;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.restassured.internal.util.IOUtils;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class AddVideoToAllure implements TestWatcher {

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        try {
            addVideo();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Attachment(value = "Video", type = "video/mp4")
    public void addVideo() throws IOException {

        System.out.println("run in end");

        String folderPath = "build/video"; //specify the folder path
        String filePath = folderPath + "/video_test.mp4"; //specify the file path

        File file = new File(filePath); //create a file object with the specified file path

        try {
            File folder = new File(folderPath); //create a folder object with the specified folder path
            if (!folder.exists()) { //check if the folder exists
                folder.mkdirs(); //if it doesn't exist, create it along with any intermediate directories
            }
            file.createNewFile(); //create the file

        } catch (IOException e) {
            System.err.println("An error occurred while creating the file: " + e.getMessage());
        }

        byte[] byteArr = IOUtils.toByteArray(new FileInputStream(filePath));
        Allure.addAttachment("attachment name", "video/mp4", new ByteArrayInputStream(byteArr), "mp4");

    }

}
