/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.tncrazvan.asciitable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class AsciiRow {
    private final HashMap<String,Integer> options;
    private final HashMap<Integer,HashMap<String,Integer>> styles;
    private final HashMap<Integer,AsciiCel> cels;
    private int width=0,height;
    public AsciiRow(AsciiCel[] cels,HashMap<String,Integer> options,HashMap<Integer,HashMap<String,Integer>> styles) {
        this.options = options;
        this.styles = styles;
        this.cels = new HashMap<>();
        for(int i=0;i<cels.length;i++){
            this.cels.put(i, cels[i]);
        }
        this.resolveHeight();
        this.resolveWidth();
    }
    
    private void resolveHeight(){
        int result = 0;
        int height = 0;
        Iterator it = this.cels.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            ((AsciiCel)pair.getValue()).resolve();
        }
        it = this.cels.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            height = ((AsciiCel)pair.getValue()).getHeight();
            if(height > result)
                result = height;
        }
        this.height = result;
    }
    
    private void resolveWidth(){
        this.width = 0;
        int numberOfCels = this.cels.size();
        for(int j=0;j<numberOfCels;j++){
            this.width += this.cels.get(j).getWidth();
        }
    }
    
    public int getHeight(){
        return this.height;
    }
    
    public int getWidth(){
        return this.width;
    }
    
    public void extendCelBy(int index, int width){
        this.cels.get(index).increaseWidth(width);
    }
    
    public int getNumberOfCels(){
        return this.cels.size();
    }
    
    public AsciiCel getCel(int index){
        if(!this.cels.containsKey(index)){
            return new AsciiCel("",this.options);
        }
        return this.cels.get(index);
    }
    
    public String[] getLines(){
        String[] lines;
        String wholeLine;
        HashMap<Integer,String> wholeLines = new HashMap<>();
        int numberOfCels = this.cels.size();
        int numberOfLines;
        AsciiCel cel;
        String original;
        for(int j=0;j<numberOfCels;j++){
            cel = this.cels.get(j);
            original = cel.getOriginalString();
            if(original.equals("CCCCC\n22222")){
                System.out.println("stop");
            }
            numberOfLines = cel.getLines().length;
            if(numberOfLines < this.height){
                this.cels.put(j, new AsciiCel(
                    original+
                    "\n".repeat(this.height-numberOfLines),
                    this.cels.get(j).getOptions()
                ));
            }
        }
        
        for(int j=0;j<numberOfCels;j++){
            cel = this.cels.get(j);
            lines = cel.getLines();
            for(int i=0;i<this.height;i++){
                if(!wholeLines.containsKey(i)) wholeLines.put(i, "");
                wholeLine = wholeLines.get(i);
                if(i < lines.length)
                    wholeLines.put(i, wholeLine+(wholeLine.equals("")?lines[i]:lines[i].substring(1)));
            }
        }
        ArrayList<Integer> keys = new ArrayList(wholeLines.keySet());
        Collections.sort(keys);
        ArrayList<String> result = new ArrayList<String>();
        for(Integer key : keys){
            result.add(wholeLines.get(key));
        }
        return result.toArray(new String[] {});
    }
    
    @Override
    public String toString(){
        return String.join("\n", this.getLines());
    }
}
