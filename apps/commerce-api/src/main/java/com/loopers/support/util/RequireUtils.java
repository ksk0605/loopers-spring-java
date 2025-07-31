package com.loopers.support.util;

import java.util.Collection;
import java.util.Map;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

/**
 * 코틀린의 require 모방한 유틸리티 클래스
 * 엔티티 생성 시 필수 값 검증을 위해 사용하는 것을 권장합니다.
 * 예시:
 * <pre>
 * public class User {
 *     private final String name;
 *     private final int age;
 * 
 *     private final String email;
 * 
 *     public User(String name, int age) {
 *         this.name = requireNotNull(name, "이름은 필수입니다.");
 *         this.age = requireNotNull(age, "나이는 필수입니다.");
 *         this.email = requireNonEmpty(email, "이메일은 필수입니다.");
 *     }
 * }
 * </pre>
 */
public final class RequireUtils {

    private RequireUtils() {
    }

    /**
     * 코틀린의 require() 함수와 동일한 기능
     * 조건이 거짓이면 CoreException을 발생시킵니다.
     *
     * @param condition 검증할 조건
     * @param message 예외 메시지
     * @throws CoreException 조건이 거짓인 경우
     */
    public static void require(boolean condition, String message) {
        if (!condition) {
            throw new CoreException(ErrorType.BAD_REQUEST, message);
        }
    }

    /**
     * 코틀린의 require() 함수와 동일한 기능 (기본 메시지)
     *
     * @param condition 검증할 조건
     * @throws CoreException 조건이 거짓인 경우
     */
    public static void require(boolean condition) {
        require(condition, "조건이 충족되지 않았습니다.");
    }

    /**
     * 코틀린의 requireNotNull() 함수와 동일한 기능
     * 객체가 null이면 CoreException을 발생시킵니다.
     *
     * @param value 검증할 객체
     * @param message 예외 메시지
     * @param <T> 객체 타입
     * @return 검증된 객체
     * @throws CoreException 객체가 null인 경우
     */
    public static <T> T requireNotNull(T value, String message) {
        require(value != null, message);
        return value;
    }

    /**
     * 코틀린의 requireNotNull() 함수와 동일한 기능 (기본 메시지)
     *
     * @param value 검증할 객체
     * @param <T> 객체 타입
     * @return 검증된 객체
     * @throws CoreException 객체가 null인 경우
     */
    public static <T> T requireNotNull(T value) {
        return requireNotNull(value, "필수 값이 null입니다.");
    }

    /**
     * 코틀린의 require() 함수와 동일한 기능 (Supplier를 사용한 지연 메시지)
     *
     * @param condition 검증할 조건
     * @param messageSupplier 예외 메시지 공급자
     * @throws CoreException 조건이 거짓인 경우
     */
    public static void require(boolean condition, java.util.function.Supplier<String> messageSupplier) {
        if (!condition) {
            throw new CoreException(ErrorType.BAD_REQUEST, messageSupplier.get());
        }
    }

    /**
     * 코틀린의 requireNotNull() 함수와 동일한 기능 (Supplier를 사용한 지연 메시지)
     *
     * @param value 검증할 객체
     * @param messageSupplier 예외 메시지 공급자
     * @param <T> 객체 타입
     * @return 검증된 객체
     * @throws CoreException 객체가 null인 경우
     */
    public static <T> T requireNotNull(T value, java.util.function.Supplier<String> messageSupplier) {
        require(value != null, messageSupplier);
        return value;
    }

    /**
     * 문자열이 null이 아니고 비어있지 않은지 검증
     *
     * @param value 검증할 문자열
     * @param message 예외 메시지
     * @return 검증된 문자열
     * @throws CoreException 문자열이 null이거나 비어있는 경우
     */
    public static String requireNonEmpty(String value, String message) {
        require(value != null && !value.trim().isEmpty(), message);
        return value;
    }

    /**
     * 문자열이 null이 아니고 비어있지 않은지 검증
     *
     * @param value 검증할 문자열
     * @return 검증된 문자열
     * @throws CoreException 문자열이 null이거나 비어있는 경우
     */
    public static String requireNonEmpty(String value) {
        return requireNonEmpty(value, "필수 문자열이 null이거나 비어있습니다.");
    }

    /**
     * 컬렉션이 null이 아니고 비어있지 않은지 검증
     *
     * @param collection 검증할 컬렉션
     * @param message 예외 메시지
     * @param <T> 컬렉션 타입
     * @return 검증된 컬렉션
     * @throws CoreException 컬렉션이 null이거나 비어있는 경우
     */
    public static <T extends Collection<?>> T requireNonEmpty(T collection, String message) {
        require(collection != null && !collection.isEmpty(), message);
        return collection;
    }

    /**
     * 컬렉션이 null이 아니고 비어있지 않은지 검증
     *
     * @param collection 검증할 컬렉션
     * @param <T> 컬렉션 타입
     * @return 검증된 컬렉션
     * @throws CoreException 컬렉션이 null이거나 비어있는 경우
     */
    public static <T extends Collection<?>> T requireNonEmpty(T collection) {
        return requireNonEmpty(collection, "필수 컬렉션이 null이거나 비어있습니다.");
    }

    /**
     * 맵이 null이 아니고 비어있지 않은지 검증
     *
     * @param map 검증할 맵
     * @param message 예외 메시지
     * @param <K> 키 타입
     * @param <V> 값 타입
     * @return 검증된 맵
     * @throws CoreException 맵이 null이거나 비어있는 경우
     */
    public static <K, V> Map<K, V> requireNonEmpty(Map<K, V> map, String message) {
        require(map != null && !map.isEmpty(), message);
        return map;
    }

    /**
     * 맵이 null이 아니고 비어있지 않은지 검증
     *
     * @param map 검증할 맵
     * @param <K> 키 타입
     * @param <V> 값 타입
     * @return 검증된 맵
     * @throws CoreException 맵이 null이거나 비어있는 경우
     */
    public static <K, V> Map<K, V> requireNonEmpty(Map<K, V> map) {
        return requireNonEmpty(map, "필수 맵이 null이거나 비어있습니다.");
    }
}
