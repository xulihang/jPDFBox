package pdfbox;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

public class PDF2Image {
	
	public static List<BufferedImage> RenderToImages(PDDocument document, int dpi) throws IOException{
		PDFRenderer render = new PDFRenderer(document);
		List<BufferedImage> images = new ArrayList<BufferedImage>();
		for (int i=0;i<=document.getNumberOfPages()-1;i++){
			images.add(render.renderImageWithDPI(i, dpi));
		}
		return images;
	}
	
	public static BufferedImage RenderPageImage(PDDocument document, int dpi,int pageNum) throws IOException{
		PDFRenderer render = new PDFRenderer(document);
		return render.renderImageWithDPI(pageNum, dpi);
	}
}
