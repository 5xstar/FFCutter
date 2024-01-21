package com._5xstar.ffcutter.domains;

/**
 *按钮执行器 
 *庞海文 2021-12-1
**/
public class ButtonAction implements  Keeper
{ 
    //按钮标签
    public String caption;
    
    //按钮说明，鼠标放在按钮上显示
    public String demo;
    
    //执行功能
    public Runnable run;
    
    public ButtonAction(final String caption, final String demo, final Runnable run){
        this.caption=caption;
        this.demo=demo;
        this.run=run;
    }  
    public ButtonAction(final String caption, final Runnable run){
        this(caption,null,run);
    }   
    public ButtonAction(){
        this(null,null,null);
    }

}
