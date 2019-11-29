/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.tncrazvan.asciitable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class AsciiCel {

    private final String originalString;
    private final ArrayList<String> data = new ArrayList<>();
    private int width = 0, height;
    private String top,bottom,empty;
    private final HashMap<String,Integer> options;
    
    public HashMap<String,Integer> getOptions(){
        return this.options;
    }
    public AsciiCel(String data,HashMap<String,Integer> options) {
        this.options = new HashMap<String,Integer>(){{
            put("width", 100);
            put("padding-left", 1);
            put("padding-right", 1);
            put("padding-top", 0);
            put("padding-bottom", 0);
            put("padding-between-lines-top", 0);
            put("padding-between-lines-bottom", 0);
        }};
        
        data = data.replaceAll("\\t", " ".repeat(4));
        this.originalString = data;
        Iterator it = options.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            this.options.put((String)pair.getKey(), (Integer) pair.getValue());
        }
        this.parseOptions();
        String[] lines = data.split("\\n",-1);
        ArrayList<String> tmp = new ArrayList<>();
        
        for(int i=0, end = lines.length-1;i<=end;i++){
            if(lines[i].length() > this.options.get("width")) {
                for(String line : this.fit(lines[i])){
                    tmp.add(line);
                }
            }else{
                tmp.add(lines[i]);
            }
        }
        
        lines = tmp.toArray(new String[]{});
        
        int length = 0;
        for(int i=0;i<lines.length;i++){
            length = lines[i].length();
            this.data.add(lines[i]);
            if(this.width < length)
                this.width = length;
        }
    }
    
    private String[] fit(String text){
        int size = this.options.get("width");
        List<String> parts = new ArrayList<>();

        int length = text.length();
        for (int i = 0; i < length; i += size) {
            parts.add(text.substring(i, Math.min(length, i + size)));
        }
        return parts.toArray(new String[0]);
    }
    
    public int getHeight(){
        return this.height;
    }
    
    public int getWidth(){
        return this.width;
    }
    
    public void setWidth(int width){
        this.width = width;
    }
    
    public void increaseWidth(int width){
        this.width += width;
    }
    
    public void decreaseWidth(int width){
        this.width -= width;
    }
    
    public String[] getLines(){
        return this.resolve();
    }
    
    public void parseOptions(){
        Iterator it = this.options.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            switch((String)pair.getKey()){
                case "padding":
                    this.options.put("padding-left", (Integer)pair.getValue());
                    this.options.put("padding-right", (Integer)pair.getValue());
                    this.options.put("padding-top", (Integer)pair.getValue());
                    this.options.put("padding-bottom", (Integer)pair.getValue());
                    break;
                case "padding-between-lines":
                    this.options.put("padding-between-lines-left", (Integer)pair.getValue());
                    this.options.put("padding-between-lines-right", (Integer)pair.getValue());
                    this.options.put("padding-between-lines-top", (Integer)pair.getValue());
                    this.options.put("padding-between-lines-bottom", (Integer)pair.getValue());
                    break;
            }
        }
    }
    
    public String getOriginalString(){
        return this.originalString;
    }
    
    public String[] resolve(){
        ArrayList<String> tmp = new ArrayList<>();
        int length = this.data.size();
        int dataLen;
        
        for(int i=0;i<length;i++){
            dataLen = this.data.get(i).length();
            if(this.width < dataLen)
                this.width = dataLen;
        }
        
        this.top = "-".repeat(this.width);
        this.bottom = "-".repeat(this.width);
        this.empty = " ".repeat(this.width);
     
        int paddingTop = this.options.get("padding-top");
        int paddingBottom = this.options.get("padding-bottom");
        int paddingBetweenLinesTop = this.options.get("padding-between-lines-top");
        int paddingBetweenLinesBottom = this.options.get("padding-between-lines-bottom");
        this.insertLineInTmp(this.top,tmp,"+",true,true);
        for(int j=0;j<paddingTop;j++){
            this.insertLineInTmp(this.empty,tmp,"|",true,true);
        }
        for(int i=0;i<length;i++){
            for(int j=0;j<paddingBetweenLinesTop;j++){
                this.insertLineInTmp(this.empty,tmp,"|",true,true);
            }
            this.insertLineInTmp(this.data.get(i),tmp);
            for(int j=0;j<paddingBetweenLinesBottom;j++){
                this.insertLineInTmp(this.empty,tmp,"|",true,true);
            }
        }
        for(int j=0;j<paddingBottom;j++){
            this.insertLineInTmp(this.empty,tmp,"|",true,true);
        }
        this.insertLineInTmp(this.bottom,tmp,"+",true,true);
        return tmp.toArray(new String[]{});
    }
    
    public void insertLineInTmp(String data, ArrayList<String> tmp){
        insertLineInTmp(data, tmp, "|");
    }
    
    public void insertLineInTmp(String data, ArrayList<String> tmp, String sideString){
        insertLineInTmp(data, tmp, sideString, false);
    }
    
    public void insertLineInTmp(String data, ArrayList<String> tmp, String sideString,boolean  extendFirstCharacter){
        insertLineInTmp(data, tmp, sideString, extendFirstCharacter, false);
    }
    public void insertLineInTmp(String data, ArrayList<String> tmp, String sideString,boolean  extendFirstCharacter, boolean  extendRightCharacter){
        if(data.matches("\\n")){
            String[] split = data.split("\\n",-1);
            for(String extraRowData : split){
                this.insertLineInTmp(extraRowData, tmp, sideString, extendFirstCharacter, extendRightCharacter);
            }
            return;
        }
        int paddingLeftCounter = this.options.get("padding-left");
        String paddingLeft = "";
        if(data.length() > 0 && extendFirstCharacter){
            paddingLeft = new String(new char[]{data.charAt(0)}).repeat(paddingLeftCounter);
        }else {
            paddingLeft = " ".repeat(paddingLeftCounter);
        }
        
        int paddingRightCounter = this.options.get("padding-right");
        String paddingRight = "";
        if(data.length() > 0 && extendFirstCharacter){
            paddingRight = new String(new char[]{data.charAt(0)}).repeat(paddingRightCounter);
        }else {
            paddingRight = " ".repeat(paddingRightCounter);
        }
        
        int len = data.length();
        
        if(len > this.width)
            this.width = len;
        else if(len < this.width)
            data += " ".repeat(this.width-len);
        
        tmp.add(sideString+paddingLeft+data+paddingRight+sideString);
        this.height++;
    }
}
