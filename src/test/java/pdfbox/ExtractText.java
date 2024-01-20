package pdfbox;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import pdfbox.PDFTextStripperWithLocations;

public class ExtractText {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

            PDDocument document = PDDocument.load(new File("E:\\B4J\\test\\PDFBOX\\Objects\\2.pdf"));
            List<String> text = PDFTextStripperWithLocations.extractLines(document);
            System.out.println(text);



	}

}
