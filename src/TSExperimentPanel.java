import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/*
    date:2021-01-30
    author:王久铭
    purpose:倾斜角离散化显示界面，将倾斜角的变化动态显示，专门为倾斜角离散化使用
 */
public class TSExperimentPanel extends JPanel {
    private int CurrentTilt = -1; //记录当前的倾斜角的值

    private int PenHeight = 70; //提示笔的长度
    private int PenWidth = 1; //提示笔的宽度
    private int PenLineWidth = 2;
    private int PenTip = 3;
    private int AngleFeedBackRadius = PenHeight + 5;
    private int MinAngle = 22; //最小角度
    private int MaxAngle = 90; //最大角度

    private int TriggerAngleSwitch_1 = 54;
    private int TriggerAngleSwitch_2 = 71;

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
    //颜色和像素标签，用来提示菜单内容，由于分为两部分，所以需要两个颜色和两个像素标签
    /*private JLabel ColorJLabel1 = new JLabel("颜色"); //上角度颜色标签
    private JLabel ColorJLabel2 = new JLabel("颜色"); //下角度颜色标签
    private JLabel PixelJLabel1 = new JLabel("像素"); //上角度像素标签
    private JLabel PixelJLabel2 = new JLabel("像素"); //下角度像素标签*/

    private JLabel ColorJLabel = new JLabel("颜色"); //颜色标签
    private JLabel PixelJLabel = new JLabel("像素"); //像素标签

    private boolean ShowBack = false; //用来控制是否显示压力的动态图像,默认为不打开

    private boolean ShowColorMenu = true; //是否显示颜色分支菜单

    private boolean ShowPixelMenu = true; //是否显示像素分支菜单
    private JLabel PixelTow = new JLabel("2.0");
    private JLabel PixelThree = new JLabel("3.0");
    private JLabel PixelFour = new JLabel("4.0");

    private boolean ShowItemMenu = false; //是否有二级菜单展开

    private int SetColor = 0; //记录被选择的颜色
    private int SetPixel = 1; //记录被选择的像素

    public TSExperimentPanel() { arrayListSpot = new ArrayList<Dot>(); }
    //传入的笔尖倾斜角值
    public void SetCurrentTilt(int c) { this.CurrentTilt = c; }
    //传入当前点的坐标
    public void SetShowPoint(Point p) { this.FeedbackShowPoint = p; }
    //用来选择是否显示倾斜角的动态图像
    public void SetShowBack(boolean b) { ShowBack = b; }
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
    //控制二级菜单
    public void SetShowItemMenu(boolean b) { ShowItemMenu = b; }
    public boolean GetShowItemMenu() { return ShowItemMenu; }
    //图像的重绘界面
    public void paintComponent(Graphics g) {
        Graphics2D graphics2D = (Graphics2D) g;
        this.PaintTestArea(g); //绘制出测试区域
        //显示动态倾斜角菜单
        if (ShowBack)
            this.PaintTiltFeedback(graphics2D);
    }
    //绘制倾斜角动态显示界面
    public void PaintTiltFeedback(Graphics2D graphics2D) {
        graphics2D.setColor(ClearWhite);
        graphics2D.fillArc((int)FeedbackShowPoint.getX() - AngleFeedBackRadius,(int)FeedbackShowPoint.getY() - AngleFeedBackRadius,AngleFeedBackRadius * 2,AngleFeedBackRadius * 2, MinAngle,MaxAngle - MinAngle);
        graphics2D.setColor(ClearBlack); //角度线和边界线的颜色
        //整个角度的弧线
        graphics2D.drawArc((int)FeedbackShowPoint.getX() - AngleFeedBackRadius, (int)FeedbackShowPoint.getY() - AngleFeedBackRadius, AngleFeedBackRadius * 2, AngleFeedBackRadius * 2, 22, 68);
        //上半部分边界线
        graphics2D.fillArc((int)FeedbackShowPoint.getX() - AngleFeedBackRadius, (int)FeedbackShowPoint.getY() - AngleFeedBackRadius, AngleFeedBackRadius * 2, AngleFeedBackRadius * 2, 90,-1);
        //下半部分边界线
        graphics2D.fillArc((int)FeedbackShowPoint.getX() - AngleFeedBackRadius, (int)FeedbackShowPoint.getY() - AngleFeedBackRadius, AngleFeedBackRadius * 2, AngleFeedBackRadius * 2, 22,1);
        //显示颜色标签
        if (ShowColorMenu && ShowPixelMenu) {
            graphics2D.setColor(ClearBlack); //设置分隔线的颜色
            //上半部分的扇形区域，由于没有直接画扇形的，所以就进行小角度的颜色填充
            graphics2D.fillArc((int)FeedbackShowPoint.getX() - AngleFeedBackRadius, (int)FeedbackShowPoint.getY() - AngleFeedBackRadius, AngleFeedBackRadius * 2, AngleFeedBackRadius * 2, 80,2);
            //下半部分的扇形区域
            graphics2D.fillArc((int)FeedbackShowPoint.getX() - AngleFeedBackRadius, (int)FeedbackShowPoint.getY() - AngleFeedBackRadius, AngleFeedBackRadius * 2, AngleFeedBackRadius * 2, 54,2);
            if (CurrentTilt >=80 && CurrentTilt <= 90) {
                ColorJLabel.setBounds((int)FeedbackShowPoint.getX(),(int)FeedbackShowPoint.getY() - AngleFeedBackRadius - 50, AngleFeedBackRadius, AngleFeedBackRadius);
                this.add(ColorJLabel);
            }
            if (CurrentTilt <= 54 && CurrentTilt >= 38){
                ColorJLabel.setBounds((int)FeedbackShowPoint.getX() + AngleFeedBackRadius,(int)FeedbackShowPoint.getY() - AngleFeedBackRadius, AngleFeedBackRadius, AngleFeedBackRadius);
                this.add(ColorJLabel);
            }
        }else if (ShowColorMenu == false) {
            remove(ColorJLabel);
            remove(PixelJLabel);
            //设置各个颜色的显示区间
            graphics2D.setColor(Color.BLUE);
            graphics2D.fillArc((int)FeedbackShowPoint.getX() - AngleFeedBackRadius, (int)FeedbackShowPoint.getY() - AngleFeedBackRadius, AngleFeedBackRadius * 2, AngleFeedBackRadius *2, 22, 22);
            graphics2D.setColor(Color.ORANGE);
            graphics2D.fillArc((int)FeedbackShowPoint.getX() - AngleFeedBackRadius, (int)FeedbackShowPoint.getY() - AngleFeedBackRadius, AngleFeedBackRadius * 2, AngleFeedBackRadius *2, 44, 22);
            graphics2D.setColor(Color.RED);
            graphics2D.fillArc((int)FeedbackShowPoint.getX() - AngleFeedBackRadius, (int)FeedbackShowPoint.getY() - AngleFeedBackRadius, AngleFeedBackRadius * 2, AngleFeedBackRadius *2, 66, 24);
        }
        //显示像素标签
        if (ShowPixelMenu && ShowColorMenu) {
            graphics2D.setColor(ClearBlack); //设置分界线颜色
            //上半部分的扇形区域
            graphics2D.fillArc((int)FeedbackShowPoint.getX() - AngleFeedBackRadius, (int)FeedbackShowPoint.getY() - AngleFeedBackRadius, AngleFeedBackRadius * 2, AngleFeedBackRadius * 2, 71,2);
            //下半部分的颜色区域
            graphics2D.fillArc((int)FeedbackShowPoint.getX() - AngleFeedBackRadius, (int)FeedbackShowPoint.getY() - AngleFeedBackRadius, AngleFeedBackRadius * 2, AngleFeedBackRadius *2, 38, 2);
            if (CurrentTilt >= 71 && CurrentTilt < 80) {
                PixelJLabel.setBounds((int)FeedbackShowPoint.getX(),(int)FeedbackShowPoint.getY() - AngleFeedBackRadius - 50, AngleFeedBackRadius, AngleFeedBackRadius);
                this.add(PixelJLabel);
            }
            if (CurrentTilt >= 22 && CurrentTilt < 38) {
                PixelJLabel.setBounds((int)FeedbackShowPoint.getX() + AngleFeedBackRadius,(int)FeedbackShowPoint.getY() - AngleFeedBackRadius, AngleFeedBackRadius, AngleFeedBackRadius);
                this.add(PixelJLabel);
            }

        }else if (ShowPixelMenu == false) {
            remove(ColorJLabel);
            remove(PixelJLabel);

            //设置像素语句出现的位置
            PixelTow.setBounds((int)FeedbackShowPoint.getX() + 5,(int)FeedbackShowPoint.getY() - AngleFeedBackRadius - 50 + 25, AngleFeedBackRadius, AngleFeedBackRadius);
            this.add(PixelTow);
            graphics2D.fillArc((int)FeedbackShowPoint.getX() - AngleFeedBackRadius, (int)FeedbackShowPoint.getY() - AngleFeedBackRadius, AngleFeedBackRadius * 2, AngleFeedBackRadius *2, 66, 2);

            PixelThree.setBounds((int)FeedbackShowPoint.getX() + AngleFeedBackRadius - 50,(int)FeedbackShowPoint.getY() - AngleFeedBackRadius - 15, AngleFeedBackRadius, AngleFeedBackRadius);
            this.add(PixelThree);
            graphics2D.fillArc((int)FeedbackShowPoint.getX() - AngleFeedBackRadius , (int)FeedbackShowPoint.getY() - AngleFeedBackRadius, AngleFeedBackRadius * 2, AngleFeedBackRadius *2, 44, 1);

            PixelFour.setBounds((int)FeedbackShowPoint.getX() + AngleFeedBackRadius - 30,(int)FeedbackShowPoint.getY() - AngleFeedBackRadius , AngleFeedBackRadius, AngleFeedBackRadius);
            this.add(PixelFour);
        }
        //显示颜色和像素菜单的选择框
        graphics2D.setColor(ClearLightGray);
        if (ShowPixelMenu == false) {
            if (CurrentTilt >= 66 && CurrentTilt <= 90) {
                graphics2D.fillArc((int)FeedbackShowPoint.getX() - AngleFeedBackRadius, (int)FeedbackShowPoint.getY() - AngleFeedBackRadius, AngleFeedBackRadius * 2, AngleFeedBackRadius *2, 66, 24);
            }
            if (CurrentTilt >= 44 && CurrentTilt < 66) {
                graphics2D.fillArc((int)FeedbackShowPoint.getX() - AngleFeedBackRadius, (int)FeedbackShowPoint.getY() - AngleFeedBackRadius, AngleFeedBackRadius * 2, AngleFeedBackRadius *2, 44, 22);
            }
            if (CurrentTilt >= 22 && CurrentTilt < 44) {
                graphics2D.fillArc((int)FeedbackShowPoint.getX() - AngleFeedBackRadius, (int)FeedbackShowPoint.getY() - AngleFeedBackRadius, AngleFeedBackRadius * 2, AngleFeedBackRadius *2, 22, 22);
            }
        }else if (CurrentTilt >= 71 && CurrentTilt < 80 && ShowPixelMenu && ShowColorMenu) {
            graphics2D.fillArc((int)FeedbackShowPoint.getX() - AngleFeedBackRadius, (int)FeedbackShowPoint.getY() - AngleFeedBackRadius, AngleFeedBackRadius * 2, AngleFeedBackRadius *2, 71, 9);
        }else if (CurrentTilt >=22 && CurrentTilt < 38 && ShowPixelMenu && ShowColorMenu) {
            graphics2D.fillArc((int)FeedbackShowPoint.getX() - AngleFeedBackRadius, (int)FeedbackShowPoint.getY() - AngleFeedBackRadius, AngleFeedBackRadius * 2, AngleFeedBackRadius *2, 22, 16);
        }

        if (ShowColorMenu == false) {
            if (CurrentTilt >= 66 && CurrentTilt <= 90) {
                graphics2D.fillArc((int)FeedbackShowPoint.getX() - AngleFeedBackRadius, (int)FeedbackShowPoint.getY() - AngleFeedBackRadius, AngleFeedBackRadius * 2, AngleFeedBackRadius *2, 66, 24);
            }
            if (CurrentTilt >= 44 && CurrentTilt < 66) {
                graphics2D.fillArc((int)FeedbackShowPoint.getX() - AngleFeedBackRadius, (int)FeedbackShowPoint.getY() - AngleFeedBackRadius, AngleFeedBackRadius * 2, AngleFeedBackRadius *2, 44, 22);
            }
            if (CurrentTilt >= 22 && CurrentTilt < 44) {
                graphics2D.fillArc((int)FeedbackShowPoint.getX() - AngleFeedBackRadius, (int)FeedbackShowPoint.getY() - AngleFeedBackRadius, AngleFeedBackRadius * 2, AngleFeedBackRadius *2, 22, 22);
            }
        }else if (CurrentTilt >= 80 && CurrentTilt <=90 && ShowColorMenu && ShowPixelMenu) {
            graphics2D.fillArc((int)FeedbackShowPoint.getX() - AngleFeedBackRadius, (int)FeedbackShowPoint.getY() - AngleFeedBackRadius, AngleFeedBackRadius * 2, AngleFeedBackRadius *2, 80, 10);
        }else if (CurrentTilt >=38 && CurrentTilt <=54 && ShowColorMenu && ShowPixelMenu) {
            graphics2D.fillArc((int)FeedbackShowPoint.getX() - AngleFeedBackRadius, (int)FeedbackShowPoint.getY() - AngleFeedBackRadius, AngleFeedBackRadius * 2, AngleFeedBackRadius *2, 38, 16);
        }

        //当在菜单范围外，而且时一级菜单展示，那么就显示箭头
        if (CurrentTilt > 54 && CurrentTilt < 71 && ShowColorMenu && ShowPixelMenu) {
            AffineTransform affineTransform = new AffineTransform(); //构造一个新的 AffineTransform代表身份转换
            affineTransform.setToRotation(Math.toRadians(360 - CurrentTilt),FeedbackShowPoint.getX(),FeedbackShowPoint.getY());
            graphics2D.transform(affineTransform); //将此转换设置为转换的旋转变换

            BasicStroke basicStroke = new BasicStroke(PenLineWidth); //构造一个固定的 BasicStroke具有指定的线宽，并使用上限和连接样式的默认值
            graphics2D.setStroke(basicStroke);

            GeneralPath generalPath = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
            generalPath.moveTo( (int)FeedbackShowPoint.getX() + PenLineWidth, (int)FeedbackShowPoint.getY() );
            generalPath.lineTo( (int)FeedbackShowPoint.getX() + PenTip + PenLineWidth, (int)FeedbackShowPoint.getY() - PenWidth);
            generalPath.lineTo( (int)FeedbackShowPoint.getX() + PenHeight + PenLineWidth, (int)FeedbackShowPoint.getY() - PenWidth);
            generalPath.lineTo( (int)FeedbackShowPoint.getX() + PenLineWidth, (int)FeedbackShowPoint.getY());
            generalPath.closePath();

            graphics2D.setPaint(ClearPink);
            graphics2D.fill(generalPath);
            graphics2D.setPaint(ClearGray);
            graphics2D.draw(generalPath);
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
        offScreen.fillRect(200,5,1150,100);

        //颜色和像素区域分隔线
        offScreen.setColor(Color.white);
        offScreen.drawLine(583,5,583,105);
        offScreen.drawLine(966,5,966,105);
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

