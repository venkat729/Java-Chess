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

/**
 * Created by Root on 12/9/2016.
 */
public class Queen extends Piece {
    public Queen(final int piecePosition, final Alliance pieceAlliance) {
        super(PieceType.QUEEN,piecePosition, pieceAlliance,true);
    }


    public Queen(final int piecePosition,final Alliance pieceAlliance,final boolean isFirstMove){
        super(PieceType.QUEEN,piecePosition,pieceAlliance,isFirstMove);
    }
    public static final int[] candidate_move_coordinates = {-9,-8,-7,-1,1,7,8,9};

    @Override
    public Queen movePiece(final Move move) {
        return new Queen(move.getDestinationCoordinate(),move.getMovedPiece().getPieceAlliance());
    }
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

    private boolean isFirstColumnExclusion(int piecePosition, int currentCandidateOffset) {
        return BoardUtils.FIRST_COLUMN[piecePosition]&&(currentCandidateOffset==7||currentCandidateOffset==-9
        ||currentCandidateOffset==-1);
    }

    private boolean isEighthColumnExclusion(int piecePosition, int currentCandidateOffset) {
        return BoardUtils.FIRST_COLUMN[piecePosition]&&(currentCandidateOffset==-7||currentCandidateOffset==9
                ||currentCandidateOffset==1);
    }

    @Override
    public String toString(){
        return PieceType.QUEEN.toString();
    }
}
