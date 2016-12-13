package com.chess.engine.board;

import java.util.Map;

public class BoardUtils {
    public static final boolean[] FIRST_COLUMN = initColumn(0);
    public static final boolean[] SECOND_COLUMN = initColumn(1);
    public static final boolean[] SEVENTH_COLUMN = initColumn(6);
    public static final boolean[] EIGHTH_COLUMN = initColumn(7);

    public static final String[] ALGEBRAIC_NOTATION = initializeAlgebraicNotation();

    private static String[] initializeAlgebraicNotation() {
        return null;
    }

    public static final Map<String,Integer> POSITION_TO_COORDINATE = initializePositionToCoordinate();

    private static Map<String,Integer> initializePositionToCoordinate() {
        return null;
    }

    public static final boolean[] EIGHTH_RANK = initRow(0);
    public static final boolean[] SEVENTH_RANK = initRow(8);
    public static final boolean[] SIXTH_RANK = initRow(16);
    public static final boolean[] FIFTH_RANK = initRow(24);
    public static final boolean[] FOURTH_RANK = initRow(32);
    public static final boolean[] THIRD_RANK = initRow(40);
    public static final boolean[] SECOND_RANK = initRow(48);
    public static final boolean[] FIRST_RANK = initRow(56);

    private static boolean[] initRow(int i) {
        final boolean[] row = new boolean[NUM_TILES];
        do{
            row[i] = true;
            i = i+1;
        }while(i%NUMBER_OF_TILES_PER_ROW!=0);
        return row;
    }

    public static final int NUM_TILES = 64;
    public static final int NUMBER_OF_TILES_PER_ROW = 8;
    private BoardUtils(){
        throw new RuntimeException("You Cannot Instantiate Me!!");
    }
    private static boolean[] initColumn(int i) {
        final boolean[] column = new boolean[NUM_TILES];
        do{
            column[i] = true;
            i = i+NUMBER_OF_TILES_PER_ROW;
        }while(i<NUM_TILES);
        return column;
    }
    public static boolean isValidTileCoordinate(final int coordinate){
        return coordinate>=0&&coordinate<NUM_TILES;
    }

    public static String getPositionAtCoordinate(final int destinationCoordinate) {
        return ALGEBRAIC_NOTATION[destinationCoordinate];
    }
    public static int getCoordinateAtPosition(final String position){
        return POSITION_TO_COORDINATE.get(position);
    }
}
