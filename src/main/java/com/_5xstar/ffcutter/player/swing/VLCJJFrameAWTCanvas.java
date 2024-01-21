package com._5xstar.ffcutter.player.swing;

import com._5xstar.ffcutter.cutter.swing.JMainFrame;
import com._5xstar.ffcutter.FFCutter;
import com._5xstar.ffcutter.player.PlayerUIProps;

import java.awt.*;


/**  
*播放器窗体，canvas特殊操作
 * 庞海文 2024-1-24
**/
public class VLCJJFrameAWTCanvas extends VLCJJFrame<Canvas> {

    public VLCJJFrameAWTCanvas(final JMainFrame client, final FFCutter cutter){
        super(client, new Canvas(), cutter);
        if(client!=null)client.display(PlayerUIProps.awtCanvasDemo );
    }

    //在画布上画上底色图  
    @Override
    protected void paintCanvasDefault(){
      if(musicImg!=null)canvas.getGraphics().drawImage(musicImg, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
   }
      
}
