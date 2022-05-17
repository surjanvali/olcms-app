package in.apcfss.struts.Actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;

import in.apcfss.struts.Forms.CommonForm;
import in.apcfss.struts.Utilities.NicDataBean;
import in.apcfss.struts.Utilities.NicDataRetrievalService2;
import in.apcfss.struts.commons.AjaxModels;
import in.apcfss.struts.commons.CommonModels;
import plugins.DatabasePlugin;


public class UploadEofficeDataAction extends DispatchAction {
	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonForm cForm = (CommonForm) form;
		Connection con = null;
		try {
			con = DatabasePlugin.connect();
			// Districts List
			cForm.setDynaForm("distList", DatabasePlugin.getSelectBox(
					"select district_id,upper(trim(district_name)) as district_name from district_mst order by district_id",
					con));
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(con!=null) {
				con.close();
			}
		}
		return mapping.findForward("success");
	}

	public ActionForward saveData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonForm cForm = (CommonForm) form;
		String tableName = "", tableNameNew="", tableNameBkp="";
		String resp = "";
		String sql = "";
		Connection con = null;
		int c = 0;
		try {
			
			/*File file = new File(cForm.getChangeLetter().getInputStream());
			FileReader readfile =  new FileReader(file);*/
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Date d1 = new Date();
			String mmyyyy = sdf.format(d1);
			
			con = DatabasePlugin.connect();
			con.setAutoCommit(false);
			// tableName = "nic_data_" + mmyyyy.trim() + "_"+ (file.getName().substring(0, (file.getName().lastIndexOf(".")))).toLowerCase() + "";
			
			tableName = AjaxModels.getTableName(CommonModels.checkStringObject(cForm.getDynaForm("distId")), con);
			
			tableNameNew = tableName+"_new_"+ mmyyyy.trim();
			tableNameBkp = tableName+"_bkp_"+ mmyyyy.trim();
			
			c += NicDataRetrievalService2.createDbScript(tableNameNew, con);

			FormFile myFile = cForm.getChangeLetter();
			InputStream is = myFile.getInputStream();
			
			BufferedReader readbuffer = new BufferedReader(new InputStreamReader(is));
			
			resp = readbuffer.readLine();

			if (resp != null && !resp.equals("")) {
				JSONArray empArray = new JSONArray(resp);
				NicDataBean ndb = new NicDataBean();
				if (empArray != null) {
					JSONObject gsonObj = new JSONObject();
					for (int i = 0; i < empArray.length(); i++) {
						gsonObj = (JSONObject) empArray.get(i);

						if (gsonObj != null) {
							ndb = new Gson().fromJson(gsonObj.toString(), NicDataBean.class);
							int result = NicDataRetrievalService2.saveResult(tableNameNew, con, ndb);
							if (result > 0)
								c++;
							else {
								i = empArray.length();
								System.out.println("EXIT");
							}
						}
					}
				}
				System.out.println("Data saved for:" + tableName);
			}
			
			
			/*/if able exists rename / backup
			sql="SELECT count(*) FROM INFORMATION_SCHEMA.TABLES where table_schema='apolcms' and table_name='"+tableName+"'";
			System.out.println("TABLE CHECK SQL:"+sql);
			if(CommonModels.checkIntObject(DatabasePlugin.getStringfromQuery(sql, con)) > 0) {
			}*/
			
			sql="alter table "+tableName+" rename to "+tableNameBkp;
			System.out.println("ALTER TO BKP QUERY:"+sql);
			DatabasePlugin.executeUpdate(sql, con);
			
			sql="alter table "+tableNameNew+" rename to "+tableName;
			System.out.println("ALTER NEW TABLE:"+sql);
			DatabasePlugin.executeUpdate(sql, con);
			
			request.setAttribute("successMsg", c+" records data saved to table "+tableName);
			con.commit();
		} catch (Exception e) {
			con.rollback();
			request.setAttribute("errorMsg", "Failed to save data to table "+tableName);
			e.printStackTrace();
		}finally {
			cForm.setDynaForm("distList", DatabasePlugin.getSelectBox(
					"select district_id,upper(trim(district_name)) as district_name from district_mst order by district_id",
					con));
			if(con!=null) {
				con.close();
			}
		}
		return mapping.findForward("success");
	}

	
	public ActionForward saveDataNew(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonForm cForm = (CommonForm) form;
		String tableName = "", tableNameNew="", tableNameBkp="";
		String resp = "";
		String sql = "";
		Connection con = null;
		int c = 0;
		try {
			
			SimpleDateFormat sdf = new SimpleDateFormat("MMyyyy");
			Date d1 = new Date();
			String mmyyyy = sdf.format(d1);
			
			con = DatabasePlugin.connect();
			con.setAutoCommit(false);

			tableName = AjaxModels.getTableName(CommonModels.checkStringObject(cForm.getDynaForm("distId")), con);
			
			tableNameNew = tableName+"_new_"+ mmyyyy.trim();
			tableNameBkp = tableName+"_bkp_"+ mmyyyy.trim();
			
			c += NicDataRetrievalService2.createDbScript(tableNameNew, con);

			FormFile myFile = cForm.getChangeLetter();
			InputStream is = myFile.getInputStream();
			
			BufferedReader readbuffer = new BufferedReader(new InputStreamReader(is));
			
			resp = readbuffer.readLine();

			if (resp != null && !resp.equals("")) {
				JSONArray empArray = new JSONArray(resp);
				NicDataBean ndb = new NicDataBean();
				if (empArray != null) {
					JSONObject gsonObj = new JSONObject();
					for (int i = 0; i < empArray.length(); i++) {
						gsonObj = (JSONObject) empArray.get(i);

						if (gsonObj != null) {
							ndb = new Gson().fromJson(gsonObj.toString(), NicDataBean.class);
							int result = NicDataRetrievalService2.saveResult(tableName, con, ndb);
							if (result > 0)
								c++;
							else {
								i = empArray.length();
								System.out.println("EXIT");
							}
						}
					}
				}
				System.out.println("Data saved for:" + tableName);
			}
			
			sql="alter table "+tableName+" rename to "+tableNameBkp;
			System.out.println("ALTER TO BKP QUERY:"+sql);
			DatabasePlugin.executeUpdate(sql, con);
			
			sql="alter table "+tableNameNew+" rename to "+tableName;
			System.out.println("ALTER NEW TABLE:"+sql);
			DatabasePlugin.executeUpdate(sql, con);
			
			request.setAttribute("successMsg", c+" records data saved to table "+tableName);
			con.commit();
		} catch (Exception e) {
			con.rollback();
			request.setAttribute("errorMsg", "Failed to save data to table "+tableName);
			e.printStackTrace();
		}finally {
			cForm.setDynaForm("distList", DatabasePlugin.getSelectBox(
					"select district_id,upper(trim(district_name)) as district_name from district_mst order by district_id",
					con));
			if(con!=null) {
				con.close();
			}
		}
		return mapping.findForward("success");
	}
	
	
	public ActionForward saveDataIntoSectDept(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonForm cForm = (CommonForm) form;
		String tableName = "nic_data";
		String resp = "";
		String sql = "";
		Connection con = null;
		int c = 0;
		try {
			
			con = DatabasePlugin.connect();
			con.setAutoCommit(false);
			
			FormFile myFile = cForm.getChangeLetter();
			InputStream is = myFile.getInputStream();
			
			BufferedReader readbuffer = new BufferedReader(new InputStreamReader(is));
			
			resp = readbuffer.readLine();

			if (resp != null && !resp.equals("")) {
				JSONArray empArray = new JSONArray(resp);
				NicDataBean ndb = new NicDataBean();
				if (empArray != null) {
					JSONObject gsonObj = new JSONObject();
					for (int i = 0; i < empArray.length(); i++) {
						gsonObj = (JSONObject) empArray.get(i);

						if (gsonObj != null) {
							ndb = new Gson().fromJson(gsonObj.toString(), NicDataBean.class);
							int result = NicDataRetrievalService2.saveResult(tableName, con, ndb);
							if (result > 0)
								c++;
							else {
								i = empArray.length();
								System.out.println("EXIT");
							}
						}
					}
				}
				System.out.println("Data saved for:" + tableName);
			}
			request.setAttribute("successMsg", c+" records data saved to table "+tableName);
			con.commit();
		} catch (Exception e) {
			con.rollback();
			request.setAttribute("errorMsg", "Failed to save data to table "+tableName);
			e.printStackTrace();
		}finally {
			cForm.setDynaForm("distList", DatabasePlugin.getSelectBox(
					"select district_id,upper(trim(district_name)) as district_name from district_mst order by district_id",
					con));
			if(con!=null) {
				con.close();
			}
		}
		return mapping.findForward("success");
	}
}
