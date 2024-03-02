//package websocket;
//
//
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.ChannelInboundHandlerAdapter;
//import io.netty.handler.timeout.IdleState;
//import io.netty.handler.timeout.IdleStateEvent;
//
///***
// * 如果连续2次无读事件，则关闭这个客户端的channel
// */
//public class HeartBeatHandler extends ChannelInboundHandlerAdapter {
//
//
//	private int lossConnectCount = 0;
//
//	@Override
//	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//
//		if (evt instanceof IdleStateEvent) {
//			IdleStateEvent event = (IdleStateEvent) evt;
//
//			if (event.state() == IdleState.READER_IDLE) {
//				lossConnectCount++;
//				if (lossConnectCount > 2) {
//					ctx.channel().close();
//				}
//			}
//
//
//		} else {
//
//			super.userEventTriggered(ctx, evt);
//		}
//	}
//}
