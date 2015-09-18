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
import io.netty.handler.codec.ByteToMessageDecoder;
import org.poweredrails.rails.net.buffer.Buffer;

import java.util.List;
import java.util.logging.Logger;

public class PacketDecoder extends ByteToMessageDecoder {

    private final Logger logger;

    private PacketRegistry registry;

    /**
     * <p>
     *     Construct a packet decoder for netty, injecting the packet registry.
     * </p>
     *
     * @param logger An instance of the server logger.
     * @param registry An instance of the packet registry.
     */
    public PacketDecoder(Logger logger, PacketRegistry registry) {
        this.logger = logger;
        this.registry = registry;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        this.logger.info("PacketDecoder > Decoding...");

        Buffer buffer = new Buffer(in);

        int length = buffer.readVarInt(); // This line may cause futures in the problem.
        if (in.readableBytes() < length) {
            return;
        }

        int id = buffer.readVarInt();

        Packet packet = this.registry.createPacket(SessionStateEnum.STATUS, id);
        packet.fromBuffer(buffer);

        out.add(packet);

        this.logger.info("PacketDecoder > Decoded: " + packet.getClass().getName());
    }

}
