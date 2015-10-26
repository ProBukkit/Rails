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

import java.util.List;

public class EventDispatcher {

    private final ListenerRegistry registry;

    public EventDispatcher(ListenerRegistry registry) {
        this.registry = registry;
    }

    /**
     * Dispatches an event across its handlers.
     * @param event the event to dispatch
     */
    public void dispatch(Event event) {
        List<EventHandler> handlers = this.registry.getHandlersFor(event);

        for (EventHandler handler : handlers) {
            if (event instanceof CancellableEvent) {
                CancellableEvent cancellableEvent = (CancellableEvent) event;
                if (handler.ignoresCancelled() && cancellableEvent.isCancelled()) {
                    continue;
                }
            }

            handler.handle(event);
        }
    }

    /**
     * Dispatches a packet event across its handlers, for a packet.
     * @param packet the packet to dispatch
     * @param <T> the packet type
     * @return true if the packet was cancelled
     */
    public <T extends Packet<?>> boolean dispatchPacket(T packet) {
        List<EventHandler> handlers = this.registry.getHandlersFor(packet);

        PacketEvent<T> event = new PacketEvent<>(packet);

        for (EventHandler handler : handlers) {
            if (handler.ignoresCancelled() && event.isCancelled()) {
                continue;
            }

            handler.handle(event);
        }

        return event.isCancelled();
    }

}
