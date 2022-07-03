//package com.wanmi.sbc.mini.mq;
//
//import com.rabbitmq.client.Channel;
//import com.wanmi.sbc.mini.mq.config.RabbitMqConfig;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.concurrent.TimeUnit;
//
//@Component
//@Slf4j
//public class DelayDemoTopicListener {
//
//
//    /**
//     * 监听延迟消息通道中的消息
//     * @param message
//     */
//    @RabbitListener(queues = RabbitMqConfig.queueName, ackMode = "MANUAL")
//    public void listener(String info , Message message, Channel channel) throws IOException, InterruptedException {
//
//        try {
//            log.info("--->>> info: {}", info);
//            log.info("--->>> message: {}", message);
////            log.info("--->>> channel: {}", channel);
//            log.info("****************" + message.getMessageProperties().getHeaders().get("X_RETRIES_HEADER"));
//            int a = 10 / 0;
//            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//        }catch (Exception e) {
//            TimeUnit.SECONDS.sleep(5);
//            log.error(e.getMessage());
//            int sum = (int) message.getMessageProperties().getHeaders().get("X_RETRIES_HEADER");
//            sum = sum - 10;
//            message.getMessageProperties().getHeaders().remove("X_RETRIES_HEADER");
//            System.out.println("**************" + sum + "++++++++++");
//            message.getMessageProperties().getHeaders().put("X_RETRIES_HEADER", sum);
//            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
//        }
//
//
////        //获取重试次数
////        int retries = (int)message.getHeaders().get(DelayConstant.X_RETRIES_HEADER);
////        //获取消息内容
////        BigDecimal money = message.getPayload();
////        try {
////            String now = DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss");
////            //模拟：如果金额大于200，则消息无法消费成功；金额如果大于100，则重试3次；如果金额小于100，直接消费成功
////            if (money.compareTo(new BigDecimal(200)) == 1){
////                throw new RuntimeException(now+":金额超出200，无法交易。");
////            }else if (money.compareTo(new BigDecimal(100)) == 1 /*&& retries <= 3*/) {
////                if (retries == 0) {
////                    throw new RuntimeException(now+":金额超出100，消费失败，将进入重试。");
////                }else {
////                    throw new RuntimeException(now+":金额超出100，当前第" + retries + "次重试。");
////                }
////            }else {
////                log.info("消息消费成功！");
////            }
////        }catch (Exception e) {
////            log.error(e.getMessage());
////            if (retries < DelayConstant.X_RETRIES_TOTAL){
////                //将消息重新塞入队列
////                MessageBuilder<BigDecimal> messageBuilder = MessageBuilder.fromMessage(message)
////                        //设置消息的延迟时间
////                        .setHeader(DelayConstant.X_DELAY_HEADER,DelayConstant.ruleMap.get(retries + 1))
////                        //设置消息已经重试的次数
////                        .setHeader(DelayConstant.X_RETRIES_HEADER,retries + 1);
////                Message<BigDecimal> reMessage = messageBuilder.build();
////                //将消息重新发送到延迟队列中
////                delayDemoTopic.delayDemoProducer().send(reMessage);
////            }else {
////                //超过重试次数，做相关处理（比如保存数据库等操作），如果抛出异常，则会自动进入死信队列
////                throw new RuntimeException("超过最大重试次数：" + DelayConstant.X_RETRIES_TOTAL);
////            }
////        }
//    }
//
//}