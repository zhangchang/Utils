import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import com.itextpdf.text.DocumentException;

public class ToPDF {
	public static void main(String[] args) throws MalformedURLException, IOException, DocumentException {
		// TODO Auto-generated method stub
		ArrayList<String> imageUrllist = new ArrayList<String>();
		
		ImageUtils im = new ImageUtils();
		
		im.getFiles("D:\\test");
		
		imageUrllist = im.fileList;
		//imageUrllist.add("D:\\test\\SNAG-0000.png");
		//imageUrllist.add("D:\\test\\SNAG-0001.png");
		//imageUrllist.add("D:\\test\\SNAG-0002.png");
		String pdfUrl = "D:\\test2\\test.pdf";
		File file = PdfManager.Pdf(imageUrllist, pdfUrl);
		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}