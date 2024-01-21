package com._5xstar.ffcutter.cutter.swing;

import com._5xstar.ffcutter.MainFrameProps;
import com._5xstar.ffcutter.constant.PubConst;
import com._5xstar.ffcutter.domains.ButtonAction;
import com._5xstar.ffcutter.domains.FileKeeper;
import com._5xstar.ffcutter.file.FFCFileFilter;
import com._5xstar.ffcutter.util.NetUtil;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 主窗口的swing实现
 * 庞海文 2024-1-24
 */
public class JMainFrameImpl extends JFrame implements JMainFrame{

    @Override
    public void openConfirmDialog(Object oFrame,
                                  String title,
                                  String demo,
                                  int timeoutSecond,
                                  List<ButtonAction> buttons,
                                  Runnable timeoutRun) {
        final JFrame frame = oFrame==null?this:(JFrame) oFrame;

        final JDialog dlg=new JDialog(frame,title==null? MainFrameProps.confirmDlgTitle:title,false);
        dlg.setSize(Const.confirmDlgWidth,Const.confirmDlgHeight);
        dlg.setResizable(false);
        dlg.setBackground(new Color(Const.confirmDlgBg_rgb[0], Const.confirmDlgBg_rgb[1], Const.confirmDlgBg_rgb[2]));
        JLabel  lblDemo=new JLabel(demo);
        dlg.setLayout(new GridBagLayout());
        GridBagConstraints gbc=new GridBagConstraints();
        gbc.fill=GridBagConstraints.HORIZONTAL;
        gbc.gridwidth=GridBagConstraints.REMAINDER;
        gbc.anchor=GridBagConstraints.NORTHWEST;
        gbc.weightx=1.0;
        gbc.insets=new Insets(5,5,5,5);

        //显示询问
        gbc.gridx=0;
        gbc.gridy=0;
        dlg.add(lblDemo,gbc);

        final boolean[] isActive=new boolean[]{true};

        if(buttons!=null && !buttons.isEmpty()){
            gbc.gridy=1;
            gbc.anchor=GridBagConstraints.SOUTH;
            JPanel btnPanel=new JPanel();
            dlg.add(btnPanel,gbc);
            btnPanel.setLayout(new FlowLayout());
            for(ButtonAction ba : buttons){
                JButton btn=new JButton(ba.caption==null?MainFrameProps.undefinedText:ba.caption);
                btnPanel.add(btn);
                btn.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        isActive[0]=false;
                        dlg.dispose();
                        if(ba.run!=null) PubConst.es.submit(ba.run);
                    }
                });
            }
        }
        //窗口关闭事件
        dlg.addWindowListener(
                new WindowAdapter(){
                    public void windowClosing(WindowEvent e){
                        isActive[0]=false;
                        dlg.dispose();
                        if(timeoutRun!=null)PubConst.es.submit(timeoutRun);
                    }
                });
        //Dimension d=dlg.getSize();
        //Dimension f=getSize();
        // int x=(displaySize.width-d.width)/2;
        // int y=(displaySize.height-d.height)/2;
        // dlg.setLocation(x,y);
        timeout(  isActive,   timeoutSecond,   dlg,   timeoutRun);
        dlg.setVisible(true);
        dlg.setLocationRelativeTo(frame);
    }

    /**
     *弹窗过时执行程序
     *@isActive[0] 保存dlg是否活动状态，false表示已经销毁
     *@timeoutSecond 过时等待秒数
     *@dlg 调用的弹窗对象
     *@timeoutRun 过时执行线程 可以null
     **/
    private void timeout(final boolean[] isActive, final int timeoutSecond, final JDialog dlg, final Runnable timeoutRun){
        //display("timeoutSecond1="+timeoutSecond+PubConst.newLine);  //测试
        PubConst.es.submit(new Runnable(){
            public void run(){
                if(timeoutSecond<=0){  //不能关闭的弹窗
                    dlg.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                    dlg.addWindowListener(new WindowAdapter(){
                        public void windowClosing(WindowEvent e){
                            openMsgDialog(3, MainFrameProps.timeOutMsg[0],  MainFrameProps.timeOutMsg[1]);
                        }
                    });
                }else{
                    dlg.addWindowListener(new WindowAdapter(){
                        public void windowClosing(WindowEvent e){
                            isActive[0]=false;
                            dlg.dispose();
                        }
                    });
                    //等待超时
                    try{Thread.sleep(timeoutSecond* 1000L);}catch(Exception e){}
                    if(!isActive[0])return;
                    //display("timeoutSecond2="+timeoutSecond);  //测试
                    dlg.dispose();
                    if(timeoutRun!=null)PubConst.es.submit(timeoutRun);
                    isActive[0]=false;
                }
            }
        });
    }

    @Override
    public void openFileSaveDialogUI(FileKeeper fKeeper,
                                     String title,
                                     List<FFCFileFilter> filters,
                                     int timeoutSecond,
                                     Runnable yesRun,
                                     Runnable cancelRun,
                                     Runnable timeoutRun,
                                     int allType) {
            //对话框
            final JDialog dlg=new JDialog(this, title, Dialog.ModalityType.MODELESS);
            final List<FileFilter> ff =  createFilters(filters, allType);
            //文件选择器
            final JFileChooser chooser=createJFileChooser(fKeeper, dlg, ff);
            chooser.setApproveButtonText(MainFrameProps.fileChooserSaveYesCaption);
            chooser.setDialogType(JFileChooser.SAVE_DIALOG);
            //加入打开执行
            final boolean[] isActive=new boolean[]{true};
            chooser.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    //display(e.getActionCommand()+"="+JFileChooser.APPROVE_SELECTION+PubConst.newLine);  //测试
                    if(e.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)){  //打开
                        //System.out.println("file="+fKeeper.file);
                        isActive[0]=false;
                        dlg.setVisible(false);
                        dlg.dispose();
                        fKeeper.file=chooser.getSelectedFile();
                        if(yesRun!=null)PubConst.es.submit(yesRun);
                    }else{                                                                                   //取消
                        isActive[0]=false;
                        dlg.setVisible(false);
                        dlg.dispose();
                        if(cancelRun!=null)PubConst.es.submit(cancelRun);
                    }
                }
            });
            //设置超时
            timeout(  isActive, timeoutSecond  ,   dlg,   timeoutRun);
            //显示
            dlg.setVisible(true);
            dlg.setLocationRelativeTo(this);

    }

    //Swing文件选择器
    private  static JFileChooser createJFileChooser(final FileKeeper fKeeper, final JDialog dlg, final List<FileFilter> filters){
        return createJFileChooser(  fKeeper,  dlg,  filters, false);
    }
    private static JFileChooser createJFileChooser(final FileKeeper fKeeper, final JDialog dlg, final List<FileFilter> filters, final boolean isMulti){
        //文件选择器
        JFileChooser chooser;
        if(fKeeper.dir==null)chooser=new JFileChooser();
        else chooser=new JFileChooser(fKeeper.dir);
        chooser.setMultiSelectionEnabled(isMulti);
        dlg.setSize(Const.fileChooserDlgWidth, Const.fileChooserDlgHeight);
        dlg.setResizable(false);
        dlg.setBackground(new Color(Const.fileChooserDlgBg_rgb[0], Const.fileChooserDlgBg_rgb[1], Const.fileChooserDlgBg_rgb[2]));
        dlg.setLayout(new GridBagLayout());
        GridBagConstraints gbc=new GridBagConstraints();
        gbc.weightx=1.0;
        gbc.weighty=1.0;
        gbc.fill=GridBagConstraints.BOTH;
        dlg.add(chooser,gbc);
        //Dimension d=dlg.getSize();
        //Dimension f=getSize();
        //int x=(displaySize.width-d.width)/2;
        //int y=(displaySize.height-d.height)/2;
        //dlg.setLocation(x,y);
        File af=fKeeper.file;
        if(af==null ){
            if(fKeeper.fullName!=null)af=new File(fKeeper.fullName);
            else if(fKeeper.shortName!=null){
                String str;
                if(fKeeper.extName==null)str=fKeeper.shortName;
                else str=fKeeper.shortName+"."+fKeeper.extName;
                if(fKeeper.dir==null)af=new File(str);
                else af=new File(fKeeper.dir,str);
            }
        }
        if(Const.saveDemoNewFile || (af!=null && af.exists() && af.isFile())){    //Mac 不能处理新文件，把saveDemoNewFile=false
            chooser.setSelectedFile(af);
        }
        //文件扩展名过滤器
        if(filters!=null && filters.size()>0){
            chooser.setAcceptAllFileFilterUsed(false);
            for(FileFilter filter : filters) chooser.addChoosableFileFilter(filter);
        }
        return  chooser;
    }
    private static List<FileFilter> createFilters(final List<FFCFileFilter> filters, final int allType){  //Swing界面中代替上面方
        System.out.println("allType="+allType);  //测试
        final List<FileFilter> lst = createFileFilters(  filters);
        switch(allType){
            case 1:
                lst.add(0, getNoFileFilter());
                break;
            case 2:
                lst.add(getNoFileFilter());
        }
        return lst;
    }
    /**
     *文件过滤器生产
     *@end 含点文件尾
     *@desc 过滤器描述
     **/
    private static List<FileFilter> createFileFilters(final List<FFCFileFilter> filters){
        if(filters==null)return new ArrayList<FileFilter>();
        final ArrayList<FileFilter> lst = new ArrayList<FileFilter>(filters.size());
        if(filters!=null)for(FFCFileFilter entry : filters)lst.add(new MyFileFilter(entry.name, entry.exts) );
        return lst;
    }
    private static class MyFileFilter extends FileFilter{
        final private String[]  ends;
        final private String desc;
        private MyFileFilter(final String name, final String[] ends){
            assert (name!=null && ends!=null && ends.length>0);
            this.ends = new String[ends.length];
            final StringBuilder sb = new StringBuilder();
            sb.append(name).append('(');
            String item = "."+ends[0].toLowerCase();
            this.ends[0]=item;
            sb.append('*').append(item);
            for(int i = 1;i<ends.length;i++){
                item = "."+ends[i].toLowerCase();
                this.ends[i]=item;
                sb.append(" *").append(item);
            }
            sb.append(')');
            this.desc=sb.toString();
        }
        @Override
        public boolean accept(File f){
            if(f.isDirectory())return true;
            for(int i=0;i<ends.length;i++)if(f.getName().toLowerCase().endsWith(ends[i]))return true;
            return false;
        }
        @Override
        public String getDescription(){return desc;}
    }
    //过滤二进制文件
    private static class WithoutBinFileFilter extends FileFilter{
        private static boolean filterNotOn = Const.binFileFilters==null || Const.binFileFilters.length==0;

        // Whether the given file is accepted by this filter.

        @Override
        public boolean accept(File f){
            if(filterNotOn || f.isDirectory())return true;
            String fileName=f.getName();
            int len=Const.binFileFilters.length;
            int idx=fileName.lastIndexOf(".");
            if(idx<0)return true;
            String sub=fileName.substring(idx+1);
            if(sub==null || "".equals(sub=sub.trim()))return true;
            for(int i=0;i<len;i++){
                if(sub.equalsIgnoreCase(Const.binFileFilters[i]))return false;
            }
            return true;
        }
        /**
         * The description of this filter. For example: "JPG and GIF Images"
         * @   see   FileViewn # getName
         **/
        @Override
        public String getDescription(){return MainFrameProps.withoutBinFileFilterDescription;}

    }

    /*
     * 不过滤的过滤器
     */
    private static FileFilter getNoFileFilter(){
        return new FileFilter(){
            @Override
            public boolean accept(File f){return true;}
            @Override
            public String getDescription(){return MainFrameProps.noFileFilterDescription;}
        };
    }


    @Override
    public void openFileSelectDialogUI(FileKeeper fKeeper,
                                       String title,
                                       List<FFCFileFilter> filters,
                                       boolean isMulti,
                                       int timeoutSecond,
                                       Runnable yesRun,
                                       Runnable cancelRun,
                                       Runnable timeoutRun,
                                       int allType) {
        //对话框
        final JDialog dlg=new JDialog(this, title, Dialog.ModalityType.MODELESS);
        final List<FileFilter> ff =  createFilters(filters,allType);
        //文件选择器
        final JFileChooser chooser=createJFileChooser(fKeeper, dlg, ff, isMulti);
        //加入打开执行
        final boolean[] isActive=new boolean[]{true};
        chooser.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(e.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)){  //打开
                    if(isMulti){
                        fKeeper.files = chooser.getSelectedFiles();
                        if(fKeeper.files!=null && fKeeper.files.length>0) fKeeper.file = fKeeper.files[0];
                    }else {
                        File f = chooser.getSelectedFile();
                        if (!f.isFile()) return;
                        if (fKeeper.maxLength > 0 && f.length() > fKeeper.maxLength) {
                            openMsgDialog(3, MainFrameProps.fileSelectMsg[0],
                                    MainFrameProps.fileSelectMsg[1] + fKeeper.maxLength + MainFrameProps.fileSelectMsg[2]);
                            return;
                        }
                        fKeeper.file = f;
                        fKeeper.files = new File[]{f};
                    }
                    isActive[0]=false;
                    dlg.dispose();
                    if(yesRun!=null)PubConst.es.submit(yesRun);
                }else{                                                                                   //取消
                    isActive[0]=false;
                    dlg.dispose();
                    if(cancelRun!=null)PubConst.es.submit(cancelRun);
                }
            }
        });
        //设置超时
        timeout(  isActive, timeoutSecond  ,   dlg,   timeoutRun);
        //显示
        dlg.setVisible(true);
        dlg.setLocationRelativeTo(this);

    }

    @Override
    public void openListDisplayDialog(Object oFrame,
                                      Object[] rtDlg,
                                      boolean[] isActive,
                                      Object component,
                                      String title,
                                      boolean modal,
                                      String demo,
                                      List<ButtonAction> buttons,
                                      Runnable timeoutRun,
                                      int timeoutSecond) {

        final JFrame frame = oFrame==null?this:(JFrame) oFrame;
        //对话框
        final JDialog dlg=new JDialog(frame, getDialogTitle(    title,     demo, MainFrameProps.listDisplayDlgTitle),modal);
        rtDlg[0]=dlg;
        dlg.setLayout(new GridBagLayout());
        dlg.setSize(Const.listDisplayDlgWidth, Const.listDisplayDlgHeight);
        dlg.setResizable(false);
        dlg.setBackground(new Color(Const.listDisplayDlgBg_rgb[0], Const.listDisplayDlgBg_rgb[1], Const.listDisplayDlgBg_rgb[2]));
        JList<?> lst=null;
        if(component instanceof  JList)lst=(JList<?>)component;
        else{
            openComponentDisplayDialog(frame,   (JDialog[])rtDlg, isActive, (Component)component, title, modal, demo, buttons, timeoutRun, timeoutSecond);
            return;
        }

        GridBagConstraints gbc=new GridBagConstraints();

        gbc.fill=GridBagConstraints.HORIZONTAL;
        gbc.gridwidth=GridBagConstraints.REMAINDER;
        gbc.anchor=GridBagConstraints.NORTHWEST;
        gbc.insets=new Insets(1,1,0,0);
        gbc.weightx=1.0;
        gbc.weighty=0.0;
        gbc.gridx=0;
        gbc.gridy=0;
        dlg.add(new JLabel(demo),gbc);


        gbc.fill=GridBagConstraints.BOTH;
        gbc.weighty=1.0;
        gbc.gridx=0;
        gbc.gridy=1;
        dlg.add(new JScrollPane(lst), gbc);

        if(buttons!=null && !buttons.isEmpty()){
            gbc.fill=GridBagConstraints.NONE;
            gbc.anchor=GridBagConstraints.SOUTH;
            gbc.weightx=0.0;
            gbc.weighty=0.0;
            gbc.gridwidth=1;
            gbc.gridy=2;
            JPanel btnPanel = new JPanel();
            dlg.add(btnPanel,gbc);
            btnPanel.setLayout(new FlowLayout());
            for(ButtonAction ba : buttons){
                JButton btn=new JButton(ba.caption==null?MainFrameProps.undefinedText:ba.caption);
                btnPanel.add(btn);
                btn.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        isActive[0]=false;
                        dlg.dispose();
                        if(ba.run!=null)PubConst.es.submit(ba.run);
                    }
                });
            }
        }

        //窗口关闭事件
        dlg.addWindowListener(
                new WindowAdapter(){
                    public void windowClosing(WindowEvent e){
                        isActive[0]=false;
                        dlg.dispose();
                        if(timeoutRun!=null)PubConst.es.submit(timeoutRun);
                    }
                });

        //Dimension d=dlg.getSize();
        //Dimension f=getSize();
        //int x=(displaySize.width-d.width)/2;
        // int y=(displaySize.height-d.height)/2;
        //dlg.setLocation(x,y);   //把对话框置于屏幕中央
        timeout(  isActive,   timeoutSecond,   dlg,  timeoutRun);

        dlg.setVisible(true);
        dlg.setLocationRelativeTo(frame);

    }

    private void openComponentDisplayDialog(final JFrame frame, final JDialog[] rtDlg,
                                            final boolean[] isActive,
                                            final Component  component,
                                            final String title,
                                            final boolean modal,
                                            final String demo,
                                            final List<ButtonAction> buttons,
                                            final Runnable timeoutRun,
                                            final int timeoutSecond){

        //对话框
        final JDialog dlg=rtDlg[0];
        GridBagConstraints gbc=new GridBagConstraints();

        gbc.fill=GridBagConstraints.HORIZONTAL;
        gbc.gridwidth=GridBagConstraints.REMAINDER;
        gbc.anchor=GridBagConstraints.NORTHWEST;
        gbc.insets=new Insets(1,1,0,0);
        gbc.weightx=1.0;
        gbc.weighty=0.0;
        gbc.gridx=0;
        gbc.gridy=0;
        dlg.add(new JLabel(demo),gbc);


        gbc.fill=GridBagConstraints.BOTH;
        gbc.weighty=1.0;
        gbc.gridx=0;
        gbc.gridy=1;
        dlg.add(new JScrollPane(component), gbc);

        if(buttons!=null && !buttons.isEmpty()){
            gbc.fill=GridBagConstraints.NONE;
            gbc.anchor=GridBagConstraints.SOUTH;
            gbc.weightx=0.0;
            gbc.weighty=0.0;
            gbc.gridwidth=1;
            gbc.gridy=2;
            JPanel btnPanel = new JPanel();
            dlg.add(btnPanel,gbc);
            btnPanel.setLayout(new FlowLayout());
            for(ButtonAction ba : buttons){
                JButton btn=new JButton(ba.caption==null?MainFrameProps.undefinedText:ba.caption);
                btnPanel.add(btn);
                btn.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        isActive[0]=false;
                        dlg.dispose();
                        if(ba.run!=null)PubConst.es.submit(ba.run);
                    }
                });
            }
        }

        //窗口关闭事件
        dlg.addWindowListener(
                new WindowAdapter(){
                    public void windowClosing(WindowEvent e){
                        isActive[0]=false;
                        dlg.dispose();
                        if(timeoutRun!=null)PubConst.es.submit(timeoutRun);
                    }
                });

        //Dimension d=dlg.getSize();
        //Dimension f=getSize();
        //int x=(displaySize.width-d.width)/2;
        // int y=(displaySize.height-d.height)/2;
        //dlg.setLocation(x,y);   //把对话框置于屏幕中央
        timeout(  isActive,   timeoutSecond,   dlg,  timeoutRun);

        dlg.setVisible(true);
        dlg.setLocationRelativeTo(frame);

    }

    @Override
    public JFrame getFrame() {
        return this;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Set the standard look and feel.
     * Metal
     * Nimbus
     * CDE/Motif
     * Windows
     * Windows Classic
     */
    private static String lookAndFeelName=JMainFrameImplStatus.Const.lookAndFeelName;
    protected static final void setLookAndFeel() {
        System.out.println("lookAndFeelName="+lookAndFeelName);
        if(lookAndFeelName==null)return;
        String lookAndFeelClassName = null;
        UIManager.LookAndFeelInfo[] lookAndFeelInfos = UIManager.getInstalledLookAndFeels();
        for(UIManager.LookAndFeelInfo lookAndFeel : lookAndFeelInfos) {
            if (lookAndFeelName.equals(lookAndFeel.getName())) {
                lookAndFeelClassName = lookAndFeel.getClassName();
                break;
            }
        }
        if (lookAndFeelClassName == null) {
            //lookAndFeelClassName = UIManager.getSystemLookAndFeelClassName();
            return;
        }
        try {
            System.out.println("init as "+ lookAndFeelName);
            UIManager.setLookAndFeel(lookAndFeelClassName);
        }
        catch(Exception e) {
            // Silently fail, it doesn't matter
        }
    }

    //日夜模式True为日模式
    private boolean dayType=JMainFrameImplStatus.Const.defaultDayType;
    private String title=JMainFrameImplProps.title;
    private int width=JMainFrameImplStatus.Const.width;
    private int height=JMainFrameImplStatus.Const.height;

    final private JButton btn=new JButton(JMainFrameImplProps.openText);
    final private MyJMenuItem nmItem=new MyJMenuItem(JMainFrameImplProps.openText);
    final private JMainFrameImpl f;

    //日夜模式
    private JButton btnChangeBG;
    private MyJMenuItem mniChangeBG;
    private final JDisplayImpl displayer;
    @Override
    public JDisplayImpl getDisplay(){
        return this.displayer;
    }


    /**
     *
     */
    public JMainFrameImpl(){
        super();
        this.f=this;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //关闭窗口，退出系统
        //图标
        setIconImage(getToolkit().getImage(JMainFrameImplStatus.Const.icon));
        //背景
        setBackground(dayType?JMainFrameImplStatus.Const.background_day:JMainFrameImplStatus.Const.background_night);
        //标题
        setTitle(title);
        final Dimension displaySize = Toolkit.getDefaultToolkit().getScreenSize(); // 获得显示器大小对象
        int _width=width>displaySize.width?displaySize.width:width;
        int _height=height>displaySize.height?displaySize.height:height;
        //窗口大小
        setSize(_width,_height);

        //显示器
        displayer= new JDisplayImpl(this, dayType);
        final  JPopupMenu pmnMyDisplay=new JPopupMenu(JMainFrameImplProps.displayPopMenuTitle);
        final List<MyJMenuItem> dpItems = displayer.getMenuItems();
        if(dpItems!=null){
            for(MyJMenuItem item : dpItems)pmnMyDisplay.add(item);
        }
        this.displayer.getContainer().addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getButton()==MouseEvent.BUTTON3){
                    //弹出菜单
                    pmnMyDisplay.show(displayer.getContainer(),e.getX(),e.getY());
                }
            }
        });
        this.displayer.getTxtContainer().addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getButton()==MouseEvent.BUTTON3){
                    //弹出菜单
                    pmnMyDisplay.show(displayer.getTxtContainer(),e.getX(),e.getY());
                }
            }
        });

        //生成日夜按钮
        btnChangeBG=new JButton(dayType?JMainFrameImplProps.btnChangeBGCaption_night:JMainFrameImplProps.btnChangeBGCaption_day);
        btnChangeBG.setToolTipText(btnChangeBG.getText());
        mniChangeBG=new MyJMenuItem(dayType?JMainFrameImplProps.btnChangeBGCaption_night:JMainFrameImplProps.btnChangeBGCaption_day);

        //布局
        final GridBagLayout gbl=new GridBagLayout();
        setLayout(gbl);

        //顶部按钮
        JPanel pnl_Top2 = new JPanel(new GridBagLayout());
        add(pnl_Top2);

        //中间
        JPanel pnl_Center = new JPanel(new GridBagLayout());
        add(pnl_Center);

        //底部
        JPanel pnl_Bottom3=new JPanel(new GridLayout());
        add(pnl_Bottom3);

        //弹出菜单
        final  JPopupMenu pmn=new JPopupMenu(JMainFrameImplProps.popupMenuText);
        addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getButton()==MouseEvent.BUTTON3){
                    //弹出菜单
                    pmn.show(f,e.getX(),e.getY());
                }
            }
        });

        //菜单栏
        final JMenuBar mnb=new JMenuBar();
        setJMenuBar(mnb);
        //菜单
        final JMenu mn = new JMenu();
        mn.setText(JMainFrameImplProps.openText);
        mn.add(nmItem);  //加入菜单
        pmn.add(nmItem.clone());  //加入弹出菜单
        //加入监听器
        ActionListener listener=new ActionListener(){
            public  void actionPerformed(ActionEvent e3){
                open();
            }
        };
        btn.addActionListener(listener);
        nmItem.addActionListener(listener);

        final JMenu mnLayout=new JMenu(JMainFrameImplProps.loaoutText);
        mnLayout.add(mniChangeBG);
        mnb.add(mnLayout);
        pmn.add(mniChangeBG.clone());
        listener=new ActionListener(){
            public  void actionPerformed(ActionEvent e3){
                if("Nimbus".equals(lookAndFeelName)){
                    openMsgDialog(3,JMainFrameImplProps.Nimbus[0], JMainFrameImplProps.Nimbus[1]);
                    return;
                }
                dayType = !dayType;
                initDayType(false);
                setVisible(true);
            }};
        btnChangeBG.addActionListener(listener);
        mniChangeBG.addActionListener(listener);

        //顶部按钮内部排版
        GridBagConstraints gbc=new GridBagConstraints();
        gbc.fill=GridBagConstraints.HORIZONTAL;
        gbc.insets=new Insets(0,1,0,0);
        gbc.weightx=1.0;
        pnl_Top2.add(btnChangeBG,gbc);

        //底部内部排版
        pnl_Bottom3.add(btn, gbc);

        //中间内部排版
        gbc.fill=GridBagConstraints.BOTH;
        gbc.anchor=GridBagConstraints.CENTER;
        gbc.weighty=1.0;
        gbc.weightx=1.0;
        pnl_Center.add(displayer.getContainer(),gbc);

        //顶部
        gbc.fill=GridBagConstraints.HORIZONTAL;
        gbc.anchor=GridBagConstraints.NORTHWEST;
        gbc.gridwidth=GridBagConstraints.REMAINDER;
        gbc.insets=new Insets(1,1,0,0);
        gbc.weightx=1.0;
        gbc.weighty=0.0;
        gbc.gridy=0;
        gbl.setConstraints(pnl_Top2,gbc);

        //中间
        gbc.fill=GridBagConstraints.BOTH;
        gbc.weightx=1.0;
        gbc.weighty=1.0;
        gbc.gridy=1;
        gbl.setConstraints(pnl_Center, gbc);

        //底部
        gbc.fill=GridBagConstraints.HORIZONTAL;
        gbc.anchor=GridBagConstraints.SOUTHWEST;
        gbc.weighty=0.0;
        gbc.gridy=2;
        gbl.setConstraints(pnl_Bottom3,gbc);

        //联系方式
        final JMenu mn_Contact=new JMenu(JMainFrameImplProps.contact);
        final JMenuItem item1= new JMenuItem(JMainFrameImplProps.contactItems[0]);
        listener=new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NetUtil.browse("https://5xstar.com");
            }
        };
        item1.addActionListener(listener);
        mn_Contact.add(item1);
        final JMenuItem item2= new JMenuItem(JMainFrameImplProps.contactItems[1]);
        item2.addActionListener(listener);
        mn_Contact.add(item2);
        final JMenuItem item3= new JMenuItem(JMainFrameImplProps.contactItems[2]);
        item3.addActionListener(listener);
        mn_Contact.add(item3);
        final JMenuItem item4= new JMenuItem(JMainFrameImplProps.contactItems[3]);
        item4.addActionListener(listener);
        mn_Contact.add(item4);
        final JMenuItem item5= new JMenuItem(JMainFrameImplProps.contactItems[4]);
        item5.addActionListener(listener);
        mn_Contact.add(item5);
        mnb.add(mn_Contact);

        //显示窗口
        setVisible(true);
        // 设置窗口居中显示器显示
        setLocationRelativeTo(null);
        //日夜模式
        initDayType(true);
        setLookAndFeel();
    }

    //界面类型转换
    private void initDayType( final boolean isInit){
        if(isInit)return;
        //背景
        setBackground(dayType?JMainFrameImplStatus.Const.background_day:JMainFrameImplStatus.Const.background_night);
        displayer.changeBackground(dayType);
        if(dayType){
            btnChangeBG.setLabel(JMainFrameImplProps.btnChangeBGCaption_night);
            btnChangeBG.setToolTipText(JMainFrameImplProps.btnChangeBGCaption_night);
            mniChangeBG.setText(JMainFrameImplProps.btnChangeBGCaption_night);
        }else{
            btnChangeBG.setLabel(JMainFrameImplProps.btnChangeBGCaption_day);
            btnChangeBG.setToolTipText(JMainFrameImplProps.btnChangeBGCaption_day);
            mniChangeBG.setText(JMainFrameImplProps.btnChangeBGCaption_day);
        }
    }
    //打开
    private void open(){
        new JFFCutter(this);
    }

}
