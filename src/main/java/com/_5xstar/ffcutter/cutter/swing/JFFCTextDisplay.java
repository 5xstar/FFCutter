package com._5xstar.ffcutter.cutter.swing;

import com._5xstar.ffcutter.Configure;
import com._5xstar.ffcutter.constant.PubConst;
import com._5xstar.ffcutter.util.StringUtil;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.Properties;

/**
 *JTextArea代码编辑文本框 ，实现修饰功能     
 *庞海文 2021-2-14
**/
public class JFFCTextDisplay extends JTextPane implements  Configure
{

    //双击最大时间间隔
    private static final long doubleClickedMaxInterv=500L;

     //简单文本显示器行列大小
    private static int txtDisplayRow=10;
    private static int txtDisplayColumn=30;
    private static Color txtDisplayBg_day=new Color(225,225,225);
    private static Color txtDisplayFg_day=new Color(0x202020);
    private static Color txtDisplayBg_night=new Color(0x202020);
    private static Color txtDisplayFg_night=new Color(225,225,225);
    private static Color selectionColor=Color.blue;
    private static Color selectedTextColor=Color.white;
    private static Color selfColor_day=new Color(200,255,200);
    private static Color selfColor_night=new Color(0,55,0);

    //配置
        //配置
        public void conf( final Properties prop){
            txtDisplayRow=StringUtil.parse(prop, "txtDisplayRow", txtDisplayRow);
            txtDisplayColumn=StringUtil.parse(prop, "txtDisplayColumn", txtDisplayColumn);
            txtDisplayBg_day=StringUtil.parse(prop, "txtDisplayBg_day", txtDisplayBg_day);
            txtDisplayFg_day=StringUtil.parse(prop, "txtDisplayFg_day", txtDisplayFg_day);
            txtDisplayBg_night=StringUtil.parse(prop, "txtDisplayBg_night", txtDisplayBg_night);
            txtDisplayFg_night=StringUtil.parse(prop, "txtDisplayFg_night", txtDisplayFg_night);
            selectionColor=StringUtil.parse(prop, "selectionColor", selectionColor);
            selectedTextColor=StringUtil.parse(prop, "selectedTextColor", selectedTextColor);
            selfColor_day=StringUtil.parse(prop, "selfColor_day", selfColor_day);
            selfColor_night=StringUtil.parse(prop, "selfColor_night", selfColor_night);

        }
    //配置构造函数
    public JFFCTextDisplay(){}
    //自己消息高亮
    final private HighlightAdapter adapter_day=new HighlightAdapter<JFFCTextDisplay>(selfColor_day);
    final private HighlightAdapter adapter_night=new HighlightAdapter<JFFCTextDisplay>(selfColor_night);


    //AttributeSet
    final private SimpleAttributeSet defaultAS=new SimpleAttributeSet();  //默认
    private JMainFrame client;  //客户端
    private JFFCTextDisplay myself;  //对象自己
    private HighlightAdapter adapter=null;
    public JFFCTextDisplay(final JMainFrame client, final boolean dayType) {
        super();  
        this.client=client;
        this.myself=this;
        changeBackground1(dayType);
        this.setFont(JFFCTextDisplayProps.txtDisplayFont);
        this.setSelectionColor(selectionColor);
        this.setSelectedTextColor(selectedTextColor);
        this.setEditable(false);
        if(JFFCTextDisplayProps.txtDisplayDefaultText!=null)this.setText(JFFCTextDisplayProps.txtDisplayDefaultText);
    }



    /**
      * 改变底色
    **/
    public void changeBackground(final boolean dayType){
          changeBackground1(dayType);
          setText(getText());  //冲销文字设置
          try{adapterUpdate();}catch(Exception e){
              e.printStackTrace();
          }
    }
    private void changeBackground1(final boolean dayType){
        if(dayType){
            this.setBackground(txtDisplayBg_day);
            StyleConstants.setForeground(this.defaultAS, txtDisplayFg_day); 
            this.setCaretColor(txtDisplayFg_day);
            adapter=adapter_day;
        }else{ 
            this.setBackground(txtDisplayBg_night);                           
            StyleConstants.setForeground(this.defaultAS, txtDisplayFg_night);
            this.setCaretColor(txtDisplayFg_night);
            adapter=adapter_night;
        }        
        this.setCharacterAttributes(this.defaultAS,true);   
    }




    /**
     * Determines the offset of the start of the given line.
     *
     * @param line  the line number to translate &gt;= 0
     * @return the offset &gt;= 0
     * @exception BadLocationException thrown if the line is
     * less than zero or greater or equal to the number of
     * lines contained in the document (as reported by
     * getLineCount).
     */
    public int getLineStartOffset(int line) throws BadLocationException {
        int lineCount = getLineCount();
        if (line < 0) {
            throw new BadLocationException("Negative line", -1);
        } else if (line >= lineCount) {
            throw new BadLocationException("No such line", getDocument().getLength()+1);
        } else {
            Element map = getDocument().getDefaultRootElement();
            Element lineElem = map.getElement(line);
            return lineElem.getStartOffset();
        }
    }

    /**
     * Determines the offset of the end of the given line.
     *
     * @param line  the line &gt;= 0
     * @return the offset &gt;= 0
     * @exception BadLocationException Thrown if the line is
     * less than zero or greater or equal to the number of
     * lines contained in the document (as reported by
     * getLineCount).
     */
    public int getLineEndOffset(int line) throws BadLocationException {
        int lineCount = getLineCount();
        if (line < 0) {
            throw new BadLocationException("Negative line", -1);
        } else if (line >= lineCount) {
            throw new BadLocationException("No such line", getDocument().getLength()+1);
        } else {
            Element map = getDocument().getDefaultRootElement();
            Element lineElem = map.getElement(line);
            int endOffset = lineElem.getEndOffset();
            // hide the implicit break at the end of the document
            return ((line == lineCount - 1) ? (endOffset - 1) : endOffset);
        }
    }

    /**
     * Determines the number of lines contained in the area.
     *
     * @return the number of lines &gt; 0
     */
    public int getLineCount() {
        Element map = getDocument().getDefaultRootElement();
        return map.getElementCount();
    }

    /**
     * Appends the given text to the end of the document.  Does nothing if
     * the model is null or the string is null or empty.
     *                     
     * @param str the text to insert
     * @see # insert
     */
    public void append(String str) {
        Document doc = getDocument();
        if (doc != null) {
            try {             
                   doc.insertString(doc.getLength(), str, defaultAS);
                   if(str.endsWith(PubConst.newLine))adapterUpdate();
            } catch (BadLocationException e) {
                   e.printStackTrace();
            }
        }
    }

    /**
     *覆盖这个方法,使不自动折行,如果不够大,背景不满,需要把背景颜色与上级背景相同. 默认值是true
    **/
    @Override
    public boolean getScrollableTracksViewportWidth() {
        return false;
    }

    /**
     *高亮自己的消息
    **/
    public void adapterUpdate()throws BadLocationException{
         final String text=getText();
         if(text==null)return;
         final int line=getLineCount();
         if(line==0)return;
         final String selfHead="System"; //client.getUserService().getSelfName();
         if(selfHead==null)return;
         final int length=selfHead.length();
         final char[] sh=new char[length];
         selfHead.getChars(0,length,sh,0);
         adapter.removeAllAdapters(this);
         for(int i=0;i<line;i++){
             adapterUpdate(text,sh,length, i);
         }
         setCaretPosition(text.length());
    }
    private void adapterUpdate(final String text, final char[] sh, final int length, final int lineNum)throws BadLocationException{
          final int start=getLineStartOffset(lineNum);    
          final int end=getLineEndOffset(lineNum);
          if(end-start<length)return;
          if(check(text,sh,length,start)){
               adapter.adapter(this, text, start, end, null);
          }     
    }
   private boolean check(final String text, final char[] sh, final int length,final int start){
          if(start+length>text.length())return false;
          for(int i=0;i<length;i++){
              if(text.charAt(start+i)!=sh[i])return false;
          }
          return true;
   }

}


