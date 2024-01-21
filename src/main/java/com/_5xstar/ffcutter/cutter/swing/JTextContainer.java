package com._5xstar.ffcutter.cutter.swing;

import javax.swing.*;
import java.util.List;

/**
 * swing文本容器
 * 庞海文 2024-1-21
 */
public interface JTextContainer {
    /**
     * 文本容器
     * @return
     */
    JComponent getTxtContainer();

    /**
     * 按钮
     */
    List<JButton> getButtons();

    /**
     * 菜单
     */
    List<MyJMenuItem> getMenuItems();

}
