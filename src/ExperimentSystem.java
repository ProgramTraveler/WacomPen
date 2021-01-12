import javax.swing.*;

/*
    date:2021-01-12
    author:王久铭
    purpose:用来启动系统
 */
public class ExperimentSystem {
    public static void main(String[] args) {
        Login login = new Login();

        login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        login.pack();
        login.setLocationRelativeTo(login);
        login.setResizable(false);
        login.setVisible(true);
    }
}
