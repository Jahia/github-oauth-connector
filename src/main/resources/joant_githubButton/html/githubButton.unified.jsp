<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="ui" uri="http://www.jahia.org/tags/uiComponentsLib" %>
<%@ taglib prefix="functions" uri="http://www.jahia.org/tags/functions" %>
<%@ taglib prefix="query" uri="http://www.jahia.org/tags/queryLib" %>
<%@ taglib prefix="utility" uri="http://www.jahia.org/tags/utilityLib" %>
<%@ taglib prefix="s" uri="http://www.jahia.org/tags/search" %>
<%--@elvariable id="currentNode" type="org.jahia.services.content.JCRNodeWrapper"--%>
<%--@elvariable id="out" type="java.io.PrintWriter"--%>
<%--@elvariable id="script" type="org.jahia.services.render.scripting.Script"--%>
<%--@elvariable id="scriptInfo" type="java.lang.String"--%>
<%--@elvariable id="workspace" type="java.lang.String"--%>
<%--@elvariable id="renderContext" type="org.jahia.services.render.RenderContext"--%>
<%--@elvariable id="currentResource" type="org.jahia.services.render.Resource"--%>
<%--@elvariable id="url" type="org.jahia.services.render.URLGenerator"--%>
<c:set var="githubNode"/>
<c:forEach var="node" items="${jcr:getDescendantNodes(renderContext.site, 'joant:githubOAuthSettings')}">
    <c:set var="githubNode" value="${node}"/>
</c:forEach>

<c:if test="${(not renderContext.liveMode and githubNode eq '') or (githubNode ne '' and not githubNode.properties.isActivate.boolean)}">
    <div style="color: red;">
        <fmt:message key="joant_githubButton.error.connectorNotConfigured"/>
    </div>
</c:if>

<c:if test="${githubNode ne '' and githubNode.properties.isActivate.boolean and (not renderContext.loggedIn or renderContext.editMode)}">
    <c:set var="cssClass" value="${currentNode.properties['cssClass'].string}"/>
    <c:set var="htmlId" value="${currentNode.properties['htmlId'].string}"/>
    <c:set var="tagType" value="${currentNode.properties['tagType'].string}"/>
    <template:addResources type="css" resources="joaFontRoboto.css"/>
    <template:addResources type="css" resources="joaGithubButton.unified.css"/>

    <template:addResources>
        <script>
            function connectToGithub${fn:replace(currentNode.identifier, '-', '')}() {
                var popup = window.open('', "Github Authorization", "menubar=no,status=no,scrollbars=no,width=1145,height=725,modal=yes,alwaysRaised=yes");
                var xhr = new XMLHttpRequest();
                xhr.open('GET', '<c:url value="${url.base}${renderContext.site.home.path}"/>.connectToGithubAction.do');
                xhr.setRequestHeader('Accept', 'application/json;');
                xhr.send();

                xhr.onreadystatechange = function () {
                    if (xhr.readyState != 4 || xhr.status != 200)
                        return;
                    var json = JSON.parse(xhr.responseText);
                    popup.location.href = json.authorizationUrl;
                    window.addEventListener('message', function (event) {
                        if (event.data.authenticationIsDone) {
                            setTimeout(function () {
                                popup.close();
                                if (event.data.isAuthenticate) {
                                    window.location.search = 'site=${renderContext.site.siteKey}';
                                }
                            }, 3000);
                        }
                    });
                };
            }
        </script>
    </template:addResources>

    <c:choose>
        <c:when test="${tagType eq 'button'}">
            <button class="github-btn custom-btn-theme ${cssClass}" type="button"
                    <c:if test="${not renderContext.editMode}"> onclick="connectToGithub${fn:replace(currentNode.identifier, '-', '')}()" </c:if>
                    <c:if test="${not empty htmlId}"> id="${htmlId}"</c:if>
                    <c:if test="${renderContext.editMode}">disabled</c:if> >
                        <span class="custom-icon-svg">
                            <svg height="16px" id="Layer_1" style="enable-background:new 0 0 67 67;" version="1.1" viewBox="0 0 67 67" width="16px" xml:space="preserve" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink">
                                <path d="M20.543,34.569c-0.054-0.001,0.592,1.366,0.61,1.366  c1.41,2.516,4.128,4.08,8.713,4.514c-0.654,0.488-1.44,1.414-1.549,2.484c-0.823,0.523-2.478,0.696-3.764,0.297  c-1.803-0.559-2.493-4.066-5.192-3.566c-0.584,0.107-0.468,0.486,0.037,0.808c0.823,0.524,1.597,1.179,2.194,2.572  c0.459,1.07,1.423,2.981,4.473,2.981c1.21,0,2.058-0.143,2.058-0.143s0.023,2.731,0.023,3.793c0,1.225-1.682,1.57-1.682,2.159  c0,0.233,0.557,0.255,1.004,0.255c0.884,0,2.723-0.725,2.723-1.998c0-1.011,0.017-4.411,0.017-5.006c0-1.3,0.709-1.712,0.709-1.712  s0.088,6.94-0.169,7.872c-0.302,1.094-0.847,0.939-0.847,1.427c0,0.726,2.214,0.179,2.948-1.416c0.567-1.239,0.319-8.05,0.319-8.05  l0.605-0.012c0,0,0.034,3.117,0.013,4.542c-0.021,1.476-0.123,3.342,0.769,4.222c0.586,0.579,2.484,1.594,2.484,0.666  c0-0.539-1.04-0.982-1.04-2.441v-6.715c0.831,0,0.706,2.208,0.706,2.208l0.061,4.103c0,0-0.184,1.494,1.645,2.119  c0.645,0.223,2.025,0.282,2.09-0.09c0.065-0.373-1.662-0.928-1.678-2.084c-0.01-0.707,0.032-1.119,0.032-4.187  c0-3.068-0.419-4.202-1.88-5.106c4.508-0.455,7.299-1.551,8.658-4.486c0.106,0.003,0.555-1.371,0.496-1.371  c0.305-1.108,0.47-2.419,0.502-3.971c-0.008-4.21-2.058-5.699-2.451-6.398c0.58-3.187-0.098-4.637-0.412-5.135  c-1.162-0.406-4.041,1.045-5.615,2.066c-2.564-0.737-7.986-0.666-10.019,0.19c-3.751-2.639-5.736-2.235-5.736-2.235  s-1.283,2.259-0.339,5.565c-1.234,1.546-2.154,2.64-2.154,5.539C19.906,31.83,20.102,33.292,20.543,34.569z M33,64  C16.432,64,3,50.569,3,34S16.432,4,33,4s30,13.431,30,30S49.568,64,33,64z" style="fill-rule:evenodd;clip-rule:evenodd;fill:#D8D9D8;"/>
                            </svg>                </span>
                        <p class="btn-text">${currentNode.displayableName}</p>
            </button>
        </c:when>
        <c:otherwise>
            <a href="#" class="github-btn custom-btn-theme ${cssClass}"
               <c:if test="${not renderContext.editMode}"> onclick="connectToGithub${fn:replace(currentNode.identifier, '-', '')}();
                       return false;" </c:if>
               <c:if test="${not empty htmlId}"> id="${htmlId}"</c:if>
               <c:if test="${renderContext.editMode}">disabled</c:if> >
                   <span class="custom-icon-svg">
                       <svg height="16px" id="Layer_1" style="enable-background:new 0 0 67 67;" version="1.1" viewBox="0 0 67 67" width="16px" xml:space="preserve" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink">
                           <path d="M20.543,34.569c-0.054-0.001,0.592,1.366,0.61,1.366  c1.41,2.516,4.128,4.08,8.713,4.514c-0.654,0.488-1.44,1.414-1.549,2.484c-0.823,0.523-2.478,0.696-3.764,0.297  c-1.803-0.559-2.493-4.066-5.192-3.566c-0.584,0.107-0.468,0.486,0.037,0.808c0.823,0.524,1.597,1.179,2.194,2.572  c0.459,1.07,1.423,2.981,4.473,2.981c1.21,0,2.058-0.143,2.058-0.143s0.023,2.731,0.023,3.793c0,1.225-1.682,1.57-1.682,2.159  c0,0.233,0.557,0.255,1.004,0.255c0.884,0,2.723-0.725,2.723-1.998c0-1.011,0.017-4.411,0.017-5.006c0-1.3,0.709-1.712,0.709-1.712  s0.088,6.94-0.169,7.872c-0.302,1.094-0.847,0.939-0.847,1.427c0,0.726,2.214,0.179,2.948-1.416c0.567-1.239,0.319-8.05,0.319-8.05  l0.605-0.012c0,0,0.034,3.117,0.013,4.542c-0.021,1.476-0.123,3.342,0.769,4.222c0.586,0.579,2.484,1.594,2.484,0.666  c0-0.539-1.04-0.982-1.04-2.441v-6.715c0.831,0,0.706,2.208,0.706,2.208l0.061,4.103c0,0-0.184,1.494,1.645,2.119  c0.645,0.223,2.025,0.282,2.09-0.09c0.065-0.373-1.662-0.928-1.678-2.084c-0.01-0.707,0.032-1.119,0.032-4.187  c0-3.068-0.419-4.202-1.88-5.106c4.508-0.455,7.299-1.551,8.658-4.486c0.106,0.003,0.555-1.371,0.496-1.371  c0.305-1.108,0.47-2.419,0.502-3.971c-0.008-4.21-2.058-5.699-2.451-6.398c0.58-3.187-0.098-4.637-0.412-5.135  c-1.162-0.406-4.041,1.045-5.615,2.066c-2.564-0.737-7.986-0.666-10.019,0.19c-3.751-2.639-5.736-2.235-5.736-2.235  s-1.283,2.259-0.339,5.565c-1.234,1.546-2.154,2.64-2.154,5.539C19.906,31.83,20.102,33.292,20.543,34.569z M33,64  C16.432,64,3,50.569,3,34S16.432,4,33,4s30,13.431,30,30S49.568,64,33,64z" style="fill-rule:evenodd;clip-rule:evenodd;fill:#D8D9D8;"/>
                       </svg>                   </span>
                   <p class="btn-text">${currentNode.displayableName}</p>
            </a>
        </c:otherwise>
    </c:choose>
</c:if>
