package sergey.shulga;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PassControl {
    private static final Logger logger = LogManager.getLogger(CheckProcess.class);
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

    // Метод для блокировки процесса и получения пароля
    protected void blockProcess(String processName) throws IOException, InterruptedException {
        Process exitProcess = Runtime.getRuntime().exec("pkill -f " + processName);
        exitProcess.waitFor();
        int exitCode = exitProcess.exitValue();
        System.out.println("Exit code: " + exitCode);

        checkPass(processName);
    }

    private void checkPass(String processName) {
        if (typedPassword().equals(correctPassword)) {
            AppStarter appStarter = new AppStarter();
            appStarter.startApp(processName);
        } else {
            System.out.println("Incorrect password");
        }
    }


    private String typedPassword() {
        System.out.println("Type password...");
        String typedPassword = "";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            return reader.readLine();
        } catch (IOException e) {
            logger.error("Error: " + e);
            return "";
        }
    }
}