package edu.sjsu.cmpe275.aop.aspect;

import java.util.HashMap;
import java.util.HashSet;

import org.aspectj.lang.JoinPoint; // if needed
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect; // if needed
import org.aspectj.lang.annotation.Before; // if needed
import org.springframework.beans.factory.annotation.Autowired; // if needed

import edu.sjsu.cmpe275.aop.BlogService;
import edu.sjsu.cmpe275.aop.exceptions.AccessDeniedExeption;

@Aspect
public class AuthorizationAspect {

	public static HashMap<String, HashSet<String>> ownerSharedWithMap = new HashMap<String, HashSet<String>>();

	/***
	 * Following is a dummy implementation of this aspect. You are expected to
	 * provide an actual implementation based on the requirements, including
	 * adding/removing advises as needed.
	 */

	@Autowired
	BlogService blogService;

	@Around(value = "execution(public * edu.sjsu.cmpe275.aop.BlogService.shareBlog(..))")
	public void dummyAfterAdvice(ProceedingJoinPoint joinPoint ) throws Throwable,AccessDeniedExeption {
		Object[] signatureArgs = joinPoint.getArgs();
		String userId = (String) signatureArgs[0];
		String blogUserId = (String) signatureArgs[1];
		String targetUserId = (String) signatureArgs[2];
		
		checkIfValidUserID(userId);
		checkIfValidUserID(blogUserId);
		checkIfValidUserID(targetUserId);
		HashSet<String> sharedWithList = new HashSet<String>();

		if (!(blogUserId.equals(targetUserId))) {

			if (userId.equals(blogUserId)) {
				if (ownerSharedWithMap.containsKey(targetUserId)) {
					sharedWithList = ownerSharedWithMap.get(targetUserId);
					sharedWithList.add(blogUserId);
					ownerSharedWithMap.put(targetUserId, sharedWithList);
				} else {
					sharedWithList = new HashSet<String>();
					sharedWithList.add(blogUserId);
					ownerSharedWithMap.put(targetUserId, sharedWithList);
				}
			} else {
				if (ownerSharedWithMap.containsKey(userId)) {
					sharedWithList = ownerSharedWithMap.get(userId);
					if (sharedWithList.contains(blogUserId)) {
						if (ownerSharedWithMap.containsKey(targetUserId)) {
							sharedWithList = ownerSharedWithMap.get(targetUserId);
							sharedWithList.add(blogUserId);
							ownerSharedWithMap.put(targetUserId, sharedWithList);
						} else {
							sharedWithList = new HashSet<String>();
							sharedWithList.add(blogUserId);
							ownerSharedWithMap.put(targetUserId, sharedWithList);
						}

					} else {
						System.out.println(String.format("The Blog is not shared with %s", userId));
						throw new AccessDeniedExeption(String.format("The Blog is not shared with %s", userId));
					}

				} else {
					System.out.println(String.format("The Blog is not shared with %s", userId));
					throw new AccessDeniedExeption(String.format("The Blog is not shared with %s", userId));
				}
			}
			joinPoint.proceed();
		}
	}

	@Around("execution(public * edu.sjsu.cmpe275.aop.BlogService.unshareBlog(..))")
	public void validateUnsharedTo(ProceedingJoinPoint joinPoint) throws Throwable,AccessDeniedExeption {
		Object[] signatureArgs = joinPoint.getArgs();
		String userId = (String) signatureArgs[0];
		String targetUserId = (String) signatureArgs[1];
		checkIfValidUserID(userId);
		checkIfValidUserID(targetUserId);

		if (!userId.equals(targetUserId)) {

			if (ownerSharedWithMap.containsKey(targetUserId)) {

				HashSet<String> sharedWithList = ownerSharedWithMap.get(targetUserId);

				if (sharedWithList.contains(userId)) {

					sharedWithList.remove(userId);
					ownerSharedWithMap.put(targetUserId, sharedWithList);

				} else {
					System.out.println(String.format("Access Denied, %s ", userId + "cannot unshare the blog"));
					throw new AccessDeniedExeption(String.format("Access Denied, %s ", userId + "cannot unshare the blog"));
				}
			} else {
				System.out.println(String.format("Access Denied, %s ", userId + "cannot unshare the blog"));
				throw new AccessDeniedExeption(String.format("Access Denied, %s ", userId + "cannot unshare the blog"));
			}
			joinPoint.proceed();
		}
	}

	@Before(value = "execution(public * edu.sjsu.cmpe275.aop.BlogService.readBlog(..)) ")
	public void readCheck(JoinPoint joinPoint) throws Throwable, AccessDeniedExeption {
		Object[] signatureArgs = joinPoint.getArgs();
		String userId = (String) signatureArgs[0];
		String blogUserId = (String) signatureArgs[1];
		checkIfValidUserID(userId);
		checkIfValidUserID(blogUserId);

		if (!(userId.equals(blogUserId))) {

			if (ownerSharedWithMap.containsKey(userId)) {
				HashSet<String> sharedWithList = ownerSharedWithMap.get(userId);
				if (!(sharedWithList.contains(blogUserId))) {
					System.out.println(String.format("Access Denied to %s, cannot read the blog %s",userId,blogUserId));
					throw new AccessDeniedExeption(String.format("Access Denied to %s, cannot read the blog %s",userId,blogUserId));
				}
			} else {
				System.out.println(String.format("Access Denied to %s, cannot read the blog %s",userId,blogUserId));
				throw new AccessDeniedExeption(String.format("Access Denied to %s, cannot read the blog %s",userId,blogUserId));
			}
		}
	}
	
	private void checkIfValidUserID(String userId) throws IllegalArgumentException {
		if(userId.length()<4 || userId.length()>16) {
			throw new IllegalArgumentException(String.format("userId %s must be between 4 and 16 unicode characters", userId));
		}
	}

}
