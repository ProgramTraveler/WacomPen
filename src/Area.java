/*
    date:2020-12-05
    author:王久铭
    purpose:
 */
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class Area extends JPanel {
    //
    private int PenPressure=0;
    private RandomAccessFile csv2;// 存实验压力的文件
    protected Graphics OffScreen;
    int number; //保存目前读到第几行数据
    public static ArrayList arrayList;

    public Area(){
        arrayList=new ArrayList();
    }
    private int endX = 0;
    private int endY = 0;

    public void SetEndX(int i) {
        endX = i;
    }

    public void SetEndY(int i) {
        endY = i;
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
        //saveText2 = subject + "," + V1 + "," + V2 + "," + "\n";
        csv2.write(SaveText.getBytes());
        csv2.close();

    }


}
