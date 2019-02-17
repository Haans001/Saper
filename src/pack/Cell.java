package pack;
import java.awt.Color;
import java.awt.Font;
import java.util.Random;

import javax.swing.*;

public class Cell extends JButton {
	
	
	public ImageIcon bombImg = new ImageIcon("bomb.png");
	public ImageIcon flagImg = new ImageIcon("flag.png");
    Random rand = new Random();
	public boolean isBomb = false;
	public boolean revealed = false;
	
	public boolean isFlagged = false;
	public int flagCount = 0;
	public int xPos,yPos;
	public int bombsAround = 0;
	
	
	////////// KONSTRUKTOR
	public Cell(int x,int y) {
		
		this.setFont(new Font("Arial",Font.BOLD,20));
		this.xPos = x;
		this.yPos = y;
				
		
	}
	
	/////////// SPRAWDZANIE BOMB WOKOLO
	
     public void countNeighboors(int x, int y, Cell[][] cells) {
    	 
    	 if(this.isBomb) return;
		
    	 for(int xoff=-1;xoff<=1;xoff++) {
    		 for(int yoff=-1;yoff<=1;yoff++) {
    			 
    			 int i = x+xoff;
    			 int j = y+yoff;
    			 
    			 if(i>-1 && i< Game.rows && j>-1 && j<Game.cols) {
    			 
    			 Cell neighboor = cells[i][j];
    			 if(neighboor.isBomb==true) {
    				 this.bombsAround++;
    			 }
    			 }   			 
   		 }  		 
    	 }  	    	 
	}
     
     /////////////// ODKRYWANIE BIALEJ PRZESTRZENI
     
     public void reveal(Cell[][] cells) {
    	 
    	 this.revealed = true;
    	 Game.revealCount++;
    	 if(this.bombsAround==0) {
    		 this.setBackground(Color.white);
    		 this.revealZeros(cells);
    		 }
    	 else {
    		 this.setBackground(Color.white);
    		 this.setText(Integer.toString(this.bombsAround));
    	 }
     }
     
     
     

	private void revealZeros(Cell[][] cells) {
		
		for(int xoff=-1;xoff<=1;xoff++) {
   		 for(int yoff=-1;yoff<=1;yoff++) {
   			 
   			 int i = this.xPos+xoff;
   			 int j = this.yPos+yoff;
   			 
   			 if(i>-1 && i<Game.rows && j>-1 && j<Game.cols) {
   			 
   			 Cell neighboor = cells[i][j];
   			 if(!neighboor.isBomb && !neighboor.revealed) {
   				 neighboor.reveal(cells);
   				 
   			 }
   			 }		 
   		 } 		 
   	 }	
		
	}
     

}
