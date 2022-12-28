package in.apcfss.struts.Actions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.codec.Base64;
import com.itextpdf.text.pdf.qrcode.QRCode;
import com.sun.javafx.iio.ImageStorage.ImageType;

import cfss.formbeans.FormBean;
import cfss.gov.login.SessionDetails;
import cfss.pdfs.NumberToWord;
import filters.FilterContent;
import in.apcfss.struts.Forms.CommonForm;
import plugins.DatabasePlugin;
import plugins.GeneralQueries;
import sun.misc.BASE64Decoder;

public class DigitalSignAction extends DispatchAction
{
	
	FilterContent fc = new FilterContent();
	

	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		CommonForm bean = (CommonForm) form;
		//FormBean bean = (FormBean) form;
		String user_id=request.getSession().getAttribute("userid").toString();
		System.out.println("user_id        Affl_OrderGen2022---- "+user_id);
		Connection con = null;
		con = DatabasePlugin.connect();
		String ub_code = request.getSession().getAttribute("userid").toString().split("_")[1].substring(1,3);
		System.out.println("ub_code--------"+ub_code);
		
	//	String ac_year=(String)request.getSession().getAttribute("affl_ac_year");
		String ac_year="2022-23";
		System.out.println("ac_year ++++++++++++++++ "+ac_year);
		
		//List<Map<String, Object>> data = DatabasePlugin.executeQuery(sql, con);

		//GeneralQueries genObj = new GeneralQueries();
		String sql = "";
		String tableHead = "";
		String headings = "";
		String align = "";
		String totals = "";
		String datatype = "";
		String links = "";
		String display = "";
		String tableFoot = "";
		String pdfwidth = "", condition="";
		ArrayList data = null;
		try {

			if(request.getSession().getAttribute("user_gr_code").equals("106")){ 
				
				  condition=" and c.ub_code='"+ub_code+"' ";
			}
			 
			 
		
			sql = " select distinct(c.coll_code)coll_code,   c.inst_name  as coll_name, coll_nature_name,  " +
					" '<a href=AffiliationPrint.edu?key=unspecified&ac_year=2022-23&ub_code='||c.ub_code||'&coll_code='||c.coll_code||'>Application Submitted by College</a>&nbsp;<i class=''fa fa-cloud-download'' aria-hidden=''true''></i>', " +
					" '<a href=FFCAAffiliationPrint2022.edu?key=unspecified&ac_year=2022-23&ub_code='||c.ub_code||'&coll_code='||c.coll_code||'>FFCA Report</a>&nbsp;<i class=''fa fa-cloud-download'' aria-hidden=''true''></i>', " +
					" ' <a href='||cdc_file_path ||' target=''_blank''><i class=''fa fa-cloud-download'' aria-hidden=''true''></i></a>', " +
					"  case when final_pdf_path is null then " +
					" '<a href=Affl_OrderGen2022.edu?key=AffiliationOrderData&ac_year=2022-23&ub_code='||c.ub_code||'&coll_code='||c.coll_code||'>Click Here to get Affiliation Order</a>&nbsp;' " +
					" else '<a href='||final_pdf_path ||' target=''_blank''><i class=''fa fa-cloud-download'' aria-hidden=''true''></i> </a> ' end final_pdf_path " +
					"  " + 
					
					/*"  case when final_pdf_path is null then 'Not Generated' " +
					" else '<a href='||final_pdf_path ||' target=''_blank''><i class=''fa fa-cloud-download'' aria-hidden=''true''></i> </a> ' end final_pdf_path " +
					"  " +*/
					" from affl_inst_details " +
					" c inner join pmss_districts_mst pd on c.inst_dist_code=pd.dist_code " +
					"  left join pmss_mandals_mst pm on c.inst_mandal_code=pm.mandal_code and c.inst_dist_code=pm.dist_code " +
					"  left join pmss_villages_mst pv on c.inst_village_code=pv.village_code and c.inst_dist_code=pv.dist_code and c.inst_mandal_code=pv.mandal_code   " +
					" inner join pmss_university_mst u on c.ub_code=u.ub_code " +
					" left join affl_coll_nature_code cnc on cnc.coll_nature_code=c.coll_nature_code " +
			        " inner join affl_order_verif o on o.coll_code=c.coll_code  and o.ac_year=c.ac_year " +
			        " left join final_affl_order_details f  on f.college_code=c.coll_code  and f.ac_year=c.ac_year " +
			        " where  c.ac_year='"+ac_year+"' "+condition+"  " +  //  and c.coll_code='17964'    and c.ub_code='12'       and c.coll_nature_code='3'
					" and cdc_file_path is not null and cdc_dsk_undertaking is not null and cdc_dsk_to_vc is not null " +
					" and cdc_course_details_confirmation=true " +
					" and cdc_inst_details_confirmation=true "+  
					" order by 1 " ; 
		//	}

			
			 
			
			System.out.println("Affl_OrderGen2022 ::: List of colleges ::::  "+sql);
			
			tableHead = "Approve and Generate Affiliation Order";

			headings="S.No.:College Code:College Name:Nature of the College:" +
					"Application Submitted by College:FFCA Report: Cdc Approval Pdf :  Affiliation Order";
			
		//	headings = "S.No:College Code:District:coll_code:coll_name:column:column:ffca_inspect_mem:ffca_univ_fee:ffca_gen_info:society_details:inst_details:society_land_details:ffca_build_approv:ffca_build_blk:admin_area:instruct_area:sports_hostel:cah_details:ffca_co_curric:course_intake:ffca_princi:employee_details:lab_details:computer_software:ffca_bio_facil:library_facilties_details:ffca_income:affiliation_fee";
			align =  	"C:L:L:L:L:L:C:C";
			totals = 	"N:N:N:N:N:N:N:N";
			datatype =  "S:S:S:S:S:S:S:S";
			display =   "Y:Y:Y:Y:Y:Y:Y:Y";
			links =     "N:N:N:N:N:N:N:N";

			data = (ArrayList) DatabasePlugin.executeQuery(sql, con);

			request.setAttribute("headings", headings);
			request.setAttribute("align", align);
			request.setAttribute("totals", totals);
			request.setAttribute("datatype", datatype);
			request.setAttribute("links", links);
			request.setAttribute("display", display);
			request.setAttribute("tableHead", tableHead);
			request.setAttribute("tableFoot", tableFoot);
			request.setAttribute("pdfwidth", pdfwidth);
			request.setAttribute("data", data);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

		return mapping.findForward("showReport");
	}
	
	
	 
	public ActionForward AffiliationOrderData(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws Exception 
	{
		CommonForm bean = (CommonForm) form;
		String user_id=request.getParameter("coll_code");
		String sql=null;
		String ac_year="2022-23";
		String ub_code =null,ub_name=null, coll_code="";
		ub_code=request.getParameter("ub_code")!=null && !request.getParameter("ub_code").equals("") ? request.getParameter("ub_code") :request.getAttribute("ub_code").toString();;
		coll_code=request.getParameter("coll_code")!=null && !request.getParameter("coll_code").equals("") ? request.getParameter("coll_code") :request.getAttribute("coll_code").toString();
		GeneralQueries gen= new GeneralQueries();

		try
		{
			sql=" select establish_dt_univ,registrar_name,u_address,u_phone_no,u_fax_no,u_email,ub_name,logo_path " +  
				" from affl_univ_mst where  ub_code='"+ub_code+"' and ac_year='"+ac_year+"' "; 

			HashMap univ_det=gen.getMap(sql);
			ub_name=univ_det.get("ub_name").toString();
			request.setAttribute("univ_det", univ_det);	
			
			sql = " select c.ub_name,a.coll_code,cdc_coll_name as inst_name, " + // inst_name
					" upper(inst_address||', Dist- '||d.dist_name||', Mdl- '||m.mandal_name||', Pincode-'||inst_pincode) as inst_address, "+
" upper(d.dist_name)dist_name, m.mandal_name,  coalesce(a.inst_type,'0')inst_type, inst_estd,  coalesce(society_name,'-')society_name,   "+
"  upper(society_addr||', Dist- '||dem.dist_name||', Mdl- '||mem.mandal_name||', Pincode-'||society_pincode) as society_addr,   "+
"  coalesce(u_address,'--')u_address,registrar_name,vc_name ,    "+
"  case when build_own_lease=true then 'Own' else 'Leased' end  as accomodation "+   
"  ,coalesce(a.lat_eoa_ref_no,'-')lat_eoa_ref_no,coalesce( to_char(a.lat_eop_date,'dd/mm/yyyy'),'-')lat_eop_date,coalesce(to_char(lat_go_date,'dd/mm/yyyy'),'-') lat_go_date, "+ 
"  coalesce(lat_go_ref_no,'-')lat_go_ref_no , coalesce(lat_affl_ord_ref_no,'-')lat_affl_ord_ref_no,coalesce(to_char(ffca_latest_affl_order,'dd/mm/yyyy'),'-') as ffca_latest_affl_order, "+ 
" coalesce(to_char(date_of_insp,'dd-mm-yyyy'),'-') as date_of_insp, " +
" string_agg(ref_no::text,', ') other_preceedings_ref, " + // other_preceedings,
//" case when other_preceedings=true then string_agg(','||coalesce(ref_no,'')||'''\n''') else '-' end other_preceedings_ref, "+
" is_affl_additional_sections_proceedings_ref_no||', Dated: '||to_char(is_affl_additional_sections_proceedings_date,'dd/mm/yyyy') as sections, "+ 
" affl_additional_courses_flag_proceedings_ref_no||', Dated: '||to_char(affl_additional_courses_flag_proceedings_date,'dd/mm/yyyy') as additional_courses, "+
" is_affl_existing_courses_proceedings_ref_no||', Dated: '||to_char(is_affl_existing_courses_proceedings_date,'dd/mm/yyyy') as existing_courses, "+
" permanent_affl_status_meeting20_21_flag_proceedings_ref_no||', Dated: '||to_char(permanent_affl_status_meeting20_21_flag_proceedings_date,'dd/mm/yyyy') as permanent_affl_status, "+
" withdrawal_existing_courses_flag_proceedings_ref_no||', Dated: '||to_char(withdrawal_existing_courses_flag_proceedings_date,'dd/mm/yyyy') as withdrawal_existing_courses, "+
" change_medium_flag_proceedings_ref_no||', Dated: '||to_char(change_medium_flag_proceedings_date,'dd/mm/yyyy') as change_medium, "+
" is_change_colg_addr_proceedings_ref_no||', Dated: '||to_char(is_change_colg_addr_proceedings_date,'dd/mm/yyyy') as colg_addr,ff.gort_no,gort_no2, " +
" 'Procgs. No. '||affl_order_proc_no_2020_21||', Dated: '||to_char(affl_order_2020_21_date,'dd/mm/yyyy') as proc_no_2021,  "+
" 'Proc No. '||affl_order_proc_no_2021_22||', Dated: '||to_char(affl_order_2021_22_date,'dd/mm/yyyy') as proc_no_2022, " +
" to_char(o.created_time,'dd/mm/yyyy')cdc_created_time, to_char(ff.gort_date,'dd/mm/yyyy') as gort_date, " +
" affl_order_proc_no_2020_21, " +
" to_char(affl_order_2020_21_date,'dd/mm/yyyy') as affl_order_2020_21_date, " +
" affl_order_proc_no_2021_22, " +
" to_char(affl_order_2021_22_date,'dd/mm/yyyy') as affl_order_2021_22_date, " +
" te.enter_on as clg_appl_date, " +
" is_affl_additional_sections,affl_additional_courses_flag,is_affl_existing_courses,permanent_affl_status_meeting20_21_flag,withdrawal_existing_courses_flag,change_medium_flag,is_change_colg_addr  "+
  " from affl_inst_details  a   "+
  " left join affl_sociery_details b on a.society_slno=b.sl_no "+   
  " inner join affl_univ_mst c on c.ub_code=a.ub_code and c.ac_year=a.ac_year "+  
  " inner join pmss_districts_mst  d on d.dist_code=a.inst_dist_code   "+
  " inner join pmss_mandals_mst m on m.mandal_code=a.inst_mandal_code and m.dist_code=d.dist_code "+  
  " inner join pmss_districts_mst dem on dem.dist_code=b.society_dist    "+
  " inner join pmss_mandals_mst mem on mem.mandal_code=b.soc_mandal_code and mem.dist_code=b.society_dist "+  
  " inner join affl_land_2_details la on la.coll_code=a.coll_code and la.ac_year=a.ac_year   "+
  " left join affl_order_verif o on o.coll_code=a.coll_code   and a.ac_year= o.ac_year " +
  " left join affl_order_verif_other_proceedings aov on aov.coll_code=a.coll_code and aov.ac_year=a.ac_year "+
  " left join ffca_inst_details f on f.coll_code=a.coll_code and f.ac_year=a.ac_year   "+
  " left join ffca_fdbck_recmnd_details fdb on fdb.coll_code=a.coll_code and fdb.ac_year=a.ac_year "+ 
  " left join affl_college_courses_flags ccf on ccf.coll_code=a.coll_code and ccf.ac_year=a.ac_year  "+
  " left join final_affl_order_details fao on fao.college_code=a.coll_code and fao.ac_year=a.ac_year " +

  /*" left join (select college_code,ac_year,min(to_char(enter_on,'dd/mm/yyyy'))enter_on " +
  " from affl_teaching_employee_details where college_code='"+coll_code+"' group by 1,2)te on te.college_code=a.coll_code and te.ac_year=a.ac_year " +
  */
  " left join (select  college_code,ac_year, max(to_char(created_time,'dd/mm/yyyy'))enter_on " +
  " from affl_college_course_intake_details_20_21 where college_code='"+coll_code+"' group by 1,2)te on te.college_code=a.coll_code and te.ac_year=a.ac_year " +
 
  " left join (select college_code, ac_year, gort_no,gort_date from final_affl_order_details where ac_year='2021-22')ff on ff.college_code=a.coll_code  "+
  " where a.coll_code='"+coll_code+"' and a.ac_year='"+ac_year+"' group by 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,23,24,25,26,27,28,29, " +
  		" ff.gort_no,gort_no2, proc_no_2021,proc_no_2022, " +
  " o.created_time,is_affl_additional_sections,affl_additional_courses_flag,is_affl_existing_courses,permanent_affl_status_meeting20_21_flag,withdrawal_existing_courses_flag,change_medium_flag,is_change_colg_addr, ff.gort_date," +
  "affl_order_proc_no_2020_21, affl_order_2020_21_date,affl_order_proc_no_2021_22, affl_order_2021_22_date,clg_appl_date ";

			 

			System.out.println("Affl_OrderGen2022::::  First ::::   data on affiliation order :::::  "+sql);

			HashMap coll_details=gen.getMap(sql);
			request.setAttribute("coll_details", coll_details);	
			
			
			String date_of_insp=coll_details.get("date_of_insp").toString();

			 
			
			sql=" select row_number()  over (order by slno) slno, course_group_desc,course_id,reg_self,  " +
					" medium_code, name,  course_affl_type, course_cat_name, " +
					" course_name,graduation_level, intake_approved_competent_authotity,univ_course_approved,affl_nature, " +
					" affl_valid_upto,final_sanction_intake,final_no_section,course_deficiencies,deficiencies_reason " +
					" from ( " +
					" select distinct(cdc.slno), "+ 
		" a.course_id,a.reg_self, " +
		" case when cdc.medium_code is not null then cdc.medium_code else a.medium_code end medium_code, " +
		" case when mm.name is not null then mm.name else m.name end  as name," +
		" a.course_affl_type,course_group_desc,  b.course_cat_name, c.course_name,graduation_level, "+ 
		" intake_approved_competent_authotity, "+ 
		" case when univ_course_approved=true then 'Yes' else 'No' end univ_course_approved,affl_nature, to_char(cdc.affl_valid_upto,'dd/mm/yyyy')affl_valid_upto,cdc.final_sanction_intake,final_no_section, "+ 
		" case when course_deficiencies=true then 'Yes' else 'No' end course_deficiencies,deficiencies_reason " +
		" from final_affl_intake_details_2022_23 a   left join edu_course_master c on a.course_id=c.course_id  " +
		" left join course_category b on c.course_cat_id=b.course_cat_id  " +
		" left join pmss_course_group_mst gm on gm.course_group_code=b.course_group_code  " +
		" left join final_affl_intake_details_cdc cdc on a.ac_year=cdc.ac_year and a.college_code=cdc.coll_code and a.course_id=cdc.course_id  " +
		" and a.reg_self=cdc.reg_self " +
		//" and  a.medium_code=cdc.medium_code " +
		" and a.course_affl_type=cdc.course_affl_type " +
		" left join medium m on cdc.medium_code=m.medium_code " +
		" left join medium mm on cdc.medium_code=mm.medium_code "+  
		 " where cdc.coll_code='"+coll_code+"' and cdc.ac_year='"+ac_year+"' and univ_course_approved=true  order by cdc.slno)aa "; //and is_new_course=false
			
			
			System.out.println("Affl_OrderGen2022::::  Second ::::   Courses - data on affiliation order :::::  "+sql);
			
			ArrayList<HashMap<String, Object>> crse_details=gen.getListMapData(sql);
			request.setAttribute("crse_details", crse_details);


		
			sql = "select sl_no,description,to_char(last_date,'dd/mm/yyyy')last_date from affl_order_deficiencies_by_cdc " +
					" where coll_code='"+coll_code+"' and ac_year='"+ac_year+"' order by sl_no";
			
			ArrayList<HashMap<String, Object>> def_details=gen.getListMapData(sql);
			request.setAttribute("def_details", def_details);
			
			String sql123="select  lat_eop_order_pdf, inst_addr_proof_pdf from affl_inst_details  " +
					" where  coll_code='"+user_id+"'   and ac_year='"+ac_year+"'"; 
			     
			System.out.println("Affl_OrderGen2022  table AfflInstDetails set values::::::::EOP::::::"+sql123);
			HashMap data123=gen.getMap(sql123);
			request.setAttribute("lat_eop_order_pdf", data123.get("lat_eop_order_pdf"));
			bean.setBeanProperties("lat_eop_order_pdf11", data123.get("lat_eop_order_pdf"));
			
			request.setAttribute("inst_addr_proof_pdf", data123.get("inst_addr_proof_pdf"));
			bean.setBeanProperties("inst_addr_proof_pdf11", data123.get("inst_addr_proof_pdf"));
			
			
			String sql124= " select final_pdf_path from final_affl_order_details " +
					 " where college_code='"+user_id+"'  and ac_year='2021-22' ";
			System.out.println("final_affl_order_details:::"+sql124);
			HashMap data124=gen.getMap(sql124);
			request.setAttribute("final_pdf_path", data124.get("final_pdf_path"));
			bean.setBeanProperties("final_pdf_path11", data124.get("final_pdf_path"));
			
			String sql125= " select upload_pdf, proceeding_upload_university from ffca_fdbck_recmnd_details " +
					 " where coll_code='"+user_id+"'  and ac_year='"+ac_year+"' ";
			System.out.println("ffca_fdbck_recmnd_details:::"+sql125);
			HashMap data125=gen.getMap(sql125);
			request.setAttribute("upload_pdf", data125.get("upload_pdf"));
			bean.setBeanProperties("upload_pdf11", data125.get("upload_pdf"));
			
			request.setAttribute("proceeding_upload_university", data125.get("proceeding_upload_university"));
			bean.setBeanProperties("proceeding_upload_university11", data125.get("proceeding_upload_university"));

		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		request.setAttribute("coll_code", user_id);
		request.setAttribute("ub_code",ub_code );
		request.setAttribute("ub_name",ub_name );
		
		 
		return mapping.findForward("success");
	}

	
	 
	public ActionForward sign_pdf(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws Exception 
	{
		CommonForm f = (CommonForm) form;
		HttpSession session=request.getSession(true);
		GeneralQueries gen = new GeneralQueries();
		String ac_year="2022-23";
		
		String userid=(String) f.getBeanProperties("coll_code");
		System.out.println("coll_code:::::::::::"+userid);
		
		String ub_code=(String) f.getBeanProperties("ub_code");
		System.out.println("ub_code:::::::::::"+ub_code);

		String gort_no=(String) f.getBeanProperties("gort_no");
		System.out.println("gort_no:::::::::::"+gort_no);
		
		String gort_no2=(String) f.getBeanProperties("gort_no2");
		System.out.println("gort_no2:::::::::::"+gort_no2);
		
		String gort_no3=(String) f.getBeanProperties("gort_no3");
		System.out.println("gort_no3:::::::::::"+gort_no3);
		
		String gort_no4=(String) f.getBeanProperties("gort_no4");
		System.out.println("gort_no4:::::::::::"+gort_no4);
		
		
		String gort_date=(String) f.getBeanProperties("gort_date");
		System.out.println("gort_date:::::::::::"+gort_date);
		
		String gort_date2=(String) f.getBeanProperties("gort_date2");
		System.out.println("gort_date2:::::::::::"+gort_date2);
		
		String gort_date3=(String) f.getBeanProperties("gort_date3");
		System.out.println("gort_date3:::::::::::"+gort_date3);
		
		String gort_date4=(String) f.getBeanProperties("gort_date4");
		System.out.println("gort_date4:::::::::::"+gort_date4);
		
		
		
		String accdby=(String) f.getBeanProperties("accd_by");
		System.out.println("accdby:::::::::::"+accdby);
		
		String auth_by=(String) f.getBeanProperties("auth_by");
		System.out.println("auth_by:::::::::::"+auth_by);
		
		String affl_order_2020_21_date=(String) f.getBeanProperties("affl_order_2020_21_date");
		System.out.println("affl_order_2020_21_date:::::::::::"+affl_order_2020_21_date);
		
		String affl_order_proc_no_2020_21=(String) f.getBeanProperties("affl_order_proc_no_2020_21");
		System.out.println("affl_order_proc_no_2020_21:::::::::::"+affl_order_proc_no_2020_21);
		
		String affl_order_2021_22_date=(String) f.getBeanProperties("affl_order_2021_22_date");
		System.out.println("affl_order_2021_22_date:::::::::::"+affl_order_2021_22_date);
		
		String affl_order_proc_no_2021_22=(String) f.getBeanProperties("affl_order_proc_no_2021_22");
		System.out.println("affl_order_proc_no_2021_22:::::::::::"+affl_order_proc_no_2021_22);
		
		
		
	
		
		String affl_order_2022_23_date=(String) f.getBeanProperties("affl_order_2022_23_date");
		System.out.println("affl_order_2022_23_date::::2022:::::::"+affl_order_2022_23_date);
		
		String affl_order_proc_no_2022_23=(String) f.getBeanProperties("affl_order_proc_no_2022_23");
		System.out.println("affl_order_proc_no_2022_23:::::2022::::::"+affl_order_proc_no_2022_23);
		
		
		
		String photo_query1=null;
		String photo_query2=null;
		
		String filename1=null;
		String filename2=null;
		
		boolean file_status1;
		boolean file_status2;
		
		FormFile pdf_file1 = null;
		FormFile pdf_file2 = null;
		
		String pic_value1=null;
		String pic_value2=null;
		
		String university_ec_doc=fc.sanitize(f.getBeanProperties("university_ec_doc").toString());
		
		if(university_ec_doc!=null && !university_ec_doc.toString().equals(""))
		{
			pdf_file2 = (FormFile)f.getBeanProperties("university_ec_doc");
			filename2 =userid.trim()+"_university_ec_doc_"+ac_year+"_"+gen.getStringfromQuery("select to_char(now(),'ddmmyyyymiss') ")+".pdf";

			photo_query2 = ("Uploads/DigitalAffiliationOrder/2022/").replace("\\", "/") ;
			file_status2=GeneralQueries.uploadingFile(pdf_file2,GeneralQueries.getPath(request, photo_query2),filename2);
		
			pic_value2=photo_query2+filename2;
		}else{
			
			pic_value2=f.getBeanProperties("university_ec_doc11").toString();
			
		}
		
		String remarks="";
		
		if(!f.getBeanProperties("remarks").equals(""))
		{
			remarks=(String) f.getBeanProperties("remarks");
			System.out.println("remarks:::::::::::"+remarks);
		}
		String sql=" select count(*) from  final_affl_order_details where college_code='"+userid+"' and ac_year='"+ac_year+"' ";
		int count=Integer.parseInt(gen.getStringfromQuery(sql));
		
		 
		if(count==0)
		{
			 sql=" insert into final_affl_order_details (college_code,university_ec_doc,gort_no,gort_no2,gort_no3,gort_no4, " +
			 		" gort_date, gort_date2,gort_date3,gort_date4, " +
					" accorded_by, " +
					" authority_by,remarks,ac_year,ub_code, affl_order_2021_22_date, affl_order_proc_no_2021_22, " +
					"  affl_order_2022_23_date, affl_order_proc_no_2022_23, entered_by,entered_date,ip_address)" +
			
					" values ('"+userid+"', '"+pic_value2+"', '"+gort_no+"', '"+gort_no2+"', '"+gort_no3+"', '"+gort_no4+"', " +
					" to_date('"+gort_date+"','dd/mm/yyyy'), to_date('"+gort_date2+"','dd/mm/yyyy'),  " +
					" to_date('"+gort_date3+"','dd/mm/yyyy'),  to_date('"+gort_date4+"','dd/mm/yyyy'), " +
					" '"+accdby+"', " +
					" '"+auth_by+"','"+remarks+"','"+ac_year+"','"+ub_code+"', " +
			 
					" to_date('"+affl_order_2021_22_date+"','dd/mm/yyyy'), '"+affl_order_proc_no_2021_22+"', " +
					"  to_date('"+affl_order_2022_23_date+"','dd/mm/yyyy'),  " +
					" '"+affl_order_proc_no_2022_23+"',  '"+userid+"' ,now(),'"+request.getRemoteAddr()+"') ";
			
			System.out.println("inserted ::::in::::::sign_pdf:::method:: "+sql);
			int res=gen.pstmtExecuteUpdate(sql, null);
			
			
		}

	 

		String file_name="DigitalAffiliationOrder_2022_23_"+userid.trim()+".pdf";

	//	boolean status1=GeneratePDF(mapping, form, request, response,file_name,userid.trim(),procdno,procdate,accdby,auth_by,remarks);
		boolean status1=GeneratePDF(mapping, form, request, response,file_name,userid.trim(),gort_no,gort_date,accdby,auth_by,remarks); 

		f.setBeanProperties("file_name", file_name);
		f.setBeanProperties("coll_code", userid);
		request.setAttribute("coll_code", userid);
		System.out.println("sign_pdf ::: method::redirecting to GeneratePDF------ ");
		return mapping.findForward("success");
	}
 

	/*public boolean GeneratePDF(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response,String file_name,String coll_code,String procdno,String procdate,String auth_by,String remarks)throws Exception 
	{*/
	
	
	/*public boolean GeneratePDF(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response,String file_name,String coll_code,String procdno,String procdate,String accdby,String auth_by,String remarks)throws Exception 
		{*/
			
	
	public boolean GeneratePDF(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response, 
			 String file_name,String coll_code,String gort_no,String gort_date,String accdby,String auth_by,String remarks)throws Exception {
		
		System.out.println("GeneratePDF:::: method:::: coll_code::::::::::::::"+coll_code);
		System.out.println("procdno::::::::::::::"+gort_no);
		System.out.println("procdate::::::::::::::"+gort_date);
		System.out.println("accdby::::::::::::::"+accdby);
		System.out.println("auth_by::::::::::::::"+auth_by);
		System.out.println("remarks::::::::::::::"+remarks);
		 
		
		CommonForm f = (CommonForm) form;
		String ac_year="2022-23";
		
		boolean status=false;
		PrintWriter out = response.getWriter();
		HashMap hm =new HashMap();
		GeneralQueries gen= new GeneralQueries();
		String strDtwProcNo="",ifsc="";
		StringBuffer htmlData=new StringBuffer();   
		String filesepartor=System.getProperty("file.separator");
		String contextName=SessionDetails.contextName,code=null,path="",officer_description="";
		//String contextPath=getServlet().getServletContext().getRealPath("") + "/" ;
		String contextPath= request.getRealPath("/");
		//String contextPath  = System.getProperty("catalina.base")+filesepartor+"webapps"+filesepartor+contextName+filesepartor;
		HttpSession session = request.getSession(true);
		ArrayList data= new ArrayList();
		Document document = null;
		String userid = (String) session.getAttribute("userid");
		System.out.println("userid::::::::::::::"+userid);
		PdfWriter writer = null;
		PdfPTable table = null;
		PdfPTable borderTbl = null,InnerTable=null;
		PdfPCell borderCell=null,InnerCol=null;
		PdfPCell cell=null;
		PdfPCell subCell=null;
		String uploaded_images_path = contextPath+"Uploads"+filesepartor+"AffiliationOrder_2022"+filesepartor;
		System.out.println("uploaded_images_path ================  "+uploaded_images_path);
		NumberToWord ntw = new NumberToWord();
		String sql="";
		String file = uploaded_images_path+file_name; 
		System.out.println("file ::::::::"+file);
		
		System.out.println("contextpath:::::::::::::::::::::::::::::::::::::>>>>>>"+contextPath+"------ ");
		
		ArrayList<String> sqlValues = new ArrayList<String>();
		String csrf_token = request.getSession().getAttribute("csrf_token")	.toString();
		String csrf_token_sl = fc.sanitize(request.getParameter("csrf_token_sl"));

		try
		{
			String sqldate="select to_char(now(),'dd_mm_yyyy_HH_mm_ss')";
			String datetime=gen.getStringfromQuery(sqldate);
			
			String sqlcourses=" select count(*) from final_affl_intake_details_cdc where coll_code='"+coll_code+"' " +
					" and univ_course_approved=true and ac_year='"+ac_year+"' ";
			String no_of_courses=gen.getStringfromQuery(sqlcourses);
			 
			
			
			createDirectories(uploaded_images_path);
			document = new Document(PageSize.A4);
			//com.itextpdf.text.Document document= new com.itextpdf.text.Document(new RectangleReadOnly(900.0f,800.0f), 10, 10, 15, 15);
			writer = PdfWriter.getInstance(document, new FileOutputStream(file));
			document.open();
			document.newPage();
			
			


			table = new PdfPTable(2);
			table.getDefaultCell().setBorder(0);
			table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			table.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP);
			table.setTotalWidth(100);
			table.setWidthPercentage(100);
			table.getDefaultCell().setSpaceCharRatio(3);
			
			/*String tot_flags=gen.getStringfromQuery("select sum(sections+courses+exist_courses+perm_proceed+withdrawl+medium+coll_change) tot_flags from ( "+ 
					" select case when is_affl_additional_sections=true then 1 else 0 end sections,  "+ 
					" case when affl_additional_courses_flag=true then 1 else 0 end courses,  "+ 
					" case when is_affl_existing_courses=true then 1 else 0 end exist_courses,  "+ 
					" case when permanent_affl_status_meeting20_21_flag=true then 1 else 0 end perm_proceed, "+  
					" case when withdrawal_existing_courses_flag=true then 1 else 0 end withdrawl,  "+ 
					" case when change_medium_flag=true then 1 else 0 end medium,  "+ 
					" case when is_change_colg_addr=true then 1 else 0 end coll_change "+   
					" from affl_college_courses_flags where coll_code='"+coll_code+"' and ac_year='2021-22')a ");*/
			
			
			
			sql = " select c.ub_name,a.coll_code,cdc_coll_name as inst_name, " +  // cdc_coll_name
					" upper(inst_address||', Dist- '||d.dist_name||', Mdl- '||m.mandal_name||', Pincode-'||inst_pincode) as inst_address, "+
" upper(d.dist_name), m.mandal_name,  coalesce(a.inst_type,'0')inst_type, inst_estd,  coalesce(society_name,'-')society_name,   "+
"  upper(society_addr||', Dist- '||dem.dist_name||', Mdl- '||mem.mandal_name||', Pincode-'||society_pincode) as society_addr,   "+
"  coalesce(u_address,'--')u_address,registrar_name,vc_name ,    "+
"  case when build_own_lease=true then 'Own' else 'Leased' end  as accomodation "+   
"  ,coalesce(a.lat_eoa_ref_no,'-')lat_eoa_ref_no,coalesce( to_char(a.lat_eop_date,'dd/mm/yyyy'),'-')lat_eop_date,coalesce(to_char(lat_go_date,'dd/mm/yyyy'),'-') lat_go_date, "+ 
"  coalesce(lat_go_ref_no,'-')lat_go_ref_no , coalesce(lat_affl_ord_ref_no,'-')lat_affl_ord_ref_no,coalesce(to_char(ffca_latest_affl_order,'dd/mm/yyyy'),'-') as ffca_latest_affl_order, "+ 
" coalesce(to_char(date_of_insp,'dd-mm-yyyy'),'-') as date_of_insp, " +
" string_agg(ref_no::text,', ') other_preceedings_ref, " + // other_preceedings,
//" case when other_preceedings=true then string_agg(','||coalesce(ref_no,'')||'''\n''') else '-' end other_preceedings_ref, "+
" is_affl_additional_sections_proceedings_ref_no||', Dated: '||to_char(is_affl_additional_sections_proceedings_date,'dd/mm/yyyy') as sections, "+ 
" affl_additional_courses_flag_proceedings_ref_no||', Dated: '||to_char(affl_additional_courses_flag_proceedings_date,'dd/mm/yyyy') as additional_courses, "+
" is_affl_existing_courses_proceedings_ref_no||', Dated: '||to_char(is_affl_existing_courses_proceedings_date,'dd/mm/yyyy') as existing_courses, "+
" permanent_affl_status_meeting20_21_flag_proceedings_ref_no||', Dated: '||to_char(permanent_affl_status_meeting20_21_flag_proceedings_date,'dd/mm/yyyy') as permanent_affl_status, "+
" withdrawal_existing_courses_flag_proceedings_ref_no||', Dated: '||to_char(withdrawal_existing_courses_flag_proceedings_date,'dd/mm/yyyy') as withdrawal_existing_courses, "+
" change_medium_flag_proceedings_ref_no||', Dated: '||to_char(change_medium_flag_proceedings_date,'dd/mm/yyyy') as change_medium, "+
" is_change_colg_addr_proceedings_ref_no||', Dated: '||to_char(is_change_colg_addr_proceedings_date,'dd/mm/yyyy') as colg_addr, " +
" gort_no,gort_no2,gort_no3,gort_no4, " +

" 'Procgs. No. '||affl_order_proc_no_2022_23||', Dated: '||to_char(affl_order_2022_23_date,'dd/mm/yyyy') as proc_no_2023,  "+
" 'Proc No. '||affl_order_proc_no_2021_22||', Dated: '||to_char(affl_order_2021_22_date,'dd/mm/yyyy') as proc_no_2022, " +
" to_char(o.created_time,'dd/mm/yyyy')cdc_created_time, to_char(gort_date,'dd/mm/yyyy') as gort_date, " +
" coalesce(to_char(gort_date2,'dd/mm/yyyy'),'-') as gort_date2, coalesce(to_char(gort_date3,'dd/mm/yyyy'),'-') as gort_date3,  " +
" coalesce(to_char(gort_date4,'dd/mm/yyyy'),'-') as gort_date4, " +
" affl_order_proc_no_2022_23, " +
" to_char(affl_order_2022_23_date,'dd/mm/yyyy') as affl_order_2022_23_date, " +
" affl_order_proc_no_2021_22, " +
" to_char(affl_order_2021_22_date,'dd/mm/yyyy') as affl_order_2021_22_date, " +
" te.enter_on as clg_appl_date, " +
" is_affl_additional_sections,affl_additional_courses_flag,is_affl_existing_courses,permanent_affl_status_meeting20_21_flag, " +
" withdrawal_existing_courses_flag,change_medium_flag,is_change_colg_addr  "+
  " from affl_inst_details  a   "+
  " left join affl_sociery_details b on a.society_slno=b.sl_no "+   
  " inner join affl_univ_mst c on c.ub_code=a.ub_code and c.ac_year=a.ac_year "+  
  " inner join pmss_districts_mst  d on d.dist_code=a.inst_dist_code   "+
  " inner join pmss_mandals_mst m on m.mandal_code=a.inst_mandal_code and m.dist_code=d.dist_code "+  
  " left join pmss_districts_mst dem on dem.dist_code=b.society_dist    "+
  " left join pmss_mandals_mst mem on mem.mandal_code=b.soc_mandal_code and mem.dist_code=b.society_dist "+  
  " inner join affl_land_2_details la on la.coll_code=a.coll_code and la.ac_year=a.ac_year   "+
  " left join affl_order_verif o on o.coll_code=a.coll_code   and a.ac_year= o.ac_year " +
  " left join affl_order_verif_other_proceedings aov on aov.coll_code=a.coll_code and aov.ac_year=a.ac_year "+
  " left join ffca_inst_details f on f.coll_code=a.coll_code and f.ac_year=a.ac_year   "+
  " left join ffca_fdbck_recmnd_details fdb on fdb.coll_code=a.coll_code and fdb.ac_year=a.ac_year "+ 
  " left join affl_college_courses_flags ccf on ccf.coll_code=a.coll_code and ccf.ac_year=a.ac_year  "+
  " left join final_affl_order_details fao on fao.college_code=a.coll_code and fao.ac_year=a.ac_year " +

 /* " left join (select college_code,ac_year,min(to_char(enter_on,'dd/mm/yyyy'))enter_on " +
  " from affl_teaching_employee_details where college_code='"+coll_code+"' group by 1,2)te on te.college_code=a.coll_code and te.ac_year=a.ac_year "+
  */
" left join (select  college_code,ac_year, max(to_char(created_time,'dd/mm/yyyy'))enter_on " +
" from affl_college_course_intake_details_20_21 where college_code='"+coll_code+"' group by 1,2)te on te.college_code=a.coll_code and te.ac_year=a.ac_year " +

  " where a.coll_code='"+coll_code+"' and a.ac_year='"+ac_year+"' group by 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,23,24,25,26,27,28,29, " +
 
  //	" o.other_preceedings, " +
  		" gort_no,gort_no2,gort_no3,gort_no4, proc_no_2023,proc_no_2022, " +
  " o.created_time,is_affl_additional_sections,affl_additional_courses_flag,is_affl_existing_courses,permanent_affl_status_meeting20_21_flag,withdrawal_existing_courses_flag,change_medium_flag,is_change_colg_addr, " +
  " gort_date, gort_date2, gort_date3, gort_date4, " +
  "affl_order_proc_no_2022_23, affl_order_2022_23_date, affl_order_proc_no_2021_22, affl_order_2021_22_date,clg_appl_date ";


			System.out.println("Affl_CdcAction2022-----GeneratePDF :::cah_data:::: "+sql);
			ArrayList<?> cah_data = gen.getListMapData(sql);

			for(int i=0 ; i<cah_data.size();i++)
			{
				 
				
				PdfPTable InnerTable1 = new PdfPTable(5);
				InnerTable1.getDefaultCell().setBorder(0);
				InnerTable1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				InnerTable1.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP);
				InnerTable1.setTotalWidth(100);
				InnerTable1.setWidths(new int[]{10,20,10,10,10});
				InnerTable1.setWidthPercentage(100);
				
				 
				
				String sql2="select affl_univ_logo_path  from affl_inst_details a left join affl_univ_mst b on a.ub_code=b.ub_code and a.ac_year=b.ac_year " +
						" where coll_code='"+coll_code+"' and a.ac_year='2022-23'";
				String logopath4=contextPath+gen.getStringfromQuery(sql2);
				
				System.out.println("logopath4::::::::"+logopath4);
				
				Image logo1= Image.getInstance(logopath4);
				logo1.setBorder(0);
				logo1.setAlignment(Element.ALIGN_TOP);
				logo1.scaleAbsolute(60,60);
				
				InnerCol =new PdfPCell(logo1);
				InnerCol.setBorder(0);
				InnerCol.setHorizontalAlignment(Element.ALIGN_LEFT);
				InnerCol.setColspan(1);
				InnerCol.setRowspan(5);
				InnerTable1.addCell(InnerCol);
				
				
				
				InnerCol =new PdfPCell(new Phrase("PROCEEDINGS OF THE "+auth_by, FontFactory.getFont(FontFactory.COURIER, 9, Font.NORMAL)));
				InnerCol.setBorder(0);
				InnerCol.setHorizontalAlignment(Element.ALIGN_CENTER);
				InnerCol.setColspan(3);
			//	InnerCol.setRowspan(5);
				InnerTable1.addCell(InnerCol);
				
				String qrdata="University :"+((HashMap)cah_data.get(i)).get("ub_name").toString()+" " +
						" \n College Code : "+((HashMap)cah_data.get(i)).get("coll_code").toString()+" " +
						" \n College Name : "+((HashMap)cah_data.get(i)).get("inst_name").toString()+"  " +
						" \n No. of Courses : "+no_of_courses+" " +
						" \n Date with Time : "+datetime+" ";
				
				ByteArrayOutputStream output=QRCode.from(qrdata).to(ImageType.PNG).stream();
				Image imageBarData= Image.getInstance(output.toByteArray());
				imageBarData.setBorder(0);
				imageBarData.setAlignment(Element.ALIGN_TOP);
				imageBarData.scaleAbsolute(60,60);
			
				InnerCol =new PdfPCell(imageBarData);
				InnerCol.setBorder(0);
				InnerCol.setHorizontalAlignment(Element.ALIGN_RIGHT);
				InnerCol.setColspan(1);
				InnerCol.setRowspan(5);
				InnerTable1.addCell(InnerCol);

				InnerCol =new PdfPCell(new Phrase(((HashMap)cah_data.get(i)).get("ub_name").toString(), FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD)));
				InnerCol.setBorder(0);
				InnerCol.setHorizontalAlignment(Element.ALIGN_CENTER);
				InnerCol.setColspan(3);
				//InnerCol.setRowspan(5);
				InnerTable1.addCell(InnerCol);

				InnerCol =new PdfPCell(new Phrase(((HashMap)cah_data.get(i)).get("u_address").toString(), FontFactory.getFont(FontFactory.COURIER, 9, Font.BOLD)));
				InnerCol.setBorder(0);
				InnerCol.setHorizontalAlignment(Element.ALIGN_CENTER);
				InnerCol.setColspan(3);
				//InnerCol.setRowspan(5);
				InnerTable1.addCell(InnerCol);

				
				if(auth_by.equals("REGISTRAR"))
				{
					InnerCol =new PdfPCell(new Phrase("PRESENT : "+((HashMap)cah_data.get(i)).get("registrar_name").toString(), FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD)));
					InnerCol.setBorder(0);
					InnerCol.setHorizontalAlignment(Element.ALIGN_CENTER);
					InnerCol.setColspan(3);
			//		InnerCol.setRowspan(5);
					InnerTable1.addCell(InnerCol);
				}
				else{
					InnerCol =new PdfPCell(new Phrase("PRESENT : "+((HashMap)cah_data.get(i)).get("vc_name").toString(), FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD)));
					InnerCol.setBorder(0);
					InnerCol.setHorizontalAlignment(Element.ALIGN_CENTER);
					InnerCol.setColspan(3);
			//		InnerCol.setRowspan(5);
					InnerTable1.addCell(InnerCol);
					
				}
				
				InnerCol =new PdfPCell(new Phrase(auth_by, FontFactory.getFont(FontFactory.COURIER, 9, Font.BOLD)));
				InnerCol.setBorder(0);
				InnerCol.setHorizontalAlignment(Element.ALIGN_CENTER);
				InnerCol.setColspan(3);
		//		InnerCol.setRowspan(5);
				InnerTable1.addCell(InnerCol);
				
				
				
				/*PdfPCell cellOne = new PdfPCell();
				cellOne = new PdfPCell(imageBarData));
				cellOne.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
				cellOne.setPadding(6);
				cellOne.setPaddingLeft(15);
				cellOne.setBorderWidthTop(1);
				cellOne.setBorderWidthBottom(1);
				cellOne.setBorderWidthLeft(1);
				cellOne.setBorderWidthRight(0);
				table.addCell(cellOne);*/

			//	InnerCol =new PdfPCell(new Phrase(" Date: "+procdate, FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD)));
				InnerCol =new PdfPCell(new Phrase(" Date: "+new SimpleDateFormat("MM/dd/yyyy").format(new Date()), FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD)));
				InnerCol.setBorder(0);
				InnerCol.setHorizontalAlignment(Element.ALIGN_RIGHT);
				InnerCol.setColspan(5);
				InnerTable1.addCell(InnerCol);
				
				InnerCol =new PdfPCell(new Phrase("(MM/DD/YYYY)", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
				InnerCol.setBorder(0);
				InnerCol.setHorizontalAlignment(Element.ALIGN_RIGHT);
				InnerCol.setColspan(5);
				InnerTable1.addCell(InnerCol);

				InnerCol =new PdfPCell(new Phrase("Procgs.No. "+((HashMap)cah_data.get(i)).get("affl_order_proc_no_2022_23").toString()+"", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD)));
		//		InnerCol =new PdfPCell(new Phrase(""+((HashMap)cah_data.get(i)).get("proc_no_2021").toString()+"" , FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD)));
				InnerCol.setBorder(0);
				InnerCol.setHorizontalAlignment(Element.ALIGN_LEFT);
				InnerCol.setColspan(5);
				
				InnerTable1.addCell(InnerCol);

				InnerCol =new PdfPCell(new Phrase("            Sub :	"+((HashMap)cah_data.get(i)).get("ub_name").toString()+" - Affiliation accorded for the academic year 2022-23 to "+((HashMap)cah_data.get(i)).get("inst_name").toString()+" , ("+((HashMap)cah_data.get(i)).get("inst_address").toString()+")  " +
						" under "+((HashMap)cah_data.get(i)).get("society_name").toString() +" , ("+((HashMap)cah_data.get(i)).get("society_addr").toString()+") - Orders - issued - regarding.", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
				InnerCol.setBorder(0);
				InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
				InnerCol.setColspan(5);
				InnerCol.setPaddingBottom(10);
				InnerTable1.addCell(InnerCol);

				
				
					 
					InnerCol =new PdfPCell(new Phrase("            Ref:  Proc No. "+((HashMap)cah_data.get(i)).get("gort_no").toString()+",  Dated: "+((HashMap)cah_data.get(i)).get("gort_date").toString()+"", FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL)));
			//		InnerCol =new PdfPCell(new Phrase("                     2. Proc No. "+((HashMap)cah_data.get(i)).get("gort_no").toString()+", "+((HashMap)cah_data.get(i)).get("gort_no2").toString()+", Dated: "+((HashMap)cah_data.get(i)).get("gort_date").toString()+"", FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL)));
					InnerCol.setBorder(0);
					InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
					InnerCol.setColspan(5);
					InnerTable1.addCell(InnerCol);
					
					
					
					if(((HashMap)cah_data.get(i)).get("gort_no2").toString()!=null && !((HashMap)cah_data.get(i)).get("gort_no2").equals("")){
						InnerCol =new PdfPCell(new Phrase("                    "+((HashMap)cah_data.get(i)).get("gort_no2").toString()+", " +
								"  Dated: "+((HashMap)cah_data.get(i)).get("gort_date2").toString()+"", FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL)));
						InnerCol.setBorder(0);
						InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
						InnerCol.setColspan(5);
						InnerTable1.addCell(InnerCol);
						}
					
					if(((HashMap)cah_data.get(i)).get("gort_no3").toString()!=null && !((HashMap)cah_data.get(i)).get("gort_no3").equals("")){
						InnerCol =new PdfPCell(new Phrase("             "+((HashMap)cah_data.get(i)).get("gort_no3").toString()+",  Dated: "+((HashMap)cah_data.get(i)).get("gort_date3").toString()+"", FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL)));
						InnerCol.setBorder(0);
						InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
						InnerCol.setColspan(5);
						InnerTable1.addCell(InnerCol);
						}
					
					if(((HashMap)cah_data.get(i)).get("gort_no4").toString()!=null && !((HashMap)cah_data.get(i)).get("gort_no4").equals("")){
						InnerCol =new PdfPCell(new Phrase("             "+((HashMap)cah_data.get(i)).get("gort_no4").toString()+",  Dated: "+((HashMap)cah_data.get(i)).get("gort_date4").toString()+"", FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL)));
						InnerCol.setBorder(0);
						InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
						InnerCol.setColspan(5);
						InnerTable1.addCell(InnerCol);
						}
					
					
					InnerCol =new PdfPCell(new Phrase("                	     Affiliation Application 2022-23 Dated: "+((HashMap)cah_data.get(i)).get("clg_appl_date").toString()+"", FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL)));
					InnerCol.setBorder(0);
					InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
					InnerCol.setColspan(5);
					InnerTable1.addCell(InnerCol);
					
					
					InnerCol =new PdfPCell(new Phrase("                	     Inspection Report 2022-23 Dated: "+((HashMap)cah_data.get(i)).get("date_of_insp").toString()+"", FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL)));
					InnerCol.setBorder(0);
					InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
					InnerCol.setColspan(5);
					InnerTable1.addCell(InnerCol);
					
					
					InnerCol =new PdfPCell(new Phrase("                	     CDC Dean Report Dated: "+((HashMap)cah_data.get(i)).get("cdc_created_time").toString()+"", FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL)));
					InnerCol.setBorder(0);
					InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
					InnerCol.setColspan(5);
					InnerTable1.addCell(InnerCol);
					
					InnerCol =new PdfPCell(new Phrase("                	     Proceedings Number of Affiliation Order 2021-22: "+((HashMap)cah_data.get(i)).get("affl_order_proc_no_2021_22").toString()+", Dated: "+((HashMap)cah_data.get(i)).get("affl_order_2021_22_date").toString()+"", FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL)));
					InnerCol.setBorder(0);
					InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
					InnerCol.setColspan(5);
					InnerTable1.addCell(InnerCol);
					
	 
			/*//		InnerCol =new PdfPCell(new Phrase("                     3. Proceedings Number of Affiliation Order 2021-22 Dated  "+((HashMap)cah_data.get(i)).get("proc_no_2021").toString()+"", FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL)));
					InnerCol =new PdfPCell(new Phrase("                	     "+((HashMap)cah_data.get(i)).get("proc_no_2021").toString()+"", FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL)));
					InnerCol.setBorder(0);
					InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
					InnerCol.setColspan(5);
					InnerTable1.addCell(InnerCol);*/
					
					/*InnerCol =new PdfPCell(new Phrase("                     4. Proceedings Number of Affiliation Order 2021-22 Dated  "+((HashMap)cah_data.get(i)).get("proc_no_2022").toString()+"", FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL)));
					InnerCol.setBorder(0);
					InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
					InnerCol.setColspan(5);
					InnerTable1.addCell(InnerCol);*/
	
					
					
					
					
			//		if(!other_preceedings.equals(""))
					/*if(((HashMap)cah_data.get(i)).get("other_preceedings").toString().equals(true))
					{
					InnerCol =new PdfPCell(new Phrase("                       6. College Application Category - Competent Authority Permissions  ( CDC dean login) "+((HashMap)cah_data.get(i)).get("date_of_insp").toString()+" ", FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL)));
					InnerCol.setBorder(0);
					InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
					InnerCol.setColspan(5);
					InnerTable1.addCell(InnerCol);
					}*/
					
					/*InnerCol =new PdfPCell(new Phrase("                              a.	Reference Number of  such Permission proceedings "+((HashMap)cah_data.get(i)).get("other_preceedings_ref").toString()+" ", FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL)));
					InnerCol.setBorder(0);
					InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
					InnerCol.setColspan(5);
					InnerTable1.addCell(InnerCol);*/
					
					/*InnerCol =new PdfPCell(new Phrase("                              b.	Date of such Permission proceedings "+((HashMap)cah_data.get(i)).get("date_of_insp").toString()+" ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
					InnerCol.setBorder(0);
					InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
					InnerCol.setColspan(5);
					InnerTable1.addCell(InnerCol);*/
					
					if(((HashMap)cah_data.get(i)).get("is_affl_additional_sections").toString().equals("true")){
					InnerCol =new PdfPCell(new Phrase("                	     "+((HashMap)cah_data.get(i)).get("sections").toString()+"" +
							                          "                              ", FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL)));
					InnerCol.setBorder(0);
					InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
					InnerCol.setColspan(5);
					InnerTable1.addCell(InnerCol);
					}
					
					if(((HashMap)cah_data.get(i)).get("affl_additional_courses_flag").toString().equals("true")){
						InnerCol =new PdfPCell(new Phrase("                	     "+((HashMap)cah_data.get(i)).get("additional_courses").toString()+"" +
														  "                              ", FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL)));
						InnerCol.setBorder(0);
						InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
						InnerCol.setColspan(5);
						InnerTable1.addCell(InnerCol);
						}
					
					if(((HashMap)cah_data.get(i)).get("is_affl_existing_courses").toString().equals("true")){
						InnerCol =new PdfPCell(new Phrase("                	     "+((HashMap)cah_data.get(i)).get("existing_courses").toString()+"" +
								                          "                              ", FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL)));
						InnerCol.setBorder(0);
						InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
						InnerCol.setColspan(5);
						InnerTable1.addCell(InnerCol);
						}
					/*if(((HashMap)cah_data.get(i)).get("permanent_affl_status_meeting20_21_flag").toString().equals("true")){
						InnerCol =new PdfPCell(new Phrase("                      Permanent Affiliation accorded by University Prior to 2021-22 and Permanent Affiliation"+"\n"+"                          status missing in digital Affiliation Order 2021-22 - "+((HashMap)cah_data.get(i)).get("permanent_affl_status").toString()+"" +
								                          "                              ", FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL)));
						InnerCol.setBorder(0);
						InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
						InnerCol.setColspan(5);
						InnerTable1.addCell(InnerCol);
						}*/
					
					if(((HashMap)cah_data.get(i)).get("withdrawal_existing_courses_flag").toString().equals("true")){
						InnerCol =new PdfPCell(new Phrase("                	     "+((HashMap)cah_data.get(i)).get("withdrawal_existing_courses").toString()+"" +
								                          "                              ", FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL)));
						InnerCol.setBorder(0);
						InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
						InnerCol.setColspan(5);
						InnerTable1.addCell(InnerCol);
						}
					
					if(((HashMap)cah_data.get(i)).get("change_medium_flag").toString().equals("true")){
						InnerCol =new PdfPCell(new Phrase("                	     "+((HashMap)cah_data.get(i)).get("change_medium").toString()+"" +
								                          "                              ", FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL)));
						InnerCol.setBorder(0);
						InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
						InnerCol.setColspan(5);
						InnerTable1.addCell(InnerCol);
						}
					
					if(((HashMap)cah_data.get(i)).get("is_change_colg_addr").toString().equals("true")){
						InnerCol =new PdfPCell(new Phrase("                	     "+((HashMap)cah_data.get(i)).get("colg_addr").toString()+"" +
								                          "                              ", FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL)));
						InnerCol.setBorder(0);
						InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
						InnerCol.setColspan(5);
						InnerTable1.addCell(InnerCol);
						}
					
					if(((HashMap)cah_data.get(i)).get("other_preceedings_ref").toString()!=null && !((HashMap)cah_data.get(i)).get("other_preceedings_ref").equals("")){
						InnerCol =new PdfPCell(new Phrase("                	     "+((HashMap)cah_data.get(i)).get("other_preceedings_ref").toString()+"", FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL)));
						InnerCol.setBorder(0);
						InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
						InnerCol.setColspan(5);
						InnerTable1.addCell(InnerCol);
						}
					
					/*if(((HashMap)cah_data.get(i)).get("other_preceedings_ref").toString()!=null && !((HashMap)cah_data.get(i)).get("other_preceedings_ref").equals("")){
					InnerCol =new PdfPCell(new Phrase("                	     Any Other Proceedings issued with respect to this college: "+((HashMap)cah_data.get(i)).get("other_preceedings_ref").toString()+"", FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL)));
					InnerCol.setBorder(0);
					InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
					InnerCol.setColspan(5);
					InnerTable1.addCell(InnerCol);
					}*/
				/*InnerCol =new PdfPCell(new Phrase("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t 5. "+((HashMap)cah_data.get(i)).get("ub_name").toString()+" - "+((HashMap)cah_data.get(i)).get("lat_go_ref_no").toString()+" and "+((HashMap)cah_data.get(i)).get("lat_go_date").toString()+", if any", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
				InnerCol.setBorder(0);
				//	InnerCol.setHorizontalAlignment(Element.ALIGN_CENTER);
				InnerCol.setColspan(5);
				InnerTable1.addCell(InnerCol);*/

				InnerCol =new PdfPCell(new Phrase("\n-oOo-", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
				InnerCol.setBorder(0);
				InnerCol.setHorizontalAlignment(Element.ALIGN_CENTER);
				InnerCol.setColspan(5);
				InnerTable1.addCell(InnerCol);

				InnerCol =new PdfPCell(new Phrase("\nORDER :-\n", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD)));
				InnerCol.setBorder(0);
				InnerCol.setHorizontalAlignment(Element.ALIGN_LEFT);
				InnerCol.setColspan(5);
				InnerTable1.addCell(InnerCol);


				InnerCol =new PdfPCell(new Phrase("                "+((HashMap)cah_data.get(i)).get("society_name").toString() +", ("+((HashMap)cah_data.get(i)).get("society_addr").toString()+" ), " +
						" has applied for the affiliation of "+((HashMap)cah_data.get(i)).get("inst_name").toString()+", " +
						" for the academic year 2022-23 to  "+((HashMap)cah_data.get(i)).get("ub_name").toString()+" " +
						" to offer its programs in the said College. ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
				InnerCol.setBorder(0);
				InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
				InnerCol.setColspan(5);
				InnerTable1.addCell(InnerCol);

				InnerCol =new PdfPCell(new Phrase("                " +
						" Considering the approval accorded by  "+accdby+"  " +
						" and after careful examination of the facts of the FFC Report, the "+auth_by+", "+((HashMap)cah_data.get(i)).get("ub_name").toString()+"  accords approval," +
						" in exercise of the powers conferred under the University Act and subject to ratification of the Executive Council, for provisional / temporary affiliation to " +
						" "+((HashMap)cah_data.get(i)).get("inst_name").toString()+" , ("+((HashMap)cah_data.get(i)).get("inst_address").toString()+") under "+((HashMap)cah_data.get(i)).get("society_name").toString() +" , ("+((HashMap)cah_data.get(i)).get("society_addr").toString()+" )," +
						" as per the details given below.", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
				InnerCol.setBorder(0);
				InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
				InnerCol.setColspan(5);
				InnerCol.setPaddingBottom(10);
				InnerTable1.addCell(InnerCol);

				/*InnerCol =new PdfPCell(new Phrase("\n", FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD)));
				InnerCol.setBorder(0);
				InnerCol.setHorizontalAlignment(Element.ALIGN_CENTER);
				InnerCol.setColspan(5);
				InnerTable1.addCell(InnerCol);*/


				InnerCol =new PdfPCell(new Phrase("Name of the College", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD)));
			// 	InnerCol.setBorder(Rectangle.BOX);
				InnerCol.setHorizontalAlignment(Element.ALIGN_LEFT);
				InnerTable1.addCell(InnerCol);

				InnerCol =new PdfPCell(new Phrase(((HashMap)cah_data.get(i)).get("inst_name").toString(), FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
			// 	InnerCol.setBorder(Rectangle.BOX);
				InnerCol.setHorizontalAlignment(Element.ALIGN_LEFT);
				InnerTable1.addCell(InnerCol);

				InnerCol =new PdfPCell(new Phrase("Name of the Society/ Trust", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD)));
			// 	InnerCol.setBorder(Rectangle.BOX);
				InnerCol.setHorizontalAlignment(Element.ALIGN_LEFT);
				InnerTable1.addCell(InnerCol);

				InnerCol =new PdfPCell(new Phrase(((HashMap)cah_data.get(i)).get("society_name").toString(), FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
			// 	InnerCol.setBorder(Rectangle.BOX);
				InnerCol.setHorizontalAlignment(Element.ALIGN_LEFT);
				InnerCol.setColspan(2);
				InnerTable1.addCell(InnerCol);

				InnerCol =new PdfPCell(new Phrase("Address of the College", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD)));
			// 	InnerCol.setBorder(Rectangle.BOX);
				InnerCol.setHorizontalAlignment(Element.ALIGN_LEFT);
				InnerTable1.addCell(InnerCol);

				InnerCol =new PdfPCell(new Phrase(((HashMap)cah_data.get(i)).get("inst_address").toString().toUpperCase(), FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
			// 	InnerCol.setBorder(Rectangle.BOX);
				InnerCol.setHorizontalAlignment(Element.ALIGN_LEFT);
				InnerTable1.addCell(InnerCol);

				InnerCol =new PdfPCell(new Phrase("Address of the Society/ Trust", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD)));
			// 	InnerCol.setBorder(Rectangle.BOX);
				InnerCol.setHorizontalAlignment(Element.ALIGN_LEFT);
				InnerTable1.addCell(InnerCol);

				InnerCol =new PdfPCell(new Phrase(((HashMap)cah_data.get(i)).get("society_addr").toString(), FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
			// 	InnerCol.setBorder(Rectangle.BOX);
				InnerCol.setHorizontalAlignment(Element.ALIGN_LEFT);
				InnerCol.setColspan(2);
				InnerTable1.addCell(InnerCol);

				InnerCol =new PdfPCell(new Phrase("Year of initial establishment of the College", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD)));
			// 	InnerCol.setBorder(Rectangle.BOX);
				InnerCol.setHorizontalAlignment(Element.ALIGN_LEFT);
				InnerTable1.addCell(InnerCol);

				InnerCol =new PdfPCell(new Phrase(((HashMap)cah_data.get(i)).get("inst_estd").toString(), FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
			// 	InnerCol.setBorder(Rectangle.BOX);
				InnerCol.setHorizontalAlignment(Element.ALIGN_LEFT);
				InnerTable1.addCell(InnerCol);

				InnerCol =new PdfPCell(new Phrase("Status of accommodation", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD)));
			// 	InnerCol.setBorder(Rectangle.BOX);
				InnerCol.setHorizontalAlignment(Element.ALIGN_LEFT);
				InnerTable1.addCell(InnerCol);

				InnerCol =new PdfPCell(new Phrase(((HashMap)cah_data.get(i)).get("accomodation").toString(), FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
			// 	InnerCol.setBorder(Rectangle.BOX);
				InnerCol.setHorizontalAlignment(Element.ALIGN_LEFT);
				InnerCol.setColspan(2);
				InnerTable1.addCell(InnerCol);

				cell = new PdfPCell(InnerTable1);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setBorder(0);
				cell.setColspan(2);
				table.addCell(cell);



			}
			
			PdfPTable InnerTable12 = new PdfPTable(1);
			InnerTable12.getDefaultCell().setBorder(0);
			InnerTable12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			InnerTable12.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP);
			InnerTable12.setTotalWidth(100);
			InnerTable12.setWidths(new int[]{100});
			InnerTable12.setWidthPercentage(100);
			
			InnerCol =new PdfPCell(new Phrase("\n", FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD)));
			InnerCol.setBorder(0);
			InnerCol.setHorizontalAlignment(Element.ALIGN_CENTER);
			InnerCol.setColspan(7);
			InnerTable12.addCell(InnerCol);

			cell = new PdfPCell(InnerTable12);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorder(0);
			cell.setColspan(2);
			table.addCell(cell);

			if(!remarks.equals(""))
			{
				InnerCol =new PdfPCell(new Phrase("   "+remarks, FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
				InnerCol.setBorder(0);
				InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
				InnerCol.setColspan(2);
				table.addCell(InnerCol);
			}
			
			PdfPTable InnerTable4 = new PdfPTable(3);
			InnerTable4.getDefaultCell().setSpaceCharRatio(2);
			InnerTable4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			InnerTable4.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP);
			InnerTable4.setTotalWidth(100);
			InnerTable4.setWidths(new int[] {10,60,30});
			InnerTable4.setWidthPercentage(100);

			InnerCol =new PdfPCell(new Phrase("                The above Provisional / Temporary / Permanent affiliation is granted subject to fulfillment of deficiencies, if any as furnished hereunder, within three months from the date of issuance of this Order. \n", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
			InnerCol.setBorder(0);
			InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			InnerCol.setColspan(3);
			InnerTable4.addCell(InnerCol);

		//	sql="  select sl_no,description  from affl_order_deficiencies_by_cdc   where coll_code='"+coll_code+"' and ac_year='"+ac_year+"' order by sl_no  ";

			sql = "select sl_no,description,to_char(last_date,'dd/mm/yyyy')last_date from affl_order_deficiencies_by_cdc " +
					" where coll_code='"+coll_code+"' and ac_year='"+ac_year+"' order by sl_no";
			
			
			System.out.println("typeee:::::::"+sql);
			ArrayList<?> cah_data4 = gen.getListMapData(sql);
			
			if(cah_data4.size()==0)
			{
				InnerCol =new PdfPCell(new Phrase(" ", FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL)));
				InnerCol.setBorder(0);
				InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
				InnerCol.setColspan(3);
				InnerTable4.addCell(InnerCol);
			}
			else
			{

				InnerCol =new PdfPCell(new Phrase("Sl. No. ", FontFactory.getFont(FontFactory.HELVETICA, 9, Font.BOLD)));
			// 	InnerCol.setBorder(Rectangle.BOX);
				InnerCol.setHorizontalAlignment(Element.ALIGN_CENTER);
				InnerTable4.addCell(InnerCol);
					
				InnerCol =new PdfPCell(new Phrase("Content of the deficiency", FontFactory.getFont(FontFactory.HELVETICA, 9, Font.BOLD)));
			// 	InnerCol.setBorder(Rectangle.BOX);
				InnerCol.setHorizontalAlignment(Element.ALIGN_CENTER);
				InnerTable4.addCell(InnerCol);
	
				InnerCol =new PdfPCell(new Phrase("Last Date for Rectification of Deficiencies", FontFactory.getFont(FontFactory.HELVETICA, 9, Font.BOLD)));
			// 	InnerCol.setBorder(Rectangle.BOX);
				InnerCol.setHorizontalAlignment(Element.ALIGN_CENTER);
				InnerTable4.addCell(InnerCol);
				
				for(int l=0 ; l<cah_data4.size();l++)
				{
					InnerCol =new PdfPCell(new Phrase(" "+(l+1), FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL)));
				// 	InnerCol.setBorder(Rectangle.BOX);
					InnerCol.setHorizontalAlignment(Element.ALIGN_LEFT);
					InnerTable4.addCell(InnerCol);
	
					InnerCol =new PdfPCell(new Phrase(((HashMap)cah_data4.get(l)).get("description").toString(), FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL)));
				// 	InnerCol.setBorder(Rectangle.BOX);
					InnerCol.setHorizontalAlignment(Element.ALIGN_LEFT);
					InnerTable4.addCell(InnerCol);
					
					InnerCol =new PdfPCell(new Phrase(((HashMap)cah_data4.get(l)).get("last_date").toString(), FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL)));
				// 	InnerCol.setBorder(Rectangle.BOX);
					InnerCol.setHorizontalAlignment(Element.ALIGN_LEFT);
					InnerTable4.addCell(InnerCol);
				}

			}
			
			cell = new PdfPCell(InnerTable4);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorder(0);
			cell.setColspan(3);
			table.addCell(cell);


			PdfPTable InnerTable2 = new PdfPTable(8);
			InnerTable2.getDefaultCell().setSpaceCharRatio(2);
			InnerTable2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			InnerTable2.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP);
			InnerTable2.setTotalWidth(100);
			InnerTable2.setWidths(new int[] {10,25,10,6,12,10,6,10});
			InnerTable2.setWidthPercentage(100);
			
			/*sql=" select row_number()  over (order by slno) slno, course_group_desc,course_id,reg_self,  " +
					" medium_code, name,  course_affl_type, course_cat_name, " +
					" course_name,graduation_level, " +
					" case when affl_nature!='0' then intake_approved_competent_authotity::varchar else '' end intake_approved_competent_authotity, univ_course_approved, " +
					" case when affl_nature!='0' then affl_nature else '' end affl_nature, " +
					" case when affl_nature!='0' then affl_valid_upto else '' end affl_valid_upto, " +
					" final_sanction_intake,final_no_section,course_deficiencies, " +
					" case when affl_nature!='0' then deficiencies_reason else '' end deficiencies_reason  " +
					" from ( " +
					" select distinct(cdc.slno), "+ 
		" a.course_id,a.reg_self, " +
		" case when cdc.medium_code is not null then cdc.medium_code else a.medium_code end medium_code, " +
		" case when mm.name is not null then mm.name else m.name end  as name," +
		" a.course_affl_type,course_group_desc,  b.course_cat_name, c.course_name,graduation_level, "+ 
		" intake_approved_competent_authotity, "+ 
		" case when univ_course_approved=true then 'Yes' else 'No' end univ_course_approved,affl_nature, to_char(cdc.affl_valid_upto,'dd/mm/yyyy')affl_valid_upto,cdc.final_sanction_intake,final_no_section, "+ 
		" case when course_deficiencies=true then 'Yes' else 'No' end course_deficiencies,deficiencies_reason " +
		" from final_affl_intake_details_2022_23 a   left join edu_course_master c on a.course_id=c.course_id  " +
		" left join course_category b on c.course_cat_id=b.course_cat_id  " +
		" left join pmss_course_group_mst gm on gm.course_group_code=b.course_group_code  " +
		" left join final_affl_intake_details_cdc cdc on a.ac_year=cdc.ac_year and a.college_code=cdc.coll_code and a.course_id=cdc.course_id  " +
		" and a.reg_self=cdc.reg_self " +
		//" and  a.medium_code=cdc.medium_code " +
		" and a.course_affl_type=cdc.course_affl_type " +
		" left join medium m on cdc.medium_code=m.medium_code " +
		" left join medium mm on cdc.medium_code=mm.medium_code "+  
		 " where cdc.coll_code='"+coll_code+"' and cdc.ac_year='"+ac_year+"'  order by course_group_desc,  b.course_cat_name, c.course_name)aa "; //and is_new_course=false  order by cdc.slno

*/
			
			sql=" select row_number()  over (order by slno) slno, course_group_desc,course_id,reg_self,  " +
					" medium_code, name,  course_affl_type, course_cat_name, " +
					" course_name,graduation_level, intake_approved_competent_authotity,univ_course_approved,affl_nature, " +
					" affl_valid_upto,final_sanction_intake,final_no_section,course_deficiencies,deficiencies_reason " +
					" from ( " +
					" select distinct(cdc.slno), "+ 
		" a.course_id,a.reg_self, " +
		" case when cdc.medium_code is not null then cdc.medium_code else a.medium_code end medium_code, " +
		" case when mm.name is not null then mm.name else m.name end  as name," +
		" a.course_affl_type,course_group_desc,  b.course_cat_name, c.course_name,graduation_level, "+ 
		" intake_approved_competent_authotity, "+ 
		" case when univ_course_approved=true then 'Yes' else 'No' end univ_course_approved,affl_nature, to_char(cdc.affl_valid_upto,'dd/mm/yyyy')affl_valid_upto,cdc.final_sanction_intake,final_no_section, "+ 
		" case when course_deficiencies=true then 'Yes' else 'No' end course_deficiencies,deficiencies_reason " +
		" from final_affl_intake_details_2022_23 a   left join edu_course_master c on a.course_id=c.course_id  " +
		" left join course_category b on c.course_cat_id=b.course_cat_id  " +
		" left join pmss_course_group_mst gm on gm.course_group_code=b.course_group_code  " +
		" left join final_affl_intake_details_cdc cdc on a.ac_year=cdc.ac_year and a.college_code=cdc.coll_code and a.course_id=cdc.course_id  " +
		" and a.reg_self=cdc.reg_self " +
		//" and  a.medium_code=cdc.medium_code " +
		" and a.course_affl_type=cdc.course_affl_type " +
		" left join medium m on cdc.medium_code=m.medium_code " +
		" left join medium mm on cdc.medium_code=mm.medium_code "+  
		 " where cdc.coll_code='"+coll_code+"' and cdc.ac_year='"+ac_year+"' and univ_course_approved=true  order by cdc.slno)aa "; //and is_new_course=false
			
			System.out.println("Affl_CdcAction2021::::course:::cah_data2::::"+sql);
			ArrayList<?> cah_data2 = gen.getListMapData(sql);
			
			if(cah_data2!=null && cah_data2.size()>0)
			{
				
				InnerCol =new PdfPCell(new Phrase("\n", FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD)));
				InnerCol.setBorder(0);
				InnerCol.setHorizontalAlignment(Element.ALIGN_CENTER);
				InnerCol.setColspan(8);
				InnerTable2.addCell(InnerCol);

			

				InnerCol =new PdfPCell(new Phrase("Course /Program", FontFactory.getFont(FontFactory.HELVETICA, 9, Font.BOLD)));
			// 	InnerCol.setBorder(Rectangle.BOX);
				InnerCol.setHorizontalAlignment(Element.ALIGN_CENTER);
				InnerTable2.addCell(InnerCol);
				
				InnerCol =new PdfPCell(new Phrase("Branch / Combination", FontFactory.getFont(FontFactory.HELVETICA, 9, Font.BOLD)));
			// 	InnerCol.setBorder(Rectangle.BOX);
				InnerCol.setHorizontalAlignment(Element.ALIGN_CENTER);
				InnerTable2.addCell(InnerCol);

			

				InnerCol =new PdfPCell(new Phrase("Medium", FontFactory.getFont(FontFactory.HELVETICA, 9, Font.BOLD)));
			// 	InnerCol.setBorder(Rectangle.BOX);
				InnerCol.setHorizontalAlignment(Element.ALIGN_CENTER);
				InnerTable2.addCell(InnerCol);
				
				InnerCol =new PdfPCell(new Phrase("Level(UG/PG)", FontFactory.getFont(FontFactory.HELVETICA, 9, Font.BOLD)));
			// 	InnerCol.setBorder(Rectangle.BOX);
				InnerCol.setHorizontalAlignment(Element.ALIGN_CENTER);
				InnerTable2.addCell(InnerCol);
				
				InnerCol =new PdfPCell(new Phrase("Nature of Affiliation being Accorded", FontFactory.getFont(FontFactory.HELVETICA, 9, Font.BOLD)));
				// 	InnerCol.setBorder(Rectangle.BOX);
					InnerCol.setHorizontalAlignment(Element.ALIGN_CENTER);
					InnerTable2.addCell(InnerCol);

				InnerCol =new PdfPCell(new Phrase("Affiliation Valid Up to", FontFactory.getFont(FontFactory.HELVETICA, 9, Font.BOLD)));
			// 	InnerCol.setBorder(Rectangle.BOX);
				InnerCol.setHorizontalAlignment(Element.ALIGN_CENTER);
				InnerTable2.addCell(InnerCol);
				
				InnerCol =new PdfPCell(new Phrase("Intake", FontFactory.getFont(FontFactory.HELVETICA, 9, Font.BOLD)));
				// 	InnerCol.setBorder(Rectangle.BOX);
					InnerCol.setHorizontalAlignment(Element.ALIGN_CENTER);
					InnerTable2.addCell(InnerCol);
					
					InnerCol =new PdfPCell(new Phrase("Deficiencies (If any)", FontFactory.getFont(FontFactory.HELVETICA, 9, Font.BOLD)));
					// 	InnerCol.setBorder(Rectangle.BOX);
						InnerCol.setHorizontalAlignment(Element.ALIGN_CENTER);
						InnerTable2.addCell(InnerCol);

				for(int j=0 ; j<cah_data2.size();j++)
				{
	
					InnerCol =new PdfPCell(new Phrase(((HashMap)cah_data2.get(j)).get("course_cat_name").toString(), FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL)));
				// 	InnerCol.setBorder(Rectangle.BOX);
					InnerCol.setHorizontalAlignment(Element.ALIGN_LEFT);
					InnerTable2.addCell(InnerCol);
					
					InnerCol =new PdfPCell(new Phrase(((HashMap)cah_data2.get(j)).get("course_name").toString(), FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL)));
				// 	InnerCol.setBorder(Rectangle.BOX);
					InnerCol.setHorizontalAlignment(Element.ALIGN_LEFT);
					InnerTable2.addCell(InnerCol);
	
					InnerCol =new PdfPCell(new Phrase(((HashMap)cah_data2.get(j)).get("name").toString(), FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL)));
				// 	InnerCol.setBorder(Rectangle.BOX);
					InnerCol.setHorizontalAlignment(Element.ALIGN_LEFT);
					InnerTable2.addCell(InnerCol);
					
					InnerCol =new PdfPCell(new Phrase(((HashMap)cah_data2.get(j)).get("graduation_level").toString(), FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL)));
				// 	InnerCol.setBorder(Rectangle.BOX);
					InnerCol.setHorizontalAlignment(Element.ALIGN_LEFT);
					InnerTable2.addCell(InnerCol);
					
					InnerCol =new PdfPCell(new Phrase(((HashMap)cah_data2.get(j)).get("affl_nature").toString(), FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL)));
					// 	InnerCol.setBorder(Rectangle.BOX);
						InnerCol.setHorizontalAlignment(Element.ALIGN_LEFT);
						InnerTable2.addCell(InnerCol);
					
					InnerCol =new PdfPCell(new Phrase(((HashMap)cah_data2.get(j)).get("affl_valid_upto").toString(), FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL)));
				// 	InnerCol.setBorder(Rectangle.BOX);
					InnerCol.setHorizontalAlignment(Element.ALIGN_LEFT);
					InnerTable2.addCell(InnerCol);
					
					InnerCol =new PdfPCell(new Phrase(((HashMap)cah_data2.get(j)).get("final_sanction_intake").toString(), FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL)));
				// 	InnerCol.setBorder(Rectangle.BOX);
					InnerCol.setHorizontalAlignment(Element.ALIGN_CENTER);
					InnerTable2.addCell(InnerCol);
						
					InnerCol =new PdfPCell(new Phrase(((HashMap)cah_data2.get(j)).get("deficiencies_reason").toString(), FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL)));
				// 	InnerCol.setBorder(Rectangle.BOX);
					InnerCol.setHorizontalAlignment(Element.ALIGN_LEFT);
					InnerTable2.addCell(InnerCol);
				}
			}
			
			cell = new PdfPCell(InnerTable2);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorder(0);
			cell.setColspan(2);
			table.addCell(cell);

			document.add(table);
			document.newPage();
			
			table = new PdfPTable(2);
			table.getDefaultCell().setBorder(0);
			table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			table.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP);
			table.setTotalWidth(100);
			table.setWidthPercentage(100);
			table.getDefaultCell().setSpaceCharRatio(3);
			//document.newPage();
			
			InnerCol =new PdfPCell(new Phrase("\n                The above provisional / temporary affiliation is granted subject to compliance of other conditions as furnished hereunder from the date of issuance of this Order. ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
			InnerCol.setBorder(0);
			InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			InnerCol.setColspan(2);
			table.addCell(InnerCol);

			InnerCol =new PdfPCell(new Phrase("1  These orders shall be in force for a period of one academic year as indicated in the subject, if the College is permitted to function in rented building." +
					" The management should renew their affiliation in each academic year until permanent affiliation is granted. ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
			InnerCol.setBorder(0);
			InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			InnerCol.setColspan(2);
			table.addCell(InnerCol);

			InnerCol =new PdfPCell(new Phrase("2  The University has the authority and reserves the right to withdraw the affiliation, at any point of time, on any action of the Management /" +
					" College which damages or causes disrepute to the Government / University and other instrumentality of the State / Central Government. ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
			InnerCol.setBorder(0);
			InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			InnerCol.setColspan(2);
			table.addCell(InnerCol);

			InnerCol =new PdfPCell(new Phrase("3  The Management / College shall fulfill the deficiencies within a period of three months and send compliance along with documentary evidence to the University," +
					" failing which, the University has authority to withdraw its affiliation. ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
			InnerCol.setBorder(0);
			InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			InnerCol.setColspan(2);
			table.addCell(InnerCol);

			InnerCol =new PdfPCell(new Phrase("4  The management should obtain prior approval from the concerned authority for establishment of the College and offering courses etc. before applying for " +
					"the affiliation,failing which the affiliation accorded is deemed to be cancelled without any further notice.  ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
			InnerCol.setBorder(0);
			InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			InnerCol.setColspan(2);
			table.addCell(InnerCol);

			InnerCol =new PdfPCell(new Phrase("5  The management shall follow the rules issued by the Government / Competent Authority from time to time on all aspects related to the administration of the college. ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
			InnerCol.setBorder(0);
			InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			InnerCol.setColspan(2);
			table.addCell(InnerCol);

			InnerCol =new PdfPCell(new Phrase("6  The staff appointed should be qualified as per the rules and regulations issued by UGC or concerned Body. ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
			InnerCol.setBorder(0);
			InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			InnerCol.setColspan(2);
			table.addCell(InnerCol);

			InnerCol =new PdfPCell(new Phrase("7  Infrastructure like Buildings, Land, Playground, Lab equipment, Library, Furniture etc., should be well maintained for the effective functioning of the College.", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
			InnerCol.setBorder(0);
			InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			InnerCol.setColspan(2);
			table.addCell(InnerCol);

			InnerCol =new PdfPCell(new Phrase("8  The balance of Corpus Fund, if applicable, should be deposited as per rules in force.", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
			InnerCol.setBorder(0);
			InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			InnerCol.setColspan(2);
			table.addCell(InnerCol);

			InnerCol =new PdfPCell(new Phrase("9  The management should not change the Programs / Combinations, intake and media  etc. sanctioned in these proceedings, failing which, such action will be construed as tampering " +
					"of records for which the University will initiate necessary legal action against the Management.", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
			InnerCol.setBorder(0);
			InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			InnerCol.setColspan(2);
			table.addCell(InnerCol);

			InnerCol =new PdfPCell(new Phrase("10  The Management shall neither make any excess admissions without approval of the concerned competent authority and the university nor make admissions without affiliation of the University", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
			InnerCol.setBorder(0);
			InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			InnerCol.setColspan(2);
			table.addCell(InnerCol);

			InnerCol =new PdfPCell(new Phrase("11  The Management shall publish prospectus of its programs offered and all other institutional details along with fee prescribed.", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
			InnerCol.setBorder(0);
			InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			InnerCol.setColspan(2);
			table.addCell(InnerCol);

			InnerCol =new PdfPCell(new Phrase("12  If the management is permitted to start the College in a rented building, they should construct permanent building for the College in the same mandal within a period of five years.", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
			InnerCol.setBorder(0);
			InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			InnerCol.setColspan(2);
			table.addCell(InnerCol);

			InnerCol =new PdfPCell(new Phrase("13  The accommodation shown for the College should not be used for any other purpose other than running the college for which permission is given by such Body and the University.", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
			InnerCol.setBorder(0);
			InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			InnerCol.setColspan(2);
			table.addCell(InnerCol);

			InnerCol =new PdfPCell(new Phrase("14  The management should not make any claim from the Government or instrumentality of the Government for any grant-in-aid either now or in future, if the College is offering unaided Programs.", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
			InnerCol.setBorder(0);
			InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			InnerCol.setColspan(2);
			table.addCell(InnerCol);

			InnerCol =new PdfPCell(new Phrase("15  The Management (a) should not shift the College to any other place; (b) should not change the name of the College, (c) should not transfer the management of the College and (d) should not convert Women’s College to Co-Education and vice-versa (e) shall not mortgage its assets without prior permission of such Body and the university. ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
			InnerCol.setBorder(0);
			InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			InnerCol.setColspan(2);
			table.addCell(InnerCol);

			InnerCol =new PdfPCell(new Phrase("16  The management of the College shall set up a grievance and complaints cell as per the Supreme Court / UGC guidelines for dealing with sexual harassment / ragging related cases and other grievances etc.", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
			InnerCol.setBorder(0);
			InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			InnerCol.setColspan(2);
			table.addCell(InnerCol);

			InnerCol =new PdfPCell(new Phrase("17  The management should establish a website for the college and keep all the necessary academic and administrative information in the website.", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
			InnerCol.setBorder(0);
			InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			InnerCol.setColspan(2);
			table.addCell(InnerCol);

			InnerCol =new PdfPCell(new Phrase("18  The University reserves the right to conduct surprise checks at any time. The University also reserves the right to cancel affiliation of the college at any stage," +
					" if it is found that the documents produced by the management are false, interpolated and improper and the management is not able to run the college as per the norms of the Government " +
					"and academic stipulations of the University, apart from any defaults/ irregularities committed which would attract legal proceedings under Law.", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
			InnerCol.setBorder(0);
			InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			InnerCol.setColspan(2);
			table.addCell(InnerCol);

			InnerCol =new PdfPCell(new Phrase("19  The Management or any other person of the Management of the College shall not collect any capitation fee and the same is prohibited as per AP Capitation fee Act 1983.", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
			InnerCol.setBorder(0);
			InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			InnerCol.setColspan(2);
			table.addCell(InnerCol);

			InnerCol =new PdfPCell(new Phrase("20  The Management shall not allow malpractices in the examination and the same is prohibited.", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
			InnerCol.setBorder(0);
			InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			InnerCol.setColspan(2);
			table.addCell(InnerCol);

			InnerCol =new PdfPCell(new Phrase("21  The Management shall ensure the non-availability of carbonated beverages and junk foods around 200 meters of the premises.", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
			InnerCol.setBorder(0);
			InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			InnerCol.setColspan(2);
			table.addCell(InnerCol);

			InnerCol =new PdfPCell(new Phrase("22  The Management shall not allow the sale of tobacco products in the premises of the College and the same is prohibited.", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
			InnerCol.setBorder(0);
			InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			InnerCol.setColspan(2);
			table.addCell(InnerCol);

			InnerCol =new PdfPCell(new Phrase("23  The Management shall take measures to safeguard the interests of the students without any prejudice to their caste, creed, religion, language, ethnicity, gender and disability.", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
			InnerCol.setBorder(0);
			InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			InnerCol.setColspan(2);
			table.addCell(InnerCol);

			InnerCol =new PdfPCell(new Phrase("24  The Management shall get the College accreditated by the State / Central Government agencies, after passing out of two batches or six years whichever is earlier, if the College has not accredited.", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
			InnerCol.setBorder(0);
			InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			InnerCol.setColspan(2);
			table.addCell(InnerCol);

			InnerCol =new PdfPCell(new Phrase("25  The management of the College shall not affiliate itself to any University outside the State of Andhra Pradesh and run the courses of other State University in their premises under Section 21-A of A.P. Education Act 1982, failing which, the Managements shall face criminal action under Section 21-A of AP Education Act.", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
			InnerCol.setBorder(0);
			InnerCol.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			InnerCol.setColspan(2);
			table.addCell(InnerCol);

			PdfPTable InnerTable5 = new PdfPTable(3);
			InnerTable5.getDefaultCell().setSpaceCharRatio(2);
			InnerTable5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			InnerTable5.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP);
			InnerTable5.setTotalWidth(100);
			InnerTable5.setWidths(new int[] {30,30,30});
			InnerTable5.setWidthPercentage(100);
			
			InnerCol =new PdfPCell(new Phrase("Sd/-", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
			InnerCol.setBorder(0);
			InnerCol.setHorizontalAlignment(Element.ALIGN_RIGHT);
			InnerCol.setColspan(3);
			InnerCol.setPaddingRight(10);
			InnerTable5.addCell(InnerCol);
			
			InnerCol =new PdfPCell(new Phrase(auth_by, FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
			InnerCol.setBorder(0);
			InnerCol.setHorizontalAlignment(Element.ALIGN_RIGHT);
			InnerCol.setColspan(3);
			InnerCol.setPaddingRight(5);
			InnerTable5.addCell(InnerCol);
			
			InnerCol =new PdfPCell(new Phrase("/ By Order / \n\n", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
			InnerCol.setBorder(0);
			InnerCol.setHorizontalAlignment(Element.ALIGN_CENTER);
			InnerCol.setColspan(3);
			InnerTable5.addCell(InnerCol);
			
			/*InnerCol =new PdfPCell(new Phrase("REGISTRAR", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
			InnerCol.setBorder(0);
			InnerCol.setHorizontalAlignment(Element.ALIGN_RIGHT);
			InnerCol.setColspan(3);
			InnerTable5.addCell(InnerCol);*/
			
			InnerCol =new PdfPCell(new Phrase("To \n The Secretary/Correspondent \n "+((HashMap)cah_data.get(0)).get("society_name").toString()+" for  \n "+((HashMap)cah_data.get(0)).get("inst_name").toString()+"", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
			InnerCol.setBorder(0);
			InnerCol.setHorizontalAlignment(Element.ALIGN_LEFT);
			InnerCol.setColspan(3);
			InnerTable5.addCell(InnerCol);
			

			cell = new PdfPCell(InnerTable5);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorder(0);
			cell.setColspan(2);
			table.addCell(cell);
			
			
			InnerCol =new PdfPCell(new Phrase("\n\n ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
			InnerCol.setBorder(0);
			InnerCol.setHorizontalAlignment(Element.ALIGN_LEFT);
			InnerCol.setColspan(2);
			table.addCell(InnerCol);
			
			InnerCol =new PdfPCell(new Phrase("Copy to:- ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
			InnerCol.setBorder(0);
			InnerCol.setHorizontalAlignment(Element.ALIGN_LEFT);
			InnerCol.setColspan(2);
			table.addCell(InnerCol);
			
			InnerCol =new PdfPCell(new Phrase("1. The Secretary, AP State Council of Higher Education,", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
			InnerCol.setBorder(0);
			InnerCol.setHorizontalAlignment(Element.ALIGN_LEFT);
			InnerCol.setColspan(2);
			table.addCell(InnerCol);
			
			InnerCol =new PdfPCell(new Phrase("2. The Dean, College Development Council of the University", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
			InnerCol.setBorder(0);
			InnerCol.setHorizontalAlignment(Element.ALIGN_LEFT);
			InnerCol.setColspan(2);
			table.addCell(InnerCol);
			
			InnerCol =new PdfPCell(new Phrase("3. The Controller of Examinations of the University ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
			InnerCol.setBorder(0);
			InnerCol.setHorizontalAlignment(Element.ALIGN_LEFT);
			InnerCol.setColspan(2);
			table.addCell(InnerCol);
			
			InnerCol =new PdfPCell(new Phrase("4. The Commissioner, Social Welfare, Samkshemabhavan, Govt., of Andhra Pradesh", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
			InnerCol.setBorder(0);
			InnerCol.setHorizontalAlignment(Element.ALIGN_LEFT);
			InnerCol.setColspan(2);
			table.addCell(InnerCol);
			
			InnerCol =new PdfPCell(new Phrase("5. The Commissioner, BC Welfare, Samkshemabhavan, Govt., of Andhra Pradesh, ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
			InnerCol.setBorder(0);
			InnerCol.setHorizontalAlignment(Element.ALIGN_LEFT);
			InnerCol.setColspan(2);
			table.addCell(InnerCol);
			
			InnerCol =new PdfPCell(new Phrase("6. The Commissioner, Tribal Welfare, Samkshemabhavan, Govt., of Andhra Pradesh", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
			InnerCol.setBorder(0);
			InnerCol.setHorizontalAlignment(Element.ALIGN_LEFT);
			InnerCol.setColspan(2);
			table.addCell(InnerCol);
			
			InnerCol =new PdfPCell(new Phrase("7. The Peshi of the Vice-Chancellor of the University – for perusal of the Vice-Chancellor", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
			InnerCol.setBorder(0);
			InnerCol.setHorizontalAlignment(Element.ALIGN_LEFT);
			InnerCol.setColspan(2);
			table.addCell(InnerCol);
			
			InnerCol =new PdfPCell(new Phrase("8. The EC Meetings Section – to place the agenda on the affiliations granted to the Colleges", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
			InnerCol.setBorder(0);
			InnerCol.setHorizontalAlignment(Element.ALIGN_LEFT);
			InnerCol.setColspan(2);
			table.addCell(InnerCol);
			
			InnerCol =new PdfPCell(new Phrase("9. Affiliation Section – for spiral binding of the affiliation orders of that academic year  ", FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL)));
			InnerCol.setBorder(0);
			InnerCol.setHorizontalAlignment(Element.ALIGN_LEFT);
			InnerCol.setColspan(2);
			table.addCell(InnerCol);
			
			document.add(table);
			
			
			

			document.close();
			writer.close();


			status=true;
			int res=1;
			if(res>0)
			{      
				sql="update final_affl_order_details set final_pdf_path=? where college_code='"+coll_code.trim()+"'  and ac_year='"+ac_year+"'  ";
				System.out.println("SQL---------Generate PDF method---------------->"+sql);
				sqlValues=new ArrayList<String>();
				sqlValues.add("Uploads/AffiliationOrder_2022/"+fc.sanitize(file_name)+"");
				System.out.println("values ::::::Uploads/AffiliationOrder_2022/"+fc.sanitize(file_name));
				//System.out.println("update sql::::::"+sql);


				int res1=gen.pstmtExecuteUpdate(sql, sqlValues);
				System.out.println("res1------------------->"+res1);
				if(res1>0)
				{
					String contextpath=request.getRealPath("") + "//" ;
					System.out.println("contextpath:::::::::::::::::::::::::::::::::::::>>>>>>"+contextpath);
					String base64data=getBS64(contextpath+"/Uploads/AffiliationOrder_2022/"+file_name.trim());
					f.setBeanProperties("urls", base64data);
					/*sql="select dsk_id from user_dsk_info where user_id=? and dsk_status=true";
					String args4[]={fc.sanitize((String) request.getSession().getAttribute("userid"))};
					  */ 
					f.setBeanProperties("sign_sno", "17000969");   
					//request.setAttribute("pro_digi_base", base64data);
					request.setAttribute("pdf_success", "pdf_success");
					request.setAttribute("path", "Uploads/AffiliationOrder_2022/"+file_name);
				}
			}    
			else
			{
				request.setAttribute("failure", "No Data Found");  
			}


		}           
		catch(Exception e)
		{
			e.printStackTrace();
			status=false;
		}
		return status;
	}


	public static boolean createDirectories(String path) {
		boolean flag = false;
		File dirName = new File(path);
		if (!(dirName.exists())) {
			dirName.mkdirs();
			flag = true;
		}
		return flag;
	}	

	public static String getBS64(String uploadform)
	{
		
		System.out.println("in :::: getBS64 :::::");
		File originalFile = new File(uploadform);
		String encodedBase64 = null;
		try {
			FileInputStream fileInputStreamReader = new FileInputStream(originalFile);
			byte[] bytes = new byte[(int)originalFile.length()];
			fileInputStreamReader.read(bytes);
			encodedBase64 = new String(Base64.encodeBytes(bytes));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return encodedBase64;
	}
	public ActionForward saveDSK(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws Exception 
	{
		System.out.println("in :::: saveDSK :::::");
		
		HttpSession session = request.getSession(true);
		String userid = (String) session.getAttribute("userid");
		String dist_code=(String)session.getAttribute("dist_code");
		String role=(String)session.getAttribute("user_gr_code");
		
			
		ArrayList<String> sqlValues = new ArrayList<String>();
		CommonForm fb = (CommonForm) form;
		String ac_year="2022-23";
		
		String coll_code= fb.getBeanProperties("coll_code").toString();
		System.out.println("coll_code::::::"+coll_code);
		
		String cond="";
		if (userid == null || userid.equalsIgnoreCase("null")|| userid.equals("")) 
		{
			request.setAttribute("msg", "Please Login to Continue");

		} 
		else
		{
			try
			{ 
				ArrayList sqls = new ArrayList();
				System.out.println("save dsk---------&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&------>");
			
				String path=fc.sanitize(request.getParameter("path"));   
				CommonForm f = (CommonForm) form;
				String contextpath=request.getRealPath("") + "//" ;   
				GeneralQueries gen= new GeneralQueries();
				
				String path1="",sql="";
				sql="select final_pdf_path From final_affl_order_details where college_code='"+coll_code+"'  and ac_year='"+ac_year+"' ";
				path1 = gen.getStringfromQuery(sql);
				
				sql="update final_affl_order_details   set  type_sign='1' where college_code='"+coll_code+"'  and ac_year='"+ac_year+"' ";
				sqls.add(sql);
				
				sql = " update pmss_colleges_registration set mc_status_id=13,manual_digital='M',form1=89,form2=89,form3=89,form4=89,form5=89,form6=89, "
						+ " serial_no_univ_date=now() where ac_year='"+ac_year+"'  and  coll_code='"+coll_code+"' ";
				sqls.add(sql);
				
				
				sql="  update affl_inst_details set ffca_inst_status=true, is_edit_requested=true  where coll_code='"+coll_code+"' and ac_year='"+ac_year+"' ";
				sqls.add(sql);
				
				System.out.println("Affiliation Order :: saveDSK :: "+sqls);
				
				int res=gen.executeBatch(sqls);
				if(res >= 0)
				{
					saveFileToDisk(fc.sanitize(f.getBeanProperties("signed_file").toString()),contextpath+path1);
					request.setAttribute("success", "Successfully Completed");
				}
				else
				{
					request.setAttribute("msg", "Not Completed");
				} 
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}

		return unspecified(mapping, form, request, response);
	}
	public static boolean saveFileToDisk(String baseCode, String file_path)
	{
		try
		{
			System.out.println(" in saveFileToDisk :::::::");
			BASE64Decoder decoder = new BASE64Decoder();
			byte[] decodedBytes = decoder.decodeBuffer(baseCode);

			/* baseCode.getBytes();   
	       byte[] imageBytes = Base64.decode(baseCode) ;*/
			FileOutputStream file= new FileOutputStream(file_path);
			file.write(decodedBytes);
			file.close();
			return true;
		}   
		catch (Exception e) 
		{
			e.printStackTrace();
			return false;
		}
	}
	
}
