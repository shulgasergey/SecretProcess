package sergey.shulga;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PassControl {
    private static final Logger logger = LogManager.getLogger(PassControl.class);
    private static PassControl instance;

    // Приватный конструктор, чтобы предотвратить создание экземпляров извне
    private PassControl() {
    }

    // Глобальная точка доступа к единственному экземпляру класса
    public static PassControl getInstance() {
        if (instance == null) {
            instance = new PassControl();
        }
        return instance;
    }

    final String correctPassword = "Hello";

    // TODO исправить цикл while
    // сейчас он правильно работает только при условии "true"
    // Неправильно реагирует на флаг isTypedPasswordCorrect (ничего не делает)
    public void startDaemonThread(String processName) {
        Thread daemonThread = new Thread(() -> {
            while (true) {
                try {
                    System.out.println("Daemon thread: " + processName + " is running.");
                    blockProcess(processName);

                } catch (InterruptedException | IOException e) {
                    logger.error("Daemon thread error: " + e);
                }
            }
        });

        daemonThread.setDaemon(true); // Устанавливаем поток как daemon
        daemonThread.start(); // Запускаем поток
    }


    // Метод для блокировки процесса и получения пароля
    protected void blockProcess(String processName) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder("pkill", "-f", processName);
        Process exitProcess = processBuilder.start();

        // Ждем завершения процесса
        int exitCode = exitProcess.waitFor();
        System.out.println("Exit code: " + exitCode);

        // Просто завершить процесс, без перезапуска
        exitProcess.destroy();

        // Вы можете добавить задержку, если это необходимо
        Thread.sleep(1000);

    }


    protected void checkPass(String processName) {
        if (isTypedPasswordCorrect()) {
            AppStarter appStarter = new AppStarter();
            appStarter.startApp(processName);
        } else {
            System.out.println("Incorrect password");
        }
    }

    protected boolean isTypedPasswordCorrect() {
        return typedPassword().equals(correctPassword);
    }

    private String typedPassword() {
        System.out.println("Type password...");

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            return reader.readLine();
        } catch (IOException e) {
            logger.error("Error: " + e);
            return "";
        }
    }
}