//package dv512.group23;
import java.io.File;
import java.io.FileWriter;
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
public class assign2_task3 {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        new File("test-directory").mkdir();
        SimpleDateFormat timeNow = new SimpleDateFormat("HH-mm-ss-SS");
        for (int i = 0; i < 500; i++) {
            Timestamp stampNow = new Timestamp(System.currentTimeMillis());
            String time = timeNow.format(stampNow);
            String fullName = time + ".txt";
            File myFile = new File("test-directory", fullName);
            FileWriter myWriter = new FileWriter(myFile);
            for (int j = 0; j < 10000; j++) {
                myWriter.write(time + "\n");
            }
            myWriter.flush();
            myWriter.close();
            TimeUnit.MICROSECONDS.sleep(10);
        }
    }
}

