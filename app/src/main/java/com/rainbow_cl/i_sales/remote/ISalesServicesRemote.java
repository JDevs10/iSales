package com.rainbow_cl.i_sales.remote;

import com.rainbow_cl.i_sales.remote.model.Categorie;
import com.rainbow_cl.i_sales.remote.model.Document;
import com.rainbow_cl.i_sales.remote.model.DolPhoto;
import com.rainbow_cl.i_sales.remote.model.Internaute;
import com.rainbow_cl.i_sales.remote.model.InternauteSuccess;
import com.rainbow_cl.i_sales.remote.model.Order;
import com.rainbow_cl.i_sales.remote.model.OrderLine;
import com.rainbow_cl.i_sales.remote.model.Product;
import com.rainbow_cl.i_sales.remote.model.Thirdpartie;
import com.rainbow_cl.i_sales.remote.model.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by netserve on 03/08/2018.
 */

public interface ISalesServicesRemote {

    //    COnnexion d'un internaute
    @POST("login")
    Call<InternauteSuccess> login(@Body Internaute internaute);

    //  Recupération de la liste des produits
    @GET("products")
    Call<ArrayList<Product>> findProductsByCategorie(@Query(ApiUtils.sortfield) String sortfield,
                                                     @Query(ApiUtils.sortorder) String sortorder,
                                                     @Query(ApiUtils.limit) long limit,
                                                     @Query(ApiUtils.category) long category,
                                                     @Query(ApiUtils.mode) int mode);

    //  Recupération de la liste des produits
    @GET("products")
    Call<ArrayList<Product>> findProducts(@Query(ApiUtils.sortfield) String sortfield,
                                                     @Query(ApiUtils.sortorder) String sortorder,
                                                     @Query(ApiUtils.limit) long limit,
                                                     @Query(ApiUtils.page) long page,
                                                     @Query(ApiUtils.mode) int mode);

    //  Recupération de la liste des thirdpartie(client, prospect, autre)
    @GET("thirdparties")
    Call<ArrayList<Thirdpartie>> findThirdpartie(@Query(ApiUtils.limit) long limit,
                                                 @Query(ApiUtils.page) long page,
                                                 @Query(ApiUtils.mode) int mode);

    //  Recupération d'un thirdpartie a partir de son id
    @GET("thirdparties/{thirdpartieId}")
    Call<Thirdpartie> findThirdpartieById(@Path("thirdpartieId") long thirdpartieId);

    //  Recupération de la liste des categories
    @GET("categories")
    Call<ArrayList<Categorie>> findCategories(@Query(ApiUtils.sortfield) String sortfield,
                                              @Query(ApiUtils.sortorder) String sortorder,
                                              @Query(ApiUtils.limit) long limit,
                                              @Query(ApiUtils.page) long page,
                                              @Query(ApiUtils.type) String type);

    //  Recupération du poster d'un produit
    @GET("documents/download")
    Call<DolPhoto> findProductsPoster(@Query(ApiUtils.module_part) String module_part,
                                      @Query(ApiUtils.original_file) String original_file);

    //  Recupération d'un document
    @GET("documents/download")
    Call<DolPhoto> findDocument(@Query(ApiUtils.module_part) String module_part,
                                @Query(ApiUtils.original_file) String original_file);

    //  Enregistrement d'une categorie
    @POST("categories")
    Call<Long> saveCategorie(@Body Categorie categorie);

    //  Enregistrement d'un thirdparty
    @POST("thirdparties")
    Call<Long> saveThirdpartie(@Body Thirdpartie thirdpartie);

    //  Modification d'un thirdparty
    @PUT("thirdparties/{thirdpartiesId}")
    Call<Thirdpartie> updateThirdpartie(@Path("thirdpartiesId") Long thirdpartiesId,
                                        @Body Thirdpartie thirdpartie);

    //  suppression d'un thirdparty
    @DELETE("thirdparties/{thirdpartieId}")
    Call<Long> deleteThirdpartie(@Path("thirdpartieId") Long thirdpartieId);

    //  Enregistrement d'une commande client
    @POST("orders")
    Call<Long> saveCustomerOrder(@Body Order order);

    //  Recupération de la liste des commandes
    @GET("orders")
    Call<ArrayList<Order>> findOrders(@Query(ApiUtils.sqlfilters) String sqlfilters,
                                      @Query(ApiUtils.sortfield) String sortfield,
                                      @Query(ApiUtils.sortorder) String sortorder,
                                      @Query(ApiUtils.limit) long limit,
                                      @Query(ApiUtils.page) long page);

    //  Recupération de la liste des ligne d'une commandes
    @GET("orders/{orderId}/lines")
    Call<ArrayList<OrderLine>> findOrderLines(@Path("orderId") Long orderId);

    //  Valide un bon de commande
    @POST("orders/{orderId}/validate")
    Call<Order> validateCustomerOrder(@Path("orderId") Long orderId);

    //  Valide un bon de commande
    @POST("documents/upload")
    Call<String> uploadDocument(@Body Document document);

    //  Recupération d'un user a partir de son login
    @GET("users")
    Call<ArrayList<User>> findUserByLogin(@Query(ApiUtils.sqlfilters) String sqlfilters);

}
