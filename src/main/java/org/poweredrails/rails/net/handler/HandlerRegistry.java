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
package org.poweredrails.rails.net.handler;

import org.poweredrails.rails.net.handler.handshake.HandshakePacketHandler;
import org.poweredrails.rails.net.handler.login.LoginPacketHandler;
import org.poweredrails.rails.net.handler.status.StatusPacketHandler;
import org.poweredrails.rails.net.packet.Packet;
import org.poweredrails.rails.net.session.Session;

import java.util.ArrayList;
import java.util.List;

public class HandlerRegistry {

    private List<Object> handlerList = new ArrayList<>();

    /**
     * Registers any packet handlers.
     */
    public HandlerRegistry() {
        this.handlerList.add(new HandshakePacketHandler());
        this.handlerList.add(new StatusPacketHandler());
        this.handlerList.add(new LoginPacketHandler());
    }

    /**
     * Calls the handle method on a packet.
     * @param <T> handler type
     * @param packet packet
     */
    public <T> void doHandle(Packet<T> packet) {
        Class<T> clazz = packet.getHandlerClass();
        T handler = getHandler(clazz);

        if (handler != null) {
            packet.handle(handler);
        }
    }

    /**
     * Returns the handler instance registered by that class.
     * @param clazz handler class
     * @param <T> handler type
     * @return handler instance
     */
    @SuppressWarnings("unchecked")
    public <T> T getHandler(Class<T> clazz) {
        for (Object obj : this.handlerList) {
            if (obj.getClass().equals(clazz)) {
                return (T) obj;
            }
        }

        return null;
    }

}
