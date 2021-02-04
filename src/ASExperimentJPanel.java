import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.util.ArrayList;

/*
    date:2021-02-02
    author:王久铭
    purpose:方位角离散化界面，主要是将方位角进行动态显示，专门为A-离散化使用
 */
public class ASExperimentJPanel extends JPanel {
    private int CurrentAzimuth = -1; //当前方位角的值

    private int PartitionLineLength = 40; //设置圆形的覆盖区域（分隔线长度）

    private int ArrowLineWidth = 3; //箭头线宽
    private int ArrowWidth = 2; //箭头宽度
    private int ArrowLength = 30; //箭头的长度
    private int ArrowTipWidth = 5; //箭头提示宽度
    private int ArrowTipLength = 6; //箭头提示长度

    private int  permeationRate = 180;
    private Color ClearWhite = new Color( Color.white.getRed(), Color.white.getGreen(), Color.white.getBlue(), permeationRate);
    private Color ClearLightGray = new Color( Color.lightGray.getRed(), Color.lightGray.getGreen(), Color.lightGray.getBlue(), permeationRate);
    private Color ClearBlack = new Color( Color.black.getRed(), Color.black.getGreen(), Color.black.getBlue(), permeationRate);
    private Color ClearRed = new Color( Color.red.getRed(), Color.red.getGreen(), Color.red.getBlue(), permeationRate);
    private Color ClearPink = new Color( Color.pink.getRed(), Color.pink.getGreen(), Color.pink.getBlue(), permeationRate);
    private Color ClearGray = new Color( Color.gray.getRed(), Color.gray.getGreen(), Color.gray.getBlue(), permeationRate);

    private Point FeedbackShowPoint = new Point(); //记录点的位置，为后面的压力提示，颜色和像素菜单切换提供位置基础
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

    private boolean ShowColorMenu = true; //是否显示颜色菜单

    private boolean ShowPixelMenu = true; //是否显示像支菜单
    private JLabel PixelTow = new JLabel("2.0");
    private JLabel PixelThree = new JLabel("3.0");
    private JLabel PixelFour = new JLabel("4.0");


    private int SetColor = 0; //记录被选择的颜色
    private int SetPixel = 1; //记录被选择的像素

    public ASExperimentJPanel() { arrayListSpot = new ArrayList<Dot>(); }
    //传入笔的方位角
    public void SetCurrentAzimuth(int c) { this.CurrentAzimuth = c; }
    //用来选择是否显示压力的动态图像
    public void SetShowBack(boolean b) { ShowBack = b; }
    //传入当前点的坐标
    public void SetShowPoint(Point p) { this.FeedbackShowPoint = p; }
    //返回用户选择的颜色（是对应菜单中的颜色）
    public void DefineColor(int i) { SetColor = i; } //初始化颜色
    public int GetSetColor() { return SetColor; }
    //放回用户选择的像素（是对应菜单中的像素）
    public void DefinePixel(int i) { SetPixel = i; } //初始化像素
    public int GetSetPixel() { return SetPixel; }
    //控制是打开颜色一级菜单还是二级菜单,true表示是一级菜单，false表示是二级菜单
    public void SetShowColorMenu(boolean b) { ShowColorMenu = b; }
    public boolean GetShowColorMenu() { return ShowColorMenu; } //返回颜色菜单状态
    //控制是打开颜色一级菜单还是二级菜单,true表示是一级菜单，false表示是二级菜单、
    public void SetShowPixelMenu(boolean b) { ShowPixelMenu = b; }
    public boolean GetShowPixelMenu() { return ShowPixelMenu; } //返回像素菜单状态
    //图像的重绘界面
    public void paintComponent(Graphics g) {
        Graphics2D graphics2D = (Graphics2D) g;
        this.PaintTestArea(g); //绘制出测试区域
        //显示动态压力菜单
        if (ShowBack)
            this.PaintAzimuthFeedback(graphics2D);
    }
    //绘制压力动态显示界面
    public void PaintAzimuthFeedback(Graphics2D graphics2D) {
        graphics2D.setColor(Color.WHITE); //设置测试背景为红色
        //设置圆形方位角展示区域出现的位置，红色覆盖的角度为0-360
        graphics2D.fillArc((int)FeedbackShowPoint.getX() - PartitionLineLength,(int)FeedbackShowPoint.getY() - PartitionLineLength,PartitionLineLength * 2,PartitionLineLength * 2,0,360);
        //设置弧线颜色
        graphics2D.setColor(Color.BLACK);
        //将弧线标黑(整个圆形区域的弧线)
        graphics2D.drawArc((int)FeedbackShowPoint.getX() - PartitionLineLength,(int)FeedbackShowPoint.getY() - PartitionLineLength,PartitionLineLength * 2,PartitionLineLength * 2,0,360);
        //设置颜色和像素一级菜单的分界线
        graphics2D.fillArc((int)FeedbackShowPoint.getX() - PartitionLineLength,(int)FeedbackShowPoint.getY() - PartitionLineLength,PartitionLineLength * 2,PartitionLineLength * 2,360 - 154 + 90,4);
        graphics2D.fillArc((int)FeedbackShowPoint.getX() - PartitionLineLength,(int)FeedbackShowPoint.getY() - PartitionLineLength,PartitionLineLength * 2,PartitionLineLength * 2,360 - 109 + 90,4);
        graphics2D.fillArc((int)FeedbackShowPoint.getX() - PartitionLineLength,(int)FeedbackShowPoint.getY() - PartitionLineLength,PartitionLineLength * 2,PartitionLineLength * 2,360 - 311 + 90,4);
        graphics2D.setPaint(ClearWhite); //设置用户常用区域显示为白色
        //设置圆形方位角展示区域出现的位置，白色覆盖区域为109-154
        graphics2D.fillArc(FeedbackShowPoint.x - PartitionLineLength,FeedbackShowPoint.y - PartitionLineLength,PartitionLineLength * 2,PartitionLineLength * 2,360 - 109 + 90,- 45); //88-176为常用的区域，所以用白色表示
        //显示颜色一级菜单
        if (ShowColorMenu && ShowPixelMenu) {
            ColorJLabel.setBounds((int)FeedbackShowPoint.getX() - PartitionLineLength + 40,(int)FeedbackShowPoint.getY() - PartitionLineLength - 20,PartitionLineLength * 2,PartitionLineLength * 2);
            this.add(ColorJLabel);
        }else if (ShowColorMenu == false) {
            //展开颜色二级菜单
            this.remove(ColorJLabel);
            this.remove(PixelJLabel);

            graphics2D.setColor(Color.BLUE);
            graphics2D.fillArc((int)FeedbackShowPoint.getX() - PartitionLineLength,(int)FeedbackShowPoint.getY() - PartitionLineLength,PartitionLineLength * 2,PartitionLineLength * 2,360 - 109 + 90, 52);

            graphics2D.setColor(Color.ORANGE);
            graphics2D.fillArc((int)FeedbackShowPoint.getX() - PartitionLineLength,(int)FeedbackShowPoint.getY() - PartitionLineLength,PartitionLineLength * 2,PartitionLineLength * 2,360 - 57 + 90,52);

            graphics2D.setColor(Color.RED);
            graphics2D.fillArc((int)FeedbackShowPoint.getX() - PartitionLineLength,(int)FeedbackShowPoint.getY() - PartitionLineLength,PartitionLineLength * 2,PartitionLineLength * 2,360 - 5 + 90,54);
        }
        //显示像素一级菜单
        if (ShowColorMenu && ShowPixelMenu) {
            PixelJLabel.setBounds((int)FeedbackShowPoint.getX() - PartitionLineLength + 10,(int)FeedbackShowPoint.getY() - PartitionLineLength + 10,PartitionLineLength * 2,PartitionLineLength * 2);
            this.add(PixelJLabel);
        }else if (ShowPixelMenu == false) {
            //展开像素二级菜单
            remove(ColorJLabel);
            remove(PixelJLabel);

            graphics2D.setColor(ClearBlack); //设置分隔线颜色
            //设置像素语句控制位置
            PixelTow.setBounds((int)FeedbackShowPoint.getX() - PartitionLineLength + 10,(int)FeedbackShowPoint.getY() - PartitionLineLength - 5,PartitionLineLength * 2,PartitionLineLength * 2);
            this.add(PixelTow);
            graphics2D.fillArc((int)FeedbackShowPoint.getX() - PartitionLineLength,(int)FeedbackShowPoint.getY() - PartitionLineLength,PartitionLineLength * 2,PartitionLineLength * 2,360 - 258 + 90,4);

            PixelThree.setBounds((int)FeedbackShowPoint.getX() - PartitionLineLength + 10,(int)FeedbackShowPoint.getY() - PartitionLineLength + 10 + 7,PartitionLineLength * 2,PartitionLineLength * 2);
            this.add(PixelThree);
            graphics2D.fillArc((int)FeedbackShowPoint.getX() - PartitionLineLength,(int)FeedbackShowPoint.getY() - PartitionLineLength,PartitionLineLength * 2,PartitionLineLength * 2,360 - 206 + 90,4);

            PixelFour.setBounds((int)FeedbackShowPoint.getX() - PartitionLineLength + 10 + 20,(int)FeedbackShowPoint.getY() - PartitionLineLength + 10 + 20,PartitionLineLength * 2,PartitionLineLength * 2);
            this.add(PixelFour);
        }
        //箭头的显示控制
        if (CurrentAzimuth > 109 && CurrentAzimuth < 154) {
            AffineTransform affineTransform = new AffineTransform();
            //控制箭头的角度
            affineTransform.setToRotation(- Math.toRadians(360 - CurrentAzimuth + 90), FeedbackShowPoint.getX(), FeedbackShowPoint.getY());
            graphics2D.transform(affineTransform);
            BasicStroke _arrowStroke = new BasicStroke(ArrowLineWidth);
            graphics2D.setStroke(_arrowStroke);
            GeneralPath _arrowPolygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
            _arrowPolygon.moveTo(FeedbackShowPoint.getX(), FeedbackShowPoint.getY() + ArrowWidth / 2);
            _arrowPolygon.lineTo(FeedbackShowPoint.getX() + ArrowLength, FeedbackShowPoint.getY() + ArrowWidth / 2);
            _arrowPolygon.lineTo(FeedbackShowPoint.getX() + ArrowLength, FeedbackShowPoint.getY() + ArrowWidth / 2 + ArrowTipWidth / 2);
            _arrowPolygon.lineTo(FeedbackShowPoint.getX() + ArrowLength + ArrowTipLength, FeedbackShowPoint.getY());
            _arrowPolygon.lineTo(FeedbackShowPoint.getX() + ArrowLength, FeedbackShowPoint.getY() - ArrowWidth / 2 - ArrowTipWidth / 2);
            _arrowPolygon.lineTo(FeedbackShowPoint.getX() + ArrowLength, FeedbackShowPoint.getY() - ArrowWidth / 2);
            _arrowPolygon.lineTo(FeedbackShowPoint.getX(), FeedbackShowPoint.getY() - ArrowWidth / 2);
            _arrowPolygon.closePath();
            graphics2D.setPaint(ClearPink);
            graphics2D.fill(_arrowPolygon);
            graphics2D.setPaint(ClearGray);
            graphics2D.draw(_arrowPolygon);
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
    //当抬笔时，清除所有颜色和像素标签
    public void RemoveAllJLabel() {
        this.removeAll();
        this.repaint();
    }
    //当笔在移动过程中，对分支颜色和像素进行移除和重组
    public void RemoveItemJLabel() {
        //移除所有的像素组件
        this.remove(PixelTow);
        this.remove(PixelThree);
        this.remove(PixelFour);

        this.repaint();
    }
}
