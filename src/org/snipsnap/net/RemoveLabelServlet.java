/*
 * This file is part of "SnipSnap Wiki/Weblog".
 *
 * Copyright (c) 2002 Stephan J. Schmidt, Matthias L. Jugel
 * All Rights Reserved.
 *
 * Please visit http://snipsnap.org/ for updates and contact.
 *
 * --LICENSE NOTICE--
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 * --LICENSE NOTICE--
 */

package org.snipsnap.net;

import org.snipsnap.snip.Snip;
import org.snipsnap.snip.SnipLink;
import org.snipsnap.snip.SnipSpaceFactory;
import org.snipsnap.app.Application;
import org.snipsnap.config.Configuration;
import org.snipsnap.net.filter.MultipartWrapper;
import org.radeox.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Removing a label from a snip
 * @author Marco Mosconi
 * @version $Id: RemoveLabelServlet.java 1609 2004-05-18 13:28:38Z stephan $
 */
public class RemoveLabelServlet extends HttpServlet {
  protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
    throws ServletException, IOException {
    doGet(httpServletRequest, httpServletResponse);
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    Configuration config = Application.get().getConfiguration();
    // If this is not a multipart/form-data request continue
    String type = request.getHeader("Content-Type");
    if (type != null && type.startsWith("multipart/form-data")) {
      try {
        request = new MultipartWrapper(request, config.getEncoding() != null ? config.getEncoding() : "UTF-8");
      } catch (IllegalArgumentException e) {
        Logger.warn("RemoveLabelServlet: multipart/form-data wrapper:" + e.getMessage());
      }
    }

    String snipName = request.getParameter("snipname");
    if (null == snipName) {
      response.sendRedirect(config.getUrl("/space/" + config.getStartSnip()));
      return;
    }
    // cancel pressed
    if (null != request.getParameter("cancel")) {
      response.sendRedirect(config.getUrl("/exec/labels?snipname=" + SnipLink.encode(snipName)));
      return;
    }

    Snip snip = SnipSpaceFactory.getInstance().load(snipName);
    String labelName = request.getParameter("labelname");
    String labelValue = request.getParameter("labelvalue");
    snip.getLabels().removeLabel(labelName, labelValue);

    RequestDispatcher dispatcher = request.getRequestDispatcher("/exec/labels?snipname=" + SnipLink.encode(snipName));
    dispatcher.forward(request, response);
  }
}
