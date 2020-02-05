package com.sist.dao;
// 오라클연동  ==> SQL(오라클 실행) ==> Java
/*
 * 	  1. 드라이버 등록
 * 	  2. 연결
 * 	  3. 해제
 *    4. 기능설정 ==> SQL전송, 결과값 받기
 *    			   ======
 *    	DML
 *    	 = SELECT : 데이터 검색
 *    		1) 형식
 *     			SELECT *|column1,column2,...
 *     			FROM table명(SELECT,View)
 *     			[
 *     				WHERE 조건절(컬럼명 연산자 값)
 *     					empno = 7788
 *     					empno LIKE '%A%'
 *     				GROUP BY 컬럼명(함수)
 *     				HAVING 그룹 조건 ==> 집합함수
 *     				ORDER BY (컬럼명,함수) ASC|DESC
 *     			]
 *     
 *     			= 연산자
 *     				1. 산술연산자 => 각 개인의 통계 (+,-,*,/(실수))
 *     				2. 비교연산자 => =, !=(<>,^=), <, >, <=, >=
 *     				3. 논리연산자 => AND, OR
 *     				4. 대입연산자 => = (UPDATE)
 *     				5. BETWEEN ~ AND => 페이지 나누기, 예약기간
 *     				6. IN => 포함된 데이터를 추출 (다중 조건) => checkbox
 *     				7. NULL => 연산처리중에 NULL이 있는 경우에는 연산처리 불가능
 *     						   IS NULL, IS NOT NULL
 *     				8. NOT => 부정
 *     						  NOT LIKE, NOT IN, NOT REGEXP_LIKE
 *     
 *     			= 내장 함수
 *     				1. 단일행 함수
 *     					= 문자관련 함수
 *     						=> LENGTH : 문자 개수
 *     						=> SUBSTR : 원하는 부분 추출 ==> 시작번호 1, 문자개수
 *     						=> INSTR  : 원하는 문자의 위치
 *     						=> RPAD   : 비밀번호,아이디 찾기 => 출력
 *     					= 숫자관련 함수
 *     						=> ROUND  : 반올림
 *     						=> TRUNC  : 버림
 *     						=> CEIL   : 올림(총페이지)
 *     					= 날짜관련 함수
 *     						=> SYSDATE : 시스템 시간
 *     						=> MONTHS_BETWEEN : 기간의 개월 수
 *     					= 일반 함수
 *     						=> NVL : NULL을 원하는 데이터값 변경
 *     				2. 집합함수
 *     					= COUNT : row의 개수 (로그인, 아이디 중복체크)
 *     					= MAX   : 자동 증가번호
 *     					= SUM, AVG
 *     					= RANK, DENSE_RANK					
 *     					= CUBE, ROLLUP
 *       = INSERT : 데이터 추가
 *       = UPDATE : 데이터 수정
 *       = DELETE : 데이터 삭제
 */
// Mybatis => DAO부분이 만들어있음
import java.util.*;
import java.sql.*;
public class EmpDAO {
	
	// 연결도구 Connection (Socket)
	private Connection conn;
	// 입출력 => InputStream, OutputStream
	private PreparedStatement ps;
	// URL주소
	private final String URL="jdbc:oracle:thin:@localhost:1521:XE";
	
	// 드라이버 등록 => 한번 수행
	public EmpDAO() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	// 연결
	public void getConnection() {
		try {
			conn=DriverManager.getConnection(URL,"hr","happy");
		} catch(Exception ex) {}
	}
	
	// 해제
	public void disConnection() {
		try {
			if(ps!=null)
				ps.close();
			if(conn!=null)
				conn.close();
		} catch(Exception ex) {}
	}
	
	// 기능 수행 1
	// SELECT => 전체 데이터 읽기
	public ArrayList<EmpVO> empAllData() {
		ArrayList<EmpVO> list=new ArrayList<EmpVO>();
		try {
			// 연결
			getConnection();
			// SQL문장 제작
			String sql="SELECT * "
					  +"FROM emp "
					  +"ORDER BY empno";
			// 오라클 전송
			ps=conn.prepareStatement(sql);
			// 결과값 받기
			ResultSet rs=ps.executeQuery();
			while(rs.next()) {
				EmpVO vo=new EmpVO();
				vo.setEmpno(rs.getInt(1));
				vo.setEname(rs.getString(2));
				vo.setJob(rs.getString(3));
				vo.setMgr(rs.getInt(4));
				vo.setHiredate(rs.getDate(5));
				vo.setSal(rs.getInt(6));
				vo.setComm(rs.getInt(7));
				vo.setDeptno(rs.getInt(8));
				
				// list에 첨부
				list.add(vo); // 14개 모아서 전송
			}
			rs.close();
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			disConnection();
		}
		return list;
	}
	
	
	// SELECT => 원하는 컬럼만 읽기
	// SELECT => WHERE 문장  이용
	public EmpVO empDetailData(int empno) {
		EmpVO vo=new EmpVO();
		try {
			// 연결
			getConnection();
			// SQL문장 제작
			String sql="SELECT * "
					  +"FROM emp "
					  +"WHERE empno=?";
			// 오라클 전송
			ps=conn.prepareStatement(sql);
			ps.setInt(1, empno);
			// 결과값 받기
			ResultSet rs=ps.executeQuery();
			rs.next();
			vo.setEmpno(rs.getInt(1));
			vo.setEname(rs.getString(2));
			vo.setJob(rs.getString(3));
			vo.setMgr(rs.getInt(4));
			vo.setHiredate(rs.getDate(5));
			vo.setSal(rs.getInt(6));
			vo.setComm(rs.getInt(7));
			vo.setDeptno(rs.getInt(8));
				
			rs.close();
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			disConnection();
		}
		return vo;
	}
	
	
	public ArrayList<EmpVO> empFindData(String ename) {
		ArrayList<EmpVO> list=new ArrayList<EmpVO>();
		try {
			// 연결
			getConnection();
			// SQL문장 제작
			String sql="SELECT * "
					  +"FROM emp "
					  +"WHERE ename LIKE '%'||?||'%'";
//					  +"WHERE ename LIKE '%"+ename+"%'";
			// 오라클 전송
			ps=conn.prepareStatement(sql);
			ps.setString(1, ename);
			// 결과값 받기
			ResultSet rs=ps.executeQuery();
			while(rs.next()) {
				EmpVO vo=new EmpVO();
				vo.setEmpno(rs.getInt(1));
				vo.setEname(rs.getString(2));
				vo.setJob(rs.getString(3));
				vo.setMgr(rs.getInt(4));
				vo.setHiredate(rs.getDate(5));
				vo.setSal(rs.getInt(6));
				vo.setComm(rs.getInt(7));
				vo.setDeptno(rs.getInt(8));
				
				// list에 첨부
				list.add(vo); //  모아서 전송
			}
			rs.close();
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			disConnection();
		}
		return list;
	}
	
	
	public ArrayList<EmpVO> empRangeData(int year) {
		ArrayList<EmpVO> list=new ArrayList<EmpVO>();
		try {
			// 연결
			getConnection();
			// SQL문장 제작
			String sql="SELECT * "
					  +"FROM emp "
					  +"WHERE hiredate BETWEEN ? AND ?";
			// 오라클 전송
			ps=conn.prepareStatement(sql);
			ps.setString(1, year+"/01/01");
			ps.setString(2, year+"/12/31");
			// 결과값 받기
			ResultSet rs=ps.executeQuery();
			while(rs.next()) {
				EmpVO vo=new EmpVO();
				vo.setEmpno(rs.getInt(1));
				vo.setEname(rs.getString(2));
				vo.setJob(rs.getString(3));
				vo.setMgr(rs.getInt(4));
				vo.setHiredate(rs.getDate(5));
				vo.setSal(rs.getInt(6));
				vo.setComm(rs.getInt(7));
				vo.setDeptno(rs.getInt(8));
				
				// list에 첨부
				list.add(vo); //  모아서 전송
			}
			rs.close();
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			disConnection();
		}
		return list;
	}
	
	// SELECT => 연산자 사용 방법
	// SELECT => 함수 이용
	// SELECT => GROUP BY 
	
	
}
