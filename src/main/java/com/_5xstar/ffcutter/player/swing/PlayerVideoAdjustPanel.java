package com._5xstar.ffcutter.player.swing;

import com._5xstar.ffcutter.MainFrame;
import com._5xstar.ffcutter.domains.ButtonAction;
import uk.co.caprica.vlcj.player.base.LibVlcConst;
import uk.co.caprica.vlcj.player.base.MediaPlayer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * 调整器
 * 庞海文 2024-1-20改
 */
public class PlayerVideoAdjustPanel extends JPanel{

    private final MediaPlayer mediaPlayer;

    private JCheckBox enableVideoAdjustCheckBox;

    private JLabel contrastLabel;
    private JSlider contrastSlider;

    private JLabel brightnessLabel;
    private JSlider brightnessSlider;

    private JLabel hueLabel;
    private JSlider hueSlider;

    private JLabel saturationLabel;
    private JSlider saturationSlider;

    private JLabel gammaLabel;
    private JSlider gammaSlider;

    public PlayerVideoAdjustPanel(final MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
        createUI();
    }
    //打开对话框
    final void openDialog(final MainFrame client, final JFrame frame){
         if(client==null){
             if(frame==null)return;
             final JDialog dlg=new JDialog(frame, PlayerVideoAdjustPanelProps.dialogTitle,false);
             dlg.add(this);           
             dlg.pack();
             dlg.setVisible(true);
             dlg.setLocationRelativeTo(frame);           
             return;
         }
         final boolean[] isActive=new  boolean[]{true};
         final JDialog[] dlg=new JDialog[]{null};     
         //不操作       
         final ButtonAction cancel = new  ButtonAction(); 
         cancel.caption=PlayerVideoAdjustPanelProps.cancelCaption;
         cancel.run=new Runnable(){
             public void run(){               
                isActive[0]=false;
                if(dlg[0]!=null)dlg[0].dispose();
             }
         };              
         final ArrayList<ButtonAction>  btns = new ArrayList<ButtonAction>(1); 
         btns.add(cancel);
         client.openListDisplayDialog(frame, dlg, isActive, this, PlayerVideoAdjustPanelProps.dialogTitle,
                  false, PlayerVideoAdjustPanelProps.dialogDemo, btns, cancel.run, 0);
     }

    private void createUI() {
        createControls();
        layoutControls();
        registerListeners();
    }

    private void createControls() {
        enableVideoAdjustCheckBox = new JCheckBox(PlayerVideoAdjustPanelProps.enableCaption);

        contrastLabel = new JLabel(PlayerVideoAdjustPanelProps.contrastText);
        contrastSlider = new JSlider();
        contrastSlider.setOrientation(JSlider.HORIZONTAL);
        contrastSlider.setMinimum(Math.round(LibVlcConst.MIN_CONTRAST * 100.0f));
        contrastSlider.setMaximum(Math.round(LibVlcConst.MAX_CONTRAST * 100.0f));
        contrastSlider.setPreferredSize(new Dimension(100, 40));
        contrastSlider.setToolTipText(PlayerVideoAdjustPanelProps.contrastTipText);
        contrastSlider.setEnabled(false);
        contrastSlider.setPaintLabels(true);
        contrastSlider.setPaintTicks(true);

        brightnessLabel = new JLabel(PlayerVideoAdjustPanelProps.brightnessText);
        brightnessSlider = new JSlider();
        brightnessSlider.setOrientation(JSlider.HORIZONTAL);
        brightnessSlider.setMinimum(Math.round(LibVlcConst.MIN_BRIGHTNESS * 100.0f));
        brightnessSlider.setMaximum(Math.round(LibVlcConst.MAX_BRIGHTNESS * 100.0f));
        brightnessSlider.setPreferredSize(new Dimension(100, 40));
        brightnessSlider.setToolTipText(PlayerVideoAdjustPanelProps.brightnessTipText);
        brightnessSlider.setEnabled(false);

        hueLabel = new JLabel(PlayerVideoAdjustPanelProps.hueText);
        hueSlider = new JSlider();
        hueSlider.setOrientation(JSlider.HORIZONTAL);
        hueSlider.setMinimum(Math.round(LibVlcConst.MIN_HUE * 100.0f));
        hueSlider.setMaximum(Math.round(LibVlcConst.MAX_HUE * 100.0f));
        hueSlider.setPreferredSize(new Dimension(100, 40));
        hueSlider.setToolTipText(PlayerVideoAdjustPanelProps.hueTipText);
        hueSlider.setEnabled(false);

        saturationLabel = new JLabel(PlayerVideoAdjustPanelProps.saturationText);
        saturationSlider = new JSlider();
        saturationSlider.setOrientation(JSlider.HORIZONTAL);
        saturationSlider.setMinimum(Math.round(LibVlcConst.MIN_SATURATION * 100.0f));
        saturationSlider.setMaximum(Math.round(LibVlcConst.MAX_SATURATION * 100.0f));
        saturationSlider.setPreferredSize(new Dimension(100, 40));
        saturationSlider.setToolTipText(PlayerVideoAdjustPanelProps.saturationTipText);
        saturationSlider.setEnabled(false);

        gammaLabel = new JLabel(PlayerVideoAdjustPanelProps.gammaText);
        gammaSlider = new JSlider();
        gammaSlider.setOrientation(JSlider.HORIZONTAL);
        gammaSlider.setMinimum(Math.round(LibVlcConst.MIN_GAMMA * 100.0f));
        gammaSlider.setMaximum(Math.round(LibVlcConst.MAX_GAMMA * 100.0f));
        gammaSlider.setPreferredSize(new Dimension(100, 40));
        gammaSlider.setToolTipText(PlayerVideoAdjustPanelProps.gammaTipText);
        gammaSlider.setEnabled(false);

        contrastSlider.setValue(Math.round(mediaPlayer.video().brightness() * 100.0f));
        brightnessSlider.setValue(Math.round(mediaPlayer.video().contrast() * 100.0f));
        hueSlider.setValue(Math.round(mediaPlayer.video().hue() * 100.0f));
        saturationSlider.setValue(Math.round(mediaPlayer.video().saturation() * 100.0f));
        gammaSlider.setValue(Math.round(mediaPlayer.video().gamma() * 100.0f));
    }

    private void layoutControls() {
        setBorder(new EmptyBorder(4, 4, 4, 4));

        setLayout(new BorderLayout());

        JPanel slidersPanel = new JPanel();
        slidersPanel.setLayout(new BoxLayout(slidersPanel, BoxLayout.Y_AXIS));
        slidersPanel.add(enableVideoAdjustCheckBox);
        slidersPanel.add(contrastLabel);
        slidersPanel.add(contrastSlider);
        slidersPanel.add(brightnessLabel);
        slidersPanel.add(brightnessSlider);
        slidersPanel.add(hueLabel);
        slidersPanel.add(hueSlider);
        slidersPanel.add(saturationLabel);
        slidersPanel.add(saturationSlider);
        slidersPanel.add(gammaLabel);
        slidersPanel.add(gammaSlider);

        add(slidersPanel, BorderLayout.CENTER);
    }

    private void registerListeners() {
        enableVideoAdjustCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean enabled = enableVideoAdjustCheckBox.isSelected();
                contrastSlider.setEnabled(enabled);
                brightnessSlider.setEnabled(enabled);
                hueSlider.setEnabled(enabled);
                saturationSlider.setEnabled(enabled);
                gammaSlider.setEnabled(enabled);
                mediaPlayer.video().setAdjustVideo(enabled);
            }
        });

        contrastSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider)e.getSource();
                // if(!source.getValueIsAdjusting()) {
                mediaPlayer.video().setContrast(source.getValue() / 100.0f);
                // }
            }
        });

        brightnessSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider)e.getSource();
                // if(!source.getValueIsAdjusting()) {
                mediaPlayer.video().setBrightness(source.getValue() / 100.0f);
                // }
            }
        });

        hueSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider)e.getSource();
                // if(!source.getValueIsAdjusting()) {
                mediaPlayer.video().setHue(source.getValue() / 100.0f);
                // }
            }
        });

        saturationSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider)e.getSource();
                // if(!source.getValueIsAdjusting()) {
                mediaPlayer.video().setSaturation(source.getValue() / 100.0f);
                // }
            }
        });

        gammaSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider)e.getSource();
                // if(!source.getValueIsAdjusting()) {
                mediaPlayer.video().setGamma(source.getValue() / 100.0f);
                // }
            }
        });
    }
}
