package edu.sjsu.cmpe275.aop.aspect;

import java.util.HashSet;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect; // if needed
import org.aspectj.lang.annotation.Before;

import edu.sjsu.cmpe275.aop.exceptions.AccessDeniedExeption;

@Aspect
public class ValidationAspect {
	/***
	 * Following is a dummy implementation of this aspect. You are expected to
	 * provide an actual implementation based on the requirements, including
	 * adding/removing advices as needed.
	 * @throws AccessDeniedExeption 
	 */

	@Before(value = "execution(public * edu.sjsu.cmpe275.aop.BlogServiceImpl.commentOnBlog(..))")
	public void addComment(JoinPoint joinPoint) throws Throwable, AccessDeniedExeption {
		Object[] signatureArgs = joinPoint.getArgs();
		String userId = (String) signatureArgs[0];
		String blogUserId = (String) signatureArgs[1];
		String comment = (String) signatureArgs[2];
		checkIfValidUserID(userId);
		checkIfValidUserID(blogUserId);

		if (comment == null || comment.length() == 0 || comment.length() > 100) {
			System.out.println(String.format("IllegalArgumentExceptionOccured, %s ", comment + "is improper"));
			throw new IllegalArgumentException(String.format("IllegalArgumentExceptionOccured, %s ", comment + "is improper"));
		}

		if (!(userId.equals(blogUserId))) {
			if (AuthorizationAspect.ownerSharedWithMap.containsKey(userId)) {
				HashSet<String> sharedWithList = AuthorizationAspect.ownerSharedWithMap.get(userId);
				if (!(sharedWithList.contains(blogUserId))) {
					System.out.println(String.format("Access Denied, %s does not have access to comment on %s's blog", userId,blogUserId));
					throw new AccessDeniedExeption(String.format("Access Denied, %s does not have access to comment on %s's blog", userId,blogUserId));
				}
			} else {
				System.out.println(String.format("Access Denied, %s does not have access to comment on %s's blog", userId,blogUserId));
				throw new AccessDeniedExeption(String.format("Access Denied, %s does not have access to comment on %s's blog", userId,blogUserId));
			}
		}
	}
	
	private void checkIfValidUserID(String userId) throws IllegalArgumentException {
		if(userId.length()<4 || userId.length()>16) {
			throw new IllegalArgumentException(String.format("userId %s must be between 4 and 16 unicode characters", userId));
		}
	}
}