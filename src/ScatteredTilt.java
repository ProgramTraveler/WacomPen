import cello.tablet.JTablet;
import cello.tablet.JTabletException;
import com.sun.org.apache.xml.internal.security.keys.content.SPKIData;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.*;

/*
    date:2021-01-30
    author:王久铭
    purpose:这是倾斜角的离散化界面，通过倾斜角的不断改变来进行选择目标颜色和像素
 */
public class ScatteredTilt extends JFrame implements MouseInputListener, KeyListener, ActionListener {
    private int time = 50; //更新时间为50毫秒
    private Timer timer = new Timer(time,this); //每50毫秒触发一次actionPerformed
    private TSExperimentPanel tsExperimentPanel = new TSExperimentPanel();
    private int CurrentTilt = -1; //获取当前倾斜角

    //下面这个两个boolean的值是用来检测颜色和像素切换是否合法
    private boolean ColorChange = false; //在进入到颜色的测试区域后，变为true
    private boolean PixelChange = false; //在进入到像素的测试区域后，变为true

    //倾斜角离散化界面的定义
    private JFrame ScatteredTFrame = new JFrame("T-离散化界面");
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

    private double x0, y0; //每一次笔尖开始的位置
    private double x1, y1; //每一次笔尖结束的位置

    private JPanel STInter = new JPanel(); //提示信息区域
    private JPanel STDraw = new JPanel(); //画线区域

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

    public ScatteredTilt(int BlockNumber) {
        tsExperimentPanel.setLayout(new BorderLayout());
        tsExperimentPanel.addMouseListener(this);
        tsExperimentPanel.addMouseMotionListener(this);
        tsExperimentPanel.addKeyListener(this);

        completeExperiment.SetRandomNUmber(); //生成测试随机数
        completeExperiment.SetRandomC(); //生成颜色提示语句
        completeExperiment.SetRandomP(); //生成像素提示语句
        completeExperiment.SetExperimentB(BlockNumber);

        this.CreateSTFrame(); //生成倾斜角离散化界面

        tsExperimentPanel.requestFocusInWindow(); //获得焦点

        try {
            tablet = new JTablet();
        } catch (JTabletException e) {
            e.printStackTrace();
        }

        timer.start();
        timer.stop();
    }

    public void CreateSTFrame() {
        this.CreateSTInter();
        this.CreateSTDraw();

         /*
            将界面分割为两部分
         */
        JSplitPane jSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, false, STInter, STDraw); //这里第一个参数是控制分割线竖直，第二个参数是当你拖曳切割面版的分隔线时，窗口内的组件是否会随着分隔线的拖曳而动态改变大小，最后两个参数就是我分割完成后分割线两边各添加哪个容器。
        jSplitPane.setDividerLocation(300); //分割线的位置  也就是初始位置
        jSplitPane.setOneTouchExpandable(false); //是否可展开或收起，在这里没用
        jSplitPane.setDividerSize(0);//设置分割线的宽度 像素为单位(这里设为0，择时不显示分割线)
        jSplitPane.setEnabled(false); //设置分割线不可拖动！！
        ScatteredTFrame.add(jSplitPane);  //加入到面板中就好了

        //界面全屏设置
        ScatteredTFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        //ScatteredTFrame.setBounds(500,200,300,800);
        ScatteredTFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ScatteredTFrame.setVisible(true);

    }
    public void CreateSTInter(){
        //上半部分的界面,背景颜色为默认颜色
        STInter.setLayout(null); //不采用布局管理器,由坐标定位

        //当前颜色提示标签
        ShowColorL.setBounds(500, 250, 100, 20);
        ShowColorL.setFont(new Font("楷体", Font.BOLD, 20));
        STInter.add(ShowColorL);
        //当前颜色（颜色块）
        ShowColorBlock.setBounds(600, 250, 60, 20);
        ShowColorBlock.setBackground(Color.BLACK);
        STInter.add(ShowColorBlock);

        //当前像素提示标签
        ShowPixelL.setBounds(700, 250, 100, 20);
        ShowPixelL.setFont(new Font("楷体", Font.BOLD, 20));
        STInter.add(ShowPixelL);
        //当前像素
        ShowPixel.setBounds(800, 250, 100, 20);
        ShowPixel.setText(StringPixel);
        ShowPixel.setHorizontalAlignment(ShowPixel.LEFT);
        ShowPixel.setFont(new Font("黑体", Font.BOLD, 20));
        STInter.add(ShowPixel);

    }
    public void CreateSTDraw() {
        STDraw.setLayout(new BorderLayout());
        STDraw.setBackground(Color.WHITE);
        STDraw.add(tsExperimentPanel, BorderLayout.CENTER);
    }
    //重绘STInter界面(将提示的移除掉)
    public void RemoveRandom() {
        STInter.removeAll();
        STInter.repaint();
        //将颜色提示语句和颜色块隐藏
        ShowColorT.setBounds(0, 0, 0, 0);
        JPanelRandomC.setBounds(0, 0, 0, 0);
        //将像素提示语句和像素值隐藏
        ShowPixelT.setBounds(0, 0, 0, 0);
        JPanelRandomP.setBounds(0, 0, 0, 0);
        //将笔的当前的提示颜色变为黑色
        ShowColorBlock.setBackground(Color.BLACK);
        //将笔的当前提示像素变为1.0
        StringPixel = "1.0";
        ShowPixel.setText(StringPixel);
        //当前颜色和像素的展示
        STInter.add(ShowColorL);
        STInter.add(ShowColorBlock);
        STInter.add(ShowPixelL);
        STInter.add(ShowPixel);
        STInter.revalidate();
        //将笔的颜色变为黑色
        tsExperimentPanel.DefineColor(0);
        //将笔的像素变为1.0
        tsExperimentPanel.DefinePixel(1);
    }

    //重绘STInter界面
    public void RepaintSTInter() {
        STInter.removeAll();
        STInter.repaint();

        //当前颜色和像素的展示
        STInter.add(ShowColorL);
        STInter.add(ShowColorBlock);
        STInter.add(ShowPixelL);
        STInter.add(ShowPixel);
        //目标颜色和像素的展示
        STInter.add(ShowColorT);
        STInter.add(JPanelRandomC);
        STInter.add(ShowPixelT);
        STInter.add(JPanelRandomP);

        STInter.revalidate();
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
