/*
    date:2020-12-05
    author:王久铭
    purpose:显示写字界面
 */

import cello.tablet.JTablet;
import cello.tablet.JTabletCursor;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import cello.tablet.*;
import com.sun.corba.se.impl.orbutil.graph.Graph;

import java.io.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.MouseInputListener;

public class AreaFrame implements ActionListener, MouseInputListener, KeyListener {
    //画板所需变量
    private Area ar1=new Area();
    private JFrame frame=new JFrame("写字板");
    private JButton EndButton = new JButton("End");
    private JTablet tablet = null;

    //获取笔的信息所需的变量
    private int number;//保存目前读到第几行数据
    private String s;//实验的时间和日期
    private double x0,y0;//实验开始时笔尖的位置
    private boolean StartFlag=true;
    private int TimerDelay = 50;//每隔50毫秒检测一次压力
    private long runtime = -1; // 当前的时间
    private Timer time = new Timer(TimerDelay, this);
    private double x1, y1;// 起始位置

    private Line2D line = null;
    private int rot = 0;

    //获取笔的信息
    private PenData pData=new PenData();

    private long RunTime=-1;

    public AreaFrame(){

        ar1.setLayout(new BorderLayout());
        ar1.add(EndButton,BorderLayout.CENTER);
        ar1.addMouseListener(this);
        ar1.addMouseMotionListener(this);
        ar1.addKeyListener(this);


        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(ar1, BorderLayout.CENTER);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width;
        frame.setBounds(width / 5, 0, 750, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        EndButton.setVisible(false);

        try {
            tablet = new JTablet();
        } catch (JTabletException e1) {
            e1.printStackTrace();
        }

    }



    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    //在组件上按下鼠标按钮时调用
    @Override
    public void mousePressed(MouseEvent e) {
        if (javax.swing.SwingUtilities.isLeftMouseButton(e)) {
            Calendar cal = Calendar.getInstance();
            Date date = cal.getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

            s = df.format(new Date());// 实验的日期和时间
            x0 = e.getX();
            y0 = e.getY();
            if (StartFlag) {
                number=ar1.number;
                StartFlag = false;
            }
            time.start();
        }
    }
    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    /*private boolean errorfleg1=false;
    private int totalnumber=0;
    private boolean can1=false;*/
    private Graphics g;
    //在组件上按下鼠标按钮然后拖动时调用
    @Override
    public void mouseDragged(MouseEvent e) {
        x1 = e.getX();
        y1 = e.getY();
        Calendar cal=Calendar.getInstance();
        Date date=cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        s = df.format(new Date());// 实验的日期和时间
        int rot0=rot;
        ar1.SetEndX((int) x1);
        ar1.SetEndY((int) y1);
        ar1.SetFlag(true);

        line = new Line2D.Double(x0, y0, x1, y1);

        ar1.arrayList.add(line);
        //Graphics g;
        //ar1.paint(g);
        ar1.repaint();
        System.out.println("sssssssssssssssssssssssssssssss");
        /*runtime = System.currentTimeMillis();
        pData.setstarttime(runtime);
        errorfleg1 = true;
        totalnumber = totalnumber + 1;
        can1 = true;*/
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
