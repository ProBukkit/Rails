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

import java.lang.reflect.Method;

public class EventHandler {

    private Listener listener;
    private Method method;
    private TypeToken<?> eventTypeToken;
    private Subscribe annotation;

    public EventHandler(Listener listener, Method method, TypeToken<?> eventTypeToken, Subscribe annotation) {
        this.listener = listener;
        this.method   = method;
        this.eventTypeToken = eventTypeToken;
        this.annotation = annotation;
    }

    public boolean handlesEvent(Class<? extends Event> clazz) {
        return clazz.equals(this.eventTypeToken.getRawType());
    }

    public boolean handlesPacket(Packet<?> packet) {
        Class<?> clazz = packet.getClass();
        Class<?> actualClass = this.eventTypeToken.resolveType(PacketEvent.class.getTypeParameters()[0]).getRawType();
        return clazz.equals(actualClass);
    }

    public void handle(Event event) {
        try {
            this.method.invoke(this.listener, event);
        } catch (Exception e) {
            throw new RuntimeException("An exception occurred whilst handling an event!", e);
        }
    }

    public EventPriority getPriority() {
        return this.annotation.priority();
    }

    public boolean ignoresCancelled() {
        return this.annotation.ignoreCancelled();
    }

}
