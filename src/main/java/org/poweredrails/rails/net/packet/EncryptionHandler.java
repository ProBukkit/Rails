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
import io.netty.handler.codec.MessageToMessageCodec;

import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;

public class EncryptionHandler extends MessageToMessageCodec<ByteBuf, ByteBuf> {

    private Cipher encryptCipher;
    private Cipher decryptCipher;

    public EncryptionHandler(SecretKey sharedSecret) {
        try {
            this.encryptCipher = Cipher.getInstance("AES/CFB8/NoPadding");
            this.decryptCipher = Cipher.getInstance("AES/CFB8/NoPadding");

            this.encryptCipher.init(Cipher.ENCRYPT_MODE, sharedSecret, new IvParameterSpec(sharedSecret.getEncoded()));
            this.decryptCipher.init(Cipher.DECRYPT_MODE, sharedSecret, new IvParameterSpec(sharedSecret.getEncoded()));
        } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException
                | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to initiate encryption handler!", e);
        }
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        ByteBuf buffer = update(msg, this.encryptCipher);
        out.add(buffer);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        ByteBuf buffer = update(msg, this.decryptCipher);
        out.add(buffer);
    }

    private ByteBuf update(ByteBuf msg, Cipher cipher) {
        ByteBuffer out = ByteBuffer.allocate(msg.readableBytes());

        try {
            cipher.update(msg.nioBuffer(), out);
        } catch (ShortBufferException e) {
            throw new RuntimeException("Failed to update an encrpytion buffer!");
        }

        out.flip();
        return Unpooled.wrappedBuffer(out);
    }

}
