package in.apcfss.struts.Utilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.struts.util.LabelValueBean;

public class CommonLabelValueBeans {
	public static ArrayList<LabelValueBean>DistrictsList(Connection con,Boolean combo){
		ArrayList<LabelValueBean>matrix = new ArrayList<LabelValueBean>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String sql = "select district_id,district_name from district_master where state_id='28' order by 2";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			if(combo)
				matrix.add(new LabelValueBean( "---SELECT---", "0"));
			if(rs != null) {
				while(rs.next()) {
					matrix.add(new LabelValueBean(rs.getString(2), rs.getString(1)));
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return matrix;
	}
}
