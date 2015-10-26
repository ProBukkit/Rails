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

import org.poweredrails.rails.net.packet.Packet;

public class EventBus {

    private final ListenerRegistry registry;
    private final EventDispatcher dispatcher;

    public EventBus() {
        this.registry   = new ListenerRegistry();
        this.dispatcher = new EventDispatcher(this.registry);
    }

    /**
     * Fires an event.
     * @param event the event
     */
    public void fire(Event event) {
        this.dispatcher.dispatch(event);
    }

    /**
     * Fires a packet event for the packet.
     * @param packet the packet
     * @param <T> the packet type
     * @return true if the packet was cancelled
     */
    public <T extends Packet<?>> boolean firePacket(T packet) {
        return this.dispatcher.dispatchPacket(packet);
    }

    /**
     * Registers a listener.
     * @param listener the listener
     */
    public void registerListener(Listener listener) {
        this.registry.register(listener);
    }

    /**
     * Unregisters a listener.
     * @param listener the listener
     */
    public void unregisterListener(Listener listener) {
        this.registry.unregister(listener);
    }

}
