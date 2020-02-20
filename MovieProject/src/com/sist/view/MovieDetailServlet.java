package com.sist.view;

import java.io.*;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.sist.vo.*;
import com.sist.dao.*;

@WebServlet("/MovieDetailServlet")
public class MovieDetailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out=response.getWriter();
		
		// 사용자 요청갑 받기=> request
		String mno=request.getParameter("mno");
		MovieDAO dao=MovieDAO.newInstance();
		MovieVO vo=dao.movieDetailData(Integer.parseInt(mno));
		
		out.println("<html>");
		out.println("<head>");
		out.println("<style type=text/css>");
		out.println(".row {");
		out.println("margin: 0px auto;");
		out.println("width: 1200px");
		out.println("}");
		out.println("</style>");
		out.println("</head>");
		
		out.println("<body>");
		// 리스트
		out.println("<div class=row>");
		out.println("<table class=\"table table-hover\">");
		
		out.println("<tr>");
		out.println("<td colspan=2 class=text-center><h3>"+vo.getTitle()+" 상세보기</h3></td>");
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<td width=20% class=\"text-center\" rowspan=7>");
		out.println("<img src="+vo.getPoster()+" width=100%>");
		out.println("</td>");
		out.println("<td width=80%>"+vo.getTitle()+"</td>");
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<td width=80%>평점: "+vo.getScore()+"</td>");
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<td width=80%>"+vo.getGenre()+"</td>");
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<td width=80%>"+vo.getRegdate()+" | "+vo.getTime()+","+vo.getGrade()+"</td>");
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<td width=80%>"+vo.getDirector()+"</td>");
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<td width=80%>"+vo.getActor()+"</td>");
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<td width=80% class=text-center>");
		out.println("<a href=\"javascript:history.back()\" class=\"btn btn-sm btn-danger\">목록</a>");
		out.println("</td>");
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<td colspan=2>"+vo.getStory()+"</td>");
		out.println("</tr>");
		
		out.println("</table>");
		out.println("</div>");
		
		out.println("</body>");
		out.println("</html>");
	}

}
