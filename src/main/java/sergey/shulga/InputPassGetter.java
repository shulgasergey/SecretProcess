package sergey.shulga;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InputPassGetter extends JFrame {
    private JFrame frame;
    private JTextField textField;
    private String typedPassword;

    protected String inputPassword(String processName) {
        frame = new JFrame("Ввод пароля");
        frame.setSize(300, 150);
        frame.setLocationRelativeTo(null);

        textField = new JTextField(20);
        JButton submitButton = new JButton("Подтвердить");

        JPanel panel = new JPanel();
        panel.add(new JLabel("Пароль: "));
        panel.add(textField);
        panel.add(submitButton);

        frame.add(panel);
        frame.setVisible(true);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                typedPassword = textField.getText();
                frame.dispose();
                System.out.println();
                PasswordChecker passwordChecker = new PasswordChecker();
                passwordChecker.checkPassword(processName, typedPassword);
            }
        });
        return typedPassword;
    }
}