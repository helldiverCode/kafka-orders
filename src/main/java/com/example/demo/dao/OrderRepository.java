package com.example.demo.dao;

import com.example.demo.entity.OrderEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEvent, Long> {}

