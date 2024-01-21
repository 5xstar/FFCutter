package com._5xstar.ffcutter;


/**
 *这个接口用于显示消息 
 *庞海文 2020-3-12
**/
public interface Display<C>
{ 

  //模式
  int CHAT_TEXT=0;     //其他人的消息
  int DEBUG_TEXT=1;   //系统消息


   /**
    *显示消息
    *@msg 要显示的消息
   **/
   default void display(String msg){
       display(msg, CHAT_TEXT);
   }
   void display(String msg, final int type);


  /**
   *获取显示消息的容器  与界面有关，不在原型使用范围
   *@return 消息容器
  **/
   C getContainer();
   
    /**
      * 改变底色
    **/
    void changeBackground(final boolean dayType);

}
