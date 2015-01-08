import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

class NodeContextMenu extends JPopupMenu {
	public NodeContextMenu(final GraphCreatorModel model) {
		JMenuItem delete=new JMenuItem("Delete");
		delete.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//delete node in model
				model.deleteNode(model.getFocusIndex());
			}
		});
		this.add(delete);
		JMenuItem start=new JMenuItem("Path start");
		start.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//mark as start in model
				model.setPathStart(model.getNode(model.getFocusIndex()));
			}
		});
		this.add(start);
		
		JMenuItem end=new JMenuItem("Path end");
		end.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				model.setPathEnd(model.getNode(model.getFocusIndex()));
			}
		});
		this.add(end);
		
	}
}


class GridPanel extends JPanel{
	Vector <Path> pathVector;
	int pathIndex;
public GridPanel() {
    setBorder(BorderFactory.createLineBorder(Color.black));
    pathIndex=0;
    pathVector=new Vector <Path> ();
}

public Dimension getPreferredSize() {return new Dimension(250,200);}

public void paintComponent(Graphics g) {
    super.paintComponent(g);       
    Grid.drawGrid(g,50, 0,0,Grid.getSize()-1,Grid.getSize()-1);
    drawPath(g);
}
 private void drawPath(Graphics g){
	 if(pathVector.size()>0)
	 {
		 g.setColor(Color.BLACK);
		 for(int j=0;j<pathVector.get(pathIndex).size()-1;++j)
		 {
			 g.drawLine((50*pathVector.get(pathIndex).get(j).getX()+25),(50*pathVector.get(pathIndex).get(j).getY()+25),
					 (50*pathVector.get(pathIndex).get(j+1).getX()+25),(50*pathVector.get(pathIndex).get(j+1).getY()+25));
		 } 
	 }
		 
 }

 public void addPath (Path path){pathVector.add(path);}
}

class GraphCreatorView extends JPanel{
	static int DEFAULT_NODE_CIRCLE_HEIGHT=50;
	static int DEFAULT_NODE_CIRCLE_WIDTH=50;
	static int DEFAULT_NODE_CIRCLE_ARC_HEIGHT=5;
	static int DEFAULT_NODE_CIRCLE_ARC_WIDTH=5;
	private int indexOfPathToDraw=-1;
	private JButton commitButton;
	private JRadioButton startButton;
	private JRadioButton endButton;
	private DefaultListModel <String> pathListModel;
	private JList<String> pathList;
	private JScrollPane pathListScroll;
	private GraphCreatorModel model;
	 Vector <Path> resultPathsContainer;
	private NodeContextMenu nodeContextMenu;
	
	
	public GraphCreatorView(final GraphCreatorModel model){
		setBorder(BorderFactory.createLineBorder(Color.black));
		
		this.model=model;
		this.addMouseListener(new GraphCreatorListener());
		nodeContextMenu=new NodeContextMenu(model);
		commitButton=new JButton();
		this.add(commitButton);
		commitButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				resultPathsContainer=new Vector<Path>();
				Graph graph;
				graph=model.createGraph();
				graph.showGraph();
					resultPathsContainer.clear();
				if(model.getNodeIndex(model.getPathStart())==-1)
					model.setPathStart(model.getNode(0));
				if(model.getNodeIndex(model.getPathEnd())==-1)
					model.setPathEnd(model.getNode(model.getNodeContainerSize()-1));
				for(int i=0;i<=graph.size();++i)
				graph.findPaths(i, graph.getVerticle(model.getNodeIndex(model.getPathStart())),
						graph.getVerticle(model.getNodeIndex(model.getPathEnd())), resultPathsContainer);
				
				makeResultPathsList();
			}
		});
		startButton=new JRadioButton();
		this.add(startButton);
		startButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				model.setPathStart(model.getNode(model.getFocusIndex()));
				
			}
		});
		endButton=new JRadioButton();
		this.add(endButton);
		endButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				model.setPathEnd(model.getNode(model.getFocusIndex()));
			}
		});
		
		
		pathListScroll=new JScrollPane();
		this.add(pathListScroll);
	}
	
	public Dimension getPreferredSize() {return new Dimension(250,200);}
	
	public void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    for(int i=0; i<model.getNodeContainer().size();++i){
	    	if( model.getFocus()==true)
	    		if(i==model.getFocusIndex())
	    		g.setColor(Color.YELLOW);
	    		else if(model.getSecondaryFocusIndex()!=-1 && i==model.getSecondaryFocusIndex())
	    		g.setColor(Color.CYAN);
	    	drawNode(g, model.getNodeContainer().get(i));
	    	g.setColor(Color.BLACK);
	    	drawConnections(g, model.getNodeContainer().get(i));
	    }
	    if(indexOfPathToDraw!=-1)
    		drawPath(g, resultPathsContainer.get(indexOfPathToDraw));
	}
	
	private void drawNode(Graphics g, Node arg){
		//g.setColor(Color.BLACK);
		g.drawRoundRect(arg.getX(), arg.getY(), DEFAULT_NODE_CIRCLE_HEIGHT, DEFAULT_NODE_CIRCLE_HEIGHT,
				DEFAULT_NODE_CIRCLE_ARC_WIDTH, DEFAULT_NODE_CIRCLE_ARC_HEIGHT);
	}
	
	private void drawConnections(Graphics g,Node arg){
		for(int i=0; i<arg.getNeighboursCount();++i){
			g.drawLine(arg.getX(), arg.getY(), arg.getNeighbour(i).getX(), arg.getNeighbour(i).getY());
		}
	}
	
	private void drawPath(Graphics g, Path path){
		g.setColor(Color.RED);
		for(int i=0;i<path.size()-1;++i)
			g.drawLine(path.get(i).getX(),path.get(i).getY(), path.get(i+1).getX(), path.get(i+1).getY());
	}
	
	public void addPath(Path path){
		resultPathsContainer.add(path);
	}
	
	private void makeResultPathsList(){
		pathListModel=new DefaultListModel<String>();
		System.out.println(resultPathsContainer.size());
		for(int i=0; i<resultPathsContainer.size();++i)
		{
			pathListModel.addElement("Sciezka nr: "+(i+1));
		}
		pathList= new JList<String>(pathListModel);
		pathList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		pathList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		pathList.setVisibleRowCount(-1);
		

		pathList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				ListSelectionModel lsm = (ListSelectionModel)e.getSource();
				
				indexOfPathToDraw=lsm.getMinSelectionIndex();
				repaint();
			}
		});;
		
		pathListScroll.setMaximumSize(new Dimension(100, 200));
		pathListScroll.setMinimumSize (new Dimension (100,200));
		pathListScroll.setPreferredSize(pathListScroll.getMaximumSize());
		pathListScroll.setViewportView(pathList);

	    repaint();
	}
	
	private class GraphCreatorListener implements MouseListener{
		boolean nodeSelected(Node arg, int x, int y){
			if(arg.getX()<x && arg.getX()+GraphCreatorView.DEFAULT_NODE_CIRCLE_WIDTH>x
					&&arg.getY()<y && arg.getY()+GraphCreatorView.DEFAULT_NODE_CIRCLE_WIDTH>y)
				return true;
			else 
				return false;
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			if(e.isPopupTrigger())
			{
				boolean nodeSelected=false;
				for(int i=0;i<model.getNodeContainerSize();++i){
					Node testNode=model.getNode(i);
					if(nodeSelected(testNode, e.getX(),e.getY())==true)
					{
						if(model.getFocus()==false){
							model.focusNode(i);
							model.setFocus(true);
							nodeContextMenu.show(e.getComponent(),e.getX(),e.getY());
							repaint();
							nodeSelected=true;
							break;
						}
						else{
							model.addConnection(i, model.getFocusIndex());
							repaint();
							nodeSelected=true;
							break;
						}
						
					}
				}
				if(nodeSelected==false){
					model.addNode(e.getX(),e.getY());
					System.out.println("size: "+model.getNodeContainerSize());
					repaint();
				}
				
			}
			
			
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			//mainPan.addNode(e.getX(),e.getY());
			//mainPan.repaint();
			if(e.getButton()==1)
			{
				if(model.getFocus()==false)
				{
					boolean nodeSelected=false;
					for(int i=0;i<model.getNodeContainerSize();++i){
						Node testNode=model.getNode(i);
						if(nodeSelected(testNode, e.getX(),e.getY())==true)
						{
							model.focusNode(i);
							repaint();
							nodeSelected=true;
							break;
						}
					}
					if(nodeSelected==false){
						model.setFocus(false);
						model.secondaryFocusNode(-1);
						repaint();
					}
						
				}
				else{
					boolean nodeSelected=false;
					for(int i=0;i<model.getNodeContainerSize()-1;++i){
						Node testNode=model.getNode(i);
						
						 if(nodeSelected(testNode, e.getX(),e.getY())==true)
						{
							 if(i==model.getSecondaryFocusIndex()){
									//mainPan.newConnection(mainPan.getNode(mainPan.getFocusIndex()), mainPan.getNode(mainPan.getSecondaryFocusIndex()));
									repaint();
									
								}
							 else{
								 model.secondaryFocusNode(i);
									repaint();
									nodeSelected=true;
							 }
							 break;
							
						}
					}
					if(nodeSelected==false){
						model.setFocus(false);
						model.secondaryFocusNode(-1);
						repaint();
					}
					
				}
			}
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
			/*
			if(e.getButton()==3)
			{
				mainPan.addNode(e.getX(),e.getY());
				mainPan.repaint();	
				
			}
				*/
		}
		
	}
	
}

class Grid {
	private static int size;
	public Grid (int size)
	{
		Grid.size=size;
	}
	
	public static int getSize()
	{
		return size;
	}
	
	public static void drawGrid(Graphics g, int tileSize, int startX, int startY, int endX, int endY)
	{
		int xIterator=0;
		int yIterator=0;
		for(int i=0;i<size*size;++i)
		{
			g.drawRect(xIterator%size*tileSize,yIterator%size*tileSize, tileSize, tileSize);
			if(i%size==0)
				++xIterator;
			else
				++yIterator;
		}
		g.setColor(Color.GREEN);
		g.fillRect(startX%size*tileSize,startY%size*tileSize, tileSize, tileSize);
		g.setColor(Color.RED);
		g.fillRect(endX%size*tileSize,endY%size*tileSize, tileSize, tileSize);
	}
	
	private static void makeGridGraph (int size, Graph graph)
	{
		int xIterator=0;
		int yIterator=0;
		for(int i=0;i<size*size;++i)
		{
			graph.getVerticle(i).setX(xIterator%size);
			graph.getVerticle(i).setY(yIterator%size);
			if(i%size==0)
				++xIterator;
			else
				++yIterator;
		}
		
		for(int i=0;i<size*size;++i)
		{
			for(int j=0;j<size*size;++j)
			{
				double distance;
				distance=graph.getVerticle(i).calculateDistance(graph.getVerticle(j));
				if(distance==1)
				{
					graph.getVerticle(i).addNeighbour(graph.getVerticle(j));
				}
			}
		}
	}
}