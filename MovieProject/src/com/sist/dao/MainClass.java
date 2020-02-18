package com.sist.dao;
import java.util.*;
import com.sist.dao.*;
import com.sist.vo.*;

public class MainClass {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MovieDAO dao=MovieDAO.newInstance();
		ArrayList<MovieVO> list=dao.movieListData(1, 2);
		for(MovieVO vo:list) {
			System.out.println(vo.getMno()+" "+vo.getTitle());
		}
	}

}
