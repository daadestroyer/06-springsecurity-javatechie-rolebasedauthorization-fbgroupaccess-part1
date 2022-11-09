package com.daadestroyer.springsecurityjavatechierolebasedauthorizationfbgroupaccesspart1.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daadestroyer.springsecurityjavatechierolebasedauthorizationfbgroupaccesspart1.entity.Post;
import com.daadestroyer.springsecurityjavatechierolebasedauthorizationfbgroupaccesspart1.entity.PostStatus;
import com.daadestroyer.springsecurityjavatechierolebasedauthorizationfbgroupaccesspart1.repo.PostRepo;

@RestController
@RequestMapping("/post")
public class PostController {

	@Autowired
	private PostRepo postRepo;

	// PUBLIC API 
	@PostMapping("/create")
	public String createPost(@RequestBody Post post, Principal principal) {
		post.setPostStatus(PostStatus.PENDING);
		post.setUserName(principal.getName());
		this.postRepo.save(post);
		return principal.getName() + " you post published successfully , Required ADMIN/MODERATOR action!";
	}

	// PUBLIC API
	// view all the approved post
	@GetMapping("/viewAll")
	public List<Post> viewAll() {
		return this.postRepo.findAll().stream().filter(post -> post.getPostStatus().equals(PostStatus.APPROVED))
				.collect(Collectors.toList());
	}

	// API for approving post by id
	@GetMapping("/approvePost/{postId}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MODERATOR')")
	public String approvePost(@PathVariable int postId) {
		Post post = this.postRepo.findById(postId).get();
		post.setPostStatus(PostStatus.APPROVED);

		this.postRepo.save(post);
		return "Post Approved...";
	}

	// API for bulk approval
	@GetMapping("/approveAll")
	@PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MODERATOR')")
	public String approveAllPost() {

		this.postRepo.findAll().stream().filter(post -> post.getPostStatus().equals(PostStatus.PENDING))
				.forEach(post -> {
					post.setPostStatus(PostStatus.APPROVED);
					this.postRepo.save(post);
				});

		return "Approved all post!";
	}

	// API for rejecting post by id
	@GetMapping("/removePost/{postId}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MODERATOR')")
	public String rejectPost(@PathVariable int postId) {
		Post post = this.postRepo.findById(postId).get();
		post.setPostStatus(PostStatus.REJECTED);

		this.postRepo.save(post);
		return "Post Approved...";
	}

	// API for bulk rejection
	@GetMapping("/rejectAll")
	@PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MODERATOR')")
	public String rejectAllPost() {

		this.postRepo.findAll().stream().filter(post -> post.getPostStatus().equals(PostStatus.PENDING))
				.forEach(post -> {
					post.setPostStatus(PostStatus.REJECTED);
					this.postRepo.save(post);
				});

		return "Rejected all post!";
	}

}
