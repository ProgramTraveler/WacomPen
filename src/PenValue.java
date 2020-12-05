/*
    date:2020-12-05
    author:王久铭
    purpose:获取笔在使用过程中的信息
 */

import cello.tablet.JTablet;
import cello.tablet.JTabletCursor;

public class PenValue {
    private static JTablet tablet=null;
    //使用cello.tablet.JTabletCursor包来读出笔的各个属性值
    private static JTabletCursor cursor=null;
    //初始化笔的压力值
    private static int PenPressure=0;

    public static int pressure()
    {
        try
        {
            tablet.poll();
            PenPressure = tablet.getPressure();
        }catch(Exception err)
        {
            err.printStackTrace();
        }
        return PenPressure;
    }
}
