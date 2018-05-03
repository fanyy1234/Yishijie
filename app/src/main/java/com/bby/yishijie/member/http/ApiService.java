package com.bby.yishijie.member.http;

import com.bby.yishijie.member.entity.Address;
import com.bby.yishijie.member.entity.CartPay;
import com.bby.yishijie.member.entity.CartTotal;
import com.bby.yishijie.member.entity.CatImage;
import com.bby.yishijie.member.entity.City;
import com.bby.yishijie.member.entity.FindItem;
import com.bby.yishijie.member.entity.Gift;
import com.bby.yishijie.member.entity.GiftOrder;
import com.bby.yishijie.member.entity.GlobalIndex;
import com.bby.yishijie.member.entity.IndexAd;
import com.bby.yishijie.member.entity.IndexProductBrand;
import com.bby.yishijie.member.entity.LimitProduct;
import com.bby.yishijie.member.entity.LimitTime;
import com.bby.yishijie.member.entity.Member;
import com.bby.yishijie.member.entity.Message;
import com.bby.yishijie.member.entity.Order;
import com.bby.yishijie.member.entity.OrderNums;
import com.bby.yishijie.member.entity.PayInfo;
import com.bby.yishijie.member.entity.PostFee;
import com.bby.yishijie.member.entity.Product;
import com.bby.yishijie.member.entity.ProductClassify;
import com.bby.yishijie.member.entity.ProductDetail;
import com.bby.yishijie.member.entity.ProductSpec;
import com.bby.yishijie.member.entity.RefundDetail;
import com.bby.yishijie.member.entity.Voucher;
import com.bby.yishijie.shop.entity.Account;
import com.bby.yishijie.shop.entity.AvailableProfit;
import com.bby.yishijie.shop.entity.BalanceDetail;
import com.bby.yishijie.shop.entity.Customer;
import com.bby.yishijie.shop.entity.FansProfit;
import com.bby.yishijie.shop.entity.IncomeRecord;
import com.bby.yishijie.shop.entity.IntegralDetail;
import com.bby.yishijie.shop.entity.ProductBrand;
import com.bby.yishijie.shop.entity.ProductMaterial;
import com.bby.yishijie.shop.entity.ProfitAll;
import com.bby.yishijie.shop.entity.Statistic;
import com.bby.yishijie.shop.entity.TotalProfit;
import com.bby.yishijie.shop.entity.WithDrawRecord;
import com.sunday.common.model.ResultDO;
import com.sunday.common.model.Version;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


/**
 * Created by admin on 2016/12/21.
 */

public interface ApiService {


    /**
     * 发送短信验证码
     * @param type (1-绑定 2-登录 3-身份验证 4-注册)
     */
    @FormUrlEncoded
    @POST("/mobi/member/AbSendActiveCode")
    Call<ResultDO> sendCode(@Field("mobile") String mobile, @Field("type") int type);



    /**
     * 绑定
     *@param
     *@param memberId 会员Id(绑定时必传)
     */
    @FormUrlEncoded
    @POST("/mobi/member/bindMobile")
    Call<ResultDO<Member>> bindMobile(@Field("mobile") String mobile, @Field("activeCode") String activeCode,
                                      @Field("memberId") Long memberId);

    /**
     * 验证短信验证码
     *@param type 类型(1-绑定 2-登录  3-身份验证1-绑定 2-登录 3-身份验证 4-注册)
     *@param memberId 会员Id(绑定时必传)
     */
    @FormUrlEncoded
    @POST("/mobi/member/AcValidateActiveCode")
    Call<ResultDO<Member>> checkCode(@Field("mobile") String mobile, @Field("activeCode") String activeCode,
                                     @Field("type") int type, @Field("memberId") Long memberId);

    /**
     * 验证短信验证码
     *@param
     *@param
     */
    @FormUrlEncoded
    @POST("/mobi/member/AbForgetPwd")
    Call<ResultDO<Integer>> forgetPwd(@Field("mobile") String mobile, @Field("code") String code,
                                      @Field("newPwd") String newPwd);


    /**
     * 密码登录
     *@param
     *@param
     */
    @FormUrlEncoded
    @POST("/mobi/member/AbLoginByPwd")
    Call<ResultDO<Member>> login(@Field("mobile") String mobile, @Field("password") String password);

    /**
     * 店主密码登录
     *@param
     *@param
     */
    @FormUrlEncoded
    @POST("/mobi/shop/AaLogin")
    Call<ResultDO<com.bby.yishijie.shop.entity.Member>> login2(@Field("mobile") String mobile, @Field("password") String password);



    /**
     * 手机号注册
     *@param
     *@param
     */
    @FormUrlEncoded
    @POST("/mobi/member/AaRegisterMemberByMobile")
    Call<ResultDO<Member>> register(@Field("mobile") String mobile,
                                    @Field("password") String password,
                                    @Field("nickName") String nickName,
                                    @Field("yzm") String yzm,
                                    @Field("rec") String rec);


    /**
     * 微信一键登录
     *
     * @param
     */
    @FormUrlEncoded
    @POST("/mobi/member/AaRegisterMemberByApp")
    Call<ResultDO<Member>> wxLogin(@Field("nickName") String nickName, @Field("logo") String logo,
                                   @Field("unionId") String unionId, @Field("openId") String openId);

    /**
     * 输入邀请码绑定
     *
     *@param memberId
     */
    @FormUrlEncoded
    @POST("/mobi/member/AbBindByCode")
    Call<ResultDO> bindShopBycode(@Field("memberId") long memberId, @Field("initCode") String initCode);

    /**
     * 首页轮播广告
     */
    @POST("/mobi/product/AgAdveList")
    Call<ResultDO<IndexAd>> getAdveList();


    /**
     * 首页限时购时间段
     */
    @POST("/mobi/product/AhLimitTimeList")
    Call<ResultDO<List<LimitTime>>> getLimitTimeList();

    /**
     * 首页限时购商品列表
     */
    @FormUrlEncoded
    @POST("/mobi/product/AhLimitTimeProList")
    Call<ResultDO<LimitProduct>> getLimitTimeProList(@Field("id") long id);


    /**
     * 发现-列表
     */
    @FormUrlEncoded
    @POST("/mobi/product/AkFindList")
    Call<ResultDO<List<FindItem>>> findList(@Field("pageNo") int pageNo, @Field("pageSize") int pageSize);

    /**
     * 一级产品分类
     */
    @POST("/mobi/product/AaProCat")
    Call<ResultDO<List<ProductClassify>>> getProCat();

    /**
     * 二级产品分类
     */
    @FormUrlEncoded
    @POST("/mobi/product/AbSubCat")
    Call<ResultDO<List<ProductClassify>>> getSubCat(@Field("parentId") long parentId);


    /**
     * 产品分类下轮播图
     */
    @FormUrlEncoded
    @POST("/mobi/product/AaCatImage")
    Call<ResultDO<List<CatImage>>> getCatImage(@Field("parentId") long parentId);



    /**
     * 产品列表
     * @param type 类型(1-普通商品 2-限时购)
     * @param parentId 一级分类id
     * @param catId 二级分类id
     * @param name 产品名称
     * @param name proType   产品类型 0-普通产品 1-积分商品
     */
    @FormUrlEncoded
    @POST("/mobi/product/AdProductList")
    Call<ResultDO<List<Product>>> getProductList(@Field("type") Integer type,@Field("proType") Integer proType,
                                                 @Field("memberId") Integer memberId,@Field("parentId") Integer parentId,
                                                 @Field("catId") Integer catId, @Field("brandId") Integer brandId,
                                                 @Field("name") String name, @Field("page") Integer pageNo,
                                                 @Field("rows") Integer pageSize);
    @FormUrlEncoded
    @POST("/mobi/product/AdProductList")
    Call<ResultDO<List<com.bby.yishijie.shop.entity.Product>>> getProductList2(@Field("type") Integer type,@Field("proType") Integer proType,
                                                 @Field("memberId") Integer memberId,@Field("parentId") Integer parentId,
                                                 @Field("catId") Integer catId, @Field("brandId") Integer brandId,
                                                 @Field("name") String name, @Field("page") Integer pageNo,
                                                 @Field("rows") Integer pageSize);

    /**
     * 产品列表(新)，获取满减产品
     * @param type 类型(2-满减)
     */
    @FormUrlEncoded
    @POST("/mobi/product/AdgetProductsByBrand")
    Call<ResultDO<List<Product>>> getProList(@Field("type") int type, @Field("brandId") Long brandId,
                                             @Field("pageNo") Integer pageNo, @Field("pageSize") Integer pageSize);

    /**
     * 产品列表(新)，获取满减产品
     * @param type 类型(2-满减)
     */
    @FormUrlEncoded
    @POST("/mobi/product/AdgetProductsByBrand")
    Call<ResultDO<List<com.bby.yishijie.shop.entity.Product>>> getProList2(@Field("type") int type, @Field("brandId") Long brandId,
                                             @Field("pageNo") Integer pageNo, @Field("pageSize") Integer pageSize,
                                             @Field("memberId") long memberId);


    /**
     * 产品品牌
     */
    @FormUrlEncoded
    @POST("/mobi/product/AcGetBrands")
    Call<ResultDO<List<IndexProductBrand>>> getProductBrand(@Field("pageNo") Integer pageNo,
                                                            @Field("pageSize") Integer pageSize);

    /**
     * 店主精选品牌
     *
     * @param
     */
    @FormUrlEncoded
    @POST("/mobi/member/AkShopBrand")
    Call<ResultDO<List<IndexProductBrand>>> getShopBrand(@Field("memberId") long memberId, @Field("pageNo") int pageNo, @Field("pageSize") int pageSize);

    /**
     * 店主精选产品
     *
     * @param
     */
    @FormUrlEncoded
    @POST("/mobi/member/AkShopProduct")
    Call<ResultDO<List<Product>>> getShopProduct(@Field("memberId") long memberId, @Field("pageNo") Integer pageNo,
                                                 @Field("pageSize") Integer pageSize);



    /**
     * 产品详情
     *
     * @param type 类型(1-普通商品 2-限时购 3-拼团 4-钻石专享)
     */
    @FormUrlEncoded
    @POST("/mobi/product/AeProductDetail")
    Call<ResultDO<ProductDetail>> getProductDetail(@Field("type") int type, @Field("id") long id,
                                                   @Field("limitId") Long limitId);
    /**
     * 产品详情
     *
     * @param type 类型(1-普通商品 2-限时购 3-拼团 4-钻石专享)
     */
    @FormUrlEncoded
    @POST("/mobi/product/AeProductDetail")
    Call<ResultDO<com.bby.yishijie.shop.entity.ProductDetail>> getProductDetail2(@Field("type") int type, @Field("id") long id,
                                                   @Field("limitId") Long limitId,@Field("memberId") Long memberId);

    /**
     * 产品规格信息
     */
    @FormUrlEncoded
    @POST("/mobi/product/AiParamInfo")
    Call<ResultDO<ProductSpec>> getSpecInfo(@Field("id") long id, @Field("colorId") Integer colorId,
                                            @Field("sizeId") Integer sizeId);

    /**
     *产品图文和购买须知
     * @param type 类型(1-产品详情2-购买须知)
     */
    @FormUrlEncoded
    @GET("/mobi/product/AfH5Info")
    Call<ResultDO<String>> getH5Info(@Field("id") long addressId, @Field("type") int type);

    /**
     * 立即购买
     * @param type 类型(1-普通商品 2-限时购 3--试用)
     */
    @FormUrlEncoded
    @POST("/mobi/cart/AiBuyNow")
    Call<ResultDO<CartPay>> buyNow(@Field("type") int type, @Field("productId") long productId,
                                   @Field("paramId") long paramId, @Field("memberId") long memberId,
                                   @Field("num") int num, @Field("addressId") Integer addressId);

    /**
     * 立即购买(新)
     * @param type 类型(1-普通商品 2-限时购 3-跨境 4-满减)
     */
    @FormUrlEncoded
    @POST("/mobi/cart/AiNewBuyNow")
    Call<ResultDO<CartPay>> buyNowNew(@Field("type") int type, @Field("productId") int productId,
                                      @Field("memberId") int memberId,@Field("paramId") int paramId,
                                      @Field("num") int num);
    /**
     * 立即购买(新)店主
     * @param type 类型(1-普通商品 2-限时购 3-跨境 4-满减)
     */
    @FormUrlEncoded
    @POST("/mobi/cart/AiNewBuyNow")
    Call<ResultDO<com.bby.yishijie.shop.entity.CartPay>> buyNowNew2(@Field("type") int type, @Field("productId") int productId,
                                                                    @Field("memberId") int memberId, @Field("paramId") int paramId,
                                                                    @Field("num") int num);







    /**
     * 提交订单(立即购买)
     *
     * @param orderType 订单类型(1-用户订单 2-店主订单)
     */
    @FormUrlEncoded
    @POST("/mobi/cart/AjCreateOrder")
    Call<ResultDO<Order>> createOrderBuyNow(@Field("addressId") int addressId, @Field("id") long id,
                                            @Field("voucherId") Integer voucherId, @Field("moneyId") Integer moneyId,
                                            @Field("message") String message, @Field("appType") Integer orderType, @Field("payType") Integer payType,
                                            @Field("identityNo") String identityNo, @Field("realName") String realName,
                                            @Field("sendType") Integer sendType);

    /**
     * 提交订单(立即购买)
     *
     * @param orderType 订单类型(1-用户订单 2-店主订单)
     */
    @FormUrlEncoded
    @POST("/mobi/cart/AjCreateOrder")
    Call<ResultDO<com.bby.yishijie.shop.entity.Order>> createOrderBuyNow(@Field("addressId") int addressId, @Field("id") long id,
                                            @Field("voucherId") Integer voucherId, @Field("score") int score,
                                            @Field("money") String money, @Field("message") String message, @Field("appType") Integer orderType,
                                            @Field("identityNo") String identityNo, @Field("realName") String realName,
                                            @Field("sendType") Integer sendType);



    /**
     * 购物车列表
     */
    @FormUrlEncoded
    @POST("/mobi/cart/AbCartList")
    Call<ResultDO<CartTotal>> getCartList(@Field("memberId") long memberId);


    /**
     * 加入购物车
     * @param type 类型(1-普通商品 2-限时购)
     */
    @FormUrlEncoded
    @POST("/mobi/cart/AaAddCart")
    Call<ResultDO> addCart(@Field("type") int type, @Field("productId") long productId,
                           @Field("paramId") long paramId, @Field("memberId") long memberId,
                           @Field("num") int num);



    /**
     * 加减购物车项
     */
    @FormUrlEncoded
    @POST("/mobi/cart/AcCartNum")
    Call<ResultDO> updateCart(@Field("id") long id, @Field("num") int num);

    /**
     * 删除购物车项
     */
    @FormUrlEncoded
    @POST("/mobi/cart/AdDeleteCart")
    Call<ResultDO> deleteCart(@Field("id") long id);

    /**
     * 购物车结算
     */
    @FormUrlEncoded
    @POST("/mobi/cart/AePayCart")
    Call<ResultDO<CartPay>> payCart(@Field("ids") String ids, @Field("memberId") long memberId, @Field("addressId") Integer addressId,
                                    @Field("isActive") Integer isActive);

    /**
     * 购物车结算(新)
     */
    @FormUrlEncoded
    @POST("/mobi/cart/AeNewPayCart")
    Call<ResultDO<CartPay>> payCartNew(@Field("ids") String ids, @Field("memberId") long memberId, @Field("addressId") Integer addressId,
                                       @Field("isActive") Integer isActive);



    /**
     * 所有省份
     */
    @POST("/mobi/cart/AhGetProvinces")
    Call<ResultDO<List<City>>> getProvinces();

    /**
     * 所有城市
     */
    @FormUrlEncoded
    @POST("/mobi/cart/AhGetCitys")
    Call<ResultDO<List<City>>> getCitys(@Field("provinceId") Integer provinceId);

    /**
     *所有区县
     */
    @FormUrlEncoded
    @POST("/mobi/cart/AhGetDistricts")
    Call<ResultDO<List<City>>> getDistricts(@Field("cityId") Integer cityId);

    /**
     * 新增或修改地址列表
     */
    @FormUrlEncoded
    @POST("/mobi/cart/AfSaveAddress")
    Call<ResultDO> saveAddr(@Field("memberId") long memberId, @Field("provinceId") int provinceId,
                            @Field("cityId") int cityId, @Field("districtId") int districtId,
                            @Field("name") String name, @Field("mobile") String mobile,
                            @Field("address") String address, @Field("cityDetail") String cityDetail,
                            @Field("addressId") Integer addressId);

    /**
     * 地址列表
     */
    @FormUrlEncoded
    @POST("/mobi/cart/AgAddressList")
    Call<ResultDO<List<Address>>> getAddrList(@Field("memberId") long memberId);

    /**
     * 设置默认地址
     */
    @FormUrlEncoded
    @POST("/mobi/cart/AgSetDefault")
    Call<ResultDO> setAddrDefault(@Field("memberId") long memberId, @Field("addressId") int addressId);

    /**
     * 删除收货地址
     */
    @FormUrlEncoded
    @POST("/mobi/cart/AgDeleteAddress")
    Call<ResultDO> delAddr(@Field("addressId") int addressId);

    /**
     * 获取默认收货地址
     */
    @FormUrlEncoded
    @POST("/mobi/cart/AgGetDefault")
    Call<ResultDO<Address>> getDefaultAddr(@Field("memberId") long memberId);

    /**
     * 提交订单(购物车)
     *
     * @param orderType 订单类型(1-用户订单 2-店主订单)
     */
    @FormUrlEncoded
    @POST("/mobi/cart/AhCreateOrder")
    Call<ResultDO<Order>> createOrder(@Field("addressId") int addressId, @Field("ids") String cartIds,
                                      @Field("voucherId") Integer voucherId, @Field("message") String message, @Field("appType") Integer orderType,
                                      @Field("identityNo") String identityNo, @Field("realName") String realName,
                                      @Field("isActive") Integer isActive, @Field("sendType") Integer sendType);

    /**
     * 提交订单(购物车)(新)
     *
     * @param orderType 订单类型(1-用户订单 2-店主订单)
     */
    @FormUrlEncoded
    @POST("/mobi/cart/AhNewCreateOrder")
    Call<ResultDO<Order>> createOrderNew(@Field("addressId") int addressId, @Field("ids") String cartIds,
                                         @Field("voucherId") Integer voucherId, @Field("message") String message, @Field("appType") Integer orderType,
                                         @Field("identityNo") String identityNo, @Field("realName") String realName,
                                         @Field("isActive") Integer isActive, @Field("sendType") Integer sendType);


    /**
     * 提交订单(购物车)(新)
     *
     * @param orderType 订单类型(1-用户订单 2-店主订单)
     */
    @FormUrlEncoded
    @POST("/mobi/cart/AhNewCreateOrder")
    Call<ResultDO<com.bby.yishijie.shop.entity.Order>> createOrderNew(@Field("addressId") int addressId, @Field("ids") String cartIds,
                                         @Field("voucherId") Integer voucherId, @Field("score") int score,
                                         @Field("money") String money, @Field("message") String message,
                                         @Field("appType") Integer orderType, @Field("identityNo") String identityNo,
                                         @Field("realName") String realName, @Field("isActive") Integer isActive,
                                         @Field("sendType") Integer sendType);


    /**
     * 订单列表
     *
     * @param orderType 订单类型(1-用户订单 2-店主订单)
     * @param status 	状态(0-待付款 1-待发货 2-待收货 4-已完成)
     */
    @FormUrlEncoded
    @POST("/mobi/order/AaOrderList")
    Call<ResultDO<List<Order>>> getOrderList(@Field("memberId") long memberId, @Field("status") Integer status,
                                             @Field("pageNo") Integer pageNo, @Field("pageSize") Integer pageSize,
                                             @Field("orderType") Integer orderType);

    /**
     * 订单列表
     *
     * @param orderType 订单类型(1-用户订单 2-店主订单)
     * @param status 	状态(0-待付款 1-待发货 2-待收货 4-已完成)
     */
    @FormUrlEncoded
    @POST("/mobi/order/AaMemberOrderList")
    Call<ResultDO<List<Order>>> getOrderListNew(@Field("memberId") long memberId, @Field("status") Integer status,
                                                @Field("pageNo") Integer pageNo, @Field("pageSize") Integer pageSize,
                                                @Field("orderType") Integer orderType);





    /**
     * 从订单列表去支付
     *
     * @param orderId
     *
     */
    @FormUrlEncoded
    @POST("/mobi/order/AeToPay")
    Call<ResultDO<Order>> getOrderPay(@Field("orderId") long orderId);


    /**
     * 删除订单
     */
    @FormUrlEncoded
    @POST("/mobi/order/AbDelete")
    Call<ResultDO> delOrder(@Field("orderId") long orderId);

    /**
     * 确认收货
     */
    @FormUrlEncoded
    @POST("/mobi/order/AcConfirm")
    Call<ResultDO> confirmRecieve(@Field("orderId") long orderId);

    /**
     * 拼团确认收货
     */
    @FormUrlEncoded
    @POST("/mobi/product/AqConfirm")
    Call<ResultDO> confirmGroupRecieve(@Field("orderId") long orderId);

    /**
     * 取消订单
     */
    @FormUrlEncoded
    @POST("/mobi/order/AdCancel")
    Call<ResultDO> cancelOrder(@Field("orderId") long orderId);

    /**
     * 申请退款退货
     */
    @FormUrlEncoded
    @POST("/mobi/order/AfCancel")
    Call<ResultDO> applyRefund(@Field("orderId") long orderId, @Field("reason") String reason, @Field("mobile") String mobile);

    /**
     * 订单售后
     */
    @FormUrlEncoded
    @POST("/mobi/order/customerService/apply")
    Call<ResultDO> applyRefundOrder(@Field("orderId") long orderId,
                                    @Field("type") int type, @Field("reason") String reason, @Field("mobile") String mobile,
                                    @Field("desc") String desc, @Field("amount") String amount, @Field("images") String images,
                                    @Field("orderItemId") Integer orderItemId);


    /**
     * 拼团申请退款退货
     */
    @FormUrlEncoded
    @POST("/mobi/product/ArCancel")
    Call<ResultDO> applyGroupRefund(@Field("orderId") long orderId, @Field("reason") String reason, @Field("mobile") String mobile);


    /**
     * 取消申请退款退货
     */
    @FormUrlEncoded
    @POST("/mobi/order/AgBack")
    Call<ResultDO> cancelRefund(@Field("orderId") long orderId);


    /**
     * 取消申请退款退货
     */
    @FormUrlEncoded
    @POST("/mobi/order/customerService/cancelRefund")
    Call<ResultDO> cancelRefundOrder(@Field("customerServiceId") String customerServiceId);

    /**
     * 售后进度
     */
    @FormUrlEncoded
    @POST("/mobi/order/customerService/getCustomerServiceInfo")
    Call<ResultDO<RefundDetail>> getCustomerServiceInfo(@Field("orderId") long orderId);






    /**
     * 优惠券和云币
     *
     * @param status 状态(0-未使用 1-已使用 2-已过期)
     * @param type 类型(1-优惠券 2-邮费券)
     */
    @FormUrlEncoded
    @POST("/mobi/member/AeMyVoucher")
    Call<ResultDO<List<Voucher>>> getVoucherList(@Field("memberId") long memberId, @Field("status") int status,
                                                 @Field("type") Integer type, @Field("amount") Double amount,
                                                 @Field("postFee") Double postFee);

    /**
     * 开店礼包列表
     * @return
     */
    @POST("/mobi/member/AgShopGIft")
    Call<ResultDO<List<Gift>>> getGifts();

    /**
     * 开店创建订单
     *
     * @param addressId
     * @param refId
     */
    @FormUrlEncoded
    @POST("/mobi/member/AhCreateOrder")
    Call<ResultDO<GiftOrder>> createGiftOrder(@Field("addressId") int addressId, @Field("refId") int refId);

    /**
     * 个人信息
     *
     *
     */
    @FormUrlEncoded
    @POST("/mobi/member/AdGetInfo")
    Call<ResultDO<Member>> getMemberInfo(@Field("memberId") long memberId);
    /**
     * 个人信息
     *
     *
     */
    @FormUrlEncoded
    @POST("/mobi/member/AdGetInfo")
    Call<ResultDO<com.bby.yishijie.shop.entity.Member>> getMemberInfo2(@Field("memberId") long memberId);


    /**
     * 多张图片上传
     */
    @Multipart
    @POST("/mobi/shop/AhImageUpload")
    Call<ResultDO<List<String>>> uploadImgs(@Part("image") RequestBody body);

    /**
     * 修改个人信息
     *
     * @param
     */
    @FormUrlEncoded
    @POST("/mobi/member/AoEditInfo")
    Call<ResultDO<Member>> saveEditInfo(@Field("memberId") long memberId, @Field("sex") Integer sex,
                                        @Field("nickName") String nickName, @Field("image") String image,
                                        @Field("provinceId") Integer provinceId, @Field("cityId") Integer cityId,
                                        @Field("cityName") String cityName);
    /**
     * 修改个人信息店主
     *
     * @param
     */
    @FormUrlEncoded
    @POST("/mobi/member/AoEditInfo")
    Call<ResultDO<com.bby.yishijie.shop.entity.Member>> saveEditInfo2(@Field("memberId") long memberId, @Field("sex") Integer sex,
                                        @Field("nickName") String nickName, @Field("image") String image,
                                        @Field("provinceId") Integer provinceId, @Field("cityId") Integer cityId,
                                        @Field("cityName") String cityName);

    /**
     * 修改密码
     *
     * @param
     */
    @FormUrlEncoded
    @POST("/mobi/member/AbUpdatePwd")
    Call<ResultDO<Integer>> updatePwd(@Field("memberId") long memberId, @Field("oldPwd") String oldPwd, @Field("newPwd") String newPwd);

    /**
     * 微信支付(用户版-普通订单)
     *
     * @param
     */
    @FormUrlEncoded
    @POST("/mobi/wxPay/AaPay")
    Call<ResultDO<PayInfo>> getNormalPayInfo(@Field("orderId") long orderId);

    /**
     * 微信支付(用户版-拼团订单)
     *
     * @param
     */
    @FormUrlEncoded
    @POST("/mobi/wxPay/AbPay")
    Call<ResultDO<PayInfo>> getGroupPayInfo(@Field("orderId") long orderId);

    /**
     * 微信支付(用户版-开店订单)
     *
     * @param
     */
    @FormUrlEncoded
    @POST("/mobi/wxPay/AcPay")
    Call<ResultDO<PayInfo>> getOpenShopPayInfo(@Field("orderId") long orderId);

    /**
     * 推送 登录保存tokend
     * @param appType 1:用户版 2:店主版
     * @param  type 	类型 1:Android 2:IOS
     */
    @FormUrlEncoded
    @POST("/mobi/appclient/saveAppClientId")
    Call<ResultDO> upLoadClientId(@Field("memberId") long memberId, @Field("clientId") String clientId, @Field("type") int type,
                                  @Field("appType") int appType);

    /**
     * 退出删除tokend
     */
    @FormUrlEncoded
    @POST("/mobi/appclient/cleanClientId")
    Call<ResultDO> deleteClientId(@Field("memberId") long memberId, @Field("appType") int appType);

    /**
     * 消息盒子
     */
    @FormUrlEncoded
    @POST("/mobi/message/messageRecords")
    Call<ResultDO<List<Message>>> getMessageList(@Field("memberId") long memberId, @Field("appType") int appType,
                                                 @Field("type") int type, @Field("isClear") int isClear);


    /**
     *订单数量标记
     */
    @FormUrlEncoded
    @POST("/mobi/order/AhOrderCount")
    Call<ResultDO<OrderNums>> getOrderCount(@Field("memberId") long memberId, @Field("orderType") int orderType);


    //版本更新
    @FormUrlEncoded
    @POST("/mobi/apk/getApkInfo")
    Call<ResultDO<Version>> checkVersion(@Field("type") Integer type, @Field("versionCode") int versionCode);


    //跨境商城
    @POST("/mobi/cross/crossData")
    Call<ResultDO<GlobalIndex>> getGlobalIndex();



    /**
     *  跨境产品列表
     * @param catId 分类id
     * @param isSelection 是否精选 0否 1是
     */
    @FormUrlEncoded
    @POST("/mobi/cross/products")
    Call<ResultDO<List<Product>>> getGlobalProList(@Field("catId") int catId, @Field("isSelection") Integer isSelection,
                                                   @Field("pageNo") Integer pageNo, @Field("pageSize") Integer pageSize);

    /**
     * 求补货
     *
     */
    @FormUrlEncoded
    @POST("/mobi/product/replenishment")
    Call<ResultDO> replenishment(@Field("productId") long productId, @Field("memberId") long memberId,
                                 @Field("appType") int appType);



    /**
     * 邮费
     *
     */
    @FormUrlEncoded
    @POST("/mobi/order/AaGetPostFee")
    Call<ResultDO<PostFee>> getPostFee(@Field("addressId") int addressId, @Field("amount") String amount);

    /**
     * 获取可领取券
     *
     */
    @FormUrlEncoded
    @POST("/mobi/member/AoGetAllVoucher")
    Call<ResultDO<List<Voucher>>> getAllVoucher(@Field("memberId") long memberId, @Field("pageNo") int pageNo,
                                                @Field("pageSize") int pageSize);

    /**
     * 领取券
     *
     */
    @FormUrlEncoded
    @POST("/mobi/member/AoReceiveVoucher")
    Call<ResultDO> receiveVoucher(@Field("memberId") long memberId, @Field("voucherId") int voucherId);


    /**
     * 发现详情
     *
     */
    @FormUrlEncoded
    @POST("/mobi/product/AkFindDetail")
    Call<ResultDO<FindItem>> getFindDetail(@Field("findId") long findId);

    /**
     * 新增素材
     */
    @FormUrlEncoded
    @POST("/mobi/shop/AiAddSC")
    Call<ResultDO> saveMaterial(@Field("memberId") long memberId, @Field("text") String text,
                                @Field("images") String images, @Field("productId") long productId);

    /**
     * 删除素材
     */
    @FormUrlEncoded
    @POST("/mobi/shop/AgDeleteSC")
    Call<ResultDO> delMaterial(@Field("id") long id);

    /**
     * 素材
     *
     * @param memberId 会员Id(官方精选传0)
     */
    @FormUrlEncoded
    @POST("/mobi/shop/AfProductSC")
    Call<ResultDO<List<ProductMaterial>>> getProductMaterial(@Field("memberId") long memberId, @Field("productId") long productId);
    /**
     * 上/下架产品
     *
     * @param type 1-上架 2-下架
     */
    @FormUrlEncoded
    @POST("/mobi/shop/AeAddPro")
    Call<ResultDO<Integer>> addPro(@Field("memberId") long memberId, @Field("type") int type,
                                   @Field("productId") Long productId, @Field("brandId") Long brandId);

    /**
     * 店铺统计(订单/销售/收益/粉丝)
     *
     * @param
     */
    @FormUrlEncoded
    @POST("/mobi/shop/AyShopCount")
    Call<ResultDO<Statistic>> getStatistic(@Field("memberId") long memberId);

    /**
     * 收益详情
     *
     * @param type 类型(1-总收益 2-待确认收益)
     */
    @FormUrlEncoded
    @POST("/mobi/shop/AxIncomeDetail")
    Call<ResultDO> getIncomeDetail(@Field("memberId") long memberId, @Field("type") int type, @Field("startTime")
            String startTime, @Field("endTime") String endTime);

    /**
     * 累计收益详情
     *
     * @param
     */
    @FormUrlEncoded
    @POST("/mobi/shop/BbProfitDetail")
    Call<ResultDO<TotalProfit>> getTotalProfit(@Field("memberId") long memberId);

    /**
     * 待收收益详情
     *
     * @param
     */
    @FormUrlEncoded
    @POST("/mobi/shop/BiWaitDetail")
    Call<ResultDO<List<AvailableProfit>>> getAvailableProfit(@Field("memberId") long memberId, @Field("pageNo") int pageNo,
                                                             @Field("pageSize") int pageSize);

    /**
     * 积分明细
     *
     * @param
     */
    @FormUrlEncoded
    @POST("/mobi/shop/BhScoreDetail")
    Call<ResultDO<List<IntegralDetail>>> getScoreDetail(@Field("memberId") long memberId, @Field("pageNo") int pageNo,
                                                        @Field("pageSize") int pageSize);


    /**
     * 销售管理
     *
     * @param
     */
    @FormUrlEncoded
    @POST("/mobi/shop/AqIncomeRecord")
    Call<ResultDO<List<IncomeRecord>>> getIncomeRecord(@Field("memberId") long memberId, @Field("startTime")
            String startTime, @Field("endTime") String endTime);

    /**
     * 店主产品
     *
     * @param
     */
    @FormUrlEncoded
    @POST("/mobi/shop/AjShopProduct")
    Call<ResultDO<List<Product>>> getShopProducts(@Field("memberId") long memberId, @Field("pageNo") int pageNo,
                                                  @Field("pageSize") int pageSize);
    /**
     * 店主产品
     *
     * @param
     */
    @FormUrlEncoded
    @POST("/mobi/shop/AjShopProduct")
    Call<ResultDO<List<com.bby.yishijie.shop.entity.Product>>> getShopProducts2(@Field("memberId") long memberId, @Field("pageNo") int pageNo,
                                                  @Field("pageSize") int pageSize);

    /**
     * 店主品牌
     *
     * @param
     */
    @FormUrlEncoded
    @POST("/mobi/shop/AkShopBrand")
    Call<ResultDO<List<ProductBrand>>> getShopBrands(@Field("memberId") long memberId, @Field("pageNo") int pageNo,
                                                     @Field("pageSize") int pageSize);

    /**
     * 钻石专享
     *
     * @param
     */
    @FormUrlEncoded
    @POST("/mobi/shop/ApDiscountList")
    Call<ResultDO<List<Product>>> getDiscountList(@Field("type") int type, @Field("memberId") long memberId);
    /**
     * 店主产品/品牌排序
     *
     * @param
     */
    @FormUrlEncoded
    @POST("/mobi/shop/AeShopProductSort")
    Call<ResultDO<Integer>> shopSort(@Field("memberId") long memberId, @Field("productIds") String productIds,
                                     @Field("brandIds") String brandIds);

    /**
     * 客户列表
     *
     * @param
     */
    @FormUrlEncoded
    @POST("/mobi/shop/AmMemberList")
    Call<ResultDO<Customer>> getMemberList(@Field("memberId") long memberId);

    /**
     * 业绩管理
     *
     * @param
     */
    @FormUrlEncoded
    @POST("/mobi/shop/ArSaleRecord")
    Call<ResultDO<FansProfit>> getSaleRecord(@Field("memberId") long memberId);
    /**
     * 我的收益
     *
     * @param
     */
    @FormUrlEncoded
    @POST("/mobi/shop/AoMyProfit")
    Call<ResultDO<ProfitAll>> getMyProfit(@Field("memberId") long memberId);
    /**
     * 今日金价
     *
     * @param
     */
    @POST("/mobi/cart/todayGoldPrice")
    Call<ResultDO> todayGoldPrice();
    /**
     *积分
     */
    @GET("/mobi/score/getScore")
    Call<ResultDO> getScore(@Query("memberId") long memberId);
    /**
     *积分记录
     */
    @GET("/mobi/score/getScoreRecord")
    Call<ResultDO> getScoreRecord(@Query("memberId") long memberId);

    /**
     * 申请提现
     *
     * @param
     */
    @FormUrlEncoded
    @POST("/mobi/shop/AtWithdraw")
    Call<ResultDO> withDraw(@Field("memberId") long memberId, @Field("amount") String amount, @Field("accId") int accId);

    /**
     * 我的提现记录
     *
     * @param
     */
    @FormUrlEncoded
    @POST("/mobi/shop/BaWithdrawList")
    Call<ResultDO<List<WithDrawRecord>>> getWithDrawList(@Field("memberId") long memberId);

    /**
     * 添加账户
     *
     * @param
     */
    @FormUrlEncoded
    @POST("/mobi/shop/AsAddAccount")
    Call<ResultDO<Account>> addAccount(@Field("memberId") long memberId, @Field("type") int type,
                                       @Field("accCode") String accCode, @Field("accName") String accName,
                                       @Field("bankName") String bankName, @Field("subBankName") String subBankName);
    /**
     * 账户列表
     *
     * @param
     */
    @FormUrlEncoded
    @POST("/mobi/shop/AsAccountList")
    Call<ResultDO<List<Account>>> getAccountList(@Field("memberId") long memberId);


    /**
     * 删除账户
     *
     * @param
     */
    @FormUrlEncoded
    @POST("/mobi/shop/AsDeleteAccount")
    Call<ResultDO> delAccount(@Field("id") int id);
    /**
     * 余额明细
     */
    @FormUrlEncoded
    @POST("/mobi/shop/BhMoneyDetail")
    Call<ResultDO<List<BalanceDetail>>> getBalanceDetail(@Field("memberId") long memberId, @Field("pageNo") int pageNo,
                                                         @Field("pageSize") int pageSize);
    /**
     * 余额明细(店主)
     */
    @FormUrlEncoded
    @POST("/mobi/shop/BhMoneyDetail2")
    Call<ResultDO<List<BalanceDetail>>> getBalanceExtraDetail(@Field("memberId") long memberId, @Field("type") int type, @Field("pageNo") int pageNo,
                                                              @Field("pageSize") int pageSize);
    /**
     * 设置修改支付密码
     *
     * @param
     */
    @FormUrlEncoded
    @POST("/mobi/shop/AcSetPayPassword")
    Call<ResultDO<com.bby.yishijie.shop.entity.Member>> updatePayPwd(@Field("memberId") long memberId, @Field("code") String code, @Field("payPassword") String payPassword);

    /**
     * 设置修改支付密码
     *
     * @param
     */
    @FormUrlEncoded
    @POST("/mobi/shop/AdCheckPayPassword")
    Call<ResultDO<Integer>> checkPayPwd(@Field("memberId") long memberId, @Field("payPassword") String payPassword);

    /**
     * 微信支付(店主版-普通订单)
     *
     * @param
     */
    @FormUrlEncoded
    @POST("/mobi/wxPay/BaPay")
    Call<ResultDO<com.bby.yishijie.shop.entity.PayInfo>> getNormalPayInfo2(@Field("orderId") long orderId);

}
