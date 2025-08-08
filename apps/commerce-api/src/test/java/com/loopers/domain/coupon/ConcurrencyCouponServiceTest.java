package com.loopers.domain.coupon;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.util.StopWatch;

import com.loopers.infrastructure.coupon.OptimisticCouponJpaRepository;
import com.loopers.infrastructure.coupon.PessimisticCouponJpaRepository;
import com.loopers.utils.DatabaseCleanUp;

@SpringBootTest
class ConcurrencyCouponServiceTest {

    @Autowired
    private ConcurrencyCouponService concurrencyCouponService;

    @Autowired
    private OptimisticCouponJpaRepository optimisticCouponJpaRepository;

    @Autowired
    private PessimisticCouponJpaRepository pessimisticCouponJpaRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("낙관락 쿠폰")
    @Nested
    class OptCoupon {
        @Test
        void 낙관락_쿠폰을_100명이_동시에_사용한다() throws InterruptedException {
            // arrange
            int numberOfThreads = 100;
            ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
            CountDownLatch latch = new CountDownLatch(numberOfThreads);

            OptimisticCoupon coupon = optimisticCouponJpaRepository.save(new OptimisticCoupon("낙관락 쿠폰", 1000L, 50L));

            AtomicInteger successCount = new AtomicInteger();
            AtomicInteger failCount = new AtomicInteger();
            List<Future<Boolean>> futures = new ArrayList<>();

            // act
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();

            for (int i = 0; i < numberOfThreads; i++) {
                futures.add(executorService.submit(() -> {
                    try {
                        concurrencyCouponService.useOptCoupon(coupon.getId());
                        successCount.incrementAndGet();
                        return true;
                    } catch (IllegalStateException e) {
                        failCount.incrementAndGet();
                        return false;
                    } finally {
                        latch.countDown();
                    }
                }));
            }

            latch.await();
            stopWatch.stop();
            executorService.shutdown();

            // assert
            System.out.printf("테스트 실행 시간: %s ms\n", stopWatch.getTotalTimeMillis());

            var updatedCoupon = optimisticCouponJpaRepository.findById(coupon.getId()).orElseThrow();
            assertAll(
                () -> assertEquals(50, successCount.get(), "성공한 구매 수는 50이어야 합니다"),
                () -> assertEquals(50, updatedCoupon.getIssuedCount(), "발급된 쿠폰 개수는 50개이어야 합니다."),
                () -> assertTrue(stopWatch.getTotalTimeMillis() > 0, "처리 시간이 측정되어야 합니다")
            );
        }

        @Test
        void 낙관락_쿠폰을_200명이_동시에_사용한다() throws InterruptedException {
            // arrange
            int numberOfThreads = 200;
            ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
            CountDownLatch latch = new CountDownLatch(numberOfThreads);

            OptimisticCoupon coupon = optimisticCouponJpaRepository.save(new OptimisticCoupon("낙관락 쿠폰", 1000L, 100L));

            AtomicInteger successCount = new AtomicInteger();
            AtomicInteger failCount = new AtomicInteger();
            List<Future<Boolean>> futures = new ArrayList<>();

            // act
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();

            for (int i = 0; i < numberOfThreads; i++) {
                futures.add(executorService.submit(() -> {
                    try {
                        concurrencyCouponService.useOptCoupon(coupon.getId());
                        successCount.incrementAndGet();
                        return true;
                    } catch (IllegalStateException e) {
                        failCount.incrementAndGet();
                        return false;
                    } finally {
                        latch.countDown();
                    }
                }));
            }

            latch.await();
            stopWatch.stop();
            executorService.shutdown();

            // assert
            System.out.printf("테스트 실행 시간: %s ms\n", stopWatch.getTotalTimeMillis());

            var updatedCoupon = optimisticCouponJpaRepository.findById(coupon.getId()).orElseThrow();
            assertAll(
                () -> assertEquals(100, successCount.get(), "성공한 구매 수는 100이어야 합니다"),
                () -> assertEquals(100, updatedCoupon.getIssuedCount(), "발급된 쿠폰 개수는 100개이어야 합니다."),
                () -> assertTrue(stopWatch.getTotalTimeMillis() > 0, "처리 시간이 측정되어야 합니다")
            );
        }

        @Test
        void 낙관락_쿠폰을_300명이_동시에_사용한다() throws InterruptedException {
            // arrange
            int numberOfThreads = 300;
            ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
            CountDownLatch latch = new CountDownLatch(numberOfThreads);

            OptimisticCoupon coupon = optimisticCouponJpaRepository.save(new OptimisticCoupon("낙관락 쿠폰", 1000L, 150L));

            AtomicInteger successCount = new AtomicInteger();
            AtomicInteger failCount = new AtomicInteger();
            List<Future<Boolean>> futures = new ArrayList<>();

            // act
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();

            for (int i = 0; i < numberOfThreads; i++) {
                futures.add(executorService.submit(() -> {
                    try {
                        concurrencyCouponService.useOptCoupon(coupon.getId());
                        successCount.incrementAndGet();
                        return true;
                    } catch (IllegalStateException e) {
                        failCount.incrementAndGet();
                        return false;
                    } finally {
                        latch.countDown();
                    }
                }));
            }

            latch.await();
            stopWatch.stop();
            executorService.shutdown();

            // assert
            System.out.printf("테스트 실행 시간: %s ms\n", stopWatch.getTotalTimeMillis());

            var updatedCoupon = optimisticCouponJpaRepository.findById(coupon.getId()).orElseThrow();
            assertAll(
                () -> assertEquals(150, successCount.get(), "성공한 구매 수는 150이어야 합니다"),
                () -> assertEquals(150, updatedCoupon.getIssuedCount(), "발급된 쿠폰 개수는 150개이어야 합니다."),
                () -> assertTrue(stopWatch.getTotalTimeMillis() > 0, "처리 시간이 측정되어야 합니다")
            );
        }

        @Test
        void 낙관락_쿠폰을_500명이_동시에_사용한다() throws InterruptedException {
            // arrange
            int numberOfThreads = 500;
            ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
            CountDownLatch latch = new CountDownLatch(numberOfThreads);

            OptimisticCoupon coupon = optimisticCouponJpaRepository.save(new OptimisticCoupon("낙관락 쿠폰", 1000L, 250L));

            AtomicInteger successCount = new AtomicInteger();
            AtomicInteger failCount = new AtomicInteger();
            List<Future<Boolean>> futures = new ArrayList<>();

            // act
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();

            for (int i = 0; i < numberOfThreads; i++) {
                futures.add(executorService.submit(() -> {
                    try {
                        concurrencyCouponService.useOptCoupon(coupon.getId());
                        successCount.incrementAndGet();
                        return true;
                    } catch (IllegalStateException e) {
                        failCount.incrementAndGet();
                        return false;
                    } finally {
                        latch.countDown();
                    }
                }));
            }

            latch.await();
            stopWatch.stop();
            executorService.shutdown();

            // assert
            System.out.printf("테스트 실행 시간: %s ms\n", stopWatch.getTotalTimeMillis());

            var updatedCoupon = optimisticCouponJpaRepository.findById(coupon.getId()).orElseThrow();
            assertAll(
                () -> assertEquals(250, successCount.get(), "성공한 구매 수는 250이어야 합니다"),
                () -> assertEquals(250, updatedCoupon.getIssuedCount(), "발급된 쿠폰 개수는 250개이어야 합니다."),
                () -> assertTrue(stopWatch.getTotalTimeMillis() > 0, "처리 시간이 측정되어야 합니다")
            );
        }

        @Test
        void 낙관락_쿠폰을_1000명이_동시에_사용한다() throws InterruptedException {
            // arrange
            int numberOfThreads = 1000;
            ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
            CountDownLatch latch = new CountDownLatch(numberOfThreads);

            OptimisticCoupon coupon = optimisticCouponJpaRepository.save(new OptimisticCoupon("낙관락 쿠폰", 1000L, 500L));

            AtomicInteger successCount = new AtomicInteger();
            AtomicInteger failCount = new AtomicInteger();
            List<Future<Boolean>> futures = new ArrayList<>();

            // act
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();

            for (int i = 0; i < numberOfThreads; i++) {
                futures.add(executorService.submit(() -> {
                    try {
                        concurrencyCouponService.useOptCoupon(coupon.getId());
                        successCount.incrementAndGet();
                        return true;
                    } catch (OptimisticLockingFailureException e) {
                        failCount.incrementAndGet();
                        return false;
                    } finally {
                        latch.countDown();
                    }
                }));
            }

            latch.await();
            stopWatch.stop();
            executorService.shutdown();

            // assert
            System.out.printf("테스트 실행 시간: %s ms\n", stopWatch.getTotalTimeMillis());

            var updatedCoupon = optimisticCouponJpaRepository.findById(coupon.getId()).orElseThrow();
            assertAll(
                () -> assertEquals(500, successCount.get(), "성공한 구매 수는 500이어야 합니다"),
                // () -> assertEquals(500, failCount.get(), "실패한 구매 수는 500이어야 합니다"),
                () -> assertEquals(500, updatedCoupon.getIssuedCount(), "발급된 쿠폰 개수는 500개이어야 합니다."),
                () -> assertTrue(stopWatch.getTotalTimeMillis() > 0, "처리 시간이 측정되어야 합니다")
            );
        }
    }

    @DisplayName("비관락 쿠폰")
    @Nested
    class PessCoupon {
        @Test
        void 비관락_쿠폰을_100명이_동시에_사용한다() throws InterruptedException {
            // arrange
            int numberOfThreads = 100;
            ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
            CountDownLatch latch = new CountDownLatch(numberOfThreads);

            PessimisticCoupon coupon = pessimisticCouponJpaRepository.save(new PessimisticCoupon("비관락 쿠폰", 1000L, 50L));

            AtomicInteger successCount = new AtomicInteger();
            AtomicInteger failCount = new AtomicInteger();
            List<Future<Boolean>> futures = new ArrayList<>();

            // act
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();

            for (int i = 0; i < numberOfThreads; i++) {
                futures.add(executorService.submit(() -> {
                    try {
                        concurrencyCouponService.usePessCoupon(coupon.getId());
                        successCount.incrementAndGet();
                        return true;
                    } catch (Exception e) {
                        System.out.println("쿠폰 발급 실패: " + e.getMessage());
                        failCount.incrementAndGet();
                        return false;
                    } finally {
                        latch.countDown();
                    }
                }));
            }

            latch.await();
            stopWatch.stop();
            executorService.shutdown();

            // assert
            System.out.printf("테스트 실행 시간: %s ms\n", stopWatch.getTotalTimeMillis());

            var updatedCoupon = pessimisticCouponJpaRepository.findById(coupon.getId()).orElseThrow();
            assertAll(
                () -> assertEquals(50, successCount.get(), "성공한 구매 수는 50이어야 합니다"),
                () -> assertEquals(50, failCount.get(), "실패한 구매 수는 50이어야 합니다"),
                () -> assertEquals(50, updatedCoupon.getIssuedCount(), "발급된 쿠폰 개수는 100개이어야 합니다."),
                () -> assertTrue(stopWatch.getTotalTimeMillis() > 0, "처리 시간이 측정되어야 합니다")
            );
        }

        @Test
        void 비관락_쿠폰을_200명이_동시에_사용한다() throws InterruptedException {
            // arrange
            int numberOfThreads = 200;
            ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
            CountDownLatch latch = new CountDownLatch(numberOfThreads);

            PessimisticCoupon coupon = pessimisticCouponJpaRepository.save(new PessimisticCoupon("비관락 쿠폰", 1000L, 100L));

            AtomicInteger successCount = new AtomicInteger();
            AtomicInteger failCount = new AtomicInteger();
            List<Future<Boolean>> futures = new ArrayList<>();

            // act
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();

            for (int i = 0; i < numberOfThreads; i++) {
                futures.add(executorService.submit(() -> {
                    try {
                        concurrencyCouponService.usePessCoupon(coupon.getId());
                        successCount.incrementAndGet();
                        return true;
                    } catch (Exception e) {
                        System.out.println("쿠폰 발급 실패: " + e.getMessage());
                        failCount.incrementAndGet();
                        return false;
                    } finally {
                        latch.countDown();
                    }
                }));
            }

            latch.await();
            stopWatch.stop();
            executorService.shutdown();

            // assert
            System.out.printf("테스트 실행 시간: %s ms\n", stopWatch.getTotalTimeMillis());

            var updatedCoupon = pessimisticCouponJpaRepository.findById(coupon.getId()).orElseThrow();
            assertAll(
                () -> assertEquals(100, successCount.get(), "성공한 구매 수는 100이어야 합니다"),
                () -> assertEquals(100, failCount.get(), "실패한 구매 수는 100이어야 합니다"),
                () -> assertEquals(100, updatedCoupon.getIssuedCount(), "발급된 쿠폰 개수는 100개이어야 합니다."),
                () -> assertTrue(stopWatch.getTotalTimeMillis() > 0, "처리 시간이 측정되어야 합니다")
            );
        }

        @Test
        void 비관락_쿠폰을_300명이_동시에_사용한다() throws InterruptedException {
            // arrange
            int numberOfThreads = 300;
            ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
            CountDownLatch latch = new CountDownLatch(numberOfThreads);

            PessimisticCoupon coupon = pessimisticCouponJpaRepository.save(new PessimisticCoupon("비관락 쿠폰", 1000L, 150L));

            AtomicInteger successCount = new AtomicInteger();
            AtomicInteger failCount = new AtomicInteger();
            List<Future<Boolean>> futures = new ArrayList<>();

            // act
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();

            for (int i = 0; i < numberOfThreads; i++) {
                futures.add(executorService.submit(() -> {
                    try {
                        concurrencyCouponService.usePessCoupon(coupon.getId());
                        successCount.incrementAndGet();
                        return true;
                    } catch (Exception e) {
                        System.out.println("쿠폰 발급 실패: " + e.getMessage());
                        failCount.incrementAndGet();
                        return false;
                    } finally {
                        latch.countDown();
                    }
                }));
            }

            latch.await();
            stopWatch.stop();
            executorService.shutdown();

            // assert
            System.out.printf("테스트 실행 시간: %s ms\n", stopWatch.getTotalTimeMillis());

            var updatedCoupon = pessimisticCouponJpaRepository.findById(coupon.getId()).orElseThrow();
            assertAll(
                () -> assertEquals(150, successCount.get(), "성공한 구매 수는 100이어야 합니다"),
                () -> assertEquals(150, failCount.get(), "실패한 구매 수는 100이어야 합니다"),
                () -> assertEquals(150, updatedCoupon.getIssuedCount(), "발급된 쿠폰 개수는 100개이어야 합니다."),
                () -> assertTrue(stopWatch.getTotalTimeMillis() > 0, "처리 시간이 측정되어야 합니다")
            );
        }

        @Test
        void 비관락_쿠폰을_500명이_동시에_사용한다() throws InterruptedException {
            // arrange
            int numberOfThreads = 500;
            ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
            CountDownLatch latch = new CountDownLatch(numberOfThreads);

            PessimisticCoupon coupon = pessimisticCouponJpaRepository.save(new PessimisticCoupon("비관락 쿠폰", 1000L, 250L));

            AtomicInteger successCount = new AtomicInteger();
            AtomicInteger failCount = new AtomicInteger();
            List<Future<Boolean>> futures = new ArrayList<>();

            // act
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();

            for (int i = 0; i < numberOfThreads; i++) {
                futures.add(executorService.submit(() -> {
                    try {
                        concurrencyCouponService.usePessCoupon(coupon.getId());
                        successCount.incrementAndGet();
                        return true;
                    } catch (Exception e) {
                        System.out.println("쿠폰 발급 실패: " + e.getMessage());
                        failCount.incrementAndGet();
                        return false;
                    } finally {
                        latch.countDown();
                    }
                }));
            }

            latch.await();
            stopWatch.stop();
            executorService.shutdown();

            // assert
            System.out.printf("테스트 실행 시간: %s ms\n", stopWatch.getTotalTimeMillis());

            var updatedCoupon = pessimisticCouponJpaRepository.findById(coupon.getId()).orElseThrow();
            assertAll(
                () -> assertEquals(250, successCount.get(), "성공한 구매 수는 100이어야 합니다"),
                () -> assertEquals(250, failCount.get(), "실패한 구매 수는 100이어야 합니다"),
                () -> assertEquals(250, updatedCoupon.getIssuedCount(), "발급된 쿠폰 개수는 100개이어야 합니다."),
                () -> assertTrue(stopWatch.getTotalTimeMillis() > 0, "처리 시간이 측정되어야 합니다")
            );
        }

        @Test
        void 비관락_쿠폰을_1000명이_동시에_사용한다() throws InterruptedException {
            // arrange
            int numberOfThreads = 1000;
            ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
            CountDownLatch latch = new CountDownLatch(numberOfThreads);

            PessimisticCoupon coupon = pessimisticCouponJpaRepository.save(new PessimisticCoupon("비관락 쿠폰", 1000L, 500L));

            AtomicInteger successCount = new AtomicInteger();
            AtomicInteger failCount = new AtomicInteger();
            List<Future<Boolean>> futures = new ArrayList<>();

            // act
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();

            for (int i = 0; i < numberOfThreads; i++) {
                futures.add(executorService.submit(() -> {
                    try {
                        concurrencyCouponService.usePessCoupon(coupon.getId());
                        successCount.incrementAndGet();
                        return true;
                    } catch (Exception e) {
                        System.out.println("쿠폰 발급 실패: " + e.getMessage());
                        failCount.incrementAndGet();
                        return false;
                    } finally {
                        latch.countDown();
                    }
                }));
            }

            latch.await();
            stopWatch.stop();
            executorService.shutdown();

            // assert
            System.out.printf("테스트 실행 시간: %s ms\n", stopWatch.getTotalTimeMillis());

            var updatedCoupon = pessimisticCouponJpaRepository.findById(coupon.getId()).orElseThrow();
            assertAll(
                () -> assertEquals(500, successCount.get(), "성공한 구매 수는 500이어야 합니다"),
                () -> assertEquals(500, failCount.get(), "실패한 구매 수는 500이어야 합니다"),
                () -> assertEquals(500, updatedCoupon.getIssuedCount(), "발급된 쿠폰 개수는 100개이어야 합니다."),
                () -> assertTrue(stopWatch.getTotalTimeMillis() > 0, "처리 시간이 측정되어야 합니다")
            );
        }
    }
}
