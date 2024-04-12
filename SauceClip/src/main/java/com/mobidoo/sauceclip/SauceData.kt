package com.mobidoo.sauceclip

/**
 * 장바구니 정보를 나타내는 데이터 클래스입니다.
 *
 * @property linkUrl 제품에 접근할 수 있는 URL입니다.
 * @property clipIdx 제품과 연관된 클립의 고유 식별자입니다.
 * @property productId 제품의 고유 식별자입니다.
 * @property price 제품의 가격입니다. 이 값은 null일 수 있습니다.
 * @property productImg 제품 이미지의 URL입니다.
 * @property productName 제품의 이름입니다.
 * @property externalProductId 외부 시스템에서의 제품 고유 식별자입니다. 이 값은 null일 수 있습니다.
 */
data class SauceCartInfo(
    val linkUrl: String?,
    val clipIdx: String,
    val productId: Int,
    val price: String?,
    val productImg: String,
    val productName: String,
    val externalProductId: String?
)

/**
 * 제품 정보를 나타내는 데이터 클래스입니다.
 *
 * @property linkUrl 제품에 접근할 수 있는 URL입니다.
 * @property clipIdx 제품과 연관된 클립의 고유 식별자입니다.
 * @property externalProductId 외부 시스템에서의 제품 고유 식별자입니다. 이 값은 null일 수 있습니다.
 * @property productId 제품의 고유 식별자입니다.
 */
data class SauceProductInfo(
    val linkUrl: String,
    val clipIdx: String,
    val externalProductId: String?,
    val productId: Int
)

/**
 * 공유 정보를 나타내는 데이터 클래스입니다.
 *
 * @property linkUrl 공유할 클립에 접근할 수 있는 URL입니다.
 * @property clipId 공유할 클립의 고유 식별자입니다.
 * @property partnerId 파트너의 고유 식별자입니다.
 * @property thumbnailUrl 썸네일 이미지의 URL입니다.
 * @property title 공유할 클립의 제목입니다.
 * @property tags 공유할 클립의 태그 목록입니다.
 */
data class SauceShareInfo(
    val linkUrl: String,
    val clipId: String,
    val partnerId: String,
    val thumbnailUrl: String,
    val title: String,
    val tags: List<String>
)

/**
 * 방송 정보를 나타내는 데이터 클래스입니다.
 *
 * @property clipId 클립의 고유 식별자입니다.
 * @property curationId 큐레이션의 고유 식별자입니다.
 * @property partnerId 파트너의 고유 식별자입니다.
 * @property shortUrl 짧은 URL입니다. 이 값은 null일 수 있습니다.
 */
data class SauceBroadcastInfo(
    val clipId: Int,
    val curationId: Int,
    val partnerId: String,
    val shortUrl: String?
)

/**
 * 에러 정보를 나타내는 데이터 클래스입니다.
 *
 * @property errorType 에러의 타입입니다.
 * @property errorDetails 에러의 상세 정보입니다.
 */
data class SauceErrorInfo(
    val errorType: String,
    val errorDetails: String
)