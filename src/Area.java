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


    //抽象类Image是表示图形图像的所有类的超类，必须以平台特定的方式获取图像
    public Image offScreenImg;

    public Area(){
        arrayListSpot = new ArrayList();
    }
    //画笔的颜色选择，默认0为黑色，1为蓝色，2为红色，3为黄色
    private  int ColorSet = 0;

    private Graphics2D g2;

    //用来显示画图的线条
    public void paint(Graphics g){
        /*
        没有这两步的话可能会导致界面错位
         */
        //得到图片的一份Copy
        offScreenImg = this.createImage(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height+20);
        //绘制与已经缩放以适应指定矩形内的指定图像的大小
        g.drawImage(offScreenImg, 0, 0, Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height, this);

        //转换
        g2 = (Graphics2D) g;

        //使用容器中点的信息来画线条
        for (int i = 0; i < arrayListSpot.size() - 1; i++) {

            ColorSet = arrayListSpot.get(i).DotColor();

            if (ColorSet == 0)
                g2.setColor(Color.BLACK);
            else if (ColorSet == 1)
                g2.setColor(Color.BLUE);
            else if (ColorSet == 2)
                g2.setColor(Color.RED);
            else if (ColorSet == 3)
                g2.setColor(Color.ORANGE);

            double x0 = arrayListSpot.get(i).DotStarX();
            double y0 = arrayListSpot.get(i).DotStarY();
            double x1 = arrayListSpot.get(i).DotEndX();
            double y1 = arrayListSpot.get(i).DotEndY();

            //System.out.println(x0 + " " + y0 + " " + x1 + " " + y1);
            //画出线段
            Line2D line = new Line2D.Double(x0,y0,x1,y1);

            g2.draw(line);
        }
    }
}
