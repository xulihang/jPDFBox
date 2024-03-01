package pdfbox;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.*;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingMode;

import java.io.File;
import java.io.IOException;

public class SearchablePDFCreator {
	
	public static PDFont loadFont(PDDocument doc, String path) throws IOException {
		PDFont font = PDType0Font.load(doc, new File(path));
		return font;
	}
	
    /**
     * Add text overlay to an existing PDF page
     * @param contentStream - PDF content stream
     * @param result - OCR result
     * @param pageHeight - Height of the image
     */
    public static void addTextOverlay(PDPageContentStream contentStream,OCRResult result, double pageHeight) throws IOException {
        addTextOverlay(contentStream,result,pageHeight,PDType1Font.TIMES_ROMAN);
    }
    /**
     * Add text overlay to an existing PDF page
     * @param contentStream - PDF content stream
     * @param result - OCR result
     * @param pageHeight - Height of the image
     * @param pdFont - Specify a font for evaluation of the position
     */
    public static void addTextOverlay(PDPageContentStream contentStream,OCRResult result, double pageHeight, PDFont pdFont) throws IOException {
        addTextOverlay(contentStream,result,pageHeight,pdFont,1.0);
    }
    /**
     * Add text overlay to an existing PDF page
     * @param contentStream - PDF content stream
     * @param result - OCR result
     * @param pageHeight - Height of the image
     * @param pdFont - Specify a font for evaluation of the position
     * @param percent - image's height / page's height
     */
    public static void addTextOverlay(PDPageContentStream contentStream,OCRResult result, double pageHeight, PDFont pdFont,double percent) throws IOException {
        PDFont font = pdFont;
        contentStream.setFont(font, 16);
        contentStream.setRenderingMode(RenderingMode.NEITHER);
        for (int i = 0; i <result.lines.size() ; i++) {
            TextLine line = result.lines.get(i);
            FontInfo fi = calculateFontSize(font,line.text, (float) (line.width * percent), (float) (line.height * percent));
            contentStream.beginText();
            contentStream.setFont(font, fi.fontSize);
            contentStream.newLineAtOffset((float) (line.left * percent), (float) ((pageHeight - line.top - line.height) * percent));
            contentStream.showText(line.text);
            contentStream.endText();
        }
    }

    private static FontInfo calculateFontSize(PDFont font, String text, float bbWidth, float bbHeight) throws IOException {
        int fontSize = 17;
        float textWidth = font.getStringWidth(text) / 1000 * fontSize;
        float textHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;

        if(textWidth > bbWidth){
            while(textWidth > bbWidth){
                fontSize -= 1;
                textWidth = font.getStringWidth(text) / 1000 * fontSize;
                textHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;
            }
        }
        else if(textWidth < bbWidth){
            while(textWidth < bbWidth){
                fontSize += 1;
                textWidth = font.getStringWidth(text) / 1000 * fontSize;
                textHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;
            }
        }

        FontInfo fi = new FontInfo();
        fi.fontSize = fontSize;
        fi.textHeight = textHeight;
        fi.textWidth = textWidth;

        return fi;
    }

}
