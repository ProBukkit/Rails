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

import io.netty.channel.*;
import org.poweredrails.rails.net.packet.Packet;

import java.util.logging.Logger;

public class Session {

    private final Logger logger = Logger.getLogger("Rails");

    private Channel channel;

    private String address;
    private int port;

    private SessionStateEnum state = SessionStateEnum.HANDSHAKE;

    /**
     * <p>
     *     Create a new session around the channel handler context, represents the connection between a client
     *     and the server.
     * </p>
     *
     * @param ctx The connection.
     */
    public Session(ChannelHandlerContext ctx) {
        this.channel = ctx.channel();
    }

    /**
     * <p>
     *     Return the channel handler context for this session.
     * </p>
     *
     * @return The channel handler context for this session.
     */
    public Channel getChannel() {
        return this.channel;
    }

    /**
     * <p>
     *     Write a packet to the handler context, to be sent to the client.
     * </p>
     *
     * @param packet The packet to be written.
     */
    public void sendPacket(Packet<?> packet) {
//        TODO: Remove - debugging.
//        this.channel.writeAndFlush(packet, new DefaultChannelPromise(this.channel).addListener(new ChannelFutureListener() {
//            @Override
//            public void operationComplete(ChannelFuture future) throws Exception {
//                if (future.isSuccess()) {
//                    logger.info("Packet send success!");
//                } else {
//                    throw new RuntimeException("Packet send failure", future.cause());
//                }
//            }
//        }));

        this.channel.writeAndFlush(packet);
    }

    /**
     * <p>
     *     Return the state for this session.
     * </p>
     *
     * @return This session's state.
     */
    public SessionStateEnum getState() {
        return this.state;
    }

    /**
     * <p>
     *     Change the state of this session.
     * </p>
     *
     * @param state The state to change it to.
     */
    public void setState(SessionStateEnum state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return this.channel.toString();
    }

}
