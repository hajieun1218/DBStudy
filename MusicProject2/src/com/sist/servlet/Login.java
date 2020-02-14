package com.sist.servlet;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sist.dao.*;

@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out=response.getWriter(); // getWriter : 현재 사용자가 보고있는 브라우저 위치
		
		out.println("<html>");
		out.println("<head>"); // js,css 사용하지 않으면 head 필요없음
		out.println("<link rel=stylesheet href=\"css/bootstrap.min.css\">"); // html은 .을 공백으로 인식 => ""사용
		out.println("<style type=text/css>"); // 내부 css
		out.println(".row {");
		out.println("margin: 0px auto;"); // 가운데 정렬
		out.println("width: 350px;");
		out.println("}");
		out.println("h1 {");
		out.println("text-align: center;");
		out.println("}");
		out.println("</style>");
		out.println("</head>");
		
		out.println("<body>");
		out.println("<div class=container>"); // container(여백사용), container-fluid(전체화면)
		out.println("<h1>Login</h1>");
		out.println("<div class=row>");
		out.println("<form method=post action=Login>"); // Login=>doPost
		out.println("<table class=\"table table-hover\">");
		out.println("<tr>");
		out.println("<td width=20% class=text-right>ID</td>");
		out.println("<td width=80% class=text-left><input type=text name=id size=15></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td width=20% class=text-right>Password</td>");
		out.println("<td width=80% class=text-left><input type=password name=pwd size=15></td>");
		out.println("</tr>");
		out.println("<tr>");
		/*
		 *   <버튼 크기>
		 *    xs
		 *    md
		 *    sm - 중간사이즈
		 *    lg
		 */
		out.println("<td colspan=2 class=text-center><input type=submit class=\"btn btn-sm btn-primary\" value=로그인></td>");
		out.println("</tr>");
		out.println("</table>");
		out.println("</form>");
		out.println("</div>"); // .row
		out.println("</div>"); // .container
		out.println("</body>");
		out.println("</html>");
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out=response.getWriter();
		
		// HTML => CSS,JavaScript 포함
		MusicDAO dao=new MusicDAO();
		// 사용자가 보내준 id,pwd
		String id=request.getParameter("id");
		String pwd=request.getParameter("pwd");
		
		String result=dao.isLogin(id, pwd);
		if(result.equals("NOID")) {
			out.println("<script>");
			out.println("alert(\"ID가 존재하지 않습니다\")");
			out.println("history.back();");
			out.println("</script>");
		}
		else if(result.equals("NOPWD")) {
			out.println("<script>");
			out.println("alert(\"비밀번호가 틀립니다\")");
			out.println("history.back();");
			out.println("</script>");
		}
		else {
			// request,response에 톰캣이 값을 넣어줌
			// 사용자가 요청하면 톰캣이 doGet,doPost 호출 => 콜백함수
			// 다른화면으로 바뀌면 메모리 회수(request,response는 지역변수) => 초기화
			//   => 서버에 저장하기 위해 session사용(로그아웃,브라우저껐을때,30분후에 끊어짐)  ==> 예약,장바구니
			HttpSession session=request.getSession(); // 세션 생성
			// id, name 저장
			// session은 object저장 가능 =>ArrayList저장 가능 , 쿠키는 String만 저장가능
			session.setAttribute("id", id); // 키,값 ==> Map
			session.setAttribute("name", result);
			
			// 파일이동
			response.sendRedirect("MusicList");
			
			
		}
	}

}
