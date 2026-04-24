package com.example.ratelimiter;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RateLimitFilter implements Filter {

    private static final int LIMIT = 5;
    private static final long WINDOW_MS = 10_000;
    private final ConcurrentHashMap<String, IpRecord> ipCounts = new ConcurrentHashMap<>();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String ip = req.getRemoteAddr();
        long now = System.currentTimeMillis();

        IpRecord record = ipCounts.compute(ip, (k, v) -> {
            if (v == null || now - v.windowStart > WINDOW_MS) {
                return new IpRecord(now);
            }
            v.count.incrementAndGet();
            return v;
        });

        if (record.count.get() > LIMIT) {
            res.setStatus(429);
            res.setContentType("text/plain");
            res.getWriter().write("Rate limit exceeded");
            return;
        }

        chain.doFilter(request, response);
    }

    public void reset() {
        ipCounts.clear();
    }

    private static class IpRecord {
        final long windowStart;
        final AtomicInteger count = new AtomicInteger(1);

        IpRecord(long windowStart) {
            this.windowStart = windowStart;
        }
    }
}