package com.example.quiz_micro_service.controller;

import com.example.quiz_micro_service.request.CourseRequest;
import com.example.quiz_micro_service.request.DeleteResourceRequest;
import com.example.quiz_micro_service.request.UpdateResourceRequest;
import com.example.quiz_microservice.response.DefaultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.*;

@RestController
public class CourseController {

    @Autowired
    JdbcTemplate jdbcTemplate;
    private final DecimalFormat decimalFormat = new DecimalFormat("000");

    @RequestMapping(value = "/course/add/", method = RequestMethod.POST)
    public ResponseEntity addCourse(@RequestBody CourseRequest courseRequest){

        if( isTeacher( courseRequest.getId(), courseRequest.getPassword() ) ){

            //getting the no. of courses present in the table to assign a new course id
            List<Integer> noOfCourses = jdbcTemplate.query(
                    "Select count(*) as count from Course",
                    (resultSet, rowNum) -> resultSet.getInt("count")
            );
            String courseID = "C" + decimalFormat.format(noOfCourses.get(0)+1);

            //create entry of a new course in Course table
            jdbcTemplate.execute(
                    "Insert into Course (cid, qzid, lid)"+
                            "values ('" + courseID + "', '"+ courseRequest.getQzid() + "', '" + courseRequest.getLid() + "' )"
            );
            return ResponseEntity.ok( new DefaultResponse("new course added with cid " + courseID));
        }

        return (ResponseEntity) ResponseEntity.status(HttpStatus.FORBIDDEN);
    }

    @RequestMapping(value = "/course/delete/", method = RequestMethod.DELETE)
    public ResponseEntity deleteCourseById(@RequestBody DeleteResourceRequest request){

        if( isTeacher(request.getId(), request.getPassword()) ){

            List<String> courses = jdbcTemplate.query(
                    "Select cid from Course where cid = '" + request.getRid() + "'",
                    (resultSet, n) -> resultSet.getString("cid")
            );

            //if there is no record with cid return not found http status
            if(courses.isEmpty())   return (ResponseEntity) ResponseEntity.status(HttpStatus.NOT_FOUND);

            //if it contains a course with this cid then delete it from the table
            jdbcTemplate.execute(
                    "Delete from Course where cid = '" + request.getRid()+ "'"
            );
            return ResponseEntity.ok( new DefaultResponse("course with cid  " + request.getRid() + " deleted successfully"));
        }
        return (ResponseEntity) ResponseEntity.status(HttpStatus.FORBIDDEN);
    }

    @RequestMapping(value = "/course/update/", method = RequestMethod.POST)
    public ResponseEntity updateCourseById(@RequestBody UpdateResourceRequest request){

        if( isTeacher(request.getId(), request.getPassword()) ){
            List<String> courses = jdbcTemplate.query(
                    "Select cid from Course where cid = '" + request.getRid() + "'",
                    (resultSet, n) -> resultSet.getString("cid")
            );

            //if there is no record with cid return not found http status
            if(courses.isEmpty())   return (ResponseEntity) ResponseEntity.status(HttpStatus.NOT_FOUND);

            //if it contains a course with this cid then update the column with the new value
            jdbcTemplate.execute(
                    "Update Course set '" + request.getColName() + "' = '" + request.getValue() + "' where cid = '" + request.getRid() + "'"
            );
            return ResponseEntity.ok( new DefaultResponse("course with cid  " + request.getRid() + " updated successfully with" + request.getColName() + " as " + request.getValue()));
        }
        return (ResponseEntity) ResponseEntity.status(HttpStatus.FORBIDDEN);
    }

    private boolean isTeacher(String tid, String password) {

        List<String> passwords = jdbcTemplate.query(
                "Select password from Teacher where tid = '"+ tid + "'",
                (resultSet, rowNum) -> resultSet.getString("tid")
        );

        //if there is no teacher with this tid then return false
        if(tid == null || tid.isEmpty())        return false;

        //if password doesn't match the password return false;
        if( !password.equals(passwords.get(0)) )    return false;

        //if everything is fine return true;
        return true;
    }
}
