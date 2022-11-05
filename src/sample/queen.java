package sample;

import javafx.scene.image.ImageView;
import java.util.HashSet;
import java.util.Set;
/**
 * Rikveet singh hayer
 * This class implements the queen
 */
public class queen extends Piece {
    public queen(Position p, ImageView img, boolean t ){
        pos = p;
        image = img;
        type = t;
        highlightList= new HashSet<>();
        power = 6;
    }

    /**
     * This function is combination of rook and bishop + the edges.
     * @param board This is the chess board. In which the piece exists.
     * @return possible position pieces set.
     */
    public Set<Position> getPossiblePositions(Piece[][] board){
        if((backgroundData.blackCheck&&!type)||(backgroundData.whiteCheck&&type)){
            return savingPositions;
        }
        positionSet= new HashSet<>();
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
            if(isValid(pos.X,j,board)){
                positionSet.add(backgroundData.positionHashtable.get((pos.X) * 8 + j));}
            if(board[pos.X][j]!=null){break;}
        }
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
