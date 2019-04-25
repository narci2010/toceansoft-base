package org.springframework.rediscache.vo;

import lombok.Data;

//一个对象80byte
@Data
public class EmptyObject {
	// 空对象本身 16byte
	// 一个long 8byte
	private long l1;
	private long l2;
	private long l3;
	private long l4;
	private long l5;
	private long l6;
	private long l7;
	private long l8;
}
