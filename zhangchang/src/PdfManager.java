
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfManager {
	public static File Pdf(ArrayList<String> imageUrllist,
			String mOutputPdfFileName) throws MalformedURLException, IOException, DocumentException {
		//String TAG = "PdfManager";
		Rectangle pdfSize = new Rectangle(720, 1280);
		//Document doc = new Document(PageSize.A4, 0, 0, 0, 0);

		Image img = Image.getInstance(imageUrllist.get(0));
		Document doc = new Document(img);
		PdfWriter.getInstance(doc, new FileOutputStream(mOutputPdfFileName));
		
		try {
			PdfWriter.getInstance(doc, new FileOutputStream(mOutputPdfFileName));
			doc.open();
			for (int i = 0; i < imageUrllist.size(); i++) {

				//doc.add(new Paragraph("简单使用iText"));
				img = Image.getInstance(imageUrllist.get(i));
				/*
				float heigth = png1.getHeight();
				float width = png1.getWidth();
				//int percent = getPercent2(heigth, width);
				float percent = (float) (3.0/2.0);
				doc.setPageSize(new Rectangle(width, heigth));
				doc.setMargins(0, 0, 0, 0);
				doc.newPage();
				png1.setAlignment(Image.LEFT);
				png1.scalePercent(percent*100);// 表示是原来图像的比例;
				//png1.scaleAbsolute(width, heigth);
				//png1.scaleToFit(width, heigth);
				png1.scaleToFit(width*percent, heigth*percent);
				//png1.setRotation((float) (Math.PI/6));
				*/
				doc.setPageSize(img);
				doc.newPage();
				img.setAbsolutePosition(0, 0);
				doc.add(img);
			}
			doc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		File mOutputPdfFile = new File(mOutputPdfFileName);
		if (!mOutputPdfFile.exists()) {
			mOutputPdfFile.deleteOnExit();
			return null;
		}
		return mOutputPdfFile;
	}

	/**
	 * 第一种解决方案 在不改变图片形状的同时，判断，如果h>w，则按h压缩，否则在w>h或w=h的情况下，按宽度压缩
	 * 
	 * @param h
	 * @param w
	 * @return
	 */

	public static int getPercent(float h, float w) {
		int p = 0;
		float p2 = 0.0f;
		if (h > w) {
			p2 = 297 / h * 100;
		} else {
			p2 = 210 / w * 100;
		}
		p = Math.round(p2);
		return p;
	}

	/**
	 * 第二种解决方案，统一按照宽度压缩 这样来的效果是，所有图片的宽度是相等的，自我认为给客户的效果是最好的
	 * 
	 * @param args
	 */
	public static int getPercent2(float h, float w) {
		int p = 0;
		float p2 = 0.0f;
		p2 = 530 / w * 100;
		p = Math.round(p2);
		return p;
	}
}