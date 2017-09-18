package org.cantaloupe.scoreboard;

import java.util.Collection;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.cantaloupe.data.DataContainer;

/**
 * A class used to create a scoreboard.
 * 
 * @author Dylan Scheltens
 *
 */
public class Scoreboard {
    private final org.bukkit.scoreboard.Scoreboard handle;
    private final DataContainer<String, Objective> objectives;
    private final DataContainer<String, Team>      teams;

    private Scoreboard() {
        this.handle = Bukkit.getScoreboardManager().getNewScoreboard();
        this.objectives = DataContainer.of();
        this.teams = DataContainer.of();
    }

    /**
     * Creates and returns a new scoreboard.
     * 
     * @return The scoreboard
     */
    public static Scoreboard of() {
        return new Scoreboard();
    }

    /**
     * Creates and returns a new objective.
     * 
     * @param name
     *            The name of the objective
     * @param criteria
     *            The criteria of the objective
     * 
     * @return The objective
     */
    public Objective createObjective(String name, String criteria) {
        Objective objective = new Objective(this.handle.registerNewObjective(name, criteria));
        this.objectives.put(name, objective);

        return objective;
    }

    /**
     * Removes an objective form the scoreboard.
     * 
     * @param name
     *            The name of the objective
     */
    public void removeObjective(String name) {
        Objective objective = this.objectives.get(name);

        if (objective != null) {
            objective.toHandle().unregister();

            this.objectives.remove(name);
        }
    }

    /**
     * Creates and returns a new team.
     * 
     * @param name
     *            The name of the team
     * 
     * @return The team
     */
    public Team createTeam(String name) {
        Team team = new Team(this.handle.registerNewTeam(name));
        this.teams.put(name, team);

        return team;
    }

    /**
     * Removes a team from the scoreboard.
     * 
     * @param name
     *            The name of the team
     */
    public void removeTeam(String name) {
        Team team = this.teams.get(name);

        if (team != null) {
            team.toHandle().unregister();

            this.teams.remove(name);
        }
    }

    /**
     * Returns the handle of the scoreboard.
     * 
     * @return The handle
     */
    public org.bukkit.scoreboard.Scoreboard toHandle() {
        return this.handle;
    }

    /**
     * Gets an objective from the scoreboard.
     * 
     * @param name
     *            The name of an objective
     * @return An optional containing the objective if it's present, an empty
     *         optional if not
     */
    public Optional<Objective> getObjective(String name) {
        return Optional.ofNullable(this.objectives.get(name));
    }

    /**
     * Gets a collection of objectives from the scoreboard.
     * 
     * @return The collection of objectives
     */
    public Collection<Objective> getObjectives() {
        return this.objectives.valueSet();
    }

    /**
     * Gets a team from the scoreboard.
     * 
     * @param name
     *            The name of a team
     * @return An optional containing the team if it's present, an empty
     *         optional if not
     */
    public Optional<Team> getTeam(String name) {
        return Optional.ofNullable(this.teams.get(name));
    }

    /**
     * Gets a collection of teams from the scoreboard.
     * 
     * @return The collection of teams
     */
    public Collection<Team> getTeams() {
        return this.teams.valueSet();
    }
}