package com._5xstar.ffcutter;

import com._5xstar.ffcutter.domains.MediaPart;

import java.io.File;
import java.util.List;

/**
 * 用播放播放器生成MediaPart
 * 庞海文 2024-1-17
 */
public interface MediaPartFactory extends MediaPartFactoryStatus, PlayerStatus, Client{



    /**
     * 连接片段，part中有文件和文件编号，开始、结束时间（百分秒长整数）
     */
    default void linkPart(final MediaPart mp){
        getClient().display("linkPart action:" +mp.file.getAbsolutePath());
         //System.out.println("linkPart action:" +mp.file.getAbsolutePath());
            setMediaPart(mp);
            //System.out.println("setMediaPart(mp)");
            //this.mp = mp;
            getPlayer().open(mp.file);
        //System.out.println("getPlayer().open(mp.file)");
            //open(mp.file);
            for(int i=0;i<10;i++){
                if(!getNoReady())break;
                try{Thread.sleep(250);}catch(Exception e){}
            }

    }

    /**
     * 阅览，MediaPart是复制品，begin和end的单位是毫秒
     */
    default void prePlayParts(final List<MediaPart> mps){

            setPrePlayOn(true);
            //prePlayOn = true;
            for (MediaPart mp : mps) {  //生成运行链
                getPlayer().open(mp.file);
                //open(mp.file);
                while(getPrePlayOn() && getNoReady())try{Thread.sleep(250);}catch (Exception e){}
                //while(prePlayOn && noReady)try{Thread.sleep(250);}catch (Exception e){}
                if(!getPrePlayOn())break;
                //if(!prePlayOn)break;
                getPlayer().prePlayAction(mp.begin,mp.end);
                //prePlayAction(mp.begin,mp.end);
                if(!getPrePlayOn())break;
                //if(!prePlayOn)break;
                setNoReady(true);
                //noReady=true;
            }
            getPlayer().clearup();  //释放内存

    }

    /**
     * 停止预览
     */
    default void prePlayStop(){
        setPrePlayOn(false);
        //prePlayOn=false;
    }

    /**
     * cutter关闭事件，player无需任何反向操作
     */
    default void cutterClosed(){
        setIsCutterClosed(true);
        //isCutterClosed=true;
        getPlayer().clearup();
    }


    /**
     * 添加剪切开始
     */
    default void addCutBegin(final long time){  //毫秒转百分秒
        final FFCutter cutter = getCutter();
        getClient().display("begin cutter="+cutter+ "time="+time, Display.DEBUG_TEXT);
        //System.out.println("begin cutter="+cutter+ "time="+time);
        if(cutter==null)return;
        MediaPart mp = getMediaPart();
        final String mrl = getMrl();
        if(mp==null  || !mp.file.getAbsolutePath().equalsIgnoreCase(mrl)){   //该处只考虑window
            mp = new MediaPart();
            mp.file = new File(mrl);
        }
        mp.begin=time/10;
        cutter.updatePart(mp);
    }
    /**
     * 添加剪切开始
     */
    default void addCutEnd(final long time){  //毫秒转百分秒
        final FFCutter cutter = getCutter();
        getClient().display("end cutter="+cutter+ "time="+time, Display.DEBUG_TEXT);
        //System.out.println("end cutter="+cutter+ "time="+time);
        if(cutter==null)return;
        MediaPart mp = getMediaPart();
        final String mrl = getMrl();
        if(mp==null || !mp.file.getAbsolutePath().equalsIgnoreCase(mrl)){   //该处只考虑window
            mp = new MediaPart();
            mp.file = new File(mrl);
        }
        mp.end=time/10;
        cutter.updatePart(mp);
    }

    /**
     * 获取描述
     * @return
     */
    String toString();
}
