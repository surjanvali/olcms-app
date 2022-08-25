package in.apcfss.struts.Actions;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import in.apcfss.struts.Forms.CommonForm;
import in.apcfss.struts.Utilities.NicDataBean;
import in.apcfss.struts.Utilities.NicDataRetrievalService;
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
			
			//update nic_data_chittoor set designation_id='0' where designation_id='' or designation_id is null
			sql="update "+tableName+" set designation_id='0' where designation_id='' or designation_id is null";
			DatabasePlugin.executeUpdate(sql, con);
			
			sql="update "+tableName+" set global_org_name=replace(global_org_name,'TRB07','TRB02') where substr(global_org_name,1,5)='TRB07'";
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

	
	public ActionForward saveDataOld(ActionMapping mapping, ActionForm form, HttpServletRequest request,
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
			
			//update nic_data_chittoor set designation_id='0' where designation_id='' or designation_id is null
			sql="update "+tableName+" set designation_id='0' where designation_id='' or designation_id is null";
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
			
			sql="update "+tableName+" set designation_id='0' where designation_id='' or designation_id is null";
			DatabasePlugin.executeUpdate(sql, con);
			
			sql="update nic_data set global_org_name='ENE55-APEPDCL' where global_org_name in ('APEPDCL','APEPDCL CORPORATE OFFICE')";
			DatabasePlugin.executeUpdate(sql, con);
			
			sql="update nic_data set global_org_name='ENE70-APCPDCL VIJAYAWADA' where global_org_name in ('APCPDCL VIJAYAWADA')";
			DatabasePlugin.executeUpdate(sql, con);
			
			sql="update nic_data set global_org_name='ENE53-CORPORATE OFFICE' where global_org_name in ('CORPORATE OFFICE')";
			DatabasePlugin.executeUpdate(sql, con);
			
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
	
	
	public static boolean updateEofficeData(Connection con) {
		
		String sql = "select * from nic_eoffice_data_mst order by district_id";
		
		sql="delete from nic_data_all";
		
		return true;
	}
	
	// final static String dbUrl = "jdbc:postgresql://localhost:5432/apolcms", dbUserName = "apolcms", dbPassword = "apolcms";
	
	public static void main(String[] args) {
		String sql = "", mobileNo = null;
		Connection con = null;
		ResultSet rs = null;
		Statement st = null;
		try {
			/* 
			sql="select mobile_no, emailid from ecourts_gps_latest order by slno ";
			// Class.forName("org.postgresql.Driver");
			// con = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
			// String myURL="https://demo.eoffice.ap.gov.in/TTReports/Apsecretariat.php";
			   String myURL="https://demo.eoffice.ap.gov.in/TTReports/Annamayya.php";
			   String resp = NicDataRetrievalService.sendPostRequest(myURL); 
			*/
			
			// Unirest.setTimeouts(0, 0);
			HttpResponse<String> response = Unirest.post("https://demo.eoffice.ap.gov.in/TTReports/Nandyal.php")
					  .body("")
					  .asString();
			
			ObjectMapper objectMapper = new ObjectMapper();
			// NicDataBean someclass = objectMapper.readValue(resp, NicDataBean.class);
			List<NicDataBean> deptList = objectMapper.readValue(response.toString(), new TypeReference<List<NicDataBean>>() { });
			
			for (NicDataBean c : deptList) {
				System.out.println("NAME:"+c.getFullname_en());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}