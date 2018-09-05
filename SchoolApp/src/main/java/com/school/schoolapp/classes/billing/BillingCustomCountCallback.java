package com.school.schoolapp.classes.billing;

import com.school.schoolapp.classes.BaseCallback;

public class BillingCustomCountCallback extends BaseCallback {
	
	private CustomCountVO data;
	

	public CustomCountVO getData() {
		return data;
	}


	public void setData(CustomCountVO data) {
		this.data = data;
	}


	public class CustomCountVO{
		private String dfk;
		
		private String djd;
		
		private String dfh;
		
		private String psz;

		public String getDfk() {
			return dfk;
		}

		public void setDfk(String dfk) {
			this.dfk = dfk;
		}

		public String getDjd() {
			return djd;
		}

		public void setDjd(String djd) {
			this.djd = djd;
		}

		public String getDfh() {
			return dfh;
		}

		public void setDfh(String dfh) {
			this.dfh = dfh;
		}

		public String getPsz() {
			return psz;
		}

		public void setPsz(String psz) {
			this.psz = psz;
		}
		
		
	}
}
