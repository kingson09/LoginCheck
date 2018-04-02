package com.netease.caipiao.common.util;

/**
 * Created by bjliuzhanyong on 2017/12/19.
 * 一个最大支持8个状态同步的同步工具类，并不支持多线程
 */

public class CountDownLatchBitVector {
  private byte bits;
  private int count;

  /*
  假如count==3，将bits初始化为00000111
   */
  public CountDownLatchBitVector(int count) {
    if (count > 8) {
      count = 8;
    }
    this.count = count;
    bits |= ~(-1 << count);
  }

  /*
  假如bit=00000111，index==2，则结果为bits=00000101,将第二位置0，之后返回是否所有位都为0的结果
   */
  public boolean countDown(int index) {
    if (index > count || index <= 0) {
      return false;
    }
    bits &= ~(1 << (index - 1));
    return bits == 0;
  }

  /*
    重新初始化
     */
  public void reset() {
    bits = 0;
    bits |= ~(-1 << count);
  }

}
