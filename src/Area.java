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
    Graphics2D g2;

    public Area(){
        arrayList = new ArrayList();
    }

    //根据用户的选择来改变线条颜色
    public void SelectColor(Graphics g,int i) {
        g2 = (Graphics2D) g;
        if (i == 0)
            g2.setColor(Color.BLUE);
        else if (i == 1)
            g2.setColor(Color.RED);
        else if (i == 2)
            g2.setColor(Color.ORANGE);

    }

    //用来显示画图的线条
    public void paint(Graphics g){
        //转换
        g2 = (Graphics2D) g;
        //设置线条颜色,默认为黑色
        g2.setColor(Color.BLACK);
        //使用容器中点的信息来画线条
        for (int i = 0; i < arrayList.size(); i++) {
            g2.draw((Line2D) arrayList.get(i));

        }
    }
}
