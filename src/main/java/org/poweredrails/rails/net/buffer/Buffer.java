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
package org.poweredrails.rails.net.buffer;

import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

public class Buffer {

    private ByteBuf buf;

    /**
     * <p>
     *     Constructs a wrapper around the byte buffer provided.
     * </p>
     *
     * @param buf The byte buffer to wrap around.
     */
    public Buffer(ByteBuf buf) {
        this.buf = buf;
    }

    /**
     * @return The byte buffer currently wrapping around.
     */
    public ByteBuf getByteBuf() {
        return this.buf;
    }

    /**
     * @return An integer, read from the buffer.
     */
    public int readInt() {
        return this.buf.readInt();
    }

    /**
     * @return A long, read from the buffer.
     */
    public long readLong() {
        return this.buf.readLong();
    }

    /**
     * @return A short, read from the buffer.
     */
    public short readShort() {
        return this.buf.readShort();
    }

    /**
     * @return A unsigned short, read from the buffer.
     */
    public int readUnsignedShort() {
        return this.buf.readUnsignedShort();
    }

    /**
     * @return A byte, read from the buffer.
     */
    public byte readByte() {
        return this.buf.readByte();
    }

    /**
     * @return An unsigned byte, read from the buffer.
     */
    public short readUnsignedByte() {
        return this.buf.readUnsignedByte();
    }

    /**
     * @return A string, read from the buffer.
     */
    public String readString() {
        int length = this.readVarInt();

        byte[] array = new byte[length];
        this.buf.readBytes(array, 0, length);

//        return new String(this.buf.readBytes(length).array(), Charsets.UTF_8);
        return new String(array, Charsets.UTF_8);
    }

    /**
     * @return A var int, read from the buffer.
     */
    public int readVarInt() {
        int result = 0;
        int count = 0;
        while (true) {
            byte in = readByte();
            result |= (in & 0x7f) << (count++ * 7);
            if (count > 5) {
                throw new RuntimeException("VarInt byte count > 5");
            }
            if ((in & 0x80) != 0x80) {
                break;
            }
        }
        return result;
    }

}
