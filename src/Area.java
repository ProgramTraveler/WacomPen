/*
    date:2020-12-05
    author:王久铭
    purpose:
 */
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class Area extends JPanel {
    //
    private int PenPressure=0;
    private RandomAccessFile csv2;// 存实验压力的文件
    public static ArrayList arrayList;

    public Area(){
        arrayList=new ArrayList();
    }

    /*private int StartX=0;
    private int StartY=0;*/
    private int EndX = 0;
    private int EndY = 0;

    /*
    public void SetStartX(int i){ StartX=i; }
    public void SetStartY(int i){ StartY=i; }
    */
    public void SetEndX(int i) { EndX = i; }
    public void SetEndY(int i) {
        EndY = i;
    }



    private boolean flag = false;
    public void SetFlag(boolean t) {
        flag = t;
    }

    public void SavaPre(int V1, int V2) throws IOException { // 存的是压力的数据，放在叫做
        File saveFile2 = new File("basic av" + ".csv");
        csv2 = new RandomAccessFile(saveFile2, "rw");
        int csvLen = (int) csv2.length();
        String SaveText = "";
        if (csvLen == 0) {
            SaveText = "Subject" + "," + "ValueFromFile1" + ","
                    + "ValueFromFile2" + "," + "\n";
            csv2.write(SaveText.getBytes());
        }
        csv2.skipBytes(csvLen);

        csv2.write(SaveText.getBytes());
        csv2.close();

    }

    Graphics2D g2;
    //用来显示画图的线条
    public void paint(Graphics g){

        g2=(Graphics2D) g;
        //设置线条颜色
        g2.setColor(Color.BLUE);
        //使用容器中点的信息来画线条
        for (int i = 0; i < arrayList.size(); i++) {
            g2.draw((Line2D) arrayList.get(i));

        }


    }



}
