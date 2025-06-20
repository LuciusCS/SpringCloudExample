


## 1. 在使用用户名和密码登录时，还需要传入 scope 原因

scope 决定了客户端请求访问用户资源的范围和权限。 也就是说：用户登录时，同意授权哪些资源 / 能做什么事情，是由 scope 控制的。



scope  roles permissions 三者概念的区别

项目	                        scope	                        roles	                  permissions
属于谁	              属于客户端 client（即应用）      	  属于用户 user                属于用户 user
存放位置	        access_token.scope（JWT claims）  	user.roles → authorities   	user.permissions → authorities
用途	                    控制客户端“能访问哪些资源”	        控制用户“能访问哪些功能”	      更细粒度控制操作权限
谁定义的	                 OAuth2 客户端注册时定义     	通常由你在数据库中设定	          通常由你在数据库中设定
示例	                  "read", "openid", "profile"	"ROLE_ADMIN", "ROLE_USER"	 "user:update", "data:export"
权限判断	            hasAuthority('SCOPE_read')          hasRole('ADMIN')    	hasAuthority('user:update')

