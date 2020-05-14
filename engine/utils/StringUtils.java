package utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.Map;

public class StringUtils {
	
	public static byte[] getBytes(String str, Charset charset) {
		try {
			return str.getBytes(charset);
		} catch(UnsupportedCharsetException e) {
			throw new InconceivableException("'" + charset + "' is not a valid encoding.");
		}
	}
	
	public static String createFromBytes(byte[] data, Charset charset) {
		return new String(data, charset);
	}
	
	public static String encodeURL(String url, Charset charset) {
		try {
            return URLEncoder.encode(url, charset.toString());
        } catch (UnsupportedEncodingException ex) {
        	throw new InconceivableException("'" + charset + "' is not a valid encoding.");
        }
	}
	
	public static String toJSON(Map<String, String> data) {
		String result = "{";
		ArrayList<String> values = new ArrayList<String>();
		for(Map.Entry<String, String> entry: data.entrySet()) {
			result += "\"%s\":\"%s\",";
			values.add(entry.getKey());
			values.add(entry.getValue());
		}
		result = result.substring(0, result.length() - 1) + "}";
		return String.format(result, values.toArray());
	}
	
}
