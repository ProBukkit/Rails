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
package org.poweredrails.rails.events.api;

import com.google.common.reflect.TypeToken;
import org.poweredrails.rails.net.packet.Packet;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

public class ListenerRegistry {

    private Map<EventHandler, Listener> handlers = new ConcurrentSkipListMap<>(new EventPriorityComparator());

    /**
     * Registers a listener's event handlers to watch when events are dispatched.
     * @param listener the listener to register
     */
    public void register(Listener listener) {
        Class<? extends Listener> clazz = listener.getClass();

        for (Method method : clazz.getMethods()) {
            TypeToken<?> token = verify(method);
            Subscribe subscribe = getAnnotation(method);

            if (token != null) {
                EventHandler handler = new EventHandler(listener, method, token, subscribe);
                this.handlers.put(handler, listener);
            }
        }
    }

    /**
     * Unregisters a previously registered listener, removing all watching event handlers.
     * @param listener the listener to unregister
     */
    public void unregister(Listener listener) {
        this.handlers.forEach((handler, handlerListener) -> {
                if (listener.equals(handlerListener)) {
                    this.handlers.remove(handler);
                }
            });
    }

    /**
     * Returns all the watching event handlers specific to this event.
     * @param event the event
     * @return list of all matching event handlers
     */
    public List<EventHandler> getHandlersFor(Event event) {
        List<EventHandler> list = new ArrayList<>();

        this.handlers.keySet().forEach(handler -> {
                if (handler.handlesEvent(event.getClass())) {
                    list.add(handler);
                }
            });

        return list;
    }

    public List<EventHandler> getHandlersFor(Packet<?> packet) {
        List<EventHandler> list = new ArrayList<>();

        this.handlers.keySet().forEach(handler -> {
                if (handler.handlesEvent(PacketEvent.class)
                        && handler.handlesPacket(packet)) {
                    list.add(handler);
                }
            });

        return list;
    }

    /**
     * Verifies if a method is an event handler, and if so returns the event it specifies.
     * This will return null if the method is not an event handler.
     * @param method the method
     * @return the event class which the handler specifies
     */
    @SuppressWarnings("unchecked")
    private TypeToken<?> verify(Method method) {
        if (method.isAnnotationPresent(Subscribe.class)
                && method.getParameterCount() == 1
                && Event.class.isAssignableFrom(method.getParameterTypes()[0])) {
            return TypeToken.of(method.getGenericParameterTypes()[0]);
        }

        return null;
    }

    private Subscribe getAnnotation(Method method) {
        return method.getAnnotation(Subscribe.class);
    }

}
