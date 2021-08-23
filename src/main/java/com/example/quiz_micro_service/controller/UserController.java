package com.example.quiz_micro_service.controller;

import com.example.quiz_micro_service.model.*;
import com.example.quiz_micro_service.request.CourseRequest;
import com.example.quiz_micro_service.request.GetQuestionRequest;
import com.example.quiz_micro_service.request.SkipQuestionRequest;
import com.example.quiz_micro_service.request.StudentCourseRequest;
import com.example.quiz_microservice.response.DefaultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.text.DecimalFormat;
import java.util.*;

@RestController
public class UserController {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private final DecimalFormat decimalFormat = new DecimalFormat("000");

    //adding a new student into the Student Table
    @RequestMapping(value = "/create/user/student/", method = RequestMethod.POST)
    public ResponseEntity createStudent(@Valid @RequestBody User user){

        List<Integer> noOfStudents = jdbcTemplate.query(
                "Select count(*) as count from Student",
                (resultSet, rowNum )-> resultSet.getInt("count")
        );

        String studentID = "S" + decimalFormat.format((noOfStudents.size() == 0 ? 0 : noOfStudents.get(0))+1);

        System.out.println("student");
        jdbcTemplate.execute(
                "Insert into Student (sid, name, password, totalPoints)" +
                        "values ('" + studentID + "', '" + user.getName() + "', '" + user.getPassword() + "', 0 )"
        );

        return ResponseEntity.ok( new DefaultResponse("new student account with sid " + studentID + " created successfully.") );
    }


    //adding a new teacher into the Teacher Table
    @RequestMapping(value = "/create/user/teacher/", method = RequestMethod.POST)
    public ResponseEntity createTeacher(@Validated @RequestBody User user){

        List<Integer> noOfTeachers = jdbcTemplate.query(
                "Select count(*) as count from Teacher",
                (resultSet, rowNum )-> resultSet.getInt("count")
        );

        String teacherID = "T" + decimalFormat.format((noOfTeachers.size() == 0 ? 0 : noOfTeachers.get(0))+1);

        jdbcTemplate.execute(
                "Insert into Teacher (tid, name, password)" +
                        "values ('" + teacherID + "', '" + user.getName() + "', '" + user.getPassword() + "' )"
        );
        return ResponseEntity.ok( new DefaultResponse("new teacher account with tid " + teacherID + " created successfully.") );
    }


    //assign a course to student and adding a new entry in the StudentCourse Table
    @RequestMapping(value = "/take-course/", method = RequestMethod.POST)
    public ResponseEntity takeCourse(@Validated @RequestBody StudentCourseRequest request){

        if( isStudent(request.id, request.password) ){

            List<String> courses = jdbcTemplate.query(
                    "Select cid from Course where cid = '" + request.getCid() + "'",
                    (resultSet, n) -> resultSet.getString("cid")
            );

            //if there is no record with cid return not found http status
            if(courses.isEmpty())   return (ResponseEntity) ResponseEntity.status(HttpStatus.NOT_FOUND);

            jdbcTemplate.execute(
                    "Insert into StudentCourse (sid, cid, scid)" +
                            "values ('" + request.getId() + "', '" + request.getCid() + "', -1 )"
            );

            return ResponseEntity.ok( new DefaultResponse("student " + request.getId() + " has taken the course with cid " + request.getCid() + " successfully"));
        }
        return (ResponseEntity) ResponseEntity.status(HttpStatus.FORBIDDEN);
    }

    //get question from the database
    @RequestMapping(value = "/get-question/", method = RequestMethod.POST)
    public ResponseEntity getQuestion(@RequestBody GetQuestionRequest request){

        if( isStudent( request.getId(), request.getPassword() ) ){

            List<String> courses = jdbcTemplate.query(
                    "Select cid from StudentCourse where cid = '" + request.getCid() + "' and sid = '" + request.getId() + "'",
                    (resultSet, n) -> resultSet.getString("cid")
            );

            //if there is no record with cid return not found http status
            if(courses.isEmpty())   return (ResponseEntity) ResponseEntity.status(HttpStatus.NOT_FOUND);

            //if the student has taken the course then we get the qzid from the Course table with the help of cid
            List<String> qzId = jdbcTemplate.query(
                    "Select qzid from Course where cid = '" + request.getCid() + "'",
                    (resultSet, n) -> resultSet.getString("qzid")
            );

            //if there is no quiz associated with that course return a message.
            if( qzId.isEmpty() )    return (ResponseEntity) ResponseEntity.status(HttpStatus.valueOf("No quiz associated with this course. Please contact your teacher or try again later"));
            //if quiz found
            String quizID = qzId.get(0);
            Integer cutOff = jdbcTemplate.queryForObject( "Select cutOff from Quiz where qzid = '" + quizID + "'", Integer.class );
            Integer maxPoints = jdbcTemplate.queryForObject( "Select maxPoints from Quiz where qzid = '" + quizID + "'", Integer.class );
            Integer maxTime = jdbcTemplate.queryForObject( "Select maxTime from Quiz where qzid = '" + quizID + "'", Integer.class );
            Quiz quiz = new Quiz(quizID, cutOff, maxPoints, maxTime);

            //if the total points of the student reached to maximum points end the quiz
            if(request.getPoints() >= quiz.getMaxPoints()){
                addScoreToDatabase( request );
                return ResponseEntity.ok( new DefaultResponse("Quiz " + quiz.getQzid() +" completed successfully") );
            }

            //get all the questionIds of that quiz
            List<String> questionIds  = jdbcTemplate.query(
                    "Select qid from Question where difficultyLevel >= '" + request.getDifficultyLevel() +"' and qid in ( Select qid from QuizQuestion where qzid = '"  + quizID + "' ) order by difficultyLevel ",
                    (resultSet, n) -> resultSet.getString("qid")
            );

            if(questionIds.isEmpty()){
                addScoreToDatabase( request );
                return ResponseEntity.ok( new DefaultResponse("Quiz " + quiz.getQzid() +" completed successfully") );
            }

            //get Question from the Question table
            Question question = new Question();
            String ques = jdbcTemplate.queryForObject("Select question from Question where qid = '" + questionIds.get(0) + "'", String.class);
            List<String> answers = jdbcTemplate.query(
                    "Select answer from Answer where qid = '" + questionIds.get(0) + "'",
                    (res, n) -> res.getString("answer")
            );
            List<String> options = jdbcTemplate.query(
                    "Select option from Option where qid = '" + questionIds.get(0) + "'",
                    (res, n) -> res.getString("option")
            );
            Integer diff = jdbcTemplate.queryForObject("Select difficultyLevel from Question where qid = '" + questionIds.get(0) + "'", Integer.class);
            Integer points = jdbcTemplate.queryForObject("Select points from Question where qid = '" + questionIds.get(0) + "'", Integer.class);
            question.setQuestion(ques);
            question.setAnswers(answers);
            question.setOptions(options);
            question.setDifficultyLevel(diff);
            question.setPoints(points);
            question.setQid(questionIds.get(0));

            return ResponseEntity.ok( question );
        }
        return (ResponseEntity) ResponseEntity.status(HttpStatus.FORBIDDEN);
    }

    private void addScoreToDatabase(GetQuestionRequest request) {
        //calculate the scoreId
        Integer noOfScores = jdbcTemplate.queryForObject("Select count(*) from Score", Integer.class );
        String scoreId = "SC" + decimalFormat.format(noOfScores+1);

        //insert score into the Score table
        jdbcTemplate.execute(
                "Insert into Score (scid, xp, tsp) values ( '" + scoreId + "', " + request.getCorrectAnswers() + ", " + request.getPoints() + " )"
        );

        //assign score to the StudentCourse Table;
        jdbcTemplate.execute(
                "Update StudentCourse set  scid = '" + scoreId +"' where cid = '" + request.getCid() + "'"
        );
    }

    //skip the question and return another question
    @RequestMapping(value = "/skip-question/", method = RequestMethod.POST)
    public ResponseEntity skipQuestion(@RequestBody SkipQuestionRequest request){

        if( isStudent( request.getId(), request.getPassword() ) ){

            //getting the maximum points from tag points associated with this question
            Integer maxPoints = jdbcTemplate.queryForObject("Select max(points) from tag where qid = '" + request.getQid() +"'", Integer.class);
            if(request.getPoints() < maxPoints)     return (ResponseEntity) ResponseEntity.status(HttpStatus.BAD_REQUEST);

            //get all the questionIds of that quiz
            List<String> questionIds  = jdbcTemplate.query(
                    "Select qid from Question where difficultyLevel <= '" + request.getDifficultyLevel() +"' and qid in ( Select qid from QuizQuestion where qzid = '"  + request.getQzid() + "' ) order by difficultyLevel desc",
                    (resultSet, n) -> resultSet.getString("qid")
            );


            if(questionIds.isEmpty()){
                addScoreToDatabase( new GetQuestionRequest( request.getId(), request.getPassword(), request.getCid(), request.getDifficultyLevel(), request.getPoints()-maxPoints, request.getCorrectAnswers() ) );
                return ResponseEntity.ok( new DefaultResponse("Quiz " + request.getQzid() +" completed successfully with " + (request.getPoints()-maxPoints) + " TSP and " + request.getCorrectAnswers()) );
            }

            //get Question from the Question table
            Question question = new Question();
            String ques = jdbcTemplate.queryForObject("Select question from Question where qid = '" + questionIds.get(0) + "'", String.class);
            List<String> answers = jdbcTemplate.query(
                    "Select answer from Answer where qid = '" + questionIds.get(0) + "'",
                    (res, n) -> res.getString("answer")
            );
            List<String> options = jdbcTemplate.query(
                    "Select option from Option where qid = '" + questionIds.get(0) + "'",
                    (res, n) -> res.getString("option")
            );
            Integer diff = jdbcTemplate.queryForObject("Select difficultyLevel from Question where qid = '" + questionIds.get(0) + "'", Integer.class);
            Integer points = jdbcTemplate.queryForObject("Select points from Question where qid = '" + questionIds.get(0) + "'", Integer.class);
            question.setQuestion(ques);
            question.setAnswers(answers);
            question.setOptions(options);
            question.setDifficultyLevel(diff);
            question.setPoints(points);
            question.setQid(questionIds.get(0));

            return ResponseEntity.ok( question );
        }
        return (ResponseEntity) ResponseEntity.status(HttpStatus.FORBIDDEN);
    }

    public boolean isEqual(List<String> list1, List<String> list2){

        if(list1.size() != list2.size())        return false;
        HashSet<String> set = new HashSet<>();
        for (String s : list1)
            set.add( s );

        for(String s : list2)
            if( !set.contains(s) )      return false;
        return true;
    }

    private boolean isStudent(String id, String password) {

        List<String> passwords = jdbcTemplate.query(
                "Select password from Student where sid = '"+ id + "'",
                (resultSet, rowNum) -> resultSet.getString("password")
        );

        //if there is no student with this tid then return false
        if(id == null || id.isEmpty())        return false;

        //if password doesn't match the password return false;
        if( !password.equals(passwords.get(0)) )    return false;

        //if everything is fine return true;
        return true;
    }
}
