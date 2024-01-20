package pdfbox;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4a.BA.Version;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import pdfbox.PDFTextStripperWithLocations;
import pdfbox.PDF2Image;

/**
 * PDFBOX
 */
@ShortName("PDFBox")
@Version(1.10f)
public class PDFBoxWrapper {
	public PDDocument document;
	public void Initialize(String filePath) throws IOException {
		document = PDDocument.load(new File(filePath));
	}
	
	public int GetNumberOfPages() {
		return document.getNumberOfPages();
	}
	
	public List<Image> GetImagesOfPages(int dpi) throws IOException{
		List<Image> fxImgs = new ArrayList<Image>();
		for (BufferedImage img : PDF2Image.RenderToImages(document, dpi)) {
			fxImgs.add(SwingFXUtils.toFXImage(img,null));
		}
		return fxImgs;
	}
	
	public List<Image> ExtractImagesOfOnePage(int pageIndex) throws IOException{		
		List<Image> fxImgs = new ArrayList<Image>();
		PDPage page = document.getPage(pageIndex);
		PDResources pdResources = page.getResources();
        for (COSName cosName:pdResources.getXObjectNames()){ 
	        if (cosName != null) {
	            PDXObject image = pdResources.getXObject(cosName);
	            if(image instanceof PDImageXObject){
	                PDImageXObject pdImage = (PDImageXObject)image;
	                BufferedImage imageStream = pdImage.getImage();
	                fxImgs.add(SwingFXUtils.toFXImage(imageStream,null));
	            }
	        }
        }
		return fxImgs;
	}

	public Image GetImageOfOnePage(int dpi, int pageIndex) throws IOException{		
		return SwingFXUtils.toFXImage(PDF2Image.RenderPageImage(document, dpi, pageIndex),null);
	}
	
	public List<String> GetLinesOfPages() throws IOException{
		List<String> textOfPages = PDFTextStripperWithLocations.extractLines(document);
        return textOfPages;
	}
	
	public String GetLinesOfOnePage(int pageNum) throws IOException{
		String text = PDFTextStripperWithLocations.extractLinesofOnePage(document, pageNum);
        return text;
	}	
	
	public List<String> GetWordsOfPages() throws IOException{
		List<String> wordsOfPages = PDFTextStripperWithLocations.extractWords(document);
        return wordsOfPages;
	}
	
	public String GetWordsOfOnePage(int pageNum) throws IOException{
		String words = PDFTextStripperWithLocations.extractWordsOfOnePage(document, pageNum);
        return words;
	}	
	
	public void Close() throws IOException{
		document.close();
	}
}
