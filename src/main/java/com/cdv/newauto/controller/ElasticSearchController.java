package com.cdv.newauto.controller;

import java.util.Date;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ElasticSearchController {
	
	@Autowired
	private TransportClient client;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/get/book/novel")
	@ResponseBody
	public ResponseEntity get(@RequestParam(name = "id",defaultValue = "") String id){
		if (id.isEmpty()) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		
		GetResponse result = this.client.prepareGet("book", "novel", id).get();
		
		if (!result.isExists()) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity(result.getSource(),HttpStatus.OK);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/add/book/novel")
	@ResponseBody
	public ResponseEntity add(
			@RequestParam(name = "title") String title,
			@RequestParam(name = "author") String author,
			@RequestParam(name = "word_count") int wordCount,
			@RequestParam(name = "publish_date") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date publishDate
		) {
		try {
			XContentBuilder content = XContentFactory.jsonBuilder()
					.startObject()
					.field("title",title)
					.field("author",author)
					.field("wordCount", wordCount)
					.field("publishDate",publishDate)
					.endObject();
			IndexResponse result = this.client.prepareIndex("book", "nobel").setSource(content).get();
			return new ResponseEntity(result.getId(),HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
