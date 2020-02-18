package com.sist.manager;

import java.text.SimpleDateFormat;
//파일 읽기
import java.util.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/*
 *  Element  : 내가 가져와야하는 태그 이름  ==> MovieVO
 *  Elements : 같은 태그 여러개                  ==> ArrayList
 */

import com.sist.vo.MovieVO;
import com.sist.vo.NewsVO;
import com.sist.dao.*;
/*
 * <div class="info_tit">
	   <em class="ico_movie ico_15rating">15세이상관람가</em>
	  <a href="/moviedb/main?movieId=122091" class="name_movie #title">남산의 부장들</a>
   </div>
 */

/*
 *   <div class="a">
 *     <p>aaaa</p>
 *     <a href="bbbb">bbb</a>
 *   </div>
 *   
 *   div.a p    => text()        ==> aaaa
 *   div.a a    => attr("href")  ==> bbbb
 *   div.a      => text()        ==> aaaa bbb
 *                 html()        ==> <p>aaaa</p>
 *                                   <a href="bbbb">bbb</a>
 */
public class MovieManager {

	MovieDAO dao=MovieDAO.newInstance();
	public ArrayList<MovieVO> movieListData() {
		ArrayList<MovieVO> list = new ArrayList<MovieVO>();
		
		try {
			int k=1;
			//MovieDAO dao=new MovieDAO();
//			for (int i = 1; i <= 7; i++) 
			{
				// 영화 목록
				Document doc = Jsoup.connect("https://movie.daum.net/boxoffice/yearly").get();
				Elements link = doc.select("div.info_tit a"); // a태그 여러개
				for (int j = 0; j < link.size(); j++) {
					try {
						Element elem = link.get(j); // a태그 한개
						// 태그 안에있는 내용 읽을 때는 attr, 태그 밖(텍스트)은 text
//					    System.out.println(i+"-"+elem.attr("href"));

						// 상세페이지
						String mLink = "https://movie.daum.net" + elem.attr("href");
						Document doc2 = Jsoup.connect(mLink).get();
//					    System.out.println(doc2);
						Element title = doc2.selectFirst("div.subject_movie strong.tit_movie");
						System.out.println(title.text());
						Element score = doc2.selectFirst("div.subject_movie em.emph_grade");
						System.out.println(score.text());
						Element genre = doc2.select("dl.list_movie dd.txt_main").get(0);
						System.out.println(genre.text());
						Element regdate = doc2.select("dl.list_movie dd.txt_main").get(1);
						System.out.println(regdate.text());
						Element time = doc2.select("dl.list_movie dd").get(3);
						System.out.println(time.text());

						String temp = time.text();
						StringTokenizer st = new StringTokenizer(temp, ",");
						String strTime = st.nextToken();
						String strGrade = st.nextToken().trim();
						System.out.println(strTime);
						System.out.println(strGrade);

//					    Element grade;
						Element director = doc2.select("dl.list_movie dd.type_ellipsis").get(0);
						System.out.println(director.text());
						Element actor = doc2.select("dl.list_movie dd.type_ellipsis").get(1);
						System.out.println(actor.text());
						Element story = doc2.selectFirst("div.desc_movie p");
						System.out.println(story.text());
						Element poster=doc2.selectFirst("a.area_poster img.img_summary");
						
						com.sist.vo.MovieVO vo=new MovieVO();
						vo.setTitle(title.text());
						vo.setScore(Double.parseDouble(score.text()));
						vo.setGenre(genre.text());
						vo.setRegdate(regdate.text());
						vo.setTime(strTime);
						vo.setGrade(strGrade);
						vo.setDirector(director.text());
						vo.setActor(actor.text());
						vo.setStory(story.text());
						vo.setPoster(poster.attr("src"));
						vo.setType(5);
						
						dao.movieInsert(vo);
						
						
						System.out.println("k="+k);
						k++;
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
			System.out.println("DataBase Insert End...");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}
	
	/*
	 * <ul class="list_line #list">

							<li>
				<a href="http://v.movie.daum.net/v/20200218165207877" class="thumb_line bg_noimage2 @1">
																				<span class="thumb_img" style="background-image:url(//img1.daumcdn.net/thumb/S320x200/?fname=https://t1.daumcdn.net/news/202002/18/starnews/20200218165207830ydsf.jpg);"></span>
				</a>
				<span class="cont_line">
					<strong class="tit_line"><a href="http://v.movie.daum.net/v/20200218165207877" class="link_txt @1">유아인·공효진, 코로나19로 버버리 패션쇼 입장 금지? "사실무근" </a></strong>
					<a href="http://v.movie.daum.net/v/20200218165207877" class="desc_line @1">
						[스타뉴스 김미화 기자]유아인, 공효진 / 사진=스타뉴스 배우 유아인 공효진 등이 코로나19 여파로 영국 버버리 패션쇼 참석을 거부당했다는 보도가 나온 가운데 양쪽 배우 모두 "사실이 아니다"라는 입장을 밝혔다. 18일 한 매체는 배우 유아인 공효진이 17일 영국 런던에서 열린 버버리 컬렉션 쇼에 참석할 예정이었으나, 코로나19 안전 대책이 마련되지 않아
					</a>
					<span class="state_line">
						스타뉴스<span class="txt_dot">・</span><span class="screen_out">발행일자</span>20.02.18
					</span>
				</span>
			</li>
	 */
	public ArrayList<NewsVO> newsAllData() {
		ArrayList<NewsVO> list=new ArrayList<NewsVO>();
		Date date=new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		String today=sdf.format(date);
		
		try {
			for(int i=1;i<=18;i++) {
				// doc => https://movie.daum.net/magazine/new 페이지 소스가 저장
				Document doc=Jsoup.connect("https://movie.daum.net/magazine/new?tab=nws&regdate="+today+"&page="+i).get();
				Elements title=doc.select("ul.list_line strong.tit_line a");
				Elements poster=doc.select("ul.list_line a.thumb_line span.thumb_img");
				Elements link=doc.select("ul.list_line strong.tit_line a");
				Elements temp=doc.select("span.cont_line span.state_line"); // author + regdate
				Elements content=doc.select("span.cont_line a.desc_line");
				
				for(int j=0;j<title.size();j++) {
					System.out.println(title.get(j).text());
					String ss=poster.get(j).attr("style");
					ss=ss.substring(ss.indexOf("(")+1,ss.lastIndexOf(")"));
					System.out.println(ss);
					System.out.println(link.get(j).attr("href"));
					String str=temp.get(j).text();
					String author=str.substring(0,str.indexOf("・"));
					String regdate=str.substring(str.lastIndexOf("자")+1);
					System.out.println(author);
					System.out.println(regdate);
					System.out.println(content.get(j).text());
					System.out.println("====================================");
					
					
					NewsVO vo=new NewsVO();
					vo.setTitle(title.get(j).text());
					vo.setLink(link.get(j).attr("href"));
					vo.setContent(content.get(j).text());
					vo.setPoster(ss);
					vo.setRegdate(regdate);
					vo.setAuthor(author);
					
					// 오라클 전송
					dao.newsInsert(vo);
				}
			}
			System.out.println("Save End...");
		} catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
		return list;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		MovieManager m = new MovieManager();
//		m.movieListData();
		m.newsAllData();
	}

}
