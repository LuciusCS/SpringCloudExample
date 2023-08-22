package push;


import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PushServiceImpl implements PushService {

	private static final Logger log= LoggerFactory.getLogger(PushServiceImpl.class);

	@Override
	public void pushMsgToOne(String userId, String msg) {

		ConcurrentHashMap<String, Channel>userChannelMap= NettyConfig.getUserChannelMap();
		Channel channel=userChannelMap.get(userId);

		if(!Objects.isNull(channel)){
			///如果该用户的客户端是与本服务器建立的channel，直接推送消息
			channel.writeAndFlush(new TextWebSocketFrame(msg));
		}else{
			///通过redis发送给其他服务器消费

		}
		log.info("pushMsgToMany","++++++++");

	}

	@Override
	public void pushMsgToAll(String msg) {

	}
}
