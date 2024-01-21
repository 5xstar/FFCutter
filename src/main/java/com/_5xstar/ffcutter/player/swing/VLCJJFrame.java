package com._5xstar.ffcutter.player.swing;

import com._5xstar.ffcutter.FFCutter;
import com._5xstar.ffcutter.cutter.swing.JMainFrame;
import com._5xstar.ffcutter.cutter.swing.JMainFrameImplStatus;
import com._5xstar.ffcutter.player.PlayerUIProps;

import javax.swing.*;
import java.awt.*;


/**
*播放器窗体类，排版
 * 庞海文 2024-1-20
**/
public abstract class VLCJJFrame<C extends Component> extends VLCJPlayer<JFrame, C> {


    /**
     * Set the standard look and feel.
     * Metal
     * Nimbus
     * CDE/Motif
     * Windows
     * Windows Classic
     */
    private static String lookAndFeelName= JMainFrameImplStatus.Const.lookAndFeelName;
    protected static final void setLookAndFeel() {
        System.out.println("lookAndFeelName="+lookAndFeelName);
        if(lookAndFeelName==null)return;
        String lookAndFeelClassName = null;
        UIManager.LookAndFeelInfo[] lookAndFeelInfos = UIManager.getInstalledLookAndFeels();
        for(UIManager.LookAndFeelInfo lookAndFeel : lookAndFeelInfos) {
            if (lookAndFeelName.equals(lookAndFeel.getName())) {
                lookAndFeelClassName = lookAndFeel.getClassName();
                break;
            }
        }
        if (lookAndFeelClassName == null) {
            //lookAndFeelClassName = UIManager.getSystemLookAndFeelClassName();
            return;
        }
        try {
            System.out.println("init as "+ lookAndFeelName);
            UIManager.setLookAndFeel(lookAndFeelClassName);
        }
        catch(Exception e) {
            // Silently fail, it doesn't matter
        }
    }

    final private JPanel controlPanel; 
  
    protected VLCJJFrame(final JMainFrame client, final C canvas, final FFCutter cutter){
        super(client,  new JFrame(PlayerUIProps.title), canvas,  cutter);
        //设置关闭退出
        if(client==null)frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);       
        //图标
        if(icon!=null)frame.setIconImage(icon); 
        //背景
        frame.setBackground(background);
        frame.getContentPane().setBackground(background);
        //大小   
        final Dimension displaySize = Toolkit.getDefaultToolkit().getScreenSize(); // 获得显示器大小对象
        final int width=Const.defaultWidth>displaySize.width?displaySize.width:Const.defaultWidth;
        final int height=Const.defaultHeight>displaySize.height?displaySize.height:Const.defaultHeight;
        //画布大小
        canvas.setSize(width,height);  
        
        //控制部分            
        controlPanel  =  new JPanel(new GridBagLayout());
        
        //第一行
        JPanel  controlPanel1  =  new JPanel(new GridBagLayout());
        
        //第二行
        JPanel  controlPanel2  =  new JPanel(new FlowLayout());
                               
        //控制按钮
        if(cutter!=null) {
            //媒体开始
            controlPanel2.add(btnCutBegin);
            //媒体结束
            controlPanel2.add(btnCutEnd);
        }
        //文件  
        controlPanel2.add(btnOpenFile);      
        //添加暂停按钮  
        controlPanel2.add(btnPause);       
        //添加播放按钮  
        controlPanel2.add(btnPlay);       
        //添加停止按钮  
        controlPanel2.add(btnStop);       
        //添加全屏按钮  
        controlPanel2.add(btnFullScreen);                            
        //关闭按钮  
        controlPanel2.add(btnClose);
        //重载
        controlPanel2.add(btnReload);        
        //静音按钮  
        controlPanel2.add(btnMute);           
        //添加声音控制块 
        controlPanel2.add(new JLabel(PlayerUIProps.lblVolumeText));
        controlPanel2.add(lblVolume);
        controlPanel2.add(slider);
        controlPanel2.add(new JLabel(PlayerUIProps.lblTrackCountText ));
        controlPanel2.add(lblTrackCount);
        controlPanel2.add(btnEqualizer); 
        controlPanel2.add(btnVideoAdjust);
        
        //组装控制块
        GridBagConstraints gbc=new GridBagConstraints();
        gbc.fill=GridBagConstraints.HORIZONTAL;
        gbc.insets=new Insets(0,0,0,0);       
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.weightx=0.0;
        controlPanel1.add(lblState, gbc);
        gbc.gridx=1;
        controlPanel1.add(lblTime, gbc);
        gbc.weightx=1.0;
        gbc.gridx=2;
        controlPanel1.add(progress, gbc);      
        gbc.weightx=0.0;
        gbc.gridx=3;
        controlPanel1.add(lblMediaLength, gbc);
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.weightx=1.0;       
        controlPanel.add(controlPanel1, gbc);  //第一行
        gbc.gridy=1;
        gbc.weightx=0.0;
        controlPanel.add(controlPanel2,gbc);
        
        //整个窗体 
        //布局

        frame.setLayout(new BorderLayout());
        canvas.setBackground(canvasBackground);  
        frame.setBackground(canvasBackground);
        frame.add(canvas, BorderLayout.CENTER);
        frame.add(controlPanel, BorderLayout.SOUTH);
        frame.pack();

        setLookAndFeel();

        /**
        final GridBagLayout gbl=new GridBagLayout();
        frame.setLayout(gbl);             
        gbc=new GridBagConstraints();
        gbc.fill=GridBagConstraints.BOTH;
        gbc.weightx=1.0;   
        gbc.weighty=1.0; 
        gbc.anchor=GridBagConstraints.NORTHWEST;
        gbc.insets=new Insets(0,0,0,0);
        gbc.gridx=0;
        gbc.gridy=0;
        frame.add(canvas, gbc);   //添加canvas容器        
        canvas.setBackground(canvasBackground);  
        gbc.fill=GridBagConstraints.HORIZONTAL;
        gbc.weighty=0.0;  
        gbc.gridy=1;
        frame.add(controlPanel, gbc);
        frame.pack();
        **/   
               
    }  
    
    //隐藏控制栏  
    @Override
    protected  void hideControls(final boolean hide){
        controlPanel.setVisible(!hide);    
    }
    
    //改变标题
    @Override
    protected void updateTitleUI(){
        frame.setTitle(title);
    } 
        
      
}
