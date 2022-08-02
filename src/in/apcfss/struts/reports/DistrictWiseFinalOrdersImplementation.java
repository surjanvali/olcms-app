package in.apcfss.struts.reports;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import in.apcfss.struts.Forms.CommonForm;
import in.apcfss.struts.commons.CommonModels;
import plugins.DatabasePlugin;

public class DistrictWiseFinalOrdersImplementation extends DispatchAction {

	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection con = null;
		HttpSession session = null;
		CommonForm cform = (CommonForm) form;
		String userId = null, roleId = null, sql = null, sqlCondition = "";
		try {
			System.out.println("heiii");
			session = request.getSession();
			userId = CommonModels.checkStringObject(session.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session.getAttribute("role_id"));

			if (userId == null || roleId == null || userId.equals("") || roleId.equals("")) {
				return mapping.findForward("Logout");
			}

			con = DatabasePlugin.connect();

			sql = "select dist_id, district_name, casescount,order_implemented,appeal_filed, \r\n"
					+ "casescount-(order_implemented + appeal_filed) as pending,  \r\n"
					+ "(order_implemented + appeal_filed)/(4*100) as actoin_taken_percent\r\n" + "from (\r\n"
					+ "select dist_id,dm.district_name,count(distinct a.cino) as casescount, \r\n"
					+ "sum(case when length(action_taken_order)> 10 then 1 else 0 end) as order_implemented ,\r\n"
					+ "sum(case when length(appeal_filed_copy)> 10 then 1 else 0 end) as appeal_filed\r\n" + " \r\n"
					+ "from district_mst dm left join ecourts_case_data a on (a.dist_id=dm.district_id) inner join ecourts_case_finalorder b on (a.cino=b.cino) \r\n"
					+ "\r\n" + "left join ecourts_olcms_case_details ocd on (a.cino=ocd.cino)\r\n"
					+ "group by dist_id, dm.district_name) a1 order by 2";

			request.setAttribute("HEADING", "Sect. Dept. Wise High Court Cases Abstract Report");

			System.out.println("SQL:" + sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);
			System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0)
				request.setAttribute("CAUSELISTCASES", data);
			else
				request.setAttribute("errorMsg", "No Records found to display");
		} catch (Exception e) {
			request.setAttribute("errorMsg", "Exception occurred : No Records found to display");
			e.printStackTrace();
		} finally {
			DatabasePlugin.closeConnection(con);
		}
		return mapping.findForward("success");
	}

}
