package com.chess.gui;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.piece.Piece;
import com.chess.engine.player.MoveTransition;
import com.google.common.collect.Lists;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static javax.swing.SwingUtilities.invokeLater;
import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class Table {
    private Tile sourceTile;
    private Tile destinationTile;
    private Piece humanMovedPiece;
    private final JFrame gameFrame;
    private final BoardPanel boardPanel;
    private BoardDirection boardDirection;
    private boolean highLightLegals;

    private static final String pieceIconPath = "pieces/";
    private Board chessBoard;
    private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(400,350);
    private static final Dimension TILE_PANEL_DIMENSION = new Dimension(10,10);
    private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(600,600);
    public Table(){
        this.gameFrame = new JFrame("Chess");
        final JMenuBar tableMenuBar = populateMenuBar();
        this.gameFrame.setLayout(new BorderLayout());
        this.chessBoard = Board.createStandardBoard();
        this.highLightLegals = false;
        this.boardDirection = BoardDirection.NORMAL;
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.boardPanel = new BoardPanel();
        this.gameFrame.add(this.boardPanel,BorderLayout.CENTER);
        this.gameFrame.setDefaultCloseOperation(3);
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.gameFrame.setVisible(true);
    }

    private JMenuBar populateMenuBar() {
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        tableMenuBar.add(createPreferrencesMenu());
        return tableMenuBar;
    }

    private JMenu createPreferrencesMenu() {
        JMenu Prefer = new JMenu("Preferences");
        final JMenuItem flip = new JMenuItem("Flip Board");
        flip.addActionListener(e->{
            boardDirection = boardDirection.opposite();
            boardPanel.drawBoard(chessBoard);
        });
        Prefer.add(flip);

        final JCheckBoxMenuItem highLight = new JCheckBoxMenuItem("High Light Legal Moves",false);
        highLight.addActionListener(e->{highLightLegals = highLight.isSelected();});
        Prefer.add(highLight);
        return Prefer;
    }

    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");
        final JMenuItem openPGN = new JMenuItem("Load PGN File");
        openPGN.addActionListener(e->{System.out.println("Open PGN File!!!");});
        fileMenu.add(openPGN);
        final JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(e->System.exit(0));
        fileMenu.add(exit);
        return fileMenu;
    }

    private class BoardPanel extends JPanel{
        final List<TilePanel> boardTiles;
        public BoardPanel(){
            super(new GridLayout(8,8));
            this.boardTiles = new ArrayList<>();
            for(int i  = 0 ; i < BoardUtils.NUM_TILES ; i++){
                final TilePanel tilePanel = new TilePanel(this,i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            setBackground(Color.decode("#8B4526"));
            validate();
        }

        public void drawBoard(final Board board) {
            removeAll();
            for(final TilePanel tilePanel : boardDirection.traverse(boardTiles)){
                tilePanel.drawTile(board);
                add(tilePanel);
            }
            validate();
            repaint();
        }
    }

    public static class MoveLog{
        private final List<Move> moves;
        MoveLog(){
            this.moves = new ArrayList<>();
        }
        public List<Move> getMoves(){
            return this.moves;
        }
        public void addMove(final Move move){
            this.moves.add(move);
        }
        public int size(){
            return this.moves.size();
        }
        public void clear(){
            this.moves.clear();
        }
        public Move removeMove(final int index){
            return this.moves.remove(index);
        }
        public boolean removeMove(final Move move){
            return this.moves.remove(move);
        }
    }

    private class TilePanel extends JPanel{
        private final int tileId;
        TilePanel(final BoardPanel boardPanel,final int tileId){
            super(new GridBagLayout());
            this.tileId = tileId;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTileColor();
            assignTilePieceIcon(chessBoard);
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(isRightMouseButton(e)){
                        sourceTile = null;
                        destinationTile = null;
                        humanMovedPiece = null;
                    }
                    else if(isLeftMouseButton(e)){
                        if(sourceTile==null){
                            sourceTile = chessBoard.getTile(tileId);
                            humanMovedPiece = sourceTile.getPiece();
                            if(humanMovedPiece == null){
                                sourceTile = null;
                            }
                        }
                        else{
                            destinationTile = chessBoard.getTile(tileId);
                            final Move move = Move.MoveFactory.createMove(chessBoard,sourceTile.getTileCoordinate(),
                                    destinationTile.getTileCoordinate());
                            final MoveTransition transition = chessBoard.currentPlayer().makeMove(move);
                            if(transition.getMoveStatus().isDone()){
                                chessBoard = transition.getTransitionBoard();
                            }
                            sourceTile = null;
                            destinationTile = null;
                            humanMovedPiece = null;
                        }
                    }
                    invokeLater(new Runnable(){
                        public void run(){
                            boardPanel.drawBoard(chessBoard);
                        }
                    });
                }
                @Override
                public void mousePressed(MouseEvent e) {}
                @Override
                public void mouseReleased(MouseEvent e) {}
                @Override
                public void mouseEntered(MouseEvent e) {}
                @Override
                public void mouseExited(MouseEvent e) {}
            });
            validate();
        }

        private void highLightLegals(final Board board){
            if(highLightLegals){
                for(final Move move:pieceLegalMoves(board)){
                    if(move.getDestinationCoordinate()==this.tileId){
                        try {
                            add(new JLabel(
                                    new ImageIcon(
                                            ImageIO.read(new File("pieces/misc/green_dot.png")))));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        private Collection<Move> pieceLegalMoves(Board board) {
            if(humanMovedPiece != null && humanMovedPiece.getPieceAlliance()==board.currentPlayer().getAlliance()){
                return humanMovedPiece.calculateLegalMoves(board);
            }
            return Collections.emptyList();
        }
        private void assignTilePieceIcon(final Board board){
            this.removeAll();
            repaint();
            if(board.getTile(this.tileId).isTileOccupied()){
                try {
                    final BufferedImage image =ImageIO.read(new File(
                            pieceIconPath+board.getTile(this.tileId).getPiece().getPieceAlliance().toString().substring(0,1)+
                                    board.getTile(this.tileId).getPiece().toString()+".gif"));
                    add(new JLabel(new ImageIcon(image)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            validate();
        }

        private void assignTileColor() {
            final Color darkTileColor = Color.decode("#593E1A");
            final Color lightTileColor = Color.decode("#FFFACD");
            if(BoardUtils.EIGHTH_RANK[this.tileId]||
                    BoardUtils.SIXTH_RANK[this.tileId]||
                    BoardUtils.FOURTH_RANK[this.tileId]||
                    BoardUtils.SECOND_RANK[this.tileId]){
                setBackground(this.tileId %2 ==0?lightTileColor:darkTileColor);
            }
            else{
                setBackground(this.tileId %2 !=0?lightTileColor:darkTileColor);
            }
        }

        public void drawTile(final Board board) {
            assignTileColor();
            assignTilePieceIcon(board);
            highLightLegals(board);
            validate();
            repaint();
        }
    }
    public enum BoardDirection{
        NORMAL {
            @Override
            List<TilePanel> traverse(List<TilePanel> boardTiles) {
                return boardTiles;
            }
            @Override
            BoardDirection opposite() {
                return FLIPPED;
            }
        },
        FLIPPED {
            @Override
            List<TilePanel> traverse(List<TilePanel> boardTiles) {
                return Lists.reverse(boardTiles);
            }
            @Override
            BoardDirection opposite() {
                return NORMAL;
            }
        };
        abstract List<TilePanel> traverse(final List<TilePanel> boardTiles);
        abstract BoardDirection opposite();
    }
}