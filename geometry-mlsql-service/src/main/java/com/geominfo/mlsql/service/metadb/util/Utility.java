package com.geominfo.mlsql.service.metadb.util;

import java.util.regex.Pattern;

/**
 * Tool class
 * 
 * @author xumh
 *
 */
public final class Utility {
	private static final Pattern isAllWhitespacePattern = Pattern.compile("^\\s*$");
	
	  /**
	   * Checks if the text is null or empty.
	   * 
	   * @param text
	   *        Text to check.
	   * @return Whether the string is blank.
	   */
	  public static boolean isBlank(final String text)
	  {
	    return text == null || text.isEmpty()
	           || isAllWhitespacePattern.matcher(text).matches();
	  }
	  
	  public static String quote(String s){
		  return "'"+s+"'";
	  }
}
