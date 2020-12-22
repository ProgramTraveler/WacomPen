import cello.tablet.JTablet;
import cello.tablet.JTabletException;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.io.IOException;
/*
    date:2020-12-18
    author:王久铭
    purpose:基于传统的写字面板的实现，用与和其他模式做对比
 */
public class TraditionalFrame implements ActionListener, MouseInputListener, KeyListener {
    //传统写字界面的定义
    private JFrame TraFrame = new JFrame("传统写字界面");
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
    private JMenuItem ItColor0 = new JMenuItem("黑色");
    private JMenuItem ItColor1 = new JMenuItem("蓝色");
    private JMenuItem ItColor2 = new JMenuItem("红色");
    private JMenuItem ItColor3 = new JMenuItem("黄色");

    //设置像素的下拉菜单
    private JMenuItem ItPixel1 = new JMenuItem("细");
    private JMenuItem ItPixel2 = new JMenuItem("中");
    private JMenuItem ItPixel3 = new JMenuItem("粗");

    //设置画笔的颜色
    private int SetColor = 0;
    //设置画笔的像素
    private int SetPixel = 2;
    //提示当前画笔颜色
    private JLabel ShowColor = new JLabel("当前颜色");
    //显示当前颜色
    private JTextField TextShoeC = new JTextField();
    //提示当前像素
    private JLabel ShowPixel = new JLabel("当前像素");
    //显示当前像素
    private JTextField TextShowP = new JTextField();

    /*
    画线这部分还是和AreaFrame类中差不多
     */

    //画板所需变量
    private Area ar1 = new Area();
    private JFrame frame = new JFrame("写字板");
    private JTablet tablet = null;
    private Dot dot = new Dot();
    //获取笔的信息所需的变量
    private double x0,y0; //实验开始时笔尖的位置
    private double x1, y1; //每一次笔结束的位置
    private Line2D line = null;
    //获取笔的信息
    private PenData pData = new PenData();
    //这个应该才是获得笔的数据
    private PenValue pValue = new PenValue();

    /*
    将TraFrame分割为两个区域
     */
    //左边区域，用于提示信息
    private JPanel TFInter = new JPanel();
    //右边区域，用于画图
    private JPanel TFDraw = new JPanel();

    public TraditionalFrame() {

        ar1.setLayout(new BorderLayout());
        ar1.addMouseListener(this);
        ar1.addMouseMotionListener(this);
        ar1.addKeyListener(this);

        /*
        将界面分割为两部分
         */
        JSplitPane jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,false,TFInter,TFDraw); //这里第一个参数是控制分割线竖直，第二个参数是当你拖曳切割面版的分隔线时，窗口内的组件是否会随着分隔线的拖曳而动态改变大小，最后两个参数就是我分割完成后分割线两边各添加哪个容器。
        jSplitPane.setDividerLocation(200); //分割线的位置  也就是初始位置
        jSplitPane.setOneTouchExpandable(false); //是否可展开或收起，在这里没用
        jSplitPane.setDividerSize(1);//设置分割线的宽度 像素为单位
        jSplitPane.setEnabled(false); //设置分割线不可拖动！！
        TraFrame.add(jSplitPane);  //加入到面板中就好了

        TFInter.setLayout(new BorderLayout());
        TFInter.setBackground(Color.WHITE);
        ShowColor.setBounds(10,50,85,3);
        TFInter.add(ShowColor);
        ShowPixel.setBounds(10,80,90,0);
        TFInter.add(ShowPixel);


        //传统写字界面的设计

        /*TraFrame.getContentPane().setLayout(new BorderLayout());
        TraFrame.setBackground(Color.WHITE);
        TraFrame.getContentPane().add(ar1,BorderLayout.CENTER);*/

        TFDraw.setLayout(new BorderLayout());
        TFDraw.setBackground(Color.WHITE);
        TFDraw.add(ar1,BorderLayout.CENTER);

        //添加菜单栏
        TraFrame.setJMenuBar(MenuB);
        //添加下拉菜单到菜单栏
        MenuB.add(MenuColor);
        MenuB.add(MenuPixel);
        //将颜色的下拉菜单添加到颜色菜单中
        MenuColor.add(ItColor0);
        MenuColor.add(ItColor1);
        MenuColor.add(ItColor2);
        MenuColor.add(ItColor3);
        //将像素的下拉菜单添加到像素菜单中
        MenuPixel.add(ItPixel1);
        MenuPixel.add(ItPixel2);
        MenuPixel.add(ItPixel3);


        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width;
        TraFrame.setBounds(width / 5, 30, 875, 875);
        //界面全屏设置
        TraFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        TraFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        TraFrame.setVisible(true);

        try {
            tablet = new JTablet();
        } catch (JTabletException e) {
            e.printStackTrace();
        }





        /*
        这一部分主要是监听用户选择的是哪个颜色
         */
        //选择黑色
        MenuColor.getItem(0).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SetColor = 0;
            }
        });
        //选择红色
        MenuColor.getItem(2).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SetColor = 2;
            }
        });
        //选择蓝色
        MenuColor.getItem(1).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SetColor = 1;
            }
        });
        //选择黄色
        MenuColor.getItem(3).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SetColor = 3;
            }
        });
        /*
        以下部分主要是监听用户选择的哪个像素
         */
        //选择细
        MenuPixel.getItem(0).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SetPixel = 2;
            }
        });
        //选择中等
        MenuPixel.getItem(1).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SetPixel = 3;
            }
        });
        //选择粗
        MenuPixel.getItem(2).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SetPixel = 5;
            }
        });

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
    //在组件上按下鼠标按钮时调用
    @Override
    public void mousePressed(MouseEvent e) {
        if (javax.swing.SwingUtilities.isLeftMouseButton(e)) {
            //获得开始时鼠标的位置
            x0 = e.getX();
            y0 = e.getY();
            pData.SetPressure(pValue.Pressure());
        }
    }
    //在组件上释放鼠标按钮时调用
    @Override
    public void mouseReleased(MouseEvent e) {

        //获取笔尖压力的值，应该是第一次笔尖的压力（也就是第一个点的压力值）
        System.out.println("pressure:"+pValue.Pressure());
        //将笔的压力保存在指定文件中
        try {
            pData.SavaPre(pValue.Pressure());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
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
        dot.SetDotRandomC(); //点颜色生成的随机数
        dot.SetDotRandomP(); //点像素生成的随机数
        //将点的信息记录在容器中
        ar1.arrayListSpot.add(dot);
        //ar1.validate();
        ar1.repaint();

        //更新点的起始坐标（下一个点的开始为上一个点的结束）
        x0 = x1;
        y0 = y1;


    }
    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
