package com.max.timemaster.data.source

import com.max.timemaster.data.CalendarEvent
import com.max.timemaster.data.Result
import com.max.timemaster.data.TimeMasterDataSource
import com.max.timemaster.data.TimeMasterRepository

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * Concrete implementation to load Stylish sources.
 */
class DefaultTimeMasterRepository(private val remoteDataSource: TimeMasterDataSource,
                                  private val localDataSource: TimeMasterDataSource
) : TimeMasterRepository {
    override suspend fun getCalendarId(greaterThan: Long ,lessThan: Long): Result<List<CalendarEvent>> {
        return remoteDataSource.getCalendarId(greaterThan ,lessThan)
    }

    override suspend fun postEvent(calendarEvent: CalendarEvent): Result<Boolean> {
        return remoteDataSource.postEvent(calendarEvent)
    }


//    override suspend fun getMarketingHots(): Result<List<HomeItem>> {
//        return stylishRemoteDataSource.getMarketingHots()
//    }
//
//    override suspend fun getProductList(type: String, paging: String?): Result<ProductListResult> {
//        return stylishRemoteDataSource.getProductList(type = type, paging = paging)
//    }
//
//    override suspend fun getUserProfile(token: String): Result<User> {
//        return stylishRemoteDataSource.getUserProfile(token)
//    }
//
//    override suspend fun getCoupon(): Result<Coupons> {
//        return stylishRemoteDataSource.getCoupon()
//    }
//
//    override suspend fun postCoupon(access_token: String,coupon_id:Int): Result<CouponResult> {
//        return stylishRemoteDataSource.postCoupon(access_token,coupon_id)
//    }
//
//    override suspend fun getQuestions(token: String): Result<Questions> {
//        return stylishRemoteDataSource.getQuestions(token)
//    }
//
//    override suspend fun postQuestions(
//        token: String,
//        content: String
//    ): Result<questionResult> {
//        return stylishRemoteDataSource.postQuestions(token, content)
//    }

//    override suspend fun userSignIn(fbToken: String): Result<UserSignInResult> {
//        return stylishRemoteDataSource.userSignIn(fbToken)
//    }
//
//    override suspend fun checkoutOrder(
//        token: String, orderDetail: OrderDetail): Result<CheckoutOrderResult> {
//        return stylishRemoteDataSource.checkoutOrder(token, orderDetail)
//    }
//
//    override fun getProductsInCart(): LiveData<List<Product>> {
//        return stylishLocalDataSource.getProductsInCart()
//    }
//
//    override fun getProductsFromPayment(): LiveData<List<OrderNumberList>> {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun isProductInCart(id: Long, colorCode: String, size: String): Boolean {
//        return stylishLocalDataSource.isProductInCart(id, colorCode, size)
//    }
//
//    override suspend fun insertProductInCart(product: Product) {
//        stylishLocalDataSource.insertProductInCart(product)
//    }
//
//    override suspend fun updateProductInCart(product: Product) {
//        stylishLocalDataSource.updateProductInCart(product)
//    }
//
//    override suspend fun removeProductInCart(id: Long, colorCode: String, size: String) {
//        stylishLocalDataSource.removeProductInCart(id, colorCode, size)
//    }
//
//    override suspend fun clearProductInCart() {
//        stylishLocalDataSource.clearProductInCart()
//    }
//
//    override suspend fun getUserInformation(key: String?): String {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override suspend fun getOrderNumber(num: String?): Result<List<Reviews>> {
//        return stylishRemoteDataSource.getOrderNumber(num)
//    }
//
//    override suspend fun getHashTag(hashTagNumber: String?): Result<List<HashTagItem>> {
//        return stylishRemoteDataSource.getHashTag(hashTagNumber)
//    }
//
//    override suspend fun getProductComment(id: String): Result<List<Reviews>> {
//        return stylishRemoteDataSource.getProductComment(id)
//    }
//
//    override suspend fun getReviewsAllOrder(token: String): Result<List<ReviewsAllOrder>> {
//        return stylishRemoteDataSource.getReviewsAllOrder(token)
//    }
//
//    override suspend fun postEvaluation(token: String,evaluation: Evaluation): Result<EvaluationMessage> {
//        return stylishRemoteDataSource.postEvaluation(token,evaluation)
//    }
}
