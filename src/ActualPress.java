import cello.tablet.JTablet;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
    date:2021-01-13
    author:王久铭
    purpose:压力实例化界面，用来实现通过压力的实例化来选择想要的颜色和想要的像素
 */
public class ActualPress implements ActionListener, MouseInputListener, KeyListener {
    //压力实列化界面的定义
    private JFrame ActualPFrame = new JFrame();

    //设置画笔的初始颜色
    private int SetColor = 0;
    //设置画笔的初始像素
    private int SetPixel = 0;


    private Area area = new Area(); //画线显示区域
    private CompleteExperiment completeExperiment = new CompleteExperiment();
    private JTablet tablet = null;

    //储存实验信息
    private PenData penData = new PenData();
    //获取笔的实时数据
    private PenValue penValue = new PenValue();


    //获取笔的相关信息
    private double x0,y0; //每一次笔尖开始的位置
    private double x1,y1; //每一次笔尖结束的位置

    /*
        将ActualPress分为两个区域
     */
    private JPanel APInter = new JPanel(); //提示信息区域
    private JPanel APDraw = new JPanel(); //画线区域

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

    public ActualPress(int BlockNumber) {
        area.setLayout(new BorderLayout());
        area.addMouseListener(this);
        area.addMouseMotionListener(this);
        area.addKeyListener(this);

        completeExperiment.SetRandomNUmber(); //生成测试的随机数
        completeExperiment.SetRandomC(); //生成颜色提示语句
        completeExperiment.SetRandomP(); //生成像素提示语句
        completeExperiment.SetExperimentB(BlockNumber);

        //生成压力实列化测试界面
        this.CreateAPFrame();
        area.requestFocusInWindow(); //让其获得焦点，这样才能是键盘监听能够正常使用

    }
    public void CreateAPFrame() {
        this.CreateAPInter();
        this.CreateAPDraw();

        /*
        将界面分割为两部分
         */
        JSplitPane jSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,false,APInter,APDraw); //这里第一个参数是控制分割线竖直，第二个参数是当你拖曳切割面版的分隔线时，窗口内的组件是否会随着分隔线的拖曳而动态改变大小，最后两个参数就是我分割完成后分割线两边各添加哪个容器。
        jSplitPane.setDividerLocation(300); //分割线的位置  也就是初始位置
        jSplitPane.setOneTouchExpandable(false); //是否可展开或收起，在这里没用
        jSplitPane.setDividerSize(0);//设置分割线的宽度 像素为单位(这里设为0，择时不显示分割线)
        jSplitPane.setEnabled(false); //设置分割线不可拖动！！
        ActualPFrame.add(jSplitPane);  //加入到面板中就好了

        //界面全屏设置
        ActualPFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        ActualPFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ActualPFrame.setVisible(true);
    }
    public void CreateAPInter() {
        //上半部分的界面,背景颜色为默认颜色
        APInter.setLayout(null); //不采用布局管理器,由坐标定位

        //当前颜色提示标签
        ShowColorL.setBounds(500,250,100,20);
        ShowColorL.setFont(new Font("楷体",Font.BOLD,20));
        APInter.add(ShowColorL);
        //当前颜色（颜色块）
        ShowColorBlock.setBounds(600,250,60,20);
        ShowColorBlock.setBackground(Color.BLACK);
        APInter.add(ShowColorBlock);

        //当前像素提示标签
        ShowPixelL.setBounds(700,250,100,20);
        ShowPixelL.setFont(new Font("楷体",Font.BOLD,20));
        APInter.add(ShowPixelL);
        //当前像素
        ShowPixel.setBounds(800,250,100,20);
        ShowPixel.setText(StringPixel);
        ShowPixel.setHorizontalAlignment(ShowPixel.LEFT);
        ShowPixel.setFont(new Font("黑体",Font.BOLD,20));
        APInter.add(ShowPixel);
    }
    public void CreateAPDraw() {
        APDraw.setLayout(new BorderLayout());
        APDraw.setBackground(Color.WHITE);
        APDraw.add(area,BorderLayout.CENTER);
    }
    //重绘IFInter界面(将提示的移除掉)
    public void RemoveRandom() {
        APInter.removeAll();
        APInter.repaint();
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
        APInter.add(ShowColorL);
        APInter.add(ShowColorBlock);
        APInter.add(ShowPixelL);
        APInter.add(ShowPixel);
        APInter.revalidate();
        //将笔的颜色变为黑色
        SetColor = 0;
        //将笔的像素变为1.0
        SetPixel = 1;
    }
    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        //当一次实验完成，用户按下空格键
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            //清空集合中的点的信息
            area.arrayListSpot.clear();
            //重绘
            area.repaint();
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

                    penData.SetColorTouchE(0); //初始化颜色误触发数
                    penData.SetPixelTouchE(0); //初始化像素误触发数

                    penData.SetColorModeE(0); //初始化颜色切换错误数
                    penData.SetPixelModeE(0); //初始化像素切换错误数

                    //关闭当前的界面
                    ActualPFrame.dispose();
                }
            }
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
            //获得开始时鼠标的位置
            x0 = e.getX();
            y0 = e.getY();
            penData.SetPressure(penValue.Pressure());
            penData.SetTilt(penValue.Tilt());
            penData.SetAzimuth(penValue.Azimuth());
            /*
            获得落笔的时间
             */
            //获得落笔的时间戳
            penData.AddTime(System.currentTimeMillis());
            //获得落笔的文字格式
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");
            penData.AddTimeString(dateFormat.format(new Date()));
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //获取笔尖压力的值，应该是第一次笔尖的压力（也就是第一个点的压力值）
        //获得落笔的时间戳
        penData.AddTime(System.currentTimeMillis());
        //获得落笔的文字格式
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");
        penData.AddTimeString(dateFormat.format(new Date()));
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

        Dot dot = new Dot();
        dot.SetStarDot(x0,y0);
        dot.SetEndDot(x1,y1);
        dot.SetColor(SetColor); //点的颜色
        dot.SetPixel(SetPixel); //点的像素

        double x = dot.DotStarX();
        double y = dot.DotStarY();

        if (x >= 350 && x <= 850 && y >= 50 && y <= 150 && ColorFlag == true) {
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


            ColorFlag = false;
        } else if (x0 >= 350 && x0 <= 850 && y0 >= 50 && y0 <= 150 && ColorFlag == false){

        }else {
            ColorFlag = true;
        }

        if (x0 >= 900 && x0 <= 1400 && y0 >= 50 && y0 <= 150 && PixelFlag == true) {

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
