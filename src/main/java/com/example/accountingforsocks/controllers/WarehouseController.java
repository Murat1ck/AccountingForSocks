package com.example.accountingforsocks.controllers;

import com.example.accountingforsocks.model.Color;
import com.example.accountingforsocks.model.Size;
import com.example.accountingforsocks.model.Socks;
import com.example.accountingforsocks.services.WarehouseServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/warehouse")
@Tag(name = "Склад", description = "CRUD-операции и другие эндпоинты для работы")
public class WarehouseController {

    private final WarehouseServices warehouseServices;

    public WarehouseController(WarehouseServices warehouseServices) {
        this.warehouseServices = warehouseServices;
    }

    @PostMapping
    @Operation(summary = "Добавление носков на склад", description = "Можно добавить носки на склад по параметрам")
    public ResponseEntity<Long> addSocks(@RequestParam Color color, @RequestParam Size size,
                                         @RequestParam int cottonPart, @RequestParam Long count) {
        if (cottonPart < 0 || cottonPart > 100 || count < 0) {
            return ResponseEntity.notFound().build();
        } else {
            warehouseServices.addSocks(new Socks(color, size, cottonPart), count);
            return ResponseEntity.ok(count);
        }
    }
    @PutMapping
    @Operation(summary = "Отправление носков со склада",
            description = "Можно отправить носки со склада")
    public ResponseEntity<Long> sendSocks(@RequestParam Color color, @RequestParam Size size,
                                          @RequestParam int cottonPart, @RequestParam Long count) {
        if (count < 0) {
            return ResponseEntity.notFound().build();
        } else {
            Long a = warehouseServices.sendSocks(new Socks(color, size, cottonPart), count);
            if (a == 0L) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(count);
        }
    }

    @DeleteMapping
    @Operation(summary = "Удаление носков со склада без остатка",
            description = "Можно удалить носки со склада полностью")
    public ResponseEntity<Void> deleteSocks(@RequestParam Color color, @RequestParam Size size,
                                            @RequestParam int cottonPart) {
        if (warehouseServices.deleteSocks(new Socks(color, size, cottonPart))) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    @Operation(summary = "Получение количества носков на складе",
            description = "Можно узнать количество носков с определенными параметрами на складе")
    public ResponseEntity<Long> SocksCount(@RequestParam Color color, @RequestParam Size size,
                                           @RequestParam int cottonPart) {
        Long n = warehouseServices.getSocksCount(new Socks(color, size, cottonPart));
        if (n == 0) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(n);

    }

}