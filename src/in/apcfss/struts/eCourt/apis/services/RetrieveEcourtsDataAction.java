package in.apcfss.struts.eCourt.apis.services;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

/*
 1. Upload cases data in excel shared by Osman and save in ecourts_cinos_new table.
 2. Text box to enter records limit and retrieve ecourts data with each cino.
 3. Retrieve and Save Order documents from API
 4. Report to show response and also to send request to API if necessary to retrieve data again
 
 * 
 * */

public class RetrieveEcourtsDataAction extends DispatchAction {
	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection con = null;
		try {

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return mapping.findForward("success");
	}
}
