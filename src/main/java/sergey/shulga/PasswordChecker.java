package sergey.shulga;

import javax.swing.*;

public class PasswordChecker extends JFrame {
    final String correctPassword = "Hello";
    BlockProcess blockProcess = new BlockProcess();

    protected void checkPassword(String processName, String typedPassword) {

        if (typedPassword.equals(correctPassword)) {
            blockProcess.setDaemonDestroyThreadFlag(false);
            AppStarter appStarter = new AppStarter();
            appStarter.startApp(processName);
        } else {
            JOptionPane.showMessageDialog(PasswordChecker.this, "Неверный пароль: " + typedPassword);
            InputPassGetter inputPassGetter = new InputPassGetter();
            inputPassGetter.inputPassword(processName);
        }
    }


}
