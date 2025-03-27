-- 清理重复数据
DELETE FROM source_data;
DELETE FROM calculated_data;

-- 重置自增ID
ALTER TABLE source_data AUTO_INCREMENT = 1;
ALTER TABLE calculated_data AUTO_INCREMENT = 1;