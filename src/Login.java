import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
/*
    date:2020-11-30
    author:王久铭
    purpose:初始化界面，也就是用户的最初的登陆的界面
 */
public class Login extends JFrame{
    //新建窗口
    private JFrame Screen = new JFrame("登录界面");
    //设置一个开始按钮
    private JButton Button = new JButton("开始测试");
    //要求用户输入ID
    private JLabel JLabelId = new JLabel("输入你的名称:"); //用户输入`ID`的提示
    private JTextField JTexId = new JTextField(); //用户输入`ID`的输入框
    //要求用户输入实验组数
    private JLabel JLabelGroup = new JLabel("实验组数:"); //用户输入`实验组数`的提示
    private  JTextField JTexGroup = new JTextField(); //用户输入`实验组数`的输入框
    //用户对是否选择`练习`
    Checkbox check = new Checkbox("practice");
    //提示用户选择模式
    private JLabel JLabelMode = new JLabel("选择模式:");
    /*
    在这里面还要写四个不同模式的写字板
     */
    private JRadioButton ButtonTraditional = new JRadioButton("传统写字面板模式");
    //实际值模式下的选择按钮
    private JRadioButton ButtonActualP =  new JRadioButton("P-实际值");
    private JRadioButton ButtonActualT = new JRadioButton("T-实际值");
    private JRadioButton ButtonActualA = new JRadioButton("A-实际值");
    //离散值模式下的选择按钮
    private JRadioButton ButtonScatteredP = new JRadioButton("P-离散值");
    private JRadioButton ButtonScatteredT = new JRadioButton("T-离散值");
    private JRadioButton ButtonScatteredA = new JRadioButton("A-离散值");
    //增量化模式下的选择按钮
    private JRadioButton ButtonIncrementP = new JRadioButton("P-增量化");
    private JRadioButton ButtonIncrementT = new JRadioButton("T-增量化");
    private JRadioButton ButtonIncrementA = new JRadioButton("A-增量化");

    private ButtonGroup ButtonFrame = new ButtonGroup(); //保存写字按钮组

    public Login() {
        Screen.getContentPane().setLayout(null);
        //`开始测试`按钮
        Button.setBounds(10, 335, 280, 30); //`开始测试`按钮的位置
        Screen.getContentPane().add(Button); //添加`开始测试`按钮
        //`ID`显示区域
        JLabelId.setBounds(20,30,135,40); //`ID`提示符位置
        Screen.getContentPane().add(JLabelId); //添加`ID`提示符
        JTexId.setBounds(105,35,78,24); //`ID`输入框的位置
        Screen.getContentPane().add(JTexId); //添加`ID`文本框
        //`实验组数`显示区域
        JLabelGroup.setBounds(30,60,135,40);//`实验组数`提示符位置
        Screen.getContentPane().add(JLabelGroup); //添加`实验组数`提示符
        JTexGroup.setBounds(105,70,78,24); //`实验组数`输入框位置
        Screen.getContentPane().add(JTexGroup); //添加`实验组数`文本框
        //`练习`选择框的选择
        check.setBounds(30, 300, 70, 30); //`练习`选择框的位置
        Screen.getContentPane().add(check); //添加`练习`选择框
        //选择模式的文字位置
        JLabelMode.setBounds(20,120,180,15);
        Screen.add(JLabelMode);
        /*
        设置四种不同写字界面的位置
         */
        //传统模式
        ButtonTraditional.setBounds(20,160,180,15); //传统写字界面按钮位置
        Screen.getContentPane().add(ButtonTraditional); //添加到窗口
        //三个不同实际值模式
        ButtonActualP.setBounds(20,200,90,15); //P-实际值界面按钮位置
        Screen.getContentPane().add(ButtonActualP); //添加到窗口
        ButtonActualT.setBounds(110,199,80,15); //T-实际值界面按钮位置
        Screen.getContentPane().add(ButtonActualT);
        ButtonActualA.setBounds(210,198,80,15); //A-实际值界面按钮位置
        Screen.getContentPane().add(ButtonActualA);
        //三个不同离散值模式
        ButtonScatteredP.setBounds(20,240,80,15); //P-离散化界面按钮位置
        Screen.getContentPane().add(ButtonScatteredP); //添加到窗口
        ButtonScatteredT.setBounds(110,239,80,15); //T-离散化界面按钮位置
        Screen.getContentPane().add(ButtonScatteredT);
        ButtonScatteredA.setBounds(210,238,80,15); //A-离散化界面按钮位置
        Screen.getContentPane().add(ButtonScatteredA);
        //三个不同增量化模式
        ButtonIncrementP.setBounds(20,280,80,15); //P-增量化界面按钮位置
        Screen.getContentPane().add(ButtonIncrementP); //添加到窗口
        ButtonIncrementT.setBounds(110,279,80,15); //T-增量化界面按钮位置
        Screen.getContentPane().add(ButtonIncrementT);
        ButtonIncrementA.setBounds(210,278,80,15); //A-增量化界面按钮位置
        Screen.getContentPane().add(ButtonIncrementA);
        /*
        将按钮添加到按钮组里
         */
        //传统模式
        ButtonFrame.add(ButtonTraditional);
        //实际值模式
        ButtonFrame.add(ButtonActualP);
        ButtonFrame.add(ButtonActualT);
        ButtonFrame.add(ButtonActualA);
        //离散值模式
        ButtonFrame.add(ButtonScatteredP);
        ButtonFrame.add(ButtonScatteredT);
        ButtonFrame.add(ButtonScatteredA);
        //增量化模式
        ButtonFrame.add(ButtonIncrementP);
        ButtonFrame.add(ButtonIncrementT);
        ButtonFrame.add(ButtonIncrementA);
        //登录界面的位置和大小
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height - 200;
        Screen.setBounds(width / 3, height / 5, 310, 420);
        Screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Screen.setVisible(true);
        //对开始按钮进行监听,要是按下了开始按钮就打开写字板
        /*
        判断之前选择的是哪个写字板模式，然后打开相应的写字板，打开写字板后需要将登陆界面覆盖
         */
        Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(getSelection(ButtonFrame).getActionCommand() == "传统写字面板模式") {
                    TraditionalFrame TF = new TraditionalFrame();
                }else if (getSelection(ButtonFrame).getActionCommand() == "实列化模式"){
                    //AreaFrame frame=new AreaFrame();
                }else if (getSelection(ButtonFrame).getActionCommand() == "离散化模式"){
                    //AreaFrame frame=new AreaFrame();
                }else if (getSelection(ButtonFrame).getActionCommand() == "增量化模式"){
                    //AreaFrame frame=new AreaFrame();
                }
                /*
                本来想在这里加一个对选择按钮的判断，当用户没有选择模式的会进行提醒
                else if (getSelection(ButtonFrame).getActionCommand() == null){
                    JOptionPane.showMessageDialog(null, "您还没有输入您的id!!");
                }*/
                //用户是否选择练习按钮
                if (check.getState()) {

                }else {

                }
                PenData penData = new PenData();
                //获取用户输入的用户名
                penData.SetName(JTexId.getText());
                //获取用户输入的组数
                penData.SetBlock(JTexGroup.getText());
                //获取用户选择的模式
                penData.SetModeNa(getSelection(ButtonFrame).getActionCommand());

                //关闭当前登录界面，当打开写字面板的时候，将会被关闭
                Screen.dispose();
            }
        });

    }
    //从按钮组中获得选择的按钮
    public static JRadioButton getSelection (ButtonGroup group) {
        for(Enumeration e = group.getElements(); e.hasMoreElements();) {
            JRadioButton b = (JRadioButton) e.nextElement();
            if (b.getModel() == group.getSelection())
                return b;
        }
        return null;

    }
    public static void main(String[] arge){
        Login login = new Login();

    }

}