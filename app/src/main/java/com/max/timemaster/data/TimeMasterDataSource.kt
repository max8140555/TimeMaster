package com.max.timemaster.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * Main entry point for accessing Stylish sources.
 */
interface TimeMasterDataSource {
    suspend fun getCalendarId(): Result<List<CalendarId>>

//    suspend fun getMarketingHots(): Result<List<HomeItem>>
//
//    suspend fun getProductList(type: String, paging: String? = null): Result<ProductListResult>
//
//    suspend fun getUserProfile(token: String): Result<User>
//
//    suspend fun getCoupon(): Result<Coupons>
//
//    suspend fun postCoupon(access_token: String,coupon_id:Int): Result<CouponResult>
//
//    suspend fun getQuestions(access_token: String): Result<Questions>
//
//    suspend fun postQuestions(access_token: String, content: String): Result<questionResult>
//
//    suspend fun userSignIn(fbToken: String): Result<UserSignInResult>
//
//    suspend fun checkoutOrder(token: String, orderDetail: OrderDetail): Result<CheckoutOrderResult>
//
//    fun getProductsInCart(): LiveData<List<Product>>
//
//    suspend fun isProductInCart(id: Long, colorCode: String, size: String): Boolean
//
//    suspend fun insertProductInCart(product: Product)
//
//    suspend fun updateProductInCart(product: Product)
//
//    suspend fun removeProductInCart(id: Long, colorCode: String, size: String)
//
//    suspend fun clearProductInCart()
//
//    suspend fun getUserInformation(key: String?): String
//
//    suspend fun getOrderNumber(num: String?): Result<List<Reviews>>
//    suspend fun getHashTag(hashTagNumber: String?): Result<List<HashTagItem>>
//    suspend fun getProductComment(id: String): Result<List<Reviews>>
//    suspend fun getReviewsAllOrder(token: String): Result<List<ReviewsAllOrder>>
//    suspend fun postEvaluation(token: String,evaluation: Evaluation): Result<EvaluationMessage>


}
