package com.sist.board;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sist.dao.BoardDAO;
import com.sist.dao.BoardVO;


@WebServlet("/BoardDelete")
public class BoardDelete extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out=response.getWriter();
		
		// http://localhost/BoardProject/BoardDelete?no=12
		String no=request.getParameter("no");
		
		
		out.println("<!DOCTYPE html>"); // html5.0 => required 사용하기 위해 //생략하면 4.0 
		out.println("<html>");
		out.println("<head>");
		out.println("<link rel=stylesheet href=\"css/table.css\">");
		out.println("</head>");
		out.println("<body>");
		out.println("<center>");
		out.println("<h1>삭제하기</h1>");
		
		// submit하려면 form태그 필요
		// method=post ===> doPost로
		out.println("<form method=post action=BoardDelete>"); 
		out.println("<table id=\"table_content\" width=300>");
		/*
		 *    submit버튼 => input, select, textarea => 입력된것만 넘어감
		 *    	===> no는 넘어가지 않음 
		 *      ===> hidden 사용
		 */
		
		// name => 자바에서 받을 목적으로
		// id, class => 디자인
		out.println("<tr>");
		//th => center, td => left
		out.println("<th width=35% align=right>비밀번호</th>");
		out.println("<td width=65%>");
		out.println("<input type=password name=pwd size=15 required>");
		out.println("<input type=hidden name=no value="+no+">");
		out.println("</td>");
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<td colspan=2 align=center>");
		out.println("<input type=submit value=삭제하기>");
		out.println("<input type=button value=취소 onclick=\"javascript:history.back()\">");
		out.println("</td>");
		out.println("</tr>");
		
		out.println("</table>");
		out.println("</center>");
		out.println("</body>");
		out.println("</html>");
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out=response.getWriter();
		
		// 저장된 값 => DAO
		String no=request.getParameter("no");
		String pwd=request.getParameter("pwd");

		// 처리 ==> DAO
		BoardDAO dao=new BoardDAO();
		boolean bCheck=dao.boardDelete(Integer.parseInt(no), pwd);
		
		// 이동 ==> 상세보기
		if(bCheck) {
			response.sendRedirect("BoardListServlet");
		}
		else {
			out.println("<html>");
			out.println("<head>");
			out.println("<script type=\"text/javascript\">");
			out.println("alert(\"비밀번호가 틀립니다!!!\")"); // 메시지창
			out.println("history.back();"); // 확인 누르면 이전창으로
			out.println("</script>");
			out.println("</head>");
			out.println("</html>");
		}
	}

}
