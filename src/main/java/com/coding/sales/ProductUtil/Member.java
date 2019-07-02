package com.coding.sales.ProductUtil;

public class Member {
	    //姓名,等级,卡号,积分
		private String name;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getLevel() {
			return level;
		}
		public void setLevel(String level) {
			this.level = level;
		}
		public String getCardNo() {
			return cardNo;
		}
		public void setCardNo(String cardNo) {
			this.cardNo = cardNo;
		}
		public int getScore() {
			return score;
		}
		public void setScore(int score) {
			this.score = score;
		}

		private String level;
		private String cardNo;
		private int score;
		public Member(String name, String level, String cardNo, int score) {
			super();
			this.name = name;
			this.level = level;
			this.cardNo = cardNo;
			this.score = score;
		}
		public static Member getMemberByName(String name) throws Exception{
			Member member;
			if(name.equals("马丁")){
				member = new Member("马丁","普卡","6236609999",9860);
			}else if(name.equals("王立")){
				member = new Member("王立","金卡","6630009999",48860);
			}else if(name.equals("李想")){
				member = new Member("李想","白金卡","8230009999",98860);
			}else if(name.equals("张三")){
				member = new Member("张三","钻石卡","9230009999",198860);
			}else{
				throw new Exception("该客户不存在!");
			}
			return member;
		}
		
		public static void main(String[] args) throws Exception {
			System.out.println(getMemberByName("张三").getCardNo());;
		}
}
