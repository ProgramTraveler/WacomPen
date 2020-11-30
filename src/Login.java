import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import javax.swing.*;

public class Login extends JFrame {
    //设置窗口按钮
    private JButton Button = new JButton("开始");
    //使用指定的文本创建一个 JLabel实例。
    private JLabel jlabel1 = new JLabel("请输入你的id号：");
    //构造新的TextField 。 创建默认模型，初始字符串为null ，列数设置为0
    private JTextField jtext1 = new JTextField();
    //使用指定的文本创建一个 JLabel实例。
    private JLabel jlabel2 = new JLabel("菜单方向：");
    //使用指定的文本创建一个未选择的单选按钮。
    private JRadioButton jRB1 = new JRadioButton("北——南");
    private JRadioButton jRB2 = new JRadioButton("西——东");
    private JRadioButton jRB3 = new JRadioButton("东北——西南");
    private JRadioButton jRB4 = new JRadioButton("西北——东南");
    //此类用于为一组按钮创建多重排除范围。 使用相同的ButtonGroup对象创建一组按钮意味着打开“其中一个”按钮会关闭组中的所有其他按钮
    private ButtonGroup buttonGroup1 = new ButtonGroup();
    //使用指定的文本创建一个 JLabel实例
    private JLabel jlabel3 = new JLabel("实验组数：");
    //使用指定的文本创建一个未选择的单选按钮。
    private JRadioButton jRB11 = new JRadioButton("Block1");
    private JRadioButton jRB12 = new JRadioButton("Block2");
    private JRadioButton jRB13 = new JRadioButton("Block3");
    private JRadioButton jRB14 = new JRadioButton("Block4");
    private JRadioButton jRB15 = new JRadioButton("Block5");
    //此类用于为一组按钮创建多重排除范围。 使用相同的ButtonGroup对象创建一组按钮意味着打开“其中一个”按钮会关闭组中的所有其他按钮
    private ButtonGroup buttonGroup2 = new ButtonGroup();
    //复选框是可以处于“开”（ true ）或“关”（ false ）状态的图形组件。 单击复选框将其状态从“开”更改为“关”，或从“关”更改为“开”
    Checkbox check = new Checkbox("练习");

    public Login() {
        //窗口标题（在界面的最左边）
        super("area experiment");
        //返回此框架的 contentPane对象。
        this.getContentPane().setLayout(null);
        //设置id按钮位置
        jlabel1.setBounds(20, 30, 135, 40);
        //设置输入id的文本框的位置
        jtext1.setBounds(120, 35, 128, 24);
        //设置当前字体
        jtext1.setFont(new Font("", Font.PLAIN, 11));
        //将id按钮和文本框添加到框架中
        this.getContentPane().add(jlabel1);
        this.getContentPane().add(jtext1);
        //设置菜单方向按钮位置
        jlabel2.setBounds(20, 75, 80, 40);
        //将菜单方向按钮添加到框架中
        this.getContentPane().add(jlabel2);
        //当选择相同的对象使用相同的ButtonGroup对象创建一组按钮，当选择其中一个按钮时会关闭组中的所有其他按钮
        buttonGroup1.add(jRB1);
        buttonGroup1.add(jRB2);
        buttonGroup1.add(jRB3);
        buttonGroup1.add(jRB4);
        //设置每个按钮的位置
        jRB1.setBounds(40, 110, 80, 20);
        jRB2.setBounds(150, 110, 80, 20);
        jRB3.setBounds(40, 155, 110, 20);
        jRB4.setBounds(150, 155, 150, 20);
        //将按钮添加到框架中
        this.getContentPane().add(jRB1);
        this.getContentPane().add(jRB2);
        this.getContentPane().add(jRB3);
        this.getContentPane().add(jRB4);
        //设置实验组号按钮的位置
        jlabel3.setBounds(20, 185, 80, 40);
        //将实验组号的按钮添加到框架
        this.getContentPane().add(jlabel3);
        //当选择相同的对象使用相同的ButtonGroup对象创建一组按钮，当选择其中一个按钮时会关闭组中的所有其他按钮
        buttonGroup2.add(jRB11);
        buttonGroup2.add(jRB12);
        buttonGroup2.add(jRB13);
        buttonGroup2.add(jRB14);
        buttonGroup2.add(jRB15);
        //设置每个按钮的位置
        jRB11.setBounds(40, 230, 80, 20);
        jRB12.setBounds(125, 230, 80, 20);
        jRB13.setBounds(210, 230, 80, 20);
        jRB14.setBounds(80, 270, 80, 20);
        jRB15.setBounds(180, 270, 80, 20);
        //将按钮添加到框架中
        this.getContentPane().add(jRB11);
        this.getContentPane().add(jRB12);
        this.getContentPane().add(jRB13);
        this.getContentPane().add(jRB14);
        this.getContentPane().add(jRB15);
        //设置复选框的位置
        check.setBounds(230, 300, 70, 30);
        //将复选框的按钮的添加到框架
        this.getContentPane().add(check);
        //设置开始按钮的位置
        Button.setBounds(10, 335, 280, 30);
        //将开始按钮添加到框架
        this.getContentPane().add(Button);
        //设置屏幕大小
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height-200;
        this.setBounds(width / 3, height / 5, 310, 420);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        /*Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (getSelection(buttonGroup1).getActionCommand() == "北——南")
                    PenData.menudire = "NN-SS";
                else if (getSelection(buttonGroup1).getActionCommand() == "西——东")
                    PenData.menudire = "WW-EE";
                else if (getSelection(buttonGroup1).getActionCommand() == "东北——西南")
                    PenData.menudire = "NE-SW";
                else if (getSelection(buttonGroup1).getActionCommand() == "西北——东南")
                    PenData.menudire = "NW-SE";
                if (getSelection(buttonGroup2).getActionCommand() == "Block1")
                    PenData.block = "1";
                else if (getSelection(buttonGroup2).getActionCommand() == "Block2")
                    PenData.block = "2";
                else if (getSelection(buttonGroup2).getActionCommand() == "Block3")
                    PenData.block = "3";
                else if (getSelection(buttonGroup2).getActionCommand() == "Block4")
                    PenData.block = "4";
                else if (getSelection(buttonGroup2).getActionCommand() == "Block5")
                    PenData.block = "5";
                if (check.getState()) {
                    PenData.ispractice = true;
                } else {
                    PenData.ispractice = false;
                }

                PenData.subject = jtext1.getText();
                if (!PenData.subject.equals("")) {
                    SwingUtilities.getRoot(Button).setVisible(false);
                    areaframe pframe = new areaframe();
                } else {
                    JOptionPane.showMessageDialog(null, "您还没有输入您的id!!");
                }
            }
        });*/
    }

    public static JRadioButton getSelection(ButtonGroup group)// 从按钮组中得到选中的按钮
    {
        for (Enumeration e = group.getElements(); e.hasMoreElements();) {
            JRadioButton b = (JRadioButton) e.nextElement();
            if (b.getModel() == group.getSelection())
                return b;
        }
        return null;
    }
    public static void main(String[] arge){
        Login log=new Login();
    }

}
