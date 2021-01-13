import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Timer;
/*
    date:2020-11-30
    author:王久铭
    purpose:记录笔的在书写过程中的信息，并保存在相应的文件中，是在实验过程中需要保存的数据
*/

public class PenData {
    private static int pressure; //当前笔的压力
    private static int tilt; //当前笔的角度
    private static int azimuth; //当前的方位角

    private static String Name; //用户的姓名
    private static int BlockNumber; //实验组数
    private static int BlockN; //记录int型的实验组数，为了判断所有组数是否做完
    private static int TrialNumber = 0; //一组实验里实验的次数
    private static String ModeTechnique; //选择的模式切换技术
    private static String TargetColor; //每次实验的出现的目标颜色
    private static String TargetLine; //每次实验出现的目标线条粗细
    private static long StartTime; //当前绘制开始的时间(第一次绘制落笔的时间)
    private static String StartTimeDate; //绘制开始的时间以文字格式保存
    private static long EndTime; //当前绘制结束的时间（第三次绘制抬笔的时间）
    private static String EndTimeDate; //绘制结束的时间以文字格式保存
    private static String ModeSwitchTime; //模式切换时间（两次切换的时间之和）
    private static String CompleteTime; //绘制完整时间（整体三次绘制的时间之和）
    private static String PaintTime1; //第一段画线绘制的时间
    private static String PaintTime2; //第二段画线绘制的时间
    private static String PaintTime3; //第三段画线绘制的时间
    private static int TouchError = 0; //误触发次数（在未弹出切换指令前，错误的切换出命令菜单）
    private static int ModelError = 0; //切换模式错误

    private static String ResultColor; //用户实际切换的颜色
    private static String ResultPixel; //用户实际切换的像素

  //记录在测试过程中的每个时间戳
    private ArrayList<Long> TimeList = new ArrayList<Long>();
    //记录在测试过程中的每个时间
    private ArrayList<String> TimeListString = new ArrayList<String>();

    private RandomAccessFile csv; // 存实验数据的文件

    /*
    获取笔在使用过程中的数据
     */
    //获取笔尖的压力
    public void SetPressure(int pre) { pressure = pre; }
    public static int GetPressure() { return pressure; }
    //获取笔的倾斜角
    public void SetTilt(int til) { tilt = til; }
    public int GetTilt() { return tilt; }
    //获取笔的旋转角
    public void SetAzimuth(int azi) { azimuth = azi; }
    public int GetAzimuth() { return azimuth; }
    /*
    获取实验数据
     */
    //获取实验者的名字
    public void SetName(String s) { Name = s; }
    public String GetName() { return Name; }
    //获取实验组数
    public void SetBlock(int b) {
        BlockNumber = b;
        //将输入的String型的组数变为int型（因为开始做的时候就是第一组，所以组数减一）
        //BlockN = Integer.getInteger(b) ;
    }
    public int GetBlock() { return BlockNumber; }

    //一组实验中的实验次数
    public void SetTrialN(int n) { TrialNumber = n; }
    public void AddTrialN() { TrialNumber ++; }
    public int GetTrialN() { return TrialNumber; }
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
    //错误触发次数
    public void SetTouchE(int i) { TouchError = i; }
    public void AddTouchE() {
        TouchError ++;
    }
    public int GetTouchE() {
        return TouchError;
    }
    //切换模式错误的次数
    public void SetModeE(int i) {
        ModelError = i;
    }
    public void AddModeE() {
        if (TargetColor.equals(ResultColor) == false)
            ModelError ++;
        if (TargetLine.equals(ResultPixel) == false)
            ModelError ++;
    }
    public int GetModeE() {
        return ModelError;
    }

    //将测试进行的时间戳存入集合中
    public void AddTime(long l) { TimeList.add(l); }
    //将时间戳容器的值分配给各个测试变量
    public void AllocateTime() {
        //获取容器最末尾的下标
        int temp = TimeList.size() - 1;
        System.out.println("集合数量：" + TimeList.size());
        /*
        使用DecimalFormat来进行输的格式控制->格式为保留两位小数，同时强转为float
         */
        DecimalFormat df = new DecimalFormat("0.00");
        //第三段绘制的时间
        PaintTime3 = df.format((float)(TimeList.get(temp) - TimeList.get(temp -1)) / 1000);
        //第二段绘制的时间
        PaintTime2 = df.format((float)(TimeList.get(temp - 2) - TimeList.get(temp - 3)) / 1000);
        //第一段绘制的时间
        PaintTime1 = df.format((float)(TimeList.get(temp - 4) - TimeList.get(temp - 5)) / 1000);
        //模式切换的时间（两次切换的时间和）
        ModeSwitchTime = df.format((float)((TimeList.get(temp - 1) - TimeList.get(temp - 2))  + (TimeList.get(temp -3) - TimeList.get(temp - 4))) / 1000);
        //绘制的完整时间（三次绘制的时间和）
        CompleteTime = df.format((float)(TimeList.get(temp) - TimeList.get(temp - 5)) / 1000);
        //System.out.println(PaintTime3 +"--"+PaintTime2 +"--"+PaintTime1+"--"+ModeSwitchTime+"--"+CompleteTime);
    }
    //将测试进行的文字时间存入集合
    public void AddTimeString(String s) { TimeListString.add(s); }
    //将文字时间容器的内容分配
    public void AllocateTimeString() {
        int temp = TimeListString.size() - 1;
        StartTimeDate = TimeListString.get(0);
        EndTimeDate = TimeListString.get(temp);
    }
    //获得用户实际选择的颜色
    public void SetResultC(String s) { ResultColor = s; }
    //获得用户实际选择的像素
    public void SetResultP(String s) { ResultPixel = s; }
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
            saveText = "姓名" + "," + "实验组数" + "," + "一组实验次数" + "," + "模式切换技术" + "," + "目标颜色" + "," + "目标粗细" + "," +
                    "开始时间" + "," + "结束时间" + "," + "模式切换时间" + "," + "绘制完整时间" + "," + "第一段绘制时间" + "," + "第二段绘制时间" + "," + "第三段绘制时间"
                    + "," + "误触发数" + "," + "模式切换错误数" + "," + "压力" + "," + "方位角" + "," + "倾斜角" + "," + "\n";
            csv.write(saveText.getBytes("GBK"));
        }
        csv.skipBytes(CsvLine);
        //最后写了两个tilt，写一个好像记录不上，不知道为什么
        saveText = Name + "," + BlockNumber + "," + TrialNumber + "," + ModeTechnique + "," + TargetColor + "," + TargetLine + ","
                + StartTimeDate + "," + EndTimeDate + "," + ModeSwitchTime + "," + CompleteTime + "," +PaintTime1 + ","
                + PaintTime2 + "," + PaintTime3 + "," + TouchError + "," + ModelError + "," + pressure +"." + azimuth + "," + tilt + "," + tilt+ ","+ "\n";
        csv.write(saveText.getBytes("GBK"));
        csv.close();
    }

}