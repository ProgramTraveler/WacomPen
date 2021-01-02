import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
/*
    date:2020-11-30
    author:王久铭
    purpose:记录笔的在书写过程中的信息，并保存在相应的文件中，是在实验过程中需要保存的数据
*/

public class PenData {
    private static int pressure;//当前笔的压力
    private static int tilt;//当前笔的角度
    private static int azimuth;//当前的方位角

    private static String Name;//用户的姓名
    private static String BlockNumber;//实验组数
    private static int TrialNumber;//一组实验里实验的次数
    private static String ModeTechnique;//选择的模式切换技术
    private static String TargetColor;//每次实验的出现的目标颜色
    private static String TargetLine;//每次实验出现的目标线条粗细
    private static long StartTime;//当前绘制开始的时间(第一次绘制落笔的时间)
    private static long EndTime;//当前绘制结束的时间（第三次绘制抬笔的时间）
    private static long ModeSwitchTime;//模式切换时间（两次切换的时间之和）
    private static long CompleteTime;//绘制完整时间（整体三次绘制的时间之和）
    private static long PaintTime1;//第一段画线绘制的时间
    private static long PaintTime2;//第二段画线绘制的时间
    private static long PaintTime3;//第三段画线绘制的时间
    private static int TouchError;//误触发次数（在未弹出切换指令前，错误的切换出命令菜单）
    private static int ModelError;//切换模式错误


    private RandomAccessFile csv;// 存实验数据的文件


    /*
    获取笔在使用过程中的数据
     */
    //获取笔尖的压力
    public void SetPressure(int pre) {
        pressure = pre;
    }
    public static int GetPressure() { return pressure; }
    //获取笔的倾斜角
    public void SetTile(int til) {
        tilt = til;
    }
    public int GetTile() {
        return tilt;
    }
    //获取笔的旋转角
    public void SetAzimuth(int azi) {
        azimuth = azi;
    }
    public int GetAzimuth() {
        return azimuth;
    }
    /*
    获取实验数据
     */
    //获取实验者的名字
    public void SetName(String s) {
        Name = s;
    }
    public String GetName() {
        return Name;
    }
    //获取实验组数
    public void SetBlock(String b) {
        BlockNumber = b;
    }
    public String GetBlock() {
        return BlockNumber;
    }
    //一组实验中的实验次数
    public void SetTrialN(int n) {
        TrialNumber = n;
    }
    public int GetTrialN() {
        return TrialNumber;
    }
    //模式切换技术
    public void SetModeNa(String s) {
        ModeTechnique = s;
    }
    public String GetModeNa() {
        return ModeTechnique;
    }
    //获取每次实验时要切换的颜色
    public void SetTargetColor(String s) {
        TargetColor = s;
    }
    public String GetTargetColor() {
        return TargetColor;
    }
    //获取每次实验时要切换的线条粗细
    public void SetTargetLine(String s) {
        TargetLine = s;
    }
    public String GetTargetLine() {
        return TargetLine;
    }
    //实验开始的时间
    public void SetStartT(long l) {
        StartTime = l;
    }
    public long GetStartT() {
        return StartTime;
    }
    //实验结束的时间
    public void SetEndT(long l) {
        EndTime = l;
    }
    public long GetEndT() {
        return EndTime;
    }
    //在模式切换的时间
    public void SetModeT(long l) {
        ModeSwitchTime = l;
    }
    public long GetModeT() {
        return ModeSwitchTime;
    }
    //绘制的完整时间
    public void SetCompleteT(long l) {
        CompleteTime = l;
    }
    public long GetCompleteT() {
        return CompleteTime;
    }
    //第一段画线绘制的时间
    public void SetPaintT1(long l) {
        PaintTime1 = l;
    }
    public long GetPaintT1() {
        return PaintTime1;
    }
    //第二段画线绘制的时间
    public void SetPaintT2(long l) {
        PaintTime2 = l;
    }
    public long GetPaintT2() {
        return PaintTime2;
    }
    //第三段画线绘制的时间
    public void SetPaintT3(long l) {
        PaintTime3 = l;
    }
    public long GetPaintT3() {
        return PaintTime3;
    }
    //错误触发次数
    public void SetTouchE(int n) {
        TouchError = n;
    }
    public int GetTouchE() {
        return TouchError;
    }
    //切换模式错误的次数
    public void SetModeE(int n) {
        ModelError = n;
    }
    public int GetModeE() {
        return ModelError;
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
    /*
    保留笔在测试过程中的数据
     */
    public void SaveInformation() throws IOException {
        //存实验数据的文件名
        File saveFile = new File("information.csv");
        csv = new RandomAccessFile(saveFile, "rw");
        int CsvLine = (int) csv.length();
        String saveText = "";
        if (CsvLine == 0) {
            /*saveText = "Name" + "Block Number" + "Trial Number" + "Mode switching technique" + "Target color" + "Target line thickness" +
                    "Start Time" + "End Time" + "Mode Switching Time" + "Complete Time" + "Painting Time 1" + "Paint Time 2" + "Paint Time 3"
                    + "Number of false trigger" + "Switching Error Number" + "Pressure" + "Tilt" + "Azimuth" + "\n";*/
            saveText = "姓名" + "实验组数" + "一组实验次数" + "模式切换技术" + "目标颜色" + "目标粗细" +
                    "开始时间" + "结束时间" + "模式切换时间" + "绘制完整时间" + "第一段绘制时间" + "第二段绘制时间" + "第三段绘制时间"
                    + "误触发数" + "模式切换错误数" + "压力" + "倾斜角" + "方位角" + "\n";
            csv.write(saveText.getBytes("GBK"));
        }
        csv.skipBytes(CsvLine);
        saveText = Name + "," + BlockNumber + "," + TrialNumber + "," + ModeTechnique + "," + TargetColor + "," + TargetLine + ","
                + StartTime + "," + EndTime + "," + ModeSwitchTime + "," + CompleteTime + "," +PaintTime1 + ","
                + PaintTime2 + "," + PaintTime3 + "," + TouchError + "," + ModelError + "," + pressure +"." + tilt + "," + azimuth + "," + "\n";
        csv.write(saveText.getBytes("GBK"));
        csv.close();
    }


    /*
    以下部分只是为了测试数据保存的情况
     */
    //保留笔的压力
    /*
    public void SavePre() throws IOException {//存的是压力的数据，放在叫做
        File saveFile2 = new File("basic pres" + ".csv");
        csv2 = new RandomAccessFile(saveFile2, "rw");
        int csvLen = (int) csv2.length();
        System.out.println("csLen:" + csvLen);
        String SaveText = "";
        if (csvLen == 0) {
            SaveText = "Pressure" + "\n";
        }else {
            csv2.skipBytes(csvLen);
            SaveText = pressure + "\n";
        }
        csv2.write(SaveText.getBytes());
        csv2.close();


    }
*/
    /*测试压力值是否能正常写入文件中*/
    /*
    public static void main(String[] arge) throws IOException {
        PenData p=new PenData();
        p.SavaPre(10);
    }
    */

}