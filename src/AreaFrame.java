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
    private double x0,y0;//实验开始时笔尖的位置
    private double x1, y1;//每一次笔结束的位置

    private Line2D line = null;


    //获取笔的信息
    private PenData pData=new PenData();


    public AreaFrame(){

        ar1.setLayout(new BorderLayout());
        ar1.add(EndButton,BorderLayout.CENTER);
        ar1.addMouseListener(this);
        ar1.addMouseMotionListener(this);
        ar1.addKeyListener(this);

        //写字板
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
            //获得开始时鼠标的位置
            x0 = e.getX();
            y0 = e.getY();


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


    //在组件上按下鼠标按钮然后拖动时调用
    @Override
    public void mouseDragged(MouseEvent e) {
        //获得笔在拖动时的坐标
        x1 = e.getX();
        y1 = e.getY();


        line = new Line2D.Double(x0, y0, x1, y1);
        //将点的信息记录在容器中
        ar1.arrayList.add(line);
        ar1.repaint();
        System.out.println("sssssssssssssssssssssssssssssss");

        //更新位置信息
        x0 = x1;
        y0 = y1;


    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
