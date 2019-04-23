package com.lebupay.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;

import org.apache.http.client.ClientProtocolException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.lebupay.daoImpl.BaseDao;
import com.lebupay.model.EmailInvoicingModel;

import oracle.jdbc.OraclePreparedStatement;

@Component
public class SpiderEmailSender extends BaseDao {
	@Async
	public void sendEmail(EmailInvoicingModel emailInvoicingModel) {
		try {
			String[] retval = fetchTempConfig(emailInvoicingModel.getAction());

			String jsonReqName = retval[0];
			String jsonReqPath = retval[1];
			String templateID = retval[2];

			String jsonReqString = getFileString(jsonReqName, jsonReqPath);
			jsonReqString = jsonReqString.replaceAll("\\r\\n|\\r|\\n", "");

			jsonReqString = jsonReqString.replace("replace_header_here", Util.isNullOrEmpty(emailInvoicingModel.getHeader())?"":emailInvoicingModel.getHeader());
			jsonReqString = jsonReqString.replace("replace_amount_here", Util.isNullOrEmpty(emailInvoicingModel.getAmount())?"":emailInvoicingModel.getAmount());
			jsonReqString = jsonReqString.replace("replace_emailMessageBody_here", Util.isNullOrEmpty(emailInvoicingModel.getEmailMessageBody())?"":emailInvoicingModel.getEmailMessageBody());
			jsonReqString = jsonReqString.replace("replace_subject_here", Util.isNullOrEmpty(emailInvoicingModel.getSubject())?"":emailInvoicingModel.getSubject());
			jsonReqString = jsonReqString.replace("replace_resetLink_here", Util.isNullOrEmpty(emailInvoicingModel.getResetLink())?"":emailInvoicingModel.getResetLink());
			jsonReqString = jsonReqString.replace("replace_to_here", Util.isNullOrEmpty(emailInvoicingModel.getEmailId())?"":emailInvoicingModel.getEmailId());
			jsonReqString = jsonReqString.replace("replace_cc_here", Util.isNullOrEmpty(emailInvoicingModel.getCC())?"":emailInvoicingModel.getCC());
			jsonReqString = jsonReqString.replace("replace_bcc_here", Util.isNullOrEmpty(emailInvoicingModel.getBCC())?"":emailInvoicingModel.getBCC());
			jsonReqString = jsonReqString.replace("replace_firstName_here", Util.isNullOrEmpty(emailInvoicingModel.getFirstName())?"":emailInvoicingModel.getFirstName());
			jsonReqString = jsonReqString.replace("replace_lastName_here", Util.isNullOrEmpty(emailInvoicingModel.getLastName())?"":emailInvoicingModel.getLastName());
			jsonReqString = jsonReqString.replace("replace_paymentLink_here", Util.isNullOrEmpty(emailInvoicingModel.getPaymentLink())?"":emailInvoicingModel.getPaymentLink());
			jsonReqString = jsonReqString.replace("replace_description_here", Util.isNullOrEmpty(emailInvoicingModel.getDescription())?"":emailInvoicingModel.getDescription());
			jsonReqString = jsonReqString.replace("replace_otp_here", Util.isNullOrEmpty(emailInvoicingModel.getOTP())?"":emailInvoicingModel.getOTP());
			jsonReqString = jsonReqString.replace("replace_phoneNumber_here", Util.isNullOrEmpty(emailInvoicingModel.getMobileNumber())?"":emailInvoicingModel.getMobileNumber());
			jsonReqString = jsonReqString.replace("replace_email_here", Util.isNullOrEmpty(emailInvoicingModel.getEmailId())?"":emailInvoicingModel.getEmailId());
			jsonReqString = jsonReqString.replace("replace_invoiceNo_here", Util.isNullOrEmpty(emailInvoicingModel.getInvoiceNo())?"":emailInvoicingModel.getInvoiceNo());
			jsonReqString = jsonReqString.replace("replace_type_here", Util.isNullOrEmpty(emailInvoicingModel.getType())?"":emailInvoicingModel.getType());
			jsonReqString = jsonReqString.replace("replace_BDT_here", Util.isNullOrEmpty(emailInvoicingModel.getBDT())?"":emailInvoicingModel.getBDT());
			jsonReqString = jsonReqString.replace("replace_name_here", Util.isNullOrEmpty(emailInvoicingModel.getFirstName())?"":emailInvoicingModel.getFirstName());
			jsonReqString = jsonReqString.replace("replace_contactUsQuery_here", Util.isNullOrEmpty(emailInvoicingModel.getQuery())?"":emailInvoicingModel.getQuery());
			jsonReqString = jsonReqString.replace("replace_contactUsReply_here", Util.isNullOrEmpty(emailInvoicingModel.getReply())?"":emailInvoicingModel.getReply());
			jsonReqString = jsonReqString.replace("replace_templateID_here", templateID);

			post(jsonReqString, emailInvoicingModel.getIsTemplate());

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void post(String emailRequest, Boolean template) throws ClientProtocolException, IOException {
		// String emailRequest =
		// "{\"name\":\"Shaker\",\"uname\":\"Lebupay\",\"upass\":\"Lebus@1193\",\"templateID\":\"1\",\"from\":\"Lebupay
		// <info@lebupay.com>\",\"to\":\"shaker@spiderdxb.com\",\"cc\":\"\",\"bcc\":\"\",\"subject\":\"NiharekahS\"}";

		/*
		 * String payload = "{\"uname\":\""+uname+"\",\"upass\":\""+upass+
		 * "\",\"isUnicode\":\"true\",\"from\":\""+from+"\",\"to\":\""+to+"\"," +
		 * "\"cc\":\""+cc+"\",\"bcc\":\""+bcc+"\",\"subject\":\""+subject+"\"," +
		 * "	\"mailBody\":\""+htmlStr+"\"}"; /
		 **/

		String templateUrl = "http://localhost:8080/EmailTemplateGenerator/EmailServlet?action=generateTemplate";
		String emailUrl = "http://localhost:8080/SpiderEmailServices/EmailServlets?action=sendEmail";
		String baseUrl;

		String uname = "Lebupay";
		String upass = "Lebus@1193";

		emailRequest = emailRequest.replace("replace_uname_here", uname);
		emailRequest = emailRequest.replace("replace_upass_here", upass);

		// System.out.println("emailRequest : "+emailRequest);

		if (template) {
			baseUrl = templateUrl;
		} else {
			baseUrl = emailUrl;
		}

		URL url = new URL(baseUrl);
		HttpURLConnection conn = null;

		try {
			conn = (HttpURLConnection) url.openConnection();
			try {
				conn.setRequestMethod("POST");
				conn.setDoOutput(true);
				conn.setDoInput(true);
				conn.setUseCaches(false);
				conn.setAllowUserInteraction(false);
				// conn.setRequestProperty("Content-Type","text/xml");
				conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
				conn.setRequestProperty("Accept", "application/json");
			} catch (ProtocolException e) {
				e.printStackTrace();
			}

			OutputStream out = conn.getOutputStream();
			try {
				OutputStreamWriter wr = new OutputStreamWriter(out);
				wr.write(emailRequest);
				wr.flush();
				wr.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (out != null)
					out.close();
			}
			String response = "", responseSingle = "";

			InputStream in = conn.getInputStream();
			try {
				BufferedReader rd = new BufferedReader(new InputStreamReader(in));
				while ((responseSingle = rd.readLine()) != null) {
					response = response + responseSingle;
				}
				rd.close();
				System.out.println("Server response : " + response);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (in != null)
					in.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.disconnect();
		}
	}

	public String getFileString(String filename, String path) throws IOException {
		File fl = new File(path + "/" + filename);

		String targetFileStr = new String(Files.readAllBytes(Paths.get(fl.getAbsolutePath())));
		return targetFileStr;
	}

	public String[] fetchTempConfig(String action) throws Exception {
		String[] retval;
		String jsonReqName = "";
		String jsonReqPath = "";
		String templateID = "";

		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;

		try {
			String sql = "select c.templateID," // 1
					+ "t.req_file_name," // 2
					+ "t.req_file_location " // 3
					+ "from template_configuration c left outer join template_table t on c.templateID = t.ID "
					+ "where c.action=:ACTION";

			System.out.println("template congfig fetching ==>> " + sql);

			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setStringAtName("ACTION", action); // mobileNo
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				jsonReqName = rs.getString("req_file_name");
				jsonReqPath = rs.getString("req_file_location");
				templateID = rs.getString("templateID");
			}
		} finally {
			try {

				if (pst != null)
					if (!pst.isClosed())
						pst.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			try {

				if (connection != null)
					if (!connection.isClosed())
						connection.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		retval = new String[] { jsonReqName, jsonReqPath, templateID };
		return retval;
	}
}
