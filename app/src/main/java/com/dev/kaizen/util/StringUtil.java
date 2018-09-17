/**
 * 
 */
package com.dev.kaizen.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;

/**
 * @author Anggoro Biandono
 * 6:05:38 PM
 */
public final class StringUtil {
	private StringUtil() {
		
	}
	
	public static boolean isNotBlank(String string){
		if(string!=null && string.trim().length()>0)
			return true;
		else
			return false;
	}
	
	public static boolean isBlank(String string) {
		if (string == null || string.trim().length() < 1)
			return true;
		else
			return false;
	}
	
	public static boolean StringContainAnywhere(String param, String search){
		return param.toLowerCase().matches("(?i).*"+ search.toLowerCase() +".*");
	}
	
	public static String replaceStr(String source, String find, String replace){
		//
        // Compiles the given regular expression into a pattern
        //
        Pattern pattern = Pattern.compile(find);

        //
        // Creates a matcher that will match the given input against the pattern
        //
        Matcher matcher = pattern.matcher(source);

        //
        // Replaces every subsequence of the input sequence that matches the
        // pattern with the given replacement string
        //
        String output = matcher.replaceAll(replace);
        
        return output;
	}

	public static String generateCrc32(String str) {
		byte[] input = str.getBytes();
		CRC32 crc32 = new CRC32();
		crc32.update(input, 0, input.length);
		return String.valueOf(crc32.getValue());
	}
	
	public static String getCurrencySymbol(String isoCurrency) {
		if ("360".equals(isoCurrency)) {
			return "IDR ";
		} else if ("840".equals(isoCurrency)) {
			return "USD ";
		}
		return isoCurrency + " ";
	}
	
	public static String paddingRight(String str, int length) {
		StringBuilder sb = new StringBuilder(str);
		while (sb.length() < length) {
			sb.append(" ");
		}
		return sb.toString();
	}
	
	
	
	public static String masking(String text, int nMask){
		String[] stringArray = text.split("");
		String mask = "";
		for(int i=0; i<stringArray.length; i++){
			
			if(i<=nMask){
				if(i!=0){
					mask = mask.concat("*");
				}
			}else{
				mask = mask.concat(stringArray[i]);
			}
		}
		return mask;
	}
	
	
	public static String defaultMoneyFormat(String str) {
		  String convertedString = str;
		  String satu = "0" , dua = "0";
		  int length = 0;
		  for (int i = 0; i < str.length(); i++) {
		   if (str.charAt(i) == '.'){
			   satu = String.valueOf(str.charAt(i+1));
			   dua = String.valueOf(str.charAt(i+2));
		    break;
		   }
		   length++;
		  }
		  String decimal = (str.length() == length ?  satu+""+dua : str.substring(length + 1));
		  decimal = StringUtil.removeChars(decimal, ".");
		  
		  if (length > 0) {
		   String temp = "";
		   int i = length;
		   while (i > 0) {
		    if (i > 2) {
		     if (temp.equalsIgnoreCase(""))
		      temp = str.substring(i - 3, i);
		     else
		      temp = str.substring(i - 3, i) + "." + temp;
		    } else {
		     if (temp.equalsIgnoreCase(""))
		      temp = str.substring(0, i);
		     else
		      temp = str.substring(0, i) + "." + temp;
		     i = 0;
		    }
		    i = i - 3;
		   }
		   
		   decimal = (decimal.length() == 1 ? decimal + "0" : decimal.substring(0, 2));
		   convertedString = temp + "," + decimal;
		  }
		  return convertedString;
		 }
	
	public static String moneyFormat(String money){
		String moneyFormated = money;
		if (money.startsWith("-") ) {
			moneyFormated = money.replaceFirst("-", "");
			moneyFormated = defaultMoneyFormat(moneyFormated);
			moneyFormated = "-"+moneyFormated;
		}else{
			moneyFormated = defaultMoneyFormat(moneyFormated);
		}
		return moneyFormated;
	}
	
	private static String removeChars(String data, String chars){
		String returnString = "";
		for (int i = 0; i < data.length(); i++) {
			if(".".equals(String.valueOf(data.charAt(i)))){
			}else{
				returnString += String.valueOf(data.charAt(i));
			}
		}
		return returnString;
	}
	
	public static String numberFormat(String value){
		String returnString = "";
		for (int i = 0; i < value.length(); i++) {
			if (String.valueOf(value.charAt(i)).equals(",")) {
				break;
			}else if (String.valueOf(value.charAt(i)).equals(".")) {
				returnString += "";
			}else{
				returnString += String.valueOf(value.charAt(i));
			}
		}
		return returnString;
	}

	
	public static String sortJsonArray(String json, String keyName, int typeSort){
		final String KEY_NAME = keyName;
		final int TYPE_SORT = typeSort;
		String jsonSorting = "";
		try{
		    JSONArray jsonArr = new JSONArray(json);
		    JSONArray sortedJsonArray = new JSONArray();
	
		    List<JSONObject> jsonValues = new ArrayList<JSONObject>();
		    for (int i = 0; i < jsonArr.length(); i++) {
		        jsonValues.add(jsonArr.getJSONObject(i));
		    }
		    Collections.sort( jsonValues, new Comparator<JSONObject>() {
		        //You can change "Name" with "ID" if you want to sort by ID
	
		        @Override
		        public int compare(JSONObject a, JSONObject b) {
		            String valA = new String();
		            String valB = new String();
	
		            try {
		                valA = (String) a.get(KEY_NAME);
		                valB = (String) b.get(KEY_NAME);
		            } 
		            catch (JSONException e) {
		                //do something
		            }
		            
		            //1 is Ascending
		            //2 is Descending
		            if(TYPE_SORT==1){
		            	 return valA.compareTo(valB);
		            }else{
		            	 return -valA.compareTo(valB);
		            }
		           
		            //if you want to change the sort order, simply use the following:
		            //return -valA.compareTo(valB);
		        }
		    });
	
		    for (int i = 0; i < jsonArr.length(); i++) {
		        sortedJsonArray.put(jsonValues.get(i));
		    }
		    jsonSorting =  sortedJsonArray.toString();
		} catch (JSONException e) {
			if(Constant.SHOW_LOG){
			e.printStackTrace();
			}
		}
		return jsonSorting;
	}

}
