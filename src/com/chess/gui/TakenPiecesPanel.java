package com.chess.gui;

import com.chess.engine.board.Move;
import com.chess.engine.piece.Piece;
import com.chess.gui.Table.MoveLog;
import org.omg.SendingContext.RunTime;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by Root on 12/13/2016.
 */
public class TakenPiecesPanel extends JPanel {
    private final static Color Panel_Color = Color.decode("0xFDFE6");
    private final static Dimension Taken_Pieces_Dimension = new Dimension(40,80);
    private static final EtchedBorder Panel_Border = new EtchedBorder(EtchedBorder.RAISED);

    private final JPanel northPanel,southPanel;

    TakenPiecesPanel(){
        super(new BorderLayout());
        this.setBackground(Panel_Color);
        this.setBorder(Panel_Border);
        this.northPanel = new JPanel(new GridLayout(8,2));
        this.southPanel = new JPanel(new GridLayout(8,2));
        this.northPanel.setBackground(Panel_Color);
        this.southPanel.setBackground(Panel_Color);
        this.add(northPanel, BorderLayout.NORTH);
        this.add(southPanel, BorderLayout.SOUTH);
        setPreferredSize(Taken_Pieces_Dimension);
    }
    public void redo(final MoveLog moveLog){
        this.southPanel.removeAll();
        this.northPanel.removeAll();
        final List<Piece> whiteTakenPieces = new ArrayList<>();
        final List<Piece> blackTakenPieces = new ArrayList<>();
        for(final Move move:moveLog.getMoves()){
            if(move.isAttack()){
                final Piece takenPiece = move.getAttackedPiece();
                if(takenPiece.getPieceAlliance().isBlack()){
                    blackTakenPieces.add(takenPiece);
                }
                else if(takenPiece.getPieceAlliance().isWhite()){
                    whiteTakenPieces.add(takenPiece);
                }
                else{
                    throw new RuntimeException("Should Not Reach Here!!!");
                }
            }
        }
    }
}
