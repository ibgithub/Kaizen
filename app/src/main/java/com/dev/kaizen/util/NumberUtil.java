/**
 * 
 */
package com.dev.kaizen.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @author Anggoro Biandono
 * 6:05:38 PM
 */
public final class NumberUtil {
	private NumberUtil() {
		
	}
	
	public static String toCurr(BigDecimal number){
		//System.out.println("Number : "+number);
		NumberFormat formatter = new DecimalFormat("#,##0.00;-#,##0.00");
		
		return formatter.format(number);
	}
	
	
	public static String toCurrWithoutDecimal(BigDecimal number){
		NumberFormat formatter = new DecimalFormat("#,##0;-#,##0");
		
		return formatter.format(number);
	}
	
	public static int dpToInteger(int padding_in_dp, final float scale){	    
	    int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
	    
	    return padding_in_px;
	}
	
	public static String toCurrDigitGrouping(String number){
		BigDecimal dec = new BigDecimal(number);
		NumberFormat formatter = new DecimalFormat("#,###.##");
		return "Rp " + formatter.format(dec);
	}
}
