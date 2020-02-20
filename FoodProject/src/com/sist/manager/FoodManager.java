package com.sist.manager;

import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.sist.dao.*;


public class FoodManager {

/*
 * <div class="top_list_slide">
        <ul class="list-toplist-slider" style="width: 531px;">
            <li>
              <img class="center-croping" alt="분짜 맛집 베스트 35곳 사진"
                   data-lazy="https://mp-seoul-image-production-s3.mangoplate.com/keyword_search/meta/pictures/ll_ikc83wddw3t9a.png?fit=around|600:400&amp;crop=600:400;*,*&amp;output-format=jpg&amp;output-quality=80"/>

              <a href="/top_lists/1649_bun_cha"
                 onclick="trackEvent('CLICK_TOPLIST', {&quot;section_position&quot;:0,&quot;section_title&quot;:&quot;믿고 보는 맛집 리스트&quot;,&quot;position&quot;:0,&quot;link_key&quot;:&quot;P5AXQ5F&quot;});">
                <figure class="ls-item">
                  <figcaption class="info">
                    <div class="info_inner_wrap">
                      <span class="title">분짜 맛집 베스트 35곳</span>
                      <p class="desc">"갑분짜: 갑자기 분짜 먹고 싶다."</p>
                      <p class="hash">
                          <span>#분짜 </span>
                          <span>#분짜 맛집 </span>
                          <span>#분짜맛집 </span>
                          <span>#베트남 </span>
                          <span>#베트남요리 </span>
                          <span>#베트남 요리 </span>
                          <span>#베트남음식 </span>
                          <span>#베트남 음식 </span>
                      </p>
                    </div>
                  </figcaption>
                </figure>
              </a>
            </li>
 */
	public ArrayList<CategoryVO> categoryAllData() {
		ArrayList<CategoryVO> list=new ArrayList<CategoryVO>();
		try {
			Document doc=Jsoup.connect("https://www.mangoplate.com/").get();
			Elements title=doc.select("div.info_inner_wrap span.title");
			Elements poster=doc.select("ul.list-toplist-slider img.center-croping");
			Elements subject=doc.select("div.info_inner_wrap p.desc");
			Elements link=doc.select("ul.list-toplist-slider li a");
			
			for(int i=0;i<12;i++) {
				System.out.println(title.get(i).text());
				System.out.println(subject.get(i).text());
				System.out.println(link.get(i).attr("href"));
				System.out.println(poster.get(i).attr("data-lazy"));
				System.out.println("========================================================");
				
				CategoryVO vo=new CategoryVO();
				vo.setCateno(i+1);
				vo.setTitle(title.get(i).text());
				vo.setSubject(subject.get(i).text());
				vo.setLink("https://www.mangoplate.com"+link.get(i).attr("href"));
				String temp=poster.get(i).attr("data-lazy");
				temp=temp.replace("&", "@"); // &가 들어가면 입력창
				vo.setPoster(temp);
				
				list.add(vo);
			}
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}
	
	
	public ArrayList<FoodHouseVO> foodAllData() {
		ArrayList<FoodHouseVO> fList=new ArrayList<FoodHouseVO>();
		try {
			ArrayList<CategoryVO> cList=categoryAllData();
			int k=1;
			for(CategoryVO cvo:cList) {
				Document doc=Jsoup.connect(cvo.getLink()).get();
				Elements link=doc.select("div.info span.title a");
				System.out.println("======"+cvo.getTitle()+"======");
				for(int j=0;j<link.size();j++) {
					try{
						String url="https://www.mangoplate.com"+link.get(j).attr("href");
						
						Document doc2=Jsoup.connect(url).get();
						Elements poster=doc2.select("figure.list-photo img.center-croping");
						String img="";
						for(int a=0;a<poster.size();a++) {
//							System.out.println(poster.get(a).attr("src"));
							img+=poster.get(a).attr("src")+"^";
						}
						img=img.substring(0,img.lastIndexOf("^")); // 맨마지막 ^ 제외
						System.out.println("**********************************");
						Element title=doc2.selectFirst("span.title h1.restaurant_name"); //select("").get(0)
						Element score=doc2.selectFirst("span.title strong.rate-point span");;
						Element address=doc2.select("table.info tr td").get(0);
						Element tel=doc2.select("table.info tr td").get(1);
						Element type=doc2.select("table.info tr td").get(2);
						Element price=doc2.select("table.info tr td").get(3);
						Element temp=doc2.selectFirst("script#reviewCountInfo");
						
						System.out.println("k="+k);
						System.out.println(title.text());
						System.out.println(score.text());
						System.out.println(address.text());
						System.out.println(tel.text());
						System.out.println(type.text());
						System.out.println(price.text());
						System.out.println(temp.data()); // javascript는 text아니고 data
						System.out.println("=======================================");
						
						FoodHouseVO vo=new FoodHouseVO();
						vo.setCno(cvo.getCateno());
						vo.setTitle(title.text());
						vo.setScore(Double.parseDouble(score.text()));
						vo.setAddress(address.text());
						vo.setTel(tel.text());
						vo.setType(type.text());
						vo.setPrice(price.text());
						vo.setImage(img);
						
						//[{"action_value":1,"count":12},{"action_value":2,"count":21},{"action_value":3,"count":59}]
						JSONParser parser=new JSONParser();
						JSONArray arr=(JSONArray)parser.parse(temp.data());
						System.out.println("JSON=> "+arr.toJSONString());
						/*
						 *     {} => JSONObject
						 *     [{},{},{}] => JSONArray
						 */
						long[] data=new long[3];
						
						for(int b=0;b<arr.size();b++) {
							JSONObject obj=(JSONObject)arr.get(b);
							System.out.println("Object=>"+obj.toJSONString());
							long count=(long)obj.get("count");
							data[b]=count;
							System.out.println("count="+count);
						}
						vo.setBad((int)data[0]);
						vo.setSoso((int)data[1]);
						vo.setGood((int)data[2]);
						
						fList.add(vo);
						k++;
						
					} catch(Exception ex) {}
					
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return fList;
	}
	
	public static void main(String[] args) {
		
		FoodManager fm=new FoodManager();
//		ArrayList<CategoryVO> list=fm.categoryAllData();
//		FoodDAO dao=FoodDAO.newInstance();
//		int k=1;
//		for(CategoryVO vo:list) {
//			dao.categoryInsert(vo);
//			System.out.println("k="+k);
//			k++;
//		}
//		System.out.println("Save End...");
		
//		fm.foodAllData();
		
		ArrayList<FoodHouseVO> list=fm.foodAllData();
		FoodDAO dao=FoodDAO.newInstance();
		int k=1;
		for(FoodHouseVO vo:list) {
			dao.foodHouseInsert(vo);
			System.out.println("k="+k);
			// 너무 빨리 돌아가면 데이터가 다 안들어갈 수도 있음
			try {
				Thread.sleep(100);
			} catch(Exception ex) {}
			k++;
		}
		System.out.println("Save End...");

	}
}
