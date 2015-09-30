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
     * Constructs a wrapper around the byte buffer provided.
     * @param buf The byte buffer to wrap around.
     */
    public Buffer(ByteBuf buf) {
        this.buf = buf;
    }

    /**
     * Returns the byte buffer this class wraps.
     * @return ByteBuf the byte buf
     */
    public ByteBuf getByteBuf() {
        return this.buf;
    }

    /**
     * Writes an integer to the byte buffer.
     * @param value integer
     */
    public void writeInt(int value) {
        this.buf.writeInt(value);
    }

    /**
     * Writes a long to the byte buffer.
     * @param value long
     */
    public void writeLong(long value) {
        this.buf.writeLong(value);
    }

    /**
     * Writes a short to the byte buffer.
     * @param value short
     */
    public void writeShort(short value) {
        this.buf.writeShort(value);
    }

    /**
     * Writes a byte to the byte buffer.
     * @param value byte
     */
    public void writeByte(byte value) {
        this.buf.writeByte(value);
    }

    /**
     * Writes a byte to the byte buffer.
     * @param value integer
     */
    public void writeByte(int value) {
        this.buf.writeByte(value);
    }

    /**
     * Writes a string to the byte buffer.
     * @param value string
     */
    public void writeString(String value) {
        byte[] array = value.getBytes();

        this.writeVarInt(array.length);
        this.buf.writeBytes(array);
    }

    /**
     * Writes a var int to the byte buffer.
     * @param value integer
     */
    public void writeVarInt(int value) {
        while ((value & -128L) != 0L) {
            this.writeByte((int) (value & 127L) | 128);
            value >>>= 7;
        }
        this.writeByte(value);
    }

    /**
     * Reads an integer from the byte buffer.
     * @return integer
     */
    public int readInt() {
        return this.buf.readInt();
    }

    /**
     * Reads a long from the byte buffer.
     * @return long
     */
    public long readLong() {
        return this.buf.readLong();
    }

    /**
     * Reads a short from the byte buffer.
     * @return short
     */
    public short readShort() {
        return this.buf.readShort();
    }

    /**
     * Reads an unsigned short from the byte buffer.
     * @return unsigned short
     */
    public int readUnsignedShort() {
        return this.buf.readUnsignedShort();
    }

    /**
     * Reads a byte from the byte buffer.
     * @return byte
     */
    public byte readByte() {
        return this.buf.readByte();
    }

    /**
     * Reads an unsigned byte from the byte buffer.
     * @return unsigned byte
     */
    public short readUnsignedByte() {
        return this.buf.readUnsignedByte();
    }

    /**
     * Reads a string from the byte buffer.
     * @return string
     */
    public String readString() {
        int length = this.readVarInt();

        byte[] array = new byte[length];
        this.buf.readBytes(array, 0, length);

        return new String(array, Charsets.UTF_8);
    }

    /**
     * Reads a var int from the byte buffer.
     * @return var int
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

    /**
     * Returns the amount of readable bytes in the byte buffer.
     * @return amount of bytes
     */
    public int readableBytes() {
        return this.buf.readableBytes();
    }

    /**
     * Marks the reader index.
     */
    public void markReaderIndex() {
        this.buf.markReaderIndex();
    }

    /**
     * Resets the reader index.
     */
    public void resetReaderIndex() {
        this.buf.resetReaderIndex();
    }

}
