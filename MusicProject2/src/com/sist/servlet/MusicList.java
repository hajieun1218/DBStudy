package com.sist.servlet;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;
import com.sist.dao.*;

@WebServlet("/MusicList")
public class MusicList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out=response.getWriter();
		
		/*
		 *    http://localhost/MusicProject2/MusicList       ==> null
		 *    http://localhost/MusicProject2/MusicList?page= ==> ""
		 */
		String strPage=request.getParameter("page");
		if(strPage==null)
			strPage="1";
		int curpage=Integer.parseInt(strPage);
		
		// DAO연결
		MusicDAO dao=new MusicDAO();
		ArrayList<MusicVO> list=dao.musicListData(curpage);
		
		int totalpage=dao.musicTotalPage();
		
		out.println("<html>");
		out.println("<head>");
		out.println("<link rel=stylesheet href=\"css/bootstrap.min.css\">");
		out.println("<style type=text/css>");
		out.println(".row {");
		out.println("margin: 0px auto;");
		out.println("width: 800px;");
		out.println("}");
		out.println("h1 {");
		out.println("text-align: center;");
		out.println("}");
		out.println("</style>");
		out.println("</head>");
		
		out.println("<body>");
		out.println("<div class=container>");
		out.println("<h1>Music Top200</h1>");
		out.println("<div class=row>");
		out.println("<table class=\"table table-hover\">");
		out.println("<tr class=danger>");
		out.println("<th width=10% class=text-center>순위</th>");
		out.println("<th width=10% class=text-center>등폭</th>");
		out.println("<th width=20% class=text-center></th>");
		out.println("<th width=35%>노래명</th>");
		out.println("<th width=25%>가수명</th>");
		out.println("</tr>");
		
		for(MusicVO vo:list) {
			
			String temp="";
			if(vo.getState().equals("상승")) {
				temp="<font color=red>▲</font>&nbsp;"+vo.getIdcrement();
			}
			else if(vo.getState().equals("하강")) {
				temp="<font color=blue>▼</font>&nbsp;"+vo.getIdcrement();
			}
			else {
				temp="<font color=gray>-</font>";
			}
			
			out.println("<tr>");
			out.println("<td width=10% class=text-center>"+vo.getRank()+"</td>");
			out.println("<td width=10% class=text-center>"+temp+"</td>");
			out.println("<td width=20% class=text-center><img src="+vo.getPoster()+" width=35 height=35 class=img-circle></td>");
			out.println("<td width=35%><a href=\"MusicDetail?mno="+vo.getMno()+"\">"+vo.getTitle()+"</a></td>");
			out.println("<td width=25%>"+vo.getSinger()+"</td>");
			out.println("</tr>");
		}
		
		out.println("</table>");
		
		out.println("<table class=\"table\">");
		out.println("<tr>");
		out.println("<td class=text-center>");
		out.println("<a href=\"MusicList?page="+(curpage>1?curpage-1:curpage)+"\" class=\"btn btn-sm btn-success\">이전</a>");
		out.println(curpage+" page / "+totalpage+" pages");
		out.println("<a href=\"MusicList?page="+(curpage<totalpage?curpage+1:curpage)+"\" class=\"btn btn-sm btn-info\">다음</a>");
		out.println("</td>");
		out.println("</tr>");
		out.println("</table>");
		
		out.println("</div>");
		out.println("</div>");
		out.println("</body>");
		out.println("</html>");
	}

}
