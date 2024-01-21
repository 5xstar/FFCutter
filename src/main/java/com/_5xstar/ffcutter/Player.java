package com._5xstar.ffcutter;

import java.io.File;

/**
*播放器实现接口
*庞海文 2024-1-17
**/
public interface Player extends PlayerUI, MediaPartFactory, AutoCloseable
{

    /**
     * 打开媒体文件
     * @param fileMrl
     */
    default void open(File fileMrl) {
        getClient().display("open action:"+ fileMrl.getAbsolutePath(), Display.DEBUG_TEXT);
        //System.out.println("open action:"+ fileMrl.getAbsolutePath());
        if(fileMrl.exists() && fileMrl.isFile()){
            //isOpenFile = true;
            //canJump=true;
            final String mrl = fileMrl.getAbsolutePath();
            setMrl(mrl);
            //this.mrl=fileMrl.getAbsolutePath();
            openStr(mrl);
            //openStr(this.mrl);
            return;
        }
        getClient().display(PlayerProps.unspportMedia + fileMrl.getAbsolutePath(), Display.DEBUG_TEXT);
        //System.out.println(PlayerProps.unspportMedia + fileMrl.getAbsolutePath());
    }
    default void openStr(final String strMrl){
        getClient().display("openStr action:"+ strMrl, Display.DEBUG_TEXT);
        //System.out.println("openStr action:"+ strMrl);
        setVisible(true);  //显示窗体
        try{
            getClient().display(PlayerProps.loadMedia+strMrl, Display.DEBUG_TEXT);
            //System.out.println(PlayerProps.loadMedia+strMrl);  //测试
            play(strMrl);  //窗口显示后主键才有peer
            getClient().display(PlayerProps.loadMediaSuccess +strMrl, Display.DEBUG_TEXT);
            //System.out.println(PlayerProps.loadMediaSuccess +strMrl);
        }catch(Exception e){
            e.printStackTrace();
            getClient().display(PlayerProps.loadMediaFail +strMrl, Display.DEBUG_TEXT);
            //System.out.println(PlayerProps.loadMediaFail +strMrl);
        }
    }
    
}
