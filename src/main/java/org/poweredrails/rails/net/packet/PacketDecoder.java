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
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.poweredrails.rails.net.buffer.Buffer;
import org.poweredrails.rails.net.packet.registry.PacketRegistry;
import org.poweredrails.rails.net.session.Session;
import org.poweredrails.rails.net.session.SessionManager;
import org.poweredrails.rails.net.session.SessionStateEnum;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class PacketDecoder extends ByteToMessageDecoder {

    private final Random rand = new Random();

    private final Logger logger;

    private PacketRegistry registry;
    private SessionManager sessionManager;

    /**
     * Construct a packet decoder for netty, injecting the packet registry.
     * @param logger logger
     * @param sessionManager session manager
     * @param registry packet registry
     */
    public PacketDecoder(Logger logger, SessionManager sessionManager, PacketRegistry registry) {
        this.logger = logger;
        this.sessionManager = sessionManager;
        this.registry = registry;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        this.logger.severe("An error occurred while decoding:" + cause);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
        Buffer in = new Buffer(buf);

        in.markReaderIndex();
        if (!readableVarInt(buf)) {
            return;
        }

        // Read packet length + byte array
        int length = in.readVarInt();

        // If we don't have the data, return.
        if (in.readableBytes() < length) {
            in.resetReaderIndex();
            return;
        }

        Buffer buffer = new Buffer( buf.readBytes(length) );

        // Read id + get session
        int id = buffer.readVarInt();
        Session session = this.sessionManager.getSession(ctx);

        // Create UnresolvedPacket + add to handler queue
        UnresolvedPacket packet = new UnresolvedPacket(session, id, buffer);
        out.add(packet);
    }

    private static boolean readableVarInt(ByteBuf buf) {
        if (buf.readableBytes() > 5) {
            // maximum varint size
            return true;
        }

        int idx = buf.readerIndex();
        byte in;
        do {
            if (buf.readableBytes() < 1) {
                buf.readerIndex(idx);
                return false;
            }
            in = buf.readByte();
        } while ((in & 0x80) != 0);

        buf.readerIndex(idx);
        return true;
    }

}
