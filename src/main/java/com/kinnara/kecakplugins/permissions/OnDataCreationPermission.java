package com.kinnara.kecakplugins.permissions;

import org.joget.apps.form.model.FormData;
import org.joget.apps.form.model.FormPermission;
import org.joget.apps.userview.model.UserviewPermission;

import java.util.Objects;
import java.util.Optional;

/**
 * @author aristo
 *
 * Returns true when contains no data (form data or primary key is NULL)
 */
public class OnDataCreationPermission extends UserviewPermission implements FormPermission {
    @Override
    public boolean isAuthorize() {
        return !Optional.ofNullable(getFormData())
                .map(FormData::getPrimaryKeyValue)
                .isPresent();
    }

    @Override
    public String getName() {
        return "On Data Creation Permission";
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
        return "";
    }
}
