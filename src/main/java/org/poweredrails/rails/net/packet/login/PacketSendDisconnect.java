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
package org.poweredrails.rails.net.packet.login;

import org.poweredrails.rails.net.buffer.Buffer;
import org.poweredrails.rails.net.handler.HandlerRegistry;
import org.poweredrails.rails.net.handler.login.LoginPacketHandler;
import org.poweredrails.rails.net.packet.Packet;
import org.poweredrails.rails.net.session.Session;

/**
 * TODO: Reason should be a TextComponent (JSON construct).
 */
public class PacketSendDisconnect extends Packet<LoginPacketHandler> {

    private static final long serialVersionUID = 6223476178758093139L;

    private String reason;

    public PacketSendDisconnect(String reason) {
        this.reason = reason;
    }

    @Override
    public void toBuffer(Buffer buffer) {
        buffer.writeString(this.reason);
    }

    @Override
    public void fromBuffer(Buffer buffer) {}

    @Override
    public void handle(LoginPacketHandler handler) {}

}
