/*
    date:2020-11-30
    author:王久铭
*/

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class PenData {
    public static String subject;//用户的id
    private int pressure;//当前笔的压力
    private int tile;//当前笔的角度
    private int azimuth;//当前的方位角
    private int rotate;//旋转

    private RandomAccessFile csv1;// 存实验数据的文件
    private RandomAccessFile csv2;// 存实验压力的文件

    /*设置数据*/
    public void SetPressure(int pre){
        pressure=pre;
    }
    public int GetPressure(){
        return pressure;
    }

    public void SetTile(int til){
        tile=til;
    }
    public int GetTile(){
        return tile;
    }

    public void SetAzimuth(int azi){
        azimuth=azi;
    }
    public int GetAzimuth(){
        return azimuth;
    }

    public void SavaPre(int pre) throws IOException {//存的是压力的数据，放在叫做
        File saveFile2 = new File("basic pres" + ".csv");
        csv2 = new RandomAccessFile(saveFile2, "rw");
        int csvLen = (int) csv2.length();
        String saveText2 = "";
        if (csvLen == 0) {
            saveText2 = "Subject" + "," + "Pressure" + "," + "\n";
            csv2.write(saveText2.getBytes());
        }
        csv2.skipBytes(csvLen);
        saveText2 = subject + "," + pre + "," + "\n";
        csv2.write(saveText2.getBytes());
        csv2.close();
    }

    /*测试压力值是否能正常写入文件中*/
    public static void main(String[] arge) throws IOException {
        PenData p=new PenData();
        p.SavaPre(10);
    }

}
