import cello.tablet.JTablet;
import cello.tablet.JTabletCursor;
import cello.tablet.JTabletException;
/*
    date:2020-12-05
    author:王久铭
    purpose:获取笔在使用过程中的信息，暂定为笔的压力，笔的角度，笔的旋转角
 */

public class PenValue {
    private static JTablet tablet=null;
    //使用cello.tablet.JTabletCursor包来读出笔的各个属性值
    private static JTabletCursor cursor = null;
    //初始化笔的压力值
    private static int PenPressure = 0;
    //初始化笔的角度，倾斜角
    private static int PenTilt = 0;
    //初始化笔的旋转角
    private static int PenAzimuth = 0;

    //初始化tablet
    static
    {
        try {
            tablet = new JTablet();
            //获取写字板最新的信息
            tablet.poll();
        }catch(Exception err) {
            err.printStackTrace();
        }
    }

    /*
    获得笔的信息的时候处理异常的时候不推荐使用throw，因为要获取笔的实时信息
     */

    //获取笔的当前压力
    public static int Pressure() {
        try {
            tablet.poll();
            PenPressure = tablet.getPressure();
        } catch (JTabletException e) {
            e.printStackTrace();
        }

        return PenPressure;
    }

    //获取笔的倾斜角度
    public static int Tilt() {
        try {
            tablet.poll();
        } catch (JTabletException e) {
            e.printStackTrace();
        }
        if(tablet.getCursor() instanceof JTabletCursor)
            PenTilt = tablet.getCursor().getData(JTabletCursor.DATA_ORIENTATION_ALTITUDE);
        return PenTilt / 10;
    }

    //获取笔的旋转角
    public static int Azimuth() {
        try {
            tablet.poll();
        } catch (JTabletException e) {
            e.printStackTrace();
        }
        if (tablet.getCursor() instanceof  JTabletCursor)
            PenAzimuth = tablet.getCursor().getData(JTabletCursor.DATA_ORIENTATION_AZIMUTH);
        return PenAzimuth;
    }
    /*
    在笔的数据这一部分可能还需要写一点用户的测试条件，比如当前笔的颜色和需要切换的颜色，以及当前笔的粗细和需要切换粗细
     */

}
