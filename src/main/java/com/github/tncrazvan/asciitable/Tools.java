/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.tncrazvan.asciitable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 *
 * @author Administrator
 */
public class Tools {
    public static <T> Stream<T> flatten(T[][] arrays) 
    { 
  
        // Create an empty list to collect the stream 
        List<T> list = new ArrayList<>(); 
  
        // Using forEach loop 
        // convert the array into stream 
        // and add the stream into list 
        for (T[] array : arrays) { 
            Arrays.stream(array) 
                .forEach(list::add); 
        } 
  
        // Convert the list into Stream and return it 
        return list.stream(); 
    } 
}
