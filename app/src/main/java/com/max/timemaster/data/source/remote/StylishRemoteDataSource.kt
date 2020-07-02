package com.max.timemaster.data.source.remote


import com.max.timemaster.data.TimeMasterDataSource

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * Implementation of the Stylish source that from network.
 */
object TimeMasterRemoteDataSource : TimeMasterDataSource {

//    override suspend fun getMarketingHots(): Result<List<HomeItem>> {
//
//        if (!isInternetConnected()) {
//            return Result.Fail(getString(R.string.internet_not_connected))
//        }
//
//        return try {
//            // this will run on a thread managed by Retrofit
//            val listResult = StylishApi.retrofitService.getMarketingHots()
//
//            listResult.error?.let {
//                return Result.Fail(it)
//            }
//            Result.Success(listResult.toHomeItems())
//
//        } catch (e: Exception) {
//            Logger.w("[${this::class.simpleName}] exception=${e.message}")
//            Result.Error(e)
//        }
//    }
//
//    override suspend fun getProductList(type: String, paging: String?): Result<ProductListResult> {
//
//        if (!isInternetConnected()) {
//            return Result.Fail(getString(R.string.internet_not_connected))
//        }
//
//        return try {
//            // this will run on a thread managed by Retrofit
//            val listResult = StylishApi.retrofitService.getProductList(type = type, paging = paging)
//
//            listResult.error?.let {
//                return Result.Fail(it)
//            }
//            Result.Success(listResult)
//
//        } catch (e: Exception) {
//            Logger.w("[${this::class.simpleName}] exception=${e.message}")
//            Result.Error(e)
//        }
//    }
//
//    override suspend fun getUserProfile(token: String): Result<User> {
//
//        if (!isInternetConnected()) {
//            return Result.Fail(getString(R.string.internet_not_connected))
//        }
//
//        return try {
//            // this will run on a thread managed by Retrofit
//            val listResult = StylishApi.retrofitService.getUserProfile(token)
//
//            listResult.error?.let {
//                return Result.Fail(it)
//            }
//            listResult.user?.let {
//                return Result.Success(it)
//            }
//            Result.Fail(getString(R.string.you_know_nothing))
//
//        } catch (e: Exception) {
//            Logger.w("[${this::class.simpleName}] exception=${e.message}")
//            Result.Error(e)
//        }
//    }
//
//    override suspend fun getCoupon(): Result<Coupons> {
//        if (!isInternetConnected()) {
//            return Result.Fail(getString(R.string.internet_not_connected))
//        }
//
//        return try {
//            // this will run on a thread managed by Retrofit
//            val listResult = StylishApi.retrofitService.getCoupons()
//
//            listResult.error?.let {
//                return Result.Fail(it)
//            }
//            Result.Success(listResult)
//
//        } catch (e: Exception) {
//            Logger.w("[${this::class.simpleName}] exception=${e.message}")
//            Result.Error(e)
//        }
//    }
//
//    override suspend fun postCoupon(access_token: String, coupon_id: Int): Result<CouponResult> {
//        if (!isInternetConnected()) {
//            return Result.Fail(getString(R.string.internet_not_connected))
//        }
//        return try {
//            // this will run on a thread managed by Retrofit
//            val listResult = StylishApi.retrofitService.postCoupons(access_token,coupon_id)
//
//            listResult.error?.let {
//                return Result.Fail(it)
//            }
//            Result.Success(listResult)
//
//        } catch (e: Exception) {
//            Logger.w("[${this::class.simpleName}] exception=${e.message}")
//            Result.Error(e)
//        }
//    }
//
//
//    override suspend fun getQuestions(access_token: String): Result<Questions> {
//        if (!isInternetConnected()) {
//            return Result.Fail(getString(R.string.internet_not_connected))
//        }
//        return try {
//            // this will run on a thread managed by Retrofit
//            val listResult = StylishApi.retrofitService.getQuestions(access_token)
//
//            listResult.error?.let {
//                return Result.Fail(it)
//            }
//            Result.Success(listResult)
//
//        } catch (e: Exception) {
//            Logger.w("[${this::class.simpleName}] exception=${e.message}")
//            Result.Error(e)
//        }
//    }
//
////    override suspend fun postQuestions(access_token: String): Result<questionResult> {
////        TODO("Not yet implemented")
////    }
//
//    override suspend fun postQuestions(
//        access_token: String,
//        content: String
//    ): Result<questionResult> {
//        if (!isInternetConnected()) {
//            return Result.Fail(getString(R.string.internet_not_connected))
//        }
//        return try {
//            // this will run on a thread managed by Retrofit
//            val listResult = StylishApi.retrofitService.postQuestions(access_token,content)
//
//            listResult.error?.let {
//                return Result.Fail(it)
//            }
//            Result.Success(listResult)
//
//        } catch (e: Exception) {
//            Logger.w("[${this::class.simpleName}] exception=${e.message}")
//            Result.Error(e)
//        }
//    }
//
//
//
//
//
//    override suspend fun userSignIn(fbToken: String): Result<UserSignInResult> {
//
//        if (!isInternetConnected()) {
//            return Result.Fail(getString(R.string.internet_not_connected))
//        }
//
//        return try {
//            // this will run on a thread managed by Retrofit
//            val listResult = StylishApi.retrofitService.userSignIn(fbToken = fbToken)
//
//            listResult.error?.let {
//                return Result.Fail(it)
//            }
//            Result.Success(listResult)
//
//        } catch (e: Exception) {
//            Logger.w("[${this::class.simpleName}] exception=${e.message}")
//            Result.Error(e)
//        }
//    }
//
//
////    override suspend fun getOrderNumber(num: String?): Result<OrderNumber> {
////
////
////            if (!isInternetConnected()) {
////                return Result.Fail(getString(R.string.internet_not_connected))
////            }
////
////            return try {
////                // this will run on a thread managed by Retrofit
////                val listResult = StylishApi.retrofitService.getOrderNumber(num = num)
////
////                listResult.error?.let {
////                    return Result.Fail(it)
////                }
////
////                Result.Success(listResult)
////
////
////            } catch (e: Exception) {
////                Logger.w("[${this::class.simpleName}] exception=${e.message}")
////                Result.Error(e)
////            }
////        }
//
//
//    override suspend fun checkoutOrder(
//        token: String, orderDetail: OrderDetail
//    ): Result<CheckoutOrderResult> {
//
//        if (!isInternetConnected()) {
//            return Result.Fail(getString(R.string.internet_not_connected))
//        }
//
//        return try {
//            // this will run on a thread managed by Retrofit
//            val listResult = StylishApi.retrofitService.checkoutOrder(token, orderDetail)
//
//            listResult.error?.let {
//                return Result.Fail(it)
//            }
//            Result.Success(listResult)
//
//
//        } catch (e: Exception) {
//            Logger.w("[${this::class.simpleName}] exception=${e.message}")
//            Result.Error(e)
//        }
//    }
//
//    override fun getProductsInCart(): LiveData<List<Product>> {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override suspend fun isProductInCart(id: Long, colorCode: String, size: String): Boolean {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override suspend fun insertProductInCart(product: Product) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override suspend fun updateProductInCart(product: Product) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override suspend fun removeProductInCart(id: Long, colorCode: String, size: String) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override suspend fun clearProductInCart() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override suspend fun getUserInformation(key: String?): String {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override suspend fun getOrderNumber(num: String?): Result<List<Reviews>> {
//        if (!isInternetConnected()) {
//            return Result.Fail(getString(R.string.internet_not_connected))
//        }
//
//        return try {
//            // this will run on a thread managed by Retrofit
//            val listResult = StylishApi.retrofitService.getOrderNumber(num)
//
//            listResult.error?.let {
//                return Result.Fail(it)
//            }
//            Result.Success(listResult.reviews)
//
//
//        } catch (e: Exception) {
//            Logger.w("[${this::class.simpleName}] exception=${e.message}")
//            Result.Error(e)
//        }
//    }
//
//    override suspend fun getHashTag(hashTagNumber: String?): Result<List<HashTagItem>> {
//        if (!isInternetConnected()) {
//            return Result.Fail(getString(R.string.internet_not_connected))
//        }
//
//        return try {
//            // this will run on a thread managed by Retrofit
//            val listResult = StylishApi.retrofitService.getHashTag(hashTagNumber = hashTagNumber)
//
//            listResult.error?.let {
//                return Result.Fail(it)
//            }
//            Result.Success(listResult.data)
//
//
//        } catch (e: Exception) {
//            Logger.w("[${this::class.simpleName}] exception=${e.message}")
//            Result.Error(e)
//        }
//    }
//
//    override suspend fun getProductComment(id: String): Result<List<Reviews>> {
//        if (!isInternetConnected()) {
//            return Result.Fail(getString(R.string.internet_not_connected))
//        }
//
//        return try {
//            // this will run on a thread managed by Retrofit
//            val listResult = StylishApi.retrofitService.getProductComment(id)
//
//            listResult.error?.let {
//                return Result.Fail(it)
//            }
//            Result.Success(listResult.reviews)
//
//
//        } catch (e: Exception) {
//            Logger.w("[${this::class.simpleName}] exception=${e.message}")
//            Result.Error(e)
//        }
//    }
//
//    override suspend fun getReviewsAllOrder(token: String): Result<List<ReviewsAllOrder>> {
//        if (!isInternetConnected()) {
//            return Result.Fail(getString(R.string.internet_not_connected))
//        }
//
//        return try {
//            // this will run on a thread managed by Retrofit
//            val listResult = StylishApi.retrofitService.getReviewsAllOrder(token)
//
//            listResult.error?.let {
//                return Result.Fail(it)
//            }
//            Result.Success(listResult.reviews)
//
//
//        } catch (e: Exception) {
//            Logger.w("[${this::class.simpleName}] exception=${e.message}")
//            Result.Error(e)
//        }
//    }
//
//    override suspend fun postEvaluation(token: String,evaluation: Evaluation): Result<EvaluationMessage> {
//        if (!isInternetConnected()) {
//            return Result.Fail(getString(R.string.internet_not_connected))
//        }
//
//        return try {
//            // this will run on a thread managed by Retrofit
//            val listResult = StylishApi.retrofitService.postEvaluation(token, evaluation)
//
//            listResult.error?.let {
//                return Result.Fail(it)
//            }
//            Result.Success(listResult)
//
//
//        } catch (e: Exception) {
//            Logger.w("[${this::class.simpleName}] exception=${e.message}")
//            Result.Error(e)
//        }
//    }
}

