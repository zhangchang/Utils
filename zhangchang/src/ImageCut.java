import java.io.File;

public class ImageCut implements Runnable{
	
	static ImageUtils utl = null;

	public static String IMAGE_TYPE_PNG = "png";// 可移植网络图形
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String srcPath = "F:\\电子书\\处理中";
		String tagPath = "F:\\电子书\\测试";
		
		String srcFolder = "";
		String tagFolder = "";
		
		File root = new File(srcPath);
		File newDir = null;
		File[] files = root.listFiles();
		
		ImageCut cut = new ImageCut();
		Thread th = null;
		
		for(File file:files) {
			if(file.isDirectory()){
				srcFolder = srcPath + "\\" +file.getName();
				tagFolder = tagPath + "\\" +file.getName();
				newDir = new File(tagFolder);
				newDir.mkdirs();
				System.out.println(srcFolder);
				System.out.println(tagFolder);
				cut.utl = new ImageUtils(srcFolder,tagFolder);
				th = new Thread(cut);
				th.start();
			}
			
		}
	}
	


	@Override
	public void run() {
		utl.imageAutoCut();
		// TODO Auto-generated method stub
		
	}

}
