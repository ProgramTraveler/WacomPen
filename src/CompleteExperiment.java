import java.util.ArrayList;
import java.util.Random;

/*
    date:2021-01-11
    author:王久铭
    purpose:用来提供一次完整实验的实验所需要的信息和变量
 */
public class CompleteExperiment {
    //用来保存在一组实验中应该提示的颜色
    private ArrayList<String> RandomC = new ArrayList<String>();
    //用来保存在一组实验中应该提示的像素
    private ArrayList<String> RandomP = new ArrayList<String>();
    private int ExperimentBlock = 0; //整个实验的总组数

    public CompleteExperiment() {}
    //保存实验的总组数
    public void SetExperimentB(String s) { ExperimentBlock = Integer.getInteger(s); }
    //返回剩下的实验组数
    public int GetExperimentB() { return ExperimentBlock; }
    //判断集合是否为空，如果为空，说明一组实验已经做完，可以进行下一组实验，因为颜色和像素一一对应的，所以只要判断一个就好了
    public boolean GetList() { return RandomC.isEmpty(); }
    //设置点颜色的切换顺序
    public void SetRandomC() {
        //2.0像素对应的颜色
        RandomC.add("请切换颜色为红色");
        RandomC.add("请切换颜色为蓝色");
        RandomC.add("请切换颜色为黄色");
        //3.0像素对应的颜色
        /*RandomC.add("请切换颜色为红色");
        RandomC.add("请切换颜色为蓝色");
        RandomC.add("请切换颜色为黄色");
        //4.0像素对应的颜色
        RandomC.add("请切换颜色为红色");
        RandomC.add("请切换颜色为蓝色");
        RandomC.add("请切换颜色为黄色");*/
    }
    //获得颜色提示语句
    public String GetRandomC() {
        String temp = RandomC.get(0);
        RandomC.remove(0);
        return temp;
    }
    //设置点像素随机数
    public void SetRandomP() {
        //红，蓝，黄对应的像素
        RandomP.add("请切换像素为2.0");
        RandomP.add("请切换像素为2.0");
        RandomP.add("请切换像素为2.0");
        //红，蓝，黄对应的像素
        /*RandomP.add("请切换像素为3.0");
        RandomP.add("请切换像素为3.0");
        RandomP.add("请切换像素为3.0");
        //红，蓝，黄对应的像素
        RandomP.add("请切换像素为4.0");
        RandomP.add("请切换像素为4.0");
        RandomP.add("请切换像素为4.0");*/
    }
    //获得像素随机数
    public String GetRandomP() {
        String temp = RandomP.get(0);
        RandomP.remove(0);
        return temp;
    }
}
