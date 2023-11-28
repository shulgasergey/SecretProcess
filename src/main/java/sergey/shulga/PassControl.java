package sergey.shulga;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalTime;

public class PassControl {
    private static final Logger logger = LogManager.getLogger(PassControl.class);
    final String correctPassword = "Hello";
    private static boolean daemonThreadFlag = true;

    /**
     * Daemon поток для вечного закрытия приложения до тех пор,
     * Пока не будет введен правильный пароль.
     * Для блокировки обхода пароля.
     **/
    // TODO Можно добавить проверку, чтобы daemon поток работал тогда, когда
    // TODO состояние процесса изменяется на "running" (из класса CheckProcess).
    protected void startDaemonThread(String processName) {
        Thread daemonThread = new Thread(() -> {

            while (daemonThreadFlag) {
                try {
                    System.out.println(getTime() + " " + "Daemon thread: " + processName + " is running.");
                    blockProcess(processName);

                } catch (InterruptedException | IOException e) {
                    logger.error("Daemon thread error: " + e);
                }
            }
        });

        daemonThread.setDaemon(true); // Устанавливаем поток как daemon
        daemonThread.start(); // Запускаем поток
    }

    /**
     * Закрывает процесс.
     */
    //TODO найти более мягкий способ закрытия, чтобы потом приложение не крашилось при запуске.
    private void blockProcess(String processName) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder("pkill", "-f", processName);
        Process exitProcess = processBuilder.start();
        int exitCode = exitProcess.waitFor();
        System.out.println(getTime() + " " + "Exit code: " + exitCode);

        exitProcess.destroy();

        Thread.sleep(1000);
    }

    protected void checkPass(String processName) {
        daemonThreadFlag = true;
        if (isTypedPasswordCorrect()) {
            daemonThreadFlag = false;
            AppStarter appStarter = new AppStarter();
            appStarter.startApp(processName);
        } else {
            System.out.println(getTime() + " " + "Incorrect password");
        }
    }

    private boolean isTypedPasswordCorrect() {
        return typedPassword().equals(correctPassword);
    }

    private String typedPassword() {
        System.out.println(getTime() + " " + "Type password...");

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            return reader.readLine();
        } catch (IOException e) {
            logger.error("Error: " + e);
            return "";
        }
    }

    private LocalTime getTime() {
        return LocalTime.now();
    }
}