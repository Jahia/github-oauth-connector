/*
 * ==========================================================================================
 * =                            JAHIA'S ENTERPRISE DISTRIBUTION                             =
 * ==========================================================================================
 *
 *                                  http://www.jahia.com
 *
 * JAHIA'S ENTERPRISE DISTRIBUTIONS LICENSING - IMPORTANT INFORMATION
 * ==========================================================================================
 *
 *     Copyright (C) 2002-2017 Jahia Solutions Group. All rights reserved.
 *
 *     This file is part of a Jahia's Enterprise Distribution.
 *
 *     Jahia's Enterprise Distributions must be used in accordance with the terms
 *     contained in the Jahia Solutions Group Terms & Conditions as well as
 *     the Jahia Sustainable Enterprise License (JSEL).
 *
 *     For questions regarding licensing, support, production usage...
 *     please contact our team at sales@jahia.com or go to http://www.jahia.com/license.
 *
 * ==========================================================================================
 */
package org.jahia.modules.githuboauthconnector.action;

import java.util.List;
import java.util.Map;
import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jahia.bin.Action;
import org.jahia.bin.ActionResult;
import org.jahia.modules.jahiaoauth.service.JahiaOAuthConstants;
import org.jahia.modules.jahiaoauth.service.JahiaOAuthService;
import org.jahia.modules.githuboauthconnector.impl.GithubConnectorImpl;
import org.jahia.services.content.JCRCallback;
import org.jahia.services.content.JCRNodeWrapper;
import org.jahia.services.content.JCRSessionWrapper;
import org.jahia.services.content.JCRTemplate;
import org.jahia.services.render.RenderContext;
import org.jahia.services.render.Resource;
import org.jahia.services.render.URLResolver;
import org.json.JSONObject;

/**
 * @author dgaillard
 */
public class ConnectToGithub extends Action {

    private JCRTemplate jcrTemplate;
    private JahiaOAuthService jahiaOAuthService;
    private GithubConnectorImpl githubConnectorImpl;

    @Override
    public ActionResult doExecute(HttpServletRequest req, RenderContext renderContext, Resource resource,
            JCRSessionWrapper session, Map<String, List<String>> parameters,
            URLResolver urlResolver) throws Exception {

        final String sitePath = renderContext.getSite().getPath();
        final String sessionId = req.getSession().getId();
        String authorizationUrl = jcrTemplate.doExecuteWithSystemSession(new JCRCallback<String>() {
            @Override
            public String doInJCR(JCRSessionWrapper session) throws RepositoryException {
                JCRNodeWrapper jahiaOAuthNode = session.getNode(sitePath).getNode(JahiaOAuthConstants.JAHIA_OAUTH_NODE_NAME);
                return jahiaOAuthService.getAuthorizationUrl(jahiaOAuthNode, githubConnectorImpl.getServiceName(), sessionId);
            }
        });

        JSONObject response = new JSONObject();
        response.put(JahiaOAuthConstants.AUTHORIZATION_URL, authorizationUrl);
        return new ActionResult(HttpServletResponse.SC_OK, null, response);
    }

    public void setJahiaOAuthService(JahiaOAuthService jahiaOAuthService) {
        this.jahiaOAuthService = jahiaOAuthService;
    }

    public void setGithubConnectorImpl(GithubConnectorImpl githubConnectorImpl) {
        this.githubConnectorImpl = githubConnectorImpl;
    }

    public void setJcrTemplate(JCRTemplate jcrTemplate) {
        this.jcrTemplate = jcrTemplate;
    }
}
