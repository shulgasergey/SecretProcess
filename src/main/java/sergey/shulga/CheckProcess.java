package sergey.shulga;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class CheckProcess {
    private static final Logger logger = LogManager.getLogger(CheckProcess.class);
    private boolean isProcessCaptured = false;

    protected void checkProcess() {
        String processName = "Telegram";

        while (!isProcessCaptured) {
            try {
                if (isProcessRunning(processName)) {
                    System.out.println(processName + " is running.");
                    PassControl passControl = PassControl.getInstance();
                    passControl.blockProcess(processName);
                    isProcessCaptured = true;
                } else {
                    System.out.println(processName + " is not running.");
                }
            } catch (InterruptedException | IOException e) {
                logger.error("Error: " + e);
            }
        }
    }

    private boolean isProcessRunning(String processName) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("pgrep", processName);
            Process process = processBuilder.start();

            // Подождать завершения процесса и получить код возврата
            int exitCode = process.waitFor();

            // Код возврата 0 означает, что процесс существует, иначе нет
            return exitCode == 0;
        } catch (IOException | InterruptedException e) {
            logger.error("Error: " + e);
            return false;
        }
    }
}