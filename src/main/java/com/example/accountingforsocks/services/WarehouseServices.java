package com.example.accountingforsocks.services;

import com.example.accountingforsocks.model.Socks;

import java.io.IOException;
import java.nio.file.Path;


public interface WarehouseServices {



    Socks addSocks(Socks socks1, Long count);



    Long sendSocks(Socks socks1, Long count);

    Long getSocksCount(Socks socks1);


    boolean deleteSocks(Socks socks);


    Path getSocksMap() throws IOException;
}
