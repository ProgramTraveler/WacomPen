import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
/*
    date:2020-11-30
    author:王久铭
    purpose:初始化界面，也就是用户的最初的登陆的界面
 */
public class Login extends JFrame implements ActionListener{

    private String TitleFrame = "登录界面";

    private JPanel IdPanel = new JPanel();
    private JLabel IdLabel = new JLabel("name:");
    private JTextField IdTF = new JTextField(5);

    private JPanel BlocksPanel = new JPanel();
    private JLabel BlocksLabel  = new JLabel("Block:");
    private JComboBox BlockCB = new JComboBox();
    private int NumberOFBlocks = 5; //可以选择的实验组数
    private int SelectedBlock = 1; //选择的组数，默认为1组

    //保存模式按钮的轻量级容器
    private JPanel TechniquesPanel = new JPanel();
    /*
   在这里面还要写四个不同模式的写字板
    */
    private JRadioButton ButtonTraditional = new JRadioButton("传统画线模式");
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

    private String Traditional = "传统画线模式";
    private String ActualP = "P-实际值";
    private String ActualT = "T-实际值";
    private String ActualA = "A-实际值";
    private String ScatteredP = "P-离散值";
    private String ScatteredT = "T-离散值";
    private String ScatteredA = "A-离散值";
    private String IncrementP = "P-增量化";
    private String IncrementT = "T-增量化";
    private String IncrementA = "A-增量化";

    private JPanel PracticePanel = new JPanel();
    private JCheckBox PracticeCB = new JCheckBox("Practice");

    private JButton StartButton = new JButton("Start");
    private String StartCom = "Start";

    //保存实验数据
    PenData penData =new PenData();

    public Login() {
        //创建一个登录界面
        this.CreateTestFrame();

        //添加开始按钮在当前也页面的监听
        StartButton.addActionListener(this);
        StartButton.setActionCommand(StartCom);


    }
    public void CreateTestFrame() {
        //界面的名称
        this.setTitle(TitleFrame);

        this.CreateIDPanel(); //创建ID的轻量级容器
        this.CreateBlockPanel(); //创建组数的轻量级容器
        this.CreateTechniquesPanel(); //创建选择的模式的轻量级容器
        this.CreatePracticePanel(); //创建是否选择练习的轻量级容器

        //将ID和组数加入到NorJPanel的容器中
        JPanel NorthPanel = new JPanel();
        JSeparator separator = new JSeparator(SwingConstants.VERTICAL); //在ID和组数间加入一条分界线
        separator.setPreferredSize(new Dimension(5,30));

        NorthPanel.setLayout(new FlowLayout());
        NorthPanel.add(IdPanel);
        NorthPanel.add(separator);
        NorthPanel.add(BlocksPanel);

        //将选择模式和选择练习加入到CentPanel的容器中
        JPanel CenterPanel = new JPanel();
        CenterPanel.setLayout(new BorderLayout());
        CenterPanel.add(TechniquesPanel,BorderLayout.CENTER);
        CenterPanel.add(PracticePanel,BorderLayout.SOUTH);

        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(NorthPanel,BorderLayout.NORTH);
        this.getContentPane().add(CenterPanel,BorderLayout.CENTER);
        this.getContentPane().add(StartButton,BorderLayout.SOUTH);

    }
    //添加ID组件
    public void CreateIDPanel() {
        IdTF.setHorizontalAlignment(JTextField.RIGHT);
        IdPanel.setLayout(new FlowLayout());
        IdPanel.add(IdLabel);
        IdPanel.add(IdTF);
    }
    //添加组数的组件
    public void CreateBlockPanel() {
        //加入可以选择的组数的个数0-5（因为numberOfBlocks为5）
        for (int i =1; i <= NumberOFBlocks; i ++) {
            String item = " " + Integer.toString(i) + " - Block";
            BlockCB.addItem(item); //为选项列表添加选项
        }

        BlocksPanel.setLayout(new FlowLayout());
        BlocksPanel.add(BlocksLabel);
        BlocksPanel.add(BlockCB);
    }
    //添加模式的组件
    public void CreateTechniquesPanel() {
        //添加选择模式的按钮
        JPanel EastPanel = new JPanel();
        EastPanel.setLayout(new BoxLayout(EastPanel,BoxLayout.Y_AXIS));
        EastPanel.add(ButtonTraditional);
        EastPanel.add(ButtonActualP);
        EastPanel.add(ButtonActualT);
        EastPanel.add(ButtonActualA);
        EastPanel.add(ButtonScatteredP);
        EastPanel.add(ButtonScatteredT);
        EastPanel.add(ButtonScatteredA);
        EastPanel.add(ButtonIncrementP);
        EastPanel.add(ButtonIncrementT);
        EastPanel.add(ButtonIncrementA);

        //划出一个区域，将这些模式圈起来
        TechniquesPanel.setBorder(new TitledBorder(new LineBorder(Color.lightGray),"Selection Techniques",TitledBorder.LEFT,TitledBorder.TOP));
        TechniquesPanel.setLayout(new BorderLayout());
        TechniquesPanel.add(EastPanel);
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
        //获取选择的模式

    }
    //添加练习的组件
    public void CreatePracticePanel() {
        PracticePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        PracticePanel.add(PracticeCB);
    }
    public void SetInputId(String id) {
        IdTF.setText(id);
    }
    public void SetSelectBlock(int Block) {
        BlockCB.setSelectedIndex(Block - 1); // 选择索引 Block - 1 处的项
    }
    public void SetSelectTechnique(String s) {

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
    @Override
    public void actionPerformed(ActionEvent e) {

        SelectedBlock = BlockCB.getSelectedIndex() + 1; //返回列表中与给定项匹配的第一个选项
        //根据选择的按钮，进入到不同的模式中
        if(getSelection(ButtonFrame).getActionCommand() == "传统画线模式") {
            TraditionalFrame traditionalFrame = new TraditionalFrame(SelectedBlock);
        }else if (getSelection(ButtonFrame).getActionCommand() == "P-实际值") {
            ActualPress actualPress = new ActualPress(SelectedBlock);
        }else if (getSelection(ButtonFrame).getActionCommand() == "T-实际值") {
            ActualTilt actualTilt = new ActualTilt(SelectedBlock);
        }else if (getSelection(ButtonFrame).getActionCommand() == "A-实际值") {
            ActualAzimuth actualAzimuth = new ActualAzimuth(SelectedBlock);
        }else if (getSelection(ButtonFrame).getActionCommand() == "P-离散值") {
            ScatteredPress scatteredPress = new ScatteredPress(SelectedBlock);
        }else if (getSelection(ButtonFrame).getActionCommand() == "T-离散值") {
            ScatteredTilt scatteredTilt = new ScatteredTilt(SelectedBlock);
        }else if (getSelection(ButtonFrame).getActionCommand() == "A-离散值") {
            ScatteredAzimuth scatteredAzimuth = new ScatteredAzimuth(SelectedBlock);
        }else if (getSelection(ButtonFrame).getActionCommand() == "P-增量化") {

        }else if (getSelection(ButtonFrame).getActionCommand() == "T-增量化") {

        }else if (getSelection(ButtonFrame).getActionCommand() == "A-增量化") {

        }

        //将在登录界面的相关信息保留
        penData.SetName(IdTF.getText()); //用户输入的名称
        penData.SetBlock(SelectedBlock); //用户选择的组数
        penData.SetModeNa(getSelection(ButtonFrame).getActionCommand()); //用户选择的模式

        penData.SetTrialN(0); //初始化实验组数
        penData.SetTouchE(0); //初始化实验误触发数
        penData.SetModeE(0); //初始化模式切换错误数

        this.dispose();
    }
}
