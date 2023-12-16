/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.maze;

import java.awt.Color;
import static java.awt.Color.gray;
import static java.awt.Color.white;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.awt.font.TextAttribute.FONT;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 *
 * @author MR.GN
 */
public class Maze extends JFrame{
    private JFrame frame;
    private Grid[][] buttons;
    private JButton start;
    private int[][] mazeState;
    private JLabel label;
    private JPanel outerPanel;
//    private JTextField path;
    private JTextArea path;
    public final int[] END;
    public final int[] START = {0,0};
    public static int[][] pathValues = null;
    public int size;
    public ArrayList<Thread> threads;
    private final Object colorLock = new Object();
    public int colorSelection = 0;
    public String pathStr = "";
    public Color[] colorPallete = {
        Color.BLUE, Color.ORANGE, 
        Color.PINK, Color.MAGENTA,
        Color.DARK_GRAY, Color.CYAN
    };
    public static volatile boolean condition = false;
    
    
    public Maze(int size){
        this.threads = new ArrayList<>();
        this.size = size;
        this.pathValues = new int[size*2][2];
        this.END = new int[]{size-1, size-1};
        frame = new JFrame("Maze");
        frame.setTitle("Maze Solver");
        frame.setSize(800 , 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel outerPanel = new JPanel();
//        outerPanel.setLocation(100, 500);
//        outerPanel.setBackground(gray);
        outerPanel.setLayout(new FlowLayout());
        outerPanel.setSize(900 , 900);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(size , size , 3 , 3));
//        panel.setSize(2700, 300);
        buttons = new Grid[size][size];
//        mazeState = new int[rows][columns];
        for(int i=0; i<buttons.length; i++){
            for(int j=0; j<buttons[i].length; j++){
                buttons[i][j] = new Grid();
                buttons[i][j].setBackground(Color.WHITE);
                buttons[i][j].setPreferredSize(new Dimension(50 , 50));
                if((i==0 && j==0) || (i==size-1 && j == size-1)){}
                else{
                    buttons[i][j].addActionListener(new ButtonClickListener(i,j));
                }
                panel.add(buttons[i][j]);
                
            }
        }
       
        buttons[0][0].setBackground(Color.YELLOW);
        buttons[0][0].setText("S");
        buttons[size-1][size-1].setBackground(Color.YELLOW);
        buttons[size-1][size-1].setText("E");

        
        JButton start = new JButton("Start");
//      set label as word(path) & change its size 
        label = new JLabel("Path : ");
        label.setFont(new Font("Arial", Font.PLAIN, 20));

        // set value of path 
//        String value = "0  5\n0  6\n1  7";
        path = new JTextArea("");
        // to can not me change the value
        path.setEditable(false);
        path.setFont(new Font("Arial", Font.PLAIN, 20));

        path.setPreferredSize(new Dimension(150, 500));
        outerPanel.add(start);
        outerPanel.add(label);
        outerPanel.add(path);

        start.addActionListener(new ButtonStart());
        outerPanel.add(panel);
        frame.add(outerPanel);
        frame.setLayout(null);
        frame.setVisible(true);
        label.setVisible(false);
        path.setVisible(false);

    }
    
    private class ButtonClickListener implements ActionListener{
        private int row;
        private int column;
            public ButtonClickListener(int row, int column){
                this.row = row;
                this.column = column;
            }

        @Override
        public void actionPerformed(ActionEvent e) {
            buttons[row][column].makeWall();
        }

    }
    
    private class ButtonStart implements ActionListener{
        public ButtonStart(){
            
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clickedButton = (JButton) e.getSource();
            findPath();
            clickedButton.setVisible(false);
            label.setVisible(true);
            path.setVisible(true);
            path.append(pathStr);
                // Close the current size_maze window
//                frame.dispose();

        }
    }
    
    public int[][] findPath(){
        Thread t1 = new Thread(() -> {
            this.colorSelection = (this.colorSelection + 1) % colorPallete.length;
            Color c = colorPallete[this.colorSelection];
            try {

                this.ratInMaze(START, pathValues, 0, c);
            } catch (Exception ex) {
                System.out.println(ex.toString());
            }

        });
        t1.start();
        this.threads.add(t1);
        while(!this.condition && !this.threads.isEmpty()){

        }
//        pathValues[1].toString()
//        System.out.println("X : " + pathValues[1][0] + " Y : " + pathValues[1][1]);
//        System.out.println("X : " + START[0] + " Y : " + START[1]);

        if (Arrays.equals(pathValues[1], START)){
                System.out.println("No Path");
                this.pathStr = "No Path";
            }
        else{
            this.pathStr = "";
            for(int x = 0; x < size*2; x++){
                if (Arrays.equals(pathValues[x], START) && x != 0){
                    break;
                }
                for(int y = 0; y < 2; y++){
                    int xPosition = pathValues[x][0];
                    int yPosition = pathValues[x][1];

                    buttons[xPosition][yPosition].setBackground(Color.GREEN);
                    this.pathStr += pathValues[x][y]+ " ";

                }
                this.pathStr += "\n";
                }
            }
        
        return null;
    }
    
    private void ratInMaze(int[] position, int[][] visited_grids, int row, Color c) throws Exception{

//        Color Place [Visit]

        int[][] current_visited_grids = visited_grids.clone();
//        System.out.println("Test");

        int x = position[0];
        int y = position[1];

        this.buttons[x][y].visit(c);

//        System.out.println("Thread ID : " + Thread.currentThread().getId() + "\n Thread is alive : " + Thread.currentThread().isAlive() );

        current_visited_grids[row] = position;
        if (row < this.size * 2){
            row++;
        }
        final int rowThread = row ;
        boolean nextCondition = false;
        boolean belowCondition = false;
        int[] next = null;
        int[] below = null ;

//      Check if position = [size-1, size-1]
        if (!Arrays.equals(position, END)){

            int nextRow = x+1;
            int nextColumn = y+1;
            
            if (nextRow <= size-1 && this.buttons[nextRow][y].isValidGrid()){
                belowCondition = true;
                below = new int[]{nextRow, y};
            }
            if (nextColumn <= size-1 && this.buttons[x][nextColumn].isValidGrid()){
                nextCondition = true;
                next = new int[]{x, nextColumn};
            }

            if (nextCondition){      
                if (belowCondition){
                       final int[] belowPosition = below.clone();
                       
                        Thread t = new Thread(() -> {
                            synchronized(this.colorLock){
                                this.colorSelection = (this.colorSelection + 1) % colorPallete.length;
                            }
                            Color color = colorPallete[this.colorSelection];
                            try {
                                this.ratInMaze(belowPosition, current_visited_grids, rowThread, color);
                            }
                                catch(Exception e){
                            
                            }
                        });
                        t.start();
                        synchronized(this.threads){
                            this.threads.add(t);
                        }
                        
                        t.join();
                }
                this.ratInMaze(next, current_visited_grids, row, c);
            }
            else if (belowCondition){
                this.ratInMaze(below, current_visited_grids, row, c);
            }
            else{
                synchronized(this.threads){
                    this.threads.remove(Thread.currentThread());
                }
                Thread.currentThread().interrupt();
            }
        }
        else{
            this.condition = true;
            this.pathValues = current_visited_grids.clone();
            synchronized(this.threads){
                    this.threads.remove(Thread.currentThread());
            }
            Thread.currentThread().interrupt();  
        }
        


        
    }

    
    public static void main(String[]args){
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run(){
                new Maze(8);
            }
        });
    }
    
}
