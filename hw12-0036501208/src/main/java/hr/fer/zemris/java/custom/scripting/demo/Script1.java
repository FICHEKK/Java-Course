package hr.fer.zemris.java.custom.scripting.demo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

/**
 * Script 1 demonstrates a simple for-loop construct.
 *
 * @author Filip Nemec
 */
public class Script1 {

	/**
	 * Program starts from here.
	 *
	 * @param args none are used.
	 */
	public static void main(String[] args) {
		String documentBody = readFromDisk("webroot/scripts/osnovni.smscr");
		Map<String, String> parameters = new HashMap<String, String>();
		Map<String, String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		
		// create engine and execute it
		new SmartScriptEngine(
				new SmartScriptParser(documentBody).getDocumentNode(),
				new RequestContext(System.out, parameters, persistentParameters, cookies, null)
		).execute();
	}

	/**
	 * Returns the text stored in the given file as a single
	 * {@code String}.
	 * 
	 * @param filename file path
	 * @return {@code String} of the file contents, or {@code null}
	 * 		   if exception occurred
	 */
	private static String readFromDisk(String filePath) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		try (InputStream is = Files.newInputStream(Paths.get(filePath))) {
			byte[] buffer = new byte[1024];
			while (true) {
				int r = is.read(buffer);
				if (r < 1) break;
				bos.write(buffer, 0, r);
			}
			return new String(bos.toByteArray(), StandardCharsets.UTF_8);
			
		} catch (IOException ex) {
			return null;
			
		}
	}
}
