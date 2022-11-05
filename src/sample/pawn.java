package sample;

import javafx.scene.image.ImageView;
import java.util.HashSet;
import java.util.Set;

/**
 * Rikveet singh hayer
 * This class implements the pawn piece.
 */
public class pawn extends Piece {
    public pawn(Position p, ImageView img, boolean t ){
        pos = p;
        image = img;
        type = t;
        highlightList= new HashSet<>();
        power = 1;
    }

    /**
     * This method will check all the possible positions the pawn piece can go to.
     * @param board This is the chess board. In which the piece exists.
     * @return position set of possible locations.
     */
    public Set<Position> getPossiblePositions(Piece[][] board) {
        if((type&&backgroundData.whiteCheck)||(!type&&backgroundData.blackCheck)){
            return savingPositions;
        }
        positionSet = new HashSet<>();
        int x;
        int x2;
        if (type) {
            x = pos.X - 1;
            x2 = pos.X - 2;
        } else {
            x = pos.X + 1;
            x2 = pos.X + 2;
        }
        if (x>=0&&pos.Y>=0&&x<8&&pos.Y<8&&board[x][pos.Y]==null){
            positionSet.add(backgroundData.positionHashtable.get(((x * 8) + pos.Y)));
            if (nMove == 0) {//First Move
                if (x2>=0&&pos.Y>=0&&x2<8&&pos.Y<8&&board[x2][pos.Y]==null) {
                    positionSet.add(backgroundData.positionHashtable.get(((x2) * 8) + pos.Y));
                }
            }
        }
        if (x>=0&&pos.Y-1>=0&&x<8&&pos.Y-1<8&&board[x][pos.Y-1]!=null&&board[x][pos.Y-1].type!=type) {
            positionSet.add(backgroundData.positionHashtable.get(((x * 8) + pos.Y - 1)));
        }
        if (x>=0&&pos.Y+1>=0&&x<8&&pos.Y+1<8&&board[x][pos.Y+1]!=null&&board[x][pos.Y+1].type!=type) {
            positionSet.add(backgroundData.positionHashtable.get(((x * 8) + pos.Y + 1)));
        }
        return positionSet;
    }
}

