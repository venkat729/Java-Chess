package com.chess.engine.piece;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import java.util.Collection;
import java.util.List;

public abstract class Piece {
    protected final int piecePosition;
    protected final Alliance pieceAlliance;
    protected final boolean isFirstMove;
    protected final PieceType pieceType;
    private final int cachedHashCode;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Piece)) return false;

        Piece piece = (Piece) o;

        if (getPiecePosition() != piece.getPiecePosition()) return false;
        if (isFirstMove() != piece.isFirstMove()) return false;
        if (getPieceAlliance() != piece.getPieceAlliance()) return false;
        return getPieceType() == piece.getPieceType();
    }

    @Override
    public int hashCode() {
        return this.cachedHashCode;
    }

    public Piece(final PieceType pieceType, final int piecePosition,
                 final Alliance pieceAlliance,final boolean isFirstMove){
        this.pieceAlliance = pieceAlliance;
        this.pieceType = pieceType;
        this.piecePosition = piecePosition;
        this.isFirstMove = isFirstMove;
        this.cachedHashCode = computeHashCode();
    }

    private int computeHashCode() {
        int result = getPiecePosition();
        result = 31 * result + getPieceAlliance().hashCode();
        result = 31 * result + (isFirstMove() ? 1 : 0);
        result = 31 * result + getPieceType().hashCode();
        return result;
    }

    public Alliance getPieceAlliance(){
        return this.pieceAlliance;
    }
    public abstract Collection<Move> calculateLegalMoves(final Board board);

    public boolean isFirstMove(){
        return this.isFirstMove;
    }

    public abstract Piece movePiece(final Move move);

    public int getPiecePosition() {
        return this.piecePosition;
    }

    public PieceType getPieceType() {
        return this.pieceType;
    }

    public enum PieceType{
        PAWN("P") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KING("K") {
            @Override
            public boolean isKing() {
                return true;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        QUEEN("Q") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KNIGHT("N") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        ROOK("R") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return true;
            }
        },
        BISHOP("B") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        };
        private String pieceName;
        PieceType(final String pieceName){
            this.pieceName = pieceName;
        }
        @Override
        public String toString(){
            return this.pieceName;
        }

        public abstract boolean isKing();

        public abstract boolean isRook();
    }
}
