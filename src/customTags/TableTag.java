package customTags;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import plugins.DatabasePlugin;


public class TableTag extends SimpleTagSupport{
	public String styleId;
	public String styleClass;
	public String style;
	public String border;
	public String sql;
	public String var;
	public String index;
	public List<Map<String,Object>>data;
	
	public List<Map<String, Object>> getData() {
		return data;
	}
	public void setData(List<Map<String, Object>> data) {
		this.data = data;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public String getVar() {
		return var;
	}
	public void setVar(String var) {
		this.var = var;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public String getStyleId() {
		return styleId;
	}
	public void setStyleId(String styleId) {
		this.styleId = styleId;
	}
	public String getStyleClass() {
		return styleClass;
	}
	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public String getBorder() {
		return border;
	}
	public void setBorder(String border) {
		this.border = border;
	}
	
	public void doTag() {
		JspWriter out = getJspContext().getOut();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String table = "<table";
			if(this.border != null)
				table += " border=\""+border+"\"";
			if(this.style != null)
				table += " style=\""+style+"\"";
			if(this.styleId != null)
				table += " id=\""+styleId+"\"";
			if(this.styleClass != null)
				table += " class=\""+styleClass+"\"";
			table += ">";
			out.print(table);
			if(sql != null && !sql.equals("")) {
				System.out.println("sql =========== " + sql);
				con = DatabasePlugin.connect();
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
			}else {
				getJspBody().invoke(null);
			}
			out.print("</table>");
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			DatabasePlugin.closeConnection(con, ps, rs, null);
		}
	}
}
