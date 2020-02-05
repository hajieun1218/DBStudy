package com.sist.dao;
/*
 EMPNO                                              NUMBER            => int
 ENAME                                              VARCHAR2(10)      => String
 JOB                                                VARCHAR2(9)       => String
 MGR                                                NUMBER(4)         => int
 HIREDATE                                           DATE              => Date
 SAL                                                NUMBER(7,2)       => int
 COMM                                               NUMBER(7,2)       => int
 DEPTNO                                             NUMBER            => int
 
 
 
 		오라클 데이터형
 		문자형
 			CHAR, VARCHAR2, CLOB => String
 		숫자형
 			NUMBER => NUMBER(4),NUMBER=>int, NUMBER(7,2)=>double,int
 		날짜형
 			DATE => java.util.Date
 */
import java.util.*;
public class EmpVO {

	private int empno;
	private String ename;
	private String job;
	private int mgr;
	private Date hiredate;
	private int sal;
	private int comm;
	private int deptno;
	
	// get => 출력
	// set => 오라클에서 값 받기
	public int getEmpno() {
		return empno;
	}
	public void setEmpno(int empno) {
		this.empno = empno;
	}
	public String getEname() {
		return ename;
	}
	public void setEname(String ename) {
		this.ename = ename;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public int getMgr() {
		return mgr;
	}
	public void setMgr(int mgr) {
		this.mgr = mgr;
	}
	public Date getHiredate() {
		return hiredate;
	}
	public void setHiredate(Date hiredate) {
		this.hiredate = hiredate;
	}
	public int getSal() {
		return sal;
	}
	public void setSal(int sal) {
		this.sal = sal;
	}
	public int getComm() {
		return comm;
	}
	public void setComm(int comm) {
		this.comm = comm;
	}
	public int getDeptno() {
		return deptno;
	}
	public void setDeptno(int deptno) {
		this.deptno = deptno;
	}
	
}
