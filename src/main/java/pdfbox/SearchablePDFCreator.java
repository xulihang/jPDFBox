package pdfbox;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.*;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingMode;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
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
     * @param page - PDPage
     */
    public static void addTextOverlay(PDPageContentStream contentStream,OCRResult result, double pageHeight, PDPage page) throws IOException {
        addTextOverlay(contentStream,result,pageHeight,page,PDType1Font.TIMES_ROMAN);
    }
    /**
     * Add text overlay to an existing PDF page
     * @param contentStream - PDF content stream
     * @param result - OCR result
     * @param pageHeight - Height of the image
     * @param page - PDPage
     * @param pdFont - Specify a font for evaluation of the position
     */
    public static void addTextOverlay(PDPageContentStream contentStream,OCRResult result, double pageHeight, PDPage page, PDFont pdFont) throws IOException {
        addTextOverlay(contentStream,result,pageHeight,page,pdFont,1.0,false);
    }
    /**
     * Add text overlay to an existing PDF page
     * @param contentStream - PDF content stream
     * @param result - OCR result
     * @param pageHeight - Height of the image
     * @param page - PDPage
     * @param pdFont - Specify a font for evaluation of the position
     * @param percent - image's height / page's height
     */
    public static void addTextOverlay(PDPageContentStream contentStream,OCRResult result, double pageHeight, PDPage page, PDFont pdFont,double percent,boolean displayText) throws IOException {
        PDFont font = pdFont;
        contentStream.setFont(font, 16);
        float pageWidth = page.getMediaBox().getWidth();
        float pageHeightPdf = page.getMediaBox().getHeight();
        if (displayText) {
            contentStream.setRenderingMode(RenderingMode.FILL);
        }else{
            contentStream.setRenderingMode(RenderingMode.NEITHER);
        }

        for (int i = 0; i <result.lines.size() ; i++) {
            TextLine line = result.lines.get(i);
            FontInfo fi = calculateFontSize(font,line.text, (float) (line.width * percent), (float) (line.height * percent));
            float x = (float)(line.left * percent);
            float y = (float)((pageHeight - line.top - line.height) * percent);

            if (x < 0) x = 0;

            float maxWidth = pageWidth - x;
            if (fi.textWidth > maxWidth) {
                System.out.println("Adjusted font size to fit width. Previous size: " + fi.fontSize);
                fi.fontSize = (int)(fi.fontSize * (maxWidth / fi.textWidth));
                System.out.println("Adjusted font size to fit width. Adjusted size: " + fi.fontSize);
                fi.textWidth = font.getStringWidth(line.text)/1000 * fi.fontSize;
            }

            if (y < 0) y = 0;

            if (y + fi.textHeight > pageHeightPdf) {
                y = pageHeightPdf - fi.textHeight;
            }

            contentStream.beginText();
            contentStream.setFont(font, fi.fontSize);
            contentStream.newLineAtOffset(x, y);
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
