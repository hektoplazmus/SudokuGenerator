/*
 * The MIT License (MIT)
 * Copyright (c) 2017 Jan Pancíř
 * www.janpancir.com
 */
package com.janpancir.sudokugenerator;

import com.itextpdf.text.BaseColor;
import static com.janpancir.sudokugenerator.Setup.FONT;
import java.awt.Desktop;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;

import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;

/**
 *
 * @author pancijan
 */
public class FXMLController implements Initializable {

    @FXML
    private Label label_result, font_preview;

    @FXML
    private ComboBox combobox_sudokuCount, combobox_difficulty;

    @FXML
    private Button btn_generate, btn_show, btn_print, btn_save, btn_saveSettings;

    @FXML
    private ColorPicker colorPicker_font, colorPicker_line;

    @FXML
    Rectangle line_preview;

    @FXML
    private AnchorPane pane_settings, pane_main;

    @FXML
    private Slider slider_fontSize;

    @FXML
    private void handleButtonAction() {

    }

    @FXML
    private void btn_generateClick() {
        PDFCreator creator = new PDFCreator();
        if (creator.generate(Integer.parseInt(combobox_sudokuCount.getValue().toString()),
                combobox_difficulty.getValue().toString())) {
            label_result.setText("Sudoku úspěšně vygenerováno.");
            btn_show.setDisable(false);
            btn_print.setDisable(false);
            btn_save.setDisable(false);
        } else {
            label_result.setText("Generování se pokazilo...");
        }

    }

    @FXML
    private void btn_showClick() {
        if (Desktop.isDesktopSupported()) {
            try {
                File myFile = new File("sudoku.pdf");
                Desktop.getDesktop().open(myFile);
            } catch (IOException ex) {
                label_result.setText("Nepodařilo se najít PDF prohlížeč.");
            }
        }
    }

    @FXML
    private void colorPicker_font_ColorChanged() {
        font_preview.setTextFill(colorPicker_font.getValue());
        Color color = colorPicker_font.getValue();

        Setup.FONT_COLOR = new BaseColor((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue());
    }

    @FXML
    private void colorPicker_line_ColorChanged() {
        Color color = colorPicker_line.getValue();
        line_preview.strokeProperty().set(color);
        Setup.LINE_COLOR = new BaseColor((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue());
    }

    @FXML
    private void slider_fontSize_valueChanged() {

    }

    @FXML
    private void btn_settingsClick() {
        pane_settings.setVisible(true);
        //pane_main.setVisible(false);
    }

    @FXML
    private void btn_saveSettingsClick() {
        pane_settings.setVisible(false);
        //pane_main.setVisible(true);
    }

    @FXML
    private void btn_printClick() {
        String filename = "sudoku.pdf";
        PDDocument document;
        try {
            document = PDDocument.load(new File(filename));

            //takes standard printer defined by OS
            PrintService myPrintService = PrintServiceLookup.lookupDefaultPrintService();
            PrinterJob job;
            job = PrinterJob.getPrinterJob();

            job.setPageable(new PDFPageable(document));
            job.setPrintService(myPrintService);
            if (job.printDialog()) {
                try {
                    job.print();
                } catch (PrinterException exc) {
                    System.out.println(exc);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PrinterException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static PrintService findPrintService(String printerName) {
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        for (PrintService printService : printServices) {
            if (printService.getName().trim().equals(printerName)) {
                return printService;
            }
        }
        return null;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        pane_settings.setVisible(false);
        //pane_main.setVisible(true);

        ObservableList<String> options = FXCollections.observableArrayList();
        for (int i = 1; i <= 50; i++) {
            options.add(Integer.toString(i));
        }
        combobox_sudokuCount.getItems().addAll(options);
        combobox_sudokuCount.setValue(options.get(0));

        ObservableList<String> options2 = FXCollections.observableArrayList("všechny náhodně", "velmi těžké", "těžké", "střední", "jednoduché", "velmi jednoduché");
        combobox_difficulty.getItems().addAll(options2);
        combobox_difficulty.setValue(options2.get(0));

        btn_show.setDisable(true);
        btn_print.setDisable(true);
        btn_save.setDisable(true);

        colorPicker_font.setValue(Color.BLACK);
        colorPicker_line.setValue(Color.BLACK);

        slider_fontSize.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                int fontSize = (int) slider_fontSize.getValue();
                FONT.setSize(fontSize - 40);
                font_preview.setFont(new Font(font_preview.getFont().getName(), fontSize));
                font_preview.setTranslateX(50 - fontSize / 3);
                font_preview.setTranslateY(75 - fontSize / 2);
            }
        });

        btn_save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                try {

                    FileChooser fileChooser = new FileChooser();

                    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
                    fileChooser.getExtensionFilters().add(extFilter);

                    File file = fileChooser.showSaveDialog(((Node) event.getTarget()).getScene().getWindow());
                    if (file == null) {
                        return;
                    }
                    File src = new File("sudoku.pdf");

                    Files.copy(src.toPath(), file.toPath(), REPLACE_EXISTING);
                    label_result.setText("Sudoku úspěšně uloženo.");
                } catch (Exception ex) {
                    Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
                    label_result.setText("Sudoku se nepodařilo uložit.");
                }
            }
        });

    }
}
