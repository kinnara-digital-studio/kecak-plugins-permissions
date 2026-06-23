package com.kinnarastudio.kecakplugins.permissions;

import com.kinnarastudio.commons.Try;
import com.kinnarastudio.commons.jsonstream.JSONCollectors;
import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.FormPermission;
import org.joget.apps.userview.model.Permission;
import org.joget.apps.userview.model.UserviewAccessPermission;
import org.joget.commons.util.LogUtil;
import org.joget.plugin.base.PluginManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kecak.apps.form.model.FormPermissionDefault;

import javax.annotation.Nonnull;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;

/**
 * Date Time Permission
 */
public class DateTimePermission extends FormPermissionDefault {
    public final static String LABEL = "Date Time Permission";

    public final static String[] dateFormat = new String[]{
            "dd-MM-yyyy",
            "dd-MM-yyyy HH:mm",
            "dd-MM-yyyy HH:mm:ss",
            "yyyy-MM-dd",
            "yyyy-MM-dd HH:mm",
            "yyyy-MM-dd HH:mm:ss"
    };

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

        LogUtil.info(getClassName(), "dateTime [" + dateTime + "]");

        final String operator = getOperator();

        final Date[] values = getValues();

        switch (operator) {
            case "=":
                return Arrays.asList(values).contains(dateTime);
            case "<>":
                return Arrays.stream(values).noneMatch(dateTime::equals);
            case ">":
                return Arrays.stream(values).filter(Objects::nonNull).findFirst().map(dateTime::after).orElse(false);
            case ">=":
                return Arrays.stream(values).filter(Objects::nonNull).findFirst().map(value -> dateTime.after(value) || dateTime.equals(value)).orElse(false);
            case "<":
                return Arrays.stream(values).filter(Objects::nonNull).findFirst().map(dateTime::before).orElse(false);
            case "<=":
                return Arrays.stream(values).filter(Objects::nonNull).findFirst().map(value -> dateTime.before(value) || dateTime.equals(value)).orElse(false);
            case "between":
                return Arrays.stream(values).filter(Objects::nonNull).findFirst()
                        .map(value -> dateTime.after(value) || dateTime.equals(value))
                        .orElse(false)
                        &&
                        Arrays.stream(values).filter(Objects::nonNull).skip(1).findFirst()
                                .map(value -> dateTime.before(value) || dateTime.equals(value))
                                .orElse(false);
            case "notBetween":
                return !(Arrays.stream(values).filter(Objects::nonNull).findFirst()
                        .map(value -> dateTime.after(value) || dateTime.equals(value))
                        .orElse(false)
                        &&
                        Arrays.stream(values).filter(Objects::nonNull).skip(1).findFirst()
                                .map(value -> dateTime.before(value) || dateTime.equals(value))
                                .orElse(false));
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

        JSONArray dateTimeFormat = Arrays.stream(dateFormat)
                .map(s -> new JSONObject() {{
                    try {
                        put("value", s);
                        put("label", s);
                    } catch (JSONException ignored) {
                    }
                }})
                .collect(JSONCollectors.toJSONArray());

        final String[] args = new String[]{
                dateTimeFormat.toString(),
                df.format(new Date())
        };

        return AppUtil.readPluginResource(getClassName(), "/properties/DateTimePermission.json", args, false,
                "/messages/DateTimePermission");
    }

    @Nonnull
    protected Date[] getValues() {
        final DateFormat df = getDateTimeFormat();
        return Optional.of("values")
                .map(this::getPropertyString)
                .map(s -> AppUtil.processHashVariable(s, null, null, null))
                .map(s -> s.split(";"))
                .stream()
                .flatMap(Arrays::stream)
                .filter(Objects::nonNull)
                .filter(Predicate.not(String::isEmpty))
                .map(Try.onFunction(df::parse))
                .filter(Objects::nonNull)
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
