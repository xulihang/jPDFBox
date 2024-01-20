package pdfbox;

import java.util.List;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.apache.pdfbox.pdmodel.PDDocument;

import javafx.scene.image.Image;
import pdfbox.PDF2Image;

public class ExtractImages {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		PDFBoxWrapper pdfWrapper = new PDFBoxWrapper();
		File pdf = new File("C:\\Users\\xulihang\\Documents\\1.pdf");
		pdfWrapper.Initialize(pdf.getAbsolutePath());
		List<Image> images = pdfWrapper.ExtractImagesOfOnePage(0);
		System.out.println(images.size());
	}

}
