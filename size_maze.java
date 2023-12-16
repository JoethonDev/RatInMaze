/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.maze;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class size_maze {
    private JFrame frame;
    private JTextField textField;
    private JButton submitButton;
    private JLabel label;
    private JTextField size;
    public static int mazeSize;
//    private JTextField n_columns;
//    public static int rows = 0;
//    public static int columns = 0;
    public size_maze() {
        // Create the frame
        frame = new JFrame("Maze");
//        location(500 , 200);
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        label = new JLabel("Size Maze");
        frame.add(label);
        // Create the text field
        size = new JTextField("", 20);
        frame.add(size);

//        label = new JLabel("column");
//        frame.add(label);
//        // Create the text field
//        n_columns = new JTextField("", 20);
//        frame.add(n_columns);

        // Create the submit button
        submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Perform an action when the button is clicked
                String sizeText = size.getText();
//                String col_text = n_columns.getText();
                mazeSize = Integer.parseInt(sizeText);
//                columns = Integer.parseInt(col_text);
                Maze maze = new Maze(mazeSize);

                // Close the current size_maze window
                frame.dispose();

            }
        });
                frame.add(submitButton);
                frame.setVisible(true);
    }
    public static void main(String[]args){
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run(){
                new size_maze();
                
            }
        });
    }
}
