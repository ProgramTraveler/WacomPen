import cello.tablet.JTablet;
import cello.tablet.JTabletException;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
    date:2021-02-08
    author:王久铭
    purpose:压力增量化界面，主要是通过压力的不断变化来进行颜色和像素的选择
 */
public class IncrementPress extends JFrame implements ActionListener, MouseInputListener, KeyListener {
    private int time = 50; //更新时间为50毫秒
    private Timer timer = new Timer(time,this); //以每50毫秒触发一次actionPerformed触发器
    private PIExperimentPanel piExperimentPanel = new PIExperimentPanel(); //创建PIExperimentPanel类
    private boolean ChooseFlag = false; //是否显示压力动态图像
    private int CurrentPress = -1; //获取当前的压力值
    private int TriggerPress = 876; //预设的压力值，当比这个值大的时候就会触发选择菜单

    //下面这个两个boolean的值是用来检测颜色和像素切换是否合法
    private boolean ColorChange = false; //在进入到颜色的测试区域后，变为true
    private boolean PixelChange = false; //在进入到像素的测试区域后，变为true

    private boolean MenuFlag = false; //是否展开选择菜单
    private boolean MenuMove = true; //是否菜单的弹出位置随着鼠标位置改变
    private int NumberOfMenuItem = 2; //一个有2个可以选择的菜单栏,分别是颜色和像素
    private int MenuX = 0;
    private int MenuY = 0;
    private int MenuWith = 50; //菜单的宽
    private int MenuHeight = 40; //菜单的高

    //压力实列化界面的定义
    private JFrame IncrementPFrame = new JFrame("P-增量化界面");

    //设置画笔的初始颜色
    private int SetColor = 0;
    //设置画笔的初始像素
    private int SetPixel = 0;

    private CompleteExperiment completeExperiment = new CompleteExperiment();
    private JTablet tablet = null;

    //储存实验信息
    private PenData penData = new PenData();
    //获取笔的实时数据
    private PenValue penValue = new PenValue();

    private double x0,y0; //每一次笔尖开始的位置
    private double x1,y1; //每一次笔尖结束的位置

    private JPanel PIInter = new JPanel(); //提示信息区域
    private JPanel PIDraw = new JPanel(); //画线区域

    /*
        提示标签和提示语句
            当前的颜色和像素
    */
    //做为当前颜色的提示标签
    private JLabel ShowColorL = new JLabel("当前颜色:");
    //设置当前的提示颜色块（默认的初始的颜色为黑色）
    private JPanel ShowColorBlock = new JPanel();
    //设置当前颜色的像素标签
    private JLabel ShowPixelL = new JLabel("当前像素:");
    //展示像素值
    private JLabel ShowPixel = new JLabel();
    //像素语句，用于提示当前的像素值
    private String StringPixel = "1.0";
    /*
    提示标签和语句
        应该切换的颜色和像素
     */
    //切换颜色的提示
    private JLabel ShowColorT = new JLabel("目标颜色");
    //颜色提示块
    private JPanel JPanelRandomC = new JPanel();
    //切换像素显示
    private JLabel ShowPixelT = new JLabel("目标像素");
    //展示目标像素值
    private JLabel JPanelRandomP = new JLabel();
    //像素提示语句
    private String RandomPixel = "";

    private boolean PromptFlag = false;
    private JLabel Prompt = new JLabel("即将进入菜单");

    //用户是否第一次进入颜色测试区域，true表示未进入
    private boolean ColorFlag = true;
    //判断用户是否第一次进入像素测试区域，true表示未进入
    private boolean PixelFlag = true;

    public IncrementPress(int BlockNumber) {
        piExperimentPanel.setLayout(new BorderLayout());
        piExperimentPanel.addMouseListener(this);
        piExperimentPanel.addMouseMotionListener(this);
        piExperimentPanel.addKeyListener(this);

        completeExperiment.SetRandomNUmber(); //生成测试的随机数
        completeExperiment.SetRandomC(); //生成颜色提示语句
        completeExperiment.SetRandomP(); //生成像素提示语句
        completeExperiment.SetExperimentB(BlockNumber);


        this.CreateIPFrame();

        piExperimentPanel.requestFocusInWindow(); //让其获得焦点，这样才能是键盘监听能够正常使用

        try {
            tablet = new JTablet();
        } catch (JTabletException e) {
            e.printStackTrace();
        }
        timer.start();
        timer.stop();
    }
    public void CreateIPFrame() {
        this.CreatePIInter();
        this.CreatePIDraw();
        /*
        将界面分割为两部分
         */
        JSplitPane jSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,false,PIInter,PIDraw); //这里第一个参数是控制分割线竖直，第二个参数是当你拖曳切割面版的分隔线时，窗口内的组件是否会随着分隔线的拖曳而动态改变大小，最后两个参数就是我分割完成后分割线两边各添加哪个容器。
        jSplitPane.setDividerLocation(300); //分割线的位置  也就是初始位置
        jSplitPane.setOneTouchExpandable(false); //是否可展开或收起，在这里没用
        jSplitPane.setDividerSize(0);//设置分割线的宽度 像素为单位(这里设为0，择时不显示分割线)
        jSplitPane.setEnabled(false); //设置分割线不可拖动！！
        IncrementPFrame.add(jSplitPane);  //加入到面板中就好了

        //界面全屏设置
        IncrementPFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        IncrementPFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        IncrementPFrame.setVisible(true);
    }
    public void CreatePIInter() {
        //上半部分的界面,背景颜色为默认颜色
        PIInter.setLayout(null); //不采用布局管理器,由坐标定位

        //当前颜色提示标签
        ShowColorL.setBounds(520,200,100,20);
        ShowColorL.setFont(new Font("楷体",Font.BOLD,20));
        PIInter.add(ShowColorL);
        //当前颜色（颜色块）
        ShowColorBlock.setBounds(620,200,60,20);
        ShowColorBlock.setBackground(Color.BLACK);
        PIInter.add(ShowColorBlock);

        //当前像素提示标签
        ShowPixelL.setBounds(903,200,100,20);
        ShowPixelL.setFont(new Font("楷体",Font.BOLD,20));
        PIInter.add(ShowPixelL);
        //当前像素
        ShowPixel.setBounds(1003,200,100,20);
        ShowPixel.setText(StringPixel);
        ShowPixel.setHorizontalAlignment(ShowPixel.LEFT);
        ShowPixel.setFont(new Font("黑体",Font.BOLD,20));
        PIInter.add(ShowPixel);

        Prompt.setFont(new Font("楷体",Font.BOLD,20));
        Prompt.setBounds(1130,250,500,20);

    }
    public void CreatePIDraw() {
        PIDraw.setLayout(new BorderLayout());
        PIDraw.setBackground(Color.WHITE);
        PIDraw.add(piExperimentPanel,BorderLayout.CENTER);
    }
    //重绘PIInter界面(将提示的移除掉)
    public void RemoveRandom() {
        PIInter.removeAll();
        PIInter.repaint();
        //将颜色提示语句和颜色块隐藏
        ShowColorT.setBounds(0,0,0,0);
        JPanelRandomC.setBounds(0,0,0,0);
        //将像素提示语句和像素值隐藏
        ShowPixelT.setBounds(0,0,0,0);
        JPanelRandomP.setBounds(0,0,0,0);
        //将笔的当前的提示颜色变为黑色
        ShowColorBlock.setBackground(Color.BLACK);
        //将笔的当前提示像素变为1.0
        StringPixel = "1.0";
        ShowPixel.setText(StringPixel);
        //当前颜色和像素的展示
        PIInter.add(ShowColorL);
        PIInter.add(ShowColorBlock);
        PIInter.add(ShowPixelL);
        PIInter.add(ShowPixel);
        PIInter.revalidate();
        //将笔的颜色变为黑色
        piExperimentPanel.DefineColor();
        //将笔的像素变为1.0
        piExperimentPanel.DefinePixel();
    }
    //重绘PIInter界面
    public void RepaintPIInter() {
        PIInter.removeAll();
        PIInter.repaint();

        //当前颜色和像素的展示
        PIInter.add(ShowColorL);
        PIInter.add(ShowColorBlock);
        PIInter.add(ShowPixelL);
        PIInter.add(ShowPixel);
        //目标颜色和像素的展示
        PIInter.add(ShowColorT);
        PIInter.add(JPanelRandomC);
        PIInter.add(ShowPixelT);
        PIInter.add(JPanelRandomP);
        if (PromptFlag)
            PIInter.add(Prompt);

        PIInter.revalidate();
    }
    //当压力到达时弹出的选择框
    public void ProcessTriggerSwitch() {
        MenuFlag = true; //展开选择菜单栏
        piExperimentPanel.SetOpenMenu(MenuFlag); //打开颜色和像素的选择菜单
        piExperimentPanel.repaint(); //重绘
    }
    //根据用户的当前的鼠标位置来计算出用户选择的是菜单中的哪块区域
    public int CheckSelectMenuItem(int x,int y) {
        int MenuItem = -1;
        int tempY = MenuY;

        for (int i = 0; i < NumberOfMenuItem; i ++) {
            if ((MenuX - MenuWith) <= x && (MenuX >= x)) {
                if ((MenuY <= y) && (tempY + MenuHeight) >= y) {
                    MenuItem = i;
                    break;
                }
            }
            tempY += MenuHeight;
        }
        //如果选择的是颜色菜单，打开颜色的分支菜单显示，关闭像素分支菜单显示
        if (MenuItem == 0) {
            piExperimentPanel.SetShowColorMenu(true);
            piExperimentPanel.SetShowPixelMenu(false);
        }
        //如果选择的像素菜单，打开像素分支菜单显示，关闭颜色分支菜单显示
        else if (MenuItem == 1) {
            piExperimentPanel.SetShowPixelMenu(true);
            piExperimentPanel.SetShowColorMenu(false);
        }else {
            //什么也不做
        }
        return MenuItem;
    }
    //判断用户选择的是哪个颜色
    public int CheckColorItem(int x, int y) {
        int ColorItem = -1;
        int tempY = MenuY;
        for (int i = 0; i < 3; i ++) {
            if ((MenuX - MenuWith * 2) <= x && (MenuX - MenuWith >= x)) {
                if ((MenuY <= y) && (tempY + MenuHeight) >= y) {
                    ColorItem = i;
                    break;
                }
            }
            tempY += MenuHeight;
        }
        return ColorItem;
    }
    //判断用户选择的是哪个像素
    public int CheckPixelItem(int x, int y) {
        int PixelItem = -1;
        int tempY = MenuY;
        for (int i = 0; i < 3; i ++) {
            if ((MenuX - MenuWith * 2) <= x && (MenuX - MenuWith >= x)) {
                if ((MenuY + MenuHeight<= y) && (tempY + MenuHeight * 2) >= y) {
                    PixelItem = i;
                    break;
                }
            }
            tempY += MenuHeight;
        }
        return PixelItem;

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        CurrentPress = penValue.Pressure();
        //如果当前的压力值超过了预设的压力值
        if (801 <= CurrentPress) {
            timer.stop(); //停止触发actionPerformed
            piExperimentPanel.SetShowBack(false);
            MenuMove = false; //当压力到达到指定值后，菜单位置就固定了
            this.ProcessTriggerSwitch(); //当压力到达规定值时，弹出选择框
            penData.SetPressure(penValue.Pressure());
            penData.SetTilt(penValue.Tilt());
            penData.SetAzimuth(penValue.Azimuth());
        }else {
            //没超过的话就展示压力的动态图像
            piExperimentPanel.SetCurrentPress(CurrentPress);
            piExperimentPanel.repaint();
        }
        if (CurrentPress >= 701 && CurrentPress < 801) {
            PromptFlag = true;
        }else {
            PromptFlag = false;
            PIInter.removeAll();
        }
        this.RepaintPIInter();
    }
    @Override
    public void keyTyped(KeyEvent e) {

    }
    @Override
    public void keyPressed(KeyEvent e) {
        //如果用户按下ALT键，说明要开始切换
        /*if (e.getKeyCode() == KeyEvent.VK_ALT) {
            ChooseFlag = true;
        }*/
        //当一次实验完成，用户按下回车键
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            //更新颜色和像素条件
            ColorFlag = true;
            PixelFlag = true;
            //清空集合中的点的信息
            piExperimentPanel.arrayListSpot.clear();
            //重绘
            piExperimentPanel.repaint();
            //将检查是否进入颜色和像素测试区域的变量设为false
            ColorChange = false;
            PixelChange = false;
            //将提示语句移除
            this.RemoveRandom();
            //在一组中做完一次实验
            penData.AddTrialN();
            //模式切换出现的错误数
            penData.AddModeE();
            //将笔的压力保存在指定文件中
            try {
                penData.AllocateTime();
                penData.AllocateTimeString();
                penData.SaveInformation();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            //判断一组实验是否做完
            if (completeExperiment.GetList() == true) {

                //打开一个新的登录界面
                Login login = new Login();
                login.SetInputId("");
                login.SetSelectBlock(1);
                login.SetSelectTechnique("");

                login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                login.pack();
                login.setLocationRelativeTo(login);
                login.setResizable(false);
                login.setVisible(true);

                //关闭当前的界面
                IncrementPFrame.dispose();

            }
            penData.SetColorTouchE(0); //初始化颜色误触发数
            penData.SetPixelTouchE(0); //初始化像素误触发数

            penData.SetColorModeE(0); //初始化颜色切换错误数
            penData.SetPixelModeE(0); //初始化像素切换错误数
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }
    @Override
    public void mousePressed(MouseEvent e) {
        if (javax.swing.SwingUtilities.isLeftMouseButton(e)) {
            //如果要显是动态压力图像
            /*if (ChooseFlag) {
                timer.restart();
                piExperimentPanel.SetShowBack(true);
            }*/
            timer.restart();
            piExperimentPanel.SetShowBack(true);

            //获得开始时鼠标的位置
            x0 = e.getX();
            y0 = e.getY();
            /*
            获得落笔的时间
             */
            //获得落笔的时间戳
            penData.AddTime(System.currentTimeMillis());
            if (ColorFlag && PixelFlag) {
                //获得落笔的文字格式
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SS");
                penData.AddTimeString(dateFormat.format(new Date()));
            }
            //如果菜单位置可以随着鼠标位置改变，那么就实时跟新菜单的出现位置
            if (MenuMove) {
                MenuX = e.getX();
                MenuY = e.getY();
                //记录菜单出现的位置
                piExperimentPanel.SetMenuX_Y(MenuX,MenuY);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //当颜色和像素都已经做过了，此时抬笔，说明已经是最后一次抬笔了
        if (ColorFlag == false && PixelFlag == false) {
            penData.AddTime(System.currentTimeMillis());
            //获得抬笔的文字格式
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SS");
            penData.AddTimeString(dateFormat.format(new Date()));
        }
        piExperimentPanel.repaint();
        //当抬笔后说明已经选择完成
        MenuFlag = false; //此时关闭显示菜单
        piExperimentPanel.SetOpenMenu(false); //在显示界面关闭界面显示
        ChooseFlag = false; //不显示压力动态图像
        piExperimentPanel.SetShowBack(false); //打开显示压力的动态显示
        MenuMove = true; //菜单位置跟随鼠标变化
        piExperimentPanel.RemoveAllJLabel(); //清除颜色和像素提示标签
        piExperimentPanel.SetSelectPixelItem(-1); //初始化像素分支选择
        piExperimentPanel.SetSelectColorItem(-1); //初始化颜色分支选择
        //对压力值重新获取
        try {
            tablet.poll();
        } catch (JTabletException e1) {
            e1.printStackTrace();
        }
        timer.stop();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
       /*
        应该每次拖动时都会产生一个点对象
         */
        //获得笔在拖动时的坐标
        x1 = e.getX();
        y1 = e.getY();
        //点的位置，用来为压力的显示提供位置信息
        piExperimentPanel.SetShowPoint(new Point((int)x0,(int)y0));
        //获得颜色切换的颜色值
        SetColor = piExperimentPanel.GetSetColor();
        //获得像素切换的像素值
        SetPixel = piExperimentPanel.GetSetPixel();
        //点的位置，是用来为选择菜单的显示提供位置信息
        if (MenuMove) {
            MenuX = e.getX();
            MenuY = e.getY();
            //记录菜单出现的位置
            piExperimentPanel.SetMenuX_Y(MenuX,MenuY);
        }
        //如果要求打开选择菜单，此时不记录笔的轨迹信息
        if (MenuFlag) {
            //在每次移动的时候对颜色分支和像素分支进行移除和重组
            piExperimentPanel.RemoveItemJLabel();
            //通过当前点的位置来计算用户选择的是菜单栏中的哪个区域
            piExperimentPanel.SetSelectMenuItem(this.CheckSelectMenuItem(e.getX(),e.getY()));
            //如果颜色的分支菜单被打开
            if (piExperimentPanel.GetShowColorMenu()) {
                //传入具体是哪个颜色被选择
                piExperimentPanel.SetSelectColorItem(this.CheckColorItem(e.getX(), e.getY()));
                //如果颜色提示还没有出现就调用，颜色误触发加一
                if (ColorChange == false && piExperimentPanel.GetSelectColorItem() != -1) {
                    penData.AddColorTouchE(); //颜色误触发加一
                    penData.AddTouchE(); //误触发总数加一
                }
                int tempC = piExperimentPanel.GetSelectColorItem();
                if (tempC == 0) {
                    penData.SetResultC("蓝色");
                    ShowColorBlock.setBackground(Color.BLUE);
                    penData.SetEndColorMode(System.currentTimeMillis());
                }
                if (tempC == 1) {
                    penData.SetResultC("红色");
                    ShowColorBlock.setBackground(Color.RED);
                    penData.SetEndColorMode(System.currentTimeMillis());
                }
                if (tempC == 2) {
                    penData.SetResultC("黄色");
                    ShowColorBlock.setBackground(Color.ORANGE);
                    penData.SetEndColorMode(System.currentTimeMillis());
                }
                this.RepaintPIInter();

            }
            //如果像素的分支菜单被打开
            if (piExperimentPanel.GetShowPixelMenu()) {
                //传入具体的哪个像素被选择
                piExperimentPanel.SetSelectPixelItem(this.CheckPixelItem(e.getX(), e.getY()));
                //如果像素提示还未出现就切换，像素误触发加一
                if (PixelChange == false && piExperimentPanel.GetSelectPixelItem() != -1) {
                    penData.AddPixelTouchE(); //像素误触发加一
                    penData.AddTouchE(); //误触发总数加一
                }
                int tempP = piExperimentPanel.GetSelectPixelItem();
                if (tempP == 0) {
                    penData.SetResultP("2.0");
                    ShowPixel.setText("2.0");
                    penData.SetEndPixelMode(System.currentTimeMillis());
                }
                if (tempP == 1) {
                    penData.SetResultP("3.0");
                    ShowPixel.setText("3.0");
                    penData.SetEndPixelMode(System.currentTimeMillis());
                }
                if (tempP == 2) {
                    ShowPixel.setText("4.0");
                    penData.SetResultP("4.0");
                    penData.SetEndPixelMode(System.currentTimeMillis());
                }
                this.RepaintPIInter();
            }
            piExperimentPanel.repaint();

        }else if (ChooseFlag == false && MenuFlag == false){
            //当不进行功能切换和菜单选择时，才会进行画线操作
            Dot dot = new Dot();
            dot.SetStarDot(x0,y0);
            dot.SetEndDot(x1,y1);
            dot.SetColor(SetColor); //点的颜色
            dot.SetPixel(SetPixel); //点的像素

            double x = dot.DotStarX();
            double y = dot.DotStarY();

            if (x >= 583 && x < 966 && y >= 5 && y <= 105 && ColorFlag == true) {
                penData.AddTime(System.currentTimeMillis()); //线条绘制结束
                ColorChange = true; //当进入到颜色测试区域时，颜色测换才合法
                penData.SetStartColorMode(System.currentTimeMillis());
                int indexC = completeExperiment.GetRandomNumberC();
                String StringRandomC = completeExperiment.GetRandomC(indexC);
                //System.out.println("进入颜色区域");
                //按照系统的提示颜色存入相应的目标颜色
                if (StringRandomC == "请切换颜色为蓝色") {
                    penData.SetTargetColor("蓝色");
                    JPanelRandomC.setBackground(Color.BLUE);
                }
                else if (StringRandomC == "请切换颜色为红色") {
                    penData.SetTargetColor("红色");
                    JPanelRandomC.setBackground(Color.RED);
                }
                else if (StringRandomC == "请切换颜色为黄色") {
                    penData.SetTargetColor("黄色");
                    JPanelRandomC.setBackground(Color.ORANGE);
                }
                //如果没有提示且按下了空格，就记为空
                else penData.SetTargetColor(null);

                //设置插件位置
                ShowColorT.setBounds(520,250,100,20);
                ShowColorT.setFont(new Font("楷体",Font.BOLD,20));

                JPanelRandomC.setBounds(620,250,60,20);
                //将插件添加到TFInter中
                PIInter.add(ShowColorT);
                PIInter.add(JPanelRandomC);
                //重绘TFInter界面
                this.RepaintPIInter();
                ColorFlag = false;
            } else if (x0 >= 583 && x0 < 966 && y0 >= 50 && y0 <= 150 && ColorFlag == false){

            }else {
                //ColorFlag = true;
            }

            if (x0 >= 966 && x0 <= 1350 && y0 >= 5 && y0 <= 105 && PixelFlag == true) {
                penData.AddTime(System.currentTimeMillis()); //线条绘制结束
                PixelChange = true; //当进入到像素测试区域时，此时的像素测换才合法
                penData.SetStartPixelMode(System.currentTimeMillis());
                int indexP = completeExperiment.GetRandomNumberP();
                String StringRandomP = completeExperiment.GetRandomP(indexP);
                //System.out.println(StringRandomP);
                //按照系统提示的像素存入目标像素
                if (StringRandomP == "请切换像素为2.0") {
                    //System.out.println("-");
                    RandomPixel = "2.0";
                    penData.SetTargetLine("2.0");
                }
                else if (StringRandomP == "请切换像素为3.0") {
                    //System.out.println("--");
                    RandomPixel = "3.0";
                    penData.SetTargetLine("3.0");
                }
                else if (StringRandomP == "请切换像素为4.0") {
                    //System.out.println("---");
                    RandomPixel = "4.0";
                    penData.SetTargetLine("4.0");
                }
                //如果没有提示就按下了空格，就记为空
                else penData.SetTargetLine(null);

                //设置插件位置
                ShowPixelT.setBounds(903,250,100,20);
                ShowPixelT.setFont(new Font("楷体",Font.BOLD,20));

                //System.out.println(RandomPixel);
                JPanelRandomP.setBounds(1003,250,100,20);
                JPanelRandomP.setText(RandomPixel);
                JPanelRandomP.setHorizontalAlignment(JPanelRandomP.LEFT);
                JPanelRandomP.setFont(new Font("黑体",Font.BOLD,20));

                //把插件添加到TFInter中
                PIInter.add(ShowPixelT);
                PIInter.add(JPanelRandomP);

                //重绘PIInter界面
                this.RepaintPIInter();
                PixelFlag = false;
            }else if (x0 >= 966 && x0 <= 1350 && y0 >= 5 && y0 <= 105 && PixelFlag == false) {

            }else {
                //PixelFlag = true;
            }

            //将点的信息记录在容器中
            piExperimentPanel.arrayListSpot.add(dot);
            piExperimentPanel.repaint();
            //更新点的起始坐标（下一个点的开始为上一个点的结束）
            x0 = x1;
            y0 = y1;
        }


    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
