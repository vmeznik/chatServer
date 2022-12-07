package chatServer.utility;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private final File logFile;
    private static Logger instance;

    private Logger() {
        this.logFile = new File("LogFile.txt");
    }

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public void log(String txt) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        try {
            PrintWriter out = new PrintWriter(new FileWriter(logFile, true));
            out.println(dtf.format(LocalDateTime.now()) + " " + txt);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}