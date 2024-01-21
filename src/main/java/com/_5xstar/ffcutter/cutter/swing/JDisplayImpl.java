package com._5xstar.ffcutter.cutter.swing;

import com._5xstar.ffcutter.Configure;
import com._5xstar.ffcutter.Display;
import com._5xstar.ffcutter.StringHandler;
import com._5xstar.ffcutter.constant.PubConst;
import com._5xstar.ffcutter.util.StringUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class JDisplayImpl implements JDisplay<JScrollPane> , Configure {

    //显示最大大小当达到maxDemoLength倍的最大消息长度后去掉早的行直到低于minDemoLength倍
    private static int maxDemoLength=50;
    //显示最小大小
    private static int minDemoLength=30;

    //配置
    public void conf( final Properties prop){
        maxDemoLength= StringUtil.parse(prop, "maxDemoLength", maxDemoLength);
        minDemoLength=StringUtil.parse(prop, "minDemoLength", minDemoLength);
    }

    private int type=Display.CHAT_TEXT;

    final private JFFCTextDisplay txtDisplay;
    final private JScrollPane pane;
    //清空按钮与菜单
    final private JButton btnCleanDisplay=new JButton(JDisplayImplProps.btnCleanDisplayCaption);  //清空消息栏
    final private ArrayList<JButton> btnList=new ArrayList<JButton>(1);
    final private MyJMenuItem mniCleanDisplay=new MyJMenuItem(JDisplayImplProps.btnCleanDisplayCaption);
    final private ArrayList<MyJMenuItem> mniList=new ArrayList<MyJMenuItem>(1);
    public JDisplayImpl(final JMainFrame client,final boolean dayType){
        txtDisplay=new JFFCTextDisplay(client,dayType);  //JTextArea(txtDisplayRow,txtDisplayColumn);
        //把定义的JTextArea放到JScrollPane里面去
        JPanel inPane= new JPanel(new GridBagLayout());  //JScrollPane不能把JTextPane拉开，必须要隔着这个
        GridBagConstraints gbc=new GridBagConstraints();
        gbc.anchor=GridBagConstraints.NORTHWEST;
        gbc.fill=GridBagConstraints.BOTH;
        gbc.insets=new Insets(0,1,0,1);
        gbc.weightx=1.0;
        gbc.weighty=1.0;
        gbc.gridx=0;
        gbc.gridy=0;
        inPane.add(txtDisplay,gbc);
        pane= new JScrollPane(inPane);
        pane.getVerticalScrollBar().setUnitIncrement(30);
        btnList.add(btnCleanDisplay);
        mniList.add(mniCleanDisplay);
        final ActionListener listener=new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clean();
            }
        };
        btnCleanDisplay.addActionListener(listener);
        mniCleanDisplay.addActionListener(listener);
    }


    private int msgNum=0;
    /**
     *显示消息
     *@msg 要显示的消息
     *@demoType 显示模式
     **/
    public void display(String msg, final int demoType){
        if(demoType!=Display.CHAT_TEXT){
            System.out.println(msg);  //写入log
        }
        //检查
        if(++msgNum % (PubConst.maxMsgLength/PubConst.lineMsgLength)==0){
            String st=txtDisplay.getText();
            int len=0;
            if(st!=null && (len=st.length())>maxDemoLength * PubConst.maxMsgLength){
                txtDisplay.setText(StringUtil.subLine(st,minDemoLength * PubConst.maxMsgLength));
            }
        }
        txtDisplay.append(msg);
    }

    /**
     * 清空消息栏
     */
    private void clean(){
        this.txtDisplay.setText("");
    }

    /**
     *获取显示消息的容器
     *@return 消息容器
     **/
    @Override
    public JScrollPane getContainer(){
        return this.pane;
    }

    /**
     * 改变底色
     **/
    @Override
    public void changeBackground(final boolean dayType){
        this.txtDisplay.changeBackground(dayType);
    }

    @Override
    public JComponent getTxtContainer() {
        return this.txtDisplay;
    }

    @Override
    public List<JButton> getButtons() {
        return btnList;
    }

    @Override
    public List<MyJMenuItem> getMenuItems() {
        return mniList;
    }
}
