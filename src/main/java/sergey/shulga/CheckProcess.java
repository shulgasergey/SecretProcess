package sergey.shulga;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CheckProcess {

    private static final Logger logger = LogManager.getLogger(CheckProcess.class);
    private boolean isPassCheckDone = false;

    protected void startChecking() {
        // Daemon поток для проверки запущен ли процесс
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::checkProcess, 0, 1, TimeUnit.SECONDS);
    }

    private void checkProcess() {
        String processName = "Telegram";

        if (isProcessRunning(processName) && !isPassCheckDone) {
            System.out.println(getTime() + " " + processName + " is running. Should be blocked.");
            PassControl passControl = new PassControl();
            passControl.startDaemonThread(processName);

            isPassCheckDone = true;
            passControl.checkPass(processName);
        } else if (isProcessRunning(processName) && isPassCheckDone) {
            System.out.println(getTime() + " " + processName + " is running. Pass check had done");
        } else {
            System.out.println(getTime() + " " + (processName) + " is not running");
            isPassCheckDone = false;
        }
    }


    private boolean isProcessRunning(String processName) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("pgrep", processName);
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            return exitCode == 0;
        } catch (IOException | InterruptedException e) {
            logger.error("Error: " + e);
            return false;
        }
    }

    private LocalTime getTime() {
        return LocalTime.now();
    }
}