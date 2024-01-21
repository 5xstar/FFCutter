package com._5xstar.ffcutter;

import java.lang.annotation.*;

/**
 * 语言包，不同地区加载不同的语言包
 * 庞海文 庞海文  2024-1-18
 */
@Target(ElementType.TYPE)    //注解使用
@Documented                  //可文档
@Retention(RetentionPolicy.RUNTIME)  //运行时起作用
@Inherited  //可继承
public @interface Language {
    /**
     * 语言包文件
     * @return
     */
    String propsFile() default "zh-CN.properties";

    /**
     * 语言包字符集
     * @return
     */
    String charset() default "UTF-8";

    String _parasFile() default "parameters.properties";
}
