package com.sist.student.dao;

import java.util.*;
import java.sql.*;
public class StudentDAO {

	private Connection conn;
	private PreparedStatement ps;
	private final String URL="jdbc:oracle:thin:@localhost:1521:XE";
	
	// 1. 드라이버 등록
	public StudentDAO() {
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
	}
	// 2. 연결
	public void getConnection()
	{
		try 
		{
			conn=DriverManager.getConnection(URL,"hr","happy");
		} catch (Exception ex) {}
	}
	// 3. 연결 해제 
	public void disConnection()
	{
		try 
		{
			if(ps!=null) ps.close();
			if(conn!=null) conn.close();
		} catch (Exception ex) {}
	}
	
	// 목록
	public ArrayList<StudentVO> stdAllData(int page) {
		ArrayList<StudentVO> list=new ArrayList<StudentVO>();
		try {
			getConnection();
			String sql="SELECT hakbun,name,kor,eng,math "
					  +"FROM student "
					  +"ORDER BY hakbun ASC";
			/*
			 * 		<서브쿼리 페이징 기법`>
			 * 	  String sql="SELECT hakbun,name,kor,eng,math,num "
					  +"FROM (SELECT hakbun,name,kor,eng,math,rownum as num "
					  +"FROM (SELECT hakbun,name,kor,eng,math "
					  +"FROM student ORDER BY 1)) "
					  +"WHERE num BETWEEN 1 AND 10";
			 */
			ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			
			int i=0;
			int j=0; // while문 돌아가는 회수
			int pageStart=(page*10)-10;
			
			while(rs.next()) {
				if(i<10 && j>=pageStart) {
					StudentVO vo=new StudentVO();
					vo.setHakbun(rs.getInt("hakbun")); // 숫자, 컬럼명 가능 (함수사용 ==> 숫자로)
					vo.setName(rs.getString("name"));  // mybatis는 컬럼명으로
					vo.setKor(rs.getInt("kor"));
					vo.setEng(rs.getInt("eng"));
					vo.setMath(rs.getInt("math"));
					list.add(vo);
					i++;
				}
				j++;
			}
			rs.close();
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			disConnection();
		}
		return list;
	}
	
	public void stdInsert(StudentVO vo) {
		try {
			getConnection();
			String sql="INSERT INTO student(hakbun,name,kor,eng,math,sex) "
					  +"VALUES(std_hakbun_seq.nextval,?,?,?,?,?)";
			ps=conn.prepareStatement(sql);
			ps.setString(1, vo.getName());
			ps.setInt(2, vo.getKor());
			ps.setInt(3, vo.getEng());
			ps.setInt(4, vo.getMath());
			ps.setString(5, vo.getSex());
			
			ps.executeUpdate();
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			disConnection();
		}
	}
	
	public int stdRowCount() {
		int count=0;
		try {
			getConnection();
			String sql="SELECT COUNT(*) FROM student";
			ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			rs.next();
			count=rs.getInt(1);
			rs.close();
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			disConnection();
		}
		return count;
	}
	
	public void stdDelete(int hakbun) {
		try {
			getConnection();
			String sql="DELETE FROM student "
					  +"WHERE hakbun=?";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, hakbun);
			
			ps.executeUpdate();
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			disConnection();
		}
	}
}
