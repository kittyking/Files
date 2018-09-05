package com.school.schoolapp.entity.technical;

import java.util.List;

public class SkillInfoEntity {
//	sid": "20161018225710055525",//服务ID
//    "nick": "我不知道",//昵称
//    "sex": "男",//性别
//    "score": "0.0",//评分
//    "city": "5",//城市ID
//    "avatar": "http://app.shxbox.com/uploadfiles/site/img/20161018/58062c9ddc888.jpg",//头像
//    "online": "0",/是否在线1、在线0、不在线
//    "title": "不知道的标题",//服务标题
//    "summary": "不知道的内容",//服务描述
//    "cid": "1",//分类ID
//    "money": "10.20",//价格
//    "views": "1",//浏览量
//    "unit": "小时",//单位
//    "imgs": [//图片列表
//      {
//        "imgsrc": "http://app.shxbox.com/uploadfiles/site/img/20161018/58062c9ddc888.jpg"
//      }

	private String sid;
	private String nick;
	private String sex;
	private String score;
	private String city;
	private String avatar;
	private String online;
	private String title;
	private String summary;
	private String cid;
	private String money;
	private String views;
	private String unit;
	private List<ImgC> imgs;
	
	
	
	public String getSid() {
		return sid;
	}



	public void setSid(String sid) {
		this.sid = sid;
	}



	public String getNick() {
		return nick;
	}



	public void setNick(String nick) {
		this.nick = nick;
	}



	public String getSex() {
		return sex;
	}



	public void setSex(String sex) {
		this.sex = sex;
	}



	public String getScore() {
		return score;
	}



	public void setScore(String score) {
		this.score = score;
	}



	public String getCity() {
		return city;
	}



	public void setCity(String city) {
		this.city = city;
	}



	public String getAvatar() {
		return avatar;
	}



	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}



	public String getOnline() {
		return online;
	}



	public void setOnline(String online) {
		this.online = online;
	}



	public String getTitle() {
		return title;
	}



	public void setTitle(String title) {
		this.title = title;
	}



	public String getSummary() {
		return summary;
	}



	public void setSummary(String summary) {
		this.summary = summary;
	}



	public String getCid() {
		return cid;
	}



	public void setCid(String cid) {
		this.cid = cid;
	}



	public String getMoney() {
		return money;
	}



	public void setMoney(String money) {
		this.money = money;
	}



	public String getViews() {
		return views;
	}



	public void setViews(String views) {
		this.views = views;
	}



	public String getUnit() {
		return unit;
	}



	public void setUnit(String unit) {
		this.unit = unit;
	}



	public List<ImgC> getImgs() {
		return imgs;
	}



	public void setImgs(List<ImgC> imgs) {
		this.imgs = imgs;
	}



	public class ImgC{
		private String imgsrc;

		public String getImgsrc() {
			return imgsrc;
		}

		public void setImgsrc(String imgsrc) {
			this.imgsrc = imgsrc;
		}
		
	}
}
