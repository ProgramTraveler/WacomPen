/*
    date:2020-11-30
    author:王久铭
    purpose:初始化界面，也就是用户的最初的登陆的界面
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame{
    //设置一个开始按钮
    private JButton Button = new JButton("开始测试");
    //要求用户输入ID
    private JLabel JLabelId = new JLabel("输入你的ID:"); //用户输入`ID`的提示
    private JTextField JTexId = new JTextField(); //用户输入`ID`的输入框
    //要求用户输入实验组数
    private JLabel JLabelGroup = new JLabel("实验组数:"); //用户输入`实验组数`的提示
    private  JTextField JTexGroup = new JTextField(); //用户输入`实验组数`的输入框
    //用户对是否选择`练习`
    Checkbox check = new Checkbox("practice");
    /*
    在这里面还要写四个不同模式的写字板
     */

    public Login() {
        super("Screen");
        this.getContentPane().setLayout(null);
        //`开始测试`按钮
        Button.setBounds(10, 335, 280, 30); //`开始测试`按钮的位置
        this.getContentPane().add(Button); //添加`开始测试`按钮
        //`ID`显示区域
        JLabelId.setBounds(20,30,135,40); //`ID`提示符位置
        this.getContentPane().add(JLabelId); //添加`ID`提示符
        JTexId.setBounds(105,35,78,24); //`ID`输入框的位置
        this.getContentPane().add(JTexId); //添加`ID`文本框
        //`实验组数`显示区域
        JLabelGroup.setBounds(30,60,135,40);//`实验组数`提示符位置
        this.getContentPane().add(JLabelGroup); //添加`实验组数`提示符
        JTexGroup.setBounds(105,70,78,24); //`实验组数`输入框位置
        this.getContentPane().add(JTexGroup); //添加`实验组数`文本框
        //`练习`选择框的选择
        check.setBounds(30, 300, 70, 30); //`练习`选择框的位置
        this.getContentPane().add(check); //添加`练习`选择框
        //登录界面的位置和大小
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height - 200;
        this.setBounds(width / 3, height / 5, 310, 420);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        //对开始按钮进行监听,要是按下了开始按钮就打开写字板
        /*
        判断之前选择的是哪个写字板模式，然后打开相应的写字板，打开写字板后需要将登陆界面覆盖
         */
        Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AreaFrame frame=new AreaFrame();
            }
        });
    }

    public static void main(String[] arge){
        Login log = new Login();

    }

}