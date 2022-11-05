package sample;
import javafx.scene.image.ImageView;
import java.util.HashSet;
import java.util.Set;

/**
 * Rikveet singh hayer
 * This class returns the king's possible moves.
 */
public class rook extends Piece {
    public rook(Position p, ImageView img, boolean t ){
        pos = p;
        image = img;
        type = t;
        highlightList= new HashSet<>();
        power = 2;
    }

    public Set<Position> getPossiblePositions(Piece[][] board){
        if((backgroundData.blackCheck&&!type)||(backgroundData.whiteCheck&&type)){
            return savingPositions;
        }
        positionSet = new HashSet<>();
        for(int i= pos.X+1;i<8;i++){
           if(isValid(i,pos.Y,board)){
               positionSet.add(backgroundData.positionHashtable.get((i) * 8 + pos.Y));
            }
            if(board[i][pos.Y]!=null){
                break;
            }
        }
        for (int j= pos.Y+1;j<8;j++){
            if(isValid(pos.X,j,board)){
                positionSet.add(backgroundData.positionHashtable.get((pos.X) * 8 + j));
                }
            if(board[pos.X][j]!=null){
                break;
            }
        }
        for(int i= pos.X-1;i>=0;i--){
            if(isValid(i,pos.Y,board)){
                positionSet.add(backgroundData.positionHashtable.get((i) * 8 + pos.Y));
            }
            if(board[i][pos.Y]!=null){
                break;
            }
        }
        for (int j= pos.Y-1;j>=0;j--){
            if(isValid(pos.X,j,board)) {
                positionSet.add(backgroundData.positionHashtable.get((pos.X) * 8 + j));
            }
            if(board[pos.X][j]!=null){break;}
        }
        return positionSet;
    }
}
