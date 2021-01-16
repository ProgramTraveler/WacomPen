import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;

/*
    date:2021-01-16
    author:王久铭
    purpose:该类的作用是将笔压力的动态信息进行图像的动态展示，专门为P-实列化界面使用
 */
public class PAExperimentPanel extends JPanel {
    private boolean OpenMenu = false; //表示是否打开选择菜单
    private int CurrentPress = -1; //记录当前的压力值

    private int  permeationRate = 180;
    private Color ClearWhite = new Color( Color.white.getRed(), Color.white.getGreen(), Color.white.getBlue(), permeationRate);
    private Color ClearGray = new Color( Color.gray.getRed(), Color.gray.getGreen(), Color.gray.getBlue(), permeationRate);
    private Color ClearLightGray = new Color( Color.lightGray.getRed(), Color.lightGray.getGreen(), Color.lightGray.getBlue(), permeationRate);
    private Color ClearBlack = new Color( Color.black.getRed(), Color.black.getGreen(), Color.black.getBlue(), permeationRate);
    private Color ClearGreen = new Color( Color.green.getRed(), Color.green.getGreen(), Color.green.getBlue(), permeationRate);

    private Point FeedbackShowPoint = new Point(); //记录点的位置，为后面的压力提示，颜色和像素菜单切换提供位置基础
    private int PressureFeedbackWidth = 50;
    private int PressureFeedbackHeight = 80;
    private int PressureCursorRadius = 3;
    private int MaxPressure =1024;
    private int TriggerPressureSwitch = 700;

    private int NumberOfMenu = 6;
    private int MenuX = 0;
    private int MenuY = 0;
    private int MenuWidth =100;
    private int MenuHeight = 20;
    private int SelectMenuItem = -1;
    private int MenuTargetItem = 4;
    private Color MenuItemColor = Color.WHITE;
    private Color MenuLineColor = Color.GRAY;
    private Color SelectMenuItemColor = Color.LIGHT_GRAY;
    private Color SelectMenuTargetItem = Color.GREEN;
    private Color MenuTargetItemColor = Color.RED;

    public static ArrayList<Dot> arrayListSpot; //记录点在绘画过程中的信息,为了方便可以直接调用，就写成了public的
    private Graphics2D Line; //设置线条的相关信息
    private Graphics2D offScreen; //显示测试区域
    //抽象类Image是表示图形图像的所有类的超类，必须以平台特定的方式获取图像
    private Image offScreenImg = null;
    private int ColorSet = 0; //设置画笔的颜色
    private int PixelSet = 1; //设置画笔的像素

    public PAExperimentPanel() { arrayListSpot = new ArrayList<Dot>(); }

    //用来控制像素和颜色选择菜单是否展开
    public void SetOpenMenu(boolean b) { this.OpenMenu = b; }
    //传入的笔尖压力值
    public void SetCurrentPress(int c) { this.CurrentPress = c; }
    //传入当前点的坐标
    public void SetShowPoint(Point p) { this.FeedbackShowPoint = p; }
    //图像的重绘界面
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics2D = (Graphics2D) g;
        //这一步是干嘛的？
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

        this.PaintTestArea(g); //绘画出测试区域
        this.PaintPressFeedback(graphics2D);

        //如果要打开颜色和像素的选择菜单
        if (OpenMenu)
            this.PaintOpenMenu(graphics2D);
    }
    public void PaintPressFeedback(Graphics2D graphics2D) {
        //显示压力值波动的方框
        graphics2D.setColor(ClearWhite);
        graphics2D.fillRect( (int)FeedbackShowPoint.getX() - PressureFeedbackWidth / 2, (int)FeedbackShowPoint.getY() - PressureFeedbackHeight,
                PressureFeedbackWidth, PressureFeedbackHeight);
        graphics2D.setColor(ClearLightGray);
        graphics2D.drawRect( (int)FeedbackShowPoint.getX() - PressureFeedbackWidth / 2, (int)FeedbackShowPoint.getY() - PressureFeedbackHeight,
                PressureFeedbackWidth, PressureFeedbackHeight);
        //显示压力值将要到达的那个绿色区域
        double TargetHeight = PressureFeedbackHeight * ( ( (double)MaxPressure - (double)TriggerPressureSwitch) / (double)MaxPressure);
        graphics2D.setColor(ClearGreen);
        graphics2D.fillRect( (int)FeedbackShowPoint.getX() - PressureFeedbackWidth / 2, (int)FeedbackShowPoint.getY() - PressureFeedbackHeight,
                PressureFeedbackWidth, (int)TargetHeight );

        graphics2D.setColor( ClearLightGray);
        graphics2D.drawRect( (int)FeedbackShowPoint.getX() - PressureFeedbackWidth / 2, (int)FeedbackShowPoint.getY() - PressureFeedbackHeight,
                PressureFeedbackWidth, (int)TargetHeight );
        //黑点上升的那条直线
        graphics2D.drawLine( (int)FeedbackShowPoint.getX(), (int)FeedbackShowPoint.getY() - PressureFeedbackHeight,
                (int)FeedbackShowPoint.getX(), (int)FeedbackShowPoint.getY() );
        //动态变化的黑点
        if ( CurrentPress >= 0 ) {
            graphics2D.setColor( ClearBlack);
            double RatioY = FeedbackShowPoint.getY() - PressureFeedbackHeight * ( (double)CurrentPress / (double)MaxPressure);
            graphics2D.fillArc( (int)FeedbackShowPoint.getX() - PressureCursorRadius, (int)RatioY - PressureCursorRadius,
                    PressureCursorRadius * 2, PressureCursorRadius * 2, 0, 360 );
        }
    }
    public void PaintOpenMenu(Graphics2D graphics2D) {
        for (int i =0; i < NumberOfMenu; i ++) {
            graphics2D.setColor(MenuItemColor);
            graphics2D.fillRect(MenuX - MenuWidth,MenuY + (MenuHeight * i),MenuWidth,MenuHeight);

            graphics2D.setColor(MenuLineColor);
            graphics2D.drawRect(MenuX - MenuWidth,MenuY + (MenuHeight * i),MenuWidth,MenuHeight);

        }
        if (SelectMenuItem >= 0) {
            graphics2D.setColor(SelectMenuItemColor);
            graphics2D.fillRect(MenuX - MenuWidth,MenuY + (MenuHeight * SelectMenuItem),MenuWidth,MenuHeight);
            graphics2D.setColor(MenuLineColor);
            graphics2D.drawRect(MenuX - MenuWidth,MenuY + (MenuHeight * SelectMenuItem),MenuWidth,MenuHeight);
        }

        if (SelectMenuItem == MenuTargetItem)
            graphics2D.setColor(SelectMenuTargetItem);
        else
            graphics2D.setColor(MenuTargetItemColor);
        graphics2D.fillRect(MenuX - MenuWidth,MenuY + (MenuHeight * MenuTargetItem),MenuWidth,MenuHeight);

        graphics2D.setColor(MenuLineColor);
        graphics2D.drawRect(MenuX - MenuWidth,MenuY + (MenuHeight * MenuTargetItem),MenuWidth,MenuHeight);
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
        }
    }
}
