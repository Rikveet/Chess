package sample;

import javafx.scene.image.ImageView;
import java.util.HashSet;
import java.util.Set;

/**
 * Rikveet singh hayer
 * This class implements the bishop/
 */
public class bishop extends Piece{
    public bishop(Position p, ImageView img, boolean t ){
        pos = p;
        image = img;
        type = t;
        highlightList= new HashSet<>();
        power = 4;
    }

    /**
     * This method returns all possible location set for the bishop.
     * @param board This is the chess board. In which the piece exists.
     * @return set of all position positions the piece can go to.
     */
    public Set<Position> getPossiblePositions(Piece[][] board){
        if((backgroundData.blackCheck&&!type)||(backgroundData.whiteCheck&&type)){
            return savingPositions;
        }
        positionSet = new HashSet<>();
        int i; int j;
        i= pos.X+1;
        j= pos.Y-1;
        while(i<8&&j>=0&&isValid(i,j,board)){
            positionSet.add(backgroundData.positionHashtable.get(((i)*8)+j));
            if(board[i][j]!=null){break;}
            i++;
            j--;
        }
        i= pos.X+1;
        j= pos.Y+1;
        while(i<8&&j<=8&&isValid(i,j,board)){
            positionSet.add(backgroundData.positionHashtable.get(((i)*8)+j));
            if(board[i][j]!=null){break;}
            i++;
            j++;
        }
        i= pos.X-1;
        j= pos.Y-1;
        while(i>=0&&j>=0&&isValid(i,j,board)){
            positionSet.add(backgroundData.positionHashtable.get(((i)*8)+j));
            if(board[i][j]!=null){break;}
            i--;
            j--;
        }
        i= pos.X-1;
        j= pos.Y+1;
        while(i>=0&&j<8&&isValid(i,j,board)){
            positionSet.add(backgroundData.positionHashtable.get(((i)*8)+j));
            if(board[i][j]!=null){break;}
            i--;
            j++;
        }
       return positionSet;
    }
}
