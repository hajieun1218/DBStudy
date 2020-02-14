package com.sist.board;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.sist.dao.*;
import java.util.*;

@WebServlet("/BoardListServlet")
public class BoardListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 *     http://localhost/BoardProject/BoardListServlet
		 *     =============================
		 *        root => WebContent
		 */
		
		// request: 요청: 사용자한테 값 보내달라고 하는 것. 즉, 사용자가 보내주는 값을 받을 때 사용. 
		// response: 응답: 사용자한테 응답할 때 사용.  ex) HTML/XML을 만들어서 사용자에게 전송할 때 사용. 
		response.setContentType("text/html; charset=UTF-8");
		// setContentType: HTML로 보낼거냐 XML로 보낼거냐 컨텐트타입을 알려줌.
		PrintWriter out=response.getWriter(); 
		// 어디에다가 저장할지. 브라우저에서 읽어가는 위치.  
		
		// 사용자가 요청한 페이지를 받는다
		String strPage=request.getParameter("page");
		if(strPage==null)
			strPage="1";
		int curPage=Integer.parseInt(strPage);
		
		BoardDAO dao=new BoardDAO();
		ArrayList<BoardVO> list=dao.boardListData(curPage);
		
		// 총페이지
		int totalPage=dao.boardTotalPage();
		
		out.println("<html>");
		out.println("<head>");
		out.println("<link rel=stylesheet href=\"css/table.css\">");
		out.println("</head>");
		out.println("<body>");
		out.println("<center>");
		out.println("<h1>자유게시판</h1>");
		
		out.println("<table id=\"table_content\" width=700>");
		out.println("<tr>");
		out.println("<td align=left>");
		out.println("<a href=\"BoardInsert\">새글</a>");
		out.println("</td>");
		out.println("</tr>");
		out.println("</table>");
		
		out.println("<table id=\"table_content\" width=700>");
		out.println("<tr>");
		out.println("<th width=10%>번호</th>");
		out.println("<th width=45%>제목</th>");
		out.println("<th width=15%>이름</th>");
		out.println("<th width=20%>작성일</th>");
		out.println("<th width=10%>조회수</th>");
		out.println("</tr>");
		// 출력 위치
		for(BoardVO vo:list) //VO에서 10번 돌리게 해놨음 
		{
			out.println("<tr class=dataTr>");
			out.println("<td width=10% align=center>"+vo.getNo()+"</td>");
			out.println("<td width=45% align=left>");
			out.println("<a href=BoardDetailServlet?no="+vo.getNo()+">");
			out.println(vo.getSubject()+"</a>");
			out.println("</td>");
			out.println("<td width=15% align=center>"+vo.getName()+"</td>");
			out.println("<td width=20% align=center>"+vo.getRegdate()+"</td>");
			out.println("<td width=10% align=center>"+vo.getHit()+"</td>");
			out.println("</tr>");
		}
		out.println("</table>");
		
		out.println("<form method=post action=BoardFind>");
		out.println("<table id=\"table_content\" width=700>");
		out.println("<tr>");
		out.println("<td align=left>");
		out.println("Search:");
		out.println("<select name=fs>");
		out.println("<option value=name>이름</option>");   // 보여주는건 이름, 넘겨주는건name
		out.println("<option value=subject>제목</option>");
		out.println("<option value=content>내용</option>");
		out.println("</select>");
		out.println("<input type=text name=ss size=15>");
		out.println("<input type=submit value=찾기>");
		out.println("</td>");
		out.println("<td align=right>");
		/*
		 *     특수문자
		 *     	&nbsp; " "(공백)
		 *     	&lt;   <
		 *     	&gt;   >
		 */
		out.println("<a href=\"BoardListServlet?page="+(curPage>1?curPage-1:curPage)+"\">&lt;이전&gt;</a>");
		out.println(curPage+" page / "+totalPage+" pages");
		out.println("<a href=\"BoardListServlet?page="+(curPage<totalPage?curPage+1:curPage)+"\">&lt;다음&gt;</a>");
		out.println("</td>");
		out.println("</tr>");
		out.println("</table>");
		
		out.println("</center>");
		out.println("</body>");
		out.println("</html>");
		// 메모리에 저장된 애를 브라우저가 읽어가서 출력함 
		// jsp에서 초록색으로 나오는 애들은 사실 앞에 out.println이 생략된 것.
		// 복잡... ==> HTML 출력할 땐 Servlet 잘 안 씀. 보안을 요구하거나 할 때만 사용함. 
		// 다만, Spring이 Servlet으로 되어 있으므로 잘 기억할 것. 
		
	}

}
