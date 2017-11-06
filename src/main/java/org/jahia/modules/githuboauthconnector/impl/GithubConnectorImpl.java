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
package org.jahia.modules.githuboauthconnector.impl;

import java.util.List;
import java.util.Map;
import org.jahia.modules.jahiaoauth.service.ConnectorService;
import org.jahia.security.license.LicenseCheckException;

/**
 * @author dgaillard
 */
public class GithubConnectorImpl implements ConnectorService {

    private String protectedResourceUrl;
    private String serviceName;
    private List<Map<String, Object>> availableProperties;

    public void onStart() throws LicenseCheckException {
//        if (!LicenseCheckerService.Stub.isAllowed("org.jahia.oauthGithub")) {
//            throw new LicenseCheckException("Your DX license does not allow you to run Github OAuth Connector. please contact Jahia Solutions.");
//        }
    }

    @Override
    public String getProtectedResourceUrl() {
        return protectedResourceUrl;
    }

    @Override
    public List<Map<String, Object>> getAvailableProperties() {
        return availableProperties;
    }

    @Override
    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setProtectedResourceUrl(String protectedResourceUrl) {
        this.protectedResourceUrl = protectedResourceUrl;
    }

    public void setAvailableProperties(List<Map<String, Object>> availableProperties) {
        this.availableProperties = availableProperties;
    }
}
