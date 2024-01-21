package com._5xstar.ffcutter.player.swing;


import com._5xstar.ffcutter.Configure;
import com._5xstar.ffcutter.util.StringUtil;
import java.util.Properties;

/**
 * EqualizerFrame语言包
 * 庞海文 2024-1-20
 */
public class EqualizerFrameProps implements Configure {

    public static String efTitle = "均衡器";
    public static String foreGrade="前级";
    public static String enableCaption="应用";
    public static String presetCaption = "预设均衡：";
    public static String selectText="--选择--";


        /**
         * @confFileAbsolutePath 配置文件全路径
         * @prop 配置值
         **/
        public void conf( final Properties prop) {
            efTitle = StringUtil.parse(prop, "efTitle", efTitle);
            foreGrade = StringUtil.parse(prop, "foreGrade", foreGrade);
            enableCaption = StringUtil.parse(prop, "enableCaption", enableCaption);
            presetCaption = StringUtil.parse(prop, "presetCaption", presetCaption);
            selectText = StringUtil.parse(prop, "selectText", selectText);
        }

}
