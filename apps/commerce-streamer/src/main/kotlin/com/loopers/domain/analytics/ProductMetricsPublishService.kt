package com.loopers.domain.analytics

import com.loopers.config.analytics.ProductMetricsPublishProperties
import com.loopers.domain.common.InternalMessage
import com.loopers.domain.order.OrderService
import com.loopers.domain.payment.PaymentEvent
import com.loopers.domain.usersignal.TargetType
import com.loopers.domain.usersignal.UserSignalEvent
import com.loopers.domain.usersignal.UserSignalRepository
import com.loopers.infrastructure.analytics.ProductMetricsEventPublisher
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.temporal.WeekFields

@Component
class ProductMetricsPublishService(
    private val metricsProps: ProductMetricsPublishProperties,
    private val publisher: ProductMetricsEventPublisher,
    private val userSignalRepository: UserSignalRepository,
    private val orderService: OrderService,
) {
    fun publishFromUserSignals(messages: List<InternalMessage<UserSignalEvent>>) {
        if (messages.isEmpty()) return
        val zone = ZoneId.of(metricsProps.zoneId)
        val periodType = metricsProps.periodType
        val periodKey = currentPeriodKey(periodType, zone)

        val impactedProducts = messages
            .map { it.payload }
            .filter { it.targetType == TargetType.PRODUCT }
            .map { it.targetId }
            .toSet()

        if (impactedProducts.isEmpty()) return

        val events = impactedProducts.mapNotNull { productId ->
            val us = userSignalRepository.findForUpdate(productId, TargetType.PRODUCT) ?: return@mapNotNull null
            ProductMetricsEvent(
                productId = productId,
                periodType = periodType,
                periodKey = periodKey,
                version = metricsProps.version,
                likes = us.likeCount,
                views = us.views,
                orderCount = null,
                generatedAt = null,
            )
        }
        publisher.publish(events)
    }

    fun publishFromOrders(messages: List<InternalMessage<PaymentEvent>>) {
        if (messages.isEmpty()) return
        val orderIds = messages.map { it.payload.orderId }
        if (orderIds.isEmpty()) return

        val orders = orderService.findAll(orderIds)
        val productOrderCounts = orders
            .flatMap { it.items }
            .groupBy { it.productId }
            .mapValues { (_, items) -> items.sumOf { it.quantity.toLong() } }
        if (productOrderCounts.isEmpty()) return

        val zone = ZoneId.of(metricsProps.zoneId)
        val periodType = metricsProps.periodType
        val periodKey = currentPeriodKey(periodType, zone)

        val events = productOrderCounts.map { (productId, count) ->
            ProductMetricsEvent(
                productId = productId,
                periodType = periodType,
                periodKey = periodKey,
                version = metricsProps.version,
                likes = null,
                views = null,
                orderCount = count,
                generatedAt = null,
            )
        }
        publisher.publish(events)
    }

    private fun currentPeriodKey(type: PeriodType, zone: ZoneId): String = when (type) {
        PeriodType.DAILY -> LocalDate.now(zone).toString()
        PeriodType.WEEKLY -> {
            val today = LocalDate.now(zone)
            val week = today.get(WeekFields.ISO.weekOfWeekBasedYear())
            val year = today.get(WeekFields.ISO.weekBasedYear())
            "%04d-%02d".format(year, week)
        }

        PeriodType.MONTHLY -> YearMonth.now(zone).toString()
    }
}

