package com._5xstar.ffcutter.cutter.swing;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MyJMenuItem extends JMenuItem implements Cloneable{
    private String cap;
    final private ArrayList<JMenuItem> items= new ArrayList<JMenuItem>(2);
    private ActionListener listener=null;
    private boolean initOK=false;
    public MyJMenuItem(final  String cap){
        super(cap);
        this.cap=cap;
        initOK=true;
    }
    public MyJMenuItem( ){
        super();
        initOK=true;
    }
    @Override
    public void addActionListener(ActionListener listener){
        super.addActionListener(listener);
        if(items!=null)for(JMenuItem item : items)item.addActionListener(listener);
        this.listener=listener;
    }
    @Override
    public JMenuItem clone(){
        JMenuItem item = new JMenuItem(this.cap);
        items.add(item);
        if(this.listener!=null)item.addActionListener(listener);  //加事件后克隆有效
        return item;
    }
    @Override
    public void setText(String text){  //初始化超类时调用该方法，items==null
        super.setText(text);
        if(!initOK)return;
        cap=text;
        if(items!=null)for(JMenuItem item : items)item.setText(text);
    }
    @Override
    public void setEnabled(boolean enabled){
        super.setEnabled(enabled);
        if(items!=null)for(JMenuItem item : items)item.setEnabled(enabled);
    }
}
