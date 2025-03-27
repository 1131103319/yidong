-- 创建源数据表
CREATE TABLE IF NOT EXISTS source_data (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    business_type VARCHAR(50) NOT NULL,
    network_type VARCHAR(10) CHECK (network_type IN ('4G', '5G')),
    time DATE NOT NULL,
    received_data_count BIGINT NOT NULL,
    received_file_count BIGINT NOT NULL,
    phone_null_count BIGINT NOT NULL,
    domain_null_count BIGINT NOT NULL,
    dest_ip_null_count BIGINT NOT NULL,
    dest_port_null_count BIGINT NOT NULL,
    source_ip_null_count BIGINT NOT NULL,
    source_port_null_count BIGINT NOT NULL
);

-- 创建计算结果表
CREATE TABLE IF NOT EXISTS calculated_data (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    business_type VARCHAR(50) NOT NULL,
    network_type VARCHAR(10) CHECK (network_type IN ('4G', '5G')),
    time DATE NOT NULL,
    total_data_count BIGINT,
    received_count BIGINT,
    total_file_count BIGINT,
    received_file_count BIGINT,
    phone_null_rate DOUBLE,
    domain_null_rate DOUBLE,
    dest_ip_null_rate DOUBLE,
    dest_port_null_rate DOUBLE,
    source_ip_null_rate DOUBLE,
    source_port_null_rate DOUBLE
);
