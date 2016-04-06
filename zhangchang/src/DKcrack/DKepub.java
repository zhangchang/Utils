package DKcrack;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import net.sf.json.JSONObject;

public class DKepub {

	public static String dkHost = "http://www.duokan.com";
	public static String epubUrlBase = "/store/v0/android/book/check_update";
	public static String localPath = "E:/DEV/DKepub";

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		getEpubDownloadUrlFromBookId("74359f8c2cbd45a5a825beffeaf0a172");
	}

	public static String getEpubDownloadUrlFromBookId(String bookid) throws Exception {

		String param_t = "t=1";
		String paramStr = param_t + "&" + "book_id=" + bookid;
		String resultStr = request(dkHost+epubUrlBase,paramStr);
		
		JSONObject jsonObj = getJSONFromString(resultStr);
		jsonObj.getJSONArray("items");
		String epubUrl = jsonObj.getJSONArray("items").getJSONObject(0).getString("epub");
		return epubUrl;
	}
	
	public static void downloadEpubFromUrl(String url,String localFileName) throws Exception {
		URL restURL = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) restURL.openConnection();
		conn.setRequestMethod("GET");
		conn.setDoOutput(true);
		conn.setAllowUserInteraction(false);
		//PrintStream ps = new PrintStream(conn.getOutputStream());
		//ps.print(query);
		//ps.close();
		BufferedReader bReader = new BufferedReader(new InputStreamReader(
				conn.getInputStream()));
		String line, resultStr = "";
		while (null != (line = bReader.readLine())) {
			resultStr += line;
		}
		bReader.close();
		//return resultStr;
	}

	public static String request(String url, String query) throws Exception {
		URL restURL = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) restURL.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setAllowUserInteraction(false);
		PrintStream ps = new PrintStream(conn.getOutputStream());
		ps.print(query);
		ps.close();
		BufferedReader bReader = new BufferedReader(new InputStreamReader(
				conn.getInputStream()));
		String line, resultStr = "";
		while (null != (line = bReader.readLine())) {
			resultStr += line;
		}
		bReader.close();
		return resultStr;
	}
	
	public static JSONObject getJSONFromString(String jsonStr) {
		JSONObject jsonObj = JSONObject.fromObject(jsonStr);
		return jsonObj;
	}
}
