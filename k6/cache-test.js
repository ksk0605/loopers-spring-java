import http from 'k6/http';
import {check, sleep} from 'k6';
import {Rate, Trend} from 'k6/metrics';

const errorRate = new Rate('errors');
const cacheHitRate = new Rate('cache_hits');
const firstRequestTime = new Trend('first_request_time');
const cachedRequestTime = new Trend('cached_request_time');

export const options = {
    stages: [
        {duration: '1m', target: 10},
        {duration: '2m', target: 20},
        {duration: '1m', target: 0},
    ],
    thresholds: {
        http_req_duration: ['p(95)<500'],
        errors: ['rate<0.1'],
        'cached_request_time': ['p(95)<100'],
    },
};

const testScenarios = [
    {sort: 'LATEST', page: 0, size: 20},
    {sort: 'LIKES_DESC', page: 0, size: 20},
    {sort: 'PRICE_ASC', page: 0, size: 20},
    {sort: 'LATEST', page: 1, size: 20},
    {sort: 'LIKES_DESC', page: 1, size: 20},
];

export default function () {
    const baseUrl = 'http://localhost:8080';

    testScenarios.forEach((scenario, index) => {
        const cacheKey = `${scenario.sort}_${scenario.page}_${scenario.size}`;

        const firstResponse = http.get(
            `${baseUrl}/api/v1/products?sort=${scenario.sort}&page=${scenario.page}&size=${scenario.size}`,
            {
                headers: {'Content-Type': 'application/json'}
            }
        );

        check(firstResponse, {
            '첫 번째 요청 성공': (r) => r.status === 200,
            '첫 번째 요청 응답시간 < 500ms': (r) => r.timings.duration < 500,
        });

        if (firstResponse.status === 200) {
            firstRequestTime.add(firstResponse.timings.duration);
            console.log(`[${cacheKey}] 첫 번째 요청: ${firstResponse.timings.duration}ms`);
        }

        sleep(0.1);

        const cachedResponse = http.get(
            `${baseUrl}/api/v1/products?sort=${scenario.sort}&page=${scenario.page}&size=${scenario.size}`,
            {
                headers: {'Content-Type': 'application/json'}
            }
        );

        check(cachedResponse, {
            '캐시 요청 성공': (r) => r.status === 200,
            '캐시 요청 응답시간 < 100ms': (r) => r.timings.duration < 100,
        });

        if (cachedResponse.status === 200) {
            cachedRequestTime.add(cachedResponse.timings.duration);

            const isCacheHit = cachedResponse.timings.duration < 100;
            cacheHitRate.add(isCacheHit);

            console.log(`[${cacheKey}] 캐시 요청: ${cachedResponse.timings.duration}ms (히트: ${isCacheHit})`);

            if (firstResponse.timings.duration > 0) {
                const improvement = ((firstResponse.timings.duration - cachedResponse.timings.duration) / firstResponse.timings.duration * 100).toFixed(1);
                console.log(`[${cacheKey}] 성능 향상: ${improvement}%`);
            }
        }

        sleep(0.5);
    });

    sleep(1);
}
