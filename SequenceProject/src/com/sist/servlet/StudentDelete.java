package com.sist.servlet;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.sist.student.dao.*;

@WebServlet("/delete.do")
public class StudentDelete extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String hakbun=request.getParameter("hakbun");
		StudentDAO dao=new StudentDAO();
		dao.stdDelete(Integer.parseInt(hakbun));
		
		response.sendRedirect("list.do"); //페이지이동
	}

}
