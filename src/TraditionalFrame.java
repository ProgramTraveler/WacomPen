import cello.tablet.JTablet;
import cello.tablet.JTabletException;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

/*
    date:2020-12-18
    author:王久铭
    purpose:基于传统的写字面板的实现，用与和其他模式做对比
 */
public class TraditionalFrame implements ActionListener, MouseInputListener, KeyListener{
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
    /*
    黑色这部分作为选择条件去掉，设为默认条件
    private JMenuItem ItColor0 = new JMenuItem("黑色");

     */
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
    画笔落笔和抬笔的数据记录
     */
    private long DownTime; //落笔的时间
    private boolean DownFirst = true; //判断是否为第一次落笔
    private long UpTime; //抬笔时间

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

    /*
    提示标签和提示语句
        当前的颜色和像素
     */
    //提示当前画笔颜色
    private JTextField ShowColor = new JTextField();
    //提示当前像素
    private JTextField ShowPixel = new JTextField();
    //颜色提示语句
    private String StringColor = "当前颜色为黑色";
    //像素提示语句
    private String StringPixel = "当前笔尖为1.0";
    /*
    提示标签和语句
        应该切换的颜色和像素
     */
    //提示应该切换的颜色
    private JTextField ShowRandomC = new JTextField();
    //提示应该切换的像素
    private JTextField ShowRandomP = new JTextField();
    //颜色提示语句
    private String StringRandomC = "";
    //像素提示语句
    private String StringRandomP = "";
    //用户是否第一次进入颜色测试区域，true表示未进入
    private boolean ColorFlag = true;
    //判断用户是否第一次进入像素测试区域，true表示未进入
    private boolean PixelFlag = true;
    //判断用户是否开始测试
    //private boolean EntrySign = false;

    public TraditionalFrame() {
        //
        //addKeyListener(this);

        ar1.setLayout(new BorderLayout());
        ar1.addMouseListener(this);
        ar1.addMouseMotionListener(this);
        ar1.addKeyListener(this);

        /*
        将界面分割为两部分
         */
        JSplitPane jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,false,TFInter,TFDraw); //这里第一个参数是控制分割线竖直，第二个参数是当你拖曳切割面版的分隔线时，窗口内的组件是否会随着分隔线的拖曳而动态改变大小，最后两个参数就是我分割完成后分割线两边各添加哪个容器。
        jSplitPane.setDividerLocation(250); //分割线的位置  也就是初始位置
        jSplitPane.setOneTouchExpandable(false); //是否可展开或收起，在这里没用
        jSplitPane.setDividerSize(10);//设置分割线的宽度 像素为单位
        jSplitPane.setEnabled(false); //设置分割线不可拖动！！
        TraFrame.add(jSplitPane);  //加入到面板中就好了

        //左边的界面
        TFInter.setLayout(null); //不使用布局管理器
        TFInter.setBackground(Color.WHITE); //设置界面背景颜色
        //当前颜色
        ShowColor.setText(StringColor);
        ShowColor.setBounds(30,90,200,60); //设置当前颜色提示的位置
        ShowColor.setHorizontalAlignment(ShowColor.LEFT); //设置为靠左
        ShowColor.setFont(new Font("黑体",Font.BOLD,20)); //设置字体颜色
        ShowColor.setEditable(false);//设置文本不可编辑
        TFInter.add(ShowColor); //添加到左边的界面中
        //当前像素
        ShowPixel.setText(StringPixel);
        ShowPixel.setBounds(30,180,200,60);
        ShowPixel.setHorizontalAlignment(ShowPixel.LEFT);
        ShowPixel.setFont(new Font("黑体",Font.BOLD,20));
        ShowPixel.setEditable(false);
        TFInter.add(ShowPixel);
        //待切换颜色
        ShowRandomC.setText(StringRandomC);
        ShowRandomC.setBounds(30,270,200,60);
        ShowRandomC.setHorizontalAlignment(ShowRandomC.LEFT);
        ShowRandomC.setFont(new Font("黑体",Font.BOLD,20));
        ShowRandomC.setEditable(false);
        TFInter.add(ShowRandomC);
        //待切换像素
        ShowRandomP.setText(StringRandomP);
        ShowRandomP.setBounds(30,360,200,65);
        ShowRandomP.setHorizontalAlignment(ShowRandomP.LEFT);
        ShowRandomP.setFont(new Font("黑体",Font.BOLD,20));
        ShowRandomP.setEditable(false);
        TFInter.add(ShowRandomP);
        //传统写字界面的设计

        /*
        TraFrame.getContentPane().setLayout(new BorderLayout());
        TraFrame.setBackground(Color.WHITE);
        TraFrame.getContentPane().add(ar1,BorderLayout.CENTER);
        */

        //右边的界面
        TFDraw.setLayout(new BorderLayout());
        TFDraw.setBackground(Color.WHITE);
        TFDraw.add(ar1,BorderLayout.CENTER);
        //添加菜单栏
        TraFrame.setJMenuBar(MenuB);
        //添加下拉菜单到菜单栏
        MenuB.add(MenuColor);
        MenuB.add(MenuPixel);
        //将颜色的下拉菜单添加到颜色菜单中
        /*
        黑色的选择条件去掉，下拉菜单也做修改
        MenuColor.add(ItColor0);
         */
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
        ar1.requestFocusInWindow(); //让其获得焦点，这样才能是键盘监听能够正常使用

        try {
            tablet = new JTablet();
        } catch (JTabletException e) {
            e.printStackTrace();
        }


        /*
        这一部分主要是监听用户选择的是哪个颜色
         */

        //选择黑色
        /*
        MenuColor.getItem(0).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SetColor = 0;
                StringColor = "当前颜色为黑色";
                ShowColor.setText(StringColor);
                TFInter.removeAll();
                TFInter.repaint();
                TFInter.add(ShowRandomC);
                TFInter.add(ShowColor);
                TFInter.add(ShowPixel);
                TFInter.add(ShowRandomP);
                TFInter.revalidate();
            }
        });
        */
        //选择红色
        MenuColor.getItem(1).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SetColor = 2;
                StringColor = "当前颜色为红色";
                ShowColor.setText(StringColor);
                TFInter.removeAll();
                TFInter.repaint();
                TFInter.add(ShowRandomC);
                TFInter.add(ShowColor);
                TFInter.add(ShowPixel);
                TFInter.add(ShowRandomP);
                TFInter.revalidate();
            }
        });
        //选择蓝色
        MenuColor.getItem(0).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SetColor = 1;
                StringColor = "当前颜色为蓝色";
                ShowColor.setText(StringColor);
                TFInter.removeAll();
                TFInter.repaint();
                TFInter.add(ShowRandomC);
                TFInter.add(ShowColor);
                TFInter.add(ShowPixel);
                TFInter.add(ShowRandomP);
                TFInter.revalidate();
            }
        });
        //选择黄色
        MenuColor.getItem(2).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SetColor = 3;
                StringColor = "当前颜色为黄色";
                ShowColor.setText(StringColor);
                TFInter.removeAll();
                TFInter.repaint();
                TFInter.add(ShowRandomC);
                TFInter.add(ShowColor);
                TFInter.add(ShowPixel);
                TFInter.add(ShowRandomP);
                TFInter.revalidate();
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
                StringPixel = "当前笔尖为2.0";
                ShowPixel.setText(StringPixel);
                TFInter.removeAll();
                TFInter.repaint();
                TFInter.add(ShowRandomC);
                TFInter.add(ShowColor);
                TFInter.add(ShowPixel);
                TFInter.add(ShowRandomP);
                TFInter.revalidate();
            }
        });
        //选择中等
        MenuPixel.getItem(1).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SetPixel = 3;
                StringPixel = "当前笔尖为3.0";
                ShowPixel.setText(StringPixel);
                TFInter.removeAll();
                TFInter.repaint();
                TFInter.add(ShowRandomC);
                TFInter.add(ShowColor);
                TFInter.add(ShowPixel);
                TFInter.add(ShowRandomP);
                TFInter.revalidate();
            }
        });
        //选择粗
        MenuPixel.getItem(2).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SetPixel = 4;
                StringPixel = "当前笔尖为4.0";
                ShowPixel.setText(StringPixel);
                TFInter.removeAll();
                TFInter.repaint();
                TFInter.add(ShowRandomC);
                TFInter.add(ShowColor);
                TFInter.add(ShowPixel);
                TFInter.add(ShowRandomP);
                TFInter.revalidate();
            }
        });

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
            ar1.arrayListSpot.clear();
            //重绘
            ar1.repaint();
            System.out.println(StringRandomC + "--" + StringRandomP);
            pData.SetTargetColor(StringRandomC);
            pData.SetTargetLine(StringRandomP);
            //将笔的压力保存在指定文件中
            try {
                pData.AllocateTime();
                pData.AllocateTimeString();
                pData.SaveInformation();
            } catch (IOException ioException) {
                ioException.printStackTrace();
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

        //获取笔尖压力的值，应该是第一次笔尖的压力（也就是第一个点的压力值）
        System.out.println("pressure:"+pData.GetPressure());
        System.out.println("azimuth:" + pData.GetAzimuth());
        System.out.println("tilt:" + pData.GetTilt());

        //获得落笔的时间戳
        pData.AddTime(System.currentTimeMillis());
        //获得落笔的文字格式
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");
        pData.AddTimeString(dateFormat.format(new Date()));

        /*if (EntrySign == true) {
            //获得落笔的时间戳
            pData.AddTime(System.currentTimeMillis());
            //获得落笔的文字格式
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");
            pData.AddTimeString(dateFormat.format(new Date()));
        }*/


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
        //dot.RegisterListener(new TraditionalFrame());

        double x = dot.DotStarX();
        double y = dot.DotStarY();

        if (x >= 300 && x <= 700 && y >= 400 && y <= 500 && ColorFlag == true) {
            //System.out.println(x0 + " " + y0);

            //当用户进入绿色区域表示开始记录数据
            //EntrySign = true;

            int temp = dot.GetDotRandomC();
            if (temp == 0)
                StringRandomC = "请切换黑色";
            else if (temp == 1)
                StringRandomC = "请切换蓝色";
            else if (temp == 2)
                StringRandomC = "请切换红色";
            else if (temp == 3)
                StringRandomC = "请切换黄色";
            ShowRandomC.setText(StringRandomC);
            TFInter.removeAll();
            TFInter.repaint();
            TFInter.add(ShowRandomC);
            TFInter.add(ShowColor);
            TFInter.add(ShowPixel);
            TFInter.add(ShowRandomP);
            TFInter.revalidate();
            ColorFlag = false;
        } else if (x0 >= 300 && x0 <= 700 && y0 >= 400 && y0 <= 500 && ColorFlag == false){
            //当用户进入绿色区域表示开始记录数据
            //EntrySign = true;

            ShowRandomC.setText(StringRandomC);
            TFInter.removeAll();
            TFInter.repaint();
            TFInter.add(ShowRandomC);
            TFInter.add(ShowColor);
            TFInter.add(ShowPixel);
            TFInter.add(ShowRandomP);
            TFInter.revalidate();
        }else {
            //当用户进入绿色区域表示开始记录数据
            //EntrySign = false;

            StringRandomC = "";
            ShowRandomC.setText(StringRandomC);
            TFInter.removeAll();
            TFInter.repaint();
            TFInter.add(ShowRandomC);
            TFInter.add(ShowColor);
            TFInter.add(ShowPixel);
            TFInter.add(ShowRandomP);
            TFInter.revalidate();
            ColorFlag = true;
        }

        if (x0 >= 750 && x0 <= 1150 && y0 >= 400 && y0 <= 500 && PixelFlag == true) {
            //当用户进入绿色区域表示开始记录数据
            //EntrySign = true;

            int temp = dot.GetDotRandomP();
            if (temp == 2)
                StringRandomP = "请切换笔尖为2.0";
            else if (temp == 3)
                StringRandomP = "请切换笔尖为3.0";
            else if (temp == 4)
                StringRandomP = "请切换笔尖为4.0";
            ShowRandomP.setText(StringRandomP);
            TFInter.removeAll();
            TFInter.repaint();
            TFInter.add(ShowRandomC);
            TFInter.add(ShowColor);
            TFInter.add(ShowPixel);
            TFInter.add(ShowRandomP);
            TFInter.revalidate();
            PixelFlag = false;
        }else if (x0 >= 750 && x0 <= 1150 && y0 >= 400 && y0 <= 500 && PixelFlag == false) {
            //当用户进入绿色区域表示开始记录数据
            //EntrySign = true;

            ShowRandomP.setText(StringRandomP);
            TFInter.removeAll();
            TFInter.repaint();
            TFInter.add(ShowRandomC);
            TFInter.add(ShowColor);
            TFInter.add(ShowPixel);
            TFInter.add(ShowRandomP);
            TFInter.revalidate();
        }else {
            //当用户进入绿色区域表示开始记录数据
            //EntrySign = false;

            StringRandomP = "";
            ShowRandomP.setText(StringRandomP);
            TFInter.removeAll();
            TFInter.repaint();
            TFInter.add(ShowRandomC);
            TFInter.add(ShowColor);
            TFInter.add(ShowPixel);
            TFInter.add(ShowRandomP);
            TFInter.revalidate();
            PixelFlag = true;

        }

        //将点的信息记录在容器中
        ar1.arrayListSpot.add(dot);
        ar1.repaint();

        //更新点的起始坐标（下一个点的开始为上一个点的结束）
        x0 = x1;
        y0 = y1;


    }
    @Override
    public void mouseMoved(MouseEvent e) {

    }


}
