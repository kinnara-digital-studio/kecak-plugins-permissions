package com.kinnara.kecakplugins.permissions;

import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.DefaultFormPermission;
import org.joget.apps.form.model.Element;
import org.joget.apps.form.model.FormData;
import org.joget.apps.form.service.FormUtil;
import org.joget.apps.userview.model.UserviewPermission;

public class ReadOnlyPermission extends DefaultFormPermission {
    @Override
    public boolean isAuthorize() {
        UserviewPermission plugin = Utilities.getPermissionObject(this, "permission");

        final boolean hasPermission = (plugin == null || plugin.isAuthorize());
        final boolean readonlyLabel = "true".equalsIgnoreCase(getPropertyString(FormUtil.PROPERTY_READONLY_LABEL));

        // process the element based on permission
        FormData formData = getFormData();
        Element element = getElement();

        if(formData != null && element != null) {
            element.getChildren(formData)
                    .forEach(e -> {
                        if (!hasPermission) {
                            // if don't have write access, set as readonly
                            FormUtil.setReadOnlyProperty(e, true, readonlyLabel);
                        }
                    });
        }
        return true;
    }

    @Override
    public String getName() {
        return "Read-Only Permission";
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
        return AppUtil.readPluginResource(getClassName(), "/properties/ReadonlyPermission.json", null, false, "/messages/ReadonlyPermission");
    }
}
