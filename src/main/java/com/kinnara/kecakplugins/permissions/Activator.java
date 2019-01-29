package com.kinnara.kecakplugins.permissions;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import java.util.ArrayList;
import java.util.Collection;

public class Activator implements BundleActivator {

    protected Collection<ServiceRegistration> registrationList;

    public void start(BundleContext context) {
        registrationList = new ArrayList<>();

        //Register plugin here
        registrationList.add(context.registerService(IsAnonymousPermission.class.getName(), new IsAnonymousPermission(), null));
        registrationList.add(context.registerService(IsAdminPermission.class.getName(), new IsAdminPermission(), null));
        registrationList.add(context.registerService(NotPermission.class.getName(), new NotPermission(), null));
        registrationList.add(context.registerService(CompositePermission.class.getName(), new CompositePermission(), null));
        registrationList.add(context.registerService(ProcessWhitelistPermission.class.getName(), new ProcessWhitelistPermission(), null));
        registrationList.add(context.registerService(ProcessCreatorPermission.class.getName(), new ProcessCreatorPermission(), null));
        registrationList.add(context.registerService(ActivityPermission.class.getName(), new ActivityPermission(), null));
        registrationList.add(context.registerService(AlwaysTruePermission.class.getName(), new AlwaysTruePermission(), null));
    }

    public void stop(BundleContext context) {
        for (ServiceRegistration registration : registrationList) {
            registration.unregister();
        }
    }
}