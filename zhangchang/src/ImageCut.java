import java.io.File;

public class ImageCut implements Runnable{
	
	static ImageUtils utl = null;
	
	public static int MAX_THREAD_COUNT = 3;
	public static int THREAD_COUNT = MAX_THREAD_COUNT;

	public static String IMAGE_TYPE_PNG = "png";// 可移植网络图形
	
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub

		String srcPath = "D:\\电子书\\处理中";
		String tagPath = "D:\\电子书\\已完成";
		
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
				
				while (!getThread()) {
					th.sleep(10000L);
				}
				th.start();
			}
			
		}
	}
	


	@Override
	public void run() {
		utl.imageAutoCut();
		// TODO Auto-generated method stub
		releaseThread();
	}
	
	public synchronized static boolean getThread() {
		if (THREAD_COUNT > 0) {
			THREAD_COUNT--;
			return true;
		} else {
			return false;
		}
	}
	
	public synchronized void releaseThread() {
		if (THREAD_COUNT < MAX_THREAD_COUNT) {
			THREAD_COUNT++;
			
		} else {
			
		}
	}

}
