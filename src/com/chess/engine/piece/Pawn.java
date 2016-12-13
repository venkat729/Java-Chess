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

public class Pawn extends Piece {
    public Pawn(final int piecePosition, final Alliance pieceAlliance) {
        super(PieceType.PAWN,piecePosition, pieceAlliance,true);
    }


    public Pawn(final int piecePosition,final Alliance pieceAlliance,final boolean isFirstMove){
        super(PieceType.PAWN,piecePosition,pieceAlliance,isFirstMove);
    }
    public static final int[] candidate_move_coordinates = {8,16,9,7};

    @Override
    public Pawn movePiece(final Move move) {
        return new Pawn(move.getDestinationCoordinate(),move.getMovedPiece().getPieceAlliance());
    }
    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for(final int currentCandidateOffset:candidate_move_coordinates){
            final int candidateDestinationCoordinate = this.piecePosition + (this.getPieceAlliance().getDirection()*currentCandidateOffset);
            if(!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                continue;
            }
            if(currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                legalMoves.add(new Move.MajorMove(board,this,candidateDestinationCoordinate));
            }else if(currentCandidateOffset == 16 && this.isFirstMove() &&
                    (BoardUtils.SEVENTH_RANK[this.piecePosition]&&this.getPieceAlliance().isBlack() ||
                    BoardUtils.SECOND_RANK[this.piecePosition]&&this.getPieceAlliance().isWhite())){
                final int behindCandidateDestinationCoordinate = this.piecePosition+(this.getPieceAlliance().getDirection()*8);
                if(!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied()&&
                        !board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                    legalMoves.add(new Move.PawnJump(board,this,candidateDestinationCoordinate));
                }
            }
            else if(
                    currentCandidateOffset == 7&&
                    !(BoardUtils.FIRST_COLUMN[this.piecePosition]&&this.getPieceAlliance().isBlack()||
                    BoardUtils.EIGHTH_COLUMN[this.piecePosition]&&this.getPieceAlliance().isWhite())
                    )
            {
                final Tile pieceOnTile = board.getTile(candidateDestinationCoordinate);
                if(pieceOnTile.isTileOccupied()) {
                    final Piece pieceAtDestination = pieceOnTile.getPiece();
                    if (this.pieceAlliance != pieceAtDestination.pieceAlliance) {
                        legalMoves.add(new Move.PawnAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                    }
                }
            }
            else if(
                            currentCandidateOffset == 9&&
                            !(BoardUtils.FIRST_COLUMN[this.piecePosition]&&this.getPieceAlliance().isWhite()||
                            BoardUtils.EIGHTH_COLUMN[this.piecePosition]&&this.getPieceAlliance().isBlack())
                    )
            {
                if(board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    final Piece pieceAtDestination = board.getTile(candidateDestinationCoordinate).getPiece();
                    if (this.pieceAlliance != pieceAtDestination.pieceAlliance) {
                        legalMoves.add(new Move.PawnAttackMove(board, this, candidateDestinationCoordinate,pieceAtDestination));
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public String toString(){
        return PieceType.PAWN.toString();
    }
}
