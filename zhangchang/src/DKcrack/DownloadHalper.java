package DKcrack;

import java.io.*;
import java.net.*;
import java.util.*;

// This class downloads a file from a URL.
class DownloadHalper extends Observable implements Runnable {

	// Max size of download buffer.
	private static final int MAX_BUFFER_SIZE = 10240;

	// These are the status names.
	public static final String STATUSES[] = { "Downloading", "Paused",
			"Complete", "Cancelled", "Error" };

	// These are the status codes.
	public static final int DOWNLOADING = 0;
	public static final int PAUSED = 1;
	public static final int COMPLETE = 2;
	public static final int CANCELLED = 3;
	public static final int ERROR = 4;

	private URL url; // download URL
	private long size; // size of download in bytes
	private long downloaded; // number of bytes downloaded
	private int status; // current status of download

	private File path;
	private String localfilename;

	// Constructor for DownloadHalper.
	public DownloadHalper(URL url) {
		this.url = url;
		size = -1;
		downloaded = 0;
		status = DOWNLOADING;

		// Begin the download.
		//download();
	}

	// Get this download's URL.
	public String getUrl() {
		return url.toString();
	}

	// Get this download's size.
	public long getSize() {
		return size;
	}

	// Get this download's progress.
	public float getProgress() {
		return ((float) downloaded / size) * 100;
	}

	// Get this download's status.
	public int getStatus() {
		return status;
	}

	// Pause this download.
	public void pause() {
		status = PAUSED;
		stateChanged();
	}

	// Resume this download.
	public void resume() {
		status = DOWNLOADING;
		stateChanged();
		download();
	}

	// Cancel this download.
	public void cancel() {
		status = CANCELLED;
		stateChanged();
	}

	// Mark this download as having an error.
	private void error() {
		status = ERROR;
		stateChanged();
	}

	// Start or resume downloading.
	private void download() {
		Thread thread = new Thread(this);
		thread.start();
	}

	// Get file name portion of URL.
	private String getFileName(URL url) {
		String fileName = url.getFile();
		return fileName.substring(fileName.lastIndexOf('/') + 1);
	}

	// DownloadHalper file.
	public void run() {
		RandomAccessFile file = null;
		InputStream stream = null;

		try {
			// Open connection to URL.
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setReadTimeout(10000000);

			// Specify what portion of file to download.
			connection.setRequestProperty("Range", "bytes=" + downloaded + "-");

			// Connect to server.
			connection.connect();

			// Make sure response code is in the 200 range.
			if (connection.getResponseCode() / 100 != 2) {
				error();
			}

			// Check for valid content length.
			long contentLength = connection.getContentLength();
			if (contentLength < 1) {
				error();
			}

			// Set the size for this download if it hasn't been already set.
			if (size == -1) {
				size = contentLength;
				//stateChanged();
			}

			// Open file and seek to the end of it.
			if (!path.isDirectory()) {
				error();
			}

			// file = new RandomAccessFile(getFileName(url), "rw");
			file = new RandomAccessFile(new File(path.getPath() + "/"
					+ this.getLocalfilename()), "rw");
			file.seek(downloaded);

			stream = connection.getInputStream();
			while (status == DOWNLOADING) {
				// Size buffer according to how much of the file is left to
				// download.
				byte buffer[];
				if (size - downloaded > MAX_BUFFER_SIZE) {
					buffer = new byte[MAX_BUFFER_SIZE];
				} else {
					buffer = new byte[(int) (size - downloaded)];
				}

				// Read from server into buffer.
				int read = stream.read(buffer);
				if (read == -1)
					break;

				// Write buffer to file.
				file.write(buffer, 0, read);
				downloaded += read;

				//stateChanged();
			}

			// Change status to complete if this point was reached because
			// downloading has finished.
			if (status == DOWNLOADING) {
				status = COMPLETE;

				//stateChanged();
			}
		} catch (Exception e) {
			e.printStackTrace();
			error();
		} finally {
			// Close file.
			if (file != null) {
				try {
					file.close();
				} catch (Exception e) {
				}
			}

			// Close connection to server.
			if (stream != null) {
				try {
					stream.close();
				} catch (Exception e) {
				}
			}
		}
	}

	// Notify observers that this download's status has changed.
	private void stateChanged() {
		setChanged();
		notifyObservers();
	}

	private void setPath(String path) {
		this.path = new File(path);
	}

	private String getPath() {
		return path.getPath();
	}

	private void setLocalfilename(String fileName) {
		this.localfilename = fileName;
	}

	private String getLocalfilename() {
		return this.localfilename;
	}

	public static void main(String args[]) {
		try {
			DownloadHalper d = new DownloadHalper(new URL("http://book.read.duokan.com/mfsv2/download/s010/Hp01Lxj9ftuF/msuqW284g989n.epub"));
			//DownloadHalper d = new DownloadHalper(new URL("http://localhost/atlassian-jira.log"));
			d.setPath("D:/tmp");
			d.setLocalfilename("7d02cabc36ad46d2a6d8288e04c2d1e3.epub");
			//d.setLocalfilename("atlassian-jira.log");
			d.run();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}