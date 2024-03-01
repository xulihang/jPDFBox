package pdfbox;

import java.util.ArrayList;

public class OCRResult {
    public ArrayList<TextLine> lines;

    public OCRResult(ArrayList<TextLine> lines) {
        this.lines = lines;
    }
}
