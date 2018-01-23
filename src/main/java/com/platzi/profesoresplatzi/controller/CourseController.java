package com.platzi.profesoresplatzi.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import com.platzi.profesoresplatzi.model.Course;
import com.platzi.profesoresplatzi.service.CourseService;
import com.platzi.profesoresplatzi.util.CustomErrorType;

@Controller
@RequestMapping("/v1")
public class CourseController {
	@Autowired
	CourseService _courseService;
	private static final Log LOG = LogFactory.getLog(CourseController.class);
	
	//GET 
	@RequestMapping(value="/courses", method = RequestMethod.GET, headers="Accept=application/json")
	public ResponseEntity<List<Course>> getCourse(@RequestParam(value="name", required=false) String name){	
		List<Course> courses = new ArrayList<>();
		LOG.info("METHOD: getCourse()-- PARAMS: name="+name); //Con Log tengo mas informacion del flujo del programa
		//si recibe un nombre busca por nombre sino los trae todos
		if (name == null) {
			courses = _courseService.findAllCourse();
			if (courses.isEmpty()) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
			}
			
			return new ResponseEntity<List<Course>>(courses, HttpStatus.OK);
		} else {
			Course course = _courseService.findByName(name);
			if (course == null) {
				return new ResponseEntity(HttpStatus.NOT_FOUND);
			}
			
			courses.add(course);
			return new ResponseEntity<List<Course>>(courses, HttpStatus.OK);
		}
		
	}

	//GET
	@RequestMapping(value="/courses/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<Course> getCourseById(@PathVariable("id") Long idCourse){
		if (idCourse == null || idCourse <= 0) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		
		Course course = _courseService.findById(idCourse);
		if (course == null) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		
		return new ResponseEntity<Course>(course, HttpStatus.OK);
	}
	
	//POST Insert
		@RequestMapping(value="/courses", method = RequestMethod.POST, headers = "Accept=application/json")
		public ResponseEntity<?> createCourse(@RequestBody Course course, UriComponentsBuilder uriComponentsBuilder){
			if (course.getName().equals(null) || course.getName().isEmpty()) {
				return new ResponseEntity(new CustomErrorType("Course name is required"), HttpStatus.CONFLICT);
			}
			
			if (_courseService.findByName(course.getName()) != null) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
			}
			
			_courseService.saveCourse(course);
			Course course2 = _courseService.findByName(course.getName());
			HttpHeaders headers = new HttpHeaders();
			headers.setLocation(
					uriComponentsBuilder.path("/v1/courses/{id}")
					.buildAndExpand(course2.getIdCurse())
					.toUri()
					);
			
			return new ResponseEntity<String>(headers, HttpStatus.CREATED);
		}
		
		//UPDATE
				@RequestMapping(value="/courses/{id}", method = RequestMethod.PATCH, headers = "Accept=application/json")
				public ResponseEntity<Course> updateCourse(@PathVariable("id") Long idCourse, @RequestBody Course course){
					if (idCourse == null || idCourse <= 0) {
						return new ResponseEntity(new CustomErrorType("idCourse is required"), HttpStatus.CONFLICT);
					}
					
					Course currentCourse = _courseService.findById(idCourse);
					if (currentCourse == null) {
						return new ResponseEntity(HttpStatus.NO_CONTENT);
					}
					
					currentCourse.setName(course.getName());
					currentCourse.setProject(course.getProject());
					currentCourse.setThemes(course.getThemes());
					
					_courseService.updateCourse(currentCourse);
					return new ResponseEntity<Course>(currentCourse, HttpStatus.OK);
				}
				
				//DELETE
				@RequestMapping(value="/courses/{id}", method = RequestMethod.DELETE)
				public ResponseEntity<?> deleteCourse(@PathVariable("id") Long idCourse){
					if (idCourse == null || idCourse <= 0) {
						return new ResponseEntity(new CustomErrorType("idCourse is required"), HttpStatus.CONFLICT);
					}
					
					Course course = _courseService.findById(idCourse);
					if (course == null) {
						return new ResponseEntity(HttpStatus.NO_CONTENT);
					}
					
					_courseService.deleteCourseById(idCourse);
					return new ResponseEntity<Course>(HttpStatus.OK);
							
				}

}
