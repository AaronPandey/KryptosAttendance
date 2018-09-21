package com.unifyed.attendance.services;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.mongodb.WriteResult;
import com.unifyed.attendance.domains.Course;
import com.unifyed.attendance.domains.Room;
import com.unifyed.attendance.domains.Semester;
import com.unifyed.attendance.domains.Specialization;
import com.unifyed.attendance.domains.Subject;
import com.unifyed.attendance.repository.CourseRepository;
import com.unifyed.attendance.repository.RoomRepository;
import com.unifyed.attendance.repository.SemesterRepository;
import com.unifyed.attendance.repository.SpecializationRepository;
import com.unifyed.attendance.repository.SubjectRepository;

/**
 * Created by Aniket on 16/08/2018.
 */
@Service
public class AdminService {

	@Autowired
	private SemesterRepository semesterRepository;

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private SubjectRepository subjectRepository;

	@Autowired
	private SpecializationRepository specializationRepository;

	@Autowired
	MongoTemplate mongoTemplate;

	public String postCourse(Course course) {
		Course newCourse;
		try {
			newCourse = courseRepository.save(course);
		} catch (Exception e) {
			if (e.getMessage().contains("duplicate key error"))
				;
			return "message: Course name already in db ";
		}

		System.out.println("Course data saved");
		return newCourse.toString();
	}

	public String putCourse(Map<String, Object> requestBody) {
		try {
			Course course = courseRepository.findByCourse(requestBody.get("oldCourse").toString());

			if (course == null)
				return "Course " + requestBody.get("oldCourse").toString() + " Doesn't exist";

			Query query = new Query();
			query.addCriteria(Criteria.where("course").is(course));

			Update update = new Update();
			update.set("courseName", requestBody.get("newCourse").toString());
			update.set("status", requestBody.get("status").toString());
			update.set("updatedDate", new Date());

			// WriteResult result = mongoTemplate.updateFirst(query, update, Course.class);
			// System.out.println(result);
			// return result.toString();

			return "A";

		} catch (Exception e) {
			return e.toString();
			// if (e.getMessage())
			// return "message: Course n ame already in dbb ";
			// else
			// return e.getMessage();
		}
	}

	public List<Course> getAllCourses() {
		return courseRepository.findAll();
	}

	public Course getCoursebyID(String id) {
		return courseRepository.findOne(id);
	}

	public String postSpecialization(Map<String, Object> requestBody) {
		Course course = courseRepository.findOne(requestBody.get("course").toString());

		if (course == null)
			return "Course " + requestBody.get("course").toString() + " Doesn't exist";

		Specialization specialization = new Specialization();
		specialization.setSpecialization(requestBody.get("specialization").toString());
		specialization.setStatus(requestBody.get("status").toString());
		specialization.setCourse(course.getId());

		Specialization newSpecialization = specializationRepository.save(specialization);
		System.out.println("Semester data saved");

		return newSpecialization.toString();
	}

	public String putSpecialization(Map<String, Object> requestBody) {

		Course course = courseRepository.findByCourse(requestBody.get("course").toString());
		Specialization oldSpecialization = specializationRepository
				.findBySpecialization(requestBody.get("oldSpecialization").toString());

		if (course == null)
			return "Course " + requestBody.get("course").toString() + " doesn't exist";

		if (oldSpecialization == null)
			return "Specialization " + requestBody.get("oldSpecialization").toString() + " doesn't exist";

		Query query = new Query();
		query.addCriteria(Criteria.where("course").is(course)
				.andOperator(Criteria.where("specialization").is(oldSpecialization)));

		Update update = new Update();
		update.set("specialization", requestBody.get("specialization").toString());
		update.set("status", requestBody.get("status").toString());
		update.set("updatedDate", new Date());

		WriteResult result = mongoTemplate.updateFirst(query, update, Specialization.class);
		System.out.println(result);

		return result.toString();
	}

	public List<Specialization> getAllSpecialization() {
		return specializationRepository.findAll();
	}

	public Specialization getSpecbyID(String id) {
		return specializationRepository.findOne(id);
	}

	public String postSemester(Map<String, Object> requestBody) {
		Course course = courseRepository.findOne(requestBody.get("course").toString());
		Specialization specialization = specializationRepository
				.findOne(requestBody.get("specialization").toString());

		if (course == null)
			return "Course " + requestBody.get("course").toString() + " Doesn't exist";

		if (specialization == null)
			return "specialization " + requestBody.get("specialization").toString() + " Doesn't exist";

		Semester semester = new Semester();
		semester.setSemester(requestBody.get("semester").toString());
		semester.setStatus(requestBody.get("status").toString());
		semester.setCourse(course.getId());
		semester.setSpecialization(specialization.getId());

		Semester newSem = semesterRepository.save(semester);
		System.out.println("Semester data saved");

		return newSem.toString();
	}

	public String putSemester(Map<String, Object> requestBody) {

		Course course = courseRepository.findByCourse(requestBody.get("course").toString());
		Specialization specialization = specializationRepository
				.findBySpecialization(requestBody.get("specialization").toString());

		if (course == null)
			return "Course " + requestBody.get("course").toString() + " Doesn't exist";

		if (specialization == null)
			return "specialization " + requestBody.get("specialization").toString() + " Doesn't exist";

		Query query = new Query();
		query.addCriteria(
				Criteria.where("course").is(course).andOperator(Criteria.where("specialization").is(specialization)));

		Update update = new Update();
		update.set("semester", requestBody.get("semester").toString());
		update.set("status", requestBody.get("status").toString());
		update.set("updatedDate", new Date());

		WriteResult result = mongoTemplate.updateFirst(query, update, Semester.class);
		System.out.println(result);

		return result.toString();
	}

	public List<Semester> findAllSemesters() {
		return semesterRepository.findAll();
	}

	public Semester getSembyId(String id) {
		return semesterRepository.findOne(id);
	}

	public Room postRoom(Room room) {
		Room newRoom = roomRepository.save(room);
		System.out.println("Room data saved");

		return newRoom;
	}

	public List<Room> findAllRooms() {
		return roomRepository.findAll();
	}

	public Room getRoombyId(String id) {
		Room newRoom = roomRepository.findOne(id);
		return newRoom;
	}

	public Subject postSubject(Subject requestBody) throws JSONException {		
		Subject newSubject = subjectRepository.save(requestBody);
		System.out.println("Subject data saved");
		return newSubject;
	}

	
	public Subject postSection(Map<String, Object> requestBody) throws JSONException {
		System.out.println(requestBody);

		Subject subject = new Subject();
		subject.setSubject(requestBody.get("subject").toString());
		subject.setStatus(requestBody.get("status").toString());
		subject.setSubject_code(requestBody.get("subjectCode").toString());	
		Subject newSubject = subjectRepository.save(subject);
		System.out.println("Subject data saved");

		return newSubject;
	}

	public List<Subject> findAllSubjects() {
		return subjectRepository.findAll();
	}

	public Subject getSubById(String id) {
		Subject sub = subjectRepository.findOne(id);
		return sub;
	}

	public Subject postSubjectBody(Map<String, Object> requestBody) {
		 Subject sub=new Subject();
		 sub.setSubject(requestBody.get("subject").toString());
		 sub.setSubject_code(requestBody.get("subject_code").toString());
		 sub.setSpecialization(requestBody.get("specialization").toString());
		 sub.setStatus(requestBody.get("status").toString());
		 subjectRepository.save(sub);
		 return sub;
	}

	public List<Specialization> getSpecByCourse(String courseId) {
		Query srchQuery=new Query();
		srchQuery.addCriteria(Criteria.where("course").is(courseId));
		return mongoTemplate.find(srchQuery, Specialization.class);
	}
	
	
	public List<Semester> getSemesterBySpecilization(String specializationId) {
		Query srchQuery=new Query();
		srchQuery.addCriteria(Criteria.where("specialization").is(specializationId));
		return mongoTemplate.find(srchQuery, Semester.class);
	}
	
	
	public List<Subject> getSubjectBySemester(String semesterId) {
		Query srchQuery=new Query();
		srchQuery.addCriteria(Criteria.where("semester").is(semesterId));
		return mongoTemplate.find(srchQuery, Subject.class);
	}
	
	
}
