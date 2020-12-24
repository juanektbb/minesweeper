import java.util.Random;

import java.awt.*; 
import javax.swing.*; 
import java.awt.event.*; 

class Minesweeper extends JFrame{

	static JFrame f; 
    static JPanel p;
    static JLabel gameMsg; 
     
    Cell cells[][];
    Random random = new Random();

    int rows;
    int cols;
    int mines;

    int counter_valid_cells;

    Minesweeper(int rows, int cols, int mines){
        this.rows = rows;
        this.cols = cols;
        this.mines = mines;
        this.counter_valid_cells = rows * cols - mines;
        this.cells = new Cell[rows][cols];
    }

    //Update the live cells counter
    public void update_counter_valid_cells(){
        this.counter_valid_cells -= 1;
    }


    //Get an random integer
    public Integer getRandomInt(int max_int){
    	return this.random.nextInt(max_int);
    }

    //CREATE GAME CELLS
    public void createAllCells(){
        for(int x = 0; x < this.rows; x++){
            for(int y = 0; y < this.cols; y++){
                Cell cell = new Cell(x, y);
                this.cells[x][y] = cell;
            }
        }
    }

    //CREATE GAME MINES
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
            this.numberMineNeighbors(rand_x, rand_y);

    	}
    }

    //USER LOST - REVEAL ALL CELLS
    final void revealToLose(){
        for(int x = 0; x < this.rows; x++){
            for(int y = 0; y < this.cols; y++){
                this.cells[x][y].revealContent(p, f, false);
            }
        }
    }

    //USER WON - REVEAL ALL MINES AS WIN
    final void revealToWin(){
        for(int x = 0; x < this.rows; x++){
            for(int y = 0; y < this.cols; y++){
                if(this.cells[x][y].isMine){
                    this.cells[x][y].revealToWin(p);
                }
            }
        }  
    }

    //CREATE LISTENER BUTTON OF EACH CELL
    public void createListener(JButton button, Cell cell){
        button.addActionListener(
            new ActionListener(){ 

                @Override
                public void actionPerformed(ActionEvent e){ 
                    
                    //User lost the game
                    if(cell.isMine){
                        cell.revealContent(p, f, true);
                        revealToLose();

                        gameMsg.setText("Damn, you touched a mine and exploded!");
                        gameMsg.setForeground(new Color(255, 100, 100));
                        return;

                    //User clicked on 0, recursion time
                    }else if(cell.cell_value == 0){
                        revealThisEmpty(cell);

                    //User clicked on a number
                    }else{
                        cell.revealContent(p, f, false);
                        update_counter_valid_cells();
                    }

                    //User won the game
                    if(counter_valid_cells == 0){
                        revealToWin();

                        gameMsg.setText("Congratulations, you won this game!");
                        gameMsg.setForeground(new Color(70, 170, 60));
                        return;
                    }

                    gameMsg.setText("You have " +  counter_valid_cells + (counter_valid_cells == 1 ? " cell left" : " cells left"));
                
                } 

            } 
        ); 
    }

    //RECURSIVE REVEALING FOR EMPTY CELLS
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

                //This is not a mine, so it can be revealed
                if(!this.cells[column][row].isMine){

                    //This hasn't been revealed yet    
                    if(!this.cells[column][row].revealed){

                        //This is the actual cell being checked
                        if(row == cell.y && column == cell.x){
                            cell.revealContent(p, f, false);
                            update_counter_valid_cells();
                        
                        //This cell is another empty, recursive it
                        }else if(this.cells[column][row].cell_value == 0){
                            revealThisEmpty(this.cells[column][row]);

                        //This is a number cell, only reveal it
                        }else if(this.cells[column][row].cell_value > 0){
                            this.cells[column][row].revealContent(p, f, false);
                            update_counter_valid_cells();

                        }

                    }
                
                }

            }
        }

    }

    //INCREMENT THE CELL VALUE OF ALL MINE'S NEIGHBORS
    public void numberMineNeighbors(int mine_x, int mine_y){

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

    			//Skip, this is the mine
				if(row == mine_y && column == mine_x)
					continue;

				//Skip, this is another mine
				if(this.cells[column][row].isMine)
					continue;

				this.cells[column][row].increment_cell_value();

    		}
		}

    }

    //METHOD TO CREATE THE GAME
    public void createdWindows(){

    	f = new JFrame("Minesweeper"); 
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        p = (JPanel) f.getContentPane();
        p.setLayout(null);

        this.createBoard();

        f.setSize(this.rows * 30, this.cols * 30 + 52);
        f.setLayout(null);
        f.setVisible(true);

    }

    //METHOD TO GENERATE GAME ELEMENTS
    public void createBoard(){

        this.createAllCells();
        this.createMines();

         for(int x = 0; x < this.rows; x++){
            for(int y = 0; y < this.cols; y++){
                JButton button = this.cells[x][y].drawButton();
                f.add(this.cells[x][y].button);
                this.createListener(button, this.cells[x][y]);
            }
        }

        this.setTextGameMsg();

    }

    //SET GAME TITLE
    public void setTextGameMsg(){
        gameMsg = new JLabel("You have " + this.counter_valid_cells + " cells left");
        gameMsg.setFont(new Font("Dialog", Font.BOLD, 16));
        gameMsg.setForeground(Color.BLACK);
        gameMsg.setBackground(new Color(246, 246, 246));

        p.add(gameMsg); 

        gameMsg.setBounds(0, 0, this.rows * 30, 30);
        gameMsg.setVerticalAlignment(SwingConstants.CENTER);
        gameMsg.setHorizontalAlignment(SwingConstants.CENTER);
        gameMsg.setOpaque(true);       
    }

    //MAIN TO START GAME
	public static void main(String[] args){

        if(args.length != 3){
            System.out.println("You need to run 3 integer arguments [rows, columns, mines], for example:");
            System.out.println("java Minesweeper 30 20 30");

        }else{
            int rows =  Integer.parseInt(args[0]);
            int cols = Integer.parseInt(args[1]);
            int mines = Integer.parseInt(args[2]);

            if(rows > 45){
                System.out.println("The limit of rows is 45");

            }else if(cols > 25){
                System.out.println("The limit is columns is 25");

            }else if(rows * cols <= mines){   
                System.out.println("You have " + (rows * cols) + " cells and " + mines + " mines");
                System.out.println("It is imposible to win this game");

            }else{
                Minesweeper minesweeper = new Minesweeper(rows, cols, mines);
                minesweeper.createdWindows();
	
                System.out.println("Minesweeper is running!"); 

            }

        }

	}

}