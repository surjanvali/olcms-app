package in.apcfss.struts.eCourt.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class JsonApplication {
	public static void main(String[] args) throws Exception {
		Connection connection = null;
		try {
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection("jdbc:postgresql://localhost/apolcms", "apolcms", "apolcms");
			connection.setAutoCommit(false);

			/*
			 * PreparedStatement respPS = connection.prepareStatement(
			 * "INSERT INTO dept_data (sno, cino, party_no, res_name, address) VALUES " +
			 * " (?, ?, ?, ?, ?);"); prepareData(respPS); respPS.executeBatch();
			 */
			System.out.println("Start:" + (new Date()));
			PreparedStatement prayerPS = connection.prepareStatement(
					"INSERT INTO nic_prayer_data (cino, asreg_case, reg_no, reg_year, subnature1_desc, prayer, case_status) VALUES "
							+ " (?, ?, ?, ?, ?, ?, ?);");
			preparePrayerData(prayerPS);
			prayerPS.executeBatch();

			connection.commit();
			//connection.setAutoCommit(true);
			System.out.println("Finished:" + (new Date()));

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (connection != null)
				connection.close();
		}
	}

	private static void prepareData(PreparedStatement preparedStatement) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			List<Dept> deptList = objectMapper.readValue(
					new File("C:\\Users\\dell\\Desktop\\HighCourt Prayers data\\dept_pend_res_add-copy.json"),
					new TypeReference<List<Dept>>() {
					});

			for (Dept c : deptList) {
				int i = 0;
				preparedStatement.setInt(++i, i);
				preparedStatement.setString(++i, c.getCino());
				preparedStatement.setLong(++i, c.getPartyNo());
				preparedStatement.setString(++i, c.getResName());
				preparedStatement.setString(++i, c.getAddress());
				preparedStatement.addBatch();
			}

		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}

	private static int preparePrayerData(PreparedStatement preparedStatement) {
		ObjectMapper objectMapper = new ObjectMapper();
		int records = 0;
		try {
			List<PrayerData> prayerDataList = objectMapper.readValue(
					//new File("E:\\olcmsdata\\dept_pending_prayer.json"),
					new File("E:\\olcmsdata\\dept_disposed_prayer.json"),
					new TypeReference<List<PrayerData>>() {
					});

			for (PrayerData c : prayerDataList) {
				int i = 0;
				/*System.out.println(""+c.getCino());
				System.out.println(""+ c.getAsreg_case());
				System.out.println(""+c.getReg_no());
				System.out.println(""+ c.getReg_year());
				System.out.println(""+ c.getSubnature1_desc());
				System.out.println(""+ c.getPrayer());*/
				
				//preparedStatement.setInt(++i, i);
				preparedStatement.setString(++i, c.getCino());
				preparedStatement.setString(++i, c.getAsreg_case());
				preparedStatement.setString(++i, c.getReg_no());
				preparedStatement.setString(++i, c.getReg_year());
				preparedStatement.setString(++i, c.getSubnature1_desc());
				preparedStatement.setString(++i, c.getPrayer());
				// preparedStatement.setString(++i, "PENDING");
				preparedStatement.setString(++i, "DISPOSED");
				preparedStatement.addBatch();
				records++;
			}

		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
		return records;
	}

	static class PrayerData {
		public PrayerData() {

		}

		public PrayerData(String cino, String asreg_case, String reg_no, String reg_year, String subnature1_desc,
				String prayer) {
			this.cino = cino;
			this.asreg_case = asreg_case;
			this.reg_no = reg_no;
			this.reg_year = reg_year;
			this.subnature1_desc = subnature1_desc;
			this.prayer = prayer;
		}

		private String cino;
		private String asreg_case;
		private String reg_no;
		private String reg_year;
		private String subnature1_desc;
		private String prayer;

		public String getCino() {
			return cino;
		}

		public void setCino(String cino) {
			this.cino = cino;
		}

		public String getAsreg_case() {
			return asreg_case;
		}

		public void setAsreg_case(String asreg_case) {
			this.asreg_case = asreg_case;
		}

		public String getReg_no() {
			return reg_no;
		}

		public void setReg_no(String reg_no) {
			this.reg_no = reg_no;
		}

		public String getReg_year() {
			return reg_year;
		}

		public void setReg_year(String reg_year) {
			this.reg_year = reg_year;
		}

		public String getSubnature1_desc() {
			return subnature1_desc;
		}

		public void setSubnature1_desc(String subnature1_desc) {
			this.subnature1_desc = subnature1_desc;
		}

		public String getPrayer() {
			return prayer;
		}

		public void setPrayer(String prayer) {
			this.prayer = prayer;
		}

	}

	static class Dept {
		public Dept() {

		}

		public Dept(String cino, long partyNo, String resName, String address) {
			this.cino = cino;
			this.partyNo = partyNo;
			this.resName = resName;
			this.address = address;
		}

		private String cino;
		@JsonProperty("party_no")
		private long partyNo;
		@JsonProperty("res_name")
		private String resName;
		private String address;

		public String getCino() {
			return cino;
		}

		public void setCino(String cino) {
			this.cino = cino;
		}

		public long getPartyNo() {
			return partyNo;
		}

		public void setPartyNo(long partyNo) {
			this.partyNo = partyNo;
		}

		public String getResName() {
			return resName;
		}

		public void setResName(String resName) {
			this.resName = resName;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}
	}
}
