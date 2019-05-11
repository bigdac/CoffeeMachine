package vendingmachine.xr.com.coffeemachine.adapter;

import java.util.logging.Logger;

public class BaseThread extends Thread {
    private boolean isSleep = true;
    private boolean isStop = false;

    public void run() {
        while(!isStop) {
            if(isSleep) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Thread: "+Thread.currentThread().getName() + " 运行中.");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Thread: "+Thread.currentThread().getName() + " 结束.");
    }

    public void setSleep(boolean sleep) {
        this.isSleep = sleep;
    }
    public void setStop(boolean stop) {
        this.isStop = stop;
    }


}
