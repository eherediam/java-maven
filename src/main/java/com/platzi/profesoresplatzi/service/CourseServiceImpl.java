package com.platzi.profesoresplatzi.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platzi.profesoresplatzi.dao.CourseDao;
import com.platzi.profesoresplatzi.model.Course;

@Service("courseService")
@Transactional
public class CourseServiceImpl implements CourseService{

	@Autowired
private CourseDao _course;
	
	@Override
	public void saveCourse(Course course) {
		// TODO Auto-generated method stub
		_course.saveCourse(course);
		//valor de password
		System.out.printf("password123");
	}

	@Override
	public void deleteCourseById(Long idCourse) {
		// TODO Auto-generated method stub
		_course.deleteCourseById(idCourse);
	}

	@Override
	public void updateCourse(Course course) {
		// TODO Auto-generated method stub
		_course.updateCourse(course);
	}

	@Override
	public List<Course> findAllCourse() {
		// TODO Auto-generated method stub
		return _course.findAllCourse();
	}

	@Override
	public Course findById(Long idCourse) {
		// TODO Auto-generated method stub
		return _course.findById(idCourse);
	}

	@Override
	public Course findByName(String name) {
		// TODO Auto-generated method stub
		return _course.findByName(name);
	}

	@Override
	public List<Course> findByIdTeacher(Long idTeacher) {
		// TODO Auto-generated method stub
		return _course.findByIdTeacher(idTeacher);
	}

}
