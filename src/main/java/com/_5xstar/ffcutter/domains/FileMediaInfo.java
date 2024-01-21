package com._5xstar.ffcutter.domains;



import com._5xstar.ffcutter.util.MediaUtil;

import java.io.File;

public class FileMediaInfo implements Keeper {
    final public int index;
    final public File file;
    final public MediaInfo mediaInfo;
    public FileMediaInfo(final   int index, final  File file, final   MediaInfo mediaInfo ){
        this.index=index;
        this.file=file;
        this.mediaInfo=mediaInfo;
    }
    @Override
    public String toString(){
        return MediaUtil.buildFileMediaInfo(this);
    }
}
