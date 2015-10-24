/*
 * This file is a part of the multiplayer platform Powered Rails, licensed under the MIT License (MIT).
 *
 * Copyright (c) Powered Rails
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.poweredrails.rails.net.handler.login;

import org.json.JSONException;
import org.json.JSONObject;
import org.poweredrails.rails.net.packet.login.PacketReceiveEncryptResponse;
import org.poweredrails.rails.net.packet.login.PacketReceiveLoginStart;
import org.poweredrails.rails.net.packet.login.PacketSendEncryptRequest;
import org.poweredrails.rails.net.session.Session;
import org.poweredrails.rails.util.UuidUtil;
import org.poweredrails.rails.util.crypto.EncryptUtil;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.UUID;
import java.util.logging.Logger;

public class LoginPacketHandler {

    private final Logger logger = Logger.getLogger("Rails");

    // TODO: Move this to a move sensible place.
    private final KeyPair keyPair;
    private final byte[] publicKey;
    private final PrivateKey privateKey;

    public LoginPacketHandler() {
        this.keyPair    = EncryptUtil.generateKeyPair();
        this.publicKey  = EncryptUtil.toX509(this.keyPair.getPublic()).getEncoded();
        this.privateKey = this.keyPair.getPrivate();
    }

    /**
     * Handles a login start packet.
     * @param packet login start packet
     */
    public void onLoginStart(PacketReceiveLoginStart packet) {
        this.logger.info("User [" + packet.getName() + "] logging in...");

        final Session sender = packet.getSender();

        String sessionId = sender.getSessionId();
        byte[] publicKey = EncryptUtil.toX509(this.keyPair.getPublic()).getEncoded();
        byte[] verifyKey = EncryptUtil.generateToken(4);

        sender.setVerifyUsername(packet.getName());
        sender.setVerifyToken(verifyKey);

        PacketSendEncryptRequest response = new PacketSendEncryptRequest(sessionId, publicKey, verifyKey);
        sender.sendPacket(response);
    }

    /**
     * Handles an encrypt response packet.
     * @param packet encrypt response packet
     */
    public void onEncryptResponse(PacketReceiveEncryptResponse packet) {
        this.logger.info("Received a PacketReceiveEncryptResponse from a User.");

        final Session sender = packet.getSender();

        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA");
        } catch (Exception e) {
            throw new RuntimeException("Failed to get an instance of a RSA cipher!", e);
        }

        SecretKey sharedSecret = null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, this.privateKey);
            sharedSecret = new SecretKeySpec(cipher.doFinal(packet.getSharedSecret()), "AES");
        } catch (Exception e) {
            // TODO: More accurately defined exception.
            throw new RuntimeException("...", e);
        }

        byte[] verifyToken = null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, this.privateKey);
            verifyToken = cipher.doFinal(packet.getVerifyToken());
        } catch (Exception e) {
            // TODO: More accurately defined exception.
            throw new RuntimeException("...", e);
        }

        if (!Arrays.equals(verifyToken, sender.getVerifyToken())) {
            // TODO: Disconnect user instead!
            throw new RuntimeException("Invalid verify token!");
        }

        // session.enableEncryption(sharedSecret);

        String hash;
        try {
            final MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(sender.getSessionId().getBytes());
            digest.update(sharedSecret.getEncoded());
            digest.update(this.publicKey);

            hash = new BigInteger(digest.digest()).toString(16);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate SHA-1 digest!", e);
        }

        new Thread(() -> {
                final String baseUrl = "https://sessionserver.mojang.com/session/minecraft/hasJoined?username=%s&serverId=%s";

                URLConnection connection = null;
                try {
                    connection = new URL(String.format(baseUrl, sender.getVerifyUsername(), hash)).openConnection();
                } catch (IOException e) {
                    throw new RuntimeException("Failed to open a connection to Mojang!", e);
                }

                JSONObject response = null;
                try {
                    final InputStream in = connection.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));

                    StringBuilder builder = new StringBuilder();
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        builder.append(line).append('\n');
                    }

                    response = new JSONObject(builder.toString());
                } catch (Exception e) {
                    // TODO: Disconnect user instead!
                    throw new RuntimeException("Failed to verify username!", e);
                }

                String name = null;
                String id   = null;
                try {
                    name = response.getString("name");
                    id   = response.getString("id");
                } catch (JSONException e) {
                    throw new RuntimeException("Failed to parse Mojang JSON response!", e);
                }

                UUID uuid = UuidUtil.fromFlatString(id);

                // TODO: Player Properties
                // TODO: Create new Profile
                // TODO: Dispatch PlayerLoginEvent

                this.logger.info("Successfully authenticated Player [" + name + ", " + uuid + "].");
            }).start();
    }

}
