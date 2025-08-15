import http from 'k6/http';
import {check, sleep} from 'k6';
import {Rate, Trend} from 'k6/metrics';

const errorRate = new Rate('errors');
const responseTime = new Trend('response_time');

export const options = {
    stages: [
        {duration: '2m', target: 50},
        {duration: '5m', target: 100},
        {duration: '2m', target: 200},
        {duration: '3m', target: 200},
        {duration: '2m', target: 0},
    ],
    thresholds: {
        http_req_duration: ['p(95)<500'],
        errors: ['rate<0.1'],
    },
};

export default function () {
    const baseUrl = 'http://localhost:8080';

    const requests = [
        {url: '/api/v1/products?sort=LATEST&page=0&size=20', weight: 0.4},
        {url: '/api/v1/products?sort=LIKES_DESC&page=0&size=20', weight: 0.3},
        {url: '/api/v1/products?sort=LATEST&page=1&size=20', weight: 0.2},
        {url: '/api/v1/products/1', weight: 0.1},
    ];

    const random = Math.random();
    let selectedRequest = requests[0];
    let cumulativeWeight = 0;

    for (const request of requests) {
        cumulativeWeight += request.weight;
        if (random <= cumulativeWeight) {
            selectedRequest = request;
            break;
        }
    }

    const response = http.get(`${baseUrl}${selectedRequest.url}`, {
        headers: {'Content-Type': 'application/json'}
    });

    check(response, {
        '요청 성공': (r) => r.status === 200,
        '응답시간 < 500ms': (r) => r.timings.duration < 500,
    });

    if (response.status === 200) {
        responseTime.add(response.timings.duration);

        const isCacheHit = response.timings.duration < 100;
        console.log(`요청: ${selectedRequest.url}, 응답시간: ${response.timings.duration}ms, 캐시히트: ${isCacheHit}`);
    }

    sleep(Math.random() * 2 + 1);
}
