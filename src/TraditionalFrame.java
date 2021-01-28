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
    date:2020-12-18
    author:王久铭
    purpose:基于传统的写字面板的实现，用与和其他模式做对比
 */
public class TraditionalFrame implements ActionListener, MouseInputListener, KeyListener{
    //传统写字界面的定义
    private JFrame TraFrame = new JFrame("传统界面");
    //创建菜单栏并添加到顶部
    private JMenuBar MenuB = new JMenuBar();
    //创建两个下拉式菜单
    private JMenu MenuColor = new JMenu("颜色选择");
    private JMenu MenuPixel = new JMenu("像素选择");
    /*
    创建菜单项
        分别为颜色的下拉菜单和像素的下拉菜单
     */
    //设置颜色选择的下拉菜单
    private JMenuItem ItColor1 = new JMenuItem("蓝色");
    private JMenuItem ItColor2 = new JMenuItem("红色");
    private JMenuItem ItColor3 = new JMenuItem("黄色");

    //设置像素的下拉菜单
    private JMenuItem ItPixel1 = new JMenuItem("2.0");
    private JMenuItem ItPixel2 = new JMenuItem("3.0");
    private JMenuItem ItPixel3 = new JMenuItem("4.0");

    //设置画笔的初始颜色
    private int SetColor = 0;
    //设置画笔的初始像素
    private int SetPixel = 1;
    /*
        画线这部分还是和AreaFrame类中差不多
     */
    //画板所需变量
    private Area area = new Area();
    private CompleteExperiment completeExperiment = new  CompleteExperiment();
    private JTablet tablet = null;

    //获取笔的信息所需的变量
    private double x0,y0; //实验开始时笔尖的位置
    private double x1, y1; //每一次笔结束的位置
    //存储测试的信息
    private PenData pData = new PenData();
    //这个应该才是获得笔的数据
    private PenValue pValue = new PenValue();

    /*
        将TraFrame分割为两个区域
     */
    //上边边区域，用于提示信息
    private JPanel TFInter = new JPanel();
    //下边区域，用于画图
    private JPanel TFDraw = new JPanel();

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

    //用户是否第一次进入颜色测试区域，true表示未进入
    private boolean ColorFlag = true;
    //判断用户是否第一次进入像素测试区域，true表示未进入
    private boolean PixelFlag = true;
    //下面这个两个boolean的值是用来检测颜色和像素切换是否合法
    private boolean ColorChange = false; //在进入到颜色的测试区域后，变为true
    private boolean PixelChange = false; //在进入到像素的测试区域后，变为true


    public TraditionalFrame(int BlockNumber) {
        area.setLayout(new BorderLayout());
        area.addMouseListener(this);
        area.addMouseMotionListener(this);
        area.addKeyListener(this);

        completeExperiment.SetRandomNUmber(); //生成测试的随机数
        completeExperiment.SetRandomC(); //生成颜色提示语句
        completeExperiment.SetRandomP(); //生成像素提示语句
        completeExperiment.SetExperimentB(BlockNumber);

        //生成传统的界面
        this.CreateTraditionalFrame();

        area.requestFocusInWindow(); //让其获得焦点，这样才能是键盘监听能够正常使用

        try {
            tablet = new JTablet();
        } catch (JTabletException e) {
            e.printStackTrace();
        }
        /*
        这一部分主要是监听用户选择的是哪个颜色
         */
        //选择红色
        MenuColor.getItem(1).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SetColor = 2;
                //存入目标颜色
                pData.SetResultC("红色");
                //如果在没有进入测试区域就点击切换，误触发次数加一
                if (ColorChange == true) {
                    pData.AddTouchE();
                    pData.AddColorTouchE();
                }
                ShowColorBlock.setBackground(Color.RED);
                pData.SetEndColorMode(System.currentTimeMillis());
            }
        });
        //选择蓝色
        MenuColor.getItem(0).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SetColor = 1;
                //存入目标颜色
                pData.SetResultC("蓝色");
                //如果在没有进入测试区域就点击切换，误触发次数加一
                if (ColorChange == true) {
                    pData.AddTouchE();
                    pData.AddColorTouchE();
                }
                ShowColorBlock.setBackground(Color.BLUE);
                pData.SetEndColorMode(System.currentTimeMillis());
            }
        });
        //选择黄色
        MenuColor.getItem(2).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SetColor = 3;
                //存入目标颜色
                pData.SetResultC("黄色");
                //如果在没有进入测试区域就点击切换，误触发次数加一
                if (ColorChange == true) {
                    pData.AddTouchE();
                    pData.AddColorTouchE();
                }
                ShowColorBlock.setBackground(Color.ORANGE);
                pData.SetEndColorMode(System.currentTimeMillis());
            }
        });
        //选完颜色后重绘IFInter界面
        this.RepaintIFInter();
        /*
        以下部分主要是监听用户选择的哪个像素
         */
        //选择细
        MenuPixel.getItem(0).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SetPixel = 2;
                StringPixel = "2.0";
                //存入目标像素
                pData.SetResultP("2.0");
                //如果在没有进入测试区域就点击切换，误触发次数加一
                if (PixelChange == true) {
                    pData.AddTouchE();
                    pData.AddPixelTouchE();
                }
                ShowPixel.setText(StringPixel);
                pData.SetEndPixelMode(System.currentTimeMillis());
            }
        });
        //选择中等
        MenuPixel.getItem(1).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SetPixel = 3;
                StringPixel = "3.0";
                //存入目标像素
                pData.SetResultP("3.0");
                //如果在没有进入测试区域就点击切换，误触发次数加一
                if (PixelChange == true) {
                    pData.AddTouchE();
                    pData.AddPixelTouchE();
                }
                ShowPixel.setText(StringPixel);
                pData.SetEndPixelMode(System.currentTimeMillis());
            }
        });
        //选择粗
        MenuPixel.getItem(2).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SetPixel = 4;
                StringPixel = "4.0";
                //存入目标像素
                pData.SetResultP("4.0");
                //如果在没有进入测试区域就点击切换，误触发次数加一
                if (PixelChange == true) {
                    pData.AddTouchE();
                    pData.AddPixelTouchE();
                }
                ShowPixel.setText(StringPixel);
                pData.SetEndPixelMode(System.currentTimeMillis());
            }
        });
        //选择完像素后重绘IFInter界面
        this.RepaintIFInter();

    }
    public void CreateTraditionalFrame() {
        //生成上半部分的界面
        this.CreateTFInter();
        //生成下半部分界面
        this.CreateTFDraw();
        /*
        将界面分割为两部分
         */
        JSplitPane jSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,false,TFInter,TFDraw); //这里第一个参数是控制分割线竖直，第二个参数是当你拖曳切割面版的分隔线时，窗口内的组件是否会随着分隔线的拖曳而动态改变大小，最后两个参数就是我分割完成后分割线两边各添加哪个容器。
        jSplitPane.setDividerLocation(300); //分割线的位置  也就是初始位置
        jSplitPane.setOneTouchExpandable(false); //是否可展开或收起，在这里没用
        jSplitPane.setDividerSize(0);//设置分割线的宽度 像素为单位(这里设为0，择时不显示分割线)
        jSplitPane.setEnabled(false); //设置分割线不可拖动！！
        TraFrame.add(jSplitPane);  //加入到面板中就好了

        //生成下拉菜单
        this.CreateMenu();

        //界面全屏设置
        TraFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        TraFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        TraFrame.setVisible(true);

    }
    //生成下拉选择的菜单栏
    public void CreateMenu() {
        //添加菜单栏
        TraFrame.setJMenuBar(MenuB);
        //添加下拉菜单到菜单栏
        MenuB.add(MenuColor);
        MenuB.add(MenuPixel);
        //将颜色的下拉菜单添加到颜色菜单中
        MenuColor.add(ItColor1);
        MenuColor.add(ItColor2);
        MenuColor.add(ItColor3);
        //将像素的下拉菜单添加到像素菜单中
        MenuPixel.add(ItPixel1);
        MenuPixel.add(ItPixel2);
        MenuPixel.add(ItPixel3);

    }
    //创建上半部分的提示区域
    public void CreateTFInter() {

        //上半部分的界面,背景颜色为默认颜色
        TFInter.setLayout(null); //不采用布局管理器,由坐标定位

        //当前颜色提示标签
        ShowColorL.setBounds(500,250,100,20);
        ShowColorL.setFont(new Font("楷体",Font.BOLD,20));
        TFInter.add(ShowColorL);
        //当前颜色（颜色块）
        ShowColorBlock.setBounds(600,250,60,20);
        ShowColorBlock.setBackground(Color.BLACK);
        TFInter.add(ShowColorBlock);

        //当前像素提示标签
        ShowPixelL.setBounds(700,250,100,20);
        ShowPixelL.setFont(new Font("楷体",Font.BOLD,20));
        TFInter.add(ShowPixelL);
        //当前像素
        ShowPixel.setBounds(800,250,100,20);
        ShowPixel.setText(StringPixel);
        ShowPixel.setHorizontalAlignment(ShowPixel.LEFT);
        ShowPixel.setFont(new Font("黑体",Font.BOLD,20));
        TFInter.add(ShowPixel);

    }
    //生成下半部分的绘画界面
    public void CreateTFDraw() {
        TFDraw.setLayout(new BorderLayout());
        TFDraw.setBackground(Color.WHITE);
        TFDraw.add(area,BorderLayout.CENTER);
    }
    //重绘IFInter界面
    public void RepaintIFInter() {
        TFInter.removeAll();
        TFInter.repaint();

        //当前颜色和像素的展示
        TFInter.add(ShowColorL);
        TFInter.add(ShowColorBlock);
        TFInter.add(ShowPixelL);
        TFInter.add(ShowPixel);
        //目标颜色和像素的展示
        TFInter.add(ShowColorT);
        TFInter.add(JPanelRandomC);
        TFInter.add(ShowPixelT);
        TFInter.add(JPanelRandomP);

        TFInter.revalidate();
    }
    //重绘IFInter界面(将提示的移除掉)
    public void RemoveRandom() {
        TFInter.removeAll();
        TFInter.repaint();
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
        TFInter.add(ShowColorL);
        TFInter.add(ShowColorBlock);
        TFInter.add(ShowPixelL);
        TFInter.add(ShowPixel);
        TFInter.revalidate();
        //将笔的颜色变为黑色
        SetColor = 0;
        //将笔的像素变为1.0
        SetPixel = 1;
    }
    @Override
    public void actionPerformed(ActionEvent e) { }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) {
        //当一次实验完成，用户按下空格键
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            //清空集合中的点的信息
            area.arrayListSpot.clear();
            //重绘
            area.repaint();
            //将检查是否进入颜色和像素测试区域的变量设为false
            ColorChange = false;
            PixelChange = false;
            //将提示语句移除
            this.RemoveRandom();
            //在一组中做完一次实验
            pData.AddTrialN();
            //模式切换出现的错误数
            pData.AddModeE();
            //将笔的压力保存在指定文件中
            try {
                pData.AllocateTime();
                pData.AllocateTimeString();
                pData.SaveInformation();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            //判断一组实验是否做完
            if (completeExperiment.GetList() == true) {
                if (completeExperiment.GetExperimentB() -1 >= 1) {
                    int temp = completeExperiment.GetExperimentB() - 1;
                    completeExperiment.SetRandomC();
                    completeExperiment.SetRandomP();
                    completeExperiment.SetExperimentB(temp);
                }else {
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

                    pData.SetColorTouchE(0); //初始化颜色误触发数
                    pData.SetPixelTouchE(0); //初始化像素误触发数

                    pData.SetColorModeE(0); //初始化颜色切换错误数
                    pData.SetPixelModeE(0); //初始化像素切换错误数

                    //关闭当前的界面
                    TraFrame.dispose();
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) { }

    @Override
    public void mouseClicked(MouseEvent e) {

    }
    //在组件上按下鼠标按钮时调用
    @Override
    public void mousePressed(MouseEvent e) {
        if (javax.swing.SwingUtilities.isLeftMouseButton(e)) {
            //获得开始时鼠标的位置
            x0 = e.getX();
            y0 = e.getY();
            pData.SetPressure(pValue.Pressure());
            pData.SetTilt(pValue.Tilt());
            pData.SetAzimuth(pValue.Azimuth());
            /*
            获得落笔的时间
             */
            //获得落笔的时间戳
            pData.AddTime(System.currentTimeMillis());
            //获得落笔的文字格式
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");
            pData.AddTimeString(dateFormat.format(new Date()));
        }
    }
    //在组件上释放鼠标按钮时调用
    @Override
    public void mouseReleased(MouseEvent e) {
        //获得抬笔的时间戳
        pData.AddTime(System.currentTimeMillis());
        //获得抬笔的文字格式
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");
        pData.AddTimeString(dateFormat.format(new Date()));
    }
    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
    //在组件上按下鼠标按钮然后拖动时调用
    @Override
    public void mouseDragged(MouseEvent e) {
        /*
        应该每次拖动时都会产生一个点对象
         */
        //获得笔在拖动时的坐标
        x1 = e.getX();
        y1 = e.getY();

        Dot dot = new Dot();
        dot.SetStarDot(x0,y0);
        dot.SetEndDot(x1,y1);
        dot.SetColor(SetColor); //点的颜色
        dot.SetPixel(SetPixel); //点的像素

        double x = dot.DotStarX();
        double y = dot.DotStarY();

        if (x >= 350 && x <= 850 && y >= 50 && y <= 150 && ColorFlag == true) {
            ColorChange = true; //当进入到颜色测试区域时，颜色测换才合法
            pData.SetStartColorMode(System.currentTimeMillis());
            int indexC = completeExperiment.GetRandomNumberC();
            String StringRandomC = completeExperiment.GetRandomC(indexC);
            //按照系统的提示颜色存入相应的目标颜色
            if (StringRandomC == "请切换颜色为蓝色") {
                pData.SetTargetColor("蓝色");
                JPanelRandomC.setBackground(Color.BLUE);
            }
            else if (StringRandomC == "请切换颜色为红色") {
                pData.SetTargetColor("红色");
                JPanelRandomC.setBackground(Color.RED);
            }
            else if (StringRandomC == "请切换颜色为黄色") {
                pData.SetTargetColor("黄色");
                JPanelRandomC.setBackground(Color.ORANGE);
            }
            //如果没有提示且按下了空格，就记为空
            else pData.SetTargetColor(null);

            //设置插件位置
            ShowColorT.setBounds(880,250,100,20);
            ShowColorT.setFont(new Font("楷体",Font.BOLD,20));

            JPanelRandomC.setBounds(980,250,60,20);
            //将插件添加到TFInter中
            TFInter.add(ShowColorT);
            TFInter.add(JPanelRandomC);
            //重绘TFInter界面
            this.RepaintIFInter();
            ColorFlag = false;
        } else if (x0 >= 350 && x0 <= 850 && y0 >= 50 && y0 <= 150 && ColorFlag == false){

        }else {
            ColorFlag = true;
        }

        if (x0 >= 900 && x0 <= 1400 && y0 >= 50 && y0 <= 150 && PixelFlag == true) {
            PixelChange = true; //当进入到像素测试区域时，此时的像素测换才合法
            pData.SetStartPixelMode(System.currentTimeMillis());
            int indexP = completeExperiment.GetRandomNumberP();
            String StringRandomP = completeExperiment.GetRandomP(indexP);
            //System.out.println(StringRandomP);
            //按照系统提示的像素存入目标像素
            if (StringRandomP == "请切换像素为2.0") {
                //System.out.println("-");
                RandomPixel = "2.0";
                pData.SetTargetLine("2.0");
            }
            else if (StringRandomP == "请切换像素为3.0") {
                //System.out.println("--");
                RandomPixel = "3.0";
                pData.SetTargetLine("3.0");
            }
            else if (StringRandomP == "请切换像素为4.0") {
                //System.out.println("---");
                RandomPixel = "4.0";
                pData.SetTargetLine("4.0");
            }
            //如果没有提示就按下了空格，就记为空
            else pData.SetTargetLine(null);

            //设置插件位置
            ShowPixelT.setBounds(1080,250,100,20);
            ShowPixelT.setFont(new Font("楷体",Font.BOLD,20));

            //System.out.println(RandomPixel);
            JPanelRandomP.setBounds(1180,250,100,20);
            JPanelRandomP.setText(RandomPixel);
            JPanelRandomP.setHorizontalAlignment(JPanelRandomP.LEFT);
            JPanelRandomP.setFont(new Font("黑体",Font.BOLD,20));

            //把插件添加到TFInter中
            TFInter.add(ShowPixelT);
            TFInter.add(JPanelRandomP);

            //重绘TFInter界面
            this.RepaintIFInter();

            PixelFlag = false;
        }else if (x0 >= 900 && x0 <= 1400 && y0 >= 50 && y0 <= 150 && PixelFlag == false) {

        }else {
            PixelFlag = true;
        }
        //将点的信息记录在容器中
        area.arrayListSpot.add(dot);
        area.repaint();
        //更新点的起始坐标（下一个点的开始为上一个点的结束）
        x0 = x1;
        y0 = y1;
    }
    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
