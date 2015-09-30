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

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.poweredrails.rails.net.buffer.Buffer;
import org.poweredrails.rails.net.packet.registry.PacketRegistry;
import org.poweredrails.rails.net.session.Session;
import org.poweredrails.rails.net.session.SessionManager;
import org.poweredrails.rails.net.session.SessionStateEnum;

import java.util.logging.Logger;

public class PacketEncoder extends MessageToByteEncoder<Packet<?>> {

    private final Logger logger;

    private SessionManager sessionManager;
    private PacketRegistry registry;

    /**
     * Construct a packet encoder for netty.
     * @param logger logger
     * @param sessionManager session manager
     * @param registry packet registry
     */
    public PacketEncoder(Logger logger, SessionManager sessionManager, PacketRegistry registry) {
        this.logger = logger;
        this.sessionManager = sessionManager;
        this.registry = registry;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet<?> packet, ByteBuf buf) throws Exception {
        Buffer out = new Buffer(buf);

        Session session = this.sessionManager.getSession(ctx);
        SessionStateEnum state = session.getState();

        int id = this.registry.find(state, packet);

        out.writeVarInt(id);
        packet.toBuffer(out);
    }

}
