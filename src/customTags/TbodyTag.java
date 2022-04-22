package customTags;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

import javax.servlet.jsp.JspWriter;

import plugins.DatabasePlugin;


public class TbodyTag extends TableTag{
	public void doTag() {
		JspWriter out = getJspContext().getOut();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		con = DatabasePlugin.connect();
		try {
			String prepareTag = "<tbody";
			if(border != null)
				prepareTag += " border=\""+border+"\"";
			if(style != null)
				prepareTag += " style=\""+style+"\"";
			if(styleId != null)
				prepareTag += " id=\""+styleId+"\"";
			if(styleClass != null)
				prepareTag += " class=\""+styleClass+"\"";
			prepareTag += ">";
			out.print(prepareTag);
			if(sql != null && !sql.equals("")) {
				System.out.println("sql =========== " + sql);
				
				ps = con.prepareStatement(sql);
				rs = ps.executeQuery();
				data = DatabasePlugin.processResultSet(rs);
				if(data != null && data.size() > 0) {
					int count = 0;
					for(Map<String,Object>m : data) {
						getJspContext().setAttribute(index, count++);
						getJspContext().setAttribute(var, m);
						getJspBody().invoke(null);
					}
				}
			}
			
			//getJspBody().invoke(null);
			out.print("</tbody>");
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			DatabasePlugin.closeConnection(con, ps, rs, null);
		}
	}
}
