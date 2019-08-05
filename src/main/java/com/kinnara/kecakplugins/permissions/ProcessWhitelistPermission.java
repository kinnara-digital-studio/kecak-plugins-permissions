package com.kinnara.kecakplugins.permissions;

import org.joget.apps.app.model.AppDefinition;
import org.joget.apps.app.model.PackageDefinition;
import org.joget.apps.app.service.AppPluginUtil;
import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.FormPermission;
import org.joget.apps.userview.model.UserviewPermission;
import org.joget.workflow.model.WorkflowProcess;
import org.joget.workflow.model.service.WorkflowManager;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

public class ProcessWhitelistPermission extends UserviewPermission implements FormPermission {
    @Override
    public boolean isAuthorize() {
        ApplicationContext appContext = AppUtil.getApplicationContext();
        PackageDefinition packageDefinition = AppUtil.getCurrentAppDefinition().getPackageDefinition();
        WorkflowManager workflowManager = (WorkflowManager) appContext.getBean("workflowManager");
        Collection<WorkflowProcess> processList = workflowManager.getProcessList(packageDefinition.getId(), packageDefinition.getVersion().toString());

        return (processList == null ? Stream.<WorkflowProcess>empty() : processList.stream())
                .filter(p -> Arrays.stream(getPropertyString("processId").split(";"))
                        .anyMatch(s -> s.equalsIgnoreCase(p.getIdWithoutVersion())))
                .anyMatch(p -> workflowManager.isUserInWhiteList(p.getId()));
    }

    @Override
    public String getName() {
        return AppPluginUtil.getMessage("procesWhitelistPermission.title", getClassName(), "/messages/ProcessWhitelistPermission");
    }

    @Override
    public String getVersion() {
        return getClass().getPackage().getImplementationVersion();
    }

    @Override
    public String getDescription() {
        return getClass().getPackage().getImplementationTitle();
    }

    @Override
    public String getLabel() {
        return getName();
    }

    @Override
    public String getClassName() {
        return getClass().getName();
    }

    @Override
    public String getPropertyOptions() {
        AppDefinition appDefinition = AppUtil.getCurrentAppDefinition();
        return AppUtil.readPluginResource(getClassName(), "/properties/ProcessWhitelistPermission.json", new String[]{appDefinition.getAppId(), String.valueOf(appDefinition.getVersion())}, false, "/messages/ProcessWhitelistPermission");
    }
}
