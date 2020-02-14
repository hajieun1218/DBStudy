package com.sist.dao;
import java.util.*;
import java.sql.*;

public class MusicDAO {
	
	private Connection conn;
	private PreparedStatement ps;
	private final String URL="jdbc:oracle:thin:@localhost:1521:XE";
	
	// 1. 드라이버 등록
	public MusicDAO() {
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
	
	// 기능
	
	// 데이터 목록 출력
	// rank, state, idcrement, poster, title, singer, album
	public ArrayList<MusicVO> musicListData(int page) {
		ArrayList<MusicVO> list=new ArrayList<MusicVO>();
		try {
			getConnection();
			String sql="SELECT rank,state,idcrement,poster,title,singer,album "
					  +"FROM music_genie "
					  +"ORDER BY rank ASC";
			ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			int rowSize=50;
			int pageStart=(page*rowSize)-rowSize;
			int i=0; // 50개씩 나눠줌
			int j=0;
			while(rs.next()) {
				if(i<rowSize && j>=pageStart) {
					MusicVO vo=new MusicVO();
					vo.setRank(rs.getInt(1));
					vo.setState(rs.getString(2));
					vo.setIdcrement(rs.getInt(3));
					vo.setPoster(rs.getString(4));
					vo.setTitle(rs.getString(5));
					vo.setSinger(rs.getString(6));
					vo.setAlbum(rs.getString(7));
					list.add(vo);
					i++;
				}
				j++;
			}
			rs.close();
		} catch(Exception ex) {
			System.out.println(ex.getMessage());
		} finally {
			disConnection();
		}
		return list;
	}
	
	// 총페이지
	public int musicTotalPage() {
		int total=0;
		try {
			getConnection();
			String sql="SELECT CEIL(COUNT(*)/50.0) FROM music_genie";
			ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			rs.next();
			total=rs.getInt(1);
			rs.close();
		} catch(Exception ex) {
			System.out.println(ex.getMessage());
		} finally {
			disConnection();
		}
		return total;
	}
	
	// 상세보기
	// rank, state, idcrement, title, singer, poster, key
	public MusicVO musicDetailData(int no) {
		MusicVO vo=new MusicVO();
		try {
			getConnection();
			String sql="SELECT rank,state,idcrement,title,singer,poster,key "
					  +"FROM music_genie "
					  +"WHERE rank=?";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, no);
			ResultSet rs=ps.executeQuery();
			rs.next();
			vo.setRank(rs.getInt(1));
			vo.setState(rs.getString(2));
			vo.setIdcrement(rs.getInt(3));
			vo.setTitle(rs.getString(4));
			vo.setSinger(rs.getString(5));
			vo.setPoster(rs.getString(6));
			vo.setKey(rs.getString(7));
			rs.close();
		} catch(Exception ex) {
			System.out.println(ex.getMessage());
		} finally {
			disConnection();
		}
		return vo;
	}
}
