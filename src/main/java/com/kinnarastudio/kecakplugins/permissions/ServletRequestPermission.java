package com.kinnarastudio.kecakplugins.permissions;

import org.joget.apps.app.service.AppUtil;
import org.joget.apps.userview.model.UserviewPermission;
import org.joget.workflow.util.WorkflowUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.joget.plugin.base.PluginManager;

import java.util.ResourceBundle;


/**
 * @author aristo
 *
 * Servlet Request Permission
 * Permission based on Http Servlet Request
 */
public class ServletRequestPermission extends UserviewPermission {
    @Override
    public boolean isAuthorize() {
        HttpServletRequest request = WorkflowUtil.getHttpServletRequest();
        Pattern p = Pattern.compile(getPropertyString("urlRegexPattern"));
        return Optional.of(request)
                .map(HttpServletRequest::getRequestURL)
                .map(StringBuffer::toString)
                .map(p::matcher)
                .map(Matcher::matches)
                .orElse(false);
    }

    @Override
    public String getName() {
        return getLabel();
    }

    @Override
    public String getVersion() {
        PluginManager pluginManager = (PluginManager) AppUtil.getApplicationContext().getBean("pluginManager");
        ResourceBundle resourceBundle = pluginManager.getPluginMessageBundle(getClassName(), "/messages/BuildNumber");
        String buildNumber = resourceBundle.getString("buildNumber");
        return buildNumber;
    }

    @Override
    public String getDescription() {
        return getClass().getPackage().getImplementationTitle();
    }

    @Override
    public String getLabel() {
        return "Servlet Request Permission";
    }

    @Override
    public String getClassName() {
        return ServletRequestPermission.class.getName();
    }

    @Override
    public String getPropertyOptions() {
        return AppUtil.readPluginResource(getClassName(), "/properties/ServletRequestPermission.json", null, false, null);
    }
}
