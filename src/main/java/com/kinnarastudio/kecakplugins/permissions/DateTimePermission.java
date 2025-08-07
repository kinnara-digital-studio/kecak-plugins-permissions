package com.kinnarastudio.kecakplugins.permissions;

import com.kinnarastudio.commons.Try;
import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.FormPermission;
import org.joget.apps.userview.model.Permission;
import org.joget.apps.userview.model.UserviewAccessPermission;
import org.joget.commons.util.LogUtil;
import org.joget.plugin.base.PluginManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;

/**
 * Date Time Permission
 */
public class DateTimePermission extends Permission implements FormPermission, UserviewAccessPermission {
    public final static String LABEL = "Date Time Permission";

    @Override
    public boolean isAuthorize() {
        String compareTo = getCompareTo();

        final Date dateTime;
        if (compareTo != null && !compareTo.equalsIgnoreCase("")) {
            Date tempDate = null;
            try {
                tempDate = getDateTimeFormat().parse(compareTo);
            } catch (ParseException e) {
                LogUtil.error(getClassName(), e, e.getMessage());
            }
            dateTime = tempDate != null ? tempDate : new Date();
        } else {
            dateTime = new Date();
        }

        final String operator = getOperator();

        final Date[] values = getValues();

        switch (operator) {
            case "=":
                return Arrays.asList(values).contains(dateTime);
            case "<>":
                return Arrays.stream(values).noneMatch(dateTime::equals);
            case ">":
                return Arrays.stream(values).findFirst().map(dateTime::after).orElse(false);
            case ">=":
                return Arrays.stream(values).findFirst().map(value -> dateTime.after(value) || dateTime.equals(value)).orElse(false);
            case "<":
                return Arrays.stream(values).findFirst().map(dateTime::before).orElse(false);
            case "<=":
                return Arrays.stream(values).findFirst().map(value -> dateTime.before(value) || dateTime.equals(value)).orElse(false);
            default:
                return false;
        }
    }

    @Override
    public String getName() {
        return LABEL;
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
        return LABEL;
    }

    @Override
    public String getClassName() {
        return getClass().getName();
    }

    @Override
    public String getPropertyOptions() {
        final DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        final String[] args = new String[] { df.format(new Date()) };
        return AppUtil.readPluginResource(getClassName(), "/properties/DateTimePermission.json", args, false,
                "/messages/DateTimePermission");
    }

    protected Date[] getValues() {
        final DateFormat df = getDateTimeFormat();
        return Optional.ofNullable(getPropertyString("values"))
                .map(s -> s.split(";"))
                .stream()
                .flatMap(Arrays::stream)
                .filter(Predicate.not(String::isEmpty))
                .map(Try.onFunction(df::parse))
                .toArray(Date[]::new);
    }

    protected DateFormat getDateTimeFormat() {
        return new SimpleDateFormat(getPropertyString("dateTimeFormat"));
    }

    protected String getOperator() {
        return getPropertyString("operator");
    }

    protected String getCompareTo() {
        return getPropertyString("compareField");
    }
}
