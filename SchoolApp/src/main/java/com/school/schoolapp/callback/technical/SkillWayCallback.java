package com.school.schoolapp.callback.technical;

import java.util.List;

import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.entity.technical.SkillWayEntity;

public class SkillWayCallback extends BaseCallback{

	private SkillWayVo data;
	
	
	public SkillWayVo getData() {
		return data;
	}


	public void setData(SkillWayVo data) {
		this.data = data;
	}


	public class SkillWayVo{
		private List<SkillWayEntity> list;

		public List<SkillWayEntity> getList() {
			return list;
		}

		public void setList(List<SkillWayEntity> list) {
			this.list = list;
		}
		
	}
}
