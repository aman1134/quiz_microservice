package com.example.quiz_micro_service.controller;

import com.example.quiz_micro_service.request.*;
import com.example.quiz_microservice.response.DefaultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.DecimalFormat;
import java.util.*;

public class QuestionController {

    @Autowired
    JdbcTemplate jdbcTemplate;
    private final DecimalFormat decimalFormat = new DecimalFormat("###");

    @RequestMapping(value = "/question/add/", method = RequestMethod.POST)
    public ResponseEntity addQuestion(@RequestBody QuestionRequest request){

        if( isTeacher( request.getId(), request.getPassword() ) ){

            ArrayList<String> answers = request.getAnswers();
            //if there is no answer we cannot add the question
            if( answers.isEmpty() )      return (ResponseEntity) ResponseEntity.status(HttpStatus.valueOf("Question cannot be added."));

            ArrayList<String> options = request.getOptions();
            //if there is no option we cannot add the question
            if( options.isEmpty() )      return (ResponseEntity) ResponseEntity.status(HttpStatus.valueOf("Question cannot be added."));

            //getting the no. of questions present in the table to assign a new question id
            List<Integer> noOfQuestions = jdbcTemplate.query(
                    "Select count(*) as count from Question",
                    (resultSet, rowNum) -> resultSet.getInt("count")
            );
            String questionID = "Q" + decimalFormat.format(noOfQuestions.get(0)+1);

            //create entry of a new question in Question table
            jdbcTemplate.execute(
                    "Insert into Question (qid, question, points, difficultyLevel)"+
                            "values ('" + questionID + "', '" + request.getQuestion() + "', " + request.getPoints() + ", " + request.getDifficultyLevel()+" )"
            );

            for(String answer : answers)
                addAnswerOfQuestion(new AnswerRequest(request.getId(), request.getPassword(), questionID, answer));
            for(String option : options)
                addOptionOfQuestion(new OptionRequest(request.getId(), request.getPassword(), questionID, option));

            return ResponseEntity.ok( new DefaultResponse("new quiz added with qid " + questionID));
        }
        return (ResponseEntity) ResponseEntity.status(HttpStatus.FORBIDDEN);
    }

    @RequestMapping(value = "/question/add-tag/", method = RequestMethod.POST)
    public ResponseEntity addTagToQuestion(@RequestBody TagRequest request){

        List<String> questions = jdbcTemplate.query(
                "Select qid from Question where qid = '" + request.getQid(),
                (resultSet, n) -> resultSet.getString("qid")
        );

        //if there is no record with qid return not found http status
        if(questions.isEmpty())   return (ResponseEntity) ResponseEntity.status(HttpStatus.NOT_FOUND);

        //add a new tag to the Question into the Tag table
        jdbcTemplate.execute(
                "Insert into Tag (qid, tag, points)"+
                        "values ('" + request.getQid() + "', '" + request.getTag() + "', '" + request.getPoints() + "' )"
        );
        return ResponseEntity.ok( new DefaultResponse("new tag " + request.getTag() +" added to question qid " + request.getQid() + "successfully.") );
    }


    @RequestMapping(value = "/question/add-answer/", method = RequestMethod.POST)
    public ResponseEntity addAnswerOfQuestion(@RequestBody AnswerRequest request){

        if( isTeacher( request.getId(), request.getPassword()) ) {
            List<String> questions = jdbcTemplate.query(
                    "Select qid from Question where qid = '" + request.getQid() + "'",
                    (resultSet, n) -> resultSet.getString("qid")
            );

            //if there is no record with qid return not found http status
            if (questions.isEmpty()) return (ResponseEntity) ResponseEntity.status(HttpStatus.NOT_FOUND);

            //add a new tag to the Question into the Tag table
            jdbcTemplate.execute(
                    "Insert into Answer (qid, answer)" +
                            "values ('" + request.getQid() + "', '" + request.getAnswer() + "' )"
            );
            return ResponseEntity.ok(new DefaultResponse("new answer " + request.getAnswer() + " added of the question qid " + request.getQid() + "successfully."));
        }
        return (ResponseEntity) ResponseEntity.status(HttpStatus.FORBIDDEN);
    }

    @RequestMapping(value = "/question/add-option/", method = RequestMethod.POST)
    public ResponseEntity addOptionOfQuestion(@RequestBody OptionRequest request){

        if( isTeacher( request.getId(), request.getPassword()) ) {
            List<String> questions = jdbcTemplate.query(
                    "Select qid from Question where qid = '" + request.getQid() + "'",
                    (resultSet, n) -> resultSet.getString("qid")
            );

            //if there is no record with qid return not found http status
            if (questions.isEmpty()) return (ResponseEntity) ResponseEntity.status(HttpStatus.NOT_FOUND);

            //add a new tag to the Question into the Tag table
            jdbcTemplate.execute(
                    "Insert into Option (qid, option)" +
                            "values ('" + request.getQid() + "', '" + request.getOption() + "' )"
            );
            return ResponseEntity.ok(new DefaultResponse("new option " + request.getOption() + " added of the question qid " + request.getQid() + "successfully."));
        }
        return (ResponseEntity) ResponseEntity.status(HttpStatus.FORBIDDEN);
    }


    @RequestMapping(value = "/question/add-answer/", method = RequestMethod.POST)
    public ResponseEntity deleteAnswerOfQuestion(@RequestBody AnswerRequest request){

        if( isTeacher( request.getId(), request.getPassword()) ) {
            List<String> questions = jdbcTemplate.query(
                    "Select qid from Question where qid = '" + request.getQid() + "'",
                    (resultSet, n) -> resultSet.getString("qid")
            );

            //if there is no record with qid return not found http status
            if (questions.isEmpty()) return (ResponseEntity) ResponseEntity.status(HttpStatus.NOT_FOUND);

            //if there is only a single answer for that question it will not be deleted and semd a
            if(questions.size() == 1) return (ResponseEntity) ResponseEntity.status(HttpStatus.valueOf("Answer cannot be deleted."));

            //add a new tag to the Question into the Tag table
            jdbcTemplate.execute(
                    "Delete from Answer where qid = '" + request.getQid() + "' and answer = '" + request.getAnswer() + "'"
            );
            return ResponseEntity.ok(new DefaultResponse(" answer of the question qid " + request.getQid() + "successfully."));
        }
        return (ResponseEntity) ResponseEntity.status(HttpStatus.FORBIDDEN);
    }

    @RequestMapping(value = "/question/delete/", method = RequestMethod.DELETE)
    public ResponseEntity deleteQuestionById(@RequestBody DeleteResourceRequest request){

        if( isTeacher(request.getId(), request.getPassword()) ){

            List<String> questions = jdbcTemplate.query(
                    "Select qid from Question where qid = '" + request.getRid() + "'",
                    (resultSet, n) -> resultSet.getString("qid")
            );

            //if there is no record with qid return not found http status
            if(questions.isEmpty())   return (ResponseEntity) ResponseEntity.status(HttpStatus.NOT_FOUND);

            //if it contains a question with this cid then delete it from the table
            jdbcTemplate.execute(
                    "Delete from Question where qid = '" + request.getRid() + "'"
            );
            return ResponseEntity.ok( new DefaultResponse("Question with qid  " + request.getRid() + " deleted successfully"));
        }
        return (ResponseEntity) ResponseEntity.status(HttpStatus.FORBIDDEN);
    }

    @RequestMapping(value = "/question/update/", method = RequestMethod.POST)
    public ResponseEntity updateQuestionById(@RequestBody UpdateResourceRequest request){

        if( isTeacher(request.getId(), request.getPassword()) ){
            List<String> courses = jdbcTemplate.query(
                    "Select qid from Question where qid = '" + request.getRid() + "'",
                    (resultSet, n) -> resultSet.getString("qid")
            );

            //if there is no record with qid return not found http status
            if(courses.isEmpty())   return (ResponseEntity) ResponseEntity.status(HttpStatus.NOT_FOUND);

            //if it contains a course with this qid then update the column with the new value
            jdbcTemplate.execute(
                    "Update Question set " + request.getColName() + " = '" + request.getValue() + "' where qid = '" + request.getRid() + "'"
            );
            return ResponseEntity.ok( new DefaultResponse("Question with qid  " + request.getRid() + " updated successfully with" + request.getColName() + " as " + request.getValue()));
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
