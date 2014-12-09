import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class GUI {
	public static void createAndShowGraphCreatorGUI(GraphCreatorPanel mainPan){
		System.out.println("Created GUI on EDT? "+
	            SwingUtilities.isEventDispatchThread());
	    JFrame f = new JFrame("Pathfinder");
	    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    f.setSize(250,250);
	    
	    
	    mainPan.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				
				
			}
		});
	    f.add(mainPan);

	    
	    f.pack();
	    
	    f.setVisible(true);
	}
	
	private static void insertPathsIntoModel (GridPanel mainPan, DefaultListModel<String> model ){

		for(int i=0; i<mainPan.pathVector.size();++i)
		{
			model.addElement("Sciezka nr: "+(i+1));
		}
	}
	private static void createAndShowGridGUI(final GridPanel mainPan) {
	    System.out.println("Created GUI on EDT? "+
	            SwingUtilities.isEventDispatchThread());
	    JFrame f = new JFrame("Pathfinder");
	    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    f.setSize(250,250);
	    
	    f.add(mainPan);
	    
	    
	    DefaultListModel <String> model = new DefaultListModel <String>();
	    JList<String> list = new JList<String>(model);
	    list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
	    insertPathsIntoModel(mainPan, model);
	    //list.setSelectionMode(model.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setVisibleRowCount(-1);
		

		list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				ListSelectionModel lsm = (ListSelectionModel)e.getSource();
				
				mainPan.pathIndex=lsm.getMinSelectionIndex();
				mainPan.repaint();
			}
		});;
		
	    JScrollPane scroll = new JScrollPane(list);
	 
	    scroll.setMaximumSize(new Dimension(100, 200));
	    scroll.setMinimumSize (new Dimension (100,200));
	    scroll.setPreferredSize(scroll.getMaximumSize());
	   // mainPan.add(list);
	    mainPan.add(scroll);

	    
	    f.pack();
	    
	    f.setVisible(true);
	}
}
