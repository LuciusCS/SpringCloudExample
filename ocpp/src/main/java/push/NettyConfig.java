//package push;
//
//import io.netty.channel.Channel;
//import io.netty.channel.group.ChannelGroup;
//import io.netty.channel.group.DefaultChannelGroup;
//import io.netty.util.concurrent.GlobalEventExecutor;
//
//import java.util.concurrent.ConcurrentHashMap;
//
//public class NettyConfig {
//
//	/***
//	 * 定义一个channel组，管理所有的channel
//	 * GlobalEventExecutor.INSTANCE 是全局的事件执行期，是一个单例
//	 */
//	private static ChannelGroup channelGroup=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
//
//
//	/**
//	 * 存放用户与channel的对应信息，用于给指定用户发送消息
//	 */
//	private static ConcurrentHashMap<String, Channel> userChannelMap=new ConcurrentHashMap<>();
//
//
//	public NettyConfig() {
//	}
//
//	public static ChannelGroup getChannelGroup() {
//		return channelGroup;
//	}
//
//	public static ConcurrentHashMap<String, Channel> getUserChannelMap() {
//		return userChannelMap;
//	}
//}
