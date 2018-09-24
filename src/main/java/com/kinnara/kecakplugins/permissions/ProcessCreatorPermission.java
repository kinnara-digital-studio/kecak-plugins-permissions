package com.kinnara.kecakplugins.permissions;

import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.FormPermission;
import org.joget.apps.userview.model.UserviewPermission;
import org.joget.workflow.model.WorkflowProcess;
import org.joget.workflow.model.service.WorkflowManager;
import org.springframework.context.ApplicationContext;

public class ProcessCreatorPermission extends UserviewPermission implements FormPermission {
    @Override
    public boolean isAuthorize() {
        ApplicationContext appContext = AppUtil.getApplicationContext();
        WorkflowManager workflowManager = (WorkflowManager) appContext.getBean("workflowManager");

        String primaryKey = getFormData().getPrimaryKeyValue();

        if(primaryKey == null || primaryKey.isEmpty())
            return false;

        WorkflowProcess process = workflowManager.getRunningProcessById(primaryKey);
        return process != null && getCurrentUser().getUsername().equalsIgnoreCase(process.getRequesterId());
    }

    @Override
    public String getName() {
        return "Process Creator Permission";
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
        return null;
    }
}
