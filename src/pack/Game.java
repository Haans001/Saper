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
	public static boolean  play  = true;
	JLabel label;
	
	
	
	public Game() {
		
		this.setTitle("Saper");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setSize(500,500);
		
		this.setLayout(new BorderLayout());
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(10,10));
		Random rand = new Random();
		
		
		Cell [][] cells = new Cell[rows][cols];
		GameListener listener = new GameListener(cells);
		FlagSetter flagSetter = new FlagSetter();
		
		
		for(int i=0;i<rows;i++) {
			for(int j=0;j<cols;j++) {
				
				cells[i][j] = new Cell(i,j);
				cells[i][j].addActionListener(listener);
				cells[i][j].addMouseListener(flagSetter);
				panel.add(cells[i][j]);
			
				
			}
		}
	
		
		////////////// WYPELNIANIE PLANSZY BOMBAMI W ZALEZNOSCI ILE ICH JEST/////////
		
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
		
		////////////////////////////////////////////////////////////////////////////////////
		
		
		///////////////////////KALKULOWANIE ILE BOMB JEST WOKOLO KAZDEGO POLA/////////////////////
		
		for(int i=0;i<rows;i++) {
			for(int j=0;j<cols;j++) {		
				cells[i][j].countNeighboors(i, j, cells);			
			}
		}
		
	///////////////////////////////////////////////////////////////////////	/////////////
		this.add(panel, BorderLayout.CENTER);
		label= new JLabel(" ");
		this.add(label, BorderLayout.SOUTH);
		
		JMenuBar menuBar = new JMenuBar();
		
		JMenu menu_1 = new JMenu("Gra");
		JMenuItem item = new JMenuItem("Nowa");
		item.addActionListener((e)->{
			Game frame = new Game();
			frame.setVisible(true);
		});
		
		menu_1.add(item);
		menuBar.add(menu_1);
		
		this.add(menuBar, BorderLayout.NORTH);
		
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
			
			if((Game.rows*Game.cols) - Game.revealCount == Game.bombsInGame) {
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
				cell.setIcon(cell.flagImg);
				cell.isFlagged = true;
				cell.revealed = true;
				}else {
					cell.setIcon(null);
					cell.isFlagged = false;
					
				}
				
				cell.flagCount++;
				
			}
		}
			
	}
	}

}
