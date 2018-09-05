package com.school.schoolapp.entity.store;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.R.bool;
import android.widget.ImageButton;
import android.widget.TextView;

public class StoreCartEntity {
	
	private static StoreCartEntity storeCart = null;

	private List<StoreGoodsVO> goodsVO;
	
	private String shopID;

	private List<StoreCartGoods> goodsList;
	
	private float money = 0;
	
	private float yuanMoney = 0;
	
	
	
	public List<StoreGoodsVO> getGoodsVO() {
		return goodsVO;
	}

	public void setGoodsVO(List<StoreGoodsVO> goodsVO) {
		this.goodsVO = goodsVO;
	}

	public float getYuanMoney() {
		return yuanMoney;
	}

	public void setYuanMoney(float yuanMoney) {
		this.yuanMoney = yuanMoney;
	}

	public static StoreCartEntity getInstance(){
		if(storeCart == null){
			storeCart = new StoreCartEntity();
		}
		
		return storeCart;
	}
	
	public static void initCart(){
		storeCart = new StoreCartEntity();
	}
	
	public Boolean addGoods(StoreGoodsVO good, String goodId,String money,String yuanMoney,ImageButton cut,TextView number){
		if(goodsList==null){
			goodsList = new ArrayList<StoreCartGoods>();
			goodsVO = new ArrayList<StoreGoodsVO>();
		}
		
		if(goodsList.size()==0){
			StoreCartGoods cartGood = new StoreCartGoods();
			cartGood.setGoods_id(goodId);
			cartGood.setNum(1);
			cartGood.setCut(cut);
			cartGood.setNumber(number);
			
			StoreGoodsVO goodVO = good;
			goodsVO.add(goodVO);
			
			addmoney(money,yuanMoney);
			goodsList.add(cartGood);
			return true;
		}
		
		for(int i=0;i<goodsList.size();i++){
			if(goodsList.get(i).getGoods_id().equals(goodId)){
				int curCount = goodsList.get(i).getNum();
				goodsList.get(i).setNum(curCount+1);
				addmoney(money,yuanMoney);
				return true;
			}
		}
		StoreCartGoods cartGood = new StoreCartGoods();
		cartGood.setGoods_id(goodId);
		cartGood.setNum(1);
		cartGood.setCut(cut);
		cartGood.setNumber(number);
		
		StoreGoodsVO goodVO = good;
		goodsVO.add(goodVO);
		
		addmoney(money,yuanMoney);
		goodsList.add(cartGood);
		
		return true;
	}
	
	public Boolean delGoods(String goodId,String money,String yuanMoney){
		if(goodsList==null){
			goodsList = new ArrayList<StoreCartGoods>();
			goodsVO = new ArrayList<StoreGoodsVO>();
		}
		
		for(int i=0; i<goodsList.size();i++){
			if(goodsList.get(i).getGoods_id().equals(goodId)){
				int curCount = goodsList.get(i).getNum();
				goodsList.get(i).setNum(curCount-1);
				if(curCount-1 == 0){
					goodsList.remove(i);
					goodsVO.remove(i);
				}
				minmoney(money,yuanMoney);
				return true;
			}
		}
		
		
		return false;
	}
	
	public Boolean delReplenishGoods(StoreGoodsVO good,String goodId,String money,String yuanMoney,int num){
		if(goodsList==null){
			goodsList = new ArrayList<StoreCartGoods>();
			goodsVO = new ArrayList<StoreGoodsVO>();
		}
		
		for(int i=0; i<goodsList.size();i++){
			if(goodsList.get(i).getGoods_id().equals(goodId)){
				int curCount = goodsList.get(i).getNum();
				goodsList.get(i).setNum(curCount-1);
				if(curCount-1 == 0){
					goodsList.remove(i);
					goodsVO.remove(i);
				}
				minmoney(money,yuanMoney);
				return true;
			}
		}
		
		StoreCartGoods cartGood = new StoreCartGoods();
		cartGood.setGoods_id(goodId);
		cartGood.setNum(num);
		
		StoreGoodsVO goodVO = good;
		goodsVO.add(goodVO);
		
		addmoney(money,yuanMoney);
		goodsList.add(cartGood);
		
		return false;
	}
	
	public List<StoreCartGoods> getCartGoods(){
		return goodsList;
	}
	public List<CartGoodVO> buyGoodsList(){
		List<CartGoodVO> buygoods = new ArrayList<>();
		for(StoreCartGoods cart : goodsList){
			CartGoodVO buygood = new CartGoodVO();
			buygood.setGoods_id(cart.getGoods_id());
			buygood.setNum(cart.getNum());
			buygoods.add(buygood);
		}
		return buygoods;
	}
	public void addmoney(String moneyStr,String yuanmoney){
		money =money + Float.parseFloat(moneyStr);
		yuanMoney = yuanMoney + Float.parseFloat(yuanmoney);
	}
	public void minmoney(String moneyStr,String yuanmoney){
		money =money - Float.parseFloat(moneyStr);
		yuanMoney = yuanMoney - Float.parseFloat(yuanmoney);
	}
	public float getAmountMoney(){
		
		return money;
	}
	public int getNumCount(){
		int num = 0;
		if(goodsList!=null){
			for(int i=0; i<goodsList.size();i++){
				num = num + goodsList.get(i).getNum();
			}
		}
		
		
		return num;
	}
	public int getGoodAddedCount(String goodid){
		if(this.goodsList==null)
			return 0;
		
		for(StoreCartGoods good : this.goodsList){
			if(good.getGoods_id().equals(goodid))
				return good.getNum();
		}
		return 0;
	}
	
}
