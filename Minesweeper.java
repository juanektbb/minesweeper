import java.util.Random;

import java.awt.event.*; 
import java.awt.*; 
import javax.swing.*; 

class Cell{

	int x;
	int y;
	JButton button;

	Boolean isMine;
	int cell_value;

    Boolean revealed;
    String content_hidden;

	Cell(int x, int y){
		this.x = x;
		this.y = y;
        this.isMine = false;
		this.cell_value = 0;
        this.revealed = false;	
	}

	public void increment_cell_value(){
		this.cell_value += 1;
	}

    public void setThisMine(){
        this.isMine = true;
    }



	public JButton drawButton(){


		if(this.isMine){
			this.content_hidden = "M";
		}else{
            if(this.cell_value > 0)
                this.content_hidden = String.valueOf(cell_value);
            else
                this.content_hidden = "";
        }




		button = new JButton("");
		button.setBounds(this.x * 30, this.y * 30, 30, 30); 

        return button;
	}

    public void revealContent(JPanel p, JFrame f){

        this.revealed = true;
        this.button.setVisible(false);


        JLabel label = new JLabel(this.content_hidden, SwingConstants.CENTER);

        label.setFont(new Font("Serif", Font.PLAIN, 18));


        p.add(label);
        Dimension size = label.getPreferredSize();

        label.setBounds(this.x * 30, this.y * 30, 30, 30);

        if(this.isMine){
            label.setForeground(Color.RED);
        }else if(this.cell_value == 1){
            label.setForeground(Color.BLUE);
        }else if(this.cell_value == 2){
            label.setForeground(Color.GREEN);
        }else if(this.cell_value == 3){
            label.setForeground(Color.RED);
        }else if(this.cell_value == 4){
            label.setForeground(Color.ORANGE);
        }else if(this.cell_value == 0){
            label.setForeground(Color.BLACK);
        }

        label.setBackground(Color.GRAY);
        label.setOpaque(true);


        label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);

    
 
    }



}



class Minesweeper extends JFrame{

	static JFrame f; 
    static JPanel p; 
    static JLabel l; 

    Random random = new Random();
    Cell cells[][];

    int rows;
    int cols;
    int mines;


    Minesweeper(int rows, int cols, int mines){

        this.rows = rows;
        this.cols = cols;
        this.mines = mines;
        this.cells = new Cell[rows][cols];
    }


    //Get an random integer
    public Integer getRandomInt(int max_int){
    	return this.random.nextInt(max_int);
    }

    //Create game cells
    public void createAllCells(){
        for(int x = 0; x < this.rows; x++){
            for(int y = 0; y < this.cols; y++){
                Cell cell = new Cell(x, y);
                this.cells[x][y] = cell;
            }
        }
    }

    //
    public void createMines(){
    	for(int i = 0; i < this.mines; i++){

    		int rand_x = this.getRandomInt(this.rows);
    		int rand_y = this.getRandomInt(this.cols);

            //This cell is already a mine, find another
            if(this.cells[rand_x][rand_y].isMine){
                i = i - 1;
                continue;
            }

            //Make it mine and to add values to its neighbors
            this.cells[rand_x][rand_y].setThisMine();
            this.createCellValues(rand_x, rand_y);

    	}
    }


    //THE USER LOST - REVEAL ALL CELLS
    final void revealAllCells(){
        for(int x = 0; x < this.rows; x++){
            for(int y = 0; y < this.cols; y++){
                this.cells[x][y].revealContent(p, f);
            }
        }
    }


    //
    public void createListener(JButton button, Cell cell){
        button.addActionListener(
            new ActionListener() { 
                public void actionPerformed(ActionEvent e){ 


                    
                    //User just lost
                    if(cell.isMine){
                        System.out.println("YOU LOST");
                        revealAllCells();


                    }else if(cell.cell_value == 0){
                        revealThisEmpty(cell);
                    }else{
                        cell.revealContent(p, f);


                    }
                } 
            } 
        ); 
    }




    public void revealThisEmpty(Cell cell){

        //There is row above
        int on_row_start = (cell.y - 1 >= 0) ? cell.y - 1 : cell.y;

        //There is row below
        int on_row_end = (cell.y + 1 < this.cols) ? cell.y + 1 : cell.y;

        //There is column in the left
        int on_column_start = (cell.x - 1 >= 0) ? cell.x - 1 : cell.x;

        //There is column in the righ
        int on_column_end = (cell.x + 1 < this.rows) ? cell.x + 1 : cell.x;

        //Loop allow rows and then loop allow columns
        for(int row = on_row_start; row <= on_row_end; row++){
            for(int column = on_column_start; column <= on_column_end; column++){

                //This is not a mine to reveal
                if(!this.cells[column][row].isMine){

                    //Skip this value is the mine
                    if(row == cell.y && column == cell.x){
                        if(!cell.revealed)
                            cell.revealContent(p, f);
                        continue;
                    }
                    
                    //
                    if(!this.cells[column][row].revealed){
                    
                        //This cell is another empty
                        if(this.cells[column][row].cell_value == 0){
                            revealThisEmpty(this.cells[column][row]);
                            
                        }else if(this.cells[column][row].cell_value > 0){
                            this.cells[column][row].revealContent(p, f);

                        }
                    }


                
                }

            }
        }

        

    }







    public void createCellValues(int mine_x, int mine_y){

        //There is row above
        int on_row_start = (mine_y - 1 >= 0) ? mine_y - 1 : mine_y;

        //There is row below
        int on_row_end = (mine_y + 1 < this.cols) ? mine_y + 1 : mine_y;

        //There is column in the left
        int on_column_start = (mine_x - 1 >= 0) ? mine_x - 1 : mine_x;

        //There is column in the righ
        int on_column_end = (mine_x + 1 < this.rows) ? mine_x + 1 : mine_x;





		//Loop allow rows and then loop allow columns
		for(int row = on_row_start; row <= on_row_end; row++){
    		for(int column = on_column_start; column <= on_column_end; column++){

    			//Skip this value is the mine
				if(row == mine_y && column == mine_x)
					continue;

				//Skip this value is another mine
				if(this.cells[column][row].isMine)
					continue;

				this.cells[column][row].increment_cell_value();

    		}
		}

    }




    public void createdWindows(){

    	f = new JFrame("Minesweeper"); 
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        p = (JPanel) f.getContentPane();
        p.setLayout(null);

    	// 
    	this.createAllCells();
        this.createMines();

    	 for(int x = 0; x < this.rows; x++){
    		for(int y = 0; y < this.cols; y++){

    			JButton button = this.cells[x][y].drawButton();
    			f.add(this.cells[x][y].button);
                this.createListener(button, this.cells[x][y]);
    		}
    	}

    	
		f.setSize(this.rows * 30, this.cols * 30 + 20);
		f.setLayout(null);
		f.setVisible(true);

    }

    






	public static void main(String[] args){

        int mines = 100;

		Minesweeper minesweeper = new Minesweeper(50, 30, mines);
		minesweeper.createdWindows();
		


		System.out.println("Minesweeper welcome!"); 
	}

}