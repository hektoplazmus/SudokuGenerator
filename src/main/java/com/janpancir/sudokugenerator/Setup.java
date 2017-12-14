/*
 * The MIT License (MIT)
 * Copyright (c) 2017 Jan Pancíř
 * www.janpancir.com
 */
package com.janpancir.sudokugenerator;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;

/**
 *
 * @author pancijan
 */
public class Setup {

    public static BaseColor FONT_COLOR = BaseColor.BLACK;
    public static BaseColor LINE_COLOR = BaseColor.BLACK;
    public static Font FONT = null;

    public Setup() {
        FontFactory.register("CalibriBold.tff", "Calibri");
        Font f1 = FontFactory.getFont("Calibri");
        FONT = f1;
        FONT.setSize(100);
    }

}
