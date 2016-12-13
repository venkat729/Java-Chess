package com.chess.engine.board;

import com.chess.engine.board.Board.Builder;
import com.chess.engine.piece.Pawn;
import com.chess.engine.piece.Piece;
import com.chess.engine.piece.Rook;

public abstract class Move {
    protected final Board board;
    protected final Piece movedPiece;
    protected final int destinationCoordinate;
    protected static final Move NULL_MOVE = new NullMove();
    protected final boolean isFirstMove;
    public Move(final Board board,final Piece movedPiece,final int destinationCoordinate){
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
        this.isFirstMove = movedPiece.isFirstMove();
    }

    private Move(final Board board,final int destinationCoordinate){
        this.board = board;
        this.movedPiece = null;
        this.destinationCoordinate = destinationCoordinate;
        this.isFirstMove = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Move)) return false;

        Move move = (Move) o;

        if (getDestinationCoordinate() != move.getDestinationCoordinate()) return false;
        if (isFirstMove != move.isFirstMove) return false;
        if (!board.equals(move.board)) return false;
        return getMovedPiece().equals(move.getMovedPiece());
    }

    @Override
    public int hashCode() {
        int result = board.hashCode();
        result = 31 * result + getMovedPiece().hashCode();
        result = 31 * result + getDestinationCoordinate();
        result = 31 * result + (isFirstMove ? 1 : 0);
        return result;
    }

    public boolean isAttack(){
        return false;
    }
    public boolean isCastlingMove(){
        return false;
    }
    public Piece getAttackedPiece(){
        return null;
    }

    public int getDestinationCoordinate() {
        return this.destinationCoordinate;
    }

    public Board execute(){
        final Builder builder = new Builder();
        for(final Piece piece : this.board.currentPlayer().getActivePieces()){
            if(!this.movedPiece.equals(piece)){
                builder.setPiece(piece);
            }
        }
        for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
            builder.setPiece(piece);
        }
        builder.setPiece(this.movedPiece.movePiece(this));
        builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
        return builder.build();
    }

    public Piece getMovedPiece() {
        return this.movedPiece;
    }

    public int getCurrentCoordinate() {
        return this.movedPiece.getPiecePosition();
    }

    public static final class MajorMove extends Move{

        public MajorMove(final Board board, final Piece movedPiece, final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof MajorMove && super.equals(other);
        }
        @Override
        public String toString(){
            return movedPiece.getPieceType().toString()+BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }

    }
    public static class AttackMove extends Move{
        Piece attackedPiece;
        public AttackMove(final Board board, final Piece movedPiece, final int destinationCoordinate,final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof AttackMove)) return false;
            if (!super.equals(o)) return false;

            AttackMove that = (AttackMove) o;

            return getAttackedPiece().equals(that.getAttackedPiece());
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + getAttackedPiece().hashCode();
            return result;
        }

        @Override
        public boolean isAttack(){
            return true;
        }
        @Override
        public Piece getAttackedPiece(){
            return this.attackedPiece;
        }
    }

    public static final class PawnMove extends Move{

        public PawnMove(final Board board, final Piece movedPiece, final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
    }


    public static class PawnAttackMove extends AttackMove{

        public PawnAttackMove(final Board board, final Piece movedPiece, final int destinationCoordinate,final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate,attackedPiece);
        }
    }


    public static final class PawnEnPassantAttackMove extends PawnAttackMove{

        public PawnEnPassantAttackMove(final Board board, final Piece movedPiece, final int destinationCoordinate,final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate,attackedPiece);
        }
    }


    public static final class PawnJump extends Move{

        public PawnJump(final Board board, final Piece movedPiece, final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
        @Override
        public Board execute(){
            final Builder builder = new Builder();
            for(final Piece piece : this.board.currentPlayer().getActivePieces()){
                if(!this.movedPiece.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }
            final Pawn movedPawn = (Pawn)this.movedPiece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.setEnPassantMove(movedPawn);
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }



    static abstract class CastleMove extends Move{

        protected final Rook castleRook;
        protected final int castleRookStart;
        protected final int castleRookDestination;

        public CastleMove(final Board board, final Piece movedPiece, final int destinationCoordinate,
                          final Rook castleRook,final int castleRookStart,final int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate);
            this.castleRook = castleRook;
            this.castleRookStart = castleRookStart;
            this.castleRookDestination = castleRookDestination;
        }
        @Override
        public boolean isCastlingMove(){
            return true;
        }
        @Override
        public Board execute(){
            final Builder builder = new Builder();
            for(final Piece piece : this.board.currentPlayer().getActivePieces()){
                if(!this.movedPiece.equals(piece)&&!this.castleRook.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setPiece(new Rook(this.castleRook.getPiecePosition(),this.castleRook.getPieceAlliance()));
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }


    public static final class KingSideCastleMove extends CastleMove{

        public KingSideCastleMove(final Board board, final Piece movedPiece, final int destinationCoordinate,
                                  final Rook castleRook,final int castleRookStart,final int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate,castleRook,castleRookStart,castleRookDestination);
        }
        @Override
        public String toString(){
            return "O-O";
        }
    }


    public static final class QueenSideCastleMove extends CastleMove{

        public QueenSideCastleMove(final Board board, final Piece movedPiece, final int destinationCoordinate,
                                   final Rook castleRook,final int castleRookStart,final int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate,castleRook,castleRookStart,castleRookDestination);
        }
        @Override
        public String toString(){
            return "O-O-O";
        }
    }

    public static final class NullMove extends Move{

        public NullMove() {
            super(null,null,-1);
        }
        @Override
        public Board execute(){
            throw new RuntimeException("Cannot Execute NullMove............");
        }
    }

    public static class MoveFactory{
        public MoveFactory(){
            throw new RuntimeException("Cannot Instantiable!!!!!");
        }
        public static Move createMove(final Board board, final int currentCoordinate, final int destinationCoordinate){
            for(final Move move:board.getAllLegalMoves()){
                if(move.getCurrentCoordinate()==currentCoordinate &&
                        move.getDestinationCoordinate()==destinationCoordinate){
                    return move;
                }
            }
            return NULL_MOVE;
        }
    }
}