package com.sist.dao;
// DBCP (DataBase Connection Pool)
// 미리 Connection을 생성하고 시작한다
// 메모리 관리하기 편하다
// 속도가 빠르다

import java.util.*;
import java.sql.*;
import javax.sql.*;
import javax.naming.*;

public class EmpDAO {
	
	private Connection conn;
	private PreparedStatement ps;
	private static EmpDAO dao;
	
	// 미리 생성된 Connection 객체를 얻어온다
	/*
	 *    Connection[] conn={100,200,300,400,500};
	 *    boolean[] sw={false,false,false,false,false};
	 *    if(요청.equals("jdbc/oracle"))
	 *    {
	 *    	for(int i=0;i<5;i++)
	 *    	{
	 *    		if(
	 *    	}
	 *    }
	 */
	public void getConnection() {
		try {
			
			Context init=new InitialContext(); // JNDI (Java Naming Directory Interface) 주소가 저장되어 있는 형식이 폴더
			// 탐색기를 연다
			Context c=(Context)init.lookup("java://comp//env"); // 미리 지정되어있는 C드라이브 ==> (ex.dbDev)
			// 저장된 폴더위치로 접근
			// lookup => setLookup("별칭", 클래스 주소)  ==> 별칭으로 클래스주소를 찾는다 
			DataSource ds=(DataSource)c.lookup("jdbc/oracle"); // C드라이브에 jdbc/oracle 이름으로 된 주소 => 우리가 만든 폴더 (ex.dbStudy)
			// 실제 Connection주소 요청 => 오라클 정보와 관련된 모든 정보를 전송 (DataSource)
			conn=ds.getConnection();
			// 주소값을 넘겨받는다 
		} catch(Exception ex) {}
	}
	
	// Connection 반환
	public void disConnection() {
		try {
			if(ps!=null)
				ps.close();
			if(conn!=null)
				conn.close();
		} catch(Exception ex) {}
	}
	
	// 기능
	public ArrayList<EmpVO> empAllData() {
		ArrayList<EmpVO> list=new ArrayList<EmpVO>();
		try {
			getConnection(); // 사용할 Connection 주소를 얻어온다
//			String sql="SELECT empno,ename,job,hiredate,sal,dname,loc "
//					  +"FROM emp,dept "
//					  +"WHERE emp.deptno=dept.deptno";
			String sql="SELECT * FROM emp_dept";
			ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			while(rs.next()) {
				EmpVO vo=new EmpVO();
				vo.setEmpno(rs.getInt(1));
				vo.setEname(rs.getString(2));
				vo.setJob(rs.getString(3));
				vo.setHiredate(rs.getDate(4));
				vo.setSal(rs.getInt(5));
				vo.getDvo().setDname(rs.getString(6));
				vo.getDvo().setLoc(rs.getString(7));
				list.add(vo);
			}
			rs.close();
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			disConnection(); // 반환
		}
		return list;
	}
	
}
