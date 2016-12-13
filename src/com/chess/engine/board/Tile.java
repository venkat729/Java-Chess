package com.chess.engine.board;
import com.chess.engine.piece.Piece;
import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;

import static com.chess.engine.board.BoardUtils.NUM_TILES;

public abstract class Tile {
    protected final int tileCoordinate;
    Tile(final int tileCoordinate){
        this.tileCoordinate = tileCoordinate;
    }
    public static final Map<Integer,EmptyTile> Empty_Tiles = createAllPossibleEmptyTiles();

    private static Map<Integer,EmptyTile> createAllPossibleEmptyTiles() {
        final Map<Integer,EmptyTile> emptyTileMap = new HashMap<>();
        for(int i =0;i<NUM_TILES;i++){
            emptyTileMap.put(i,new EmptyTile(i));
        }
        return ImmutableMap.copyOf(emptyTileMap);
    }
    public static Tile createTile(final int tileCoordinate,final Piece piece){
        return piece!=null ? new OccupiedTile(tileCoordinate,piece):Empty_Tiles.get(tileCoordinate);
    }
    public abstract boolean isTileOccupied();
    public abstract Piece getPiece();

    public int getTileCoordinate() {
        return this.tileCoordinate;
    }

    public static final class EmptyTile extends Tile {

        EmptyTile(final int tileCoordinate) {
            super(tileCoordinate);
        }

        @Override
        public boolean isTileOccupied() {
            return false;
        }

        @Override
        public String toString(){
            return "-";
        }

        @Override
        public Piece getPiece() {
            return null;
        }
    }
    public static final class OccupiedTile extends Tile{
        private final Piece pieceOnTile;
        OccupiedTile(final int tileCoordinate,final Piece pieceOnTile) {
            super(tileCoordinate);
            this.pieceOnTile = pieceOnTile;
        }

        @Override
        public String toString(){
            return getPiece().getPieceAlliance().isBlack()?getPiece().toString().toLowerCase():
                    getPiece().toString();
        }

        @Override
        public boolean isTileOccupied() {
            return true;
        }

        @Override
        public Piece getPiece() {
            return this.pieceOnTile;
        }
    }
}
