package pack;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.*;

public class Game extends JFrame {
	
	
	public static final int bombsInGame = 15;
	public static int revealCount=0;
	
	
	public static final int rows = 10;
	public static final int cols = 10;
	public static int flags = bombsInGame;
	public static boolean  play  = true;
	JLabel label;
	JLabel flagLabel;
	JLabel flagCountLabel;
	
	
	
	
	
	public Game() {
		
		this.setTitle("Saper");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setSize(600,600);
		
		this.setLayout(new BorderLayout());
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(10,11));
		
		
		Cell [][] cells = new Cell[rows][cols];
		GameListener listener = new GameListener(cells);
		FlagSetter flagSetter = new FlagSetter();
		
		
		for(int i=0;i<rows;i++) {
			for(int j=0;j<cols;j++) {
				
				
				cells[i][j] = new Cell(i,j);
				cells[i][j].addActionListener(listener);
				cells[i][j].addMouseListener(flagSetter);
				if(j%2==0) cells[i][j].setBackground(new Color(70, 183, 33));
				else cells[i][j].setBackground(new Color(67, 209, 20));
				panel.add(cells[i][j]);
			
				
			}
		}
		
		newGame(cells);

		
	///////////////////////////////////////////////////////////////////////	/////////////
		
		this.add(panel, BorderLayout.CENTER);
		label= new JLabel(" ");
		this.add(label, BorderLayout.SOUTH);
		
		JMenuBar menuBar = new JMenuBar();
		
		JMenu menu_1 = new JMenu("Gra");
		JMenuItem item = new JMenuItem("Nowa");
		item.addActionListener((e)->{
			/* newGame(cells); */
			Game newGame = new Game();
			newGame.setVisible(true);
			this.dispose();
		});
		
		menu_1.add(item);
		menuBar.add(menu_1);
		
		JPanel panelNorth = new JPanel();
		panelNorth.setLayout(new BorderLayout());
		panelNorth.add(menuBar, BorderLayout.NORTH);
		JPanel flagPanel = new JPanel();
		flagLabel = new JLabel();
		flagLabel.setIcon(cells[0][0].flagImg);
		flagPanel.add(flagLabel);
		flagCountLabel = new JLabel();
		flagCountLabel.setText(Integer.toString(flags));
		flagPanel.add(flagCountLabel);
		panelNorth.add(flagPanel, BorderLayout.SOUTH);
	    this.add(panelNorth, BorderLayout.NORTH);
	    
		
	}
	
	
	
        private void newGame(Cell[][] cells) {
        	int g=0;
		
		for(int i=0;i<rows;i++) {
			for(int j=0;j<cols;j++) {		
				cells[i][j].setIcon(null);
				cells[i][j].isBomb = false;
				cells[i][j].isFlagged = false;
				cells[i][j].revealed = false;
				cells[i][j].bombsAround = 0;
				cells[i][j].setText("");
				cells[i][j].flagCount = 0;
//				if(g%2==1 ) cells[i][j].setBackground(new Color(70, 183, 33));
//				else cells[i][j].setBackground(new Color(67, 209, 20));
				
				if(i%2==0) {
					if(g%2==1)cells[i][j].setBackground(new Color(70, 183, 33));
					else cells[i][j].setBackground(new Color(67, 209, 20));
				}else {
					if(g%2==0)cells[i][j].setBackground(new Color(70, 183, 33));
					else cells[i][j].setBackground(new Color(67, 209, 20));
				}
				g++;
			}
		}
		
		resetBombs(cells);
		
		for(int i=0;i<rows;i++) {
			for(int j=0;j<cols;j++) {		
				cells[i][j].countNeighboors(i, j, cells);			
			}
		}
		
		play = true;
			
		
	}
      
        private void resetBombs(Cell[][] cells) {
        	
        	Random rand = new Random();
        	boolean lookingForEmpty;
    		int a,b, count=0;
    		
    		while( count< bombsInGame) {

    		lookingForEmpty = true;
    		
    		while(lookingForEmpty) {
    			a = rand.nextInt(rows);
    			b = rand.nextInt(cols);
    			
    			if(cells[a][b].isBomb==false) {
    				cells[a][b].isBomb=true;
    				lookingForEmpty = false;
    				count++;
    			}
    		}
    		}
        	
        }




	private class GameListener implements ActionListener {
		
		Cell[][] cells;
		
		public GameListener(Cell[][] cells) {
			this.cells = cells;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if(play) {
			
			Cell cell = (Cell) e.getSource();
			cell.revealed =true;
			cell.setBackground(Color.white);
			System.out.println(cell.bombsAround);
			
			
			/// Klikniecie na cyfre
			if(cell.bombsAround>0 && cell.isBomb==false && !cell.isFlagged) {
				cell.setText(Integer.toString(cell.bombsAround));
				revealCount++;
			}
			
			// Klikniecie na puste pole
			if(cell.bombsAround==0 && cell.isBomb==false & !cell.isFlagged) {
				cell.setBackground(Color.white);
				cell.reveal(cells);
			}
			
			// klikniecie na bombe
			if(cell.isBomb==true && !cell.isFlagged) {
				cell.setIcon(cell.bombImg);
				lose();
					
			}
			
			checkForWin();
			
			}
		}
		
		
		

		private void checkForWin() {
			
			if(revealCount == rows*cols) {
				Game.play = false;
				label.setText("Wygrales");
			}
			
		}

		private void lose() {
			Game.play = false;
			label.setText("Przegrales");
			
			for(int i=0;i<Game.rows;i++) {
				for(int j=0;j<Game.rows;j++) {
					
					if(cells[i][j].isBomb == true) {
						cells[i][j].setIcon(cells[i][j].bombImg);
						cells[i][j].setBackground(Color.red);
						
					}
				}
			}
		}
		
		
		
	}
	
	
	///          SPRAWDZA PRAWE KLIKNIECIE MYSZKA I USTAWIA FLAGE ALBO JA USUWA
	
	private class FlagSetter extends MouseAdapter{
		
		public void mousePressed(MouseEvent e) {
			
			if(play) {
			
			if(SwingUtilities.isRightMouseButton(e)) {
				
				Cell cell = (Cell) e.getSource();
				
				if(cell.flagCount%2==0) {
					
					if(flags>0) {
				cell.setIcon(cell.flagImg);
				cell.isFlagged = true;
				cell.revealed = true;
				Game.revealCount++;
				Game.flags--;
				flagCountLabel.setText(Integer.toString(flags));
					}
					
				}else {
					cell.setIcon(null);
					cell.revealed = false;
					cell.isFlagged = false;
					Game.revealCount--;
					Game.flags++;
					flagCountLabel.setText(Integer.toString(flags));
				}
				cell.flagCount++;
				
			}
		}
			
	}
	}

}
