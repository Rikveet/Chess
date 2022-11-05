package sample;

import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

/**
 * Rikveet singh hayer
 * This class the engine of the game.
 * It handle check mate, moving a players etc.
 */
public class backgroundData {
    static Stage primaryStage;
    static String playerName;
    static String fileName;
    static Piece[][] board;
    static Piece blackKing;
    static Piece whiteKing;
    static GridPane highlight;
    static boolean currentPlayer;//True is 1, false is 2.
    static boolean blackCheck=false;
    static boolean whiteCheck=false;
    static boolean isAiPlaying=true;
    static int winner=-1;
    static int staleMate=-1;
    static Set<Pane> currentlyHighlighted;
    static Hashtable<Integer,Piece> currentLocation = new Hashtable<>();
    static Hashtable<Integer, Position> positionHashtable = new Hashtable<>();
    static Position cBlackDeathPoint= new Position(0,0);
    static Position cWhiteDeathPoint = new Position(0,0);
    static Set<Piece> potentialSavers= new HashSet<>();
    static int depth;
    static int kingW;
    static int queenW;
    static int rookW;
    static int bishopW;
    static int knightW;
    static int pawnW;
    static double deadLockPawnW;
    static double mobilityW;

    /**
     * This method is used in initializing the gui
     * @param s Stage of the board.fxml.
     */
    public static void setPrimaryStage(Stage s){
        primaryStage = s;
        winner=-1;
        staleMate=-1;
        whiteCheck=false;
        blackCheck=false;
    }

    /**
     * Setting the user parameters
     * @param d depth
     * @param kW king's weight
     * @param qW queen's weight
     * @param rW rook's weight
     * @param bW bishop's weight
     * @param knW knight's weight
     * @param pW pawn's weight
     * @param dLPW the related to pawns in the blocked state
     * @param mW mobility rate. (how moves in total does a player have).
     */
    public static void setParameters(int d, int kW, int qW, int rW, int bW, int knW,  int pW ,  double dLPW,  double mW){
        depth = d;
        kingW = kW;
        queenW = qW;
        rookW = rW;
        bishopW = bW;
        knightW = knW;
        pawnW = pW;
        deadLockPawnW = dLPW;
        mobilityW = mW;
    }

    public static void setScene(Parent root, String title){
        if(primaryStage!=null){
            primaryStage.setTitle(title);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }
    }

    public static void setPlayerName(String n){
        playerName =n;
    }

    public static String getPlayerName(){
        if(playerName.isEmpty()){
            return "Player 1";
        }else{
            return playerName;
        }
    }

    public static void setFileName(String n){
        fileName =n;
    }

    public static String getFileName(){
        if(fileName.isEmpty()){
            return "default.txt";
        }else{
            return fileName;
        }
    }

    public static void Quit(){
        if(primaryStage!=null){
            primaryStage.close();
            System.exit(0);
        }
    }

    /**
     * This method is responsible for placing a piece on the board.
     * @param i piece's i location
     * @param j piece's j location
     */
    public static void place(int i, int j){
        ImageView image = board[i][j].image;
        image.setPreserveRatio(true);
        image.setFitHeight(100);
        double x=highlight.getLayoutX()+(100*i)-120;
        double y=highlight.getLayoutY()+(100*j)+130;
        final Piece p = board[i][j];
        image.setLayoutX(y);
        image.setLayoutY(x);
        currentLocation.put((i*8)+j,board[i][j]);
        ((AnchorPane)highlight.getParent()).getChildren().add(image);
        image.setOnMouseClicked(event ->{
            if (currentPlayer == p.type) {
                clearHighlights();
                if((whiteCheck&&p.type)||(blackCheck&&!p.type)){
                    System.out.println("Check state "+potentialSavers.toString());
                    if(potentialSavers.contains(p)){
                        p.highlightSavingPositions(highlight);
                    }
                    if(p.power==5){
                        p.highlightPossiblePositions(highlight,board);
                    }
                }else{
                    p.highlightPossiblePositions(highlight, board);
                }
            } else {
                System.out.println("Other players turn");
            }
        });
    }

    /**
     * This method sets the chess board for the class
     * @param b the board
     */
    public static void setBoard(Piece[][] b){
        board = b;
        for(Node p:((highlight.getChildren()))){
            p.setOnMouseClicked(event->{clearHighlights();});
        }
        for(int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                if(board[i][j]!=null){
                    place(i,j);
                }
            }
        }
    }

    /**
     * THis method sets the currently highlighted squares in the chess board.
     * @param h set of Panes storing "highlighted Image"
     */
    public static void setCurrentlyHighlighted(Set<Pane> h){
        currentlyHighlighted = h;
    }

    /**
     * This method removes all highlights from the currentlyHighlighed set.
     */
    public static void clearHighlights(){
        if(currentlyHighlighted!=null){
            for (Pane p: currentlyHighlighted) {
                p.getChildren().clear();
            }
            currentlyHighlighted.clear();
        }
    }


    public static Piece[][] getBoard(){
        return board;
    }

    /**
     * This sets the underlying grid for the board.
     * @param bd gridpane
     */
    public static void setBoardDisplay(GridPane bd){
        highlight = bd;
    }

    /**
     * This method handles movement of piece on the chess board
     * if the board[i][j] is null it simply moves the piece to the new location and updates the gui
     * if i,j holds an enemy piece it would check if that piece is king. At this point the game will end.
     * if not a king it will replace the i,j piece. And move the piece to the appropriate death location in the gui.
     * It also handles piece promotion.
     * if the pawn moves to a valid promotion place it provides the user with a dialog box to choose the piece they want.
     * at the end of the move it checks if the opposite player's king is in check  state. If so it calls the checkBoard method.
     * The check board will return either the set of pieces that can save the king(including king) or null.
     * Null means either check mate has happened or stalemate has happened.
     * @param ci current location of piece's (i)
     * @param cj current location of piece's (j)
     * @param i new location i
     * @param j new location j
     * @param p the piece to be moved.
     */
    public static void movePiece(int ci, int cj, int i, int j, Piece p){
        boolean flag = false;
        Alert a;
        System.out.println("Moving Piece ");
        if(board[i][j]==whiteKing){
            a = new Alert(Alert.AlertType.INFORMATION);
            a.setHeaderText("Game over");
            a.setContentText("CheckMate, Black won!");
            a.show();
            flag = true;
            endGame();
        }
        else if(board[i][j]==blackKing) {
            a = new Alert(Alert.AlertType.INFORMATION);
            a.setHeaderText("Game over");
            a.setContentText("CheckMate, White won!");
            flag = true;
            endGame();
        }else if(!(board[i][j]!=null&&!board[i][j].type&&isAiPlaying)&&(board[i][j]==null ||board[i][j].type!=p.type)&& board[ci][cj].power==1 &&((board[ci][cj].type&&i==0)||(!board[ci][cj].type&&i==7))){//promotion
                RadioButton radioButton1 = new RadioButton("queen");
                RadioButton radioButton2 = new RadioButton("knight");
                RadioButton radioButton3 = new RadioButton("bishop");
                RadioButton radioButton4 = new RadioButton("rook");

                ToggleGroup radioGroup = new ToggleGroup();

                radioButton1.setToggleGroup(radioGroup);
                radioButton2.setToggleGroup(radioGroup);
                radioButton3.setToggleGroup(radioGroup);
                radioButton4.setToggleGroup(radioGroup);
                radioGroup.selectToggle(radioButton1);
                GridPane box = new GridPane();
                box.add(radioButton1, 0, 1);
                box.add(radioButton2, 0, 2);
                box.add(radioButton3, 0, 3);
                box.add(radioButton4, 0, 4);
                box.setMinHeight(100);
                if ((p.type && i == 0) || (!p.type && i == 7)) {
                    a = new Alert(Alert.AlertType.INFORMATION);
                    a.getDialogPane().setExpandableContent(box);
                    a.getDialogPane().setExpanded(true);
                    //a.getDialogPane().setMinHeight(200);
                    Alert finalA = a;
                    a.setOnCloseRequest(event -> {
                        if (board[i][j] != null) {
                            guiKill(i, j);
                        }
                        RadioButton selected = (RadioButton) radioGroup.getSelectedToggle();
                        Piece pChoosen = null;
                        switch (selected.getText()) {
                            case "queen": {
                                if (p.type) {
                                    pChoosen = new queen(backgroundData.positionHashtable.get((i * 8) + j), new ImageView(new Image(String.valueOf(backgroundData.class.getResource("Pieces/White/queen.png")))), true);
                                } else {
                                    pChoosen = new queen(backgroundData.positionHashtable.get((i * 8) + j), new ImageView(new Image(String.valueOf(backgroundData.class.getResource("Pieces/Black/queen.png")))), false);
                                }
                                break;
                            }
                            case "knight": {
                                if (p.type) {
                                    pChoosen = new knight(backgroundData.positionHashtable.get((i * 8) + j), new ImageView(new Image(String.valueOf(backgroundData.class.getResource("Pieces/White/horse_fRight.png")))), true);
                                } else {
                                    pChoosen = new knight(backgroundData.positionHashtable.get((i * 8) + j), new ImageView(new Image(String.valueOf(backgroundData.class.getResource("Pieces/Black/horse_fRight.png")))), false);
                                }
                                break;
                            }
                            case "bishop": {
                                if (p.type) {
                                    pChoosen = new bishop(backgroundData.positionHashtable.get((i * 8) + j), new ImageView(new Image(String.valueOf(backgroundData.class.getResource("Pieces/White/bishop.png")))), true);
                                } else {
                                    pChoosen = new bishop(backgroundData.positionHashtable.get((i * 8) + j), new ImageView(new Image(String.valueOf(backgroundData.class.getResource("Pieces/Black/bishop.png")))), false);
                                }
                                break;
                            }
                            case "rook": {
                                if (p.type) {
                                    pChoosen = new rook(backgroundData.positionHashtable.get((i * 8) + j), new ImageView(new Image(String.valueOf(backgroundData.class.getResource("Pieces/White/rook.png")))), true);
                                } else {
                                    pChoosen = new rook(backgroundData.positionHashtable.get((i * 8) + j), new ImageView(new Image(String.valueOf(backgroundData.class.getResource("Pieces/Black/rook.png")))), false);
                                }
                                break;
                            }
                        }
                        System.out.println(board[ci][cj].image.getParent().getChildrenUnmodifiable().indexOf(board[ci][cj].image.getParent()));
                        board[ci][cj].image.setVisible(false);
                        board[ci][cj].image.setOnMouseClicked(e -> {
                        });
                        board[i][j] = pChoosen;
                        place(i, j);
                        board[ci][cj] = null;
                        finalA.close();
                    });
                    a.show();
                }
                flag = true;
        }
        else if (board[i][j]==null){
            System.out.println("Moved "+board[ci][cj]);
            board[i][j]=board[ci][cj];
            board[ci][cj]=null;
            flag= true;
        }else if(board[i][j].type!=p.type){
            System.out.println(board[i][j]+" died");
            guiKill(i,j);
            board[i][j]=board[ci][cj];
            board[ci][cj]=null;
            flag= true;
        }
        System.out.println("Flag "+flag);
        if(flag) {
            System.out.println("Completing move for "+p);
            if (ci >= i) {
                p.image.setLayoutY(p.image.getLayoutY() - ((ci - i) * 100));
            } else {
                p.image.setLayoutY(p.image.getLayoutY() + ((i - ci) * 100));
            }
            if (cj >= j) {
                p.image.setLayoutX(p.image.getLayoutX() - ((cj - j) * 100));
            } else {
                p.image.setLayoutX(p.image.getLayoutX() + ((j - cj) * 100));
            }
            p.pos = backgroundData.positionHashtable.get((i * 8) + j);
            clearHighlights();
            currentLocation.put((i * 8) + j, currentLocation.get((ci * 8) + cj));
            currentLocation.remove((ci * 8) + cj);
            board[i][j].nMove++;
            System.out.println("-------------Changing current Player "+currentPlayer);
            currentPlayer = !currentPlayer;
            System.out.println("-------------Changing current Player "+currentPlayer);
            Set<Piece> potentialS=null;
            if (blackKing.isCheck(blackKing.pos.X, blackKing.pos.Y, board)) {
                System.out.println("black is in check state");
                blackCheck = true;
                potentialS = checkBoard(board,false);
            } else {
                blackCheck = false;
            }
            if (whiteKing.isCheck(whiteKing.pos.X, whiteKing.pos.Y, board)) {
                System.out.println("White is in check state.");
                whiteCheck = true;
                potentialS = checkBoard(board,true);
            } else {
                whiteCheck = false;
            }
            if(potentialS==null){
                Alert f = new Alert(Alert.AlertType.INFORMATION);
                f.setHeaderText("Game over");
                if(staleMate==1){
                    f.setContentText("Stale Mate, Draw");
                }
                else if(winner==0){
                    if(!currentPlayer) {
                        f.setContentText("CheckMate, Black won!");
                    }else {
                        f.setContentText("CheckMate, White won!");
                    }
                }
                else{
                    f=null;
                }
                if(f!=null) {
                    Alert finalF = f;
                    f.setOnCloseRequest(event -> {
                        finalF.close();
                        endGame();
                    });
                    f.show();
                    System.out.println("CheckMate");
                }
            }else {
                potentialSavers = potentialS;
            }
            System.out.println("End of turn , next turn "+currentPlayer);
            if(isAiPlaying&& !currentPlayer){
                cpu t  = new cpu(board,depth,kingW,queenW,rookW,bishopW,knightW,pawnW,deadLockPawnW,mobilityW);
                Task<Void> task = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        t.calculate();
                        return null;
                    }
                };
                task.run();
                task.setOnSucceeded(e->{
                    System.out.println("Succeeded");
                    movePiece(t.p.pos.X,t.p.pos.Y,t.i,t.j,t.p);
                });
            }
        }
    }

    /**
     * This method handles the gui side of the killing a piece.
     * It calls appropriate kill method, by checking the type of piece.
     * @param i piece to be killed i
     * @param j piece to be killed j
     */
    public static void guiKill(int i, int j){
        board[i][j].image.getParent().getChildrenUnmodifiable().remove(board[i][j].image.getParent());
        board[i][j].image.setOnMouseClicked(e -> {});
        if(board[i][j].type){
            killWhite(board[i][j],(StackPane)((GridPane)(highlight.getParent().getChildrenUnmodifiable().get(3))).getChildren().get(cWhiteDeathPoint.X*8+cWhiteDeathPoint.Y));
        }else{
            killBlack(board[i][j],(StackPane)((GridPane)(highlight.getParent().getChildrenUnmodifiable().get(4))).getChildren().get(cBlackDeathPoint.X*8+cBlackDeathPoint.Y));
        }
    }

    /**
     * This method places the dead piece on the left side of the board.
     * @param p Piece to be moved
     * @param s Pane location to be added to.
     */
    public static void killWhite(Piece p, StackPane s){
        s.getChildren().add(p.image);
        if(cWhiteDeathPoint.X+1>1){
            cWhiteDeathPoint.Y+=1;
            cWhiteDeathPoint.X=0;
        }else{
            cWhiteDeathPoint.X+=1;
        }
    }
    /**
     * This method places the dead piece on the right side of the board.
     * @param p Piece to be moved
     * @param s Pane location to be added to.
     */
    public static void killBlack(Piece p, StackPane s){
        s.getChildren().add(p.image);
        if(cBlackDeathPoint.X+1>1){
            cBlackDeathPoint.Y+=1;
            cBlackDeathPoint.X=0;
        }else{
            cBlackDeathPoint.X+=1;
        }
    }
    public static boolean isPieceThere(int i, int j,boolean type) {
        if((i >= 0 && j >= 0 && i < 8 && j < 8) ){
            if(board[i][j] == null){
                return true;
            }
            else return board[i][j].type != type;
        }
        return false;
    }
    public static boolean typeOfPiece(int i, int j){
        try{
            return currentLocation.get((i*8)+j).type;
        }catch (Exception e){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText("Something went wrong");
            a.show();
            return false;
        }
    }

    public static void setCurrentPlayer(boolean p){
        currentPlayer=p;
    }

    /**
     * Method used for debugging purposes.
     * @param positions concat all current positions.
     * @return return string of positions.
     */
    public static String printPoints(Set<Position> positions){
        String s="";
        if(positions!=null) {
            for (Position p : positions) {
                s += p.X + "-" + p.Y + " ,";
            }
        }
        return s.length()>0?s.substring(0,s.length()-2):"[]";
    }

    /**
     * This method checks weather the king who is checked can be saved or not and also checks if the it is a stalemate.
     * It first takes the appropriate king that is being checked.
     * Get the positions the king can move to.
     * For each piece that is of the enemy team check if it can attack the king.
     * Store all the locations where friendlies can move to save the king.
     * For each piece that is friendly in the board.
     * Get it's possible move locations. if the piece's possible locations either include the attacker's location or can block the attacker.
     * The piece is considered to be a saving piece.
     * On the turn all these type pieces are used to save the king.
     * @param board current board
     * @param type type of player to check for
     * @return set pieces that can save the king.
     */
    public static Set<Piece> checkBoard(Piece[][] board, boolean type){
        // 0 fine, 1 stalemate, 2 checkmate
        Piece king;
        boolean check;
        Set<Piece> potentialS = new HashSet<>();
        if(type){
            king = whiteKing;
        }else{
            king = blackKing;
        }
        potentialSavers.clear();
        Hashtable<Piece,Set<Position>> attackers = new Hashtable<>();
        Set<Piece> killedAttackers = new HashSet<>();
        Set<Position> kingsPos = new HashSet<>(king.getPossiblePositions(board));
        int possibleMoves=0;
        for(int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                if(board[i][j]!=null&&board[i][j].type != currentPlayer) {
                    Set<Position> enemyPlayerPossiblePositions = board[i][j].getPossiblePositions(board);
                    if(enemyPlayerPossiblePositions.contains(king.pos)) {//check
                        int y1 = board[i][j].pos.Y;
                        int x1 = board[i][j].pos.X;
                        int y2 = king.pos.Y;
                        int x2 = king.pos.X;
                        Set<Position> possiblePath = new HashSet<>(kingsPos);
                        if (Math.abs(y2 - y1) == Math.abs(x2 - x1)) {//diagonal
                            int dx = x1;
                            int dy = y1;
                            int diff;
                            int end = x2;
                            diff = y2 > y1 ? -1 : +1;
                            if (x2 > x1) {//do not include the king
                                dx = x2;
                                dy = y2;
                                end = x1;
                            }
                            dx++;
                            dy -= diff;
                            for (; dx >= end; dx--) {
                                if (dx >= 0 && dx < 8 && dy >= 0 && dy < 8) {
                                    possiblePath.add(backgroundData.positionHashtable.get((dx * 8) + dy));
                                }
                                dy += diff;
                            }
                        } else if (Math.abs(y2 - y1) == 0) {//horizontal
                            int dx;
                            int end;
                            if (x2 > x1) {
                                dx = x2;
                                end = x1 + 1;
                            } else {
                                dx = x1;
                                end = x2 - 1;
                            }
                            for (; dx >= end; dx--) {
                                if (dx >= 0 && dx < 8) {
                                    possiblePath.add(backgroundData.positionHashtable.get((dx * 8) + y2));
                                }
                            }
                        } else {//vertical
                            int dy;
                            int end;
                            if (y2 > y1) {
                                dy = y2;
                                end = y1 + 1;
                            } else {
                                dy = y1;
                                end = y2 - 1;
                            }
                            for (; dy >= end; dy--) {
                                if (dy >= 0 && dy < 8) {
                                    possiblePath.add(backgroundData.positionHashtable.get((x2 * 8) + dy));
                                }
                            }
                        }
                        possiblePath.retainAll(enemyPlayerPossiblePositions);
                        if (possiblePath.size() > 0) {
                            possiblePath.add(board[i][j].pos);
                            attackers.put(board[i][j], possiblePath);
                        }
                    }
                }
                if(board[i][j]!=null&&board[i][j].type!=type){
                    possibleMoves++;
                }
            }
        }
        check = attackers.size() > 0;
        for(int i=0;i<8;i++) {
            for (int j = 0; j < 8; j++) {
                if(board[i][j]!=null && board[i][j].type == currentPlayer) {
                    Set<Position> piecePossiblePosition = board[i][j].getPossiblePositions(board);
                    for (Pane p : currentlyHighlighted) {
                        piecePossiblePosition.add(backgroundData.positionHashtable.get((int) (((p.getLayoutY() / 100) * 8) + p.getLayoutX() / 100)));
                    }
                    if(check&&piecePossiblePosition!=null){//checking for checkmate
                        for(Piece enemy: attackers.keySet()){
                            Set<Position> possibleBlocks = new HashSet<>(piecePossiblePosition);
                            if(board[i][j].power==5) {//piece is a king
                                if(possibleBlocks.size()>0){
                                    killedAttackers.add(enemy);
                                    potentialS.add(board[i][j]);
                                    board[i][j].savingPositions = possibleBlocks;
                                    possibleMoves++;
                                }
                            }
                            else {
                                possibleBlocks.retainAll(attackers.get(enemy));
                                if(possibleBlocks.size()>0){
                                    killedAttackers.add(enemy);
                                    potentialS.add(board[i][j]);
                                    board[i][j].savingPositions = possibleBlocks;
                                    possibleMoves++;
                                }
                            }
                        }
                    }else{//checking for stalemate
                        possibleMoves+=currentlyHighlighted.size();
                    }
                }
            }
        }
        if(check&&(killedAttackers.size()<attackers.size())){
            winner=0;
            return null;
        }
        else if(possibleMoves==0){
            staleMate=1;//staleMate
            return null;
        }else {
            winner=-1;
            staleMate=-1;
            return potentialS;
        }
    }

    /**
     * This method loads the previous screen as the game has concluded.
     */
    public static void endGame(){
        Parent root = null;
        try {
            root = FXMLLoader.load(backgroundData.class.getResource("singlePlayer.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        backgroundData.setScene(root,"Chess by Rikveet Singh Hayer");
        backgroundData.setPlayerName("");
        backgroundData.setFileName("");
        blackCheck=false;
        whiteCheck=false;
    }
}
