//package dv512.group23;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;
/**
 * Group 23
 * @author Mohammadali Rashidfarokhi (mr223jp)
 * @author Nima Safavi (ns222tv)
 * @author Khalil Mardini (km222pw)
 */
public class assign2_task1 {
    public static void main(String[] args) throws IOException, InterruptedException {
        long pid = ProcessHandle.current().pid();
        SimpleDateFormat timeNow = new SimpleDateFormat("HH-mm-ss-SS");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        System.out.println("<PID " + pid + ">" + "<" + timeNow.format(timestamp) + ">" + "Process Started");
        FileReader fr;
        BufferedReader br;
        String line;
        while (true) {
            try {
                fr = new FileReader("/home/mr223jp/test-named-pipe");
                br = new BufferedReader(fr);
                pid = ProcessHandle.current().pid();
                timestamp = new Timestamp(System.currentTimeMillis());
                System.out.println("<PID " + pid + ">" + "<" + timeNow.format(timestamp) + ">" + "Pipe Opened");

                while ((line = br.readLine()) != null) {
                    pid = ProcessHandle.current().pid();
                    timestamp = new Timestamp(System.currentTimeMillis());
                    System.out.println("<PID " + pid + ">" + "<" + timeNow.format(timestamp) + ">" + line);
                }
                fr.close();
                pid = ProcessHandle.current().pid();
                timestamp = new Timestamp(System.currentTimeMillis());
                System.out.println("<PID " + pid + ">" + "<" + timeNow.format(timestamp) + ">" + "Pipe closed");
                TimeUnit.SECONDS.sleep(3);
            } catch (FileNotFoundException fe) {
                System.out.println("File not found");
            }
        }
    }
}
