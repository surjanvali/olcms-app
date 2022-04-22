package in.apcfss.struts.reports;

import in.apcfss.struts.Forms.CommonForm;
import in.apcfss.struts.commons.ApplicationVariables;
import in.apcfss.struts.commons.Itext_pdf_setting;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import plugins.DatabasePlugin;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.Barcode39;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class DeptMasterAction extends DispatchAction {

	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		String sql = null;
		try {
			if (session == null || session.getAttribute("userid") == null
					|| session.getAttribute("role_id") == null) {
				return mapping.findForward("Logout");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabasePlugin.close(con, ps, null);
		}
		return getDeptList(mapping, cform, request, response);
	}

	public ActionForward displayForm(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		String sql = null;
		try {
			if (session == null || session.getAttribute("userid") == null
					|| session.getAttribute("role_id") == null) {
				return mapping.findForward("Logout");
			}
			con = DatabasePlugin.connect();

			request.setAttribute("saveAction", "INSERT");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			request.setAttribute("HEADING", "Department Master");
			saveToken(request);
			DatabasePlugin.close(con, ps, null);
		}
		return mapping.findForward("success");
	}

	public ActionForward getDeptList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		String sql = null;
		try {
			if (session == null || session.getAttribute("userid") == null
					|| session.getAttribute("role_id") == null) {
				return mapping.findForward("Logout");
			}
			con = DatabasePlugin.connect();

			sql = "select sdeptcode,deptcode,description,ddo_code,ddo_desg,dept_id,reporting_dept from dept_new order by sdeptcode,deptcode";

			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql,
					con);
			System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0) {
				request.setAttribute("deptdata", data);
			} else {
				return displayForm(mapping, cform, request, response);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			request.setAttribute("HEADING", "Department Master");
			DatabasePlugin.close(con, ps, null);
		}
		return mapping.findForward("success");
	}

	public ActionForward editForm(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		String sql = null;
		try {
			if (session == null || session.getAttribute("userid") == null
					|| session.getAttribute("role_id") == null) {
				return mapping.findForward("Logout");
			}
			con = DatabasePlugin.connect();

			String dept_code = cform.getDynaForm("dept") != null ? cform
					.getDynaForm("dept").toString() : "";
			System.out.println("ddddocoede" + dept_code);
			if (dept_code != null && !dept_code.contentEquals("")) {

				sql = "select sdeptcode,deptcode,description,ddo_code,ddo_desg from dept where dept_id='"
						+ dept_code + "'";

				List<Map<String, Object>> data = DatabasePlugin.executeQuery(
						sql, con);
				System.out.println("data=:::::::::::::" + data);
				if (data != null && !data.isEmpty() && data.size() > 0) {

					Map<String, Object> deptData = (Map<String, Object>) data
							.get(0);

					cform.setDynaForm("dept_code", dept_code);
					cform.setDynaForm("secr_code", deptData.get("sdeptcode")
							.toString());
					cform.setDynaForm("dept_code", deptData.get("deptcode")
							.toString());
					cform.setDynaForm("description", deptData
							.get("description").toString());
					cform.setDynaForm("ddo_desg", deptData.get("ddo_desg")
							.toString());

				} 
				  else {
					  return displayForm(mapping, cform, request, response);
				}
			} else {
				request.setAttribute("errorMsg",
						"Invalid Data. Please try again.");
				return getDeptList(mapping, cform, request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			request.setAttribute("saveAction", "UPDATE");

			request.setAttribute("HEADING", "Department Master");
			DatabasePlugin.close(con, ps, null);
		}
		return mapping.findForward("success");
	}

	public ActionForward deleteDept(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		String sql = null;
		try {
			if (session == null || session.getAttribute("userid") == null
					|| session.getAttribute("role_id") == null) {
				return mapping.findForward("Logout");
			}
			con = DatabasePlugin.connect();
			String ddo_code = cform.getDynaForm("dept") != null ? cform
					.getDynaForm("dept").toString() : "";

			if (ddo_code != null && !ddo_code.contentEquals("")) {
				int i = 1;
				sql = "update dept set delete_status=true where dept_code=?";
				ps = con.prepareStatement(sql);
				ps.setString(i, ddo_code);
				int a = ps.executeUpdate();
				if (a > 0) {
					request.setAttribute("successMsg",
							"Department deleted successfully.");
				} else {
					request.setAttribute("errorMsg",
							"Invalid Data Please try again.");
				}
			}
		} catch (Exception e) {
			request.setAttribute("errorMsg",
					"Exception Occurred while deletion.");
			e.printStackTrace();
		} finally {
			DatabasePlugin.close(con, ps, null);
		}
		return getDeptList(mapping, cform, request, response);
	}

	public ActionForward updateDetails(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		String sql = null;
		try {
			if (session == null || session.getAttribute("userid") == null
					|| session.getAttribute("role_id") == null) {
				return mapping.findForward("Logout");
			}

			con = DatabasePlugin.connect();
			String dept_code = cform.getDynaForm("dept") != null ? cform
					.getDynaForm("dept").toString() : "";
			System.out.println("dept:::::::::::::::::::::::" + dept_code);
			if (dept_code != null && !dept_code.contentEquals("")) {

				int i = 0;
				sql = "update dept set  description=?,ddo_code=? , ddo_desg=?  where dept_id=? ";

				ps = con.prepareStatement(sql);

				ps.setString(++i,
						cform.getDynaForm("description") != null ? cform
								.getDynaForm("description").toString() : "");
				ps.setString(++i, cform.getDynaForm("ddo_code") != null ? cform
						.getDynaForm("ddo_code").toString() : "");

				ps.setString(++i, cform.getDynaForm("ddo_desg") != null ? cform
						.getDynaForm("ddo_desg").toString() : "0");

				ps.setString(++i, dept_code);

				int a = ps.executeUpdate();

				if (a > 0) {

					request.setAttribute("successMsg", "Department details updated successfully.");
				} else
					request.setAttribute("errorMsg",
							"Error in saving Department details. Kindly try again with valid data.");
			} else {
				request.setAttribute("errorMsg",
						"Invalid Data. Kindly try again.");
			}
		} catch (Exception e) {
			request.setAttribute(
					"errorMsg",
					"Exception Occurred while saving Department details. Kindly try again with valid data.");
			e.printStackTrace();
		} finally {
			DatabasePlugin.close(con, ps, null);
		}
		return getDeptList(mapping, cform, request, response);

	}

	public ActionForward saveDetails(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		CommonForm cform = (CommonForm) form;
		HttpSession session = request.getSession();
		String sql = null;
		try {
			if (session == null || session.getAttribute("userid") == null
					|| session.getAttribute("role_id") == null) {
				return mapping.findForward("Logout");
			}

			if (isTokenValid(request)) {

				con = DatabasePlugin.connect();

				sql = "select count(*) from dept where sdeptcode='"
						+ cform.getDynaForm("secr_code").toString()
						+ "' and deptcode='"
						+ cform.getDynaForm("dept_code").toString() + "'";

				if (Integer.parseInt(DatabasePlugin
						.getStringfromQuery(sql, con)) > 0) {
					request.setAttribute(
							"errorMsg",
							"Error: Submitting duplicate Department details. Kindly try again with valid data.");
				} else {

					String deptNo = DatabasePlugin.getSingleValue(con,
							"select max(dept_id)+1 from dept");

					System.out.println("deptno::::::::::::::::" + deptNo);

					if (deptNo != null && !deptNo.contentEquals("")) {

						int i = 1;
						sql = "insert into dept (dept_id ,sdeptcode, deptcode, description , ddo_code , ddo_desg )"
								+ "values (?, ?, ?, ?, ?, ?)";

						ps = con.prepareStatement(sql);
						ps.setString(i, deptNo);

						ps.setString(++i,
								cform.getDynaForm("secr_code") != null ? cform
										.getDynaForm("secr_code").toString()
										: "");
						ps.setString(++i,
								cform.getDynaForm("dept_code") != null ? cform
										.getDynaForm("dept_code").toString()
										: "");

						ps.setString(
								++i,
								cform.getDynaForm("description") != null ? cform
										.getDynaForm("description").toString()
										: "0");

						ps.setString(++i,
								cform.getDynaForm("ddo_code") != null ? cform
										.getDynaForm("ddo_code").toString()
										: "");
						ps.setString(++i,
								cform.getDynaForm("ddo_desg") != null ? cform
										.getDynaForm("ddo_desg").toString()
										: "");

						int a = ps.executeUpdate();

						if (a > 0) {

							request.setAttribute("successMsg",
									"Department details saved successfully.");
						}

					} else
						request.setAttribute("errorMsg",
								"Error in saving Department details. Kindly try again with valid data.");
					request.setAttribute("saveAction", "INSERT");
				}
			}
		} catch (Exception e) {
			request.setAttribute(
					"errorMsg",
					"Exception Occurred while saving Department details. Kindly try again with valid data.");
			e.printStackTrace();
		} finally {
			DatabasePlugin.close(con, ps, null);
		}
		return getDeptList(mapping, cform, request, response);

	}
}