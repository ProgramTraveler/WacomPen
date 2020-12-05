/*
    date:2020-11-30
    author:王久铭
    purpose:初始化界面
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame{
    //设置一个开始按钮
    private JButton Button=new JButton("开始");

    public Login(){
        super("Screen");
        this.getContentPane().setLayout(null);
        Button.setBounds(50,50,200,30);
        this.getContentPane().add(Button);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height-200;
        this.setBounds(width / 3, height / 5, 310, 420);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        //对开始按钮进行监听,要是按下了开始按钮就打开写字板
        Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AreaFrame frame=new AreaFrame();
            }
        });
    }

    public static void main(String[] arge){
        Login log=new Login();

    }

}