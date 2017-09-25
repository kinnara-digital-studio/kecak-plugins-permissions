package com.kinnara.kecakplugins.permissions;

import java.util.ArrayList;
import java.util.Collection;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator {

    protected Collection<ServiceRegistration> registrationList;

    public void start(BundleContext context) {
        registrationList = new ArrayList<ServiceRegistration>();

        //Register plugin here
        registrationList.add(context.registerService(IsAnonymousPermission.class.getName(), new IsAnonymousPermission(), null));
        registrationList.add(context.registerService(IsAdminPermission.class.getName(), new IsAdminPermission(), null));
        registrationList.add(context.registerService(NotPermission.class.getName(), new NotPermission(), null));
    }

    public void stop(BundleContext context) {
        for (ServiceRegistration registration : registrationList) {
            registration.unregister();
        }
    }
}