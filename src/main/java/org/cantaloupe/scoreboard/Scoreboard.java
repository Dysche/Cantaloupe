package org.cantaloupe.scoreboard;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.cantaloupe.data.DataContainer;

public class Scoreboard {
    private final org.bukkit.scoreboard.Scoreboard handle;
    private final DataContainer<String, Objective> objectives;
    private final DataContainer<String, Team>      teams;

    private Scoreboard() {
        this.handle = Bukkit.getScoreboardManager().getNewScoreboard();
        this.objectives = DataContainer.of();
        this.teams = DataContainer.of();
    }

    public static Scoreboard of() {
        return new Scoreboard();
    }

    public Objective createObjective(String name, String criteria) {
        Objective objective = new Objective(this.handle.registerNewObjective(name, criteria));
        this.objectives.put(name, objective);

        return objective;
    }

    public void removeObjective(String name) {
        Objective objective = this.objectives.get(name);

        if (objective != null) {
            objective.toHandle().unregister();

            this.objectives.remove(name);
        }
    }

    public Team createTeam(String name) {
        Team team = new Team(this.handle.registerNewTeam(name));
        this.teams.put(name, team);

        return team;
    }

    public void removeTeam(String name) {
        Team team = this.teams.get(name);

        if (team != null) {
            team.toHandle().unregister();

            this.teams.remove(name);
        }
    }

    public org.bukkit.scoreboard.Scoreboard toHandle() {
        return this.handle;
    }

    public Objective getObjective(String name) {
        return this.objectives.get(name);
    }

    public Collection<Objective> getObjectives() {
        return this.objectives.valueSet();
    }

    public Team getTeam(String name) {
        return this.teams.get(name);
    }

    public Collection<Team> getTeams() {
        return this.teams.valueSet();
    }
}