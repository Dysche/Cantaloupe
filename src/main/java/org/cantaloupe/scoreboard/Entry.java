package org.cantaloupe.scoreboard;

import org.cantaloupe.scoreboard.Objective;

public abstract class Entry {
    protected Objective objective = null;
    
    protected Entry() {
        
    }
    
    protected void setObjective(Objective objective) {
        this.objective = objective;
    }

    public abstract String getText();
}