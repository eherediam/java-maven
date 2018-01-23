package com.platzi.profesoresplatzi.service;

import java.util.List;

import com.platzi.profesoresplatzi.model.SocialMedia;
import com.platzi.profesoresplatzi.model.TeacherSocialMedia;

public interface SocialMediaService {
void saveSocialMedia(SocialMedia socialMedia);
	
	void deleteSocialMediaById(Long id);
	
	void updateSocialMedia(SocialMedia socialMedia);
	
	List<SocialMedia> findAllSocialMedia();
	
	SocialMedia findById(Long idCourse);
	
	SocialMedia findByName(String name);
	
	//List<SocialMedia> findByIdTeacher(Long idTeacher);
	TeacherSocialMedia findSocialMediaByIdAndName(Long idSocialMedia,String nickname);
}
