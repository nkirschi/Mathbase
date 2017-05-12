package util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExceptionUtil {
    private static BufferedWriter writer;

    public static void log(Exception exception) {
        if (writer == null) {
            try {
                 writer = new BufferedWriter(new FileWriter("log.txt", true));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            String timestamp = new SimpleDateFormat("yyyy/mm/dd hh:mm:ss").format(new Date());
            writer.write(timestamp);
            writer.newLine();
            for (StackTraceElement s : exception.getStackTrace()) {
                writer.write("Exception: " + exception.getMessage());
                writer.newLine();
                writer.write("File: " + s.getFileName());
                writer.newLine();
                writer.write("Class: " + s.getClassName());
                writer.newLine();
                writer.write("Method: " + s.getMethodName());
                writer.newLine();
                writer.write("Line: " + s.getLineNumber());
                writer.newLine();
                writer.newLine();
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        RuntimeException e = new RuntimeException("Du bist böse");
        log(e);
    }
}
