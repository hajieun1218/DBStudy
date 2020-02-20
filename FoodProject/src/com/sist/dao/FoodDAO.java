package com.sist.dao;
import java.util.*;
import java.sql.*;
import javax.naming.*;
import javax.sql.*;
import com.sist.manager.*;

public class FoodDAO {

	private Connection conn;
	private PreparedStatement ps;
	private static FoodDAO dao;
	private final String URL="jdbc:oracle:thin:@localhost:1521:XE";
	
	// <dataSource type="POOLED"> ==> Mybatis에서 커넥션풀 사용할 때
	// Mybatis는 getConnection,disConnection 안함
	
	// 데이터 수집할때는 Main을 사용하여 수집하기 때문에 DBCP 사용 X => jdbc 사용
	
	// jdbc
//	public FoodDAO() {
//		try {
//			Class.forName("oracle.jdbc.driver.OracleDriver");
//		} catch(Exception ex) {
//			System.out.println(ex.getMessage());
//		}
//	}
	
	// 미리 만들어 놓고 주소값을 가져와서 사용
	public void getConnection() {
		try {
//			conn=DriverManager.getConnection(URL,"hr","happy");
			Context init=new InitialContext(); // JNDI reg ==> 탐색기 열기
			Context c=(Context)init.lookup("java://comp//env"); // 지정된 폴더이름
			DataSource ds=(DataSource)c.lookup("jdbc/oracle"); 
			conn=ds.getConnection();
			//DataSource ds=(DataSource)c.lookup("java://comp//env/jdbc/oracle"); => 한줄에 코딩 가능
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	// Connection 반환
	public void disConnection() {
		// ps,conn 닫으면 자동으로 반환
		// close => 컴파일exception => 반드시 try~catch문
		try {
			if(ps!=null)
				ps.close();
			if(conn!=null)
				conn.close();
		} catch(Exception ex) {}
		
	}
	
	// DAO를 각 사용자당 한개만 사용이 가능하게 만든다 (싱글턴패턴)
	public static FoodDAO newInstance() {
		if(dao==null)
			dao=new FoodDAO();
		return dao;
	}
	
	
	public void categoryInsert(CategoryVO vo) {
		try {
			getConnection();
			String sql="INSERT INTO category VALUES(?,?,?,?,?)";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, vo.getCateno());
			ps.setString(2, vo.getTitle());
			ps.setString(3, vo.getSubject());
			ps.setString(4, vo.getPoster());
			ps.setString(5, vo.getLink());
			
			ps.executeUpdate();
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			disConnection();
		}
	}
	
	public void foodHouseInsert(FoodHouseVO vo) {
		try {
			getConnection();
			String sql="INSERT INTO foodhouse VALUES("
					  +"foodhouse_no_seq.nextval,?,?,?,?,"
					  +"?,?,?,?,?,?,?,'none')";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, vo.getCno());
			ps.setString(2, vo.getTitle());
			ps.setDouble(3, vo.getScore());
			ps.setString(4, vo.getAddress());
			ps.setString(5, vo.getTel());
			ps.setString(6, vo.getType());
			ps.setString(7, vo.getPrice());
			ps.setString(8, vo.getImage());
			ps.setInt(9, vo.getGood());
			ps.setInt(10, vo.getSoso());
			ps.setInt(11, vo.getBad());
			
			ps.executeUpdate();
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			disConnection();
		}
	}
	
	
	public ArrayList<CategoryVO> categoryAllData() {
		ArrayList<CategoryVO> list=new ArrayList<CategoryVO>();
		try {
			getConnection();
			String sql="SELECT cateno,title,subject,poster "
					  +"FROM category "
					  +"ORDER BY cateno ASC";
			ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			while(rs.next()) {
				CategoryVO vo=new CategoryVO();
				vo.setCateno(rs.getInt(1));
				vo.setTitle(rs.getString(2));
				vo.setSubject(rs.getString(3));
				vo.setPoster(rs.getString(4));
				list.add(vo);
			} 
			rs.close();
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			disConnection();
		}
		return list;
	}
}
