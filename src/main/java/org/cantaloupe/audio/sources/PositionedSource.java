package org.cantaloupe.audio.sources;

import org.cantaloupe.audio.AudioWrapper;
import org.cantaloupe.audio.source.WorldSource;
import org.cantaloupe.player.Player;
import org.cantaloupe.world.location.ImmutableLocation;

/**
 * A physical source placed in a world.
 * 
 * @author Dylan Scheltens
 *
 */
public class PositionedSource extends WorldSource {
    private PositionedSource(String ID, ImmutableLocation location) {
        super(ID, location);
    }

    @Override
    protected void tick() {
        if (this.hasStarted()) {
            for (Player player : this.getWorld().getPlayers()) {
                AudioWrapper wrapper = player.getWrapper(AudioWrapper.class);

                if (wrapper.isConnected()) {
                    if (wrapper.isConnected()) {
                        if (player.getPosition().distance(this.getLocation().getPosition()) < this.getSettings().getMaxRadius()) {
                            if (this.getElapsedTime() < this.getSound().getDuration()) {
                                if (!this.hasPlayer(player)) {
                                    this.addPlayer(player);
                                }

                                if (!wrapper.getSourceSettings(this).isPlaying() && !wrapper.getSourceSettings(this).isPaused()) {
                                    this.playBounds(player, this.getSettings().calculateVolume(this, player), this.getSettings().usePan() ? this.getSettings().calculatePan(this, player) : 0, this.getElapsedTime(), -1);
                                }
                            }
                        } else {
                            if (this.hasPlayer(player)) {
                                this.stop(player);
                                this.removePlayer(player);
                            }
                        }
                    }
                }
            }

            if (this.getElapsedTime() >= this.getSound().getDuration()) {
                this.stop();
            }
        }
    }

    @Override
    protected void tickFor(Player player) {
        if (this.hasStarted()) {
            AudioWrapper wrapper = player.getWrapper(AudioWrapper.class);

            if (wrapper.isConnected()) {
                if (wrapper.isConnected()) {
                    if (this.hasPlayer(player)) {
                        if (player.getWorld() != this.getWorld()) {
                            this.stop(player);
                            this.removePlayer(player);
                        } else {
                            if (wrapper.getSourceSettings(this).isPlaying()) {
                                int newVolume = this.getSettings().calculateVolume(this, player);
                                int oldVolume = wrapper.getSourceSettings(this).getVolume();
                                int oldPan = wrapper.getSourceSettings(this).getPan();

                                if (this.getSettings().usePan()) {
                                    int newPan = this.getSettings().calculatePan(this, player);

                                    if (oldVolume != newVolume && oldPan != newPan) {
                                        this.setVolumePan(player, newVolume, newPan);
                                    } else if (oldVolume != newVolume) {
                                        this.setVolume(player, newVolume);
                                    } else {
                                        if (oldPan != newPan) {
                                            this.setPan(player, newPan);
                                        }
                                    }
                                } else {
                                    if (oldVolume != newVolume) {
                                        this.setVolume(player, newVolume);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}