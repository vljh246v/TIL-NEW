package com.blog.retry.controller

import java.util.concurrent.atomic.AtomicInteger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/chaos")
class ChaosController {

    private val logger = LoggerFactory.getLogger(this::class.java)
    
    // 요청 횟수 카운팅을 위한 변수
    private val requestCounter = AtomicInteger(0)

    /**
     * 시나리오 1: "3번 중 2번은 실패하고 3번째에 성공한다"
     * -> Retry가 제대로 동작하는지 확인하는 가장 고전적인 예제
     */
    @GetMapping("/flaky")
    fun flakyEndpoint(): ResponseEntity<String> {
        val count = requestCounter.incrementAndGet()
        logger.info("Attempt count: $count")

        if (count % 3 != 0) {
            logger.warn("Simulating 500 Error...")
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("앗! 서버가 불안정해요. (시도 횟수: $count)")
        }

        logger.info("Success!")
        return ResponseEntity.ok("성공했습니다! (시도 횟수: $count)")
    }

    /**
     * 시나리오 2: "지정된 시간만큼 늦게 응답한다"
     * -> ReadTimeout 설정 테스트용
     * ex) /chaos/delay?sec=5  (5초 뒤 응답)
     */
    @GetMapping("/delay")
    fun delayedEndpoint(@RequestParam(defaultValue = "3") sec: Long): String {
        logger.info("Sleeping for $sec seconds...")
        Thread.sleep(sec * 1000)
        return "늦어서 죄송합니다! ${sec}초 걸렸네요."
    }

    /**
     * 시나리오 3: "무조건 404를 뱉는다"
     * -> RetryTemplate의 CustomPolicy(404일 때만 재시도 등) 테스트용
     */
    @GetMapping("/404")
    fun notFoundEndpoint(): ResponseEntity<String> {
        logger.info("Returning 404 Not Found")
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("찾을 수 없는 리소스입니다.")
    }
    
    /**
     * 테스트 끝날 때 카운터 초기화용
     */
    @PostMapping("/reset")
    fun reset() {
        requestCounter.set(0)
    }
}