/**
 * @formatter:off
 *
 */
package com.lebupay.common;

import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import com.lebupay.exception.EmptyValueException;
import com.lebupay.exception.FormExceptions;

/*
import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import com.google.gson.stream.JsonReader;
import com.lebupay.exception.EmptyValueException;
import com.lebupay.exception.FormExceptions;
/**/
/**
 * This Class contains all the Utility method that are used multiple times.
 * @author Java Team
 */
@Component
public class Util {

	@Autowired
	private static MessageUtil messageUtil;

	private static Logger logger = Logger.getLogger(Util.class);

	private static Pattern pattern1;
	private static Matcher matcher1;
	private static JsonObject jsonObject;
	private static final String IPADDRESS_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

	/**
	 * Validate ip address with regular expression
	 * @param ip address for validation
	 * @return true valid ip address, false invalid ip address
	 */
	public static boolean validateIP(String ip) {

		pattern1 = Pattern.compile(IPADDRESS_PATTERN);
		matcher1 = pattern1.matcher(ip);
		return matcher1.matches();
	}

	/**
	 * This method is to validate data as Nummber.
	 * @param data
	 * @return boolean
	 */
	public static boolean isNumber(String data) {

		String regex = "\\d+";
		String value = "";
		String[] result = data.split("\\.");
		if (result[1].length() == 1) {
			if (result[1].contains("0")) {
				value = result[0];
			} else {
				value = data;
			}
		}
		return !(value.matches(regex));
	}

	private static final String PHONE_NUMBER_GARBAGE_REGEX = "[()\\s-]+";
	private static final String PHONE_NUMBER_REGEX = "^((\\+[1-9]?[0-9])|0)?[7-9][0-9]{9}$";
	private static final Pattern PHONE_NUMBER_PATTERN = Pattern
			.compile(PHONE_NUMBER_REGEX);

	/**
	 * This method is used to validate Phone Number
	 * @param phoneNumber
	 * @return boolean
	 */
	public static boolean validatePhoneNumber(String phoneNumber) {
		return phoneNumber != null
				&& PHONE_NUMBER_PATTERN.matcher(
						phoneNumber.replaceAll(PHONE_NUMBER_GARBAGE_REGEX, ""))
						.matches();
	}
	/**
	 * Validate start and end date
	 * @param firstDate
	 * @param endDate
	 * @return int
	 */
	public static int getDayDiff(String firstDate, String endDate) {

		try {

			DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
			Date date1 = format.parse(firstDate);
			Date date2 = format.parse(endDate);
			Double diffInMillies = (date2.getTime() - date1.getTime()) / (1000.0 * 60 * 60 * 24);
			return diffInMillies.intValue();

		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * This method is used to Check Special Character
	 * @param value
	 * @return boolean
	 */
	public static boolean isSpecialChar(String value) {
		Pattern p = Pattern.compile("[a-zA-Z0-9 ]", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(value);
		return m.find();
	}
	
	/**
	 * This method is used to Save Http Request Data in to log file.
	 * @param request
	 */
	public static void saveHttpRequestDataInLogFile(HttpServletRequest request) {

		if (logger.isDebugEnabled()) {

			logger.debug("saveHttpRequestDataInLogFile ::  " + request);

		}

		try {
			Calendar c = Calendar.getInstance();

			long milliseconds = c.getTimeInMillis();

			String remoteAddress = null;
			String remoteHost = null;
			int remotePort = -1;
			String remoteUser = null;
			String requestUrl = null;

			try {

				remoteAddress = request.getRemoteAddr();
				remoteHost = request.getRemoteHost();
				remotePort = request.getRemotePort();
				remoteUser = request.getRemoteUser();
				requestUrl = request.getRequestURL().toString();

				if (logger.isDebugEnabled()) {

					logger.debug("Request Data Follows -------> ");
					logger.debug("Time Of API Hit :: " + milliseconds);
					logger.debug("Remote Address :: " + remoteAddress);
					logger.debug("Remote Host :: " + remoteHost);
					logger.debug("Remote Port :: " + remotePort);
					logger.debug("Remote User :: " + remoteUser);
					logger.debug("Request URL ::  " + requestUrl);

				}

			} catch (Exception e) {

				if (logger.isDebugEnabled()) {

					logger.debug("Exception In Receiving Request Data, Exception Message is :: "
							+ e.getMessage());

				}

			}

		} catch (Exception e) {

			if (logger.isDebugEnabled()) {

				logger.debug("Exception In Receiving Request Data, Exception Message is :: "
						+ e.getMessage());

			}
		}

	}

	/**
	 * This method is used to check Empty of String.
	 * @param value
	 * @return boolean
	 */
	public static boolean isEmpty(String value) {
		return value == null || value.isEmpty();
	}

	/**
	 * This method is used to check Empty of Date.
	 * @param date
	 * @return boolean
	 */
	public static boolean isEmpty(Date date) {
		return date == null;
	}

	/**
	 * This method is used to Empty Check of Object.
	 * @param object
	 * @return boolean
	 */
	public static boolean isEmpty(Object object) {
		return object == null;
	}

	/**
	 * This method is used to Check Empty String With different Method.
	 * @param value
	 * @return boolean
	 */
	public static boolean stringIsEmpty(String value) {

		return value.equalsIgnoreCase("") || value == null;
	}

	/**
	 * This method is used to check String as number.
	 * @param value
	 * @return boolean
	 */
	public static boolean isNumeric(String value) {
		Pattern pattern = Pattern.compile("\\d+");

		Matcher matcher = pattern.matcher(value);
		return matcher.matches();
	}

	/**
	 * This method is used to Format Date.
	 * @param value
	 * @return String
	 */
	public static String formatDateFromEntity(Date value) {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

		String formatDate = simpleDateFormat.format(value);

		return formatDate;

	}

	/**
	 * This method is used to Cut String
	 * @author Supratim Sarkar
	 * @param data
	 * @return String
	 */
	public static String stringcut(String data) {
		return data.replace(" ", "-").trim().toLowerCase();
	}

	/**
	 * this method get the Input String and convert the string to UTF-8 compatible
	 * @param string
	 * @return String
	 * @throws UnsupportedEncodingException
	 */
	public static String utfEightConverter(String string) {

		try {
			byte[] array = string.getBytes("ISO-8859-1");
			String s = new String(array, Charset.forName("UTF-8"));
			string = StringEscapeUtils.unescapeJava(s);

		} catch (Exception e) {
			logger.error(e);
		}

		return string;
	}

	/**
	 * This method is used to validate the email
	 * @param email
	 * @return boolean
	 */
	public static boolean emailValidator(String email) {

		String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
		java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
		java.util.regex.Matcher m = p.matcher(email);

		return !m.matches();
	}
	
	//	added by Shaker on 23.04.2019
	/**
	 * This method is used to check null or empty
	 * @param String
	 * @return boolean
	 */
	public static boolean isNullOrEmpty(String s) {
		if(s==null) return true;
		else if(s.isEmpty()) return true;
		else return false;
	}
	
	//	added by Shaker on 23.04.2019
	/**
	 * This method is used to check null or empty
	 * @param Integer
	 * @return boolean
	 */
	public static boolean isNullOrEmpty(Integer s) {
		if(s==null) return true;
		else return false;
	}

	/**
	 * This method is used to get Date from Current time Stamp.
	 * @param timestamp
	 * @param dateFormate
	 * @return Date
	 * @throws ParseException
	 */
	public static Date getDatefromTimestamp(String timestamp, String dateFormate)
			throws ParseException {
		long time = Long.valueOf(String.valueOf(timestamp));
		Date date = new Date(time);
		Format format = new SimpleDateFormat(dateFormate);
		format.format(date);
		SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormate);
		return dateFormat.parse(format.format(date));
	}

	/**
	 * This method is used to Construct App url.
	 * @param request
	 * @return String
	 */
	public static String constructAppUrl(HttpServletRequest request) {
		return request.getScheme() + "://" + request.getServerName() + ":"
				+ request.getServerPort() + request.getContextPath();

	}

	/**
	 * This method is used to get Current Day.
	 * @return String
	 */
	public static String currentDay() {
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		String day = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date
				.getTime());
		return day.toUpperCase();
	}

	/**
	 * This method is used to Get Yesterday Date as String.
	 * @return String
	 */
	public static String getYesterdayDateString() {

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);

		return dateFormat.format(cal.getTime());
	}

	/**
	 * This method is used to get Current Month and Year
	 * @return String
	 */
	public static String getCurrentMonthAndYear() {

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
		Calendar cal = Calendar.getInstance();
		// cal.add(Calendar.DATE, -1);

		return dateFormat.format(cal.getTime());
	}

	/**
	 * This method is used to Get before yesterday Date String.
	 * @return String
	 */
	public static String getBeforeYesterdayDateString() {

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -2);

		return dateFormat.format(cal.getTime());
	}

	/**
	 * This method is used to get Current Date time.
	 * @return String
	 */
	public static String getCurrentDateTime() {

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();

		String dateInString = dateFormat.format(cal.getTime());

		return dateInString;
	}
	
	/**
	 * This method is used to get Random 28.
	 * @return String
	 */
	public static String getRandom28() {

		char[] chars = "4b195529515dbcfa525f3d3261648b4c07ef6aa6ca7f548973758a6e25bb9bf17d43104245199d6a4b5e044c4f5f89401de337e16b77a2ac72e8e8a9150cbc2e"
				.toCharArray();

		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < 28; i++) {
			char c = chars[random.nextInt(chars.length)];
			sb.append(c);
		}
		String output = sb.toString();

		return output;
	}

	/**
	 * This method is used to decrypt password.
	 * @param hashPass
	 * @return String
	 */
	public static String decPass(String hashPass) {

		String salt = Util.getRandomSalt();
		int saltLengthHalf = (salt.length()) / 2;
		String md5 = hashPass.substring(saltLengthHalf, hashPass.length());
		md5 = md5.substring(0, md5.length() - saltLengthHalf);

		return md5;
	}

	/**
	 * This method is used to get Random Salt.
	 * @return String
	 */
	public static String getRandomSalt() {

		char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?"
				.toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < 28; i++) {
			char c = chars[random.nextInt(chars.length)];
			sb.append(c);
		}

		String output = sb.toString();

		return output;

	}

	/**
	 * This method is used to Strip Html Tags.
	 * @param text
	 * @param key
	 * @return String
	 */
	public static String strip_html_tags(String text, String key) {

		text = text.replaceAll("@(.*?)<style[^>]*?>.*?</style>@siu", " ");
		text = text.replaceAll("@(.*?)<script[^>]*?.*?</script>@siu", " ");
		text = text.replaceAll("@(.*?)<iframe[^>]*?.*?</iframe>@siu", " ");
		text = text.replaceAll("@(.*?)<html[^>]*?.*?</html>@siu", " ");
		String allowed_tags = null;
		int descflg = 0;
		if (key == "firstName" || key == "middleName" || key == "lastName"
				|| key == "menuId" || key == "locationId"
				|| key == "staticBlock" || key == "node" || key == "Node"
				|| key == "locationNameEnglish" || key == "locationNameHindi"
				|| key == "name" || key == "url" || key == "menuTitle"
				|| key == "Status") {
			allowed_tags = "<a><br><b><h1><h2><h3><h4><h5><h6>";
			allowed_tags
					.concat("<table><tr><th><td><img><li><ol><p><strong><table><tr><td><th><u><ul><thead>");
			allowed_tags
					.concat("<tbody><tfoot><em><dd><dt><dl><span><div><del><add><i><hr>");
			allowed_tags
					.concat("<pre><br><blockquote><address><code><caption><abbr><acronym>");
			allowed_tags
					.concat("<cite><dfn><q><ins><sup><sub><kbd><samp><var><tt><small><big>");
			descflg = 1;
		} else {
			allowed_tags = "<br><b>";
		}

		if (isUTF8MisInterpreted(text) == true) {
			text = Jsoup.clean(
					text,
					Whitelist
							.basic()
							.addTags("img")
							.addTags("table")
							.addTags("span")
							.addTags("cite")
							.addTags("dfn")
							.addTags("acronym")
							.addTags("sup")
							.addTags("samp")
							.addTags("strong")
							.addTags("caption")
							.addTags("blockquote")
							.addTags("tfoot")
							.addTags("tbody")
							.addTags("div")
							.addTags("td")
							.addTags("tr")
							.addTags("p")
							.addAttributes("table", "border", "class", "style")
							.addAttributes("span", "style", "strong")
							.addTags("br")
							.addAttributes("img", "alt", "style", "height",
									"width", "src")
							.addAttributes("th", "colspan", "align", "*")
							.addAttributes("thead", "align"));

		}

		text = text.replaceAll("/alert/i", "");
		text = text.replaceAll("/onerror=/i", "");
		text = text.replaceAll("/onmouseover=/i", "");
		text = text.replaceAll("/onmouseover/i", "");
		text = text.replaceAll("/onmouseout=/i", "");
		text = text.replaceAll("/onmouseout/i", "");
		text = text.replaceAll("/onkeydown/i", "");
		text = text.replaceAll("/onkeydown=/i", "");
		text = text.replaceAll("/onkeypress=/i", "");
		text = text.replaceAll("/onkeypress/i", "");
		text = text.replaceAll("/onkeyup=/i", "");
		text = text.replaceAll("/onkeyup/i", "");
		text = text.replaceAll("/onclick=/i", "");
		text = text.replaceAll("/onclick/i", "");
		text = text.replaceAll("/onload=/i", "");
		text = text.replaceAll("/onload/i", "");
		text = text.replaceAll("/ondblclick/i", "");
		text = text.replaceAll("/ondblclick=/i", "");
		text = text.replaceAll("/ondrag=/i", "");
		text = text.replaceAll("/ondrag/i", "");
		text = text.replaceAll("/ondragend=/i", "");
		text = text.replaceAll("/ondragend/i", "");
		text = text.replaceAll("/ondragenter=/i", "");
		text = text.replaceAll("/ondragenter/i", "");
		text = text.replaceAll("/ondragleave/i", "");
		text = text.replaceAll("/ondragleave=/i", "");
		text = text.replaceAll("/ondragover=/i", "");
		text = text.replaceAll("/ondragover/i", "");
		text = text.replaceAll("/ondragstart=/i", "");
		text = text.replaceAll("/ondragstart/i", "");
		text = text.replaceAll("/ondrop=/i", "");
		text = text.replaceAll("/ondrop/i", "");
		text = text.replaceAll("/onmousedown/i", "");
		text = text.replaceAll("/onmousedown=/i", "");
		text = text.replaceAll("/onmousemove=/i", "");
		text = text.replaceAll("/onmousemove/i", "");
		text = text.replaceAll("/onmouseup=/i", "");
		text = text.replaceAll("/onmouseup/i", "");
		text = text.replaceAll("/onmousewheel=/i", "");
		text = text.replaceAll("/onmousewheel/i", "");
		text = text.replaceAll("/onscroll=/i", "");
		text = text.replaceAll("/onscroll/i", "");
		text = text.replaceAll("/document.cookie/i", "");
		text = text.replaceAll("/prompt/i", "");
		text = text.replaceAll("/onselect/i", "");
		text = text.replaceAll("/type=/i", "");
		text = text.replaceAll("/document.domain/i", "");
		text = text.replaceAll("/confirm(domain)/i", "");
		if (descflg == 1) {
			return text;
		} else {
			return escapeHtml(text);
		}
	}

	/**
	 * This method is used to check UTF -8 .
	 * @param input
	 * @return boolean
	 */
	public static boolean isUTF8MisInterpreted(String input) {
		// convenience overload for the most common UTF-8 misinterpretation
		// which is also the case in your question
		return isUTF8MisInterpreted(input, "Windows-1252");
	}

	/**
	 * This method is used to validate Is UTF-8.
	 * @param input
	 * @param encoding
	 * @return boolean
	 */
	public static boolean isUTF8MisInterpreted(String input, String encoding) {

		CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
		CharsetEncoder encoder = Charset.forName(encoding).newEncoder();
		ByteBuffer tmp;
		try {
			tmp = encoder.encode(CharBuffer.wrap(input));
		}

		catch (CharacterCodingException e) {
			return false;
		}

		try {
			decoder.decode(tmp);
			return true;
		} catch (CharacterCodingException e) {
			return false;
		}
	}

	/**
	 * 
	 * @param plainText  Text input to be encrypted
	 * @return Returns encrypted text
	 * 
	 */
	public String encrypt(String plainText) throws NoSuchAlgorithmException,
			InvalidKeySpecException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException,
			UnsupportedEncodingException, IllegalBlockSizeException,
			BadPaddingException {
		// Key generation for enc and desc
		String secretKey = "ezeon8547";
		KeySpec keySpec = new PBEKeySpec(secretKey.toCharArray(), salt,
				iterationCount);
		SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES")
				.generateSecret(keySpec);
		// Prepare the parameter to the ciphers
		AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt,
				iterationCount);

		// Enc process
		ecipher = Cipher.getInstance(key.getAlgorithm());
		ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
		String charSet = "UTF-8";
		byte[] in = plainText.getBytes(charSet);
		byte[] out = ecipher.doFinal(in);
		String encStr = new sun.misc.BASE64Encoder().encode(out);

		return encStr;
	}

	/**
	 * This method is used to rename file name.
	 * @param fileName
	 * @return
	 */
	public static String renameFileName(String fileName) {
		String imageExtention = fileName
				.substring(fileName.lastIndexOf(".") + 1);

		fileName = fileName.substring(0, fileName.lastIndexOf("."));

		fileName = fileName.replaceAll("[^\\w]", "") + "_"
				+ System.currentTimeMillis() + "." + imageExtention;
		return fileName;
	}

	/**
	 * This method is used to generate upload path.
	 * @param request
	 * @param folderName
	 * @return
	 */
	public static String generateUploadPath(HttpServletRequest request,
			String folderName) {
		if (logger.isInfoEnabled()) {
			logger.info("generateImageUploadPath-Start");
		}

		String rootPath = System.getProperty("catalina.home");
		rootPath = rootPath + File.separator + "webapps";

		// Creating the directory to store file
		File dir = new File(rootPath + File.separator + folderName);

		if (!dir.exists())
			dir.mkdirs();

		String uploadImagePath = dir + File.separator;

		if (logger.isInfoEnabled()) {
			logger.info("generateImageUploadPath-End");
		}
		return uploadImagePath;
	}

	/**
	 * This method is used to generate file upload path using Servlet Context.
	 * @param context
	 * @param folderName
	 * @return String
	 */
	public static String generateFileUploadPath(ServletContext context,
			String folderName) {

		if (logger.isInfoEnabled()) {
			logger.info("generateImageUploadPath-Start");
		}

		// get absolute path of the application
		String appPath = context.getRealPath("");
		appPath = appPath + "resources";
		File dir = new File(appPath + File.separator + folderName);

		if (!dir.exists())
			dir.mkdirs();

		String uploadImagePath = dir + File.separator;

		if (logger.isInfoEnabled()) {
			logger.info("generateImageUploadPath-End");
		}
		return uploadImagePath;
	}

	/**
	 * This method is used to resize Image.
	 * @param originalImage
	 * @param type
	 * @param IMG_WIDTH
	 * @param IMG_HEIGHT
	 * @return BufferedImage
	 */
	public static BufferedImage resizeImage(BufferedImage originalImage,
			int type, int IMG_WIDTH, int IMG_HEIGHT) {

		BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT,
				type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
		g.dispose();

		return resizedImage;
	}
	
	private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";

	private static Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

	/**
	 * This method is used to validate password.
	 * @param password
	 * @return boolean
	 */
	public static boolean validatePassword(final String password) {

		Matcher matcher = pattern.matcher(password);
		return matcher.matches();
	}

	Cipher ecipher;
	Cipher dcipher;
	// 8-byte Salt
	byte[] salt = { (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32,
			(byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03 };
	// Iteration count
	int iterationCount = 19;

	/**
	 * This method is used to get Current Financial Year. 
	 * @return String
	 */
	public static String getFinancialYear() {
		int year = Calendar.getInstance().get(Calendar.YEAR);
		String finYear = "";
		int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
		if (month <= 3) {
			finYear = (year - 1) + "-" + year;
		} else {
			finYear = year + "-" + (year + 1);
		}

		return finYear;
	}

	/**
	 * This method is used to add Date.
	 * @param time
	 * @return String
	 */
	public static String addDate(String time) {

		String output = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar c = Calendar.getInstance();
			c.setTime(new Date()); // Now use today date.
			c.add(Calendar.DATE, Integer.parseInt(time)); // Adding days
			output = sdf.format(c.getTime());
		} catch (Exception e) {

		}

		return output;

	}

	/**
	 * This method is used to add Hour to date.
	 * @param time
	 * @return String
	 */
	public static String addHour(String time) {

		String output = "";
		try {

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar c = Calendar.getInstance();
			c.setTime(new Date()); // Now use today date.
			c.add(Calendar.HOUR_OF_DAY, Integer.parseInt(time)); // Adding hour
			output = sdf.format(c.getTime());
		} catch (Exception e) {

		}

		return output;

	}

	/**
	 * This method is used to Strip HTML tags from String Input.
	 * @param text
	 * @param key
	 * @param exceptions
	 * @return String
	 * @throws FormExceptions
	 */
	public static String strip_html_tags(String text, String key,
			Map<String, Exception> exceptions) throws FormExceptions {

		key = strip_html_tags(text, key);
		if (Util.isEmpty(key)) {
			exceptions.put(
					"strip.empty",
					new EmptyValueException(messageUtil
							.getBundle("strip.empty")));
		}

		if (exceptions.size() > 0)
			throw new FormExceptions(exceptions);

		return key;
	}

	/**
	 * This method is used to Check Password Policy.
	 * @param password
	 * @return boolean
	 */
	public static boolean checkPasswordPolicy(String password) {

		String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,20}";
		return password.matches(pattern);
	}

	/**
	 * This method is used to Check String for Alpha.
	 * @param str
	 * @return boolean
	 */
	public static boolean checkAlpha(String str) {

		String regex = "^[0-9]\\d*(\\.\\d+)?$";
		return str.matches(regex);
	}
	
	/**
	 * This method is used to Check Decimal Digit.
	 * @param str
	 * @return boolean
	 */
	public static boolean checkDecialDigit(String str) {

		String regex = "^[0-9]\\d*(\\.\\d{1,2}+)?$";
		return str.matches(regex);
	}
	
	/**
	 * This method is used to format double value to two digit after decimal.
	 * @param inputValue
	 * @return Double
	 */
	public static Double twoDecimalFormatString(Double inputValue) {
		// 456455.9396 = 456455.94 Output
		Double multiplicationValue = inputValue * 100.0;
		Double twoDecimalValue = Math.floor(multiplicationValue) / 100.0;
		return twoDecimalValue;

	}
	
	/**
	 * This method is used to convert Document to String.
	 * @param doc
	 * @return String
	 */
	public static String convertDocumentToString(Document doc) {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = tf.newTransformer();
            // below code to remove XML declaration
            // transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            String output = writer.getBuffer().toString();
            return output;
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        
        return null;
    }

	/**
	 * This method is used to Check Mobile no.
	 * @param mobileNumber
	 * @return boolean
	 */ 
  public static boolean checkMobileNumber(String mobileNumber) {
	   
	   String mobileNumberPattern = "((?=.*\\d)(?=.*[0-9])(?=.*[+]).{13,14})";
	   Pattern pattern = Pattern.compile(mobileNumberPattern);
	   Matcher matcher = pattern.matcher(mobileNumber);
	   return matcher.matches();
   }
  
  /**
  * @return the jsonObject
  */
	  static JsonObject getJsonObject(String jsonString) {
	  String errorCode="-1";
	  String errorMessage;
	  String inputJSONString=jsonString;
	  InputStream is;
	  Map<String,JsonValue> jsonObjectMap;
	  jsonObject=null;
	  jsonObjectMap=null;
	  try {
	  if(!jsonString.isEmpty()) {
	  is = new ByteArrayInputStream(jsonString.getBytes("UTF-8")); //jsonString.getBytes("UTF-8")
	  JsonReader jsonReader = Json.createReader(is);
	  jsonObject = jsonReader.readObject();
	  jsonObjectMap=jsonObject;
	  errorCode="0";
	  jsonReader.close();
	  is.close();
	  }else {
	  errorCode="1"; errorMessage="Empty string supplied.";
	  }
	  } catch (UnsupportedEncodingException e) {
	  errorCode="2"; errorMessage=e.getMessage();
	  } catch (IOException e) {
	  errorCode="3"; errorMessage=e.getMessage();
	  } catch (Exception e) {
	  errorCode="4"; errorMessage=e.getMessage();
	  }
	  return jsonObject;
	  }

  
  

}
