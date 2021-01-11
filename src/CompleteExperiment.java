import java.util.ArrayList;

/*
    date:2021-01-11
    author:王久铭
    purpose:用来提供一次完整实验的实验所需要的信息和变量
 */
public class CompleteExperiment {
    private ArrayList<String> DotRandomC = new ArrayList<String>();
    private ArrayList<String> DotRandomP = new ArrayList<String>();
    public CompleteExperiment() {}
    //设置点颜色的切换顺序
    public void SetRandomC() {
        //2.0像素对应的颜色
        DotRandomC.add("请切换颜色为红色");
        DotRandomC.add("请切换颜色为蓝色");
        DotRandomC.add("请切换颜色为黄色");
        //3.0像素对应的颜色
        DotRandomC.add("请切换颜色为红色");
        DotRandomC.add("请切换颜色为蓝色");
        DotRandomC.add("请切换颜色为黄色");
        //4.0像素对应的颜色
        DotRandomC.add("请切换颜色为红色");
        DotRandomC.add("请切换颜色为蓝色");
        DotRandomC.add("请切换颜色为黄色");
    }
    //获得颜色提示语句
    public String GetRandomC() {
        String temp = DotRandomC.get(0);
        DotRandomC.remove(0);
        return temp;
    }
    //设置点像素随机数
    public void SetRandomP() {
        //红，蓝，黄对应的像素
        DotRandomP.add("请切换像素为2.0");
        DotRandomP.add("请切换像素为2.0");
        DotRandomP.add("请切换像素为2.0");
        //红，蓝，黄对应的像素
        DotRandomP.add("请切换像素为3.0");
        DotRandomP.add("请切换像素为3.0");
        DotRandomP.add("请切换像素为3.0");
        //红，蓝，黄对应的像素
        DotRandomP.add("请切换像素为4.0");
        DotRandomP.add("请切换像素为4.0");
        DotRandomP.add("请切换像素为4.0");
    }
    //获得像素随机数
    public String GetRandomP() {
        String temp = DotRandomP.get(0);
        DotRandomP.remove(0);
        return temp;
    }
}
