/*
 * Copyright (C) 2020 PatrickKR
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 *
 * Contact me on <mailpatrickkr@gmail.com>
 */

package com.github.patrick.websocket

import com.github.patrick.websocket.exception.WebSocketNoResponseException

/**
 * This API object provides simple WebSocket client for Bukkit plugins.
 */
@Suppress("unused")
object WebSocketAPI {
    /**
     * Creates a websocket client with tls option.
     * Returns null when websocket has no response.
     *
     * @param url websocket url to connect
     * @param tls whether to use tls support
     * @param suppress whether to suppress warnings
     * @return nullable WebSocketClient
     */
    @JvmStatic
    @JvmOverloads
    fun createWebSocket(url: String, tls: Boolean = false, suppress: Boolean = false): WebSocketClient? {
        return try {
            WebSocketClient(url, WebSocketHandler(), tls, suppress)
        } catch (exception: WebSocketNoResponseException) {
            null
        }
    }


    /**
     * Creates a websocket client with tls option.
     * This method throws WebSocketNoResponseException
     * when websocket has no response.
     *
     * @param url websocket url to connect
     * @param tls whether to use tls support
     * @param suppress whether to suppress warnings
     * @return WebSocketClient
     */
    @JvmStatic
    @JvmOverloads
    @Throws(WebSocketNoResponseException::class)
    fun createUnsafeWebSocket(url: String, tls: Boolean = false, suppress: Boolean = false): WebSocketClient {
        return WebSocketClient(url, WebSocketHandler(), tls, suppress)
    }
}