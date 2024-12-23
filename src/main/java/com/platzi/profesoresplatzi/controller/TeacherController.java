package com.platzi.profesoresplatzi.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.multipart.MultipartFile;

import com.platzi.profesoresplatzi.model.SocialMedia;
import com.platzi.profesoresplatzi.model.Teacher;
import com.platzi.profesoresplatzi.model.TeacherSocialMedia;
import com.platzi.profesoresplatzi.service.SocialMediaService;
import com.platzi.profesoresplatzi.service.TeacherService;
import com.platzi.profesoresplatzi.util.CustomErrorType;


@Controller
@RequestMapping("/v1")
public class TeacherController {

	@Autowired
	TeacherService _teacherService;
	
	@Autowired
	SocialMediaService _socialMediaService;
	
	//GET 
	@RequestMapping(value="/teachers", method = RequestMethod.GET, headers="Accept=application/json")
	public ResponseEntity<List<Teacher>> getTeachers(@RequestParam(value="name", required=false) String name){	
		List<Teacher> teachers = new ArrayList<Teacher>();
		//si recibe un nombre busca por nombre sino los trae todos
		if (name == null) {
			teachers = _teacherService.findAllTeachers();
			if (teachers.isEmpty()) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
			}
			
			return new ResponseEntity<List<Teacher>>(teachers, HttpStatus.OK);
		} else {
			Teacher teacher = _teacherService.findByName(name);
			//String pass = "PASSWORD"
			//System.out.printf("pass", pass);	//es solo de prueba 
			if (teacher == null) {
				return new ResponseEntity(HttpStatus.NOT_FOUND);
			}
			
			teachers.add(teacher);
			return new ResponseEntity<List<Teacher>>(teachers, HttpStatus.OK);
		}
		
	}

	//GET
	@RequestMapping(value="/teachers/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<Teacher> getSocialMediaById(@PathVariable("id") Long idTeacher){
		if (idTeacher == null || idTeacher <= 0) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		
		Teacher teacher = _teacherService.findById(idTeacher);
		if (teacher == null) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		
		return new ResponseEntity<Teacher>(teacher, HttpStatus.OK);
	}
	
	//POST Insert
		@RequestMapping(value="/teachers", method = RequestMethod.POST, headers = "Accept=application/json")
		public ResponseEntity<?> createTeacher(@RequestBody Teacher teacher, UriComponentsBuilder uriComponentsBuilder){
			if (teacher.getName().equals(null) || teacher.getName().isEmpty()) {
				return new ResponseEntity(new CustomErrorType("Teacher name is required"), HttpStatus.CONFLICT);
			}
			
			if (_teacherService.findByName(teacher.getName()) != null) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
			}
			
			_teacherService.saveTeacher(teacher);
			Teacher teacher2 = _teacherService.findByName(teacher.getName());
			HttpHeaders headers = new HttpHeaders();
			headers.setLocation(
					uriComponentsBuilder.path("/v1/socialMedias/{id}")
					.buildAndExpand(teacher2.getIdTeacher())
					.toUri()
					);
			
			return new ResponseEntity<String>(headers, HttpStatus.CREATED);
		}
		
		//UPDATE
				@RequestMapping(value="/teachers/{id}", method = RequestMethod.PATCH, headers = "Accept=application/json")
				public ResponseEntity<Teacher> updateTeacher(@PathVariable("id") Long idTeacher, @RequestBody Teacher teacher){
					if (idTeacher == null || idTeacher <= 0) {
						return new ResponseEntity(new CustomErrorType("idTeacher is required"), HttpStatus.CONFLICT);
					}
					
					Teacher currentTeacher = _teacherService.findById(idTeacher);
					if (currentTeacher == null) {
						return new ResponseEntity(HttpStatus.NO_CONTENT);
					}
					
					currentTeacher.setName(teacher.getName());
					currentTeacher.setAvatar(teacher.getAvatar());
					
					_teacherService.updateTeacher(currentTeacher);
					return new ResponseEntity<Teacher>(currentTeacher, HttpStatus.OK);
				}
				
				//DELETE
				@RequestMapping(value="/teachers/{id}", method = RequestMethod.DELETE)
				public ResponseEntity<?> deleteteacher(@PathVariable("id") Long idTeacher){
					if (idTeacher == null || idTeacher <= 0) {
						return new ResponseEntity(new CustomErrorType("idTeacher is required"), HttpStatus.CONFLICT);
					}
					
					Teacher teacher = _teacherService.findById(idTeacher);
					if (teacher == null) {
						return new ResponseEntity(HttpStatus.NO_CONTENT);
					}
					
					_teacherService.deleteTeacherById(idTeacher);
					return new ResponseEntity<Teacher>(HttpStatus.OK);
							
				}
					
				public static final String TEACHER_UPLOADED_FOLDER ="images/teachers/";
				//CREATE TEACHER IMAGE
				@RequestMapping(value="/teachers/image", method = RequestMethod.POST, headers=("content-type=multipart/form-data"))
				public ResponseEntity<byte[]> uploadTeacherImage(@RequestParam("id_teacher") Long idTeacher, 
						@RequestParam("file") MultipartFile multipartFile, 
						UriComponentsBuilder componentsBuilder){
					if (idTeacher == null) {
						return new ResponseEntity(new CustomErrorType("Please set id_teacher"), HttpStatus.NO_CONTENT);
					}
					
					if (multipartFile.isEmpty()) {
						return new ResponseEntity(new CustomErrorType("Please select a file to upload"), HttpStatus.NO_CONTENT);
					}
					
					Teacher teacher = _teacherService.findById(idTeacher);
					if (teacher == null) {
						return new ResponseEntity(new CustomErrorType("Teacher with id_teacher: " + idTeacher + " not dfound"), HttpStatus.NOT_FOUND);
					}
					
					if (!teacher.getAvatar().isEmpty() || teacher.getAvatar() != null) {
						String fileName = teacher.getAvatar();
						Path path = Paths.get(fileName);
						File f = path.toFile();
						if (f.exists()) {
							f.delete();
						}
					}
					
					try {
						Date date = new Date();
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
						String dateName = dateFormat.format(date);
						
						String fileName = String.valueOf(idTeacher) + "-pictureTaecher-" + dateName + "." + multipartFile.getContentType().split("/")[1];
						teacher.setAvatar(TEACHER_UPLOADED_FOLDER + fileName);
						
						byte[] bytes = multipartFile.getBytes();
						Path path = Paths.get(TEACHER_UPLOADED_FOLDER + fileName);
						Files.write(path, bytes);
						
						_teacherService.updateTeacher(teacher);
						return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						return new ResponseEntity(new CustomErrorType("Error during upload: " + multipartFile.getOriginalFilename()),HttpStatus.CONFLICT);
					}
				}
				
				//GET IMAGE
				@RequestMapping(value="/teachers/{id_teacher}/images", method = RequestMethod.GET)
				public ResponseEntity<byte[]> getTeacherImage(@PathVariable("id_teacher") Long idTeacher){
					if (idTeacher == null) {
						 return new ResponseEntity(new CustomErrorType("Please set id_teacher "), HttpStatus.NO_CONTENT);
					}
					
					Teacher teacher = _teacherService.findById(idTeacher);
					if (teacher == null) {
						return new ResponseEntity(new CustomErrorType("Teacher with id_teacher: " + idTeacher + " not found"), HttpStatus.NOT_FOUND);
					}
					
					try {
						
						String fileName = teacher.getAvatar();
						Path path = Paths.get(fileName);
						File f = path.toFile();
						if (!f.exists()) {
							return new ResponseEntity(new CustomErrorType("Image not found"),HttpStatus.CONFLICT);
						}
						
						byte[] image = Files.readAllBytes(path);
						return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
						
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						return new ResponseEntity(new CustomErrorType("Error to show image"),HttpStatus.CONFLICT);
					}
				
				}
				//Delete IMG
				@RequestMapping(value="/teachers/{id_teacher}/images", method = RequestMethod.DELETE,headers = "Accept=application/json")
				public ResponseEntity<?> deleteTeacherImage(@PathVariable("id_teacher") Long idTeacher){
					if (idTeacher == null) {
						 return new ResponseEntity(new CustomErrorType("Please set id_teacher "), HttpStatus.NO_CONTENT);
					}
					
					Teacher teacher = _teacherService.findById(idTeacher);
					if (teacher == null) {
						return new ResponseEntity(new CustomErrorType("Teacher with id_teacher: " + idTeacher + " not found"), HttpStatus.NOT_FOUND);
					}
					
					if (teacher.getAvatar().isEmpty() || teacher.getAvatar() == null) {
						 return new ResponseEntity(new CustomErrorType("This Teacher dosen't have image assigned"), HttpStatus.NO_CONTENT);
					}
					
					String fileName = teacher.getAvatar();
					Path path = Paths.get(fileName);
					File file = path.toFile();
					if (file.exists()) {
						file.delete();
					}
					
					teacher.setAvatar("");
					_teacherService.updateTeacher(teacher);
					
					return new ResponseEntity<Teacher>(HttpStatus.NO_CONTENT);
				}
				//consultar TeacherSocialMedias
				@RequestMapping(value="teachers/socialMedias", method = RequestMethod.PATCH, headers = "Accept=application/json")
				public ResponseEntity<?> assignTeacherSocialMedia(@RequestBody Teacher teacher, UriComponentsBuilder uriComponentsBuilder){
					if (teacher.getIdTeacher() == null) {
						return new ResponseEntity(new CustomErrorType("We nee almost id_teacher, id_social_media and nickname"), HttpStatus.NO_CONTENT);
					}
					
					Teacher teacherSaved = _teacherService.findById(teacher.getIdTeacher());
					if (teacherSaved == null) {
						return new ResponseEntity(new CustomErrorType("The id_teacher: " + teacher.getIdTeacher() + " not found"), HttpStatus.NO_CONTENT);
					}
					
					if (teacher.getTeacherSocialMedias().size() == 0) {
						return new ResponseEntity(new CustomErrorType("We nee almost id_teacher, id_social_media and nickname"), HttpStatus.NO_CONTENT);
					}else{
						Iterator<TeacherSocialMedia> i = teacher.getTeacherSocialMedias().iterator();
						while (i.hasNext()) {
							TeacherSocialMedia teacherSocialMedia = i.next();
							if (teacherSocialMedia.getSocialMedia().getIdSocialMedia() == null || teacherSocialMedia.getNickname() == null) {
								return new ResponseEntity(new CustomErrorType("We nee almost id_teacher, id_social_media and nickname"), HttpStatus.NO_CONTENT);
							}else{
								TeacherSocialMedia tsmAux = _socialMediaService.findSocialMediaByIdAndName(
										teacherSocialMedia.getSocialMedia().getIdSocialMedia(), 
										teacherSocialMedia.getNickname());
								
								if (tsmAux == null) {
									return new ResponseEntity(new CustomErrorType("The id social media " 
									+ teacherSocialMedia.getSocialMedia().getIdSocialMedia() 
									+ " witch nickname: " + teacherSocialMedia.getNickname() 
									+ " already exist"), HttpStatus.NO_CONTENT);
								}
								
								SocialMedia socialMedia = _socialMediaService.findById(teacherSocialMedia.getSocialMedia().getIdSocialMedia());
								if (socialMedia == null) {
									return new ResponseEntity(new CustomErrorType("The id social media " 
											+ teacherSocialMedia.getSocialMedia().getIdSocialMedia() 
											+ " not found"), HttpStatus.NO_CONTENT);
								}
								
								teacherSocialMedia.setSocialMedia(socialMedia);
								teacherSocialMedia.setTeacher(teacherSaved);
								
								if (tsmAux == null) {
									teacherSaved.getTeacherSocialMedias().add(teacherSocialMedia);
								}else{
									LinkedList<TeacherSocialMedia> teacherSocialMedias = new LinkedList<>();
									teacherSocialMedias.addAll(teacherSaved.getTeacherSocialMedias());
									
									for (int j = 0; j < teacherSocialMedias.size(); j++) {
										TeacherSocialMedia teacherSocialMedia2 = teacherSocialMedias.get(j);
										if (teacherSocialMedia.getTeacher().getIdTeacher() == teacherSocialMedia2.getTeacher().getIdTeacher()
												&& teacherSocialMedia.getSocialMedia().getIdSocialMedia() == teacherSocialMedia2.getSocialMedia().getIdSocialMedia()) {

											teacherSocialMedia2.setNickname(teacherSocialMedia.getNickname());
											teacherSocialMedias.set(j, teacherSocialMedia2);
										}else{
											teacherSocialMedias.set(j, teacherSocialMedia2);
										}
									}
									
									teacherSaved.getTeacherSocialMedias().clear();
									teacherSaved.getTeacherSocialMedias().addAll(teacherSocialMedias);
									
								}
										
							}
							
							
						}
					}
					
					_teacherService.updateTeacher(teacherSaved);
					return new ResponseEntity<Teacher>(teacherSaved, HttpStatus.OK);
					
				}
}
