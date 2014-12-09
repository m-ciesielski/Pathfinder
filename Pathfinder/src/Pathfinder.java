import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.Math;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;



class GraphCreatorPanel extends JPanel{
	private Vector <Node> nodeContainer;	
	static int DEFAULT_NODE_CIRCLE_HEIGHT=50;
	static int DEFAULT_NODE_CIRCLE_WIDTH=50;
	static int DEFAULT_NODE_CIRCLE_ARC_HEIGHT=5;
	static int DEFAULT_NODE_CIRCLE_ARC_WIDTH=5;
	private int focusIndex=0;
	private int secondaryFocusIndex=0;
	private boolean focus=false;
	
	public GraphCreatorPanel(){
		setBorder(BorderFactory.createLineBorder(Color.black));
		nodeContainer= new Vector <Node> ();
	}
	
	public Dimension getPreferredSize() {
	    return new Dimension(250,200);
	}
	
	public void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    for(int i=0; i<nodeContainer.size();++i){
	    	if( focus==true)
	    		if(i==focusIndex)
	    		g.setColor(Color.YELLOW);
	    		else if(secondaryFocusIndex!=-1 && i==secondaryFocusIndex)
	    		g.setColor(Color.CYAN);
	    	drawNode(g, nodeContainer.get(i));
	    	g.setColor(Color.BLACK);
	    	drawConnections(g, nodeContainer.get(i));
	    }
	}
	
	public int getNodeContainerSize(){
		return nodeContainer.size();
	}
	
	public Node getNode(int index){
		return nodeContainer.get(index);
	}
	void addNode(int x, int y){
		nodeContainer.add(new Node (x,y));
	}
	void deleteNode(int index){
		for(int i=0; i<nodeContainer.size();++i){
			for(int j=0; j<nodeContainer.get(i).getNeighboursCount();++j){
				if(nodeContainer.get(i).getNeighbour(j).equals(nodeContainer.get(index)))
					nodeContainer.get(i).deleteNeighbour(j);
			}
		}
			nodeContainer.remove(index);	
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
	public void focusNode(int index){
		focusIndex=index;
		focus=true;
	}
	
	private boolean checkConnection(int index1, int index2){
		for(int i=0; i<nodeContainer.get(index1).getNeighboursCount();++i){
			if(nodeContainer.get(index1).getNeighbour(i).equals(index2))
				return false;
		}
		return true;
	}
	
	public void addConnection(int index1, int index2){
		if(checkConnection(index1, index2)==true){
			nodeContainer.get(index1).addNeighbour(nodeContainer.get(index2));
			nodeContainer.get(index2).addNeighbour(nodeContainer.get(index1));
		}
		
	}

	public Graph createGraph(){
		Graph graph=new Graph(nodeContainer.size());
		graph.addVerticles((Node[]) nodeContainer.toArray());
		return graph;
	}
	
	public void secondaryFocusNode(int index){secondaryFocusIndex=index;}
	
	public void setFocus(boolean focus){this.focus=focus;}
	
	
	public boolean getFocus(){return this.focus;}
	public int getSecondaryFocusIndex(){return this.secondaryFocusIndex;}
	public int getFocusIndex(){return this.focusIndex;}
}


class GridPanel extends JPanel {
	Vector <Path> pathVector;
	int pathIndex;
public GridPanel() {
    setBorder(BorderFactory.createLineBorder(Color.black));
    pathIndex=0;
    pathVector=new Vector <Path> ();
}

public Dimension getPreferredSize() {
    return new Dimension(250,200);
}

public void paintComponent(Graphics g) {
    super.paintComponent(g);       
    Grid.drawGrid(g,50, 0,0,Grid.getSize()-1,Grid.getSize()-1);
    drawPath(g);
}
 private void drawPath(Graphics g){
	 if(pathVector.size()>0)
	 {
		 g.setColor(Color.BLACK);
		 for(int j=0;j<pathVector.get(pathIndex).vector.size()-1;++j)
		 {
			 g.drawLine((50*pathVector.get(pathIndex).vector.get(j).getX()+25),(50*pathVector.get(pathIndex).vector.get(j).getY()+25),
					 (50*pathVector.get(pathIndex).vector.get(j+1).getX()+25),(50*pathVector.get(pathIndex).vector.get(j+1).getY()+25));
		 } 
	 }
		 
 }

 public void addPath (Path path){
	 pathVector.add(path);
	 //path.showPath();
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
}

class Coords {
	private int x;
	private int y;
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public void setX(int x){
		this.x=x;
	}
	
	public void setY(int y){
		this.y=y;
	}
	public double calculateDistance (Coords destination)
	{
		return (Math.sqrt(Math.pow(this.x-destination.x,2)+Math.pow(this.y-destination.y,2)));
	}
}

class Node extends Coords{
	//public int [] neighbour;
	private Vector <Node> neighbourContainer;
	Node()
	{
		//position=new Coords();
		neighbourContainer=new Vector <Node>();
		//neighbour=new int[4];
	}
	
	Node(int x, int y){
		this.setX(x);
		this.setY(y);
		neighbourContainer=new Vector <Node>();
	}
	public void addNeighbour(Node arg){neighbourContainer.add(arg);}
	
	public Node getNeighbour(int index){return neighbourContainer.get(index);}
	
	public void deleteNeighbour(int index){neighbourContainer.remove(index);}
	
	public int getNeighboursCount(){return neighbourContainer.size();}
	
	
	public double calculateDistance (Node destination)
	{
		return (Math.sqrt(Math.pow(this.getX()-destination.getX(),2)+Math.pow(this.getY()-destination.getY(),2)));
	}
	
	public boolean equals (Node arg)
	{
		if(this.getX()==arg.getX() && this.getY()==arg.getY())
			return true;
		else
			return false;
	}
}

class Graph {
	private Node [] verticle;
	private int size;
	public Graph(int size)
	{
		this.size=size;
		verticle=new Node[size];
		//makeGridGraph(size);
	}
	
	public void addVerticles(Node [] verticle){
		this.verticle=verticle;
	}
	public int size(){return this.size;}
	
	public Node getVerticle(int index){return this.verticle[index];}
	
	public void addVerticle(Node verticle, int index){this.verticle[index]=verticle;}
	private void makeGridGraph (int size)
	{
		int xIterator=0;
		int yIterator=0;
		for(int i=0;i<size*size;++i)
		{
			verticle[i].setX(xIterator%size);
			verticle[i].setY(yIterator%size);
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
				distance=verticle[i].calculateDistance(verticle[j]);
				if(distance==1)
				{
					verticle[i].addNeighbour(verticle[j]);
				}
			}
		}
	}
	
	public void showGraph()
	{
		for(int i=0;i<size*size;++i)
		{
			System.out.println("i= "+i+"("+verticle[i].getX()+" ,"+verticle[i].getY()+")");
						
		}
	}
	/*
	private boolean deletePath(int pathContainerSize, int index, int pathLength, Node end){
		int index=pathContainer.indexOf(basePathI);
		if(index<pathContainer.size()-1){
			//pathContainer.remove(basePath);
			//makeStep(pathContainer, pathContainer.get(index), pathLength, end);
			return true;
		}
		else{
			//pathContainer.remove(basePath);
			//return;
			return false;
		}
	}
	*/
	private void makeStep (Vector<Path> pathContainer, Path basePath, int pathLength, Node end)
	{
		//Sprawdzanie sciezki na przedostatnim kroku
		if(basePath.vector.size()==pathLength-1){
			int index=pathContainer.indexOf(basePath);
			boolean correctPath=false;
			for(int i=0;i<basePath.vector.lastElement().getNeighboursCount();++i)
			{
				if(basePath.vector.lastElement().getNeighbour(i).equals(end)){
					correctPath=true;
					basePath.vector.addElement(end);
					if(index<pathContainer.size()-1){
						makeStep(pathContainer, pathContainer.get(index+1), pathLength, end);
					}
					else
						return;
				}
			}
			if(correctPath==false){
				if(index<pathContainer.size()-1){
					pathContainer.remove(basePath);
					makeStep(pathContainer, pathContainer.get(index), pathLength, end);
				}
				else{
					pathContainer.remove(basePath);
					return;
				}
			}
		}
		else
		{
			int possibleMoves=0;
			//Wyliczenie mozliwych ruchow
			for(int i=0;i<basePath.vector.lastElement().getNeighboursCount();++i)
			{
				if(basePath.checkNode(basePath.vector.lastElement().getNeighbour(i),end)){
					++possibleMoves;
				}
			}
			//usuwanie sciezki, gdy brak mozliwosci ruchu
			if(possibleMoves==0)
			{
				int index=pathContainer.indexOf(basePath);
				if(index<pathContainer.size()-1){
					pathContainer.remove(basePath);
					makeStep(pathContainer, pathContainer.get(index), pathLength, end);
				}
				else{
					pathContainer.remove(basePath);
					return;
				}
			}
			//wykonanie ruchu(gdy possibleMoves=1, dodanie nowego wezla do sciezki bazowej
			//gdy pMoves>1 dodanie nowych sciezek do kontenera pathContainer
			else{
				int makedMoves=0;
				for(int i=0;i<basePath.vector.lastElement().getNeighboursCount();++i)
				{
					if(basePath.checkNode(basePath.vector.lastElement().getNeighbour(i),end)==true)
					{
						if(makedMoves==possibleMoves-1){
							basePath.vector.add(basePath.vector.lastElement().getNeighbour(i));
							break;
						}
						else{
							pathContainer.addElement(new Path (basePath));
							pathContainer.lastElement().vector.addElement(basePath.vector.lastElement().getNeighbour(i));
							++makedMoves;
						}
					}
					
				}
				makeStep(pathContainer, basePath, pathLength, end);
			}
			
		}
	}

	

	public void findPaths(int pathLength, Node start, Node end, GridPanel mainPan)
	{
		Vector<Path> pathContainer=new Vector <Path> ();
		pathContainer.add(new Path(start));
		
		makeStep(pathContainer, pathContainer.get(0), pathLength, end);
		
		System.out.println(pathContainer.size());
		
		for(int j=0;j<pathContainer.size();++j)
		{
			//if(pathContainer.get(j).vector.size()==pathLength)
				mainPan.addPath(pathContainer.get(j));
				pathContainer.get(j).showPath();
			
		}	
		
	}
}

class Path
{
	Vector<Node> vector=new Vector <Node> ();
	
	public Path(Node start)
	{
		vector.add(start);
	}
	
	public Path (Path parentPath)
	{
		this.vector= (Vector<Node>) parentPath.vector.clone();
	}
	
	public void showPath ()
	{
		System.out.println("--------------");
		System.out.println("Path size: "+this.vector.size());
		for(int i=0;i<vector.size();++i)
			System.out.println("x= "+vector.get(i).getX()+",y= "+vector.get(i).getY());
		
		System.out.println("--------------");
			
	}
	
	public boolean checkNode (Node tryNode, Node endNode)
	{
		if(tryNode.equals(endNode))
				return false;
		for(int i=0;i<vector.size();++i)
		{
			if(tryNode.equals(vector.get(i)))
				return false;
		}
		return true;
	}
	
	
}

public class Pathfinder {
	
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
	
	private static void createAndShowGraphCreatorGUI(final GraphCreatorPanel mainPan){
		System.out.println("Created GUI on EDT? "+
	            SwingUtilities.isEventDispatchThread());
	    JFrame f = new JFrame("Pathfinder");
	    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    f.setSize(250,250);
	    
	    
	    mainPan.addMouseListener(new MouseListener() {
			
	    	
	    	boolean nodeSelected(Node arg, int x, int y){
	    		if(arg.getX()<x && arg.getX()+GraphCreatorPanel.DEFAULT_NODE_CIRCLE_WIDTH>x
						&&arg.getY()<y && arg.getY()+GraphCreatorPanel.DEFAULT_NODE_CIRCLE_WIDTH>y)
	    			return true;
	    		else 
	    			return false;
	    	}
			@Override
			public void mouseReleased(MouseEvent e) {
				if(e.getButton()==3)
				{
					boolean nodeSelected=false;
					for(int i=0;i<mainPan.getNodeContainerSize();++i){
						Node testNode=mainPan.getNode(i);
						System.out.println("size: "+mainPan.getNodeContainerSize());
						if(nodeSelected(testNode, e.getX(),e.getY())==true)
						{
							if(mainPan.getSecondaryFocusIndex()==-1){
								mainPan.deleteNode(i);
								mainPan.repaint();
								nodeSelected=true;
								break;
							}
							else{
								mainPan.addConnection(i, mainPan.getFocusIndex());
								mainPan.repaint();
								nodeSelected=true;
								break;
							}
							
						}
					}
					if(nodeSelected==false){
						mainPan.addNode(e.getX(),e.getY());
						System.out.println("size: "+mainPan.getNodeContainerSize());
						mainPan.repaint();
					}
					
				}
				
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				//mainPan.addNode(e.getX(),e.getY());
				//mainPan.repaint();
				if(e.getButton()==1)
				{
					if(mainPan.getFocus()==false)
					{
						boolean nodeSelected=false;
						for(int i=0;i<mainPan.getNodeContainerSize();++i){
							Node testNode=mainPan.getNode(i);
							System.out.println("size: "+mainPan.getNodeContainerSize());
							if(nodeSelected(testNode, e.getX(),e.getY())==true)
							{
								mainPan.focusNode(i);
								mainPan.repaint();
								nodeSelected=true;
								break;
							}
						}
						if(nodeSelected==false){
							mainPan.setFocus(false);
							mainPan.secondaryFocusNode(-1);
							mainPan.repaint();
						}
							
					}
					else{
						boolean nodeSelected=false;
						for(int i=0;i<mainPan.getNodeContainerSize()-1;++i){
							Node testNode=mainPan.getNode(i);
							
							 if(nodeSelected(testNode, e.getX(),e.getY())==true)
							{
								 if(i==mainPan.getSecondaryFocusIndex()){
										//mainPan.newConnection(mainPan.getNode(mainPan.getFocusIndex()), mainPan.getNode(mainPan.getSecondaryFocusIndex()));
										mainPan.repaint();
										
									}
								 else{
									 mainPan.secondaryFocusNode(i);
										mainPan.repaint();
										nodeSelected=true;
								 }
								 break;
								
							}
						}
						if(nodeSelected==false){
							mainPan.setFocus(false);
							mainPan.secondaryFocusNode(-1);
							mainPan.repaint();
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
	
	public static void main(String[] args) {
		//Grid space=new Grid(4);
	// final GridPanel mainPan=new GridPanel();
		final GraphCreatorPanel mainPan= new GraphCreatorPanel();

		//Graph spaceGraph=new Graph(4);
		//spaceGraph.showGraph();
		//spaceGraph.verticle[3].neighboursCount=0;
		//spaceGraph.findPaths(7, spaceGraph.verticle[0], spaceGraph.verticle[10], mainPan);
		//spaceGraph.findPaths(9, spaceGraph.verticle[0], spaceGraph.verticle[10], mainPan);
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	
                //createAndShowGridGUI(mainPan);
                createAndShowGraphCreatorGUI(mainPan);
            }
        });
	}
}
