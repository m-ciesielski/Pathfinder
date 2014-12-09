import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

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

class GraphCreatorView extends JPanel{
	static int DEFAULT_NODE_CIRCLE_HEIGHT=50;
	static int DEFAULT_NODE_CIRCLE_WIDTH=50;
	static int DEFAULT_NODE_CIRCLE_ARC_HEIGHT=5;
	static int DEFAULT_NODE_CIRCLE_ARC_WIDTH=5;
	private GraphCreatorModel model;
	public GraphCreatorView(GraphCreatorModel model){
		setBorder(BorderFactory.createLineBorder(Color.black));
		this.model=model;
		this.addMouseListener(new GraphCreatorListener());
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
	
	class GraphCreatorListener implements MouseListener{
		boolean nodeSelected(Node arg, int x, int y){
			if(arg.getX()<x && arg.getX()+GraphCreatorView.DEFAULT_NODE_CIRCLE_WIDTH>x
					&&arg.getY()<y && arg.getY()+GraphCreatorView.DEFAULT_NODE_CIRCLE_WIDTH>y)
				return true;
			else 
				return false;
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			if(e.getButton()==3)
			{
				boolean nodeSelected=false;
				for(int i=0;i<model.getNodeContainerSize();++i){
					Node testNode=model.getNode(i);
					System.out.println("size: "+model.getNodeContainerSize());
					if(nodeSelected(testNode, e.getX(),e.getY())==true)
					{
						if(model.getSecondaryFocusIndex()==-1){
							model.deleteNode(i);
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
						System.out.println("size: "+model.getNodeContainerSize());
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

