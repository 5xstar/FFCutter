package com._5xstar.ffcutter.player.swing;

import com._5xstar.ffcutter.FFCutter;
import com._5xstar.ffcutter.Player;
import com._5xstar.ffcutter.PlayerProps;
import com._5xstar.ffcutter.cutter.swing.JMainFrame;
import com._5xstar.ffcutter.player.PlayerUIProps;
import com._5xstar.ffcutter.player.PlayerUIStatus;
import com._5xstar.ffcutter.player.VLCJPlayerBase;
import com._5xstar.ffcutter.constant.PubConst;
import com._5xstar.ffcutter.util.VLCJUtil;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.*;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.fullscreen.adaptive.AdaptiveFullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.videosurface.VideoSurface;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.net.URL;


/**
*播放器窗体，处理事件
 * 庞海文 2024-1-20
**/
public abstract class VLCJPlayer<F extends Window, C extends Component> extends VLCJPlayerBase
implements PlayerUIStatus, MouseListener, MouseMotionListener, MouseWheelListener, KeyListener, HierarchyBoundsListener      //implements MediaPlayerEventListener, MediaEventListener,
{
   
    protected static BufferedImage icon= null;
    protected static BufferedImage musicImg = null;
    static{
        try{
           icon = createImage(Const.logoFile);
           musicImg = createImage(Const.musicImgFile);
        }catch(Exception e){
           e.printStackTrace();
        }        
    }
    private static BufferedImage createImage(String path)throws Exception{
        //运行根目录
        File f = new File(path);
        if(f.exists() && f.isFile()){
            try{
                return ImageIO.read(f);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        //classes 装载根目录
        URL url = VLCJPlayer.class.getClassLoader().getResource(path);  //classes根目录
        if(url!=null) {
            f = new File(url.getFile());
            if(f.exists() && f.isFile()){
                try{
                    return ImageIO.read(f);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        //class 目录
        url = VLCJPlayer.class.getResource(path);  //class所在目录
        if(url!=null) {
            f = new File(url.getFile());
            if(f.exists() && f.isFile()){
                try{
                    return ImageIO.read(f);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    //客户端
    final protected JMainFrame client;
    //播放器三件套
    final protected MediaPlayerFactory factory;
    final protected EmbeddedMediaPlayer player;
    final protected VideoSurface videoSurface;

    //播放窗口两要素
    final protected F frame;
    final protected C canvas;

    protected static Color background = Color.BLACK;
    protected static Color canvasBackground = Color.BLACK;
    
    final protected JLabel lblTime = new JLabel("00:00");
    final protected JLabel lblMediaLength = new JLabel("00:00");
    final protected JLabel lblState = new JLabel("");  //state);
    final protected JLabel lblVolume = new JLabel(PlayerUIProps.lblVolumeDefaultText);
    final protected JLabel lblTrackCount = new JLabel("0");
    //添加进度条  
    final protected JProgressBar progress = new JProgressBar(); 
    private boolean progressStatus = false;  //启动后不退出，媒体播完后退出
    //添加停止按钮  
    final protected JButton btnStop  = new JButton(); 
    //添加播放按钮  
    final protected JButton btnPlay= new JButton();     
    //添加暂停按钮  
    final protected JButton btnPause= new JButton();   
    //全屏按钮，可能无效  
    final protected JButton btnFullScreen = new JButton();   
    //打开文件按钮  
    final protected JButton btnOpenFile = new JButton(); 
    //关闭按钮  
    final protected JButton btnClose=new JButton();      
    //静音按钮  
    final protected JButton btnMute=new JButton(); 
    //添加声音控制块  
    final protected JSlider slider = new JSlider();  
    //重新加载  
    final protected JButton btnReload = new JButton();
    //屏幕尺寸
    final protected static Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
    //声音均衡器
    final protected JButton btnEqualizer = new JButton();
    final protected Equalizer equalizer;
    final protected EqualizerFrame equalizerFrame;
    //视频调节器
    final protected JButton btnVideoAdjust = new JButton();
    final protected PlayerVideoAdjustPanel videoAdjustPanel;
    //剪切开始按钮
    final protected JButton btnCutBegin= new JButton();
    //剪切结束按钮
    final protected JButton btnCutEnd= new JButton();

    private boolean canJump = false;             //是否可以跳转和暂停
    private boolean isOpenFile = true;  //是否从文件打开

    private String state=null;    //播放方状态
    private volatile long mediaLength=Const.defaultMediaLength;  //媒体时长
    private volatile long providerTime=0;     //发送方播放时间
    private boolean repeat=false;

    //reload问题解决，忽略网络传输时间，视频流大概有3-4秒的时差
    private int flowWaitTimeMillis = PubConst.defaultFlowWaitTimeMillis-Const.flowWaitDelayTimeMillis;

    //媒体名称
    private String name=null;

    //VLCJPlayerUI构造方法，创建视屏播放的主界面      
    protected VLCJPlayer(final JMainFrame client, final F frame, final C canvas,  final FFCutter cutter){
        super();  //保存客户端
        this.client=client;
        setClient(client);
        this.frame=frame;  //媒体播放窗口
        this.canvas=canvas;
        setCutter(cutter);
        //vlc内置播放器      
        List<String> vlcArgs = new ArrayList<String>();
        vlcArgs.add("--no-snapshot-preview");
        vlcArgs.add("--quiet");      
        this.factory = new MediaPlayerFactory(vlcArgs.toArray(new String[vlcArgs.size()]));
        this.factory.application().setUserAgent("vlcj 5xstar player");
        this.videoSurface = this.factory.videoSurfaces().newVideoSurface(this.canvas);
        this.player = this.factory.mediaPlayers().newEmbeddedMediaPlayer();
        this.player.videoSurface().set(this.videoSurface);  //player中加入videoSurface
        this.player.overlay().set(this.frame);
        this.player.fullScreen().strategy(new AdaptiveFullScreenStrategy(this.frame)); 
        //this.player.fullScreen().strategy(new ExclusiveModeFullScreenStrategy(this.frame));
        this.player.input().enableMouseInputHandling(false);
        this.player.input().enableKeyInputHandling(false); 
        //音频均衡器
        this.equalizer = this.factory.equalizer().newEqualizer();
        this.equalizerFrame = new EqualizerFrame(this.factory.equalizer().bands(), this.factory.equalizer().presets(), this.factory, this.player, this.equalizer);
        if(icon!=null)this.equalizerFrame.setIconImage(icon); 
        //视频调节器
        this.videoAdjustPanel = new PlayerVideoAdjustPanel(this.player);


        //音频时实现图片功能
        //canvas.addMouseListener(this); 
        //canvas.addKeyListener(this); 
        //canvas.addMouseMotionListener(this); 
        //canvas.addMouseWheelListener(this); 
        canvas.setEnabled(false); 
        //全屏模式
        frame.addMouseListener(this); 
        frame.addKeyListener(this);      
        frame.addMouseMotionListener(this); 
        frame.addMouseWheelListener(this);    
        //控制按钮事件绑定        
        //进度条      
        progress.addMouseListener(new MouseAdapter() {  
            @Override  
            public void mouseClicked(MouseEvent e){     //点击进度条调整视屏播放进度
                //if(prePlayOn)return;
                int x=e.getX();  
                jumpTo((float)x/progress.getWidth());  
            }  
        });   
        progress.setStringPainted(true);  //打印百分比
        progress.setBorderPainted(true);  //打印边框

        //剪切开始
        if(cutter!=null) {
            //btnCutBegin.setIcon(createImageIcon("icons/cut_begin.png"));
            btnCutBegin.setText(PlayerUIProps.btnCutBeginCaption);
            btnCutBegin.setToolTipText(PlayerUIProps.btnCutBeginCaption);
            btnCutBegin.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (getPrePlayOn()) return;
                    addCutBegin(player.status().time());
                }
            });
            //剪切结束
            //btnCutEnd.setIcon(createImageIcon("icons/cut_end.png"));
            btnCutEnd.setText(PlayerUIProps.btnCutEndCaption);
            btnCutEnd.setToolTipText(PlayerUIProps.btnCutEndCaption);
            btnCutEnd.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (getPrePlayOn()) return;
                    addCutEnd(player.status().time());
                }
            });
        }

        //停止       
        btnStop.setIcon(createImageIcon("icons/control_stop_blue.png"));
        btnStop.setToolTipText(PlayerUIProps.btnStopCaption);
        btnStop.addMouseListener(new MouseAdapter() {  
            @Override  
            public void mouseClicked(MouseEvent e) {
                if(getPrePlayOn())return;
                stop();  
            }  
        }); 
        //播放      
        btnPlay.setIcon(createImageIcon("icons/control_play_blue.png"));
        btnPlay.setToolTipText(PlayerUIProps.btnPlayCaption);
        btnPlay.addMouseListener(new MouseAdapter() {  
            @Override  
            public void mouseClicked(MouseEvent e) {
                //if(prePlayOn)return;
                play();  
            }  
        });  
        //暂停      
        btnPause.setIcon(createImageIcon("icons/control_pause_blue.png"));
        btnPause.setToolTipText(PlayerUIProps.btnPauseCaption);
        btnPause.addMouseListener(new MouseAdapter() {  
            @Override  
            public void mouseClicked(MouseEvent e) {
                //if(prePlayOn)return;
                pause();  
            }  
        }); 
        //全屏       
        btnFullScreen.setIcon(createImageIcon("icons/image.png"));
        btnFullScreen.setToolTipText(PlayerUIProps.btnFullScreenCaption);
        btnFullScreen.addMouseListener(new MouseAdapter() {  
            @Override  
            public void mouseClicked(MouseEvent e) {  
                fullScreen();  
            }  
        });         
        //打开文件     
        btnOpenFile.setIcon(createImageIcon("icons/control_eject_blue.png"));
        btnOpenFile.setToolTipText(PlayerUIProps.btnOpenFileCaption);
        btnOpenFile.addMouseListener(new MouseAdapter() {  
            @Override  
            public void mouseClicked(MouseEvent e) {
                if(getPrePlayOn())return;
                //openFile();
                client.openMsgDialog(2, PlayerProps.unsupportOpenFileTitle,
                        PlayerProps.unsupportOpenFile);
            }  
        }); 
        //关闭       
        btnClose.setIcon(createImageIcon("icons/disconnect.png"));
        btnClose.setToolTipText(PlayerUIProps.btnCloseCaption );
        btnClose.addMouseListener(new MouseAdapter() {  
            @Override  
            public void mouseClicked(MouseEvent e) {
                if(getPrePlayOn())return;
                close();  
            }  
        });        
        //静音      
        btnMute.setIcon(createImageIcon("icons/sound_mute.png"));
        btnMute.setToolTipText(PlayerUIProps.btnMuteCaption);
        btnMute.addMouseListener(new MouseAdapter() {  
            @Override  
            public void mouseClicked(MouseEvent e) {  
                mute();  
            }  
        });        
        //声音滑块
        slider.setMinimum(LibVlcConst.MIN_VOLUME);
        slider.setMaximum(LibVlcConst.MAX_VOLUME);
        slider.setValue((LibVlcConst.MAX_VOLUME-LibVlcConst.MIN_VOLUME)/2);
        slider.setToolTipText(PlayerUIProps.sliderTipText );
        slider.addChangeListener(new ChangeListener() {              
            @Override  
            public void stateChanged(ChangeEvent e) {  
                setVolume(slider.getValue());  
            }  
        }); 
        //重载
        btnReload.setIcon(createImageIcon("icons/connect.png"));
        btnReload.setToolTipText(PlayerUIProps.btnReloadCaption);
        btnReload.addMouseListener(new MouseAdapter() {  
            @Override  
            public void mouseClicked(MouseEvent e) {
                if(getPrePlayOn())return;
                if(isOpenFile || player.status().state()==State.PLAYING){
                    load(getMrl());
                }  
            }
        });      
        //均衡器
        btnEqualizer.setIcon(createImageIcon("icons/equalizer.png"));
        btnEqualizer.setToolTipText(PlayerUIProps.btnEqualizerCaption);
        btnEqualizer.addMouseListener(new MouseAdapter() {  
            @Override  
            public void mouseClicked(MouseEvent e) {  
                    openEqualizer(); 
            }
        });      
        //均衡器
        btnVideoAdjust.setIcon(createImageIcon("icons/video.png"));
        btnVideoAdjust.setToolTipText(PlayerUIProps.btnVideoAdjustCaption);
        btnVideoAdjust.addMouseListener(new MouseAdapter() {  
            @Override  
            public void mouseClicked(MouseEvent e) {  
                    openVideoAdjust();   
            }
        });  
        
        //加入画布开始自适用视频尺寸
        if(cutter==null)player.events().addMediaPlayerEventListener(new SizePlayerMediaPlayerEventListener());
     
        //初始化按钮
        initButtonsStatus(false); 
               
        //窗口关闭事件
        this.frame.addWindowListener( 
          new WindowAdapter(){
            public void windowClosing(WindowEvent e){
               clearup(); 
            }  
        });

        setPlayer(this);
    }

    /**
     * cutter关闭事件
     */
    private boolean isCutterClosed=false;
    @Override
    public void cutterClosed(){
        isCutterClosed=true;
        clearup();
    }
    //均衡器
    private void openEqualizer(){
        this.equalizerFrame.setVisible(true);
        this.equalizerFrame.setLocationRelativeTo(null); 
    }
    //视觉调节器
    private void openVideoAdjust(){
            if(frame instanceof JFrame)
                this.videoAdjustPanel.openDialog(client, (JFrame)frame);
            else
                this.videoAdjustPanel.openDialog(client, null);        
    }
    
    //开始适用远视频尺寸
    private final class SizePlayerMediaPlayerEventListener extends MediaPlayerEventAdapter {
        @Override
        public void videoOutput(MediaPlayer mediaPlayer, int newCount) {
            if (newCount == 0) {
                return;
            }
            final Dimension dimension = player.video().videoDimension();
            if (dimension != null) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        canvas.setSize(dimension);
                        frame.pack();
                    }
                });
            }
        }
    }
    
  
    //初始化控制按钮      
    private void initButtonsStatus(final boolean statusType){      
        //进度条      
        progress.setEnabled(statusType?canJump:statusType);        
        //停止
        btnStop.setEnabled(statusType); 
        //播放 
        btnPlay.setEnabled(statusType);
        //暂停   
        btnPause.setEnabled(statusType?canJump:statusType); 
        //全屏  
        btnFullScreen.setEnabled(statusType);        
        //打开 
        btnOpenFile.setEnabled(isOpenFile);
        //关闭 
        btnClose.setEnabled(statusType);      
        //静音 
        btnMute.setEnabled(statusType);        
        //声音滑块  
        slider.setEnabled(statusType);       
        //重载 
        btnReload.setEnabled(statusType); 
        //均衡
        btnEqualizer.setEnabled(statusType);     
        //视觉调节
        btnVideoAdjust.setEnabled(statusType);    
    } 
  
    //进度条刷新线程，该线程退出会引起播放器release关闭
    final protected Runnable progressThread = new  Runnable(){  //根据媒体播放的位置移动，侦测时间间隔1秒,媒体开始后启动
            @Override
            public void run(){
                waitStart();  //等待开始
                State state=null;
                preCmp=0;
                progressStatus=true;
                long tempTime;
                boolean toInit=true;
                boolean hasPlay=false;
                boolean mustWait=false;
                while(progressStatus){
                    state = player.status().state();
                    if(toInit){
                        if(state == State.PLAYING){
                            toInit=false;
                            hasPlay=true;
                            if(mustWait){
                                mustWait=false;
                                waitStart();
                            }
                            //打开按钮
                            startStatus();
                            //打印底色图，初始化音量
                            paintCanvasBackImage();
                            //打印状态，如果时长大于0，更时长
                            //printStatus();
                            //获取媒体长度
                            final long ml=player.status().length();
                            if(ml>0){
                                mediaLength=ml;
                                lblMediaLength.setText(VLCJUtil.buildTime(mediaLength));
                            }
                            //初始化音量 
                            initVolume();                           
                        }else{
                            progressUpdate(player.status().time(), true);
                            try{Thread.sleep(Const.progressWaitTimeMillis);}catch(Exception e){}
                            continue;
                        }                      
                     }else if(state == State.STOPPED){
                        toInit=true;
                        mustWait=true;  
                        progressUpdate(player.status().time(),true);
                        try{Thread.sleep(Const.progressWaitTimeMillis);}catch(Exception e){}
                        continue;                       
                     }else if(state ==  State.ENDED || state == State.ERROR){  //在未完全打开之前，除了PLAYING 和PAUSED外，其它都有可能
                        break; 
                     }
                     //printVolume();
                     progressUpdate(player.status().time(),true);
                     try{Thread.sleep(Const.progressWaitTimeMillis);}catch(Exception e){}
                }
                if(state ==  State.ENDED){
                    progressUpdate(mediaLength,true);            
                }
                if(!isOpenFile)clearup();   //播完关闭               
                progressStatus=false; 
            }
   };
  
 
   //打印声音状态
   private void printVolume(){
       StringBuilder sb = new StringBuilder()
       .append("outputDevice=").append(player.audio().outputDevice())
       .append(" isMute=").append(player.audio().isMute())
       .append(" volume=").append(player.audio().volume())
       .append(" channel=").append(player.audio().channel())
       .append(" delay=").append(player.audio().delay())
       .append(" equalizer=").append(player.audio().equalizer())
       .append(" trackCount=").append(player.audio().trackCount())
       .append(" track=").append(player.audio().track());
       if(client!=null)client.display(sb.append(PubConst.newLine).toString());
       else getClient().display(sb.toString());
   }

   
   private int preCmp=0;
   //进度条更新,子类可覆盖
   private void progressUpdate(long time,final boolean isSelf){
       updateStateUI();
       if(!isOpenFile && canJump && isSelf)return;
       if(time<0)time=0;
       if(time>mediaLength)time=mediaLength;
       playTime= time;
       updateTimeUI(time);
       try{
            int cmp= (int)(100*time/mediaLength);
            progress.setValue(cmp);
            //progress.setString(new StringBuilder().append(cmp).append('%').toString());       
            if(preCmp>cmp){  //重置进度条
                progress.updateUI();
            }
            preCmp=cmp;
        }catch(Exception e){
            e.printStackTrace();  //测试
        }
   }
      
    //开播改变
    private void startStatus(){
        //设置初始音量
        initButtonsStatus(true);
    } 


    //清理连接资源
    private boolean hasClear=false;
   @Override
    public synchronized void clearup(){
          if(hasClear)return;
          hasClear=true;
          setNoReady(true);
          progressStatus = false;
          if(frame!=null)frame.dispose();  //关闭界面
          if(equalizerFrame!=null)equalizerFrame.dispose();
          if(player!=null)try{player.controls().stop();player.release();}catch(Exception e){}  //关闭播放器
          if(factory!=null)try{factory.release();}catch(Exception e){}  //关闭工厂，释放后台资源
          if(client!=null){
             if(!isCutterClosed && getCutter()!=null){
                 setPrePlayOn(false);
                 getCutter().mediaPartFactoryClosed();
             }
          }else System.exit(0); //如果不是client启动，退出虚拟机
    }
    
    //隐藏控制栏
    protected abstract void hideControls(final boolean hide);
   
    //设置canvas播放音频的底色图
    private void paintCanvasBackImage(){
        PubConst.es.submit(new Runnable(){
            public void run(){
                paintCanvasBackImage(player.media().info().videoTracks().size()>0);
            }
        });
    }   
    private void paintCanvasBackImage(final boolean isVedio){
                if(isVedio){
                    canvas.removeHierarchyBoundsListener(this);
                    //VLCJUtil.initLogo(player, logoFile);
                    //VLCJUtil.initMarquee(player, marqueeText);
                 }else{
                    canvas.addHierarchyBoundsListener(this);
                    paintCanvasDefault(); 
                 }
    }
    private void waitStart(){
        int t=0;
        while(player.status().time()==-1){
           if(++t>Const.maxWaitStartTimes)break;
           try{Thread.sleep(Const.flushIntervalMillis);}catch(Exception e){}
        } 
        t=0;       
        while(player.audio().volume()==-1){
           if(++t>Const.maxWaitStartTimes)break;
           try{Thread.sleep(Const.flushIntervalMillis);}catch(Exception e){}
        }     
    }
    //在画布上画上底色图
    protected abstract void paintCanvasDefault(); /**{
      if(icon!=null)canvas.getGraphics().drawImage(icon, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
   } **/
    
    //重新加载
    private void reload(){
        PubConst.es.submit(new Runnable(){
            public void run(){
                player.controls().stop();  //这样新流加载后可以初始化
                paintCanvasBackImage(false);
                if(reload1())return;
                if(!reload1()){
                    openMsgDialog(3, 2);
                }
            }
        });
    }
    final protected boolean reload1(){
            final int seconds = getReloadWaitWaitSeconds();
            openMsgDialog(seconds, 0);
            try {
                Thread.sleep(flowWaitTimeMillis);
            } catch (Exception e) {
            }
                if(player.media().prepare(getMrl())){
                    if(!progressStatus){  //如果已经关闭启动
                       progressStatus=true;
                       PubConst.es.submit(progressThread);
                    }                    
                    try{Thread.sleep(Const.flowWaitDelayTimeMillis/2);}catch(Exception e){}  //提前一半时间
                    player.controls().start();
                    return true;
                }  
                return false;      
    }

    private void openMsgDialog(final int seconds, final int type){
        final String demo = getReloadWaitMsg(seconds, type);
        if(frame instanceof JFrame)
            if(client==null)getClient().display(demo);
            else
            client.openMsgDialog((JFrame)frame, seconds, PlayerUIProps.msgDialogTitle, demo );
        else
            if(client==null)getClient().display(demo);
            else
            client.openMsgDialog(seconds, PlayerUIProps.msgDialogTitle, demo );
    }
    private int getReloadWaitWaitSeconds(){
        int s = flowWaitTimeMillis/1000;
        if(flowWaitTimeMillis % 1000>0)s+=1;
        if(s==0)s=1; 
        return s;      
    }
    private String getReloadWaitMsg(final int seconds, final int type){    
        StringBuilder sb = new StringBuilder();
        switch(type){
            case 1:
                sb.append(PlayerUIProps.loadSuccessMsg);
                sb.append(seconds).append(PlayerUIProps.closeLateText);
                break;
            case 2:
                sb.append(PlayerUIProps.loadFalseText);
                sb.append(seconds).append(PlayerUIProps.closeLateText );
                break;
            default:
                sb.append(PlayerUIProps.waitLoadMsg);
                sb.append(seconds).append(PlayerUIProps.delayText);
        }            
        return sb.toString();
    }
    
    
    private boolean load(final String strMrl){
        if(player.media().start(strMrl)){
            if(!progressStatus){
                progressStatus=true;
                PubConst.es.submit(progressThread);
            }
            return true;
        }
        openMsgDialog(3, 2);
        return false;
    }
    
    //打开媒体，首次
    @Override
    public void play(final String strMrl){
        if(player.media().start(strMrl)){  //阻塞  如果是互联网，reload才真正播放     
            PubConst.es.submit(progressThread);
            setNoReady(false); 
            return;
        }
        try{Thread.sleep(flowWaitTimeMillis);}catch(Exception e){}
        if(player.media().start(strMrl)){  //阻塞  如果是互联网，reload才真正播放     
            PubConst.es.submit(progressThread);
            setNoReady(false);
        }else{
            openMsgDialog(3, 2);  
        }
    }
    
    //显示窗体
    @Override
    public void setVisible(final boolean visible){
        if(frame==null)return;      
        if(visible){
           updateTitle();
           if(!frame.isVisible())frame.setVisible(visible);
           centreWindow();       
        }else frame.setVisible(visible);     
    }
    
    private void centreWindow(){
        if(frame==null)return;
        int x,y;
        if(getCutter()==null) {
              x = (int) ((screenDimension.getWidth() - frame.getWidth()) / 2);
              y = (int) ((screenDimension.getHeight() - frame.getHeight()) / 2);
        }else{
             x = (int) (screenDimension.getWidth() - frame.getWidth());
             y = 0;
        }
        frame.setLocation(x, y);
        frame.setAlwaysOnTop(true);
    }
    
  
    //全屏，不是真正的全屏只是缩放
    private boolean isFullScreen=false;
    public void fullScreen() {
        isFullScreen=!isFullScreen;
        player.fullScreen().set(isFullScreen);
        hideControls(isFullScreen);
        lastMouseEventTime = System.currentTimeMillis();
        if(isFullScreen){
            cursorHidding = true;
            PubConst.es.submit(cursorHideRun);
        }else{
            cursorHidding = false;
        }
    }

    // 实现控制声音的方法setVolume(range 0 to 200（可能是100）
    public void setVolume(int v) {
        //client.getDisplayService().display("volume="+v+PubConst.newLine);
        if(player.audio().isMute())mute();
        player.audio().setVolume(v);
        lblVolume.setText(String.valueOf(100 * v / (LibVlcConst.MAX_VOLUME - LibVlcConst.MIN_VOLUME)) + '%');
    }   
    // 实现控制声音的方法mute
    public void mute() {
        //client.getDisplayService().display("mute="+player.audio().isMute()+" volume="+player.audio().volume()+PubConst.newLine);circlePlayText
        final boolean isMute = player.audio().isMute();
        player.audio().mute();
        btnMute.setToolTipText(PlayerUIProps.btnMuteCaption + (isMute ? PlayerUIProps.btnMuteCaptionExt [1] : PlayerUIProps.btnMuteCaptionExt [0]));
    } 
    //初始化音量
    private void initVolume(){
       final int v1 = slider.getValue();
       final int v2 = player.audio().volume();
       if(v1==v2)lblVolume.setText(String.valueOf(100 * v1 / (LibVlcConst.MAX_VOLUME - LibVlcConst.MIN_VOLUME)) + '%');
       else slider.setValue(v2);  //激发setVolume(int v)
       lblTrackCount.setText(""+player.audio().trackCount());
       btnMute.setToolTipText(PlayerUIProps.btnMuteCaption + (player.audio().isMute() ?PlayerUIProps.btnMuteCaptionExt [0] : PlayerUIProps.btnMuteCaptionExt [1]));
    }  

/////////////// == I_VLCJPlayer ========================


   
        //暂停状态，true为需要暂停
        private final boolean pauseStatus=false;
        //媒体播放时间，可能是服务器传了的timeForServer =true
        private volatile long playTime=0;
        /**
         *执行发送方终止命令 
        **/
        private void kill(){
            clearup();                
        }        
       /**
        *执行己方跳转百分比命令
        *@to 0.0-100.0
       **/
       private void jumpTo(float to){

                    long lto = (long) (to * mediaLength);
                    if(getPrePlayOn()){
                        if(lto<begin+Const.prePlayPrefixTimeMillis){
                            lto=begin;
                        }
                        if(lto>end){
                            lto=end-Const.prePlayPrefixTimeMillis;
                            if(lto<0)lto=0;
                        }
                    }
                    player.controls().setTime(lto);

        }
        

    private long begin;
       private long end;
    @Override
    public void prePlayAction(long begin, long end){
        this.begin=begin;
        this.end=end;
        if(begin>0)player.controls().setTime(begin);
        long t1,t2,t3,t4;  //检测是不是在滑动
        while(getPrePlayOn()){
            State state = player.status().state();
            if(state==State.STOPPED || state==State.ENDED || state==State.ERROR)break;
            t1=System.currentTimeMillis();
            t2= player.status().time();
            try{Thread.sleep(250);}catch(Exception e){}
            player.status().state();
            if(state==State.STOPPED || state==State.ENDED || state==State.ERROR)break;
            t3=System.currentTimeMillis();
            t4= player.status().time();
            if(t4-t2>t3-t1+Const.jumpSafeTimeMillis || t4<end){
                try{Thread.sleep(250);}catch(Exception e){}
            }else if(t4>=end)break;
        }
    }
        
        private volatile long prePauseClickTime=0;      
        // 实现暂停按钮的方法
        private void pause() { 
            final long now =  System.currentTimeMillis();
            if(now < prePauseClickTime +Const.minPauseTime){  //频繁点击不响应
                prePauseClickTime=now;
                return;
            }

                player.controls().pause();
    
        }

        
        //实现播放按钮的方法
        private void play() {
                    player.controls().play();
        }
        
        //实现停止按钮的方法
        private void stop() {
                    player.controls().stop();
                    paintCanvasBackImage(false); 
                    //本地无需停止stopProgress();
        }
        
        /**
        *重复播放命令
       **/
        private void setRepeat(final boolean repeat){
               player.controls().setRepeat(repeat);
        }


/////VLCJProviderDisplay
    protected String title = PlayerUIProps.title;
    //改变标题
    public void updateTitle(){
        title=getTitle();
        updateTitleUI();
    }
    //更新标题改变，子类可覆盖
    protected abstract void updateTitleUI();
        
    //更改状态
    protected void updateState(final String state){
        if(getNoReady())return;  //二进制错误try{}catch{}没用，只能用开关避免
        if(this.state==null || !this.state.equals(state)){
            this.state=state;
            updateStateUI(); 
        }      
    }
    private void updateStateUI(){
        if(getNoReady())return;  //二进制错误try{}catch{}没用，只能用开关避免
        try{
            if(state==null)
                lblState.setText(player.status().state().toString());
            else
                lblState.setText(String.valueOf(player.status().state()) + '/' + state);
        }catch(Exception e){
           e.printStackTrace();
        }
    }    
   
    //更新时间.
    protected void updateTime(final long  time){
        if(this.providerTime!=time){
           this.providerTime=time;
           if(getNoReady())return;
           progressUpdate( time,false);
        }
    } 
    private void updateTimeUI(final long time){
        if(this.providerTime==0)
           lblTime.setText(VLCJUtil.buildTime(time));
        else
           lblTime.setText(VLCJUtil.buildTime(time) + '/' + VLCJUtil.buildTime(this.providerTime));
    }
   
    //更新长度
    protected void updateMediaLength(final long mediaLength){
        if(this.mediaLength!=mediaLength){
            this.mediaLength=mediaLength;
            lblMediaLength.setText(VLCJUtil.buildTime(mediaLength));
        }
    }
    
    //更新重复
    protected void updateRepeat(final boolean repeat){
        if(this.repeat!=repeat){
            this.repeat=repeat;
            title=getTitle();
            updateTitleUI();          
        }
    }
    private String getTitle(){
        final String t=name==null?getMrl():name;
            if(repeat)return t + PlayerUIProps.circlePlayText;
            return t;
    }
    

 
      
        
////////////////
  
/*****
    // === MediaPlayerEventListener =============================================

    @Override
    public void mediaChanged(MediaPlayer mediaPlayer, MediaRef media) {
    }

    @Override
    public void opening(MediaPlayer mediaPlayer) {
    }

    @Override
    public void buffering(MediaPlayer mediaPlayer, float newCache) {
    }

    @Override
    public void playing(MediaPlayer mediaPlayer) {
    }

    @Override
    public void paused(MediaPlayer mediaPlayer) {
    }

    @Override
    public void stopped(MediaPlayer mediaPlayer) {
    }

    @Override
    public void forward(MediaPlayer mediaPlayer) {
    }

    @Override
    public void backward(MediaPlayer mediaPlayer) {
    }

    @Override
    public void finished(MediaPlayer mediaPlayer) {
    }

    @Override
    public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
    }

    @Override
    public void positionChanged(MediaPlayer mediaPlayer, float newPosition) {
    }

    @Override
    public void seekableChanged(MediaPlayer mediaPlayer, int newSeekable) {
    }

    @Override
    public void pausableChanged(MediaPlayer mediaPlayer, int newSeekable) {
    }

    @Override
    public void titleChanged(MediaPlayer mediaPlayer, int newTitle) {
    }

    @Override
    public void snapshotTaken(MediaPlayer mediaPlayer, String filename) {
    }

    @Override
    public void lengthChanged(MediaPlayer mediaPlayer, long newLength) {
    }

    @Override
    public void videoOutput(MediaPlayer mediaPlayer, int newCount) {
    }

    @Override
    public void scrambledChanged(MediaPlayer mediaPlayer, int newScrambled) {
    }

    @Override
    public void elementaryStreamAdded(MediaPlayer mediaPlayer, TrackType type, int id) {
    }

    @Override
    public void elementaryStreamDeleted(MediaPlayer mediaPlayer, TrackType type, int id) {
    }

    @Override
    public void elementaryStreamSelected(MediaPlayer mediaPlayer, TrackType type, int id) {
    }

    @Override
    public void corked(MediaPlayer mediaPlayer, boolean corked) {
    }

    @Override
    public void muted(MediaPlayer mediaPlayer, boolean muted) {
    }

    @Override
    public void volumeChanged(MediaPlayer mediaPlayer, float volume) {
    }

    @Override
    public void audioDeviceChanged(MediaPlayer mediaPlayer, String audioDevice) {
    }

    @Override
    public void chapterChanged(MediaPlayer mediaPlayer, int newChapter) {
    }

    @Override
    public void error(MediaPlayer mediaPlayer) {
    }

    @Override
    public void mediaPlayerReady(MediaPlayer mediaPlayer) {
    }

    // === MediaEventListener ===================================================

    @Override
    public void mediaMetaChanged(Media media, Meta metaType) {
    }

    @Override
    public void mediaSubItemAdded(Media media, MediaRef newChild) {
    }

    @Override
    public void mediaDurationChanged(Media media, long newDuration) {
    }

    @Override
    public void mediaParsedChanged(Media media, MediaParsedStatus newStatus) {
    }

    @Override
    public void mediaFreed(Media media, MediaRef mediaFreed) {
    }

    @Override
    public void mediaStateChanged(Media media, State newState) {
    }

    @Override
    public void mediaSubItemTreeAdded(Media media, MediaRef item) {
    }

    @Override
    public void mediaThumbnailGenerated(Media media, Picture picture) {
    }
*/
    // === MouseListener ========================================================
    private volatile long lastMouseEventTime = 0;
    private volatile boolean cursorHidding = false;
    private final Runnable cursorHideRun = new Runnable(){
         public void run(){
             while(cursorHidding){
                 final long lmet = lastMouseEventTime;
                 try{Thread.sleep(Const.hideMouseCursorWaitTimeMillis);}catch(Exception e){}
                 if(player.fullScreen().isFullScreen()){
                     hideCursor(lmet == lastMouseEventTime);
                 }

             }
        }
    };
    
    // Create a new blank cursor.
    final protected static Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
        new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "blank cursor");

    private Cursor cursor=null;
    private synchronized void hideCursor(final boolean isHide){
        //getClient().display("now="+System.currentTimeMillis()+ " lastMouseEventTime="+lastMouseEventTime+ " isHide="+isHide);
        if(isHide){
            if(cursor==null)cursor = frame.getCursor();
            frame.setCursor(blankCursor);            
        }else{
            lastMouseEventTime = System.currentTimeMillis();
            frame.setCursor(cursor); 
            //frame.setVisible(true);          
        }  
    }
    
    //双击支持
    private long preTime=0;
    private boolean oneClicked=false;
    @Override
    public void mouseClicked(MouseEvent e) {
                hideCursor(false);
                long now = System.currentTimeMillis();
                if(now-preTime<Const.doubleClickedInterval){
                    oneClicked=false;
                    fullScreen();
                }else{
                        oneClicked=true;
                        PubConst.es.submit(new Runnable(){
                            public void run(){
                                try{Thread.sleep(Const.doubleClickedInterval+50);}catch(Exception e){}
                                if(oneClicked)pause(); 
                            }
                        });
                }
                preTime = now;
    }

    @Override
    public void mousePressed(MouseEvent e) {
       hideCursor(false);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        hideCursor(false);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        hideCursor(false);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        hideCursor(false);
    }
    


    // === MouseMotionListener ==================================================

    @Override
    public void mouseDragged(MouseEvent e) {
        hideCursor(false);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        hideCursor(false);
    }

    // === MouseWheelListener ===================================================

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        hideCursor(false);
    }
    


    // === KeyListener ==========================================================

    @Override
    public void keyTyped(KeyEvent e) {
        fullScreen();
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    //  ============ HierarchyBoundsListener ==================
    /**
     * Called when an ancestor of the source is moved.
     */
    public void ancestorMoved(HierarchyEvent e) {
        hideCursor(false);
        paintCanvasDefault();          
    }


    /**
     * Called when an ancestor of the source is resized.  
    */
    private volatile boolean resizeOn = false;  //多线程使用的快速变化的变量，使用volatile修饰符解决cup级变量不同版本问题
    public void ancestorResized(HierarchyEvent e) {
        hideCursor(false);
        resizeOn = false;
        paintCanvasDefault();
        resizeOn = true;
        PubConst.es.submit(new Runnable(){  //这种简单粗暴的延迟刷新解决了黑屏问题。
            public void run(){
               try{Thread.sleep(Const.resizingIntervalMillis);}catch(Exception e){}
               if(resizeOn){
                  resizeOn = false;
                  paintCanvasDefault();
                  resizeOn = true;
               }
           }
       });
    }

    /**
     *创建icon
    **/
     private ImageIcon createImageIcon(final String path){
        client.display("path="+path);  //测试
        URL url = getClass().getClassLoader().getResource(path);  //classes根目录
        if(url!=null)return new ImageIcon(url);
        url = getClass().getResource(path);  //class所在目录
        if(url!=null)return new ImageIcon(url);
        try{
             File f = new File(path);  //运行根目录
             client.display(f.getAbsolutePath());
             Image image = ImageIO.read(f);
             return new ImageIcon(image,null);
        }catch(Exception e){
             e.printStackTrace();
             return null;
        }

    }


 }
