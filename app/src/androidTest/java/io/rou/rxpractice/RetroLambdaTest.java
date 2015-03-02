package io.rou.rxpractice;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

import rx.Observable;
import rx.functions.Action1;

@RunWith(AndroidJUnit4.class)
public class RetroLambdaTest {
    @Test
    public void Action1でobserveする練習() throws Throwable{
        CountDownLatch latch = new CountDownLatch(1);
        final String[] names = {"Ken", "George"};
        Observable.from(names).subscribe(new Action1<String>() {
            @Override
            public void call(String name) {
                Assert.assertTrue(Arrays.asList(names).contains(name));
                latch.countDown();
            }
        });
        latch.await();
    }
}
