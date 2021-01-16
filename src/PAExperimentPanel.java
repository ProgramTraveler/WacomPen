import javax.swing.*;
import java.awt.*;

/*
    date:2021-01-16
    author:王久铭
    purpose:该类的作用是将笔压力的动态信息进行图像的动态展示
 */
public class PAExperimentPanel extends JPanel {
    private boolean OpenMenu = false; //表示是否打开选择菜单
    private int CurrentPress = -1; //记录当前的压力值

    private int  permeationRate = 180;
    private Color ClearWhite = new Color( Color.white.getRed(), Color.white.getGreen(), Color.white.getBlue(), permeationRate);
    private Color ClearGray = new Color( Color.gray.getRed(), Color.gray.getGreen(), Color.gray.getBlue(), permeationRate);
    private Color ClearLightGray = new Color( Color.lightGray.getRed(), Color.lightGray.getGreen(), Color.lightGray.getBlue(), permeationRate);
    private Color ClearBlack = new Color( Color.black.getRed(), Color.black.getGreen(), Color.black.getBlue(), permeationRate);
    private Color ClearGreen = new Color( Color.green.getRed(), Color.green.getGreen(), Color.green.getBlue(), permeationRate);

    private Point FeedbackShowPoint = new Point(); //作用？
    private int PressureFeedbackWidth = 50;
    private int PressureFeedbackHeight = 80;
    private int PressureCursorRadius = 3;
    private int MaxPressure =1024;
    private int TriggerPressureSwitch = 700;

    private int NumberOfMenu = 6;
    private int MenuX = 0;
    private int MenuY = 0;
    private int MenuWidth =100;
    private int MenuHeight = 20;
    private int SelectMenuItem = -1;
    private int MenuTargetItem = 4;
    private Color MenuItemColor = Color.WHITE;
    private Color MenuLineColor = Color.GRAY;
    private Color SelectMenuItemColor = Color.LIGHT_GRAY;
    private Color SelectMenuTargetItem = Color.GREEN;
    private Color MenuTargetItemColor = Color.RED;


    public void SetOpenMenu(boolean b) { this.OpenMenu = b; }
    public void SetCurrentPress(int c) { this.CurrentPress = c; }
    //图像的重绘界面
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D graphics2D = (Graphics2D) g;
        //这一步是干嘛的？
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        this.PaintPressFeedback(graphics2D);

        //如果要打开颜色和像素的选择菜单
        if (OpenMenu)
            this.PaintOpenMenu(graphics2D);
    }
    public void PaintPressFeedback(Graphics2D graphics2D) {
        //显示压力值波动的方框
        graphics2D.setColor(ClearWhite);
        graphics2D.fillRect( (int)FeedbackShowPoint.getX() - PressureFeedbackWidth / 2, (int)FeedbackShowPoint.getY() - PressureFeedbackHeight,
                PressureFeedbackWidth, PressureFeedbackHeight);
        graphics2D.setColor(ClearLightGray);
        graphics2D.drawRect( (int)FeedbackShowPoint.getX() - PressureFeedbackWidth / 2, (int)FeedbackShowPoint.getY() - PressureFeedbackHeight,
                PressureFeedbackWidth, PressureFeedbackHeight);
        //显示压力值将要到达的那个绿色区域
        double TargetHeight = PressureFeedbackHeight * ( ( (double)MaxPressure - (double)TriggerPressureSwitch) / (double)MaxPressure);
        graphics2D.setColor(ClearGreen);
        graphics2D.fillRect( (int)FeedbackShowPoint.getX() - PressureFeedbackWidth / 2, (int)FeedbackShowPoint.getY() - PressureFeedbackHeight,
                PressureFeedbackWidth, (int)TargetHeight );

        graphics2D.setColor( ClearLightGray);
        graphics2D.drawRect( (int)FeedbackShowPoint.getX() - PressureFeedbackWidth / 2, (int)FeedbackShowPoint.getY() - PressureFeedbackHeight,
                PressureFeedbackWidth, (int)TargetHeight );
        //黑点上升的那条直线
        graphics2D.drawLine( (int)FeedbackShowPoint.getX(), (int)FeedbackShowPoint.getY() - PressureFeedbackHeight,
                (int)FeedbackShowPoint.getX(), (int)FeedbackShowPoint.getY() );
        //动态变化的黑点
        if ( CurrentPress >= 0 ) {
            graphics2D.setColor( ClearBlack);
            double RatioY = FeedbackShowPoint.getY() - PressureFeedbackHeight * ( (double)CurrentPress / (double)MaxPressure);
            graphics2D.fillArc( (int)FeedbackShowPoint.getX() - PressureCursorRadius, (int)RatioY - PressureCursorRadius,
                    PressureCursorRadius * 2, PressureCursorRadius * 2, 0, 360 );
        }
    }
    public void PaintOpenMenu(Graphics2D graphics2D) {
        for (int i =0; i < NumberOfMenu; i ++) {
            graphics2D.setColor(MenuItemColor);
            graphics2D.fillRect(MenuX - MenuWidth,MenuY + (MenuHeight * i),MenuWidth,MenuHeight);

            graphics2D.setColor(MenuLineColor);
            graphics2D.drawRect(MenuX - MenuWidth,MenuY + (MenuHeight * i),MenuWidth,MenuHeight);

        }
        if (SelectMenuItem >= 0) {
            graphics2D.setColor(SelectMenuItemColor);
            graphics2D.fillRect(MenuX - MenuWidth,MenuY + (MenuHeight * SelectMenuItem),MenuWidth,MenuHeight);
            graphics2D.setColor(MenuLineColor);
            graphics2D.drawRect(MenuX - MenuWidth,MenuY + (MenuHeight * SelectMenuItem),MenuWidth,MenuHeight);
        }

        if (SelectMenuItem == MenuTargetItem)
            graphics2D.setColor(SelectMenuTargetItem);
        else
            graphics2D.setColor(MenuTargetItemColor);
        graphics2D.fillRect(MenuX - MenuWidth,MenuY + (MenuHeight * MenuTargetItem),MenuWidth,MenuHeight);

        graphics2D.setColor(MenuLineColor);
        graphics2D.drawRect(MenuX - MenuWidth,MenuY + (MenuHeight * MenuTargetItem),MenuWidth,MenuHeight);
    }

}
