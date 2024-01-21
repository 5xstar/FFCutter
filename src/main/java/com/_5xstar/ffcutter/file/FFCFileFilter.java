package com._5xstar.ffcutter.file;

import com._5xstar.ffcutter.domains.Keeper;

public class FFCFileFilter implements Keeper {
    //名称或描述
    final public String name;
    //后缀数组，不含点，小写
    final public String[] exts;
    public FFCFileFilter(final String name, final String[] exts){
        this.name=name;
        this.exts=exts;
    }
}
