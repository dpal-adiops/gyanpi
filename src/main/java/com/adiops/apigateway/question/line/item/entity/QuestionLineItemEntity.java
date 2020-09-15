package com.adiops.apigateway.question.line.item.entity;

import java.util.Date;
import java.util.Set;
import java.util.HashSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import org.hibernate.annotations.GenericGenerator;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;
import com.adiops.apigateway.topic.line.group.entity.TopicLineGroupEntity;
/**
 * The Main data entity class for QuestionLineItem Data
 * @author Deepak Pal
 *
 */
@Entity(name = "question_line_item")
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"keyid"})})
public class QuestionLineItemEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	@CsvDate(value = "dd/MM/yyyy")
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModified;	
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date createdDate;
	
	public Long getId(){
		return id;
	}
	
	public void setId(Long uuid){
		id=uuid;
		
	}

	@Column(name="keyid")
	@CsvBindByPosition(position = 0)
	private String keyid;
	@Column(name="status")
	@CsvBindByPosition(position = 1)
	private Integer status;
	@Column(name="score")
	@CsvBindByPosition(position = 2)
	private Integer score;
	@Column(name="max_score")
	@CsvBindByPosition(position = 3)
	private Integer maxScore;
	@Column(name="title")
	@CsvBindByPosition(position = 4)
	private String title;
	@Column(name="description")
	@CsvBindByPosition(position = 5)
	private String description;
	@Column(name="user_key")
	@CsvBindByPosition(position = 6)
	private String userKey;
	@Column(name="course_key")
	@CsvBindByPosition(position = 7)
	private String courseKey;
	@Column(name="module_key")
	@CsvBindByPosition(position = 8)
	private String moduleKey;
	@Column(name="topic_key")
	@CsvBindByPosition(position = 9)
	private String topicKey;
	@Column(name="question_key")
	@CsvBindByPosition(position = 10)
	private String questionKey;
	@Column(name="answer")
	@CsvBindByPosition(position = 11)
	private String answer;
	@Column(name="correct_answer")
	@CsvBindByPosition(position = 12)
	private String correctAnswer;
	@Column(name="question_type")
	@CsvBindByPosition(position = 13)
	private Integer questionType;
	
	
	
	
	 @ManyToOne
	 @JoinColumn(name="topic_line_group_id", nullable=true)	 
	 private TopicLineGroupEntity topic_line_group;
	
	
	
	
	public void setKeyid(String keyid){
		this.keyid=keyid;
	}
	
	public String getKeyid(){
		return this.keyid;
	}
	
	
	
	public void setStatus(Integer status){
		this.status=status;
	}
	
	public Integer getStatus(){
		return this.status;
	}
	
	
	
	public void setScore(Integer score){
		this.score=score;
	}
	
	public Integer getScore(){
		return this.score;
	}
	
	
	
	public void setMaxScore(Integer maxScore){
		this.maxScore=maxScore;
	}
	
	public Integer getMaxScore(){
		return this.maxScore;
	}
	
	
	
	public void setTitle(String title){
		this.title=title;
	}
	
	public String getTitle(){
		return this.title;
	}
	
	
	
	public void setDescription(String description){
		this.description=description;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	
	
	public void setUserKey(String userKey){
		this.userKey=userKey;
	}
	
	public String getUserKey(){
		return this.userKey;
	}
	
	
	
	public void setCourseKey(String courseKey){
		this.courseKey=courseKey;
	}
	
	public String getCourseKey(){
		return this.courseKey;
	}
	
	
	
	public void setModuleKey(String moduleKey){
		this.moduleKey=moduleKey;
	}
	
	public String getModuleKey(){
		return this.moduleKey;
	}
	
	
	
	public void setTopicKey(String topicKey){
		this.topicKey=topicKey;
	}
	
	public String getTopicKey(){
		return this.topicKey;
	}
	
	
	
	public void setQuestionKey(String questionKey){
		this.questionKey=questionKey;
	}
	
	public String getQuestionKey(){
		return this.questionKey;
	}
	
	
	
	public void setAnswer(String answer){
		this.answer=answer;
	}
	
	public String getAnswer(){
		return this.answer;
	}
	
	
	
	public void setCorrectAnswer(String correctAnswer){
		this.correctAnswer=correctAnswer;
	}
	
	public String getCorrectAnswer(){
		return this.correctAnswer;
	}
	
	
	
	public void setQuestionType(Integer questionType){
		this.questionType=questionType;
	}
	
	public Integer getQuestionType(){
		return this.questionType;
	}
	
	
	
	
	
	
	
	public void setTopicLineGroup(TopicLineGroupEntity topic_line_group){
		this.topic_line_group=topic_line_group;
	}
	
	public TopicLineGroupEntity getTopicLineGroup(){
		return this.topic_line_group;
	}
	
	
	
}
