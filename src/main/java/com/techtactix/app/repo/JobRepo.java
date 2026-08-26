package com.techtactix.app.repo;

import java.util.Arrays;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techtactix.app.model.JobPost;

@Repository
public interface JobRepo extends JpaRepository<JobPost, Integer> {

	

	public List<JobPost> findByPostProfileContainingOrPostDescContaining(String postProfile,String postDescription);









}

























//private final JobPost jobPost;
//// ArrayList to store JobPost objects
//List<JobPost> jobs = new ArrayList<>(Arrays.asList(
//
//        new JobPost(1, "Java Developer", "Must have good experience in core Java and advanced Java", 2,
//                List.of("Core Java", "J2EE", "Spring Boot", "Hibernate")),
//
//
//        new JobPost(2, "Frontend Developer", "Experience in building responsive web applications using React", 3,
//                List.of("HTML", "CSS", "JavaScript", "React")),
//
//
//        new JobPost(3, "Data Scientist", "Strong background in machine learning and data analysis", 4,
//                List.of("Python", "Machine Learning", "Data Analysis")),
//
//
//        new JobPost(4, "Network Engineer", "Design and implement computer networks for efficient data communication", 5,
//                List.of("Networking", "Cisco", "Routing", "Switching")),
//
//
//        new JobPost(5, "Mobile App Developer", "Experience in mobile app development for iOS and Android", 3,
//                List.of("iOS Development", "Android Development", "Mobile App"))
//));
//
//JobRepo(JobPost jobPost) {
//	this.jobPost = jobPost;
//}
//
//// method to return all JobPosts
//public List<JobPost> getAllJobs() {
//    return jobs;
//}
//
//// method to save a job post object into arrayList
//public void addJob(JobPost job) {
//    jobs.add(job);
//    System.out.println(jobs);
//
//}
//
//public JobPost getJob(int pId) {
//	for(JobPost job:jobs) {
//		if(job.getPostId()==pId) {
//			return job;
//		}
//	}
//	return null;
//}
//
//public void updateJob(JobPost jobPost) {
//	for(JobPost jp:jobs) {
//		if(jp.getPostId()== jobPost.getPostId()) {
//			jp.setPostDesc(jobPost.getPostDesc());
//			jp.setPostProfile(jobPost.getPostProfile());
//			jp.setReqExperience(jobPost.getReqExperience());
//			jp.setPostTechStack(jobPost.getPostTechStack());
//		}
//	}
//}
//
//public void jobDelete(int pid) {
//	JobPost jp=null;
//	for(JobPost job:jobs) {
//		if(job.getPostId()==pid) {
//			jp=job;
//		}
//	}
//	jobs.remove(jp);
//}