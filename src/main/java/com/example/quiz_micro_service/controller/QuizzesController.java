package com.example.quiz_micro_service.controller;

import com.example.quiz_micro_service.request.DeleteResourceRequest;
import com.example.quiz_micro_service.request.QuizQuestionRequest;
import com.example.quiz_micro_service.request.QuizRequest;
import com.example.quiz_micro_service.request.UpdateResourceRequest;
import com.example.quiz_micro_service.response.DefaultErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.util.List;

@RestController
public class QuizzesController {

    @Autowired
    JdbcTemplate jdbcTemplate;
    private final DecimalFormat decimalFormat = new DecimalFormat("000");

    @RequestMapping(value = "/quiz/add/", method = RequestMethod.POST)
    public ResponseEntity addQuiz(@RequestBody QuizRequest request){

        if( isTeacher( request.getId(), request.getPassword() ) ){

            //getting the no. of quiz present in the table to assign a new quiz id
            List<Integer> noOfQuiz = jdbcTemplate.query(
                    "Select count(*) as count from Quiz",
                    (resultSet, rowNum) -> resultSet.getInt("count")
            );
            String quizID = "QZ" + decimalFormat.format(noOfQuiz.get(0)+1);

            //create entry of a new quiz in Quiz table
            jdbcTemplate.execute(
                    "Insert into Quiz (qzid, cutOff, maxTime, maxPoints)"+
                            "values ('" + quizID + "', " + request.getCutOff() + ", " + request.getMaxTime() + ", " + request.getMaxPoints()+" )"
            );
            return ResponseEntity.ok( new com.example.quiz_microservice.response.DefaultResponse("new quiz added with qzid " + quizID));
        }
        return ResponseEntity.ok(new DefaultErrorResponse("You are not authorized."));
    }

    @RequestMapping(value = "/quiz/add-question/", method = RequestMethod.POST)
    public ResponseEntity addQuestionToQuiz(@RequestBody QuizQuestionRequest request){

        if( isTeacher( request.getId(), request.getPassword() ) ){

            List<String> questions = jdbcTemplate.query(
                    "Select qid from Question where qid = '" + request.getQid() + "'",
                    (resultSet, n) -> resultSet.getString("qid")
            );

            //if there is no record with qid return not found http status
            if(questions.isEmpty())   return (ResponseEntity) ResponseEntity.status(HttpStatus.NOT_FOUND);

            List<String> quizList = jdbcTemplate.query(
                    "Select qzid from Quiz where qzid = '" + request.getQzid() + "'",
                    (resultSet, n) -> resultSet.getString("qzid")
            );

            //if there is no record with qzid return not found http status
            if(quizList.isEmpty())   return (ResponseEntity) ResponseEntity.status(HttpStatus.NOT_FOUND);

            //create entry of a new quiz in Quiz table
            jdbcTemplate.execute(
                    "Insert into QuizQuestion (qzid, qid)"+
                            "values ('" + request.getQzid() + "', '" + request.getQid() + "' )"
            );
            return ResponseEntity.ok( new com.example.quiz_microservice.response.DefaultResponse("new question added with qid " + request.getQid() + " to the quiz qzid " + request.getQzid()));
        }
        return (ResponseEntity) ResponseEntity.status(HttpStatus.FORBIDDEN);
    }

    @RequestMapping(value = "/quiz/delete/", method = RequestMethod.DELETE)
    public ResponseEntity deleteQuizById(@RequestBody DeleteResourceRequest request){

        if( isTeacher(request.getId(), request.getPassword()) ){

            List<String> quizList = jdbcTemplate.query(
                    "Select qzid from Quiz where qid = '" + request.getRid() + "'",
                    (resultSet, n) -> resultSet.getString("qid")
            );

            //if there is no record with qid return not found http status
            if(quizList.isEmpty())   return (ResponseEntity) ResponseEntity.status(HttpStatus.NOT_FOUND);

            //if it contains a course with this cid then delete it from the table
            jdbcTemplate.execute(
                    "Delete from Quiz where qzid = '" + request.getRid() + "'"
            );
            return ResponseEntity.ok( new com.example.quiz_microservice.response.DefaultResponse("Quiz with qid  " + request.getRid() + " deleted successfully"));
        }
        return (ResponseEntity) ResponseEntity.status(HttpStatus.FORBIDDEN);
    }

    @RequestMapping(value = "/quiz/update/", method = RequestMethod.POST)
    public ResponseEntity updateQuizById(@RequestBody UpdateResourceRequest request){

        if( isTeacher(request.getId(), request.getPassword()) ){
            List<String> quizList = jdbcTemplate.query(
                    "Select qzid from Quiz where qzid = '" + request.getRid() + "'",
                    (resultSet, n) -> resultSet.getString("qzid")
            );

            //if there is no record with qzid return not found http status
            if(quizList.isEmpty())   return (ResponseEntity) ResponseEntity.ok( new DefaultErrorResponse("not found"));

            //if it contains a course with this qid then update the column with the new value
            jdbcTemplate.execute(
                    "Update Quiz set " + request.getColName() + " = '" + request.getValue() + "' where qzid = '" + request.getRid() + "'"
            );
            return ResponseEntity.ok( new com.example.quiz_microservice.response.DefaultResponse("Quiz with qzid  " + request.getRid() + " updated successfully with" + request.getColName() + " as " + request.getValue()));
        }
        return (ResponseEntity) ResponseEntity.status(HttpStatus.FORBIDDEN);
    }

    private boolean isTeacher(String tid, String password) {

        List<String> passwords = jdbcTemplate.query(
                "Select password from Teacher where tid = '"+ tid + "'",
                (resultSet, rowNum) -> resultSet.getString("password")
        );

        //if there is no teacher with this tid then return false
        if(tid == null || tid.isEmpty())        return false;

        //if password doesn't match the password return false;
        if( !password.equals(passwords.get(0)) )    return false;

        //if everything is fine return true;
        return true;
    }
}
