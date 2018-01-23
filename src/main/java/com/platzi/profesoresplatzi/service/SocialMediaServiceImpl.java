package com.platzi.profesoresplatzi.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platzi.profesoresplatzi.dao.SocialMediaDao;
import com.platzi.profesoresplatzi.model.SocialMedia;
import com.platzi.profesoresplatzi.model.TeacherSocialMedia;

@Service("socialMediaService")
@Transactional
public class SocialMediaServiceImpl implements SocialMediaService{

	@Autowired
	private SocialMediaDao _socialMedia; 
	@Override
	public void saveSocialMedia(SocialMedia socialMedia) {
		// TODO Auto-generated method stub
      		_socialMedia.saveSocialMedia(socialMedia);
	}

	@Override
	public void deleteSocialMediaById(Long id) {
		// TODO Auto-generated method stub
		_socialMedia.deleteSocialMediaById(id);
	}

	@Override
	public void updateSocialMedia(SocialMedia socialMedia) {
		// TODO Auto-generated method stub
		_socialMedia.updateSocialMedia(socialMedia);
	}

	@Override
	public List<SocialMedia> findAllSocialMedia() {
		// TODO Auto-generated method stub
		return _socialMedia.findAllSocialMedia();
	}

	@Override
	public SocialMedia findById(Long idCourse) {
		// TODO Auto-generated method stub
		return _socialMedia.findById(idCourse);
	}

	@Override
	public SocialMedia findByName(String name) {
		// TODO Auto-generated method stub
		return _socialMedia.findByName(name);
	}

	@Override
	public TeacherSocialMedia findSocialMediaByIdAndName(Long idSocialMedia, String nickname) {
		// TODO Auto-generated method stub
		return _socialMedia.findSocialMediaByIdAndName(idSocialMedia, nickname);
	}

}
