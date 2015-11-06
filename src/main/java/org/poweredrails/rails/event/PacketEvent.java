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
package org.poweredrails.rails.event;

import com.google.common.reflect.TypeToken;
import org.poweredrails.rails.net.packet.Packet;
import org.poweredrails.rails.net.session.Session;

/**
 * A class used by event handlers to specify what packet it should be fired for.
 * @param <T> the packet type
 */
public class PacketEvent<T extends Packet<?>> extends CancellableEvent {

    private final TypeToken<T> token = new TypeToken<T>(getClass()) {
        private static final long serialVersionUID = 103948516358702773L;
    };

    private Session client;
    private final T packet;

    public PacketEvent(T packet) {
        this.packet = packet;
    }

    public T getPacket() {
        return this.packet;
    }

    public Session getClient() {
        return this.client;
    }

    public void setClient(Session client) {
        this.client = client;
    }

    @SuppressWarnings("unchecked")
    public Class<T> getPacketClass() {
        return (Class<T>) this.token.getRawType();
    }

}
