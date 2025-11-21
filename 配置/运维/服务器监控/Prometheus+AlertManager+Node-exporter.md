


## 使用docker 启动Prometheus、AlertManager、Node-Exporter

在 monitor目录下使用 docker-compose.yml进行启动

```shell
 ## 启动
 docker compose up -d
 ## 停止
 docker compose down
```

## 启动 `dingding_webhook.py`
通过 `dingding_webhook,py` 将告警消息推送至钉钉

```shell
  ## 安装需要的工具
   sudo python3 get-pip.py
   pip3 install flask requests
  
  # 启动
  python3 dingtalk_webhook.py
```

## 启动告警测试 `test_alerts.sh`

`test_alerts.sh` 会提升CPU占用率、磁盘占用率、内存占有率,

```shell
  # 需要提前安装压力测试工具
  sudo dnf install epel-release -y
  sudo dnf install stress -y  

  chmod +x test_alerts.sh
  # 启动
 ./test_alerts.sh
```

## 网络不通

Docker 镜像调用运行在宿主机上的 webhook 网络不通