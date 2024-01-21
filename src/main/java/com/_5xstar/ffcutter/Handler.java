/**
 * 
 */
package com._5xstar.ffcutter;

/**
 * Handler
 * 对象接收者接口
 * 庞海文 2014-11-23 
 */
public interface Handler<T> {
	/**
	 * 处理对象
	 * @param obj
	 */
    void handle( T obj );
}


