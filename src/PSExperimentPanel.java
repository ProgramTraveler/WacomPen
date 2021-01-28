import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/*
    date:2021-01-27
    author:王久铭
    purpose:压力离散化的显示界面，将压力的波动动态显示，专门为P-离散化使用
 */
public class PSExperimentPanel extends JPanel {
    private boolean OpenMenu = false; //表示是否打开选择菜单
    private int CurrentPress = -1; //记录当前的压力值

    private int  permeationRate = 180;
    private Color ClearWhite = new Color( Color.white.getRed(), Color.white.getGreen(), Color.white.getBlue(), permeationRate);
    private Color ClearLightGray = new Color( Color.lightGray.getRed(), Color.lightGray.getGreen(), Color.lightGray.getBlue(), permeationRate);
    private Color ClearBlack = new Color( Color.black.getRed(), Color.black.getGreen(), Color.black.getBlue(), permeationRate);
    private Color ClearRed = new Color( Color.red.getRed(), Color.red.getGreen(), Color.red.getBlue(), permeationRate);

    private Point FeedbackShowPoint = new Point(); //记录点的位置，为后面的压力提示，颜色和像素菜单切换提供位置基础
    private int PressureFeedbackWidth = 50;
    private int PressureFeedbackHeight = 80;
    private int PressureCursorRadius = 3;
    private int MaxPressure =1024;
    private int TriggerPressureSwitch = 876;

    private int NumberOfMenu = 2; //可以选择的菜单栏，分别是颜色选择和像素选择
    private int MenuX = 0; //菜单的弹出位置 X值
    private int MenuY = 0; //菜单的弹出位置 Y值
    private int MenuWidth =50; //设置菜单的宽
    private int MenuHeight = 40; //设置菜单的高
    private int SelectMenuItem = -1; //选择的菜单栏

    private Color MenuItemColor = Color.WHITE;
    private Color MenuLineColor = Color.GRAY;
    private Color SelectMenuItemColor = Color.LIGHT_GRAY;

    public static ArrayList<Dot> arrayListSpot; //记录点在绘画过程中的信息,为了方便可以直接调用，就写成了public的
    private Graphics2D Line; //设置线条的相关信息
    private Graphics2D offScreen; //显示测试区域
    //抽象类Image是表示图形图像的所有类的超类，必须以平台特定的方式获取图像
    private Image offScreenImg = null;
    private int ColorSet = 0; //设置画笔的颜色
    private int PixelSet = 1; //设置画笔的像素
    //颜色和像素标签，用来提示菜单内容
    private JLabel ColorJLabel = new JLabel("颜色"); //颜色标签
    private JLabel PixelJLabel = new JLabel("像素"); //像素标签
    //在颜色菜单和像素菜单的基础上还能选择的分支菜单
    private int NumberColor_Pixel = 3; //颜色和像素分别都可以选择3个
    private int MenuItemWidth = 50; //设置分支菜单的宽
    private int MenuItemHeight = 40; //设置分支菜单的高

    private boolean ShowBack = false; //用来控制是否显示压力的动态图像,默认为不打开

    private boolean ShowColorMenu = false; //是否显示颜色分支菜单
    private int SelectColorItem = -1; //颜色分支菜单中的具体颜色
    private JLabel ColorBlue = new JLabel("蓝色");
    private JLabel ColorRed = new JLabel("红色");
    private JLabel ColorYellow = new JLabel("黄色");

    private boolean ShowPixelMenu = false; //是否显示像素分支菜单
    private int SelectPixelItem = -1; //像素分支菜单中的具体像素
    private JLabel PixelTow = new JLabel("2.0");
    private JLabel PixelThree = new JLabel("3.0");
    private JLabel PixelFour = new JLabel("4.0");

    private int SetColor = 0; //记录被选择的颜色
    private int SetPixel = 1; //记录被选择的像素

    public PSExperimentPanel() { arrayListSpot = new ArrayList<Dot>(); }
    //用来选择是否显示压力的动态图像
    public void SetShowBack(boolean b) { ShowBack = b; }
    //传入当前点的坐标
    public void SetShowPoint(Point p) { this.FeedbackShowPoint = p; }
    //返回用户选择的颜色（是对应菜单中的颜色）
    public int GetSetColor() { return SetColor; }
    //放回用户选择的像素（是对应菜单中的像素）
    public int GetSetPixel() { return SetPixel; }
    //设置是否显示颜色分支菜单
    public void SetShowColorMenu(boolean b) { ShowColorMenu = b; }
    public boolean GetShowColorMenu() { return ShowColorMenu; }


}
