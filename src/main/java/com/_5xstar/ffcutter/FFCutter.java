package com._5xstar.ffcutter;

import com._5xstar.ffcutter.constant.PubConst;
import com._5xstar.ffcutter.domains.FileMediaInfo;
import com._5xstar.ffcutter.domains.MediaPart;
import com._5xstar.ffcutter.file.MyFileService;
import com._5xstar.ffcutter.util.MediaUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

/**
 * 剪切器接口，用于与媒体片段发生器沟通
 * 庞海文 2024-1-17改
 */
public interface FFCutter extends FFCutterUI, FFCutterStatus, Media, AutoCloseable{

    /**
     *   mp是临时对象，更新（mp.num>0)或添加(mp.num<0)片段，当mp.num<0时，如果mp.index<0则插入文件
     */
    default void updatePart(final MediaPart mp){
        if(mp.num>0){  //更新片段
            if(getTmpPartSrc()==null){
                getClient().openMsgDialog(3,FFCutterProps.cutTitle, FFCutterProps.updatePartMsg[0]);
                clearMediaPartFactory();
            }else{
                updateMediaPartsChooser();
                setCurrentIndex(getTmpPartSrc().num-1);
                //currentIndex=tmpPartSrc.num-1;
                loadMediaPart(mp);
                setEditType(updateType);
                //editType=updateType;
                updateYesCaption();
            }
        }else{  //新片段
            final String factory = getMediaPartFactory().toString();
            if(mp.index<0){  //媒体片段发生器打开文件添加片段
                final List<FileMediaInfo> fileMediaInfos = getFileMediaInfos();
                final int fidx = fileMediaInfos.size();
                final FileMediaInfo fmi = checkFile(fidx, mp.file);
                if(fmi!=null) {
                    fileMediaInfos.add(fmi);
                    addItem(fmi);
                    mp.index = fidx;   //首次更新加入
                    getClient().display("1:"+factory,Display.DEBUG_TEXT);
                    //System.out.println("1:"+factory);
                    fileChooserChange1( fmi);
                    getClient().display("2:"+factory,Display.DEBUG_TEXT);
                    //System.out.println("2:"+factory);
                    updateFileChooserUI();
                    getClient().display("3:"+factory,Display.DEBUG_TEXT);
                    //System.out.println("3:"+factory);
                    setTmpPartSrc(null);
                    //tmpPartSrc=null;
                }else{
                    getClient().openMsgDialog(3,FFCutterProps.cutTitle, FFCutterProps.updatePartMsg[1]);
                    clearMediaPartFactory();
                    return;
                }
            }
            MediaUtil.load(getBegins(),getEnds(), mp);
            setEditType(addType);
            //editType=addType;
            updateYesCaption();
            getClient().display("4:"+factory,Display.DEBUG_TEXT);
            //System.out.println("4:"+factory);
        }
    }

    /**
     * 清理媒体片段发生器
     */
    private void clearMediaPartFactory() {
        setTmpPartSrc(null);
        //tmpPartSrc = null;  //情况临时关联片段
        MediaPartFactory factory = getMediaPartFactory();
        if (factory != null) {
            factory.cutterClosed();  //关闭媒体片段发生器，不用反馈消息
            resetPrePlayStatus();  //重置浏览按钮
            setMediaPartFactory(null);
            //this.mediaPartFactory = null;
        }
    }

    /**
     *     更新媒体片段列表界面
     */
    private void updateMediaPartsChooser(){
        Vector<MediaPart> parts = getParts();
        if(parts.isEmpty()) {    //为空时重置
            reset();
        }else{   //对片段进行编码
            MediaPart mp;
            for(int i=0;i<parts.size();i++){
                mp=parts.elementAt(i);
                mp.num=i+1;
            }
            updateMediaPartsChooserUI();  //更新列表界面
        }
    }


    /**
     * 重置数据与界面
     */
    private   void reset(){
        //清理媒体片段发生器
        clearMediaPartFactory();
        //重置媒体片段选择器
        resetMediaPartsChooser();
        //源文件选择器
        resetFileChooser();
        //重置输入组件
        resetInputFields();
    }

    /**
     * 重置媒体片段选择器
     */
    private void resetMediaPartsChooser(){
        Vector<MediaPart> parts = getParts();
        //媒体片段数据清空
        if(!parts.isEmpty())parts.clear();
        setCurrentIndex(-1);
        //currentIndex=-1;  //重置媒体片段指针
        resetMediaPartsChooserUI();
    }

    /**
     * 重置文件选择器
     */
    private void resetFileChooser(){
        List<FileMediaInfo> fileMediaInfos = getFileMediaInfos();
        if(fileMediaInfos.size()<2)return;  //只有一个源文件不用重置
        setIndex(0);
        //this.index=0;  //源文件指针指向第一个文件
        resetFileChooserUI();
        updateTitle();
    }

    /**
     * 重置输入组件
     */
    private void resetInputFields(){
        resetInputFields(getEditType()==addType);
        updateYesCaption();
    }
    private void resetInputFields(final boolean isAdd){
        if(isAdd)passEnds2Begins();  //把结束拷贝到开始
        else{
            FFCTextField[] bs = getBegins();  //不可以直接使用begins
            for(int i=0; i<bs.length; i++)bs[i].setText("");
        }
        FFCTextField[] es = getEnds();  //不可以直接使用ends
        for(int i=0; i<es.length; i++)es[i].setText("");
    }

    /**
     * 更新确定按钮标签
     */
    private void updateYesCaption(){
        int editType = getEditType();
        switch (editType){
            case addType:
                updateYesCaption(FFCutterProps.addCaption);
                break;
            case insertType:
                updateYesCaption(FFCutterProps.insertCaption);
                break;
            case updateType:
                updateYesCaption(FFCutterProps.updateCaption);
        }
    }

    /**
     * 变更文件选择器
     * @param fmi
     */
    private void fileChooserChange1(final FileMediaInfo fmi){
        if (getIndex() == fmi.index) return;  //源文件无变化
        setIndex(fmi.index);
        //this.index  = fmi.index;
        updateTitle();
    }



    /**
     * 媒体片段发生器关闭
     */
     default void mediaPartFactoryClosed(){
         resetPrePlayStatus();
         setMediaPartFactory(null);
         //this.mediaPartFactory=null;
     }

    /**
     * 预览完成
     */
    default void resetPrePlayStatus(){
        setEnabled(true, true, false);
    }
    

    ////////////////    FFCutterBase        ///////////////////
    /**
     *  添加事件操作
     */
    default void add(){
        //清理媒体片段发生器
        clearMediaPartFactory();
        setEditType(addType);
        //editType=addType;
        Vector<MediaPart> mps = getParts();
        final int size = mps.size();
        if(size>1 && getCurrentIndex()<size-1){
            setCurrentIndex(size-1);
            //currentIndex=size-1;  //移到最后一项
            updateMediaPartsChooserUI();  //更新选择器，文件选择器不变化
        }
        resetInputFields(); //重置输入组件
    }


    /**
     *插入事件操作
     */
    default  void insert(){
        //清理媒体片段发生器
        clearMediaPartFactory();
        if(getCurrentIndex()<0)return;
        Vector<MediaPart> mps = getParts();
        setEditType(mps.size()>0?insertType:addType);
        //editType=mps.size()>0?insertType:addType;
        resetInputFields(); //重置输入组件
    }

    /**
     * 更新事件
     */
    default void update(){
        //清理媒体片段发生器
        clearMediaPartFactory();
        if(getCurrentIndex()<0)return;
        setEditType(updateType);
        //editType=updateType;
        updateYesCaption();
        Vector<MediaPart> mps = getParts();
        loadMediaPart(mps.elementAt(getCurrentIndex()));  //装载片段内容
    }

    /**
     *移除
     */
    default void remove(){
        //清理媒体片段发生器
        clearMediaPartFactory();
        if(getCurrentIndex()<0)return;
        Vector<MediaPart> mps = getParts();
        mps.remove(getCurrentIndex());
        refreshAfterRemove();
    }
    private void refreshAfterRemove(){
        final int cidx = getCurrentIndex();
        final Vector<MediaPart> mps = getParts();
        if(cidx>mps.size()-1)setCurrentIndex(mps.size()-1);  //不能超过最后一项
        updateMediaPartsChooser();
        if(mps.isEmpty() || cidx==mps.size()-1)setEditType(addType);
        else setEditType(insertType);
        resetInputFields(); //重置输入组件
    }

    /**
     *向上移动一位
     */
    default void up(){
        //清理媒体片段发生器
        clearMediaPartFactory();
        final int cidx = getCurrentIndex();
        if(cidx<=0)return;
        final Vector<MediaPart> mps = getParts();
        MediaPart tmp=mps.elementAt(cidx-1);
        mps.setElementAt(mps.elementAt(cidx), cidx-1);
        mps.setElementAt(tmp, cidx);
        setCurrentIndex(cidx-1);
        updateMediaPartsChooser();
    }

    /**
     * 向下移动一位
     */
    default void down(){
        //清理媒体片段发生器
        clearMediaPartFactory();
        final int cidx = getCurrentIndex();
        final Vector<MediaPart> mps = getParts();
        if(mps.size()<2 || cidx==mps.size()-1)return;
        MediaPart tmp=mps.elementAt(cidx+1);
        mps.setElementAt(mps.elementAt(cidx), cidx+1);
        mps.setElementAt(tmp, cidx);
        setCurrentIndex(cidx+1);
        updateMediaPartsChooser();
    }

    /**
     * 当前预览
     */
    default  void preViewCurrent(){
        //setEnabled(false, false, true);
        //prePlayRunning=true;
        //PubConst.es.submit(preViewCurrentThread);
        final Vector<MediaPart> mps = getParts();
        if(mps.isEmpty())return;
        final ArrayList<MediaPart> tps = new ArrayList<MediaPart>(1);
        tps.add(createPrePlayMediaPart(mps.get(getCurrentIndex())));
        prePlay(tps);
    }

    /**
     * 全部预览
     */
    default void preViewAll(){
        //setEnabled(false, false, true);
        //prePlayRunning=true;
        //PubConst.es.submit(preViewAllThread);
        final Vector<MediaPart> mps = getParts();
        if(mps.isEmpty())return;
        final ArrayList<MediaPart> tps = new ArrayList<MediaPart>(mps.size());
        for(MediaPart mp : mps){
            tps.add(createPrePlayMediaPart(mp));
        }
        prePlay(tps);
    }
    private MediaPart createPrePlayMediaPart(final MediaPart mp){
        final MediaPart nmp= new MediaPart();
        nmp.file = mp.file;
        nmp.begin = mp.begin<0L?0L:mp.begin*10;
        if(mp.end<0L)nmp.end=getFileMediaInfos().get(mp.index).mediaInfo.duration.longDuration*10;
        else nmp.end=mp.end*10;
        return nmp;
    }

    /**
     * 不再预览，在播放媒体是无法关闭，只能用户自己关闭
     */
    default void preViewStop(){
        //setEnabled(true, true, false);
        //prePlayRunning=false;
        prePlayStop();
    }

    /**
     * 剪切，合并为一个媒体文件
     */
    default void cut(final Runnable execRun){
        //清理媒体片段发生器
        clearMediaPartFactory();
        getClient().openConfirmDialog(FFCutterProps.dialogTitle,     //有确定、否定、超时，弹窗可以关闭
                FFCutterProps.cutMsg,
                6,
                new  Runnable(){  //yes
                    public void run(){
                        dispose();
                        action(execRun);
                    }
                },
                new Runnable(){public void run(){}},
                null);
    }

    /**
     * 只剪切，不合并为一个媒体文件
     */
    default void onlyCut(final Runnable execRun){
        //清理媒体片段发生器
        clearMediaPartFactory();
        setToOne(false);
        //toOne = false;   //不合并文件
        getClient().openConfirmDialog(FFCutterProps.dialogTitle,     //有确定、否定、超时，弹窗可以关闭
                FFCutterProps.onlyCutMsg,
                6,
                new  Runnable(){  //yes
                    public void run(){
                        dispose();
                        action(execRun);
                    }
                },
                new Runnable(){public void run(){}},
                null);
    }

    /**
     *
     * @param execRun
     */
    private void action(final Runnable execRun){   //toOne=true合并为1文件
        //创建输入输出临时文件
        HashMap<File, File> sysSrcFiles=null;
        final List<FileMediaInfo> fmis = getFileMediaInfos();
        try{
            //拷贝用户媒体文件到临时文件，防止加工破坏文件
            final List<File> tpfs = getTempFiles();
            final boolean toOne = getToOne();
            sysSrcFiles = new HashMap<File, File>(fmis.size());
            File f;
            FileMediaInfo fmi;
            for(int i=0;i<fmis.size();i++){
                fmi = fmis.get(i);
                f = MediaUtil.createSrcFile(fmi.file);
                tpfs.add(f);
                sysSrcFiles.put(fmi.file, f);
                //System.out.println(this.srcFiles[i].getName() + ">>" + f.getName());  //测试
            }
            //目标文件
            File tf = getTarFile();
            final String fn = tf.getName();  //输入没有后缀的名称
            //System.out.println("tarFile:"+fn);
            if(toOne && fn.lastIndexOf('.')<0){
                String extName;
                final String sfn = fmis.get(0).file.getName();  //第一个文件的格式
                final int idx = sfn.lastIndexOf('.');
                if(idx>-1)extName=sfn.substring(idx);
                else extName = ".mp4";
                tf = new File(tf.getParentFile(), new StringBuilder().append(fn).append(extName).toString());
                setTarFile(tf);
                //this.tarFile = new File(tf.getParentFile(), new StringBuilder().append(fn).append(extName).toString());
            }
            final File stf = MediaUtil.createTarFile(tf);  //生成临时文件或文件夹，并没有创建本地文件
            setSysTarFile(stf);
            //sysTarFile = MediaUtil.createTarFile(this.tarFile);  //生成临时文件或文件夹，并没有创建本地文件
            //System.out.println(this.tarFile.getName() + ">>" + sysTarFile.getName());  //测试
            tpfs.add(stf);
        }catch(Exception e){
            e.printStackTrace();
            getClient().display(FFCutterProps.actionMsg[0], Display.DEBUG_TEXT);
            return;
        }
        //System.out.println("临时文件生成完成，准备进行加工！");  //测试
        MediaPart mp;
        List<String> concat=null;
        final Vector<MediaPart> mps = getParts();
        if(fmis.size()==1 && mps.size()<=1) {
            final File sf = fmis.get(0).file;
            final File tf = getTarFile();
            if (mps.isEmpty()) {
                mp = new MediaPart();
                mp.isCopy = MediaUtil.checkCopy(sf, tf);  //采用智能判断 剪切与copy不兼容
                mps.add(mp);
            } else {
                mp = mps.elementAt(0);
            }
            mp.srcFile = sysSrcFiles.get(sf);
            mp.tarFile = getSysTarFile();
            //mp.isCopy=MediaUtil.checkCopy(this.srcFile, this.tarFile);  //采用智能判断 剪切与copy不兼容
        }else{  //要建立concat utf-8   一行：file '201.mp4'
            try{
                final int len=mps.size();
                final boolean toOne = getToOne();
                System.out.println("len="+len+" toOne="+toOne);
                List<File> tempFiles = getTempFiles();
                if(toOne){
                    final StringBuilder concatLines=new StringBuilder(len*100);
                    for(int i=0; i<len; i++){
                        mp=mps.elementAt(i);
                        mp.srcFile = sysSrcFiles.get(mp.file);  //原文件改为临时文件
                        mp.tarFile = MediaUtil.createTarFile(mp.file);  //采用拷贝，格式与原文件相同
                        tempFiles.add(mp.tarFile);
                        concatLines.append(MediaUtil.buildConcatLine(mp.tarFile));
                    }
                    File concatFile=MediaUtil.createTarFile("txt");
                    tempFiles.add(concatFile);
                    MyFileService.writeStringToUTF8File(concatFile, concatLines.toString());
                    concat=MediaUtil.createConcatCom(concatFile,  getSysTarFile());
                }else{
                    //建立输出目录
                    final File  tf = getTarFile();
                    tf.mkdirs();
                    String name = tf.getName();
                    String extName;
                    int idx = name.lastIndexOf(".");
                    if(idx >= 0){
                        extName = name.substring(idx);
                    }else{
                        extName = ".mp4";
                    }
                    final int nameLen = (""+len).length();
                    for(int i=0; i<len; i++){
                        mp=mps.elementAt(i);
                        mp.srcFile=sysSrcFiles.get(mp.file);
                        mp.tarFile=new File(tf, getName(i+1,nameLen,extName));
                    }
                }
            }catch(IOException e){
                e.printStackTrace();
                getClient().openMsgDialog(3, FFCutterProps.dialogTitle, FFCutterProps.actionMsg[1]);
                return;
            }
        }
        List<List<String>> coms = getComs();
        MediaUtil.buildMediaPartComs( coms, mps);
        if(concat!=null){
            coms.add(concat);
        }
        getClient().display("coms="+ coms);  //测试
        PubConst.es.submit(execRun);  //准备完成，执行操作
    }

    /**
     * 重置按钮
     */
    default void resetAction(){
        //清理媒体片段发生器
        clearMediaPartFactory();
        getClient().openConfirmDialog(FFCutterProps.dialogTitle,     //有确定、否定、超时，弹窗可以关闭
                FFCutterProps.resetActionMsg,
                0,
                new  Runnable(){  //yes
                    public void run(){
                        reset();
                    }
                },
                new Runnable(){public void run(){}},
                null);
    }

    /**
     * 编辑录入执行
     */
    default void yes(){
        try{
            clearMediaPartFactory();
            MediaPart mp=new MediaPart();
            mp.begin=getBeginTime();
            mp.end= getEndTime();
            final long endTime = getFileMediaInfos().get(getIndex()).mediaInfo.duration.longDuration;
            if((mp.end<0L && mp.begin<endTime)        //到结束，开始时间比结束小
                    || (mp.end>mp.begin && mp.end<=endTime)){  //结束大于开始，结束小于等于文件结束
                //添加、插入或更新片段
                if(getEditType()==updateType){
                    final MediaPart smp= getParts().elementAt(getCurrentIndex());
                    smp.begin=mp.begin;
                    smp.end=mp.end;
                    edit1(smp);
                }else {
                    edit(mp);
                }
                resetInputFields();
            }else{
                getClient().openMsgDialog(3, FFCutterProps.partTitle, FFCutterProps.yesMsg[0]+MediaUtil.buildStrFromPersentSeconds(endTime));
            }
        }catch(Exception e33){
            getClient().openMsgDialog(3, FFCutterProps.partTitle, FFCutterProps.yesMsg[1]+e33.getMessage());
        }
    }

    /**
     * 添加、插入或更新片段
     * @param mp
     */
    private void edit(final MediaPart mp){
        final int idx = getIndex();
        mp.index = idx;  //加入文件索引
        mp.file = getFileMediaInfos().get(idx).file;  //添加文件便于显示
        edit1(mp);
    }
    private void edit1(final MediaPart mp){
        final int editType = getEditType();
        final Vector<MediaPart> parts = getParts();
        switch(editType){
            case addType:
                parts.add(mp);
                setCurrentIndex(parts.size()-1);
                //currentIndex=parts.size()-1;  //增加之后在最后一项
                break;
            case insertType:
                parts.add(getCurrentIndex(),mp);
                break;
            case updateType:
                //parts.setElementAt(mp, currentIndex);
                //nothing to do
                break;
        }
        updateMediaPartsChooser();
    }

    /**
     * UI源文件变化事件处理
     * @param fmi
     */
    default void fileChooserChange(final FileMediaInfo fmi){
        getClient().display(fmi.file.getName());
        //System.out.println(fmi.file.getName());  //测试
        clearMediaPartFactory();
        refresh();
        fileChooserChange1(  fmi);
    }
    

    /**
     *   清空正在编辑的内容
     */
    default void refresh(){
        resetInputFields(false);
    }


    /**
     * 一个片段被点
     */
    default void partsChange(final int newCurrentIndex){
        if(getCurrentIndex()!=newCurrentIndex) {
            setCurrentIndex(newCurrentIndex);
            MediaPart mp = getParts().elementAt(getCurrentIndex());
            if (mp != null && getIndex() != mp.index) {
                setIndex(mp.index);
                updateFileChooserUI();
                updateTitle();
            }
        }
        update();
    }


    private String getName(final int num, final int nameLen, final String extName){
        String snum = "" + num;
        final int snumLen = snum.length();
        if(snumLen == nameLen)return snum+extName;
        StringBuilder sb = new StringBuilder();
        for(int i= 0; i<nameLen - snumLen; i++)sb.append('0');
        sb.append(snum);
        sb.append(extName);
        return sb.toString();
    }

    /**
     * 获得焦点，清除内容
     */
    default void clearTextOnGetFocus(final FFCTextField tf ){
        tf.setText("");
    }




    //implements for I_FFCutter
    Object synObject = new Object();

    //媒体片段发生器
    default void linkMediaPartFactory(){
        synchronized (synObject) {
            refresh();
            if (getMediaPartFactory() == null) {
                setMediaPartFactory(createMediaPartFactory());
                //mediaPartFactory = createMediaPartFactory();
                setOkRun(new Runnable() {
                    @Override
                    public void run() {
                        final MediaPartFactory mediaPartFactory = getMediaPartFactory();
                        if (mediaPartFactory != null) mediaPartFactory.cutterClosed();
                    }
                });
            }
            MediaPart mp;
            if (getEditType() != updateType) {
                mp = new MediaPart();
                if (!getFileMediaInfos().isEmpty()) {
                    mp.index = getIndex();  //加入文件索引，未入parts，未加入
                    mp.file = getFileMediaInfos().get(getIndex()).file;  //添加文件便于显示
                }
                setTmpPartSrc(mp);
                //tmpPartSrc=mp;
            } else{
                setTmpPartSrc(getParts().get(getCurrentIndex()));
                //tmpPartSrc=parts.get(currentIndex);
                mp = getTmpPartSrc().clone();
            }
            PubConst.es.submit(new Runnable() {
                @Override
                public void run() {
                    final MediaPartFactory mediaPartFactory = getMediaPartFactory();
                    if(mediaPartFactory!=null)mediaPartFactory.linkPart(mp);
                }
            });
        }
    }

    /**
     * 创建媒体片段发生器
     */
    MediaPartFactory createMediaPartFactory();
    

    private void prePlay(final List<MediaPart> prts){
        synchronized (synObject) {
            final MediaPartFactory mediaPartFactory = getMediaPartFactory();
            if (mediaPartFactory == null) {
                setMediaPartFactory(createMediaPartFactory());
                //mediaPartFactory = createMediaPartFactory();
                setOkRun( new Runnable() {
                    @Override
                    public void run() {
                        final MediaPartFactory mediaPartFactory1 = getMediaPartFactory();
                        if (mediaPartFactory1 != null) mediaPartFactory1.cutterClosed();
                    }
                });
            }
            PubConst.es.submit(new Runnable() {
                @Override
                public void run() {
                    setEnabled(false, false, true);
                    final MediaPartFactory mediaPartFactory1 = getMediaPartFactory();
                    if (mediaPartFactory1 != null)mediaPartFactory1.prePlayParts(prts);
                }
            });
        }
    }
    private void prePlayStop(){
        synchronized (synObject) {
            final MediaPartFactory mediaPartFactory = getMediaPartFactory();
            if (mediaPartFactory != null) mediaPartFactory.prePlayStop();
        }
    }

    //////////////////////    FFCutter           //////////////////////////

    /**
     *实现Media接口getHelp中的Help接口
     **/
    class MyHelp implements Help
    {
        final private String hlp_title;  //标题
        final private String hlp_Desc;  //描述
        final private Runnable hlp_Run;  //执行块
        final private MainFrame client;  //客户端
        public MyHelp(final MainFrame client, final String hlp_title, final String hlp_Desc, final  Runnable hlp_Run){
            this.client=client;
            this.hlp_title=hlp_title;
            this.hlp_Desc=hlp_Desc;
            this.hlp_Run=hlp_Run;
        }

        /**
         *@return 标题
         **/
        @Override
        public String getTitle(){
            return this.hlp_title;
        }
        /**
         *@return 描述
         **/
        @Override
        public String getDesc(){
            return this.hlp_Desc;
        }
        /**
         *实现Runnable
         **/
        @Override
        public void run(){
            if(this.hlp_Run==null){
                if( client!=null) {
                    StringBuilder sb = new StringBuilder().append(this.hlp_title )
                            .append( MediaProps.colon )
                            .append( PubConst.newLine )
                            .append( this.hlp_Desc);
                    client.display(sb.toString(), Display.DEBUG_TEXT);
                }
            }else{
                PubConst.es.submit(this.hlp_Run);
            }
        }
    }

    //获取帮助对象
    default Help getHelp(){
        return new MyHelp(getClient(), getTitle(), getDesc(), null);
    }

    //获取标题
    default String getTitle(){
        final List<FileMediaInfo> fileMediaInfos = getFileMediaInfos();
        final int index = getIndex();
        final String title = getSrcTitle();
        if(fileMediaInfos.isEmpty() || index<0){  //用于菜单
            if(getFromOne())return new StringBuilder().append(FFCutterProps.getTitleMsg[0]).append(title).toString();
            else return new StringBuilder().append(FFCutterProps.getTitleMsg[1]).append(title).toString();
        }
        return new StringBuilder().append(title).append(FFCutterProps.getTitleMsg[2]).append(fileMediaInfos.get(index).file.getAbsolutePath()).toString();
    }

    //开始时间输入组件
    default FFCTextField[] createBegins(){
        return create4FFCTextFieldsBegins();
    }
    /**
     * 界面实现，创建四个文本框
     * @return
     */
    FFCTextField[] create4FFCTextFieldsBegins();
    //结束时间输入组件
    default FFCTextField[] createEnds(){
        return create4FFCTextFieldsEnds();
    }
    /**
     * 界面实现，创建四个文本框
     * @return
     */
    FFCTextField[] create4FFCTextFieldsEnds();

    /**
     * 把结束数据传递到开始
     */
    default void passEnds2Begins(){
        MediaUtil.endsToBegins(getBegins(), getEnds());
    }

    /**
     * 获取开始时间
     */
    default long getBeginTime(){
        final FFCTextField[] begins = getBegins();
        return MediaUtil.getMediaPartTime( begins[0].getText(),  begins[1].getText(),  begins[2].getText(),  begins[3].getText());
    }

    /**
     * 获取结束时间
     */
    default long getEndTime(){
        final FFCTextField[] ends = getEnds();
        return MediaUtil.getMediaPartTime(ends[0].getText(), ends[1].getText(), ends[2].getText(), ends[3].getText());
    }

    /**
     * 更新标题
     */
    default void updateTitle(){
        updateTitleUI(getTitle());
    }
    void updateTitleUI(final String newTitle);

    default void loadMediaPart(final MediaPart mp ){   //装载片段内容
        MediaUtil.load(getBegins(), getEnds(), mp);
    }

}
