package com.dev.kaizen.util;

public final class Constant {
	private Constant() {
		// TODO Auto-generated constructor stub
	}

	public static final String JSON_TIMEOUT = "{\"errorCode\":\"IB-0000\", \"fullMessage\":\"Gangguan koneksi. Mohon cek koneksi internet anda.\"}";

	/**
	 * Date Format
	 */
    public static final String DENOM_FORMAT = "##0.#####";
	public static final String DATE_FORMAT = "dd MM yyyy HH:mm:ss";
	public static final String DATE_FORMAT_MUTASI = "dd-MMM-yyyy";
	public static final String DATE_FORMAT_MUTASI_DETAIL = "dd-MM-yyyy, HH:mm:ss";
	public static final String PRINTED_DATE_FORMAT = "dd-MMM-yyyy HH:mm:ss";
	public static final String DATE_TIME_FORMAT = "dd-MMM-yyyy HH:mm";

    public static final int FONT_PLAIN = 1;
    public static final int FONT_SEMIBOLD = 2;
	public static final int FONT_BOLD = 3;

	//dev
//	public static final String BASE_URL = "http://156.67.221.248/kaizen/api/";
//	public static final String BASE_PICT = "http://156.67.221.248/kaizen/";

//	public static final String BASE_URL = "http://192.168.56.1:8080/api/";
//	public static final String BASE_PICT = "http://192.168.56.1:8080/";

	//prod
	public static final String BASE_URL = "http://kgts-indonesia.com/kaizen/api/";
	public static final String BASE_PICT = "http://kgts-indonesia.com/kaizen/";

  	public static final Boolean SHOW_LOG = true;

	public static final String ERROR_COD_SESSION_EXPIRED = "IB-1010";
	public static final String ERROR_INVALID_SESSION = "IB-1001";
	public static final String ERROR_INVALID_AUTHENTICATION = "IB-1000";

	public static final String TRUE = "1";

	public static final int TIMEOUT_CONN = 180000;

	public static final int REST_GET = 0;
	public static final int REST_PUT = 1;
	public static final int REST_POST = 2;
	public static final int REST_DELETE = 3;
}
