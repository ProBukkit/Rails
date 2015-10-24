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
package org.poweredrails.rails;

import org.poweredrails.rails.log.ConsoleFormatter;
import org.poweredrails.rails.net.NetworkManager;

import java.net.InetSocketAddress;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger("Rails");

    protected Main(NetworkManager networkManager) {
        this(networkManager, "localhost", 25565);
    }

    protected Main(NetworkManager networkManager, String host, int port) {
        networkManager.bindTo(new InetSocketAddress(host, port));
    }

    /**
     * Starts the Server.
     * @param args boot arguments
     */
    public static void main(String[] args) {
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new ConsoleFormatter());

        logger.setUseParentHandlers(false);
        logger.addHandler(consoleHandler);

        logger.info("Starting server...");

        new Main(new NetworkManager(logger));
    }

}
