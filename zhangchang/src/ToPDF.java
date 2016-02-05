import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import com.itextpdf.text.DocumentException;

public class ToPDF {
	public static void main(String[] args) throws MalformedURLException, IOException, DocumentException {
		// TODO Auto-generated method stub
		String rootPath = "D:\\电子书\\已完成";
		
		File root = new File(rootPath);
		File[] files = root.listFiles();
		
		for(File file:files) {
			if(file.isDirectory()){
				ArrayList<String> imageUrllist = new ArrayList<String>();
				
				ImageUtils im = new ImageUtils();
				
				
				
				imageUrllist = im.sortFileList(file.getAbsolutePath());
				String pdfUrl = root + "\\" + file.getName() + ".pdf";
				File pdffile = PdfManager.Pdf(imageUrllist, pdfUrl);
				try {
					pdffile.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		

	}
}