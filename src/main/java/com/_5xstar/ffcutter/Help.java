package com._5xstar.ffcutter;


/**
 *帮助
 *庞海文 2020-3-20
**/
public interface Help extends Runnable
{ 

   /**
     *标题
     *@return 标题
   **/
   String getTitle();

   /**
     *描述
     *@return 描述
   **/
   String getDesc();


}
