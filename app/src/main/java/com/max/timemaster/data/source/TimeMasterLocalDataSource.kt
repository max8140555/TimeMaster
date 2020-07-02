package com.max.timemaster.data.source

import android.content.Context
import com.max.timemaster.data.TimeMasterDataSource


/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * Concrete implementation of a Stylish source as a db.
 */
class TimeMasterLocalDataSource(val context: Context) : TimeMasterDataSource {
//
//    override suspend fun getMarketingHots(): Result<List<HomeItem>> {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override suspend fun getProductList(type: String, paging: String?): Result<ProductListResult> {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override suspend fun getUserProfile(token: String): Result<User> {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override suspend fun getCoupon(): Result<Coupons> {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun postCoupon(access_token: String, coupon_id: Int): Result<CouponResult> {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun getQuestions(access_token: String): Result<Questions> {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun postQuestions(
//        access_token: String,
//        content: String
//    ): Result<questionResult> {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun userSignIn(fbToken: String): Result<UserSignInResult> {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override suspend fun checkoutOrder(
//        token: String, orderDetail: OrderDetail): Result<CheckoutOrderResult> {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun getProductsInCart(): LiveData<List<Product>> {
//        return StylishDatabase.getInstance(context).stylishDatabaseDao.getAllProducts()
//    }
//
//    override suspend fun isProductInCart(id: Long, colorCode: String, size: String): Boolean {
//        return withContext(Dispatchers.IO) {
//            StylishDatabase.getInstance(context).stylishDatabaseDao.get(id, colorCode, size) != null
//        }
//    }
//
//    override suspend fun insertProductInCart(product: Product) {
//        withContext(Dispatchers.IO) {
//            StylishDatabase.getInstance(context).stylishDatabaseDao.insert(product)
//        }
//    }
//
//    override suspend fun updateProductInCart(product: Product) {
//        withContext(Dispatchers.IO) {
//            StylishDatabase.getInstance(context).stylishDatabaseDao.update(product)
//        }
//    }
//
//    override suspend fun removeProductInCart(id: Long, colorCode: String, size: String) {
//        withContext(Dispatchers.IO) {
//            StylishDatabase.getInstance(context).stylishDatabaseDao.delete(id, colorCode, size)
//        }
//    }
//
//    override suspend fun clearProductInCart() {
//        withContext(Dispatchers.IO) {
//            StylishDatabase.getInstance(context).stylishDatabaseDao.clear()
//        }
//    }
//
//    override suspend fun getUserInformation(key: String?): String {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override suspend fun getOrderNumber(num: String?): Result<List<Reviews>> {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun getHashTag(hashTagNumber: String?): Result<List<HashTagItem>> {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun getProductComment(id: String): Result<List<Reviews>> {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun getReviewsAllOrder(token: String): Result<List<ReviewsAllOrder>> {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun postEvaluation(token: String,evaluation: Evaluation): Result<EvaluationMessage> {
//        TODO("Not yet implemented")
//    }
}
