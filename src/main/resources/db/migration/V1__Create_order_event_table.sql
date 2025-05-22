-- V1__Create_order_event_table.sql
CREATE TABLE order_event (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- 'id' will be automatically generated
    shipment_number VARCHAR(255) NOT NULL,  -- 'shipmentNumber' as a non-nullable column
    receiver_email VARCHAR(255) NOT NULL,   -- 'receiverEmail' as a non-nullable column
    receiver_country_code VARCHAR(10) NOT NULL,  -- 'receiverCountryCode' as a non-nullable column
    sender_country_code VARCHAR(10) NOT NULL,   -- 'senderCountryCode' as a non-nullable column
    status_code INT NOT NULL,                -- 'statusCode' as a non-nullable column
    received_at TIMESTAMP NOT NULL           -- 'receivedAt' as a non-nullable column
);