package com.sist.servlet;

import java.io.*;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sist.dao.*;

@WebServlet("/MusicDetail")
public class MusicDetail extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out=response.getWriter(); // 여기에 쓰면 브라우저가 알아서 읽는다
		
		String mno=request.getParameter("mno");
		
		// DAO연동
		MusicDAO dao=new MusicDAO();
		MusicVO vo=dao.musicDetailData(Integer.parseInt(mno));
		
		// 댓글 가져오기
		ArrayList<MusicReplyVO> list=dao.replyListData(Integer.parseInt(mno));
		// top5 가져오기
		ArrayList<MusicVO> topList=dao.musicTop5();
		
		out.println("<html>");
		out.println("<head>");
		out.println("<link rel=stylesheet href=\"css/bootstrap.min.css\">");
		out.println("<style type=text/css>");
		out.println(".col-md-9 {");
		out.println("margin: 0px auto;");
		out.println("width: 900px;");
		out.println("}");
		out.println("h1 {");
		out.println("text-align: center;");
		out.println("}");
		out.println("</style>");
		out.println("<script type=text/javascript src=\"http://code.jquery.com/jquery.js\"></script>");
		out.println("<script>");
		out.println("var i=0;");
		/*
		 *   변수 => 데이터형이 한개(자동지정변수)
		 *   var i=0;       ==> int
		 *   var i=10.5;    ==> double
		 *   var i="Hello"; ==> String
		 *   var i=[]       ==> 배열
		 *   var i={}       ==> 객체
		 */
		out.println("$(function(){"); // window.onload()
		out.println("$('#ubtn').click(function(){");
		out.println("if(i==0) {");
		out.println("$('#ubtn').val('취소');");
		out.println("i=1;");
		out.println("}");
		out.println("else {");
		out.println("$('#ubtn').val('수정');");
		out.println("i=0;");
		out.println("}");
		out.println("});");
		out.println("});");
		out.println("</script>");
		out.println("</head>");
		
		out.println("<body>");
		// container-fluid : 전체화면, container : 여백 사용
		out.println("<div class=container-fluid>");
		
		out.println("<h1>&lt;"+vo.getTitle()+"&gt;상세보기</h1>");
		out.println("<div class=col-md-9>");
		out.println("<table class=\"table table-bordered\">");
		
		out.println("<tr>");
		out.println("<td colspan=2 class=text-center>");
		out.println("<embed src=\"http://youtube.com/embed/"+vo.getKey()+"\" width=800 height=350>");
		out.println("</td>");
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<td width=10% class=text-right>");
		out.println("노래명");
		out.println("</td>");
		out.println("<td width=90% class=text-left>");
		out.println(vo.getTitle());
		out.println("</td>");
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<td width=10% class=text-right>");
		out.println("가수명");
		out.println("</td>");
		out.println("<td width=90% class=text-left>");
		out.println(vo.getSinger());
		out.println("</td>");
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<td width=10% class=text-right>");
		out.println("앨범");
		out.println("</td>");
		out.println("<td width=90% class=text-left>");
		out.println(vo.getAlbum());
		out.println("</td>");
		out.println("</tr>");
		
		out.println("<tr>");
		out.println("<td colspan=2 class=text-right>");
		out.println("<a href=\"MusicList\" class=\"btn btn-lg btn-success\">목록</a>");
		out.println("</td>");
		out.println("</tr>");
		
		
		out.println("</table>");
		
		out.println("<div style=\"height=20px\"></div>");
		// 댓글출력
		
		
		HttpSession session=request.getSession(); // session을 얻어온다 
		// request => session, cookie를 가지고 올 수 있다
		String id=(String)session.getAttribute("id");
				
		// MusicDAO replyListData에서 메모리 할당을 했기때문에 null은 아님
		if(list.size()<1) {
			out.println("<table class=\"table table-hover\">");
			out.println("<tr>");
			out.println("<td class=text-center>");
			out.println("<h3>댓글이 존재하지 않습니다</h3>");
			out.println("</td>");
			out.println("</tr>");
			out.println("</table>");
		}
		else {
			out.println("<table class=\"table table-hover\">");
			for(MusicReplyVO rvo:list) {
				out.println("<tr>");
				out.println("<td class=text-left>");
				String temp="";
				if(rvo.getSex().equals("남자"))
					temp="m1.png";
				else
					temp="w1.png";
				out.println("<img src=\"image/"+temp+"\" width=25 height=25>");
				out.println("&nbsp;"+rvo.getName()+"("+rvo.getDbDay()+")");
				out.println("</td>");
				out.println("<td class=text-right>");
				/*
				 *   String id=null;
				 *   if(id.equals(rvo.getId())) ==> 오류
				 *   if(rvo.getId().equals(id)) ==> null 값이 있는것을 뒤로 뺀다
				 */
				// 본인만 수정,삭제 가능
				if(rvo.getId().equals(id)) {
					out.println("<input type=button class=\"btn btn-xs btn-primary\" value=수정 id=ubtn data="+rvo.getNo()+">");
					// ReplyDelete => no: 삭제할때, mno: detail로 이동하기 위해 필요
					out.println("<a href=\"ReplyDelete?no="+rvo.getNo()+"&mno="+mno+"\" class=\"btn btn-xs btn-danger\">삭제</a>");
				}
				out.println("</td>");
				out.println("</tr>");
				
				out.println("<tr>");
				// white-space:pre-wrap => 크기를 벗어나면 자동으로 줄바꿈
				out.println("<td colspan=2 class=text-left><pre style=\"white-space:pre-wrap; border:none; background:white\">"+rvo.getMsg()+"</pre></td>");
				out.println("</tr>");
				
				out.println("<tr id=\"m"+rvo.getNo()+"\" style=\"display:none\">"); // 감추기
				out.println("<td colspan=2>");
				out.println("<textarea row=3 cols=80 style=\"float:left\" required name=msg>"+rvo.getMsg()+"</textarea>");
				out.println("<input type=hidden value="+mno+" name=mno>");
				out.println("<input type=submit value=댓글수정 style=\"height:50px;float:left\" class=\"btn btn-primary\">");
				out.println("</td>");
				out.println("</tr>");
				
			}
			out.println("</table>");
		}
		
		
		// 댓글입력
		// null => 저장이 안된상태 => Login이 안된 상태(null), Login이 된 상태(등록 id)
		// 로그인이 된 상태만 댓글쓰기 가능
		if(id!=null) {
			out.println("<form method=post action=\"ReplyInsert\">");
			out.println("<table class=\"table table-striped\">");
			out.println("<tr>");
			out.println("<td>");
			out.println("<textarea row=3 cols=80 style=\"float:left\" required name=msg></textarea>");
			out.println("<input type=hidden value="+mno+" name=mno>");
			out.println("<input type=submit value=댓글쓰기 style=\"height:50px;float:left\" class=\"btn btn-primary\">");
			out.println("</td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("</form>");
		}
		
		out.println("</div>");
		
		out.println("<div class=col-sm-3>");
		out.println("<table class=\"table table-striped\">");
		out.println("<caption>인기순위 5</caption>"); // 테이블 제목
		for(MusicVO tvo:topList) {
			out.println("<tr>");
			out.println("<td>"+tvo.getRank()+"</td>");
			out.println("<td>");
			out.println("<img src=\""+tvo.getPoster()+"\" width=35 height=35>");
			out.println("</td>");
			out.println("<td>"+tvo.getTitle()+"</td>");
			out.println("</tr>");
		}
		out.println("</table>");
		out.println("</div>");
		out.println("</div>");
		out.println("</body>");
		out.println("</html>");
	}

}
