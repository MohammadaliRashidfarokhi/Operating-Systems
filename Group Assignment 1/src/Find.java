import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Find {
    public static void main(String[] args)  {

        ProcessBuilder processBuilder = new ProcessBuilder();
        System.out.println("Invoking id");
        processBuilder.command("zsh","-c", "find ./etc -name 'rc*'");
        processBuilder.directory(new File("/"));
        System.out.println("directory :" +processBuilder.directory());
        try {
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();
            System.out.println("\nExited with error code : " + exitCode);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


