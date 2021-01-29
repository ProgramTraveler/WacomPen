import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
/*
    date:2021-01-27
    author:王久铭
    purpose:压力离散化的显示界面，将压力的波动动态显示，专门为P-离散化使用
 */
public class PSExperimentPanel extends JPanel {
    private int CurrentPress = -1; //记录当前的压力值

    private int  permeationRate = 180;
    private Color ClearWhite = new Color( Color.white.getRed(), Color.white.getGreen(), Color.white.getBlue(), permeationRate);
    private Color ClearLightGray = new Color( Color.lightGray.getRed(), Color.lightGray.getGreen(), Color.lightGray.getBlue(), permeationRate);
    private Color ClearBlack = new Color( Color.black.getRed(), Color.black.getGreen(), Color.black.getBlue(), permeationRate);
    private Color ClearRed = new Color( Color.red.getRed(), Color.red.getGreen(), Color.red.getBlue(), permeationRate);

    private Point FeedbackShowPoint = new Point(); //记录点的位置，为后面的压力提示，颜色和像素菜单切换提供位置基础
    private int PressureFeedbackWidth = 90;
    private int PressureFeedbackHeight = 120;
    private int PressureCursorRadius = 3;
    private int MaxPressure =1023;

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

    private boolean ShowBack = false; //用来控制是否显示压力的动态图像,默认为不打开

    private boolean ShowColorMenu = true; //是否显示颜色分支菜单
    private int SelectColorItem = -1; //颜色分支菜单中的具体颜色
    private JLabel ColorBlue = new JLabel("蓝色");
    private JLabel ColorRed = new JLabel("红色");
    private JLabel ColorYellow = new JLabel("黄色");

    private boolean ShowPixelMenu = true; //是否显示像素分支菜单
    private int SelectPixelItem = -1; //像素分支菜单中的具体像素
    private JLabel PixelTow = new JLabel("2.0");
    private JLabel PixelThree = new JLabel("3.0");
    private JLabel PixelFour = new JLabel("4.0");

    private int SetColor = 0; //记录被选择的颜色
    private int SetPixel = 1; //记录被选择的像素

    public PSExperimentPanel() { arrayListSpot = new ArrayList<Dot>(); }
    //传入的笔尖压力值
    public void SetCurrentPress(int c) { this.CurrentPress = c; }
    //用来选择是否显示压力的动态图像
    public void SetShowBack(boolean b) { ShowBack = b; }
    //传入当前点的坐标
    public void SetShowPoint(Point p) { this.FeedbackShowPoint = p; }
    //返回用户选择的颜色（是对应菜单中的颜色）
    public int GetSetColor() { return SetColor; }
    //放回用户选择的像素（是对应菜单中的像素）
    public int GetSetPixel() { return SetPixel; }
    //图像的重绘界面
    public void paintComponent(Graphics g) {
        Graphics2D graphics2D = (Graphics2D) g;
        this.PaintTestArea(g); //绘制出测试区域
        //显示动态压力菜单
        if (ShowBack)
            this.PaintPressFeedback(graphics2D);
    }
    //绘制出压力动态显示界面
    public void PaintPressFeedback(Graphics2D graphics2D) {
        //显示压力值波动的方框
        graphics2D.setColor(ClearWhite);
        graphics2D.fillRect( (int)FeedbackShowPoint.getX() - PressureFeedbackWidth / 2, (int)FeedbackShowPoint.getY() - PressureFeedbackHeight,
                PressureFeedbackWidth, PressureFeedbackHeight);
        graphics2D.setColor(ClearLightGray);
        graphics2D.drawRect( (int)FeedbackShowPoint.getX() - PressureFeedbackWidth / 2, (int)FeedbackShowPoint.getY() - PressureFeedbackHeight,
                PressureFeedbackWidth, PressureFeedbackHeight);
        //颜色标签所占高度（按压力比例）
        double TargetColorHeight = PressureFeedbackHeight * (((double)1023 - (double) 863) / (double) 1023);
        //像素标签所占高度（按压力比例）
        double TargetPixelHeight = PressureFeedbackHeight * (((double)863 - (double)702) / (double)1023);
        //显示颜色菜单标签
        if (ShowColorMenu) {
            ColorJLabel.setBounds((int)FeedbackShowPoint.getX() - PressureFeedbackWidth / 6, (int)FeedbackShowPoint.getY() - PressureFeedbackHeight, PressureFeedbackWidth, (int)TargetColorHeight);
            this.add(ColorJLabel);
            //画出颜色的分隔线
            graphics2D.setColor(ClearLightGray);
            graphics2D.drawRect( (int)FeedbackShowPoint.getX() - PressureFeedbackWidth / 2, (int)FeedbackShowPoint.getY() - PressureFeedbackHeight, PressureFeedbackWidth, (int)TargetColorHeight);
        }else {

        }
        //显示像素菜单标签
        if (ShowPixelMenu) {
            PixelJLabel.setBounds((int)FeedbackShowPoint.getX() - PressureFeedbackWidth / 6, (int)FeedbackShowPoint.getY() - PressureFeedbackHeight + (int)TargetColorHeight, PressureFeedbackWidth, (int)TargetPixelHeight);
            this.add(PixelJLabel);
            //画出像素的分隔线
            graphics2D.setColor(ClearLightGray);
            graphics2D.drawRect( (int)FeedbackShowPoint.getX() - PressureFeedbackWidth / 2, (int)FeedbackShowPoint.getY() - PressureFeedbackHeight + (int)TargetColorHeight, PressureFeedbackWidth, (int)TargetColorHeight);
        }
        //黑点上升的那条直线
        graphics2D.drawLine( (int)FeedbackShowPoint.getX(), (int)FeedbackShowPoint.getY() - PressureFeedbackHeight,
                (int)FeedbackShowPoint.getX(), (int)FeedbackShowPoint.getY() );
        //动态变化的黑点
        if ( CurrentPress >= 0 ) {
            graphics2D.setColor( ClearBlack);
            double RatioY = FeedbackShowPoint.getY() - PressureFeedbackHeight * ( (double)CurrentPress / (double)MaxPressure);
            graphics2D.fillArc( (int)FeedbackShowPoint.getX() - PressureCursorRadius, (int)RatioY - PressureCursorRadius, PressureCursorRadius * 2, PressureCursorRadius * 2, 0, 360 );
        }
    }

    public void PaintTestArea(Graphics g) {
        /*
           没有这两步的话可能会导致界面错位
         */
        //得到图片的一份Copy
        offScreenImg = this.createImage(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height+20);
        //绘制与已经缩放以适应指定矩形内的指定图像的大小
        g.drawImage(offScreenImg, 0, 0, Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height, this);

        //转换
        Line = (Graphics2D) g;

        //设置写字板中的测试区域
        offScreen = (Graphics2D) g;
        offScreen.setColor(Color.GREEN);
        offScreen.fillRect(200,50,1150,100);

        //使用容器中点的信息来画线条
        for (int i = 0; i < arrayListSpot.size() ; i++) {

            double x0 = arrayListSpot.get(i).DotStarX();
            double y0 = arrayListSpot.get(i).DotStarY();
            double x1 = arrayListSpot.get(i).DotEndX();
            double y1 = arrayListSpot.get(i).DotEndY();

            //判断点的颜色
            ColorSet = arrayListSpot.get(i).DotColor();
            if (ColorSet == 0)
                Line.setColor(Color.BLACK);
            else if (ColorSet == 1)
                Line.setColor(Color.BLUE);
            else if (ColorSet == 2)
                Line.setColor(Color.RED);
            else if (ColorSet == 3)
                Line.setColor(Color.ORANGE);

            //判断点的像素
            PixelSet = arrayListSpot.get(i).DotPixel();
            if (PixelSet == 2)
                Line.setStroke(new BasicStroke(2));
            else if (PixelSet == 3)
                Line.setStroke(new BasicStroke(3));
            else if (PixelSet == 4)
                Line.setStroke(new BasicStroke(4));
            //画出线段
            Line2D line = new Line2D.Double(x0,y0,x1,y1);
            Line.draw(line);
            Line.setStroke(new BasicStroke(1));
        }
    }
}
