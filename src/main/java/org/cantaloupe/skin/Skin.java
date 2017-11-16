package org.cantaloupe.skin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.cantaloupe.player.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class Skin {
    private String texture   = null;
    private String signature = null;

    private Skin(String texture, String signature) {
        this.texture = texture;
        this.signature = signature;
    }

    public static Skin of(String texture, String signature) {
        if (SkinCache.hasSkin(texture)) {
            return SkinCache.getSkin(texture);
        } else {
            Skin skin = new Skin(texture, signature);
            SkinCache.addSkin(texture, skin);

            return skin;
        }
    }

    public static Skin of(String name) {
        if (SkinCache.hasSkin(name)) {
            return SkinCache.getSkin(name);
        } else {
            JSONObject object = getTexture(getID(name));

            Skin skin = new Skin((String) object.get("value"), (String) object.get("signature"));
            SkinCache.addSkin(name, skin);

            return skin;
        }
    }

    public static Skin of(URL url) {
        if (SkinCache.hasSkin(url.toString())) {
            return SkinCache.getSkin(url.toString());
        } else {
            Skin skin = new Skin(Base64Coder.encodeString("{textures:{SKIN:{url:\"" + url.toString() + "\"}}}"), null);
            SkinCache.addSkin(url.toString(), skin);

            return skin;
        }
    }

    public static Skin of(Player player) {
        return of(player.getUUID());
    }

    public static Skin of(UUID uuid) {
        if (SkinCache.hasSkin(uuid.toString())) {
            return SkinCache.getSkin(uuid.toString());
        } else {
            JSONObject object = getTexture(uuid.toString().replaceAll("-", ""));

            Skin skin = new Skin((String) object.get("value"), (String) object.get("signature"));
            SkinCache.addSkin(uuid.toString(), skin);

            return skin;
        }
    }

    private static String getID(String name) {
        InputStream inputStream = null;

        try {
            inputStream = new URL("https://api.mojang.com/users/profiles/minecraft/" + name).openStream();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        try {
            JSONParser parser = new JSONParser();
            JSONObject object = (JSONObject) parser.parse(IOUtils.toString(inputStream, Charset.defaultCharset()));

            return (String) object.get("id");
        } catch (ParseException | IOException e) {
            e.printStackTrace();

            return null;
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    private static JSONObject getTexture(String ID) {
        InputStream inputStream = null;

        try {
            inputStream = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + ID + "?unsigned=false").openStream();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        try {
            JSONParser parser = new JSONParser();
            JSONObject object = (JSONObject) parser.parse(IOUtils.toString(inputStream, Charset.defaultCharset()));
            JSONArray properties = (JSONArray) object.get("properties");

            return (JSONObject) properties.get(0);
        } catch (ParseException | IOException e) {
            e.printStackTrace();

            return null;
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    public String getTexture() {
        return this.texture;
    }

    public String getSignature() {
        return this.signature;
    }
}