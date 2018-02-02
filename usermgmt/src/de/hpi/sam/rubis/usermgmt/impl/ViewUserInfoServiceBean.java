package de.hpi.sam.rubis.usermgmt.impl;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import de.hpi.sam.rubis.entity.Comment;
import de.hpi.sam.rubis.entity.User;
import de.hpi.sam.rubis.entity.UserInfo;
import de.hpi.sam.rubis.entity.UserInfoComment;
import de.hpi.sam.rubis.queryservice.BasicQueryService;
import de.hpi.sam.rubis.usermgmt.ViewUserInfoService;
import de.hpi.sam.rubis.usermgmt.ViewUserInfoServiceException;

/**
 * Implementation of the {@link ViewUserInfoService}.
 * 
 * @author thomas vogel
 * 
 */
@Stateless(mappedName = ViewUserInfoService.MAPPED_NAME)
public class ViewUserInfoServiceBean implements ViewUserInfoService {

	@EJB(mappedName = BasicQueryService.MAPPED_NAME)
	private BasicQueryService basicQueryService;

	@Resource
	private String privacyLevel;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserInfo getUserInfo(int userId) throws ViewUserInfoServiceException {

		try {
			User user = this.basicQueryService.findUserById(userId);
			if (user == null) {
				String msg = "There is no user with id = " + userId + ".";
				throw new ViewUserInfoServiceException(msg);
			} else {
				List<Comment> receivedComments = user.getReceivedComments();
				List<UserInfoComment> userInfoComments = new LinkedList<UserInfoComment>();
				for (Comment comment : receivedComments) {
					userInfoComments.add(new UserInfoComment(comment.getId(),
							comment.getComment(), comment.getRating(), comment
									.getFromUser().getId(), comment
									.getFromUser().getNickname(), comment
									.getDate(), comment.getItem().getId(),
							comment.getItem().getName(), comment.getItem()
									.getDescription()));
				}

				// lookup the privacy level
				try {
					InitialContext ctx = new InitialContext();
					this.privacyLevel = (String) ctx
							.lookup("java:comp/env/privacyLevel");
				} catch (NamingException e) {
					e.printStackTrace();
				}
				// end of lookup the privacy level
				
				String userMail = "N/A";
				if (this.privacyLevel.equals(PrivacyLevel.LOW.toString())) {
					userMail = user.getEmail();
				} else if (this.privacyLevel.equals(PrivacyLevel.HIGH
						.toString())) {
					userMail = "Contact the user through RUBiS. Available soon!";
				}

				UserInfo userInfo = new UserInfo(user.getId(),
						user.getNickname(), userMail, user.getRating(),
						user.getCreationDate(), user.getRegion().getName(),
						user.getRegion().getId(), userInfoComments);

				return userInfo;

				// result.append(user.getHTMLGeneralUserInformation() + "\n");
				// List<Comment> commentList = user.getReceivedComments();
				// if (commentList.isEmpty()) {
				// result
				// .append("<h3>There is no comment yet for this user.</h3>");
				// } else {
				// result.append("<h3>Comments for this user</h3><br/>\n");
				// result.append("<DL>\n");
				// for (Comment comment : commentList) {
				// User commentingUser = comment.getFromUser();
				// result
				// .append("<DT><b><BIG><a href=\""
				// + RubisNameSchema.WEB_CONTAINER_CONTEXT
				// +
				// "/servlet/edu.rice.rubis.beans.servlets.ViewUserInfo?userId="
				// + commentingUser.getId() + "\">"
				// + commentingUser.getNickname()
				// + "</a></BIG></b>" + " wrote the "
				// + comment.getDate() + "<DD><i>"
				// + comment.getComment() + "</i><p>\n");
				// }
				// result.append("</DL>\n");
				// }
			}
		} catch (Exception e) {
			String msg = "Failure in retrieving information about the user with id = "
					+ userId + ": " + e.getMessage();
			throw new ViewUserInfoServiceException(msg, e);
		}

	}
}
