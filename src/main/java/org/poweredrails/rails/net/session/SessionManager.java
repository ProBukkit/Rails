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
package org.poweredrails.rails.net.session;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class SessionManager {

    private final Logger logger = Logger.getLogger("Rails");

    private List<Session> sessionList = new ArrayList<>();

    public Session getSession(Channel channel) {
        for (Session session : this.sessionList) {
            if (session.getChannel().equals(channel)) {
                return session;
            }
        }

        Session session = new Session(channel);
        this.sessionList.add(session);
        return session;
    }

    /**
     * Gets an instance of the session for the connection it relates to.
     * @param ctx connection
     * @return session
     */
    @Deprecated
    public Session getSession(ChannelHandlerContext ctx) {
        for (Session session : this.sessionList) {
            if (session.getChannel().equals(ctx.channel())) {
                return session;
            }
        }

        Session session = new Session(ctx);
        this.sessionList.add(session);
        return session;
    }

    /**
     * Disposes of any sessions relating to this connection.
     * @param ctx connection
     */
    public void dispose(ChannelHandlerContext ctx) {
        Iterator<Session> it = this.sessionList.iterator();

        while (it.hasNext()) {
            Session session = it.next();
            if (session.getChannel().equals(ctx.channel())) {
                it.remove();
            }
        }
    }

}
