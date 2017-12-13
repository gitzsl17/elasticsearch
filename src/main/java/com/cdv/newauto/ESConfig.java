package com.cdv.newauto;

import java.net.InetAddress;


import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ESConfig {

	@Bean
	public TransportClient client() throws Exception{
		InetSocketTransportAddress node = new InetSocketTransportAddress(InetAddress.getByName("192.168.8.169"), 9300);
		
		Settings settings = Settings.builder()
				.put("cluster.name","elasticsearch")
				.put("client.transport.sniff",true)
				.build();
		TransportClient client = new PreBuiltTransportClient(settings);
		
		client.addTransportAddress(node);
		return  client;
	}
}
