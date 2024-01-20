package pdfbox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

public class PDFTextStripperWithLocations
{

	public static String extractWordsOfOnePage(PDDocument document,int PageNum) throws IOException
    {
        PDFTextStripper stripper = new PDFTextStripper()
        {
            @Override
            protected void writeString(String text, List<TextPosition> textPositions) throws IOException
            {                
                TextPosition firstPosition = textPositions.get(0);
                TextPosition lastPosition = textPositions.get(textPositions.size() - 1);
                float PageHeight=firstPosition.getPageHeight();
                float PageWidth=firstPosition.getPageWidth();
                float X,Y,width,height;
                X=firstPosition.getXDirAdj();
                Y=firstPosition.getYDirAdj();
                width=lastPosition.getXDirAdj() + lastPosition.getWidthDirAdj() - X;
                height=lastPosition.getYDirAdj() + lastPosition.getHeightDir() - Y;
                X=X/PageWidth;
                Y=Y/PageHeight;
                width=width/PageWidth;
                height=height/PageHeight;
                writeString(String.format("%s	%s	%s	%s	", X, Y, width, height));
                super.writeString(text, textPositions);
            }
        };
        stripper.setSortByPosition(true);
        return stripper.getText(document);
    }
	
	public static List<String> extractWords(PDDocument document) throws IOException
    {
		List<String> wordsOfPages = new ArrayList<String>();
		for (int i=1;i<=document.getNumberOfPages();i++){
			wordsOfPages.add(extractWordsOfOnePage(document,i));
		}
		return wordsOfPages;	
    }
	
	public static String extractLinesofOnePage(PDDocument document,int PageNum) throws IOException
	    {
	        PDFTextStripper stripper = new PDFTextStripper()
	        {
	            @Override
	            protected void startPage(PDPage page) throws IOException
	            {
	                startOfLine = true;
	                super.startPage(page);
	            }

	            @Override
	            protected void writeLineSeparator() throws IOException
	            {
	                startOfLine = true;
	                super.writeLineSeparator();
	            }

	            @Override
	            protected void writeString(String text, List<TextPosition> textPositions) throws IOException
	            {
	                if (startOfLine)
	                {
	                    TextPosition firstPosition = textPositions.get(0);
	                    float PageHeight=firstPosition.getPageHeight();
	                    float PageWidth=firstPosition.getPageWidth();
	                    float X,Y,maxX,maxY;
	                    maxX=0;
	                    maxY=0;	          
	                    X= firstPosition.getX();
	                    Y= firstPosition.getY();
	                    for (int i=0;i<=textPositions.size()-1;i++){
	                    	TextPosition pos = textPositions.get(i);
	                    	X=Math.min(X, pos.getX());
	                    	Y=Math.min(Y, pos.getY());
	                    	maxX = Math.max(pos.getX()+pos.getWidth(),maxX);	                    			;
	                    	maxY = Math.max(pos.getY()+pos.getHeight(),maxY);	                    	
	                    }	   
	                    float width,height;
	                    width=maxX-X;
	                    height=maxY-Y;
	                    writeString(String.format("%s	%s	%s	%s	", X/PageWidth,(Y-height)/PageHeight,width/PageWidth,height/PageHeight));
	                    startOfLine = false;
	                }
	                super.writeString(text, textPositions);
	            }
	            boolean startOfLine = true;
	        };
	        //stripper.setSortByPosition(true);
	        stripper.setStartPage(PageNum);
	        stripper.setEndPage(PageNum);	        
	        return stripper.getText(document);
	    }
	
	public static List<String> extractLines(PDDocument document) throws IOException
    {
		List<String> textOfPages = new ArrayList<String>();
		for (int i=1;i<=document.getNumberOfPages();i++){
			textOfPages.add(extractLinesofOnePage(document,i));
		}
		return textOfPages;		
    }

}