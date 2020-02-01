package uniandes.lym.robot.control;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import javax.swing.SwingUtilities;

import uniandes.lym.robot.kernel.*;



/**
 * Receives commands and relays them to the Robot. 
 */

public class Interpreter   
{

	/**
	 * HashMap that contains the variables
	 */
	private HashMap<String, Integer> hashVariables;

	/**
	 * Robot's world
	 */
	private RobotWorldDec world;  

	// --------------------------------------------------------------------------
	// Constants
	// --------------------------------------------------------------------------

	/** 
	 * Constants to model the directions	
	 */
	public static final int NORTH = 0;
	public static final int SOUTH = 1;
	public static final int EAST = 2;
	public static final int WEST = 3;

	public static final String RIGHT = "RIGHT";
	public static final String LEFT = "LEFT";
	public static final String AROUND = "AROUND";
	public static final String FRONT = "FRONT";
	public static final String BACK = "BACK";
	public static final String BALLOONS = "BALLOONS";
	public static final String CHIPS = "CHIPS";


	public Interpreter()
	{
	}


	/**
	 * Creates a new interpreter for a given world
	 * @param world 
	 */


	public Interpreter(RobotWorld mundo)
	{
		this.world =  (RobotWorldDec) mundo;

	}


	/**
	 * sets a the world
	 * @param world 
	 */

	public void setWorld(RobotWorld m) 
	{
		world = (RobotWorldDec) m;

	}



	/**
	 *  Processes a sequence of commands. A command is a letter  followed by a ";"
	 *  The command can be:
	 *  M:  moves forward
	 *  R:  turns right
	 *  
	 * @param input Contiene una cadena de texto enviada para ser interpretada
	 */

	public String process(String input) throws Error
	{   
		StringBuffer output=new StringBuffer("SYSTEM RESPONSE: -->\n");	
		String[] instructions = input.split(" ");
		if( instructions[0].equals("ROBOT_R") )
		{
			if( instructions[1].equals("VARS") )
			{
				String[] variables = instructions[2].split(",");
				for( String actual : variables )
					hashVariables.put(actual, null);
			}
			if( instructions[3].equals("BEGIN") )
			{
				String[] commands = instructions[4].split(";");
				for( String actualInst: commands )
				{
					if( actualInst.startsWith("assign") )
					{
						String inParentheses =actualInst.substring(7, actualInst.length()-1);
						String[] values =inParentheses.split(",");
						String name = values[0];
						int number = Integer.parseInt(values[1]);
						assignTo(name,number);
					}
					else if( actualInst.startsWith("move") )
					{
						String inParentheses1 =actualInst.substring(5, actualInst.length()-1);
						if( inParentheses1.length() == 1 )
						{
							move(inParentheses1);
						}
						else if(inParentheses1.length() ==2 )
						{
							String[] values1 =inParentheses1.split(",");
							String name1 =values1[0];
							
							if( isNumeric(values1[1]) )
							{
								int num1 =Integer.parseInt(values1[1]);
								moveInDir(name1,num1);
							}
							else if( values1[1].equals(FRONT) || values1[1].equals(RIGHT) || values1[1].equals(LEFT) || values1[1].equals(BACK) )
							{
								String var1 =values1[1];
								moveToThe(name1,var1);
							}
								
						}

					}
					else if( actualInst.startsWith("turn") )
					{
						String inParentheses2 =actualInst.substring(5, actualInst.length()-1);
						turn(inParentheses2);
					}
					else if( actualInst.startsWith("face") )
					{
						String inParentheses3 =actualInst.substring(5, actualInst.length()-1);
						int num2 =Integer.parseInt(inParentheses3);
						face(num2);
					}
					else if( actualInst.startsWith("put") )
					{
						String inParentheses4 =actualInst.substring(4, actualInst.length()-1);
						String[] values2 = inParentheses4.split(",");
						String name2 =values2[0];
						String name3 =values2[1];
						
						putNumberOf(name2,name3);
					}
					else if( actualInst.startsWith("pick") )
					{
						String inParentheses5 =actualInst.substring(5, actualInst.length()-1);
						String[] values3 = inParentheses5.split(",");
						String name4 =values3[0];
						String name5 =values3[1];
						
						pickNumberOf(name4, name5);
					}
						
				}
			}
		}
		//
		//		int i;
		//		int n;
		//		boolean ok = true;
		//		n= input.length();
		//
		//		i  = 0;
		//		try	    {
		//			while (i < n &&  ok) {
		//				switch (input.charAt(i)) {
		//				case 'M': world.moveForward(1); output.append("move \n");break;
		//				case 'R': world.turnRight(); output.append("turnRignt \n");break;
		//				case 'C': world.putChips(1); output.append("putChip \n");break;
		//				case 'B': world.putBalloons(1); output.append("putBalloon \n");break;
		//				case  'c': world.pickChips(1); output.append("getChip \n");break;
		//				case  'b': world.grabBalloons(1); output.append("getBalloon \n");break;
		//				default: output.append(" Unrecognized command:  "+ input.charAt(i)); ok=false;
		//				}
		//
		//				if (ok) {
		//					if  (i+1 == n)  { output.append("expected ';' ; found end of input; ");  ok = false ;}
		//					else if (input.charAt(i+1) == ';') 
		//					{
		//						i= i+2;
		//						try {
		//							Thread.sleep(1000);
		//						} catch (InterruptedException e) {
		//							System.err.format("IOException: %s%n", e);
		//						}
		//
		//					}
		//					else {output.append(" Expecting ;  found: "+ input.charAt(i+1)); ok=false;
		//					}
		//				}
		//
		//
		//			}
		//
		//		}
		//		catch (Error e ){
		//			output.append("Error!!!  "+e.getMessage());
		//
		//		}
		else
		{
			output.append(" Unrecognized command:  "+ instructions[0]);
		}
		return output.toString();
	}



	// --------------------------------------------------------------------------
	// Project methods
	// --------------------------------------------------------------------------

	/**
	 * 
	 * @param name
	 * @param n
	 */
	public void assignTo(String name, int n)
	{
		hashVariables.replace(name, n);
	}

	/**
	 * 
	 */
	public void move(String n)
	{
		int steps = Integer.parseInt(n);
		world.moveForward(steps);
	}

	/**
	 * 
	 */
	public void turn( String D )
	{
		if( D.equals(RIGHT) )
		{
			world.turnRight();
		}
		else if( D.equals(AROUND) )
		{
			world.turnRight();
			world.turnRight();
		}
		else if( D.equals(LEFT) )
		{
			world.turnRight();
			world.turnRight();
			world.turnRight();
		}
	}

	/**
	 * 
	 */
	public void face( int O )
	{
		int orientation = world.getOrientacion();
		if( O == 0 )
		{
			if( orientation == 1 )
			{
				world.turnRight();
				world.turnRight();
			}
			else if( orientation == 2 )
			{
				world.turnRight();
				world.turnRight();
				world.turnRight();
			}
			else if( orientation == 3 )
			{
				world.turnRight();
			}
		}
		else if( O == 1)
		{
			if( orientation == 0 )
			{
				world.turnRight();
				world.turnRight();
			}
			else if( orientation == 2 )
			{
				world.turnRight();
			}
			else if( orientation ==3 )
			{
				world.turnRight();
				world.turnRight();
				world.turnRight();
			}
		}
		else if( O == 2 )
		{
			if ( orientation == 0 )
			{
				world.turnRight();
			}
			else if( orientation == 1 )
			{
				world.turnRight();
				world.turnRight();
				world.turnRight();
			}
			else if( orientation == 3 )
			{
				world.turnRight();
				world.turnRight();
			}
		}
		else if( O == 3 )
		{
			if( orientation == 0 )
			{
				world.turnRight();
				world.turnRight();
				world.turnRight();
			}
			else if( orientation == 1 )
			{
				world.turnRight();
			}
			else if( orientation == 2 )
			{
				world.turnRight();
				world.turnRight();
			}
		}
	}

	/**
	 * Metodo corresponde a poner un numero de globos o de papas
	 * @param n corresponde a la cantidad de elementos
	 * @param ballonsChips puede poner elemento Ballons o elemento chips
	 */
	public void putNumberOf (String n, String ballonsChips){
		try{
			int number = Integer.parseInt(n);
			if(ballonsChips== BALLOONS){
				world.putBalloons(number);
			}
			else if(ballonsChips== CHIPS){
				world.putChips(number);
			}
		}
		catch(Exception e){
			int num= hashVariables.get(n);

			if(ballonsChips== BALLOONS){
				world.putBalloons(num);
			}
			else if(ballonsChips== CHIPS){
				world.putChips(num);
			}
		}
	}
	/**
	 * Metodo encargado de recoger un cierto numero de globos o papas
	 * @param n corresponde a la cantidad de elementos
	 * @param ballonsChips puede poder elementos Ballons o elementos chips 
	 */
	public void pickNumberOf (String n, String ballonsChips){
		try{
			int number = Integer.parseInt(n);
			if(ballonsChips== BALLOONS){
				world.grabBalloons(number);
			}
			else if(ballonsChips== CHIPS){
				world.pickChips(number);
			}
		}
		catch(Exception e){
			int num= hashVariables.get(n);

			if(ballonsChips== BALLOONS){
				world.grabBalloons(num);
			}
			else if(ballonsChips== CHIPS){
				world.pickChips(num);
			}
		}
	}
	/**
	 * Metodo encargado de mover el robot mirando a cierto punto sin cambiar su mirada incial	
	 * @param n cuanto se mueve 
	 * @param direction si se mueve hacia el FRONT, BACK. RIGHT, LEFT
	 */
	public void moveToThe(String n, String direction){
		try{

			int number = Integer.parseInt(n);
			if(!world.estaArriba() && direction== FRONT ){
				world.moveVertically(number);
			}
			else if(!world.estaAbajo() && direction== BACK){
				world.moveVertically(-number);
			}
			else if(!world.estaDerecha() && direction== RIGHT){
				world.moveHorizontally(number);
			}
			else if(!world.estaIzquierda() && direction== LEFT){
				world.moveHorizontally(-number);
			}
		}
		catch(Exception e){
			int num=hashVariables.get(n);
			if(!world.estaArriba() && direction== FRONT ){
				world.moveVertically(num);
			}
			else if(!world.estaAbajo() && direction== BACK){
				world.moveVertically(-num);
			}
			else if(!world.estaDerecha() && direction== RIGHT){
				world.moveHorizontally(num);
			}
			else if(!world.estaIzquierda() && direction== LEFT){
				world.moveHorizontally(-num);
			}
		}
	}

	public void moveInDir(String n, int O){
		face(O);
		move(n);
	}

	/**
	 * Checks if the parameter string is a number or not
	 * @param string
	 * @return true if the string is a number; false if it is not.
	 */
	private boolean isNumeric(String string)
	{
		try 
		{
			Integer.parseInt(string);
			return true;
		} 
		catch (NumberFormatException nfe)
		{
			return false;
		}
	}

}
