/*
 * Copyright 2014 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package pro.jiefzz.eden.websocket.base;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.channels.UnsupportedAddressTypeException;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import net.minidev.json.JSONObject;

public class WebSocketClient {
	
	private URI uri;
	private EventLoopGroup group;
	private Channel channel;
	
	private final boolean useSsl;
	private final String host;
	private final String scheme;
	private final int port;

	public static String serverUrl = "wss://api.zb.com:9999/websocket";

	public WebSocketClient(String wsApi) {

		if (wsApi == null || "".equals(wsApi.trim())) {
			throw new NullPointerException("the url can not be empty");
		}
		try {
			uri = new URI(wsApi);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		this.scheme = uri.getScheme() == null ? "http" : uri.getScheme();
		this.host = uri.getHost() == null ? "127.0.0.1" : uri.getHost();
		if (uri.getPort() == -1) {
			if ("http".equalsIgnoreCase(scheme)) {
				this.port = 80;
			} else if ("wss".equalsIgnoreCase(scheme)) {
				this.port = 443;
			} else {
				this.port = -1;
			}
		} else {
			this.port = uri.getPort();
		}

		if (!"ws".equalsIgnoreCase(scheme) && !"wss".equalsIgnoreCase(scheme)) {
			System.err.println("Only WS(S) is supported.");
			throw new UnsupportedAddressTypeException();
		}
		this.useSsl = "wss".equalsIgnoreCase(scheme);
		
	}

	public void connect() throws Exception {
		final SslContext sslCtx;
		if (useSsl) {
			sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
		} else {
			sslCtx = null;
		}
		group = new NioEventLoopGroup();
		try {
			final WebSocketClientHandler handler = new WebSocketClientHandler(WebSocketClientHandshakerFactory
					.newHandshaker(uri, WebSocketVersion.V13, null, false, new DefaultHttpHeaders())) {
				@Override
				public void onReceive(String msg) {
					System.out.println("channel获取消息：" + msg);
				}
			};
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel channel) {
					ChannelPipeline pipeline = channel.pipeline();
					if (sslCtx != null) {
						pipeline.addLast(sslCtx.newHandler(channel.alloc(), host, port));
					}
					pipeline.addLast(new HttpClientCodec(), new HttpObjectAggregator(8192), handler);
				}
			});

			channel = bootstrap.connect(uri.getHost(), port).sync().channel();
			// ChannelFuture f = channel.closeFuture().await();
			handler.handshakeFuture().sync();
		} catch (Exception e) {
			this.cancel();
			throw e;
		}
	}

	/**
	 * 订阅频道（仅限公共频道）
	 * 
	 * @param channel
	 * @throws ChannelException
	 */
	public void addChannel(String channel) throws ChannelException {
		if (!isAlive())
			throw new ChannelException("the channel is not active");
		JSONObject data = new JSONObject();
		data.put("event", "addChannel");
		data.put("channel", channel);

		System.out.println(data);

		this.channel.writeAndFlush(new TextWebSocketFrame(data.toString()));
	}

	/**
	 * 取消订阅（仅限公共频道）
	 * 
	 * @param channel
	 * @param currency
	 * @throws ChannelException
	 */
	public void removeChannel(String channel, String currency) throws ChannelException {
		if (!isAlive())
			throw new ChannelException("the channel is not active");
		this.channel.writeAndFlush(new TextWebSocketFrame("{'event':'removeChannel','channel':'" + channel + "'}"));
	}

	/**
	 * 注销客户端
	 */
	public void cancel() {
		if (group != null)
			group.shutdownGracefully();
	}

	/**
	 * 判断客户端是否保持激活状态
	 * 
	 * @return
	 */
	public boolean isAlive() {
		return this.channel != null && this.channel.isActive() ? true : false;
	}

	/**
	 * 测试帐号: API访问密匙(Access Key)： d31f15d5-xxxx-xxxx-xxxx-5ab5e6326b2e
	 * API私有密匙(Secret Key)： c1639fa5-xxxx-xxxx-xxxx-f42759830a19[仅显示一次]
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		System.out.println("websocket通讯地址：" + serverUrl);
		WebSocketClient client = new WebSocketClient(System.getProperty("url", serverUrl));
		try {
			client.connect();
			System.out.println("================================" + client.isAlive());
			// client.addChannel("ltcbtc_ticker");//通过
			// client.addChannel("ltcbtc_depth");//通过
			client.addChannel("ltcbtc_trades");// 通过

			// client.order( 0.019258, 1, "ethbtc", 0);
			// client.order( 0.009258, 1, "ltcbtc", 1);
			// client.cancelOrder(20151006160133624L , "ethbtc");
			// client.getOrder(20151006160133556L , "ethbtc");
			// client.getOrders(1,1 , "ethbtc");
			// client.getOrdersIgnoreTradeType(1,10 , "ethbtc");
			// client.getUnfinishedOrdersIgnoreTradeType(1,10 , "ethbtc");
			// client.getOrdersNew( 1,10, 1, "ethbtc");
			// client.cancelWithdraw("ethbtc", "20160425916");

			// client.getAccountInfo();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// }
	}

}
