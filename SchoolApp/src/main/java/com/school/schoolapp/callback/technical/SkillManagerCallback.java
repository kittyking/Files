package com.school.schoolapp.callback.technical;

import java.util.List;

import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.entity.technical.SkillMangerEntity;

public class SkillManagerCallback extends BaseCallback{

	private SkillManagerVO data;
	
	
	public SkillManagerVO getData() {
		return data;
	}


	public void setData(SkillManagerVO data) {
		this.data = data;
	}


	public class SkillManagerVO{
		private List<SkillMangerEntity> skillList;

		public List<SkillMangerEntity> getSkillList() {
			return skillList;
		}

		public void setSkillList(List<SkillMangerEntity> skillList) {
			this.skillList = skillList;
		}
		
	}
}
