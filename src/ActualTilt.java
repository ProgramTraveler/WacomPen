import cello.tablet.JTablet;
import cello.tablet.JTabletException;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.*;

/*
    date:2021-01-21
    author:王久铭
    purpose:倾斜角实例化界面，用来实现通过倾斜角的实例化的值来实现对颜色和像素的选择
 */
public class ActualTilt extends JFrame implements ActionListener, MouseInputListener, KeyListener {
    private int time = 50;
    private javax.swing.Timer timer = new Timer(time,this);
    private TAExperimentPanel taExperimentPanel = new TAExperimentPanel();

    //倾斜角实例化界面的定义
    private JFrame ActualTFrame = new JFrame("T-实例化");

    //设置画笔的初始颜色和像素
    int SetColor = 0;
    int SetPixel = 0;

    private CompleteExperiment completeExperiment = new CompleteExperiment(); //为测试提供实验数据
    private JTablet tablet = null; //获得笔的相关信息

    private PenData penData = new PenData(); //记录笔在实验过程中的信息
    private PenValue penValue = new PenValue(); //获取笔的实时数据

    private double x0,y0; //笔尖开始的位置
    private double x1,y1; //笔尖结束的位置

    /*
        将ActualTilt分为两个区域
     */
    private JPanel ATInter = new JPanel(); //提示信息区域
    private JPanel  ATDraw = new JPanel(); //画线区域

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

    public ActualTilt(int BlockNumber) {
        taExperimentPanel.setLayout(new BorderLayout());
        taExperimentPanel.addMouseListener(this);
        taExperimentPanel.addMouseMotionListener(this);
        taExperimentPanel.addKeyListener(this);

        completeExperiment.SetRandomNUmber(); //生成测试的随机数
        completeExperiment.SetRandomC(); //生成颜色提示语句
        completeExperiment.SetRandomP(); //生成像素提示语句
        completeExperiment.SetExperimentB(BlockNumber);

        this.CreateATFrame(); //生成倾斜角实例化界面

        taExperimentPanel.requestFocusInWindow(); //获得焦点


        try {
            tablet = new JTablet();
        } catch (JTabletException e) {
            e.printStackTrace();
        }
        timer.start();
        timer.stop();

    }
    public void CreateATFrame() {
        this.CreateATInter();
        this.CreateATFDraw();

        /*
        将界面分割为两部分
         */
        JSplitPane jSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,false,ATInter,ATDraw); //这里第一个参数是控制分割线竖直，第二个参数是当你拖曳切割面版的分隔线时，窗口内的组件是否会随着分隔线的拖曳而动态改变大小，最后两个参数就是我分割完成后分割线两边各添加哪个容器。
        jSplitPane.setDividerLocation(300); //分割线的位置  也就是初始位置
        jSplitPane.setOneTouchExpandable(false); //是否可展开或收起，在这里没用
        jSplitPane.setDividerSize(0);//设置分割线的宽度 像素为单位(这里设为0，择时不显示分割线)
        jSplitPane.setEnabled(false); //设置分割线不可拖动！！
        ActualTFrame.add(jSplitPane);  //加入到面板中就好了

        //界面全屏设置
        ActualTFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        ActualTFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ActualTFrame.setVisible(true);

    }
    //上半部分，主要用于信息的显示
    public void CreateATInter() {
        ATInter.setLayout(null);
        //当前颜色提示标签
        ShowColorL.setBounds(500,250,100,20);
        ShowColorL.setFont(new Font("楷体",Font.BOLD,20));
        ATInter.add(ShowColorL);
        //当前颜色（颜色块）
        ShowColorBlock.setBounds(600,250,60,20);
        ShowColorBlock.setBackground(Color.BLACK);
        ATInter.add(ShowColorBlock);

        //当前像素提示标签
        ShowPixelL.setBounds(700,250,100,20);
        ShowPixelL.setFont(new Font("楷体",Font.BOLD,20));
        ATInter.add(ShowPixelL);
        //当前像素
        ShowPixel.setBounds(800,250,100,20);
        ShowPixel.setText(StringPixel);
        ShowPixel.setHorizontalAlignment(ShowPixel.LEFT);
        ShowPixel.setFont(new Font("黑体",Font.BOLD,20));
        ATInter.add(ShowPixel);
    }
    //下半部分，主要是用于线条绘制区域的显示
    public void CreateATFDraw() {
        ATDraw.setLayout(new BorderLayout());
        ATDraw.setBackground(Color.WHITE);
        ATDraw.add(taExperimentPanel,BorderLayout.CENTER);
    }
    //对ATInter界面进行重绘，目的是移除提示的目标信息
    public void RemoveRandom() {
        ATInter.removeAll();
        ATInter.repaint();
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
        ATInter.add(ShowColorL);
        ATInter.add(ShowColorBlock);
        ATInter.add(ShowPixelL);
        ATInter.add(ShowPixel);
        ATInter.revalidate();
        //将笔的颜色变为黑色
        SetColor = 0;
        //将笔的像素变为1.0
        SetPixel = 1;
    }
    //重绘ATInter界面
    public void RepaintATInter() {
        ATInter.removeAll();
        ATInter.repaint();

        //当前颜色和像素的展示
        ATInter.add(ShowColorL);
        ATInter.add(ShowColorBlock);
        ATInter.add(ShowPixelL);
        ATInter.add(ShowPixel);
        //目标颜色和像素的展示
        ATInter.add(ShowColorT);
        ATInter.add(JPanelRandomC);
        ATInter.add(ShowPixelT);
        ATInter.add(JPanelRandomP);

        ATInter.revalidate();
    }
    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
