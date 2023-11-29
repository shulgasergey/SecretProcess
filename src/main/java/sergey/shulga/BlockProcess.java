package sergey.shulga;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.time.LocalTime;

public class BlockProcess {
    private static final Logger logger = LogManager.getLogger(BlockProcess.class);
    /*
    Флаг для потока-демона.
    Меняется на false, когда пароль введен правильно (больше блокировать приложение не нужно).
    Меняется на true, когда приложение было закрыто.
     */
    public static boolean daemonDestroyThreadFlag = true;

    /**
     * Daemon поток для вечного закрытия приложения до тех пор,
     * Пока не будет введен правильный пароль.
     * Для блокировки обхода пароля.
     **/
    protected void startDaemonDestroyThread(String processName) {
        Thread daemonDeleteThread = new Thread(() -> {
            while (daemonDestroyThreadFlag) {
                try {
                    System.out.println(getTime() + " " + "Daemon thread: " + processName + " is running.");
                    blockProcess(processName);
                } catch (InterruptedException | IOException e) {
                    logger.error("Daemon thread error: " + e);
                }
            }
        });

        daemonDeleteThread.setDaemon(true);
        daemonDeleteThread.start(); //
    }

    /*
     * Закрывает процесс.
     */
    private void blockProcess(String processName) throws IOException, InterruptedException {
        // Команда для закрытия приложения.
        ProcessBuilder processBuilder = new ProcessBuilder("pkill", "-f", processName);
        Process exitProcess = processBuilder.start();
        int exitCode = exitProcess.waitFor();
        exitProcess.destroy();

        System.out.println(getTime() + " " + "Exit code: " + exitCode);

        Thread.sleep(1000);
    }

    public void setDaemonDestroyThreadFlag(boolean instance) {
        daemonDestroyThreadFlag = instance;
    }

    private LocalTime getTime() {
        return LocalTime.now();
    }
}