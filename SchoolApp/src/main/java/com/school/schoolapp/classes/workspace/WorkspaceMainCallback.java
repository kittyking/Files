package com.school.schoolapp.classes.workspace;

import java.util.List;

import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.entitys.shopVO.LetterVO;

public class WorkspaceMainCallback extends BaseCallback {
	
	private WorkspaceMainVO data;
	
	public WorkspaceMainVO getData() {
		return data;
	}
	public void setData(WorkspaceMainVO data) {
		this.data = data;
	}
	public class WorkspaceMainVO{
		private String xstc;
		
		private String ljxs;
		
		private String jrcj;
		
		private String order;
		
		private String dtj;
		
		private String dbh;
		
		private String dsj;
		

		private String ye;
		private String ylr;
		private String zlr;
		
		public String getYe() {
			return ye;
		}

		public void setYe(String ye) {
			this.ye = ye;
		}

		public String getYlr() {
			return ylr;
		}

		public void setYlr(String ylr) {
			this.ylr = ylr;
		}

		public String getZlr() {
			return zlr;
		}

		public void setZlr(String zlr) {
			this.zlr = zlr;
		}

		private List<LetterVO> letter;

		public String getXstc() {
			return xstc;
		}

		public void setXstc(String xstc) {
			this.xstc = xstc;
		}

		public String getLjxs() {
			return ljxs;
		}

		public void setLjxs(String ljxs) {
			this.ljxs = ljxs;
		}

		public String getJrcj() {
			return jrcj;
		}

		public void setJrcj(String jrcj) {
			this.jrcj = jrcj;
		}

		public String getOrder() {
			return order;
		}

		public void setOrder(String order) {
			this.order = order;
		}

		public String getDtj() {
			return dtj;
		}

		public void setDtj(String dtj) {
			this.dtj = dtj;
		}

		public String getDbh() {
			return dbh;
		}

		public void setDbh(String dbh) {
			this.dbh = dbh;
		}

		public String getDsj() {
			return dsj;
		}

		public void setDsj(String dsj) {
			this.dsj = dsj;
		}

		public List<LetterVO> getLetter() {
			return letter;
		}

		public void setLetter(List<LetterVO> letter) {
			this.letter = letter;
		}
		
	}
}
