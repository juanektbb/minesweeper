import java.util.Random;

import java.awt.event.*; 
import java.awt.*; 
import javax.swing.*; 

import java.util.ArrayList;
import java.util.HashMap;

// import utils.Math


class Cell{

	int x;
	int y;
	JButton button;

	Boolean isMine;
	int cell_value;

	public Cell(int x, int y, Boolean is_mine){
		this.x = x;
		this.y = y;
		cell_value = 0;

		this.isMine = is_mine;
	}

	public JButton getButton(){
		return this.button;
	}

	public void increment_cell_value(){
		this.cell_value += 1;
	}



	public void drawButton(){

		String drawing = "";

		if(this.isMine){
			drawing = "M";

		}else if(cell_value != 0){
			drawing = String.valueOf(cell_value);
		}




		button = new JButton(drawing);
		button.setBounds(this.x * 30, this.y * 30, 30, 30); 
	}



	public int returnX(){
		return this.x;
	}


}


class Mine{
	int coorX;
	int coorY;

	public Mine(int coor_x, int coor_y){
		this.coorX = coor_x;
		this.coorY = coor_y;
	}

	public int getCoorX(){
		return this.coorX;
	}
}




class Minesweeper extends JFrame{

	static JFrame f; 
  
    // label to display text 
    static JLabel l; 

    Random random = new Random();

    ArrayList<Mine> mines = new ArrayList<Mine>();

    Cell cells[][] = new Cell[14][14];


    //Get an random integer
    public Integer getRandomInt(int max_int){
    	return this.random.nextInt(max_int);
    }

    //Check if this mine exists in ArrayList
    public Boolean mine_exists(int x, int y){
    	Mine mine = this.mines.stream()
    		.filter(iMine -> (x == iMine.coorX && y == iMine.coorY))
    		.findFirst()
    		.orElse(null);

    	return (mine != null) ? true : false;
    }


    public void createMines(){
    	for(int i = 0; i < 10; i++){

    		int rand_x = this.getRandomInt(14);
    		int rand_y = this.getRandomInt(14);

			Boolean exists = this.mine_exists(rand_x, rand_y);

			//This mine exists, create another one
			if(exists){
				i = i - 1;
				continue;
			}

    		Mine mine = new Mine(rand_x, rand_y);
			this.mines.add(mine);

    	}
    }



    public void createAllCells(){

    	for(int x = 0; x < 14; x++){
    		for(int y = 0; y < 14; y++){

    			Boolean is_mine = this.mine_exists(x, y);

    			Cell cell = new Cell(x, y, is_mine);
    			this.cells[x][y] = cell;
    			// f.add(cell.button);
    		}
    	}


    }







    public void createCellValues(){
    	for(Mine mine : mines){

    		Boolean above = false;
    		Boolean left = false;
    		Boolean right = false;
    		Boolean bottom = false;

    		//There is row above
    		if(mine.coorY - 1 >= 0)
    			above = true;

    		//There is column in the left
    		if(mine.coorX - 1 >= 0)
    			left = true;

    		//There is column in the righ
    		if(mine.coorX + 1 < 14)
    			right = true;

			//There is row below
    		if(mine.coorY + 1 < 14)
    			bottom = true;

    		int on_row_start = (above) ? mine.coorY - 1 : mine.coorY;
    		int on_row_end = (bottom) ? mine.coorY + 1 : mine.coorY;

    		int on_column_start = (left) ? mine.coorX - 1 : mine.coorX;
    		int on_column_end = (right) ? mine.coorX + 1 : mine.coorX;

    		for(int row = on_row_start; row <= on_row_end; row++){
	    		for(int column = on_column_start; column <= on_column_end; column++){

	    			//Skip this value is the mine
					if(row == mine.coorY && column == mine.coorX)
						continue;

					//Skip this value is another mine
					if(this.cells[column][row].isMine)
						continue;

					this.cells[column][row].increment_cell_value();

	    		}
    		}

    	}
    }


    public void createdWindows(){
    	f = new JFrame("Minesweeper"); 

    	this.createMines();
    	this.createAllCells();
    	this.createCellValues();

    	 for(int x = 0; x < 14; x++){
    		for(int y = 0; y < 14; y++){

    			this.cells[x][y].drawButton();

    			f.add(this.cells[x][y].button);
    		}
    	}

    	
		f.setSize(420, 420 + 20);
		f.setLayout(null);
		f.setVisible(true);

    }





	public static void main(String[] args){



		Minesweeper minesweeper = new Minesweeper();
		minesweeper.createdWindows();
		


		System.out.println("Minesweeper welcome!"); 
	}

}