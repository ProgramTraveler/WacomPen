import java.security.PublicKey;
import java.util.Random;

/*
date:2020-12-20
author:王久铭
purpose:用来记录绘画过程中点的信息
 */
public class Dot {
    private double StartX,StartY; //点开始的位置
    private double EndX,EndY; //点结束的位置
    private int Color = 0; //点的颜色
    private int Pixel = 2; //点的像素
    private int DotRandomC = 0; //点颜色的随机数，当点进入到测试区域时，会以点的随机数来作为提示颜色
    private int DotRandomP = 0; //点像素的随机数，当点进入到测试区域时，会以点的随机数来作为提示像素

    public Dot(){}
    //设置点开始的位置
    public void SetStarDot(double x0,double y0) {
        StartX = x0;
        StartY = y0;
    }
    //返回点的开始的X值
    public double DotStarX() {
        return StartX;
    }
    //返回点开始的Y值
    public double DotStarY() {
        return StartY;
    }
    //设置点结束的位置
    public void SetEndDot(double x1,double y1) {
        EndX = x1;
        EndY = y1;
    }
    //返回点结束的X值
    public double DotEndX() {
        return EndX;
    }
    //返回点结束的Y值
    public double DotEndY() {
        return EndY;
    }
    //设置点的颜色
    public void SetColor(int c) {
        Color = c;
    }
    //返回点的颜色信息
    public int DotColor() {
        return Color;
    }
    //设置点的像素
    public void SetPixel(int p) {
        Pixel = p;
    }
    //返回点的像素
    public int DotPixel() {
        return Pixel;
    }
    //设置点颜色的随机数
    public void SetDotRandomC() {
        Random temp = new Random();
        DotRandomC = temp.nextInt(4);
    }
    //获得颜色随机数
    public int GetDotRandomC() {
        return DotRandomC;
    }
    //设置点像素随机数
    public void SetDotRandomP() {
        Random temp = new Random();
        DotRandomP = temp.nextInt(4) + 2;
    }
    //获得像素随机数
    public int GetDotRandomP() {
        return DotRandomP;
    }



}
