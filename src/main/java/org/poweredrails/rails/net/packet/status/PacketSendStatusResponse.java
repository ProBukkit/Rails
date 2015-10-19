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
package org.poweredrails.rails.net.packet.status;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.poweredrails.rails.net.buffer.Buffer;
import org.poweredrails.rails.net.handler.status.StatusPacketHandler;
import org.poweredrails.rails.net.packet.Packet;
import org.poweredrails.rails.net.session.Session;

public class PacketSendStatusResponse extends Packet<StatusPacketHandler> {

    private static final long serialVersionUID = -6126870628730207287L;

    private JSONObject json;

    public PacketSendStatusResponse() throws JSONException {
        this.json = new JSONObject()
                .put("version", new JSONObject()
                        .put("name", "1.8.8")
                        .put("protocol", 47))
                .put("players", new JSONObject()
                        .put("max", 100)
                        .put("online", 5)
                        .put("sample", new JSONArray()
                                .put(new JSONObject()
                                        .put("name", "Bluesocks")
                                        .put("id", "8652d6de-69bd-4319-8991-065231982198"))))
                .put("description", new JSONObject()
                        .put("text", "Hello world"));
    }

    @Override
    public void toBuffer(Buffer buffer) {
        buffer.writeString(this.json.toString());
    }

    @Override
    public void fromBuffer(Buffer buffer) {}

    @Override
    public void handle(StatusPacketHandler handler) {}

}
