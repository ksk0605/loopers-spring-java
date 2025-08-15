import http from 'k6/http';
import {check, sleep} from 'k6';
import {Trend} from 'k6/metrics';

const responseTime = new Trend('response_time');

export const options = {
    vus: 1,
    duration: '20m',
};

export default function () {
    const baseUrl = 'http://localhost:8080';
    const testUrl = `${baseUrl}/api/v1/products?sort=LATEST&page=0&size=20`;

    const response = http.get(testUrl, {
        headers: {'Content-Type': 'application/json'}
    });

    check(response, {
        '요청 성공': (r) => r.status === 200,
    });

    if (response.status === 200) {
        responseTime.add(response.timings.duration);

        const timestamp = new Date().toISOString();
        console.log(`[${timestamp}] 응답시간: ${response.timings.duration}ms`);

        if (response.timings.duration < 100) {
            console.log(`[${timestamp}] 캐시 히트`);
        } else if (response.timings.duration < 500) {
            console.log(`[${timestamp}] 캐시 미스 (빠른 DB 조회)`);
        } else {
            console.log(`[${timestamp}] 캐시 미스 (느린 DB 조회)`);
        }
    }

    sleep(300);
}
