package com.rtcomps.data;

import com.rtcomps.core.def.Address;
import com.rtcomps.core.def.HomePrice;



public interface PriceDao {

	HomePrice getPrice(String url, Address addr);

}
