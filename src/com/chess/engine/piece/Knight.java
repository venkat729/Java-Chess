package com.chess.engine.piece;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Knight extends Piece{
    public Knight(final int piecePosition, final Alliance pieceAlliance) {
        super(PieceType.KNIGHT,piecePosition, pieceAlliance,true);
    }


    public Knight(final int piecePosition,final Alliance pieceAlliance,final boolean isFirstMove){
        super(PieceType.KNIGHT,piecePosition,pieceAlliance,isFirstMove);
    }
    public static final int[] candidate_move_coordinates = {-17,-15,-10,-6,6,10,15,17};

    @Override
    public Knight movePiece(final Move move) {
        return new Knight(move.getDestinationCoordinate(),move.getMovedPiece().getPieceAlliance());
    }

    @Override
    public String toString(){
        return PieceType.KNIGHT.toString();
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for(int currentCandidateOffset: candidate_move_coordinates){
            final int candidateDestinationCoordinate;
            candidateDestinationCoordinate = this.piecePosition+currentCandidateOffset;
            if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                if(isEighthColumnExclusion(this.piecePosition,currentCandidateOffset)||
                        isSeventhColumnExclusion(this.piecePosition,currentCandidateOffset)||
                        isSecondColumnExclusion(this.piecePosition,currentCandidateOffset)||
                        isFirstColumnExclusion(this.piecePosition,currentCandidateOffset)){
                    continue;
                }
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                if(!candidateDestinationTile.isTileOccupied()){
                    legalMoves.add(new Move.MajorMove(board,this,candidateDestinationCoordinate));
                }
                else{
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
                    if(this.pieceAlliance!=pieceAlliance){
                        legalMoves.add(new Move.AttackMove(board,this,candidateDestinationCoordinate,pieceAtDestination));
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }
    private static boolean isFirstColumnExclusion(final int currentPosition,final int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition]&&(candidateOffset==-17||candidateOffset==-10||
        candidateOffset==6||candidateOffset==15);
    }
    private static boolean isSecondColumnExclusion(final int currentPosition,final int candidateOffset){
        return BoardUtils.SECOND_COLUMN[currentPosition]&&(candidateOffset==-10|| candidateOffset==6);
    }
    private static boolean isSeventhColumnExclusion(final int currentPosition,final int candidateOffset){
        return BoardUtils.SEVENTH_COLUMN[currentPosition]&&(candidateOffset==10|| candidateOffset==-6);
    }
    private static boolean isEighthColumnExclusion(final int currentPosition,final int candidateOffset){
        return BoardUtils.EIGHTH_COLUMN[currentPosition]&&(candidateOffset==17||candidateOffset==10||
                candidateOffset==-6||candidateOffset==-15);
    }
}
