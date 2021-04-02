import com.sun.corba.se.impl.resolver.FileResolverImpl;

import java.io.*;
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
    private static int pressureColor; //颜色提示菜单出现，当前笔的压力
    private static int pressurePixel; //像素提示菜单出现，当前笔的压力
    private static int pressure; //记录的是压力的平均值

    private static int tiltColor; //当前笔的角度
    private static int tiltPixel;
    private static int tilt;

    private static int azimuthColor; //当前的方位角
    private static int azimuthPixel;
    private static int azimuth;

    private static int count = 0; //记录绘制的点数，方便为上述变量求平均值

    private static String Name; //用户的姓名
    private static int BlockNumber; //实验组数
    private static int TrialNumber = 0; //一组实验里实验的次数
    private static String ModeTechnique; //选择的模式切换技术

    private static String TargetColor; //每次实验的出现的目标颜色
    private static String TargetLine; //每次实验出现的目标线条粗细

    private static long StartTime; //当前绘制开始的时间(第一次绘制落笔的时间)
    private static String StartTimeDate; //绘制开始的时间以文字格式保存
    private static long EndTime; //当前绘制结束的时间（第三次绘制抬笔的时间）
    private static String EndTimeDate; //绘制结束的时间以文字格式保存

    private static long ModeSwitchTime; //模式切换时间（两次切换的时间之和）
    private static long ColorModeSwitchT; //颜色切换时间
    private static long PixelModeSwitchT; //像素切换时间

    private static long CompleteTime; //绘制完整时间（整体三次绘制的时间之和）
    private static long PaintTime1; //第一段画线绘制的时间
    private static long PaintTime2; //第二段画线绘制的时间
    private static long PaintTime3; //第三段画线绘制的时间

    private static int TouchError = 0; //误触发次数（在未弹出切换指令前，错误的切换出命令菜单）
    private static int ColorTouchE = 0; //颜色切换的误触发数
    private static int PixelTouchE = 0; //像素切换的误触发数

    private static int ModelError = 0; //切换模式错误
    private static int ColorModeE = 0; //颜色模式切换错误数
    private static int PixelModeE = 0; //像素切换错误数

    private static String ResultColor; //用户实际切换的颜色
    private static String ResultPixel; //用户实际切换的像素

    //记录在测试过程中的每个时间戳
    private ArrayList<Long> TimeList;
    //记录在测试过程中的每个时间
    private ArrayList<String> TimeListString;

    private long StartColorMode; //记录颜色模式切换的开始时间
    private long EndColorMode; //记录颜色模式切换的结束时间
    private long StartPixelMode; //记录像素模式切换的开始时间
    private long EndPixelMode; //记录像素模式切换的结束时间

    private RandomAccessFile csv; // 存实验数据的文件

    public PenData() {
        //对所有数据进行初始化
        pressureColor = 0;
        pressurePixel = 0;
        pressure = 0;

        tiltColor = 0;
        tiltPixel = 0;
        tilt = 0;

        azimuthColor = 0;
        azimuthPixel = 0;
        azimuth = 0;

        Name = "";
        BlockNumber = 0;
        TrialNumber = 0;
        ModeTechnique = "";

        TargetColor = "";
        TargetLine = "";

        StartTimeDate = "";
        EndTimeDate = "";

        ModeSwitchTime = 0;
        ColorModeSwitchT = 0;
        PixelModeSwitchT = 0;

        CompleteTime = 0;
        PaintTime1 = 0;
        PaintTime2 = 2;
        PaintTime3 = 3;

        TouchError = 0;
        ColorTouchE = 0;
        PixelTouchE = 0;

        ModelError = 0;
        ColorModeE = 0;
        PixelModeE = 0;

        ResultColor = "";
        ResultPixel = "";

        TimeList = new ArrayList<Long>();
        TimeListString = new ArrayList<String>();

        StartColorMode = 0;
        EndColorMode = 0;
        StartPixelMode = 0;
        EndPixelMode = 0;
    }

    /*
    获取笔在使用过程中的数据
     */
    //获取笔尖的压力
    public void SetPressureC(int pre) { pressureColor = pre; }
    public void SetPressureP(int pre) { pressurePixel = pre; }
    public void SetPressure(int pre) { pressure += pre; }
    //public static int GetPressure() { return pressure; }
    //获取笔的倾斜角
    public void SetTiltC(int til) { tiltColor = til; }
    public void SetTiltP(int til) { tiltPixel = til;}
    public void SetTilt(int til) {tilt += til; }
    //public int GetTilt() { return tilt; }
    //获取笔的旋转角
    public void SetAzimuthC(int azi) { azimuthColor = azi; }
    public void SetAzimuthP(int azi) { azimuthPixel = azi; }
    public void SetAzimuth(int azi) { azimuth += azi; }
    //public int GetAzimuth() { return azimuth; }

    //记录点数
    public void AddCount() { count ++;}
    /*
    获取实验数据
     */
    //获取实验者的名字
    public void SetName(String s) { Name = s; }
    public String GetName() { return Name; }
    //获取实验组数
    public void SetBlock(int b) {
        BlockNumber = b;
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
    //错误触发总的次数
    public void SetTouchE(int i) { TouchError = i; }
    public void AddTouchE() {
        TouchError ++;
    }
    public int GetTouchE() {
        return TouchError;
    }
    //颜色错误触发的次数
    public void SetColorTouchE(int i) { ColorTouchE = i; }
    public void AddColorTouchE() { ColorTouchE ++; }
    //像素错误触发的次数
    public void SetPixelTouchE(int i) { PixelTouchE = i; }
    public void AddPixelTouchE() { PixelTouchE ++; }
    //切换模式错误的总次数
    public void SetModeE(int i) {
        ModelError = i;
    }
    //由于模式切换错误数是在实验结束之后判断的，所以在这里一起处理了
    public void AddModeE() {
        if (TargetColor.equals(ResultColor) == false) {
            ModelError ++;
            ColorModeE ++;
        }
        if (TargetLine.equals(ResultPixel) == false) {
            ModelError ++;
            PixelModeE ++;
        }

    }
    public int GetModeE() {
        return ModelError;
    }
    //颜色模式切换错误
    public void SetColorModeE(int i) { ColorModeE = i; }
    //像素模式切换错误
    public void SetPixelModeE(int i) { PixelModeE = i; }

    //将测试进行的线条绘制时间戳存入集合中
    public void AddTime(long l) { TimeList.add(l); }
    //将测试时颜色模式切换时间记录在集合中
    public void SetStartColorMode(long l) { StartColorMode = l; }
    public void SetEndColorMode(long l) { EndColorMode = l; }
    //将测试时像素模式切换时间记录在集合中
    public void SetStartPixelMode(long l) { StartPixelMode = l; }
    public void SetEndPixelMode(long l) { EndPixelMode = l; }
    //将时间戳容器的值分配给各个测试变量
    public void AllocateTime() {
        //获取容器最末尾的下标
        int temp = TimeList.size() - 1;
        /*
        使用DecimalFormat来进行输的格式控制->格式为保留两位小数，同时强转为float
         */
        DecimalFormat df = new DecimalFormat("0.00");
        //第三段绘制的时间
        //PaintTime3 = df.format((float)(TimeList.get(temp) - TimeList.get(temp -1)) / 1000);
        PaintTime3 = TimeList.get(temp) - TimeList.get(temp -1);
        //第二段绘制的时间
        PaintTime2 = TimeList.get(temp - 2) - TimeList.get(temp - 3);
        //第一段绘制的时间
        PaintTime1 = TimeList.get(temp - 4) - TimeList.get(temp - 5);
        //颜色模式切换时间
        ColorModeSwitchT = EndColorMode - StartColorMode;
        //像素模式切换时间
        PixelModeSwitchT = EndPixelMode - StartPixelMode;
        //模式切换的时间（两次切换的时间和）
        ModeSwitchTime = ColorModeSwitchT + PixelModeSwitchT;
        //绘制的完整时间（三次绘制的时间和）
        CompleteTime = TimeList.get(temp) - TimeList.get(temp - 5);
        //System.out.println(PaintTime3 +"--"+PaintTime2 +"--"+PaintTime1+"--"+ModeSwitchTime+"--"+CompleteTime);
    }
    //将测试进行的文字时间存入集合
    public void AddTimeString(String s) { TimeListString.add(s); }
    //将文字时间容器的内容分配
    public void AllocateTimeString() {
        StartTimeDate = TimeListString.get(0);
        EndTimeDate = TimeListString.get(1);
    }
    //获得用户实际选择的颜色
    public void SetResultC(String s) { ResultColor = s; }
    //获得用户实际选择的像素
    public void SetResultP(String s) { ResultPixel = s; }
    /*
    保留笔在测试过程中的数据
     */
    public void SaveInformation() throws IOException {
        //System.out.println(pressure + " " + count);
        //System.out.println(pressure);
        //存实验数据的文件名
        File saveFile = new File("information.csv");
        csv = new RandomAccessFile(saveFile, "rw");


        int CsvLine = (int) csv.length();
        String saveText = "";
        if (CsvLine == 0) {
            /*saveText = "Name" + "Block Number" + "Trial Number" + "Mode switching technique" + "Target color" + "Target line thickness" +
                    "Start Time" + "End Time" + "Mode Switching Time" + "Complete Time" + "Painting Time 1" + "Paint Time 2" + "Paint Time 3"
                    + "Number of false trigger" + "Switching Error Number" + "Pressure" + "Tilt" + "Azimuth" + "\n";*/
            saveText = "id" + "," +  "姓名" + "," + "实验组数" + "," + "实验组编号" + "," + "模式切换技术" + "," + "目标颜色" + "," + "目标粗细" + "," +
                    "开始时间" + "," + "结束时间" + "," + "颜色切换时间" +"," + "像素切换时间" + "," + "模式切换总时间" + "," + "绘制完整时间" + "," + "第一段绘制时间" + "," + "第二段绘制时间" + "," + "第三段绘制时间"
                    + "," + "误触发总数" + "," + "颜色切换错误数" +","+ "像素切换错误数" +","+"模式切换总错误数" + "," + "压力1" + "," + "压力2" + "," + "压力平均值" + ","+ "方位角1" + "," + "方位角2" + "," + "方位角平均值" + ","+ "倾斜角1" + "," + "倾斜角2" + "," + "倾斜角平均值" + "," + "\n";
            csv.write(saveText.getBytes("GBK"));
        }
        csv.skipBytes(CsvLine);

        int index = 0;
        //获得文件行数
        try {
            FileReader f = new FileReader("information.csv");
            BufferedReader b = new BufferedReader(f);
            String s;
            while ((s = b.readLine()) != null) {
                index++;
            }
            System.out.println(index);
            b.close();
            f.close();
        }catch (IOException o) {
            o.printStackTrace();
        }

        if (count == 0)
            count = 1;
        //最后写了两个tilt，写一个好像记录不上，不知道为什么（3月2号，发现是我的分隔符敲错了，现在没错了）
        saveText = index + "," + Name + "," + BlockNumber + "," + TrialNumber + "," + ModeTechnique + "," + TargetColor + "," + TargetLine + ","
                + StartTimeDate + "," + EndTimeDate + ","+ ColorModeSwitchT + "," + PixelModeSwitchT + "," + ModeSwitchTime + "," + CompleteTime + "," +PaintTime1 + ","
                + PaintTime2 + "," + PaintTime3 + ","  + TouchError + "," + ColorModeE + "," + PixelModeE +","+ModelError + "," + pressureColor  +"," + pressurePixel + "," + pressure / count+ "," + azimuthColor + ","  + azimuthPixel+ "," + azimuth / count + "," + tiltColor+ ","+ tiltPixel + "," + tilt / count + "," + "\n";
        csv.write(saveText.getBytes("GBK"));
        csv.close();

    }

}