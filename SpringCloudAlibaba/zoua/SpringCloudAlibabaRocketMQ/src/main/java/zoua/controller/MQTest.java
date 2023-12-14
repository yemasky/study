package zoua.controller;

import java.io.UnsupportedEncodingException;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import zoua.consumer.ConsumerMq;
import zoua.producer.SyncProducer;

@Controller
@RequestMapping("/mq")
public class MQTest {
	
	
	@GetMapping("/testConsumer")
	public void testConsumer(){
		ConsumerMq consumer = new ConsumerMq();
		try {
			consumer.consumerTest();
		} catch (MQClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@GetMapping("/testSend")
	public void testSend(){
		SyncProducer producer = new SyncProducer();
		try {
			producer.sendTest();
		} catch (UnsupportedEncodingException | MQClientException | RemotingException | MQBrokerException
				| InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@GetMapping("/testSendCallback")
	public void sendCallbackTest(){
		SyncProducer producer = new SyncProducer();
		try {
			producer.sendCallbackTest();
		} catch (UnsupportedEncodingException | MQClientException | RemotingException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
