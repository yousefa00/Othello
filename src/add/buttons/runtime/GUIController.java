/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package add.buttons.runtime;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

/**
 *
 * @author rcortez
 */
public class GUIController implements Initializable {
    private boolean notRan = true;
    private ImageView[][] btn = new ImageView[8][8];           // Technically these values can be changed to anything but i didt have time to 
    private int[][] board = new int[btn.length][btn[0].length];// add this as a feature
    private String[] paths = {"resources/square.png", "resources/black.png", "resources/white.png", "resources/blacksmall.png"};
    private int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}, {1, 1}, {-1, -1}, {1, -1}, {-1, 1}};  //Array of all directions
    private int pTurn = 1, countNoMove = 0;
    
    @FXML
    private MenuItem startMenu, resetMenu;
    
    @FXML
    private Menu editTab;
    
    @FXML
    private CheckMenuItem guideMenu;
    
    @FXML                                       //Sets up necessary instance fields
    private GridPane gPane;

    @FXML
    private Label title, author, turn;
    
    public GUIController(){
        for (int i = 0; i < btn.length; i++){
            for (int j = 0; j < btn[0].length; j++){
                board[i][j] = 0;
            }
        }
        
        board[btn.length / 2][btn[0].length / 2] = 1;
        board[btn.length / 2 - 1][btn[0].length / 2 - 1] = 1;
        board[btn.length / 2][btn[0].length / 2 - 1] = -1;      // Sets middle starting squares
        board[btn.length / 2 - 1][btn[0].length / 2] = -1;
        
    }
    
    @FXML
    private void reset(){
        for (int i = 0; i < btn.length; i++){
            for (int j = 0; j < btn[0].length; j++){
                board[i][j] = 0;
            }
        }
        
        board[btn.length / 2][btn[0].length / 2] = 1;
        board[btn.length / 2 - 1][btn[0].length / 2 - 1] = 1;
        board[btn.length / 2][btn[0].length / 2 - 1] = -1;          // Resets everything
        board[btn.length / 2 - 1][btn[0].length / 2] = -1;
        guideMenu.setSelected(false);
        notRan = true;
        turn.setText("Player " + pTurn + ", it is your turn");
        updateUI();
    }
    
    @FXML
    private void updateUI(){
        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board[0].length; j++){
                switch (board[i][j]){
                    case 0:
                        btn[i][j].setImage(new Image(paths[0]));
                        break;
                    case 1:
                        btn[i][j].setImage(new Image(paths[1]));
                        break;                                          // Sets all images based on data array
                    case -1:
                        btn[i][j].setImage(new Image(paths[2]));
                        break;
                    case 2:
                        btn[i][j].setImage(new Image(paths[3]));
                }    
            }    
        }
    }
    
    private void randPlayer(){
        pTurn = (int)( Math.random() * 2) + 1;
        turn.setText("Player " + pTurn + ", it is your turn");      // Gets a random player
        turn.setVisible(true);
    }
    
    private void checkWin(){
        int countW = 0;
        int countB = 0;
        for (int i = 0; i < btn.length; i++){
            for (int j = 0; j < btn[0].length; j++){
                if (board[i][j] == 1){
                    countB++;                           // Adds all of the numbers of pieces and finds winner
                }
                else if (board[i][j] == -1){
                    countW++;
                }
            }
        }
        
        if (countW == countB){
            turn.setText("It was a tie");
        }
        else if (countW > countB){
            turn.setText("Player 2, you win");
        }
        else{
            turn.setText("Player 1, you win");
        }
    }
    
    @FXML
    private void setup() {
        startMenu.setVisible(false);
        resetMenu.setVisible(true);
        editTab.setVisible(true);
        title.setVisible(false);
        author.setVisible(false);
    
        for(int i=0; i<btn.length; i++){
            for(int j=0; j<btn[0].length;j++){
                    btn[i][j] = new ImageView();
                    btn[i][j].setFitHeight(50);
                    btn[i][j].setFitWidth(50);          // Sets up everything for images
                    gPane.add(btn[i][j], j, i);

                }
        }
        gPane.setGridLinesVisible(true);
        gPane.setVisible(true);
        updateUI();
        randPlayer();
        
        EventHandler z = new EventHandler<MouseEvent>() 
        {
            
            @Override
            public void handle(MouseEvent t) 
            {
                int r = GridPane.getRowIndex(((ImageView) t.getSource()));
                int c = GridPane.getColumnIndex(((ImageView) t.getSource()));
                if (checkValid(r, c)){
                   if (pTurn == 1){
                        board[r][c] = 1;  
                        pTurn = 2;
                    }
                    else if (pTurn == 2){
                        board[r][c] = -1;
                        pTurn = 1;
                    }
                   turn.setText("Player " + pTurn + ", it is your turn");
                    for (int[] move : directions){
                        checkDir(move[0], move[1], r, c);
                    }                                                   // Runs necessary methods when user presses a button
                    if (guideMenu.isSelected()){
                        addGuide();
                    }
                    int test = 0;
                    addGuide();
                    for (int i = 0; i < btn.length; i++){
                            for (int j = 0; j < btn[0].length; j++){
                                if (board[i][j] == 2 || notRan){
                                    test++;
                                    notRan = false;
                                    if (!guideMenu.isSelected()){
                                        board[i][j] = 0;
                                    }
                                }
                            }
                        }
                    System.out.println(test);
                    if (test == 0){
                        if (pTurn == 1){
                            pTurn = 2;
                        }
                        else {
                            pTurn = 1;
                        }
                        turn.setText("Player " + pTurn + ", it is your turn");
                        countNoMove++;
                    }
                    if (countNoMove == 2){
                        checkWin();
                    }
                            }
                            updateUI();
                        }
            
        };
        for(int i=0; i<btn.length; i++){
                for(int j=0; j<btn[0].length;j++){
                    btn[i][j].setOnMouseClicked(z);     // Adds an eventlistener to all of the imageviews
                }
        }    
    }
    
    private boolean checkValid(int r, int c){
        int color;
        if (pTurn == 1){
            color = pTurn;
        }
        else{
            color = -1;
        }    
        for (int[] move: directions){
            int numR = move[0]; int numRPer = move[0];      // Checks valid moves using direction array
            int numC = move[1]; int numCPer = move[1];
            if (board[r][c] == -1 || board[r][c] == 1){
                return false;
            }
            while (r + numR < btn.length - 1 && c + numC < btn[0].length - 1 && r + numR >= 1 && c + numC >= 1){
                if (board[r + numR][c + numC] == 0 || board[r + numR][c + numC] == 2){
                    break;
                }
                if (board[r + numR][c + numC] == color){
                    break;
                }
                if (board[r + numR][c + numC] == (color * -1) && board[r + numR + numRPer][c + numC + numCPer] == color){
                    return true;
                }
                numR += move[0];
                numC += move[1];
            }
            
            
        }
        return false;
    }
    
    @FXML
    private void addGuide(){
        for (int i = 0; i < btn.length; i++){
            for (int j = 0; j < btn[0].length; j++){
                if (board[i][j] == 2){
                    board[i][j] = 0;
                }
            }
        }
        int color;
        if (pTurn == 1){
            color = pTurn;          // Adds guide in possible places
        }
        else{
            color = -1;
        }
        for (int i = 0; i < btn.length; i++){
            for (int j = 0; j < btn[0].length; j++){
                if (board[i][j] != color){
                    continue;
                }
                for (int[] move: directions){
                    int numR = move[0]; int numRPer = move[0];
                    int numC = move[1]; int numCPer = move[1];
                    while (i + numR < btn.length - 1 && j + numC < btn[0].length - 1 && i + numR >= 1 && j + numC >= 1){
                        if (board[i + numR][j + numC] == 0){
                            break;
                        }
                        if (board[i + numR][j + numC] == color){
                            break;
                        }
                        if (board[i + numR][j + numC] == (color * -1) && board[i + numR + numRPer][j + numC + numCPer] == 0){
                            board[i + numR + numRPer][j + numC + numCPer] = 2;
                            countNoMove = 0;
                        }
                        numR += move[0];
                        numC += move[1];
                        }   
                    }
            }
        }
        updateUI();
    }
    
    private void checkDir(int i, int z, int row, int col){
        boolean run = true;
        int color = board[row][col];
        int rowPerm = row;
        int colPerm = col;
        while (run){       // Checks all directions and flips squares
            row += i;
            col += z;
            if (row >= btn.length || col >= btn[0].length || row < 0 || col < 0){
                break;
            }
            if (board[row][col] == 0){
                break;
            }
            if (color == (board[row][col])){
                run = false;
                for (int[] points = {rowPerm, colPerm}; points[0] != row || points[1] != col; points[0] += i){
                    board[points[0]][points[1]] = color;
                    points[1] += z;
                }
            }
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
}
