package com.daadestroyer.springsecurityjavatechierolebasedauthorizationfbgroupaccesspart1.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daadestroyer.springsecurityjavatechierolebasedauthorizationfbgroupaccesspart1.entity.Post;
import com.daadestroyer.springsecurityjavatechierolebasedauthorizationfbgroupaccesspart1.entity.PostStatus;

@Repository
public interface PostRepo extends JpaRepository<Post, Integer> {

}
