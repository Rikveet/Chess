package sample;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.util.Set;
//1 pawn
//2 rook
//3 knight
//4 bishop
//5 king
//6 queen

/**
 * Rikveet singh hayer
 * This class is an abstract class for pieces
 * It stores and performs the basic functions that every piece must do
 */

public abstract class Piece {
    public Position pos;
    public ImageView image;
    public boolean type;//white or black
    public Set<Pane> highlightList;
    public Set<Position> positionSet;
    public boolean saver;
    public Set<Position> savingPositions;
    public int power;
    public int nMove=0;

    /**
     * Returns all the possible locations a piece can go to on the given board.
     * @param board This is the chess board. In which the piece exists.
     * @return the function returns the set of possible x,y values that the piece can go to.
     */
    public Set<Position> getPossiblePositions(Piece[][] board){return null;}

    /**
     * This method highlight a single square. Where the piece can go to.
     * @param highlight Highlighted squares(Gui Grid).
     * @param ci current ith location on the Piece[][] board.
     * @param cj current jth location on the Piece[][] board.
     * @param ni new ith location.
     * @param nj new jth location.
     */
    public void highlight(GridPane highlight,final int ci,final int cj,final int ni,final int nj){
        ImageView mark = new ImageView(new Image(String.valueOf(getClass().getResource("Pieces/highlight.png"))));
        mark.setOnMouseClicked(event -> {
            backgroundData.movePiece(ci,cj,ni,nj,this);
        });
        mark.setOpacity(0.25);
        ((Pane)(highlight.getChildren().get((ni*8)+nj))).getChildren().add(mark);
    }

    /**
     * This method highlights all possible positions a piece can go to.
     * @param highlight Highlighted squares(Gui Grid).
     * @param board the board to highlight for.
     */
    public void highlightPossiblePositions(GridPane highlight, Piece[][] board){
        backgroundData.clearHighlights();
        getPossiblePositions(board);
        for (Position p :  positionSet) {
                highlight(highlight, pos.X, pos.Y, p.X, p.Y);
                highlightList.add((Pane) (highlight.getChildren().get((p.X * 8) + p.Y)));
        }
        backgroundData.setCurrentlyHighlighted(highlightList);
    }

    /**
     * This method is only executed when the relative king is in check and this piece move to save it.
     * @param highlight Highlighted squares(Gui Grid).
     */
    public void highlightSavingPositions(GridPane highlight){
        backgroundData.clearHighlights();
        for (Position p :  savingPositions) {
            highlight(highlight, pos.X, pos.Y, p.X, p.Y);
            highlightList.add((Pane) (highlight.getChildren().get((p.X * 8) + p.Y)));
        }
        backgroundData.setCurrentlyHighlighted(highlightList);
    }

    /**
     * Checks if a move is valid. If there is no friendly piece on the new location.
     * @param i New i location on the board
     * @param j New j location on the board
     * @param board  new location
     * @return true if valid.
     */
    public boolean isValid(int i, int j, Piece[][] board){
        if(i>=0&&i<8&&j>=0&&j<8) {
            if(board[i][j] == null){
                return true;
            }
            else return board[i][j].type != type;
        }
        return false;
    }

    /**
     * This method is specifically for the king piece which returns true if any enemy pieces on the board can attack it.
     * @param x x location to check on.
     * @param y y location to check on.
     * @param board the main board.
     * @return returns true if the king is in check state.
     */
    public boolean isCheck(int x,int y, Piece[][] board){return false;}
}
