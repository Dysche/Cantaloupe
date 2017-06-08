package org.cantaloupe.service.services;

import org.cantaloupe.Cantaloupe;
import org.cantaloupe.plugin.CantaloupePlugin;
import org.cantaloupe.service.Service;

public class ScheduleService implements Service {
    public ScheduleService() {
        if (Cantaloupe.getServiceManager().provide(ScheduleService.class) != null) {
            CantaloupePlugin provider = Cantaloupe.getServiceManager().getProvider(ScheduleService.class);
            
            throw new RuntimeException("'" + provider.getID() + "' is trying to initialize a Cantaloupe base service.");
        }
    }

    @Override
    public String getName() {
        return "schedule";
    }
}