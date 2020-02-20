package com.sist.view;

import java.io.*;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sist.dao.MovieDAO;
import com.sist.vo.NewsVO;

// 뉴스
@WebServlet("/NewsServlet")
public class NewsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out=response.getWriter();
		
		/*
		 * 	  request => String getParameter() => 하나씩 받는다
		 * 				 String[] getParameterValues() => 여러개 동시에 받는다 (체크박스)
		 */
		// 사용자 요청값 받기 => request
		String strPage=request.getParameter("page");
		if(strPage==null)
			strPage="1";
		int curpage=Integer.parseInt(strPage);
		MovieDAO dao=MovieDAO.newInstance();
		ArrayList<NewsVO> list=dao.NewsListData(curpage);
		int totalpage=dao.newsTotalPage();
		
		
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
		out.println("<table class\"table table-hover\">");
		out.println("<tr>");
		out.println("<td>");
		for(NewsVO vo:list) {
			out.println("<table class=\"table table-hover\">");
			out.println("<tr>");
			out.println("<td width=30% class=text-center rowspan=3>");
			out.println("<img src="+vo.getPoster()+" width=100%>");
			out.println("</td>");
			out.println("<td class=text-center width=70%><b><a href="+vo.getLink()+">"+vo.getTitle()+"</a></b></td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td width=70%>"+vo.getContent()+"</td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td width=70%>"+vo.getAuthor()+"・"+vo.getRegdate()+"</td>");
			out.println("</tr>");
			out.println("</table>");
		}
		out.println("</td>");
		out.println("</tr>");
		out.println("</table>");
		out.println("</div>");
		
		// 페이지
		out.println("<div class=\"row text-center\">");
		out.println("<ul class=\"pagination pagination-lg\">");
		// mode는 MainServlet에서 받고, page는 ReleasedServlet에서 받는다
		for(int i=1;i<=totalpage;i++) {
			out.println("<li><a href=\"MainServlet?mode=3&page="+i+"\">"+i+"</a></li>"); 
		}
		out.println("</ul>");
		out.println("</div>");	
		
		out.println("</body>");
		out.println("</html>");
	}

}
