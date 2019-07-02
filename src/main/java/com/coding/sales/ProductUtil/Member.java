package com.coding.sales.ProductUtil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Member {
	    public static Member getMemberByMemberId(String memberId) throws Exception{
			Member member;
			if(memberId.equals("6236609999")){
				member = new Member("马丁","普卡","6236609999",9860);
			}else if(memberId.equals("6630009999")){
				member = new Member("王立","金卡","6630009999",48860);
			}else if(memberId.equals("8230009999")){
				member = new Member("李想","白金卡","8230009999",98860);
			}else if(memberId.equals("9230009999")){
				member = new Member("张三","钻石卡","9230009999",198860);
			}else{
				throw new Exception("该客户不存在!");
			}
			return member;
		}
	    
		private String level;
		private String memberId;
		//姓名,等级,卡号,积分
		private String name;
		private int score;
		public Member(String name, String level, String memberId, int score) {
			super();
			this.name = name;
			this.level = level;
			this.memberId = memberId;
			this.score = score;
		}

		public String getLevel() {
			return level;
		}
		public String getMemberId() {
			return memberId;
		}
		public String getName() {
			return name;
		}
		public int getScore() {
			return score;
		}

		public void setLevel(String level) {
			this.level = level;
		}
		public void setMemberId(String memberId) {
			this.memberId = memberId;
		}
		public void setName(String name) {
			this.name = name;
		}
		
		public void setScore(int score) {
			this.score = score;
		}
		
		public static Map getScoreAndLevel(String memberId,BigDecimal payment) throws Exception{
			Map m = new HashMap();
			Member member = Member.getMemberByMemberId(memberId);
			String oldlevel = member.getLevel();
			int oldscore = member.getScore();
			int newscore = 0; //最新积分
			int increasescore = 0;//新增积分
			String newlevel = "";//最新等级
			if(oldlevel.endsWith("普卡")){
				newscore = payment.intValue()+oldscore;
				increasescore = payment.intValue();
			}else if(oldlevel.endsWith("金卡")){
				newscore = payment.multiply(new BigDecimal(1.5)).intValue()+oldscore;
				increasescore = payment.multiply(new BigDecimal(1.5)).intValue();
			}else if(oldlevel.endsWith("白金卡")){
				newscore = payment.multiply(new BigDecimal(1.8)).intValue()+oldscore;
				increasescore = payment.multiply(new BigDecimal(1.8)).intValue();
			}else if(oldlevel.endsWith("钻石卡")){
				newscore = payment.multiply(new BigDecimal(2)).intValue()+oldscore;
				increasescore = payment.multiply(new BigDecimal(2)).intValue();
			}
			
			if(newscore < 10000){
				newlevel = "普卡";
			}else if( newscore >= 10000 && newscore < 50000){
				newlevel = "金卡";
			} if(newscore >= 50000 && newscore < 100000){
				newlevel = "白金卡";
			} if(newscore >= 100000){
				newlevel = "钻石卡";
			}
			
			m.put("oldlevel", oldlevel);
			m.put("increasescore", increasescore);
			m.put("newlevel", newlevel.equals("")?oldlevel:newlevel);
			m.put("newscore", newscore);
			return m;
		}
		
		public static void main(String[] args) throws Exception {
			System.out.println(getScoreAndLevel("6236609999",new BigDecimal(100)));
		}
}
