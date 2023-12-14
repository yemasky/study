package zoua.producer;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.CountDownLatch2;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

public class SyncProducer {
	public void sendTest() throws MQClientException, RemotingException, MQBrokerException, InterruptedException,
			UnsupportedEncodingException {
		// 可靠同步发送
		// 实例化消息生产者Producer
		DefaultMQProducer producer = new DefaultMQProducer("please_rename_unique_group_name");
		// 设置NameServer的地址
		producer.setNamesrvAddr("127.0.0.1:9876");
		// 启动Producer实例
		producer.start();
		for (int i = 0; i < 10; i++) {
			// 创建消息，并指定Topic，Tag和消息体
			Message msg = new Message("TopicTest" /* Topic */, "TagA" /* Tag */,
					("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
			);
			// 发送消息到一个Broker
			SendResult sendResult = producer.send(msg);
			// 通过sendResult返回消息是否成功送达
			System.out.printf("%s%n", sendResult);
		}
		// 如果不再发送消息，关闭Producer实例。
		producer.shutdown();
	}

	// 可靠异步发送
	public void sendCallbackTest()
			throws MQClientException, RemotingException, InterruptedException, UnsupportedEncodingException {
		// 实例化消息生产者Producer
		DefaultMQProducer producer = new DefaultMQProducer("please_rename_unique_group_name");
		// 设置NameServer的地址
		producer.setNamesrvAddr("127.0.0.1:9876");
		// 启动Producer实例
		producer.start();
		producer.setRetryTimesWhenSendAsyncFailed(0);

		int messageCount = 10;
		// 根据消息数量实例化倒计时计算器
		final CountDownLatch2 countDownLatch = new CountDownLatch2(messageCount);
		for (int i = 0; i < messageCount; i++) {
			final int index = i;
			// 创建消息，并指定Topic，Tag和消息体
			Message msg = new Message("TopicTest", "TagA", "OrderID188",
					"Hello world".getBytes(RemotingHelper.DEFAULT_CHARSET));
			// SendCallback接收异步返回结果的回调
			producer.send(msg, new SendCallback() {
				@Override
				public void onSuccess(SendResult sendResult) {
					countDownLatch.countDown();
					System.out.printf("%-10d OK %s %n", index, sendResult.getMsgId()+"<--->"+sendResult.getOffsetMsgId());
				}

				@Override
				public void onException(Throwable e) {
					countDownLatch.countDown();
					System.out.printf("%-10d Exception %s %n", index, e);
					e.printStackTrace();
				}
			});
		}
		// 等待5s
		countDownLatch.await(5, TimeUnit.SECONDS);
		// 如果不再发送消息，关闭Producer实例。
		producer.shutdown();
	}

	// 单向发送消息
	public void OnewayProducer() throws MQClientException, UnsupportedEncodingException, RemotingException, InterruptedException {
		// 实例化消息生产者Producer
		DefaultMQProducer producer = new DefaultMQProducer("please_rename_unique_group_name");
		// 设置NameServer的地址
		producer.setNamesrvAddr("127.0.0.1:9876");
		// 启动Producer实例
		producer.start();
		for (int i = 0; i < 10; i++) {
			// 创建消息，并指定Topic，Tag和消息体
			Message msg = new Message("TopicTest" /* Topic */, "TagA" /* Tag */,
					("Hello RocketMQ One " + i).getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
			);
			// 发送单向消息，没有任何返回结果
			producer.sendOneway(msg);

		}
		// 如果不再发送消息，关闭Producer实例。
		producer.shutdown();
	}
	

}
