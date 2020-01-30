package uniandes.lym.robot.view;

  

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import uniandes.lym.robot.kernel.*;

/**
 * A  board provides the visualization of the robot's world
 * Implements  java.beans.PropertyChangeListener to be able to update  the board's view
 * @see kernel.RobotWorld
 */
@SuppressWarnings("serial")


// public class Board extends JPanel implements Observer
public class Board extends JPanel implements PropertyChangeListener
{
	
	/**
	 *@return  robot world that is being visualized by this component
	 */
	private RobotWorld mundo; 
	//private Observable m = new MiMundo(1,new Point(1,2),3,4);
	
	/**
	 * Visual component for the Robot World
	 */
	private JLabel[][] tabla;
	/**
	 * Boar size
	 */
	private Dimension size;
	
		 
	/**
	 * Creates the visualization object for viewing the corresponding RobotWorld
 	 * @param robotWorld
	 */
	 
	public Board(RobotWorld mundo) {
       
		this.mundo = mundo;
		this.tabla = new JLabel[mundo.getN()+1][mundo.getN()+1];
		//size
		int tam = 40*(mundo.getN()+1);
		this.size = new Dimension(tam,tam);
		this.setSize(this.size);
		this.mundo.addObserver(this);
		this.setLayout(new GridLayout(mundo.getN()+1,mundo.getN()+1,0,0));
		Images.inicializar(this);
		pintarTodo();
	}
	
	/**
	 * This method is invoked when there have been updates to the model. The view must be updated accordingly
	 * @param evt receives   Change object that indicates what cells must be repainted.
	 * @see kernel.Change
	 */
		 
	@Override
	 public void propertyChange(PropertyChangeEvent evt) {
	  	Object arg = evt.getNewValue() ;
		 if (arg instanceof Change ){
		 final Change c = (Change)arg;
		 final Point inicio = c.getStart();
         final Point fin = c.getEnd(); 
         
        
         if ((int)inicio.getX() > mundo.getN() ||(int)inicio.getY() >mundo.getN() || (int)fin.getX() > mundo.getN() ||(int)fin.getY()> mundo.getN())
		 {
		 	pintarTodo();
		 }
		 else {
		 	pintar(inicio);
		 	pintar(fin);
		 }
		}
		else if (arg instanceof Integer) {
			Integer val =  (Integer) arg;
			reset(val.intValue());
			Console.relaunch(val);
		}
		
	
    }
	
	
	 /**
	  * Draws the cell at the given position.
	  * If  it is row zero it displays the column number
	  * If it is column zero it displays the row number
	  * If there are chips or balloons it draws a balloon.
	  * If the  robot is located there, it paints the robot.
	  * @param p : the location to be drawn
	  */
	  public void pintar(Point p) {
		 int x = (int)p.getX();
		 int y = (int)p.getY();
		 boolean tieneGlobo = false;
		 String globos="no balloons"; // tool tip for the cell
		
	 String  id;
				 
		if ((x==0) || (y==0)) {
			 if ((x==0) && (y==0)) {
				 //La esquina superior izquierda, que debe ir vacia
				 id = "";
			  } else if (x==0){
				 //Estamos en la fila de arriba, hay que pintar los numeros
				 id = String.valueOf(y);
			  } else  {
				 //Estamos en la columna de la izquierda
				 id = String.valueOf(x);
			  } 
			  if(this.tabla[x][y]==null) {
				 //Si es la primera vez que  se dibuja
				 this.tabla[x][y]= new JLabel(id);
			  } else {
				 //No es la primera vez que se pone el objeto
				 this.tabla[x][y].setText(id);
			  }
		} 
		else {
			 //Un punto dentro del tablero, mirar que es, Poner el numero de globos que tiene en el tooltip
			 int numGlobos = this.mundo.contarGlobos(p);
			 if(numGlobos>0) {
			 	tieneGlobo = true;
			 	globos =  String.valueOf(numGlobos) + " balloons";
			 }
			 int orientacion1 = 0;
			 if(p.equals(this.mundo.getPosicion())) {
				// En este caso hay robot, hay que actualizar el tip
				globos = globos + ". "+ "has "+ this.mundo.getMisGlobos()+ " balloons and "+ this.mundo.getMisFichas()+ " chipss." ;
				//	Obtenemos el valor correspondiente a la orientacion del robot
				if(this.mundo.getOrientacion() == RobotWorld.NORTH){
					orientacion1 = 4;
				}else if(this.mundo.getOrientacion() == RobotWorld.SOUTH){
					orientacion1 = 12;
				}else if(this.mundo.getOrientacion() == RobotWorld.EAST){
					orientacion1 = 8;
				}else{
					orientacion1 =16;
				}
			 }
			 // Obtener la imagen correspondiente a los booleanos y a la orienteacion
			 Icon o  = Images.get(((this.mundo.hayFicha(p))?1:0) + ((tieneGlobo)?2:0) + orientacion1);
			 //En este punto ya esta en la variable o lo que hay que poner en el JLabel
			 if(this.tabla[x][y]==null) {
			 	 this.tabla[x][y]= new JLabel(o);
			 } else {
				 this.tabla[x][y].setIcon(o);
			 }
			 this.tabla[x][y].setToolTipText(globos);
		}
	 }
	 /**
	  * Pinta por primera vez el tablero, casilla por casilla
	  */
	 private void pintarTodo() {
		int a,b;
		for(b=0;b<=this.mundo.getN();b++) {
			for(a=0;a<=this.mundo.getN();a++) {
				this.pintar(new Point(a,b));
				this.add(this.tabla[a][b]);
			}
		}
	 }
	 
	 /**
	  * Devuelve el tama&ntilde;o m&iacute;mo de un tablero 
	  */
	 public Dimension getMinimumSize() {
		 return this.size;
	 }
	 /**
	  * Devuelve el tama�o que debe tener este tablero a pintarse
	  */
	 public Dimension getSize() {
		return this.size;
	 }
	 /**
	  * Devuelve el mundo al que escucha esta visualizacion
	  */
	 public RobotWorld getMundo() {
		 return this.mundo;
	 }

	 //public void reset(int dim, Point nuevaPos, int nuevosGlobos, int nuevasFichas, boolean limpiar) 
	 public void reset(int dim)
	 {
		    this.tabla = new JLabel[dim+1][dim+1];
		   int tam = 40*(mundo.getN()+1);
			this.size = new Dimension(tam,tam);
			this.removeAll();
			this.setSize(this.size);
			
			this.setLayout(new GridLayout(mundo.getN()+1,mundo.getN()+1,0,0));
			pintarTodo();
	 }

	 
	 
}
