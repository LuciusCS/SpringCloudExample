
自动对配置文件信息进行注入

AutoConfiguration.imports vs Starter
特性	                    AutoConfiguration.imports	                        Starter
封装程度                	更轻量，适合简单的配置和功能	                    封装较为复杂的功能，适合提供服务、功能和接口的集成
使用场景	                   适合共享通用配置、简单功能                       	适合封装复杂的功能、服务或集成第三方工具
配置灵活性	            需要开发者在配置类中自行处理配置项	    提供更多的定制选项，支持通过 application.properties 或 yml 文件进行配置
模块化和复用性	            较为简单，适合轻量级共享模块	                    提供更强的模块化和重用性，功能清晰、可扩展
外部服务集成              	无法支持外部服务的复杂集成	                    适合与外部服务（如 Kafka、ElasticSearch 等）集成
依赖管理和版本控制	       不需要管理 Starter，直接依赖共享模块	                需要管理 Starter 依赖的版本和其他可能的传递依赖
维护难度	                  较低，适合小型项目和轻量级模块	                        较高，适合大型系统和复杂服务的封装