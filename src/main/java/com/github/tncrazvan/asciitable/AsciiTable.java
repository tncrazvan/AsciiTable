/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.tncrazvan.asciitable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Administrator
 */
public class AsciiTable {
    public static void main(String[] args) {
        
        AsciiTable table2 = new AsciiTable(new HashMap<>(){{
            put("padding", 1);
        }});
        table2.add("hello2","world2");
        table2.add("hello2","world2");
        String t2 = table2.toString();
        
        AsciiTable table = new AsciiTable(new HashMap<>(){{
            put("padding", 3);
        }});
        table.add("AAAAA","BBBBB");
        
        table.add("hello",t2);
        //table.add("CCCCC","DDDDD\n22222");
        System.err.println(table.toString());
    }
    
    
    private final HashMap<String,Integer> options;
    private final HashMap<Integer,HashMap<String,Integer>> styles;
    private ArrayList<AsciiRow> rows = new ArrayList<>();
    private int width;
    private AsciiRow masterRow;
    private int numberOfCols = 0;
    
    public AsciiTable(){
        this.styles = new HashMap<>();
        this.options= new HashMap<>();
    }
    
    public AsciiTable(HashMap<String,Integer> options){
        this.styles = new HashMap<>();
        this.options=options;
    
    }
    
    public static AsciiTable resolve(String[] input,boolean countLines,HashMap<String,Integer> options){
        AsciiTable table = new AsciiTable(options);
        table.add("Name","Value");
        for(int i=0;i<input.length;i++){
            table.add(String.valueOf(i),input[i]);
        }
        return table;
    }
    
    public void style(int index, HashMap<String,Integer> options){
        this.styles.put(index,options);
    }
    
    public int getWidth(){
        return this.width;
    }
    
    public void add(String... inputCels){
        ArrayList<AsciiCel> cels = new ArrayList<>();
        for(int i=0;i<inputCels.length;i++){
            cels.add(new AsciiCel(inputCels[i],this.styles.containsKey(i)?this.styles.get(i):this.options));
        }
        add(cels.toArray(new AsciiCel[]{}));
    }
    
    public void add(AsciiCel... cels){
        AsciiRow row = new AsciiRow(cels,this.options,this.styles);
        rows.add(row);
        this.toString();
    }
    
    @Override
    public String toString(){
        return toString(false);
    }
    public String toString(boolean countLines){
        this.findRowWithMostCels();
        this.fixWidths();
        String result="";
        int numberOfRows = this.rows.size();
        for(int i=0;i<numberOfRows;i++){
            if(i>0)
                result += this.rows.get(i).toString().replaceAll("^.+\\n", "")+(i+1 == numberOfRows?"":"\n");
            else{
                result += this.rows.get(i).toString()+(i+1 == numberOfRows?"":"\n");
                this.width = result.length();
            }
        }
        
        if(countLines){
            String[] tmp = result.split("\\n",-1);
            int length = tmp.length;
            result = "";
            int rowNumber = 1;
            for(int i=0;i<length;i++){
                if(tmp[i].charAt(0) != '|'){
                    rowNumber = 1;
                    continue;
                }
                tmp[i] = tmp[i]+" <= ["+rowNumber+"]";
                rowNumber++;
            }
        }
        
        return result;
    }
    
    public void findRowWithMostCels(){
        int length = this.rows.size();
        int num = 0;
        for(int i=0;i<length;i++){
            num = this.rows.get(i).getNumberOfCels();
            if(num > this.numberOfCols){
                this.numberOfCols = num;
                this.masterRow = this.rows.get(i);
            }
        }
    }
    
    private void fixWidths(){
        numberOfCols = this.numberOfCols;
        AsciiCel widestCel,cel;
        int width, widestCelWidth, numberOfRows;
        
        for(int i=0;i<numberOfCols;i++){
            widestCel = this.getWidestCelByIndex(i);
            widestCelWidth = widestCel.getWidth();
            numberOfRows = this.rows.size();
            for(int j=0;j<numberOfRows;j++){
                cel = this.rows.get(j).getCel(i);
                width = widestCelWidth - cel.getWidth();
                if(width > 0){
                    this.rows.get(j).extendCelBy(i, width);
                }
            }
        }
    }

    private AsciiCel getWidestCelByIndex(int index) {
        int length = this.rows.size();
        AsciiCel cel = null;
        for(int i=0;i<length;i++){
            if(cel == null || cel.getWidth() < this.rows.get(i).getCel(index).getWidth()){
                cel = this.rows.get(i).getCel(index);
            }
        }
        return cel;
    }
    
}
