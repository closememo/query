package com.closememo.query.infra.sync;

import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * key 로 구별 할 수 있는, lock 을 관리한다.
 */
@Slf4j
@Component
public class LockManager {

  private final ConcurrentHashMap<Integer, CountableLock> lockHolderMap;

  public LockManager() {
    this.lockHolderMap = new ConcurrentHashMap<>();
  }

  public void lock(Integer key) {
    CountableLock lock = lockHolderMap.get(key);
    if (lock == null) {
      log.info("[LOG] create new lock. key=" + key);
      lock = new CountableLock();
      lockHolderMap.put(key, lock);
    }
    lock.increaseCount();
    log.info("[LOG] increase lock count. key=" + key);
    lock.lock();
  }

  public void unlock(Integer key) {
    CountableLock lock = lockHolderMap.get(key);
    if (lock == null) {
      log.error("[LOG] lock doesn't exist. key=" + key);
      throw new RuntimeException();
    }
    if (lock.getCount() == 1) {
      log.info("[LOG] remove lock. key=" + key);
      lockHolderMap.remove(key);
    } else {
      lock.decreaseCount();
      log.info("[LOG] decrease lock count. key=" + key);
    }
    lock.unlock();
  }
}
