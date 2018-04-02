# 我的学习方法：DFS
![image](https://github.com/kingson09/Tools/blob/master/resources/dfs.gif)
# LoginCheck
使用AspectJ实现的几个登录状态检查注解，在需要判断登录状态的方法上加上此注解，会自动进行登录状态判断，如果未登录则跳转登录页，登录完成后会自动继续原代码逻辑或者交给原代码处理
# HookAms
之前项目出现binder大小超出系统限制问题，写的一个hook ams统计bundle大小的小工具

# EventManger
项目在做架构设计，将某些复杂页面拆分为fragment（fragment并非android的fragment，而是我们重新实现的类似于Fragment的View模块）之后，fragment之间存在一些一对多的通信需求，使用本地广播对开发不太友好，因此设计了EventManager，将一组事件定义为接口，某些fragment可以注册自己为这个接口的发布者，某些fragment可以注册为这个接口事件的订阅者，这样消息发布者就可以像调用同步方法一样发布事件，并且可以省去各层级的fragment繁琐的相互引用关系，目前的实现方式为通过动态代理自动为事件接口实现发布者类型，以后还可能通过apt优化为编译期生成发布者，这个工具主要目的是省去了开发者自己编写事件发布者以及事件注册的繁琐过程，只需要定义事件接口这个协议，就可以实现本地广播

例如可以定义一个大厅上消息按钮的点击事件为OnNoticeBtnClick,消息按钮在大厅中的HeaderFragment的view tree中，这样HeaderFragment可以通过EventManager注册自己为OnNoticeBtnClick的发布者，而需要对此消息响应的各级Fragment可以实现OnNoticeBtnClick此接口，并通过EventManager注册为OnNoticeBtnClick的订阅者，这样在消息按钮点击的时候，各层级的Fragment订阅者就能同步收到点击事件了，当然为了高效EventManager只支持主线程操作
```
puiblic interface OnNoticeBtnClick{
	void onClick(View view);
	default void onLongClick(View view){}
}
```
```
//注册自己为发布者，获得一个发布者引用
OnNoticeBtnClick noticeBtnPublisher = eventManager.registerPublisher(OnNoticeBtnClick.class);

//在需要发布事件的地方，调用noticeBtnPublisher发布事件
public void onClick(View view) throws NullPointerException {
        noticeBtnPublisher.onClick(view);
      }
	  
//在实现了OnNoticeBtnClick接口的订阅者内，就收到了此事件通知 
eventManager.registerSubscriber(OnNoticeBtnClick.class, this);

public void onClick(View view) {
    System.out.println("我收到了点击事件！");
  }

```
最后，最好一个Activity或者Fragment使用一个EventManager实例，切记在onDestory中进行反注册

#CountDownLatchBitVector
结合CountDownLatch和BitVector特点，编写的CountDownLatchBitVector，由一个byte的8位最多支持同步8个状态，主要用于一个页面多个请求的同步