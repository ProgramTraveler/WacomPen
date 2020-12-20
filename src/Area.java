/*
    date:2020-12-05
    author:王久铭
    purpose:在写字板的线条处理
 */
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class Area extends JPanel {
    //用于存储写字过程中的点的信息
    public static ArrayList arrayList;

    //抽象类Image是表示图形图像的所有类的超类，必须以平台特定的方式获取图像
    public Image offScreenImg;

    public Area(){
        arrayList = new ArrayList();
    }
    //画笔的颜色选择，默认0为黑色，1为蓝色，2为红色，3为黄色
    private  int ColorSet = 0;
    //根据用户的选择来改变线条颜色
    public void SelectColor(int i) {
        ColorSet = i;

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

        Graphics2D g2;
        //转换
        g2 = (Graphics2D) g;
        //进行颜色切换,设置线条颜色,默认为黑色
        //注意颜色选择后所有的线条都会变成这一种颜色
        if (ColorSet == 0)
            g2.setColor(Color.BLACK);
        else if (ColorSet == 1)
            g2.setColor(Color.BLUE);
        else if (ColorSet == 2)
            g2.setColor(Color.RED);
        else if (ColorSet == 3)
            g2.setColor(Color.ORANGE);

        //使用容器中点的信息来画线条
        for (int i = 0; i < arrayList.size(); i++) {
            g2.draw((Line2D) arrayList.get(i));

        }
    }
}
