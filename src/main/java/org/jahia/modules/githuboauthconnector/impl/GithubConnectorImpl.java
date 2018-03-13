package org.jahia.modules.githuboauthconnector.impl;

import java.util.List;
import java.util.Map;
import org.jahia.modules.jahiaoauth.service.ConnectorService;

public class GithubConnectorImpl implements ConnectorService {

    private String protectedResourceUrl;
    private String serviceName;
    private List<Map<String, Object>> availableProperties;

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
