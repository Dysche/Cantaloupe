package org.cantaloupe.util;

import java.lang.reflect.InvocationTargetException;

import org.cantaloupe.Cantaloupe;
import org.cantaloupe.service.services.NMSService;

import com.google.common.base.MoreObjects.ToStringHelper;

public class GuavaUtils {
    public static ToStringHelper toStringHelper(Object object) {
        int version = Cantaloupe.getServiceManager().provide(NMSService.class).getIntVersion();

        if (version < 12) {
            try {
                return (ToStringHelper) Class.forName("Objects").getMethod("toStringHelper", Object.class).invoke(null, object);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            try {
                return (ToStringHelper) Class.forName("MoreObjects").getMethod("toStringHelper", Object.class).invoke(null, object);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}