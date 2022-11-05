package sample;

import javafx.scene.image.ImageView;
import java.util.HashSet;
import java.util.Set;

/**
 * Rikveet singh hayer.
 * This class checks all possible positions the knight can go to
 */
public class knight extends Piece {
    public knight(Position p, ImageView img, boolean t ){
        pos = p;
        image = img;
        type = t;
        highlightList= new HashSet<>();
        power = 3;
    }

    /**
     *
     * @param board This is the chess board. In which the piece exists.
     * @return returns the position set of possible moves.
     */
    public Set<Position> getPossiblePositions(Piece[][] board) {
        if((backgroundData.blackCheck&&!type)||(backgroundData.whiteCheck&&type)){
            return savingPositions;
        }
        positionSet = new HashSet<>();
        if(isValid(pos.X-2,pos.Y+1,board)){positionSet.add(backgroundData.positionHashtable.get(((pos.X-2)*8)+pos.Y+1));}
        if(isValid(pos.X-2,pos.Y-1,board)){positionSet.add(backgroundData.positionHashtable.get(((pos.X-2)*8)+pos.Y-1));}
        if(isValid(pos.X+2,pos.Y+1,board)){positionSet.add(backgroundData.positionHashtable.get(((pos.X+2)*8)+pos.Y+1));}
        if(isValid(pos.X+2,pos.Y-1,board)){positionSet.add(backgroundData.positionHashtable.get(((pos.X+2)*8)+pos.Y-1));}
        if(isValid(pos.X+1,pos.Y+2,board)){positionSet.add(backgroundData.positionHashtable.get(((pos.X+1)*8)+pos.Y+2));}
        if(isValid(pos.X-1,pos.Y+2,board)){positionSet.add(backgroundData.positionHashtable.get(((pos.X-1)*8)+pos.Y+2));}
        if(isValid(pos.X+1,pos.Y-2,board)){positionSet.add(backgroundData.positionHashtable.get(((pos.X+1)*8)+pos.Y-2));}
        if(isValid(pos.X-1,pos.Y-2,board)){positionSet.add(backgroundData.positionHashtable.get(((pos.X-1)*8)+pos.Y-2));}
        return positionSet;
    }
}
