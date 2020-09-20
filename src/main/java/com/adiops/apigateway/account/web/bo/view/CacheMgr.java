package com.adiops.apigateway.account.web.bo.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.adiops.apigateway.account.web.bo.LearningPathBO;
import com.adiops.apigateway.course.resourceobject.CourseRO;
import com.adiops.apigateway.module.resourceobject.ModuleRO;
import com.adiops.apigateway.question.resourceobject.QuestionRO;
import com.adiops.apigateway.topic.resourceobject.TopicRO;

public class CacheMgr {

	private static Map<String, CourseRO> courseROs = new TreeMap<>();

	private static Map<String, ArrayList<String>> courseModuleAssigns = new TreeMap<>();

	private static Map<String, ModuleRO> moduleROs = new TreeMap<>();

	private static Map<String, TopicRO> topicROs = new TreeMap<>();

	private static Map<String, QuestionRO> questionROs = new TreeMap<>();

	private static LRUCache<String, LearningPathBO> learningPathBOs = new LRUCache<>(20);

	private static Map<String, ArrayList<String>> moduleTopicAssigns = new TreeMap<>();

	private static Map<String, ArrayList<String>> topicQuestionAssigns = new TreeMap<>();

	public static void addCourseRO(CourseRO tCourseRO) {
		courseROs.put(tCourseRO.getKeyid(), tCourseRO);
	}

	public static void addModuleRO(ModuleRO tModuleRO) {
		moduleROs.put(tModuleRO.getKeyid(), tModuleRO);
	}

	public static void addTopicRO(TopicRO tTopicRO) {
		topicROs.put(tTopicRO.getKeyid(), tTopicRO);
	}

	public static void addQuestionRO(QuestionRO tQuestionRO) {
		questionROs.put(tQuestionRO.getKeyid(), tQuestionRO);
	}

	public static CourseRO getCourseRO(String key) {
		return courseROs.get(key);
	}

	public static Collection<CourseRO> getCourseROs() {
		return courseROs.values();
	}

	public static void setCourseROs(Map<String, CourseRO> courseROs) {
		CacheMgr.courseROs = courseROs;
	}

	public static ModuleRO getModuleRO(String key) {
		return moduleROs.get(key);
	}

	public static Collection<ModuleRO> getModuleROs() {
		return moduleROs.values();
	}

	public static void addCourseModuleROs(String courseKey, String moduleKey) {
		if (courseModuleAssigns.containsKey(courseKey)) {
			courseModuleAssigns.get(courseKey).add(moduleKey);
		} else {
			ArrayList<String> aList= new ArrayList<String>();
			aList.add(moduleKey);
			courseModuleAssigns.put(courseKey, aList);
		}
	}

	public static Collection<ModuleRO> getCourseModuleROs(String courseKey) {
		if (courseModuleAssigns.containsKey(courseKey)) {
			List<ModuleRO> tModuleROs = courseModuleAssigns.get(courseKey).stream().map(key -> getModuleRO(key))
					.collect(Collectors.toList());
			return tModuleROs;
		}
		return Collections.emptyList();
	}

	public static void setModuleROs(Map<String, ModuleRO> moduleROs) {
		CacheMgr.moduleROs = moduleROs;
	}

	public static TopicRO getTopicRO(String key) {
		return topicROs.get(key);
	}

	public static Collection<TopicRO> getTopicROs() {
		return topicROs.values();
	}

	public static void setTopicROs(Map<String, TopicRO> topicROs) {
		CacheMgr.topicROs = topicROs;
	}

	public static void addModuleTopicROs(String moduleKey, String topicKey) {
		if (moduleTopicAssigns.containsKey(moduleKey)) {
			moduleTopicAssigns.get(moduleKey).add(topicKey);
		} else {
			ArrayList<String> aList= new ArrayList<String>();
			aList.add(topicKey);
			moduleTopicAssigns.put(moduleKey, aList);
		}
	}

	public static Collection<TopicRO> getModuleTopicROs(String courseKey) {
		if (moduleTopicAssigns.containsKey(courseKey)) {
			List<TopicRO> tTopicROs = moduleTopicAssigns.get(courseKey).stream().map(key -> getTopicRO(key))
					.collect(Collectors.toList());
			return tTopicROs;
		}
		return Collections.emptyList();
	}

	public static QuestionRO getQuestionRO(String key) {
		return questionROs.get(key);
	}

	public static Collection<QuestionRO> getQuestionROs() {
		return questionROs.values();
	}

	public static void setQuestionROs(Map<String, QuestionRO> questionROs) {
		CacheMgr.questionROs = questionROs;
	}

	public static void addTopicQuestionROs(String topicKey, String questionKey) {
		if (topicQuestionAssigns.containsKey(topicKey)) {
			topicQuestionAssigns.get(topicKey).add(questionKey);
		} else {
			ArrayList<String> aList= new ArrayList<String>();
			aList.add(questionKey);
			topicQuestionAssigns.put(topicKey, aList);
		}
	}

	public static Collection<QuestionRO> getTopicQuestionROs(String topicKey) {
		if (topicQuestionAssigns.containsKey(topicKey)) {
			List<QuestionRO> tQuestionROs = topicQuestionAssigns.get(topicKey).stream().map(key -> getQuestionRO(key))
					.collect(Collectors.toList());
			return tQuestionROs;
		}
		return Collections.emptyList();
	}

	public static void addLearningPathBO(LearningPathBO tLearningPathBO) {
		learningPathBOs.put(tLearningPathBO.getKeyId(), tLearningPathBO);
	}

	public static LearningPathBO getLearningPathBO(String key) {
		return learningPathBOs.get(key);
	}

	public static LRUCache<String, LearningPathBO> getLearningPathBOs() {
		return learningPathBOs;
	}

	public static void setLearningPathBOs(LRUCache<String, LearningPathBO> learningPathBOs) {
		CacheMgr.learningPathBOs = learningPathBOs;
	}

	public static void clear() {
		courseROs.clear();
		moduleROs.clear();
		topicROs.clear();
		questionROs.clear();
		learningPathBOs.clear();
	}

	public static void clearByKey(String key) {
		courseROs.remove(key);
		moduleROs.remove(key);
		topicROs.remove(key);
		questionROs.remove(key);
		learningPathBOs.remove(key);
	}
}
