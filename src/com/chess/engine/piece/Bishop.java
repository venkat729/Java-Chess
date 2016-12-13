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

public class Bishop extends Piece{
    public Bishop(final int piecePosition, final Alliance pieceAlliance) {
        super(PieceType.BISHOP,piecePosition, pieceAlliance,true);
    }
    public Bishop(final int piecePosition,final Alliance pieceAlliance,final boolean isFirstMove){
        super(PieceType.BISHOP,piecePosition,pieceAlliance,isFirstMove);
    }
    public static final int[] candidate_move_coordinates = {-9,-7,7,9};
    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for(int currentCandidateOffset: candidate_move_coordinates){
            final int candidateDestinationCoordinate;
            candidateDestinationCoordinate = this.piecePosition+currentCandidateOffset;
            while(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                if(isEighthColumnExclusion(this.piecePosition,currentCandidateOffset)||
                        isFirstColumnExclusion(this.piecePosition,currentCandidateOffset)){
                    break;
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
                    break;
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Bishop movePiece(final Move move) {
        return new Bishop(move.getDestinationCoordinate(),move.getMovedPiece().getPieceAlliance());
    }

    private boolean isFirstColumnExclusion(final int currentPosition,final int currentCandidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition]&&(currentCandidateOffset==7||currentCandidateOffset==-9);
    }

    private boolean isEighthColumnExclusion(final int piecePosition, final int currentCandidateOffset) {
        return BoardUtils.EIGHTH_COLUMN[piecePosition]&&(currentCandidateOffset==9||currentCandidateOffset==-7);
    }

    @Override
    public String toString(){
        return PieceType.BISHOP.toString();
    }
}
