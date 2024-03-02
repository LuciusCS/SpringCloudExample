//package websocket;
//
//
//import cn.hutool.json.JSONObject;
//import cn.hutool.json.JSONUtil;
//import com.eastsoft.smp.config.NettyConfig;
//import io.netty.channel.ChannelHandler;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.SimpleChannelInboundHandler;
//import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
//import io.netty.util.AttributeKey;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
///**
// * TextWebSocketFrame 类型，表示一个文本帧(frame)
// */
//
//
//@Component
//@ChannelHandler.Sharable
//public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
//
//	private static final Logger log= LoggerFactory.getLogger(NettyServer.class);
//
//
//	/**
//	 * 一旦连接，第一个被执行
//	 * @param ctx
//	 * @throws Exception
//	 */
//
//	@Override
//	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
//		log.info("handlerAdded 被调用"+ ctx.channel().id().asLongText());
//		///添加到channelGroup 通道组
//		NettyConfig.getChannelGroup().add(ctx.channel());
////		super.handlerAdded(ctx);
//	}
//
////	///客户端与服务器建立连接时的触发
////	@Override
////	public void channelActive(ChannelHandlerContext ctx) throws Exception {
//////		super.channelActive(ctx);
////
////		log.info("channel Active" + "与客户端建立连接，通道开启");
////		///添加到channelGroup通道组
//////		NettyConfig.getChannelGroup().add(ctx.channel());
////
////
////	}
////
////
////	///客户端与服务器端关闭连接的时候触发
////	@Override
////	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//////		super.channelInactive(ctx);
////		log.info("channel Inactive"+"与客户端断开连接，通道关闭");
////		//删除通道
//////		NettyConfig.getChannelGroup().remove(ctx.channel());
//////		removeUserId(ctx);
////	}
//
//	/**
//	 * 处理建立连接时候请求（用于拿参数）
//	 * @param ctx
//	 * @param msg
//	 * @throws Exception
//	 */
////	@Override
////	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
////		if (null!=msg&& msg instanceof FullHttpRequest){
////			log.info("连接请求，准备提取参数");
////
////			///转化为http请求
////			FullHttpRequest request=(FullHttpRequest) msg;
////			///拿到请求地址
////			String uri = request.uri();
////			log.info("uri:"+uri);
////			if (StringUtils.isNotBlank(uri)){
////				String path=StringUtils.substringBefore(uri,"?");
////				log.info("path:{}",path);
////				String userName=StringUtils.substringAfterLast(path,"/");
////				log.info(userName);
////
////				//		NettyConfig.getChannelGroup().remove(ctx.channel());
//////               log.info("channelMap:{}");
////			}
////
////			///重新设置请求地址为WebSocketServerProtocolHandler 匹配的地址
////			// request.setUri("/ws");
////
////
////		}
////
////
////        ///接着建立请求
////		super.channelRead(ctx, msg);
////	}
//
//
//
//	@Override
//	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
//
//		log.info("服务器收到消息：{}",msg.text());
//
//		///获取用户ID
//		JSONObject jsonObject= JSONUtil.parseObj(msg.text());
//
//		String uid=jsonObject.getStr("uid");
//
//		///将用户ID作为自定义属性加入到channel中，方便随时channel中获取用户用户ID
//		AttributeKey<String>key=AttributeKey.valueOf("userId");
//		ctx.channel().attr(key).setIfAbsent(uid);
//
//		///关联channel
//		NettyConfig.getUserChannelMap().put(uid,ctx.channel());
//
//		///回复消息
//		ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器连接成功！")) ;
//
//	}
//
//	@Override
//	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
////		super.handlerRemoved(ctx);
//		log.info("handlerRemoved 被调用"+ctx.channel().id().asLongText());
//		//删除通道
//		NettyConfig.getChannelGroup().remove(ctx.channel());
//	    removeUserId(ctx);
////		ctx.close();
//	}
//
//	@Override
//	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
////		super.exceptionCaught(ctx, cause);
//	   log.info("异常:{}",cause.getMessage());
//	   //删除通道
//		NettyConfig.getChannelGroup().remove(ctx.channel());
//		removeUserId(ctx);
//		ctx.close();
//
//	}
//
//	/***
//	 * 删除用户与channel的对应关系
//	 */
//	private void removeUserId(ChannelHandlerContext ctx){
//		AttributeKey<String>key= AttributeKey.valueOf("userId");
//		String userId=ctx.channel().attr(key).get();
//		NettyConfig.getUserChannelMap().remove(userId);
//
//	}
//
//
//
//
//
//
//}
