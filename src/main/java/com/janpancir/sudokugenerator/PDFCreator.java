/*
 * The MIT License (MIT)
 * Copyright (c) 2017 Jan Pancíř
 * www.janpancir.com
 */
package com.janpancir.sudokugenerator;

/**
 *
 * @author pancijan
 */
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.FontSelector;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import static com.janpancir.sudokugenerator.Setup.FONT;
import static com.janpancir.sudokugenerator.Setup.FONT_COLOR;
import static com.janpancir.sudokugenerator.Setup.LINE_COLOR;
import com.janpancir.sudokugenerator.sudoku.Generator;
import com.janpancir.sudokugenerator.sudoku.Grid;
import java.io.FileOutputStream;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PDFCreator {

    private static final String FILE = "sudoku.pdf";

    private static final float marginTop = 15;
    private static final float marginLeft = 20;

    Document document;

    public PDFCreator() {

    }

    public boolean generate(int count, String difficulty) {
        document = new Document();
        document.setMargins(marginLeft, marginLeft, marginTop, marginTop);
        try {

            PdfWriter.getInstance(document, new FileOutputStream(FILE));
            document.open();
            addMetaData();
            addSudoku(count,difficulty);
            document.close();

        } catch (Exception e) {
            e.printStackTrace();
            return false;

        }

        return true;
    }

    // iText allows to add metadata to the PDF which can be viewed in your Adobe
    // Reader
    // under File -> Properties
    private void addMetaData() {
        document.addTitle("Moje sudoku");
        document.addSubject("sudoku");
        document.addKeywords("sudoku");
        document.addAuthor("Jan Pancir");
        document.addCreator("Jan Pancir");
    }

    private void addSudoku(int count,String difficulty) throws DocumentException {
        
        Font f1 = FONT;
        f1.setColor(FONT_COLOR);
        Generator generator = new Generator();
        
        for (int sudokuCnt = 1; sudokuCnt <= count; sudokuCnt++) {
            
            FontSelector selector = new FontSelector();

            int emptySpaces = getEmptySpacesByDifficulty(difficulty);
            
            Grid grid = generator.generate(emptySpaces);

            PdfPTable table = new PdfPTable(9);
            table.setWidthPercentage(100);
            table.setSpacingBefore(0f);
            table.setSpacingAfter(0f);
            
            float cellHeight = (document.getPageSize().getHeight() - marginTop * 2) / 9;

            
           // f1.setSize();
            f1.setStyle(Font.BOLD);
            selector.addFont(f1);

            int borderWidth = 2;
            int borderWidthBold = 4;

            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    int value = grid.getCell(i, j).getValue();
                    String valString = Integer.toString(value);
                    
                    if (value == 0) {
                        valString = "";
                    }
                    Phrase ph = selector.process(valString);
                    PdfPCell c1 = new PdfPCell(ph);
                    c1.setBorderColor(LINE_COLOR);
                    c1.setUseAscender(true);
                    c1.setBorderWidth(borderWidth);
                    c1.setFixedHeight(cellHeight);
                    c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    c1.setHorizontalAlignment(Element.ALIGN_CENTER);

                    if (i == 0) {
                        c1.setBorderWidthTop(borderWidth);
                    }
                    if (i == 8) {
                        c1.setBorderWidthBottom(borderWidth);
                    }
                    if (j == 0) {
                        c1.setBorderWidthLeft(borderWidth);
                    }
                    if (j == 8) {
                        c1.setBorderWidthRight(borderWidth);
                    }
                    if (i % 3 == 1 && j % 3 == 1) {
                        c1.setBorderWidthTop(borderWidth);
                        c1.setBorderWidthBottom(borderWidth);
                        c1.setBorderWidthLeft(borderWidth);
                        c1.setBorderWidthRight(borderWidth);

                    }
                    if (i == 2 || i == 5) {
                        c1.setBorderWidthBottom(borderWidthBold);
                    }
                    if (i == 3 || i == 6) {
                        c1.setBorderWidthTop(borderWidthBold);
                    }

                    if (j == 2 || j == 5) {
                        c1.setBorderWidthRight(borderWidthBold);
                    }
                    if (j == 3 || j == 6) {
                        c1.setBorderWidthLeft(borderWidthBold);
                    }

                    table.addCell(c1);
                }
            }

            try {
                document.add(table);
            } catch (DocumentException ex) {
                Logger.getLogger(PDFCreator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public int getEmptySpacesByDifficulty(String dif) {
        Random rnd = new Random();

        if (dif.equals("velmi těžké")) {
            return rnd.nextInt(5)+60;
        } else if (dif.equals("těžké")) {
            return rnd.nextInt(5)+50;
        } else if (dif.equals("střední")) {
            return rnd.nextInt(5)+40;
        } else if (dif.equals("jednoduché")) {
            return rnd.nextInt(5)+30;
        } else if (dif.equals("velmi jednoduché")) {
            return rnd.nextInt(5)+20;
        } else 
            return rnd.nextInt(45)+20; 
            //všechny náhodně    
    }
}
