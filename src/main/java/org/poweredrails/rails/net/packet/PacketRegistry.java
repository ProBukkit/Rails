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
package org.poweredrails.rails.net.packet;

import org.poweredrails.rails.net.packet.handshake.PacketReceiveHandshake;

import java.util.HashMap;
import java.util.Map;

public class PacketRegistry {

    private Map<SessionStateEnum, Map<Integer, Class<? extends Packet>>> packets = new HashMap<>();

    /**
     * <p>
     *     Register all packets to their ids.
     * </p>
     */
    public PacketRegistry() {
        register(SessionStateEnum.HANDSHAKE, 0x00, PacketReceiveHandshake.class);
    }

    /**
     * <p>
     *     Finds the class of the packet registered to that id, then creates a new instance of it.
     * </p>
     *
     * @param state The session state.
     * @param id The id of the packet.
     * @return A new instance of the packet registered.
     */
    public Packet createPacket(SessionStateEnum state, int id) {
        Class<? extends Packet> clazz = getPacketClass(state, id);

        if (clazz == null) {
            throw new RuntimeException("Packet doesn't exist by state " + state.name() + " and id " + id + "!");
        }

        Packet packet = null;
        try {
            packet = clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("Failed to create an instance of the packet class " + clazz.getName(), e);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return packet;
    }

    /**
     * <p>
     *     Finds the class of the packet registered to that id, and returns it.
     * </p>
     *
     * @param state The session state.
     * @param id The id of the packet.
     * @return The class of the packet.
     */
    public Class<? extends Packet> getPacketClass(SessionStateEnum state, int id) {
        if (this.packets.containsKey(state)) {
            return this.packets.get(state).get(id);
        }

        return null;
    }

    private void register(SessionStateEnum state, int id, Class<? extends Packet> clazz) {
        if (this.packets.containsKey(state)) {
            this.packets.get(state).put(id, clazz);
        }

        Map<Integer, Class<? extends Packet>> map = new HashMap<>();
        map.put(id, clazz);
        this.packets.put(state, map);
    }

}
