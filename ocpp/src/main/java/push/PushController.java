package push;


import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@ApiModel(value = "测试信息")
@RestController
@RequestMapping("/smp/push")
@Slf4j
public class PushController {

	@Autowired
	private PushService pushService;

	/**
	 * 推送给所有用户
	 */
    @PostMapping("/pushAll")
	public void pushToAll(@RequestParam("msg")String msg){
		pushService.pushMsgToAll(msg);
	}


	/**
	 * 推送给指定用户
	 */
	@GetMapping("/pushOne")
	public void pushMsgToOne(@RequestParam("userId")String userId,@RequestParam("msg")String msg){
		pushService.pushMsgToOne(userId,msg);
	}

}
