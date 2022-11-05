package sample;

import javafx.scene.image.ImageView;
import java.util.HashSet;
import java.util.Set;

/**
 * Rikveet singh hayer
 * This class returns the king's possible moves.
 */
public class king extends Piece{
    public king(Position p, ImageView img, boolean t ){
        pos = p;
        image = img;
        type = t;
        highlightList= new HashSet<>();
        power = 5;
    }

    /**
     * Checks if king has a valid move.
     * @param board This is the chess board. In which the piece exists.
     * @return returns set of possible positions a king can go to.
     */
    public Set<Position> getPossiblePositions(Piece[][] board){
        positionSet = new HashSet<>();
        if(isValid(pos.X,pos.Y+1,board)&&!isCheck(pos.X,pos.Y+1, board)) {
            positionSet.add(backgroundData.positionHashtable.get((pos.X*8)+pos.Y+1));
        }
        if(isValid(pos.X,pos.Y-1,board)&&!isCheck(pos.X,pos.Y-1, board)){
            positionSet.add(backgroundData.positionHashtable.get((pos.X*8)+pos.Y-1));}
        if(isValid(pos.X+1,pos.Y+1,board)&&!isCheck(pos.X+1,pos.Y+1, board)){
            positionSet.add(backgroundData.positionHashtable.get(((pos.X+1)*8)+pos.Y+1));}
        if(isValid(pos.X+1,pos.Y,board)&&!isCheck(pos.X+1,pos.Y, board)){
            positionSet.add(backgroundData.positionHashtable.get(((pos.X+1)*8)+pos.Y));}
        if(isValid(pos.X+1,pos.Y-1,board)&&!isCheck(pos.X+1,pos.Y-1, board)){
            positionSet.add(backgroundData.positionHashtable.get(((pos.X+1)*8)+pos.Y-1));}
        if(isValid(pos.X-1,pos.Y+1,board)&&!isCheck(pos.X-1,pos.Y+1, board)){
            positionSet.add(backgroundData.positionHashtable.get(((pos.X-1)*8)+pos.Y+1));}
        if(isValid(pos.X-1,pos.Y,board)&&!isCheck(pos.X-1,pos.Y, board)){
            positionSet.add(backgroundData.positionHashtable.get(((pos.X-1)*8)+pos.Y));}
        if(isValid(pos.X-1,pos.Y-1,board)&&!isCheck(pos.X-1,pos.Y-1, board)){
            positionSet.add(backgroundData.positionHashtable.get(((pos.X-1)*8)+pos.Y-1));}
        return positionSet;
    }

    /**
     * This method is responsible for checking if king is under check.
     * It gets possible position of all enemies and checks if the king's position exists.
     * @param x x location to check on.
     * @param y y location to check on.
     * @param board the main board.
     * @return returns true if so
     */
    public boolean isCheck(int x, int y, Piece[][] board){
        if(backgroundData.currentPlayer==type) {
            Piece temp = board[x][y];
            board[pos.X][pos.Y] = null;
            board[x][y] = this;
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (board[i][j] != null && board[i][j].type != type) {
                        Set<Position> enemyPlayerPossiblePositions = board[i][j].getPossiblePositions(board);
                        if (enemyPlayerPossiblePositions!=null&&enemyPlayerPossiblePositions.contains(backgroundData.positionHashtable.get((x * 8) + y))) {
                           board[x][y] = temp;
                            board[pos.X][pos.Y] = this;
                            return true;
                        }
                    }
                }
            }
            board[x][y] = temp;
            board[pos.X][pos.Y] = this;
            return false;
        }
        return false;
    }
}
