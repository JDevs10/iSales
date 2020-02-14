package com.iSales.interfaces;

import com.iSales.remote.rest.FindProductVirtualREST;

public interface FindProductVirtualListener {
    void onFindProductVirtualCompleted(Boolean downloadAll, FindProductVirtualREST findProductVirtualREST);
}
