package com.backend.rate_limiter.strategy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenBucketTest {

    @Test
    void shouldAllowRequestsWhenTokensAreAvailable() {

        TokenBucket bucket = new TokenBucket(3, 1);

        TokenBucket.RateLimitResult result =
                bucket.allowRequest();

        assertTrue(result.isAllowed());
        assertEquals(3, result.getLimit());
        assertEquals(2, result.getRemaining());
    }

    @Test
    void shouldDenyRequestWhenNoTokensAreAvailable() {

        TokenBucket bucket = new TokenBucket(2, 0);

        bucket.allowRequest();
        bucket.allowRequest();

        TokenBucket.RateLimitResult result =
                bucket.allowRequest();

        assertFalse(result.isAllowed());
        assertEquals(2, result.getLimit());
        assertEquals(0, result.getRemaining());
    }

    @Test
    void shouldRefillTokensAfterElapsedTime() throws InterruptedException {

        TokenBucket bucket = new TokenBucket(3, 1);

        // Consume all 3 tokens
        bucket.allowRequest();
        bucket.allowRequest();
        bucket.allowRequest();

        // Bucket should now be empty
        assertEquals(0, bucket.getAvailableTokens());

        // Wait for 1 second so one token can refill
        Thread.sleep(1100);

        TokenBucket.RateLimitResult result =
                bucket.allowRequest();

        assertTrue(result.isAllowed());
        assertEquals(3, result.getLimit());
        assertEquals(0, result.getRemaining());
    }

    @Test
    void shouldNeverExceedCapacity() throws InterruptedException {

        TokenBucket bucket = new TokenBucket(3, 10);

        // Wait long enough for multiple tokens to theoretically refill
        Thread.sleep(1100);

        bucket.refillTokens();

        assertEquals(3, bucket.getAvailableTokens());
    }

    @Test
    void shouldReturnCorrectRemainingTokens() {

        TokenBucket bucket = new TokenBucket(5, 0);

        TokenBucket.RateLimitResult result1 =
                bucket.allowRequest();

        assertEquals(4, result1.getRemaining());

        TokenBucket.RateLimitResult result2 =
                bucket.allowRequest();

        assertEquals(3, result2.getRemaining());

        TokenBucket.RateLimitResult result3 =
                bucket.allowRequest();

        assertEquals(2, result3.getRemaining());
    }

    @Test
    void shouldReturnZeroRetryAfterWhenTokensAreAvailable() {

        TokenBucket bucket = new TokenBucket(5, 1);

        assertEquals(
                0,
                bucket.getRetryAfterSeconds()
        );
    }

    @Test
    void shouldReturnRetryAfterWhenNoTokensAreAvailable() {

        TokenBucket bucket = new TokenBucket(1, 1);

        // Consume the only token
        bucket.allowRequest();

        long retryAfter =
                bucket.getRetryAfterSeconds();

        assertTrue(retryAfter >= 1);
    }
}