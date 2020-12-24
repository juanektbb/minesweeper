import java.awt.*; 
import javax.swing.*; 

class Cell{

	int x;
	int y;
	JButton button;
    JLabel label;

	Boolean isMine;
	int cell_value;

    Boolean revealed;
    String content_hidden;

    Color cell_1 = new Color(1, 0, 254);
    Color cell_2 = new Color(40, 127, 0);
    Color cell_3 = new Color(245, 14, 2);
    Color cell_4 = new Color(2, 0, 128);
    Color cell_5 = new Color(129, 3, 2);
    Color cell_6 = new Color(38, 128, 129);
    Color cell_7 = new Color(0, 0, 0);
    Color cell_8 = new Color(128, 128, 128);

    Color win = new Color(185, 220, 185);
    Color lose = new Color(255, 100, 100);
    Color lose_2 = new Color(230, 205, 205);
    Color bg_cell = new Color(192, 192, 192);
    Color border_cell = new Color(128, 128, 128);
    
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

    //DRAW BUTTON, ONLY CALLED WHEN THE CELL SETUP IS FINISHED 
	public JButton drawButton(){

        //Set the cell visible content
		if(this.isMine){
			this.content_hidden = "@";
		}else{
            if(this.cell_value > 0)
                this.content_hidden = String.valueOf(cell_value);
            else
                this.content_hidden = "";
        }

        //Hide that content for now
		button = new JButton("");
		button.setBounds(this.x * 30, this.y * 30 + 30, 30, 30); 

        return button;
	}

    //REVEAL ALL THE MINES AS A WIN
    public void revealToWin(JPanel p){

        this.revealed = true;
        this.button.setVisible(false);

        label = new JLabel("^");
        label.setFont(new Font("Dialog", Font.BOLD, 18));
        label.setBounds(this.x * 30, this.y * 30 + 30, 30, 30);
        label.setForeground(cell_2);
        label.setBackground(win);
        label.setOpaque(true);
        label.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);

        p.add(label);

    }

    //REVEAL THIS CELL'S CONTENT
    public void revealContent(JPanel p, JFrame f, Boolean touched){

        this.revealed = true;
        this.button.setVisible(false);

        label = new JLabel(this.content_hidden);

        if(!this.isMine)
            label.setFont(new Font("Dialog", Font.BOLD, 20));
        else
            label.setFont(new Font("Dialog", Font.BOLD, 16));

        label.setBounds(this.x * 30, this.y * 30 + 30, 30, 30);

        if(!this.isMine){
            switch(this.cell_value){
                case 1:
                    label.setForeground(cell_1); break;
                case 2:
                    label.setForeground(cell_2); break;
                case 3:
                    label.setForeground(cell_3); break;
                case 4:
                    label.setForeground(cell_4); break;
                case 5:
                    label.setForeground(cell_5); break;
                case 6:
                    label.setForeground(cell_6); break;
                case 7:
                    label.setForeground(cell_7); break;
                case 9:
                    label.setForeground(cell_8); break;
            }
        }

        //Touched means, this is a mine and user just lost
        if(!touched && this.isMine)
            label.setBackground(lose_2);
        else if(!touched)
            label.setBackground(bg_cell);
        else
            label.setBackground(lose);

        label.setOpaque(true);
        label.setBorder(BorderFactory.createLineBorder(border_cell, 1));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);

        p.add(label);
        
    }

}