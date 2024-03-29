/*******************************************************************************
 * This file is part of hommkmessage.
 * 
 * hommkmessage is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * hommkmessage is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with hommkmessage.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.appspot.hommkmessage.server.service;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.appspot.hommkmessage.server.repository.Message;
import com.appspot.hommkmessage.server.repository.RepositoryAccess;

public class AddMessageServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String htmlSource = req.getParameter("source");
		String key = req.getParameter("key");
		String subjectText = req.getParameter("subjectText");
		String dateText = req.getParameter("dateText");
		String receiverText = req.getParameter("receiverText");
		String contentText = req.getParameter("contentText");
		String userId = req.getParameter("userId");
		validateParameters(htmlSource, key, subjectText, dateText,
				receiverText, contentText, userId);
		Message message = saveMessage(htmlSource, key, subjectText, dateText,
				receiverText, contentText, userId);

		writeResponse(req, resp, userId, message);
	}

	private void validateParameters(String htmlSource, String key,
			String subjectText, String dateText, String receiverText,
			String contentText, String userId) {
		if (htmlSource == null || key == null || subjectText == null
				|| dateText == null || receiverText == null
				|| contentText == null || userId == null) {
			throw new IllegalArgumentException("missing parameter");
		}
	}

	private Message saveMessage(String htmlSource, String key,
			String subjectText, String dateText, String receiverText,
			String contentText, String userId) {
		Message message = new Message(htmlSource, key, subjectText, dateText,
				receiverText, contentText, userId);
		new RepositoryAccess(key).save(message);
		return message;
	}

	private void writeResponse(HttpServletRequest req,
			HttpServletResponse resp, String userId, Message message)
			throws IOException {
		String newMessageUrl = req.getContextPath() + "message?messageId="
				+ message.getId();
		PrintWriter writer = resp.getWriter();
		writer.append("<html><head><script type='text/javascript'>");
		writer.append("localStorage.userId='" + userId + "';");
		writer.append("window.location.href='" + newMessageUrl + "';");
		writer.append("</script></head></html>");
		writer.flush();
		writer.close();
	}

}
