package pdfbox;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.apache.pdfbox.pdmodel.PDDocument;
import pdfbox.PDF2Image;

public class Convert2Images {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		int dpi=75;
		File pdf = new File("E:\\B4J\\test\\PDFBOX\\Objects\\1.pdf");
		PDDocument document = PDDocument.load(pdf);
		int pageIndex=1;
		for (BufferedImage img : PDF2Image.RenderToImages(document, dpi)) {
			File f = new File(pdf.getAbsolutePath()+"-"+pageIndex+".jpg");
			ImageIO.write(img, "jpg", f);
			pageIndex=pageIndex+1;
		}
	}

}
