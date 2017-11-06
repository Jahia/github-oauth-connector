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
import org.apache.commons.lang.StringUtils;
import org.jahia.bin.Action;
import org.jahia.bin.ActionResult;
import org.jahia.modules.githuboauthconnector.impl.GithubConnectorImpl;
import org.jahia.modules.jahiaoauth.service.JahiaOAuthConstants;
import org.jahia.modules.jahiaoauth.service.JahiaOAuthService;
import org.jahia.services.content.JCRCallback;
import org.jahia.services.content.JCRNodeWrapper;
import org.jahia.services.content.JCRSessionWrapper;
import org.jahia.services.content.JCRTemplate;
import org.jahia.services.render.RenderContext;
import org.jahia.services.render.Resource;
import org.jahia.services.render.URLResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dgaillard
 */
public class GithubOAuthCallback extends Action {
    private static final Logger LOGGER = LoggerFactory.getLogger(GithubOAuthCallback.class);
    private JCRTemplate jcrTemplate;
    private JahiaOAuthService jahiaOAuthService;
    private GithubConnectorImpl githubConnectorImpl;

    @Override
    public ActionResult doExecute(final HttpServletRequest req, RenderContext renderContext, Resource resource,
                                  final JCRSessionWrapper session, Map<String, List<String>> parameters,
                                  URLResolver urlResolver) throws Exception {

        Boolean isAuthenticate = false;
        if (parameters.containsKey("code") && parameters.containsKey(JahiaOAuthConstants.STATE)) {
            final String token = parameters.get("code").get(0);
            final String state = parameters.get(JahiaOAuthConstants.STATE).get(0);
            if (StringUtils.isBlank(token) || StringUtils.isBlank(state)) {
                return ActionResult.BAD_REQUEST;
            }

            final String sitePath = renderContext.getSite().getPath();
            isAuthenticate = jcrTemplate.doExecuteWithSystemSession(new JCRCallback<Boolean>() {
                @Override
                public Boolean doInJCR(JCRSessionWrapper systemSession) throws RepositoryException {
                    JCRNodeWrapper jahiaOAuthNode = systemSession.getNode(sitePath).getNode(JahiaOAuthConstants.JAHIA_OAUTH_NODE_NAME);
                    try {
                        jahiaOAuthService.extractAccessTokenAndExecuteMappers(jahiaOAuthNode, githubConnectorImpl.getServiceName(), token, state);
                        return true;
                    } catch (Exception ex) {
                        LOGGER.error("Could not authenticate user", ex);
                        return false;
                    }
                }
            });
        } else {
            LOGGER.error("Could not authenticate user with Github, the callback from the Github server was missing mandatory parameters");
        }

        return new ActionResult(HttpServletResponse.SC_OK,
                jahiaOAuthService.getResultUrl(renderContext.getSite().getUrl(), isAuthenticate),
                true, null);
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
