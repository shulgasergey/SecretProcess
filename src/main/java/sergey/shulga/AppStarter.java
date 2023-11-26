package sergey.shulga;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class AppStarter {

    private static final Logger logger = LogManager.getLogger(CheckProcess.class);

    protected void startApp(String processName) {
        try {
            Process exitProcess = Runtime.getRuntime().exec("open -a " + processName);
            exitProcess.waitFor();
        } catch (InterruptedException | IOException e) {
            logger.error("Error: " + e);
        }
    }
}
