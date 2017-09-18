package org.cantaloupe.audio.network.packets;

import java.util.Optional;

import org.cantaloupe.Cantaloupe;
import org.cantaloupe.audio.source.ISource;
import org.cantaloupe.network.web.WebServerPacket;
import org.json.simple.JSONObject;

public abstract class SXXXPacketSource extends WebServerPacket {
    protected ISource source = null;

    protected SXXXPacketSource(ISource source) {
        this.source = source;
    }

    @Override
    public void read(Object data) {
        super.read(data);

        Optional<ISource> sourceOpt = Cantaloupe.getAudioServer().get().getSourceManager().getSource((String) ((JSONObject) data).get("sID"));
        if (sourceOpt.isPresent()) {
            this.source = sourceOpt.get();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public JSONObject write() {
        JSONObject jsonObject = super.write();
        jsonObject.put("srcID", this.source.getID());

        return jsonObject;
    }

    public ISource getSource() {
        return this.source;
    }
}