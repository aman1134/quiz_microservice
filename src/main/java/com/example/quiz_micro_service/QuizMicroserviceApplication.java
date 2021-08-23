package com.example.quiz_micro_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class QuizMicroserviceApplication implements CommandLineRunner {

	@Autowired
	private JdbcTemplate jdbcTemplate;


	public static void main(String[] args) {
		SpringApplication.run(QuizMicroserviceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		//creating Teacher table if not exists
		String createTeacherTable = "Create table if not exists Teacher(tid varchar(5), name varchar(100), password varchar(100))";
		jdbcTemplate.execute( createTeacherTable );

		//creating Student table if not exists
		String createStudentTable = "Create table if not exists Student(sid varchar(5), name varchar(100), password varchar(100), totalPoints int)";
		jdbcTemplate.execute( createStudentTable );

		//creating StudentCourse table if not exists
		String createStudentCourseTable = "Create table if not exists StudentCourse(sid varchar(5), cid varchar(5), scid varchar(5))";
		jdbcTemplate.execute( createStudentCourseTable );

		//creating Score table if not exists
		String createScoreTable = "Create table if not exists Score(scid varchar(5), xp int, tsp int)";
		jdbcTemplate.execute( createScoreTable );

		//creating Course table if not exists
		String createCourseTable = "Create table if not exists Course(cid varchar(5), qzid varchar(5), lid varchar(5))";
		jdbcTemplate.execute( createCourseTable );

		//creating Leaderboard table if not exists
		String createLeaderboardTable = "Create table if not exists Leaderboard(lid varchar(5), sid varchar(5), xp int)";
		jdbcTemplate.execute( createLeaderboardTable );

		//creating Quiz table if not exists
		String createQuizTable = "Create table if not exists Quiz(qzid varchar(5), cutoff int, maxTime int, maxPoints int)";
		jdbcTemplate.execute(createQuizTable);

		//creating QuizQuestion table if not exists
		String createQuizQuestionTable = "Create table if not exists QuizQuestion(qzid varchar(5), qid varchar(5))";
		jdbcTemplate.execute( createQuizQuestionTable );

		//creating Question table if not exists
		String createQuestionTable = "Create table if not exists Question(qid varchar(5), question varchar(100), points int, difficultyLevel int)";
		jdbcTemplate.execute( createQuestionTable );

		//creating Answer table if not exists
		String createAnswerTable = "Create table if not exists Answer(qid varchar(5), answer varchar(100))";
		jdbcTemplate.execute( createAnswerTable );

		//creating Option table if not exists
		String createOptionTable = "Create table if not exists Option(qid varchar(5), option varchar(100))";
		jdbcTemplate.execute( createOptionTable );

		//creating Tag table if not exists
		String createTagTable = "Create table if not exists Tag(qid varchar(5), tag varchar(10), points int)";
		jdbcTemplate.execute( createTagTable );

		System.out.println("running");
	}

	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}


}
