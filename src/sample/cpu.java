package sample;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

/**
 * Rivkeet singh hayer
 * This class implements the Ai for the assignment
 */
public class cpu {
    int i;
    int j;
    int kingW=200;
    int queenW = 9;
    int rookW = 5;
    int bishopW = 3;
    int knightW = 3;
    int pawnW = 1;
    double deadLockPawnW=0.5;
    double mobilityW = 0.1;
    Piece p;
    Piece[][] brd;
    Hashtable<String,Integer> computed;

    /**
     * This method loads the initial values for the ai.
     * @param b initial board
     * @param d depth
     * @param kW king's weight
     * @param qW queen's weight
     * @param rW rook's weight
     * @param bW bishop's weight
     * @param knW knight's weight
     * @param pW pawn's weight
     * @param dLPW the pawn at blocked position
     * @param mW mobility rate.
     */
    public cpu(Piece[][] b,int d, int kW, int qW, int rW, int bW, int knW,  int pW ,  double dLPW,  double mW){
        kingW = kW;
        queenW = qW;
        rookW = rW;
        bishopW = bW;
        knightW = knW;
        deadLockPawnW = dLPW;
        mobilityW = mW;
        pawnW = pW;
        brd = copy(b);
        computed = new Hashtable<>();
    }

    /**
     * This method evaluates the value of the board. For the player's turn. White(true) ,black(false).
     * @param b the board to be evaluated.
     * @param type the type of the player.
     * @return the calculated value.
     */
    public Integer evaluate(Piece[][] b,boolean type) {
        double moves = 0;
        double king = 0;
        double queen = 0;
        double rook = 0;
        double bishop = 0;
        double knight = 0;
        double pawn = 0;
        double isolatedPawns=0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.println(b[i][j]);
                if (b[i][j] != null) {
                    int change = 0;
                    Set<Position> p = b[i][j].getPossiblePositions(b);
                    if (b[i][j].type==type) {
                        change = +1;
                        if(p!=null) {
                            moves += p.size();
                        }
                    } else {
                        change = -1;
                        if(p!=null) {
                            moves -= p.size();
                        }
                    }
                    switch (b[i][j].power) {
                        case 1: {
                            pawn += change;
                            if(p!=null&&p.size()==0){
                               isolatedPawns+=change;
                            }
                            break;
                        }
                        case 2: {
                            rook +=change;
                            break;
                        }
                        case 3: {
                            knight +=change;
                            break;
                        }
                        case 4: {
                            bishop += change;
                            break;
                        }
                        case 5: {
                            king += change;
                            break;
                        }
                        case 6: {
                            queen += change;
                            break;
                        }
                    }
                }
            }
        }
        return (int)( ((kingW*king)+(queenW*queen)+(rookW*rook)+(bishopW*bishop)+(knightW*knight)+(pawnW*pawn))-(deadLockPawnW*isolatedPawns)+(mobilityW*moves));
    }

    /**
     * This method copies a given board.
     * @param b input board
     * @return a new copy of the board.
     */
    public Piece[][] copy(Piece[][] b){
        Piece[][] board= new Piece[b.length][b.length];
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                board[i][j]=b[i][j];
            }
        }
        return board;
    }

    /**
     * This method returns a key for the board. It takes the piece's power and its type and concatenates to a string.
     * This key is used to store already calculated board variations.
     * @param board the board
     * @return String key.
     */
    public String getKey(Piece[][] board){
        String key="";
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                if(board[i][j]==null){
                    key+="n";
                }else {
                    key += (board[i][j].power)+""+(board[i][j].type);
                }
            }
        }
        return key;
    }

    /**
     * This method gives all the types of pieces alive in the board.
     * @param b board
     * @param type type requested (white)true, black false
     * @return returns all alive pieces in the board.
     */
    public Set<Piece> getPieces(Piece[][] b,boolean type){
        Set<Piece> p = new HashSet<>();
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                if(b[i][j]!=null&&b[i][j].type==type){
                    p.add(b[i][j]);
                }
            }
        }
        return p;
    }

    /**
     * This is the min max function
     * Algo:
     *  Check if the king is check state.
     *  Check if the depth >0
     *  Create a variable value.
     *  If it white(maximizing player) Integer.max, other wise Integer.min.
     *  get all appropriate pieces on the board. For each piece get all it's possible locations.
     *  for each position. copy the board. Move the piece. Calculate the key for the iteration.
     *  if the iteration is already calculated take the current iteration.
     *  else call recursively on the new board.
     *  If it is the maximizing player. Calculate alpha.
     *  If it is the minimizing player. Calculate the beta.
     *  Check if beta<=Alpha if so stop iterating possible positions.
     * @param board current board.
     * @param depth current depth.
     * @param alpha alpha.
     * @param beta beta.
     * @param player current player.
     * @return returns the calculated value for the board called on.
     */
    public int alphaBeta(Piece[][] board,int depth,int alpha, int beta, boolean player){
    Piece bKing =backgroundData.blackKing;
    Piece wKing =backgroundData.whiteKing;
    Set<Piece> pieces = getPieces(board,player);//get all white peices
    if((wKing.isCheck(wKing.pos.X, wKing.pos.Y,board))&&backgroundData.checkBoard(board,true)==null){
        return player?Integer.MIN_VALUE:Integer. MAX_VALUE;//bad for white good for black
    }else if((bKing.isCheck(bKing.pos.X, bKing.pos.Y,board))&&backgroundData.checkBoard(board,false)==null){
        return !player?Integer.MAX_VALUE:Integer. MIN_VALUE;//bad for black good for white
    }
    else if(depth>0){
        return evaluate(board,player);
    }
    int val;
    if(player) {//white
        val = Integer.MIN_VALUE;
    }else {
        val = Integer.MAX_VALUE;
    }
    for (Piece p: pieces){
        Set<Position> positionSet = p.getPossiblePositions(board);
        for (Position pos: positionSet){
            Piece[][] newBoard = copy(board);
            boolean addFlag=false;
            newBoard[pos.X][pos.Y] = p;
            newBoard[p.pos.X][p.pos.Y] = null;
            String key = getKey(board);
            try{
                val = computed.get(key);
            }catch (Exception e){
                addFlag = true;
            }
            if(addFlag) {
                val = alphaBeta(newBoard, depth--, alpha, beta, !player);
                computed.put(key,val);
            }
            if(!player) {//Maximizing for white in black's turn
                alpha = Math.max(alpha, val);
            }else {//Minimizing for black in white's turn
                beta = Math.min(beta, val);
            }
            if(beta<=alpha) {//Prune
                break;
            }
        }
    }
    return val;
}

    /**
     * This method is run by the backgroundData class to get the best move form the ai.
     * It updates the class best values so that the backgroundData class can make the decided move.
     * For every piece on the board. It gets the possible locations. and for every iteration it calls the a
     */
    protected void calculate(){
        Set<Piece> blackPieces= new HashSet<>();
        if(backgroundData.blackKing.isCheck(backgroundData.blackKing.pos.X,backgroundData.blackKing.pos.Y,brd)){
            blackPieces = backgroundData.checkBoard(brd,false);
        }else {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (brd[i][j] != null && !brd[i][j].type) {
                        blackPieces.add(brd[i][j]);
                    }
                }
            }
        }
        int bestI=0;
        int bestJ=0;
        Piece bestP=null;
        int bestEval=Integer.MAX_VALUE;
        int currentEval;
        if(blackPieces!=null) {
            for (Piece p : blackPieces) {
                Set<Position> positions = p.getPossiblePositions(brd);
                System.out.println(p+" "+backgroundData.printPoints(positions));
                for (Position pos :positions ) {
                    Piece[][] board = copy(brd);
                    board[pos.X][pos.Y] = p;
                    board[p.pos.X][p.pos.Y]=null;
                    String key = getKey(board);
                    currentEval = alphaBeta(board,3,Integer.MIN_VALUE,Integer.MAX_VALUE,true);
                    computed.put(key,currentEval);
                    if(currentEval<bestEval){
                        bestI=pos.X;
                        bestJ=pos.Y;
                        bestP = p;
                    }
                }
            }
        }
        this.i = bestI;
        this.j = bestJ;
        this.p = bestP;
    }
}
