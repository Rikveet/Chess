package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.IOException;

/**
 * Name: Rikveet singh hayer. 6590327
 * This class loads the main board and places the chess pieces into the appropriate locations.
 * To change the location for test purposes change the place pieces method.
 */
public class SinglePlayer {
    public TextField name;
    public TextField depth;
    public TextField fileName;

    /**
     * Controller function for the single player screen.
     * @param actionEvent
     * @throws IOException
     */
    public void play(ActionEvent actionEvent) throws IOException {
        boolean valid = false;
        int d=3;
        if(name.getText().isEmpty()||depth.getText().isEmpty()){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText("Invalid Input!!");
            a.setContentText("Player name cannot be empty");
            a.show();
        }
        else{
            try {
                d = Integer.parseInt(depth.getText());
                backgroundData.setPlayerName(name.getText());
                valid = true;
            }
            catch (Exception e){
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setHeaderText("Invalid Input!!");
                a.setContentText("depth is only int");
                a.show();
            }

        }
        if(valid){
            Parent root = FXMLLoader.load(getClass().getResource("board.fxml"));
            backgroundData.setScene(root,"Game");
            GridPane gridPane = ((GridPane) root.getChildrenUnmodifiable().get(2));
            backgroundData.setBoardDisplay(gridPane);
            //place the pieces
            Piece[][] board = new Piece[8][8];
            //Place pieces
            for (int i=0;i<8;i++){
                for (int j=0;j<8;j++){
                    backgroundData.positionHashtable.put((i*8)+j,new Position(i,j));
                }
            }
            placePieces(board);
            int kingWeight=200;
            int queenWeight=9;
            int rookWeight=5;
            int bishopWeight=3;
            int knightWeight=3;
            int pawnWeight=1;
            double pawnlock=0.5;
            double mobility= 0.1;
            backgroundData.setParameters(d,kingWeight,queenWeight,rookWeight,bishopWeight,knightWeight,pawnWeight,pawnlock,mobility);
            System.out.println("Hi");
        }
    }

    /**
     * Sends the user back to the main screen.
     * @param actionEvent
     * @throws IOException
     */
    public void back(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("mainscreen.fxml"));
        backgroundData.setScene(root,"Chess by Rikveet Singh Hayer");
        backgroundData.setPlayerName("");
        backgroundData.setFileName("");
    }

    /**
     * Closes the program
     * @param actionEvent
     */
    public void quit(ActionEvent actionEvent) {
        backgroundData.Quit();
    }

    /**
     * This method places the pieces on the board
     * Please follow the method used to change pieces locations.
     * @param board the board storing all the pieces.
     */
    public void placePieces(Piece[][] board){
        //pawns
        for(int j=0;j<8;j++){
            board[1][j] = new pawn(backgroundData.positionHashtable.get((1*8)+j),new ImageView(new Image(String.valueOf(getClass().getResource("Pieces/Black/pawn.png")))),false);
            board[6][j] = new pawn(backgroundData.positionHashtable.get((6*8)+j),new ImageView(new Image(String.valueOf(getClass().getResource("Pieces/White/pawn.png")))),true);
        }
        //rooks
        board[0][0] = new rook(backgroundData.positionHashtable.get((0*8)+0),new ImageView(new Image(String.valueOf(getClass().getResource("Pieces/Black/rook.png")))),false);
        board[0][7] = new rook(backgroundData.positionHashtable.get((0*8)+7),new ImageView(new Image(String.valueOf(getClass().getResource("Pieces/Black/rook.png")))),false);
        board[7][0] = new rook(backgroundData.positionHashtable.get((7*8)+0),new ImageView(new Image(String.valueOf(getClass().getResource("Pieces/White/rook.png")))),true);
        board[7][7] = new rook(backgroundData.positionHashtable.get((7*8)+7),new ImageView(new Image(String.valueOf(getClass().getResource("Pieces/White/rook.png")))),true);
        //knights
        board[0][1] = new knight(backgroundData.positionHashtable.get((0*8)+1),new ImageView(new Image(String.valueOf(getClass().getResource("Pieces/Black/horse_fRight.png")))),false);
        board[0][6] = new knight(backgroundData.positionHashtable.get((0*8)+6),new ImageView(new Image(String.valueOf(getClass().getResource("Pieces/Black/horse_fLeft.png")))),false);
        board[7][1] = new knight(backgroundData.positionHashtable.get((7*8)+1),new ImageView(new Image(String.valueOf(getClass().getResource("Pieces/White/horse_fLeft.png")))),true);
        board[7][6] = new knight(backgroundData.positionHashtable.get((7*8)+6),new ImageView(new Image(String.valueOf(getClass().getResource("Pieces/White/horse_fRight.png")))),true);
        //bishop 2,5
        board[0][2] = new bishop(backgroundData.positionHashtable.get((0*8)+2),new ImageView(new Image(String.valueOf(getClass().getResource("Pieces/Black/bishop.png")))),false);
        board[0][5] = new bishop(backgroundData.positionHashtable.get((0*8)+5),new ImageView(new Image(String.valueOf(getClass().getResource("Pieces/Black/bishop.png")))),false);
        board[7][2] = new bishop(backgroundData.positionHashtable.get((7*8)+2),new ImageView(new Image(String.valueOf(getClass().getResource("Pieces/White/bishop.png")))),true);
        board[7][5] = new bishop(backgroundData.positionHashtable.get((7*8)+5),new ImageView(new Image(String.valueOf(getClass().getResource("Pieces/White/bishop.png")))),true);
        //queen 4
        board[0][4] = new queen(backgroundData.positionHashtable.get((0*8)+4),new ImageView(new Image(String.valueOf(getClass().getResource("Pieces/Black/queen.png")))),false);
        board[7][4] = new queen(backgroundData.positionHashtable.get((7*8)+4),new ImageView(new Image(String.valueOf(getClass().getResource("Pieces/White/queen.png")))),true);
        // king 3
        board[0][3] = new king(backgroundData.positionHashtable.get((0*8)+3),new ImageView(new Image(String.valueOf(getClass().getResource("Pieces/Black/king.png")))),false);
        board[7][3] = new king(backgroundData.positionHashtable.get((7*8)+3),new ImageView(new Image(String.valueOf(getClass().getResource("Pieces/White/king.png")))),true);
        backgroundData.blackKing = board[0][3];
        backgroundData.whiteKing = board[7][3];
        backgroundData.setCurrentPlayer(true);
        backgroundData.setBoard(board);
    }
}
