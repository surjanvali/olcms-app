package in.apcfss.struts.reports;

import in.apcfss.struts.Forms.CommonForm;
import in.apcfss.struts.commons.AjaxModels;
import in.apcfss.struts.commons.CommonModels;
import in.apcfss.struts.commons.SendSMSAction;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import plugins.DatabasePlugin;

public class AdvocateRegistationAction extends DispatchAction {

	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonForm cform = (CommonForm) form;
		try {
			request.setAttribute("HEADING", "GP Registation ");
		} catch (Exception e) {
			request.setAttribute("errorMsg", "Exception occurred : No Records found to display");
			e.printStackTrace();
		} finally {
		}
		return mapping.findForward("success");
	}

	public ActionForward getOtpData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println(
				"GPOAcknowledgementAction..............................................................................getOtp()");
		Connection con = null;
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		String sql = null;
		String ackDate = "";
		String userId = null, roleId = null, deptId = null, distId = null;
		response.setContentType("text/html");
		PrintWriter out = null;
		try {

			con = DatabasePlugin.connect();
			out = response.getWriter();
			int b = 0;
			String mobileNo=request.getParameter("mobileNo");
			if (!mobileNo.equals(null)) {

				int Random = (int) (Math.random() * 100);

				for (int i = 1; i <= 10; i++) {
					Random = (int) (Math.random() * 100);
					// System.out.println(Random);
				}
				String randomValue1 = Integer.toString((Random + Random) * (Random + Random) + Random + 3456);
				System.out.println("randomValue1--" + randomValue1);
				String randomValue = randomValue1.substring(0, 4);

				System.out.println("randomValue:" + randomValue);

				sql = "insert into generate_otp_gp (mobile_no, otp) values ('" + mobileNo + "','"
						+ randomValue + "')";
				System.out.println("INSERT ROLE SQL:" + sql);
				b += DatabasePlugin.executeUpdate(sql, con);

				System.out.println("MOBILE b:" + b);
				if (b > 0) {
					//request.setAttribute("otpSetNo", "otpSetNo");

					String smsText="Your User Id is "+randomValue+" and Password is olcms@2021 to Login to https://apolcms.ap.gov.in/ Portal. Please do not share with anyone. \r\n-APOLCMS";
					// String smsText = "Your User otp is " + randomValue + " Please do not share with anyone. \r\n-APOLCMS";
					String templateId = "1007784197678878760";

					System.out.println(mobileNo + "--" + smsText + "--" + templateId);
					if (mobileNo != null && !mobileNo.equals("")) {
						mobileNo = "8500909816";
						System.out.println("mobileNo::" + mobileNo);
						SendSMSAction.sendSMS(mobileNo, smsText, templateId, con);
					}
					con.commit();
					// request.setAttribute("successMsg", "Your User otp is " + randomValue + " sent to Registered Mobile No. \r\n-APOLCMS");
					out.println("1");
				} else {
					con.rollback();
					// request.setAttribute("errorMsg", "Error in otp creation. Kindly try again.");
					out.println("0");
				}
			} else {
				out.println("0");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabasePlugin.close(con, ps, null);
			if (out != null) {
				out.close();
			}
		}
		return null;
	}

	public ActionForward submitOtp(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println(
				"GPOAcknowledgementAction..............................................................................submitOtp()");
		Connection con = null;
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		String sql = null;
		String target = "success";
		String userId = null, roleId = null, deptId = null;
		try {
			con = DatabasePlugin.connect();
			int b = 0;
			sql = "select distinct otp from generate_otp_gp where otp='" + cform.getDynaForm("otpNo")
					+ "' and mobile_no is not null";
			System.out.println("MOBILE SQL:" + sql);
			String otp = DatabasePlugin.getStringfromQuery(sql, con);

			System.out.println("otp--" + otp);
			if (otp.equals(cform.getDynaForm("otpNo"))) {
				String mobileNo = cform.getDynaForm("mobileNo").toString();
				String newRoleId = "15";
				String ip = InetAddress.getLocalHost().toString();
				int i = 1;
				sql = "INSERT INTO apolcms.advocate_cc_details_mst(cc_no, name, email, mobile_no, submit_time, ip_addrs,otp) " // ,designation,mandalid,villageid
						+ "    VALUES(?, ?, ?, ?, now(), ?,?) ";// ,?,?,? , ?, ?
				System.out.println("sql--" + sql);
				ps = con.prepareStatement(sql);
				ps.setString(i,
						cform.getDynaForm("advocateCCno") != null ? cform.getDynaForm("advocateCCno").toString() : "");
				ps.setString(++i,
						cform.getDynaForm("advocateName") != null ? cform.getDynaForm("advocateName").toString() : "");
				ps.setString(++i,
						cform.getDynaForm("advocateEmail") != null ? cform.getDynaForm("advocateEmail").toString()
								: "");
				ps.setString(++i,
						cform.getDynaForm("mobileNo") != null ? cform.getDynaForm("mobileNo").toString() : "");
				ps.setString(++i, ip != null ? ip : "");
				ps.setString(++i, cform.getDynaForm("otpNo") != null ? cform.getDynaForm("otpNo").toString() : "");

				b += ps.executeUpdate();

				System.out.println("SQL bb:" + b);

				if (b == 1) {

					sql = "insert into users (userid, password, user_description, created_by, created_on, created_ip, user_type) "
							+ " values ('" + cform.getDynaForm("advocateCCno") + "', md5('olcms@2021'),'"
							+ cform.getDynaForm("advocateName") + "', '" + cform.getDynaForm("advocateEmail")
							+ "', now(),'" + request.getRemoteAddr() + "'::inet," + newRoleId + " ) ";

					System.out.println("sql user :" + sql);

					b += DatabasePlugin.executeUpdate(sql, con);

					sql = "insert into user_roles (userid, role_id) values ('" + cform.getDynaForm("advocateCCno")
							+ "','" + newRoleId + "')";
					System.out.println("INSERT ROLE SQL:" + sql);
					b += DatabasePlugin.executeUpdate(sql, con);
					System.out.println("in bb:" + b);
					if (b == 3) {
					String smsText = "Your User Id is " + cform.getDynaForm("advocateCCno")+ " and Password is olcms@2021 to Login to https://apolcms.ap.gov.in/ Portal. Please do not share with anyone. \r\n-APOLCMS";
					String templateId = "1007784197678878760";

					System.out.println(mobileNo + "" + smsText + "" + templateId);
					
					  if (mobileNo != null && !mobileNo.equals("")) { 
						  mobileNo = "8500909816";
					  System.out.println("mobileNo::" + mobileNo); 
					  SendSMSAction.sendSMS(mobileNo, smsText, templateId, con); }
					 
					
					
				con.commit();
					request.setAttribute("successMsg",
							"User Login created successfully. Login details sent to Registered Mobile No.");
					
					target = "regSuccess";
					

					}else {
						
						con.rollback();
						request.setAttribute("errorMsg", "Error in User Login creation. Kindly try again.");
					}
					
				} else {
					con.rollback();
					request.setAttribute("errorMsg", "Error in User Login creation. Kindly try again.");
				}

			} else {
				request.setAttribute("errorMsg", "otp is not matched...please enter again");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabasePlugin.close(con, ps, null);
		}
		return mapping.findForward(target);
	}
}