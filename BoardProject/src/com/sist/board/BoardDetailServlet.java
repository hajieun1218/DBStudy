package com.sist.board;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.sist.dao.*;

@WebServlet("/BoardDetailServlet")
public class BoardDetailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8"); // 브라우저에 html을 보내는데 한글로 읽어라
		PrintWriter out=response.getWriter(); // 브라우저가 읽어가는 메모리 위치
		
		String no=request.getParameter("no");
		// 오라클 => 데이터를 가지고 온다
		BoardDAO dao=new BoardDAO();
		BoardVO vo=dao.boardDetailData(Integer.parseInt(no));
		
		out.println("<html>");
		out.println("<head>");
		out.println("<link rel=stylesheet href=\"css/table.css\">");
		out.println("</head>");
		out.println("<body>");
		out.println("<center>");
		out.println("<h1>내용보기</h1>");
		
		out.println("<table id=\"table_content\" width=700>");
		out.println("<tr>");
		out.println("<th width=20%>번호</th>");  // th는 가운데 정렬
		out.println("<td width=30% align=center>"+vo.getNo()+"</td>"); // td는 왼쪽정렬
		out.println("<th width=20%>작성일</th>"); 
		out.println("<td width=30% align=center>"+vo.getRegdate()+"</td>"); 
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<th width=20%>이름</th>");  
		out.println("<td width=30% align=center>"+vo.getName()+"</td>");
		out.println("<th width=20%>조회수</th>"); 
		out.println("<td width=30% align=center>"+vo.getHit()+"</td>"); 
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<th width=20%>제목</th>");  
		out.println("<td colspan=3>"+vo.getSubject()+"</td>");
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<td colspan=4 align=left valign=top height=200>"+vo.getContent()+"</td>");
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<td colspan=4 align=right>");
		out.println("<a href=\"BoardUpdate?no="+vo.getNo()+"\">수정</a>&nbsp;");
		out.println("<a href=\"BoardDelete?no="+vo.getNo()+"\">삭제</a>&nbsp;");
		out.println("<a href=\"BoardListServlet\">목록</a>");
		out.println("</td>");
		out.println("</tr>");
		
		out.println("</table>");
	}

}
