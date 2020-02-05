package com.sist.dao;
// 오라클 연결
import java.util.*;
import java.sql.*;

public class MusicDAO {

	private Connection conn; // 오라클 연결
	private PreparedStatement ps; // SQL문장 전송
	// SELECT, INSERT, UPDATE, DELETE => 오라클에서 코딩 VS 자바에서 SQL
	// SELECT * FROM emp WHERE ename LIKE '%A%'   - 오라클
	// SELECT * FROM emp WHERE ename LIKE '%'||A||'%'   - 자바
	
	// 주소
	private final String URL="jdbc:oracle:thin:@localhost:1521:XE";
	
	// 드라이버 등록
	public MusicDAO() {
		try { 
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// 리플렉션 => 클래스의 이름을 읽어와서 메모리 할당
			// new, Class.forName ==> 메모리 할당  ==> Spring (클래스 이름만 넘겨주면 메모리 할당)
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	// 연결
	public void getConnection() {
		try {
			conn=DriverManager.getConnection(URL,"hr","happy");
		} catch (Exception ex) {}
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
	
	// 기능 => 저장 ==> AutoCommit(false)
	public void musicInsert(MusicVO vo) {
		try { 
			getConnection();
			String sql="INSERT INTO music_genie VALUES("
					  +"(SELECT NVL(MAX(mno)+1,1) FROM music_genie),?,?,?,?,?,?,?,?)"; // 전송
			ps=conn.prepareStatement(sql);
			// 값 채우기
			ps.setInt(1, vo.getRank());
			ps.setString(2, vo.getTitle());
			ps.setString(3, vo.getSinger());
			ps.setString(4, vo.getAlbum());
			ps.setString(5, vo.getPoster());
			ps.setInt(6, vo.getIdcrement());
			ps.setString(7, vo.getState());
			ps.setString(8, vo.getKey());
			
			// 실행
			ps.executeUpdate(); // commit()
			
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			disConnection();
		}
	}
	
	public ArrayList<MusicVO> musicListData() {
		ArrayList<MusicVO> list=new ArrayList<MusicVO>();
		try {
			getConnection();
			String sql="SELECT mno,title,singer,poster,album,idcrement,state FROM music_genie "
					  +"ORDER BY rank ASC";
			ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			while(rs.next()) {
				MusicVO vo=new MusicVO();
				vo.setMno(rs.getInt(1));
				vo.setTitle(rs.getString(2));
				vo.setSinger(rs.getString(3));
				vo.setPoster(rs.getString(4));
				vo.setAlbum(rs.getString(5));
				vo.setIdcrement(rs.getInt(6));
				vo.setState(rs.getString(7));
				
				list.add(vo);
			}
			rs.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			disConnection();
		}
		return list;
		
	}
}
