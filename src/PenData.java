import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
/*
    date:2020-11-30
    author:王久铭
    purpose:记录笔的在书写过程中的信息，并保存在相应的文件中
*/

public class PenData {
    public static String subject;//用户的id

    private static int pressure;//当前笔的压力
    private int tile;//当前笔的角度
    private int azimuth;//当前的方位角
    private int StartTime;//当前绘制开始的时间
    private int EndTime;//当前绘制结束的时间
    private int ChangeTime;//模式切换时间
    private int SumTime;//绘制完整时间
    private int TouchError;//误触发次数
    private int ModelError;//切换模式错误


    private RandomAccessFile csv1;// 存实验数据的文件
    private RandomAccessFile csv2;// 存实验压力的文件

    /*设置数据*/
    public void SetPressure(int pre){
        pressure = pre;
    }

    public static int GetPressure(){ return pressure; }

    public void SetTile(int til){
        tile = til;
    }
    public int GetTile(){
        return tile;
    }

    public void SetAzimuth(int azi){
        azimuth = azi;
    }
    public int GetAzimuth(){
        return azimuth;
    }

    /*
    在文件记录数据这一部分有待改善，文件存储的数据有待讨论，目前以压力值的存储为测试
    在不同模式下，需要记录的信息不一样
        传统写字面板模式：
            用户ID，实验组数，一组实验里的实验次数，模式切换技术，每次切换的技术，每次实验出现的目标颜色，
            每次实验出现的目标线条粗细，绘画开始的时间（第一次绘制落笔时间），画线绘制结束时间（第二次抬笔时间），
            模式切换时间（两次切换时间之和），绘制完整时间（整体绘制三次时间之和），误触发数（在未弹出切换指令前，错误的切换出命令菜单），
            模式切换错误数，做切换过程时到达的最后压力，做切换过程时到达的最后倾斜角，做切换时到达最后方位角
    */
    //保留笔的倾斜角
    public void SaveTile(int til) {
        
    }
    //保留笔的压力
    public void SavePre(int pre) throws IOException {//存的是压力的数据，放在叫做
        File saveFile2 = new File("basic pres" + ".csv");
        csv2 = new RandomAccessFile(saveFile2, "rw");
        int csvLen = (int) csv2.length();
        String SaveText = "";
        if (csvLen == 0) {
            SaveText = "Pressure" + "\n";
            csv2.write(SaveText.getBytes());
        }
        csv2.skipBytes(csvLen);
        SaveText = pre + "\n";
        csv2.write(SaveText.getBytes());
        csv2.close();
    }

    /*测试压力值是否能正常写入文件中*/
    /*
    public static void main(String[] arge) throws IOException {
        PenData p=new PenData();
        p.SavaPre(10);
    }
    */

}
