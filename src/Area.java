import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
/*
    date:2020-12-05
    author:王久铭
    purpose:在写字板的线条处理
 */
public class Area extends JPanel {
    //用于存储写字过程中的点的信息
    public static ArrayList<Dot> arrayListSpot;
    //画笔的颜色选择，默认0为黑色，1为蓝色，2为红色，3为黄色
    private  int ColorSet = 0;
    //画笔粗细
    private int PixelSet = 1;
    //用于设置线条的相关信息
    private Graphics2D Line;
    //用于显示测试区域（为绿色区域）
    private Graphics2D offScreen;
    //当用户进入到测试区域时显示待选颜色
    private Graphics2D WaitCol;
    //当用户进行到测试区域时显示待选像素
    private Graphics2D WaitPix;

    //抽象类Image是表示图形图像的所有类的超类，必须以平台特定的方式获取图像
    public Image offScreenImg;



    public Area(){
        arrayListSpot = new ArrayList();
    }



    //用来显示画图的线条
    public void paintComponent(Graphics g){
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
        offScreen.fillRect(300,300,400,300);
        offScreen.fillRect(900,300,400,300);

        //使用容器中点的信息来画线条
        for (int i = 0; i < arrayListSpot.size() ; i++) {

            double x0 = arrayListSpot.get(i).DotStarX();
            double y0 = arrayListSpot.get(i).DotStarY();
            double x1 = arrayListSpot.get(i).DotEndX();
            double y1 = arrayListSpot.get(i).DotEndY();

            WaitCol = (Graphics2D) g;
            if (x0 > 300 && x0 < 700 && y0 > 300 && y0 < 600) {
                //C.setColor(Color.red);
                //System.out.println("进入测试区域");
                //JOptionPane.showMessageDialog(null, "问题不能为空","格式错误",JOptionPane.ERROR_MESSAGE);

                WaitCol.setColor(Color.red);
            } else {
                WaitCol.setColor(Color.WHITE);
            }
            WaitCol.fillRect(500,0,30,30);

            System.out.println(x0 + " " + y0 + " " + x1 + " " + y1);

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
            if (PixelSet == 1)
                Line.setStroke(new BasicStroke(1));
            else if (PixelSet == 3)
                Line.setStroke(new BasicStroke(3));
            else if (PixelSet == 5)
                Line.setStroke(new BasicStroke(5));
            
            //画出线段
            Line2D line = new Line2D.Double(x0,y0,x1,y1);
            Line.draw(line);


        }
    }
}
