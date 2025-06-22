
import java.util.Scanner;

// This class is the connect four game. It takes in two players and gives them different tokens. 
// It then determines a winner based on who stacks four tokens.
public class ConnectFour extends AbstractStrategyGame {

    public static final char PLAYER_1_TOKEN = 'X';
    public static final char PLAYER_2_TOKEN = 'O';
    public static final int tie = 0;

    public static final int PLAYER_1 = 1;
    public static final int PLAYER_2 = 2;
    public static final int NUM_ROW = 6;
    public static final int NUM_COL = 7;


    public static final int GAME_IS_OVER = 1;
    public static final int GAME_NOT_OVER = -1;

    private char[][] board;
    private boolean isXTurn;

    // Behavior: 
    //   - This method constructs the connect four board game. Player 1 will go first.
    // Parameters:
    //   - N/A
    // Returns:
    //   - N/A
    // Exceptions:
    //   - N/A
    public ConnectFour(){
        this.board = new char[NUM_ROW][NUM_COL];
        for(int i =0; i < 6; i++){
            for(int j = 0; j < 7; j++){
                board[i][j] = ' ';
            }
        }
        this.isXTurn = true;
    }

    
    // Behavior: 
    //   - This method provides documentation on how to play connect four.
    // Parameters:
    //   - N/A
    // Returns:
    //   - String: the instructions
    // Exceptions:
    //   - N/A
    public String instructions(){
        String result = "";
        result += "Player 1 is X and goes first. Choose where to play by entering the number\n";
        result += "of the column (displayed by the board). Spaces shown as a number are empty.\n";
        result += "The game ends when one player marks 4 spaces vertically, horizontally" +
         "and diagonally in which that ";
        result += "player wins, or when the board is full, in which case the game end in a tie.";
        return result;
    }

    // Behavior: 
    //   - This method creates a textual representation of the game board.
    // Parameters:
    //   - N/A
    // Returns:
    //   - String: the line that divides columns
    // Exceptions:
    //   - N/A
    public String toString(){
         String result = "";
        for (int i = 0; i < board.length; i++) {
            result += "|";
            for (int j = 0; j < board[i].length; j++) {
                result += " " + board[i][j] + " |";
            }
            result += "\n";
        }
        return result;
    }

    // Behavior: 
    //   - This method gives the next player a chance to play the game.
    // Parameters:
    //   - N/A
    // Returns:
    //   - int: returns 1 the player 1 has won the game,
    //  returns 2 if player 2 wins the game,
    //  or -1 if the game is not over. If the game ended in a tie, returns 0.
    // Exceptions:
    //   - N/A
    public int getNextPlayer(){
        if (isGameOver()) {
            return GAME_IS_OVER;
        }
        return isXTurn ? PLAYER_1 : PLAYER_2;
    }

    // Behavior: 
    //   - This method will get the winner of the game depending on how many tokens
    //   - are stacked together vertically, horizontally, and diagonally. This method
    //   - also declares a tie if it occurs.
    // Parameters:
    //   - N/A
    // Returns:
    //   - int: returns 1 the player 1 has won the game,
    //  returns 2 if player 2 wins the game,
    //  or -1 if the game is not over. If the game ended in a tie, returns 0.
    // Exceptions:
    //   - N/A
    public int getWinner(){
        for(int i = 0; i < 6; i++){
            int rowWinner = getRowWinner(i);
            if(rowWinner != GAME_NOT_OVER){
                return rowWinner;
            }
        }
        for(int i = 0; i < 7; i++){
            int colWinner = getColWinner(i);
            if(colWinner != GAME_NOT_OVER){
                return colWinner;
            }
        }
        int diagWinner = getDiagWinner();
        if(diagWinner != GAME_NOT_OVER){
            return diagWinner;
        }
        return checkTie();
    }

    // Behavior: 
    //   - This method allows the players to place a token, depending on whos turn it is.
    //   - This method also checks if a player won before placing a token.Once a player
    //   - places a token, it is the next players turn.
    // Parameters:
    //   - input: the user's input
    // Returns:
    //   - N/A
    // Exceptions:
    //   - if the player does not input anything, an IllegalArgumentException is thrown.
    //   - if the player inputs an invalid column,
    //  an IllegalArgumentException is thrown.
    public void makeMove(Scanner input){
        if(input == null){
            throw new IllegalArgumentException();
        }
        char currToken = isXTurn ? PLAYER_1_TOKEN : PLAYER_2_TOKEN;
        boolean isRightMove = false;

        while(!isRightMove){
            System.out.print("Column? ");
            int col = input.nextInt() - 1;

            if (col > NUM_COL){
                throw new IllegalArgumentException();
            }

            if(col >= 0 && col < 7){
                int row = 5;
                while(row >= 0 && !isRightMove){
                    if(board[row][col] == ' '){
                        board[row][col] = currToken;
                        isRightMove = true;
                    }
                    if(getWinner() == PLAYER_1){
                        System.out.println("Player 1 wins the game.");
                    }
                    else if(getWinner() == PLAYER_2){
                        System.out.println("Player 2 wins the game.");
                    }
                    row--;
                }
            }
        }
        isXTurn = !isXTurn;
    }

    // Behavior: 
    //   - This method checks to see if a player won horizontally with four tokens in a row.
    // Parameters:
    //   - row: the row the user has four tokens in.
    // Returns:
    //   - int: 1 if player 1 wins, 2 if player 2 wins, -1 otherwise.
    //   - int: once there is a winner, a number representing whether the game is
    //   - is over or not, will be returned. 
    // Exceptions:
    //   - N/A
    private int getRowWinner(int row) {
        for(int i = 0; i < row; i++){
            for(int col = 0; col <= 3; col++){
                if (!isEmpty(row, col) && board[row][col] == board[row][col + 1] &&
                    board[row][col] == board[row][col + 2] && board[row][col] ==
                     board[row][col + 3]){
                    return getPlayer(board[row][col]);
                }
            }

        }
        return GAME_NOT_OVER;
    }

    // Behavior: 
    //   - This method checks to see if a player won vertically with four tokens in a row.
    // Parameters:
    //   - col: the column the user has four tokens in.
    // Returns:
    //   - int: 1 if player 1 won, 2 if player 2 won, -1 otherwise.
    //   - int: once there is a winner, a number representing whether the game is
    //   - is over or not, will be returned. 1 if game is over, -1 otherwise.
    // Exceptions:
    //   - N/A
    private int getColWinner(int col) {
        for(int row = 0; row < 3; row++){
            if (!isEmpty(row, col) && board[row][col] == board[row + 1][col] &&
            board[row][col] == board[row + 2][col] && board[row][col] == board[row + 3][col]){
                return getPlayer(board[row][col]);
            }
            
        }
        return GAME_NOT_OVER;
    }

    // Behavior: 
    //   - This method checks to see if a player won diagonally with four tokens in a row.
    // Parameters:
    //   - N/A.
    // Returns:
    //   - int: 1 if player 1 won, 2 if player 2 won, -1 otherwise.
    //   - int: once there is a winner, a number representing whether the game is
    //   - is over or not, will be returned. 1 if game is over, -1 otherwise.
    // Exceptions:
    //   - N/A
    private int getDiagWinner() {
        for (int row = 0; row <= 2; row++) {
            for (int col = 0; col <= 3; col++) {
                if (board[row][col] != ' ' && board[row][col] == board[row + 1][col + 1] &&
                    board[row][col] == board[row + 2][col + 2] && board[row][col] == 
                    board[row + 3][col + 3]) {
                    return board[row][col] == PLAYER_1_TOKEN ? PLAYER_1 : PLAYER_2;
                }
            }
        }
        for (int row = 3; row < 6; row++) {
            for (int col = 0; col <= 3; col++) {
                if (board[row][col] != ' ' && board[row][col] == board[row - 1][col + 1] &&
                    board[row][col] == board[row - 2][col + 2] && board[row][col] ==
                     board[row - 3][col + 3]) {
                    return board[row][col] == PLAYER_1_TOKEN ? PLAYER_1 : PLAYER_2;
                }
            }
        }
        return GAME_NOT_OVER;
    }

    // Behavior: 
    //   - This method associates the players token to the player's to differentiate them.
    // Parameters:
    //   - token: the token the user inputs.
    // Returns:
    //   - int: the player number, 1 for player 1, 2 for player 2
    // Exceptions:
    //   - N/A
    private int getPlayer(char token) {
        if (token == PLAYER_1_TOKEN) {
            return PLAYER_1;
        } else if (token == PLAYER_2_TOKEN) {
            return PLAYER_2;
        }
        return GAME_NOT_OVER;
    }

    // Behavior: 
    //   - This method checks if a tie occurs within the game, if the entire board is filled.
    // Parameters:
    //   - N/A
    // Returns:
    //   - int: returns 0 if a tie occurs
    // Exceptions:
    //   - N/A
    private int checkTie() {
        for (int col = 0; col < 7; col++){
            if(board[0][col] == ' '){
                return GAME_NOT_OVER;
            }
        }
        return tie;
    }

    // Behavior: 
    //   - This method checks if the board is empty and does not contain any tokens.
    // Parameters:
    //   - row: the row
    //   - col: the column
    // Returns:
    //   - boolean: true if the board does not have any tokens,
    //   - false if the board does have tokens.
    // Exceptions:
    //   - N/A
    private boolean isEmpty(int row, int col) {
        return board[row][col] == ' ';
    }
}