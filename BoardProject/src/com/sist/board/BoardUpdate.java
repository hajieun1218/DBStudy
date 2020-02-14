package com.sist.board;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.sist.dao.*;

@WebServlet("/BoardUpdate")
public class BoardUpdate extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out=response.getWriter();
		
		// 사용자가 요청한 번호를 받는다
		// http://localhost/BoardProject/BoardUpdate?no=13
		String no=request.getParameter("no");
		BoardDAO dao=new BoardDAO();
		BoardVO vo=dao.boardUpdateData(Integer.parseInt(no));
		
		out.println("<!DOCTYPE html>"); // html5.0 => required 사용하기 위해 //생략하면 4.0 
		out.println("<html>");
		out.println("<head>");
		out.println("<link rel=stylesheet href=\"css/table.css\">");
		out.println("</head>");
		out.println("<body>");
		out.println("<center>");
		out.println("<h1>수정하기</h1>");
		
		// submit하려면 form태그 필요
		// method=post ===> doPost로
		out.println("<form method=post action=BoardUpdate>"); 
		out.println("<table id=\"table_content\" width=500>");
		
		// name => 자바에서 받을 목적으로
		// id, class => 디자인
		out.println("<tr>");
		//th => center, td => left
		out.println("<th width=15% align=right>이름</th>");
		out.println("<td width=85%>");
		// 안에 값을 넣을 때는 value 사용
		out.println("<input type=text name=name size=15 required value="+vo.getName()+">");
		// ***hidden*** => 사용자에게 데이터를 보여주지 않고 숨겨서 데이터 전송
		out.println("<input type=hidden name=no value="+vo.getNo()+">");
		out.println("</td>");
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<th width=15% align=right>제목</th>");
		out.println("<td width=85%>");
		// 데이터에 공백이 들어가면 따옴표 붙여야한다 => 안붙이면 공백 앞까지만 가져옴
		out.println("<input type=text name=subject size=50 required value=\""+vo.getSubject()+"\">");
		out.println("</td>");
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<th width=15% align=right valign=top>내용</th>");
		out.println("<td width=85%>");
		//  textarea는 value가 아니고 태그와 태그 사이에
		out.println("<textarea rows=8 cols=55 name=content required>"+vo.getContent()+"</textarea>");
		out.println("</td>");
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<th width=15% align=right>비밀번호</th>");
		out.println("<td width=85%>");
		out.println("<input type=password name=pwd size=10 required>");
		out.println("</td>");
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<td colspan=2 align=center>");
		out.println("<input type=submit value=수정하기>");
		out.println("<input type=button value=취소 onclick=\"javascript:history.back()\">");
		out.println("</td>");
		out.println("</tr>");
		
		out.println("</table>");
		out.println("</form>");
		
		out.println("</center>");
		out.println("</body>");
		out.println("</html>");
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out=response.getWriter();
		
		// 사용자가 보내준 값을 저장
		try {
			// 넘어오는 값이 한글일때만 사용 
			request.setCharacterEncoding("UTF-8");
		} catch(Exception ex) {}
		
		// 저장된 값 => DAO
		String no=request.getParameter("no");
		String name=request.getParameter("name");
		String subject=request.getParameter("subject");
		String content=request.getParameter("content");
		String pwd=request.getParameter("pwd");

		BoardVO vo=new BoardVO();
		vo.setNo(Integer.parseInt(no));
		vo.setName(name);
		vo.setSubject(subject);
		vo.setContent(content);
		vo.setPwd(pwd);
		
		// 처리 ==> DAO
		BoardDAO dao=new BoardDAO();
		boolean bCheck=dao.boardUpdate(vo);
		
		// 이동 ==> 상세보기
		if(bCheck) {
			response.sendRedirect("BoardDetailServlet?no="+no);
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
