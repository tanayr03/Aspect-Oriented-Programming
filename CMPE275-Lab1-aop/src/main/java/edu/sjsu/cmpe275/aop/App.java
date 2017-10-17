package edu.sjsu.cmpe275.aop;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
	public static void main(String[] args) {
		/***
		 * Following is a dummy implementation of App to demonstrate bean creation with
		 * Application context. You may make changes to suit your need, but this file is
		 * NOT part of the submission.
		 */

		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("context.xml");
		BlogService blogService = (BlogService) ctx.getBean("blogService");

		try {
			// blogService.shareBlog("Alice", "Alice", "Bobo");
			blogService.shareBlog("Alice", "Alice", "Bobo");
			blogService.unshareBlog("Alice", "Alice");
			blogService.shareBlog("Bobo", "Bobo", "Alice");
			blogService.shareBlog("Alice", "Bobo", "A2qw");
			blogService.shareBlog("A2qw", "Bobo", "Bobo");
		    blogService.readBlog("Alice", "Bobo");
		    blogService.readBlog("Alice", "Alice");
			//blogService.unshareBlog("Alice", "Bobo");
		    blogService.commentOnBlog("Bobo", "Alice", "hey");
			blogService.commentOnBlog("A2", "Alice", "hey");
			// blogService.commentOnBlog("Bob", "A2", "hey");
		} catch (Exception e) {
			e.printStackTrace();
		}
		ctx.close();
	}
}