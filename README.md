# Aspect-Oriented-Programming
Implementing AOP Aspects for validation

The actual implementation of the service, the “official” version of BlogServiceImpl.java, however, does not provide enforce access control; i.e., the shareBlog method does not check whether the current user is shared with the blog in the first place, and readBlog does not to check that either. Part of the task is to use AOP to enforce the following authorization policies. 

1.	Once can share his blog with anybody.
2.	One can only read blogs that are shared with him, or his own blog. In any other case, an AccessDeniedExeption is thrown.
3.	If Alice shares her blog with Bob, Bob can further share Alice’s blog with Carl. If Alice attempts to share Bob’s blog with    Carl while Bob’s blog is not shared with Alice in the first place, Alice gets an AccessDeniedExeption.
4.	One can only unshare his own blog. When unsharing a blog with Bob that the blog is not shared by any means with Bob in the first place, the operation throws an AccessDeniedExeption. 
5.	Both sharing and unsharing of Alice’s blog with Alice have no effect; i.e. Alice always has access to her own blog, and can share and unshare with herself without encountering any exception, even these operations do not take any effect.

BlogService has the following validation rules, which the “official” version of BlogServiceImpl.java fails to implement, and you are expected to enforce it with an aspect.
6.	For all the methods, every parameters for user ID must of a string of at least 4 and maximum 16 unicode characters, or an IllegalArgumentException is thrown.  
7.	For any blog that is shared with Alice, she can comment on it with a message that is up to 100 unicode characters. If the message is longer than 100, or null, or empty, an IllegalArgumentException is thrown.

Please note that our access control here assumes that authentication is already taken care of elsewhere, i.e., it’s outside the scope of the project to make sure only Alice can call readBlog with userId as “Alice”.

All the methods in BlogService can also run into network failures, in which case, an NetworkException will be thrown, and the method takes no effect at all. Actually, since network failure happens relatively frequently, you are asked to add the feature to automatically retry for up to two times for a network failure (indicated by an NetworkException). Please note the two retries are in addition to the original failed invocation; if you still encounter NetworkException on your second (i.e., final) retry, you should throw out this NetworkException so that the caller knows its occurrence.
