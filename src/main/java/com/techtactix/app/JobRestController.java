package com.techtactix.app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.techtactix.app.model.JobPost;
import com.techtactix.app.service.JobService;

@RestController
@CrossOrigin("http://localhost:3000")
public class JobRestController {

	@Autowired
	private JobService service;
	
	
	
	@GetMapping("jobPosts")
	public List<JobPost> getAllJobs() {
		return service.getAllJobs();
	}
	
	
	@GetMapping("jobPost/{postId}")
	public JobPost getJob(@PathVariable("postId") int pId) {
		return service.getJob(pId);
	}
	
	@GetMapping("jobPost/keyword/{keyword}")
	public List<JobPost> searchByKeyword(@PathVariable("keyword") String keyword){
		return service.search(keyword);
	}
	
	@PostMapping("jobPost")
	public JobPost addJob(@RequestBody JobPost jobpost) {
		service.addJob(jobpost);
		return service.getJob(jobpost.getPostId());
	}
	
	@PutMapping("jobPost")
	public JobPost updateJob(@RequestBody JobPost jobPost) {
		service.jobUpdate(jobPost);
		return service.getJob(jobPost.getPostId());
	}
	
	@DeleteMapping("jobPost/{postId}")
	public String deleteJob(@PathVariable("postId") int pId) {
		service.deleteJob(pId);
		return "deleted";
	}
	
	
	@GetMapping("load")
	public String loadData() {
		service.load();
		return "loaded";
	}
	
	
	
	
	
}
