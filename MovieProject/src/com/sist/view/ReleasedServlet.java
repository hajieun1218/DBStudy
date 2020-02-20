package com.sist.view;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import com.sist.vo.*;
import com.sist.dao.*;

// 현재상영
@WebServlet("/ReleasedServlet")
public class ReleasedServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out=response.getWriter();
		
		String strPage=request.getParameter("page");
		if(strPage==null)
			strPage="1";
		int curpage=Integer.parseInt(strPage);
		
		// DAO 연결
		MovieDAO dao=MovieDAO.newInstance();
		ArrayList<MovieVO> list=dao.movieListData(curpage, 1);
		
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
		out.println("<div class=row>");
		
		for(MovieVO vo:list) {
			String title=vo.getTitle();
			String title2="";
			if(title.length()>20) {
				title2=title.substring(0,20)+"...";
			}
			else {
				title2=title;
			}
				
			out.println("<div class=\"col-md-3\">");
			out.println("<div class=\"thumbnail\">");
			out.println("<a href=\"MainServlet?mode=7&mno="+vo.getMno()+"\">");
			out.println("<img src=\""+vo.getPoster()+"\" alt=\"Lights\" style=\"width:100%\">");
			out.println("</a>");
			out.println("<div class=\"caption\">");
			out.println("<p>"+title2+"</p>");
			out.println("<p><small>네티즌&nbsp;</small><font color=red>★"+vo.getScore()+"</font></p>");
			out.println("<p><font color=gray><sup>"+vo.getRegdate()+"</sup></font></p>");
			out.println("</div>");
			out.println("</div>");
			out.println("</div>");
		}
		out.println("</div>");
		
		out.println("<div class=\"row text-center\">");
		out.println("<ul class=\"pagination pagination-lg\">");
		// mode는 MainServlet에서 받고, page는 ReleasedServlet에서 받는다
		out.println("<li><a href=\"MainServlet?mode=1&page=1\">1</a></li>"); 
		out.println("<li><a href=\"MainServlet?mode=1&page=2\">2</a></li>");
		out.println("</ul>");
		out.println("</div>");
		
		
		out.println("</body>");
		out.println("</html>");
		
		
		
	}

}
