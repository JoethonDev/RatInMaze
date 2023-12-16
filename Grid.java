/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.maze;

import java.awt.Color;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.JButton;

/**
 *
 * @author Joeth
 */
public class Grid extends JButton {
    private long value = 0;
    private String text = "0";
    private ReentrantLock lock = new ReentrantLock();
    
    public void Grid(){
       this.setText(text);
    }
    
    public void makeWall(){
        this.value = -1;
        this.text = "-1";
        this.setText(text);
        this.setBackground(Color.GRAY);
        
    }
    
    public long getValue(){
        return this.value;
    }
    
    public void visit(Color color){
        if(this.acquireGrid()){
            if (this.value == 0){
                long id = Thread.currentThread().getId();
                this.value = id;
                this.text = String.valueOf(id);
                this.setText(text);
                this.setBackground(color);
            }
            this.releaseGrid();
        }
        

        
    }
    
    public boolean isValidGrid(){
        return this.value == 0;
    }
    
    public boolean acquireGrid(){
        return lock.tryLock();
    }
    
    public void releaseGrid(){
        lock.unlock();
    }
}
