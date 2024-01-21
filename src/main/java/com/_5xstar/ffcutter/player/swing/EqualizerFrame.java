package com._5xstar.ffcutter.player.swing;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.Equalizer;
import uk.co.caprica.vlcj.player.base.LibVlcConst;
import uk.co.caprica.vlcj.player.base.MediaPlayer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

/**
 * 均衡器
 * 庞海文 2024-1-20改
 */
public class EqualizerFrame extends JFrame implements ChangeListener, ActionListener, ItemListener {

    private static final String BAND_INDEX_PROPERTY = "equalizerBandIndex";

    private final String dbFormat = "%.2fdB";

    private final MediaPlayerFactory mediaPlayerFactory;
    private final MediaPlayer mediaPlayer;
    private final Equalizer equalizer;

    private final SliderControl preampControl;
    private final SliderControl[] bandControls;

    private final JToggleButton enableButton;
    private final JComboBox presetComboBox;


    public EqualizerFrame(List<Float> list, List<String> presets, MediaPlayerFactory mediaPlayerFactory, MediaPlayer mediaPlayer, Equalizer equalizer) {
        super(EqualizerFrameProps.efTitle);

        this.mediaPlayerFactory = mediaPlayerFactory;
        this.mediaPlayer = mediaPlayer;
        this.equalizer = equalizer;

        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setAlwaysOnTop(true);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(BorderFactory.createEmptyBorder(4,  4,  4,  4));
        contentPane.setLayout(new BorderLayout(0, 4));

        JPanel bandsPane = new JPanel();
        bandsPane.setLayout(new GridLayout(1, 1 + list.size(), 2, 0));

        preampControl = new SliderControl(EqualizerFrameProps.foreGrade, (int)LibVlcConst.MIN_GAIN, (int)LibVlcConst.MAX_GAIN, 0, dbFormat);
        preampControl.getSlider().addChangeListener(this);
        bandsPane.add(preampControl);

        bandControls = new SliderControl[list.size()];
        for(int i = 0; i < list.size(); i++) {
            bandControls[i] = new SliderControl(formatFrequency(list.get(i)), (int)LibVlcConst.MIN_GAIN, (int)LibVlcConst.MAX_GAIN, 0, dbFormat);
            bandControls[i].getSlider().putClientProperty(BAND_INDEX_PROPERTY, i);
            bandControls[i].getSlider().addChangeListener(this);
            bandsPane.add(bandControls[i]);
        }

        contentPane.add(bandsPane, BorderLayout.CENTER);

        JPanel controlsPane = new JPanel();
        controlsPane.setLayout(new BoxLayout(controlsPane, BoxLayout.X_AXIS));

        enableButton = new JToggleButton(EqualizerFrameProps.enableCaption);
        enableButton.setMnemonic('e');
        controlsPane.add(enableButton);

        controlsPane.add(Box.createHorizontalGlue());

        JLabel presetLabel = new JLabel(EqualizerFrameProps.presetCaption);
        presetLabel.setDisplayedMnemonic('p');
        controlsPane.add(presetLabel);

        presetComboBox = new JComboBox();
        presetLabel.setLabelFor(presetComboBox);
        DefaultComboBoxModel presetModel = (DefaultComboBoxModel)presetComboBox.getModel();
        presetModel.addElement(null);
        for(String presetName : presets) {
            presetModel.addElement(presetName);
        }
        presetComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if(value != null) {
                    label.setText(String.valueOf(value));
                }
                else {
                    label.setText(EqualizerFrameProps.selectText);
                }
                return label;
            }
        });
        controlsPane.add(presetComboBox);

        contentPane.add(controlsPane, BorderLayout.SOUTH);

        setContentPane(contentPane);
        
        pack();

        enableButton.addActionListener(this);
        presetComboBox.addItemListener(this);
    }

    private String formatFrequency(float hz) {
        if(hz < 1000.0f) {
            return String.format("%.0f Hz", hz);
        }
        else {
            return String.format("%.0f kHz", hz / 1000f);
        }
    }

    @Override
    public final void actionPerformed(ActionEvent e) {
        boolean enable = enableButton.isSelected();
        if(!enable) {
            presetComboBox.setSelectedItem(null);
        }
        mediaPlayer.audio().setEqualizer(enable ? equalizer : null);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if(e.getSource() instanceof JSlider slider) {

            Integer index = (Integer)slider.getClientProperty(BAND_INDEX_PROPERTY);
            int value = slider.getValue();
            // Band...
            if(index != null) {
                //System.out.println(value);
                equalizer.setAmp(index, value / 100f);
            }
            // Preamp...
            else {
                equalizer.setPreamp(value / 100f);
            }

            if(!applyingPreset) {
                presetComboBox.setSelectedItem(null);
            }
        }
    }

    boolean applyingPreset;

    @Override
    public final void itemStateChanged(ItemEvent e) {
        String presetName = (String)presetComboBox.getSelectedItem();
        if(e.getStateChange() == ItemEvent.SELECTED) {
            if(presetName != null) {
                Equalizer presetEqualizer = mediaPlayerFactory.equalizer().newEqualizer(presetName);
                if(presetEqualizer != null) {
                    applyingPreset = true;
                    preampControl.getSlider().setValue((int)(presetEqualizer.preamp() * 100f));
                    float[] amps = presetEqualizer.amps();
                    for(int i = 0; i < amps.length; i++) {
                        bandControls[i].getSlider().setValue((int)(amps[i] * 100f));
                    }

                    applyingPreset = false;
                }
            }
        }
    }
}
