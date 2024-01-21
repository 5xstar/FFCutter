package com._5xstar.ffcutter.player;

import com._5xstar.ffcutter.FFCutter;
import com._5xstar.ffcutter.MainFrame;
import com._5xstar.ffcutter.Player;
import com._5xstar.ffcutter.domains.MediaPart;

public abstract class PlayerAbstractImpl implements Player{



    ///////////////  implements MediaPartFactoryStatus   begin ////////////////

    /**
     * 获取剪接器
     * @return
     */
    private FFCutter ffCutter;
    @Override
    public FFCutter getCutter() {
        return this.ffCutter;
    }
    @Override
    public void setCutter(FFCutter ffCutter){
        this.ffCutter=ffCutter;
    }

    /**
     * 获取播放器
     * @return
     */
    private Player player;
    @Override
    public Player getPlayer() {
        return this.player;
    }
    @Override
    public void setPlayer(Player player){
        this.player=player;
    }

    /**
     *true 初始化未完成
     * @return
     */
    private boolean noReady=true;  //初始化未完成
    @Override
    public boolean getNoReady() {
        return this.noReady;
    }
    @Override
    public void setNoReady(boolean noReady) {
        this.noReady=noReady;
    }

    /**
     * 阅览
     */
    private volatile boolean prePlayOn=false;
    @Override
    public boolean getPrePlayOn() {
        return this.prePlayOn;
    }
    @Override
    public void setPrePlayOn(boolean prePlayOn) {
        this.prePlayOn=prePlayOn;
    }

    /**
     * cutter关闭事件
     */
    private boolean isCutterClosed=false;
    @Override
    public void setIsCutterClosed(boolean isCutterClosed) {
        this.isCutterClosed=isCutterClosed;
    }

    /**
     * 当前媒体片段
     */
    private MediaPart mediaPart=null;
    @Override
    public void setMediaPart(MediaPart mediaPart) {
        this.mediaPart=mediaPart;
    }
    @Override
    public MediaPart getMediaPart(){
        return this.mediaPart;
    }

    ///////////////  implements MediaPartFactoryStatus   end ////////////////
    ///////////////  implements PlayerStatus   begin ////////////////
    private String mrl;    //媒体链接
    @Override
    public void setMrl(String mrl) {
         this.mrl=mrl;
    }
    @Override
    public String getMrl(){
        return this.mrl;
    }
    ///////////////  implements PlayerStatus   end ////////////////

    ///////////////  implements Client   begin ////////////////
    private MainFrame client; //客户端
    @Override
    public MainFrame getClient(){
        return this.client;
    }
    @Override
    public void setClient(MainFrame client){
        this.client=client;
    }
    ///////////////  implements Client   end ////////////////
    ///////////////  implements AutoClosable   begin ////////////////
    //退出播放
    @Override
    public void close(){
        clearup();
    }
    ///////////////  implements AutoClosable  end ////////////////

}
