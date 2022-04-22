package in.apcfss.struts.masters;

import in.apcfss.struts.Forms.CommonForm;
import in.apcfss.struts.commons.CommonModels;

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

import plugins.DatabasePlugin;

public class StatesMasterReportAction extends DispatchAction {
	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql = null;
		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session
					.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session
					.getAttribute("role_id"));

			if (userId == null || roleId == null || userId.equals("")
					|| roleId.equals("")) {
				return mapping.findForward("Logout");
			}
			con = DatabasePlugin.connect();

			// sql = "select slno,state_code,upper(state_name) as state_name from ecourts_mst_states order by state_code";
			sql = "select sm.slno,sm.state_code,upper(state_name) as state_name, count(dm.*) as districts from ecourts_mst_states sm " + 
					"left join ecourts_mst_districts dm on (sm.state_code=dm.state_code) " + 
					"group by sm.slno,sm.state_code,sm.state_name " + 
					"order by sm.state_code ";

			request.setAttribute("HEADING", "States Master Report.");

			System.out.println("SQL:" + sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql,
					con);

			System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0)
				request.setAttribute("STATESLIST", data);
			else
				request.setAttribute("errorMsg", "No Records found to display");
		} catch (Exception e) {
			request.setAttribute("errorMsg",
					"Exception occurred : No Records found to display");
			e.printStackTrace();
		} finally {
			DatabasePlugin.closeConnection(con);
		}
		return mapping.findForward("success");
	}
	
	public ActionForward showDistricts(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql = null;
		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session
					.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session
					.getAttribute("role_id"));

			if (userId == null || roleId == null || userId.equals("")
					|| roleId.equals("")) {
				return mapping.findForward("Logout");
			}
			con = DatabasePlugin.connect();
			
			String stateCode=(String)request.getParameter("stateId");
			String stateName=(String)request.getParameter("stateName");
			
			sql = "select dist_code,upper(dist_name) as dist_name,upper(state_name) as state_name from ecourts_mst_districts inner join ecourts_mst_states using (state_code) where state_code='"+stateCode+"' order by dist_name ";

			request.setAttribute("HEADING", "Districts Master Report for "+stateName);

			System.out.println("SQL:" + sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql,
					con);

			System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0)
				request.setAttribute("DISTLIST", data);
			else
				request.setAttribute("errorMsg", "No Records found to display");
		} catch (Exception e) {
			request.setAttribute("errorMsg",
					"Exception occurred : No Records found to display");
			e.printStackTrace();
		} finally {
			DatabasePlugin.closeConnection(con);
		}
		return mapping.findForward("success");
	}
	
	public ActionForward showBenches(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Connection con = null;
		HttpSession session = null;
		String userId = null, roleId = null, sql = null;
		try {
			session = request.getSession();
			userId = CommonModels.checkStringObject(session
					.getAttribute("userid"));
			roleId = CommonModels.checkStringObject(session
					.getAttribute("role_id"));

			if (userId == null || roleId == null || userId.equals("")
					|| roleId.equals("")) {
				return mapping.findForward("Logout");
			}
			con = DatabasePlugin.connect();
			
			/*
			 * String stateCode=(String)request.getParameter("stateId"); String
			 * stateName=(String)request.getParameter("stateName");
			 */
			sql = "select est_code,upper(bench_name) as bench_name,bench_id,upper(state_name) as state_name from ecourts_mst_bench inner join ecourts_mst_states using (state_code) order by est_code,bench_id ";

			request.setAttribute("HEADING", "Bench Master Report");

			System.out.println("SQL:" + sql);
			List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql,
					con);

			System.out.println("data=" + data);
			if (data != null && !data.isEmpty() && data.size() > 0)
				request.setAttribute("BENCHLIST", data);
			else
				request.setAttribute("errorMsg", "No Records found to display");
		} catch (Exception e) {
			request.setAttribute("errorMsg",
					"Exception occurred : No Records found to display");
			e.printStackTrace();
		} finally {
			DatabasePlugin.closeConnection(con);
		}
		return mapping.findForward("success");
	}
	
	
}