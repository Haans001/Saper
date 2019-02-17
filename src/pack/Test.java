package pack;

import java.awt.EventQueue;
import javax.swing.*;


public class Test {
	
	public static void main(String[]args) {
		
		EventQueue.invokeLater(() ->
		{
			Game frame = new Game();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
			
			
			
		});
		
	}

}
