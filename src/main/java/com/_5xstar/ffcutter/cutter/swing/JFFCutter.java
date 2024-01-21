package com._5xstar.ffcutter.cutter.swing;


import com._5xstar.ffcutter.MediaPartFactory;
import com._5xstar.ffcutter.cutter.FFCutterAbstractImpl;
import com._5xstar.ffcutter.cutter.FFCutterUIProps;
import com._5xstar.ffcutter.domains.FileMediaInfo;
import com._5xstar.ffcutter.domains.MediaPart;
import com._5xstar.ffcutter.player.swing.VLCJJFrameAWTCanvas;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;

public class JFFCutter extends FFCutterAbstractImpl implements JFFCutterStatus {

    //对话窗口
    private JDialog dlg;
    //媒体片段列表
    final private JList<MediaPart> lst=new JList<>(getParts());
    private boolean isUIAction=false;
    //文件选择器
    private JComboBox<FileMediaInfo> cb;
    private boolean isFileChooserUIAction=false;
    //编辑执行按钮
    final private JButton btnYes=new JButton(FFCutterUIProps.btnMediaPartYesCaption);
    //预览三按钮
    private JButton btnPreViewCurrent;
    private JButton btnPreView;
    private JButton btnPreViewStop;
    private JFFCTextField[] begins;
    private JFFCTextField[] ends;

    /**
     * 界面实现，创建四个文本框
     * @return
     */
    @Override
    public JFFCTextField[] create4FFCTextFieldsBegins() {
        this.begins=new JFFCTextField[]{createJFFCTextField(2),
                                        createJFFCTextField(2),
                                        createJFFCTextField(2),
                                        createJFFCTextField(2)};
        return this.begins;
    }
    /**
     * 界面实现，创建四个文本框
     * @return
     */
    @Override
    public JFFCTextField[] create4FFCTextFieldsEnds() {
        this.ends=new JFFCTextField[]{createJFFCTextField(2),
                                      createJFFCTextField(2),
                                      createJFFCTextField(2),
                                      createJFFCTextField(2)};
        return this.ends;
    }
    private JFFCTextField createJFFCTextField(final int col){
        final JFFCTextField tf = new JFFCTextField(col);
        tf.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                clearTextOnGetFocus(tf);
            }
            @Override
            public void focusLost(FocusEvent e) {
                //do nothing
            }
        });
        return tf;
    }
    /**
     * 更新标题
     */
    @Override
    public void updateTitleUI(String newTitle) {
        dlg.setTitle(newTitle);
    }

    /**
     * 重置媒体片段选择器
     */
    @Override
    public void resetMediaPartsChooserUI() {
        lst.updateUI();
    }

    /**
     * 重置文件选择器
     */
    @Override
    public void resetFileChooserUI() {
        isFileChooserUIAction=true;
        cb.setSelectedIndex(0);
    }

    /**
     * 更新文件选择器UI，阻止事件发生
     */
    @Override
    public void updateFileChooserUI() {
        isFileChooserUIAction=true;
        cb.setSelectedIndex(getIndex());
    }
    @Override
    public void addItem(final FileMediaInfo fmi){
        cb.addItem(fmi);
    }

    private void initFileChooser(){
        this.cb = new JComboBox<FileMediaInfo>();
        final java.util.List<FileMediaInfo>  fileMediaInfos = getFileMediaInfos();
        for(int i=0; i<fileMediaInfos.size(); i++){
            cb.addItem(fileMediaInfos.get(i));
        }
        cb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(isFileChooserUIAction){
                     isFileChooserUIAction=false;
                }else {
                    fileChooserChange((FileMediaInfo) cb.getSelectedItem());
                }
            }
        });
    }

    /**
     * 媒体片段更新
     */
    @Override
    public void updateMediaPartsChooserUI() {
        isUIAction=true;  //阻止处理事件
        lst.setSelectedIndex(getCurrentIndex());
        lst.updateUI();
    }

    @Override
    public void dispose() {
        dlg.dispose();
    }

    private void initMediaPartsChooser(){
        // 一个片段被点
        lst.addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent e){
                if(isUIAction){isUIAction=false;return;}
                partsChange(lst.getMaxSelectionIndex());
            }
        });
    }

    /**
     * 更新yes按钮
     * @param caption
     */
    @Override
    public void updateYesCaption(String caption) {
           btnYes.setText(caption);
    }

    /**
     * 改变预览按钮状态
     * @param preViewCurrent
     * @param preView
     * @param preViewStop
     */
    @Override
    public void setEnabled(final boolean preViewCurrent, final boolean preView, final boolean preViewStop ) {
        btnPreViewCurrent.setEnabled(preViewCurrent);
        btnPreView.setEnabled(preView);
        btnPreViewStop.setEnabled(preViewStop);
    }
    private void initPreView(){
        this.btnPreViewCurrent=new JButton(FFCutterUIProps.btnPreViewCurrentCaption);
        this.btnPreView=new JButton(FFCutterUIProps.btnPreViewCaption);
        this.btnPreViewStop=new JButton(FFCutterUIProps.btnPreViewStopCaption);
        //当前
        btnPreViewCurrent.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                preViewCurrent();
            }
        });
        //全部预览
        btnPreView.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                preViewAll();
            }
        });
        //不再预览，在播放媒体是无法关闭，只能用户自己关闭
        btnPreViewStop.setEnabled(false);
        btnPreViewStop.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                preViewStop();
            }
        });
    }

    /**
     *实现数据准备，
     **/
    @Override
    public void tempFilesAndParasPrepare(final Runnable execRun){
        //构建对话框，使用工作区，不弹出对话框
        //final JFrame client.getFrame() = ((JMainFrame)getClient()).getFrame();  //初始化是用JMainFrame
        //对话框
        this.dlg=new JDialog(client.getFrame(), getTitle(),false);
        //对话框布局
        //布局
        final GridBagLayout gbl=new GridBagLayout();
        dlg.setLayout(gbl);
        //对话框大小
        dlg.setSize(Const.mediaPartDlgWidth,Const.mediaPartDlgHeight);
        dlg.setResizable(false);
        //对话框背景
        dlg.setBackground(Const.mediaPartDlgBg);
        //排版器
        GridBagConstraints gbc=new GridBagConstraints();
        gbc.fill=GridBagConstraints.HORIZONTAL;
        gbc.anchor=GridBagConstraints.NORTHWEST;
        gbc.insets=new Insets(1,1,0,0);
        gbc.weighty=0.0;
        gbc.weightx=1.0;
        //
        gbc.gridx=0;
        gbc.gridy=1;
        JPanel pnlFun=new JPanel();
        dlg.add(pnlFun,gbc);

        //初始化列表
        initMediaPartsChooser();
        //列表区
        gbc.gridy=2;
        gbc.weighty=1.0;
        gbc.fill=GridBagConstraints.BOTH;
        dlg.add(new JScrollPane(lst), gbc);
        //操作
        gbc.weighty=0.0;
        gbc.gridy=3;
        gbc.fill=GridBagConstraints.HORIZONTAL;
        gbc.anchor=GridBagConstraints.SOUTH;
        JPanel pnlWork0=new JPanel();
        dlg.add(pnlWork0, gbc);
        gbc.gridy=4;
        JPanel pnlWork1=new JPanel();
        dlg.add(pnlWork1, gbc);
        gbc.gridy=5;
        JPanel pnlWork2=new JPanel();
        dlg.add(pnlWork2, gbc);
        gbc.gridy=6;
        JPanel pnlWork3=new JPanel();
        dlg.add(pnlWork3, gbc);
        gbc.gridy=7;
        JPanel pnlWork4=new JPanel();
        dlg.add(pnlWork4, gbc);




        //
        gbc.fill=GridBagConstraints.HORIZONTAL;
        gbc.anchor=GridBagConstraints.NORTHWEST;
        gbc.weighty=0.0;
        gbc.weightx=0.0;
        gbc.gridy=0;

        int x=0;

        //添加按钮
        gbc.gridx=x++;
        JButton btnAdd=new JButton(FFCutterUIProps.btnAddCaption);
        pnlFun.add(btnAdd, gbc);
        btnAdd.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                add();
            }
        });

        //插入
        gbc.gridx=x++;
        JButton btnInsert=new JButton(FFCutterUIProps.btnInsertCaption);
        pnlFun.add(btnInsert, gbc);
        btnInsert.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                insert();
            }
        });

        //更新
        gbc.gridx=x++;
        JButton btnUpdate=new JButton(FFCutterUIProps.btnUpdateCaption);
        pnlFun.add(btnUpdate, gbc);
        btnUpdate.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                update();
            }
        });

        //删除
        gbc.gridx=x++;
        JButton btnDel=new JButton(FFCutterUIProps.btnDelCaption);
        pnlFun.add(btnDel, gbc);
        btnDel.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                remove();
            }
        });

        //上移动
        gbc.gridx=x++;
        JButton btnUp=new JButton(FFCutterUIProps.btnUpCaption);
        pnlFun.add(btnUp, gbc);
        btnUp.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                up();
            }
        });

        //下移动
        gbc.gridx=x++;
        JButton btnDown=new JButton(FFCutterUIProps.btnDownCaption);
        pnlFun.add(btnDown, gbc);
        btnDown.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                down();
            }
        });

        //初始化预览按钮
        initPreView();

        //当前预览
        gbc.gridx=x++;
        pnlFun.add(btnPreViewCurrent, gbc);

        //全部预览
        gbc.gridx=x++;
        pnlFun.add(btnPreView, gbc);


        //不再预览，在播放媒体是无法关闭，只能用户自己关闭
        gbc.gridx=x++;
        pnlFun.add(btnPreViewStop, gbc);

        //完成按钮
        gbc.gridx=x++;
        JButton btnFinished=new JButton(FFCutterUIProps.btnFinishedCaption);
        pnlFun.add(btnFinished, gbc);
        btnFinished.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                cut(execRun);
            }
        });

        //完成按钮2
        gbc.gridx=x++;
        JButton btnFinished2=new JButton(FFCutterUIProps.btnFinishedCaption2);
        pnlFun.add(btnFinished2, gbc);
        btnFinished2.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                onlyCut(execRun);
            }
        });

        //重新按钮
        gbc.gridx=x++;
        JButton btnReset=new JButton(FFCutterUIProps.btnResetCaption);
        pnlFun.add(btnReset, gbc);
        btnReset.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                resetAction();
            }
        });

        //
        gbc=new GridBagConstraints();
        gbc.fill=GridBagConstraints.NONE;
        gbc.anchor=GridBagConstraints.NORTHWEST;



        //初始化文件选择器
        initFileChooser();

        //媒体文件及信息
        gbc.gridy=0;
        gbc.gridx=0;
        pnlWork0.add(cb,gbc);

        //播放器
        JButton btnLinkMediaPartFactory=new JButton(FFCutterUIProps.btnLinkMediaPartFactoryCaption);
        gbc.gridx=1;
        pnlWork0.add(btnLinkMediaPartFactory,gbc);
        btnLinkMediaPartFactory.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                linkMediaPartFactory();
            }
        });

        x=0;
        //媒体开始
        //gbc.gridy=1;
        gbc.gridx=x++;
        pnlWork1.add(new JLabel(FFCutterUIProps.mediaPartBeginLabel), gbc);

        gbc.gridx=x++;
        pnlWork1.add(this.begins[0], gbc);
        gbc.gridx=x++;
        pnlWork1.add(new JLabel(FFCutterUIProps.mediaPartHourLabel), gbc);

        gbc.gridx=x++;
        pnlWork1.add(this.begins[1], gbc);
        gbc.gridx=x++;
        pnlWork1.add(new JLabel(FFCutterUIProps.mediaPartMinuteLabel), gbc);

        gbc.gridx=x++;
        pnlWork1.add(this.begins[2], gbc);
        gbc.gridx=x++;
        pnlWork1.add(new JLabel(FFCutterUIProps.mediaPartSecondLabel), gbc);

        gbc.gridx=x++;
        pnlWork1.add(this.begins[3], gbc);
        gbc.gridx=x;
        pnlWork1.add(new JLabel(FFCutterUIProps.mediaPartPersentSecondLabel), gbc);

        //媒体结束
        x=0;
        //gbc.gridy=2;
        gbc.gridx=x++;
        pnlWork2.add(new JLabel(FFCutterUIProps.mediaPartEndLabel), gbc);

        gbc.gridx=x++;
        pnlWork2.add(this.ends[0], gbc);
        gbc.gridx=x++;
        pnlWork2.add(new JLabel(FFCutterUIProps.mediaPartHourLabel), gbc);

        gbc.gridx=x++;
        pnlWork2.add(this.ends[1], gbc);
        gbc.gridx=x++;
        pnlWork2.add(new JLabel(FFCutterUIProps.mediaPartMinuteLabel), gbc);

        gbc.gridx=x++;
        pnlWork2.add(this.ends[2], gbc);
        gbc.gridx=x++;
        pnlWork2.add(new JLabel(FFCutterUIProps.mediaPartSecondLabel), gbc);

        gbc.gridx=x++;
        pnlWork2.add(this.ends[3], gbc);
        gbc.gridx=x;
        pnlWork2.add(new JLabel(FFCutterUIProps.mediaPartPersentSecondLabel), gbc);

        //控制按钮
        //gbc.gridy=3;
        gbc.gridx=0;
        JPanel bp=new JPanel();
        gbc.gridwidth=GridBagConstraints.REMAINDER;
        pnlWork3.add(bp, gbc);
        bp.add(btnYes);
        btnYes.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                yes();
            }
        });
        //刷新
        JButton btnRefresh=new JButton(FFCutterUIProps.btnMediaPartRefreshCaption);
        bp.add(btnRefresh);
        btnRefresh.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                refresh();
            }
        });

        //gbc.gridy=4;
        pnlWork4.add(new JLabel(FFCutterUIProps.mediaPartDemoLabel), gbc);

        dlg.addWindowListener(
                new WindowAdapter(){
                    public void windowClosing(WindowEvent e){
                        final MediaPartFactory mediaPartFactory = getMediaPartFactory();
                        if(mediaPartFactory!=null)mediaPartFactory.cutterClosed();
                    }
                });


        Dimension d=dlg.getSize();
        Dimension f=client.getFrame().getSize();
        int px=(f.width-d.width)/2;
        int py=(f.height-d.height)/2;
        dlg.setLocation(px,py);   //把对话框置于屏幕中央

        dlg.setVisible(true);

    }
    @Override
    public MediaPartFactory createMediaPartFactory(){
        return  new VLCJJFrameAWTCanvas(client, this);
    }

    final private JMainFrame client;

    /**
     * 构造函数
     */
    public JFFCutter(JMainFrame client){
        super(client);
        this.client = client;
        try{
            exec();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

}
