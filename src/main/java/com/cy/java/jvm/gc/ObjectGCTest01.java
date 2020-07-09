package com.cy.java.jvm.gc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ClassA{
	/**
	 *此对象在被销毁(GC)之前会执行此方法 
	 */
	 @Override
	 protected void finalize() throws Throwable{
		 System.out.println("finalize()");
	 }
	 
}
//JVM参数分析
//-XX:+PrintGCDetails
public class ObjectGCTest01 {
	static Map<String,Object> beanPool=new HashMap<>();
	static ClassA o2;
	public static void main(String[] args) {
		ClassA o1=new ClassA();
		beanPool.put("classA",o1 );//放入池中不销毁
		//spring框架中单例作用域的对象会存储到池中
		//手动启动垃圾回收
		//思考:对于JVM中的GC回收来说,什么样的对象是垃圾对象?不可达对象
		//o2=o1;如果有外部引用,则不会被回收
		o1=null;//只要有引用指向这个对象,就不是垃圾对象,就不会被销毁
		//jvm优化后如果即使有引用但后边再也没用到,也会被回收
		System.gc();//启动垃圾回收系统,进行垃圾回收.
		//通过如下代码演示系统底层自动启动GC的过程
		List<byte[]> list=new ArrayList<>();
		for(int i=0;i<1000000001;i++) {
			list.add(new byte[1024]);
		}
		//GC系统一旦启动,他会做如下几件事,
		//1)扫描内存中的不可达对象
		//2)对不可达对象进行标记(这些对象是垃圾)
		//3)对垃圾对象进行回收(清楚对象释放内存)
		//4)内存整理(碎片比较多)
		//(jvm智能地判断对象是否还有用,即便有引用,但是没用的话,还是会被回收)
		/**
		 * 只要不是不可达对象,就算内存满了也不会被GC清除
		 * 在栈上的内存在方法执行结束后就会被回收
		 * */
		
	}
}

